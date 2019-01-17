package com.easygo.cashier.module.settlement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.Configs;
import com.easygo.cashier.Events;
import com.easygo.cashier.MemberUtils;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.Test;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.CreateOderResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.request.CreateOrderRequestBody;
import com.easygo.cashier.bean.request.PrintRequestBody;
import com.easygo.cashier.module.goods.GoodsFragment;
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.widget.ConfirmDialog;
import com.easygo.cashier.widget.Keyboard;
import com.easygo.cashier.widget.MyTitleBar;
import com.easygo.cashier.widget.PayWayView;
import com.easygo.cashier.widget.ScanCodeDialog;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.EventUtils;
import com.niubility.library.utils.GsonUtils;
import com.niubility.library.utils.ScreenUtils;
import com.niubility.library.utils.ToastUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 结算收银页面
 */
@Route(path = ModulePath.settlement)
public class CashierActivity extends BaseMvpActivity<SettlementContract.IView, SettlementPresenter> implements SettlementContract.IView {

    @BindView(R.id.cl_title)
    MyTitleBar clTitle;
    @BindView(R.id.pay_way_view)
    PayWayView payWayView;
    @BindView(R.id.kb)
    Keyboard kb;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.et_money)
    EditText etMoney;

    @Autowired(name = "admin_name")
    String admin_name;
    @Autowired(name = "goods_count")
    int mGoodsCount;
    @Autowired(name = "coupon")
    float mCoupon;
    @Autowired(name = "member_balance")
    String mBalance;
    /**
     * 订单金额， 应收
     */
    @Autowired(name = "total_money")
    float mTotalMoney;
    @Autowired(name = "goods_data")
    Serializable mGoodsDataSerializable;

    /**
     * 商品数据
     */
    List<GoodsEntity<GoodsResponse>> mGoodsData;

    /**
     * 实收 键盘输入的现金数
     */
    float mRealPay;
    /**
     * 找零
     */
    float mChange;

    private SettlementView settlementView;

    private int mPayWay = PayWayView.WAY_CASH;
    private ConfirmDialog confirmDialog;

    private Handler mHandler = new Handler();

    /**
     * 订单是否支付完成
     */
    private boolean mOrderFinished;


    @Override
    protected SettlementPresenter createPresenter() {
        return new SettlementPresenter();
    }

    @Override
    protected SettlementContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cashier;
    }

    @Override
    protected void init() {
        ARouter.getInstance().inject(this);

//        payWayView.setPayWayShow(MemberUtils.isMember ? new int[]{0, 1, 2, 3, 6} : new int[]{0, 1, 2, 6});
        payWayView.setPayWayShow(MemberUtils.isMember ? new int[]{0, 1, 2, 3} : new int[]{0, 1, 2});
        settlementView = SettlementView.create(this);
        ((FrameLayout) findViewById(R.id.framelayout)).addView(settlementView);

        clTitle.setCashierAccount(admin_name);

        mRealPay = mTotalMoney;
        settlementView.setData(mTotalMoney, mCoupon, mRealPay, 0, mBalance);

        mGoodsData = (List<GoodsEntity<GoodsResponse>>) this.mGoodsDataSerializable;

        //扫码、打印、提交 按钮监听
        settlementView.setOnSettlementClickListener(new SettlementView.OnClickListener() {

            @Override
            public void onScanClicked() {
                showScanCodeDialog();
            }

            @Override
            public void onPrintClicked(boolean isChecked) {
                if (!isChecked) {
                    showToast("交易完成后将不打印小票");
                }
//                print();
            }

            @Override
            public void onCommitOrderClicked() {
                if (mRealPay < mTotalMoney) {
                    showToast("实收金额小于应收金额， 请确认！");
                    return;
                }
                if (!TextUtils.isEmpty(Configs.order_no)) {
                    showScanCodeDialog();
                }
                if (MemberUtils.isMember) {
                    if (mTotalMoney > Float.parseFloat(mBalance.substring(1,mBalance.length()))) {
                        showToast("会员钱包余额不足，不能进行支付");
                        return;
                    }
                }
                //弹出确认弹窗
                Bundle bundle = ConfirmDialog.getDataBundle(mTotalMoney, mRealPay, mChange, mPayWay);
                confirmDialog = new ConfirmDialog();
                confirmDialog.setArguments(bundle);
                confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListenr() {
                    @Override
                    public void onConfirm() {
                        onCommitOrder();
                    }
                });
                confirmDialog.show(getSupportFragmentManager(), "tag_tip_dialog");
            }
        });

        //关联EditText
        kb.attachEditText(etMoney);
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();

                if (TextUtils.isEmpty(text)) {
                    mRealPay = mTotalMoney;
                    mChange = 0;
                    //刷新价格
                    if (settlementView != null)
                        settlementView.setData(mTotalMoney, mCoupon, mRealPay, mChange, mBalance);
                    return;
                }

                //以00 . 开头 直接返回
                if (text.startsWith(".") || text.startsWith("00")) {
                    s.delete(0, s.length());
                    return;
                }
                float data = Float.valueOf(text);

                if (data < 0.01f && s.length() > 4) {
                    showToast("找零不能小于0.01元");
                    s.delete(s.length() - 1, s.length());
                } else {
                    mRealPay = data;
                    mChange = mRealPay - mTotalMoney - mCoupon;

                    //刷新价格
                    if (settlementView != null)
                        settlementView.setData(mTotalMoney, mCoupon, mRealPay, mChange, mBalance);

                }

            }
        });

        payWayView.setOnPayWayListener(new PayWayView.OnPayWayListener() {
            @Override
            public void onPayWaySelected(boolean isCombinePay, int pay_way) {
                mPayWay = pay_way;

                //支付方式选择回调
                if (settlementView != null) {
                    settlementView.setPayType(isCombinePay, pay_way);
                    if (pay_way == PayWayView.WAY_COUPON) {
                        return;
                    }

                    boolean needShowKeyboard = isCombinePay || pay_way == PayWayView.WAY_CASH;
                    //需要输入现金时 显示键盘
                    etMoney.setVisibility(needShowKeyboard ? View.VISIBLE : View.GONE);
                    btnDelete.setVisibility(needShowKeyboard ? View.VISIBLE : View.GONE);
                    kb.setVisibility(needShowKeyboard ? View.VISIBLE : View.GONE);

                    //清空 刷新价格
                    etMoney.setText("");

                    //支付方式为有现金时 键盘才可用
                    etMoney.setEnabled(needShowKeyboard);
                }
            }
        });

    }

    public void print() {
        PrintRequestBody requestBody = new PrintRequestBody();
        //测试代码
//        requestBody.setGoods_count(2);
//        requestBody.setOrder_no("2018121417565998545099");
//        requestBody.setPrinter_sn(Configs.printer_sn);
//        requestBody.setTimes(2);
//        requestBody.setShop_sn(Configs.shop_sn);
        //测试代码

        requestBody.setGoods_count(mGoodsCount);
        requestBody.setOrder_no(Configs.order_no);
        requestBody.setPrinter_sn(Configs.printer_sn);
        requestBody.setTimes(2);
        requestBody.setShop_sn(Configs.shop_sn);

        ArrayList<PrintRequestBody.GoodsListBean> goodsListBeans = new ArrayList<>();

        PrintRequestBody.GoodsListBean goodsListBean;


        DecimalFormat df = new DecimalFormat("#");
        float price;
        int total_count = mGoodsData.size();
        for (int i = 0; i < total_count; i++) {
//        for (int i = 0; i < 2; i++) {
            //测试代码
//            goodsListBean.setDiscount(1);
//            goodsListBean.setGoods_name("a");
//            goodsListBean.setCount_price(100);
//            goodsListBean.setPrice(100);
//            goodsListBean.setCount(1);
            //测试代码

            goodsListBean = new PrintRequestBody.GoodsListBean();
            GoodsEntity<GoodsResponse> good = mGoodsData.get(i);
            int count = good.getCount();
            GoodsResponse data;
            Integer price_int;

            data = good.getData();
            price = Float.valueOf(data.getPrice());
            price_int = Integer.valueOf(df.format(price * 100));
            goodsListBean.setCount_price(count * price_int);

            goodsListBean.setDiscount(1);
            goodsListBean.setGoods_name(data.getG_sku_name());
            goodsListBean.setPrice(price_int);

            switch (good.getItemType()) {
                case GoodsEntity.TYPE_WEIGHT:
                case GoodsEntity.TYPE_ONLY_PROCESSING:
                case GoodsEntity.TYPE_PROCESSING:
                    goodsListBean.setCount(count + "g");
                    break;
                case GoodsEntity.TYPE_GOODS:
                default:
                    goodsListBean.setCount(String.valueOf(count));
                    break;
            }


            goodsListBeans.add(goodsListBean);

            if (good.getItemType() == GoodsEntity.TYPE_PROCESSING) {
                data = good.getProcessing();
                if (data != null) {
                    goodsListBean = new PrintRequestBody.GoodsListBean();
                    price = Float.valueOf(data.getProcess_price());
                    price_int = Integer.valueOf(df.format(price * 100));
                    goodsListBean.setCount_price(price_int);

                    goodsListBean.setDiscount(1);
                    goodsListBean.setGoods_name(data.getG_sku_name());
                    goodsListBean.setPrice(price_int);

                    goodsListBean.setCount(String.valueOf(data.getCount()));

                    goodsListBeans.add(goodsListBean);
                }
            }

        }

        requestBody.setGoods_list(goodsListBeans);


        mPresenter.print(GsonUtils.getInstance().getGson().toJson(requestBody));


    }


    /**
     * 提交交易
     */
    public void onCommitOrder() {

        if (TextUtils.isEmpty(Configs.order_no)) {
//            showToast("确认提交订单");
            createOrder();
        } else {
//            showToast("订单已经创建 --> " + Configs.order_no);
            onAfterCreateOrder();
        }

    }

    /**
     * 扫描到的付款码 回调
     */
    public void onScanAuthCode(String auth_code) {

        if (TextUtils.isEmpty(Configs.order_no)) {
            showToast("订单未创建， 请点击提交交易按钮");
        } else {
            if (mScanCodeDialog != null) {
                mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_SCANNING);
            }
//            showToast("付款码 --> " + auth_code);

            //请求接口
            DecimalFormat df = new DecimalFormat("#");
            switch (mPayWay) {
                case PayWayView.WAY_ALIPAY://支付宝
                    mPresenter.aliPay(Configs.shop_sn, Configs.order_no,
                            Integer.valueOf(df.format(mTotalMoney * 100)), auth_code);
                    break;
                case PayWayView.WAY_WECHAT://微信
                    mPresenter.wechatPay(Configs.shop_sn, Configs.order_no,
                            Integer.valueOf(df.format(mTotalMoney * 100)), auth_code);
                    break;
                case PayWayView.WAY_MEMBER://会员钱包
                    mPresenter.memberWalletPay(Configs.order_no,auth_code);
                    break;
            }

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ScreenUtils.hideNavigationBar(this);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOrderFinished) {
            showToast("支付成功！！！");
            return true;
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @OnClick({R.id.cl_back, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_back://返回
                finish();
            case R.id.btn_delete://键盘删除按钮
                int selectionStart = etMoney.getSelectionStart();
                Editable editable = etMoney.getText();

                if (editable != null && editable.length() > 0) {
                    if (selectionStart > 0) {
                        editable.delete(selectionStart - 1, selectionStart);
                    }
                }
                break;
        }
    }


    private ScanCodeDialog mScanCodeDialog;

    /**
     * 显示扫码弹窗
     */
    public void showScanCodeDialog() {
        mScanCodeDialog = new ScanCodeDialog(this, R.style.DialogStyle);
        WindowManager.LayoutParams lp = mScanCodeDialog.getWindow().getAttributes();
        lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        lp.y = getResources().getDimensionPixelSize(R.dimen.y411);
        mScanCodeDialog.getWindow().setAttributes(lp);
        mScanCodeDialog.setCanceledOnTouchOutside(false);
        mScanCodeDialog.setCancelable(false);
        mScanCodeDialog.setOnScanCodeListener(new ScanCodeDialog.OnScanCodeListener() {
            @Override
            public void onScanCode(String barcode) {
                onScanAuthCode(barcode);
            }
        });
        mScanCodeDialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settlementView != null) {
            settlementView.release();
        }
        if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
            mScanCodeDialog.dismiss();
        }
        //离开页面 清除订单
        Configs.order_no = null;
    }

    public void createOrder() {
        DecimalFormat df = new DecimalFormat("#");
        String total_money = df.format(mTotalMoney * 100);
        String real_pay = df.format(mTotalMoney * 100 - mCoupon * 100);
//        String real_pay = df.format(mRealPay*100);

        CreateOrderRequestBody requestBody1 = new CreateOrderRequestBody();
        requestBody1.setCashier_id(Configs.cashier_id);
        requestBody1.setShop_sn(Configs.shop_sn);
        requestBody1.setTotal_money(Integer.valueOf(total_money));
        requestBody1.setReal_pay(Integer.valueOf(real_pay));
        requestBody1.setGoods_count(mGoodsCount);

        int size = mGoodsData.size();

        List<CreateOrderRequestBody.GoodsListBean> list = new ArrayList<>();
        GoodsEntity<GoodsResponse> good;
        GoodsResponse data;
        CreateOrderRequestBody.GoodsListBean goodsBean; 
        float price;
        for (int i = 0; i < size; i++) {
            good = mGoodsData.get(i);

            //商品信息
            goodsBean = new CreateOrderRequestBody.GoodsListBean();
            data = good.getData();
            goodsBean.setG_sku_id(data.getG_sku_id());
            goodsBean.setIdentity(data.getIdentity());
            //设置类型
            goodsBean.setType(data.getType());

            goodsBean.setCount(good.getCount());
            if (good.getItemType() == GoodsEntity.TYPE_ONLY_PROCESSING) {
                price = Float.valueOf(data.getProcess_price());
            } else {
                price = Float.valueOf(data.getPrice());
            }
            goodsBean.setPrice(Integer.valueOf(df.format(price * 100)));
            goodsBean.setBarcode(data.getBarcode());
            list.add(goodsBean);

            //加工方式
            switch (good.getItemType()) {
                case GoodsEntity.TYPE_PROCESSING:
                    data = good.getProcessing();
                    if (data != null) {//选择加工
                        goodsBean = new CreateOrderRequestBody.GoodsListBean();
                        goodsBean.setG_sku_id(data.getG_sku_id());
                        goodsBean.setIdentity(data.getIdentity());
                        //设置类型
                        goodsBean.setType(data.getType());
                        goodsBean.setCount(data.getCount());
                        price = Float.valueOf(data.getProcess_price());
                        goodsBean.setPrice(Integer.valueOf(df.format(price * 100)));
                        goodsBean.setBarcode(data.getBarcode());
                        list.add(goodsBean);
                    }
                    break;
            }

        }

        requestBody1.setGoods_list(list);

        mPresenter.createOrder(GsonUtils.getInstance().getGson().toJson(requestBody1));
    }


    @Override
    public void createOrderSuccess(CreateOderResponse result) {
//        Configs.order_no = result.getTrade_no();
        Configs.order_no = result.getTrade_num();

        onAfterCreateOrder();

    }

    /**
     * 创建订单后，调起支付方法
     */
    private void onAfterCreateOrder() {
        switch (mPayWay) {
            case PayWayView.WAY_CASH:

                DecimalFormat df = new DecimalFormat("#");
                String real_pay = df.format(mTotalMoney * 100 - mCoupon * 100);
                String change_money = df.format(mChange * 100);

                mPresenter.cash(Configs.shop_sn, Configs.order_no,
                        Integer.valueOf(real_pay), Integer.valueOf(change_money));
                break;
            case PayWayView.WAY_ALIPAY:
            case PayWayView.WAY_WECHAT:
                showScanCodeDialog();
                break;
            case PayWayView.WAY_MEMBER:
                showScanCodeDialog();
                break;
        }
    }

    @Override
    public void createOrderFailed(Map<String, Object> map) {
        if (HttpExceptionEngine.isBussinessError(map)) {
            showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
        }
    }

    @Override
    public void aliPaySuccess(String result) {
        showToast("支付宝支付成功");

        onPaySuccessAfter();
    }

    private void onPaySuccessAfter() {
        mOrderFinished = true;
        EventUtils.post(Events.CLEAR_GOODS_INFO);

        if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
            mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_SUCCESSFUL_RECEIPT);
        }

        //设置为已收款
        settlementView.setAlreadySettlement();

        if (settlementView.needPrint()) {
            print();
        }
        if (mPayWay == PayWayView.WAY_CASH) {//现金支付时
            //弹出钱箱
            mPresenter.print_info(Configs.shop_sn, Configs.printer_sn, PrintHelper.pop_till);
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    @Override
    public void aliPayFailed(Map<String, Object> map) {

        if (HttpExceptionEngine.isBussinessError(map)) {
            int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);

            if (20000 == err_code) {
                showToast("支付宝： 开始查询订单支付状态");
                mPresenter.checkAliPayStatus(Test.shop_sn, Configs.order_no);
            } else {
                showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
                if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
                    mScanCodeDialog.dismiss();
                }
            }
        } else {
            showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
        }

    }

    @Override
    public void checkAlipayStatusSuccess(String result) {
        showToast("支付宝： 查询订单 - 支付成功");

        onPaySuccessAfter();
    }

    @Override
    public void checkAlipayStatusFailed(Map<String, Object> map) {
        if (HttpExceptionEngine.isBussinessError(map)) {
            int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);

            if (20000 == err_code) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("支付宝： 继续查询订单支付状态");
                        mPresenter.checkAliPayStatus(Test.shop_sn, Configs.order_no);

                    }
                }, 1000);
            } else {
                showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
                if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
                    mScanCodeDialog.dismiss();
                }
            }
        } else {
            showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
        }
    }

    @Override
    public void wechatPaySuccess(String result) {
        showToast("微信支付成功");

        onPaySuccessAfter();
    }


    @Override
    public void wechatPayFailed(Map<String, Object> map) {
        if (HttpExceptionEngine.isBussinessError(map)) {
            int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);

            if (20000 == err_code) {
                showToast("微信： 开始查询订单支付状态");
                mPresenter.checkWechatPayStatus(Test.shop_sn, Configs.order_no);
            } else {
                showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
                if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
                    mScanCodeDialog.dismiss();
                }
            }
        } else {
            showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
        }
    }

    @Override
    public void checkWechatStatusSuccess(String result) {
        showToast("微信： 查询订单 - 支付成功");

        onPaySuccessAfter();
    }

    @Override
    public void checkWechatStatusFailed(Map<String, Object> map) {
        if (HttpExceptionEngine.isBussinessError(map)) {
            int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);

            if (20000 == err_code) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("微信： 继续查询订单支付状态");
                        mPresenter.checkWechatPayStatus(Test.shop_sn, Configs.order_no);

                    }
                }, 1000);
            } else {
                showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
                if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
                    mScanCodeDialog.dismiss();
                }
            }
        } else {
            showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
        }
    }

    @Override
    public void cashSuccess(String result) {
        showToast("现金支付成功");

        onPaySuccessAfter();
    }


    @Override
    public void cashFailed(Map<String, Object> map) {
        showToast("现金支付失败 - " + ((String) map.get(HttpExceptionEngine.ErrorMsg)));
    }

    @Override
    public void printSuccess(String result) {
//        showToast("打印成功");
    }

    @Override
    public void printFailed(Map<String, Object> map) {
        showToast("打印失败 - " + ((String) map.get(HttpExceptionEngine.ErrorMsg)));

    }

    @Override
    public void memberWalletSuccess(String result) {
        showToast("支付成功");

        onPaySuccessAfter();
    }

    @Override
    public void memberWalletFailed(Map<String, Object> map) {
        showToast("会员钱包支付失败 - " + ((String) map.get(HttpExceptionEngine.ErrorMsg)));
    }
}

package com.easygo.cashier.module.settlement;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.Test;
import com.easygo.cashier.bean.AlipayResponse;
import com.easygo.cashier.bean.CheckAlipayStatus;
import com.easygo.cashier.bean.CreateOderResponse;
import com.easygo.cashier.bean.GoodsNum;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.request.CreateOrderRequestBody;
import com.easygo.cashier.bean.request.PrintRequestBody;
import com.easygo.cashier.widget.ConfirmDialog;
import com.easygo.cashier.widget.Keyboard;
import com.easygo.cashier.widget.MyTitleBar;
import com.easygo.cashier.widget.PayWayView;
import com.easygo.cashier.widget.ScanCodeDialog;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.GsonUtils;

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
    /**应收*/
    @Autowired(name = "total_money")
    float mTotalMoney;
    @Autowired(name = "goods_data")
    Serializable mGoodsDataSerializable;

    /**商品数据*/
    List<GoodsNum<GoodsResponse>> mGoodsData;

    /**实收*/
    float mRealPay;
    /**找零*/
    float mChange;

    private SettlementView settlementView;

    private int mPayWay = PayWayView.WAY_CASH;
    private ConfirmDialog confirmDialog;

    private Handler mHandler = new Handler();


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

        settlementView = SettlementView.create(this);
        ((FrameLayout) findViewById(R.id.framelayout)).addView(settlementView);

        clTitle.setCashierAccount(admin_name);

        mRealPay = mTotalMoney;
        settlementView.setData(mTotalMoney, mCoupon, mRealPay, 0);

        mGoodsData = (List<GoodsNum<GoodsResponse>>) this.mGoodsDataSerializable;

        //扫码、打印、提交 按钮监听
        settlementView.setOnSettlementClickListener(new SettlementView.OnClickListener() {

            @Override
            public void onScanClicked() {
                showScanCodeDialog();
            }

            @Override
            public void onPrintClicked(boolean isChecked) {
                if(!isChecked) {
                    showToast("交易完成后将不打印小票");
                }
//                print();
            }

            @Override
            public void onCommitOrderClicked() {
                if(mRealPay < mTotalMoney) {
                    showToast("实收金额小于应收金额， 请确认！");
                }
                //弹出确认弹窗
                Bundle bundle = ConfirmDialog.getDataBundle(mTotalMoney, mRealPay, mChange);
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

                if(TextUtils.isEmpty(text)) {
                    mRealPay = mTotalMoney;
                    mChange = 0;
                    //刷新价格
                    if(settlementView != null)
                        settlementView.setData(mTotalMoney, mCoupon, mRealPay, mChange);
                    return;
                }
                float data = Float.valueOf(text);

                if(data >= mTotalMoney) {
                    showToast("找零不能大于等于应收");
                    s.delete(s.length()-1, s.length());
                } else if(data < 0.01f && s.length() > 4) {
                    showToast("找零不能小于0.01元");
                    s.delete(s.length()-1, s.length());
                } else {
                    mRealPay = data;
                    mChange = mRealPay - mTotalMoney;

                    //刷新价格
                    if(settlementView != null)
                        settlementView.setData(mTotalMoney, mCoupon, mRealPay, mChange);

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

                    //清空 刷新价格
                    etMoney.setText("");

                    //支付方式为有现金时 键盘才可用
                    etMoney.setEnabled(isCombinePay || pay_way == PayWayView.WAY_CASH);
                }
            }
        });

    }

    public void print() {
        PrintRequestBody requestBody = new PrintRequestBody();
//        requestBody.setGoods_count(2);
//        requestBody.setOrder_no("2018121417565998545099");
//        requestBody.setPrinter_sn(Configs.printer_sn);
//        requestBody.setTimes(2);
//        requestBody.setShop_sn(Configs.shop_sn);

        int total_count = mGoodsData.size();
        requestBody.setGoods_count(total_count);
        requestBody.setOrder_no(Configs.order_no);
        requestBody.setPrinter_sn(Configs.printer_sn);
        requestBody.setTimes(2);
        requestBody.setShop_sn(Configs.shop_sn);

        ArrayList<PrintRequestBody.GoodsListBean> goodsListBeans = new ArrayList<>();

        PrintRequestBody.GoodsListBean goodsListBean;
        goodsListBean = new PrintRequestBody.GoodsListBean();

        DecimalFormat df = new DecimalFormat("#");
        float price;
        for (int i = 0; i < total_count; i++) {
//            goodsListBean.setDiscount(1);
//            goodsListBean.setGoods_name("a");
//            goodsListBean.setCount_price(100);
//            goodsListBean.setPrice(100);
//            goodsListBean.setCount(1);

            GoodsNum<GoodsResponse> goodsNum = mGoodsData.get(i);
            GoodsResponse data = goodsNum.getData();
            int count = goodsNum.getCount();
            goodsListBean.setDiscount(1);
            goodsListBean.setGoods_name(data.getG_sku_name());
            price = Float.valueOf(data.getPrice());
            Integer price_int = Integer.valueOf(df.format(price * 100));
            goodsListBean.setPrice(price_int);
            goodsListBean.setCount_price(count*price_int);
            goodsListBean.setCount(count);

            goodsListBeans.add(goodsListBean);
        }

        requestBody.setGoods_list(goodsListBeans);


        mPresenter.print(GsonUtils.getInstance().getGson().toJson(requestBody));



    }


    public void onCommitOrder() {
        if(mPayWay == PayWayView.WAY_ALIPAY) {
            if(TextUtils.isEmpty(Configs.order_no)) {
                showToast("确认提交订单");
                createOrder();
            } else {
                showToast("订单已经创建 --> " + Configs.order_no);
            }
        } else {
            showToast("开发中");
        }
    }
    /**
     * 扫描到的付款码 回调
     */
    public void onScanAuthCode(String auth_code) {

        if(TextUtils.isEmpty(Configs.order_no)) {
            showToast("订单未创建， 请点击提交交易按钮");
        } else {
            if (mScanCodeDialog != null) {
                mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_SCANNING);
            }
            showToast("付款码 --> " + auth_code);

            //请求接口
            if(mPayWay == PayWayView.WAY_ALIPAY) {//支付宝
                DecimalFormat df = new DecimalFormat("#");
                mPresenter.aliPay(Configs.shop_sn, Configs.order_no,
                        Integer.valueOf(df.format(mTotalMoney*100)), auth_code);
            }
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
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
    public  boolean isShouldHideInput(View v, MotionEvent event) {
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
        if(mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
            mScanCodeDialog.dismiss();
        }
        //离开页面 清除订单
        Configs.order_no = null;
    }

    public void createOrder() {
        DecimalFormat df = new DecimalFormat("#");
        String total_money = df.format(mTotalMoney*100);
        String real_pay = df.format(mRealPay*100);

        CreateOrderRequestBody requestBody1 = new CreateOrderRequestBody();
        requestBody1.setShop_sn(Configs.shop_sn);
        requestBody1.setTotal_money(Integer.valueOf(total_money));
        requestBody1.setReal_pay(Integer.valueOf(real_pay));
        requestBody1.setGoods_count(mGoodsCount);

        int size = mGoodsData.size();

        List<CreateOrderRequestBody.GoodsListBean> list = new ArrayList<>();
        GoodsNum<GoodsResponse> goodsNum;
        GoodsResponse data;
        CreateOrderRequestBody.GoodsListBean goodsBean;
        float price;
        for (int i = 0; i < size; i++) {
            goodsNum = mGoodsData.get(i);
            data = goodsNum.getData();
            goodsBean = new CreateOrderRequestBody.GoodsListBean();
            goodsBean.setG_sku_id(data.getG_sku_id());
            goodsBean.setCount(goodsNum.getCount());
            price = Float.valueOf(data.getPrice());
            goodsBean.setPrice(Integer.valueOf(df.format(price*100)));
            goodsBean.setBarcode(data.getBarcode());
            list.add(goodsBean);
        }

        requestBody1.setGoods_list(list);

        mPresenter.createOrder(GsonUtils.getInstance().getGson().toJson(requestBody1));
    }


    @Override
    public void createOrderSuccess(CreateOderResponse result) {
//        Configs.order_no = result.getTrade_no();
        Configs.order_no = result.getTrade_num();
        showToast("订单号 --> " + Configs.order_no);

        showScanCodeDialog();

    }

    @Override
    public void createOrderFailed(Map<String, Object> map) {
        if(HttpExceptionEngine.isBussinessError(map)) {
            showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
        }
    }

    @Override
    public void aliPaySuccess(String result) {
        showToast("alipaySuccess - ");
        if (mScanCodeDialog != null) {
            mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_SUCCESSFUL_RECEIPT);
        }

        if(settlementView.needPrint()) {
            print();
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
        if(HttpExceptionEngine.isBussinessError(map)) {
            showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
            int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);

            if(20000 == err_code) {
                showToast("支付宝： 开始查询订单支付状态");
                mPresenter.checkAliPayStatus(Test.shop_sn, Configs.order_no);
            }
        }
    }

    @Override
    public void checkAlipayStatusSuccess(CheckAlipayStatus result) {
        showToast("支付宝： 查询订单 - 支付成功");
        if (mScanCodeDialog != null) {
            mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_SUCCESSFUL_RECEIPT);

        }
    }

    @Override
    public void checkAlipayStatusFailed(Map<String, Object> map) {
        if(HttpExceptionEngine.isBussinessError(map)) {
            int err_code = (int) map.get(HttpExceptionEngine.ErrorCode);

            if(20000 == err_code) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("支付宝： 继续查询订单支付状态");
                        mPresenter.checkAliPayStatus(Test.shop_sn, Configs.order_no);

                    }
                }, 1000);
            }
        }
    }

    @Override
    public void wechatPaySuccess() {

    }

    @Override
    public void wechatPayFailed(Map<String, Object> map) {

    }

    @Override
    public void printSuccess() {
        showToast("打印成功");
    }

    @Override
    public void printFailed(Map<String, Object> map) {
        showToast("打印失败");

    }
}

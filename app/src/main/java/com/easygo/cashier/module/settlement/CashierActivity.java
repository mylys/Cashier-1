package com.easygo.cashier.module.settlement;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.ActivitiesUtils;
import com.easygo.cashier.Configs;
import com.easygo.cashier.Events;
import com.easygo.cashier.MemberUtils;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.Test;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.BankcardStatusResponse;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.CreateOderResponse;
import com.easygo.cashier.bean.GiftCardResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.request.CreateOrderRequestBody;
import com.easygo.cashier.bean.request.PrintRequestBody;
import com.easygo.cashier.module.CouponUtils;
import com.easygo.cashier.module.GiftCardUtils;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.goods.BaseGoodsPromotion;
import com.easygo.cashier.module.promotion.shop.BaseShopPromotion;
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.printer.local.PrinterHelpter;
import com.easygo.cashier.printer.local.PrinterUtils;
import com.easygo.cashier.printer.local.obj.CashierPrintObj;
import com.easygo.cashier.widget.BankcardDialog;
import com.easygo.cashier.widget.ChooseCouponsDialog;
import com.easygo.cashier.widget.Keyboard;
import com.easygo.cashier.widget.MyTitleBar;
import com.easygo.cashier.widget.PayWayView;
import com.easygo.cashier.widget.ScanCodeDialog;
import com.easygo.cashier.widget.TempPromotionDialog;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.EventUtils;
import com.niubility.library.utils.GsonUtils;
import com.niubility.library.utils.ScreenUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 结算收银页面
 */
@Route(path = ModulePath.settlement)
public class CashierActivity extends BaseMvpActivity<SettlementContract.IView, SettlementPresenter> implements SettlementContract.IView {

    public static final String TAG = CashierActivity.class.getSimpleName();

    @BindView(R.id.cl_title)
    MyTitleBar clTitle;
    @BindView(R.id.pay_way_view)
    PayWayView payWayView;
    @BindView(R.id.kb)
    Keyboard kb;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.et_money)
    EditText etMoney;

    @Autowired(name = "admin_name")
    String admin_name;
    @Autowired(name = "goods_count")
    int mGoodsCount;
    /**优惠金额（不包含优惠券优惠金额）*/
    @Autowired(name = "coupon")
    float mCoupon;
    /**优惠券优惠金额*/
    @Autowired(name = "coupon_money")
    float mCouponMoney;
    @Autowired(name = "member_balance")
    String mBalance;
    /**
     * 订单金额， 应收
     */
    @Autowired(name = "total_money")
    float mTotalMoney;
    @Autowired(name = "goods_data")
    Serializable mGoodsDataSerializable;

    float mTempOrderPromotionMoney;
    float mGiftCardMoney;
    /**是否纯礼品卡支付*/
    boolean mOnlyGiftCardPay;

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

    /**
     * 银联支付 单号
     */
    String transaction_no;
    /**
     * 银联支付 金额（以分为单位）
     */
    int pay_fee;
    /**
     * 银联支付 是否查询支付状态
     */
    boolean mCheckBankcardStatus;

    private SettlementView settlementView;

    private int mPayWay = PayWayView.WAY_CASH;

    private final int MSG_CHECK_BANK_CARD_PAY_STATUS = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            removeMessages(msg.what);
            switch (msg.what) {
                case MSG_CHECK_BANK_CARD_PAY_STATUS:
                    Log.i(TAG, "bankcardSuccess: 银联支付查询中");
                    mPresenter.checkBankcardStatus(Configs.order_no);
                    break;
            }

        }
    };

    private ChooseCouponsDialog couponsDialog;


    /**
     * 订单是否支付完成
     */
    private boolean mOrderFinished;

    /**
     * 创建订单中
     */
    private boolean mDuringCreateOrder = false;

    /**
     * 调用支付接口中
     */
    private boolean mDuringPay = false;

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
        float payMoney = getPayMoney();

        if(payMoney <= 0 || !Configs.isOnlineMode()) {//支付金额为 小于等于0时 只能现金支付
            payWayView.setPayWayShow(new int[]{PayWayView.WAY_CASH});
        } else {
            payWayView.setPayWayShow(MemberUtils.isMember ?
                    new int[] {
                            PayWayView.WAY_ALIPAY,
                            PayWayView.WAY_WECHAT,
                            PayWayView.WAY_MEMBER,
                            PayWayView.WAY_CASH,
                            PayWayView.WAY_BANK_CARD,
                    }
                    : new int[] {
                            PayWayView.WAY_ALIPAY,
                            PayWayView.WAY_WECHAT,
                            PayWayView.WAY_CASH,
                            PayWayView.WAY_BANK_CARD,
                    }
            );
        }
        settlementView = SettlementView.create(this);
        ((FrameLayout) findViewById(R.id.framelayout)).addView(settlementView);

        clTitle.setCashierAccount(admin_name);

        if(mCouponMoney != 0) {
            showCouponInfo(CouponUtils.getInstance().getCouponInfo().getName(), mCouponMoney);
        }

        refreshGiftCardMoney();
        refreshRealPay();
        refreshSettlementView();


        mGoodsData = (List<GoodsEntity<GoodsResponse>>) this.mGoodsDataSerializable;

        //扫码、打印、提交 按钮监听
        settlementView.setOnSettlementClickListener(new SettlementView.OnClickListener() {

            @Override
            public void onCommitOrderClicked() {
//                if (mRealPay < mTotalMoney - mCoupon - mCouponMoney) {
//                    showToast("实收金额小于应收金额， 请确认！");
//                    return;
//                }
                onConfirmCommit();
//                //弹出确认弹窗
//                Bundle bundle = ConfirmDialog.getDataBundle(mTotalMoney, mRealPay, mChange, mPayWay);
//                confirmDialog = new ConfirmDialog();
//                confirmDialog.setArguments(bundle);
//                confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListenr() {
//                    @Override
//                    public void onConfirm() {
//                        onCommitOrder();
//                    }
//                });
//                confirmDialog.show(getSupportFragmentManager(), "tag_tip_dialog");
            }

            @Override
            public void onCancelCoupon() {
               cancelCoupon();
            }

            @Override
            public void onCancelTempPromotion() {
                cancelTempPromotion();
            }

            @Override
            public void onCancelGiftCard() {
                cancelGiftCard();
            }

        });

        //关联EditText
        kb.attachEditText(etMoney);
        //保留小数点后一位
        kb.setRetain(1);
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
                    mChange = 0;
                    refreshGiftCardMoney();
                    refreshRealPay();
                    //刷新价格
                    refreshSettlementView();
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

                    if(mChange < 0) {
//                        showToast("找零金额小于0");
                    }

                    //刷新价格
                    refreshSettlementView();

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

                    boolean needShowKeyboard = isCombinePay || pay_way == PayWayView.WAY_CASH;
                    //需要输入现金时 显示键盘
                    etMoney.setVisibility(needShowKeyboard ? View.VISIBLE : View.GONE);
                    btnDelete.setVisibility(needShowKeyboard ? View.VISIBLE : View.GONE);
                    ivDelete.setVisibility(needShowKeyboard ? View.VISIBLE : View.GONE);
                    kb.setVisibility(needShowKeyboard ? View.VISIBLE : View.GONE);

                    settlementView.setBottomButtonVisibility(needShowKeyboard);


                    //清空 刷新价格
                    etMoney.setText("");

                    //支付方式为有现金时 键盘才可用
                    etMoney.setEnabled(needShowKeyboard);
                }
            }
        });

        //支付金额大于0时
        if(payMoney > 0 && Configs.isOnlineMode()) {
            //登录会员时 默认选择会员钱包支付
            if(MemberUtils.isMember) {
                payWayView.performSelect(PayWayView.WAY_MEMBER);
            }
            //纯礼品卡支付时
            else if(mOnlyGiftCardPay) {
                payWayView.performSelect(PayWayView.WAY_CASH);
            }
            //其他情况
            else {
                payWayView.performSelect(PayWayView.WAY_ALIPAY);
            }
        } else {
            //支付金额小于等于0时 默认现金支付
            payWayView.performSelect(PayWayView.WAY_CASH);
        }
    }

    /**
     * 点击了确定提交
     */
    private void onConfirmCommit() {
        if(mOrderFinished || mDuringCreateOrder || mDuringPay) {
            showToast("请稍候");
            return;
        }
        if(mChange < 0) {
            showToast("找零金额小于0");
            return;
        }

//        if (!TextUtils.isEmpty(Configs.order_no) && mPayWay != PayWayView.WAY_CASH) {
//            showScanCodeDialog();
//            return;
//        }
        //会员钱包支付时
        if (MemberUtils.isMember && mPayWay == PayWayView.WAY_MEMBER) {
            if (getReceivableMoney() - mGiftCardMoney > Float.parseFloat(mBalance.substring(1,mBalance.length()))) {
                showToast("会员钱包余额不足，不能进行支付");
                return;
            }
        }
        if(mPayWay == PayWayView.WAY_BANK_CARD) {
            if(!mOnlyGiftCardPay) {
                showToast("银联不能与礼品卡混合支付，请选择其他支付方式");
                return;
            }
        }
        onCommitOrder();
    }

    /**
     * 刷新金额显示
     */
    private void refreshSettlementView() {
        //刷新找零
        refreshChangeMoney();
        settlementView.setData(mTotalMoney, mCoupon,
                mCouponMoney, mGiftCardMoney, mTempOrderPromotionMoney,
                mRealPay, mChange, mBalance);
    }


    /**
     * 刷新实收       订单总额-优惠-优惠券优惠 -礼品卡- 临时整单折扣 = 实收 - 找零
     */
    private void refreshRealPay() {
        mRealPay = getPayMoney() - mGiftCardMoney - mTempOrderPromotionMoney + mChange;
        if(mRealPay < 0) {
            mRealPay = 0;
        }
    }

    /**
     * 刷新找零      找零 = 实收 - （订单总额-优惠-优惠券优惠 -礼品卡 - 临时整单折扣）
     */
    private void refreshChangeMoney() {
        float pay = getPayMoney() - mGiftCardMoney - mTempOrderPromotionMoney;
        if(pay < 0) {
            pay = 0;
        }
        mChange = mRealPay - pay;

        etMoney.setSelected(mChange < 0);
    }

    /**
     * 刷新礼品卡金额
     */
    private void refreshGiftCardMoney() {
        if(GiftCardUtils.getInstance().getGiftCardInfo() == null) {
            return;
        }
        float giftCardBalance = GiftCardUtils.getInstance().getGiftCardInfo().getBalance_amount();
        float payMoney = getPayMoney() - mTempOrderPromotionMoney;
        if(giftCardBalance > payMoney) {
            mGiftCardMoney = payMoney;
            mOnlyGiftCardPay = true;
        } else {
            mGiftCardMoney = giftCardBalance;
            mOnlyGiftCardPay = false;
        }

        settlementView.setGiftCardVisiable(true);
        settlementView.setGiftCardInfo(GiftCardUtils.getInstance().getGiftCardInfo().getSn(),
                mGiftCardMoney);
    }

    public void print() {

        for (int j = 0; j < PrintHelper.printers_count; j++) {
            InitResponse.PrintersBean printersBean = PrintHelper.printersBeans.get(j);
            String device_sn = printersBean.getDevice_sn();
            int print_times = printersBean.getPrint_times();

            if(!printersBean.canUse(InitResponse.PrintersBean.type_settlement)) {
                return;
            }

            PrintRequestBody requestBody = new PrintRequestBody();

            requestBody.setGoods_count(mGoodsCount);
            requestBody.setOrder_no(Configs.order_no);
            requestBody.setPrinter_sn(device_sn);
            requestBody.setTimes(print_times);
            requestBody.setShop_sn(Configs.shop_sn);

            ArrayList<PrintRequestBody.GoodsListBean> goodsListBeans = new ArrayList<>();

            PrintRequestBody.GoodsListBean goodsListBean;


            DecimalFormat df = new DecimalFormat("#");
            DecimalFormat df_weight = new DecimalFormat("#0.000");
            float price;
            float count;
            float discount;
            int total_count = mGoodsData.size();
            for (int i = 0; i < total_count; i++) {
                goodsListBean = new PrintRequestBody.GoodsListBean();
                GoodsEntity<GoodsResponse> good = mGoodsData.get(i);
                count = good.getCount();
                GoodsResponse data;
                Integer price_int;

                data = good.getData();
                price = Float.valueOf(data.getPrice());
                discount = Float.valueOf(data.getDiscount_price());
                price_int = Integer.valueOf(df.format(price * 100));
                goodsListBean.setCount_price(Integer.valueOf(df.format((count * price - discount) * 100)));

                goodsListBean.setDiscount(Integer.valueOf(df.format(discount)));
                goodsListBean.setGoods_name(data.getPrintName());
                goodsListBean.setBarcode(data.getBarcode());
                goodsListBean.setPrice(price_int);
                goodsListBean.setUnit_price(price_int);

                switch (good.getItemType()) {
                    case GoodsEntity.TYPE_WEIGHT:
                    case GoodsEntity.TYPE_PROCESSING:
                        goodsListBean.setCount(df_weight.format(count) + data.getG_u_symbol());
                        break;
                    case GoodsEntity.TYPE_GOODS:
                    case GoodsEntity.TYPE_ONLY_PROCESSING:
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
                        discount = Float.valueOf(data.getDiscount_price());
                        price_int = Integer.valueOf(df.format(price * 100));
                        goodsListBean.setCount_price(price_int);

                        goodsListBean.setDiscount(Integer.valueOf(df.format(discount)));
                        goodsListBean.setGoods_name(data.getG_sku_name());
                        goodsListBean.setBarcode(data.getBarcode());
                        goodsListBean.setPrice(price_int);
                        goodsListBean.setUnit_price(price_int);

                        goodsListBean.setCount(String.valueOf(data.getCount()));

                        goodsListBeans.add(goodsListBean);
                    }
                }

            }

            requestBody.setGoods_list(goodsListBeans);

            mPresenter.print(GsonUtils.getInstance().getGson().toJson(requestBody));
        }
    }

    public void printLocal() {
        CashierPrintObj obj = new CashierPrintObj();
        obj.shop_name = Configs.shop_name;
        obj.trade_no = Configs.trade_no;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        obj.time = sdf.format(new Date());
        obj.admin_name = String.valueOf(Configs.cashier_id);
        obj.data = mGoodsData;
        switch (mPayWay) {
            case PayWayView.WAY_CASH:
                obj.pay_type = "现金支付";
                obj.pop_till = true;
                break;
            case PayWayView.WAY_ALIPAY:
                obj.pay_type = "支付宝支付";
                break;
            case PayWayView.WAY_WECHAT:
                obj.pay_type = "微信支付";
                break;
            case PayWayView.WAY_MEMBER:
                obj.pay_type = "会员钱包支付";
                break;
        }
        obj.count = String.valueOf(mGoodsCount);
        obj.total_money = mTotalMoney;
        obj.discount = mCoupon + mCouponMoney + mTempOrderPromotionMoney;
        obj.real_pay = mTotalMoney - mCoupon - mCouponMoney - mTempOrderPromotionMoney;
        obj.gift_card_money = mGiftCardMoney;
        obj.change = mChange;

        PrinterUtils.getInstance().print(PrinterHelpter.cashier(obj));
    }


    /**
     * 提交交易
     */
    public void onCommitOrder() {

//        if (TextUtils.isEmpty(Configs.order_no)) {
//            showToast("确认提交订单");
            createOrder();
//        } else {
////            showToast("订单已经创建 --> " + Configs.order_no);
//            onAfterCreateOrder();
//        }

    }

    /**
     * 扫描到的付款码 回调
     */
    public void onScanAuthCode(String auth_code) {

        if(TextUtils.isEmpty(auth_code)) {
            return;
        }

        if (TextUtils.isEmpty(Configs.order_no)) {
            showToast("订单未创建， 请点击提交交易按钮");
        } else {
            if (mScanCodeDialog != null) {
                mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_SCANNING);
            }
//            showToast("付款码 --> " + auth_code);

            float real_pay = getReceivableMoney();
            if(real_pay <= 0) {
                real_pay = 0.01f;
            }

            //请求接口
            DecimalFormat df = new DecimalFormat("#");
            switch (mPayWay) {
                case PayWayView.WAY_ALIPAY://支付宝
                    mPresenter.aliPay(Configs.shop_sn, Configs.order_no,
                            Integer.valueOf(df.format(real_pay * 100)), auth_code);
                    break;
                case PayWayView.WAY_WECHAT://微信
                    mPresenter.wechatPay(Configs.shop_sn, Configs.order_no,
                            Integer.valueOf(df.format(real_pay * 100)), auth_code);
                    break;
                case PayWayView.WAY_MEMBER://会员钱包
                    mPresenter.memberWalletPay(Configs.order_no,auth_code);
                    break;
            }

        }
    }

    /**
     * 首页的收银金额，应用临时整单折扣之前的金额     （订单总额-优惠-优惠券优惠） =  应收 + 临时整单折扣
     */
    private float getPayMoney() {
        float money = mTotalMoney - mCoupon - mCouponMoney;
        if(money <= 0) {
            return 0;
        }
        return money;
    }

    /**
     * 应收金额     （订单总额-优惠-优惠券优惠-临时整单折扣） =  应收
     */
    private float getReceivableMoney() {
        float money = getPayMoney() - mTempOrderPromotionMoney;
        if(money <= 0) {
            return 0;
        }
        return money;
    }

    /**
     * 金额     （订单总额-优惠-优惠券优惠-临时整单折扣）+ 找零 =  实收
     */
    private float getReceipts() {
        float money = getPayMoney() - mTempOrderPromotionMoney + mChange;
        if(money <= 0) {
            return 0;
        }
        return money;
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

    @OnClick({R.id.cl_back, R.id.btn_delete,
            R.id.btn_coupon, R.id.btn_cancel_coupon,
            R.id.btn_temp_promotion, R.id.btn_cancel_temp_promotion,
            R.id.btn_search_gift_card, R.id.btn_cancel_gift_card,
            R.id.cl_confirm_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_back://返回
                finish();
                break;
            case R.id.btn_coupon://优惠券
                if(ActivitiesUtils.getInstance().hasShopPromotion() || ActivitiesUtils.getInstance().hasGoodsPromotion()) {
                    if(!ActivitiesUtils.getInstance().isWith_coupon()) {
                        showToast("促销不可与优惠券共用");
                        return;
                    }
                }

                showCoupon();
                break;
            case R.id.btn_delete://键盘删除按钮
                int selectionStart = etMoney.getSelectionStart();
                Editable editable = etMoney.getText();

                if (editable != null && editable.length() > 0) {
                    if (selectionStart > 0) {
                        editable.delete(selectionStart - 1, selectionStart);
                    }
                }
                break;
            case R.id.btn_cancel_coupon:
                cancelCoupon();
                break;
            case R.id.btn_temp_promotion:
                if(ActivitiesUtils.getInstance().getCurrentTempOrderPromotion() != null) {
                    showToast("已设置折扣");
                    return;
                }

                final TempPromotionDialog tempPromotionDialog = new TempPromotionDialog();
                tempPromotionDialog.setSelected(null);
                tempPromotionDialog.setOnDialogClickListener(new TempPromotionDialog.OnDialogClickListener() {
                    @Override
                    public void onClick(List<GoodsEntity<GoodsResponse>> selectGoods, int mode, boolean isFreeOrder, float value) {
                        tempPromotionDialog.dismiss();
                        ActivitiesUtils.getInstance().createTempOrderPromotion(mode, isFreeOrder, value);

                        mTempOrderPromotionMoney = ActivitiesUtils.getInstance().getTempOrderPromotionMoney(mGoodsData, getPayMoney());
//                        settlementView.setCancleTempPromotionVisibility(true);

                        //清空键盘输入  回调afterTextChanged()
                        etMoney.setText("");

                        //刷新首页和副屏
                        refreshGoodsData();
                    }
                });
                tempPromotionDialog.showCenter(this);
                break;
            case R.id.btn_cancel_temp_promotion://取消临时整单促销
                cancelTempPromotion();

                break;
            case R.id.btn_search_gift_card://礼品卡
                if(GiftCardUtils.getInstance().getGiftCardInfo() != null) {
                    showToast("已设置礼品卡");
                    return;
                }
                showGiftCardDialog();

                break;
            case R.id.btn_cancel_gift_card://取消礼品卡
                cancelGiftCard();

                break;
            case R.id.cl_confirm_commit:
                onConfirmCommit();

                break;
        }
    }


    /**
     * 取消优惠券
     */
    private void cancelCoupon() {
        if(CouponUtils.getInstance().getCouponInfo() == null) {
            return;
        }

        settlementView.setCouponVisiable(false);

        mCouponMoney = 0f;
        refreshRealPay();
        refreshSettlementView();

        //置空
        CouponUtils.getInstance().setCouponInfo(null);

        refreshGoodsData();

    }

    /**
     * 取消临时整单促销
     */
    private void cancelTempPromotion() {
        if(ActivitiesUtils.getInstance().getCurrentTempOrderPromotion() == null) {
            return;
        }

        ActivitiesUtils.getInstance().clearTempOrderPromotion();
        //置空临时整单促销分摊优惠金额
        int size = mGoodsData.size();
        for (int i = 0; i < size; i++) {
            mGoodsData.get(i).getData().setTemp_order_discount(0);
        }
        mTempOrderPromotionMoney = 0f;

        //清空键盘输入  回调afterTextChanged()
        etMoney.setText("");

        //刷新首页和副屏
        refreshGoodsData();
    }

    /**
     * 取消礼品卡
     */
    private void cancelGiftCard() {
        if(GiftCardUtils.getInstance().getGiftCardInfo() == null) {
            return;
        }
        GiftCardUtils.getInstance().setGiftCardInfo(null);
        settlementView.setGiftCardVisiable(false);

        mGiftCardMoney = 0f;
        mOnlyGiftCardPay = false;
        refreshRealPay();
        refreshSettlementView();
    }

    /**
     * 显示优惠券弹窗
     */
    private void showCoupon() {
        //判断是否有促销
        if(ActivitiesUtils.getInstance().hasGoodsPromotion() || ActivitiesUtils.getInstance().hasShopPromotion()) {

            if(!ActivitiesUtils.getInstance().isWith_coupon()) {
                showToast("参与的促销活动不可与优惠券公用");
            }
        }

        if (couponsDialog == null) {
            couponsDialog = new ChooseCouponsDialog();
        }
        couponsDialog.showCenter(this);
        couponsDialog.setTitle(getResources().getString(R.string.text_coupon_coupon));
        couponsDialog.setOnSearchListener(new ChooseCouponsDialog.OnSearchListener() {
            @Override
            public void onSearch(String content) {
                mPresenter.get_coupon(content);
            }

            @Override
            public void onItemClick(CouponResponse result) {

                //判断优惠券是否满足使用条件
                boolean canUseCoupon = CouponUtils.getInstance().canUse(result, mTotalMoney - mCoupon - mCouponMoney);
                if(!canUseCoupon) {
                    showToast("不满足优惠券使用条件");
                    return;
                }

                CouponUtils.getInstance().setCouponInfo(result);
                //优惠券优惠金额
                float couponMoney = CouponUtils.getInstance().getCouponMoney(mGoodsData, Configs.shop_sn, mTotalMoney - mCoupon - mCouponMoney);


                Log.i(TAG, " 优惠券 优惠价格 --> " + couponMoney);
                mCouponMoney = couponMoney;

                if(mCouponMoney > 0) {
                    //设置优惠券信息
                    showCouponInfo(result.getName(), couponMoney);
                    //重新设置临时整单促销
                    if(ActivitiesUtils.getInstance().hasTempOrderPromotion()) {
                        mTempOrderPromotionMoney = ActivitiesUtils.getInstance().getTempOrderPromotionMoney(mGoodsData, getPayMoney());

                        //清空键盘输入  回调afterTextChanged()
                        etMoney.setText("");
                    }

                    //刷新首页和副屏
                    refreshGoodsData();
                } else {
                    cancelCoupon();
                }
            }
        });
    }

    private void showCouponInfo(String name, float couponMoney) {
        if (settlementView != null) {
            settlementView.setCouponVisiable(true);
            settlementView.setCouponInfo(name, couponMoney);

            refreshGiftCardMoney();
            refreshRealPay();
            refreshSettlementView();

        }
    }


    private ScanCodeDialog mScanCodeDialog;
    private BankcardDialog mBankcardDialog;

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
                mScanCodeDialog.setStopScan(true);
            }
        });
        mScanCodeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                unlockCoupon();
                mDuringPay = false;
            }
        });
        mScanCodeDialog.show();

    }
    /**
     * 显示礼品卡弹窗
     */
    public void showGiftCardDialog() {
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

                mPresenter.giftCard(barcode);
                mScanCodeDialog.setStopScan(true);

                mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_SCAN_GIFT_CARD);
            }
        });
        mScanCodeDialog.show();

    }


    /**
     * 显示银联支付QRcode
     * <p>
     * 二维码字符串 格式： _201903201441469383566861039274_YHTEST8920932_100
     */
    public void showBankcardDialog() {
        String text = "_" + transaction_no + "_YHTEST8920932_" + pay_fee;
        Log.i(TAG, "showBankcardDialog: 银联QRcode -> " + text);
        DecimalFormat df = new DecimalFormat("0.00");
        mBankcardDialog = BankcardDialog.getInstance(df.format(getReceivableMoney()), text);
        mBankcardDialog.showCenter(this);
        mBankcardDialog.setOnDialogListener(new BankcardDialog.OnDialogClickListener() {
            @Override
            public void onClose() {
                mCheckBankcardStatus = false;
                mDuringPay = false;
            }
        });

        mCheckBankcardStatus = true;
        checkBankcardPayStatus();
    }

    public void checkBankcardPayStatus() {
        if(mCheckBankcardStatus) {
            mHandler.sendEmptyMessageDelayed(MSG_CHECK_BANK_CARD_PAY_STATUS, 1500);
        }
    }

    /**
     * 解绑优惠券
     */
    public void unlockCoupon() {
        if(CouponUtils.getInstance().getCouponInfo() != null
                && !TextUtils.isEmpty(Configs.order_no)) {
            //解除优惠券锁定
            mPresenter.closeOrder(Configs.order_no);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (settlementView != null) {
            settlementView.release();
        }
        dismissScanDialog();
        dismissBankcardDialog();
        //离开页面 清除订单
        Configs.order_no = null;
        Configs.trade_no = null;
        //清除临时整单促销
        ActivitiesUtils.getInstance().clearTempOrderPromotion();
        //置空临时整单促销分摊优惠金额
        int size = mGoodsData.size();
        for (int i = 0; i < size; i++) {
            mGoodsData.get(i).getData().setTemp_order_discount(0);
        }
        mTempOrderPromotionMoney = 0f;

        if(!mOrderFinished) {//刷新
            //刷新首页和副屏
            refreshGoodsData();
        }
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 刷新商品数据 （首页 、副屏）
     */
    private void refreshGoodsData() {
        EventUtils.post(Events.REFRESH_DATA, mGoodsData);
    }

    public void createOrder() {
        mDuringCreateOrder = true;
        DecimalFormat df = new DecimalFormat("#");
        String total_money = df.format(mTotalMoney * 100);

        float receivableMoney = getReceivableMoney();
        String real_pay = df.format(receivableMoney * 100);

        CreateOrderRequestBody requestBody1 = new CreateOrderRequestBody();
        requestBody1.setCashier_id(Configs.cashier_id);
        requestBody1.setShop_sn(Configs.shop_sn);
        requestBody1.setTotal_money(Integer.valueOf(total_money));
        requestBody1.setReal_pay(Integer.valueOf(real_pay));
        requestBody1.setGoods_count(mGoodsCount);
        switch (mPayWay) {
            case PayWayView.WAY_BANK_CARD:
                requestBody1.setOrder_type(2);
                break;
            default:
                requestBody1.setOrder_type(0);
                break;
        }

        //礼品卡
        GiftCardResponse giftCardInfo = GiftCardUtils.getInstance().getGiftCardInfo();
        if(giftCardInfo != null) {
            requestBody1.setGift_card_no(giftCardInfo.getSn());
            requestBody1.setGift_card_pay(Integer.valueOf(df.format(mGiftCardMoney * 100)));
        }
        //会员
        if(MemberUtils.isMember && MemberUtils.memberInfo != null) {
            requestBody1.setBuyer(MemberUtils.memberInfo.getNick_name());
            requestBody1.setMember_id(MemberUtils.memberInfo.getMember_id());
            requestBody1.setMember_card_no(MemberUtils.memberInfo.getCard_no());
        }

        float all_activity_discount = 0f;
        float all_member_discount = 0f;

        //店铺促销
        List<CreateOrderRequestBody.ActivitiesBean> activitiesBeans = new ArrayList<>();
        requestBody1.setActivities(activitiesBeans);
        if(ActivitiesUtils.getInstance().hasShopPromotion()) {
            BaseShopPromotion promotion = ActivitiesUtils.getInstance().getCurrentShopPromotion();
            CreateOrderRequestBody.ActivitiesBean activitiesBean = new CreateOrderRequestBody.ActivitiesBean();
            activitiesBean.setId(promotion.getId());
            activitiesBean.setType("shop");
            String shop_promotion_money = df.format(ActivitiesUtils.getInstance().getShopPromotionMoney() * 100);
            activitiesBean.setDiscount(Integer.valueOf(shop_promotion_money));
            activitiesBeans.add(activitiesBean);
            all_activity_discount += ActivitiesUtils.getInstance().getShopPromotionMoney();
        }
//        //会员
//        else if(MemberUtils.isMember) {
//            List<CreateOrderRequestBody.ActivitiesBean> activitiesBeans = new ArrayList<>();
//
//            //会员日
//            if(MemberUtils.isMemberDay) {
//                CreateOrderRequestBody.ActivitiesBean activitiesBean = new CreateOrderRequestBody.ActivitiesBean();
//                activitiesBean.setId(MemberUtils.day_rc_id);
//                activitiesBean.setType("member_day");
//                activitiesBean.setDiscount(Integer.valueOf(df.format((mCoupon*100))));
//                activitiesBeans.add(activitiesBean);
//            }
//            //会员固定折扣
//            else if(MemberUtils.isMemberDiscount){
//                CreateOrderRequestBody.ActivitiesBean activitiesBean = new CreateOrderRequestBody.ActivitiesBean();
//                activitiesBean.setId(MemberUtils.discount_rc_id);
//                activitiesBean.setType("member_discount");
//                activitiesBean.setDiscount(Integer.valueOf(df.format((mCoupon*100))));
//                activitiesBeans.add(activitiesBean);
//            }
//            if(activitiesBeans.size() != 0) {
//                requestBody1.setActivities(activitiesBeans);
//            }
//        }

        //优惠券
        CouponResponse couponInfo = CouponUtils.getInstance().getCouponInfo();
        if(couponInfo != null) {
            requestBody1.setCoupon_sn(couponInfo.getCoupon_sn());
            requestBody1.setCoupon_discount(Integer.valueOf(
                    df.format(CouponUtils.getInstance().getCoupon_discount() * 100)));
        }

        int size = mGoodsData.size();

        List<CreateOrderRequestBody.GoodsListBean> list = new ArrayList<>();
        GoodsEntity<GoodsResponse> good;
        GoodsResponse data;
        CreateOrderRequestBody.GoodsListBean goodsBean;
        float price;
        float unit_price;
        float count;

        float discount;//总优惠
        float shop_activity_discount;//店铺促销
        float good_activity_discount;//商品促销
        float member_discount;//会员促销
        float coupon_discount;//优惠券促销
        float cashier_discount;//收银员促销（= 临时商品促销 + 临时订单促销）
        float cashier_item_discount;//收银员单品促销（= 临时商品促销）
        float temp_good_discount = 0;// 总临时商品促销
        HashMap<Integer, Float> id2discount = new HashMap<>();//商品促销活动id 映射 促销优惠金额
        for (int i = 0; i < size; i++) {
            good = mGoodsData.get(i);

            //商品信息
            goodsBean = new CreateOrderRequestBody.GoodsListBean();
            data = good.getData();
            count = good.getCount();
            goodsBean.setG_sku_id(data.getG_sku_id());
            goodsBean.setIdentity(data.getIdentity());
            //设置类型
            goodsBean.setType(data.getType());
            discount = Float.valueOf(data.getDiscount_price());

            goodsBean.setDiscount(Integer.valueOf(df.format(discount * 100)));
            BaseGoodsPromotion promotion = good.getPromotion();
            if(promotion != null && promotion.getType() != IPromotion.TYPE_TEMP) {
                //商品促销
                int id = promotion.getId();
                Float goods_discount = id2discount.get(id);
                if(goods_discount == null) {
                    id2discount.put(id, data.getGoods_activity_discount());
                } else {
                    id2discount.put(id, goods_discount + data.getGoods_activity_discount());
                }
            }

            //商品促销
            good_activity_discount = data.getGoods_activity_discount();
            all_activity_discount += good_activity_discount;
            goodsBean.setGoods_activity_discount(Integer.valueOf(df.format(good_activity_discount * 100)));
            //会员促销
            member_discount = data.getMember_discount();
            all_member_discount += member_discount;
            goodsBean.setMember_discount(Integer.valueOf(df.format(member_discount * 100)));
            //店铺促销
            shop_activity_discount = data.getShop_activity_discount();
            goodsBean.setShop_activity_discount(Integer.valueOf(df.format(shop_activity_discount * 100)));
            //优惠券促销
            coupon_discount = data.getCoupon_discount();
            goodsBean.setCoupon_discount(Integer.valueOf(df.format(coupon_discount * 100)));
            //收银员单品促销（临时商品折扣）
            cashier_item_discount = data.getTemp_goods_discount();
            goodsBean.setCashier_item_discount(Integer.valueOf(
                    df.format(cashier_item_discount * 100)));
            //收银员促销
            temp_good_discount += cashier_item_discount;
            cashier_discount = data.getTemp_goods_discount() + data.getTemp_order_discount();
            goodsBean.setCashier_discount(Integer.valueOf(
                    df.format(cashier_discount * 100)));



            goodsBean.setCount(count);
            if (good.getItemType() == GoodsEntity.TYPE_ONLY_PROCESSING) {
                price = Float.valueOf(data.getProcess_price());
                unit_price = price;
            } else if (good.getItemType() == GoodsEntity.TYPE_WEIGHT) {
                price = Float.valueOf(data.getPrice()) * count;
                unit_price = Float.valueOf(data.getPrice());
            } else {
                price = Float.valueOf(data.getPrice());
                unit_price = Float.valueOf(data.getPrice());
            }
            goodsBean.setPrice(Integer.valueOf(df.format(price * 100)));
            goodsBean.setUnit_price(Integer.valueOf(df.format(unit_price * 100)));
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
                        price = Float.valueOf(data.getProcess_price()) * data.getCount();
                        unit_price = Float.valueOf(data.getProcess_price());
                        goodsBean.setPrice(Integer.valueOf(df.format(price * 100)));
                        goodsBean.setUnit_price(Integer.valueOf(df.format(unit_price * 100)));
                        goodsBean.setBarcode(data.getBarcode());
                        list.add(goodsBean);
                    }
                    break;
            }

        }

        //总收银员让利
        requestBody1.setCashier_discount(Integer.valueOf(
                df.format(mTempOrderPromotionMoney * 100 + temp_good_discount * 100)));
        //收银员整单让利
        requestBody1.setCashier_order_discount(Integer.valueOf(
                df.format(mTempOrderPromotionMoney * 100)));
        //总促销让利
        requestBody1.setActivity_discount(Integer.valueOf(
                df.format(all_activity_discount * 100)));
        //总会员让利
        requestBody1.setMember_discount(Integer.valueOf(
                df.format(all_member_discount * 100)));

        //商品促销详情
        for (Map.Entry<Integer, Float> next : id2discount.entrySet()) {
            Integer key = next.getKey();
            Float value = next.getValue();

            CreateOrderRequestBody.ActivitiesBean activitiesBean = new CreateOrderRequestBody.ActivitiesBean();
            activitiesBean.setId(key);
            activitiesBean.setType("goods");
            activitiesBean.setDiscount(Integer.valueOf(df.format(value * 100)));
            activitiesBeans.add(activitiesBean);
        }

        requestBody1.setGoods_list(list);

        mPresenter.createOrder(GsonUtils.getInstance().getGson().toJson(requestBody1));
    }


    @Override
    public void createOrderSuccess(CreateOderResponse result) {
        Configs.trade_no = result.getTrade_no();
        Configs.order_no = result.getTrade_num();

        transaction_no = result.getTransaction_no();
        pay_fee = result.getPay_fee();

        onAfterCreateOrder();
        mDuringCreateOrder = false;

    }

    /**
     * 创建订单后，调起支付方法
     */
    private void onAfterCreateOrder() {
        mDuringPay = true;

        if(mOnlyGiftCardPay) {//纯礼品卡支付
            mPresenter.giftCardPay(Configs.order_no);
            return;
        }
        switch (mPayWay) {
            case PayWayView.WAY_CASH:

                DecimalFormat df = new DecimalFormat("#");
                float pay = getReceivableMoney();
                String real_pay = df.format(pay * 100);
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
            case PayWayView.WAY_BANK_CARD:
                showBankcardDialog();
                break;
        }
    }

    @Override
    public void createOrderFailed(Map<String, Object> map) {
//        if (HttpExceptionEngine.isBussinessError(map)) {
            showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
//        }
        mDuringCreateOrder = false;
    }

    @Override
    public void aliPaySuccess(String result) {
        showToast("支付宝支付成功");

        onPaySuccessAfter();
    }

    private void onPaySuccessAfter() {
        mOrderFinished = true;
        mDuringPay = false;
        EventUtils.post(Events.CLEAR_GOODS_INFO);

        if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
            mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_SUCCESSFUL_RECEIPT);
        }

        if(mBankcardDialog != null && mBankcardDialog.getDialog() != null && mBankcardDialog.getDialog().isShowing()) {
            mBankcardDialog.dismiss();
        }

        //设置为已收款
        settlementView.setAlreadySettlement();

        //打印小票
        print();
        printLocal();

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

            if (11306 == err_code) {
                showToast("支付宝： 开始查询订单支付状态");
                mPresenter.checkAliPayStatus(Test.shop_sn, Configs.order_no);
            } else {
                showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
                dismissScanDialog();
            }
        } else {
            showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
            dismissScanDialog();
        }

        mDuringPay = false;
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

            if (11306 == err_code) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("支付宝： 继续查询订单支付状态");
                        mPresenter.checkAliPayStatus(Test.shop_sn, Configs.order_no);

                    }
                }, 1000);
            } else {
                showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
                dismissScanDialog();
            }
        } else {
            showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
            dismissScanDialog();
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

            if (11306 == err_code) {
                showToast("微信： 开始查询订单支付状态");
                mPresenter.checkWechatPayStatus(Test.shop_sn, Configs.order_no);
            } else {
                showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
                dismissScanDialog();
            }
        } else {
            showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
            dismissScanDialog();
        }

        mDuringPay = false;
    }

    /**
     * 取消扫码弹窗
     */
    private void dismissScanDialog() {
        if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
            mScanCodeDialog.dismiss();
        }
    }
    /**
     * 取消银联弹窗
     */
    private void dismissBankcardDialog() {
        if (mBankcardDialog != null &&  mBankcardDialog.getDialog() != null && mBankcardDialog.getDialog().isShowing()) {
            mBankcardDialog.dismiss();
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

            if (11306 == err_code) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showToast("微信： 继续查询订单支付状态");
                        mPresenter.checkWechatPayStatus(Test.shop_sn, Configs.order_no);

                    }
                }, 1000);
            } else {
                showToast(((String) map.get(HttpExceptionEngine.ErrorMsg)));
                dismissScanDialog();
            }
        } else {
            showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
            dismissScanDialog();
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
        mDuringPay = false;
    }

    @Override
    public void bankcardSuccess(BankcardStatusResponse result) {
        int status = result.getStatus();
        switch (status) {
            case 1:
                checkBankcardPayStatus();
                break;
            case 2:
                showToast("银联支付成功");
                onPaySuccessAfter();
                break;
        }
    }

    @Override
    public void bankcardFailed(Map<String, Object> map) {
        checkBankcardPayStatus();
    }

    @Override
    public void giftCardSuccess(GiftCardResponse result) {
        float giftCardBalance = result.getBalance_amount();
        if (giftCardBalance <= 0) {
            if(mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
                mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_GIFT_CARD_NULL);
            }
            return;
        }
        GiftCardUtils.getInstance().setGiftCardInfo(result);
        refreshGiftCardMoney();
        refreshRealPay();
        refreshSettlementView();


        if(mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
            mScanCodeDialog.dismiss();
        }
    }

    @Override
    public void giftCardFailed(Map<String, Object> map) {
        if(mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
            mScanCodeDialog.setStatus(ScanCodeDialog.STATUS_GIFT_CARD_NULL);
        }
    }

    @Override
    public void giftCardPaySuccess(String result) {
        showToast("礼品卡支付成功");
        onPaySuccessAfter();
    }

    @Override
    public void giftCardPayFailed(Map<String, Object> map) {
        showToast("礼品卡支付失败 - " + ((String) map.get(HttpExceptionEngine.ErrorMsg)));
        mDuringPay = false;
    }

    @Override
    public void printSuccess(String result) {
//        showToast("打印成功");
    }

    @Override
    public void printFailed(Map<String, Object> map) {
//        showToast("打印失败 - " + ((String) map.get(HttpExceptionEngine.ErrorMsg)));

    }

    @Override
    public void couponSuccess(CouponResponse result) {
        //设置优惠券信息
        List<CouponResponse> list = new ArrayList<>();
        list.add(result);
        couponsDialog.setNewData(list);
    }

    @Override
    public void couponFailed(Map<String, Object> map) {
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg) + ", " + map.get(HttpExceptionEngine.ErrorCode));
    }


    @Override
    public void memberWalletSuccess(String result) {
        showToast("会员钱包支付成功");

        onPaySuccessAfter();
    }

    @Override
    public void memberWalletFailed(Map<String, Object> map) {
        showToast("会员钱包支付失败 - " + ((String) map.get(HttpExceptionEngine.ErrorMsg)));
        dismissScanDialog();
        mDuringPay = false;
    }

    @Override
    public void unionPaySuccess(String result) {

    }

    @Override
    public void unionPayFailed(Map<String, Object> map) {

    }

    @Override
    public void checkUnionPaySuccess(String result) {

    }

    @Override
    public void checkUnionPayFailed(Map<String, Object> map) {

    }

    @Override
    public void unlockCouponSuccess() {
        Log.i(TAG, "unlockCouponSuccess: 优惠券解绑成功");
    }

    @Override
    public void unlockCouponFailed(Map<String, Object> map) {
        Log.i(TAG, "unlockCouponSuccess: 优惠券解绑失败");
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                unlockCoupon();
            }
        }, 1000);
    }
}

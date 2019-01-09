package com.easygo.cashier.module.refund;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.easygo.cashier.Configs;
import com.easygo.cashier.Events;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.request.RefundCashRequestBody;
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.widget.ConfirmDialog;
import com.easygo.cashier.widget.Keyboard;
import com.easygo.cashier.widget.PayWayView;
import com.niubility.library.base.BaseMvpFragment;
import com.niubility.library.utils.EventUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RefundCashFragment extends BaseMvpFragment<RefundCashContract.IView, RefundCashPresenter> implements RefundCashContract.IView {

    @BindView(R.id.pay_way_view)
    PayWayView payWayView;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.kb)
    Keyboard kb;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;

    private RefundCashView refundCashView;
    private ConfirmDialog confirmDialog;
    private int mPayWay = PayWayView.WAY_CASH;

    /**
     * 数量
     */
    private int mGoodsCount;
    /**
     * 总额
     */
    private float mTotalMoney;
    /**
     * 商品数据
     */
    private List<GoodsEntity<GoodsResponse>> mData;

    /**
     * 实退
     */
    private float mRealRefundCash;

    public static RefundCashFragment newInstance() {
        return new RefundCashFragment();
    }

    public static RefundCashFragment newInstance(Bundle bundle) {

        RefundCashFragment refundCashFragment = new RefundCashFragment();
        refundCashFragment.setArguments(bundle);
        return refundCashFragment;
    }

    @Override
    protected RefundCashPresenter createPresenter() {
        return new RefundCashPresenter();
    }

    @Override
    protected RefundCashContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refund_cash;
    }

    @Override
    protected void init() {
        payWayView.setPayWayShow(new int[]{0});
        refundCashView = RefundCashView.create(getActivity());
        framelayout.addView(refundCashView);

        setArgumentsData();

        setListener();

        initKeyboard();
    }

    //初始化数据
    private void setArgumentsData() {
        Bundle bundle = getArguments();
        if (bundle != null) {

            setData(bundle);
        }
    }

    private void setListener() {
        refundCashView.setOnClickListener(new RefundCashView.OnClickListener() {
            @Override
            public void onPrintClicked(boolean isChecked) {
                if (!isChecked) {
                    showToast("交易完成后将不打印小票");
                }
//                print();
            }

            @Override
            public void onCommitOrderClicked() {
//                if(mRealPay < mTotalMoney) {
//                    showToast("实收金额小于应收金额， 请确认！");
//                }
                //弹出确认弹窗
                Bundle bundle = ConfirmDialog.getDataBundle(mGoodsCount + "", mRealRefundCash, PayWayView.WAY_CASH, false, "应退：", "实退：");
                confirmDialog = new ConfirmDialog();
                confirmDialog.setArguments(bundle);
                confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListenr() {
                    @Override
                    public void onConfirm() {
                        onCommitOrder();
                    }
                });
                confirmDialog.show(getActivity().getSupportFragmentManager(), "tag_tip_dialog");
            }
        });
    }

    private void initKeyboard() {
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
                    mRealRefundCash = mTotalMoney;
                    //刷新价格
                    if (refundCashView != null)
                        refundCashView.setData(mTotalMoney, mRealRefundCash);
                    return;
                }

                //以00 . 开头 直接返回
                if (text.startsWith(".") || text.startsWith("00")) {
                    s.delete(0, s.length());
                    return;
                }
                float data = Float.valueOf(text);

                if (data > mTotalMoney) {
                    showToast("实退金额不能大于应退金额！");
                    s.delete(s.length() - 1, s.length());
                } else {
                    mRealRefundCash = data;

                    //刷新价格
                    if (refundCashView != null)
                        refundCashView.setData(mTotalMoney, mRealRefundCash);

                }

            }
        });
    }

    /**
     * 设置数据
     */
    public void setData(Bundle bundle) {
        mGoodsCount = bundle.getInt("goods_count");
        mTotalMoney = bundle.getFloat("total_money");
        mData = (List<GoodsEntity<GoodsResponse>>) bundle.getSerializable("goods_data");

        mRealRefundCash = mTotalMoney;
        if (refundCashView != null) {
            refundCashView.setData(mTotalMoney, mRealRefundCash);
        }
    }

    @OnClick(R.id.btn_delete)
    public void onViewClicked() {
        //键盘删除按钮
        int selectionStart = etMoney.getSelectionStart();
        Editable editable = etMoney.getText();

        if (editable != null && editable.length() > 0) {
            if (selectionStart > 0) {
                editable.delete(selectionStart - 1, selectionStart);
            }
        }

    }

    public void onCommitOrder() {
//        if (mPayWay == PayWayView.WAY_ALIPAY) {
//            if (TextUtils.isEmpty(Configs.order_no)) {
//                showToast("确认提交订单");
        refund();
//            } else {
//                showToast("订单已经创建 --> " + Configs.order_no);
//            }
//        } else {
//            showToast("开发中");
//        }
    }

    private void refund() {

        int size = mData.size();
        RefundCashRequestBody requsetBody = new RefundCashRequestBody();
        requsetBody.setShop_sn(Configs.shop_sn);


//        RefundCashRequestBody.GoodsList goodsList = new RefundCashRequestBody.GoodsList();
        for (int i = 0; i < size; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = mData.get(i);


        }


//        mPresenter.cashRefund();

        printRefund();

    }

    private void printRefund() {

        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        DecimalFormat df = new DecimalFormat("0.00");

        sb.append(PrintHelper.CB_left).append(Configs.shop_name).append(PrintHelper.CB_right).append(PrintHelper.BR)
                .append("时间：").append(sdf.format(new Date())).append(PrintHelper.BR)
                .append("收银员：").append(Configs.admin_name).append(PrintHelper.BR)
                .append("--------------------------------").append(PrintHelper.BR)
                .append("品名  ").append("单价  ").append("优惠  ").append("数量/重量  ").append("小计  ")
                .append(PrintHelper.BR);

        GoodsEntity<GoodsResponse> goodsEntity;
        boolean is_processing;
        int size = mData.size();

        int index = 1;
        int count;
        float price;
        float sub_total;
        GoodsResponse data;
        GoodsResponse processing;
        for (int i = 0; i < size; i++) {
            goodsEntity = mData.get(i);
            data = goodsEntity.getData();
            //加工方式商品
            processing = goodsEntity.getProcessing();

            price = Float.valueOf(data.getPrice());
            count = goodsEntity.getCount();
            sub_total = price * count;

            is_processing = processing != null;

            sb.append(index).append(".")
                    .append(data.getG_sku_name()).append("   ").append(PrintHelper.BR)
                    .append("     ")
                    .append(data.getPrice()).append("   ")
                    .append(data.getDiscount_price()).append("   ")
                    .append(is_processing ? count + "g" : count).append("   ")
                    .append(df.format(sub_total)).append(PrintHelper.BR);


            if (is_processing) {
                index++;
                price = Float.valueOf(processing.getProcess_price());
                sub_total = price * processing.getCount();

                sb.append(index).append(".")
                        .append(processing.getG_sku_name()).append("   ").append(PrintHelper.BR)
                        .append("     ")
                        .append(processing.getProcess_price()).append("   ")
                        .append(processing.getDiscount_price()).append("   ")
                        .append(processing.getCount()).append("   ")
                        .append(df.format(sub_total)).append(PrintHelper.BR);
            }

            index++;
        }
        sb.append("--------------------------------").append(PrintHelper.BR)
                .append("退货数：").append(mGoodsCount).append(PrintHelper.BR)
                .append("退款：").append(df.format(mRealRefundCash)).append("元").append(PrintHelper.BR);


        mPresenter.print_info(Configs.shop_sn, Configs.printer_sn, sb.toString());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refundCashView != null) {
            refundCashView.release();
        }
    }

    @Override
    public void cashRefundSuccess(String result) {
        printRefund();
        EventUtils.post(Events.CLEAR_GOODS_INFO);
    }

    @Override
    public void cashRefundFailed(Map<String, Object> map) {

    }

    @Override
    public void printSuccess(String result) {

    }

    @Override
    public void printFailed(Map<String, Object> map) {

    }
}

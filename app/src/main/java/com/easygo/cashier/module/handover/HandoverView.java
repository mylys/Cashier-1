package com.easygo.cashier.module.handover;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.bean.HandoverResponse;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HandoverView extends FrameLayout {

    private static final String TAG = "HandoverView";
    @BindView(R.id.tv_total_sales)
    TextView tvTotalSales;
    @BindView(R.id.tv_cash)
    TextView tvCash;
    @BindView(R.id.tv_alipay)
    TextView tvAlipay;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.tv_total_order_count)
    TextView tvTotalOrderCount;
    @BindView(R.id.tv_sale_count)
    TextView tvSaleCount;
    @BindView(R.id.tv_refund_count)
    TextView tvRefundCount;
    @BindView(R.id.tv_total_cash)
    TextView tvTotalCash;
    @BindView(R.id.tv_cash_income)
    TextView tvCashIncome;
    private View mView;
    private Unbinder unbinder;

    public HandoverView(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_handover, this, true);
        unbinder = ButterKnife.bind(this, mView);
    }


    public static HandoverView create(Context context) {
        return new HandoverView(context);
    }


    public void setData(HandoverResponse result) {

        DecimalFormat df = new DecimalFormat("#0.00");

        //总单据数
        int sale_count = result.getSale_count();
        int refund_count = result.getRefund_count();
        tvTotalOrderCount.setText(String.valueOf(sale_count + refund_count));
        tvSaleCount.setText(String.valueOf(sale_count));
        tvRefundCount.setText(String.valueOf(refund_count));

        //总销售额
        float cash_money = result.getCash_money();
        String cash = df.format(cash_money);
        tvTotalSales.setText(df.format(Float.valueOf(result.getAll_money())));
        tvCash.setText(cash);
        tvAlipay.setText(df.format(result.getAlipay_money()));
        tvWechat.setText(df.format(result.getWx_money()));

        //总现金
        tvTotalCash.setText(cash);
        tvCashIncome.setText(cash);

    }

    public void release() {
        if (unbinder != null)
            unbinder.unbind();
    }

}

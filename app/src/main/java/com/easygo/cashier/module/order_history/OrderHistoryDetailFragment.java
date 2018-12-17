package com.easygo.cashier.module.order_history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easygo.cashier.R;
import com.easygo.cashier.adapter.OrderHistoryGoodsAdapter;
import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.bean.OrderHistoryInfo;
import com.niubility.library.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OrderHistoryDetailFragment extends BaseFragment {

    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_cashier_account)
    TextView tvCashierAcount;
    @BindView(R.id.tv_return_of_goods_count)
    TextView tvReturnOfGoodsCount;
    @BindView(R.id.tv_refund)
    TextView tvRefund;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.tv_receipts)
    TextView tvReceipts;
    @BindView(R.id.tv_receipts_way)
    TextView tvReceiptsWay;
    @BindView(R.id.tv_receipts_cash_change)
    TextView tvReceiptsCashChange;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.tv_receivable)
    TextView tvReceivable;
    @BindView(R.id.tv_receivable_total_money)
    TextView tvReceivableTotalMoney;
    @BindView(R.id.tv_refund_way)
    TextView tvRefundWay;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_buyer)
    TextView tvBuyer;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    Unbinder unbinder;

    /**商品数据*/
    private List<GoodsInfo> mdata;
    private OrderHistoryGoodsAdapter orderHistoryGoodsAdapter;

    private float mTotalMoney;
    private float mReceivable;
    private float mReceipts;
    private float mCoupon;
    private float mGoodsCount;
    private float mReturnOfGoodsCount;
    private float mRefund;
    private int mPayWay;
    private String mBuyer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        rvGoods.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        orderHistoryGoodsAdapter = new OrderHistoryGoodsAdapter();
        rvGoods.setAdapter(orderHistoryGoodsAdapter);

        mdata = new ArrayList<>();
        orderHistoryGoodsAdapter.setNewData(mdata);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick({R.id.btn_refund, R.id.btn_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_refund:
                Toast.makeText(getContext(), "退款", Toast.LENGTH_SHORT).show();

                if (getActivity() != null) {
                    ((OrderHistoryActivity) getActivity()).toOrderHistoryRefundFragment();
                }
                break;
            case R.id.btn_print:
                Toast.makeText(getContext(), "打印单据", Toast.LENGTH_SHORT).show();
            break;
        }
    }

    public void showOrderHistory(OrderHistoryInfo orderHistoryInfo) {
        tvOrderNo.setText(orderHistoryInfo.getOrder_no());
        tvCashierAcount.setText(orderHistoryInfo.getAcount());
        tvGoodsCount.setText("共" + orderHistoryInfo.getCount() + "件");
        tvReceivableTotalMoney.setText("总额：￥" + orderHistoryInfo.getTotal_money());
        tvReceipts.setText("实收：￥" + orderHistoryInfo.getReceipts());
        tvReceivable.setText("应收：￥" + orderHistoryInfo.getReceivable());
        tvBuyer.setText(orderHistoryInfo.getBuyer());


        //优惠
        float coupon = orderHistoryInfo.getCoupon();
        if(coupon > 0) {
            tvCoupon.setText("- 优惠：￥" + coupon);
            tvCoupon.setVisibility(View.VISIBLE);
        } else {
            tvCoupon.setVisibility(View.GONE);
        }

        //退款
        float refund = orderHistoryInfo.getRefund();
        if(refund > 0) {
            tvRefund.setText("退款：￥" + refund);
            switch (orderHistoryInfo.getPay_way()) {
                case 0:
                    tvRefundWay.setText("现金：￥" + refund);
                    break;
                case 1:
                    tvRefundWay.setText("支付宝：￥" + refund);
                    break;
                case 2:
                    tvRefundWay.setText("微信：￥" + refund);
                    break;
            }
            tvReturnOfGoodsCount.setText("共退货" + orderHistoryInfo.getReturn_of_goods_count() + "件");
            tvRefund.setVisibility(View.VISIBLE);
            tvRefundWay.setVisibility(View.VISIBLE);
            tvReturnOfGoodsCount.setVisibility(View.VISIBLE);
        } else {
            tvRefund.setVisibility(View.GONE);
            tvRefundWay.setVisibility(View.GONE);
            tvReturnOfGoodsCount.setVisibility(View.GONE);
        }

        switch (orderHistoryInfo.getPay_way()) {
            case 0:
                tvReceiptsWay.setText("现金：￥" + orderHistoryInfo.getReceivable());
                tvReceiptsCashChange.setText("找零：￥" + orderHistoryInfo.getChange());
                tvReceiptsCashChange.setVisibility(View.VISIBLE);
                break;
            case 1:
                tvReceiptsWay.setText("支付宝：￥" + orderHistoryInfo.getReceivable());
                tvReceiptsCashChange.setVisibility(View.GONE);
                break;
            case 2:
                tvReceiptsWay.setText("微信：￥" + orderHistoryInfo.getReceivable());
                tvReceiptsCashChange.setVisibility(View.GONE);
                break;
        }

        List<GoodsInfo> goods = orderHistoryInfo.getGoods();


    }
}

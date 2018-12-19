package com.easygo.cashier.module.order_history;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
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
import com.easygo.cashier.bean.OrderHistorysInfo;
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

    private String payType = "";

    /**
     * 商品数据
     */
    private OrderHistoryGoodsAdapter orderHistoryGoodsAdapter;

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
        rvGoods.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvGoods.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        orderHistoryGoodsAdapter = new OrderHistoryGoodsAdapter();
        rvGoods.setAdapter(orderHistoryGoodsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick({R.id.btn_refund, R.id.btn_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_refund:
                if (getActivity() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("data", (ArrayList<OrderHistorysInfo.ListBean>) orderHistoryGoodsAdapter.getData());
                    bundle.putString("pay_type","支付方式：" + payType);
                    ((OrderHistoryActivity) getActivity()).toOrderHistoryRefundFragment(bundle);
                }
                break;
            case R.id.btn_print:
                Toast.makeText(getContext(), "打印单据", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showOrderHistory(OrderHistorysInfo orderHistoryInfo, String name) {
        orderHistoryGoodsAdapter.setNewData(orderHistoryInfo.getList());
        tvOrderNo.setText(orderHistoryInfo.getTrade_num());
        tvCashierAcount.setText(name);
        tvGoodsCount.setText("共" + orderHistoryInfo.getList().size() + "件");
        tvReceivableTotalMoney.setText("总额：￥" + orderHistoryInfo.getTotal_money());
        tvReceipts.setText("实收：￥" + orderHistoryInfo.getBuyer_pay());
        tvReceivable.setText("应收：￥" + orderHistoryInfo.getReal_pay());
        tvBuyer.setText("付款人：" + orderHistoryInfo.getBuyer());

        switch (orderHistoryInfo.getStatus()){
            case 1:
                tvBuyer.setVisibility(View.INVISIBLE);
                tvReturnOfGoodsCount.setVisibility(View.INVISIBLE);
                break;
            case 2:
                tvBuyer.setVisibility(View.VISIBLE);
                tvReturnOfGoodsCount.setVisibility(View.INVISIBLE);
                break;
            case 3:
                tvReturnOfGoodsCount.setVisibility(View.VISIBLE);
                tvBuyer.setVisibility(View.INVISIBLE);
                break;
        }

        switch (orderHistoryInfo.getPay_type()) {
            case 1:
                payType = "微信支付";
                break;
            case 2:
                payType = "微信 + 现金支付";
                break;
            case 3:
                payType = "现金支付";
                break;
            case 4:
                payType = "企业钱包支付";
                break;
            case 5:
                payType = "微信 + 企业钱包支付";
                break;
            case 6:
                payType = "支付宝支付";
                break;
        }
//        //优惠
//        float coupon = orderHistoryInfo.getCoupon();
//        if (coupon > 0) {
//            tvCoupon.setText("- 优惠：￥" + coupon);
//            tvCoupon.setVisibility(View.VISIBLE);
//        } else {
//            tvCoupon.setVisibility(View.GONE);
//        }

        //退款
//        float refund = (float) orderHistoryInfo.getRefund_fee();
//        if (refund > 0) {
//            tvRefund.setText("退款：￥" + refund);

//            tvReturnOfGoodsCount.setText("共退货" + orderHistoryInfo.getReturn_of_goods_count() + "件");
//            tvRefund.setVisibility(View.VISIBLE);
//            tvRefundWay.setVisibility(View.VISIBLE);
//            tvReturnOfGoodsCount.setVisibility(View.VISIBLE);
//        } else {
//            tvRefund.setVisibility(View.GONE);
//            tvRefundWay.setVisibility(View.GONE);
//            tvReturnOfGoodsCount.setVisibility(View.GONE);
//        }

//        switch (orderHistoryInfo.getPay_type()) {
//            case 0:
//                tvReceiptsWay.setText("现金：￥" + orderHistoryInfo.getReceivable());
//                tvReceiptsCashChange.setText("找零：￥" + orderHistoryInfo.getChange());
//                tvReceiptsCashChange.setVisibility(View.VISIBLE);
//                break;
//            case 1:
//                tvReceiptsWay.setText("支付宝：￥" + orderHistoryInfo.getReceivable());
//                tvReceiptsCashChange.setVisibility(View.GONE);
//                break;
//            case 2:
//                tvReceiptsWay.setText("微信：￥" + orderHistoryInfo.getReceivable());
//                tvReceiptsCashChange.setVisibility(View.GONE);
//                break;
//        }
    }
}

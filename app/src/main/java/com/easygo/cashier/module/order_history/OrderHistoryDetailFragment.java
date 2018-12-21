package com.easygo.cashier.module.order_history;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.tv_receivable)
    TextView tvReceivable;
    @BindView(R.id.tv_buyer)
    TextView tvBuyer;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.tv_receivable_text)
    TextView tvReceivableText;
    @BindView(R.id.tv_receipts_text)
    TextView tvReceiptsText;
    @BindView(R.id.tv_refund_text)
    TextView tvRefundText;
    Unbinder unbinder;

    /* 获取支付方式，传递给退款Fragment */
    private String payType = "";
    /* 获取订单单号，传递给退款Fragment */
    private String order_number = "";

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
        rvGoods.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
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
                    bundle.putString("pay_type", "支付方式：" + payType);
                    bundle.putString("order_number", "订单单号：" + order_number);
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
        tvReceipts.setText("实收：￥" + orderHistoryInfo.getBuyer_pay());

        tvReceivable.setText("应收：￥" + orderHistoryInfo.getReal_pay());
        tvReceivableText.setText("总额：￥" + orderHistoryInfo.getTotal_money() + " = ");

        order_number = orderHistoryInfo.getTrade_num();

        switch (orderHistoryInfo.getPay_type()) {
            case 1:
                payType = "微信";
                break;
            case 2:
                payType = "微信 + 现金";
                break;
            case 3:
                payType = "现金";
                break;
            case 4:
                payType = "企业钱包";
                break;
            case 5:
                payType = "微信 + 企业钱包";
                break;
            case 6:
                payType = "支付宝";
                break;
        }
        double change_money = Double.parseDouble(orderHistoryInfo.getChange_money());
        double buyer_price = Double.parseDouble(orderHistoryInfo.getBuyer_pay());
        double total_buyer_price = buyer_price + change_money;
        String change_price = "0.00";
        if (change_money != 0) {
            change_price = change_money + "";
        }
        if (total_buyer_price != 0) {
            tvReceiptsText.setText(payType + "：￥" + total_buyer_price + " - 找零：￥" + change_price + " = ");
        } else {
            tvReceiptsText.setText(payType + "：￥0.00 - 找零：￥" + change_price + " = ");
        }

        /*付款*/
        tvBuyer.setVisibility(View.INVISIBLE);

        /*退款*/
        tvRefund.setVisibility(View.INVISIBLE);
        tvRefundText.setVisibility(View.INVISIBLE);
        tvReturnOfGoodsCount.setVisibility(View.INVISIBLE);
        switch (orderHistoryInfo.getStatus()) {
            case 1:
                break;
            case 2:
                tvBuyer.setVisibility(View.VISIBLE);
                tvBuyer.setText("付款人：" + orderHistoryInfo.getBuyer());
                break;
            case 3:
                tvReturnOfGoodsCount.setVisibility(View.VISIBLE);
                tvRefund.setVisibility(View.VISIBLE);

                double refund_price = 0;
                if (orderHistoryInfo.getRefund_fee() != null) {
                    refund_price = (double) orderHistoryInfo.getRefund_fee();
                }
                double total_refund = refund_price + change_money;
                if (total_refund != 0) {
                    tvRefundText.setText(payType + "：￥" + total_refund + " - 找零：￥" + change_price + " = ");
                } else {
                    tvRefundText.setText(payType + "：￥0.00 - 找零：￥" + change_price + " = ");
                }
                tvRefund.setText("退款：￥" + refund_price);
                tvReturnOfGoodsCount.setText("共退款" + orderHistoryInfo.getList().size() + "件");
                break;
        }
    }
}

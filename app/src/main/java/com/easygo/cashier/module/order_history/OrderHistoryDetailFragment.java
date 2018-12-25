package com.easygo.cashier.module.order_history;

import android.os.Bundle;
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
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.niubility.library.base.BaseFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;

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

    private int total_price = 0;
    private DecimalFormat df = new DecimalFormat("0.00");

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
                    bundle.putString("pay_type", payType);
                    bundle.putInt("total_price", total_price);
                    bundle.putString("order_number", order_number);
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
        total_price = Integer.parseInt(orderHistoryInfo.getTotal_money().replace(".", ""));

        switch (orderHistoryInfo.getPay_type()) {
            case 1:
                payType = getResources().getString(R.string.pay_wechat);
                break;
            case 2:
                payType = getResources().getString(R.string.pay_wechat_cash);
                break;
            case 3:
                payType = getResources().getString(R.string.pay_cash);
                break;
            case 4:
                payType = getResources().getString(R.string.pay_wallet);
                break;
            case 5:
                payType = getResources().getString(R.string.pay_wechat_wallet);
                break;
            case 6:
                payType = getResources().getString(R.string.pay_alipay);
                break;
        }
        double change_money = Double.parseDouble(orderHistoryInfo.getChange_money());
        double buyer_price = Double.parseDouble(orderHistoryInfo.getBuyer_pay());
        String total_buyer_price = df.format(buyer_price + change_money);
        String change_price = "0.00";
        if (change_money != 0) {
            change_price = df.format(change_money);
        }
        tvReceiptsText.setText(payType + "：￥" + total_buyer_price + " - 找零：￥" + change_price + " = ");

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
                String total_refund_str = df.format(refund_price + change_money);
                tvRefundText.setText(payType + "：￥" + total_refund_str + " - 找零：￥" + change_price + " = ");
                tvRefund.setText("退款：￥" + refund_price);
                tvReturnOfGoodsCount.setText("共退款" + orderHistoryInfo.getList().size() + "件");
                break;
        }
    }
}

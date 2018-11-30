package com.easygo.cashier.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.easygo.cashier.R;
import com.easygo.cashier.bean.OrderHistoryInfo;
import com.niubility.library.base.BaseFragment;

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
    @BindView(R.id.tv_receipts_cash)
    TextView tvReceiptsCash;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.tv_receivable)
    TextView tvReceivable;
    @BindView(R.id.tv_receivable_cash)
    TextView tvReceivableCash;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    Unbinder unbinder;

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


//        rvGoods.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_refund, R.id.btn_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_refund:
                Toast.makeText(getContext(), "退款", Toast.LENGTH_SHORT).show();
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
        tvReceipts.setText("=实收:￥" + orderHistoryInfo.getReceipts());
        tvReceivable.setText("=应收:￥" + orderHistoryInfo.getReceivable());
        tvRefund.setText("退款金额=￥" + orderHistoryInfo.getRefund());
        tvReturnOfGoodsCount.setText("共退货" + orderHistoryInfo.getReturn_of_goods_count() + "件");
        tvReceiptsCash.setText("现金:￥" + orderHistoryInfo.getReceivable());



    }
}

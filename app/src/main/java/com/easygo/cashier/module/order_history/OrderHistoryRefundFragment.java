package com.easygo.cashier.module.order_history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.easygo.cashier.Configs;
import com.easygo.cashier.R;
import com.easygo.cashier.Test;
import com.easygo.cashier.adapter.OrderHistoryRefundAdapter;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.widget.ConfirmDialog;
import com.easygo.cashier.widget.MySearchView;
import com.easygo.cashier.widget.PayWayView;
import com.niubility.library.base.BaseFragment;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OrderHistoryRefundFragment extends BaseFragment {

    @BindView(R.id.search_view)
    MySearchView searchView;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_order_cashier)
    TextView tvOrderCashier;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.tv_refoundcash_price)
    TextView tvRefoundcashPrice;
    Unbinder unbinder;

    private ConfirmDialog confirmDialog;
    private int mPayWay = PayWayView.WAY_CASH;
    private OrderHistoryRefundAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history_refund, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();
        return view;
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        adapter = new OrderHistoryRefundAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setNewData(Test.getGoodsRefundInfos());

        setListener();
    }

    private void setListener() {
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果点击checkbox，则全选，反之全否
                adapter.setClick(checkbox.isChecked());
            }
        });
        adapter.setOnItemClickListener(new OrderHistoryRefundAdapter.OnItemClickListener() {
            @Override
            public void onClick(boolean isClick) {
                //点击Item判断是否全选，反之checkbox关闭
                checkbox.setChecked(isClick);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    public static OrderHistoryRefundFragment newInstance() {
        return new OrderHistoryRefundFragment();
    }

    @OnClick(R.id.btn_refund)
    public void onViewClicked() {
        Bundle bundle = ConfirmDialog.getDataBundle(0, 0, 0, PayWayView.WAY_CASH, false, "应退", "实退");
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

    public void onCommitOrder() {
        if (mPayWay == PayWayView.WAY_ALIPAY) {
            if (TextUtils.isEmpty(Configs.order_no)) {
                showToast("确认提交订单");
//                createOrder();
            } else {
                showToast("订单已经创建 --> " + Configs.order_no);
            }
        } else {
            showToast("开发中");
        }
    }
}

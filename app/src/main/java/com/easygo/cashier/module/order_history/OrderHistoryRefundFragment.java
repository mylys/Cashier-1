package com.easygo.cashier.module.order_history;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderHistoryRefundFragment extends BaseFragment {

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history_refund, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null)
            unbinder.unbind();
    }

    public static OrderHistoryRefundFragment newInstance() {
        return new OrderHistoryRefundFragment();
    }
}
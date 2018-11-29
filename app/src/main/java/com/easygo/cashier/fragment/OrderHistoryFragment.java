package com.easygo.cashier.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.easygo.cashier.R;
import com.easygo.cashier.activity.Test;
import com.easygo.cashier.adapter.OrderHistoryAdapter;
import com.easygo.cashier.bean.OrderHistoryInfo;
import com.niubility.library.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class OrderHistoryFragment extends BaseFragment {

    private static final String TAG = "OrderHistoryFragment";

    @BindView(R.id.rv_order_history)
    RecyclerView rvOrderHistory;
    private Unbinder unbinder;

    private final String TAG_ORDER_HISTORY = "tag_order_history";
    private OrderHistoryDetailFragment orderHistoryDetailFragment;

    public static OrderHistoryFragment newInstance() {
        return new OrderHistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayoutManager llm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        final OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter();
        rvOrderHistory.setLayoutManager(llm);
        rvOrderHistory.setAdapter(orderHistoryAdapter);

        orderHistoryAdapter.setData(Test.getOrderHistoryData());

        orderHistoryAdapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(OrderHistoryInfo orderHistoryInfo, int position) {
                Log.i(TAG, "onItemClick: orderHistoryInfo --> " + orderHistoryInfo.toString());
                Toast.makeText(getContext(), "position-> " + position, Toast.LENGTH_SHORT).show();
                showOrderHistory(orderHistoryInfo);
            }
        });

        if(orderHistoryDetailFragment == null) {
            orderHistoryDetailFragment = new OrderHistoryDetailFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, orderHistoryDetailFragment, TAG_ORDER_HISTORY).commit();
        }

    }

    public void showOrderHistory(final OrderHistoryInfo orderHistoryInfo) {

                orderHistoryDetailFragment.showOrderHistory(orderHistoryInfo);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, 2000);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }
}

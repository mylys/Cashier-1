package com.easygo.cashier.module.order_history;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.R;
import com.easygo.cashier.Test;
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

        Resources res = getResources();
        int normal_color = res.getColor(R.color.color_505050);
        int selected_color = res.getColor(R.color.color_text_white);
        int background = res.getColor(R.color.color_51beaf);
        orderHistoryAdapter.setColor(normal_color, selected_color, background);


        rvOrderHistory.setLayoutManager(llm);
        rvOrderHistory.setAdapter(orderHistoryAdapter);

        //分割线
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(res.getDrawable(R.drawable.bg_item_decoration_vertical_order_history));
        rvOrderHistory.addItemDecoration(verticalDecoration);

        orderHistoryAdapter.setNewData(Test.getOrderHistoryData());

        orderHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                OrderHistoryAdapter myself = (OrderHistoryAdapter) adapter;
                int selected_old = myself.getSelected();
                if (selected_old != position) {
                    myself.setSelected(position);
                    myself.notifyItemChanged(selected_old);
                    myself.notifyItemChanged(position);
                }
                OrderHistoryInfo orderHistoryInfo = (OrderHistoryInfo) adapter.getData().get(position);
                Log.i(TAG, "onItemClick: orderHistoryInfo --> " + orderHistoryInfo.toString());
                showToast("position-> " + position);
                showOrderHistory(orderHistoryInfo);
            }
        });

        if(orderHistoryDetailFragment == null) {
            orderHistoryDetailFragment = new OrderHistoryDetailFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, orderHistoryDetailFragment, TAG_ORDER_HISTORY).commit();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //显示第一条数据
                orderHistoryDetailFragment.showOrderHistory(Test.getOrderHistoryData().get(0));
            }
        },1000);

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

package com.easygo.cashier.module.entry_orders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easygo.cashier.R;
import com.easygo.cashier.adapter.OrderHistoryAdapter;
import com.easygo.cashier.widget.MySearchView;
import com.niubility.library.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe：
 * @author：hgeson
 * @date：2019-01-03
 */
public class EntryOrdersFragment extends BaseFragment {

    @BindView(R.id.rv_entry_orders)
    RecyclerView rvEntryOrders;
    @BindView(R.id.cl_search)
    MySearchView clSearch;
    Unbinder unbinder;

    private EntryOrdersDetailFragment entryOrdersDetailFragment;
    private OrderHistoryAdapter adapter = new OrderHistoryAdapter();

    public static EntryOrdersFragment newInstance() {
        return new EntryOrdersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_orders, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        if (entryOrdersDetailFragment == null) {
            entryOrdersDetailFragment = new EntryOrdersDetailFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, entryOrdersDetailFragment, "tag_detail_entry_orders").commit();
        }

        rvEntryOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvEntryOrders.setAdapter(adapter);

        //分割线
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical_order_history));
        rvEntryOrders.addItemDecoration(verticalDecoration);
        setEmpty();
        setListener();
    }

    /* 设置adapter数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        adapter.setEmptyView(emptyView);
    }

    private void setListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}

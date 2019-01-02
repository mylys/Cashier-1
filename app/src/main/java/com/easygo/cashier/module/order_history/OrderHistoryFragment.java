package com.easygo.cashier.module.order_history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.Configs;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.OrderHistoryAdapter;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.widget.MySearchView;
import com.niubility.library.base.BaseMvpFragment;
import com.niubility.library.constants.Constans;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class OrderHistoryFragment extends BaseMvpFragment<OrderHistoryContract.IView, OrderHistoryPresenter> implements OrderHistoryContract.IView {

    @BindView(R.id.rv_order_history)
    RecyclerView rvOrderHistory;
    @BindView(R.id.cl_search)
    MySearchView clSearch;
    @BindView(R.id.refresh)
    SwipeRefreshLayout layout;

    private OrderHistoryDetailFragment orderHistoryDetailFragment;
    private OrderHistoryAdapter adapter = new OrderHistoryAdapter();
    SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getActivity());
    int handover_id = -1;

    private int page = 1;
    private int pageCount = 10;
    private boolean isSearch = false;

    public static OrderHistoryFragment newInstance() {
        return new OrderHistoryFragment();
    }

    @Override
    protected OrderHistoryPresenter createPresenter() {
        return new OrderHistoryPresenter();
    }

    @Override
    protected OrderHistoryContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_history;
    }

    @Override
    protected void init() {
        if (orderHistoryDetailFragment == null) {
            orderHistoryDetailFragment = new OrderHistoryDetailFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.framelayout, orderHistoryDetailFragment, "tag_order_history").commit();
        }

        handover_id = sp.getInt(Constans.KEY_HANDOVER_ID, -1);
        mPresenter.post(handover_id, null, page, pageCount);

        rvOrderHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrderHistory.setAdapter(adapter);

        //分割线
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical_order_history));
        rvOrderHistory.addItemDecoration(verticalDecoration);
        setEmpty();
        setListener();
    }

    private void setListener() {
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                mPresenter.post(handover_id, null, page, pageCount);
            }
        });

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mPresenter.post(handover_id, null, page, pageCount);
            }
        });

        adapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClck(int position) {
                orderHistoryDetailFragment.showOrderHistory(adapter.getData().get(position));
            }
        });

        clSearch.setOnSearchListenerClick(new MySearchView.OnSearhListenerClick() {
            @Override
            public void onSearch(String content) {
                page = 1;
                isSearch = content.length() != 0;
                mPresenter.post(handover_id, content.length() == 0 ? null : content, page, pageCount);
            }
        });
    }

    /* 设置adapter数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        adapter.setEmptyView(emptyView);
    }

    public void print(String info) {
        mPresenter.print_info(Configs.shop_sn, Configs.printer_sn, info);
    }

    @Override
    public void getHistoryInfo(List<OrderHistorysInfo> orderHistorysInfo) {
        if (orderHistorysInfo != null) {
            if (page == 1) {
                adapter.setNewData(orderHistorysInfo);
                if (orderHistoryDetailFragment != null) {
                    if (orderHistorysInfo.size() > 0) {
                        adapter.setItemClick();
                        orderHistoryDetailFragment.showOrderHistory(orderHistorysInfo.get(0));
                    }
                }
                if (isSearch){
                    adapter.loadMoreEnd();
                }
                layout.setRefreshing(false);
            } else {
                if (orderHistorysInfo.size() == 0) {
                    adapter.loadMoreEnd();
                    return;
                }
                adapter.addData(orderHistorysInfo);
                adapter.loadMoreComplete();
            }
        }
    }

    @Override
    public void getHistorfFailed(Map<String, Object> map) {
        if (HttpExceptionEngine.isBussinessError(map)) {
            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
            showToast("错误信息:" + err_msg);
        }
    }

    @Override
    public void printSuccess(String result) {

    }

    @Override
    public void printFailed(Map<String, Object> map) {

    }
}

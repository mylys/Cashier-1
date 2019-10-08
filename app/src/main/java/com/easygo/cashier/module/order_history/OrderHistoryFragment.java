package com.easygo.cashier.module.order_history;

import android.content.SharedPreferences;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.Configs;
import com.easygo.cashier.Constants;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.OrderHistoryAdapter;
import com.easygo.cashier.base.BaseAppMvpFragment;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.widget.view.MySearchView;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;

public class OrderHistoryFragment extends BaseAppMvpFragment<OrderHistoryContract.IView, OrderHistoryPresenter> implements OrderHistoryContract.IView {

    @BindView(R.id.rv_order_history)
    RecyclerView rvOrderHistory;
    @BindView(R.id.cl_search)
    MySearchView clSearch;
    @BindView(R.id.refresh)
    SwipeRefreshLayout layout;
    @BindView(R.id.cb_local_order)
    CheckBox cbLocalOrder;

    private OrderHistoryDetailFragment orderHistoryDetailFragment;
    private OrderHistoryAdapter adapter = new OrderHistoryAdapter();
    private SharedPreferences sp;
    int handover_id = -1;

    private int page = 1;
    private int pageCount = 20;
    private boolean isSearch = false;
    private int is_local_order = 0;


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
        sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getActivity());
        handover_id = sp.getInt(Constants.KEY_HANDOVER_ID, -1);

        rvOrderHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvOrderHistory.setAdapter(adapter);

        //分割线
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical_order_history));
        rvOrderHistory.addItemDecoration(verticalDecoration);
        rvOrderHistory.setVisibility(View.INVISIBLE);
        setEmpty();
        setListener();
        toRefresh();
    }

    private void setListener() {
        cbLocalOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                is_local_order = isChecked? 1: 0;
                toRefresh();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                page++;
                mPresenter.post(handover_id, null, page, pageCount, is_local_order);
            }
        });

        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                mPresenter.post(handover_id, null, page, pageCount, is_local_order);
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
                if(isSearch) {
                    clSearch.startLoading();
                }
                mPresenter.post(handover_id, content.length() == 0 ? null : content, page, pageCount, is_local_order);
            }
        });
    }

    public void toRefresh() {
        layout.setRefreshing(true);
        page = 1;
        mPresenter.post(handover_id, null, page, pageCount, is_local_order);
    }

    /* 设置adapter空数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        TextView view = emptyView.findViewById(R.id.text);
        view.setText(getResources().getString(R.string.text_no_order));
        adapter.setEmptyView(emptyView);
    }

    //type 为0 时 为历史订单打印 ，  1时 为退款打印
    public void print(String info, int type) {
        mPresenter.print_info(Configs.shop_sn, Configs.printer_sn, info, type);
    }

    public void printRefundInfo(String info) {
        mPresenter.print_info(Configs.shop_sn, Configs.printer_sn, info, 1);
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
                    clSearch.stopLoading();
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
            rvOrderHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getHistorfFailed(Map<String, Object> map) {
        if (HttpExceptionEngine.isBussinessError(map)) {
            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
            showToast("错误信息:" + err_msg);
        }
        layout.setRefreshing(false);
    }

    @Override
    public void printSuccess(String result) {

    }

    @Override
    public void printFailed(Map<String, Object> map) {

    }
}

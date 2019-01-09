package com.easygo.cashier.module.entry_orders;

import android.content.SharedPreferences;
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

import com.easygo.cashier.Events;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.EntryOrdersAdapter;
import com.easygo.cashier.bean.EntryOrders;
import com.google.gson.reflect.TypeToken;
import com.niubility.library.base.BaseApplication;
import com.niubility.library.base.BaseFragment;
import com.niubility.library.constants.Constans;
import com.niubility.library.utils.EventUtils;
import com.niubility.library.utils.GsonUtils;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.util.List;

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
    Unbinder unbinder;

    private EntryOrdersDetailFragment entryOrdersDetailFragment;
    private EntryOrdersAdapter adapter = new EntryOrdersAdapter();
    private int select_position = 0;
    private List<EntryOrders> list;

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
            transaction.replace(R.id.framelayout, entryOrdersDetailFragment, "tag_entry_orders").commit();
        }

        rvEntryOrders.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvEntryOrders.setAdapter(adapter);

        //分割线
        DividerItemDecoration verticalDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        verticalDecoration.setDrawable(getResources().getDrawable(R.drawable.bg_item_decoration_vertical_order_history));
        rvEntryOrders.addItemDecoration(verticalDecoration);

        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication);
        String json = sp.getString(Constans.KEY_ENTRY_ORDERS_LIST, "");

        list = GsonUtils.getInstance().getGson().fromJson(json, new TypeToken<List<EntryOrders>>() {
        }.getType());
        adapter.setNewData(list);
        adapter.setItemClick();

        setEmpty();
        setListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (entryOrdersDetailFragment != null) {
            entryOrdersDetailFragment.showEntryOrders(adapter.getData().get(0));
        }
    }

    /* 设置adapter数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        adapter.setEmptyView(emptyView);
    }

    private void setListener() {
        adapter.setOnItemClickListener(new EntryOrdersAdapter.OnItemClickListener() {
            @Override
            public void onItemClck(int position) {
                select_position = position;
                entryOrdersDetailFragment.showEntryOrders(adapter.getData().get(position));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    //作废
    public void toInvaild() {
        list.remove(select_position);

        SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication).edit();
        if (list.size() == 0) {
            editor.remove(Constans.KEY_ENTRY_ORDERS_LIST).apply();
            if (getActivity() != null) {
                getActivity().finish();
            }
            return;
        }
        editor.putString(Constans.KEY_ENTRY_ORDERS_LIST, GsonUtils.getInstance().getGson().toJson(list)).apply();

        adapter.setItemClick();
        entryOrdersDetailFragment.showEntryOrders(adapter.getData().get(0));
        select_position = 0;
    }

    //选择
    public void toChoose() {
        EventUtils.post(Events.ENTRY_ORDERS_VALUE, list.get(select_position));

        list.remove(select_position);

        SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication).edit();
        if (list.size() == 0) {
            editor.remove(Constans.KEY_ENTRY_ORDERS_LIST).apply();
        } else {
            editor.putString(Constans.KEY_ENTRY_ORDERS_LIST, GsonUtils.getInstance().getGson().toJson(list)).apply();
        }

        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}

package com.easygo.cashier.module.handover;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.easygo.cashier.R;
import com.easygo.cashier.adapter.HandoverSaleListAdapter;
import com.easygo.cashier.bean.HandoverSaleResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 交接班销售列表
 */
public class HandoverSaleListView extends FrameLayout {

    private static final String TAG = "HandoverSaleListView";
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    private View mView;
    private Unbinder unbinder;


    private HandoverSaleListAdapter mHandoverSaleListAdapter;

    private List<HandoverSaleResponse> mData;


    public HandoverSaleListView(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_handover_sale_list, this, true);
        unbinder = ButterKnife.bind(this, mView);

        rvGoods.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mHandoverSaleListAdapter = new HandoverSaleListAdapter();
        rvGoods.setAdapter(mHandoverSaleListAdapter);

        mData = new ArrayList<>();
        mHandoverSaleListAdapter.setNewData(mData);


    }


    public static HandoverSaleListView create(Context context) {
        return new HandoverSaleListView(context);
    }


    /**
     * 设置数据
     */
    public void setData(List<HandoverSaleResponse> data) {
        this.mData = data;
        mHandoverSaleListAdapter.setNewData(mData);

    }

    public void release() {
        if (unbinder != null)
            unbinder.unbind();
    }

}

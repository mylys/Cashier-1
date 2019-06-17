package com.easygo.cashier.module.weighing;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppFragment;
import com.easygo.cashier.bean.WeightListBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe：
 * @Date：2019-06-14
 */
public class WeighingFragment extends BaseAppFragment {
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    Unbinder unbinder;

    private BaseQuickAdapter<WeightListBean, BaseViewHolder> adapter;

    @Override
    protected int getLayout() {
        return R.layout.fragment_weight;
    }

    @Override
    protected void initView() {
        adapter = new BaseQuickAdapter<WeightListBean, BaseViewHolder>(R.layout.item_quick_choose_list) {
            @Override
            protected void convert(BaseViewHolder helper, WeightListBean item) {
                helper.setText(R.id.tv_good_price, item.getPrice() + "/斤");
            }
        };
        rvGoods.setLayoutManager(new GridLayoutManager(getActivity(), 4, LinearLayoutManager.VERTICAL, false));
        rvGoods.setAdapter(adapter);
        setEmpty();
    }

    @Override
    protected void setListener() {

    }

    /* 设置adapter空数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        TextView textView = emptyView.findViewById(R.id.text);
        textView.setText("暂无商品");
        adapter.setEmptyView(emptyView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

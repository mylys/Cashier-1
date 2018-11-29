package com.easygo.cashier.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.R;
import com.easygo.cashier.activity.MainActivity;
import com.easygo.cashier.activity.Test;
import com.easygo.cashier.adapter.GoodsAdapter;
import com.easygo.cashier.bean.GoodsInfo;
import com.niubility.library.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GoodsFragment extends BaseFragment {


    @BindView(R.id.tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    private Unbinder unbinder;

    public static GoodsFragment newInstance() {
        return new GoodsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_goods, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rvGoods.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        final GoodsAdapter goodsAdapter = new GoodsAdapter();
        rvGoods.setAdapter(goodsAdapter);

        goodsAdapter.setNewData(Test.getGoodsInfos());

        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsInfo goodsInfo = (GoodsInfo) (adapter.getData().get(position));
                Toast.makeText(getContext(), goodsInfo.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null)
            unbinder.unbind();
    }

    @OnClick({R.id.cl_no_barcode_goods, R.id.cl_pop_money_box, R.id.btn_clear, R.id.btn_settlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_no_barcode_goods://无码商品
                break;
            case R.id.cl_pop_money_box://弹出钱箱
                break;
            case R.id.btn_clear://清空
                break;
            case R.id.btn_settlement://收银
                MainActivity activity = (MainActivity) getActivity();
                if(activity != null)
                    activity.toCashierActivity();
                break;
        }
    }
}

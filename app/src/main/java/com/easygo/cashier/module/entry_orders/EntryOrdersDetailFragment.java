package com.easygo.cashier.module.entry_orders;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.adapter.EntryOrdersGoodsAdapter;
import com.easygo.cashier.bean.EntryOrders;
import com.easygo.cashier.widget.dialog.GeneraDialog;
import com.niubility.library.base.BaseFragment;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Describe：
 * @author：hgeson
 * @date：2019-01-03
 */
public class EntryOrdersDetailFragment extends BaseFragment {
    @BindView(R.id.tv_entry_orders_time)
    TextView tvEntryOrdersTime;
    @BindView(R.id.tv_note)
    TextView tvNote;
    @BindView(R.id.tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    Unbinder unbinder;

    private int position = -1;
    private EntryOrdersGoodsAdapter adapter = new EntryOrdersGoodsAdapter();

    private DecimalFormat df = new DecimalFormat("0.00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entry_orders_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        rvGoods.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvGoods.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rvGoods.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick({R.id.btn_invaild, R.id.btn_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_invaild:
                GeneraDialog dialog = GeneraDialog.getInstance("确认作废该笔订单？", "取消", "确定");
                dialog.showCenter(getActivity());
                dialog.setOnDialogClickListener(new GeneraDialog.OnDialogClickListener() {
                    @Override
                    public void onSubmit() {
                        ((EntryOrdersActivity) getActivity()).toInvaild();
                    }
                });
                break;
            case R.id.btn_choose:
                ((EntryOrdersActivity)getActivity()).toChoose();
                break;
        }
    }

    public void showEntryOrders(EntryOrders orders) {
        tvNote.setText(getActivity().getResources().getString(R.string.text_note) + orders.getEntry_orders_note());
        tvEntryOrdersTime.setText(orders.getEntry_orders_time());
        tvGoodsCount.setText("共" + orders.getEntry_orders_total_number() + "件");
        tvTotalPrice.setText("总额：" + df.format(Float.valueOf(orders.getEntry_orders_total_price())));

        if (adapter != null) {
            adapter.setNewData(orders.getGoodsEntityList());
        }
    }
}

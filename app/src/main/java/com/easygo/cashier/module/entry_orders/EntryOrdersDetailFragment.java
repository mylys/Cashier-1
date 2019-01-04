package com.easygo.cashier.module.entry_orders;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.widget.GeneraDialog;
import com.niubility.library.base.BaseApplication;
import com.niubility.library.base.BaseFragment;
import com.niubility.library.constants.Constans;
import com.niubility.library.utils.SharedPreferencesUtils;

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

    @SuppressLint("SetTextI18n")
    private void init() {
        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication);
        tvEntryOrdersTime.setText(sp.getString(Constans.KEY_ENTRY_ORDERS_TIME, ""));
        tvNote.setText(getResources().getString(R.string.text_note) + sp.getString(Constans.KEY_ENTRY_ORDERS_NOTE, ""));
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

                    }
                });
                break;
            case R.id.btn_choose:
                if (getActivity() != null) {
                    getActivity().finish();
                }
                break;
        }
    }
}

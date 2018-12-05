package com.easygo.cashier.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.easygo.cashier.R;
import com.easygo.cashier.activity.MainActivity;
import com.easygo.cashier.activity.RefundActivity;
import com.easygo.cashier.activity.Test;
import com.easygo.cashier.adapter.GoodsAdapter;
import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.widget.NoGoodsDialog;
import com.niubility.library.base.BaseFragment;
import com.niubility.library.utils.ToastUtils;

import androidx.constraintlayout.widget.ConstraintLayout;
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
    @BindView(R.id.btn_settlement)
    Button btnSettlement;
    @BindView(R.id.cl_pop_money_box)
    ConstraintLayout clPopMoneyBox;
    @BindView(R.id.line1)
    View line1;

    private Unbinder unbinder;

    public static final int TYPE_GOODS = 0;
    public static final int TYPE_REFUND = 1;
    public static final String KEY_TYPE = "key_type";
    private int mType = TYPE_GOODS;

    public static GoodsFragment newInstance() {
        return newInstance(null);
    }

    public static GoodsFragment newInstance(Bundle bundle) {
        GoodsFragment goodsFragment = new GoodsFragment();
        if (bundle != null)
            goodsFragment.setArguments(bundle);
        return goodsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_goods, container, false);
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

        Bundle data = getArguments();
        if (data != null) {
            mType = data.getInt(KEY_TYPE, TYPE_GOODS);

        }
        switch (mType) {
            case TYPE_GOODS:
                clPopMoneyBox.setVisibility(View.VISIBLE);
                line1.setVisibility(View.VISIBLE);
                btnSettlement.setText("收银：  ￥6.00");
                break;
            case TYPE_REFUND:
                clPopMoneyBox.setVisibility(View.GONE);
                line1.setVisibility(View.GONE);
                btnSettlement.setText("退款：  ￥6.00");
                break;
        }

        btnSettlement.setText(mType == TYPE_GOODS ? "收银：  ￥6.00" : "退款：  ￥6.00");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @OnClick({R.id.cl_no_barcode_goods, R.id.cl_pop_money_box, R.id.btn_clear, R.id.btn_settlement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_no_barcode_goods://无码商品
                final NoGoodsDialog dialog = new NoGoodsDialog();
                dialog.showCenter(getActivity());
                dialog.setOnDialogClickListener(new NoGoodsDialog.OnDialogClickListener() {
                    @Override
                    public void onDialogClick(String content) {
                        if (TextUtils.isEmpty(content)) {
                            ToastUtils.showToast(getActivity(), "请输入金额");
                            return;
                        }
                        ToastUtils.showToast(getActivity(), content);
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.cl_pop_money_box://弹出钱箱
                break;
            case R.id.btn_clear://清空
                break;
            case R.id.btn_settlement://收银 or  退款
                switch (mType) {
                    case TYPE_GOODS:
                        MainActivity mainActivity = (MainActivity) getActivity();
                        if (mainActivity != null)
                            mainActivity.toCashierActivity();
                        break;
                    case TYPE_REFUND:
                        RefundActivity refundActivity = (RefundActivity) getActivity();
                        if (refundActivity != null)
                            refundActivity.toRefundFragment();
                        break;
                }

                break;
        }
    }
}

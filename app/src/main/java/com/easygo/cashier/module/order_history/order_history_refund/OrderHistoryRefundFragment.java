package com.easygo.cashier.module.order_history.order_history_refund;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.easygo.cashier.Configs;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.OrderHistoryRefundAdapter;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.bean.RequsetBody;
import com.easygo.cashier.widget.ConfirmDialog;
import com.easygo.cashier.widget.MySearchView;
import com.easygo.cashier.widget.PayWayView;
import com.niubility.library.base.BaseMvpFragment;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.GsonUtils;
import com.niubility.library.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OrderHistoryRefundFragment extends BaseMvpFragment<OrderHistoryRefundContract.IView, OrderHistoryRefundPresenter> implements OrderHistoryRefundContract.IView {

    @BindView(R.id.search_view)
    MySearchView searchView;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.tv_order_cashier)
    TextView tvOrderCashier;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tv_pay_type)
    TextView tvPayType;
    @BindView(R.id.tv_refund_cash_num)
    TextView tvRefundcashNum;
    @BindView(R.id.edit_refund_case_price)
    EditText editRefundcashPrice;
    Unbinder unbinder;

    private ConfirmDialog confirmDialog;
    private OrderHistoryRefundAdapter adapter;
    private ArrayList<RequsetBody.GoodsList> goodsLists = new ArrayList<>();

    private boolean isRefund = false;
    private String order_number = "";
    private String pay_type = "";
    private String refund_pay_type = "";
    private int total_price = 0;
    private int pay_way = PayWayView.WAY_CASH;

    public static OrderHistoryRefundFragment getInstance(Bundle bundle) {
        OrderHistoryRefundFragment fragment = new OrderHistoryRefundFragment();
        if (bundle != null)
            fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected OrderHistoryRefundPresenter createPresenter() {
        return new OrderHistoryRefundPresenter();
    }

    @Override
    protected OrderHistoryRefundContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_history_refund;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void init() {
        List<OrderHistorysInfo.ListBean> data = null;
        if (getArguments() != null) {
            data = getArguments().getParcelableArrayList("data");
            order_number = getArguments().getString("order_number");
            pay_type = getArguments().getString("pay_type");
            total_price = getArguments().getInt("total_price");
        }
        tvPayType.setText(getResources().getString(R.string.text_pay_type) + pay_type + getResources().getString(R.string.text_pay));
        tvOrderNumber.setText(getResources().getString(R.string.text_order_number) + order_number);
        tvOrderCashier.setText(getResources().getString(R.string.text_cashier) + Configs.admin_name);

        if (pay_type.equals(getResources().getString(R.string.pay_wechat))) {
            refund_pay_type = "wechat";
            pay_way = PayWayView.WAY_WECHAT;
        } else if (pay_type.equals(getResources().getString(R.string.pay_cash))) {
            refund_pay_type = "cash";
            pay_way = PayWayView.WAY_CASH;
        } else if (pay_type.equals(getResources().getString(R.string.pay_alipay))) {
            refund_pay_type = "alipay";
            pay_way = PayWayView.WAY_ALIPAY;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        adapter = new OrderHistoryRefundAdapter();
        recyclerView.setAdapter(adapter);
        if (data != null) {
            for (OrderHistorysInfo.ListBean bean : data) {
                GoodsRefundInfo info = new GoodsRefundInfo();
                info.setProduct_name(bean.getG_sku_name());
                info.setProduct_price(bean.getSell_price());
                info.setProduct_subtotal(bean.getMoney());
                info.setProduct_preferential("0");
                info.setProduct_num(bean.getCount());
                info.setS_sku_id(bean.getS_sku_id());
                info.setRefund_num("1");
                info.setRefund_subtotal(bean.getSell_price());
                info.setSelect(false);
                adapter.addData(info);
            }
        }
        tvRefundcashNum.setText("共退货" + adapter.getTotalNum() + "件,退款金额：");
        setListener();
    }

    private void setListener() {
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果点击checkbox，则全选，反之全否
                adapter.setClick(checkbox.isChecked());
                tvRefundcashNum.setText("共退货" + adapter.getTotalNum() + "件,退款金额：");
                editRefundcashPrice.setText(adapter.getTotalPrice());
            }
        });
        adapter.setOnItemClickListener(new OrderHistoryRefundAdapter.OnItemClickListener() {
            @Override
            public void onClick(boolean isClick) {
                //点击Item判断是否全选，反之checkbox关闭
                checkbox.setChecked(isClick);
            }

            @Override
            public void onListener() {
                tvRefundcashNum.setText(getResources().getString(R.string.text_total_refund_product) +
                        adapter.getTotalNum() + getResources().getString(R.string.text_total_refund_price));
                editRefundcashPrice.setText(adapter.getTotalPrice());
            }

            @Override
            public void onSelectBean(ArrayList<RequsetBody.GoodsList> lists) {
                goodsLists.clear();
                goodsLists.addAll(lists);
            }
        });

        editRefundcashPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() != 0){
                    if (s.toString().substring(0,1).equals(".")){
                        editRefundcashPrice.setText("");
                    }
                }
            }
        });
    }

    @OnClick(R.id.btn_refund)
    public void onViewClicked() {
        if (TextUtils.isEmpty(editRefundcashPrice.getText().toString())){
            return;
        }
        float price = Float.parseFloat(editRefundcashPrice.getText().toString());
        if (price == 0) {
            ToastUtils.showToast(getActivity(), "退款金额不能为0");
            return;
        }
        int edit_price;
        if (editRefundcashPrice.getText().toString().contains(".")) {
            DecimalFormat df = new DecimalFormat("0.00");
            double db = Double.parseDouble(editRefundcashPrice.getText().toString());
            edit_price = Integer.parseInt(df.format(db).replace(".", ""));
        } else {
            edit_price = Integer.parseInt(editRefundcashPrice.getText().toString() + "00");
        }
        if (edit_price > total_price) {
            ToastUtils.showToast(getActivity(), "输入金额不能大于商品总额");
            return;
        }
        Bundle bundle = ConfirmDialog.getDataBundle(adapter.getTotalNum() + "", price, pay_way, false, "退货", "实退");
        confirmDialog = new ConfirmDialog();
        confirmDialog.setArguments(bundle);
        confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListenr() {
            @Override
            public void onConfirm() {
                onCommitOrder();
            }
        });
        confirmDialog.show(getActivity().getSupportFragmentManager(), "tag_tip_dialog");
    }

    public void onCommitOrder() {
        int price = Integer.parseInt(editRefundcashPrice.getText().toString().replace(".", ""));
        RequsetBody body = new RequsetBody(order_number, Configs.shop_sn, price, refund_pay_type, goodsLists);
        String json = GsonUtils.getInstance().getGson().toJson(body);
        mPresenter.post(json);
    }

    @Override
    public void getHistoryRefundSuccess(String message) {
        ToastUtils.showToast(getActivity(), message);
    }

    @Override
    public void getHistorfRefundFailed(Map<String, Object> map) {
        if (HttpExceptionEngine.isBussinessError(map)) {
            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
            showToast("错误信息:" + err_msg);
        }
    }
}
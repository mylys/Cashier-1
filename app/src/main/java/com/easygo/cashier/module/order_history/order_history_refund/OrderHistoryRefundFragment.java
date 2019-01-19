package com.easygo.cashier.module.order_history.order_history_refund;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easygo.cashier.Configs;
import com.easygo.cashier.R;
import com.easygo.cashier.TestUtils;
import com.easygo.cashier.adapter.OrderHistoryRefundAdapter;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.bean.RequsetBody;
import com.easygo.cashier.module.order_history.OrderHistoryActivity;
import com.easygo.cashier.module.order_history.OrderHistoryFragment;
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.widget.ConfirmDialog;
import com.easygo.cashier.widget.GeneraListDialog;
import com.easygo.cashier.widget.MySearchView;
import com.easygo.cashier.widget.PayWayView;
import com.niubility.library.base.BaseMvpFragment;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.GsonUtils;
import com.niubility.library.utils.ToastUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    @BindView(R.id.child)
    LinearLayout child;
    @BindView(R.id.rl_view)
    RelativeLayout parent;
    @BindView(R.id.btn_refund)
    Button btnRefund;
    Unbinder unbinder;

    private ConfirmDialog confirmDialog;
    private OrderHistoryRefundAdapter adapter;

    private ArrayList<GoodsRefundInfo> infoList = new ArrayList<>();
    private TestUtils textUtils = new TestUtils();

    private String order_number = "";
    private String pay_type = "";
    private String refund_pay_type = "";
    private String real_name = "";
    private String order_no_number = "";
    private int total_price = 0;
    private int refund_status = 0;
    private int have_refund = 0;
    private int pay_way = PayWayView.WAY_CASH;

    private List<OrderHistorysInfo.ActivitiesBean> activities;

    private  DecimalFormat df = new DecimalFormat("#0.00");

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
            activities = getArguments().getParcelableArrayList("activities");
            order_number = getArguments().getString("order_number");
            order_no_number = getArguments().getString("order_no_number");
            pay_type = getArguments().getString("pay_type");
            real_name = getArguments().getString("real_name");
            total_price = getArguments().getInt("total_price");
            refund_status = getArguments().getInt("refund_status");
            have_refund = getArguments().getInt("have_refund");
        }
        tvPayType.setText(getResources().getString(R.string.text_pay_type) + pay_type + getResources().getString(R.string.text_pay));
        tvOrderNumber.setText(getResources().getString(R.string.text_order_number) + order_no_number);
        tvOrderCashier.setText(getResources().getString(R.string.text_cashier) + real_name);

        if (pay_type.equals(getResources().getString(R.string.pay_wechat))) {
            refund_pay_type = "wechat";
            pay_way = PayWayView.WAY_WECHAT;
        } else if (pay_type.equals(getResources().getString(R.string.pay_cash))) {
            refund_pay_type = "cash";
            pay_way = PayWayView.WAY_CASH;
        } else if (pay_type.equals(getResources().getString(R.string.pay_alipay))) {
            refund_pay_type = "alipay";
            pay_way = PayWayView.WAY_ALIPAY;
        } else if (pay_type.equals(getResources().getString(R.string.pay_wallet))) {
            refund_pay_type = "member_wallet";
            pay_way = PayWayView.WAY_MEMBER;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        adapter = new OrderHistoryRefundAdapter();
        recyclerView.setAdapter(adapter);
        setEmpty();

        if (data != null) {
            for (OrderHistorysInfo.ListBean bean : data) {
                GoodsRefundInfo info = new GoodsRefundInfo();
                info.setProduct_name(bean.getG_sku_name());
                info.setProduct_price(bean.getSell_price());
                info.setProduct_subtotal(df.format(bean.getMoney()));
                info.setProduct_preferential(bean.getDiscount());
                info.setProduct_num(bean.getCount());
                info.setS_sku_id(bean.getS_sku_id());
                info.setRefund_num("1");
                info.setRefund_subtotal(bean.getSell_price());
                info.setSelect(false);
                info.setType(bean.getType());
                info.setIdentity(bean.getIdentity());
                info.setRefund(bean.getRefund());
                adapter.addData(info);
                infoList.add(info);
            }
        }
        if (adapter.getTotalRefund()) {
            btnRefund.setEnabled(false);
            btnRefund.setBackgroundResource(R.drawable.bg_btn_enable_commit);
            editRefundcashPrice.setInputType(InputType.TYPE_NULL);
        }
        tvRefundcashNum.setText("共退货" + adapter.getTotalNum() + "件，退款金额：￥");
        setListener();
    }

    /* 设置adapter数据 */
    private void setEmpty() {
        View emptyView = getLayoutInflater().inflate(R.layout.item_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        adapter.setEmptyView(emptyView);
    }

    private void setListener() {
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果点击checkbox，则全选，反之全否
                boolean checked = checkbox.isChecked();
                adapter.setClick(checked);
                tvRefundcashNum.setText("共退货" + adapter.getTotalNum() + "件，退款金额：￥");

                if(!checked) {
                    editRefundcashPrice.setText("0.00");
                } else {
                    float totalCoupon = adapter.getTotalCoupon();
                    if (totalCoupon != 0) {
                        editRefundcashPrice.setText(df.format(Float.valueOf(adapter.getTotalPrice()) - totalCoupon));
                    } else {
                        float discount = 0f;
                        if (activities != null) {
                            int size = activities.size();
                            for (int i = 0; i < size; i++) {
                                discount += Float.valueOf(activities.get(i).getDiscount_money());
                            }
                        }
                        editRefundcashPrice.setText(df.format(Float.valueOf(adapter.getTotalPrice()) - discount));
                    }
                }
                editRefundcashPrice.setSelection(editRefundcashPrice.getText().toString().length());
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
                tvRefundcashNum.setText(getResources().getString(R.string.text_total_refund_product) + adapter.getTotalNum() + getResources().getString(R.string.text_total_refund_price));
                editRefundcashPrice.setText(adapter.getTotalPrice());
                editRefundcashPrice.setSelection(editRefundcashPrice.getText().toString().length());
            }
        });

        searchView.setOnSearchListenerClick(new MySearchView.OnSearhListenerClick() {
            @Override
            public void onSearch(String content) {
                if (content.length() == 0){
                    adapter.setNewData(infoList);
                    return;
                }
                ArrayList<GoodsRefundInfo> infos = new ArrayList<>();
                for (GoodsRefundInfo info : adapter.getData()) {
                    if (info.getProduct_name().contains(content)) {
                        infos.add(info);
                    }
                }
                if (infos.size() == 0) {
                    ToastUtils.showToast(getActivity(), R.string.search_null);
                    adapter.setNewData(infoList);
                } else {
                    adapter.setNewData(infos);
                }
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
                if (s.toString().length() != 0) {
                    if (s.toString().substring(0, 1).equals(".")) {
                        editRefundcashPrice.setText("");
                    }
                }
            }
        });

        editRefundcashPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }

                    searchView.setFocuable(true);
                    return true;
                }
                return false;
            }
        });

        editRefundcashPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textUtils.addSoftKeyBoardListener(parent, child);
                } else {
                    textUtils.removeSoftKeyBoardListener(parent);
                }
            }
        });
    }

    @OnClick(R.id.btn_refund)
    public void onViewClicked() {
        if(refund_status == 1 && have_refund == 0) {//订单有退过款，商品没有退过款
            //无退货 退过款
            showToast("此订单已退过款");
            return;
        }

        if (TextUtils.isEmpty(editRefundcashPrice.getText().toString())) {
            return;
        }
        float price = Float.parseFloat(editRefundcashPrice.getText().toString());
        if (price == 0) {
            ToastUtils.showToast(getActivity(), "退款金额不能为0");
            return;
        }
        float edit_price;
        if (editRefundcashPrice.getText().toString().contains(".")) {
            DecimalFormat df = new DecimalFormat("0.00");
            double db = Double.parseDouble(editRefundcashPrice.getText().toString());
            edit_price = Float.parseFloat(df.format(db).replace(".", ""));
        } else {
            edit_price = Float.parseFloat(editRefundcashPrice.getText().toString()) * 100;
        }
        if (edit_price > total_price) {
            ToastUtils.showToast(getActivity(), "输入金额不能大于商品总额");
            return;
        }
        Bundle bundle = ConfirmDialog.getDataBundle(adapter.getTotalNum() + "", price, pay_way, false, "退货：", "实退：");
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
        DecimalFormat df = new DecimalFormat("0.00");
        float price = Float.parseFloat(editRefundcashPrice.getText().toString());
        String format = df.format(price);
        int prices = Integer.parseInt(format.replace(".",""));
        RequsetBody body = new RequsetBody(order_number, Configs.shop_sn,prices , refund_pay_type, adapter.getList());
        String json = GsonUtils.getInstance().getGson().toJson(body);
        mPresenter.post(json);
        btnRefund.setEnabled(false);
    }

    @Override
    public void getHistoryRefundSuccess(String message) {
        btnRefund.setEnabled(true);
        printRefundInfo();

        if (pay_type.equals(getResources().getString(R.string.pay_cash))) {

            mPresenter.popTill(Configs.shop_sn, Configs.printer_sn);
        }
        checkbox.setChecked(false);
        adapter.setRefundInfo();
        if (adapter.getTotalRefund()) {
            btnRefund.setEnabled(false);
            btnRefund.setBackgroundResource(R.drawable.bg_btn_enable_commit);
            editRefundcashPrice.setInputType(InputType.TYPE_NULL);
        }
        editRefundcashPrice.setText("0.00");
        ToastUtils.showToast(getActivity(), message);
        if (getActivity() != null) {
            ((OrderHistoryActivity) getActivity()).toRefresh();
            ((OrderHistoryActivity) getActivity()).back();
        }
    }

    /**
     * 打印退款单
     */
    private void printRefundInfo() {

        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        sb.append(PrintHelper.CB_left).append("退款单").append(PrintHelper.CB_right).append(PrintHelper.BR)
                .append("订单号：").append(order_no_number).append(PrintHelper.BR)
                .append("收银员：").append(real_name).append(PrintHelper.BR)
                .append("时间：").append(sdf.format(new Date())).append(PrintHelper.BR)
                .append("--------------------------------").append(PrintHelper.BR)
                .append("品名  ").append("单价  ").append("折扣  ").append("数量/重量  ").append("小计  ")
                .append(PrintHelper.BR);


        int size = infoList.size();
        for (int i = 0; i < size; i++) {
            GoodsRefundInfo goodsRefundInfo = infoList.get(i);

            boolean select = goodsRefundInfo.isSelect();
            if(!select) {
                continue;
            }

            sb.append(i+1).append(".")
                    .append(goodsRefundInfo.getProduct_name()).append("   ").append(PrintHelper.BR)
                    .append("    ")
                    .append(goodsRefundInfo.getProduct_price()).append("   ").append(PrintHelper.BR)
                    .append("1.00    ")
                    .append(goodsRefundInfo.getRefund_num()).append("   ")
                    .append(goodsRefundInfo.getRefund_subtotal()).append("   ").append(PrintHelper.BR);
        }
        sb.append("--------------------------------").append(PrintHelper.BR)
                .append("总数量：").append(adapter.getTotalNum()).append(PrintHelper.BR)
                .append("退款方式：").append(pay_type).append(PrintHelper.BR)
                .append("退款金额：").append(Float.parseFloat(editRefundcashPrice.getText().toString())).append(PrintHelper.BR);


        Activity activity = getActivity();
        if (activity != null) {
            OrderHistoryActivity orderHistoryActivity = (OrderHistoryActivity) activity;

            orderHistoryActivity.printRefundInfo(sb.toString());
        }
    }

    @Override
    public void getHistorfRefundFailed(Map<String, Object> map) {
        btnRefund.setEnabled(true);
//        if (HttpExceptionEngine.isBussinessError(map)) {
            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
            showToast("错误信息:" + err_msg);
//        }
    }

    @Override
    public void popTillSuccess() {
        Log.i("TAG", "弹出钱箱成功");
    }

    @Override
    public void popTillFailed(Map<String, Object> map) {
        showToast("弹出钱箱失败");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}

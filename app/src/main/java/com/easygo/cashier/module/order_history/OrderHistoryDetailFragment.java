package com.easygo.cashier.module.order_history;

import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.easygo.cashier.Configs;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.OrderHistoryGoodsAdapter;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.printer.local.PrinterHelpter;
import com.easygo.cashier.printer.local.PrinterUtils;
import com.easygo.cashier.printer.local.obj.OrderHistoryGoodsListPrintObj;
import com.niubility.library.base.BaseFragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 历史订单详情页面
 */
public class OrderHistoryDetailFragment extends BaseFragment {

    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;
    @BindView(R.id.tv_cashier_account)
    TextView tvCashierAcount;
    @BindView(R.id.tv_return_of_goods_count)
    TextView tvReturnOfGoodsCount;
    @BindView(R.id.tv_refund)
    TextView tvRefund;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.tv_receipts)
    TextView tvReceipts;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.tv_goods_count)
    TextView tvGoodsCount;
    @BindView(R.id.tv_receivable)
    TextView tvReceivable;
    @BindView(R.id.tv_buyer)
    TextView tvBuyer;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.tv_receivable_text)
    TextView tvReceivableText;
    @BindView(R.id.tv_receipts_text)
    TextView tvReceiptsText;
    @BindView(R.id.tv_refund_text)
    TextView tvRefundText;
    @BindView(R.id.btn_refund)
    Button btnRefund;
    @BindView(R.id.btn_print)
    Button btnPrint;
    Unbinder unbinder;

    /* 获取支付方式 */
    private String payType = "";
    /* 获取订单长单号，用于退款上传 */
    private String order_number = "";
    /* 获取收银员真实姓名 */
    private String order_real_name = "";
    /* 获取订单短单号 */
    private String order_no_number = "";

    private int total_price = 0;
    private float real_pay = 0;
    private DecimalFormat df = new DecimalFormat("0.00");

    /**
     * 商品数据
     */
    private OrderHistoryGoodsAdapter orderHistoryGoodsAdapter;
    private OrderHistorysInfo mOrderHistorysInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void init() {
        if (Configs.getRole(Configs.menus[5]) == 0) {
            btnRefund.setVisibility(View.GONE);
        }
        rvGoods.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvGoods.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        orderHistoryGoodsAdapter = new OrderHistoryGoodsAdapter();
        rvGoods.setAdapter(orderHistoryGoodsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @OnClick({R.id.btn_refund, R.id.btn_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_refund:
                if (orderHistoryGoodsAdapter.getItemCount() == 0){
                    showToast("订单没有商品");
                    return;
                } else if(mOrderHistorysInfo.getRefund_status() == 1
                        && mOrderHistorysInfo.getHave_refund() == 0) {//订单有退过款，商品没有退过款
                    //无退货 退过款
                    showToast("此订单已退过款");
                    return;
                } else if(orderHistoryGoodsAdapter.getRefundSize() == orderHistoryGoodsAdapter.getItemCount()) {
                    //所有商品都退过款
                    showToast("此订单中所有商品已退过款");
                    return;
                }

                if (getActivity() != null) {
                    Bundle bundle = new Bundle();
                    ArrayList<OrderHistorysInfo.ListBean> data = (ArrayList<OrderHistorysInfo.ListBean>) orderHistoryGoodsAdapter.getData();
                    bundle.putParcelableArrayList("data", data);
                    bundle.putString("pay_type", payType);
                    bundle.putFloat("gift_card_pay", mOrderHistorysInfo.getGift_card_pay());
                    bundle.putInt("total_price", total_price);
                    bundle.putFloat("real_pay", real_pay);
                    bundle.putString("order_number", order_number);
                    bundle.putString("real_name", order_real_name);
                    bundle.putString("order_no_number",order_no_number);
                    bundle.putInt("refund_status", mOrderHistorysInfo.getRefund_status());
                    bundle.putInt("have_refund", mOrderHistorysInfo.getHave_refund());
                    bundle.putFloat("total_discount", getAllDiscount());
                    ((OrderHistoryActivity) getActivity()).toOrderHistoryRefundFragment(bundle);
                }
                break;
            case R.id.btn_print:
                if(mOrderHistorysInfo == null) {
                    return;
                }
                printOrderHistoryDetail();
                printOrderHistoryDetailLocal();

                break;
        }
    }

    /**
     * 打印历史订单详情
     */
    private void printOrderHistoryDetail() {
        List<OrderHistorysInfo.ListBean> data = mOrderHistorysInfo.getList();
        int size = data.size();
        if (size <= 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

        sb.append(PrintHelper.CB_left).append(Configs.shop_name).append(PrintHelper.CB_right).append(PrintHelper.BR)
                .append("订单号：").append(tvOrderNo.getText().toString()).append(PrintHelper.BR)
                .append("时间：").append(sdf.format(new Date())).append(PrintHelper.BR)
                .append("收银员：").append(tvCashierAcount.getText().toString()).append(PrintHelper.BR)
                .append("--------------------------------").append(PrintHelper.BR)
                .append("品名  ").append("单价  ").append("优惠  ").append("数量/重量  ").append("小计  ")
                .append(PrintHelper.BR);

        int count = 0;
        float total_discount = 0f;
        float discount = 0f;
        for (int i = 0; i < size; i++) {
            OrderHistorysInfo.ListBean listBean = data.get(i);

            int type = listBean.getType();
            count += listBean.getQuantity();
            discount = Float.valueOf(listBean.getDiscount());
            total_discount += discount;

            sb.append(i + 1).append(".")
                    .append(listBean.getG_sku_name()).append("   ").append(PrintHelper.BR)
                    .append("     ")
                    .append(listBean.getUnit_price()).append("   ")
                    .append(listBean.getDiscount()).append("   ")
                    .append(type == 1 ? listBean.getCount() + listBean.getG_u_symbol() : listBean.getCount()).append("   ")
                    .append(df.format(listBean.getMoney())).append(PrintHelper.BR);
        }

        DecimalFormat df = new DecimalFormat("0.00");

        sb.append("--------------------------------").append(PrintHelper.BR)
                .append("总数量：").append(count).append(PrintHelper.BR)
                .append("原价：").append(mOrderHistorysInfo.getTotal_money()).append("元").append(PrintHelper.BR)
                .append("优惠：").append(df.format(total_discount)).append("元").append(PrintHelper.BR)
                .append("总金额：").append(mOrderHistorysInfo.getReal_pay()).append("元").append(PrintHelper.BR)
                .append("支付方式：").append(payType).append(PrintHelper.BR)
                .append("实收：").append(mOrderHistorysInfo.getBuyer_pay()).append("元").append(PrintHelper.BR)
                .append("找零：").append(mOrderHistorysInfo.getChange_money()).append("元").append(PrintHelper.BR)
                .append("退款：").append(mOrderHistorysInfo.getRefund_fee() != null ?
                mOrderHistorysInfo.getRefund_fee() : "0.00").append("元").append(PrintHelper.BR);

        Fragment parentFragment = getParentFragment();
        if (parentFragment != null) {
            OrderHistoryFragment fragment = (OrderHistoryFragment) parentFragment;

            fragment.print(sb.toString(), 0);
        }

    }

    /**
     * 打印历史订单详情
     */
    private void printOrderHistoryDetailLocal() {
        List<OrderHistorysInfo.ListBean> data = mOrderHistorysInfo.getList();
        int size = data.size();
        if (size <= 0) {
            return;
        }

        OrderHistoryGoodsListPrintObj obj = new OrderHistoryGoodsListPrintObj();
        obj.shop_name = Configs.shop_name;
        obj.order_no = tvOrderNo.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        obj.time = sdf.format(new Date());
        obj.admin_name = tvCashierAcount.getText().toString();
        obj.data = mOrderHistorysInfo;
        obj.pay_type = payType;

        PrinterUtils.getInstance().print(PrinterHelpter.orderHistroyGoodsList(obj));
    }

    public void showOrderHistory(OrderHistorysInfo orderHistoryInfo) {

        this.mOrderHistorysInfo = orderHistoryInfo;
        List<OrderHistorysInfo.ActivitiesBean> activities = orderHistoryInfo.getActivities();

        boolean have_shop_promotion = activities != null && activities.size() > 0;

        orderHistoryGoodsAdapter.setHavePromotion(have_shop_promotion);
        orderHistoryGoodsAdapter.setNewData(orderHistoryInfo.getList());
        tvOrderNo.setText(orderHistoryInfo.getTrade_no());
        tvCashierAcount.setText(orderHistoryInfo.getCashier_id() == 0? "": String.valueOf(orderHistoryInfo.getCashier_id()));
        int size = orderHistoryInfo.getList().size();
        int count = 0;
        for (int i = 0; i < size; i++) {
            count += orderHistoryInfo.getList().get(i).getQuantity();
        }
        tvGoodsCount.setText("共" + count + "件");
        tvReceipts.setText("实付：￥" + orderHistoryInfo.getBuyer_pay());

        tvReceivable.setText("应收：￥" + orderHistoryInfo.getReal_pay());

        //临时整单促销
        String cashier_discount = mOrderHistorysInfo.getCashier_discount();
        boolean has_temp_order_promotion = !TextUtils.isEmpty(cashier_discount) && Float.parseFloat(cashier_discount) > 0;
        if(has_temp_order_promotion) {
            orderHistoryGoodsAdapter.setHavePromotion(true);
        }

        float allDiscount = getAllDiscount();
        if(allDiscount > 0) {
            tvReceivableText.setText("总额：￥" + orderHistoryInfo.getTotal_money()
                    + " - 优惠：￥" + df.format(allDiscount) + " = ");
        } else {
            tvReceivableText.setText("");
        }

        order_real_name = orderHistoryInfo.getCashier_id() == 0? "": String.valueOf(orderHistoryInfo.getCashier_id());
        order_number = orderHistoryInfo.getTrade_num();
        order_no_number = orderHistoryInfo.getTrade_no();
        total_price = Integer.parseInt(orderHistoryInfo.getTotal_money().replace(".", ""));
        real_pay = Float.valueOf(orderHistoryInfo.getReal_pay());

        float gift_card_pay = orderHistoryInfo.getGift_card_pay();
        String pay_text = "";
        Resources res = getResources();
        double change_money = Double.parseDouble(orderHistoryInfo.getChange_money());
        double buyer_price = Double.parseDouble(orderHistoryInfo.getBuyer_pay());
        double total_price = buyer_price + change_money;
        String total_buyer_price = df.format(total_price);
        String change_price = "0.00";
        if (change_money != 0) {
            change_price = df.format(change_money);
        }
        switch (orderHistoryInfo.getPay_type()) {
            case 1:
                payType = getResources().getString(R.string.pay_wechat);
                break;
            case 2:
                payType = getResources().getString(R.string.pay_wechat_cash);
                break;
            case 3:
                payType = getResources().getString(R.string.pay_cash);
                break;
            case 4:
                payType = getResources().getString(R.string.pay_wallet);
                break;
            case 5:
                payType = getResources().getString(R.string.pay_wechat_wallet);
                break;
            case 6:
                payType = getResources().getString(R.string.pay_alipay);
                break;
            case 8:
                payType = getResources().getString(R.string.pay_bank_card);
                break;
            case 9:
                payType = getResources().getString(R.string.pay_gift_card);
                pay_text = res.getString(R.string.pay_gift_card) + "：￥" + df.format(gift_card_pay);
                break;
            case 10:
                payType = getResources().getString(R.string.pay_wechat_gift_card);
                pay_text = res.getString(R.string.pay_wechat) + "：￥" + df.format(real_pay - gift_card_pay)
                        + " + "
                        + res.getString(R.string.pay_gift_card) + "：￥" + df.format(gift_card_pay);
                break;
            case 11:
                payType = getResources().getString(R.string.pay_alipay_gift_card);
                pay_text = res.getString(R.string.pay_alipay) + "：￥" + df.format(real_pay - gift_card_pay)
                        + " + "
                        + res.getString(R.string.pay_gift_card) + "：￥" + df.format(gift_card_pay);
                break;
            case 12:
                payType = getResources().getString(R.string.pay_wallet_gift_card);
                pay_text = res.getString(R.string.pay_wallet) + "：￥" + df.format(real_pay - gift_card_pay)
                        + " + "
                        + res.getString(R.string.pay_gift_card) + "：￥" + df.format(gift_card_pay);
                break;
            case 13:
                payType = getResources().getString(R.string.pay_cash_gift_card);
                pay_text = res.getString(R.string.pay_cash) + "：￥" + df.format(total_price - gift_card_pay)
                        + " + "
                        + res.getString(R.string.pay_gift_card) + "：￥" + df.format(gift_card_pay);
                break;


        }

        if(gift_card_pay > 0) {
            tvReceiptsText.setText(pay_text + " - 找零：￥" + change_price + " = ");
        } else {
            tvReceiptsText.setText(payType + "：￥" + total_buyer_price + " - 找零：￥" + change_price + " = ");
        }

        /*付款*/
        tvBuyer.setVisibility(View.INVISIBLE);

        /*退款*/
        line3.setVisibility(View.INVISIBLE);
        tvRefund.setVisibility(View.INVISIBLE);
        tvRefundText.setVisibility(View.INVISIBLE);
        tvReturnOfGoodsCount.setVisibility(View.INVISIBLE);
        switch (orderHistoryInfo.getStatus()) {
            case 1:
                break;
            case 2://状态为已付款
                if(getString(R.string.pay_gift_card).equals(payType)) {//礼品卡支付时
                    tvBuyer.setVisibility(View.VISIBLE);
                    tvBuyer.setText(getResources().getString(R.string.text_gift_card_colon) + orderHistoryInfo.getGift_card_no());
                    return;
                }
                String buyer = orderHistoryInfo.getBuyer();
                if (buyer != null) {
                    tvBuyer.setVisibility(View.VISIBLE);
                    tvBuyer.setText(getResources().getString(R.string.text_buyer) + buyer);
                }
                break;
            case 3:
                break;
        }

        if (orderHistoryInfo.getRefund_status() != 0){
            has_refund(orderHistoryInfo, change_money, change_price);
        }
    }

    private void has_refund(OrderHistorysInfo orderHistoryInfo, double change_money, String change_price) {
        tvRefundText.setVisibility(View.INVISIBLE);
        tvReturnOfGoodsCount.setVisibility(View.VISIBLE);
        tvRefund.setVisibility(View.VISIBLE);
        line3.setVisibility(View.VISIBLE);

        tvRefund.setText("退款：￥" + orderHistoryInfo.getRefund_fee());
        tvReturnOfGoodsCount.setText("共退款" + orderHistoryGoodsAdapter.getRefundSize() + "件");
    }

    /** 获取所有优惠金额总额 */
    private float getAllDiscount() {
        float coupon = 0f;
        List<OrderHistorysInfo.ListBean> list = mOrderHistorysInfo.getList();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            coupon += Float.valueOf(list.get(i).getDiscount());
        }

        return coupon;
    }
}

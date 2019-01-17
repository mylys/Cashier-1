package com.easygo.cashier.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.bean.OrderHistorysInfo;

import java.text.DecimalFormat;


public class OrderHistoryGoodsAdapter extends BaseQuickAdapter<OrderHistorysInfo.ListBean, BaseViewHolder> {

    public OrderHistoryGoodsAdapter() {
        super(R.layout.item_order_history_detail_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderHistorysInfo.ListBean item) {
        DecimalFormat df = new DecimalFormat("#0.00");
        int count = item.getCount();
        String sell_price = item.getSell_price();

        helper.getView(R.id.tv_refund).setVisibility(item.getRefund() > 0 ? View.VISIBLE : View.GONE);
        helper.setText(R.id.tv_text_goods_name, item.getG_sku_name())
                .setText(R.id.tv_text_price, sell_price)
                .setText(R.id.tv_text_coupon, "0.00")
                .setText(R.id.tv_text_goods_count, item.getType() == 1 ? count + item.getG_u_symbol() : count + "")
                .setText(R.id.tv_text_subtotal, df.format(item.getMoney()));

        if (item.getType() == 1) {
            helper.setText(R.id.tv_refund, "全退");
        } else {
            helper.setText(R.id.tv_refund, item.getRefund() == item.getCount() ? "全退" : "退" + item.getRefund() + "件");
        }

    }

    public int getRefundSize() {
        int num = 0;
        for (OrderHistorysInfo.ListBean listBean : getData()) {
            if (listBean.getRefund() > 0) {
                num++;
            }
        }
        return num;
    }
}

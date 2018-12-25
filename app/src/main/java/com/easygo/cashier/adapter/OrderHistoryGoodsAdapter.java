package com.easygo.cashier.adapter;

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
        int quantity = item.getQuantity();
        String sell_price = item.getSell_price();
        helper.setText(R.id.tv_text_goods_name,item.getG_sku_name())
                .setText(R.id.tv_text_price, sell_price)
                .setText(R.id.tv_text_coupon,"0")
                .setText(R.id.tv_text_goods_count, quantity + "")
                .setText(R.id.tv_text_subtotal,item.getMoney());

        if(quantity != item.getCount()) {
            float price = Float.valueOf(sell_price);
            DecimalFormat df = new DecimalFormat("#0.00");
            helper.setText(R.id.tv_text_goods_count, quantity + "g")
                    .setText(R.id.tv_text_subtotal, df.format(quantity * price));
        }
    }
}

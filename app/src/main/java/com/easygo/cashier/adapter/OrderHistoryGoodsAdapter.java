package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.bean.OrderHistorysInfo;


public class OrderHistoryGoodsAdapter extends BaseQuickAdapter<OrderHistorysInfo.ListBean, BaseViewHolder> {

    public OrderHistoryGoodsAdapter() {
        super(R.layout.item_order_history_detail_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderHistorysInfo.ListBean item) {
        helper.setText(R.id.tv_text_goods_name,item.getG_sku_name())
                .setText(R.id.tv_text_price,item.getMoney())
                .setText(R.id.tv_text_coupon,"还没有优惠价")
                .setText(R.id.tv_text_goods_count,item.getCount() + "")
                .setText(R.id.tv_text_subtotal,item.getSell_price());
    }
}

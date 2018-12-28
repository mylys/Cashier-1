package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.HandoverSaleResponse;

public class HandoverSaleListAdapter extends BaseQuickAdapter<HandoverSaleResponse, BaseViewHolder> {

    public HandoverSaleListAdapter() {
        super(R.layout.item_handover_sale_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, HandoverSaleResponse item) {
        Object is_weigh = item.getIs_weigh();
        String count = is_weigh != null && ((int) is_weigh) == 1? item.getCount() + "g": item.getCount();

        helper.setText(R.id.tv_goods_name, item.getG_sku_name())
                .setText(R.id.tv_goods_classification, item.getG_c_name() == null? "": (String)item.getG_c_name())
                .setText(R.id.tv_price, item.getSell_price())
                .setText(R.id.tv_goods_count, count)
                .setText(R.id.tv_subtotal, String.valueOf(item.getMoney()));
    }
}

package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;

import java.text.DecimalFormat;

public class HandoverSaleListAdapter extends BaseQuickAdapter<HandoverSaleResponse, BaseViewHolder> {

    public HandoverSaleListAdapter() {
        super(R.layout.item_handover_sale_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, HandoverSaleResponse item) {
        String count;
        switch (item.getType()) {
            case GoodsResponse.type_normal:
            case GoodsResponse.type_processing:
            case GoodsResponse.type_no_code:
                count = String.valueOf(item.getCount());
                break;
            case GoodsResponse.type_weight:
                count = String.valueOf(item.getCount() + "g");
                break;
            default:
                count = String.valueOf(item.getCount());
                break;
        }

        helper.setText(R.id.tv_goods_name, item.getG_sku_name())
                .setText(R.id.tv_goods_classification, item.getG_c_name())
                .setText(R.id.tv_price, item.getSell_price())
                .setText(R.id.tv_goods_count, count)
                .setText(R.id.tv_subtotal, String.valueOf(item.getMoney()));
    }
}

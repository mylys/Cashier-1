package com.easygo.cashier.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.OrderHistorysInfo;

import java.text.DecimalFormat;


public class EntryOrdersGoodsAdapter extends BaseQuickAdapter<GoodsEntity<GoodsResponse>, BaseViewHolder> {

    public EntryOrdersGoodsAdapter() {
        super(R.layout.item_entry_orders_detail_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity<GoodsResponse> item) {
        GoodsResponse goods = item.getData();
        DecimalFormat df = new DecimalFormat("#0.00");

        int count = item.getCount();
        final String price = goods.getPrice();
        float subtotal = Float.valueOf(price) * count;

        helper.setText(R.id.tv_text_goods_name, goods.getG_sku_name())
                .setText(R.id.tv_text_price, price)
                .setText(R.id.tv_text_goods_count, goods.getType() == 1 ? count + "g" : count + "")
                .setText(R.id.tv_text_subtotal, df.format(subtotal));
    }
}

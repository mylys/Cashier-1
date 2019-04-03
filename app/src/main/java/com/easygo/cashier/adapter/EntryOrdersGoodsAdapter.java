package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsResponse;

import java.text.DecimalFormat;


public class EntryOrdersGoodsAdapter extends BaseQuickAdapter<GoodsEntity<GoodsResponse>, BaseViewHolder> {

    private DecimalFormat df_int = new DecimalFormat("#");
    private DecimalFormat df = new DecimalFormat("#0.00");
    private DecimalFormat df_weight = new DecimalFormat("#0.000");

    public EntryOrdersGoodsAdapter() {
        super(R.layout.item_entry_orders_detail_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsEntity<GoodsResponse> item) {
        GoodsResponse goods = item.getData();

        float count = item.getCount();
        final String price = goods.getPrice();
        float subtotal = Float.valueOf(price) * count;


        helper.setText(R.id.tv_text_goods_name, goods.getG_sku_name())
                .setText(R.id.tv_text_price, price)
                .setText(R.id.tv_text_goods_count, goods.isWeightGood() ? df_weight.format(count) + goods.getG_u_symbol() : df_int.format(count))
                .setText(R.id.tv_text_subtotal, df.format(subtotal));
    }
}

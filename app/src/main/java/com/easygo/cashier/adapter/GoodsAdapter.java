package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsInfo;

public class GoodsAdapter extends BaseQuickAdapter<GoodsInfo, BaseViewHolder> {


    public GoodsAdapter() {
        super(R.layout.item_goods);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo item) {
        helper.setText(R.id.tv_barcode, item.getBarcode())
                .setText(R.id.tv_goods_name, item.getName())
                .setText(R.id.tv_price, String.valueOf(item.getPrice()))
                .setText(R.id.tv_coupon, String.valueOf(item.getCoupon()))
                .setText(R.id.tv_subtotal, String.valueOf(item.getSubtotal()))
                .setText(R.id.tv_goods_count, String.valueOf(item.getCount()));
    }
}

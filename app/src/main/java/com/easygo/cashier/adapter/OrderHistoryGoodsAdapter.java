package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsInfo;


public class OrderHistoryGoodsAdapter extends BaseQuickAdapter<GoodsInfo, BaseViewHolder> {

    public OrderHistoryGoodsAdapter() {
        super(R.layout.item_order_history_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsInfo item) {


    }
}

package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsResponse;


/**
 * 搜索结果 及 加工方式选择适配器
 */
public class GoodsResponseAdapter extends BaseQuickAdapter<GoodsResponse, BaseViewHolder> {


    public GoodsResponseAdapter() {
        super(R.layout.item_search_result);
    }

    public GoodsResponseAdapter(int resId) {
        super(resId);
    }


    @Override
    protected void convert(BaseViewHolder helper, GoodsResponse item) {

        helper.setText(R.id.tv_goods_name, item.getG_sku_name());

    }
}

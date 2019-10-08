package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;


/**
 * 账号选择适配器
 */
public class AccountAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public AccountAdapter() {
        super(R.layout.item_search_result);
    }

    public AccountAdapter(int resId) {
        super(resId);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_goods_name, item);

    }
}

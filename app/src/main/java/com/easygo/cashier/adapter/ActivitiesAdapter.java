package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;


/**
 * 活动适配器
 */
public class ActivitiesAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ActivitiesAdapter() {
        super(R.layout.item_activities);
    }

    public ActivitiesAdapter(int resId) {
        super(resId);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_name, item);

    }
}

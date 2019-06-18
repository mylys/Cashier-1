package com.easygo.cashier.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.WeightBean;

/**
 * @Describe：
 * @Date：2019-06-18
 */
public class WeightAdapter extends BaseQuickAdapter<WeightBean.SkuListBean, BaseViewHolder> {
    private RequestOptions options;

    public WeightAdapter() {
        super(R.layout.item_quick_choose_list);
        options = new RequestOptions().placeholder(R.drawable.ic_pic_null).error(R.drawable.ic_pic_null);

    }

    @Override
    protected void convert(BaseViewHolder helper, WeightBean.SkuListBean item) {
        Glide.with(mContext).load(item.getPic_big()).apply(options).into((ImageView) helper.getView(R.id.iv_good));
        helper.setText(R.id.tv_good_price, "￥" + item.getPrice() + "/" + item.getG_u_name())
                .setText(R.id.tv_good_name, item.getG_sku_name());
        helper.getView(R.id.iv_select).setVisibility(item.isSelect() ? View.VISIBLE : View.GONE);
        helper.getView(R.id.constraint).setBackgroundResource(item.isSelect() ?
                R.drawable.bg_selector_btn_frame_green :
                R.drawable.bg_btn_frame);
    }
}

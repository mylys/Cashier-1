package com.easygo.cashier.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsResponse;


/**
 * @Describe：
 * @Date：2019-01-15
 */
public class QuickGoodsAdapter extends BaseQuickAdapter<GoodsResponse, BaseViewHolder> {

    private RequestOptions options;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public QuickGoodsAdapter() {
        super(R.layout.item_quick_choose_list);
        options = new RequestOptions().centerCrop().placeholder(R.drawable.ic_pic_null).error(R.drawable.ic_pic_null);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsResponse item) {
        int choose_background = R.drawable.bg_selector_btn_frame_green;
        int nochoose_background = R.drawable.bg_btn_frame;
        Glide.with(mContext).load(item.getPic_big()).apply(options).into((ImageView) helper.getView(R.id.iv_good));

        helper.getView(R.id.iv_select).setVisibility(item.isSelect() ? View.VISIBLE : View.GONE);
        helper.getView(R.id.constraint).setBackgroundResource(item.isSelect() ? choose_background : nochoose_background);
        helper.setText(R.id.tv_good_name, item.getG_sku_name())
                .setText(R.id.tv_good_price, "￥" + item.getPrice())
                .getView(R.id.constraint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelect()) {
                    item.setSelect(true);
                } else {
                    item.setSelect(false);
                }
                if (listener != null){
                    listener.onItemClick(helper.getLayoutPosition());
                }
                notifyItemChanged(helper.getLayoutPosition());
            }
        });
    }
}

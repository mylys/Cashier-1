package com.easygo.cashier.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.EntryOrders;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.niubility.library.utils.TimeUtils;

import androidx.constraintlayout.widget.ConstraintLayout;


public class EntryOrdersAdapter extends BaseQuickAdapter<EntryOrders, BaseViewHolder> {

    public EntryOrdersAdapter() {
        super(R.layout.item_entry_orders_list);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final EntryOrders item) {
        int white = mContext.getResources().getColor(R.color.color_text_white);
        int theme = mContext.getResources().getColor(R.color.color_51beaf);
        int text_color = mContext.getResources().getColor(R.color.color_505050);

        ConstraintLayout root = helper.getView(R.id.root);
        //设置点击状态
        root.setBackgroundColor(item.isSelect() ? theme : white);
        helper.setTextColor(R.id.tv_entry_orders_total_number, item.isSelect() ? white : text_color);
        helper.setTextColor(R.id.tv_money, item.isSelect() ? white : text_color);
        helper.setTextColor(R.id.tv_entry_orders_time, item.isSelect() ? white : text_color);

        helper.setText(R.id.tv_entry_orders_total_number, mContext.getResources().getString(R.string.text_product_name) + item.getEntry_orders_total_number())
                .setText(R.id.tv_money, item.getEntry_orders_total_price())
                .setText(R.id.tv_entry_orders_time, item.getEntry_orders_time());

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelect()) {
                    for (EntryOrders info : getData()) {
                        info.setSelect(false);
                    }
                    item.setSelect(true);
                    listener.onItemClck(helper.getLayoutPosition());
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void setItemClick() {
        for (EntryOrders orders : getData()) {
            if (orders.isSelect()) {
                orders.setSelect(false);
            }
        }
        getData().get(0).setSelect(true);
        notifyDataSetChanged();
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClck(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }
}

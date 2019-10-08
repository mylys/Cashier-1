package com.easygo.cashier.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.niubility.library.utils.TimeUtils;


public class OrderHistoryAdapter extends BaseQuickAdapter<OrderHistorysInfo, BaseViewHolder> {

    public OrderHistoryAdapter() {
        super(R.layout.item_order_history_list);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OrderHistorysInfo item) {
        long time = Long.parseLong(item.getCreate_time() + "") * 1000;
        String getTime = TimeUtils.stampToDate(time + "");
        int white = mContext.getResources().getColor(R.color.color_text_white);
        int theme = mContext.getResources().getColor(R.color.color_51beaf);
        int text_color = mContext.getResources().getColor(R.color.color_505050);

        //已退款状态 是否显示
//        helper.getView(R.id.cl_refund).setVisibility(item.getStatus() == 3 ? View.VISIBLE : View.GONE);
        helper.getView(R.id.cl_refund).setVisibility(item.getRefund_status() == 0 ? View.GONE : View.VISIBLE);

        //设置点击状态
        helper.getView(R.id.root).setBackgroundColor(item.isSelect() ? theme : white);
        helper.getView(R.id.tv_refund).setBackgroundResource(item.isSelect() ? R.drawable.bg_refund_selected : R.drawable.bg_refund_normal);
        helper.setTextColor(R.id.tv_order_no, item.isSelect() ? white : text_color);
        helper.setTextColor(R.id.tv_money, item.isSelect() ? white : text_color);
        helper.setTextColor(R.id.tv_time, item.isSelect() ? white : text_color);

        helper.setText(R.id.tv_order_no, item.getTrade_no() != null ? item.getTrade_no() : "")
                .setText(R.id.tv_money, "￥" + item.getReal_pay())
                .setText(R.id.tv_time, getTime);

        helper.getView(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!item.isSelect()) {
                    for (OrderHistorysInfo info : getData()) {
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
        for (OrderHistorysInfo info : getData()) {
            if (info.isSelect()) {
                info.setSelect(false);
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

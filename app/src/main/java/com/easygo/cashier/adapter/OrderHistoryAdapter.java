package com.easygo.cashier.adapter;

import android.app.Activity;
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
    protected void convert(BaseViewHolder helper, OrderHistorysInfo item) {
        long time = Long.parseLong(item.getCreate_time() + "") * 1000;
        String getTime = TimeUtils.stampToDate(time + "");

        //已退款状态 是否显示
        helper.getView(R.id.cl_refund).setVisibility(item.getRefund_status() == 0 ? View.GONE : View.VISIBLE);
        helper.getView(R.id.tv_refund).setVisibility(item.getRefund_status() == 0 ? View.GONE : View.VISIBLE);
        //设置点击状态
        if (item.isSelect()) {
            helper.getView(R.id.root).setBackgroundColor(mContext.getResources().getColor(R.color.color_51beaf));
            helper.getView(R.id.tv_refund).setBackgroundResource(R.drawable.bg_refund_selected);
            helper.setTextColor(R.id.tv_order_no, mContext.getResources().getColor(R.color.color_text_white));
            helper.setTextColor(R.id.tv_money, mContext.getResources().getColor(R.color.color_text_white));
            helper.setTextColor(R.id.tv_time, mContext.getResources().getColor(R.color.color_text_white));
        } else {
            helper.getView(R.id.root).setBackgroundColor(mContext.getResources().getColor(R.color.color_text_white));
            helper.getView(R.id.tv_refund).setBackgroundResource(R.drawable.bg_refund_normal);
            helper.setTextColor(R.id.tv_order_no, mContext.getResources().getColor(R.color.color_505050));
            helper.setTextColor(R.id.tv_money, mContext.getResources().getColor(R.color.color_505050));
            helper.setTextColor(R.id.tv_time, mContext.getResources().getColor(R.color.color_505050));
        }

        helper.setText(R.id.tv_order_no, item.getTrade_no() != null ? item.getTrade_no() : "")
                .setText(R.id.tv_money, "￥" + item.getReal_pay())
                .setText(R.id.tv_time, getTime);
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
}

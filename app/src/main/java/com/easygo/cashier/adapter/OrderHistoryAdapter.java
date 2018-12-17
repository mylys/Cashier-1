package com.easygo.cashier.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.OrderHistoryInfo;


public class OrderHistoryAdapter extends BaseQuickAdapter<OrderHistoryInfo, BaseViewHolder> {

    private int mSelected = 0;
    private int normal_color = -1;
    private int selected_color = -1;
    private int background_color = -1;

    public OrderHistoryAdapter() {
        super(R.layout.item_order_history_list);
    }

    public void setColor(int normal, int selected, int background) {
        normal_color = normal;
        selected_color = selected;
        background_color = background;
    }
    public int getSelected() {
        return this.mSelected;
    }
    public void setSelected(int selected) {
         this.mSelected = selected;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderHistoryInfo item) {

        helper.setText(R.id.tv_order_no, item.getOrder_no())
                .setText(R.id.tv_money, "￥" + item.getTotal_money())
                .setText(R.id.tv_time, item.getTime());

        //已退款状态 是否显示
        helper.getView(R.id.cl_refund).setVisibility(item.getRefund() != 0? View.VISIBLE: View.GONE);

        boolean isSelected = mSelected == helper.getAdapterPosition();
        //设置颜色
        helper.setTextColor(R.id.tv_order_no, isSelected ? selected_color : normal_color)
                .setTextColor(R.id.tv_money, isSelected ? selected_color : normal_color)
                .setTextColor(R.id.tv_time, isSelected ? selected_color : normal_color);
        //设置退款状态
        helper.getView(R.id.cl_refund).setSelected(isSelected);
        //设置背景色
        helper.getView(R.id.root).setBackgroundColor(isSelected ? background_color: Color.WHITE);

    }
}

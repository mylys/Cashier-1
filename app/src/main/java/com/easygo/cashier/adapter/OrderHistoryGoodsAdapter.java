package com.easygo.cashier.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.OrderHistorysInfo;

import java.text.DecimalFormat;

/**
 * 历史订单详情页面商品适配器
 */
public class OrderHistoryGoodsAdapter extends BaseQuickAdapter<OrderHistorysInfo.ListBean, BaseViewHolder> {

    private boolean have_promotion = false;
    private DecimalFormat df = new DecimalFormat("#0.00");
    private DecimalFormat df_int = new DecimalFormat("#");
    private DecimalFormat df_weight= new DecimalFormat("#0.000");


    public OrderHistoryGoodsAdapter() {
        super(R.layout.item_order_history_detail_list);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderHistorysInfo.ListBean item) {

        float count = item.getCount();
        String unit_price = item.getUnit_price();
        String sell_price = item.getSell_price();
        String discount = item.getDiscount();
        float subtotal = Float.valueOf(unit_price) * count - Float.valueOf(discount);

        helper.getView(R.id.tv_refund).setVisibility(item.getRefund() > 0 ? View.VISIBLE : View.GONE);
        helper.setText(R.id.tv_text_goods_name, item.getG_sku_name())
                .setText(R.id.tv_text_price, unit_price)
                .setText(R.id.tv_text_coupon, discount)
                .setText(R.id.tv_text_goods_count, item.isWeightGood() ?
                        df_weight.format(count) + item.getG_u_symbol() : df_int.format(count))
                .setText(R.id.tv_text_subtotal, df.format(subtotal));

        helper.setVisible(R.id.image_discount, Float.valueOf(discount) > 0);

        if (item.getType() == 1) {
            helper.setText(R.id.tv_refund, "全退");
        } else {
            helper.setText(R.id.tv_refund, item.getRefund() == item.getCount() ? "全退" : "退" + item.getRefund() + "件");
        }

    }

    public int getRefundSize() {
        int num = 0;
        for (OrderHistorysInfo.ListBean listBean : getData()) {
            if (listBean.getRefund() > 0) {
                num++;
            }
        }
        return num;
    }

    public void setHavePromotion(boolean have_promotion) {
        this.have_promotion = have_promotion;
    }
}

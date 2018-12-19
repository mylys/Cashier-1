package com.easygo.cashier.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.widget.CountEditText;
import com.easygo.cashier.widget.CountTextView;

import java.util.ArrayList;
import java.util.List;


/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-19
 */
public class OrderHistoryRefundAdapter extends BaseQuickAdapter<GoodsRefundInfo, BaseViewHolder> {
    public OrderHistoryRefundAdapter() {
        super(R.layout.item_order_history_refund_list);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsRefundInfo item) {
        //设置选择时显示隐藏
        helper.getView(R.id.tv_refund_num).setVisibility(item.isSelect() ? View.VISIBLE : View.GONE);
        helper.getView(R.id.tv_refund_num_no).setVisibility(item.isSelect() ? View.GONE : View.VISIBLE);
        helper.getView(R.id.edit_ll).setVisibility(item.isSelect() ? View.VISIBLE : View.GONE);
        helper.getView(R.id.tv_refund_subtotal).setVisibility(item.isSelect() ? View.GONE : View.VISIBLE);

        //设置选中样式
        helper.setImageResource(R.id.image_select, item.isSelect() ? R.drawable.icon_select : R.drawable.icon_no_select);

        //设置数据
        helper.setText(R.id.tv_product_name, item.getProduct_name())
                .setText(R.id.tv_product_preferential, item.getProduct_preferential())
                .setText(R.id.tv_product_price, item.getProduct_price())
                .setText(R.id.tv_product_subtotal, item.getProduct_subtotal())
                .setText(R.id.tv_refund_subtotal, item.getRefund_subtotal())
                .setText(R.id.tv_refund_num_no, item.getRefund_num());
        CountTextView countTextView = helper.getView(R.id.tv_refund_num);
//        CountEditText countEditText = helper.getView(R.id.edit_refund_subtotal);
        countTextView.setCount(item.getRefund_num());
//        countEditText.setCount(item.getRefund_subtotal());

        //设置监听
        countTextView.setOnCountListener(new CountTextView.OnCountListener() {
            @Override
            public void onCountChanged(int count) {
                item.setRefund_num(count + "");
            }
        });
//
//        countEditText.setOnCountListener(new CountEditText.OnCountListener() {
//            @Override
//            public void onCountChanged(String count) {
//                item.setRefund_subtotal(count);
//            }
//        });

        helper.getView(R.id.image_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setSelect(!item.isSelect());
                int num = 0;
                for (GoodsRefundInfo goodsRefundInfo : getData()) {
                    if (goodsRefundInfo.isSelect()) {
                        num++;
                    }
                }
                listener.onClick(num == getData().size());
                notifyItemChanged(helper.getLayoutPosition());
            }
        });

    }

    public void setClick(boolean click) {
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            goodsRefundInfo.setSelect(click);
        }
        notifyDataSetChanged();
    }

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(boolean click);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

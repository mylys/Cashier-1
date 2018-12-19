package com.easygo.cashier.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.widget.CountTextView;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-19
 */
public class OrderHistoryRefundAdapter extends BaseQuickAdapter<GoodsRefundInfo, BaseViewHolder> {
    private OnItemClickListener listener;

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
        countTextView.setCount(item.getRefund_num());

        /*设置EditText,以免滑动复用*/
        EditText countEditText = helper.getView(R.id.edit_goods_count);
        //1.移除绑定的EditText监听
        if (countEditText.getTag() instanceof TextWatcher) {
            countEditText.removeTextChangedListener((TextWatcher) countEditText.getTag());
        }
        //2.移除监听后设置EditText值
        countEditText.setText(item.getRefund_subtotal());
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setRefund_subtotal(s.toString());
            }
        };
        //3.设置EditText监听
        countEditText.addTextChangedListener(watcher);
        //4.绑定监听
        countEditText.setTag(watcher);

        countTextView.setOnCountListener(new CountTextView.OnCountListener() {
            @Override
            public void onCountChanged(int count) {
                item.setRefund_num(count + "");
                listener.onListener();
            }
        });

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
                listener.onListener();
                if (num != getData().size()) {
                    listener.onClick(false);
                }
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

    public String getTotalPrice() {
        double totalPrcie = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelect()) {
                totalPrcie += Double.parseDouble(goodsRefundInfo.getProduct_price())
                        * Integer.parseInt(goodsRefundInfo.getRefund_num());
            }
        }
        if (totalPrcie == 0){
            return "￥0.00";
        }
        return "￥" + totalPrcie;
    }

    public int getTotalNum() {
        int totalPrcie = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelect()) {
                totalPrcie++;
            }
        }
        return totalPrcie;
    }

    public interface OnItemClickListener {
        void onClick(boolean click);
        void onListener();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

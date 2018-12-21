package com.easygo.cashier.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.bean.RequsetBody;
import com.easygo.cashier.widget.CountTextView;
import com.niubility.library.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-19
 */
public class OrderHistoryRefundAdapter extends BaseQuickAdapter<GoodsRefundInfo, BaseViewHolder> {
    private OnItemClickListener listener;
    private DecimalFormat df;

    public OrderHistoryRefundAdapter() {
        super(R.layout.item_order_history_refund_list);
        df = new DecimalFormat("0.00");
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsRefundInfo item) {
        //设置选择时显示隐藏
        helper.getView(R.id.tv_refund_num).setVisibility(item.isSelect() ? View.VISIBLE : View.GONE);
        helper.getView(R.id.tv_refund_num_no).setVisibility(item.isSelect() ? View.GONE : View.VISIBLE);

        //设置选中样式
        helper.setImageResource(R.id.image_select, item.isSelect() ? R.drawable.icon_select : R.drawable.icon_no_select);

        //设置数据
        helper.setText(R.id.tv_product_name, item.getProduct_name())
                .setText(R.id.tv_product_preferential, item.getProduct_preferential())
                .setText(R.id.tv_product_price, item.getProduct_price())
                .setText(R.id.tv_product_subtotal, item.getProduct_subtotal())
                .setText(R.id.tv_product_total_num, item.getProduct_num() + "")
                .setText(R.id.tv_refund_subtotal, item.getRefund_subtotal())
                .setText(R.id.tv_refund_num_no, item.getRefund_num());
        final CountTextView countTextView = helper.getView(R.id.tv_refund_num);
        countTextView.setCount(item.getRefund_num());

        countTextView.setOnCountListener(new CountTextView.OnCountListener() {
            @Override
            public void onCountChanged(int count) {
                if (count > item.getProduct_num()) {
                    ToastUtils.showToast(mContext, "退款数量不能大于商品数量");
                    countTextView.setCount(item.getRefund_num() + "");
                    return;
                }
                if (count == 0) {
                    Toast.makeText(mContext, "商品数量不能小于1件", Toast.LENGTH_SHORT).show();
                    countTextView.setCount("1");
                    return;
                }
                item.setRefund_num(count + "");
                double subtotal = (count * Double.parseDouble(item.getProduct_price()));
                item.setRefund_subtotal(df.format(subtotal));
                listener.onListener();
                notifyDataSetChanged();
            }
        });

        helper.getView(R.id.ll_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<RequsetBody.GoodsList> goodsInfos = new ArrayList<>();
                item.setSelect(!item.isSelect());
                for (GoodsRefundInfo goodsRefundInfo : getData()) {
                    if (goodsRefundInfo.isSelect()) {
                        goodsInfos.add(new RequsetBody.GoodsList(item.getS_sku_id(), Integer.parseInt(item.getRefund_num())));
                    }
                }
                listener.onSelectBean(goodsInfos);
                listener.onListener();
                if (goodsInfos.size() != getData().size()) {
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
        return df.format(totalPrcie);
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

        void onSelectBean(ArrayList<RequsetBody.GoodsList> goodsInfos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

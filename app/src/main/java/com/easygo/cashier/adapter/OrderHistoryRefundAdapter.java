package com.easygo.cashier.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
 * @Describe：历史订单退款item
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
        final CountTextView countTextView = helper.getView(R.id.tv_refund_num);

        int type = item.getType();//0:正常商品 --- 1:称重商品 --- 2:无码商品 --- 3:加工方式

        //设置选择时显示隐藏
        if (item.isSelect()) {
            countTextView.setVisibility(View.VISIBLE);
            helper.getView(R.id.tv_refund_num_no).setVisibility(View.GONE);

            if (type == 1 || type == 3) {
                countTextView.setVisibility(View.GONE);
                helper.getView(R.id.tv_refund_num_no).setVisibility(View.VISIBLE);
            }
        } else {
            countTextView.setVisibility(View.GONE);
            helper.getView(R.id.tv_refund_num_no).setVisibility(View.VISIBLE);
        }
        helper.getView(R.id.tv_refund).setVisibility(item.getRefund() > 0 ? View.VISIBLE : View.GONE);

        //设置选中样式
        helper.setImageResource(R.id.image_select, item.isSelect() ? R.drawable.icon_select : R.drawable.icon_no_select);

        //设置数据
        String tv_total_num = type == 1 ? item.getProduct_num() + "g" : item.getProduct_num() + "";
        String tv_subtotal = df.format(Double.parseDouble(item.getProduct_subtotal()));
        helper.setText(R.id.tv_product_name, item.getProduct_name())
                .setText(R.id.tv_product_preferential, item.getProduct_preferential())
                .setText(R.id.tv_product_price, item.getProduct_price())
                .setText(R.id.tv_product_subtotal, tv_subtotal)
                .setText(R.id.tv_product_total_num, tv_total_num)
                .setText(R.id.tv_refund_subtotal, type == 1 || type == 3 ? tv_subtotal : item.getRefund_subtotal())
                .setText(R.id.tv_refund_num_no, type == 1 || type == 3 ? tv_total_num : item.getRefund_num());

        if (type == 1) {
            helper.setText(R.id.tv_refund, "全退");
        } else {
            helper.setText(R.id.tv_refund, item.getRefund() == item.getProduct_num() ? "全退" : "退" + item.getRefund() + "件");
        }

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

        //点击item监听状态变化
        helper.getView(R.id.ll_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getRefund() > 0) {
                    ToastUtils.showToast(mContext, "该商品已退款");
                    return;
                }
                item.setSelect(!item.isSelect());
                listener.onListener();
                int num = 0;
                for (int i = 0; i < getData().size(); i++) {
                    if (getData().get(i).isSelect()) {
                        if (++num != getData().size()) {
                            listener.onClick(false);
                        }
                    }
                }
                notifyItemChanged(helper.getLayoutPosition());
            }
        });
    }

    public ArrayList<RequsetBody.GoodsList> getList() {
        ArrayList<RequsetBody.GoodsList> goodsLists = new ArrayList<>();
        for (GoodsRefundInfo item : getData()) {
            if (item.isSelect()) {
                RequsetBody.GoodsList list = new RequsetBody.GoodsList();
                list.setS_sku_id(item.getS_sku_id());
                list.setIs_weigh(item.getIs_weigh());
                if (item.getIs_weigh() == 1) {
                    if (item.getParent_id() == 0) {
                        list.setCount(item.getProduct_num());
                    } else {
                        list.setCount(Integer.parseInt(item.getRefund_num()));
                    }
                } else {
                    list.setCount(Integer.parseInt(item.getRefund_num()));
                }
                goodsLists.add(list);
            }
        }
        return goodsLists;
    }

    public void setClick(boolean click) {
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.getRefund() == 0) {
                goodsRefundInfo.setSelect(click);
            }
        }
        notifyDataSetChanged();
    }

    public String getTotalPrice() {
        double totalPrcie = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelect()) {
                //0:正常商品 --- 1:称重商品 --- 2:无码商品 --- 3:加工方式
                int type = goodsRefundInfo.getType();
                totalPrcie += Double.parseDouble(type == 1 || type == 3 ? goodsRefundInfo.getProduct_subtotal() : goodsRefundInfo.getRefund_subtotal());
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

    public boolean getTotalRefund() {
        int num = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.getRefund() > 0) {
                num++;
            }
        }
        return num == getData().size();
    }

    /* 退款成功时，模拟退款信息详情 */
    public void setRefundInfo() {
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelect()) {
                if (goodsRefundInfo.getType() != 1 && goodsRefundInfo.getType() != 3) {
                    goodsRefundInfo.setRefund(Integer.parseInt(goodsRefundInfo.getRefund_num()));
                } else {
                    goodsRefundInfo.setRefund(1);
                }
                goodsRefundInfo.setSelect(false);
            }
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onClick(boolean click);

        void onListener();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

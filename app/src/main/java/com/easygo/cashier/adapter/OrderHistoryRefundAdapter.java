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
        final CountTextView countTextView = helper.getView(R.id.tv_refund_num);

        final boolean is_weigh = item.getIs_weigh() == 1;//是否为称重商品(1:是 其他为否)
        final boolean is_parentId = item.getParent_id() == 0;//是否为加工方式(>0:是 =0否) 称重商品为前提才有加工方式
        //设置选择时显示隐藏
        if (item.isSelect()) {
            if (is_weigh) {
                countTextView.setVisibility(is_parentId ? View.GONE : View.VISIBLE);
                helper.getView(R.id.tv_refund_num_no).setVisibility(is_parentId ? View.VISIBLE : View.GONE);
            } else {
                countTextView.setVisibility(View.VISIBLE);
                helper.getView(R.id.tv_refund_num_no).setVisibility(View.GONE);
            }
        } else {
            countTextView.setVisibility(View.GONE);
            helper.getView(R.id.tv_refund_num_no).setVisibility(View.VISIBLE);
        }
        helper.getView(R.id.tv_refund).setVisibility(item.getRefund() > 0 ? View.VISIBLE : View.GONE);

        //设置选中样式
        helper.setImageResource(R.id.image_select, item.isSelect() ? R.drawable.icon_select : R.drawable.icon_no_select);

        //设置数据
        String tv_total_num = is_weigh && is_parentId ? item.getProduct_num() + "g" : item.getProduct_num() + "";
        String tv_subtotal = df.format(Double.parseDouble(item.getProduct_subtotal()));
        helper.setText(R.id.tv_product_name, item.getProduct_name())
                .setText(R.id.tv_product_preferential, item.getProduct_preferential())
                .setText(R.id.tv_product_price, item.getProduct_price())
                .setText(R.id.tv_product_subtotal, tv_subtotal)
                .setText(R.id.tv_product_total_num, tv_total_num)
                .setText(R.id.tv_refund_subtotal, is_weigh && is_parentId ? tv_subtotal : item.getRefund_subtotal())
                .setText(R.id.tv_refund_num_no, is_weigh && is_parentId ? tv_total_num : item.getRefund_num())
                .setText(R.id.tv_refund, item.getRefund() == item.getProduct_num() ? "全退" : "退" + item.getRefund() + "件");

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
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

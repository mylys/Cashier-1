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
    private DecimalFormat df_int;

    public OrderHistoryRefundAdapter() {
        super(R.layout.item_order_history_refund_list);
        df = new DecimalFormat("0.00");
        df_int = new DecimalFormat("#");
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
        String tv_total_num = type == 1 ? item.getProduct_num() + item.getG_u_symbol() : df_int.format(item.getProduct_num());

        double product_subtotal = Double.parseDouble(item.getProduct_subtotal());//小计
        double refund_subtotal = Double.parseDouble(item.getRefund_subtotal());//退货小计
        String tv_subtotal = df.format(product_subtotal);
        String tv_refund_subtotal = df.format(refund_subtotal);
        if(refund_subtotal > product_subtotal) {//退货小计不能大于小计
            tv_refund_subtotal = df.format(product_subtotal);
        }

        helper.setText(R.id.tv_product_name, item.getProduct_name())
                .setText(R.id.tv_product_preferential, item.getProduct_preferential())
                .setText(R.id.tv_product_price, item.getProduct_price())
                .setText(R.id.tv_product_subtotal, tv_subtotal)
                .setText(R.id.tv_product_total_num, tv_total_num)
                .setText(R.id.tv_refund_subtotal, type == 1 || type == 3 ? tv_subtotal : tv_refund_subtotal)
//                .setText(R.id.tv_refund_num_no, type == 1 || type == 3 ? tv_total_num : item.getRefund_num());
                .setText(R.id.tv_refund_num_no, df_int.format(item.getProduct_num()));

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
                    ToastUtils.showToast(mContext, "商品数量不能小于1件");
                    countTextView.setCount("1");
                    return;
                }
                item.setRefund_num(count + "");
                double subtotal = (count * (Double.parseDouble(item.getProduct_subtotal()) / item.getProduct_num()));
                double money = Double.parseDouble(item.getProduct_subtotal());//小计
                if(subtotal > money) {
                    subtotal = money;
                }
                item.setRefund_subtotal(df.format(subtotal));
                listener.onListener();
                notifyDataSetChanged();
            }

            @Override
            public void onCountClick() {

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
                for (int i = 0; i < getData().size(); i++) {
                    if (!getData().get(i).isSelect()) {
                        listener.onClick(false);
                        break;
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
                list.setIdentity(item.getIdentity());
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

    /** 设置全选按钮（true ： 全选 ； false : 全否） */
    public void setClick(boolean click) {
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.getRefund() == 0) {
                goodsRefundInfo.setSelect(click);
                if (goodsRefundInfo.getType() != 1 || goodsRefundInfo.getType() != 3) {
                    goodsRefundInfo.setRefund_num(goodsRefundInfo.getProduct_num() + "");
                    goodsRefundInfo.setRefund_subtotal(goodsRefundInfo.getProduct_subtotal());
                }
            }
        }
        notifyDataSetChanged();
    }

    public float getRatioRefund(float real_pay) {
        float refund_total = 0;
        float total = 0;
        float product_subtotal;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelect()) {
                product_subtotal = Float.parseFloat(goodsRefundInfo.getProduct_subtotal());
                refund_total += (Integer.parseInt(goodsRefundInfo.getRefund_num()) * (product_subtotal / goodsRefundInfo.getProduct_num()));
            }
            total += Float.parseFloat(goodsRefundInfo.getProduct_subtotal());
        }
        return real_pay * (refund_total / total);
    }

    /** 获取商品选择总额 */
    public String getTotalPrice() {
        double totalPrcie = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelect()) {
                //0:正常商品 --- 1:称重商品 --- 2:无码商品 --- 3:加工方式
                int type = goodsRefundInfo.getType();
                totalPrcie += Double.parseDouble(type == 1 || type == 3 ? goodsRefundInfo.getProduct_subtotal() : goodsRefundInfo.getRefund_subtotal());
//                totalPrcie -= Double.parseDouble(goodsRefundInfo.getProduct_preferential());
            }
        }
        return df.format(totalPrcie);
    }

    /** 获取商品总优惠 */
    public float getTotalCoupon() {
        double totalCoupon = 0f;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelect()) {
                totalCoupon += Double.parseDouble(goodsRefundInfo.getProduct_preferential());
            }
        }
        return (float) totalCoupon;
    }

    /** 获取商品退货总数量 */
    public int getTotalNum() {
        int totalPrcie = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelect()) {
                totalPrcie += Integer.parseInt(goodsRefundInfo.getRefund_num());
            }
        }
        return totalPrcie;
    }

    /** 获取商品已退货数量 */
    public boolean getTotalRefund() {
        int num = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.getRefund() > 0) {
                num++;
            }
        }
        return num == getData().size();
    }

    /** 退款成功时，模拟退款信息详情 */
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

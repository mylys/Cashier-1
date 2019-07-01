package com.easygo.cashier.adapter;

import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.request.RefundRequsetBody;
import com.easygo.cashier.widget.view.CountTextView;
import com.niubility.library.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @Describe：历史订单退款item
 * @date：2018-12-19
 */
public class OrderHistoryRefundAdapter extends BaseQuickAdapter<GoodsRefundInfo, BaseViewHolder> {
    private OnItemClickListener listener;
    private DecimalFormat df;
    private DecimalFormat df_int;
    private DecimalFormat df_weight;

    public OrderHistoryRefundAdapter() {
        super(R.layout.item_order_history_refund_list);
        df = new DecimalFormat("0.00");
        df_int = new DecimalFormat("#");
        df_weight = new DecimalFormat("#0.000");
    }

    @Override
    protected void convert(final BaseViewHolder helper, final GoodsRefundInfo item) {
        final CountTextView countTextView = helper.getView(R.id.tv_refund_num);

        int type = item.getType();//0:正常商品 --- 1:称重商品 --- 2:无码商品 --- 3:加工方式

        helper.setOnCheckedChangeListener(R.id.cb_refund, null);
        helper.setOnCheckedChangeListener(R.id.cb_return_of_goods, null);

        boolean selectRefund = item.isSelectRefund();
        boolean selectReturnOfGoods = item.isSelectReturnOfGoods();

        helper.setChecked(R.id.cb_refund, selectRefund);
        helper.setChecked(R.id.cb_return_of_goods, selectReturnOfGoods);

        //设置选择时显示隐藏
        if (selectRefund || selectReturnOfGoods) {//退货时
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
//        helper.setImageResource(R.id.image_select, item.isSelect() ? R.drawable.icon_select : R.drawable.icon_no_select);

        //设置数据
        String tv_total_num = type == 1 ? df_weight.format(item.getProduct_num()) + item.getG_u_symbol() : df_int.format(item.getProduct_num());

        double product_subtotal = Double.parseDouble(item.getProduct_subtotal());//小计
        double refund_subtotal = Double.parseDouble(item.getRefund_subtotal());//退货小计
        String tv_subtotal = df.format(product_subtotal);
        String tv_refund_subtotal = df.format(refund_subtotal);
        if (refund_subtotal > product_subtotal) {//退货小计不能大于小计
            tv_refund_subtotal = df.format(product_subtotal);
        }

        helper.setText(R.id.tv_product_name, item.getProduct_name())
                .setText(R.id.tv_product_preferential, item.getProduct_preferential())
                .setText(R.id.tv_product_price, item.getProduct_price())
                .setText(R.id.tv_product_subtotal, tv_subtotal)
                .setText(R.id.tv_product_total_num, tv_total_num)
                .setText(R.id.tv_refund_subtotal, type == 1 || type == 3 ? tv_subtotal : tv_refund_subtotal)
                .setText(R.id.tv_refund_num_no, type == 1 ? item.getProduct_num() + "" : df_int.format(item.getProduct_num()));

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
                    ToastUtils.showToast(mContext, R.string.text_refund_cannot_more_than_goods_num);
                    countTextView.setCount(item.getRefund_num() + "");
                    return;
                }
                if (count == 0) {
                    ToastUtils.showToast(mContext, R.string.text_goods_num_cannot_less_than_one);
                    countTextView.setCount("1");
                    return;
                }
                item.setRefund_num(count + "");
                double subtotal = (count * (Double.parseDouble(item.getProduct_subtotal()) / item.getProduct_num()));
                double money = Double.parseDouble(item.getProduct_subtotal());//小计
                if (subtotal > money) {
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
                    ToastUtils.showToast(mContext, R.string.text_goods_already_refund);
                    return;
                }
                boolean selected = !item.isSelectReturnOfGoods();
                CheckBox return_of_goods = helper.getView(R.id.cb_return_of_goods);
                return_of_goods.setChecked(selected);
            }
        });

        CheckBox cb_refund = helper.getView(R.id.cb_refund);
        CheckBox cb_return_of_goods = helper.getView(R.id.cb_return_of_goods);
        cb_refund.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (item.getRefund() > 0) {
                        ToastUtils.showToast(mContext, R.string.text_goods_already_refund);
                        return true;
                    }
                    if (((CheckBox) helper.getView(R.id.cb_return_of_goods)).isChecked()) {//选择退货 不能取消退款
                        return true;
                    }
                }
                return false;
            }
        });
        cb_return_of_goods.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (item.getRefund() > 0) {
                        ToastUtils.showToast(mContext, R.string.text_goods_already_refund);
                        return true;
                    }
                }
                return false;
            }
        });

        helper.setOnCheckedChangeListener(R.id.cb_refund, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setSelectRefund(isChecked);
                listener.onListener();
                notifyItemChanged(helper.getLayoutPosition());
            }
        });
        helper.setOnCheckedChangeListener(R.id.cb_return_of_goods, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                item.setSelectReturnOfGoods(isChecked);
                CheckBox refund = helper.getView(R.id.cb_refund);
                if (isChecked) {//选中了退货，此时退款已经被选中时 刷新
                    if (refund.isChecked()) {
                        item.setSelectRefund(true);
                        listener.onListener();
                        notifyItemChanged(helper.getLayoutPosition());
                        return;
                    }
                }
                //同步改变 退款
                refund.setChecked(isChecked);
            }
        });

    }

    public ArrayList<RefundRequsetBody.GoodsList> getList() {
        ArrayList<RefundRequsetBody.GoodsList> goodsLists = new ArrayList<>();
        boolean isSelectRefund;
        boolean isSelectReturnOfGoods;
        for (GoodsRefundInfo item : getData()) {
            isSelectRefund = item.isSelectRefund();
            isSelectReturnOfGoods = item.isSelectReturnOfGoods();
            if (isSelectRefund || isSelectReturnOfGoods) {
                RefundRequsetBody.GoodsList list = new RefundRequsetBody.GoodsList();
                list.setS_sku_id(item.getS_sku_id());
                list.setIs_weigh(item.getIs_weigh());
                list.setIdentity(item.getIdentity());
                if (item.getIs_weigh() == 1) {
                    list.setCount(item.getProduct_num());
                } else {
                    list.setCount(Integer.parseInt(item.getRefund_num()));
                }
                if (isSelectRefund && !isSelectReturnOfGoods) {//退款 不退货
                    list.setType(1);
                } else { //退款 退货
                    list.setType(2);
                }
                goodsLists.add(list);
            }
        }
        return goodsLists;
    }

    /**
     * 判断是否需要默认选中退款
     */
    public void judgeNeedSelectRefund() {
        if (mData.size() == 1) {
            CheckBox refund = (CheckBox) getViewByPosition(0, R.id.cb_refund);
            if (refund != null) {
                refund.setChecked(true);
            }
        }
    }

    public float getRatioRefund(float real_pay) {
        float refund_total = 0;
        float total = 0;
        float product_subtotal;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelectRefund()) {

                product_subtotal = Float.parseFloat(goodsRefundInfo.getProduct_subtotal());
                if (GoodsResponse.isWeightGood(goodsRefundInfo.getType())) {
                    refund_total += product_subtotal;
                } else {
                    refund_total += (Integer.parseInt(goodsRefundInfo.getRefund_num()) * (product_subtotal / goodsRefundInfo.getProduct_num()));
                }
            }
            total += Float.parseFloat(goodsRefundInfo.getProduct_subtotal());
        }
        return real_pay * (refund_total / total);
    }

    /**
     * 获取商品选择总额
     */
    public String getTotalPrice() {
        double totalPrcie = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelectRefund()) {
                //0:正常商品 --- 1:称重商品 --- 2:无码商品 --- 3:加工方式
                int type = goodsRefundInfo.getType();
                totalPrcie += Double.parseDouble(type == 1 || type == 3 ? goodsRefundInfo.getProduct_subtotal() : goodsRefundInfo.getRefund_subtotal());
            }
        }
        return df.format(totalPrcie);
    }


    /**
     * 获取商品退货总数量
     */
    public int getTotalNum() {
        int totalPrcie = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelectReturnOfGoods()) {
                totalPrcie += Integer.parseInt(goodsRefundInfo.getRefund_num());
            }
        }
        return totalPrcie;
    }

    /**
     * 获取商品已退货数量
     */
    public boolean getTotalRefund() {
        int num = 0;
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.getRefund() > 0) {
                num++;
            }
        }
        return num == getData().size();
    }

    /**
     * 退款成功时，模拟退款信息详情
     */
    public void setRefundInfo() {
        for (GoodsRefundInfo goodsRefundInfo : getData()) {
            if (goodsRefundInfo.isSelectRefund()) {
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
        void onListener();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

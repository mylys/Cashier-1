package com.easygo.cashier.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.easygo.cashier.utils.MemberUtils;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.widget.view.CountTextView;

import java.text.DecimalFormat;

/**
 * 用户购买的商品列表 适配器
 * 1、普通商品
 * 2、重量商品
 * 3、纯加工商品（通过搜索获得）
 * 4、加工商品
 * 5、无码商品
 */
public class UserGoodsAdapter extends GoodsMultiItemAdapter {

    private DecimalFormat df = new DecimalFormat("0.00");
    private DecimalFormat df_int = new DecimalFormat("#");
    private DecimalFormat df_weight = new DecimalFormat("0.000");


    public UserGoodsAdapter() {

        addItemType(GoodsEntity.TYPE_GOODS, R.layout.item_goods);
        addItemType(GoodsEntity.TYPE_WEIGHT, R.layout.item_goods_weight);
        addItemType(GoodsEntity.TYPE_ONLY_PROCESSING, R.layout.item_goods);
        addItemType(GoodsEntity.TYPE_PROCESSING, R.layout.item_user_processing);
        addItemType(GoodsEntity.TYPE_NO_CODE, R.layout.item_goods);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final GoodsEntity<GoodsResponse> item) {
        final float good_count = item.getCount();
        final GoodsResponse good = item.getData();

        final String barcode = good.getBarcode();
        final String price = good.getPrice();
        float subtotal = Float.valueOf(price) * good_count - Float.valueOf(good.getDiscount_price());

        if(subtotal < 0) {
            subtotal = 0;
        }

        helper.getView(R.id.tv_member_price).setVisibility(MemberUtils.isMember && MemberUtils.isSuperMember ? View.VISIBLE : View.GONE);
        helper.setText(R.id.tv_barcode, barcode)
                .setText(R.id.tv_goods_name, good.getG_sku_name())
                .setText(R.id.tv_price, String.valueOf(price))
                .setText(R.id.tv_coupon,  df.format(Float.valueOf(good.getDiscount_price())))
                .setText(R.id.tv_subtotal, String.valueOf(df.format(subtotal)))
                .setText(R.id.tv_member_price, good.getMembership_price());

        switch (helper.getItemViewType()) {
            case GoodsEntity.TYPE_GOODS://普通商品
            case GoodsEntity.TYPE_NO_CODE://无码商品
                CountTextView count = helper.getView(R.id.count_view);
                count.setCount(df_int.format(good_count));
                count.setCountChangeEnable(false);
                break;
            case GoodsEntity.TYPE_WEIGHT://称重商品
                helper.setText(R.id.tv_count, df_weight.format(good_count) + good.getG_u_symbol());
                break;
            case GoodsEntity.TYPE_ONLY_PROCESSING://纯加工方式
                CountTextView view = helper.getView(R.id.count_view);
                view.setCountChangeEnable(false);
                break;
            case GoodsEntity.TYPE_PROCESSING://加工方式


                GoodsResponse processing = item.getProcessing();
                if (processing != null) {//选择了加工方式

                    //刷新数据
                    refreshPrcessingData(helper, processing);
                }

                final boolean is_processing = processing != null;

                //设置加工方式相关控件可见性
                helper.setText(R.id.tv_count, good_count + good.getG_u_symbol());
                setProcessingLayout(helper, is_processing);
                break;
        }

    }

    /**
     * 设置加工方式相关UI 可见性
     */
    private void setProcessingLayout(BaseViewHolder helper, boolean showProcessing) {

        helper.getView(R.id.view2).setVisibility(showProcessing ? View.VISIBLE : View.GONE);
        helper.getView(R.id.line).setVisibility(showProcessing ? View.VISIBLE : View.GONE);

        helper.setVisible(R.id.tv_text_processing_way, showProcessing)
                .setVisible(R.id.tv_processing_name, showProcessing)
                .setVisible(R.id.tv_processing_price, showProcessing)
                .setVisible(R.id.tv_processing_coupon, showProcessing)
                .setVisible(R.id.tv_processing_subtotal, showProcessing)
                .setVisible(R.id.tv_processing_count, showProcessing);

    }

    /**
     * 刷新加工方式相关UI 数据
     */
    private void refreshPrcessingData(BaseViewHolder helper, GoodsResponse processing) {
        DecimalFormat df = new DecimalFormat("#0.00");
        float processing_subtotal = Float.valueOf(processing.getPrice()) * processing.getCount()
                -  Float.valueOf(processing.getDiscount_price());

        if(processing_subtotal < 0) {
            processing_subtotal = 0;
        }

        helper.setText(R.id.tv_processing_name, processing.getG_sku_name())
                .setText(R.id.tv_processing_price, processing.getPrice())
                .setText(R.id.tv_processing_coupon, processing.getDiscount_price())
                .setText(R.id.tv_processing_subtotal, String.valueOf(df.format(processing_subtotal)))
                .setText(R.id.tv_processing_count, String.valueOf(processing.getCount()));
    }

    public void chooseProcessing(int position, GoodsResponse choice) {
        GoodsEntity<GoodsResponse> goodsEntity = mData.get(position);
        goodsEntity.setProcessing(choice);
        notifyItemChanged(position);

        //刷新价格
        refreshPrice();
    }
}

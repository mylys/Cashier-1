package com.easygo.cashier.module.promotion.goods;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;
import com.easygo.cashier.utils.BigDecimalUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品满额促销
 */
public class GoodsFulfilMoneyPromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    public static final String TAG = GoodsFulfilMoneyPromotion.class.getSimpleName();

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        return false;
    }

    @Override
    public void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data) {
        Log.i(TAG, "computePromotionMoney: 满额促销");
        if(isInGoodsEffectedTime()) {

//            float total_money = promotionGoods.getTotal_money();

            int data_size = data.size();
            double total_money = 0D;
            for (int i = 0; i < data_size; i++) {
                GoodsResponse good = data.get(i).getData();

                //减去临时商品折扣后的订单额
                total_money += (BigDecimalUtils
                        .mul(Double.valueOf(good.getPrice()), data.get(i).getCount())
                        .subtract(new BigDecimal(Float.toString(good.getTemp_goods_discount()))))
                        .doubleValue();

                if(GoodsEntity.TYPE_PROCESSING == data.get(i).getItemType()) {
                    GoodsResponse processing = data.get(i).getProcessing();
                    if(processing != null) {
//                        total_money += Float.valueOf(processing.getPrice()) * processing.getCount();
                        total_money += BigDecimalUtils
                                .mul(Float.valueOf(processing.getPrice()), processing.getCount())
                                .doubleValue();
                    }
                }
            }

            if(total_money < getCondition_value()) {
                //不满足满额促销条件  直接返回
                Log.i(TAG, "computePromotionMoney: 不满足满额促销条件金额");
                return;
            }

            List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
            int goods_size = goodsBeans.size();

            float max_promotion = 0f;
            float subtract = 0;
            int index = -1;

            int size = this.listBeans.size();
            for (int i = 0; i < size; i++) {
                GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean listBean = this.listBeans.get(i);
                float offer_value = Float.valueOf(listBean.getOffer_value());

                for (int j = 0; j < goods_size; j++) {
                    PromotionGoods.GoodsBean goodsBean = goodsBeans.get(j);
                    //找到对应商品
                    if(listBean.getBarcode().equals(goodsBean.getBarcode())) {

                        //排除参与了临时商品促销的商品
                        if (goodsBean.getCount() == 0
                                || Float.valueOf(data.get(goodsBean.getIndex()).getData().getDiscount_price()) > 0) {
                            continue;
                        }

                        //计算差价
                        subtract = goodsBean.getPrice() - offer_value;
                        Log.i(TAG, "computePromotionMoney: 第" + i + "个，差价 - " + subtract);
                        if (subtract > max_promotion) {
                            max_promotion = subtract;
                            index = j;
                        }
                    }
                }
            }

            if(index != -1) {
                Log.i(TAG, "computePromotionMoney: 商品满额促销 选择第" + index + "个，最大差价 - " + max_promotion);
                PromotionGoods.GoodsBean goodsBean = goodsBeans.get(index);
                if(max_promotion > 0) {
                    goodsBean.setPromotion_money(max_promotion);
                    data.get(goodsBean.getIndex()).setPromotion(this);
//                data.get(goodsBean.getIndex()).getData().setDiscount_price(String.valueOf(max_promotion));
                    data.get(goodsBean.getIndex()).getData().setGoods_activity_discount(max_promotion);
                }
            }
        }
    }

}

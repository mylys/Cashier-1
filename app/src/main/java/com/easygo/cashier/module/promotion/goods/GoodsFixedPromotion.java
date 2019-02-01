package com.easygo.cashier.module.promotion.goods;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;

import java.util.List;

/**
 * 商品固定促销价
 */
public class GoodsFixedPromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    public static final String TAG = GoodsFixedPromotion.class.getSimpleName();

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        return false;
    }

    @Override
    public void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data) {
        Log.i(TAG, "computePromotionMoney: 固定促销");
        if(isInGoodsEffectedTime()) {

            List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
            int goods_size = goodsBeans.size();

            float promotion = 0f;
            float subtract = 0;

            int size = this.listBeans.size();
            for (int i = 0; i < size; i++) {
                GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean listBean = this.listBeans.get(i);
                float offer_value = Float.valueOf(listBean.getOffer_value());

                for (int j = 0; j < goods_size; j++) {
                    PromotionGoods.GoodsBean goodsBean = goodsBeans.get(j);
                    //找到对应商品
                    if(listBean.getBarcode().equals(goodsBean.getBarcode())) {

                        if (goodsBean.getCount() == 0) {
                            return;
                        }

                        //计算差价
                        subtract = goodsBean.getPrice() - offer_value;
                        if (subtract < 0){
                            subtract = goodsBean.getPrice();
                        }
                        promotion = subtract * goodsBean.getCount();
                        Log.i(TAG, "computePromotionMoney: 固定促销, "
                                + goodsBean.getBarcode() + " 优惠: " + promotion);
                        goodsBean.setPromotion_money(promotion);
                        data.get(goodsBean.getIndex()).setPromotion(this);
                        data.get(goodsBean.getIndex()).getData().setDiscount_price(String.valueOf(promotion));

                    }
                }
            }
        }
    }

}

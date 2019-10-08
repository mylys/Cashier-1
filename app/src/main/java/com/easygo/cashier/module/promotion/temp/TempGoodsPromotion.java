package com.easygo.cashier.module.promotion.temp;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;
import com.easygo.cashier.module.promotion.goods.BaseGoodsPromotion;

import java.util.List;

/**
 * 临时商品促销
 */
public class TempGoodsPromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    public static final String TAG = TempGoodsPromotion.class.getSimpleName();

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        return false;
    }

    @Override
    public void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data) {
        Log.i(TAG, "computePromotionMoney: 临时商品促销");

        if(promotionGoods == null) {
            return;
        }

        int total_count = promotionGoods.getTotal_count();
        float total_money = promotionGoods.getTotal_money();

        List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
        int size = goodsBeans.size();

        //促销金额
        float promotion_money = 0f;

//        promotion_money = getPromotionMoney(total_money);

        for (int i = 0; i < size; i++) {
            PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);

            if(goodsBean.getCount() == 0) {
                return;
            }
            GoodsEntity<GoodsResponse> goodsEntity = data.get(goodsBean.getIndex());

//            promotion_money = getPromotionMoney(goodsBean.getPrice()) * goodsBean.getQuanlity();
            promotion_money = getPromotionMoney(goodsBean.getPrice() * goodsBean.getCount());

            //需要设置促销金额的  根据比例计算出促销金额
            float promotion = (goodsBean.getSubtotal() / total_money) * promotion_money;
            Log.i(TAG, "computePromotionMoney: 临时商品促销 ， 促销金额 -> " + promotion);
            goodsBean.setPromotion_money(promotion);
            if(promotion > 0) {
                goodsEntity.setPromotion(this);
//            goodsEntity.getData().setDiscount_price(String.valueOf(promotion));
                goodsEntity.getData().setTemp_goods_discount(promotion);
            }
        }

    }

    private float getPromotionMoney(float price) {
        switch (getOffer_type()) {
            case IPromotion.OFFER_TYPE_MONEY:
                return price - getOffer_value();
            case IPromotion.OFFER_TYPE_RATIO:
                return price * ((100-getOffer_value()) / 100f);
            default:
                return 0f;
        }
    }

}
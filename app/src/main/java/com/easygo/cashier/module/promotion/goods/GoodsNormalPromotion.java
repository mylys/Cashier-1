package com.easygo.cashier.module.promotion.goods;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;

import java.util.List;

/**
 * 商品普通促销
 */
public class GoodsNormalPromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    public static final String TAG = GoodsNormalPromotion.class.getSimpleName();

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        return false;
    }

    @Override
    public void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data) {

        Log.i(TAG, "computePromotionMoney: 普通促销");

        if(promotionGoods == null) {
            return;
        }

        int total_count = promotionGoods.getTotal_count();
        float total_money = promotionGoods.getTotal_money();

        List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
        int size = goodsBeans.size();

        //促销金额
        float promotion_money = 0f;
        float temp_subtotal = 0f;
        int count;

        //  1、判断是否满足 促销条件
        switch (getCondition_type()) {
            case IPromotion.CONDITION_TYPE_EVEN:
                //倍数
                if(total_count < 2) {
                    //商品总数小于2  则不满足 偶数件 直接返回
                    return;
                }
                int times = total_count / 2;

                promotion_money = getPromotionMoney(times, 0);


                int remain = 0;

                int current_count = 0;
                int current_times = 0;

                int temp_count = 0;

                //遍历

                /**
                 * 3    (0+3)/2 = 1...1
                 * 1    (1+1)/2 = 1...0
                 * 2    (0+2)/2 = 1...0
                 * 1    (0+1)/2 = 1...0
                 */

                for (int i = 0; i < size; i++) {
                    PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
                    count = goodsBean.getCount();
                    current_count += count;

                    if(count == 0) {
                        continue;
                    }

                    temp_count = remain + count;

                    current_times += temp_count / 2;
                    remain = temp_count % 2;

                    if(!(i == size - 1 && temp_count == 1)) {
                        //最后一个 商品数为
                        goodsBean.setPromotion_money(PromotionGoods.FLAG_NEED_SET_PROMOTION_MONEY);
                        temp_subtotal += goodsBean.getSubtotal();
                    }
                }
                break;
            case IPromotion.CONDITION_TYPE_MONEY:
                if(total_money < condition_value) {
                    //商品总额小于条件金额 则不满足 满额 直接返回
                    return;
                }

                promotion_money = getPromotionMoney(0, temp_subtotal);

                for (int i = 0; i < size; i++) {
                    PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
                    count = goodsBean.getCount();

                    if(count == 0) {
                        return;
                    }

                    goodsBean.setPromotion_money(PromotionGoods.FLAG_NEED_SET_PROMOTION_MONEY);

                }

                break;
        }


        for (int i = 0; i < size; i++) {
            PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
            GoodsEntity<GoodsResponse> goodsEntity = data.get(goodsBean.getIndex());

            if (goodsBean.getPromotion_money() == PromotionGoods.FLAG_NEED_SET_PROMOTION_MONEY) {
                //需要设置促销金额的  根据比例计算出促销金额
                float promotion = (goodsBean.getSubtotal() / temp_subtotal) * promotion_money;
                Log.i(TAG, "computePromotionMoney: 商品普通促销 促销金额 -> " + promotion);
                goodsBean.setPromotion_money(promotion);
                goodsEntity.setPromotion(this);
                goodsEntity.getData()
                        .setDiscount_price(String.valueOf(promotion));
            } else {
                Log.i(TAG, "computePromotionMoney: 清空促销金额 -> ");
                goodsEntity.setPromotion(null);
                goodsEntity.getData()
                        .setDiscount_price("0.00");
            }
        }

    }

    private float getPromotionMoney(int times, float subtotal) {

        switch (getOffer_type()) {
            case IPromotion.OFFER_TYPE_MONEY:

                if(times != 0) {
                    return times * getOffer_value();
                } else {
                    return getOffer_value();
                }
            case IPromotion.OFFER_TYPE_RATIO:
                if(times != 0) {
                    return subtotal * times * getOffer_value();
                } else {
                    return subtotal * (getOffer_value() / 100f);
                }
            default:
                return 0f;
        }
    }
}

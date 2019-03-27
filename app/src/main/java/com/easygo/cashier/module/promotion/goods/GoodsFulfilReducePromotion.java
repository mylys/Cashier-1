package com.easygo.cashier.module.promotion.goods;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;

import java.util.List;

/**
 * 商品满减促销
 */
public class GoodsFulfilReducePromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    public static final String TAG = GoodsFulfilReducePromotion.class.getSimpleName();

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        return false;
    }

    @Override
    public void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data) {
        Log.i(TAG, "computePromotionMoney: 满减促销");
        if(isInGoodsEffectedTime()) {

            float total_money = promotionGoods.getTotal_money();

            if(total_money < getCondition_value()) {
                //不满足满额促销条件  直接返回
                Log.i(TAG, "computePromotionMoney: 不满足满减促销条件金额");
                return;
            }

            List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
            int goods_size = goodsBeans.size();

            float promotion_money = 0f;
            float substract = total_money;
            float current_substract;
            int index = -1;
            float offer_value = 0f;
            int offer_type = IPromotion.OFFER_TYPE_MONEY;

            int size = this.listBeans.size();
            for (int i = 0; i < size; i++) {
                GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean listBean = this.listBeans.get(i);
                float conditon_value = Float.valueOf(listBean.getCondition_value());

                if(total_money < conditon_value) {//跳过 商品总金额比条件金额小的规则
                    continue;
                }

                //寻找差价最小
                current_substract = total_money - conditon_value;
                if(current_substract < substract) {
                    substract = current_substract;
                    index = i;

                    offer_type = Integer.valueOf(listBean.getOffer_type());
                    offer_value = Float.valueOf(listBean.getOffer_value());
                }

            }

            if(index != -1) {

                switch (offer_type) {
                    case IPromotion.OFFER_TYPE_MONEY:
                        promotion_money = offer_value;
                        break;
                    case IPromotion.OFFER_TYPE_RATIO:
                        promotion_money = total_money * (offer_value / 100f);
                        break;
                    default:
                        promotion_money = 0f;
                        break;
                }

                Log.i(TAG, "computePromotionMoney: 商品满减促销 选择第" + index + "个，最大差价 - " + promotion_money);

                if(promotion_money <= 0) {
                    return;
                }

                for (int i = 0; i < size; i++) {
                    PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);

                    if (goodsBean.getCount() == 0) {
                        return;
                    }
                    GoodsEntity<GoodsResponse> goodsEntity = data.get(goodsBean.getIndex());

                    //需要设置促销金额的  根据比例计算出促销金额
                    float promotion = (goodsBean.getSubtotal() / total_money) * promotion_money;
                    Log.i(TAG, "computePromotionMoney: 商品满减 index -> " + goodsBean.getIndex()
                            + ", 促销金额 -> " + promotion);

                    if(promotion > 0) {
                        goodsBean.setPromotion_money(promotion);
                        goodsEntity.setPromotion(this);
                        goodsEntity.getData().setGoods_activity_discount(promotion);
                    }
                }
            }
        }
    }
}

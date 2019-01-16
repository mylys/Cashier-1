package com.easygo.cashier.module.promotion.goods;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.BasePromotion;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GoodsTimePromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    public static final String TAG = GoodsTimePromotion.class.getSimpleName();

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        return false;
    }

    @Override
    public void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data) {
        Log.i(TAG, "computePromotionMoney: 时段促销");
        if(isInGoodsEffectedTime()) {
            String current = sdf.format(new Date());

            int size = listBeans.size();
            GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean listBean;
            GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean selectedListBean = null;

            for (int i = 0; i < size; i++) {
                listBean = listBeans.get(i);

                String effected_at = listBean.getEffected_at();
                String expired_at = listBean.getExpired_at();
                //在时段内
                if (current.compareTo(effected_at) >= 0 && current.compareTo(expired_at) <= 0) {
                    selectedListBean = listBean;
                    break;
                }
            }

            if (selectedListBean != null) {
                int offer_type = Integer.valueOf(selectedListBean.getOffer_type());
                float offer_value = Float.valueOf(selectedListBean.getOffer_value());
                PromotionGoods promotionGoods = getPromotionGoods();

                float total_money = promotionGoods.getTotal_money();
                float promotion_money = getPromotionMoney(offer_type, offer_value, total_money);
                List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
                int goods_size = goodsBeans.size();
                for (int i = 0; i < goods_size; i++) {
                    PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
                    if (goodsBean.getCount() != 0) {
                        data.get(goodsBean.getIndex()).setPromotion(this);
                        //需要设置促销金额的  根据比例计算出促销金额
                        float promotion = (goodsBean.getSubtotal() / total_money) * promotion_money;
                        Log.i(TAG, "computePromotionMoney: 商品时段促销 促销金额 -> " + promotion);
                        goodsBean.setPromotion_money(promotion);
                        data.get(goodsBean.getIndex()).getData()
                                .setDiscount_price(String.valueOf(promotion));
                    }
                }
            }
        }
    }

    private float getPromotionMoney(int offer_type, float offer_value, float subtotal) {

        switch (offer_type) {
            case IPromotion.OFFER_TYPE_MONEY:
                return offer_value;

            case IPromotion.OFFER_TYPE_RATIO:
                return subtotal * (offer_value / 100f);

            default:
                return 0f;
        }
    }
}

package com.easygo.cashier.module.promotion.shop;

import android.util.Log;

import com.easygo.cashier.bean.ShopActivityResponse;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.IShopPromotion;

import java.util.List;

/**
 * 店铺普通促销
 */
public class ShopNormalPromotion extends BaseShopPromotion implements IShopPromotion {

    private static final String TAG = ShopTimePromotion.class.getSimpleName();

    @Override
    public float getPromotionMoney(float money) {

        Log.i(TAG, "getPromotionMoney: 店铺时段促销");

        if(!isInEffectedTime()) {
            return 0f;
        }

        List<ShopActivityResponse.ListBean.ConfigBean> configBeans = getConfigBeans();
        ShopActivityResponse.ListBean.ConfigBean configBean = configBeans.get(0);
        float condition_value = Float.valueOf(configBean.getCondition_value());
        if(money < condition_value) {
            return 0f;
        }

        float offer_value = Float.valueOf(configBean.getOffer_value());


        switch (getOffer_type()) {
            case OFFER_TYPE_MONEY:
                return offer_value;
            case OFFER_TYPE_RATIO:
                return money * (offer_value / 100f);
            default:
                return 0f;
        }
    }
}

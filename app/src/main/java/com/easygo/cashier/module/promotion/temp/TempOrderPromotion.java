package com.easygo.cashier.module.promotion.temp;

import android.util.Log;

import com.easygo.cashier.module.promotion.base.IShopPromotion;
import com.easygo.cashier.module.promotion.shop.BaseShopPromotion;

/**
 * 临时整单促销
 */
public class TempOrderPromotion extends BaseShopPromotion implements IShopPromotion {

    private static final String TAG = TempOrderPromotion.class.getSimpleName();

    @Override
    public float getPromotionMoney(float money) {

        Log.i(TAG, "getPromotionMoney: 临时整单促销");

        float offer_value = getOffer_value();
        int offer_type = getOffer_type();
        Log.i(TAG, "getPromotionMoney: offer_value - > " + offer_value);

        switch (offer_type) {
            case OFFER_TYPE_MONEY:
                return money - offer_value;
            case OFFER_TYPE_RATIO:
                return money * ((100-offer_value) / 100f);
            default:
                return 0f;
        }
    }
}

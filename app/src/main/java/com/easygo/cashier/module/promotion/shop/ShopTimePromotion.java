package com.easygo.cashier.module.promotion.shop;

import com.easygo.cashier.module.promotion.base.BasePromotion;
import com.easygo.cashier.module.promotion.base.IShopPromotion;

/**
 * 店铺时段促销
 */
public class ShopTimePromotion extends BasePromotion implements IShopPromotion {

    @Override
    public int getPromotionClassify() {
        return PROMOTION_SHOP;
    }

    @Override
    public boolean isMeetCondition(float money) {

        if(money >= condition_value) {//达到购满金额
            long currentTimeMillis = System.currentTimeMillis();
            return currentTimeMillis >= getEffected_at() && currentTimeMillis <= getExpired_at();
        }

        return false;
    }

    @Override
    public float getPromotionMoney(float money) {
        switch (getOffer_type()) {
            case OFFER_TYPE_MONEY:

                return offer_value;
            case OFFER_TYPE_RATIO:

                return money * (offer_value / 100f);
            default:
                return 0;
        }
    }

}

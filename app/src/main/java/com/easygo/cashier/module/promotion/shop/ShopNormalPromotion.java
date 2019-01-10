package com.easygo.cashier.module.promotion.shop;

import com.easygo.cashier.module.promotion.base.BasePromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.IShopPromotion;

/**
 * 店铺普通促销
 */
public class ShopNormalPromotion extends BasePromotion implements IShopPromotion {


    @Override
    public int getPromotionClassify() {
        return IPromotion.PROMOTION_SHOP;
    }


    @Override
    public boolean isMeetCondition(float money) {
        return money >= getCondition_value();
    }

    @Override
    public float getPromotionMoney(float money) {
        if(!isMeetCondition(money)) {
            return 0f;
        }
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

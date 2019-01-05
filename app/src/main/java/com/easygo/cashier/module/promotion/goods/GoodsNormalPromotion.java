package com.easygo.cashier.module.promotion.shop;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.module.promotion.BasePromotion;
import com.easygo.cashier.module.promotion.IPromotion;

/**
 * 店铺普通促销
 */
public class ShopNormalPromotion extends BasePromotion implements IPromotion {


    @Override
    public int getPromotionClassify() {
        return IPromotion.PROMOTION_SHOP;
    }


    @Override
    public boolean isMeetCondition(GoodsEntity entity) {
        return false;
    }

    @Override
    public boolean isMeetCondition(float money) {
        return money >= getCondition_value();
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

    @Override
    public boolean isLimitTime() {
        return false;
    }
}

package com.easygo.cashier.module.promotion.base;

public interface IShopPromotion extends IPromotion {

    /**是否满足促销条件*/
    boolean isMeetCondition(float money);
}

package com.easygo.cashier.module.promotion;

import com.easygo.cashier.adapter.GoodsEntity;

public interface IPromotion {

    /**促销类型 店铺促销*/
    int PROMOTION_SHOP = 0;
    /**促销类型 商品促销*/
    int PROMOTION_GOODS = 1;

    /**获取促销大类型， 店铺促销或者商品促销*/
    int getPromotionClassify();


    /**促销名称*/
    String getName();
    /**促销id*/
    int getId();

    /**是否满足促销条件*/
    boolean isMeetCondition(GoodsEntity entity);

    /**获取促销金额*/
    float getPromotionMoney();

    /**促销是否有时间限制*/
    boolean isLimitTime();


}

package com.easygo.cashier.module.promotion.base;

public interface IPromotion {

    /**促销类型 店铺促销*/
    int PROMOTION_SHOP = 0;
    /**促销类型 商品促销*/
    int PROMOTION_GOODS = 1;

    /**获取促销大类型， 店铺促销或者商品促销*/
    int getPromotionClassify();


    /**减免类型 金额*/
    int OFFER_TYPE_MONEY = 0;
    /**减免类型 比例*/
    int OFFER_TYPE_RATIO = 1;




    /**条件类型 偶数*/
    int CONDITION_TYPE_EVEN = 1;
    /**条件类型 金额*/
    int CONDITION_TYPE_MONEY = 2;

    /**活动类型 普通*/
    int TYPE_NORMAL = 1;
    /**活动类型 时段*/
    int TYPE_TIME = 2;
    /**活动类型 满额*/
    int TYPE_MEET = 3;
    /**活动类型 捆绑*/
    int TYPE_BUNDLE = 4;


    /**获取促销金额*/
    float getPromotionMoney(float money);

}

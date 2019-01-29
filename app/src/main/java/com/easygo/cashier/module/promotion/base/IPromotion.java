package com.easygo.cashier.module.promotion.base;

public interface IPromotion {

    /**促销类型 店铺促销*/
    int PROMOTION_SHOP = 0;
    /**促销类型 商品促销*/
    int PROMOTION_GOODS = 1;

    /**获取促销大类型， 店铺促销或者商品促销*/
    int getPromotionClassify();


    /**减免类型 金额*/
    int OFFER_TYPE_MONEY = 1;
    /**减免类型 比例*/
    int OFFER_TYPE_RATIO = 2;




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
    /**活动类型 固定*/
    int TYPE_FIXED = 5;







    String getName();
    void setName(String name);

    int getId();
    void setId(int id);

    int getType();
    void setType(int type);

    long getEffected_at();
    void setEffected_at(long effected_at);

    long getExpired_at();
    void setExpired_at(long expired_at);

    int getCondition_type();
    void setCondition_type(int condition_type);

    float getCondition_value();
    void setCondition_value(float condition_value);

    int getOffer_type();
    void setOffer_type(int offer_type);

    float getOffer_value();
    void setOffer_value(float offer_value);


}

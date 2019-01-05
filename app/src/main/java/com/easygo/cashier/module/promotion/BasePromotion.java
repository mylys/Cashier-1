package com.easygo.cashier.module.promotion;

public class BasePromotion {

    /**促销名称*/
    protected String name;

    /**促销id*/
    protected int id;

    /**条件类型,  1:偶数件  2：金额*/
    protected int condition_type;
    /**促销条件金额*/
    protected float condition_value;


    /**促销类型,  1：减免  2：折扣*/
    protected int offer_type;
    /**促销金额*/
    protected float offer_value;


}

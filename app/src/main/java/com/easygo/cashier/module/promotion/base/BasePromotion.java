package com.easygo.cashier.module.promotion.base;

public class BasePromotion implements IPromotion{

    /**促销名称*/
    protected String name;

    /**促销id*/
    protected int id;

    /**活动类型*/
    protected int type;

    /**开始时间*/
    protected long effected_at;
    /**结束时间*/
    protected long expired_at;


    /**条件类型,  1:偶数件  2：金额*/
    protected int condition_type;
    /**促销条件金额*/
    protected float condition_value;


    /**促销类型,  1：减免  2：折扣*/
    protected int offer_type;
    /**促销金额或者比例， 根据促销类型决定*/
    protected float offer_value;


    @Override
    public int getPromotionClassify() {
        return IPromotion.PROMOTION_SHOP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getEffected_at() {
        return effected_at;
    }

    public void setEffected_at(long effected_at) {
        this.effected_at = effected_at;
    }

    public long getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(long expired_at) {
        this.expired_at = expired_at;
    }

    public int getCondition_type() {
        return condition_type;
    }

    public void setCondition_type(int condition_type) {
        this.condition_type = condition_type;
    }

    public float getCondition_value() {
        return condition_value;
    }

    public void setCondition_value(float condition_value) {
        this.condition_value = condition_value;
    }

    public int getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(int offer_type) {
        this.offer_type = offer_type;
    }

    public float getOffer_value() {
        return offer_value;
    }

    public void setOffer_value(float offer_value) {
        this.offer_value = offer_value;
    }
}

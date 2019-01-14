package com.easygo.cashier.bean;

public class CouponResponse {


    /**
     * name : 明天见
     * condition_value : 1.00
     * offer_type : 2
     * offer_type_str : 折扣券
     * offer_value : 18
     * effected_at : 1547395200000
     * expired_at : 1547567999000
     */

    private String name;
    private String condition_value;
    private int offer_type;
    private String offer_type_str;
    private int offer_value;
    private long effected_at;
    private long expired_at;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition_value() {
        return condition_value;
    }

    public void setCondition_value(String condition_value) {
        this.condition_value = condition_value;
    }

    public int getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(int offer_type) {
        this.offer_type = offer_type;
    }

    public String getOffer_type_str() {
        return offer_type_str;
    }

    public void setOffer_type_str(String offer_type_str) {
        this.offer_type_str = offer_type_str;
    }

    public int getOffer_value() {
        return offer_value;
    }

    public void setOffer_value(int offer_value) {
        this.offer_value = offer_value;
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
}

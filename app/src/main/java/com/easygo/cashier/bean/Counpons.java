package com.easygo.cashier.bean;

/**
 * @Describe：
 * @Date：2019-02-15
 */
public class Counpons {
    private float coupons;
    private CouponResponse bean;

    public float getCoupons() {
        return coupons;
    }

    public void setCoupons(float coupons) {
        this.coupons = coupons;
    }

    public CouponResponse getBean() {
        return bean;
    }

    public void setBean(CouponResponse bean) {
        this.bean = bean;
    }
}

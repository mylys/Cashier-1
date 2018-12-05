package com.easygo.cashier.bean;

public class RealMoneyResponse {


    /**
     * sum_money : 0.1
     * real_money : 0.1
     * activity_sn :
     * activity_name :
     * discount_money : 0
     * coupon_sn : 优惠卷
     */

    private double sum_money;
    private double real_money;
    private String activity_sn;
    private String activity_name;
    private int discount_money;
    private String coupon_sn;

    public double getSum_money() {
        return sum_money;
    }

    public void setSum_money(double sum_money) {
        this.sum_money = sum_money;
    }

    public double getReal_money() {
        return real_money;
    }

    public void setReal_money(double real_money) {
        this.real_money = real_money;
    }

    public String getActivity_sn() {
        return activity_sn;
    }

    public void setActivity_sn(String activity_sn) {
        this.activity_sn = activity_sn;
    }

    public String getActivity_name() {
        return activity_name;
    }

    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    public int getDiscount_money() {
        return discount_money;
    }

    public void setDiscount_money(int discount_money) {
        this.discount_money = discount_money;
    }

    public String getCoupon_sn() {
        return coupon_sn;
    }

    public void setCoupon_sn(String coupon_sn) {
        this.coupon_sn = coupon_sn;
    }
}

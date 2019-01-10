package com.easygo.cashier.bean;

/**
 * @Describe：
 * @Date：2019-01-10
 */
public class MemberDiscountInfo {

    /**
     * rc_id : 8
     * shop_name : 测试测试123213
     * discount_type : 1
     * discount_amount : 10
     * shop_id : 593
     * is_enabled : 1
     */

    private int rc_id;
    private String shop_name;
    private int discount_type;
    private int discount_amount;
    private int shop_id;
    private int is_enabled;

    public int getRc_id() {
        return rc_id;
    }

    public void setRc_id(int rc_id) {
        this.rc_id = rc_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getDiscount_type() {
        return discount_type;
    }

    public void setDiscount_type(int discount_type) {
        this.discount_type = discount_type;
    }

    public int getDiscount_amount() {
        return discount_amount;
    }

    public void setDiscount_amount(int discount_amount) {
        this.discount_amount = discount_amount;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(int is_enabled) {
        this.is_enabled = is_enabled;
    }
}

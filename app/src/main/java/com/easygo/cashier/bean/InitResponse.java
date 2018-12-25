package com.easygo.cashier.bean;

public class InitResponse {


    /**
     * shop_id : 592
     * shop_name : 测试设备
     * shop_sn : 0001
     * is_reserve : 0
     */

    private int shop_id;
    private String shop_name;
    private String shop_sn;
    private int is_reserve;

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_sn() {
        return shop_sn;
    }

    public void setShop_sn(String shop_sn) {
        this.shop_sn = shop_sn;
    }

    public int getIs_reserve() {
        return is_reserve;
    }

    public void setIs_reserve(int is_reserve) {
        this.is_reserve = is_reserve;
    }
}

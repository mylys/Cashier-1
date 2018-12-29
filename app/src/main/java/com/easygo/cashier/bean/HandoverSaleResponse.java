package com.easygo.cashier.bean;

public class HandoverSaleResponse {


    /**
     * g_sku_name : 脉动青柠味饮料
     * g_c_name : null
     * s_sku_id : 111859
     * type : 0
     * quantity : 3
     * sell_price : 0.02
     * refund : 0
     * count : 3
     * money : 0.06
     */

    private String g_sku_name;
    private Object g_c_name;
    private int s_sku_id;
    private int type;
    private int quantity;
    private String sell_price;
    private int refund;
    private int count;
    private double money;

    public String getG_sku_name() {
        return g_sku_name;
    }

    public void setG_sku_name(String g_sku_name) {
        this.g_sku_name = g_sku_name;
    }

    public String getG_c_name() {
        return g_c_name == null? "": (String) g_c_name;
    }

    public void setG_c_name(Object g_c_name) {
        this.g_c_name = g_c_name;
    }

    public int getS_sku_id() {
        return s_sku_id;
    }

    public void setS_sku_id(int s_sku_id) {
        this.s_sku_id = s_sku_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}

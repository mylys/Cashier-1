package com.easygo.cashier.bean;

import android.text.TextUtils;

public class HandoverSaleResponse {


    /**
     * g_sku_name : 脉动青柠味饮料
     * g_c_name : null
     * s_sku_id : 111859
     * type : 0
     * quantity : 3
     * sell_price : 0.02
     * discount : 0.02
     * cashier_discount : 0.02
     * refund : 0
     * count : 3
     * money : 0.06
     * g_u_symbol : g
     */

    private String g_sku_name;
    private String g_c_name;
    private int s_sku_id;
    private int type;
    private int quantity;
    private String sell_price;
    private String discount;
    private String cashier_discount;
    private int refund;
    private float count;
    private double money;
    private String g_u_symbol;

    public String getG_sku_name() {
        return g_sku_name;
    }

    public void setG_sku_name(String g_sku_name) {
        this.g_sku_name = g_sku_name;
    }

    public String getG_c_name() {
        return TextUtils.isEmpty(g_c_name)? "": g_c_name;
    }

    public void setG_c_name(String g_c_name) {
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

    public String getDiscount() {
        return !TextUtils.isEmpty(cashier_discount) && !"0".equals(cashier_discount) && !"0.00".equals(cashier_discount)?
                cashier_discount: discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCashier_discount() {
        return cashier_discount;
    }

    public void setCashier_discount(String cashier_discount) {
        this.cashier_discount = cashier_discount;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getG_u_symbol() {
        return g_u_symbol;
    }

    public void setG_u_symbol(String g_u_symbol) {
        this.g_u_symbol = g_u_symbol;
    }
}

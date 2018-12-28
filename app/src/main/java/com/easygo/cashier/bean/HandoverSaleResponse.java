package com.easygo.cashier.bean;

public class HandoverSaleResponse {


    /**
     * o_i_id : 810340
     * g_sku_id : 0
     * barcode : null
     * g_sku_name : 无码商品
     * g_c_name : null
     * is_weigh : null
     * cashier_id : 358
     * pay_time : 1545968381
     * status : 2
     * g_u_id : 0
     * g_u_name :
     * purchase_price : 0.00
     * sell_price : 5.00
     * count : 1
     * money : 5
     */

    private int o_i_id;
    private int g_sku_id;
    private Object barcode;
    private String g_sku_name;
    private Object g_c_name;
    private Object is_weigh;
    private int cashier_id;
    private int pay_time;
    private int status;
    private int g_u_id;
    private String g_u_name;
    private String purchase_price;
    private String sell_price;
    private String count;
    private float money;

    public int getO_i_id() {
        return o_i_id;
    }

    public void setO_i_id(int o_i_id) {
        this.o_i_id = o_i_id;
    }

    public int getG_sku_id() {
        return g_sku_id;
    }

    public void setG_sku_id(int g_sku_id) {
        this.g_sku_id = g_sku_id;
    }

    public Object getBarcode() {
        return barcode;
    }

    public void setBarcode(Object barcode) {
        this.barcode = barcode;
    }

    public String getG_sku_name() {
        return g_sku_name;
    }

    public void setG_sku_name(String g_sku_name) {
        this.g_sku_name = g_sku_name;
    }

    public Object getG_c_name() {
        return g_c_name;
    }

    public void setG_c_name(Object g_c_name) {
        this.g_c_name = g_c_name;
    }

    public Object getIs_weigh() {
        return is_weigh;
    }

    public void setIs_weigh(Object is_weigh) {
        this.is_weigh = is_weigh;
    }

    public int getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(int cashier_id) {
        this.cashier_id = cashier_id;
    }

    public int getPay_time() {
        return pay_time;
    }

    public void setPay_time(int pay_time) {
        this.pay_time = pay_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getG_u_id() {
        return g_u_id;
    }

    public void setG_u_id(int g_u_id) {
        this.g_u_id = g_u_id;
    }

    public String getG_u_name() {
        return g_u_name;
    }

    public void setG_u_name(String g_u_name) {
        this.g_u_name = g_u_name;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}

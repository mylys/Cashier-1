package com.easygo.cashier.bean;

public class HandoverSaleResponse {


    /**
     * g_sku_id : 0
     * barcode : null
     * g_sku_name : 无码商品
     * g_c_name : null
     * purchase_price : 0.00
     * sell_price : 2.00
     * count : 1
     * money : 2
     */

    private int g_sku_id;
    private Object barcode;
    private String g_sku_name;
    private Object g_c_name;
    private String purchase_price;
    private String sell_price;
    private int count;
    private float money;

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }
}

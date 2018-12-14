package com.easygo.cashier.bean;


import java.io.Serializable;

public class GoodsResponse implements Serializable {


    /**
     * g_sku_id : 5
     * company_id : 0
     * g_sku_name : 美国柯可蓝天然矿泉水 500mL
     * first_c_id : 185
     * second_c_id : 213
     * three_c_id : null
     * four_c_id : null
     * g_c_name : 饮料类-纯水类
     * pic_big :
     * barcode : 096619756803
     * g_u_id : 12
     * g_u_name : 瓶
     * purchase_price : 2.00
     * price : 4.00
     * discount_price : 0.00
     * membership_price : 0.00
     * on_sale_count : 1
     * birthday_price : 0.00
     * sell_status : 1
     * status : 1
     */

    private int g_sku_id;
    private int company_id;
    private String g_sku_name;
    private int first_c_id;
    private int second_c_id;
    private Object three_c_id;
    private Object four_c_id;
    private String g_c_name;
    private String pic_big;
    private String barcode;
    private int g_u_id;
    private String g_u_name;
    private String purchase_price;
    private String price;
    private String discount_price;
    private String membership_price;
    private int on_sale_count;
    private String birthday_price;
    private int sell_status;
    private int status;

    public int getG_sku_id() {
        return g_sku_id;
    }

    public void setG_sku_id(int g_sku_id) {
        this.g_sku_id = g_sku_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getG_sku_name() {
        return g_sku_name;
    }

    public void setG_sku_name(String g_sku_name) {
        this.g_sku_name = g_sku_name;
    }

    public int getFirst_c_id() {
        return first_c_id;
    }

    public void setFirst_c_id(int first_c_id) {
        this.first_c_id = first_c_id;
    }

    public int getSecond_c_id() {
        return second_c_id;
    }

    public void setSecond_c_id(int second_c_id) {
        this.second_c_id = second_c_id;
    }

    public Object getThree_c_id() {
        return three_c_id;
    }

    public void setThree_c_id(Object three_c_id) {
        this.three_c_id = three_c_id;
    }

    public Object getFour_c_id() {
        return four_c_id;
    }

    public void setFour_c_id(Object four_c_id) {
        this.four_c_id = four_c_id;
    }

    public String getG_c_name() {
        return g_c_name;
    }

    public void setG_c_name(String g_c_name) {
        this.g_c_name = g_c_name;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getMembership_price() {
        return membership_price;
    }

    public void setMembership_price(String membership_price) {
        this.membership_price = membership_price;
    }

    public int getOn_sale_count() {
        return on_sale_count;
    }

    public void setOn_sale_count(int on_sale_count) {
        this.on_sale_count = on_sale_count;
    }

    public String getBirthday_price() {
        return birthday_price;
    }

    public void setBirthday_price(String birthday_price) {
        this.birthday_price = birthday_price;
    }

    public int getSell_status() {
        return sell_status;
    }

    public void setSell_status(int sell_status) {
        this.sell_status = sell_status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

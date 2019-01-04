package com.easygo.cashier.bean;

public class HandoverResponse {


    /**
     * end_time : 2018-12-14 15:11:46
     * all_money : null
     * cash_money : null
     * wx_money : null
     * alipay_money : null
     * all_refund : null
     * cash_refund : null
     * cash_change : null
     * sku_count : null
     * goods_count : null
     * sale_count : 0
     * refund_count : 0
     * reserve_money : 0.00
     * start_time : 1970-01-01 08:00:00
     */

    private String end_time;
    private float all_money;
    private float cash_money;
    private float wx_money;
    private float alipay_money;
    private float all_refund;
    private float cash_refund;
    private float cash_change;
    private int sku_count;
    private int goods_count;
    private int sale_count;
    private int refund_count;
    private String reserve_money;
    private String start_time;

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public float getAll_money() {
        return all_money;
    }

    public void setAll_money(float all_money) {
        this.all_money = all_money;
    }

    public float getCash_money() {
        return cash_money;
    }

    public void setCash_money(float cash_money) {
        this.cash_money = cash_money;
    }

    public float getWx_money() {
        return wx_money;
    }

    public void setWx_money(float wx_money) {
        this.wx_money = wx_money;
    }

    public float getAlipay_money() {
        return alipay_money;
    }

    public void setAlipay_money(float alipay_money) {
        this.alipay_money = alipay_money;
    }

    public float getAll_refund() {
        return all_refund;
    }

    public void setAll_refund(float all_refund) {
        this.all_refund = all_refund;
    }

    public float getCash_refund() {
        return cash_refund;
    }

    public void setCash_refund(float cash_refund) {
        this.cash_refund = cash_refund;
    }

    public float getCash_change() {
        return cash_change;
    }

    public void setCash_change(float cash_change) {
        this.cash_change = cash_change;
    }

    public int getSku_count() {
        return sku_count;
    }

    public void setSku_count(int sku_count) {
        this.sku_count = sku_count;
    }

    public int getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(int goods_count) {
        this.goods_count = goods_count;
    }

    public int getSale_count() {
        return sale_count;
    }

    public void setSale_count(int sale_count) {
        this.sale_count = sale_count;
    }

    public int getRefund_count() {
        return refund_count;
    }

    public void setRefund_count(int refund_count) {
        this.refund_count = refund_count;
    }

    public String getReserve_money() {
        return reserve_money;
    }

    public void setReserve_money(String reserve_money) {
        this.reserve_money = reserve_money;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
}

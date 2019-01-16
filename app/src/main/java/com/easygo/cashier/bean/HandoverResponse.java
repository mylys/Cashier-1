package com.easygo.cashier.bean;

import android.text.TextUtils;

public class HandoverResponse {


    /**
     * end_time : 2019-01-14 21:42:28
     * all_money : 5.00
     * cash_money : 5.00
     * wx_money :
     * alipay_money :
     * all_refund :
     * cash_refund :
     * cash_change : 1.00
     * sku_count : 1
     * goods_count : 1
     * sale_count : 1
     * refund_count : 0
     * reserve_money : 500.00
     * start_time : 2019-01-14 21:42:04
     */

    private String end_time;
    private String all_money;
    private String cash_money;
    private String wx_money;
    private String alipay_money;
    private String all_refund;
    private String cash_refund;
    private String cash_change;
    private String sku_count;
    private String goods_count;
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

    public String getAll_money() {
        return TextUtils.isEmpty(all_money)? "0.00": all_money;
    }

    public void setAll_money(String all_money) {
        this.all_money = all_money;
    }

    public String getCash_money() {
        return TextUtils.isEmpty(cash_money)? "0.00": all_money;
    }

    public void setCash_money(String cash_money) {
        this.cash_money = cash_money;
    }

    public String getWx_money() {
        return TextUtils.isEmpty(wx_money)? "0.00": all_money;
    }

    public void setWx_money(String wx_money) {
        this.wx_money = wx_money;
    }

    public String getAlipay_money() {
        return TextUtils.isEmpty(alipay_money)? "0.00": all_money;
    }

    public void setAlipay_money(String alipay_money) {
        this.alipay_money = alipay_money;
    }

    public String getAll_refund() {
        return TextUtils.isEmpty(all_refund)? "0.00": all_money;
    }

    public void setAll_refund(String all_refund) {
        this.all_refund = all_refund;
    }

    public String getCash_refund() {
        return TextUtils.isEmpty(cash_refund)? "0.00": all_money;
    }

    public void setCash_refund(String cash_refund) {
        this.cash_refund = cash_refund;
    }

    public String getCash_change() {
        return TextUtils.isEmpty(cash_change)? "0.00": all_money;
    }

    public void setCash_change(String cash_change) {
        this.cash_change = cash_change;
    }

    public String getSku_count() {
        return TextUtils.isEmpty(sku_count)? "0": all_money;
    }

    public void setSku_count(String sku_count) {
        this.sku_count = sku_count;
    }

    public String getGoods_count() {
        return TextUtils.isEmpty(goods_count)? "0": all_money;
    }

    public void setGoods_count(String goods_count) {
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
        return TextUtils.isEmpty(reserve_money)? "0.00": all_money;
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

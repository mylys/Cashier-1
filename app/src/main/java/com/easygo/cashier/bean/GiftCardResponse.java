package com.easygo.cashier.bean;

import android.text.TextUtils;

public class GiftCardResponse {

    private int gc_id;
    private String sn;
    private String card_no;
    private String balance_amount;
    private String total_amount;

    public int getGc_id() {
        return gc_id;
    }

    public void setGc_id(int gc_id) {
        this.gc_id = gc_id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public float getBalance_amount() {
        return TextUtils.isEmpty(balance_amount)? 0: Float.valueOf(balance_amount);
    }

    public void setBalance_amount(String balance_amount) {
        this.balance_amount = balance_amount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }
}

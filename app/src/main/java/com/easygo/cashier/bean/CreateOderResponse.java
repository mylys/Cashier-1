package com.easygo.cashier.bean;

public class CreateOderResponse {


    /**
     * trade_no : eg201812131542103232
     * trade_num : 2018121315421050539710
     */

    private String trade_no;
    private String trade_num;

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTrade_num() {
        return trade_num;
    }

    public void setTrade_num(String trade_num) {
        this.trade_num = trade_num;
    }
}

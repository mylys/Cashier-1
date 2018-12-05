package com.easygo.cashier.bean.request;

import java.util.List;

public class CreateOrderRequestBody {


    /**
     * openid : Shanghai
     * shop_id : 411
     * list : [{"barcode":"6923450605226","goods_count":1}]
     * total_money :
     * real_pay :
     * act_sn :
     * coun_sn :
     * integral :
     * wallet_pay :
     * union_id :
     * card_no :
     */

    private String openid;
    private String shop_id;
    private List<GoodsBean> goods;
    private String total_money;
    private String real_pay;
    private String act_sn;
    private String coun_sn;
    private int integral;
    private int wallet_pay;
    private String union_id;
    private String card_no;
    private int member_money;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public static class GoodsBean {
        /**
         * barcode : 6923450605226
         * goods_count : 1
         */

        private String barcode;
        private int goods_count;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public int getGoods_count() {
            return goods_count;
        }

        public void setGoods_count(int goods_count) {
            this.goods_count = goods_count;
        }
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getReal_pay() {
        return real_pay;
    }

    public void setReal_pay(String real_pay) {
        this.real_pay = real_pay;
    }

    public String getAct_sn() {
        return act_sn;
    }

    public void setAct_sn(String act_sn) {
        this.act_sn = act_sn;
    }

    public String getCoun_sn() {
        return coun_sn;
    }

    public void setCoun_sn(String coun_sn) {
        this.coun_sn = coun_sn;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getWallet_pay() {
        return wallet_pay;
    }

    public void setWallet_pay(int wallet_pay) {
        this.wallet_pay = wallet_pay;
    }

    public String getUnion_id() {
        return union_id;
    }

    public void setUnion_id(String union_id) {
        this.union_id = union_id;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public int getMember_money() {
        return member_money;
    }

    public void setMember_money(int member_money) {
        this.member_money = member_money;
    }
}

package com.easygo.cashier.bean.request;


import java.util.List;

public class CreateOrderRequestBody {


    /**
     * goods_list : [{"g_sku_id":5,"count":1,"price":400,"barcode":"096619756803"},{"g_sku_id":200,"count":2,"price":1000,"barcode":"8885012290555"}]
     * goods_count : 2
     * cashier_id : 358
     * shop_sn : GZ1002
     * member_id : 633
     * member_card_no : EG
     * total_money : 2400
     * real_pay : 2400
     */

    private int goods_count;
    private int cashier_id;
    private String shop_sn;
    private int member_id;
    private String member_card_no;
    private int total_money;
    private int real_pay;
    private String coupon_sn;
    private int coupon_discount;
    private List<GoodsListBean> goods_list;
    private List<ActivitiesBean> activities;

    public int getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(int goods_count) {
        this.goods_count = goods_count;
    }

    public int getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(int cashier_id) {
        this.cashier_id = cashier_id;
    }

    public String getShop_sn() {
        return shop_sn;
    }

    public void setShop_sn(String shop_sn) {
        this.shop_sn = shop_sn;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember_card_no() {
        return member_card_no;
    }

    public void setMember_card_no(String member_card_no) {
        this.member_card_no = member_card_no;
    }

    public int getTotal_money() {
        return total_money;
    }

    public void setTotal_money(int total_money) {
        this.total_money = total_money;
    }

    public int getReal_pay() {
        return real_pay;
    }

    public void setReal_pay(int real_pay) {
        this.real_pay = real_pay;
    }

    public String getCoupon_sn() {
        return coupon_sn;
    }

    public void setCoupon_sn(String coupon_sn) {
        this.coupon_sn = coupon_sn;
    }

    public int getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(int coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public List<ActivitiesBean> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivitiesBean> activities) {
        this.activities = activities;
    }

    public static class GoodsListBean {
        /**
         * g_sku_id : 5
         * count : 1
         * price : 400
         * type : 0
         * identity : "0"
         * barcode : 096619756803
         * discount :
         */

        private int g_sku_id;
        private int count;
        private int price;
        /**
         * 0: 非重量  1：重量  2：无码商品 3: 加工商品
         */
        private int type;
        /**
         * 唯一识别码， 扫到商品时的时间戳（毫秒）
         */
        private String identity;
        private String barcode;
        private int discount;

        public int getG_sku_id() {
            return g_sku_id;
        }

        public void setG_sku_id(int g_sku_id) {
            this.g_sku_id = g_sku_id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }
    }

    public static class ActivitiesBean {

        private int id;
        private String type;
        private int discount;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }
    }
}

package com.easygo.cashier.bean.request;


import java.util.List;

public class CreateOrderRequestBody {


    /**
     * goods_list : [{"g_sku_id":5,"count":1,"price":400,"barcode":"096619756803"},{"g_sku_id":200,"count":2,"price":1000,"barcode":"8885012290555"}]
     * goods_count : 2
     * shop_sn : GZ1002
     * total_money : 2400
     * real_pay : 2400
     */

    private int goods_count;
    private String shop_sn;
    private int total_money;
    private int real_pay;
    private List<GoodsListBean> goods_list;

    public int getGoods_count() {
        return goods_count;
    }

    public void setGoods_count(int goods_count) {
        this.goods_count = goods_count;
    }

    public String getShop_sn() {
        return shop_sn;
    }

    public void setShop_sn(String shop_sn) {
        this.shop_sn = shop_sn;
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

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }

    public static class GoodsListBean {
        /**
         * g_sku_id : 5
         * count : 1
         * price : 400
         * barcode : 096619756803
         */

        private int g_sku_id;
        private int count;
        private int price;
        private String barcode;

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

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }
    }
}

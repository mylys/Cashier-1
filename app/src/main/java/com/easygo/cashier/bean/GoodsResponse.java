package com.easygo.cashier.bean;

import java.util.List;

public class GoodsResponse {

//    private List<GoodsBean> list;

//    public static class GoodsBean {

        /**
         * on_sale_count : 11
         * price : 10.00
         * g_sku_id : 2
         * g_sku_name : 美国柯可蓝天然矿泉水 1L
         * first_c_id : 185
         * second_c_id : 213
         * g_c_name : 饮料类-纯水类
         * pic_big : null
         * barcode : 096619438839
         */

        private String on_sale_count;
        private String price;
        private String g_sku_id;
        private String g_sku_name;
        private String first_c_id;
        private String second_c_id;
        private String g_c_name;
        private Object pic_big;
        private String barcode;

        public String getOn_sale_count() {
            return on_sale_count;
        }

        public void setOn_sale_count(String on_sale_count) {
            this.on_sale_count = on_sale_count;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getG_sku_id() {
            return g_sku_id;
        }

        public void setG_sku_id(String g_sku_id) {
            this.g_sku_id = g_sku_id;
        }

        public String getG_sku_name() {
            return g_sku_name;
        }

        public void setG_sku_name(String g_sku_name) {
            this.g_sku_name = g_sku_name;
        }

        public String getFirst_c_id() {
            return first_c_id;
        }

        public void setFirst_c_id(String first_c_id) {
            this.first_c_id = first_c_id;
        }

        public String getSecond_c_id() {
            return second_c_id;
        }

        public void setSecond_c_id(String second_c_id) {
            this.second_c_id = second_c_id;
        }

        public String getG_c_name() {
            return g_c_name;
        }

        public void setG_c_name(String g_c_name) {
            this.g_c_name = g_c_name;
        }

        public Object getPic_big() {
            return pic_big;
        }

        public void setPic_big(Object pic_big) {
            this.pic_big = pic_big;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }
//    }
//
//    public List<GoodsBean> getList() {
//        return list;
//    }
//
//    public void setList(List<GoodsBean> list) {
//        this.list = list;
//    }
}

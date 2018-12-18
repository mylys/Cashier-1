package com.easygo.cashier.bean;

import java.util.List;

public class HandoverSaleResponse {

    private List<ResultBean> result;

    public static class ResultBean {


        /**
         * g_sku_id : 12988
         * barcode : 6921168550111
         * g_sku_name : 力量帝维他命热带水果味500ml
         * g_c_name : 饮料类-功能性类
         * purchase_price : 3.50
         * sell_price : 6.00
         * count : 1
         * money : 6
         */


        private int g_sku_id;
        private String barcode;
        private String g_sku_name;
        private String g_c_name;
        private String purchase_price;
        private String sell_price;
        private int count;
        private int money;

        public int getG_sku_id() {
            return g_sku_id;
        }

        public void setG_sku_id(int g_sku_id) {
            this.g_sku_id = g_sku_id;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getG_sku_name() {
            return g_sku_name;
        }

        public void setG_sku_name(String g_sku_name) {
            this.g_sku_name = g_sku_name;
        }

        public String getG_c_name() {
            return g_c_name;
        }

        public void setG_c_name(String g_c_name) {
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

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }
}

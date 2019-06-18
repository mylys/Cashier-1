package com.easygo.cashier.bean;

import android.text.TextUtils;

import java.util.List;

/**
 * @Describe：
 * @Date：2019-06-15
 */
public class WeightBean {

    private List<SkuListBean> sku_list;
    private List<CateListBean> cate_list;

    public List<CateListBean> getCate_list() {
        return cate_list;
    }

    public void setCate_list(List<CateListBean> cate_list) {
        this.cate_list = cate_list;
    }

    public List<SkuListBean> getSku_list() {
        return sku_list;
    }

    public void setSku_list(List<SkuListBean> sku_list) {
        this.sku_list = sku_list;
    }

    public static class CateListBean{
        private int g_c_id;
        private String g_c_name;

        public int getG_c_id() {
            return g_c_id;
        }

        public void setG_c_id(int g_c_id) {
            this.g_c_id = g_c_id;
        }

        public String getG_c_name() {
            return g_c_name;
        }

        public void setG_c_name(String g_c_name) {
            this.g_c_name = g_c_name;
        }
    }

    public static class SkuListBean {
        /**
         * g_c_id : 1094
         * g_sku_name : 海尔商品D
         * g_c_name : 海尔1-海尔2-1-海尔3-海尔4-海尔6
         * barcode : 135636353636
         * pic_big : null
         * g_u_name : 斤
         * price : 0.01
         */

        private int g_c_id;
        private String g_sku_name;
        private String g_c_name;
        private String barcode;
        private String pic_big;
        private String g_u_name;
        private String price;
        private boolean select;

        public boolean isSelect() {
            return select;
        }

        public void setSelect(boolean select) {
            this.select = select;
        }

        public int getG_c_id() {
            return g_c_id;
        }

        public void setG_c_id(int g_c_id) {
            this.g_c_id = g_c_id;
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

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getPic_big() {
            return TextUtils.isEmpty(pic_big) ? "" : pic_big;
        }

        public void setPic_big(String pic_big) {
            this.pic_big = pic_big;
        }

        public String getG_u_name() {
            return g_u_name;
        }

        public void setG_u_name(String g_u_name) {
            this.g_u_name = g_u_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }
}

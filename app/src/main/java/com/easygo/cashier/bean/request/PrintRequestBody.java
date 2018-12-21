package com.easygo.cashier.bean.request;

import java.util.List;

public class PrintRequestBody {


    private int goods_count;
    private String shop_sn;
    private String order_no;
    private String printer_sn;
    private int times;
    private List<GoodsListBean> goods_list ;

    public static class GoodsListBean {

        private String goods_name;
        private String count;
        private int discount;
        private int price;
        private int count_price;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public int getCount_price() {
            return count_price;
        }

        public void setCount_price(int count_price) {
            this.count_price = count_price;
        }
    }

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

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getPrinter_sn() {
        return printer_sn;
    }

    public void setPrinter_sn(String printer_sn) {
        this.printer_sn = printer_sn;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public List<GoodsListBean> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsListBean> goods_list) {
        this.goods_list = goods_list;
    }
}

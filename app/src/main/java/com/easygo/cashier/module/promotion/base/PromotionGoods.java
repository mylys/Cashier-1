package com.easygo.cashier.module.promotion.base;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 顾客所购买的商品中 参与促销的所有商品信息
 */
public class PromotionGoods {

    public static final int FLAG_DONT_SET_PROMOTION_MONEY = -1;
    public static final int FLAG_NEED_SET_PROMOTION_MONEY = 0;

    private int total_count;//总件数
    private float total_money;//总额
    private List<GoodsBean> goodsBeans = new ArrayList<>();//商品信息


    public static class GoodsBean implements Comparable<GoodsBean> {

        /**
         * 在顾客购买商品列表中的序号
         */
        private int index = -1;
        private String barcode;
        private int count;
        private float price;
        private float subtotal;
        /**
         * 促销金额
         */
        private float promotion_money = FLAG_DONT_SET_PROMOTION_MONEY;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(float subtotal) {
            this.subtotal = subtotal;
        }

        public float getPromotion_money() {
            return promotion_money;
        }

        public void setPromotion_money(float promotion_money) {
            this.promotion_money = promotion_money;
        }

        @Override
        public int compareTo(@NonNull GoodsBean o) {
            float result = getPrice() - o.getPrice();
            if(result > 0) {
                return 1;
            } else if(result == 0){
                return 0;
            } else {
                return -1;
            }
        }
    }



    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public float getTotal_money() {
        return total_money;
    }

    public void setTotal_money(float total_money) {
        this.total_money = total_money;
    }

    public List<GoodsBean> getGoodsBeans() {
        return goodsBeans;
    }

    public void setGoodsBeans(List<GoodsBean> goodsBeans) {
        this.goodsBeans = goodsBeans;
    }
}

package com.easygo.cashier.bean;

import java.util.List;

/**
 * @Describe：
 * @Date：2019-01-10
 */
public class MemberDayInfo {

    /**
     * rc_id : 14
     * shop_name : 测试测试123213
     * type : 1
     * shop_id : 593
     * is_enabled : 1
     * items : [{"day":1,"discount_type":2,"discount_amount":10,"promo_id":14,"least_cost":1024,"rc_id":72},{"day":2,"discount_type":1,"discount_amount":2,"promo_id":14,"least_cost":2048,"rc_id":73},{"day":1,"discount_type":2,"discount_amount":10,"promo_id":14,"least_cost":1024,"rc_id":74},{"day":2,"discount_type":1,"discount_amount":2,"promo_id":14,"least_cost":2048,"rc_id":75},{"day":1,"discount_type":2,"discount_amount":10,"promo_id":14,"least_cost":1024,"rc_id":76},{"day":2,"discount_type":1,"discount_amount":2,"promo_id":14,"least_cost":2048,"rc_id":77}]
     */

    private int rc_id;
    private String shop_name;
    private int type;
    private int shop_id;
    private int is_enabled;
    private List<ItemsBean> items;

    public int getRc_id() {
        return rc_id;
    }

    public void setRc_id(int rc_id) {
        this.rc_id = rc_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(int is_enabled) {
        this.is_enabled = is_enabled;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean {
        /**
         * day : 1
         * discount_type : 2
         * discount_amount : 10
         * promo_id : 14
         * least_cost : 1024
         * rc_id : 72
         */

        private int day;
        private int discount_type;
        private int discount_amount;
        private int promo_id;
        private int least_cost;
        private int rc_id;

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getDiscount_type() {
            return discount_type;
        }

        public void setDiscount_type(int discount_type) {
            this.discount_type = discount_type;
        }

        public int getDiscount_amount() {
            return discount_amount;
        }

        public void setDiscount_amount(int discount_amount) {
            this.discount_amount = discount_amount;
        }

        public int getPromo_id() {
            return promo_id;
        }

        public void setPromo_id(int promo_id) {
            this.promo_id = promo_id;
        }

        public int getLeast_cost() {
            return least_cost;
        }

        public void setLeast_cost(int least_cost) {
            this.least_cost = least_cost;
        }

        public int getRc_id() {
            return rc_id;
        }

        public void setRc_id(int rc_id) {
            this.rc_id = rc_id;
        }
    }
}

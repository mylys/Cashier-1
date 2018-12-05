package com.easygo.cashier.bean;

public class CheckPayStatusResponse {


    /**
     * is_pay : 1
     * order_info : {"total_money":12,"real_pay":11,"coupon_name":"优惠大酬宾","discount_money":1}
     */

    private int is_pay;
    private OrderInfoBean order_info;

    public int getIs_pay() {
        return is_pay;
    }

    public void setIs_pay(int is_pay) {
        this.is_pay = is_pay;
    }

    public OrderInfoBean getOrder_info() {
        return order_info;
    }

    public void setOrder_info(OrderInfoBean order_info) {
        this.order_info = order_info;
    }

    public static class OrderInfoBean {
        /**
         * total_money : 12
         * real_pay : 11
         * coupon_name : 优惠大酬宾
         * discount_money : 1
         */

        private int total_money;
        private int real_pay;
        private String coupon_name;
        private int discount_money;

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

        public String getCoupon_name() {
            return coupon_name;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public int getDiscount_money() {
            return discount_money;
        }

        public void setDiscount_money(int discount_money) {
            this.discount_money = discount_money;
        }
    }
}

package com.easygo.cashier.bean;

import java.util.List;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-21
 */
public class RequsetBody {
    private String order_no;
    private String shop_sn;
    private int refund_amount;
    private String pay_type;
    private List<GoodsList> goods_list;

    public RequsetBody(String order_no, String shop_sn, int refund_amount, String pay_type, List<GoodsList> goodsList) {
        this.order_no = order_no;
        this.shop_sn = shop_sn;
        this.refund_amount = refund_amount;
        this.pay_type = pay_type;
        this.goods_list = goodsList;
    }

    public List<GoodsList> getGoodsList() {
        return goods_list;
    }

    public void setGoodsList(List<GoodsList> goodsList) {
        this.goods_list = goodsList;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getShop_sn() {
        return shop_sn;
    }

    public void setShop_sn(String shop_sn) {
        this.shop_sn = shop_sn;
    }

    public int getRefund_amount() {
        return refund_amount;
    }

    public void setRefund_amount(int refund_amount) {
        this.refund_amount = refund_amount;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public static class GoodsList {
        private int s_sku_id;
        private int count;

        public GoodsList(int s_sku_id, int count) {
            this.s_sku_id = s_sku_id;
            this.count = count;
        }

        public int getS_sku_id() {
            return s_sku_id;
        }

        public void setS_sku_id(int s_sku_id) {
            this.s_sku_id = s_sku_id;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}

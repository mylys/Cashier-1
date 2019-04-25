package com.easygo.cashier.bean.request;

import java.util.List;

/**
 * 退款请求json
 * @Describe：
 * @author：hgeson
 * @date：2018-12-21
 */
public class RefundRequsetBody {
    private String order_no;
    private String shop_sn;
    private int refund_amount;
    private String pay_type;
    private String refund_password;
    private String admin_name;
    private String password;
    private List<GoodsList> goods_list;

    public RefundRequsetBody(String order_no, String shop_sn, int refund_amount, String pay_type, List<GoodsList> goodsList) {
        this.order_no = order_no;
        this.shop_sn = shop_sn;
        this.refund_amount = refund_amount;
        this.pay_type = pay_type;
        this.goods_list = goodsList;
    }

    public RefundRequsetBody(String order_no, String shop_sn, int refund_amount, String pay_type, String refund_password, String admin_name, String password, List<GoodsList> goods_list) {
        this.order_no = order_no;
        this.shop_sn = shop_sn;
        this.refund_amount = refund_amount;
        this.pay_type = pay_type;
        this.refund_password = refund_password;
        this.admin_name = admin_name;
        this.password = password;
        this.goods_list = goods_list;
    }

    public String getRefund_password() {
        return refund_password;
    }

    public void setRefund_password(String refund_password) {
        this.refund_password = refund_password;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<GoodsList> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsList> goods_list) {
        this.goods_list = goods_list;
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
        private float count;
        private int is_weigh;
        private String identity;
        /**类型   1：仅退款 2：退货退款*/
        private int type;

        public int getIs_weigh() {
            return is_weigh;
        }

        public void setIs_weigh(int is_weigh) {
            this.is_weigh = is_weigh;
        }

        public int getS_sku_id() {
            return s_sku_id;
        }

        public void setS_sku_id(int s_sku_id) {
            this.s_sku_id = s_sku_id;
        }

        public float getCount() {
            return count;
        }

        public void setCount(float count) {
            this.count = count;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}

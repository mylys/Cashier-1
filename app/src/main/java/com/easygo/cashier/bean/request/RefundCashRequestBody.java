package com.easygo.cashier.bean.request;

import com.easygo.cashier.bean.RequsetBody;

import java.util.List;

public class RefundCashRequestBody {

    private String shop_sn;
    private int refund_amount;
    private List<GoodsList> goods_list;

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

    public List<GoodsList> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<GoodsList> goods_list) {
        this.goods_list = goods_list;
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

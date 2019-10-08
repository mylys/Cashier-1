package com.easygo.cashier.bean.request;

import java.util.List;

public class RealMoneyRequestBody {


    /**
     * openid : Shanghai
     * shop_id : 411
     * goods : [{"barcode":"6923450605226","goods_count":1}]
     * union_id :
     * card_no :
     */

    private String openid;
    private String shop_id;
    private List<GoodsBean> goods;
    private String union_id;
    private String card_no;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * barcode : 6923450605226
         * goods_count : 1
         */

        private String barcode;
        private int goods_count;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public int getGoods_count() {
            return goods_count;
        }

        public void setGoods_count(int goods_count) {
            this.goods_count = goods_count;
        }
    }

    public String getUnion_id() {
        return union_id;
    }

    public void setUnion_id(String union_id) {
        this.union_id = union_id;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }
}

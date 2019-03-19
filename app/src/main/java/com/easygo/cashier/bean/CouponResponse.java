package com.easygo.cashier.bean;

import android.text.TextUtils;

import java.util.List;

public class CouponResponse {


    /**
     * name : 明天见
     * condition_value : 1.00
     * offer_type : 2
     * offer_type_str : 折扣券
     * offer_value : 18
     * effected_at : 1547395200000
     * expired_at : 1547567999000
     * shop_list : []
     * gsku_list : [{"g_sku_id":30451,"barcode":"135636353636","g_sku_name":"海尔商品D"}]
     */

    private String name;
    private String sn;
    private String coupon_sn;
    private String condition_value;
    private int offer_type;
    private String offer_type_str;
    private String offer_value;
    private long effected_at;
    private long expired_at;
    private List<ShopListBean> shop_list;
    private List<GSkuListBean> gsku_list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getCoupon_sn() {
        return !TextUtils.isEmpty(coupon_sn)? coupon_sn: sn;
    }

    public void setCoupon_sn(String coupon_sn) {
        this.coupon_sn = coupon_sn;
    }

    public String getCondition_value() {
        return condition_value;
    }

    public void setCondition_value(String condition_value) {
        this.condition_value = condition_value;
    }

    public int getOffer_type() {
        return offer_type;
    }

    public void setOffer_type(int offer_type) {
        this.offer_type = offer_type;
    }

    public String getOffer_type_str() {
        return offer_type_str;
    }

    public void setOffer_type_str(String offer_type_str) {
        this.offer_type_str = offer_type_str;
    }

    public float getOffer_value() {
        return Float.parseFloat(offer_value);
    }

    public void setOffer_value(String offer_value) {
        this.offer_value = offer_value;
    }

    public long getEffected_at() {
        return effected_at;
    }

    public void setEffected_at(long effected_at) {
        this.effected_at = effected_at;
    }

    public long getExpired_at() {
        return expired_at;
    }

    public void setExpired_at(long expired_at) {
        this.expired_at = expired_at;
    }

    public List<ShopListBean> getShop_list() {
        return shop_list;
    }

    public void setShop_list(List<ShopListBean> shop_list) {
        this.shop_list = shop_list;
    }

    public List<GSkuListBean> getGsku_list() {
        return gsku_list;
    }

    public void setGsku_list(List<GSkuListBean> gsku_list) {
        this.gsku_list = gsku_list;
    }

    public static final int TYPE_GOODS = 0;
    public static final int TYPE_SHOP = 1;
    public static final int TYPE_ALL = 2;

    public boolean isForGood() {
        return gsku_list != null && gsku_list.size() != 0;
    }
    public boolean isForShop() {
        return shop_list != null && shop_list.size() != 0;
    }
    public boolean isForAll() {
        return gsku_list != null && gsku_list.size() == 0
                && shop_list != null && shop_list.size() == 0;
    }



    public static class ShopListBean {


        /**
         * shop_id : 619
         * shop_name : 万物市集精品超市
         * shop_sn : 5465456456
         */

        private int shop_id;
        private String shop_name;
        private String shop_sn;

        public int getShop_id() {
            return shop_id;
        }

        public void setShop_id(int shop_id) {
            this.shop_id = shop_id;
        }

        public String getShop_name() {
            return shop_name;
        }

        public void setShop_name(String shop_name) {
            this.shop_name = shop_name;
        }

        public String getShop_sn() {
            return shop_sn;
        }

        public void setShop_sn(String shop_sn) {
            this.shop_sn = shop_sn;
        }
    }
    public static class GSkuListBean {

        /**
         * g_sku_id : 30451
         * barcode : 135636353636
         * g_sku_name : 海尔商品D
         * is_mix_sku : 0
         */

        private int g_sku_id;
        private String barcode;
        private String g_sku_name;
        private int is_mix_sku;

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

        public int getIs_mix_sku() {
            return is_mix_sku;
        }

        public void setIs_mix_sku(int is_mix_sku) {
            this.is_mix_sku = is_mix_sku;
        }
    }
}

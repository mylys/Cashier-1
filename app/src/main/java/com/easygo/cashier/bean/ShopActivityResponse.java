package com.easygo.cashier.bean;

import java.util.List;

public class ShopActivityResponse {
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 734
         * name : 测试排除商品2
         * type : 2
         * effected_at : 1551888000000
         * expired_at : 1552060799000
         * with_coupon : 1
         * config : [{"effected_at":"19:55","expired_at":"20:55","condition_value":"25","offer_type":"1","offer_value":"5"},{"effected_at":"20:55","expired_at":"23:56","condition_value":"50","offer_type":"2","offer_value":"10"}]
         * exclude_list : [{"rc_id":1939,"act_id":734,"barcode":"4897064100137","g_sku_id":26944,"g_sku_name":"众合美滋沙嗲猪肉脯80g"},{"rc_id":1940,"act_id":734,"barcode":"8858223008172","g_sku_id":200,"g_sku_name":"Mix原味脆脆条 75g"},{"rc_id":1941,"act_id":734,"barcode":"8885012290555","g_sku_id":202,"g_sku_name":"利宾纳黑加仑子柠檬味饮料500ml"}]
         */

        private int id;
        private String name;
        private int type;
        private long effected_at;
        private long expired_at;
        private int with_coupon;
        private List<ConfigBean> config;
        private List<ExcludeListBean> exclude_list;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public int getWith_coupon() {
            return with_coupon;
        }

        public void setWith_coupon(int with_coupon) {
            this.with_coupon = with_coupon;
        }

        public List<ConfigBean> getConfig() {
            return config;
        }

        public void setConfig(List<ConfigBean> config) {
            this.config = config;
        }

        public List<ExcludeListBean> getExclude_list() {
            return exclude_list;
        }

        public void setExclude_list(List<ExcludeListBean> exclude_list) {
            this.exclude_list = exclude_list;
        }

        public static class ConfigBean {
            /**
             * effected_at : 19:55
             * expired_at : 20:55
             * condition_value : 25
             * offer_type : 1
             * offer_value : 5
             */

            private String effected_at;
            private String expired_at;
            private String condition_value;
            private int offer_type;
            private String offer_value;

            public String getEffected_at() {
                return effected_at;
            }

            public void setEffected_at(String effected_at) {
                this.effected_at = effected_at;
            }

            public String getExpired_at() {
                return expired_at;
            }

            public void setExpired_at(String expired_at) {
                this.expired_at = expired_at;
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

            public String getOffer_value() {
                return offer_value;
            }

            public void setOffer_value(String offer_value) {
                this.offer_value = offer_value;
            }
        }

        public static class ExcludeListBean {
            /**
             * rc_id : 1939
             * act_id : 734
             * barcode : 4897064100137
             * g_sku_id : 26944
             * g_sku_name : 众合美滋沙嗲猪肉脯80g
             */

            private int rc_id;
            private int act_id;
            private String barcode;
            private int g_sku_id;
            private String g_sku_name;

            public int getRc_id() {
                return rc_id;
            }

            public void setRc_id(int rc_id) {
                this.rc_id = rc_id;
            }

            public int getAct_id() {
                return act_id;
            }

            public void setAct_id(int act_id) {
                this.act_id = act_id;
            }

            public String getBarcode() {
                return barcode;
            }

            public void setBarcode(String barcode) {
                this.barcode = barcode;
            }

            public int getG_sku_id() {
                return g_sku_id;
            }

            public void setG_sku_id(int g_sku_id) {
                this.g_sku_id = g_sku_id;
            }

            public String getG_sku_name() {
                return g_sku_name;
            }

            public void setG_sku_name(String g_sku_name) {
                this.g_sku_name = g_sku_name;
            }
        }
    }
}

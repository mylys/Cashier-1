package com.easygo.cashier.bean;

import java.util.List;

public class GoodsActivityResponse {

    private List<ActivitiesBean> activities;
    private List<MapBean> map;

    public List<ActivitiesBean> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivitiesBean> activities) {
        this.activities = activities;
    }

    public List<MapBean> getMap() {
        return map;
    }

    public void setMap(List<MapBean> map) {
        this.map = map;
    }

    public static class ActivitiesBean {
        /**
         * id : 374
         * name : testing 10004
         * effected_at : 1546444800000
         * expired_at : 1546617599000
         * type : 1
         * with_coupon : 1
         * goods : [{"sku_name":"众合美滋黑椒猪肉脯80g","barcode":"4897064100175","g_sku_id":3},{"sku_name":"众合美滋沙嗲猪肉脯80g","barcode":"4897064100137","g_sku_id":4}]
         * config : {"condition_type":1,"condition_value":"0.00","offer_type":2,"offer_value":"10.00"}
         */

        private int id;
        private String name;
        private long effected_at;
        private long expired_at;
        private int type;
        private int with_coupon;//是否可与优惠券公用
        private ConfigBean config;
        private List<GoodsBean> goods;

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getWith_coupon() {
            return with_coupon;
        }

        public void setWith_coupon(int with_coupon) {
            this.with_coupon = with_coupon;
        }

        public ConfigBean getConfig() {
            return config;
        }

        public void setConfig(ConfigBean config) {
            this.config = config;
        }

        public List<GoodsBean> getGoods() {
            return goods;
        }

        public void setGoods(List<GoodsBean> goods) {
            this.goods = goods;
        }

        public static class ConfigBean {
            /**
             * condition_type : 1
             * condition_value : 0.00
             * offer_type : 2
             * offer_value : 10.00
             * offer_value : 10.00
             */

            private int condition_type;
            private String condition_value;
            private int offer_type;
            private String offer_value;
            private List<ListBean> list;

            public int getCondition_type() {
                return condition_type;
            }

            public void setCondition_type(int condition_type) {
                this.condition_type = condition_type;
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

            public static class ListBean {

                /**
                 * barcode : 4800116018021
                 * effected_at ：10:00:01
                 * expired_at ：11:24:00
                 * offer_type ：1
                 * condition_value : 3
                 * offer_value : 3.33
                 */

                private String barcode;
                private String effected_at;
                private String expired_at;
                private String offer_type;
                private String condition_value;
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

                public String getOffer_type() {
                    return offer_type;
                }

                public void setOffer_type(String offer_type) {
                    this.offer_type = offer_type;
                }

                public String getBarcode() {
                    return barcode;
                }

                public void setBarcode(String barcode) {
                    this.barcode = barcode;
                }

                public String getCondition_value() {
                    return condition_value;
                }

                public void setCondition_value(String condition_value) {
                    this.condition_value = condition_value;
                }

                public String getOffer_value() {
                    return offer_value;
                }

                public void setOffer_value(String offer_value) {
                    this.offer_value = offer_value;
                }
            }

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
            }
        }

        public static class GoodsBean {
            /**
             * sku_name : 众合美滋黑椒猪肉脯80g
             * barcode : 4897064100175
             * g_sku_id : 3
             */

            private String sku_name;
            private String barcode;
            private int g_sku_id;

            public String getSku_name() {
                return sku_name;
            }

            public void setSku_name(String sku_name) {
                this.sku_name = sku_name;
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
        }
    }

    public static class MapBean {
        /**
         * barcode : 4897064100175
         * activity_id : 374
         */

        private String barcode;
        private int activity_id;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public int getActivity_id() {
            return activity_id;
        }

        public void setActivity_id(int activity_id) {
            this.activity_id = activity_id;
        }
    }
}

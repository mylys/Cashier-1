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
         * id : 699
         * name : 测试活动 v1 (RunnerLee)
         * type : 1
         * effected_at : 1547136000000
         * expired_at : 1547308799000
         * config : [{"effected_at":"00:00","expired_at":"23:59","condition_value":"1000.00","offer_type":1,"offer_value":"10.00"}]
         */

        private int id;
        private String name;
        private int type;
        private long effected_at;
        private long expired_at;
        private List<ConfigBean> config;

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

        public List<ConfigBean> getConfig() {
            return config;
        }

        public void setConfig(List<ConfigBean> config) {
            this.config = config;
        }

        public static class ConfigBean {
            /**
             * effected_at : 00:00
             * expired_at : 23:59
             * condition_value : 1000.00
             * offer_type : 1
             * offer_value : 10.00
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
    }
}

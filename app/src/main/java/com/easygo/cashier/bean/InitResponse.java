package com.easygo.cashier.bean;

import java.util.List;

public class InitResponse {

    /**
     * id : 23
     * name : 小泽测试6
     * shop_id : 620
     * mac_address : 46:54:65:65:46:54
     * "is_disabled": "no",
     * printers : [{"id":21,"mac_address":"","device_sn":"454654564"},{"id":22,"mac_address":"","device_sn":"6465454"},{"id":23,"mac_address":"","device_sn":"4545454654"},{"id":25,"mac_address":"","device_sn":"11211313213"},{"id":24,"mac_address":"","device_sn":"444564564"}]
     * shop : {"shop_id":620,"shop_name":"小泽测试88","shop_sn":"84646546","is_reserve":0}
     */

    private int id;
    private String name;
    private int shop_id;
    private String mac_address;
    private String is_disabled;
    private ShopBean shop;
    private List<PrintersBean> printers;

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

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getMac_address() {
        return mac_address;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public String getIs_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(String is_disabled) {
        this.is_disabled = is_disabled;
    }

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public List<PrintersBean> getPrinters() {
        return printers;
    }

    public void setPrinters(List<PrintersBean> printers) {
        this.printers = printers;
    }

    public static class ShopBean {
        /**
         * shop_id : 620
         * shop_name : 小泽测试88
         * shop_sn : 84646546
         * is_reserve : 0
         */

        private int shop_id;
        private String shop_name;
        private String shop_sn;
        private int is_reserve;
        private int refund_auth;
        private int till_auth;
        private int lock_auth;

        public int getLock_auth() {
            return lock_auth;
        }

        public void setLock_auth(int lock_auth) {
            this.lock_auth = lock_auth;
        }

        public int getRefund_auth() {
            return refund_auth;
        }

        public void setRefund_auth(int refund_auth) {
            this.refund_auth = refund_auth;
        }

        public int getTill_auth() {
            return till_auth;
        }

        public void setTill_auth(int till_auth) {
            this.till_auth = till_auth;
        }

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

        public int getIs_reserve() {
            return is_reserve;
        }

        public void setIs_reserve(int is_reserve) {
            this.is_reserve = is_reserve;
        }
    }

    public static class PrintersBean {


        /**
         * id : 21
         * mac_address :
         * device_sn : 454654564
         * print_times : 1
         * content_types : [1]
         */

        private int id;
        private String mac_address;
        private String device_sn;
        private int print_times;

        /**
         * 1: 收银, （支付，弹钱箱）
         * 2：退款
         * 3：交接班(登陆登出弹钱箱， 交接班信息）
         * 4：销售列表
         * 5：历史订单
         */
        private List<Integer> content_types;

        public static int type_settlement = 1;
        public static int type_refund = 2;
        public static int type_handover = 3;
        public static int type_sale = 4;
        public static int type_order_history = 5;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMac_address() {
            return mac_address;
        }

        public void setMac_address(String mac_address) {
            this.mac_address = mac_address;
        }

        public String getDevice_sn() {
            return device_sn;
        }

        public void setDevice_sn(String device_sn) {
            this.device_sn = device_sn;
        }

        public int getPrint_times() {
            return print_times;
        }

        public void setPrint_times(int print_times) {
            this.print_times = print_times;
        }

        public List<Integer> getContent_types() {
            return content_types;
        }

        public void setContent_types(List<Integer> content_types) {
            this.content_types = content_types;
        }

        /**
         * 判断打印机是否可用
         *
         * @param type
         * @return
         */
        public boolean canUse(int type) {
            for (Integer content_type : content_types) {
                if (type == content_type) {
                    return true;
                }
            }
            return false;
        }


    }
}

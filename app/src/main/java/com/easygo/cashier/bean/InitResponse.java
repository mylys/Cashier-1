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
         */

        private int id;
        private String mac_address;
        private String device_sn;

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
    }
}

package com.easygo.cashier.bean;

import java.util.List;

public class LoginResponse {


    /**
     * real_name : zzz
     * cashier_id : 361
     * session_id : 6a90f1b290b1e4ae1c06950f6477c79b361
     * handover_id : 163
     * menu : [{"menu_id":277,"menu_name":"首页","menu_url":"","parent_id":0,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1,"child_menus":[{"menu_id":278,"menu_name":"收银","menu_url":null,"parent_id":277,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1}]},{"menu_id":279,"menu_name":"历史订单","menu_url":"api/cash/order","parent_id":0,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1,"child_menus":[{"menu_id":280,"menu_name":"退款","menu_url":"api/pay/refund","parent_id":279,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1}]},{"menu_id":281,"menu_name":"交接班","menu_url":"api/cash/loginout","parent_id":0,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1,"child_menus":[{"menu_id":282,"menu_name":"交接班登出","menu_url":"api/cash/loginout","parent_id":281,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1},{"menu_id":283,"menu_name":"销售列表","menu_url":"api/cash/sale","parent_id":281,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1}]},{"menu_id":294,"menu_name":"备用金","menu_url":"api/cash/reserve","parent_id":0,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1,"child_menus":[]},{"menu_id":295,"menu_name":"打印机","menu_url":"","parent_id":0,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1,"child_menus":[{"menu_id":296,"menu_name":"打印机打印","menu_url":"api/pay/printer","parent_id":295,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1},{"menu_id":297,"menu_name":"打印机控制钱柜","menu_url":"api/pay/printer_till","parent_id":295,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1},{"menu_id":298,"menu_name":"打印机状态","menu_url":"api/pay/printer_status","parent_id":295,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1}]},{"menu_id":299,"menu_name":"订单","menu_url":"","parent_id":0,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1,"child_menus":[{"menu_id":300,"menu_name":"获取商品信息","menu_url":"api/pay/get_info","parent_id":299,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1},{"menu_id":301,"menu_name":"创建订单","menu_url":"api/pay/create_order","parent_id":299,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1}]},{"menu_id":302,"menu_name":"支付","menu_url":"","parent_id":0,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1,"child_menus":[{"menu_id":303,"menu_name":"支付宝支付","menu_url":"api/pay/alipay","parent_id":302,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1},{"menu_id":304,"menu_name":"微信支付","menu_url":"api/pay/wechat","parent_id":302,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1},{"menu_id":305,"menu_name":"现金支付","menu_url":"api/pay/cash","parent_id":302,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1}]}]
     */

    private String real_name;
    private int cashier_id;
    private String session_id;
    private int handover_id;
    private List<MenuBean> menu;

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public int getCashier_id() {
        return cashier_id;
    }

    public void setCashier_id(int cashier_id) {
        this.cashier_id = cashier_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public int getHandover_id() {
        return handover_id;
    }

    public void setHandover_id(int handover_id) {
        this.handover_id = handover_id;
    }

    public List<MenuBean> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuBean> menu) {
        this.menu = menu;
    }

    public static class MenuBean {
        /**
         * menu_id : 277
         * menu_name : 首页
         * menu_url :
         * parent_id : 0
         * status : 1
         * update_time : null
         * create_time : 0
         * level : 0
         * sort : 0
         * type : 3
         * is_show : 1
         * role : 1
         * child_menus : [{"menu_id":278,"menu_name":"收银","menu_url":null,"parent_id":277,"status":1,"update_time":null,"create_time":0,"level":0,"sort":0,"type":3,"is_show":1,"role":1}]
         */

        private int menu_id;
        private String menu_name;
        private String menu_url;
        private int parent_id;
        private int status;
        private Object update_time;
        private int create_time;
        private int level;
        private int sort;
        private int type;
        private int is_show;
        private int role;
        private List<ChildMenusBean> child_menus;

        public int getMenu_id() {
            return menu_id;
        }

        public void setMenu_id(int menu_id) {
            this.menu_id = menu_id;
        }

        public String getMenu_name() {
            return menu_name;
        }

        public void setMenu_name(String menu_name) {
            this.menu_name = menu_name;
        }

        public String getMenu_url() {
            return menu_url;
        }

        public void setMenu_url(String menu_url) {
            this.menu_url = menu_url;
        }

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(Object update_time) {
            this.update_time = update_time;
        }

        public int getCreate_time() {
            return create_time;
        }

        public void setCreate_time(int create_time) {
            this.create_time = create_time;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getIs_show() {
            return is_show;
        }

        public void setIs_show(int is_show) {
            this.is_show = is_show;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public List<ChildMenusBean> getChild_menus() {
            return child_menus;
        }

        public void setChild_menus(List<ChildMenusBean> child_menus) {
            this.child_menus = child_menus;
        }

        public static class ChildMenusBean {
            /**
             * menu_id : 278
             * menu_name : 收银
             * menu_url : null
             * parent_id : 277
             * status : 1
             * update_time : null
             * create_time : 0
             * level : 0
             * sort : 0
             * type : 3
             * is_show : 1
             * role : 1
             */

            private int menu_id;
            private String menu_name;
            private Object menu_url;
            private int parent_id;
            private int status;
            private Object update_time;
            private int create_time;
            private int level;
            private int sort;
            private int type;
            private int is_show;
            private int role;

            public int getMenu_id() {
                return menu_id;
            }

            public void setMenu_id(int menu_id) {
                this.menu_id = menu_id;
            }

            public String getMenu_name() {
                return menu_name;
            }

            public void setMenu_name(String menu_name) {
                this.menu_name = menu_name;
            }

            public Object getMenu_url() {
                return menu_url;
            }

            public void setMenu_url(Object menu_url) {
                this.menu_url = menu_url;
            }

            public int getParent_id() {
                return parent_id;
            }

            public void setParent_id(int parent_id) {
                this.parent_id = parent_id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public Object getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(Object update_time) {
                this.update_time = update_time;
            }

            public int getCreate_time() {
                return create_time;
            }

            public void setCreate_time(int create_time) {
                this.create_time = create_time;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getIs_show() {
                return is_show;
            }

            public void setIs_show(int is_show) {
                this.is_show = is_show;
            }

            public int getRole() {
                return role;
            }

            public void setRole(int role) {
                this.role = role;
            }
        }
    }
}

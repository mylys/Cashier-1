package com.easygo.cashier.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-19
 */
public class OrderHistorysInfo {

    /**
     * o_id : 446242
     * trade_no : eg201812272016551476
     * trade_num : 2018122720165555571029
     * create_time : 1545913015
     * refund_order_id : null
     * buyer :
     * total_money : 0.02
     * real_pay : 0.02
     * buyer_pay : 0.02
     * change_money : 0.00
     * refund_fee : 0.00
     * sku_count : 1
     * pay_type : 3
     * status : 2
     * admin_name : cs2@163.com
     * real_name : 测试收银2
     */

    private int o_id;
    private String trade_no;
    private String trade_num;
    private int create_time;
    private Object refund_order_id;
    private String buyer;
    private String total_money;
    private String real_pay;
    private String buyer_pay;
    private String change_money;
    private String refund_fee;
    private int sku_count;
    private int pay_type;
    private int status;
    private String admin_name;
    private String real_name;
    private List<ListBean> list;
    private boolean isSelect;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getO_id() {
        return o_id;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTrade_num() {
        return trade_num;
    }

    public void setTrade_num(String trade_num) {
        this.trade_num = trade_num;
    }

    public int getCreate_time() {
        return create_time;
    }

    public void setCreate_time(int create_time) {
        this.create_time = create_time;
    }

    public Object getRefund_order_id() {
        return refund_order_id;
    }

    public void setRefund_order_id(Object refund_order_id) {
        this.refund_order_id = refund_order_id;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getReal_pay() {
        return real_pay;
    }

    public void setReal_pay(String real_pay) {
        this.real_pay = real_pay;
    }

    public String getBuyer_pay() {
        return buyer_pay;
    }

    public void setBuyer_pay(String buyer_pay) {
        this.buyer_pay = buyer_pay;
    }

    public String getChange_money() {
        return change_money;
    }

    public void setChange_money(String change_money) {
        this.change_money = change_money;
    }

    public String getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(String refund_fee) {
        this.refund_fee = refund_fee;
    }

    public int getSku_count() {
        return sku_count;
    }

    public void setSku_count(int sku_count) {
        this.sku_count = sku_count;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public static class ListBean implements Parcelable {

        /**
         * o_i_id : 810336
         * g_sku_id : 13005
         * barcode : 6902538004045
         * g_sku_name : 脉动青柠味饮料
         * s_sku_id : 111859
         * g_c_name : 饮料类-功能性类
         * is_weigh : 0
         * cashier_id : 368
         * pay_time : 1545913015
         * status : 2
         * refund : 0
         * g_u_id : 12
         * g_u_name : 瓶
         * purchase_price : 3.38
         * sell_price : 0.02
         * quantity : 1
         * count : 1
         * money : 0.02
         */

        private int o_i_id;
        private int g_sku_id;
        private String barcode;
        private String g_sku_name;
        private int s_sku_id;
        private String g_c_name;
        private int is_weigh;
        private int cashier_id;
        private int pay_time;
        private int status;
        private int refund;
        private int g_u_id;
        private String g_u_name;
        private String purchase_price;
        private String sell_price;
        private String quantity;
        private String count;
        private String money;
        private int parent_id;

        protected ListBean(Parcel in) {
            o_i_id = in.readInt();
            g_sku_id = in.readInt();
            barcode = in.readString();
            g_sku_name = in.readString();
            s_sku_id = in.readInt();
            g_c_name = in.readString();
            is_weigh = in.readInt();
            cashier_id = in.readInt();
            pay_time = in.readInt();
            status = in.readInt();
            refund = in.readInt();
            g_u_id = in.readInt();
            g_u_name = in.readString();
            purchase_price = in.readString();
            sell_price = in.readString();
            quantity = in.readString();
            count = in.readString();
            money = in.readString();
            parent_id = in.readInt();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel in) {
                return new ListBean(in);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };

        public int getParent_id() {
            return parent_id;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }

        public int getO_i_id() {
            return o_i_id;
        }

        public void setO_i_id(int o_i_id) {
            this.o_i_id = o_i_id;
        }

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

        public int getS_sku_id() {
            return s_sku_id;
        }

        public void setS_sku_id(int s_sku_id) {
            this.s_sku_id = s_sku_id;
        }

        public String getG_c_name() {
            return g_c_name;
        }

        public void setG_c_name(String g_c_name) {
            this.g_c_name = g_c_name;
        }

        public int getIs_weigh() {
            return is_weigh;
        }

        public void setIs_weigh(int is_weigh) {
            this.is_weigh = is_weigh;
        }

        public int getCashier_id() {
            return cashier_id;
        }

        public void setCashier_id(int cashier_id) {
            this.cashier_id = cashier_id;
        }

        public int getPay_time() {
            return pay_time;
        }

        public void setPay_time(int pay_time) {
            this.pay_time = pay_time;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getRefund() {
            return refund;
        }

        public void setRefund(int refund) {
            this.refund = refund;
        }

        public int getG_u_id() {
            return g_u_id;
        }

        public void setG_u_id(int g_u_id) {
            this.g_u_id = g_u_id;
        }

        public String getG_u_name() {
            return g_u_name;
        }

        public void setG_u_name(String g_u_name) {
            this.g_u_name = g_u_name;
        }

        public String getPurchase_price() {
            return purchase_price;
        }

        public void setPurchase_price(String purchase_price) {
            this.purchase_price = purchase_price;
        }

        public String getSell_price() {
            return sell_price;
        }

        public void setSell_price(String sell_price) {
            this.sell_price = sell_price;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(o_i_id);
            dest.writeInt(g_sku_id);
            dest.writeString(barcode);
            dest.writeString(g_sku_name);
            dest.writeInt(s_sku_id);
            dest.writeString(g_c_name);
            dest.writeInt(is_weigh);
            dest.writeInt(cashier_id);
            dest.writeInt(pay_time);
            dest.writeInt(status);
            dest.writeInt(refund);
            dest.writeInt(g_u_id);
            dest.writeString(g_u_name);
            dest.writeString(purchase_price);
            dest.writeString(sell_price);
            dest.writeString(quantity);
            dest.writeString(count);
            dest.writeString(money);
            dest.writeInt(parent_id);
        }
    }
}

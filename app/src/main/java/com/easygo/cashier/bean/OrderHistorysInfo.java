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
     * o_id : 446169
     * trade_no : null
     * trade_num : 201812035c04cc5f5cdb8675291
     * create_time : 1543818335
     * total_money : 0.01
     * real_pay : 0.01
     * buyer_pay : 0.00
     * change_money : 0.00
     * refund_fee : null
     * sku_count : 1
     * pay_type : 1
     * status : 1
     * list : [{"g_sku_name":"可口可乐小瓶","sell_price":"0.01","count":1,"money":"0.01"}]
     */

    private int o_id;
    private Object trade_no;
    private String trade_num;
    private int create_time;
    private Object refund_order_id;
    private String total_money;
    private String real_pay;
    private String buyer_pay;
    private String change_money;
    private Object refund_fee;
    private int sku_count;
    private int pay_type;
    private int status;
    private List<ListBean> list;
    private boolean isSelect;
    private String buyer;
    private String admin_name;

    public Object getRefund_order_id() {
        return refund_order_id;
    }

    public void setRefund_order_id(Object refund_order_id) {
        this.refund_order_id = refund_order_id;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
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

    public Object getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(Object trade_no) {
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

    public Object getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(Object refund_fee) {
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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {

        /**
         * g_sku_name : 可口可乐小瓶
         * sell_price : 0.01
         * quantity : 1
         * count : 1
         * money : 0.01
         */

        private String g_sku_name;
        private String sell_price;
        private int quantity;
        private int count;
        private String money;
        private int s_sku_id;

        protected ListBean(Parcel in) {
            g_sku_name = in.readString();
            sell_price = in.readString();
            count = in.readInt();
            money = in.readString();
            s_sku_id = in.readInt();
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

        public String getG_sku_name() {
            return g_sku_name;
        }

        public void setG_sku_name(String g_sku_name) {
            this.g_sku_name = g_sku_name;
        }

        public String getSell_price() {
            return sell_price;
        }

        public void setSell_price(String sell_price) {
            this.sell_price = sell_price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getS_sku_id() {
            return s_sku_id;
        }

        public void setS_sku_id(int s_sku_id) {
            this.s_sku_id = s_sku_id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(g_sku_name);
            dest.writeString(sell_price);
            dest.writeInt(count);
            dest.writeString(money);
            dest.writeInt(s_sku_id);
        }
    }
}

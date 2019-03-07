package com.easygo.cashier.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

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
    private int refund_status;
    private String refund_fee;
    private int sku_count;
    private int pay_type;
    private int status;
    private String admin_name;
    private String real_name;
    private String cashier_discount;
    private List<ListBean> list;
    private List<ActivitiesBean> activities;
    private boolean isSelect;
    private int have_refund;

    public int getHave_refund() {
        return have_refund;
    }

    public void setHave_refund(int have_refund) {
        this.have_refund = have_refund;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public List<ActivitiesBean> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivitiesBean> activities) {
        this.activities = activities;
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

    public int getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(int refund_status) {
        this.refund_status = refund_status;
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

    public String getCashier_discount() {
        return cashier_discount;
    }

    public void setCashier_discount(String cashier_discount) {
        this.cashier_discount = cashier_discount;
    }

    public static class ListBean implements Parcelable {


        /**
         * g_sku_name : 发舒服撒发-就是个福
         * s_sku_id : 111865
         * g_u_symbol : g
         * type : 3
         * quantity : 1
         * sell_price : 0.01
         * discount : 0.01
         * cashier_discount : 0.01
         * identity : 1547787879989
         * refund : 0
         * count : 1
         * money : 0.01
         */

        private String g_sku_name;
        private int s_sku_id;
        private String g_u_symbol;
        private int type;
        private int quantity;
        private String sell_price;
        private String discount;
        private String cashier_discount;
        private String identity;
        private int refund;
        private float count;
        private double money;

        protected ListBean(Parcel in) {
            g_sku_name = in.readString();
            s_sku_id = in.readInt();
            type = in.readInt();
            quantity = in.readInt();
            sell_price = in.readString();
            discount = in.readString();
            cashier_discount = in.readString();
            identity = in.readString();
            refund = in.readInt();
            count = in.readInt();
            money = in.readDouble();
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

        public int getS_sku_id() {
            return s_sku_id;
        }

        public void setS_sku_id(int s_sku_id) {
            this.s_sku_id = s_sku_id;
        }

        public String getG_u_symbol() {
            return g_u_symbol;
        }

        public void setG_u_symbol(String g_u_symbol) {
            this.g_u_symbol = g_u_symbol;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getSell_price() {
            return sell_price;
        }

        public void setSell_price(String sell_price) {
            this.sell_price = sell_price;
        }

        public String getDiscount() {
            return discount;
//            return !TextUtils.isEmpty(cashier_discount) && !"0.00".equals(cashier_discount)? cashier_discount: discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getCashier_discount() {
            return cashier_discount;
        }

        public void setCashier_discount(String cashier_discount) {
            this.cashier_discount = cashier_discount;
        }

        public String getIdentity() {
            return identity;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public int getRefund() {
            return refund;
        }

        public void setRefund(int refund) {
            this.refund = refund;
        }

        public float getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(g_sku_name);
            dest.writeInt(s_sku_id);
            dest.writeInt(type);
            dest.writeFloat(quantity);
            dest.writeString(sell_price);
            dest.writeString(discount);
            dest.writeString(cashier_discount);
            dest.writeString(identity);
            dest.writeInt(refund);
            dest.writeFloat(count);
            dest.writeDouble(money);
        }
    }

    public static class ActivitiesBean implements Parcelable {

        /**
         * activity_name : test
         * discount_money : 1.00
         */

        private String activity_name;
        private String discount_money;

        protected ActivitiesBean(Parcel in) {
            activity_name = in.readString();
            discount_money = in.readString();
        }

        public static final Creator<ActivitiesBean> CREATOR = new Creator<ActivitiesBean>() {
            @Override
            public ActivitiesBean createFromParcel(Parcel in) {
                return new ActivitiesBean(in);
            }

            @Override
            public ActivitiesBean[] newArray(int size) {
                return new ActivitiesBean[size];
            }
        };

        public String getActivity_name() {
            return activity_name;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public String getDiscount_money() {
            return discount_money;
        }

        public void setDiscount_money(String discount_money) {
            this.discount_money = discount_money;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(activity_name);
            dest.writeString(discount_money);
        }
    }
}

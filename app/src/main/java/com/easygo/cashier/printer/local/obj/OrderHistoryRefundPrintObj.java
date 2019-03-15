package com.easygo.cashier.printer.local.obj;

import com.easygo.cashier.bean.GoodsRefundInfo;

import java.util.ArrayList;

public class OrderHistoryRefundPrintObj {

    public String shop_name;
    public String order_no;
    public String time;
    public String admin_name;
    public ArrayList<GoodsRefundInfo> data;
    public String count;
    public String total_price;
    public String discount;
    public String real_pay;
    public String pay_type;
    public String refund;
}

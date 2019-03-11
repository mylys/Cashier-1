package com.easygo.cashier.printer.obj;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;

import java.util.List;

public class CashierPrintObj {

    public String shop_name;
    public String order_no;
    public String time;
    public String admin_name;
    public List<GoodsEntity<GoodsResponse>> data;
    public String count;
    public String total_money;
    public String discount;
    public String real_pay;
    public String change;
    public String pay_type;
    public String refund;
}

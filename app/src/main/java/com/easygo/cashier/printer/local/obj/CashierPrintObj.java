package com.easygo.cashier.printer.local.obj;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;

import java.util.List;

public class CashierPrintObj {

    public String shop_name;
    public String trade_no;
    public String time;
    public String admin_name;
    public List<GoodsEntity<GoodsResponse>> data;
    public String count;
    public float total_money;
    public float discount;
    public float real_pay;
    public float change;
    public String pay_type;
}

package com.easygo.cashier.bean;

import java.util.List;

public class OrderHistoryInfo {

    private String acount;
    private String order_no;
    private List<GoodsInfo> goods;
    private int count;
    private float receivable;
    private float receipts;
    private float refund;
    private float return_of_goods_count;
    private String time;


    private float total_money;

    public String getAcount() {
        return acount;
    }

    public void setAcount(String acount) {
        this.acount = acount;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public List<GoodsInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsInfo> goods) {
        this.goods = goods;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getReceivable() {
        return receivable;
    }

    public void setReceivable(float receivable) {
        this.receivable = receivable;
    }

    public float getReceipts() {
        return receipts;
    }

    public void setReceipts(float receipts) {
        this.receipts = receipts;
    }

    public float getRefund() {
        return refund;
    }

    public void setRefund(float refund) {
        this.refund = refund;
    }

    public float getTotal_money() {
        return total_money;
    }

    public void setTotal_money(float total_money) {
        this.total_money = total_money;
    }

    public float getReturn_of_goods_count() {
        return return_of_goods_count;
    }

    public void setReturn_of_goods_count(float return_of_goods_count) {
        this.return_of_goods_count = return_of_goods_count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "OrderHistoryInfo{" +
                "acount='" + acount + '\'' +
                ", order_no='" + order_no + '\'' +
                ", goods=" + goods +
                ", count=" + count +
                ", receivable=" + receivable +
                ", receipts=" + receipts +
                ", refund=" + refund +
                ", time='" + time + '\'' +
                ", total_money=" + total_money +
                '}';
    }
}

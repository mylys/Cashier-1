package com.easygo.cashier.bean;

import com.easygo.cashier.adapter.GoodsEntity;

import java.util.List;

/**
 * @Describe：挂单详情
 * @date：2019-01-04
 */
public class EntryOrders {
    private String entry_orders_time;
    private String entry_orders_note;
    private String entry_orders_total_price;
    private String entry_orders_total_number;
    private boolean isSelect;
    private List<GoodsEntity<GoodsResponse>> goodsEntityList;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getEntry_orders_time() {
        return entry_orders_time;
    }

    public void setEntry_orders_time(String entry_orders_time) {
        this.entry_orders_time = entry_orders_time;
    }

    public String getEntry_orders_note() {
        return entry_orders_note;
    }

    public void setEntry_orders_note(String entry_orders_note) {
        this.entry_orders_note = entry_orders_note;
    }

    public String getEntry_orders_total_price() {
        return entry_orders_total_price;
    }

    public void setEntry_orders_total_price(String entry_orders_total_price) {
        this.entry_orders_total_price = entry_orders_total_price;
    }

    public String getEntry_orders_total_number() {
        return entry_orders_total_number;
    }

    public void setEntry_orders_total_number(String entry_orders_total_number) {
        this.entry_orders_total_number = entry_orders_total_number;
    }

    public List<GoodsEntity<GoodsResponse>> getGoodsEntityList() {
        return goodsEntityList;
    }

    public void setGoodsEntityList(List<GoodsEntity<GoodsResponse>> goodsEntityList) {
        this.goodsEntityList = goodsEntityList;
    }

    @Override
    public String toString() {
        return "EntryOrders{" +
                "entry_orders_time='" + entry_orders_time + '\'' +
                ", entry_orders_note='" + entry_orders_note + '\'' +
                ", entry_orders_total_price='" + entry_orders_total_price + '\'' +
                ", entry_orders_total_number='" + entry_orders_total_number + '\'' +
                ", goodsEntityList=" + goodsEntityList +
                '}';
    }
}

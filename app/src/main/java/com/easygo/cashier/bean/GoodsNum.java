package com.easygo.cashier.bean;

import java.io.Serializable;

public class GoodsNum<T> implements Serializable {

    int count;
    T data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

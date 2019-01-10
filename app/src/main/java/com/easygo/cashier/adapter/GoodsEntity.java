package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;

import java.io.Serializable;
import java.util.List;

public class GoodsEntity<T> implements MultiItemEntity, Serializable {

    public static final int TYPE_GOODS = 0;
    public static final int TYPE_WEIGHT = 1;
    public static final int TYPE_ONLY_PROCESSING = 2;
    public static final int TYPE_PROCESSING = 3;
    public static final int TYPE_NO_CODE = 4;
    private int itemType;


    public GoodsEntity(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    private int count;
    private T data;
    private List<T> processing_list;
    private T processing;


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

    public List<T> getProcessing_list() {
        return processing_list;
    }

    public void setProcessing_list(List<T> processing_list) {
        this.processing_list = processing_list;
    }

    public T getProcessing() {
        return processing;
    }

    public void setProcessing(T processing) {
        this.processing = processing;
    }

    private IGoodsPromotion promotion;

    public IGoodsPromotion getPromotion() {
        return promotion;
    }

    public void setPromotion(IGoodsPromotion promotion) {
        this.promotion = promotion;
    }
}

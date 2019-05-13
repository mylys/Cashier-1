package com.easygo.cashier.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.easygo.cashier.module.promotion.goods.BaseGoodsPromotion;

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

    public boolean isWeightGood() {
        return itemType == TYPE_WEIGHT || itemType == TYPE_PROCESSING;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    private float count;
    private T data;
    private List<T> processing_list;
    private T processing;


    public float getCount() {
        return count;
    }

    public void setCount(float count) {
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

    private BaseGoodsPromotion promotion;
    private boolean isSelected;
    /**计算店铺促销时是否排除计算*/
    private boolean isExcludeInShopActivity;

    public BaseGoodsPromotion getPromotion() {
        return promotion;
    }

    public void setPromotion(BaseGoodsPromotion promotion) {
        this.promotion = promotion;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isExcludeInShopActivity() {
        return isExcludeInShopActivity;
    }

    public void setExcludeInShopActivity(boolean excludeInShopActivity) {
        isExcludeInShopActivity = excludeInShopActivity;
    }

    public boolean equals(GoodsEntity<T> obj) {
        if(super.equals(obj)) {
            return true;
        }
        boolean count_equal = count == obj.getCount();
        boolean data_equal = data.equals(obj.getData());
        boolean processing_equal;
        if(processing != null && obj.getProcessing() != null) {
            processing_equal = processing.equals(obj.getProcessing());
        } else if(processing == null && obj.getProcessing() == null) {
            processing_equal = true;
        } else {
            processing_equal = false;
        }

        return super.equals(obj) ||
                (count_equal && data_equal && processing_equal);
    }
}

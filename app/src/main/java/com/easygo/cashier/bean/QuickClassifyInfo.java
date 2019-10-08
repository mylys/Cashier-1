package com.easygo.cashier.bean;

import java.util.List;

/**
 * @Describe：
 * @Date：2019-01-15
 */
public class QuickClassifyInfo {

    private int class_id;
    private String class_name;
    private List<GoodsResponse> goods;
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public List<GoodsResponse> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsResponse> goods) {
        this.goods = goods;
    }
}

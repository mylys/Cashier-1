package com.easygo.cashier.module.promotion.goods;

import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.module.promotion.base.BasePromotion;

import java.util.List;

public class BaseGoodsPromotion extends BasePromotion {


    /**商品促销周期的开始时间*/
    protected long goods_effected_at;
    /**商品促销周期的结束时间*/
    protected long goods_expired_at;

    /**参与促销的商品列表*/
    protected List<GoodsActivityResponse.ActivitiesBean.GoodsBean> goodsBeans;

    /**是否在商品促销的生效时间内*/
    public boolean isInGoodsEffectedTime() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis >= goods_effected_at && currentTimeMillis <= goods_expired_at;
    }


    public long getGoods_effected_at() {
        return goods_effected_at;
    }

    public void setGoods_effected_at(long goods_effected_at) {
        this.goods_effected_at = goods_effected_at;
    }

    public long getGoods_expired_at() {
        return goods_expired_at;
    }

    public void setGoods_expired_at(long goods_expired_at) {
        this.goods_expired_at = goods_expired_at;
    }

    public List<GoodsActivityResponse.ActivitiesBean.GoodsBean> getGoodsBeans() {
        return goodsBeans;
    }

    public void setGoodsBeans(List<GoodsActivityResponse.ActivitiesBean.GoodsBean> goodsBeans) {
        this.goodsBeans = goodsBeans;
    }
}

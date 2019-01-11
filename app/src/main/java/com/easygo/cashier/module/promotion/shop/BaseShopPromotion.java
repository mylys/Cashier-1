package com.easygo.cashier.module.promotion.shop;

import com.easygo.cashier.bean.ShopActivityResponse;
import com.easygo.cashier.module.promotion.base.BasePromotion;

import java.util.List;

public class BaseShopPromotion extends BasePromotion {

    /**促销周期的开始时间*/
    protected long goods_effected_at;
    /**促销周期的结束时间*/
    protected long goods_expired_at;

    protected List<ShopActivityResponse.ListBean.ConfigBean> configBeans;

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

    /**是否在促销的生效时间内*/
    public boolean isInEffectedTime() {
        long currentTimeMillis = System.currentTimeMillis();
        return currentTimeMillis >= goods_effected_at && currentTimeMillis <= goods_expired_at;
    }

    public List<ShopActivityResponse.ListBean.ConfigBean> getConfigBeans() {
        return configBeans;
    }

    public void setConfigBeans(List<ShopActivityResponse.ListBean.ConfigBean> configBeans) {
        this.configBeans = configBeans;
    }

    @Override
    public int getPromotionClassify() {
        return PROMOTION_SHOP;
    }

    public float getPromotionMoney(float money) {
        return 0f;
    }

}

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
}

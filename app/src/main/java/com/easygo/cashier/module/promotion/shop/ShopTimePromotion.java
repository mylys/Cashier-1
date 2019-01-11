package com.easygo.cashier.module.promotion.shop;

import com.easygo.cashier.bean.ShopActivityResponse;
import com.easygo.cashier.module.promotion.base.IShopPromotion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 店铺时段促销
 */
public class ShopTimePromotion extends BaseShopPromotion implements IShopPromotion {

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);

    @Override
    public boolean isMeetCondition(float money) {

        return false;
    }

    @Override
    public float getPromotionMoney(float money) {

        if(!isInEffectedTime()) {
            return 0f;
        }

        //当前时间 HH:mm 格式
        String current = sdf.format(new Date());

        List<ShopActivityResponse.ListBean.ConfigBean> configBeans = getConfigBeans();
        int size = configBeans.size();

        for (int i = 0; i < size; i++) {
            ShopActivityResponse.ListBean.ConfigBean configBean = configBeans.get(i);

            String effected_at = configBean.getEffected_at();
            String expired_at = configBean.getExpired_at();

            //判断 是否在时段内
            if(current.compareTo(effected_at) > 0 && current.compareTo(expired_at) < 0) {

                switch (getOffer_type()) {
                    case OFFER_TYPE_MONEY://金额

                        return offer_value;
                    case OFFER_TYPE_RATIO://比例

                        return money * (offer_value / 100f);
                    default:
                        return 0f;
                }
            }
        }
        return 0f;
    }

}

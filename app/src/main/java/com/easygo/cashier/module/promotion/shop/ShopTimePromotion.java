package com.easygo.cashier.module.promotion.shop;

import android.util.Log;

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

    private static final String TAG = ShopTimePromotion.class.getSimpleName();

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);

    @Override
    public float getPromotionMoney(float money) {

        Log.i(TAG, "getPromotionMoney: 店铺时段促销");

        if(!isInEffectedTime()) {
            return 0f;
        }

        //当前时间 HH:mm 格式
        String current = sdf.format(new Date());

        List<ShopActivityResponse.ListBean.ConfigBean> configBeans = getConfigBeans();
        int size = configBeans.size();

        for (int i = 0; i < size; i++) {
            ShopActivityResponse.ListBean.ConfigBean configBean = configBeans.get(i);

            float condition_value = Float.valueOf(configBean.getCondition_value());

            if(money < condition_value) {
                continue;
            }

            String effected_at = configBean.getEffected_at();
            String expired_at = configBean.getExpired_at();
            int offer_type = configBean.getOffer_type();
            float offer_value = Float.valueOf(configBean.getOffer_value());



            //判断 是否在时段内
            if(current.compareTo(effected_at) > 0 && current.compareTo(expired_at) < 0) {

                switch (offer_type) {
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

package com.easygo.cashier;

import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.ShopActivityResponse;

import java.util.List;

/**
 * 促销工具类
 */
public class ActivitiesUtils {


    /**
     * 商品促销
     * @param response
     */
    public void parseGoodsActivity(GoodsActivityResponse response) {

    }

    /**
     * 店铺促销
     * @param response
     */
    public void parseShopActivity(ShopActivityResponse response) {
        List<ShopActivityResponse.ListBean> list = response.getList();



    }


}

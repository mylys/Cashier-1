package com.easygo.cashier;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
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

        int size = list.size();
        for (int i = 0; i < size; i++) {
            ShopActivityResponse.ListBean listBean = list.get(i);

            int type = listBean.getType();
            List<ShopActivityResponse.ListBean.ConfigBean> config = listBean.getConfig();
            int config_size = config.size();
            for (int j = 0; j < config_size; j++) {
                ShopActivityResponse.ListBean.ConfigBean configBean = config.get(j);
//                configBean.getCondition_value()



            }


            if(type == 1) {//金额满减

//                listBean
            } else if(type == 2) {//折扣

            }


        }


    }

    public void computeGoodsActivity(GoodsEntity goodsEntity) {

        //根据条码 找到活动id
        //

//        if()
    }


}

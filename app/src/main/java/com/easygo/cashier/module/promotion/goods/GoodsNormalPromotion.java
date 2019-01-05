package com.easygo.cashier.module.promotion.goods;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;

import java.util.List;

/**
 * 商品普通促销
 */
public class GoodsNormalPromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    @Override
    public int getPromotionClassify() {
        return IPromotion.PROMOTION_GOODS;
    }



    @Override
    public float getPromotionMoney(float money) {
        switch (getOffer_type()) {
            case OFFER_TYPE_MONEY:
                return offer_value;
            case OFFER_TYPE_RATIO:
                return money * (offer_value / 100f);
            default:
                return 0;
        }
    }

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        boolean result = false;
        if(isInGoodsEffectedTime()) {

            int count = 0;
            float money = 0;
            int size = goodsBeans.size();
            int data_size = data.size();
            String barcode;
            int goods_count;//商品数量
            for (int i = 0; i < size; i++) {
                GoodsActivityResponse.ActivitiesBean.GoodsBean goodsBean = goodsBeans.get(i);
                barcode = goodsBean.getBarcode();

                for (int j = 0; j < data_size; j++) {
                    GoodsEntity<GoodsResponse> goodsEntity = data.get(j);
                    if (barcode.equals(goodsEntity.getData().getBarcode())) {
                        goods_count = goodsEntity.getCount();
                        count += goods_count;
                        money += goods_count * Float.valueOf(goodsEntity.getData().getPrice());
                        break;
                    }
                }
            }


            switch (getCondition_type()) {
                case IPromotion.CONDITION_TYPE_EVEN:
                    //倍数
                    result = count / 2 >= 1;

                    break;
                case IPromotion.CONDITION_TYPE_MONEY:
                    result = money >= condition_value;

                    break;
            }
        }
        return result;
    }
}

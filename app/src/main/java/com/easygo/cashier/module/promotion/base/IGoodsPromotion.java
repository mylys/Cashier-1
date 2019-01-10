package com.easygo.cashier.module.promotion.base;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;

import java.util.List;

public interface IGoodsPromotion extends IPromotion {

    /**是否满足促销条件*/
    boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data);


    /**计算促销金额*/
    void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data);
}

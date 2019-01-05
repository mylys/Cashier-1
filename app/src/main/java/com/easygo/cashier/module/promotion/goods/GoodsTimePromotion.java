package com.easygo.cashier.module.promotion.goods;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.BasePromotion;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;

import java.util.ArrayList;
import java.util.List;

public class GoodsTimePromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    List<BasePromotion> times = new ArrayList<>();

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        return false;
    }

    @Override
    public int getPromotionClassify() {
        return IPromotion.PROMOTION_GOODS;
    }

    @Override
    public float getPromotionMoney(float money) {
        return 0;
    }
}

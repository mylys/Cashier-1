package com.easygo.cashier.module.promotion.goods;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.BasePromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;

import java.util.List;

public class BaseGoodsPromotion extends BasePromotion {


    /**商品促销周期的开始时间*/
    protected long goods_effected_at;
    /**商品促销周期的结束时间*/
    protected long goods_expired_at;

    /**参与促销的商品列表*/
    protected List<GoodsActivityResponse.ActivitiesBean.GoodsBean> goodsBeans;

    /**用于时段、满额、捆绑促销 多条件的列表*/
    protected List<GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean> listBeans;

    /**顾客所购商品参与促销的商品信息*/
    protected PromotionGoods promotionGoods;


    @Override
    public int getPromotionClassify() {
        return IPromotion.PROMOTION_GOODS;
    }

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

    public List<GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean> getListBeans() {
        return listBeans;
    }

    public void setListBeans(List<GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean> listBeans) {
        this.listBeans = listBeans;
    }

    public PromotionGoods getPromotionGoods() {
        return promotionGoods;
    }

    public void setPromotionGoods(PromotionGoods promotionGoods) {
        this.promotionGoods = promotionGoods;
    }

    public void computePromotionGoods() {
        if(promotionGoods != null) {
            List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
            int size = goodsBeans.size();
            int total_count = 0;
            float total_subtotal = 0;

            for (int i = 0; i < size; i++) {
                PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
//                total_count += goodsBean.getCount();
                total_count += goodsBean.getQuanlity();
                total_subtotal += goodsBean.getSubtotal();
            }
            promotionGoods.setTotal_count(total_count);
            promotionGoods.setTotal_money(total_subtotal);
        }
    }

    /**计算促销金额*/
    public void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data) {

    }

    public boolean isTempGoodsPromotion() {
        return getType() == IPromotion.TYPE_TEMP;
    }
}

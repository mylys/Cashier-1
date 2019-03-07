package com.easygo.cashier.module.promotion.goods;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品普通促销
 */
public class GoodsNormalPromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    public static final String TAG = GoodsNormalPromotion.class.getSimpleName();

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        return false;
    }

    @Override
    public void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data) {

        Log.i(TAG, "computePromotionMoney: 商品普通促销");

        if(promotionGoods == null) {
            return;
        }

        int total_count = promotionGoods.getTotal_count();
        float total_money = promotionGoods.getTotal_money();

        List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
        int size = goodsBeans.size();

        //促销金额
        float promotion_money = 0f;
        int count;

        //  1、判断是否满足 促销条件
        switch (getCondition_type()) {
            case IPromotion.CONDITION_TYPE_EVEN:
                //倍数
                if(total_count < 2) {
                    //商品总数小于2  则不满足 偶数件 直接返回
                    return;
                }

                List<Rule> rules = new ArrayList<>();

                getRules(rules, goodsBeans, total_count);


                int rules_size = rules.size();
                for (int i = 0; i < rules_size; i++) {
                    Rule rule = rules.get(i);

                    if(rule.list.size() != 2) {//不足2种商品的
                        if(rule.list.get(0).count != 2)//同种商品不足2件
                        continue;
                    }

                    rule.computeTotalMoney();
                    rule.computePromotionMoney();
                }


                int rule_list_size;

                int goods_size = goodsBeans.size();
                for (int i = 0; i < goods_size; i++) {
                    PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
                    int index = goodsBean.getIndex();
                    GoodsEntity<GoodsResponse> goodsEntity = data.get(index);

                    promotion_money = 0f;

                    //遍历 合并每个规则对象中的同一商品的促销金额
                    for (int j = 0; j < rules_size; j++) {
                        Rule rule = rules.get(j);

                        rule_list_size = rule.list.size();

                        for (int k = 0; k < rule_list_size; k++) {
                            Rule.Good good = rule.list.get(k);
                            if(good.index == index) {
                                promotion_money += good.promotion_money;
                            }

                        }
                    }
                    goodsEntity.setPromotion(this);
                    goodsBean.setPromotion_money(promotion_money);
                    Log.i(TAG, "computePromotionMoney: 商品普通（偶数件） index -> " + index + ", 促销金额 -> " + promotion_money);
//                    goodsEntity.getData().setDiscount_price(String.valueOf(promotion_money));
                    goodsEntity.getData().setGoods_activity_discount(promotion_money);
                }

                break;
            case IPromotion.CONDITION_TYPE_MONEY:
                if(total_money < condition_value) {
                    //商品总额小于条件金额 则不满足 满额 直接返回
                    return;
                }

                promotion_money = getPromotionMoney(total_money);

                for (int i = 0; i < size; i++) {
                    PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);

                    if(goodsBean.getCount() == 0) {
                        return;
                    }
                    GoodsEntity<GoodsResponse> goodsEntity = data.get(goodsBean.getIndex());

                    //需要设置促销金额的  根据比例计算出促销金额
                    float promotion = (goodsBean.getSubtotal() / total_money) * promotion_money;
                    Log.i(TAG, "computePromotionMoney: 商品普通（满金额） index -> " + goodsBean.getIndex()
                            + ", 促销金额 -> " + promotion);
                    goodsBean.setPromotion_money(promotion);
                    if(promotion > 0) {
                        goodsEntity.setPromotion(this);
                    }
//                    goodsEntity.getData().setDiscount_price(String.valueOf(promotion));
                    goodsEntity.getData().setGoods_activity_discount(promotion);
                }
                break;
        }
    }

    private void getRules(List<Rule> list, List<PromotionGoods.GoodsBean> goodsBeans, int count) {

        //创建一个规则对象
        Rule rule = new Rule(getOffer_type(), getOffer_value());

        //记录还需添加多少件 才能满足 指定件数
        int needCount = 2;

        int size = goodsBeans.size();
        for (int i = 0; i < size; i++) {
            PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);

//            int good_count = goodsBean.getCount();
            int good_count = goodsBean.getQuanlity();
            if(good_count == 0) {
                //此商品已经添加到T.list中，跳过
                continue;
            }
            Rule.Good good = new Rule.Good();
            good.index = goodsBean.getIndex();
            good.price = goodsBean.getPrice();
            if(needCount > good_count) {//指定数量大于此商品数量， 将此商品全部添加到Rule.list
                good.count = good_count;
                good.subtotal = good.price * good.count;

                rule.list.add(good);
                //计算还需要添加多少件才能满足 指定件数
                needCount = needCount - good_count;

                //更新数量、小计
                goodsBean.setQuanlity(0);
//                goodsBean.setCount(0);
                goodsBean.setSubtotal(0f);

            } else {//指定数量小于此商品数量，将指定数量添加到Rule.list
                good.count = needCount;
                good.subtotal = good.price * good.count;

                rule.list.add(good);

                int remain = good_count - needCount;
                //更新数量、小计
                goodsBean.setQuanlity(remain);
//                goodsBean.setCount(remain);
                goodsBean.setSubtotal(remain * goodsBean.getPrice());
                //已经完整添加到Rule.list
                break;
            }
        }

        //将规则对象添加到list
        list.add(rule);

        count -= 2;

        if(count > 0)
            getRules(list, goodsBeans, count);

    }

    private float getPromotionMoney(float subtotal) {
        switch (getOffer_type()) {
            case IPromotion.OFFER_TYPE_MONEY:
                return getOffer_value();
            case IPromotion.OFFER_TYPE_RATIO:
                return subtotal * (getOffer_value() / 100f);
            default:
                return 0f;
        }
    }


    /**
     * 偶数件封装对象，
     */
    private static class Rule {
        /**减免类型*/
        private int offer_type;
        /**减免金额/比例*/
        private float offer_value;

        Rule(int offer_type, float offer_value) {
            this.offer_type = offer_type;
            this.offer_value = offer_value;
        }

        /**本部分商品总价*/
        private float total_money;
        /**本部分商品总促销金额*/
        private float total_promotion_money;
        /**本部分商品列表*/
        private List<Good> list = new ArrayList<>(2);

        private static class Good {

            /**商品在数据源中的位置*/
            private int index;
            /**商品数量*/
            private int count;
            /**商品单价*/
            private float price;
            /**商品小计*/
            private float subtotal;
            /**商品促销金额*/
            private float promotion_money;
        }

        private float getPromotionMoney(float total_money) {
            switch (offer_type) {
                case IPromotion.OFFER_TYPE_MONEY:
                    return offer_value;
                case IPromotion.OFFER_TYPE_RATIO:
                    return total_money * (offer_value / 100f);
                default:
                    return 0f;
            }
        }

        /**计算本部分商品总价*/
        private void computeTotalMoney() {
            int size = list.size();

            for (int i = 0; i < size; i++) {
                Good good = list.get(i);
                total_money += good.subtotal;
            }

            total_promotion_money = getPromotionMoney(total_money);

        }

        /**计算各商品促销金额*/
        private void computePromotionMoney() {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Good good = list.get(i);

                good.promotion_money = (good.subtotal/total_money) * total_promotion_money;
            }
        }
    }
}

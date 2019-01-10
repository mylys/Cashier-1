package com.easygo.cashier.module.promotion.goods;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;

import java.util.ArrayList;
import java.util.List;

public class GoodsBundlePromotion extends BaseGoodsPromotion implements IGoodsPromotion {

    public static final String TAG = GoodsBundlePromotion.class.getSimpleName();

    @Override
    public boolean isMeetCondition(List<GoodsEntity<GoodsResponse>> data) {
        boolean result = false;
        if(isInGoodsEffectedTime()) {

        }
        return result;
    }


    /**
     * A	，4件	，20元	，共80元
     * B	，2件	，10元	，共20元
     * C	，3件	，5元	，共15元
     *
     * 购买了8件
     *
     * 满5件   总价为30
     * 满3件   总价为15
     *
     * 8/5 = 1...3
     * 8/3 = 2...2
     *
     * 选择 满5件  总价为30
     * A 4件 + B 1件     原价 4*20+10*1= 90   优惠 90-30=60
     * 其中   A 优惠了  （80/90) * 60
     *        B 优惠了  （10/90) * 60
     *
     *
     * 8-5=3
     * 选择 满3件   总价为15
     * B 1件 + C 2件     原件  1*10+2*5= 20    优惠 20-15=5
     * 其中   B 优惠了   （10/20）*5
     *        C 优惠了   （2*5/20）*5
     *
     *
     * A    （80/90) * 60
     * B    （10/90) * 60 + （10/20）*5
     * C    （2*5/20）*5
     *
     * @param data
     */

    @Override
    public void computePromotionMoney(List<GoodsEntity<GoodsResponse>> data) {
        Log.i(TAG, "computePromotionMoney: 捆绑促销");
        if(isInGoodsEffectedTime()) {
            int size = listBeans.size();

            PromotionGoods promotionGoods = getPromotionGoods();
            int total_count = promotionGoods.getTotal_count();

            //存储 需要考虑的条件
            List<GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean> temp = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean listBean = listBeans.get(i);
                //购满的件数
                int condition_value = Integer.valueOf(listBean.getCondition_value());

                if(condition_value > total_count) {
                    //购满件数大于商品总数量 不需要考虑此条
                    continue;
                }
                //需要考虑的条件加到此集合中
                temp.add(listBean);
            }

            int temp_size = temp.size();
            if(temp_size == 0) {
                //商品总数量小于 所有情况的购买件数
                return;
            }

            //==============================================

            int index = find(total_count, temp);

            GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean current = temp.get(index);
            int condition_value = Integer.valueOf(current.getCondition_value());
            float offer_value = Float.valueOf(current.getOffer_value());

            /**
             * 8     满5件
             *     A    4                   1
             *     B    2
             *     C    3
             *
             */

            List<T> list = new ArrayList<>();
            T t = new T();

            List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
            int goods_size = goodsBeans.size();
            //记录 还差多少件满足
            int subtract;
            for (int i = 0; i < goods_size; i++) {
                PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);

                int count = goodsBean.getCount();
                T.Good good = new T.Good();
                good.barcode = goodsBean.getBarcode();
                good.price = goodsBean.getPrice();
                if(condition_value > count) {
                    good.count = count;
                    good.subtotal = good.price * good.count;

                    t.list.add(good);
                    subtract = condition_value - count;
                } else {
                    subtract = condition_value - count;
                }


            }


        }
    }

    /**
     * 查找应该应用那条规则
     * @param count
     * @param list
     * @return
     */
    public int find(int count, List<GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean> list) {
        int index = -1;


        int min_quotient = count;

        int quotient;
        int remain;

        boolean remain_is_zero = false;


        int size = list.size();
        List<Result> resultlist = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean listBean = list.get(i);
            int condition_value = Integer.valueOf(listBean.getCondition_value());

            quotient = count / condition_value;
            remain = count % condition_value;

            if(remain == 0) {
                //余数等于0
                remain_is_zero = true;
                if(quotient < min_quotient) {
                    min_quotient = quotient;
                    index = i;
                }
            }


            resultlist.add(new Result(quotient, remain));
        }

        if(remain_is_zero) {
            return index;
        }

        min_quotient = resultlist.get(0).quotient;

        for (int i = 1; i < size; i++) {
            Result result = resultlist.get(i);

            if(result.quotient < min_quotient) {
                min_quotient = result.quotient;
                index = i;
            }
        }

        return index;
    }

    public static class Result {
        int quotient;
        int remain;

        Result(int quotient, int remain) {
            this.quotient = quotient;
            this.remain = remain;
        }
    }


    public static class T {
        private float total_money;
        private float total_promotion_money;
        private List<Good> list = new ArrayList<>();

        private static class Good {

            private String barcode;
            private int count;
            private float price;
            private float subtotal;
            private float promotion_money;
        }

        private void computePromotionMoney() {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                Good good = list.get(i);

                good.promotion_money = (good.subtotal/total_money) * total_promotion_money;
            }
        }

    }

}

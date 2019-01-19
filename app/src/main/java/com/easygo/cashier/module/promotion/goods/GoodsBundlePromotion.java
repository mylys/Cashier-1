package com.easygo.cashier.module.promotion.goods;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品捆绑促销
 */
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

            PromotionGoods promotionGoods = getPromotionGoods();
            int total_count = promotionGoods.getTotal_count();

            //存储 需要考虑的条件
            List<GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean> temp = new ArrayList<>();

            //填充获取 需要考虑的 规则条件
            getTempList(temp, total_count);

            int temp_size = temp.size();
            if(temp_size == 0) {
                //商品总数量小于 所有情况的购买件数
                return;
            }

            //==============================================
            List<Rule> list = new ArrayList<>();
            List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();

            //填充获取 list集合
            getRules(list, goodsBeans, total_count, temp);

            int rules_size = list.size();
            for (int i = 0; i < rules_size; i++) {
                Rule rule = list.get(i);
                rule.computeTotalMoney();
                rule.computePromotionMoney();
            }


            int rule_list_size;
            float promotion_money;

            int goods_size = goodsBeans.size();
            for (int i = 0; i < goods_size; i++) {
                PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
                int index = goodsBean.getIndex();
                GoodsEntity<GoodsResponse> goodsEntity = data.get(index);

                promotion_money = 0f;

                //遍历 合并每个规则对象中的同一商品的促销金额
                for (int j = 0; j < rules_size; j++) {
                    Rule rule = list.get(j);

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
                Log.i(TAG, "computePromotionMoney: index -> " + i + ", 促销金额 -> " + promotion_money);
                goodsEntity.getData().setDiscount_price(String.valueOf(promotion_money));
            }
        }
    }

    /**
     * 获取需要考虑的 规则集合
     * @param temp  集合
     * @param total_count 商品总数量
     */
    private void getTempList(List<GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean> temp, int total_count) {
        int size = listBeans.size();
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
    }

    /**
     * 查找应该应用哪条规则
     * @param count
     * @param list
     * @return
     */
    private int find(int count, List<GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean> list) {
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

    /**
     * 递归， 获取需要计算的Rule对象集合
     * @param list 集合
     * @param goodsBeans 顾客所购买的参与捆绑促销的商品数据信息
     * @param count 顾客所购买的商品总数量
     * @param listBeans 满指定件数，总价改为···的对象集合
     */
    private void getRules(List<Rule> list, List<PromotionGoods.GoodsBean> goodsBeans,
                         int count, List<GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean> listBeans) {

        int index = find(count, listBeans);
        if(index == -1) {
            //匹配不到规则了
            Log.i(TAG, "getRules: 没有规则匹配");
            return;
        }
        Log.i(TAG, "getRules: 匹配到规则序号 -> " + index);

        //创建一个规则对象
        Rule rule = new Rule();


        GoodsActivityResponse.ActivitiesBean.ConfigBean.ListBean current = listBeans.get(index);
        //满足的指定件数
        int condition_value = Integer.valueOf(current.getCondition_value());
        //该部分商品总价改为
        float offer_value = Float.valueOf(current.getOffer_value());

        Log.i(TAG, "getRules: 满足的件数 -> " + condition_value);
        Log.i(TAG, "getRules: 总价改为 -> " + offer_value);

        rule.total_promotion_money = offer_value;


        //记录还需添加多少件 才能满足 指定件数
        int needCount = condition_value;

        int size = goodsBeans.size();
        for (int i = 0; i < size; i++) {
            PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);

            int good_count = goodsBean.getCount();
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
                goodsBean.setCount(0);
                goodsBean.setSubtotal(0f);

            } else {//指定数量小于此商品数量，将指定数量添加到Rule.list
                good.count = needCount;
                good.subtotal = good.price * good.count;

                rule.list.add(good);

                int remain = good_count - needCount;
                //更新数量、小计
                goodsBean.setCount(remain);
                goodsBean.setSubtotal(remain * goodsBean.getPrice());
                //已经完整添加到Rule.list
                break;
            }
        }

        //将规则对象添加到list
        list.add(rule);

        //更新数量
        count -= condition_value;
        //移除已经应用的规则
        listBeans.remove(index);

        getRules(list, goodsBeans, count, listBeans);
    }


    /**
     * 保存 商 和 余数的 对象
     */
    public static class Result {
        int quotient;
        int remain;

        Result(int quotient, int remain) {
            this.quotient = quotient;
            this.remain = remain;
        }
    }


    /**
     * 封装对象，封装了 需要计算的 一条规则
     * 满  x  件， 该部分商品总价改为  y  元
     */
    private static class Rule {
        /**本部分商品总价*/
        private float total_money;
        /**本部分商品总促销金额*/
        private float total_promotion_money;
        /**本部分商品列表*/
        private List<Good> list = new ArrayList<>();

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

        /**计算本部分商品总价*/
        private void computeTotalMoney() {
            int size = list.size();

            for (int i = 0; i < size; i++) {
                Good good = list.get(i);
                total_money += good.subtotal;
            }
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

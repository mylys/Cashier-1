package com.easygo.cashier;

import android.util.ArrayMap;
import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.ShopActivityResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;
import com.easygo.cashier.module.promotion.goods.BaseGoodsPromotion;
import com.easygo.cashier.module.promotion.goods.GoodsFulfilMoneyPromotion;
import com.easygo.cashier.module.promotion.goods.GoodsBundlePromotion;
import com.easygo.cashier.module.promotion.goods.GoodsNormalPromotion;
import com.easygo.cashier.module.promotion.goods.GoodsTimePromotion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 促销工具类
 */
public class ActivitiesUtils {

    public static final String TAG = ActivitiesUtils.class.getSimpleName();
    /**
     * Barcode映射活动id
     */
    private ArrayMap<String, Integer> barcode2IdMap = new ArrayMap<>();
    /**
     * 活动id映射促销对象，保存所有促销对象的集合
     */
    private ArrayMap<Integer, BaseGoodsPromotion> id2PromotionMap = new ArrayMap<>();
    /**
     * 需要计算促销的促销对象集合
     */
    private ArrayMap<Integer, BaseGoodsPromotion> needComputeMap = new ArrayMap<>();


    private ActivitiesUtils() {}

    public static ActivitiesUtils getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static final ActivitiesUtils sInstance = new ActivitiesUtils();
    }

    public void parseGoods(GoodsActivityResponse response) {
        List<GoodsActivityResponse.ActivitiesBean> activities = response.getActivities();
        int size = activities.size();
        if(size == 0) {
            Log.i(TAG, "parseGoods: 没有促销活动");
            return;
        }
        List<GoodsActivityResponse.MapBean> map = response.getMap();
        barcode2IdMap = toMap(map);

        id2PromotionMap.clear();

        for (int i = 0; i < size; i++) {
            GoodsActivityResponse.ActivitiesBean activitiesBean = activities.get(i);
            BaseGoodsPromotion promotion = new BaseGoodsPromotion();
            promotion.setId(activitiesBean.getId());
            promotion.setName(activitiesBean.getName());
            promotion.setType(activitiesBean.getType());
            promotion.setGoods_effected_at(activitiesBean.getEffected_at());
            promotion.setGoods_expired_at(activitiesBean.getExpired_at());
            promotion.setGoodsBeans(activitiesBean.getGoods());

            GoodsActivityResponse.ActivitiesBean.ConfigBean config = activitiesBean.getConfig();
            int type = activitiesBean.getType();
            switch (type) {
                case IGoodsPromotion.TYPE_NORMAL:
                    GoodsNormalPromotion normalPromotion = new GoodsNormalPromotion();
                    fillData(normalPromotion, activitiesBean);
                    normalPromotion.setCondition_type(config.getCondition_type());
                    normalPromotion.setCondition_value(Float.valueOf(config.getCondition_value()));
                    normalPromotion.setOffer_type(activitiesBean.getConfig().getOffer_type());
                    normalPromotion.setOffer_value(Float.valueOf(activitiesBean.getConfig().getOffer_value()));
                    id2PromotionMap.put(activitiesBean.getId(), normalPromotion);
                    break;
                case IGoodsPromotion.TYPE_TIME:
                    GoodsTimePromotion timePromotion = new GoodsTimePromotion();
                    fillData(timePromotion, activitiesBean);
                    timePromotion.setListBeans(activitiesBean.getConfig().getList());
                    id2PromotionMap.put(activitiesBean.getId(), timePromotion);
                    break;
                case IGoodsPromotion.TYPE_MEET:
                    GoodsFulfilMoneyPromotion moneyPromotion = new GoodsFulfilMoneyPromotion();
                    fillData(moneyPromotion, activitiesBean);
                    moneyPromotion.setListBeans(activitiesBean.getConfig().getList());
                    id2PromotionMap.put(activitiesBean.getId(), moneyPromotion);
                    break;
                case IGoodsPromotion.TYPE_BUNDLE:
                    GoodsBundlePromotion bundlePromotion = new GoodsBundlePromotion();
                    fillData(bundlePromotion, activitiesBean);
                    bundlePromotion.setListBeans(activitiesBean.getConfig().getList());
                    id2PromotionMap.put(activitiesBean.getId(), bundlePromotion);
                    break;
            }
        }
    }

    private void fillData(BaseGoodsPromotion promotion, GoodsActivityResponse.ActivitiesBean activitiesBean) {
        promotion.setId(activitiesBean.getId());
        promotion.setName(activitiesBean.getName());
        promotion.setType(activitiesBean.getType());
        promotion.setGoods_effected_at(activitiesBean.getEffected_at());
        promotion.setGoods_expired_at(activitiesBean.getExpired_at());
        promotion.setGoodsBeans(activitiesBean.getGoods());
    }


    public void promotion(List<GoodsEntity<GoodsResponse>> data) {

        if(barcode2IdMap == null) {
            return;
        } else if(id2PromotionMap == null) {
            return;
        }

        int size = data.size();
        String barcode;
        int actvitity_id;

        //  1、遍历顾客所购商品
        for (int i = 0; i < size; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = data.get(i);
            barcode = goodsEntity.getData().getBarcode();

            actvitity_id = -1;
            //  2、根据barcode 去map中找此商品是否有促销信息
            actvitity_id = barcode2IdMap.get(barcode);

            if(actvitity_id == -1) {
                //说明此商品没有促销信息
                continue;
            }
            //此商品有促销信息
            //  3、根据活动id 去map中找此活动对象
            BaseGoodsPromotion promotion = id2PromotionMap.get(actvitity_id);

            List<GoodsActivityResponse.ActivitiesBean.GoodsBean> goodsBeans = promotion.getGoodsBeans();
            int goods_size = goodsBeans.size();
            PromotionGoods promotionGoods = null;
            //  4、遍历活动中参与促销的商品 找到各有多少件、小计等相关数据
            for (int j = 0; j < goods_size; j++) {
                GoodsActivityResponse.ActivitiesBean.GoodsBean goodsBean = goodsBeans.get(j);
                //判断 比对成功
                if(barcode.equals(goodsBean.getBarcode())) {
                    promotionGoods = promotion.getPromotionGoods();
                    if(promotionGoods == null) {
                        promotionGoods = new PromotionGoods();
                    }

                    //设置数量、小计、位置等信息
                    PromotionGoods.GoodsBean good = new PromotionGoods.GoodsBean();
                    good.setIndex(i);//设置在data数据源中的位置
                    good.setBarcode(barcode);
                    good.setCount(goodsEntity.getCount());
                    good.setPrice(Float.valueOf(goodsEntity.getData().getPrice()));
                    good.setSubtotal(goodsEntity.getCount() * Float.valueOf(goodsEntity.getData().getPrice()));
                    promotionGoods.getGoodsBeans().add(good);
                    break;
                }

            }


            if(promotionGoods != null) {
                List<PromotionGoods.GoodsBean> list = promotionGoods.getGoodsBeans();
                //将商品按照价格 从高到底排序
                Collections.reverse(list);
                promotionGoods.setGoodsBeans(list);
                promotion.setPromotionGoods(promotionGoods);

                //  5、判断 添加到 needComputeMap
                if(needComputeMap.get(actvitity_id) == null) {
                    needComputeMap.put(actvitity_id, promotion);
                }
            }
        }

        //  6、遍历 needComputeMap 判断各个促销是否满足促销条件、计算优惠价格
        for (Map.Entry<Integer, BaseGoodsPromotion> entry : needComputeMap.entrySet()) {
            BaseGoodsPromotion promotion = entry.getValue();

            promotion.computePromotionGoods();
            promotion.computePromotionMoney(data);

        }



    }


    /**
     * 商品促销
     * @param response
     */
    public void parseGoodsActivity(GoodsActivityResponse response) {

    }

    public ArrayMap<String, Integer> toMap(List<GoodsActivityResponse.MapBean> mapBeans) {
        ArrayMap<String, Integer> map = new ArrayMap<>();

        int size = mapBeans.size();
        for (int i = 0; i < size; i++) {
            GoodsActivityResponse.MapBean mapBean = mapBeans.get(i);
            map.put(mapBean.getBarcode(), mapBean.getActivity_id());
        }

        return map;
    }
    public ArrayMap<Integer, BaseGoodsPromotion> getAllPromotionMap(List<BaseGoodsPromotion> promotions) {
        ArrayMap<Integer, BaseGoodsPromotion> map = new ArrayMap<>();

        int size = promotions.size();
        for (int i = 0; i < size; i++) {
            BaseGoodsPromotion promotion = promotions.get(i);
            map.put(promotion.getId(), promotion);
        }

        return map;
    }


    public void getGoods(List<GoodsActivityResponse.MapBean> mapBeans, List<BaseGoodsPromotion> promotions,
                         List<GoodsEntity<GoodsResponse>> data) {
        //根据顾客购买的商品条码 找到所有活动id
        ArrayMap<String, Integer> map = toMap(mapBeans);

        //遍历
        int size = data.size();
        String barcode;
        int activity_id;
        for (int i = 0; i < size; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = data.get(i);

            if(goodsEntity.getPromotion()== null) {//没有参与促销
                barcode = goodsEntity.getData().getBarcode();
                //根据条码 到 条码->活动id 映射中找到活动id
                if(map.containsKey(barcode)) {
                    activity_id = map.get(barcode);

                    //根据活动id  找到 促销对象
                    BaseGoodsPromotion promotion = null;
                    int promotion_size = promotions.size();
                    for (int j = 0; j < promotion_size; j++) {
                        if (activity_id == promotions.get(j).getId()) {
                            promotion = promotions.get(j);
                        }
                    }

                    if(promotion == null || !promotion.isInGoodsEffectedTime()) {
                        //找不到活动 或者 不在有效时段内 进入下一循环
                        continue;
                    }

                    //根据活动id 遍历data 寻找此活动中参数促销的商品及其数量、小计和总数量、总小计
                    for (int j = 0; j < size; j++) {
                        
                    }


                }


            }


        }

        //根据活动id  计算商品的优惠价格

    }

    /**
     * 查找促销活动中参与促销的各个商品的数量、小计，及总数量、总小计，存入PromotionGoods对象
     * @param data
     */
    public void findGoodsInPromotion(List<GoodsEntity<GoodsResponse>> data, BaseGoodsPromotion promotion) {

        PromotionGoods promotionGoods = new PromotionGoods();


        List<GoodsActivityResponse.ActivitiesBean.GoodsBean> goodsBeans = promotion.getGoodsBeans();
        int promotion_goods_size = goodsBeans.size();
        for (int i = 0; i < promotion_goods_size; i++) {
            GoodsActivityResponse.ActivitiesBean.GoodsBean goodsBean = goodsBeans.get(i);
            String barcode = goodsBean.getBarcode();
            //遍历data 获取商品 数量 小计等




        }

        int size = data.size();
        for (int i = 0; i < size; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = data.get(i);

        }
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

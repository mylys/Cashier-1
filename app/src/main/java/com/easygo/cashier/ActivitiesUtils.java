package com.easygo.cashier;

import android.util.ArrayMap;
import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.ShopActivityResponse;
import com.easygo.cashier.module.promotion.base.IGoodsPromotion;
import com.easygo.cashier.module.promotion.base.IPromotion;
import com.easygo.cashier.module.promotion.base.PromotionGoods;
import com.easygo.cashier.module.promotion.goods.BaseGoodsPromotion;
import com.easygo.cashier.module.promotion.goods.GoodsBundlePromotion;
import com.easygo.cashier.module.promotion.goods.GoodsFixedPromotion;
import com.easygo.cashier.module.promotion.goods.GoodsFulfilMoneyPromotion;
import com.easygo.cashier.module.promotion.goods.GoodsFulfilReducePromotion;
import com.easygo.cashier.module.promotion.goods.GoodsFullfilGivePromotion;
import com.easygo.cashier.module.promotion.goods.GoodsNormalPromotion;
import com.easygo.cashier.module.promotion.goods.GoodsTimePromotion;
import com.easygo.cashier.module.promotion.shop.BaseShopPromotion;
import com.easygo.cashier.module.promotion.shop.ShopNormalPromotion;
import com.easygo.cashier.module.promotion.shop.ShopTimePromotion;
import com.easygo.cashier.module.promotion.temp.TempGoodsPromotion;
import com.easygo.cashier.module.promotion.temp.TempOrderPromotion;

import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * 店铺促销对象集合
     */
    private List<BaseShopPromotion> shopList = new ArrayList<>();
    /**
     * 店铺促销排除商品集合
     */
    private List<String> excludeBarcodeList = new ArrayList<>();


    private BaseShopPromotion currentShopPromotion;
    private List<BaseGoodsPromotion> currentGoodsPromotions;

    private float mShopPromotionMoney;
    private float mGoodsPromotionMoney;
    private float mTempGoodsPromotionMoney;
    private float mTempOrderPromotionMoney;
    private boolean goods_with_coupon = true;
    private boolean shop_with_coupon = true;

    private List<String> currentPromotionNames;
    private boolean hasTempGoodsPromotion;


    private ActivitiesUtils() {}

    public static ActivitiesUtils getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static final ActivitiesUtils sInstance = new ActivitiesUtils();
    }


    private List<BaseGoodsPromotion> tempGoodsPromotions = new ArrayList<>();
    private TempOrderPromotion currentTempOrderPromotion;
    /**
     * 临时商品促销Map    key为    条码_单价
     */
    private ArrayMap<String, BaseGoodsPromotion> barcode2TempMap = new ArrayMap<>();

    /**
     * 创建临时商品促销
     * @param selecteds
     * @param mode
     * @param isFreeOrder
     * @param value
     */
    public void createTempGoodsPromotion(List<GoodsEntity<GoodsResponse>> selecteds, int mode, boolean isFreeOrder, float value) {

        int size = selecteds.size();
        if(size == 0) {
            return;
        }

        for (int i = 0; i < size; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = selecteds.get(i);
            GoodsResponse data = goodsEntity.getData();
            String barcode = data.getBarcode();

            TempGoodsPromotion promotion = new TempGoodsPromotion();
            promotion.setId(-promotion.hashCode());
            promotion.setName("临时商品促销");
            promotion.setType(IPromotion.TYPE_TEMP);
            promotion.setWith_coupon(1);
            promotion.setOffer_type(mode);
            if(mode == IPromotion.OFFER_TYPE_RATIO) {
                if (isFreeOrder) {
                    promotion.setOffer_value(0);
                } else {
                    promotion.setOffer_value(value);
                }
            } else if(mode == IPromotion.OFFER_TYPE_MONEY) {
                if (isFreeOrder) {
                    promotion.setOffer_value(0);
                } else {
                    promotion.setOffer_value(value);
                }
            }

            //封装进集合中
            List<GoodsActivityResponse.ActivitiesBean.GoodsBean> goods = new ArrayList<>();
            GoodsActivityResponse.ActivitiesBean.GoodsBean goodsBean = new GoodsActivityResponse.ActivitiesBean.GoodsBean();
            goodsBean.setBarcode(barcode);
            goodsBean.setG_sku_id(data.getG_sku_id());
            goodsBean.setSku_name(data.getG_sku_name());
            goods.add(goodsBean);

            promotion.setGoodsBeans(goods);

            barcode2TempMap.put(barcode + "_" + data.getPrice(), promotion);
        }


    }
    /**取消指定商品临时促销*/
    public void cancelTempGoodsPromotion(String key) {
        barcode2TempMap.remove(key);
    }
    /**取消所有商品临时促销*/
    public void clearTempPromotion() {
        tempGoodsPromotions.clear();
        barcode2TempMap.clear();
        hasTempGoodsPromotion = false;
    }

    /**
     * 创建临时整单临时促销
     * @param mode
     * @param isFreeOrder
     * @param value
     */
    public void createTempOrderPromotion(int mode, boolean isFreeOrder, float value) {
        TempOrderPromotion promotion = new TempOrderPromotion();
        promotion.setId(-promotion.hashCode());
        promotion.setName("临时整单促销");
        promotion.setType(IPromotion.TYPE_TEMP);
        promotion.setWith_coupon(1);
        promotion.setOffer_type(mode);
        if(mode == IPromotion.OFFER_TYPE_RATIO) {
            if (isFreeOrder) {
                promotion.setOffer_value(0);
            } else {
                promotion.setOffer_value(value);
            }
        } else if(mode == IPromotion.OFFER_TYPE_MONEY) {
            if (isFreeOrder) {
                promotion.setOffer_value(0);
            } else {
                promotion.setOffer_value(value);
            }
        }

        currentTempOrderPromotion = promotion;
    }

    public void clearTempOrderPromotion() {
        currentTempOrderPromotion = null;
        mTempOrderPromotionMoney = 0f;
    }

    /**
     * 获取临时整单促销的促销金额
     * @param money
     * @return
     */
    public float getTempOrderPromotionMoney(float money) {
        if(currentTempOrderPromotion == null) {
            return 0f;
        }

        float promotionMoney = currentTempOrderPromotion.getPromotionMoney(money);

        if(promotionMoney > 0) {
            Log.i(TAG, "promotion: " + currentTempOrderPromotion.getName() + ", 促销金额： " + promotionMoney);
            //记录临时整单促销金额
            mTempOrderPromotionMoney = promotionMoney;

            return promotionMoney;
        }
        return 0f;
    }

    public float getTempOrderPromotionMoney(List<GoodsEntity<GoodsResponse>> data, float money) {
        if(currentTempOrderPromotion == null) {
            return 0f;
        }

        float promotionMoney = currentTempOrderPromotion.getPromotionMoney(money);

        if(promotionMoney > 0) {
            Log.i(TAG, "promotion: " + currentTempOrderPromotion.getName() + ", 促销金额： " + promotionMoney);
            //记录临时整单促销金额
            mTempOrderPromotionMoney = promotionMoney;

        }

        //有临时整单促销时
        if(currentTempOrderPromotion != null && mTempOrderPromotionMoney != 0) {
            int data_size = data.size();
            float temp_order_discount;
            float subtotal;
            for (int i = 0; i < data_size; i++) {
                GoodsEntity<GoodsResponse> goodsEntity = data.get(i);
                GoodsResponse good = goodsEntity.getData();
                subtotal = Float.parseFloat(good.getPrice()) * goodsEntity.getCount()
                        - Float.parseFloat(good.getDiscount_price());
                temp_order_discount = mTempOrderPromotionMoney * (subtotal / money);
                //设置临时整单促销分摊优惠金额
                good.setTemp_order_discount(temp_order_discount);
            }
            return mTempOrderPromotionMoney;
        } else {
            return 0f;
        }
    }




    /**
     * 根据商品促销返回体，解析出促销对象并保存
     * @param response
     */
    public void parseGoods(GoodsActivityResponse response) {

        if (currentGoodsPromotions == null) {
            currentGoodsPromotions = new ArrayList<>();
        } else {
            currentGoodsPromotions.clear();
        }

        List<GoodsActivityResponse.ActivitiesBean> activities = response.getActivities();
        int size = activities.size();
        if(size == 0) {
            Log.i(TAG, "parseGoods: 没有促销活动");
            barcode2IdMap = null;
            id2PromotionMap.clear();
            return;
        }
        List<GoodsActivityResponse.MapBean> map = response.getMap();
        barcode2IdMap = toMap(map);

        id2PromotionMap.clear();

        for (int i = 0; i < size; i++) {
            GoodsActivityResponse.ActivitiesBean activitiesBean = activities.get(i);

            GoodsActivityResponse.ActivitiesBean.ConfigBean config = activitiesBean.getConfig();
            int type = activitiesBean.getType();
            switch (type) {
                case IGoodsPromotion.TYPE_NORMAL://普通促销
                    GoodsNormalPromotion normalPromotion = new GoodsNormalPromotion();
                    fillData(normalPromotion, activitiesBean);
                    normalPromotion.setCondition_type(config.getCondition_type());
                    normalPromotion.setCondition_value(Float.valueOf(config.getCondition_value()));
                    normalPromotion.setOffer_type(config.getOffer_type());
                    normalPromotion.setOffer_value(Float.valueOf(config.getOffer_value()));
                    id2PromotionMap.put(activitiesBean.getId(), normalPromotion);
                    break;
                case IGoodsPromotion.TYPE_TIME://时段促销
                    GoodsTimePromotion timePromotion = new GoodsTimePromotion();
                    fillData(timePromotion, activitiesBean);
                    timePromotion.setListBeans(config.getList());
                    id2PromotionMap.put(activitiesBean.getId(), timePromotion);
                    break;
                case IGoodsPromotion.TYPE_MEET://满额促销
                    GoodsFulfilMoneyPromotion moneyPromotion = new GoodsFulfilMoneyPromotion();
                    fillData(moneyPromotion, activitiesBean);
                    moneyPromotion.setCondition_value(Float.valueOf(config.getCondition_value()));
                    moneyPromotion.setListBeans(config.getList());
                    id2PromotionMap.put(activitiesBean.getId(), moneyPromotion);
                    break;
                case IGoodsPromotion.TYPE_BUNDLE://捆绑促销
                    GoodsBundlePromotion bundlePromotion = new GoodsBundlePromotion();
                    fillData(bundlePromotion, activitiesBean);
                    bundlePromotion.setListBeans(config.getList());
                    id2PromotionMap.put(activitiesBean.getId(), bundlePromotion);
                    break;
                case IGoodsPromotion.TYPE_FIXED://固定促销
                    GoodsFixedPromotion fixedPromotion = new GoodsFixedPromotion();
                    fillData(fixedPromotion, activitiesBean);
                    fixedPromotion.setListBeans(config.getList());
                    id2PromotionMap.put(activitiesBean.getId(), fixedPromotion);
                    break;
                case IGoodsPromotion.TYPE_REDUCE://满减促销
                    GoodsFulfilReducePromotion reducePromotion = new GoodsFulfilReducePromotion();
                    fillData(reducePromotion, activitiesBean);
                    reducePromotion.setListBeans(config.getList());
                    id2PromotionMap.put(activitiesBean.getId(), reducePromotion);
                    break;
                case IGoodsPromotion.TYPE_GIVE://满赠促销
                    GoodsFullfilGivePromotion givePromotion = new GoodsFullfilGivePromotion();
                    fillData(givePromotion, activitiesBean);
                    givePromotion.setCondition_type(config.getCondition_type());
                    givePromotion.setListBeans(config.getList());
                    id2PromotionMap.put(activitiesBean.getId(), givePromotion);
                    break;
            }
        }
    }

    private void fillData(BaseGoodsPromotion promotion, GoodsActivityResponse.ActivitiesBean activitiesBean) {
        promotion.setId(activitiesBean.getId());
        promotion.setName(activitiesBean.getName());
        promotion.setType(activitiesBean.getType());
        promotion.setWith_coupon(activitiesBean.getWith_coupon());
        promotion.setGoods_effected_at(activitiesBean.getEffected_at());
        promotion.setGoods_expired_at(activitiesBean.getExpired_at());
        promotion.setGoodsBeans(activitiesBean.getGoods());
    }


    /**
     * 应用商品促销
     */
    public void promotion(List<GoodsEntity<GoodsResponse>> data) {
        int size = data.size();
        //重置
        GoodsResponse goodsResponse;
        for (int i = 0; i < size; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = data.get(i);
            goodsResponse = goodsEntity.getData();
            goodsResponse.setDiscount_price("0.00");
            goodsResponse.setGoods_activity_discount(0);
            goodsResponse.setTemp_goods_discount(0);
            goodsResponse.setShop_activity_discount(0);
            goodsResponse.setCoupon_discount(0);
            goodsResponse.setMember_discount(0);
            goodsEntity.setPromotion(null);
        }

        if(barcode2TempMap.isEmpty()) {

            if (barcode2IdMap == null) {
                return;
            } else if (id2PromotionMap == null) {
                return;
            }
        }
        needComputeMap.clear();

        String barcode;
        int actvitity_id = -1;


        for (Map.Entry<String, BaseGoodsPromotion> entry : barcode2TempMap.entrySet()) {
            BaseGoodsPromotion promotion = entry.getValue();
            promotion.setPromotionGoods(null);
        }
        for (Map.Entry<Integer, BaseGoodsPromotion> entry : id2PromotionMap.entrySet()) {
            BaseGoodsPromotion promotion = entry.getValue();
            promotion.setPromotionGoods(null);
        }

        //  1、遍历顾客所购商品
        for (int i = 0; i < size; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = data.get(i);
            barcode = goodsEntity.getData().getBarcode();

            // 2、先根据barcode去map中找临时商品促销活动对象，找不到则根据活动id去map中找商品促销活动对象
            BaseGoodsPromotion promotion = null;
            if (!barcode2TempMap.isEmpty()) {//有临时促销
                promotion = barcode2TempMap.get(barcode + "_" + goodsEntity.getData().getPrice());
                actvitity_id = promotion == null? -1: promotion.getId();

            }
            if(promotion == null && barcode2IdMap == null) {//没有临时商品促销 也没有商品促销时
                continue;
            }

            if (promotion == null && barcode2IdMap != null) {//临时促销中找不到

                //  3、根据barcode 去map中找此商品是否有促销信息
                Integer integer = barcode2IdMap.get(barcode);
                actvitity_id = integer == null ? -1 : integer;

                if (actvitity_id == -1) {
                    //说明此商品没有促销信息
                    continue;
                }
                promotion = id2PromotionMap.get(actvitity_id);
            }

            if(promotion == null) {
                continue;
            }

            List<GoodsActivityResponse.ActivitiesBean.GoodsBean> goodsBeans = promotion.getGoodsBeans();
            int goods_size = goodsBeans.size();
            PromotionGoods promotionGoods = promotion.getPromotionGoods();
            if(promotionGoods == null) {
                promotionGoods = new PromotionGoods();
            }
            //  4、遍历活动中参与促销的商品 找到各有多少件、小计等相关数据
            for (int j = 0; j < goods_size; j++) {
                GoodsActivityResponse.ActivitiesBean.GoodsBean goodsBean = goodsBeans.get(j);
                //判断 比对成功
                if(barcode.equals(goodsBean.getBarcode())) {

                    boolean weight = goodsEntity.getItemType() == GoodsEntity.TYPE_WEIGHT
                            || goodsEntity.getItemType() == GoodsEntity.TYPE_PROCESSING;

                    //设置数量、小计、位置等信息
                    PromotionGoods.GoodsBean good = new PromotionGoods.GoodsBean();
                    good.setIndex(i);//设置在data数据源中的位置
                    good.setBarcode(barcode);
                    good.setQuanlity(weight? 1: (int) goodsEntity.getCount());
                    good.setCount(goodsEntity.getCount());
                    good.setPrice(Float.valueOf(goodsEntity.getData().getPrice()));
                    good.setSubtotal(goodsEntity.getCount() * Float.valueOf(goodsEntity.getData().getPrice()));
                    promotionGoods.getGoodsBeans().add(good);
                    break;
                }

            }


            if(promotionGoods != null) {
                List<PromotionGoods.GoodsBean> list = promotionGoods.getGoodsBeans();
                Collections.sort(list);
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
     * 根据商品条码-活动id，解析转换成映射
     */
    public ArrayMap<String, Integer> toMap(List<GoodsActivityResponse.MapBean> mapBeans) {
        ArrayMap<String, Integer> map = new ArrayMap<>();

        int size = mapBeans.size();
        for (int i = 0; i < size; i++) {
            GoodsActivityResponse.MapBean mapBean = mapBeans.get(i);
            map.put(mapBean.getBarcode(), mapBean.getActivity_id());
        }

        return map;
    }

    /**
     * 根据店铺促销返回体，解析出促销对象并保存
     * @param response
     */
    public void parseShop(ShopActivityResponse response) {
        //重置
        shopList.clear();
        currentShopPromotion = null;
        excludeBarcodeList.clear();

        List<ShopActivityResponse.ListBean> list = response.getList();
        //保存店铺促销需要排除的商品条码
        if(list != null && list.size() > 0) {
            List<ShopActivityResponse.ListBean.ExcludeListBean> exclude_list = list.get(0).getExclude_list();
            if (exclude_list != null) {
                int exclude_list_size = exclude_list.size();
                for (int i = 0; i < exclude_list_size; i++) {
                    ShopActivityResponse.ListBean.ExcludeListBean excludeListBean = exclude_list.get(i);
                    excludeBarcodeList.add(excludeListBean.getBarcode());
                }
            }
        }

        int size = list.size();
        for (int i = 0; i < size; i++) {
            ShopActivityResponse.ListBean listBean = list.get(i);
            int type = listBean.getType();

            switch (type) {
                case IPromotion.TYPE_NORMAL:
                    ShopNormalPromotion normalPromotion = new ShopNormalPromotion();
                    fillData(normalPromotion, listBean);
                    shopList.add(normalPromotion);
                    break;
                case IPromotion.TYPE_TIME:
                    ShopTimePromotion timePromotion = new ShopTimePromotion();
                    fillData(timePromotion, listBean);
                    shopList.add(timePromotion);
                    break;
            }
        }
    }

    private void fillData(BaseShopPromotion promotion, ShopActivityResponse.ListBean listBean) {
        promotion.setId(listBean.getId());
        promotion.setName(listBean.getName());
        promotion.setType(listBean.getType());
        promotion.setWith_coupon(listBean.getWith_coupon());
        promotion.setGoods_effected_at(listBean.getEffected_at());
        promotion.setGoods_expired_at(listBean.getExpired_at());
        promotion.setConfigBeans(listBean.getConfig());
    }

    /**
     * 计算店铺促销
     * @param total_money 订单总额
     */
    public float promotion(float total_money)  {
        int size = shopList.size();
        if(size == 0) {
            return 0f;
        }

        currentShopPromotion = null;

        shop_with_coupon = true;

        for (int i = 0; i < size; i++) {

            BaseShopPromotion baseShopPromotion = shopList.get(i);
            float promotionMoney = baseShopPromotion.getPromotionMoney(total_money);

            if(promotionMoney > 0) {
                Log.i(TAG, "promotion: " + baseShopPromotion.getName() + ", 促销金额： " + promotionMoney);
                currentShopPromotion = baseShopPromotion;
                //记录店铺促销金额
                mShopPromotionMoney = promotionMoney;

                shop_with_coupon = currentShopPromotion.getWith_coupon() == 1;

                return promotionMoney;
            }
        }
        return 0f;
    }

    /**
     * 计算店铺促销, 并分摊优惠金额
     * @param total_money 订单总额
     * @return 店铺促销优惠总金额
     */
    public float promotion(List<GoodsEntity<GoodsResponse>> data, float total_money) {
        int size = shopList.size();
        if(size == 0) {
            return 0f;
        }
        currentShopPromotion = null;

        shop_with_coupon = true;

        for (int i = 0; i < size; i++) {

            BaseShopPromotion baseShopPromotion = shopList.get(i);
            float promotionMoney = baseShopPromotion.getPromotionMoney(total_money);

            if(promotionMoney > 0) {
                Log.i(TAG, "promotion: " + baseShopPromotion.getName() + ", 促销金额： " + promotionMoney);
                currentShopPromotion = baseShopPromotion;
                //记录店铺促销金额
                mShopPromotionMoney = promotionMoney;

                shop_with_coupon = currentShopPromotion.getWith_coupon() == 1;
            }
        }

        //有店铺促销时
        if(currentShopPromotion != null && mShopPromotionMoney != 0) {
            int data_size = data.size();
            float discount_price;
            float goods_discount;
            float temp_goods_discount;
            float member_discount;
            float subtotal;
            for (int i = 0; i < data_size; i++) {
                GoodsEntity<GoodsResponse> goodsEntity = data.get(i);
                GoodsResponse good = goodsEntity.getData();
                temp_goods_discount = good.getTemp_goods_discount();
                goods_discount = good.getGoods_activity_discount();
                member_discount = good.getMember_discount();
                if(temp_goods_discount != 0 || goods_discount != 0 || member_discount != 0) {
                    continue;
                }
                if(goodsEntity.isExcludeInShopActivity()) {
                    continue;
                }
                subtotal = Float.parseFloat(good.getPrice()) * goodsEntity.getCount();
                discount_price = mShopPromotionMoney * (subtotal / total_money);
//                good.setDiscount_price(String.valueOf(discount_price));
                good.setShop_activity_discount(discount_price);
            }
            return mShopPromotionMoney;
        } else {
            return 0f;
        }
    }


    public void getCurrentGoodsPromotions(List<GoodsEntity<GoodsResponse>> data) {
        mTempGoodsPromotionMoney = 0f;
        mGoodsPromotionMoney = 0f;
        int size = data.size();
        BaseGoodsPromotion promotion;
        if (currentGoodsPromotions == null) {
            currentGoodsPromotions = new ArrayList<>();
        } else {
            currentGoodsPromotions.clear();
        }
        goods_with_coupon = true;
        hasTempGoodsPromotion = false;
        float temp_goods_discount = 0f;
        for (int i = 0; i < size; i++) {

            promotion = data.get(i).getPromotion();
            if(promotion != null) {
                //添加到正在参与的商品促销集合中
                currentGoodsPromotions.add(promotion);
                //累加商品促销金额
                temp_goods_discount = data.get(i).getData().getTemp_goods_discount();
                mTempGoodsPromotionMoney += temp_goods_discount;
                if(temp_goods_discount != 0) {
                    mGoodsPromotionMoney += temp_goods_discount;
                } else {
                    mGoodsPromotionMoney += data.get(i).getData().getGoods_activity_discount();
                }
                if(promotion.getWith_coupon() == 0) {
                    goods_with_coupon = false;
                }
                if(promotion.isTempGoodsPromotion()) {
                    hasTempGoodsPromotion = true;
                }
            }
        }
        Log.i(TAG, "getCurrentGoodsPromotions: 商品总促销金额 -> " + mGoodsPromotionMoney);
    }

    public List<BaseGoodsPromotion> getCurrentGoodsPromotions() {
        return currentGoodsPromotions;
    }


    public boolean hasGoodsPromotion() {
        return currentGoodsPromotions != null && currentGoodsPromotions.size() != 0;
    }
    public boolean hasShopPromotion() {
        return currentShopPromotion != null;
    }
    public boolean hasTempGoodsPromotion() {
        return hasTempGoodsPromotion;
    }
    public boolean hasTempOrderPromotion() {
        return currentTempOrderPromotion != null;
    }

    public TempOrderPromotion getCurrentTempOrderPromotion() {
        return currentTempOrderPromotion;
    }


    public BaseShopPromotion getCurrentShopPromotion() {
        return currentShopPromotion;
    }

    public float getShopPromotionMoney() {
        return mShopPromotionMoney;
    }

    public float getGoodsPromotionMoney() {
        return mGoodsPromotionMoney;
    }

    public float getTempGoodsPromotionMoney() {
        return mTempGoodsPromotionMoney;
    }

    public float getTempOrderPromotionMoney() {
        return mTempOrderPromotionMoney;
    }

    public List<String> getCurrentPromotionNames() {
        if(currentPromotionNames == null) {
            currentPromotionNames = new ArrayList<>();
        } else {
            currentPromotionNames.clear();
        }

        //商品促销
        if(currentGoodsPromotions != null && currentGoodsPromotions.size() != 0) {
            int size = currentGoodsPromotions.size();
            for (int i = 0; i < size; i++) {
                BaseGoodsPromotion promotion = currentGoodsPromotions.get(i);
                String name = promotion.getName();

                if(currentPromotionNames.contains(name)) {
                    continue;
                }
                currentPromotionNames.add(name);

            }
        }
        //店铺促销
        else if(currentShopPromotion != null){
            currentPromotionNames.add((currentShopPromotion.getName()));
        }

        return currentPromotionNames;

    }

    public boolean isWith_coupon() {
        boolean has_goods = hasGoodsPromotion();
        boolean has_shop = hasShopPromotion();

        if(has_goods) {
            //有商品促销
            if(has_shop) {//有店铺促销
                return goods_with_coupon && shop_with_coupon;
            } else {//没有店铺促销
                return goods_with_coupon;
            }
        }
        //只有店铺促销
        else if(has_shop) {
            return shop_with_coupon;
        }
        //没有任何促销
        else {
            return true;
        }
    }

    public List<String> getShopExcludeBarcodeList() {
        return excludeBarcodeList;
    }


    public void reset() {
        currentShopPromotion = null;
        currentGoodsPromotions = null;
        currentPromotionNames = null;
        goods_with_coupon = true;
        shop_with_coupon = true;
        hasTempGoodsPromotion = false;

        //清除临时商品促销
       clearTempPromotion();
        //清除临时整单促销
       clearTempOrderPromotion();
    }
}

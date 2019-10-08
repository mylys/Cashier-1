package com.easygo.cashier.utils;

import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.easygo.cashier.Configs;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.Counpons;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.module.promotion.base.PromotionGoods;
import com.niubility.library.utils.GsonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 优惠券工具类
 */
public class CouponUtils {

    private static final String TAG = CouponUtils.class.getSimpleName();

    private CouponUtils() {
    }

    public static CouponUtils getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static final CouponUtils sInstance = new CouponUtils();
    }

    private CouponResponse mCouponResponse;
    private float coupon_discount;

    private List<CouponResponse> mAllCoupons;
    private List<GoodsEntity<GoodsResponse>> mGoodsData;

    public void setCouponInfo(CouponResponse result) {
        mCouponResponse = result;

        if (result == null) {
            coupon_discount = 0f;

            //置空 优惠券分摊金额
            resetCouponDiscount();
        }
    }

    /**
     * 置空 优惠券分摊金额
     */
    public void resetCouponDiscount() {
        //置空 优惠券分摊金额
        if (mGoodsData != null) {
            int size = mGoodsData.size();
            for (int i = 0; i < size; i++) {
                mGoodsData.get(i).getData().setCoupon_discount(0);
            }
            mGoodsData = null;
        }
    }

    public CouponResponse getCouponInfo() {
        return mCouponResponse;
    }

    public void setAllCoupons(List<CouponResponse> list) {
        this.mAllCoupons = list;
    }

    /**
     * 是否在有效期内
     *
     * @return
     */
    private boolean isInEffectTime(CouponResponse result) {
        long effected_at = result.getEffected_at();
        long expired_at = result.getExpired_at();

        long currentTimeMillis = System.currentTimeMillis();

        return currentTimeMillis >= effected_at && currentTimeMillis <= expired_at;
    }

    public float getCouponMoney(float price) {

        coupon_discount = 0f;
        if (mCouponResponse == null || !isInEffectTime(mCouponResponse)) {
            return 0f;
        }

        float condition_value = Float.valueOf(mCouponResponse.getCondition_value());
        if (price < condition_value) {
            Log.i(TAG, "getCouponMoney: 不满足优惠券触发金额");
            return 0f;
        }
        int offer_type = mCouponResponse.getOffer_type();
        float offer_value = mCouponResponse.getOffer_value();


        if (offer_type == 1) {
            coupon_discount = offer_value;
        } else if (offer_type == 2) {
            coupon_discount = price * (offer_type / 100f);
        }

        return coupon_discount;

    }

    /**
     * 优惠券是否可用
     */
    public boolean isCouponEnable(CouponResponse couponResponse, List<GoodsEntity<GoodsResponse>> data,
                                  String shop_sn, float price) {

        coupon_discount = 0f;
        if (couponResponse == null) {
            Log.i(TAG, "isCouponEnable: couponResponse = null");
            return false;
        } else if (data == null || data.size() == 0) {
            Log.i(TAG, "isCouponEnable: data = null || size = 0");
            return false;
        } else if (!isInEffectTime(couponResponse)) {
            return false;
        }

        //优惠券使用条件金额
        float condition_value = Float.valueOf(couponResponse.getCondition_value());

        //判断是针对商品的还是针对店铺的
        boolean isForShop = couponResponse.isForShop();
        boolean isForGoods = couponResponse.isForGood();
        boolean isForAll = couponResponse.isForAll();
        PromotionGoods promotionGoods;
        if (isForAll) {
            if (price < condition_value) {
                Log.i(TAG, "getCouponMoney: 全店通用  不满足优惠券触发金额");
                return false;
            }

        } else if (isForGoods) {
            promotionGoods = findGoods(couponResponse, data);

            if (promotionGoods.getTotal_money() < condition_value) {
                Log.i(TAG, "getCouponMoney: 针对商品  不满足优惠券触发金额");
                return false;
            }
        } else if (isForShop) {
            if (TextUtils.isEmpty(shop_sn)) {
                Log.i(TAG, "getCouponMoney: shop_sn - " + shop_sn);
                return false;
            }
            boolean can_use = false;
            List<CouponResponse.ShopListBean> shop_list = couponResponse.getShop_list();
            int size = shop_list.size();
            for (int i = 0; i < size; i++) {
                CouponResponse.ShopListBean shopListBean = shop_list.get(i);
                if (shop_sn.equals(shopListBean.getShop_sn())) {
                    can_use = true;
                }
            }

            if (!can_use) {
                Log.i(TAG, "getCouponMoney: 针对店铺  当前店铺不满足");
                return false;
            }

            if (price < condition_value) {
                Log.i(TAG, "getCouponMoney: 针对店铺  不满足优惠券触发金额");
                return false;
            }
        }
        return true;
    }

    private PromotionGoods findGoods(CouponResponse couponResponse, List<GoodsEntity<GoodsResponse>> data) {
        if (data == null) {
            return new PromotionGoods();
        }
        List<CouponResponse.GSkuListBean> gsku_list = couponResponse.getGsku_list();
        int size = gsku_list.size();
        PromotionGoods promotionGoods = new PromotionGoods();

        int goods_size = data.size();
        //找到优惠券 需要的商品信息
        String barcode;
        GoodsEntity<GoodsResponse> goodsEntity;
        GoodsResponse goodsResponse;
        for (int i = 0; i < size; i++) {
            CouponResponse.GSkuListBean gSkuListBean = gsku_list.get(i);
            barcode = gSkuListBean.getBarcode();
            for (int j = 0; j < goods_size; j++) {
                goodsEntity = data.get(j);
                goodsResponse = goodsEntity.getData();

                if (barcode.equals(goodsResponse.getBarcode())) {
                    boolean weight = goodsEntity.getItemType() == GoodsEntity.TYPE_WEIGHT
                            || goodsEntity.getItemType() == GoodsEntity.TYPE_PROCESSING;

                    //设置数量、小计、位置等信息
                    PromotionGoods.GoodsBean good = new PromotionGoods.GoodsBean();
                    good.setIndex(j);//设置在data数据源中的位置
                    good.setBarcode(barcode);
                    good.setQuanlity(weight ? 1 : (int) goodsEntity.getCount());
                    good.setCount(goodsEntity.getCount());
                    good.setPrice(Float.valueOf(goodsEntity.getData().getPrice()));
                    good.setSubtotal(goodsEntity.getCount() * Float.valueOf(goodsEntity.getData().getPrice()));
                    promotionGoods.getGoodsBeans().add(good);
                }
            }

        }

        //计算优惠券相关商品 总数和总额
        if (promotionGoods.getGoodsBeans().size() != 0) {
            List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
            int goodsBeans_size = goodsBeans.size();
            int total_count = 0;
            float total_subtotal = 0;

            for (int i = 0; i < goodsBeans_size; i++) {
                PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
                total_count += goodsBean.getQuanlity();
                total_subtotal += goodsBean.getSubtotal();
            }
            promotionGoods.setTotal_count(total_count);
            promotionGoods.setTotal_money(total_subtotal);
        }
        return promotionGoods;
    }


    public static final int DONT_MEET = 0;

    public float getCouponMoney(List<GoodsEntity<GoodsResponse>> data, String shop_sn, float price) {

        coupon_discount = 0f;
        if (mCouponResponse == null) {
            Log.i(TAG, "getCouponMoney: mCouponResponse = null");
            return DONT_MEET;
        } else if (data == null || data.size() == 0) {
            Log.i(TAG, "getCouponMoney: data = null || size = 0");
            return DONT_MEET;
        } else if (!isInEffectTime(mCouponResponse)) {
            return DONT_MEET;
        }

        //优惠券使用条件金额
        float condition_value = Float.valueOf(mCouponResponse.getCondition_value());

        mGoodsData = data;

        //重置
        int goods_size = mGoodsData.size();
        for (int i = 0; i < goods_size; i++) {
            mGoodsData.get(i).getData().setCoupon_discount(0);
        }


        boolean goods_need_compute = false;
        boolean shop_need_compute = false;
        boolean all_need_compute = false;
        //判断是针对商品的还是针对店铺的
        boolean isForShop = mCouponResponse.isForShop();
        boolean isForGoods = mCouponResponse.isForGood();
        boolean isForAll = mCouponResponse.isForAll();
        PromotionGoods promotionGoods = null;
        if (isForAll) {
            if (price < condition_value) {
                Log.i(TAG, "getCouponMoney: 全店通用  不满足优惠券触发金额");
                return DONT_MEET;
            }

            all_need_compute = true;
        } else if (isForGoods) {
            promotionGoods = findGoods();

            if (promotionGoods.getTotal_money() < condition_value) {
                Log.i(TAG, "getCouponMoney: 针对商品  不满足优惠券触发金额");
                return DONT_MEET;
            }
            goods_need_compute = true;


        } else if (isForShop) {
            if (TextUtils.isEmpty(shop_sn)) {
                Log.i(TAG, "getCouponMoney: shop_sn - " + shop_sn);
                return DONT_MEET;
            }
            boolean can_use = false;
            List<CouponResponse.ShopListBean> shop_list = mCouponResponse.getShop_list();
            int size = shop_list.size();
            for (int i = 0; i < size; i++) {
                CouponResponse.ShopListBean shopListBean = shop_list.get(i);
                if (shop_sn.equals(shopListBean.getShop_sn())) {
                    can_use = true;
                }
            }

            if (!can_use) {
                Log.i(TAG, "getCouponMoney: 针对店铺  当前店铺不满足");
                return DONT_MEET;
            }

            if (price < condition_value) {
                Log.i(TAG, "getCouponMoney: 针对店铺  不满足优惠券触发金额");
                return DONT_MEET;
            }
            shop_need_compute = true;
        }

        if (goods_need_compute || shop_need_compute || all_need_compute) {
            int offer_type = mCouponResponse.getOffer_type();
            float offer_value = mCouponResponse.getOffer_value();


            if (offer_type == 1) {//减免
                coupon_discount = offer_value;
            } else if (offer_type == 2) {//折扣
                if (goods_need_compute) {//针对商品
                    coupon_discount = promotionGoods.getTotal_money() * (offer_value / 100f);
                } else {//针对店铺 或者所有
                    coupon_discount = price * (offer_value / 100f);
                }
            }

            GoodsEntity<GoodsResponse> goodsEntity;
            GoodsResponse good;
            float sum_coupon_discount = 0f;
            float coupon_money = 0f;

            //分摊优惠金额
            if (goods_need_compute) {
                List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
                int size = goodsBeans.size();
                for (int i = 0; i < size; i++) {
                    PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
                    int index = goodsBean.getIndex();
                    good = mGoodsData.get(index).getData();
                    coupon_money = BigDecimalUtils.round(
                            coupon_discount * (goodsBean.getSubtotal() / promotionGoods.getTotal_money()))
                            .floatValue();
                    if (i == size - 1) {
                        coupon_money = coupon_discount - sum_coupon_discount;
                    } else {
                        sum_coupon_discount += coupon_money;
                    }
                    good.setCoupon_discount(coupon_money);

                }
            } else if (shop_need_compute || all_need_compute) {
                float subtotal;
                for (int i = 0; i < goods_size; i++) {
                    goodsEntity = mGoodsData.get(i);
                    good = goodsEntity.getData();
                    subtotal = Float.parseFloat(good.getPrice()) * goodsEntity.getCount()
                            - Float.parseFloat(good.getDiscount_price());
                    coupon_money = BigDecimalUtils.round(coupon_discount * (subtotal / price)).floatValue();
                    if (i == goods_size - 1) {
                        coupon_money = coupon_discount - sum_coupon_discount;
                    } else {
                        sum_coupon_discount += coupon_money;
                    }
                    good.setCoupon_discount(coupon_money);
                }
            }

        }

        return coupon_discount;

    }

    /**
     * 查找 针对商品的优惠券中 满足的商品信息
     *
     * @return
     */
    @NonNull
    private PromotionGoods findGoods() {
        List<CouponResponse.GSkuListBean> gsku_list = mCouponResponse.getGsku_list();
        int size = gsku_list.size();
        PromotionGoods promotionGoods = new PromotionGoods();

        int goods_size = mGoodsData.size();
        //找到优惠券 需要的商品信息
        String barcode;
        GoodsEntity<GoodsResponse> goodsEntity;
        GoodsResponse goodsResponse;
        for (int i = 0; i < size; i++) {
            CouponResponse.GSkuListBean gSkuListBean = gsku_list.get(i);
            barcode = gSkuListBean.getBarcode();
            for (int j = 0; j < goods_size; j++) {
                goodsEntity = mGoodsData.get(j);
                goodsResponse = goodsEntity.getData();

                if (barcode.equals(goodsResponse.getBarcode())) {
                    boolean weight = goodsEntity.getItemType() == GoodsEntity.TYPE_WEIGHT
                            || goodsEntity.getItemType() == GoodsEntity.TYPE_PROCESSING;

                    //设置数量、小计、位置等信息
                    PromotionGoods.GoodsBean good = new PromotionGoods.GoodsBean();
                    good.setIndex(j);//设置在data数据源中的位置
                    good.setBarcode(barcode);
                    good.setQuanlity(weight ? 1 : (int) goodsEntity.getCount());
                    good.setCount(goodsEntity.getCount());
                    good.setPrice(Float.valueOf(goodsEntity.getData().getPrice()));
                    good.setSubtotal(goodsEntity.getCount() * Float.valueOf(goodsEntity.getData().getPrice()));
                    promotionGoods.getGoodsBeans().add(good);
                }
            }

        }

        //计算优惠券相关商品 总数和总额
        if (promotionGoods.getGoodsBeans().size() != 0) {
            List<PromotionGoods.GoodsBean> goodsBeans = promotionGoods.getGoodsBeans();
            int goodsBeans_size = goodsBeans.size();
            int total_count = 0;
            float total_subtotal = 0;

            for (int i = 0; i < goodsBeans_size; i++) {
                PromotionGoods.GoodsBean goodsBean = goodsBeans.get(i);
                total_count += goodsBean.getQuanlity();
                total_subtotal += goodsBean.getSubtotal();
            }
            promotionGoods.setTotal_count(total_count);
            promotionGoods.setTotal_money(total_subtotal);
        }
        return promotionGoods;
    }

    /* 获取最佳优惠券算法 */
    // 计算方式：
    // 1.得到当前总价格
    // 2.遍历优惠券列表
    // 3.判断是否有满足于使用价格，如果满足，则通过减免类型计算最终优惠总价并计入集合中
    // 4.给集合中coupons字段进行排序，取优惠总价最少（优惠最多）的值作为最佳优惠券并取到集合中的coupons优惠券，赋值给静态变量coupon进行使用
    public void getBestCoupon(List<GoodsEntity<GoodsResponse>> goodsEntity, float amount) {
        if (goodsEntity == null) {
            Log.i(TAG, "goodsEntity == null");
            return;
        }
        if (mAllCoupons == null) {
            Log.i(TAG, "mAllCoupons == null");
            return;
        }
        mCouponResponse = null;
        mGoodsData = goodsEntity;
        if (mAllCoupons.size() > 0) {
            List<Counpons> counponList = new ArrayList<>();
            for (CouponResponse item : mAllCoupons) {
                float coupon_total = Float.parseFloat(item.getCondition_value());
                Counpons counpons = new Counpons();
                if (amount >= coupon_total) {
                    boolean goods = true;
                    boolean shops = true;
                    if (item.getGsku_list() != null) {
                        if (item.getGsku_list().size() > 0) {
                            for (CouponResponse.GSkuListBean sku_list : item.getGsku_list()) {
                                for (GoodsEntity<GoodsResponse> entity : mGoodsData) {
                                    goods = false;
                                    float discount = (entity.getCount() * Float.parseFloat(entity.getData().getPrice()))
                                            - Float.parseFloat(entity.getData().getDiscount_price());
                                    if (sku_list.getBarcode().equals(entity.getData().getBarcode())
                                            && sku_list.getG_sku_name().equals(entity.getData().getG_sku_name())
                                            && discount >= coupon_total) {
                                        goods = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (item.getShop_list().size() > 0) {
                        for (CouponResponse.ShopListBean shop_list : item.getShop_list()) {
                            shops = false;
                            if (String.valueOf(shop_list.getShop_id()).equals(Configs.shop_id + "")) {
                                shops = true;
                                break;
                            }
                        }
                    }
                    if (goods && shops) {
                        if (!isInEffectTime(item)) {
                            continue;
                        }
                        if (item.getOffer_type() == 1) {     //减免
                            counpons.setCoupons(amount - item.getOffer_value());
                            counpons.setBean(item);
                            counponList.add(counpons);
                        } else if (item.getOffer_type() == 2) {   //折扣
                            counpons.setCoupons(amount * ((100 - item.getOffer_value()) / 100f));
                            counpons.setBean(item);
                            counponList.add(counpons);
                        }
                    }
                }
            }
            Comparator<Counpons> comparable = new Comparator<Counpons>() {
                @Override
                public int compare(Counpons o1, Counpons o2) {
                    int cr = 0;
                    float a = o1.getCoupons() - o2.getCoupons();
                    if (a != 0) {
                        cr = (a > 0) ? 2 : -1;
                    } else {
                        long b = o1.getBean().getExpired_at() - o2.getBean().getExpired_at();
                        if (b != 0) {
                            cr = (b > 0) ? 1 : -2;
                        }
                    }
                    return cr;
                }
            };
            if (counponList.size() > 0) {
                if (counponList.size() > 1) {
                    Log.i(TAG, "优惠券列表 --- 未排序之前数据： " + GsonUtils.getInstance().getGson().toJson(counponList));
                    Collections.sort(counponList, comparable);
                    Log.i(TAG, "优惠券列表 --- 排序之后的数据： " + GsonUtils.getInstance().getGson().toJson(counponList));
                    mCouponResponse = counponList.get(0).getBean();
                } else {
                    mCouponResponse = counponList.get(0).getBean();
                }
            }
        }
        if (mCouponResponse == null) {
            Log.i(CouponUtils.class.getSimpleName(), "暂时没有最佳优惠券可使用");
        }
    }


    public float getCoupon_discount() {
        return coupon_discount;
    }


    public void reset() {
        setCouponInfo(null);
        coupon_discount = 0;
    }
}

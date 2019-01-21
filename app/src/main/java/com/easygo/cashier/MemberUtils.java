package com.easygo.cashier;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.MemberDayInfo;
import com.easygo.cashier.bean.MemberDiscountInfo;
import com.easygo.cashier.bean.MemberInfo;
import com.niubility.library.utils.ToastUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @Describe：会员系统Utils
 * @Date：2019-01-10
 *
 * 18027347946
 */
public class MemberUtils {
    private static final String TAG = MemberUtils.class.getSimpleName();
    /*
     * 会员系统 if为会员 item会员价显示，显示优惠价，  商品会员优先级： 会员价 > 会员日 > 会员折扣
     */

    /* 是否为会员 */
    public static boolean isMember = false;
    /* 是否有会员日 */
    public static boolean isMemberDay = false;
    /* 是否有会员折扣 */
    public static boolean isMemberDiscount = false;
    /* 会员固定折扣价 */
    public static double discount = 0;

    public static int full_type = 0;
    /* 会员日 满 */
    public static float full = 0;
    /* 会员日 满折扣 */
    public static double full_discount = 0;
    /* 会员日 满减 */
    public static float full_reduction = 0;

    /* 会员信息 */
    public static MemberInfo memberInfo;
    /* 会员日信息*/
    public static MemberDayInfo sMemberDayInfo;
    /* 会员固定折扣信息*/
    public static MemberDiscountInfo sMemberDiscountInfo;
    /* 会员日 活动id*/
    public static int day_rc_id;
    /* 会员固定折扣 活动id*/
    public static int discount_rc_id;

    /**正在参与会员活动*/
    public static List<String> currentNames = new ArrayList<>();

    //查看当天是否是会员日 得到type == 1 ? 周 : 月
    public static boolean isDateMemberDay(MemberDayInfo memberDayInfo) {
        if (memberDayInfo != null) {
            int day = 0;
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            if (memberDayInfo.getType() == 1) {
                Log.i(TAG, "当前为会员日状态为周");
                int week = c.get(Calendar.DAY_OF_WEEK);
                if (week == 1) {
                    day = 7;
                } else {
                    day = week - 1;
                }
            } else if (memberDayInfo.getType() == 2) {
                Log.i(TAG, "当前为会员日状态为月");
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String format = sdf.format(date);
                day = Integer.parseInt(format.substring(format.lastIndexOf("-") + 1, format.length()));
            }
            for (MemberDayInfo.ItemsBean itemsBean : memberDayInfo.getItems()) {
                if (day == itemsBean.getDay()) {
                    Log.i(TAG, memberDayInfo.getType() == 1 ? "有会员活动：星期" + day : "有会员活动：" + day + "天");
                    full = itemsBean.getLeast_cost();
                    full_type = itemsBean.getDiscount_type();
                    if (itemsBean.getDiscount_type() == 1) {
                        full_discount = (100 - itemsBean.getDiscount_amount()) * 0.01;
                    } else {
                        full_reduction = itemsBean.getDiscount_amount();
                    }
                    day_rc_id = itemsBean.getRc_id();
                    return true;
                }
            }
            Log.i(TAG, memberDayInfo.getType() == 1 ? "当前不是会员周" : "当前不是会员月");
        }
        return false;
    }

    public static Float getCoupon(float totalPrice, float price, int count) {
        float coupon = 0;
        if (full_type == 1) { //满折扣
            coupon = (float) (((price * count) / totalPrice) * (totalPrice - (totalPrice * full_discount)));
        } else if (full_type == 2) { //满减
            coupon = ((price * count) / totalPrice) * full_reduction;
        }
        return coupon;
    }

    /**
     * 计算各个商品的会员价或者优惠
     */
    public static void computeMember(List<GoodsEntity<GoodsResponse>> data) {
        int size = data.size();

        DecimalFormat df = new DecimalFormat("0.00");

        float fullTotalPrice = getFullTotalPrice(data);
        currentNames.clear();

        for (int i = 0; i < size; i++) {
            GoodsEntity<GoodsResponse> goodsEntity = data.get(i);
            if (goodsEntity.getPromotion() != null) {//此商品已有商品促销
                return;
            } else {
                GoodsResponse good = goodsEntity.getData();
                int good_count = goodsEntity.getCount();
                float price = 0;

                if(goodsEntity.getItemType() == GoodsEntity.TYPE_ONLY_PROCESSING) {
                    price = Float.valueOf(good.getProcess_price());
                } else {
                    price = Float.valueOf(good.getPrice());
                }

                float membership_price = Float.valueOf(good.getMembership_price());

                //获取每种商品的优惠价格
                float coupon = getCoupon(good, good_count, fullTotalPrice, price, membership_price);
                good.setDiscount_price(df.format(coupon));

                if(goodsEntity.getItemType() == GoodsEntity.TYPE_PROCESSING) {
                    GoodsResponse processing = goodsEntity.getProcessing();

                    if(processing != null) {
                        price = Float.valueOf(processing.getProcess_price());

                        membership_price = Float.valueOf(processing.getMembership_price());
                        coupon = getCoupon(processing, good_count, fullTotalPrice, price, membership_price);
                        processing.setDiscount_price(df.format(coupon));
                    }
                }
            }
        }
    }

    /**
     * 根据价格 计算各商品优惠
     * @param good
     * @param good_count
     * @param fullTotalPrice
     * @param price
     * @param membership_price
     * @return
     */
    public static float getCoupon(GoodsResponse good, int good_count, float fullTotalPrice, float price, float membership_price) {

        float coupon = 0f;
        if(good.isMemberPrice()) {//有会员价
            coupon = (price - membership_price) * good_count;
            if(!currentNames.contains("会员价")) {
                currentNames.add("会员价");
            }
            Log.i(TAG, "getCoupon: 会员价 优惠 -> " + coupon);
        } else if (!good.isMemberPrice() && MemberUtils.isMemberDay) {           //会员日计算
            if (fullTotalPrice >= MemberUtils.full){
                coupon = MemberUtils.getCoupon(fullTotalPrice, price, good_count);
                if(!currentNames.contains("会员日")) {
                    currentNames.add("会员日");
                }
                Log.i(TAG, "getCoupon: 会员日 优惠 -> " + coupon);
            } else if(MemberUtils.isMemberDiscount){
                coupon = (float) (price - (MemberUtils.discount * price)) * good_count;
                if(!currentNames.contains("会员固定折扣")) {
                    currentNames.add("会员固定折扣");
                }
                Log.i(TAG, "getCoupon: 会员固定折扣 优惠 -> " + coupon);
            }
        } else if (!good.isMemberPrice() && !MemberUtils.isMemberDay && MemberUtils.isMemberDiscount) {   //会员固定折扣计算
            coupon = (float) (price - (MemberUtils.discount * price)) * good_count;
            if(!currentNames.contains("会员固定折扣")) {
                currentNames.add("会员固定折扣");
            }
            Log.i(TAG, "getCoupon: 会员固定折扣 （2）  优惠 -> " + coupon);
        }
        return coupon;
    }


    public static Float getFullTotalPrice(List<GoodsEntity<GoodsResponse>> data){
        float total_price = 0;
        for (GoodsEntity<GoodsResponse> entity : data) {
            GoodsResponse good = entity.getData();
            if (entity.getPromotion() == null && !good.isMemberPrice()) {
                if (entity.getItemType() == GoodsEntity.TYPE_ONLY_PROCESSING) {
                    total_price += Float.parseFloat(good.getProcess_price()) * entity.getCount();
                } else if(entity.getItemType() == GoodsEntity.TYPE_PROCESSING){
                    total_price += Float.parseFloat(good.getPrice()) * entity.getCount();

                    GoodsResponse processing = entity.getProcessing();
                    if (processing != null) {//此时 选择了加工
                        total_price += Double.valueOf(processing.getProcess_price());
                    }
                } else {
                    total_price += Float.parseFloat(good.getPrice()) * entity.getCount();
                }
            }
        }
        return total_price;
    }


    public static void reset() {
        isMember = false;
        isMemberDay = false;
        isMemberDiscount = false;
    }




}

/* 满减折扣价 */
// 满10块钱 2
// 10  price / member_day_totalPrice * discount

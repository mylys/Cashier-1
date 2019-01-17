package com.easygo.cashier;

import android.util.Log;

import com.easygo.cashier.bean.MemberDayInfo;
import com.easygo.cashier.bean.MemberDiscountInfo;
import com.easygo.cashier.bean.MemberInfo;
import com.niubility.library.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @Describe：会员系统Utils
 * @Date：2019-01-10
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
}

/* 满减折扣价 */
// 满10块钱 2
// 10  price / member_day_totalPrice * discount

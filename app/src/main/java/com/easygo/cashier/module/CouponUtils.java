package com.easygo.cashier.module;

import android.util.Log;

import com.easygo.cashier.bean.CouponResponse;

public class CouponUtils {

    private static final String TAG = CouponUtils.class.getSimpleName();

    private CouponUtils() {}

    public static CouponUtils getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static final CouponUtils sInstance = new CouponUtils();
    }

    private CouponResponse mCouponResponse;
    private float coupon_discount;


    public void setCouponInfo(CouponResponse result) {
        mCouponResponse = result;

        if(result == null) {
            coupon_discount = 0f;
        }
    }
    public CouponResponse getCouponInfo() {
        return mCouponResponse;
    }

    /**
     * 是否在有效期内
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
        if(mCouponResponse == null || !isInEffectTime(mCouponResponse)) {
            return 0f;
        }

        float condition_value = Float.valueOf(mCouponResponse.getCondition_value());
        if(price < condition_value) {
            Log.i(TAG, "getCouponMoney: 不满足优惠券触发金额");
            return 0f;
        }
        int offer_type = mCouponResponse.getOffer_type();
        float offer_value = mCouponResponse.getOffer_value();


        if(offer_type == 1) {
            coupon_discount = offer_value;
        } else if(offer_type == 2) {
            coupon_discount = price * (offer_type / 100f);
        }

        return coupon_discount;

    }

    public boolean canUse(CouponResponse result, float price) {
        if(result == null || !isInEffectTime(result)) {
            return false;
        }

        float condition_value = Float.valueOf(result.getCondition_value());
        if(price < condition_value) {
            Log.i(TAG, "getCouponMoney: 不满足优惠券触发金额");
            return false;
        }
        return true;
    }

    public float getCoupon_discount() {
        return coupon_discount;
    }
}

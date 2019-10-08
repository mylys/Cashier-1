package com.easygo.cashier.utils;

import com.easygo.cashier.bean.GiftCardResponse;

/**
 * 礼品卡工具类
 */
public class GiftCardUtils {

    private final String TAG = getClass().getSimpleName();

    private GiftCardUtils() {}

    public static GiftCardUtils getInstance() {
        return Holder.sInstance;
    }

    private static class Holder {
        private static final GiftCardUtils sInstance = new GiftCardUtils();
    }

    private GiftCardResponse mGiftCardResponse;


    public void setGiftCardInfo(GiftCardResponse result) {
        mGiftCardResponse = result;
    }

    public GiftCardResponse getGiftCardInfo() {
        return mGiftCardResponse;
    }

    public void reset() {
        setGiftCardInfo(null);
    }
}

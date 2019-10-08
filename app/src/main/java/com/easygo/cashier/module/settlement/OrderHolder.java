package com.easygo.cashier.module.settlement;

import android.util.Log;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.utils.CouponUtils;
import com.easygo.cashier.utils.GiftCardUtils;
import com.easygo.cashier.utils.MemberUtils;

import java.util.List;

public class OrderHolder {

    public static final String TAG = OrderHolder.class.getSimpleName();

    public static List<GoodsEntity<GoodsResponse>> goodsData;
    public static int goods_count;
    public static float total_money;
    public static float discount;
    public static String coupon_sn;
    public static float coupon_money;
    public static float temp_order_money;
    public static String gift_card_no;
    public static float gift_card_money;
    public static String buyer;
    public static int member_id;
    public static String member_card_no;
    public static float change;
    public static int order_type;

    public static boolean needCreateOrder(List<GoodsEntity<GoodsResponse>> data, int goodsCount, float totalMoney,
                                          float discountMoney, float couponMoney, float giftCardMoney,
                                          float tempOrderMoney, float changeMoney, int orderType) {
        if(goodsData == null || data == null) {
            Log.i(TAG, "needCreateOrder: goodsData == null || data == null");
            return true;
        }
        Log.i(TAG, "needCreateOrder: 判断基本条件");

        //判断基本条件
        if(goods_count != goodsCount || total_money != totalMoney || discount != discountMoney
                || coupon_money != couponMoney || gift_card_money != giftCardMoney
                || temp_order_money != tempOrderMoney || change != changeMoney || order_type != orderType) {
            return true;
        }
        Log.i(TAG, "needCreateOrder: 基本条件相同");

        //判断礼品卡、会员、优惠券等
        if(GiftCardUtils.getInstance().getGiftCardInfo() != null) {
            if(!GiftCardUtils.getInstance().getGiftCardInfo().getSn().equals(gift_card_no)) {
                return true;
            }
        }
        Log.i(TAG, "needCreateOrder: 礼品卡信息相同");
        if(MemberUtils.isMember && MemberUtils.memberInfo != null) {
            if(member_id != MemberUtils.memberInfo.getMember_id()) {
                return true;
            } else if(!MemberUtils.memberInfo.getNick_name().equals(buyer)) {
                return true;
            } else if(!MemberUtils.memberInfo.getCard_no().equals(member_card_no)) {
                return true;
            }
        }
        Log.i(TAG, "needCreateOrder: 会员信息相同");
        if(CouponUtils.getInstance().getCouponInfo() != null) {
            if(!CouponUtils.getInstance().getCouponInfo().getCoupon_sn().equals(coupon_sn)) {
                return true;
            }
        }
        Log.i(TAG, "needCreateOrder: 优惠券信息相同");

        //判断商品数量是否相同
        int size = goodsData.size();
        if(size != data.size()) {
            return true;
        }
        Log.i(TAG, "needCreateOrder: 商品数量相同");

        //判断商品数据是否相同
        for (int i = 0; i < size; i++) {
            if(!goodsData.get(i).equals(data.get(i))) {
                return true;
            }
        }
        Log.i(TAG, "needCreateOrder: 全部商品数据相同");
        Log.i(TAG, "needCreateOrder: 不需要重复创建！！！！");

        //商品数据都相同, 不需要重复创建订单
        return false;
    }

    public static void cache(List<GoodsEntity<GoodsResponse>> data, int goodsCount, float totalMoney,
                             float discountMoney, float couponMoney, float giftCardMoney, float tempOrderMoney,
                             float changeMoney, int orderType) {
        goodsData = data;
        goods_count = goodsCount;
        total_money = totalMoney;
        discount = discountMoney;
        coupon_money = couponMoney;
        gift_card_money = giftCardMoney;
        temp_order_money = tempOrderMoney;
        change = changeMoney;
        order_type = orderType;

        //礼品卡
        if(GiftCardUtils.getInstance().getGiftCardInfo() != null) {
            gift_card_no = GiftCardUtils.getInstance().getGiftCardInfo().getSn();
        } else {
            gift_card_no = null;
        }

        //会员
        if(MemberUtils.isMember && MemberUtils.memberInfo != null) {
            buyer = MemberUtils.memberInfo.getNick_name();
            member_id = MemberUtils.memberInfo.getMember_id();
            member_card_no = MemberUtils.memberInfo.getCard_no();
        } else {
            buyer = null;
            member_id = -1;
            member_card_no = null;
        }

        //优惠券
        if(CouponUtils.getInstance().getCouponInfo() != null) {
            coupon_sn = CouponUtils.getInstance().getCouponInfo().getCoupon_sn();
        } else {
            coupon_sn = null;
        }


    }

    public static void reset() {
        goodsData = null;

    }


    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

package com.easygo.cashier.module.settlement;

import com.easygo.cashier.bean.BankcardStatusResponse;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.CreateOderResponse;
import com.easygo.cashier.bean.GiftCardResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

public class SettlementContract {

    public interface IPresenter extends BaseContract.Presenter {
        void createOrder(String json);

        void wechatPay(String shop_sn, String order_sn, int total_fee, String auth_code);

        void checkWechatPayStatus(String shop_sn, String order_no);

        void aliPay(String shop_sn, String order_sn, int total_fee, String auth_code);

        void checkAliPayStatus(String shop_sn, String order_no);

        void cash(String shop_sn, String order_no, int buyer_pay, int change_money);

        void checkBankcardStatus(String order_no);

        void giftCard(String card_no);
        void giftCardPay(String order_no);

        void print(String json);

        void print_info(String shop_sn, String printer_sn, String info);

        void get_coupon(String coupon_sn);

        void memberWalletPay(String order_sn, String auth_code);

        void closeOrder(String order_no);
    }

    public interface IView extends BaseView {
        void createOrderSuccess(CreateOderResponse result);
        void createOrderFailed(Map<String, Object> map);

        void aliPaySuccess(String result);
        void aliPayFailed(Map<String, Object> map);
        void checkAlipayStatusSuccess(String result);
        void checkAlipayStatusFailed(Map<String, Object> map);

        void wechatPaySuccess(String result);
        void wechatPayFailed(Map<String, Object> map);
        void checkWechatStatusSuccess(String result);
        void checkWechatStatusFailed(Map<String, Object> map);


        void cashSuccess(String result);
        void cashFailed(Map<String, Object> map);

        void bankcardSuccess(BankcardStatusResponse result);
        void bankcardFailed(Map<String, Object> map);

        void giftCardSuccess(GiftCardResponse result);
        void giftCardFailed(Map<String, Object> map);

        void giftCardPaySuccess(String result);
        void giftCardPayFailed(Map<String, Object> map);

        void printSuccess(String result);
        void printFailed(Map<String, Object> map);

        void couponSuccess(CouponResponse result);
        void couponFailed(Map<String, Object> map);

        void memberWalletSuccess(String result);
        void memberWalletFailed(Map<String, Object> map);

        void unlockCouponSuccess();
        void unlockCouponFailed(Map<String, Object> map);
    }
}

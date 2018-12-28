package com.easygo.cashier.module.settlement;

import com.easygo.cashier.bean.CreateOderResponse;
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

        void print(String json);
        void print_info(String shop_sn, String printer_sn, String info);
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

        void printSuccess(String result);
        void printFailed(Map<String, Object> map);
    }
}

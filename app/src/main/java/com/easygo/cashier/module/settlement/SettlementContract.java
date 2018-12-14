package com.easygo.cashier.module.settlement;

import com.easygo.cashier.bean.AlipayResponse;
import com.easygo.cashier.bean.CheckAlipayStatus;
import com.easygo.cashier.bean.CreateOderResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseModel;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

public class SettlementContract {

    public interface IModel extends BaseModel {
//        void wechatPay(String order_sn, RequestListener<PayResponse> listener);
//        void aliPay(String order_sn, RequestListener<PayResponse> listener);
//        void checkPayStatus(String order_sn, RequestListener<CheckPayStatusResponse> listener);
    }

    public interface IPresenter extends BaseContract.Presenter {
        void createOrder(String json);

        void wechatPay(String order_sn);
        void aliPay(String shop_sn, String order_sn, int total_fee, String auth_code);
        void checkAliPayStatus(String shop_sn, String order_sn);

        void checkPayStatus(String order_sn);

        void print(String json);
    }

    public interface IView extends BaseView {
        void createOrderSuccess(CreateOderResponse result);
        void createOrderFailed(Map<String, Object> map);

        void aliPaySuccess(String result);
        void aliPayFailed(Map<String, Object> map);

        void checkAlipayStatusSuccess(CheckAlipayStatus result);
        void checkAlipayStatusFailed(Map<String, Object> map);

        void wechatPaySuccess();
        void wechatPayFailed(Map<String, Object> map);

        void printSuccess();
        void printFailed(Map<String, Object> map);
    }
}

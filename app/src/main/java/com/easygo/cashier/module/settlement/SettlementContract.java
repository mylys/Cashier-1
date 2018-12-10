package com.easygo.cashier.module.settlement;

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
        void wechatPay(String order_sn);
        void aliPay(String order_sn);

        void checkPayStatus(String order_sn);
    }

    public interface IView extends BaseView {
        void aliPaySuccess();
        void aliPayFailed(Map<String, Object> map);

        void wechatPaySuccess();
        void wechatPayFailed(Map<String, Object> map);
    }
}

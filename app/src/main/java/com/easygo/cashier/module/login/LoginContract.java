package com.easygo.cashier.module.login;

import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

public class LoginContract {

    public interface IPresenter extends BaseContract.Presenter {
         void login(String mac_address, String account, String password);

         void resever_money(String session_id, String shop_sn, int handover_id, int resever_money);
         void pop_till(String shop_sn, String printer_sn);

         void heartbeat();
    }

    interface IView extends BaseView {

        String getAccount();
        String getPassword();
        void login();
        void save(InitResponse result);
        void reserveMoney(LoginResponse result);

        void setLoginButtonEnable(boolean buttonEnable);
        void loginSuccess(LoginResponse result);
        void loginFailed(Map<String, Object> map);

        void reseverMoneySuccess();
        void reseverMoneyFailed(Map<String, Object> map);

        void popTillSuccess();
        void popTillFailed(Map<String, Object> map);


    }
}

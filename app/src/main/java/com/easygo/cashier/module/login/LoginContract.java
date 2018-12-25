package com.easygo.cashier.module.login;

import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.niubility.library.http.base.HttpResult;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseView;

import java.util.Map;

public class LoginContract {

//    public interface IModel extends BaseModel {
//        void login(String account, String password, RequestListener<LoginResponse> listener);
//        void init(String mac_adr, RequestListener<InitResponse> listener);
//    }

    public interface IPresenter extends BaseContract.Presenter {
         void login(String shop_sn, String account, String password);
         void init(String mac_adr);


         void resever_money(String session_id, String shop_sn, int handover_id, int resever_money);
    }

    interface IView extends BaseView {
        void loginSuccess(LoginResponse result);
        void loginFailed(Map<String, Object> map);

        void initSuccess(InitResponse result);
        void initFailed(Map<String, Object> map);

        void reseverMoneySuccess();
        void reseverMoneyFailed(Map<String, Object> map);


    }
}

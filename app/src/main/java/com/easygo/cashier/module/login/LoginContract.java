package com.easygo.cashier.module.login;

import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.niubility.library.http.RequestListener;
import com.niubility.library.mvp.BaseContract;
import com.niubility.library.mvp.BaseModel;
import com.niubility.library.mvp.BaseRxView;

import java.util.Map;

public class LoginContract {

//    public interface IModel extends BaseModel {
//        void login(String account, String password, RequestListener<LoginResponse> listener);
//        void init(String mac_adr, RequestListener<InitResponse> listener);
//    }

    public interface IPresenter extends BaseContract.Presenter {
         void login(String account, String password);
         void init(String mac_adr);
    }

    public interface IView extends BaseRxView {
        void loginSuccess(LoginResponse result);
        void loginFailed(Map<String, Object> map);

        void initSuccess(InitResponse result);
        void initFailed(Map<String, Object> map);
    }
}

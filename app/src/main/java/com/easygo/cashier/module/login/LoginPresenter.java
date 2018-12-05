package com.easygo.cashier.module.login;

import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.Map;

public class LoginPresenter extends BasePresenter<LoginContract.IView> implements LoginContract.IPresenter{


    @Override
    public void login(String account, String password) {
        subscribeBindLifecycle(
                HttpAPI.getInstance().httpService().login(account, password),
                new BaseResultObserver<LoginResponse>() {
            @Override
            protected void onSuccess(LoginResponse result) {
                mView.loginSuccess(result);
            }

            @Override
            protected void onFailure(Map<String, Object> map) {
                mView.loginFailed(map);
            }
        });
    }

    @Override
    public void init(String mac_adr) {
        subscribeBindLifecycle(
//                HttpAPI.getInstance().httpService().init(mac_adr),
                HttpAPI.getInstance().httpService().getShopId(mac_adr),
                new BaseResultObserver<InitResponse>() {
                    @Override
                    protected void onSuccess(InitResponse result) {
                        mView.initSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.initFailed(map);
                    }
                });
    }
}

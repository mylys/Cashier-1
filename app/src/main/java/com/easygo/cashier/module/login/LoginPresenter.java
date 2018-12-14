package com.easygo.cashier.module.login;

import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.Map;

public class LoginPresenter extends BasePresenter<LoginContract.IView> implements LoginContract.IPresenter{


    @Override
    public void login(String shop_sn, String account, String password) {

        Map<String, String> header = HttpClient.getInstance().getLoginHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().login(header, shop_sn, account, password),
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
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().init(mac_adr),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mView.initSuccess(result);
                    }


                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.initFailed(map);
                    }
                });
    }
}

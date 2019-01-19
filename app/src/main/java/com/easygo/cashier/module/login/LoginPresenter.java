package com.easygo.cashier.module.login;

import android.content.SharedPreferences;
import android.util.Log;

import com.easygo.cashier.Configs;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.http.HttpAPI;
import com.easygo.cashier.printer.PrintHelper;
import com.niubility.library.base.BaseApplication;
import com.niubility.library.constants.Constans;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.base.HttpResult;
import com.niubility.library.http.rx.BaseErrorObserver;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;
import com.niubility.library.utils.GetSign;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginContract.IView> implements LoginContract.IPresenter{


    @Override
    public void login(String shop_sn, String account, String password) {

//        Map<String, String> header = HttpClient.getInstance().getLoginHeader();
        Map<String, String> header = HttpClient.getInstance().getLoginSignHeader();
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
        Map<String, String> header = HttpClient.getInstance().getLoginSignHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().init(header, mac_adr),
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

    @Override
    public void resever_money(String session_id, String shop_sn, int handover_id, int resever_money) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().reserve_money(header, shop_sn, handover_id, resever_money),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String string) {
                        mView.reseverMoneySuccess();
                    }


                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.reseverMoneyFailed(map);
                    }
                });
    }

    @Override
    public void pop_till(String shop_sn, String printer_sn) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        for (int i = 0; i < PrintHelper.printers_count; i++) {
            InitResponse.PrintersBean printersBean = PrintHelper.printersBeans.get(i);
            String device_sn = printersBean.getDevice_sn();

            if(!printersBean.canUse(InitResponse.PrintersBean.type_handover)) {
                return;
            }
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("shop_sn", shop_sn);
            requestMap.put("printer_sn", device_sn);
            requestMap.put("times", 1);
            requestMap.put("info",  PrintHelper.pop_till);

            subscribeAsyncToResult(
                    HttpAPI.getInstance().httpService().printer_info(header, requestMap),
                    new BaseResultObserver<String>() {
                        @Override
                        protected void onSuccess(String string) {
                            mView.popTillSuccess();
                        }

                        @Override
                        protected void onFailure(Map<String, Object> map) {
                            mView.popTillFailed(map);
                        }
                    });
        }
    }

    @Override
    public void heartbeat() {
        Observable<Object> retry = Observable.interval(Configs.interval, TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<HttpResult<String>> apply(Long aLong) throws Exception {
                        Map<String, String> header = HttpClient.getInstance().getHeader();
                        return HttpAPI.getInstance().httpService().heartbeat(header);
                    }
                })
                .subscribeOn(Schedulers.io())
                .retry();  // retry保证请求失败后能重新订阅

        BaseErrorObserver<Object> observer = new BaseErrorObserver<Object>() {
            @Override
            public void onNext(Object s) {
                Log.i("heartbeat", "onNext: 心跳--------");
            }

            @Override
            protected void onFailure(Map<String, Object> map) {

            }
        };
        retry.subscribe(observer);
        mCompositeDisposable.add(observer);
    }
}

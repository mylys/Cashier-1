package com.easygo.cashier.module.login;

import android.util.Log;

import com.easygo.cashier.Configs;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.http.HttpAPI;
import com.easygo.cashier.printer.PrintHelper;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.base.HttpResult;
import com.niubility.library.http.rx.BaseErrorObserver;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.http.rx.Threadscheduler;
import com.niubility.library.http.rx.TransformToResult;
import com.niubility.library.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginContract.IView> implements LoginContract.IPresenter{


    @Override
    public void login(final String mac_adr, final String account, final String password) {
        final Map<String, String> header = HttpClient.getInstance().getLoginSignHeader();

        BaseResultObserver<LoginResponse> observer = new BaseResultObserver<LoginResponse>(){

            @Override
            protected void onSuccess(LoginResponse result) {
                mView.setLoginButtonEnable(true);
                mView.hideLoading();
                mView.loginSuccess(result);
            }

            @Override
            protected void onFailure(Map<String, Object> map) {
                mView.setLoginButtonEnable(true);
                mView.hideLoading();
                mView.loginFailed(map);
            }
        };
        mView.showLoading();
        HttpAPI.getInstance().httpService().init(header, "")
                .compose(new Threadscheduler<HttpResult<InitResponse>>())
                .map(new TransformToResult<InitResponse>())
                .filter(new Predicate<InitResponse>() {
                            @Override
                            public boolean test(InitResponse initResponse) throws Exception {
                                //判断是否禁止登录
                                boolean disable = "yes".equals(initResponse.getIs_disabled());
                                if(disable) {
                                    mView.hideLoading();
                                    mView.showToast("设备已停用");
                                    return false;
                                } else {
                                    mView.setLoginButtonEnable(false);
                                    return true;
                                }
                            }
                        }
                )
                .observeOn(Schedulers.io())
                .flatMap(new Function<InitResponse, ObservableSource<HttpResult<LoginResponse>>>() {
                    @Override
                    public ObservableSource<HttpResult<LoginResponse>> apply(InitResponse initResponse) throws Exception {
                        mView.save(initResponse);
                        return HttpAPI.getInstance().httpService().login(header, Configs.shop_sn, account, password);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .map(new TransformToResult<LoginResponse>())
                .subscribe(observer);

        mCompositeDisposable.add(observer);



//        subscribeAsyncToResult(
//                HttpAPI.getInstance().httpService().login(header, shop_sn, account, password),
//                new BaseResultObserver<LoginResponse>() {
//            @Override
//            protected void onSuccess(LoginResponse result) {
//                mView.hideLoading();
//                mView.loginSuccess(result);
//            }
//
//            @Override
//            protected void onFailure(Map<String, Object> map) {
//                mView.hideLoading();
//                mView.loginFailed(map);
//            }
//        });
    }

    @Override
    public void resever_money(String session_id, String shop_sn, int handover_id, int resever_money) {
        mView.showLoading();
        Map<String, String> header = HttpClient.getInstance().getHeader();

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().reserve_money(header, shop_sn, handover_id, resever_money),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String string) {
                        mView.hideLoading();
                        mView.reseverMoneySuccess();
                    }


                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.hideLoading();
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

package com.easygo.cashier.module.handover;

import com.easygo.cashier.bean.HandoverResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class HandoverPresenter extends BasePresenter<HandoverContract.IView> implements HandoverContract.IPresenter {


    @Override
    public void handover(int handover_id) {

        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().handover(header, handover_id, 2),
                new BaseResultObserver<HandoverResponse>() {
                    @Override
                    protected void onSuccess(HandoverResponse result) {
                        mView.handoverSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.handoverFailed(map);

                    }
                });
    }

    @Override
    public void loginout(int handover_id) {

        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().loginout(header, handover_id, 1),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mView.loginoutSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.loginoutFailed(map);
                    }
                });
    }

    @Override
    public void sale_list(int handover_id) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().sale_list(header, handover_id),
                new BaseResultObserver<List<HandoverSaleResponse>>() {
                    @Override
                    protected void onSuccess(List<HandoverSaleResponse> result) {
                        mView.saleListSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.saleListFailed(map);
                    }
                });
    }

    @Override
    public void print_info(String shop_sn, String printer_sn, String info) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("printer_sn", printer_sn);
        requestMap.put("times", 1);
        requestMap.put("info", info);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().printer_info(header, requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.printSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.printFailed(map);
                    }
                });
    }

}

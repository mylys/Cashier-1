package com.easygo.cashier.module.settlement;

import com.easygo.cashier.bean.CreateOderResponse;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class SettlementPresenter extends BasePresenter<SettlementContract.IView> implements SettlementContract.IPresenter{

    @Override
    public void createOrder(String json) {
        RequestBody requestBody = HttpClient.getInstance().createRequestBody(json);
        Map<String, String> header = HttpClient.getInstance().getHeader();

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().createOrder(header, requestBody),
                new BaseResultObserver<CreateOderResponse>() {

                    @Override
                    protected void onSuccess(CreateOderResponse result) {
                        mView.createOrderSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.createOrderFailed(map);
                    }
                });
    }

    @Override
    public void wechatPay(String shop_sn,  String order_no, int total_fee, String auth_code) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", "pay");
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);
        requestMap.put("total_fee", total_fee);
        requestMap.put("auth_code", auth_code);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().wechatPay(header,  requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.wechatPaySuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.wechatPayFailed(map);
                    }
                });
    }

    @Override
    public void checkWechatPayStatus(String shop_sn, String order_no) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", "query");
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().checkWechatPayStatus(header,  requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.checkWechatStatusSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.checkWechatStatusFailed(map);
                    }
                });
    }

    @Override
    public void aliPay(String shop_sn,  String order_no, int total_fee, String auth_code) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", "pay");
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);
        requestMap.put("total_fee", total_fee);
        requestMap.put("auth_code", auth_code);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().alipay(header,  requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.aliPaySuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.aliPayFailed(map);
                    }
                });


//        HttpAPI.getInstance().httpService().alipay(header,  requestMap)
//                .compose(new Threadscheduler<HttpResult<AlipayResponse>>())
//                .map(new TransformToResult<AlipayResponse>())
//                .flatMap(new Function<AlipayResponse, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(AlipayResponse alipayResponse) throws Exception {
//                        if(alipayResponse)
//
//                        return null;
//                    }
//                })


    }

    @Override
    public void checkAliPayStatus(String shop_sn, String order_no) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", "query");
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().checkAlipayStatus(header,  requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.checkAlipayStatusSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.checkAlipayStatusFailed(map);
                    }
                });
    }

    @Override
    public void cash(String shop_sn, String order_no, int buyer_pay, int change_money) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);
        requestMap.put("buyer_pay", buyer_pay);
        requestMap.put("change_money", change_money);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().cash(header, requestMap),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mView.cashSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.cashFailed(map);
                    }
                });
    }


    @Override
    public void print(String json) {
        RequestBody requestBody = HttpClient.getInstance().createRequestBody(json);
        Map<String, String> header = HttpClient.getInstance().getHeader();

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().print(header, requestBody),
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

    @Override
    public void print_info(String shop_sn, String printer_sn, String info) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("printer_sn", printer_sn);
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

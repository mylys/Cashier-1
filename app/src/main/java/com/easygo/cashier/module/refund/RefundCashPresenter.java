package com.easygo.cashier.module.refund;

import com.easygo.cashier.http.HttpAPI;
import com.easygo.cashier.printer.PrintHelper;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class RefundCashPresenter extends BasePresenter<RefundCashContract.IView> implements RefundCashContract.IPresenter {

    @Override
    public void cashRefund(String json) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        RequestBody requestBody = HttpClient.getInstance().createRequestBody(json);

        subscribeAsyncToResult(HttpAPI.getInstance().httpService().cash_refund(header, requestBody),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mView.cashRefundSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.cashRefundFailed(map);
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

        subscribeAsyncToResult(HttpAPI.getInstance().httpService().printer_info(header, requestMap),
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

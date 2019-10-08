package com.easygo.cashier.module.handover;

import com.easygo.cashier.bean.HandoverResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.http.HttpAPI;
import com.easygo.cashier.printer.PrintHelper;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandoverPresenter extends BasePresenter<HandoverContract.IView> implements HandoverContract.IPresenter {


    @Override
    public void handover(int handover_id) {
        mView.showLoading();

        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().handover(header, handover_id, 2),
                new BaseResultObserver<HandoverResponse>() {
                    @Override
                    protected void onSuccess(HandoverResponse result) {
                        mView.hideLoading();
                        mView.handoverSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.hideLoading();
                        mView.handoverFailed(map);

                    }
                });
    }

    @Override
    public void loginout(int handover_id) {
        mView.showLoading();

        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().loginout(header, handover_id, 1),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mView.hideLoading();
                        mView.loginoutSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.hideLoading();
                        mView.loginoutFailed(map);
                    }
                });
    }

    @Override
    public void sale_list(int handover_id) {
        mView.showLoading();
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().sale_list(header, handover_id),
                new BaseResultObserver<List<HandoverSaleResponse>>() {
                    @Override
                    protected void onSuccess(List<HandoverSaleResponse> result) {
                        mView.hideLoading();
                        mView.saleListSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.hideLoading();
                        mView.saleListFailed(map);
                    }
                });
    }

    @Override
    public void print_info(String shop_sn, String printer_sn, String info) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        for (int i = 0; i < PrintHelper.printers_count; i++) {
            InitResponse.PrintersBean printersBean = PrintHelper.printersBeans.get(i);
            String device_sn = printersBean.getDevice_sn();
            int print_times = printersBean.getPrint_times();

            if(PrintHelper.pop_till.equals(info)) {
                print_times = 1;
            }

            if(!printersBean.canUse(InitResponse.PrintersBean.type_handover)) {
                continue;
            }
            mView.showLoading();

            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("shop_sn", shop_sn);
            requestMap.put("printer_sn", device_sn);
            requestMap.put("times", print_times);
            requestMap.put("info", info);

            subscribeAsyncToResult(
                    HttpAPI.getInstance().httpService().printer_info(header, requestMap),
                    new BaseResultObserver<String>() {

                        @Override
                        protected void onSuccess(String result) {
                            mView.hideLoading();
                            mView.printSuccess(result);
                        }

                        @Override
                        protected void onFailure(Map<String, Object> map) {
                            mView.hideLoading();
                            mView.printFailed(map);
                        }
                    });
        }
    }

}

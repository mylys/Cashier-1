package com.easygo.cashier.module.order_history;

import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.http.HttpAPI;
import com.easygo.cashier.printer.PrintHelper;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryPresenter extends BasePresenter<OrderHistoryContract.IView> implements OrderHistoryContract.IPresenter {

    @Override
    public void post(int handover_id, String keyword, int page, int count) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().getOrderHistory(header, handover_id, keyword, page, count),
                new BaseResultObserver<List<OrderHistorysInfo>>() {
                    @Override
                    protected void onSuccess(List<OrderHistorysInfo> result) {
                        mView.getHistoryInfo(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.getHistorfFailed(map);
                    }
                });
    }

    @Override
    public void print_info(String shop_sn, String printer_sn, String info, int type) {
        mView.showLoading();
        Map<String, String> header = HttpClient.getInstance().getHeader();

        for (int i = 0; i < PrintHelper.printers_count; i++) {
            InitResponse.PrintersBean printersBean = PrintHelper.printersBeans.get(i);
            String device_sn = printersBean.getDevice_sn();
            int print_times = printersBean.getPrint_times();

            if(type == 0) {
                if(!printersBean.canUse(InitResponse.PrintersBean.type_order_history)) {
                    return;
                }
            } else {
                if(!printersBean.canUse(InitResponse.PrintersBean.type_refund)) {
                    return;
                }
            }

            if(!printersBean.canUse(InitResponse.PrintersBean.type_order_history)) {
                return;
            }
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

package com.easygo.cashier.module.order_history.order_history_refund;

import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.http.HttpAPI;
import com.easygo.cashier.printer.PrintHelper;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-21
 */
public class OrderHistoryRefundPresenter extends BasePresenter<OrderHistoryRefundContract.IView> implements OrderHistoryRefundContract.IPresenter {

    @Override
    public void post(String json) {
        RequestBody requestBody = HttpClient.getInstance().createRequestBody(json);
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().refund(header,requestBody),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mView.getHistoryRefundSuccess("退款成功");
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.getHistorfRefundFailed(map);
                    }
                });
    }

    @Override
    public void popTill(String shop_sn, String printer_sn) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("printer_sn", printer_sn);
        requestMap.put("info", PrintHelper.pop_till);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().printer_info(header, requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.popTillSuccess();
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.popTillFailed(map);
                    }
                });
    }
}

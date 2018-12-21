package com.easygo.cashier.module.order_history.order_history_refund;

import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

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
}

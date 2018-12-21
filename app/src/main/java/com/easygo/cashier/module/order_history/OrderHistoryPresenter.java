package com.easygo.cashier.module.order_history;

import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

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
}

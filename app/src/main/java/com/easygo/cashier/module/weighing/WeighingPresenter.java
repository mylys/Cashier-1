package com.easygo.cashier.module.weighing;

import com.easygo.cashier.MyApplication;
import com.easygo.cashier.bean.PrinterStatusResponse;
import com.easygo.cashier.bean.WeightBean;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.Map;

/**
 * @Describe：
 * @Date：2019-06-18
 */
public class WeighingPresenter extends BasePresenter<WeighingContract.View> implements WeighingContract.Presenter {

    @Override
    public void getWeightSku() {
        mView.showLoading();
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().weightSku(header),
                new BaseResultObserver<WeightBean>() {
                    @Override
                    protected void onSuccess(WeightBean bean) {
                        mView.hideLoading();
                        mView.getWeightSkuSuccess(bean);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.hideLoading();
                        mView.getWeightSkuFailed(map);
                    }
                });
    }
}

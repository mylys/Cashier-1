package com.easygo.cashier.module.goods;

import com.easygo.cashier.bean.CheckPayStatusResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.mvp.BasePresenter;

import java.util.Map;

import okhttp3.RequestBody;

public class GoodsPresenter extends BasePresenter<GoodsContract.IView> implements GoodsContract.IPresenter {

    @Override
    public void getGoods(String shop_id, String barcode) {
        subscribeBindLifecycle(
                HttpAPI.getInstance().httpService().getGoods(shop_id, barcode),
                new BaseResultObserver<GoodsResponse>() {

                    @Override
                    protected void onSuccess(GoodsResponse result) {
                        mView.getGoodsSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.getGoodsFailed(map);
                    }
                });
    }

    @Override
    public void searchGoods(String shop_id, String barcode, String good_name) {
        subscribeBindLifecycle(
                HttpAPI.getInstance().httpService().searchGoods(shop_id, barcode, good_name),
                new BaseResultObserver<GoodsResponse>() {

                    @Override
                    protected void onSuccess(GoodsResponse result) {
                        mView.getGoodsSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.getGoodsFailed(map);
                    }
                });
    }

    @Override
    public void createOrder(String json) {

        RequestBody requestBody = HttpClient.getInstance().createRequestBody(json);

        subscribeBindLifecycle(
                HttpAPI.getInstance().httpService().createOrder(requestBody),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.createOrderSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.createOrderFailed(map);
                    }
                });
    }

    @Override
    public void checkPayStatus(String order_sn) {
        subscribeBindLifecycle(
                HttpAPI.getInstance().httpService().checkPayStatus(order_sn),
                new BaseResultObserver<CheckPayStatusResponse>() {

                    @Override
                    protected void onSuccess(CheckPayStatusResponse result) {
                        mView.checkPayStatusSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.checkPayStatusFailed(map);
                    }
                });
    }

    @Override
    public void realMoney(String json) {

        RequestBody requestBody = HttpClient.getInstance().createRequestBody(json);

        subscribeBindLifecycle(
                HttpAPI.getInstance().httpService().getRealMoney(requestBody),
                new BaseResultObserver<RealMoneyResponse>() {

                    @Override
                    protected void onSuccess(RealMoneyResponse result) {
                        mView.realMoneySuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.realMoneyFailed(map);
                    }
                });
    }
}

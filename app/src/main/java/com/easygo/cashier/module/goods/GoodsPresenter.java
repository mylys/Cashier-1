package com.easygo.cashier.module.goods;

import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.easygo.cashier.http.HttpAPI;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class GoodsPresenter extends BasePresenter<GoodsContract.IView> implements GoodsContract.IPresenter {

    @Override
    public void getGoods(String shop_sn, String barcode) {

        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().getGoods(header, shop_sn, barcode, 1),
                new BaseResultObserver<List<GoodsResponse>>() {

                    @Override
                    protected void onSuccess(List<GoodsResponse> result) {
                        mView.getGoodsSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.getGoodsFailed(map);
                    }
                });
    }

    @Override
    public void searchGoods(String shop_sn, String barcode) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().getGoods(header, shop_sn, barcode, 2),
                new BaseResultObserver<List<GoodsResponse>>() {

                    @Override
                    protected void onSuccess(List<GoodsResponse> result) {
                        mView.searchGoodsSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.searchGoodsFailed(map);
                    }
                });
    }



    @Override
    public void realMoney(String json) {

        RequestBody requestBody = HttpClient.getInstance().createRequestBody(json);
        subscribeAsyncToResult(
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

    @Override
    public void popTill(String shop_sn, String printer_sn) {

        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().pop_till(header, shop_sn, printer_sn),
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

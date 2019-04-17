package com.easygo.cashier.module.goods;

import android.util.Log;

import com.easygo.cashier.Configs;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.GiftCardResponse;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.MemberDayInfo;
import com.easygo.cashier.bean.MemberDiscountInfo;
import com.easygo.cashier.bean.MemberInfo;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.easygo.cashier.bean.ShopActivityResponse;
import com.easygo.cashier.http.HttpAPI;
import com.easygo.cashier.printer.PrintHelper;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.base.HttpResult;
import com.niubility.library.http.rx.BaseErrorObserver;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class GoodsPresenter extends BasePresenter<GoodsContract.IView> implements GoodsContract.IPresenter {

    @Override
    public void getGoods(String shop_sn, String barcode) {
        mView.showLoading();

        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().getGoods(header, shop_sn, barcode, 1),
                new BaseResultObserver<List<GoodsResponse>>() {

                    @Override
                    protected void onSuccess(List<GoodsResponse> result) {
                        mView.hideLoading();
                        mView.getGoodsSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.hideLoading();
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
    public void popTill(String shop_sn, String printer_sn) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        for (int i = 0; i < PrintHelper.printers_count; i++) {
            InitResponse.PrintersBean printersBean = PrintHelper.printersBeans.get(i);
            String device_sn = printersBean.getDevice_sn();

            if (!printersBean.canUse(InitResponse.PrintersBean.type_settlement)) {
                return;
            }
            mView.showLoading();
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("shop_sn", shop_sn);
            requestMap.put("printer_sn", device_sn);
            requestMap.put("times", 1);
            requestMap.put("info", PrintHelper.pop_till);

            subscribeAsyncToResult(
                    HttpAPI.getInstance().httpService().printer_info(header, requestMap),
                    new BaseResultObserver<String>() {

                        @Override
                        protected void onSuccess(String result) {
                            mView.hideLoading();
                            mView.popTillSuccess();
                        }

                        @Override
                        protected void onFailure(Map<String, Object> map) {
                            mView.hideLoading();
                            mView.popTillFailed(map);
                        }
                    });
        }
    }

    @Override
    public void goods_activity(String shop_sn) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().goods_activity(header, shop_sn),
                new BaseResultObserver<GoodsActivityResponse>() {

                    @Override
                    protected void onSuccess(GoodsActivityResponse result) {
                        mView.goodsActivitySuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.goodsActivityFailed(map);
                    }
                });
    }

    @Override
    public void shop_activity(String shop_sn) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().shop_activity(header, shop_sn),
                new BaseResultObserver<ShopActivityResponse>() {

                    @Override
                    protected void onSuccess(ShopActivityResponse result) {
                        mView.shopActivitySuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.shopActivityFailed(map);
                    }
                });
    }

    @Override
    public void getMember(final String phone, final String barcode) {
        mView.showLoading();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().getMembers(HttpClient.getInstance().getHeader(), phone, barcode),
                new BaseResultObserver<MemberInfo>() {
                    @Override
                    protected void onSuccess(MemberInfo result) {
                        mView.hideLoading();
                        mView.getMemberSuccess(result, barcode, phone);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.hideLoading();
                        mView.getMemberFailed(map, barcode, phone);
                    }
                }
        );
    }

    @Override
    public void getMemberDay() {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService()
                        .getMembersDay(header, null, Configs.shop_sn)
                        .retry(2),
                new BaseResultObserver<List<MemberDayInfo>>() {
                    @Override
                    protected void onSuccess(List<MemberDayInfo> result) {
                        mView.getMemberDaySuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.getMemberDayFailed(map);
                    }
                });
    }

    @Override
    public void getMemberDiscount() {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService()
                        .getMemberDiscount(header, null, Configs.shop_sn)
                        .retry(2),
                new BaseResultObserver<List<MemberDiscountInfo>>() {
                    @Override
                    protected void onSuccess(List<MemberDiscountInfo> result) {
                        mView.getMemberDiscountSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.getMemberDiscountFailed(map);
                    }
                });
    }

    @Override
    public void get_coupon(final String coupon) {
        mView.showLoading();
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().get_coupon(header, coupon),
                new BaseResultObserver<CouponResponse>() {
                    @Override
                    protected void onSuccess(CouponResponse result) {
                        mView.hideLoading();
                        result.setCoupon_sn(coupon);
                        mView.couponSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.hideLoading();
                        mView.couponFailed(map);
                    }
                });
    }

    @Override
    public void gift_card(String card_no) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("type", 2);
        requestMap.put("card_no", card_no);

        subscribeAsyncToResult(HttpAPI.getInstance().httpService().gift_card(header, requestMap),
                new BaseResultObserver<GiftCardResponse>() {
                    @Override
                    protected void onSuccess(GiftCardResponse result) {
                        mView.giftCardSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.giftCardFailed(map);
                    }
                });
    }

    @Override
    public void heartbeat() {
        Observable<Object> retry = Observable.interval(Configs.interval, TimeUnit.SECONDS)
                .flatMap(new Function<Long, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<HttpResult<String>> apply(Long aLong) throws Exception {
                        Map<String, String> header = HttpClient.getInstance().getHeader();
                        return HttpAPI.getInstance().httpService().heartbeat(header);
                    }
                })
                .subscribeOn(Schedulers.io())
                .retry();  // retry保证请求失败后能重新订阅

        BaseErrorObserver<Object> observer = new BaseErrorObserver<Object>() {
            @Override
            public void onNext(Object s) {
                Log.i("heartbeat", "onNext: 心跳--------");
            }

            @Override
            protected void onFailure(Map<String, Object> map) {

            }
        };
        retry.subscribe(observer);
        mCompositeDisposable.add(observer);
    }

    @Override
    public void tillAuth(final String type, String till_password, String lock_password, String refund_password, String account, String password) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().verifyAuth(header, type, till_password, lock_password, refund_password, account, password),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        if ("till".equals(type)) {
                            mView.getTillAuthSuccess(result);
                        }else if ("lock".equals(type)){
                            mView.getLockSuccess(result);
                        }
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        if ("till".equals(type)) {
                            mView.getTillAythFailed(map);
                        }else if ("lock".equals(type)){
                            mView.getLockFailed(map);
                        }
                    }
                });
    }
}

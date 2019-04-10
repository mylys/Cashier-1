package com.easygo.cashier.module.settlement;

import com.easygo.cashier.bean.BankcardStatusResponse;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.CreateOderResponse;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.http.HttpAPI;
import com.easygo.cashier.printer.PrintHelper;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class SettlementPresenter extends BasePresenter<SettlementContract.IView> implements SettlementContract.IPresenter{

    @Override
    public void createOrder(String json) {
        RequestBody requestBody = HttpClient.getInstance().createRequestBody(json);
        Map<String, String> header = HttpClient.getInstance().getHeader();

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().createOrder(header, requestBody),
                new BaseResultObserver<CreateOderResponse>() {

                    @Override
                    protected void onSuccess(CreateOderResponse result) {
                        mView.createOrderSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.createOrderFailed(map);
                    }
                });
    }

    @Override
    public void wechatPay(String shop_sn,  String order_no, int total_fee, String auth_code) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", "pay");
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);
        requestMap.put("total_fee", total_fee);
        requestMap.put("auth_code", auth_code);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().wechatPay(header,  requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.wechatPaySuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.wechatPayFailed(map);
                    }
                });
    }

    @Override
    public void checkWechatPayStatus(String shop_sn, String order_no) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", "query");
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().checkWechatPayStatus(header,  requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.checkWechatStatusSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.checkWechatStatusFailed(map);
                    }
                });
    }

    @Override
    public void aliPay(String shop_sn,  String order_no, int total_fee, String auth_code) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", "pay");
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);
        requestMap.put("total_fee", total_fee);
        requestMap.put("auth_code", auth_code);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().alipay(header,  requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.aliPaySuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.aliPayFailed(map);
                    }
                });


//        HttpAPI.getInstance().httpService().alipay(header,  requestMap)
//                .compose(new Threadscheduler<HttpResult<AlipayResponse>>())
//                .map(new TransformToResult<AlipayResponse>())
//                .flatMap(new Function<AlipayResponse, ObservableSource<?>>() {
//                    @Override
//                    public ObservableSource<?> apply(AlipayResponse alipayResponse) throws Exception {
//                        if(alipayResponse)
//
//                        return null;
//                    }
//                })


    }

    @Override
    public void checkAliPayStatus(String shop_sn, String order_no) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("method", "query");
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().checkAlipayStatus(header,  requestMap),
                new BaseResultObserver<String>() {

                    @Override
                    protected void onSuccess(String result) {
                        mView.checkAlipayStatusSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.checkAlipayStatusFailed(map);
                    }
                });
    }

    @Override
    public void cash(String shop_sn, String order_no, int buyer_pay, int change_money) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_no", order_no);
        requestMap.put("buyer_pay", buyer_pay);
        requestMap.put("change_money", change_money);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().cash(header, requestMap),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mView.cashSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.cashFailed(map);
                    }
                });
    }

    @Override
    public void checkBankcardStatus(String order_no) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().checkBankcardPayStatus(header, order_no),
                new BaseResultObserver<BankcardStatusResponse>() {

                    @Override
                    protected void onSuccess(BankcardStatusResponse result) {
                        mView.bankcardSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.bankcardFailed(map);
                    }
                });
    }


    @Override
    public void print(String json) {
        RequestBody requestBody = HttpClient.getInstance().createRequestBody(json);
        Map<String, String> header = HttpClient.getInstance().getHeader();

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().print(header, requestBody),
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

    @Override
    public void print_info(String shop_sn, String printer_sn, String info) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        for (int i = 0; i < PrintHelper.printers_count; i++) {
            InitResponse.PrintersBean printersBean = PrintHelper.printersBeans.get(i);
            String device_sn = printersBean.getDevice_sn();

            if(!printersBean.canUse(InitResponse.PrintersBean.type_refund)) {
                return;
            }
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
                            mView.printSuccess(result);
                        }

                        @Override
                        protected void onFailure(Map<String, Object> map) {
                            mView.printFailed(map);
                        }
                    });
        }
    }

    @Override
    public void get_coupon(final String coupon_sn) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().get_coupon(header, coupon_sn),
                new BaseResultObserver<CouponResponse>() {
                    @Override
                    protected void onSuccess(CouponResponse result) {
                        result.setCoupon_sn(coupon_sn);
                        mView.couponSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.couponFailed(map);
                    }
                });
    }

    @Override
    public void memberWalletPay(String order_sn, String auth_code) {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().member_wallet(header, order_sn, auth_code),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mView.memberWalletSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.memberWalletFailed(map);
                    }
                }
        );
    }

    @Override
    public void unionPay(String order_sn) {
        Map<String, String> header = HttpClient.getInstance().getHeader();

        Map<String, Object> requestMap = new HashMap<>();
//        requestMap.put("shop_sn", shop_sn);
        requestMap.put("order_sn", order_sn);

        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().unionPay(header, requestMap),
                new BaseResultObserver<String>() {
                    @Override
                    protected void onSuccess(String result) {
                        mView.unionPaySuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.unionPayFailed(map);
                    }
                }
        );

//        HttpAPI.getInstance().httpService().unionPay(header, requestMap)
//                .map(new Function<HttpResult<String>, String>() {
//                    @Override
//                    public String apply(HttpResult<String> stringHttpResult) throws Exception {
//                        return stringHttpResult.getResult();
//                    }
//                })
//                .map(new Function<String, Bitmap>() {
//                    @Override
//                    public Bitmap apply(String s) throws Exception {
//                        return CodeUtils.createImage(s, );
//                    }
//                })



    }

    @Override
    public void closeOrder(String order_no) {
        mView.showLoading();
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().close_order(header, order_no), new BaseResultObserver<String>() {
            @Override
            protected void onSuccess(String result) {
                mView.hideLoading();
                mView.unlockCouponSuccess();
            }

            @Override
            protected void onFailure(Map<String, Object> map) {
                mView.unlockCouponFailed(map);
            }
        });


    }
}

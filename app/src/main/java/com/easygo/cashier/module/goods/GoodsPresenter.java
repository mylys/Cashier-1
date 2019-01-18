package com.easygo.cashier.module.goods;

import android.content.SharedPreferences;
import android.gesture.GestureUtils;

import com.easygo.cashier.Configs;
import com.easygo.cashier.bean.CouponResponse;
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
import com.niubility.library.base.BaseApplication;
import com.niubility.library.constants.Constans;
import com.niubility.library.http.base.HttpClient;
import com.niubility.library.http.base.HttpResult;
import com.niubility.library.http.rx.BaseResultObserver;
import com.niubility.library.mvp.BasePresenter;
import com.niubility.library.utils.GetSign;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.util.Date;
import java.util.HashMap;
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

        for (int i = 0; i < PrintHelper.printers_count; i++) {
            InitResponse.PrintersBean printersBean = PrintHelper.printersBeans.get(i);
            String device_sn = printersBean.getDevice_sn();

            if(!printersBean.canUse(InitResponse.PrintersBean.type_settlement)) {
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
                            mView.popTillSuccess();
                        }

                        @Override
                        protected void onFailure(Map<String, Object> map) {
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
        subscribeAsyncToResult(
                HttpAPI.getInstance().httpService().getMembers(HttpClient.getInstance().getHeader(), phone, barcode),
                new BaseResultObserver<MemberInfo>() {
                    @Override
                    protected void onSuccess(MemberInfo result) {
                        mView.getMemberSuccess(result, barcode, phone);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.getMemberFailed(map, barcode, phone);
                    }
                }
        );
    }

    @Override
    public void getMemberDay() {
        Map<String, String> header = HttpClient.getInstance().getHeader();
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().getMembersDay(header, null, Configs.shop_sn),
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
        subscribeAsyncToResult(HttpAPI.getInstance().httpService().getMemberDiscount(header, null, Configs.shop_sn),
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
        Map<String, String> header = new HashMap<>();
        header.put("LC-Appkey", "25");

        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication);
        long time = new Date().getTime() / 1000;
        String session_id = sp.getString(Constans.KEY_SESSION_ID, "");

        header.put("LC-Sign", GetSign.sign(time));
        header.put("LC-Session", session_id);
        header.put("LC-Timestamp", String.valueOf(time));

        subscribeAsyncToResult(HttpAPI.getInstance().httpService().get_coupon(header, coupon),
                new BaseResultObserver<CouponResponse>() {
                    @Override
                    protected void onSuccess(CouponResponse result) {
                        result.setCoupon_sn(coupon);
                        mView.couponSuccess(result);
                    }

                    @Override
                    protected void onFailure(Map<String, Object> map) {
                        mView.couponFailed(map);
                    }
                });
    }
}

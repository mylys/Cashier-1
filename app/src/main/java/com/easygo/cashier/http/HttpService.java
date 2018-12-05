package com.easygo.cashier.http;

import com.easygo.cashier.bean.CheckPayStatusResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.niubility.library.http.HttpResult;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpService {

    /**
     * 初始化
     */
    @FormUrlEncoded
    @POST("api/weixin/minapp/eq_s/pay/get_shop_by_asid")
    Observable<HttpResult<InitResponse>> init(@Field("mac_adr") String mac_adr);

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("api/")
    Observable<HttpResult<LoginResponse>> login(@Field("account") String account, @Field("password") String password);

    /**
     * 获取商品列表信息总额及促销信息
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("api/weixin/minapp/eq_s/pay/order_info")
    Observable<HttpResult<RealMoneyResponse>> getRealMoney(@Body RequestBody json);

    /**
     * 获取商品信息
     */
    @GET("api/weixin/minapp/eq_s/pay/goods_by_barcode")
    Observable<HttpResult<GoodsResponse>> getGoods(@Query("shop_id") String shop_id, @Query("barcode") String barcode);

    /**
     * 获取商品信息
     */
    @GET("api/weixin/minapp/eq_s/pay/goods_by_barcode")
    Observable<HttpResult<GoodsResponse>> searchGoods(@Query("shop_id") String shop_id, @Query("barcode") String barcode,
                                                        @Query("good_name") String good_name);



    /**
     * 创建订单
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("api/weixin/minapp/eq_s/pay/create_or")
    Observable<HttpResult<String>> createOrder(@Body RequestBody json);

    /**
     * 获取shop_id
     */
    @FormUrlEncoded
    @POST("api/weixin/minapp/eq_s/pay/get_shop_by_asid")
    Observable<HttpResult<InitResponse>> getShopId(@Field("asid") String asid);


    /**
     * 查询订单状态
     */
    @FormUrlEncoded
    @POST("api/weixin/minapp/eq_s/pay/check_pay_status")
    Observable<HttpResult<CheckPayStatusResponse>> checkPayStatus(@Field("order_sn") String order_sn);

    /**
     * 调起会员支付
     */
    @FormUrlEncoded
    @POST("api/weixin/minapp/eq_s/pay/pay_vip")
    Observable<HttpResult<String>> payVip();
}

package com.easygo.cashier.http;

import com.easygo.cashier.bean.CheckAlipayStatus;
import com.easygo.cashier.bean.CheckPayStatusResponse;
import com.easygo.cashier.bean.CreateOderResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.easygo.cashier.bean.AlipayResponse;
import com.niubility.library.http.base.HttpResult;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface HttpService {

    /**
     * 初始化
     * mac 08:ea:40:36:4f:3b
     * 190001—参数错误
     * 190002—店铺不存在
     */
    @FormUrlEncoded
    @POST("api/cash/shop")
    Observable<HttpResult<String>> init(@Field("mac") String mac);

    /**
     * 登录
     * 190001—参数错误
     * 190003—用户不存在
     * 190004—用户已锁定
     * 190005—密码错误
     */
    @FormUrlEncoded
    @POST("api/cash/login")
    Observable<HttpResult<LoginResponse>> login(@HeaderMap Map<String, String> header, @Field("shop_sn") String shop_sn,
                                                @Field("admin_name") String admin_name, @Field("password") String password);

    /**
     * 获取商品列表信息总额及促销信息
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("api/weixin/minapp/eq_s/pay/order_info")
    Observable<HttpResult<RealMoneyResponse>> getRealMoney(@Body RequestBody json);

    /**
     * 获取商品信息
     */
    @FormUrlEncoded
    @POST("api/pay/get_info")
    Observable<HttpResult<GoodsResponse>> getGoods(@HeaderMap Map<String, String> header, @Field("shop_sn") String shop_sn,
                                                   @Field("barcode") String barcode);

    /**
     * 获取商品信息
     */
    @GET("api/weixin/minapp/eq_s/pay/goods_by_barcode")
    Observable<HttpResult<GoodsResponse>> searchGoods(@Query("shop_sn") String shop_sn, @Query("barcode") String barcode,
                                                        @Query("good_name") String good_name);



    /**
     * 创建订单
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("api/pay/create_order")
    Observable<HttpResult<CreateOderResponse>> createOrder(@HeaderMap Map<String, String> header, @Body RequestBody json);

    /**
     * 获取shop_id
     * mac 08:ea:40:36:4f:3b
     */
    @FormUrlEncoded
    @POST("api/cash/shop_id")
    Observable<String> getShopId(@Field("mac") String mac);


    /**
     * 支付宝支付
     * method  = pay  支付 , method = query  查询 , method=cancel  撤销
     *
     */
    @FormUrlEncoded
    @POST("api/pay/alipay")
    Observable<HttpResult<String>> alipay(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);

    /**
     * 检测支付宝支付状态
     * method  = pay  支付 , method = query  查询 , method=cancel  撤销
     *
     */
    @FormUrlEncoded
    @POST("api/pay/alipay")
    Observable<HttpResult<CheckAlipayStatus>> checkAlipayStatus(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);


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

    /**
     * 打印
     */
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("api/pay/printer")
    Observable<HttpResult<String>> print(@HeaderMap Map<String, String> header, @Body RequestBody json);
}

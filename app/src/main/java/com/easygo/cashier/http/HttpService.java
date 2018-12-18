package com.easygo.cashier.http;

import com.easygo.cashier.bean.CreateOderResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.HandoverResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.bean.RealMoneyResponse;
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
    Observable<HttpResult<String>> checkAlipayStatus(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);


    /**
     * 微信支付
     * method  = pay  支付 , method = query  查询 , method=cancel  撤销
     *
     */
    @FormUrlEncoded
    @POST("api/pay/wechat")
    Observable<HttpResult<String>> wechatPay(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);

    /**
     * 检测微信支付状态
     * method  = pay  支付 , method = query  查询 , method=cancel  撤销
     *
     */
    @FormUrlEncoded
    @POST("api/pay/wechat")
    Observable<HttpResult<String>> checkWechatPayStatus(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);



    /**
     * 现金
     */
    @FormUrlEncoded
    @POST("api/pay/cash")
    Observable<HttpResult<String>> cash(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);


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

    /**
     * 打印机状态
     */
    @FormUrlEncoded
    @POST("api/pay/printer_status")
    Observable<HttpResult<String>> printer_status(@HeaderMap Map<String, String> header, @Field("shop_sn") String shop_sn,
                                                      @Field("printer_sn") String printer_sn);

    /**
     * 钱箱
     */
    @FormUrlEncoded
    @POST("api/pay/printer_till")
    Observable<HttpResult<String>> pop_till(@HeaderMap Map<String, String> header, @Field("shop_sn") String shop_sn,
                                                  @Field("printer_sn") String printer_sn);

    /**
     * 交接班信息
     *
     * type 1是退出登录，2是返回交接页的数据
     */
    @FormUrlEncoded
    @POST("api/cash/loginout")
    Observable<HttpResult<HandoverResponse>> handover(@HeaderMap Map<String, String> header, @Field("handover_id") int handover_id,
                                                      @Field("type") int type);

    /**
     * 登出
     *
     * type 1是退出登录，2是返回交接页的数据
     */
    @FormUrlEncoded
    @POST("api/cash/loginout")
    Observable<HttpResult<String>> loginout(@HeaderMap Map<String, String> header, @Field("handover_id") int handover_id,
                                                      @Field("type") int type);


    /**
     * type 1是退出登录，2是返回交接页的数据
     */
    @FormUrlEncoded
    @POST("api/cash/sale")
    Observable<HttpResult<HandoverSaleResponse>> sale_list(@HeaderMap Map<String, String> header, @Field("handover_id") int handover_id);





}

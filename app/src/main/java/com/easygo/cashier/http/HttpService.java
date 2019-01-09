package com.easygo.cashier.http;

import com.easygo.cashier.bean.CreateOderResponse;
import com.easygo.cashier.bean.GoodsActivityResponse;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.HandoverResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.bean.MemberInfo;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.bean.RealMoneyResponse;
import com.easygo.cashier.bean.ShopActivityResponse;
import com.niubility.library.http.base.HttpResult;

import java.util.List;
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
    @GET("api/cash_register/detail")
    Observable<HttpResult<InitResponse>> init(@Query("mac_address") String mac_address);


    /**
     * 备用金
     */
    @FormUrlEncoded
    @POST("api/cash/reserve")
    Observable<HttpResult<InitResponse>> reserve_money(@HeaderMap Map<String, String> header,
                                                       @Field("shop_sn") String shop_sn,
                                                       @Field("handover_id") int handover_id,
                                                       @Field("reserve_money") int reserve_money);


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
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/weixin/minapp/eq_s/pay/order_info")
    Observable<HttpResult<RealMoneyResponse>> getRealMoney(@Body RequestBody json);

    /**
     * 获取商品信息
     * type 1 精确搜索， 2 模糊搜索（此时barcode传关键字）
     */
    @FormUrlEncoded
    @POST("api/pay/get_info")
    Observable<HttpResult<List<GoodsResponse>>> getGoods(@HeaderMap Map<String, String> header, @Field("shop_sn") String shop_sn,
                                                         @Field("barcode") String barcode, @Field("type") int type);

    /**
     * 获取商品信息
     */
    @GET("api/weixin/minapp/eq_s/pay/goods_by_barcode")
    Observable<HttpResult<GoodsResponse>> searchGoods(@Query("shop_sn") String shop_sn, @Query("barcode") String barcode,
                                                      @Query("good_name") String good_name);


    /**
     * 创建订单
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/pay/create_order")
    Observable<HttpResult<CreateOderResponse>> createOrder(@HeaderMap Map<String, String> header, @Body RequestBody json);


    /**
     * 支付宝支付
     * method  = pay  支付 , method = query  查询 , method=cancel  撤销
     */
    @FormUrlEncoded
    @POST("api/pay/alipay")
    Observable<HttpResult<String>> alipay(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);

    /**
     * 检测支付宝支付状态
     * method  = pay  支付 , method = query  查询 , method=cancel  撤销
     */
    @FormUrlEncoded
    @POST("api/pay/alipay")
    Observable<HttpResult<String>> checkAlipayStatus(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);


    /**
     * 微信支付
     * method  = pay  支付 , method = query  查询 , method=cancel  撤销
     */
    @FormUrlEncoded
    @POST("api/pay/wechat")
    Observable<HttpResult<String>> wechatPay(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);

    /**
     * 检测微信支付状态
     * method  = pay  支付 , method = query  查询 , method=cancel  撤销
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
    @Headers({"Content-Type: application/json", "Accept: application/json"})
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
     * 打印机打印
     */
    @FormUrlEncoded
    @POST("api/pay/printer_till")
    Observable<HttpResult<String>> printer_info(@HeaderMap Map<String, String> header, @FieldMap Map<String, Object> map);


    /**
     * 钱箱
     */
    @FormUrlEncoded
    @POST("api/pay/printer_till")
    Observable<HttpResult<String>> pop_till(@HeaderMap Map<String, String> header, @Field("shop_sn") String shop_sn,
                                            @Field("printer_sn") String printer_sn);

    /**
     * 交接班信息
     * <p>
     * type 1是退出登录，2是返回交接页的数据
     */
    @FormUrlEncoded
    @POST("api/cash/loginout")
    Observable<HttpResult<HandoverResponse>> handover(@HeaderMap Map<String, String> header, @Field("handover_id") int handover_id,
                                                      @Field("type") int type);

    /**
     * 登出
     * <p>
     * type 1是退出登录，2是返回交接页的数据
     */
    @FormUrlEncoded
    @POST("api/cash/loginout")
    Observable<HttpResult<String>> loginout(@HeaderMap Map<String, String> header, @Field("handover_id") int handover_id,
                                            @Field("type") int type);


    /**
     * 交接班销售列表
     */
    @FormUrlEncoded
    @POST("api/cash/sale")
    Observable<HttpResult<List<HandoverSaleResponse>>> sale_list(@HeaderMap Map<String, String> header, @Field("handover_id") int handover_id);


    /*
     * page 页数
     * count 每页数量
     * keyword 搜索关键词
     */
    @FormUrlEncoded
    @POST("api/cash/order")
    Observable<HttpResult<List<OrderHistorysInfo>>> getOrderHistory(@HeaderMap Map<String, String> header,
                                                                    @Field("handover_id") int handover_id,
                                                                    @Field("keyword") String keyword,
                                                                    @Field("page") int page,
                                                                    @Field("count") int count);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/pay/refund")
    Observable<HttpResult<String>> refund(@HeaderMap Map<String, String> header, @Body RequestBody json);


    /**
     * 现金退款
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("api/pay/cash")
    Observable<HttpResult<String>> cash_refund(@HeaderMap Map<String, String> header, @Body RequestBody json);


    /**
     * 商品促销
     */
    @GET("api/activity/goods")
    Observable<HttpResult<GoodsActivityResponse>> goods_activity(@HeaderMap Map<String, String> header, @Query("shop_sn") String shop_sn);

    /**
     * 店铺促销
     */
    @GET("api/activity/shop")
    Observable<HttpResult<ShopActivityResponse>> shop_activity(@HeaderMap Map<String, String> header, @Query("shop_sn") String shop_sn);

    /*
     * 搜索会员
     */
    @GET("/api/member/search")
    Observable<HttpResult<MemberInfo>> getMembers(@HeaderMap Map<String, String> header, @Query("phone_number") String phone_number, @Query("member_token") String member_token);
}

package com.easygo.cashier;

/**
 * Handler 发送的消息
 */
public class Msg {

    /** 获取店铺信息 */
    public static final int MSG_GET_SHOP = 0;
    /** 获取店铺信息 */
    public static final int MSG_NETWORK = 1;
     /** 获取店铺信息 */
    public static final int MSG_RED_POINT = 2;
    /** 获取促销信息 */
    public static final int MSG_PROMOTION = 3;
    /** 查询支付宝支付状态 */
    public static final int MSG_CHECK_ALIPAY_STATUS = 4;
    /** 查询微信支付状态 */
    public static final int MSG_CHECK_WECHAT_STATUS = 5;
}

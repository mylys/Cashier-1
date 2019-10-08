package com.easygo.cashier.bean;

/**
 * @Describe：
 * @Date：2019-07-22
 */
public class WxPayResult {
    private String result_code;
    /**
     * query_fail查询支付状态失败，重新发起查询
     * pay_fail发起支付失败，重新发起支付（创建订单）
     * order no exist订单不存在
     * order paid or closed订单已支付或者已关单
     */
    private String result_msg;
    private String result_desc;

    public String getResult_desc() {
        return result_desc;
    }

    public void setResult_desc(String result_desc) {
        this.result_desc = result_desc;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_msg() {
        return result_msg;
    }

    public void setResult_msg(String result_msg) {
        this.result_msg = result_msg;
    }
}

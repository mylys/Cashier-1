package com.easygo.cashier.bean;

/**
 * @Describe：
 * @Date：2019-01-09
 */
public class MemberInfo {

    /**
     * member_id : 834
     * etps_id : 1
     * real_name : 张旗伟
     * headimg : https://egmall.esgao.cn/web/statics/images/avatar.png
     * integral : 888
     * wallet : 348.7
     * consumefee_convert_integral : 1
     * etps_name : 造就科技
     * etps_sn : E100001
     * is_enabled : 1
     * card_no : EG20181157745201
     * union_id : om2HJwvYbe-__e8shBPOdtdUagcM
     * phone : 180****7946
     * nick_name : fantasy
     * is_member : 1
     */

    private int member_id;
    private int etps_id;
    private String real_name;
    private String headimg;
    private int integral;
    private double wallet;
    private int consumefee_convert_integral;
    private String etps_name;
    private String etps_sn;
    private int is_enabled;
    private String card_no;
    private String union_id;
    private String phone;
    private String nick_name;
    private int is_member;

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getEtps_id() {
        return etps_id;
    }

    public void setEtps_id(int etps_id) {
        this.etps_id = etps_id;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public int getConsumefee_convert_integral() {
        return consumefee_convert_integral;
    }

    public void setConsumefee_convert_integral(int consumefee_convert_integral) {
        this.consumefee_convert_integral = consumefee_convert_integral;
    }

    public String getEtps_name() {
        return etps_name;
    }

    public void setEtps_name(String etps_name) {
        this.etps_name = etps_name;
    }

    public String getEtps_sn() {
        return etps_sn;
    }

    public void setEtps_sn(String etps_sn) {
        this.etps_sn = etps_sn;
    }

    public int getIs_enabled() {
        return is_enabled;
    }

    public void setIs_enabled(int is_enabled) {
        this.is_enabled = is_enabled;
    }

    public String getCard_no() {
        return card_no;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public String getUnion_id() {
        return union_id;
    }

    public void setUnion_id(String union_id) {
        this.union_id = union_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getIs_member() {
        return is_member;
    }

    public void setIs_member(int is_member) {
        this.is_member = is_member;
    }
}

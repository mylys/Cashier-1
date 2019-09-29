package com.easygo.cashier.bean;

import java.util.List;

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
     * wallet : 333.7
     * consumefee_convert_integral : 1
     * etps_name : 造就科技
     * etps_sn : E100001
     * is_enabled : 1
     * card_no : EG20181157745201
     * union_id : om2HJwvYbe-__e8shBPOdtdUagcM
     * phone : 180****7946
     * nick_name : fantasy
     * is_member : 1
     * coupons : [{"name":"减3全店","condition_value":"0.01","offer_type":1,"offer_type_str":"立减优惠券","offer_value":"3.00","effected_at":1546876800000,"expired_at":1548950399000}]
     */

    private int member_id;
    private int etps_id;
    private String real_name;
    private String headimg;
    private float integral;
    private double wallet;
    private int consumefee_convert_integral;
    private String etps_name;
    private String etps_sn;
    private int is_enabled;
    private String card_no;
    private String union_id;
    private String phone;
    private String nick_name;
    private String level;
    private int is_member;
    private List<CouponResponse> coupons;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

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

    public float getIntegral() {
        return integral;
    }

    public void setIntegral(float integral) {
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

    public List<CouponResponse> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponResponse> coupons) {
        this.coupons = coupons;
    }

    public static class CouponsBean {
        /**
         * name : 减3全店
         * condition_value : 0.01
         * offer_type : 1
         * offer_type_str : 立减优惠券
         * offer_value : 3.00
         * effected_at : 1546876800000
         * expired_at : 1548950399000
         */

        private String name;
        private String sn;
        private String condition_value;
        private int offer_type;
        private String offer_type_str;
        private String offer_value;
        private long effected_at;
        private long expired_at;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getCondition_value() {
            return condition_value;
        }

        public void setCondition_value(String condition_value) {
            this.condition_value = condition_value;
        }

        public int getOffer_type() {
            return offer_type;
        }

        public void setOffer_type(int offer_type) {
            this.offer_type = offer_type;
        }

        public String getOffer_type_str() {
            return offer_type_str;
        }

        public void setOffer_type_str(String offer_type_str) {
            this.offer_type_str = offer_type_str;
        }

        public String getOffer_value() {
            return offer_value;
        }

        public void setOffer_value(String offer_value) {
            this.offer_value = offer_value;
        }

        public long getEffected_at() {
            return effected_at;
        }

        public void setEffected_at(long effected_at) {
            this.effected_at = effected_at;
        }

        public long getExpired_at() {
            return expired_at;
        }

        public void setExpired_at(long expired_at) {
            this.expired_at = expired_at;
        }
    }
}

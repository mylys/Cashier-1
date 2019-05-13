package com.easygo.cashier.bean;


import android.text.TextUtils;

import java.io.Serializable;

public class GoodsResponse implements Serializable {


    /**
     * g_sku_id : 29986
     * company_id : 0
     * g_sku_name : 发舒服撒发-就是个福
     * short_name : 发舒服撒发-就是个福
     * first_c_id : 722
     * second_c_id : 727
     * three_c_id : 728
     * four_c_id : 729
     * g_c_name : 测试一级1-测试二级-测试三级-test
     * pic_big : null
     * barcode : wrwqr2413
     * g_u_id : 19
     * g_u_name : 千克
     * parent_id : 29985
     * process_id : 13
     * process_price : 1.00
     * purchase_price : 3.38
     * price : 5.50
     * discount_price : 0.00
     * membership_price : 0.00
     * is_inventory_limit : 0
     * on_sale_count : 100
     * birthday_price : 0.00
     * sell_status : 1
     * status : 1
     * is_weigh : 0
     * has_single_sale_price : 0
     * single_sale_price : 0
     */

    private int g_sku_id;
    private int company_id;
    private String g_sku_name;
    private String short_name;
    private int first_c_id;
    private int second_c_id;
    private int three_c_id;
    private int four_c_id;
    private String g_c_name;
    private String pic_big;
    private String barcode;
    private int g_u_id;
    private String g_u_name;
    private int parent_id;
    private int process_id;
    private String process_price;
    private String purchase_price;
    private String price;
    private String discount_price;
    private String membership_price;
    private int is_inventory_limit;
    private String on_sale_count;
    private String birthday_price;
    private int sell_status;
    private int status;
    private int is_weigh;
    private int has_single_sale_price;
    private Object single_sale_price;
    private String g_u_symbol;

    private boolean select;
    private boolean isMember;
    private int count;
    /**不可加减数量*/
    private boolean count_disable;
    /**
     * 唯一识别码， 扫到商品时的时间戳（毫秒）
     */
    private String identity;

    /**
     * 0: 非重量  1：重量  2：无码商品 3: 加工商品
     */
    private int type;

    public static final int type_normal = 0;
    public static final int type_weight = 1; //加工方式主商品也属于此类
    public static final int type_no_code = 2;
    public static final int type_processing = 3;

    public boolean isWeightGood() {
        return type == type_weight;
    }

    public static boolean isWeightGood(int type) {
        return type == type_weight;
    }


    /**
     * 商品促销优惠金额
     */
    private float goods_activity_discount;
    /**
     * 店铺促销优惠金额
     */
    private float shop_activity_discount;
     /**
     * 会员促销优惠金额
     */
    private float member_discount;

    /**
     * 优惠券促销分摊优惠金额
     */
    private float coupon_discount;
    /**
     * 临时商品促销分摊优惠金额
     */
    private float temp_goods_discount;

    /**
     * 临时整单促销分摊优惠金额
     */
    private float temp_order_discount;



    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public boolean isMemberPrice() {
        return isMember;
    }

    public void setMemberPrice(boolean member) {
        isMember = member;
    }

    public boolean isCount_disable() {
        return count_disable;
    }

    public void setCount_disable(boolean count_disable) {
        this.count_disable = count_disable;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getG_sku_id() {
        return g_sku_id;
    }

    public void setG_sku_id(int g_sku_id) {
        this.g_sku_id = g_sku_id;
    }

    public int getCompany_id() {
        return company_id;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public String getG_sku_name() {
        return g_sku_name;
    }

    public void setG_sku_name(String g_sku_name) {
        this.g_sku_name = g_sku_name;
    }

    /**
     * 判断 按斤为单位
     */
    public boolean isJin() {
        return TextUtils.equals("斤", g_u_name);
    }

    public String getShort_name() {
        return short_name;
    }

    /**
     * 获取打印时的商品名称   有商品简称时使用简称，否则使用商品名称
     * @return
     */
    public String getPrintName() {
        return !TextUtils.isEmpty(short_name)? short_name: g_sku_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public int getFirst_c_id() {
        return first_c_id;
    }

    public void setFirst_c_id(int first_c_id) {
        this.first_c_id = first_c_id;
    }

    public int getSecond_c_id() {
        return second_c_id;
    }

    public void setSecond_c_id(int second_c_id) {
        this.second_c_id = second_c_id;
    }

    public int getThree_c_id() {
        return three_c_id;
    }

    public void setThree_c_id(int three_c_id) {
        this.three_c_id = three_c_id;
    }

    public int getFour_c_id() {
        return four_c_id;
    }

    public void setFour_c_id(int four_c_id) {
        this.four_c_id = four_c_id;
    }

    public String getG_c_name() {
        return g_c_name;
    }

    public void setG_c_name(String g_c_name) {
        this.g_c_name = g_c_name;
    }

    public String getPic_big() {
        return pic_big;
    }

    public void setPic_big(String pic_big) {
        this.pic_big = pic_big;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getG_u_id() {
        return g_u_id;
    }

    public void setG_u_id(int g_u_id) {
        this.g_u_id = g_u_id;
    }

    public String getG_u_name() {
        return g_u_name;
    }

    public void setG_u_name(String g_u_name) {
        this.g_u_name = g_u_name;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    /**
     * 是否是主商品（加工方式不属于主商品）
     *  <p>true: 主商品</p>
     *  <p>false: 加工方式</p>
     */
    public boolean isMainGood() {
        return parent_id == 0;
    }
    /** 是否是加工方式，并且是属于此商品的加工方式 */
    public boolean isBelongTo(int g_sku_id) {
        return !isMainGood() && this.parent_id == g_sku_id;
    }

    public int getProcess_id() {
        return process_id;
    }

    public void setProcess_id(int process_id) {
        this.process_id = process_id;
    }

    public String getProcess_price() {
        return TextUtils.isEmpty(process_price) ? "0.00" : process_price;
    }

    public void setProcess_price(String process_price) {
        this.process_price = process_price;
    }

    public String getPurchase_price() {
        return purchase_price;
    }

    public void setPurchase_price(String purchase_price) {
        this.purchase_price = purchase_price;
    }

    public String getPrice() {
        return isMainGood()? price: getProcess_price();
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount_price() {
//        return discount_price;
        return String.valueOf(
                goods_activity_discount
                + member_discount
                + shop_activity_discount
                + coupon_discount
                + temp_goods_discount
                + temp_order_discount
        );
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public float getGoods_activity_discount() {
        return goods_activity_discount;
    }

    public void setGoods_activity_discount(float goods_activity_discount) {
        this.goods_activity_discount = goods_activity_discount;
    }

    public float getShop_activity_discount() {
        return shop_activity_discount;
    }

    public void setShop_activity_discount(float shop_activity_discount) {
        this.shop_activity_discount = shop_activity_discount;
    }

    public float getMember_discount() {
        return member_discount;
    }

    public void setMember_discount(float member_discount) {
        this.member_discount = member_discount;
    }

    public float getTemp_goods_discount() {
        return temp_goods_discount;
    }

    public void setTemp_goods_discount(float temp_goods_discount) {
        this.temp_goods_discount = temp_goods_discount;
    }

    public float getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(float coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public float getTemp_order_discount() {
        return temp_order_discount;
    }

    public void setTemp_order_discount(float temp_order_discount) {
        this.temp_order_discount = temp_order_discount;
    }

    public String getMembership_price() {
        return membership_price;
    }

    public void setMembership_price(String membership_price) {
        this.membership_price = membership_price;
    }

    public int getIs_inventory_limit() {
        return is_inventory_limit;
    }

    public void setIs_inventory_limit(int is_inventory_limit) {
        this.is_inventory_limit = is_inventory_limit;
    }

    public float getOn_sale_count() {
        return !TextUtils.isEmpty(on_sale_count)? Float.valueOf(on_sale_count): 0f;
    }

    public void setOn_sale_count(String on_sale_count) {
        this.on_sale_count = on_sale_count;
    }

    public String getBirthday_price() {
        return birthday_price;
    }

    public void setBirthday_price(String birthday_price) {
        this.birthday_price = birthday_price;
    }

    public int getSell_status() {
        return sell_status;
    }

    public void setSell_status(int sell_status) {
        this.sell_status = sell_status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_weigh() {
        return is_weigh;
    }

    public void setIs_weigh(int is_weigh) {
        this.is_weigh = is_weigh;
    }

    public boolean has_single_sale_price() {
        return has_single_sale_price == 1;
    }

    public void setHas_single_sale_price(int has_single_sale_price) {
        this.has_single_sale_price = has_single_sale_price;
    }

    public float getSingle_sale_price() {
        if(single_sale_price instanceof String) {
            return TextUtils.isEmpty((String) single_sale_price)? 0: Float.parseFloat((String) single_sale_price);
        } else {
            return 0f;
        }
    }

    public void setSingle_sale_price(Object single_sale_price) {
        this.single_sale_price = single_sale_price;
    }

    public String getG_u_symbol() {
        return !TextUtils.isEmpty(g_u_symbol)? g_u_symbol: "斤";
    }

    public void setG_u_symbol(String g_u_symbol) {
        this.g_u_symbol = g_u_symbol;
    }

    public boolean equals(GoodsResponse obj) {
        return super.equals(obj) ||
                (g_sku_id == obj.g_sku_id
                && getDiscount_price().equals(obj.getDiscount_price())
                && price.equals(obj.price)
                && temp_goods_discount == obj.temp_goods_discount
                && temp_order_discount == obj.temp_order_discount
                && goods_activity_discount == obj.goods_activity_discount
                && shop_activity_discount == obj.shop_activity_discount
                && member_discount == obj.member_discount
                && coupon_discount == obj.coupon_discount
                );
    }
}

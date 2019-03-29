package com.easygo.cashier.bean;

/**
 * @date：2018-12-19
 */
public class GoodsRefundInfo {
    private String product_name;
    private String product_price;
    private String product_preferential;
    private String product_subtotal;
    private String refund_num;
    private String refund_subtotal;
    private float product_num;
    private float count;
    private int quantity;
    private int s_sku_id;
    private boolean isSelect;
    private boolean isSelectRefund;
    private boolean isSelectReturnOfGoods;
    private int is_weigh;
    private int refund;
    private int parent_id;
    private int type;
    private String identity;
    private String discount;
    private float total_discount;
    private String g_u_symbol;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getRefund() {
        return refund;
    }

    public void setRefund(int refund) {
        this.refund = refund;
    }

    public int getIs_weigh() {
        return is_weigh;
    }

    public void setIs_weigh(int is_weigh) {
        this.is_weigh = is_weigh;
    }

    public int getS_sku_id() {
        return s_sku_id;
    }

    public void setS_sku_id(int s_sku_id) {
        this.s_sku_id = s_sku_id;
    }

    public float getProduct_num() {
        return product_num;
    }

    public void setProduct_num(float product_num) {
        this.product_num = product_num;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_preferential() {
        return product_preferential;
    }

    public void setProduct_preferential(String product_preferential) {
        this.product_preferential = product_preferential;
    }

    public String getProduct_subtotal() {
        return product_subtotal;
    }

    public void setProduct_subtotal(String product_subtotal) {
        this.product_subtotal = product_subtotal;
    }

    public String getRefund_num() {
        return refund_num;
    }

    public void setRefund_num(String refund_num) {
        this.refund_num = refund_num;
    }

    public String getRefund_subtotal() {
        return refund_subtotal;
    }

    public void setRefund_subtotal(String refund_subtotal) {
        this.refund_subtotal = refund_subtotal;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelectRefund() {
        return isSelectRefund;
    }

    public void setSelectRefund(boolean selectRefund) {
        isSelectRefund = selectRefund;
    }

    public boolean isSelectReturnOfGoods() {
        return isSelectReturnOfGoods;
    }

    public void setSelectReturnOfGoods(boolean selectReturnOfGoods) {
        isSelectReturnOfGoods = selectReturnOfGoods;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public float getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(float total_discount) {
        this.total_discount = total_discount;
    }

    public String getG_u_symbol() {
        return g_u_symbol;
    }

    public void setG_u_symbol(String g_u_symbol) {
        this.g_u_symbol = g_u_symbol;
    }
}

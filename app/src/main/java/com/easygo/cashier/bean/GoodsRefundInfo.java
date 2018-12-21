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
    private int product_num;
    private int s_sku_id;
    private boolean isSelect;

    public int getS_sku_id() {
        return s_sku_id;
    }

    public void setS_sku_id(int s_sku_id) {
        this.s_sku_id = s_sku_id;
    }

    public int getProduct_num() {
        return product_num;
    }

    public void setProduct_num(int product_num) {
        this.product_num = product_num;
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
}

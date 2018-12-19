package com.easygo.cashier;

import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.bean.OrderHistoryInfo;

import java.util.ArrayList;
import java.util.List;

public class Test {

    public static String shop_sn = "0001";
    public static String barcode = "096619756803";

    public static List<OrderHistoryInfo> getOrderHistoryData() {
        ArrayList<OrderHistoryInfo> orderHistoryInfos = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            OrderHistoryInfo info = new OrderHistoryInfo();
            info.setAcount("账号 " + i);
            info.setCount(i);


            info.setGoods(getGoodsInfos());
            info.setOrder_no("订单单号" + i);
            info.setReceipts(i);
            info.setReceivable(i);
            info.setRefund(i % 3);
            info.setTime("时间" + i);
            info.setTotal_money(i);
            info.setReturn_of_goods_count(i);
            info.setPay_way(i % 3);
            info.setCoupon(i);
            info.setBuyer("昵称" + i);

            orderHistoryInfos.add(info);
        }

        return orderHistoryInfos;
    }

    public static List<GoodsInfo> getGoodsInfos() {
        ArrayList<GoodsInfo> goodsInfos = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            GoodsInfo goodsInfo = new GoodsInfo();
            goodsInfo.setBarcode("条码" + j);
            goodsInfo.setCount(j);
            goodsInfo.setCoupon(j);
            goodsInfo.setName("商品" + j);
            goodsInfo.setPrice(j);
            goodsInfo.setReal_pay(j);
            goodsInfo.setSubtotal(j);
            goodsInfo.setTotal_money(j);

            goodsInfos.add(goodsInfo);
        }

        return goodsInfos;
    }

    public static List<GoodsRefundInfo> getGoodsRefundInfos() {
        ArrayList<GoodsRefundInfo> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            GoodsRefundInfo goodsRefundInfo = new GoodsRefundInfo();
            goodsRefundInfo.setProduct_name("商品 - " + (i + 1));
            goodsRefundInfo.setProduct_price("￥" + (i + 1) + "000.00");
            goodsRefundInfo.setProduct_preferential(i + "");
            goodsRefundInfo.setProduct_subtotal("￥" + (i + 1) + "000.00");
            goodsRefundInfo.setRefund_num("1");
            goodsRefundInfo.setRefund_subtotal((i + 1) + "000");
            goodsRefundInfo.setSelect(false);

            list.add(goodsRefundInfo);
        }
        return list;
    }
}

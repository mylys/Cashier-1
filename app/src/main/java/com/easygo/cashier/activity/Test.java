package com.easygo.cashier.activity;

import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.bean.OrderHistoryInfo;

import java.util.ArrayList;
import java.util.List;

public class Test {

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
            info.setRefund(i);
            info.setTime("时间" + i);
            info.setTotal_money(i);
            info.setReturn_of_goods_count(i);

            orderHistoryInfos.add(info);
        }

        return  orderHistoryInfos;
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
}

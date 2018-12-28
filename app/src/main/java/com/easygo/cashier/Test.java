package com.easygo.cashier;

import android.app.Activity;
import android.hardware.input.InputManager;
import android.util.Log;
import android.view.InputDevice;

import com.easygo.cashier.bean.GoodsInfo;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.bean.OrderHistoryInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_SERVICE;

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

    public static void detectInputDeviceWithShell() {
        try {
            //获得外接USB输入设备的信息
            Process p = Runtime.getRuntime().exec("cat /proc/bus/input/devices");
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                String deviceInfo = line.trim();
                //对获取的每行的设备信息进行过滤，获得自己想要的。
                if (deviceInfo.contains("Name="))
                    Log.i("hjq", "detectInputDeviceWithShell: " + deviceInfo);
            }
            Log.i("hjq", "-----------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

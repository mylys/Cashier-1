package com.easygo.cashier;

/**
 * 自编码解析类
 */
public class BarcodeUtils {

    public static boolean isWeightCode(String code) {
        return code.startsWith("22");
    }

    public static String getProductCode(String code) {
        return code.subSequence(0, 7).toString();
    }

    public static int getProductWeight(String code) {
        String weight = code.subSequence(7, 12).toString();
        return Integer.valueOf(weight);
    }
}
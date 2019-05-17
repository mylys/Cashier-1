package com.easygo.cashier.utils;

/**
 * 自编码解析类
 */
public class BarcodeUtils {

    public static boolean isWeightCode(String code) {
        return code.startsWith("22");
    }

    public static boolean isMember(String code){
        return code.length() == 18 && (code.startsWith("16") || code.startsWith("18")) ;
    }

    public static String getProductCode(String code) {
        if(code.length() < 7) {
            return "";
        }
        return code.subSequence(2, 7).toString();
    }

    /**
     * 金额码
     * @param code
     * @return
     */
    public static int getProductTotalMoney(String code) {
        if(code.length() < 12) {
            return 0;
        }
        String weight = code.subSequence(7, 12).toString();
        return Integer.valueOf(weight);
    }

    public static int getProductWeight(String code) {
        if(code.length() != 18) {
            return 0;
        }

//        String weight = code.subSequence(7, 12).toString();
        String weight = code.subSequence(12, 17).toString();
        return Integer.valueOf(weight);
    }
}

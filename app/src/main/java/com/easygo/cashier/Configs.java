package com.easygo.cashier;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.niubility.library.constants.Constans;
import com.niubility.library.utils.SharedPreferencesUtils;

public class Configs {

    /**
     * 门店
     */
    public static String shop_sn;
    /**
     * 订单号
     */
    public static String order_no;

    /**
     * 打印机
     */
//    public static String printer_sn = "918510026";
    public static String printer_sn = "918529231";

    /* admin */
    public static String admin_name = "";



    public static String getShop_sn(Context context) {
        if(TextUtils.isEmpty(shop_sn)) {
            SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(context);
            shop_sn = sp.getString(Constans.KEY_SHOP_SN, "");
        }
        return shop_sn;
    }

}

package com.easygo.cashier;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.easygo.cashier.bean.LoginResponse;
import com.niubility.library.constants.Constans;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class Configs {

    /**
     * 门店
     */
    public static String shop_sn;
    /**
     * 门店名称
     */
    public static String shop_name;
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
    public static int cashier_id;

    /* 登录获取权限列表 --- 全局控制 */
    public static List<LoginResponse.MenuBean> menuBeanList;

    /* 根据字段判断后台返回权限是否存在（权限id） --- 防止权限字段更新导致奔溃 */
    public static final int[] menus = new int[]{
            277, 278, 294, 309, //首页id（收银id，备用金id，查看id）
            279, 280, 311,      //历史订单id（退款id，查看id）
            281, 282, 283,      //交接班id（登出id，销售列表id）
            295, 298,           //设备状态id（打印机状态id）
            296,                //打印机打印id
            297,                //弹出钱箱id
            299, 300, 301,      //订单id（获取商品信息id，创建订单id）
            302, 303, 304, 305, //支付id（支付宝支付id，微信支付id，现金支付id）
            310                 //无码商品id
    };

    /* 判断权限id是否存在于权限列表中 并获取role权限是否显示 */
    public static int getRole(int permissionId) {
        if (Configs.menuBeanList != null && Configs.menuBeanList.size() > 0) {
            for (LoginResponse.MenuBean parentBean : menuBeanList) {
                if (parentBean.getMenu_id() == permissionId) {
                    return parentBean.getRole();
                } else {
                    for (LoginResponse.MenuBean.ChildMenusBean childBean : parentBean.getChild_menus()) {
                        if (childBean.getMenu_id() == permissionId) {
                            return childBean.getRole();
                        }
                    }
                }
            }
        }
        return -1;
    }

    public static String getShop_sn(Context context) {
        if (TextUtils.isEmpty(shop_sn)) {
            SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(context);
            shop_sn = sp.getString(Constans.KEY_SHOP_SN, "");
        }
        return shop_sn;
    }

}

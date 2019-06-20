package com.easygo.cashier.printer.ZQPrint;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.zqebsdk.zqebsdk;
import com.zqprintersdk.PrinterConst;


/**
 * @Describe：中崎电子秤---称重机
 * @Date：2019-06-12
 */
public class ZQEBUtil {
    private static volatile ZQEBUtil instance;
    private zqebsdk zqeb = null;
    private boolean isConnect;      //是否连接
    private String error_msg;
    private String[] strings;

    public static ZQEBUtil getInstance() {
        if (instance == null) {
            synchronized (ZQEBUtil.class) {
                if (instance == null) {
                    instance = new ZQEBUtil();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        zqeb = new zqebsdk();
        zqebConnect("ttyS4:9600", context);//// 串口连接：ttyS4:9600
    }

    private void zqebConnect(String connect, Context context) {
        int net = zqeb.EB_Connect(connect, context);
        switch (net) {
            case PrinterConst.ErrorCode.INVALIDPARAM:
                error_msg = "打印机参数无效";
                break;
            case PrinterConst.ErrorCode.NOPERMISSION:
                error_msg = "没有该端口的访问权限";
                break;
            case PrinterConst.ErrorCode.PORTERROR:
                error_msg = "端口错误";
                break;
        }
        isConnect = net == PrinterConst.ErrorCode.SUCCESS;
    }

    public void zqebDisconnect() {
        if (isConnect) {
            zqeb.EB_Disconnect();
            isConnect = false;
        }
    }

    /**
     * 置零
     */
    public void zqebSetZero() {
        if (isConnect) {
            zqeb.EB_SetZero();
        }
    }

    /**
     * 除皮
     */
    public void zqebNetWeight() {
        if (isConnect) {
            zqeb.EB_SetNetWeight();
        }
    }

    /**
     * 清皮
     */
    public void zqebClearTare() {
        if (isConnect) {
            zqeb.EB_ClearTare();
        }
    }

    /**
     * 置皮
     */
    public void zqebSetTare(double weight) {
        if (isConnect) {
            zqeb.EB_SetTare(weight);
        }
    }

    /**
     * 是否连接
     *
     * @return
     */
    public boolean isConnect() {
        return isConnect;
    }

    /**
     * 连接错误提示
     *
     * @return
     */
    public String Error_msg() {
        return error_msg;
    }

    /**
     * 读取数据 返回[状态，重量]
     *
     * @return
     */
    public String[] ReadData() {
        String[] strRet = zqeb.EB_GetWeightTare();
        Log.i("电子秤输出信息===", String.format("%s,%s", strRet[0], strRet[1]));
        String strState = "";//状态
        String strWeight = "";//重量
        switch (Integer.valueOf(strRet[0])) {
            case -3:
                strState = "数据异常";
                break;
            case -2:
                strState = "读取数据失败";
                break;
            case -1:
                strState = "端口错误";
                break;
            case 0:
                strState = "稳定";
                strWeight = strRet[1];
                break;
            case 1:
                strState = "不稳定";
                strWeight = strRet[1];
                break;
            case 2:
                strState = "重量移除或不为零";
                strWeight = strRet[1];
                break;
        }
        if (strings == null) {
            strings = new String[2];
        }
        strings[0] = strWeight;
        strings[1] = strState;
        return strings;
    }
}

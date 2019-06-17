package com.easygo.cashier.printer.ZQPrint;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.zqprintersdk.PrinterConst;
import com.zqprintersdk.ZQPrinterSDK;

/**
 * @Describe：中崎电子秤---打印机
 * @Date：2019-06-12
 */

public class ZQPrinterUtil {
    private static volatile ZQPrinterUtil instance;
    private ZQPrinterSDK prn = null;
    private boolean isConnect = false;//是否连接

    //  1.统一对齐方式
    //      * 1.PrinterConst.Alignment.LEFT
    //      * 2.PrinterConst.Alignment.CENTER
    //      * 3.PrinterConst.Alignment.RIGHT
    private final int LEFT = PrinterConst.Alignment.LEFT;
    private final int CENTER = PrinterConst.Alignment.CENTER;
    private final int RIGHT = PrinterConst.Alignment.RIGHT;

    public static ZQPrinterUtil getInstance() {
        if (instance == null) {
            synchronized (ZQPrinterUtil.class) {
                if (instance == null) {
                    instance = new ZQPrinterUtil();
                }
            }
        }
        return instance;
    }

    /* 初始化打印SDK */
    public void init(Context context) {
        prn = new ZQPrinterSDK();
        prn.SetCharacterSet("gb2312");                    //默认打印“gb2312”模式
        prn.Prn_PrinterInit();                               // 初始化打印
        ZQPrinterConnect("ttyS1:115200", context);  // 默认串口连接，串口号：ttyS1:115200
    }

    /* 设置打印字体格式 */
    public void setZQPrinterCharacterSet(String characterSet) {
        if (isConnect)
            prn.SetCharacterSet(characterSet);
    }

    /* 获取打印版本号 */
    public String getZQPrinterVersion() {
        if (isConnect)
            return prn.SDK_GetVersion();
        return "";
    }

    /**
     * 获取打印端口列表信息 --- USB，蓝牙，串口，WIFI
     *
     * @param i
     * @param context
     * @return
     */
    public String[] getZQPrinterPortList(int i, Context context) {
        if (isConnect)
            return prn.Prn_GetPortList(i, context);
        return null;
    }

    /**
     * 设置特定连接方式 --- USB，蓝牙，串口，WIFI
     *
     * @param connect
     * @param context
     */
    public void ZQPrinterConnect(String connect, Context context) {
        int nRet = prn.Prn_Connect(connect, context);
        if (nRet != PrinterConst.ErrorCode.SUCCESS) {
            Log.i("TAG ---- ", "连接异常");
        }
        isConnect = nRet == PrinterConst.ErrorCode.SUCCESS;
    }

    /**
     * 获取打印机状态
     *
     * @return
     */
    public String getZQPrintStatus() {
        if (prn != null) {
            int nRet = prn.Prn_Status();
            switch (nRet) {
                case PrinterConst.PrinterStatus.ONLINE:
                    //状态正常
                    return "状态正常";
                case PrinterConst.PrinterStatus.COVEROPEN:
                    return "请检查打印机是否开盖";
                case PrinterConst.PrinterStatus.EMPTYPAPER:
                    return "请检查打印机是否缺纸";
                case PrinterConst.PrinterStatus.OFFLINE:
                    return "打印机已掉线";
                case PrinterConst.PrinterStatus.OTHERERROR:
                    return "打印机未连接";
            }
        }
        return "未知状态";
    }

    /**
     * 获取打印机电池状态
     *
     * @return
     */
    public String getZQPrinterPowerStatus() {
        if (prn != null) {
            int nRet = prn.Prn_PowerStatus();
            switch (nRet) {
                case PrinterConst.PowerStatus.HIGH:
                    return "电量高";
                case PrinterConst.PowerStatus.LOW:
                    return "电量低";
                case PrinterConst.PowerStatus.MIDDLE:
                    return "电量中";
                case PrinterConst.PowerStatus.SMALL:
                    return "电量少";
                case PrinterConst.PowerStatus.NO:
                    return "没电";
                default:
                    return "电池容量:" + nRet + "%";
            }
        }
        return "未知状态";
    }

    /**
     * 断开连接
     */
    public void setZQPrinterDisconnect() {
        if (isConnect) {
            prn.Prn_Disconnect();
            isConnect = false;
        }
    }

    /**
     * 普通打印，如果传输是StringBuffer或StringBuilder并且以“，”隔开---则循环打印，否则输出当前字符串
     *
     * @param msg
     */
    public void printString(boolean isSplit, String msg) {
        if (isConnect) {
            if (isSplit) {
                String[] str = msg.split(",");
                for (String string : str) {
                    prn.Prn_PrintString(string);
                    prn.Prn_LineFeed(1);
                }
            } else {
                prn.Prn_PrintString(msg);
            }
            spaceLine();
        }
    }

    /**
     * 打印字符串，可设置字体大小
     * Prn_PrintText(数据体，显示对齐位置，字体模式，字体宽度)
     *
     * @param msg
     */
    public void printText(String msg) {
        if (isConnect) {
            //  1.Font.DEFAULT（FontA字体，12x24）
            //  2.Font.FONTB（FontB字体，9x17，仅对ASCII字符有效）
            //  3.Font.BOLD（加粗打印）
            //  4.Font.UNDERLIN（加下划线打印）
            //  5.Font.REVERSE（反转打印）
            prn.Prn_PrintText(msg, LEFT, PrinterConst.Font.DEFAULT, PrinterConst.WidthSize.SIZE0);
            prn.Prn_LineFeed(1);
        }
    }

    /**
     * 打印一维条码
     *
     * @param data 数据体
     *             void Prn_PrintBarcode(数据体，条码类型，宽度，高度，是否显示字体(显示的位置))
     */
    public void printBarcode(String data, int width, int height) {
        /*
         * Barcode.EAN8（支持条码字符个数：7≤n≤8，字符范围：纯数字，48≤data≤57）
         * Barcode.JAN8（支持条码字符个数：7≤n≤8，字符范围：如上）
         * Barcode.EAN13（支持条码字符个数：12≤n≤13，字符范围：如上）
         * Barcode.JAN13（支持条码字符个数：12≤n≤13，字符范围：如上）
         * Barcode.CODE39（支持条码字符个数：1≤n≤255，字符范围：48≤data≤57 65≤data≤90 Data=32 36 37 43 45 46 47）
         * Barcode.CODE128（支持条码字符个数：2≤n≤255，字符范围：0≤data≤127）
         * Barcode.CODE128_ZQ（支持条码字符个数：2≤n≤255，字符范围：如上）
         */
        if (isConnect) {
            prn.Prn_SetAlignment(CENTER);//设置居中
            prn.Prn_PrintBarcode(data, PrinterConst.Barcode.JAN13, width, height, PrinterConst.BarcodeText.TEXT_NONE);
        }
    }

    /**
     * 打印二维码（一体秤不支持打印二维码）
     *
     * @param data
     */
    public void printQRcode(String data) {
        /*
         * PrinterConst.QRCode.ALPH 字母
         * PrinterConst.QRCode.NUM  数字
         * PrinterConst.QRCode.CHN  中文
         */
        if (isConnect)
            prn.Prn_PrintQRCode(PrinterConst.QRCode.ALPH, data);
    }

    /**
     * 打印位图
     *
     * @param data
     */
    public void printBitmap(Bitmap data) {
        if (isConnect) {
            prn.Prn_SetAlignment(CENTER);
            prn.Prn_PrintBitmap(data, PrinterConst.BitmapSize.SIZE3);
        }
    }

    //打印空格行
    public void spaceLine() {
        if (isConnect) {
            for (int i = 0; i < 6; i++) {
                prn.Prn_LineFeed(1);
                prn.Prn_PrintString("");
            }
        }
    }

    public ZQPrinterSDK getRrn() {
        if (prn != null && isConnect) {
            return prn;
        }
        return null;
    }
}

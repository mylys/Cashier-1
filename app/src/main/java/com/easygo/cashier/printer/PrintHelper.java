package com.easygo.cashier.printer;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PrintHelper {

    /**
     * <BR> ：换行符
     *  <CUT> ：切刀指令(主动切纸,仅限切刀打印机使用才有效果)
     *  <LOGO> ：打印LOGO指令(前提是预先在机器内置LOGO图片)
     *  <PLUGIN> ：钱箱或者外置音响指令
     *  <CB></CB>：居中放大
     *  <B></B>：放大一倍
     *  <C></C>：居中
     *  <L></L>：字体变高一倍
     *  <W></W>：字体变宽一倍
     *  <QR></QR>：二维码
     *  <RIGHT></RIGHT>：右对齐
     *  <BOLD></BOLD>：字体加粗
     */

    public static final String CB_left = "<CB>";
    public static final String CB_right = "</CB>";
    /**换行*/
    public static final String BR = "<BR>";
    /**弹出钱箱*/
    public static final String pop_till = "<PLUGIN>";

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private StringBuilder sb = new StringBuilder();


    public static final String NORMAL = "在线，工作状态正常。";
    public static final String OFFLINE = "离线";


}

package com.easygo.cashier.printer;

import com.easygo.cashier.printer.obj.HandoverInfoPrintObj;
import com.tools.command.EscCommand;

import java.util.Vector;

/**
 * 佳博打印机，打印单据帮助类
 */
public class PrinterHelpter {
    
    public static final String TAG = PrinterHelpter.class.getSimpleName();
    
    public static Vector<Byte> handoverInfoDatas(HandoverInfoPrintObj obj) {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 2);
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        // 打印文字
        esc.addText("交接班单据\n");
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);

        // 打印文字
        esc.addText("收银员：");
        esc.addText(obj.admin_name + "\n");
        esc.addText("登录时间:");
        esc.addText(obj.login_time + "\n");
        esc.addText("登出时间:");
        esc.addText(obj.loginout_time + "\n");
        esc.addText("--------------------------------\n");
        esc.addText("总单据数：");
        esc.addText(obj.total_order_count + "\n");
        esc.addText("销售单：");
        esc.addText(obj.sale_count + "\n");
        esc.addText("退货单：");
        esc.addText(obj.refund_count + "\n");
        esc.addText("--------------------------------\n");
        esc.addText("总销售额：");
        esc.addText(obj.total_sales + "\n");
        esc.addText("现金：");
        esc.addText(obj.cash + "\n");
        esc.addText("支付宝：");
        esc.addText(obj.alipay + "\n");
        esc.addText("微信：");
        esc.addText(obj.wechat + "\n");
        esc.addText("总退款金额：");
        esc.addText(obj.all_refund + "\n");
        esc.addText("--------------------------------\n");
        esc.addText("总现金数：");
        esc.addText(obj.total_cash + "\n");
        esc.addText("现金收入：");
        esc.addText(obj.cash_income + "\n");
        esc.addText("实收金额：");
        esc.addText(obj.receipts + "\n");
        esc.addText("找零金额：");
        esc.addText(obj.change + "\n");
        esc.addText("退款现金：");
        esc.addText(obj.cash_refund + "\n");
        esc.addPrintAndFeedLines((byte) 1);

        return esc.getCommand();
    }
    
    
}

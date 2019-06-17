package com.easygo.cashier.printer.ZQPrint.printObject;

import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.easygo.cashier.printer.ZQPrint.ZQPrinterUtil;
import com.easygo.cashier.printer.local.obj.HandoverInfoPrintObj;
import com.easygo.cashier.printer.local.obj.HandoverSaleListPrintObj;
import com.zqprintersdk.PrinterConst;
import com.zqprintersdk.ZQPrinterSDK;

import java.text.DecimalFormat;

/**
 * @Describe：
 * @Date：2019-06-14
 */
public class PrintHandover {

    private ZQPrinterSDK prn = ZQPrinterUtil.getInstance().getRrn();
    public static final String TAG = PrintCashier.class.getSimpleName();
    private final int mode = PrinterConst.Font.DEFAULT;//默认字体模式

    private final int LEFT = PrinterConst.Alignment.LEFT;
    private final int CENTER = PrinterConst.Alignment.CENTER;

    public void printInfo(HandoverInfoPrintObj obj) {
        if (prn != null) {
            prn.Prn_PrintText("交接班单据", CENTER, mode, PrinterConst.WidthSize.SIZE1 | PrinterConst.HeightSize.SIZE1);
            prn.Prn_LineFeed(2);
            printText("收银员：" + obj.admin_name);
            printText("登录时间：" + obj.login_time);
            printText("登出时间：" + obj.loginout_time);
            printText("--------------------------------");
            printText("总单据数：" + obj.total_order_count);
            printText("销售单：" + obj.sale_count);
            printText("退货单：" + obj.refund_count);
            printText("--------------------------------");
            printText("总销售额：" + obj.total_sales + "元");
            printText("现金：" + obj.cash + "元");
            printText("支付宝：" + obj.alipay + "元");
            printText("微信：" + obj.wechat + "元");
            printText("会员钱包:" + obj.member + "元");
            printText("银联：" + obj.bank_card + "元");
            printText("礼品卡：" + obj.gift_card + "元");
            printText("总退款金额：" + obj.all_refund + "元");
            printText("--------------------------------");
            printText("总现金数：" + obj.total_cash + "元");
            printText("现金收入：" + obj.cash_income + "元");
            printText("实收金额：" + obj.receipts + "元");
            printText("找零金额：" + obj.change + "元");
            printText("退款现金：" + obj.cash_refund + "元");
            ZQPrinterUtil.getInstance().spaceLine();
        }
    }

    public void printSaleList(HandoverSaleListPrintObj obj) {
        if (prn != null) {
            prn.Prn_PrintText(obj.shop_name, CENTER, mode, PrinterConst.WidthSize.SIZE1 | PrinterConst.HeightSize.SIZE1);
            prn.Prn_LineFeed(2);
            printText("时间：" + obj.time);
            printText("收银员：" + obj.admin_name);
            printText("--------------------------------");
            printText("品名  分类  单价  数量/重量  小计  ");

            DecimalFormat df = new DecimalFormat("0.00");
            DecimalFormat df_weight = new DecimalFormat("0.000");
            int size = obj.data.size();
            int count = 0;
            float total_money = 0;

            for (int i = 0; i < size; i++) {
                HandoverSaleResponse item = obj.data.get(i);
                count += item.getQuantity();
                total_money += item.getMoney();

                printText((i + 1) + "." + item.getG_sku_name());
                printString("   " + item.getG_c_name());
                printString("          " + item.getUnit_price());
                String text = GoodsResponse.isWeightGood(item.getType()) ? df_weight.format(item.getCount()) + item.getG_u_symbol()
                        : String.valueOf(item.getCount());
                printString("   " + text);
                printText("   " + df.format(item.getMoney()));
            }
            printText("--------------------------------");
            printText("总数量：" + String.valueOf(count));
            printText("总金额：" + df.format(total_money) + "元");
            ZQPrinterUtil.getInstance().spaceLine();
        }
    }

    private void printText(String msg) {
        prn.Prn_PrintText(msg, LEFT, mode, PrinterConst.WidthSize.SIZE0);
        prn.Prn_LineFeed(1);
    }

    private void printString(String msg) {
        prn.Prn_PrintText(msg, LEFT, mode, PrinterConst.WidthSize.SIZE0);
    }
}

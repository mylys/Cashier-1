package com.easygo.cashier.printer.ZQPrint.printObject;

import android.app.Activity;

import com.easygo.cashier.Events;
import com.easygo.cashier.R;
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

    public void printInfo(Activity activity, HandoverInfoPrintObj obj) {
        if (prn != null) {
            boolean en = Events.LANGUAGE.equals("en");
            prn.Prn_PrintText(activity.getString(R.string.print_handover), CENTER, mode, PrinterConst.WidthSize.SIZE1 | PrinterConst.HeightSize.SIZE1);
            prn.Prn_LineFeed(2);
            printText(activity.getString(R.string.print_cashier) + obj.admin_name);
            printText(activity.getString(R.string.print_login_time) + obj.login_time);
            printText(activity.getString(R.string.print_login_out_time) + obj.loginout_time);
            printText("--------------------------------");
            printText(activity.getString(R.string.print_total_document) + obj.total_order_count);
            printText(activity.getString(R.string.print_sale_order) + obj.sale_count);
            printText(activity.getString(R.string.print_return_order) + obj.refund_count);
            printText("--------------------------------");
            printText(activity.getString(R.string.print_total_sale) + obj.total_sales + (en ? "" : "元"));
            printText(activity.getString(R.string.print_cash) + obj.cash + (en ? "" : "元"));
            printText(activity.getString(R.string.print_alipay) + obj.alipay + (en ? "" : "元"));
            printText(activity.getString(R.string.print_wechat) + obj.wechat + (en ? "" : "元"));
            printText(activity.getString(R.string.print_member_wallet) + obj.member + (en ? "" : "元"));
            printText(activity.getString(R.string.print_unionpay) + obj.bank_card + (en ? "" : "元"));
            printText(activity.getString(R.string.print_gift_card) + obj.gift_card + (en ? "" : "元"));
            printText(activity.getString(R.string.print_total_refund) + obj.all_refund + (en ? "" : "元"));
            printText("--------------------------------");
            printText(activity.getString(R.string.print_total_cash) + obj.total_cash + (en ? "" : "元"));
            printText(activity.getString(R.string.print_cash_income) + obj.cash_income + (en ? "" : "元"));
            printText(activity.getString(R.string.print_paid_amount_money) + obj.receipts + (en ? "" : "元"));
            printText(activity.getString(R.string.print_change_money) + obj.change + (en ? "" : "元"));
            printText(activity.getString(R.string.print_refund_cash) + obj.cash_refund + (en ? "" : "元"));
            ZQPrinterUtil.getInstance().spaceLine();
        }
    }

    public void printSaleList(Activity activity, HandoverSaleListPrintObj obj) {
        if (prn != null) {
            prn.Prn_PrintText(obj.shop_name, CENTER, mode, PrinterConst.WidthSize.SIZE1 | PrinterConst.HeightSize.SIZE1);
            prn.Prn_LineFeed(2);
            printText(activity.getString(R.string.print_time) + obj.time);
            printText(activity.getString(R.string.print_cashier) + obj.admin_name);
            printText("--------------------------------");
            printText(activity.getString(R.string.print_list_title));

            DecimalFormat df = new DecimalFormat("0.00");
            DecimalFormat df_weight = new DecimalFormat("0.000");
            int size = obj.data.size();
            int count = 0;
            float total_money = 0;

            boolean en = Events.LANGUAGE.equals("en");
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
            printText(activity.getString(R.string.print_total_number) + String.valueOf(count));
            printText(activity.getString(R.string.print_total_money) + df.format(total_money) + (en ? "" : "元"));

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

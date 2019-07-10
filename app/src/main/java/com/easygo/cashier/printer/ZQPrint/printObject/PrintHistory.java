package com.easygo.cashier.printer.ZQPrint.printObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.easygo.cashier.Events;
import com.easygo.cashier.R;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.printer.ZQPrint.ZQPrinterUtil;
import com.easygo.cashier.printer.local.obj.OrderHistoryGoodsListPrintObj;
import com.easygo.cashier.printer.local.obj.OrderHistoryRefundPrintObj;
import com.niubility.library.common.config.BaseConfig;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zqprintersdk.PrinterConst;
import com.zqprintersdk.ZQPrinterSDK;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @Describe：
 * @Date：2019-06-14
 */
public class PrintHistory {

    private ZQPrinterSDK prn = ZQPrinterUtil.getInstance().getRrn();
    public static final String TAG = PrintCashier.class.getSimpleName();
    private final int mode = PrinterConst.Font.DEFAULT;//默认字体模式

    private final int LEFT = PrinterConst.Alignment.LEFT;
    private final int CENTER = PrinterConst.Alignment.CENTER;

    public void print(Activity activity, OrderHistoryGoodsListPrintObj obj) {
        if (prn != null) {
            DecimalFormat df = new DecimalFormat("#0.00");
            DecimalFormat df_weight = new DecimalFormat("#0.000");
            boolean en = Events.LANGUAGE.equals("en");

            prn.Prn_PrintText(obj.shop_name, CENTER, mode, PrinterConst.WidthSize.SIZE1 | PrinterConst.HeightSize.SIZE1);
            prn.Prn_LineFeed(2);
            printText(activity.getString(R.string.print_order_number) + obj.order_no);
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_time) + obj.time);
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_cashier) + obj.admin_name);
            prn.Prn_LineFeed(1);
            printText("--------------------------------");
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_list_title));
            prn.Prn_LineFeed(1);

            OrderHistorysInfo info = obj.data;
            List<OrderHistorysInfo.ListBean> list = info.getList();

            int count = 0;
            float total_discount = 0f;
            float discount;

            for (int i = 0, j = obj.data.getList().size(); i < j; i++) {
                OrderHistorysInfo.ListBean item = list.get(i);

                discount = Float.valueOf(item.getDiscount());
                total_discount += discount;
                count += item.getQuantity();

                printText((i + 1) + "." + item.getG_sku_name());
                prn.Prn_LineFeed(1);
                printText("    " + item.getUnit_price());
                printText("  " + item.getDiscount());
                printText("  ");
                printText(GoodsResponse.isWeightGood(item.getType()) ? df_weight.format(item.getCount()) + item.getG_u_symbol() : String.valueOf(item.getCount()));
                printText("  " + df.format(item.getMoney()));
                prn.Prn_LineFeed(1);
            }
            printText("--------------------------------");
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_total_number) + count);
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_original_money) + info.getTotal_money() + (en ? "" : "元"));
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_total_offer) + df.format(total_discount) + (en ? "" : "元"));
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_total_money) + info.getReal_pay() + (en ? "" : "元"));
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_pay_type) + obj.pay_type);
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_paid_amount) + info.getBuyer_pay() + (en ? "" : "元"));
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_change) + info.getChange_money() + (en ? "" : "元"));
            prn.Prn_LineFeed(1);
            String text = info.getRefund_fee() != null ? info.getRefund_fee() + (en ? "" : "元") : "0.00" + (en ? "" : "元");
            printText(activity.getString(R.string.print_refund) + text);
            ZQPrinterUtil.getInstance().spaceLine();
        }
    }

    public void printRefund(Activity activity, OrderHistoryRefundPrintObj obj) {
        if (prn != null) {
            DecimalFormat df_weight = new DecimalFormat("#0.000");
            boolean en = Events.LANGUAGE.equals("en");

            prn.Prn_PrintText(activity.getString(R.string.print_refund_order), CENTER, mode, PrinterConst.WidthSize.SIZE1 | PrinterConst.HeightSize.SIZE1);
            prn.Prn_LineFeed(2);
            printText(activity.getString(R.string.print_order_number) + obj.order_no);
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_cashier) + obj.admin_name);
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_time) + obj.time);
            prn.Prn_LineFeed(1);
            printText("--------------------------------");
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_list_title));
            prn.Prn_LineFeed(1);

            ArrayList<GoodsRefundInfo> info = obj.data;

            for (int i = 0, j = info.size(); i < j; i++) {
                GoodsRefundInfo item = info.get(i);

                boolean select = item.isSelectReturnOfGoods();
                if (!select) {
                    continue;
                }

                printText((i + 1) + "." + item.getProduct_name());
                prn.Prn_LineFeed(1);
                printText("     " + item.getProduct_price());
                printText("  " + item.getProduct_preferential());
                printText("   ");
                printText(GoodsResponse.isWeightGood(item.getType()) ? df_weight.format(Float.valueOf(item.getRefund_num())) + item.getG_u_symbol()
                        : item.getRefund_num());
                printText("  " + item.getRefund_subtotal());
                prn.Prn_LineFeed(1);
            }
            printText("--------------------------------");
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_total_number) + obj.count);
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_original_money) + obj.total_price + (en ? "" : "元"));
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_total_offer) + obj.discount + (en ? "" : "元"));
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_total_money) + obj.real_pay + (en ? "" : "元"));
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_refund_type) + obj.pay_type);
            prn.Prn_LineFeed(1);
            printText(activity.getString(R.string.print_refund_money) + obj.refund + (en ? "" : "元"));
            ZQPrinterUtil.getInstance().spaceLine();
        }
    }

    private void printText(String msg) {
        prn.Prn_PrintText(msg, LEFT, mode, PrinterConst.WidthSize.SIZE0);
    }
}

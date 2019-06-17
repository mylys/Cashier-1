package com.easygo.cashier.printer.ZQPrint.printObject;

import android.graphics.Bitmap;
import android.text.TextUtils;

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

    public void print(OrderHistoryGoodsListPrintObj obj) {
        if (prn != null) {
            DecimalFormat df = new DecimalFormat("#0.00");
            DecimalFormat df_weight = new DecimalFormat("#0.000");

            prn.Prn_PrintText(obj.shop_name, CENTER, mode, PrinterConst.WidthSize.SIZE1 | PrinterConst.HeightSize.SIZE1);
            prn.Prn_LineFeed(2);
            printText("订单号：" + obj.order_no);
            prn.Prn_LineFeed(1);
            printText("时间：" + obj.time);
            prn.Prn_LineFeed(1);
            printText("收银员：" + obj.admin_name);
            prn.Prn_LineFeed(1);
            printText("--------------------------------");
            prn.Prn_LineFeed(1);
            printText("品名  单价  优惠  数量/重量  小计");
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
            printText("总数量：" + count);
            prn.Prn_LineFeed(1);
            printText("原价：" + info.getTotal_money() + "元");
            prn.Prn_LineFeed(1);
            printText("优惠：" + df.format(total_discount) + "元");
            prn.Prn_LineFeed(1);
            printText("总金额：" + info.getReal_pay() + "元");
            prn.Prn_LineFeed(1);
            printText("支付方式：" + obj.pay_type);
            prn.Prn_LineFeed(1);
            printText("实收：" + info.getBuyer_pay() + "元");
            prn.Prn_LineFeed(1);
            printText("找零：" + info.getChange_money() + "元");
            prn.Prn_LineFeed(1);
            String text = info.getRefund_fee() != null ? info.getRefund_fee() + "元" : "0.00" + "元";
            printText("退款：" + text);
            ZQPrinterUtil.getInstance().spaceLine();
        }
    }

    public void printRefund(OrderHistoryRefundPrintObj obj) {
        if (prn != null) {
            DecimalFormat df_weight = new DecimalFormat("#0.000");

            prn.Prn_PrintText("退款单", CENTER, mode, PrinterConst.WidthSize.SIZE1 | PrinterConst.HeightSize.SIZE1);
            prn.Prn_LineFeed(2);
            printText("订单号：" + obj.order_no);
            prn.Prn_LineFeed(1);
            printText("时间：" + obj.time);
            prn.Prn_LineFeed(1);
            printText("收银员：" + obj.admin_name);
            prn.Prn_LineFeed(1);
            printText("--------------------------------");
            prn.Prn_LineFeed(1);
            printText("品名  单价  优惠  数量/重量  小计");
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
            printText("总数量：" + obj.count);
            prn.Prn_LineFeed(1);
            printText("原价：" + obj.total_price + "元");
            prn.Prn_LineFeed(1);
            printText("优惠：" + obj.discount + "元");
            prn.Prn_LineFeed(1);
            printText("总金额：" + obj.real_pay + "元");
            prn.Prn_LineFeed(1);
            printText("退款方式：" + obj.pay_type);
            prn.Prn_LineFeed(1);
            printText("退款金额：" + obj.refund + "元");
            ZQPrinterUtil.getInstance().spaceLine();
        }
    }

    private void printText(String msg) {
        prn.Prn_PrintText(msg, LEFT, mode, PrinterConst.WidthSize.SIZE0);
    }
}

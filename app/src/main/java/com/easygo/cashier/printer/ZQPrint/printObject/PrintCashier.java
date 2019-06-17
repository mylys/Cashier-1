package com.easygo.cashier.printer.ZQPrint.printObject;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.printer.ZQPrint.ZQPrinterUtil;
import com.easygo.cashier.printer.local.obj.CashierPrintObj;
import com.niubility.library.common.config.BaseConfig;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zqprintersdk.PrinterConst;
import com.zqprintersdk.ZQPrinterSDK;

import java.text.DecimalFormat;


/**
 * @Describe：
 * @Date：2019-06-14
 */
public class PrintCashier {

    private ZQPrinterSDK prn = ZQPrinterUtil.getInstance().getRrn();
    public static final String TAG = PrintCashier.class.getSimpleName();
    private final int mode = PrinterConst.Font.DEFAULT;//默认字体模式

    private final int LEFT = PrinterConst.Alignment.LEFT;
    private final int CENTER = PrinterConst.Alignment.CENTER;

    public void print(CashierPrintObj obj) {
        if (prn != null) {
            DecimalFormat df = new DecimalFormat("#0.00");
            DecimalFormat df_weight = new DecimalFormat("#0.000");

            prn.Prn_PrintText(obj.shop_name, CENTER, mode, PrinterConst.WidthSize.SIZE1 | PrinterConst.HeightSize.SIZE1);
            prn.Prn_LineFeed(2);
            printText("订单号：" + obj.trade_no);
            prn.Prn_LineFeed(1);
            printText("时间：" + obj.time);
            prn.Prn_LineFeed(1);
            printText("收银员：" + obj.admin_name);
            prn.Prn_LineFeed(1);
            printText("--------------------------------");
            prn.Prn_LineFeed(1);
            printText("  品名  单价  优惠  数量  小计  ");
            prn.Prn_LineFeed(1);
            for (int i = 0; i < obj.data.size(); i++) {
                GoodsResponse item = obj.data.get(i).getData();

                float price = Float.parseFloat(item.getPrice());
                float discount = Float.parseFloat(item.getDiscount_price());
                float count = obj.data.get(i).getCount();
                String subtotal = df.format(count * price - discount);

                boolean empty = TextUtils.isEmpty(item.getBarcode());

                String text = empty ? item.getG_sku_name() : item.getBarcode();
                printText((i + 1) + "." + text);
                prn.Prn_LineFeed(1);
                if (!empty) {
                    printText(item.getG_sku_name());
                    prn.Prn_LineFeed(1);
                }
                printText("      " + df.format(price));
                printText("  " + df.format(discount));
                printText("  ");
                switch (obj.data.get(i).getItemType()) {
                    case GoodsEntity.TYPE_WEIGHT:
                    case GoodsEntity.TYPE_PROCESSING:
                        printText(df_weight.format(count) + item.getG_u_symbol());
                        break;
                    case GoodsEntity.TYPE_GOODS:
                    case GoodsEntity.TYPE_ONLY_PROCESSING:
                    default:
                        printText(String.valueOf(count));
                        break;
                }
                printText("  " + subtotal);
                prn.Prn_LineFeed(1);
            }
            printText("--------------------------------");
            prn.Prn_LineFeed(1);
            printText("总数量：" + obj.count);
            prn.Prn_LineFeed(1);
            printText("原价：" + df.format(obj.total_money) + "元");
            prn.Prn_LineFeed(1);
            printText("优惠：" + df.format(obj.discount) + "元");
            prn.Prn_LineFeed(1);
            printText("总金额：" + df.format(obj.real_pay) + "元");
            prn.Prn_LineFeed(1);
            printText("支付方式：" + obj.pay_type);
            prn.Prn_LineFeed(1);
            printText("礼品卡：" + df.format(obj.gift_card_money) + "元");
            prn.Prn_LineFeed(1);
            printText("实收：" + df.format(obj.real_pay + obj.change) + "元");
            prn.Prn_LineFeed(1);
            printText("找零：" + df.format(obj.change) + "元");
            prn.Prn_LineFeed(2);
            ZQPrinterUtil.getInstance().printBarcode(obj.trade_no, 80, 3);
            prn.Prn_PrintText(obj.trade_no, CENTER, mode, PrinterConst.WidthSize.SIZE0);
            prn.Prn_LineFeed(2);
            prn.Prn_PrintText("--扫码开具发票--", CENTER, mode, PrinterConst.WidthSize.SIZE0);
            prn.Prn_LineFeed(2);
            String content = "https://h5.esgao.cn/easygo-pos-invoice?trade_no=" + obj.trade_no;
            if (BaseConfig.environment_index != 0) {
                content = "http://test.h5.esgao.cn/easygo-pos-invoice?trade_no=" + obj.trade_no;
            }
            Bitmap image = CodeUtils.createImage(content, 150, 150, null);
            ZQPrinterUtil.getInstance().printBitmap(image);
            ZQPrinterUtil.getInstance().spaceLine();
        }
    }

    private void printText(String msg) {
        prn.Prn_PrintText(msg, LEFT, mode, PrinterConst.WidthSize.SIZE0);
    }
}

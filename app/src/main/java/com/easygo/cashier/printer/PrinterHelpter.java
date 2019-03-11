package com.easygo.cashier.printer;

import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.bean.GoodsRefundInfo;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.easygo.cashier.bean.OrderHistorysInfo;
import com.easygo.cashier.printer.obj.CashierPrintObj;
import com.easygo.cashier.printer.obj.HandoverInfoPrintObj;
import com.easygo.cashier.printer.obj.HandoverSaleListPrintObj;
import com.easygo.cashier.printer.obj.OrderHistoryGoodsListPrintObj;
import com.easygo.cashier.printer.obj.OrderHistoryRefundPrintObj;
import com.tools.command.EscCommand;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * 佳博打印机，打印单据帮助类
 */
public class PrinterHelpter {
    
    public static final String TAG = PrinterHelpter.class.getSimpleName();

    /**
     * 收银
     */
    public static Vector<Byte> cashier(CashierPrintObj obj) {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 2);
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        // 打印文字
        esc.addText(obj.shop_name + "\n");
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);

        // 打印文字
        esc.addText("订单号:");
        esc.addText(obj.order_no + "\n");
        esc.addText("时间:");
        esc.addText(obj.time + "\n");
        esc.addText("收银员：");
        esc.addText(obj.admin_name + "\n");
        esc.addText("--------------------------------\n");
        esc.addText("品名  单价  折扣  数量  小计  \n");

        List<GoodsEntity<GoodsResponse>> goodsData = obj.data;

        DecimalFormat df = new DecimalFormat("#0.00");
        int size = goodsData.size();
        int count = 0;
        float price = 0f;
        String subtotal;
        GoodsEntity<GoodsResponse> good;
        GoodsResponse data;
        int index = 0;
        for (int i = 0; i < size; i++) {
            index += 1;
            good = goodsData.get(i);
            data = good.getData();

            count = data.getCount();
            price = Float.valueOf(data.getPrice());
            subtotal = df.format(count * price);


            esc.addText(String.valueOf(index));
            esc.addText(".");
            esc.addText(data.getG_sku_name());
            esc.addText("   \n");
            esc.addText("     ");
            esc.addText(data.getPrice());
            esc.addText("   ");
            esc.addText("1.00");
            esc.addText("   ");
            switch (good.getItemType()) {
                case GoodsEntity.TYPE_WEIGHT:
                case GoodsEntity.TYPE_ONLY_PROCESSING:
                case GoodsEntity.TYPE_PROCESSING:
                    esc.addText(count + "g");
                    break;
                case GoodsEntity.TYPE_GOODS:
                default:
                    esc.addText(String.valueOf(count));
                    break;
            }
            esc.addText("   ");
            esc.addText(subtotal + "\n");

            if (good.getItemType() == GoodsEntity.TYPE_PROCESSING) {
                data = good.getProcessing();
                if (data != null) {
                    index += 1;

                    count = data.getCount();
                    price = Float.valueOf(data.getPrice());
                    subtotal = df.format(count * price);


                    esc.addText(String.valueOf(index));
                    esc.addText(".");
                    esc.addText(data.getG_sku_name());
                    esc.addText("   \n");
                    esc.addText("     ");
                    esc.addText(data.getPrice());
                    esc.addText("   ");
                    esc.addText("1.00");
                    esc.addText("   ");
                    esc.addText(String.valueOf(count));
                    esc.addText("   ");
                    esc.addText(subtotal + "\n");
                }
            }
        }

        esc.addText("--------------------------------\n");
        esc.addText("总数量：");
        esc.addText(obj.count + "\n");
        esc.addText("原价：");
        esc.addText(obj.total_money + "元\n");
        esc.addText("优惠：");
        esc.addText(obj.discount + "元\n");
        esc.addText("总金额：");
        esc.addText(obj.real_pay + "元\n");
        esc.addText("支付方式：");
        esc.addText(obj.pay_type + "\n");
        esc.addText("实收：");
        esc.addText(df.format(Float.valueOf(obj.real_pay) + Float.valueOf(obj.change)) + "元\n");
        esc.addText("找零：");
        esc.addText(obj.change + "元\n");

        esc.addPrintAndFeedLines((byte) 3);

        return esc.getCommand();
//        return null;
    }


    /**
     * 历史订单列表
     */
    public static Vector<Byte> orderHistroyGoodsList(OrderHistoryGoodsListPrintObj obj) {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 2);
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        // 打印文字
        esc.addText(obj.shop_name + "\n");
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);

        // 打印文字
        esc.addText("订单号:");
        esc.addText(obj.order_no + "\n");
        esc.addText("时间:");
        esc.addText(obj.time + "\n");
        esc.addText("收银员：");
        esc.addText(obj.admin_name + "\n");
        esc.addText("--------------------------------\n");
        esc.addText("品名  单价  优惠  数量/重量  小计  \n");

        OrderHistorysInfo info = obj.data;
        List<OrderHistorysInfo.ListBean> list = info.getList();

        int size = list.size();
        int count = 0;
        float total_discount = 0f;
        float discount = 0f;
        for (int i = 0; i < size; i++) {
            OrderHistorysInfo.ListBean data = list.get(i);

            int type = data.getType();
            count += data.getQuantity();
            discount = Float.valueOf(data.getDiscount());
            total_discount += discount;

            esc.addText(String.valueOf(i+1));
            esc.addText(".");
            esc.addText(data.getG_sku_name());
            esc.addText("   \n");
            esc.addText("     ");
            esc.addText(data.getSell_price());
            esc.addText("   ");
            esc.addText(data.getDiscount());
            esc.addText("   ");
            esc.addText(type == 1 ? data.getCount() + data.getG_u_symbol() : String.valueOf(data.getCount()));
            esc.addText("   ");
            esc.addText(data.getMoney() + "\n");
        }

        DecimalFormat df = new DecimalFormat("0.00");

        esc.addText("--------------------------------\n");
        esc.addText("总数量：");
        esc.addText(String.valueOf(count) + "\n");
        esc.addText("原价：");
        esc.addText(info.getTotal_money() + "元\n");
        esc.addText("优惠：");
        esc.addText(df.format(total_discount) + "元\n");
        esc.addText("总金额：");
        esc.addText(info.getReal_pay() + "元\n");
        esc.addText("支付方式：");
        esc.addText(obj.pay_type + "\n");
        esc.addText("实收：");
        esc.addText(info.getBuyer_pay() + "元\n");
        esc.addText("找零：");
        esc.addText(info.getChange_money() + "元\n");
        esc.addText("退款：");
        esc.addText(info.getRefund_fee() != null ?
                ((String) info.getRefund_fee()) : "0.00" + "元\n");

        esc.addPrintAndFeedLines((byte) 3);

        return esc.getCommand();
    }

    /**
     * 历史订单退款
     */
    public static Vector<Byte> orderHistroyRefund(OrderHistoryRefundPrintObj obj) {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 2);
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        // 打印文字
        esc.addText("退款单\n");
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);

        // 打印文字
        esc.addText("订单号:");
        esc.addText(obj.order_no + "\n");
        esc.addText("收银员：");
        esc.addText(obj.admin_name + "\n");
        esc.addText("时间:");
        esc.addText(obj.time + "\n");
        esc.addText("--------------------------------\n");
        esc.addText("品名  单价  优惠  数量/重量  小计  \n");

        ArrayList<GoodsRefundInfo> info = obj.data;
        int size = info.size();
        for (int i = 0; i < size; i++) {
            GoodsRefundInfo data = info.get(i);

            boolean select = data.isSelect();
            if (!select) {
                continue;
            }

            esc.addText(String.valueOf(i+1));
            esc.addText(".");
            esc.addText(data.getProduct_name());
            esc.addText("   \n");
            esc.addText("      ");
            esc.addText(data.getProduct_price());
            esc.addText("   ");
            esc.addText(data.getProduct_preferential());
            esc.addText("    ");
            esc.addText(data.getRefund_num());
            esc.addText(data.getG_u_symbol());
            esc.addText("   ");
            esc.addText(data.getRefund_subtotal());
            esc.addText("   \n");
        }
        esc.addText("总数量：");
        esc.addText(obj.count + "\n");
        esc.addText("原价：");
        esc.addText(obj.total_price + "元\n");
        esc.addText("优惠：");
        esc.addText(obj.discount + "元\n");
        esc.addText("总金额：");
        esc.addText(obj.real_pay + "元\n");
        esc.addText("退款方式：");
        esc.addText(obj.pay_type + "\n");
        esc.addText("退款金额：");
        esc.addText(obj.refund + "元\n");

        esc.addPrintAndFeedLines((byte) 3);

        return esc.getCommand();
    }



    /**
     * 交接班信息
     */
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
        esc.addPrintAndFeedLines((byte) 3);

        return esc.getCommand();
    }
    /**
     * 交接班销售列表
     */
    public static Vector<Byte> handoverSaleListDatas(HandoverSaleListPrintObj obj) {
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 2);
        // 设置打印居中
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        // 设置为倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        // 打印文字
        esc.addText(obj.shop_name + "\n");
        esc.addPrintAndLineFeed();

        /* 打印文字 */
        // 取消倍高倍宽
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        // 设置打印左对齐
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);

        // 打印文字
        esc.addText("时间:");
        esc.addText(obj.time + "\n");
        esc.addText("收银员：");
        esc.addText(obj.admin_name + "\n");
        esc.addText("--------------------------------\n");
        esc.addText("品名  分类  单价  数量/重量  小计  \n");


        DecimalFormat df = new DecimalFormat("0.00");
        int size = obj.data.size();
        int count = 0;
        float total_money = 0;
        for (int i = 0; i < size; i++) {
            HandoverSaleResponse saleResponse = obj.data.get(i);
            count += Integer.valueOf(saleResponse.getQuantity());
            total_money += saleResponse.getMoney();

            esc.addText(String.valueOf(i+1));
            esc.addText(".");
            esc.addText(saleResponse.getG_sku_name());
            esc.addText("   \n");
            esc.addText(saleResponse.getG_c_name());
            esc.addText("            ");
            esc.addText(saleResponse.getSell_price());
            esc.addText("   ");
            esc.addText(String.valueOf(saleResponse.getCount()));
            esc.addText(saleResponse.getType() == GoodsResponse.type_weight? saleResponse.getG_u_symbol(): "");
            esc.addText("   ");
            esc.addText(df.format(saleResponse.getMoney()) + "\n");

        }
        esc.addText("--------------------------------\n");
        esc.addText("总数量：");
        esc.addText(String.valueOf(count) + "\n");
        esc.addText("总金额：");
        esc.addText(df.format(total_money) + "\n");

        esc.addPrintAndFeedLines((byte) 3);

        return esc.getCommand();
    }
    
    
}

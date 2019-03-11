package com.easygo.cashier.module.handover;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.Configs;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppMvpActivity;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.HandoverResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.easygo.cashier.module.login.LoginActivity;
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.printer.PrinterHelpter;
import com.easygo.cashier.printer.PrinterUtils;
import com.easygo.cashier.printer.ThreadPool;
import com.easygo.cashier.printer.obj.HandoverInfoPrintObj;
import com.easygo.cashier.printer.obj.HandoverSaleListPrintObj;
import com.easygo.cashier.widget.MyTitleBar;
import com.niubility.library.constants.Constans;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.ScreenUtils;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 交接班页面
 */
@Route(path = ModulePath.handover)
public class HandoverActivity extends BaseAppMvpActivity<HandoverContract.IView, HandoverPresenter> implements HandoverContract.IView {

    private static final String TAG = "HandoverActivity";
    @BindView(R.id.cl_title)
    MyTitleBar clTitle;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.tv_text_login_time)
    TextView tvTextLoginTime;
    @BindView(R.id.tv_login_time)
    TextView tvLoginTime;
    @BindView(R.id.cb_print)
    CheckBox cbPrint;

    @Autowired(name = "admin_name")
    String admin_name;
    @BindView(R.id.btn_handover)
    Button btnHandover;
    @BindView(R.id.btn_sales_list)
    Button btnSalesList;
    @BindView(R.id.btn_print)
    Button btnPrint;

    private HandoverView mHandoverView;
    private HandoverSaleListView mHandoverSaleListView;
    private int handover_id;

    private boolean during_handover;


    @Override
    protected HandoverPresenter createPresenter() {
        return new HandoverPresenter();
    }

    @Override
    protected HandoverContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_handover;
    }

    @Override
    public void init() {
        ARouter.getInstance().inject(this);

        handoverPermission();

        clTitle.setCashierAccount(admin_name);

        if (mHandoverView == null) {
            mHandoverView = HandoverView.create(this);
            framelayout.addView(mHandoverView);
        }


        //获取交接班信息
        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getApplicationContext());
        handover_id = sp.getInt(Constans.KEY_HANDOVER_ID, -1);

        mPresenter.handover(handover_id);

//        cbPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked) {
//                    printHandoverInfo();
//                }
//            }
//        });

    }

    private void handoverPermission() {
        if (Configs.getRole(Configs.menus[8]) == 0){
            btnHandover.setVisibility(View.GONE);
        }
        if (Configs.getRole(Configs.menus[9]) == 0){
            btnSalesList.setVisibility(View.GONE);
        }
    }


    @Override
    public void handoverSuccess(HandoverResponse result) {

        try {
            mHandoverView.setData(result);
        } catch (Exception e) {
            e.printStackTrace();
            showToast("错误, 可能带有null对象");
        }
        tvLoginTime.setText(result.getStart_time());

    }

    @Override
    public void handoverFailed(Map<String, Object> map) {

        if (HttpExceptionEngine.isBussinessError(map)) {

            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);

            showToast("错误信息:" + err_msg);
        }


    }

    @Override
    public void loginoutSuccess(String result) {

        //弹出钱箱
        mPresenter.print_info(Configs.shop_sn, Configs.printer_sn, PrintHelper.pop_till);
        PrinterUtils.getInstance().popTill();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clearLoginInfo();
                showToast("交接成功");
                during_handover = false;
                Intent intent = new Intent(HandoverActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }, 500);


    }

    private void clearLoginInfo() {
        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        //登录状态中，清除 session_id 、 admin_name
        if (sp.contains(Constans.KEY_SESSION_ID)) {
            editor.remove(Constans.KEY_SESSION_ID).apply();
        }
        if (sp.contains(Constans.KEY_ADMIN_NAME)) {
            editor.remove(Constans.KEY_ADMIN_NAME).apply();
        }
        if (sp.contains(Constans.KEY_SHOP_SN)) {
            editor.remove(Constans.KEY_SHOP_SN).apply();
        }
        if (sp.contains(Constans.KEY_SHOP_NAME)) {
            editor.remove(Constans.KEY_SHOP_NAME).apply();
        }
        if (sp.contains(Constans.KEY_TIME)) {
            editor.remove(Constans.KEY_TIME).apply();
        }
        if (sp.contains(Constans.KEY_HANDOVER_ID)) {
            editor.remove(Constans.KEY_HANDOVER_ID).apply();
        }
    }

    @Override
    public void loginoutFailed(Map<String, Object> map) {
        during_handover = false;
//        if (HttpExceptionEngine.isBussinessError(map)) {
//
//            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
//
//            showToast("错误信息:" + err_msg);
//        }
        String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);

        showToast("错误信息:" + err_msg);
    }

    @Override
    public void saleListSuccess(List<HandoverSaleResponse> result) {
        if (mHandoverSaleListView != null) {
            mHandoverSaleListView.setData(result);
        }
    }

    @Override
    public void saleListFailed(Map<String, Object> map) {
        if (HttpExceptionEngine.isBussinessError(map)) {

            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);

            showToast("错误信息:" + err_msg);
        }
    }

    @Override
    public void printSuccess(String result) {

    }

    @Override
    public void printFailed(Map<String, Object> map) {

    }

    @OnClick({R.id.btn_handover, R.id.btn_sales_list, R.id.cl_back, R.id.btn_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_handover:

                if(cbPrint.isChecked()) {
                    printHandoverInfo();
                    printHandoverInfoLocal();
                }
                during_handover = true;
                mPresenter.loginout(handover_id);


                break;
            case R.id.btn_sales_list:
                if (mHandoverSaleListView == null) {
                    mHandoverSaleListView = HandoverSaleListView.create(this);
                    framelayout.addView(mHandoverSaleListView);
                }
                if (mHandoverView != null) {
                    mHandoverView.setVisibility(View.GONE);
                }
                if (mHandoverSaleListView != null) {
                    mHandoverSaleListView.setVisibility(View.VISIBLE);
                }
                setBottomLayout(2);

                mPresenter.sale_list(handover_id);
                break;
            case R.id.btn_print://打印销售列表
                printHandoverSaleList();
                printHandoverSaleListLocal();
                break;
            case R.id.cl_back:
                onBack();
                break;
        }
    }

    private void printHandoverInfo() {

        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);



        sb.append(PrintHelper.CB_left).append("交接班单据").append(PrintHelper.CB_right).append(PrintHelper.BR)
                .append("收银员：").append(admin_name).append(PrintHelper.BR)
                .append("登录时间：").append(tvLoginTime.getText().toString()).append(PrintHelper.BR)
                .append("登出时间：").append(sdf.format(new Date())).append(PrintHelper.BR)
                .append("--------------------------------").append(PrintHelper.BR)
                .append("总单据数：")
                    .append(mHandoverView.tvTotalOrderCount.getText())
                    .append(PrintHelper.BR)
                .append("销售单：")
                    .append(mHandoverView.tvSaleCount.getText())
                    .append(PrintHelper.BR)
                .append("退货单：")
                    .append(mHandoverView.tvRefundCount.getText())
                    .append(PrintHelper.BR)
                .append("--------------------------------").append(PrintHelper.BR)
                .append("总销售额：")
                    .append(mHandoverView.tvTotalSales.getText())
                    .append(PrintHelper.BR)
                .append("现金：")
                    .append(mHandoverView.tvCash.getText())
                    .append(PrintHelper.BR)
                .append("支付宝：")
                    .append(mHandoverView.tvAlipay.getText())
                    .append(PrintHelper.BR)
                .append("微信：")
                    .append(mHandoverView.tvWechat.getText())
                    .append(PrintHelper.BR)
                .append("总退款金额：")
                    .append(mHandoverView.tvAllRefund.getText())
                    .append(PrintHelper.BR)
                .append("--------------------------------").append(PrintHelper.BR)
                .append("总现金数：")
                    .append(mHandoverView.tvTotalCash.getText())
                    .append(PrintHelper.BR)
                .append("现金收入：")
                    .append(mHandoverView.tvCashIncome.getText())
                    .append(PrintHelper.BR)
                .append("实收金额：")
                    .append(mHandoverView.tvReceipts.getText())
                    .append(PrintHelper.BR)
                .append("找零金额：")
                    .append(mHandoverView.tvChange.getText())
                    .append(PrintHelper.BR)
                .append("退款现金：")
                    .append(mHandoverView.tvCashRefund.getText())
                    .append(PrintHelper.BR);


        Log.i(TAG, "printHandoverInfo: sb -> " + sb.toString());

        mPresenter.print_info(Configs.shop_sn, Configs.printer_sn, sb.toString());
    }

    private void printHandoverInfoLocal() {

        HandoverInfoPrintObj handoverInfoPrintObj = new HandoverInfoPrintObj();
        handoverInfoPrintObj.admin_name = admin_name;
        handoverInfoPrintObj.login_time = tvLoginTime.getText().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        handoverInfoPrintObj.loginout_time = sdf.format(new Date());
        handoverInfoPrintObj.total_order_count = mHandoverView.tvTotalOrderCount.getText().toString();
        handoverInfoPrintObj.sale_count = mHandoverView.tvSaleCount.getText().toString();
        handoverInfoPrintObj.refund_count = mHandoverView.tvRefundCount.getText().toString();
        handoverInfoPrintObj.total_sales = mHandoverView.tvTotalSales.getText().toString();
        handoverInfoPrintObj.cash = mHandoverView.tvCash.getText().toString();
        handoverInfoPrintObj.alipay = mHandoverView.tvAlipay.getText().toString();
        handoverInfoPrintObj.wechat = mHandoverView.tvWechat.getText().toString();
        handoverInfoPrintObj.all_refund = mHandoverView.tvAllRefund.getText().toString();
        handoverInfoPrintObj.total_cash = mHandoverView.tvTotalCash.getText().toString();
        handoverInfoPrintObj.cash_income = mHandoverView.tvCashIncome.getText().toString();
        handoverInfoPrintObj.receipts = mHandoverView.tvReceipts.getText().toString();
        handoverInfoPrintObj.change = mHandoverView.tvChange.getText().toString();
        handoverInfoPrintObj.cash_refund = mHandoverView.tvCashRefund.getText().toString();

        PrinterUtils.getInstance().print(PrinterHelpter.handoverInfoDatas(handoverInfoPrintObj));
    }


    public void printHandoverSaleList() {

        List<HandoverSaleResponse> data = null;
        if (mHandoverSaleListView != null) {
            data = mHandoverSaleListView.getData();
        }
        if(data == null) {
            Log.i(TAG, "printHandoverSaleList: data = null");
            return;
        }

        StringBuilder sb = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        DecimalFormat df = new DecimalFormat("0.00");

        sb.append(PrintHelper.CB_left).append(Configs.shop_name).append(PrintHelper.CB_right).append(PrintHelper.BR)
                .append("时间：").append(sdf.format(new Date())).append(PrintHelper.BR)
                .append("收银员：").append(admin_name).append(PrintHelper.BR)
                .append("--------------------------------").append(PrintHelper.BR)
                .append("品名  ").append("分类  ").append("单价  ").append("数量/重量  ").append("小计  ")
                .append(PrintHelper.BR);

        int size = data.size();
        int count = 0;
        float total_money = 0;
        for (int i = 0; i < size; i++) {
            HandoverSaleResponse saleResponse = data.get(i);
            count += Integer.valueOf(saleResponse.getQuantity());
            total_money += saleResponse.getMoney();

            sb.append(i+1).append(".")
            .append(saleResponse.getG_sku_name()).append("   ").append(PrintHelper.BR)
            .append(saleResponse.getG_c_name()).append("   ").append(PrintHelper.BR)
            .append("            ")
            .append(saleResponse.getSell_price()).append("   ")
            .append(saleResponse.getCount()).append(saleResponse.getType() == GoodsResponse.type_weight? saleResponse.getG_u_symbol(): "").append("   ")
            .append(df.format(saleResponse.getMoney())).append(PrintHelper.BR);
        }
        sb.append("--------------------------------").append(PrintHelper.BR)
        .append("总数量：").append(count).append(PrintHelper.BR)
        .append("总金额：").append(df.format(total_money)).append(PrintHelper.BR);


        Log.i(TAG, "printHandoverSaleList: sb -> " + sb.toString());

        mPresenter.print_info(Configs.shop_sn, Configs.printer_sn, sb.toString());


    }
    private void printHandoverSaleListLocal() {

        List<HandoverSaleResponse> data = null;
        if (mHandoverSaleListView != null) {
            data = mHandoverSaleListView.getData();
        }
        if(data == null) {
            Log.i(TAG, "printHandoverSaleList: data = null");
            return;
        }

        HandoverSaleListPrintObj obj = new HandoverSaleListPrintObj();
        obj.shop_name = Configs.shop_name;
        obj.admin_name = admin_name;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        obj.time = sdf.format(new Date());
        obj.data = data;

        PrinterUtils.getInstance().print(PrinterHelpter.handoverSaleListDatas(obj));
    }



    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

            onBack();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 返回， 销售列表显示时先关闭，否则退出交接班页面
     */
    public void onBack() {
        if (mHandoverSaleListView != null && mHandoverSaleListView.isShown()) {
            mHandoverSaleListView.setVisibility(View.GONE);
            if (mHandoverView != null) {
                mHandoverView.setVisibility(View.VISIBLE);
            }
            setBottomLayout(1);
        } else if (mHandoverView != null && mHandoverView.isShown()) {
            finish();
        }

    }

    public void setBottomLayout(int status) {

        switch (status) {
            case 1:
                btnSalesList.setVisibility(View.VISIBLE);
                btnHandover.setVisibility(View.VISIBLE);
                tvTextLoginTime.setVisibility(View.VISIBLE);
                tvLoginTime.setVisibility(View.VISIBLE);
                btnPrint.setVisibility(View.GONE);
                break;
            case 2:
                btnSalesList.setVisibility(View.GONE);
                btnHandover.setVisibility(View.GONE);
                tvTextLoginTime.setVisibility(View.GONE);
                tvLoginTime.setVisibility(View.GONE);
                btnPrint.setVisibility(View.VISIBLE);
                break;
        }
        handoverPermission();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ScreenUtils.hideNavigationBar(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(during_handover) {
            showToast("交接中...");
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandoverView != null) {
            mHandoverView.release();
        }
        if (mHandoverSaleListView != null) {
            mHandoverSaleListView.release();
        }
    }

}

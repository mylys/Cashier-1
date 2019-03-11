package com.easygo.cashier.module.goods;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.Configs;
import com.easygo.cashier.Events;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.Test;
import com.easygo.cashier.adapter.GoodsEntity;
import com.easygo.cashier.base.BaseAppMvpActivity;
import com.easygo.cashier.bean.EntryOrders;
import com.easygo.cashier.bean.EquipmentState;
import com.easygo.cashier.bean.GoodsResponse;
import com.easygo.cashier.bean.MemberInfo;
import com.easygo.cashier.bean.PrinterStatusResponse;
import com.easygo.cashier.module.login.LoginActivity;
import com.easygo.cashier.module.status.StatusContract;
import com.easygo.cashier.module.status.StatusPresenter;
import com.easygo.cashier.printer.PrinterUtils;
import com.easygo.cashier.widget.EquipmentstateDialog;
import com.easygo.cashier.widget.FunctionListDialog;
import com.easygo.cashier.widget.MyTitleBar;
import com.niubility.library.base.BaseApplication;
import com.niubility.library.base.BaseEvent;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.constants.Constans;
import com.niubility.library.utils.NetworkUtils;
import com.niubility.library.utils.ScreenUtils;
import com.niubility.library.utils.SharedPreferencesUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 */
@Route(path = ModulePath.goods)
public class MainActivity extends BaseAppMvpActivity<StatusContract.IView, StatusPresenter> implements StatusContract.IView {

    public static final String TAG = "MainActivity";

    private final String TAG_MAIN = "tag_main";
    private final String TAG_FUNCTION_LIST = "tag_function_list";
    @BindView(R.id.cl_title)
    MyTitleBar myTitleBar;
    @BindView(R.id.tv_cashier_account)
    TextView tvCashierAcount;
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.network)
    ImageView network;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.tv_cashier)
    TextView tvCashier;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    private Fragment fragment;
    private GoodsFragment goodsFragment;
    private EquipmentstateDialog dialog;

    @Autowired(name = "admin_name")
    String admin_name;

    private ExecutorService cachedThreadPool;

    public static final int MSG_NETWORK = 0;
    public static final int MSG_RED_POINT = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            removeMessages(what);

            switch (what) {
                case MSG_NETWORK:
                    judgeNetworkAvalible();
                    break;
                case MSG_RED_POINT:
                    myTitleBar.setRedPointVisibility(Beta.getUpgradeInfo() != null);
                    break;
            }
        }
    };

    @Override
    protected StatusPresenter createPresenter() {
        return new StatusPresenter();
    }

    @Override
    protected StatusContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        ARouter.getInstance().inject(this);

        if (Configs.getRole(Configs.menus[13]) == 0) {
            myTitleBar.setPopTillVisibility(false);
        }
        tvCashierAcount.setText("收银员: " + admin_name);

        Test.detectInputDeviceWithShell();
        mPresenter.printerStatus(Configs.shop_sn, Configs.printer_sn);


        initReceiver();
        mHandler.sendEmptyMessageDelayed(MSG_RED_POINT, 1000);

        initPrinter();
    }

    private void initPrinter() {

        PrinterUtils.getInstance().setOnPrinterListener(new PrinterUtils.OnPrinterListener() {
            @Override
            public void onUsbPermissionDeny() {
                showToast("USB权限 被拒绝！！！");
            }

            @Override
            public void onConnecting() {
                showToast(getString(R.string.str_conn_state_connecting));
            }

            @Override
            public void onConnected() {
                showToast(getString(R.string.str_conn_state_connected) +
                        "\n" + PrinterUtils.getInstance().getConnDeviceInfo());

//                PrinterUtils.getInstance().popTill();
//                btnReceiptPrint();

//                btnPrinterState();
            }

            @Override
            public void onDisconnected() {
                showToast(getString(R.string.str_conn_state_disconnect));
            }

            @Override
            public void onPleaseConnectPrinter() {
                showToast(getString(R.string.str_cann_printer));
            }

            @Override
            public void onConnectFailed() {
                showToast(getString(R.string.str_conn_fail));
            }

            @Override
            public void onCommandError() {
                showToast(getString(R.string.str_choice_printer_command));
            }
        });

        PrinterUtils.getInstance().registerReceiver(this);
    }

    private void initReceiver() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connReceiver, intentFilter);
    }

    private BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                //ping
                mHandler.sendEmptyMessageDelayed(MSG_NETWORK, 1000);
            }
        }
    };

    /**
     * 判断网络是否连通
     */
    private void judgeNetworkAvalible() {
        if (cachedThreadPool == null) {
            cachedThreadPool = Executors.newCachedThreadPool();
        }

        cachedThreadPool.execute(networkRunnable);
    }

    private NetworkRunnable networkRunnable = new NetworkRunnable();

    private class NetworkRunnable implements Runnable {

        @Override
        public void run() {
            final boolean networkOnline = NetworkUtils.isNetworkOnline();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myTitleBar.setOnline(networkOnline);
                    mHandler.sendEmptyMessageDelayed(MSG_NETWORK, 60 * 1000);
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ScreenUtils.hideNavigationBar(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment = getSupportFragmentManager().findFragmentByTag(TAG_MAIN);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(GoodsFragment.KEY_ADMIN_NAME, admin_name);
            goodsFragment = GoodsFragment.newInstance(bundle);

            transaction.replace(R.id.framelayout, goodsFragment, TAG_MAIN);
        }
        transaction.commit();
    }

    @OnClick({R.id.setting, R.id.pop_till, R.id.network, R.id.menu, R.id.update, R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting://设置

                break;
            case R.id.menu://功能列表
//                if (goodsFragment.getAdapterSize() != 0) {
//                    ToastUtils.showToast(this, "请先完成收银操作");
//                    return;
//                }
                FunctionListDialog functionListDialog = new FunctionListDialog();
                functionListDialog.setOnFunctionListItemListener(mFunctionListItemListener);
//                functionListDialog.show(getSupportFragmentManager(), TAG_FUNCTION_LIST);
                functionListDialog.showCenter(this);
                break;
            case R.id.pop_till://弹出钱箱
                goodsFragment.clickPopTill();
                break;
            case R.id.network://网络在线或离线
                //显示打印机状态
                checkPrinterStatus();

                if(PrinterUtils.STATE_DISCONNECTED == PrinterUtils.getInstance().getPrinterState()) {
                    showToast(getString(R.string.str_cann_printer));
                };
                break;
            case R.id.update://更新
                checkUpdate();
                break;
            case R.id.cl_back://返回
                break;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                v.clearFocus();

            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public FunctionListDialog.OnFunctionListItemListener mFunctionListItemListener = new FunctionListDialog.OnFunctionListItemListener() {
        @Override
        public void orderHistory() {
            ARouter.getInstance()
                    .build(ModulePath.order_history)
                    .withString("admin_name", admin_name)
                    .navigation();
        }

        @Override
        public void refund() {
            ARouter.getInstance()
                    .build(ModulePath.refund)
                    .withString("admin_name", admin_name)
                    .navigation();
        }

        @Override
        public void handover() {

            //跳转交接班页面
            ARouter.getInstance()
                    .build(ModulePath.handover)
                    .withString("admin_name", admin_name)
                    .navigation();

            if (true)
                return;


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
            if (sp.contains(Constans.KEY_TIME)) {
                editor.remove(Constans.KEY_TIME).apply();
            }
            if (sp.contains(Constans.KEY_HANDOVER_ID)) {
                editor.remove(Constans.KEY_HANDOVER_ID).apply();
            }
            //跳转登录页
//            ARouter.getInstance()
//                    .create(ModulePath.login)
//                    .withFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    .navigation();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        @Override
        public void enterSystem() {

        }

        @Override
        public void entryOrders() {
            SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication);
            if (TextUtils.isEmpty(sp.getString(Constans.KEY_ENTRY_ORDERS_LIST, ""))) {
                showToast("暂无挂单信息");
                return;
            }
//            SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication).edit();
//            editor.remove(Constans.KEY_ENTRY_ORDERS_LIST).apply();
            ARouter.getInstance()
                    .build(ModulePath.entry_orders)
                    .navigation();
        }

        @Override
        public void languageSetting() {

        }

        @Override
        public void deviceStatus() {
            checkPrinterStatus();
        }

        @Override
        public void systemSetting() {

        }

        @Override
        public void onItemClickBefore() {

        }

        @Override
        public void onItemClickAfter() {

        }

        @Override
        public void lockCashier() {
            goodsFragment.lockCashier();
        }
    };

    /**
     * 检查打印机在线状态
     */
    private void checkPrinterStatus() {
        mPresenter.printerStatus(Configs.shop_sn, Configs.printer_sn);
        getPrinterstateShowLoading();
    }
    /**
     * 检查更新
     */
    private void checkUpdate() {
        Beta.checkUpgrade();

        mHandler.removeMessages(MSG_RED_POINT);
        mHandler.sendEmptyMessageDelayed(MSG_RED_POINT, 2000);
    }



    private void getPrinterstateShowLoading() {
        Bundle bundle = new Bundle();
        ArrayList<EquipmentState> arrayList = new ArrayList<>();
        arrayList.add(new EquipmentState(getResources().getString(R.string.the_printer), true, true));
//        arrayList.add(new EquipmentState(getResources().getString(R.string.the_code_gun), true, true));
//        arrayList.add(new EquipmentState(getResources().getString(R.string.the_till), true, true));
        bundle.putString("title", getResources().getString(R.string.device_state));
        bundle.putParcelableArrayList("data", arrayList);
        dialog = EquipmentstateDialog.getInstance(bundle);
        dialog.showCenter(MainActivity.this);
    }

    @Override
    public void printerStatusSuccess(PrinterStatusResponse result) {
        boolean is_printer_normal = result.getOnline() == 1;
        if (dialog != null && dialog.isShow()) {
            dialog.setNewData(0, getResources().getString(R.string.the_printer), is_printer_normal);
            return;
        }
        showToast("打印机: " + (is_printer_normal?"在线":"离线"));
    }

    @Override
    public void printerStatusFailed(Map<String, Object> map) {
        if (dialog != null && dialog.isShow()) {
            dialog.setNewData(0, getResources().getString(R.string.the_printer), false);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null && dialog.isShow()) {
            dialog.dismiss();
        }

        if(connReceiver != null) {
            unregisterReceiver(connReceiver);
        }
        PrinterUtils.getInstance().unregisterReceiver(this);
        PrinterUtils.getInstance().release();
    }

    @Override
    public void onEvent(BaseEvent event) {
        super.onEvent(event);
        switch (event.getString()) {
            case Events.ENTRY_ORDERS_VALUE://挂单
                EntryOrders orders = (EntryOrders) event.getObject();
                goodsFragment.addData(orders);
                break;
            case Events.CLEAR_GOODS_INFO://清除商品数据
                goodsFragment.clearInfo();
                break;
            case Events.MEMBER_INFO://设置会员信息
                MemberInfo info = (MemberInfo) event.getObject();
                goodsFragment.updateMebmerInfo(info);
                break;
            case Events.QUICK_CHOOSE://快速选择
                List<GoodsResponse> goodsResponses = (List<GoodsResponse>) event.getObject();
                goodsFragment.addChooseInfo(goodsResponses);
                break;
            case Events.REFRESH_DATA://刷新首页商品数据
                List<GoodsEntity<GoodsResponse>> data = (List<GoodsEntity<GoodsResponse>>) event.getObject();
                goodsFragment.refreshGoodsData(data);
                break;
        }
    }
}

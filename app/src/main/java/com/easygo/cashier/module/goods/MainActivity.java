package com.easygo.cashier.module.goods;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.Configs;
import com.easygo.cashier.Constants;
import com.easygo.cashier.Events;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.Msg;
import com.easygo.cashier.MyApplication;
import com.easygo.cashier.R;
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
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.printer.ZQPrint.ZQEBUtil;
import com.easygo.cashier.printer.local.PrinterUtils;
import com.easygo.cashier.widget.dialog.EquipmentstateDialog;
import com.easygo.cashier.widget.dialog.FunctionListDialog;
import com.easygo.cashier.widget.dialog.GeneraDialog;
import com.easygo.cashier.widget.view.MyTitleBar;
import com.niubility.library.base.BaseApplication;
import com.niubility.library.base.BaseEvent;
import com.niubility.library.base.BaseHandler;
import com.niubility.library.utils.NetworkUtils;
import com.niubility.library.utils.SharedPreferencesUtils;
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
    public GoodsFragment goodsFragment;
    private EquipmentstateDialog dialog;

    @Autowired(name = "admin_name")
    String admin_name;

    private ExecutorService cachedThreadPool;

    private MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends BaseHandler<MainActivity> {

        public MyHandler(MainActivity activity) {
            super(activity);
        }

        @Override
        public void onHandleMessage(Message msg, MainActivity activity) {
            switch (msg.what) {
                case Msg.MSG_NETWORK:
                    activity.judgeNetworkAvalible();
                    break;
                case Msg.MSG_RED_POINT:
                    activity.myTitleBar.setRedPointVisibility(Beta.getUpgradeInfo() != null);
                    break;
            }
        }
    }

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
        if (!Configs.isOnlineMode()) {
            myTitleBar.setOfflineModeVisibility(true);
        }
        tvCashierAcount.setText("收银员: " + admin_name);

        mPresenter.printerStatus(Configs.shop_sn, Configs.printer_sn);


        initReceiver();
        mHandler.sendEmptyMessageDelayed(Msg.MSG_RED_POINT, 1000);

        initLocalPrinter();
    }

    private void initLocalPrinter() {

        PrinterUtils.getInstance().setOnPrinterListener(new PrinterUtils.OnPrinterListener() {
            @Override
            public void onUsbPermissionDeny() {
                showToast(getString(R.string.str_permission_deny));
            }

            @Override
            public void onConnecting() {
//                showToast(getString(R.string.str_conn_state_connecting));
            }

            @Override
            public void onConnected(boolean showTip) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.setNewData(0, getString(R.string.local_printer), true);
//                    return;
                }
//                if(showTip){
//                    showToast(PrinterUtils.getInstance().getConnDeviceInfo() + " " + getString(R.string.str_conn_state_connected));
//                }
            }

            @Override
            public void onDisconnected() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.setNewData(0, getString(R.string.local_printer), false);
//                    return;
                }
//                showToast(getString(R.string.str_conn_state_disconnect));
            }

            @Override
            public void onPleaseConnectPrinter() {
                if (dialog != null && dialog.isShowing()) {
                    showToast(getString(R.string.str_cann_printer));
                }
            }

            @Override
            public void onConnectFailed() {
                if (dialog != null && dialog.isShowing()) {
                    showToast(getString(R.string.str_conn_fail));
                }
            }

            @Override
            public void onConnectError(int error, String content) {
                if (dialog != null && dialog.isShowing()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.setErrorData(0, getResources().getString(R.string.local_printer), getString(R.string.device_abnormal));
                    }
                }
            }

            @Override
            public void onCommandError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("USB打印异常");
//                        showToast(getString(R.string.str_choice_printer_command));
                    }
                });
            }

            @Override
            public void onNeedReplugged() {
                if (dialog != null && dialog.isShowing()) {
                    showToast(getString(R.string.str_need_replugged));
                }
            }
        });

        PrinterUtils.getInstance().registerReceiver(this);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                PrinterUtils.getInstance().connectUSB(MainActivity.this);
            }
        }, 1000);
    }

    private void initReceiver() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Events.CLEAR_GOODS_INFO);
        intentFilter.addAction(Events.ADD_GOODS);
        registerReceiver(connReceiver, intentFilter);
    }

    private BroadcastReceiver connReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ConnectivityManager.CONNECTIVITY_ACTION:
                        //ping
                        mHandler.sendEmptyMessageDelayed(Msg.MSG_NETWORK, 1000);
                        break;
                    case Events.CLEAR_GOODS_INFO://清除商品数据
                        if (goodsFragment != null) {
                            goodsFragment.clearInfo();
                        }
                        break;
                    case Events.ADD_GOODS://添加称重商品
                        if (intent.getExtras() != null) {
                            int weight = intent.getExtras().getInt("WEIGHT");
                            String barcode = intent.getExtras().getString("BARCODE");
                            if (goodsFragment != null) {
                                goodsFragment.addGoods(weight, barcode);
                            }
                        }
                        break;
                }
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
                    mHandler.sendEmptyMessageDelayed(Msg.MSG_NETWORK, 60 * 1000);
//                    Configs.current_mode = networkOnline? Configs.mode_online: Configs.mode_offline;
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: goodFragment - " + goodsFragment);
        ZQEBUtil.getInstance().zqebDisconnect();
        fragment = getSupportFragmentManager().findFragmentByTag(TAG_MAIN);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(fragment);
            goodsFragment = (GoodsFragment) fragment;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.KEY_ADMIN_NAME, admin_name);
            goodsFragment = GoodsFragment.newInstance(bundle);

            transaction.replace(R.id.framelayout, goodsFragment, TAG_MAIN);
        }
        transaction.commit();

    }

    @OnClick({R.id.cl_exit, R.id.setting, R.id.pop_till, R.id.network, R.id.menu, R.id.update, R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting://设置

                break;
            case R.id.cl_exit://退出系统 到登录页
                exit();

                break;
            case R.id.menu://功能列表
                FunctionListDialog functionListDialog = new FunctionListDialog();
                functionListDialog.setOnFunctionListItemListener(mFunctionListItemListener);
                functionListDialog.showCenter(this);

                break;
            case R.id.pop_till://弹出钱箱
                goodsFragment.clickPopTill();
                break;
            case R.id.network://网络在线或离线
                //显示打印机状态
                checkPrinterStatus();

                break;
            case R.id.update://更新
                checkUpdate();
                break;
            case R.id.cl_back://返回
                break;
        }
    }

    /**
     * 退出登录 到登录页
     */
    private void exit() {

        GeneraDialog generaDialog = GeneraDialog.getInstance("确认退出到登录页面？", "取消", "确定");
        generaDialog.showCenter(MainActivity.this);
        generaDialog.setOnDialogClickListener(new GeneraDialog.OnDialogClickListener() {
            @Override
            public void onSubmit() {
                //弹出钱箱
                goodsFragment.realPopTill();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }, 500);
            }
        });


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
        public void handover() {

            //跳转交接班页面
            ARouter.getInstance()
                    .build(ModulePath.handover)
                    .withString("admin_name", admin_name)
                    .navigation();
        }

        @Override
        public void enterSystem() {

        }

        @Override
        public void entryOrders() {
            SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication);
            if (TextUtils.isEmpty(sp.getString(Constants.KEY_ENTRY_ORDERS_LIST, ""))) {
                showToast("暂无挂单信息");
                return;
            }
//            SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(BaseApplication.sApplication).edit();
//            editor.remove(Constants.KEY_ENTRY_ORDERS_LIST).apply();
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

        @Override
        public void switchMode(int mode) {
            goodsFragment.switchMode(mode);
        }
    };

    /**
     * 检查打印机在线状态
     */
    private void checkPrinterStatus() {
        getPrinterstateShowLoading();
        mPresenter.printerStatus(Configs.shop_sn, Configs.printer_sn);

        //本地打印机
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PrinterUtils.STATE_DISCONNECTED == PrinterUtils.getInstance().getPrinterState()) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.setErrorData(0, getString(R.string.local_printer), getString(R.string.device_abnormal));
                    }
                }
            }
        }, 500);

    }

    /**
     * 检查更新
     */
    private void checkUpdate() {
        Beta.checkUpgrade();

        mHandler.removeMessages(Msg.MSG_RED_POINT);
        mHandler.sendEmptyMessageDelayed(Msg.MSG_RED_POINT, 2000);
    }


    private void getPrinterstateShowLoading() {

        int printer_count = 1;
        int feie_printer_count = 0;
        if (PrintHelper.printersBeans != null) {
            feie_printer_count = PrintHelper.printersBeans.size();
            printer_count += feie_printer_count;
        }

        Bundle bundle = new Bundle();
        ArrayList<EquipmentState> arrayList = new ArrayList<>();
        arrayList.add(new EquipmentState(getResources().getString(R.string.local_printer), true, true, null));

//        for (int i = 0; i < feie_printer_count; i++) {
//            InitResponse.PrintersBean printersBean = PrintHelper.printersBeans.get(i);

        arrayList.add(new EquipmentState(getString(R.string.the_printer), true, true, null));

//        }
//        arrayList.add(new EquipmentState(getResources().getString(R.string.the_code_gun), true, true));
//        arrayList.add(new EquipmentState(getResources().getString(R.string.the_till), true, true));
        bundle.putString("title", getResources().getString(R.string.device_state));
        bundle.putParcelableArrayList("data", arrayList);
        dialog = EquipmentstateDialog.getInstance(bundle);
        dialog.setCallback(new EquipmentstateDialog.Callback() {
            @Override
            public void onConnectClick() {
                PrinterUtils.getInstance().connectUSB(MyApplication.sApplication);
            }
        });
        dialog.showCenter(MainActivity.this);
    }

    @Override
    public void printerStatusSuccess(PrinterStatusResponse result) {
        boolean is_printer_normal = result.getOnline() == 1;
        if (dialog != null && dialog.isShowing()) {
            dialog.setNewData(getString(R.string.the_printer), is_printer_normal);
            return;
        }
        showToast("打印机: " + (is_printer_normal ? "在线" : "离线"));
    }

    @Override
    public void printerStatusFailed(Map<String, Object> map) {
        if (dialog != null && dialog.isShowing()) {
//            dialog.setNewData(1, Configs.printer_sn, false);
            dialog.setNewData(1, getString(R.string.the_printer), false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (connReceiver != null) {
            unregisterReceiver(connReceiver);
        }
        mHandler.removeCallbacksAndMessages(null);
        PrinterUtils.getInstance().setOnPrinterListener(null);
        PrinterUtils.getInstance().unregisterReceiver(this);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                PrinterUtils.getInstance().release();
//            }
//        }).start();

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
                goodsFragment.updateMemberInfo(info);
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

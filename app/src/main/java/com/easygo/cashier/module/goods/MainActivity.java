package com.easygo.cashier.module.goods;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.easygo.cashier.SoftKeyboardUtil;
import com.easygo.cashier.Test;
import com.easygo.cashier.bean.EntryOrders;
import com.easygo.cashier.bean.EquipmentState;
import com.easygo.cashier.bean.MemberInfo;
import com.easygo.cashier.module.login.LoginActivity;
import com.easygo.cashier.module.secondary_sreen.SecondaryScreen;
import com.easygo.cashier.module.status.StatusContract;
import com.easygo.cashier.module.status.StatusPresenter;
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.widget.EquipmentstateDialog;
import com.easygo.cashier.widget.FunctionListDialog;
import com.google.gson.reflect.TypeToken;
import com.niubility.library.base.BaseApplication;
import com.niubility.library.base.BaseEvent;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.constants.Constans;
import com.niubility.library.utils.GsonUtils;
import com.niubility.library.utils.ScreenUtils;
import com.niubility.library.utils.SharedPreferencesUtils;
import com.niubility.library.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 */
@Route(path = ModulePath.goods)
public class MainActivity extends BaseMvpActivity<StatusContract.IView, StatusPresenter> implements StatusContract.IView {

    public static final String TAG = "MainActivity";

    private final String TAG_MAIN = "tag_main";
    private final String TAG_FUNCTION_LIST = "tag_function_list";
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
    private SecondaryScreen mPresentation = null;

    @Autowired(name = "admin_name")
    String admin_name;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        ButterKnife.bind(this);
//
//        init();
//    }

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

        tvCashierAcount.setText("收银员: " + admin_name);

        Test.detectInputDeviceWithShell();
        mPresenter.printerStatus(Configs.shop_sn, Configs.printer_sn);
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

    @OnClick({R.id.setting, R.id.menu, R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting://设置
                //临时代码
                //屏幕管理类
                DisplayManager mDisplayManager = (DisplayManager) this.getSystemService(Context.DISPLAY_SERVICE);
                Display[] displays = mDisplayManager.getDisplays();

                if (mPresentation == null) {
                    mPresentation = new SecondaryScreen(this, displays[displays.length - 1]);// displays[1]是副屏
                    mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }
                mPresentation.show();

                break;
            case R.id.menu://功能列表
//                if (goodsFragment.getAdapterSize() != 0) {
//                    ToastUtils.showToast(this, "请先完成收银操作");
//                    return;
//                }
                FunctionListDialog functionListDialog = new FunctionListDialog();
                functionListDialog.setOnFunctionListItemListener(mFunctionListItemListener);
                functionListDialog.show(getSupportFragmentManager(), TAG_FUNCTION_LIST);
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
            mPresenter.printerStatus(Configs.shop_sn, Configs.printer_sn);
            getPrinterstateShowLoading();
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
    };

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
    public void printerStatusSuccess(String result) {
        //打印机是否正常
        boolean is_printer_normal = result.equals(PrintHelper.NORMAL);
        if (dialog != null && dialog.isShow()) {
            dialog.setNewData(0, getResources().getString(R.string.the_printer), is_printer_normal);
            return;
        }
        showToast("打印机: " + result);
    }

    @Override
    public void printerStatusFailed(Map<String, Object> map) {
        if (dialog != null && dialog.isShow()) {
            dialog.setNewData(0, getResources().getString(R.string.the_printer), false);
            return;
        }
        showToast("打印机可能连接失败");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (dialog != null && dialog.isShow()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onEvent(BaseEvent event) {
        super.onEvent(event);
        switch (event.getString()) {
            case Events.ENTRY_ORDERS_VALUE:
                EntryOrders orders = (EntryOrders) event.getObject();
                goodsFragment.addData(orders);
                break;
            case Events.CLEAR_GOODS_INFO:
                goodsFragment.clearInfo();
                break;
            case Events.MEMBER_INFO:
                MemberInfo info = (MemberInfo) event.getObject();
                goodsFragment.updateMebmerInfo(info);
                break;
        }
    }
}

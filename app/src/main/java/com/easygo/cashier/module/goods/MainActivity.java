package com.easygo.cashier.module.goods;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
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
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppActivity;
import com.easygo.cashier.module.login.LoginActivity;
import com.easygo.cashier.module.secondary_sreen.SecondaryScreen;
import com.easygo.cashier.widget.FunctionListDialog;
import com.niubility.library.constants.Constans;
import com.niubility.library.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */
@Route(path = ModulePath.goods)
public class MainActivity extends BaseAppActivity {

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

    @Autowired(name = "admin_name")
    String admin_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        init();
    }

    private void init() {

        tvCashierAcount.setText("收银员: " + admin_name);
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

                SecondaryScreen mPresentation = null;
                if (mPresentation == null) {
                    mPresentation = new SecondaryScreen(this, displays[displays.length - 1]);// displays[1]是副屏

                    mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }
                mPresentation.show();

                break;
            case R.id.menu://功能列表
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
    public  boolean isShouldHideInput(View v, MotionEvent event) {
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
        public void shift() {


            SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sp.edit();
            //登录状态中，清除 session_id 、 admin_name
            if(sp.contains(Constans.KEY_SESSION_ID)) {
                editor.remove(Constans.KEY_SESSION_ID).apply();
            }
            if(sp.contains(Constans.KEY_ADMIN_NAME)) {
                editor.remove(Constans.KEY_ADMIN_NAME).apply();
            }
            if(sp.contains(Constans.KEY_SHOP_SN)) {
                editor.remove(Constans.KEY_SHOP_SN).apply();
            }
            if(sp.contains(Constans.KEY_TIME)) {
                editor.remove(Constans.KEY_TIME).apply();
            }
            if(sp.contains(Constans.KEY_HANDOVER_ID)) {
                editor.remove(Constans.KEY_HANDOVER_ID).apply();
            }
            //跳转登录页
//            ARouter.getInstance()
//                    .build(ModulePath.login)
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
        public void languageSetting() {

        }

        @Override
        public void deviceStatus() {

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

}

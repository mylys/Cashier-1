package com.easygo.cashier.module.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.Configs;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.LoginResponse;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.constants.Constans;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.ScreenUtils;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */
@Route(path = ModulePath.login)
public class LoginActivity extends BaseMvpActivity<LoginContract.IView, LoginPresenter> implements LoginContract.IView {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;


    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected LoginContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getApplicationContext());
        String session_id = sp.getString(Constans.KEY_SESSION_ID, "");
        String admin_name = sp.getString(Constans.KEY_ADMIN_NAME, "");
        int handover_id = sp.getInt(Constans.KEY_HANDOVER_ID, -1);
        String shop_sn = sp.getString(Constans.KEY_SHOP_SN, "");
        if(!TextUtils.isEmpty(session_id) && !TextUtils.isEmpty(admin_name)
                && !TextUtils.isEmpty(shop_sn) && handover_id != -1) {
            //登录状态 直接跳转首页

            Configs.shop_sn = shop_sn;

            ARouter.getInstance().build(ModulePath.goods)
                    .withString("admin_name", admin_name)
                    .navigation();
            showToast("登录： " + admin_name);
            Configs.admin_name = admin_name;
            finish();
        } else {

            etAccount.setText(admin_name);
//            etAccount.setText("15017740901");
//            etPassword.setText("123456");
        }
    }

    @OnClick({R.id.btn_login})
    public void onClick() {

//        ARouter.getInstance().build(ModulePath.goods)
//                .withString("admin_name", "xxx")
//                .navigation();
//        finish();
//
//        if(true)
//            return;

        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(TextUtils.isEmpty(account)) {
            showToast("账号不能为空！");
        } else if(TextUtils.isEmpty(password)) {
            showToast("密码不能为空！");
        } else {
            //登录
            mPresenter.login(Configs.shop_sn, account, password);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ScreenUtils.hideNavigationBar(this);
    }


    @Override
    protected void onStart() {
        super.onStart();




        if(TextUtils.isEmpty(Configs.shop_sn)) {

            //获取shop_sn
//            mPresenter.init(DeviceUtils.getMacAddress());
            mPresenter.init("08:ea:40:36:4f:3b");
        }

    }

    @Override
    public void loginSuccess(LoginResponse result) {
        SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(this).edit();
        editor.putString(Constans.KEY_ADMIN_NAME, result.getReal_name())
                .putString(Constans.KEY_SESSION_ID, result.getSession_id())
                .putInt(Constans.KEY_HANDOVER_ID, result.getHandover_id())
                .apply();

        Toast.makeText(this, "登录成功：" + result.getReal_name(), Toast.LENGTH_SHORT).show();

        //跳转首页
        ARouter.getInstance().build(ModulePath.goods)
                .withString("admin_name", result.getReal_name())
                .navigation();
        Configs.admin_name = result.getReal_name();
        finish();
    }

    @Override
    public void loginFailed(Map<String, Object> map) {

        if(HttpExceptionEngine.isBussinessError(map)) {
            String error_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
            showToast(error_msg);
        }

        int errorType = (int) map.get(HttpExceptionEngine.ErrorType);
        int errorCode = (int) map.get(HttpExceptionEngine.ErrorCode);
        String errorMsg = (String) map.get(HttpExceptionEngine.ErrorMsg);
        Log.i(TAG, "loginFailed: map --> errorType:" + errorType
                + ", errorCode: " + errorCode
                + ", errorMsg: " + errorMsg);

    }

    @Override
    public void initSuccess(String result) {
        SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(this).edit();
        editor.putString(Constans.KEY_SHOP_SN, result)
                .apply();

        Configs.shop_sn = result;

    }

    @Override
    public void initFailed(Map<String, Object> map) {
        int errorType = (int) map.get(HttpExceptionEngine.ErrorType);
        int errorCode = (int) map.get(HttpExceptionEngine.ErrorCode);
        String errorMsg = (String) map.get(HttpExceptionEngine.ErrorMsg);
        Log.i(TAG, "loginFailed: map --> errorType:" + errorType
                + ", errorCode: " + errorCode
                + ", errorMsg: " + errorMsg);
    }
}

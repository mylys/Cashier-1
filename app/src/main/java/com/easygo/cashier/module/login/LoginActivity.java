package com.easygo.cashier.module.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.ActivitiesUtils;
import com.easygo.cashier.BuildConfig;
import com.easygo.cashier.Configs;
import com.easygo.cashier.MemberUtils;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.TestUtils;
import com.easygo.cashier.base.BaseAppMvpActivity;
import com.easygo.cashier.bean.AccountInfo;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.module.CouponUtils;
import com.easygo.cashier.module.secondary_sreen.UserGoodsScreen;
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.printer.local.PrinterUtils;
import com.easygo.cashier.widget.AccountWindow;
import com.easygo.cashier.widget.ConfigDialog;
import com.easygo.cashier.widget.PettyCashDialog;
import com.niubility.library.constants.Constans;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.DeviceUtils;
import com.niubility.library.utils.GsonUtils;
import com.niubility.library.utils.ScreenUtils;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */
@Route(path = ModulePath.login)
public class LoginActivity extends BaseAppMvpActivity<LoginContract.IView, LoginPresenter> implements LoginContract.IView {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.constraint_parent)
    ConstraintLayout parent;
    @BindView(R.id.cl_frame)
    ConstraintLayout child;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_system)
    TextView tvSystem;
    private int is_reserve;
    private PettyCashDialog dialog;
    private UserGoodsScreen mUserGoodsScreen;

    private AccountInfo mAccountInfo;
    private final String KEY_ACCOUNTS = "key_accounts";
    private AccountWindow mAccountWindow;

    private ConfigDialog mConfigDialog;
    private TestUtils textUtils = new TestUtils();


    private static final int MSG_GET_SHOP = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_GET_SHOP:
                    removeMessages(MSG_GET_SHOP);
                    mPresenter.init(DeviceUtils.getMacAddress());
//                    mPresenter.init("08:ea:40:36:4f:3b");
//                    mPresenter.init("10:d0:7a:02:b4:b4");

                    break;
            }
        }
    };
    private SharedPreferences sp;


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
        textUtils.addSoftKeyBoardListener(parent, child);

        sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getApplicationContext());
        String session_id = sp.getString(Constans.KEY_SESSION_ID, "");
        String admin_name = sp.getString(Constans.KEY_ADMIN_NAME, "");
        int handover_id = sp.getInt(Constans.KEY_HANDOVER_ID, -1);
        String shop_sn = sp.getString(Constans.KEY_SHOP_SN, "");
        String shop_name = sp.getString(Constans.KEY_SHOP_NAME, "");
        String appkey = sp.getString(Constans.KEY_APPKEY, "");
        String secret = sp.getString(Constans.KEY_SECRET, "");
        String account = sp.getString("account", "");
        etAccount.setText(account);
//            etAccount.setText("15017740901");
//            etPassword.setText("123456");


        initAccount();

        initUserGoodsScreen();

        if(TextUtils.isEmpty(appkey) || TextUtils.isEmpty(secret)) {
            showConfigDialog();
        } else {
            //发送心跳
            mPresenter.heartbeat();
        }

        //长按 收银系统 弹出配置弹窗
        tvSystem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showConfigDialog();
                return true;
            }
        });

        //清除会员、优惠券、促销等信息
        MemberUtils.reset();
        CouponUtils.getInstance().reset();
        ActivitiesUtils.getInstance().reset();


    }

    private void initAccount() {
//        etAccount.setShowSoftInputOnFocus(false);
        mAccountInfo = getSaveAccount();


        etAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAcccountWindow();
            }
        });
        etAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showAcccountWindow();
                }
//                else {
//                    if(mAccountWindow != null && mAccountWindow.isShowing()) {
//                        mAccountWindow.dismiss();
//                    }
//                }
            }
        });
    }

    private void showAcccountWindow() {
        if (mAccountInfo == null) {
            return;
        }
        List<String> accounts = mAccountInfo.getAccounts();
        if (accounts.size() == 0) {
            //没有保存过账号 直接返回
            return;
        }

        if (mAccountWindow == null) {
            mAccountWindow = new AccountWindow(this);
            mAccountWindow.setOutsideTouchable(true);
//            mAccountWindow.setFocusable(true);
            mAccountWindow.setElevation(getResources().getDimensionPixelSize(R.dimen.x20));
            mAccountWindow.setWidth(etAccount.getWidth());
            mAccountWindow.setHeight(-2);
            mAccountWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_search_result_frame));

            mAccountWindow.setOnItemClickListener(new AccountWindow.OnItemClickListener() {
                @Override
                public void onItemClicked(String account) {
                    etAccount.setText(account);
                    mAccountWindow.dismiss();
                    etPassword.requestFocus();
                }
            });
        }
        mAccountWindow.setData(accounts);
        if (!mAccountWindow.isShowing())
            mAccountWindow.showAsDropDown(etAccount);

    }

    //初始化副屏
    public void initUserGoodsScreen() {
        DisplayManager mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();

        if (mUserGoodsScreen == null && displays.length == 2) {
            mUserGoodsScreen = new UserGoodsScreen(this,
                    displays[displays.length - 1], "");// displays[1]是副屏
//            mUserGoodsScreen.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            mUserGoodsScreen.show();
        }
    }

    /**
     * 弹出 配置id、secret弹窗
     */
    public void showConfigDialog() {
        mConfigDialog = new ConfigDialog();
        mConfigDialog.showCenter(this);
    }


    /**
     * 获取登录过的账号
     */
    public AccountInfo getSaveAccount() {
        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(this);
        String accounts_string = sp.getString(KEY_ACCOUNTS, "");

        if (!TextUtils.isEmpty(accounts_string)) {
            return GsonUtils.getInstance().getGson().fromJson(accounts_string, AccountInfo.class);

        } else {
            return null;
        }
    }

    /**
     * 保存账号到sp
     */
    public void saveAccount(String save) {
        if (mAccountInfo == null) {
            mAccountInfo = new AccountInfo();
            List<String> accounts = new ArrayList<>();
            mAccountInfo.setAccounts(accounts);
        }
        List<String> accounts = mAccountInfo.getAccounts();
        //如果该账号已经保存过 直接返回
        if (accounts.contains(save)) {
            return;
        }

        accounts.add(save);
        //最多保存5个账号, 删除最旧的账号
        if (accounts.size() == 5) {
            accounts.remove(0);
        }
        SharedPreferences.Editor edit = SharedPreferencesUtils.getInstance().getSharedPreferences(this).edit();

        //转化为json字符串保存
        String json = GsonUtils.getInstance().getGson().toJson(mAccountInfo);
        edit.putString(KEY_ACCOUNTS, json).apply();

    }


    @OnClick({R.id.btn_login})
    public void onClick() {

        String appkey = sp.getString(Constans.KEY_APPKEY, "");
        String secret = sp.getString(Constans.KEY_SECRET, "");

        if(TextUtils.isEmpty(appkey) || TextUtils.isEmpty(secret)) {
            showToast("请先配置id、secret");
            return;
        }

        //获取初始化信息 收银机是否可用
        mPresenter.init(DeviceUtils.getMacAddress());
//        mPresenter.init("08:ea:40:36:4f:3b");
//        mPresenter.init("10:d0:7a:02:b4:b4");
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ScreenUtils.hideNavigationBar(this);
    }


    private void initLocalPrinter() {
        PrinterUtils.getInstance().setOnPrinterListener(new PrinterUtils.OnPrinterListener() {
            @Override
            public void onUsbPermissionDeny() {
                showToast(getString(R.string.str_permission_deny));
            }

            @Override
            public void onConnecting() {
                showToast(getString(R.string.str_conn_state_connecting));
            }

            @Override
            public void onConnected() {
                showToast(PrinterUtils.getInstance().getConnDeviceInfo() + " " + getString(R.string.str_conn_state_connected));

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
            public void onConnectError(int error, String content) {

            }

            @Override
            public void onCommandError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(getString(R.string.str_choice_printer_command));
                    }
                });
            }

            @Override
            public void onNeedReplugged() {
                showToast(getString(R.string.str_need_replugged));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        PrinterUtils.getInstance().registerReceiver(this);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                PrinterUtils.getInstance().popTill(LoginActivity.this);
//            }
//        }, 1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        PrinterUtils.getInstance().unregisterReceiver(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        textUtils.removeSoftKeyBoardListener(parent);
        mHandler.removeCallbacksAndMessages(null);

        if (mUserGoodsScreen != null && mUserGoodsScreen.isShowing()) {
            mUserGoodsScreen.dismiss();
        }
        if (mAccountWindow != null && mAccountWindow.isShowing()) {
            mAccountWindow.dismiss();
        }
    }

    @Override
    public void loginSuccess(final LoginResponse result) {
        String account = etAccount.getText().toString().trim();
        saveAccount(account);

        SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(this).edit();
        editor.putString(Constans.KEY_ADMIN_NAME, result.getReal_name())
                .putString(Constans.KEY_SESSION_ID, result.getSession_id())
                .putString("account", account)
                .putInt(Constans.KEY_HANDOVER_ID, result.getHandover_id())
                .apply();

        Configs.cashier_id = result.getCashier_id();
        Configs.admin_name = result.getReal_name();
        Configs.menuBeanList = result.getMenu();


        if (is_reserve == 1) {

            //弹出钱箱
            mPresenter.pop_till(Configs.shop_sn, Configs.printer_sn);

            initLocalPrinter();
            PrinterUtils.getInstance().popTill();

            if (dialog == null)
                dialog = new PettyCashDialog();

            dialog.showCenter(this);
            dialog.setOnDialogClickListener(new PettyCashDialog.OnDialogClickListener() {
                @Override
                public void onClick(String content) {
                    String price;
                    if (!content.contains(".")) {
                        price = content + "00";
                    } else {
                        price = content.replace(".", "");
                    }
                    mPresenter.resever_money(result.getSession_id(), Configs.shop_sn, result.getHandover_id(),
                            Integer.parseInt(price));
                }
            });
        } else {
            login();
        }
    }

    private void login() {
        //跳转首页
        ARouter.getInstance().build(ModulePath.goods)
                .withString("admin_name", Configs.admin_name)
                .navigation();
        showToast("登录成功：" + Configs.admin_name);
        finish();
    }

    @Override
    public void loginFailed(Map<String, Object> map) {
        btnLogin.setEnabled(true);
//        if (HttpExceptionEngine.isBussinessError(map)) {
        String error_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
        showToast(error_msg);
//        }

        int errorType = (int) map.get(HttpExceptionEngine.ErrorType);
        int errorCode = (int) map.get(HttpExceptionEngine.ErrorCode);
        String errorMsg = (String) map.get(HttpExceptionEngine.ErrorMsg);
        Log.i(TAG, "loginFailed: map --> errorType:" + errorType
                + ", errorCode: " + errorCode
                + ", errorMsg: " + errorMsg);

    }

    @Override
    public void initSuccess(InitResponse result) {

        String shop_sn = result.getShop().getShop_sn();
        String shop_name = result.getShop().getShop_name();
        is_reserve = result.getShop().getIs_reserve();

        SharedPreferences.Editor editor = SharedPreferencesUtils.getInstance().getSharedPreferences(this).edit();
        editor.putString(Constans.KEY_SHOP_SN, shop_sn)
                .putString(Constans.KEY_SHOP_NAME, shop_name)
                .apply();

        Configs.shop_name = shop_name;
        Configs.shop_sn = shop_sn;
        Configs.refund_auth = result.getShop().getRefund_auth();
        Configs.till_auth = result.getShop().getTill_auth();
        Configs.lock_auth = result.getShop().getLock_auth();

        List<InitResponse.PrintersBean> printers = result.getPrinters();
        PrintHelper.printers_count = printers.size();
        if(PrintHelper.printers_count != 0) {
            Configs.printer_sn = printers.get(0).getDevice_sn();
        }
        PrintHelper.printersBeans = printers;



        //判断是否禁止登录
        if("yes".equals(result.getIs_disabled())) {
            hideLoading();
            showToast("设备已停用");
            return;
        }

        String account = etAccount.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(account)) {
            hideLoading();
            showToast("账号不能为空！");
        } else if (TextUtils.isEmpty(password)) {
            hideLoading();
            showToast("密码不能为空！");
        } else {
            //登录
            mPresenter.login(Configs.shop_sn, account, password);
            btnLogin.setEnabled(false);
        }

    }

    @Override
    public void initFailed(Map<String, Object> map) {
        int errorType = (int) map.get(HttpExceptionEngine.ErrorType);
        int errorCode = (int) map.get(HttpExceptionEngine.ErrorCode);
        String errorMsg = (String) map.get(HttpExceptionEngine.ErrorMsg);
        Log.i(TAG, "loginFailed: map --> errorType:" + errorType
                + ", errorCode: " + errorCode
                + ", errorMsg: " + errorMsg);
        showToast(errorMsg);

//        mHandler.sendEmptyMessageDelayed(MSG_GET_SHOP, 2000);

    }

    @Override
    public void reseverMoneySuccess() {
//        showToast("备用金 成功");
        if (dialog != null && dialog.isShow()) {
            dialog.dismiss();
        }
        login();
    }

    @Override
    public void reseverMoneyFailed(Map<String, Object> map) {
        String errorMsg = (String) map.get(HttpExceptionEngine.ErrorMsg);
        showToast(errorMsg);
    }

    @Override
    public void popTillSuccess() {

    }

    @Override
    public void popTillFailed(Map<String, Object> map) {
        showToast((String) map.get(HttpExceptionEngine.ErrorMsg));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult: ");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.i(TAG, "onRequestPermissionsResult: ");

    }
}

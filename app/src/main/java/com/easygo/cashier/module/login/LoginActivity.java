package com.easygo.cashier.module.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.BuildConfig;
import com.easygo.cashier.Configs;
import com.easygo.cashier.Constants;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.TestUtils;
import com.easygo.cashier.base.BaseAppMvpActivity;
import com.easygo.cashier.bean.AccountInfo;
import com.easygo.cashier.bean.InitResponse;
import com.easygo.cashier.bean.LoginResponse;
import com.easygo.cashier.module.secondary_sreen.UserGoodsScreen;
import com.easygo.cashier.printer.PrintHelper;
import com.easygo.cashier.printer.local.PrinterUtils;
import com.easygo.cashier.utils.ActivitiesUtils;
import com.easygo.cashier.utils.CouponUtils;
import com.easygo.cashier.utils.GiftCardUtils;
import com.easygo.cashier.utils.MemberUtils;
import com.easygo.cashier.widget.AccountWindow;
import com.easygo.cashier.widget.dialog.ConfigDialog;
import com.easygo.cashier.widget.dialog.GeneraDialog;
import com.easygo.cashier.widget.dialog.PettyCashDialog;
import com.easygo.cashier.widget.view.MyTitleBar;
import com.niubility.library.base.BaseHandler;
import com.niubility.library.common.config.BaseConfig;
import com.niubility.library.common.config.ConfigPreferenceActivity;
import com.niubility.library.common.constants.BaseConstants;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.DeviceUtils;
import com.niubility.library.utils.GsonUtils;
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
    @BindView(R.id.cl_title)
    MyTitleBar clTitle;
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
    private PettyCashDialog dialog;
    private UserGoodsScreen mUserGoodsScreen;

    private AccountInfo mAccountInfo;
    private AccountWindow mAccountWindow;

    private ConfigDialog mConfigDialog;
    private TestUtils textUtils = new TestUtils();


    private MyHandler mHandler = new MyHandler(this);
    private static class MyHandler extends BaseHandler<LoginActivity> {

        public MyHandler(LoginActivity activity) {
            super(activity);
        }

        @Override
        public void onHandleMessage(Message msg, LoginActivity activity) {
//            switch (msg.what) {
//                case Msg.MSG_GET_SHOP:
//                    break;
//            }
        }
    }

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
        String appkey = sp.getString(BaseConstants.KEY_APPKEY, "");
        String secret = sp.getString(BaseConstants.KEY_SECRET, "");
        String account = sp.getString(Constants.KEY_ACCOUNT, "");
        etAccount.setText(account);


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

        //清除会员、优惠券、促销、礼品卡等信息
        MemberUtils.reset();
        CouponUtils.getInstance().reset();
        ActivitiesUtils.getInstance().reset();
        GiftCardUtils.getInstance().reset();


        if(Configs.current_mode == Configs.mode_offline) {
            btnLogin.setText("登录（离线）");
        }

        clTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if("release".equals(BuildConfig.BUILD_TYPE)) {
                    if(!Configs.open_offline) {
                        return true;
                    }
                    BaseConfig.hideKeys = new String[]{
                            getString(R.string.key_environment)
                    };
                }
                startActivity(new Intent(LoginActivity.this, ConfigPreferenceActivity.class));
                return true;
            }
        });

        if(Configs.open_offline) {
            btnLogin.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (TextUtils.isEmpty(getAccount()) || TextUtils.isEmpty(getPassword())) {
                        return true;
                    }
                    if (Configs.current_mode == Configs.mode_online) {
                        Configs.current_mode = Configs.mode_offline;
                        showToast("已切换到离线模式");
                        btnLogin.setText("登录（离线）");
                    } else {
                        Configs.current_mode = Configs.mode_online;
                        showToast("已切换到在线模式");
                        btnLogin.setText(R.string.text_login);
                    }
                    return true;
                }
            });
        }


    }

    private void initAccount() {
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
        String accounts_string = sp.getString(Constants.KEY_ACCOUNTS, "");

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
        SharedPreferences.Editor edit = sp.edit();

        //转化为json字符串保存
        String json = GsonUtils.getInstance().getGson().toJson(mAccountInfo);
        edit.putString(Constants.KEY_ACCOUNTS, json).apply();

    }


    @OnClick({R.id.btn_login})
    public void onClick() {

        String appkey = sp.getString(BaseConstants.KEY_APPKEY, "");
        String secret = sp.getString(BaseConstants.KEY_SECRET, "");

        if(TextUtils.isEmpty(appkey) || TextUtils.isEmpty(secret)) {
            showToast("请先配置id、secret");
            return;
        }

        final String account = getAccount();
        final String password = getPassword();

        if(TextUtils.isEmpty(account)) {
            showToast("账号不能为空！");
        } else if(TextUtils.isEmpty(password)) {
            showToast("密码不能为空！");
        } else {

             if(Configs.current_mode == Configs.mode_offline) {
                GeneraDialog generaDialog = GeneraDialog.getInstance("登录离线模式？", "取消", "确定");
                generaDialog.showCenter(this);
                generaDialog.setOnDialogClickListener(new GeneraDialog.OnDialogClickListener() {
                    @Override
                    public void onSubmit() {
                        //获取初始化信息 收银机是否可用
                        mPresenter.login(DeviceUtils.getMacAddress(), account, password);
                    }
                });
            } else {
                 //获取初始化信息 收银机是否可用
                 mPresenter.login(DeviceUtils.getMacAddress(), account, password);
             }
        }
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
//                if(showTip) {
//                    showToast(PrinterUtils.getInstance().getConnDeviceInfo() + " " + getString(R.string.str_conn_state_connected));
//                }
            }

            @Override
            public void onDisconnected() {
//                showToast(getString(R.string.str_conn_state_disconnect));
            }

            @Override
            public void onPleaseConnectPrinter() {
//                showToast(getString(R.string.str_cann_printer));
            }

            @Override
            public void onConnectFailed() {
//                showToast(getString(R.string.str_conn_fail));
            }

            @Override
            public void onConnectError(int error, String content) {

            }

            @Override
            public void onCommandError() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        showToast(getString(R.string.str_choice_printer_command));
//                    }
//                });
            }

            @Override
            public void onNeedReplugged() {
//                showToast(getString(R.string.str_need_replugged));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        PrinterUtils.getInstance().registerReceiver(this);
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
        if (mConfigDialog != null && mConfigDialog.getDialog() != null && mConfigDialog.getDialog().isShowing()) {
            mConfigDialog.dismiss();
        }
    }

    @Override
    public String getAccount() {
        return etAccount.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return etPassword.getText().toString().trim();
    }

    @Override
    public void setLoginButtonEnable(boolean buttonEnable) {
        btnLogin.setEnabled(buttonEnable);
    }

    @Override
    public void loginSuccess(final LoginResponse result) {
        String account = etAccount.getText().toString().trim();
        saveAccount(account);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.KEY_ADMIN_NAME, result.getReal_name())
                .putString(BaseConstants.KEY_SESSION_ID, result.getSession_id())
                .putString(Constants.KEY_ACCOUNT, account)
                .putInt(Constants.KEY_HANDOVER_ID, result.getHandover_id())
                .apply();

        Configs.cashier_id = result.getCashier_id();
        Configs.admin_name = result.getReal_name();
        Configs.menuBeanList = result.getMenu();


        if (Configs.is_reserve == 1) {
            reserveMoney(result);
        } else {
            login();
        }
    }

    @Override
    public void login() {
        //跳转首页
        ARouter.getInstance().build(ModulePath.goods)
                .withString("admin_name", String.valueOf(Configs.cashier_id))
                .navigation();
        showToast("登录成功：" + Configs.admin_name);
        finish();
    }

    @Override
    public void reserveMoney(final LoginResponse result) {
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
    }

    @Override
    public void loginFailed(Map<String, Object> map) {
        btnLogin.setEnabled(true);
        String error_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);
        showToast(error_msg);

        int errorType = (int) map.get(HttpExceptionEngine.ErrorType);
        int errorCode = (int) map.get(HttpExceptionEngine.ErrorCode);
        String errorMsg = (String) map.get(HttpExceptionEngine.ErrorMsg);
        Log.i(TAG, "loginFailed: map --> errorType:" + errorType
                + ", errorCode: " + errorCode
                + ", errorMsg: " + errorMsg);

    }


    @Override
    public void save(InitResponse result) {
        Configs.shop_sn = result.getShop().getShop_sn();
        Configs.shop_name = result.getShop().getShop_name();
        Configs.is_reserve = result.getShop().getIs_reserve();
        Configs.refund_auth = result.getShop().getRefund_auth();
        Configs.till_auth = result.getShop().getTill_auth();
        Configs.lock_auth = result.getShop().getLock_auth();

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(BaseConstants.KEY_SHOP_SN, Configs.shop_sn)
                .putString(BaseConstants.KEY_SHOP_NAME, Configs.shop_name)
                .apply();


        List<InitResponse.PrintersBean> printers = result.getPrinters();
        PrintHelper.printers_count = printers.size();
        if(PrintHelper.printers_count != 0) {
            Configs.printer_sn = printers.get(0).getDevice_sn();
        }
        PrintHelper.printersBeans = printers;
    }

    @Override
    public void reseverMoneySuccess() {
        if (dialog != null && dialog.isShowing()) {
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

}

package com.easygo.cashier.widget.dialog;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.fragment.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.utils.SoftKeyboardUtil;
import com.niubility.library.common.constants.BaseConstants;
import com.niubility.library.utils.SharedPreferencesUtils;
import com.niubility.library.utils.ToastUtils;

/**
 * 配置 appkey、secret弹窗
 */
public class ConfigDialog extends MyBaseDialog {

    private EditText etPassword;
    private EditText etId;
    private EditText etSecret;
    private Button btnConfirm;
    private TextView tvVersion;

    private SharedPreferences sp;
    private String appkey;
    private String secret;

    private boolean isPassed = false;

    @Override
    protected void initView(View rootView) {
        etPassword = rootView.findViewById(R.id.et_password);
        etId = rootView.findViewById(R.id.et_id);
        etSecret = rootView.findViewById(R.id.et_secret);
        btnConfirm = rootView.findViewById(R.id.btn);
        tvVersion = rootView.findViewById(R.id.tv_version);

        sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getContext());
        readAndSetConfig();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPassed) {//还没有验证通过
                    if("ezgo123".equals(etPassword.getText().toString().trim())) {
                        isPassed = true;
                        showUI(isPassed);
                        etPassword.setText("");
                    } else {
                        ToastUtils.showToast(getContext(), "密码不正确！");
                    }
                } else {//已经验证通过
                    SharedPreferences.Editor edit = sp.edit();
                    String id = etId.getText().toString().trim();
                    String secret = etSecret.getText().toString().trim();
                    if(TextUtils.isEmpty(id) || TextUtils.isEmpty(secret)) {
                        return;
                    }

                    edit.putString(BaseConstants.KEY_APPKEY, id)
                            .putString(BaseConstants.KEY_SECRET, secret)
                            .apply();
                    if (getActivity() != null) {
                        SoftKeyboardUtil.hideSoftKeyboard(getActivity());
                    }

                    dismiss();
                }
            }
        });

        etSecret.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    etSecret.getText().delete(etSecret.length()-1, etSecret.length());
                }
                return false;
            }
        });


        PackageManager pm = getContext().getPackageManager();
        if(pm != null) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(getContext().getPackageName(), 0);
                tvVersion.setText("Version: " + packageInfo.versionName + "(" + packageInfo.versionCode + ")");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }



    }

    /**
     * 读取本地配置并显示
     */
    private void readAndSetConfig() {
        appkey = sp.getString(BaseConstants.KEY_APPKEY, "");
        secret = sp.getString(BaseConstants.KEY_SECRET, "");

        etId.setText(appkey);
        etSecret.setText(secret);
//        if("dev".equals(BuildConfig.BUILD_TYPE)) {
//            etSecret.setText("134ad99e0d7a37ecc89e544938426e58");
//        } else if("T".equals(BuildConfig.BUILD_TYPE)) {
//            etSecret.setText("121a653ac09dd3164fb4549edb0294d6");
//        }
    }

    /**
     * 对应显示相应UI
     * @param isPassed 是否验证通过
     */
    public void showUI(boolean isPassed) {
        etPassword.setVisibility(isPassed? View.GONE: View.VISIBLE);
        etId.setVisibility(isPassed? View.VISIBLE: View.GONE);
        etSecret.setVisibility(isPassed? View.VISIBLE: View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        isPassed = false;
        showUI(isPassed);
        readAndSetConfig();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_config;
    }

    @Override
    protected int getLayoutWidth() {
        int width = getResources().getDimensionPixelSize(R.dimen.login_btn_width);
        int padding = getResources().getDimensionPixelSize(R.dimen.x100);
        return width + 2 * padding;
    }

    @Override
    protected int getLayoutHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected boolean canCanceledOnTouchOutside() {
        return true;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_CONFIG");
    }
}

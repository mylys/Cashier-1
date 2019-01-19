package com.easygo.cashier.widget;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseDialog;
import com.niubility.library.constants.Constans;
import com.niubility.library.utils.SharedPreferencesUtils;

/**
 * 配置 appkey、secret弹窗
 */
public class ConfigDialog extends BaseDialog {

    private EditText etId;
    private EditText etSecret;
    private Button btnConfirm;

    private SharedPreferences sp;
    private String appkey;
    private String secret;

    @Override
    protected void initView(View rootView) {
        etId = rootView.findViewById(R.id.et_id);
        etSecret = rootView.findViewById(R.id.et_secret);
        btnConfirm = rootView.findViewById(R.id.btn);

        sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getContext());
        appkey = sp.getString(Constans.KEY_APPKEY, "");
        secret = sp.getString(Constans.KEY_SECRET, "");

        etId.setText(appkey);
        etSecret.setText(secret);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = sp.edit();

                edit.putString(Constans.KEY_APPKEY, etId.getText().toString().trim())
                    .putString(Constans.KEY_SECRET, etSecret.getText().toString().trim())
                    .apply();

                dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(-2, -2);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_config;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected boolean shouldHideBackground() {
        return false;
    }

    @Override
    protected boolean canCanceledOnTouchOutside() {
        return false;
    }

    @Override
    protected boolean isWindowWidthMatchParent() {
        return false;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_CONFIG");
    }
}

package com.easygo.cashier.widget;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseDialog;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 银联支付弹窗
 */
public class BankcardDialog extends BaseDialog implements View.OnClickListener {
    private ConstraintLayout clClose;
    private TextView tvMoney;
    private ImageView ivQrcode;

    private String dialog_money = "";
    private String dialog_text = "";

    public static BankcardDialog getInstance(String... dialogInfo) {
        BankcardDialog dialog = new BankcardDialog();
        Bundle bundle = new Bundle();
        bundle.putString("DIALOG_MONEY", dialogInfo[0]);
        bundle.putString("DIALOG_TEXT", dialogInfo[1]);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_bankcard;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected void initView(View rootView) {
        if (getArguments() != null) {
            dialog_money = getArguments().getString("DIALOG_MONEY");
            dialog_text = getArguments().getString("DIALOG_TEXT");
        }
        clClose = rootView.findViewById(R.id.cl_close);
        tvMoney = rootView.findViewById(R.id.tv_money);
        ivQrcode = rootView.findViewById(R.id.iv_qrcode);

        tvMoney.setText("金额￥" + dialog_money);

        if(!TextUtils.isEmpty(dialog_text)) {
            ivQrcode.setImageBitmap(CodeUtils.createImage(dialog_text, 256, 256, null));
        }

        clClose.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(getResources().getDimensionPixelSize(R.dimen.x526),
                    getResources().getDimensionPixelSize(R.dimen.y610));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cl_close:
                dismiss();
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(listener != null) {
            listener.onClose();
        }
    }

    OnDialogClickListener listener;

    public interface OnDialogClickListener {
        void onClose();
    }

    public void setOnDialogListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_BANK_CARD");
    }
}

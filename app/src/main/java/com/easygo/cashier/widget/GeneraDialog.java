package com.easygo.cashier.widget;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseDialog;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-29
 */
public class GeneraDialog extends BaseDialog implements View.OnClickListener {
    private TextView cancel;
    private TextView submit;
    private TextView title;

    private String dialog_title = "";
    private String dialog_cancel = "";
    private String dialog_submit = "";

    public static GeneraDialog getInstance(String... dialogInfo) {
        GeneraDialog dialog = new GeneraDialog();
        Bundle bundle = new Bundle();
        bundle.putString("DIALOG_TITLE", dialogInfo[0]);
        bundle.putString("DIALOG_CANCEL", dialogInfo[1]);
        bundle.putString("DIALOG_SUBMIT", dialogInfo[2]);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_genera;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected void initView(View rootView) {
        if (getArguments() != null) {
            dialog_title = getArguments().getString("DIALOG_TITLE");
            dialog_cancel = getArguments().getString("DIALOG_CANCEL");
            dialog_submit = getArguments().getString("DIALOG_SUBMIT");
        }
        title = rootView.findViewById(R.id.dialog_title);
        cancel = rootView.findViewById(R.id.dialog_cancel);
        submit = rootView.findViewById(R.id.dialog_submit);

        title.setText(dialog_title);
        cancel.setText(dialog_cancel);
        submit.setText(dialog_submit);

        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if (window != null) {
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
        return true;
    }

    @Override
    protected boolean isWindowWidthMatchParent() {
        return false;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.dialog_cancel:
                break;
            case R.id.dialog_submit:
                if (listener != null) {
                    listener.onSubmit();
                }
                break;
        }
    }

    OnDialogClickListener listener;

    public interface OnDialogClickListener {
        void onSubmit();
    }

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_GENERA");
    }
}

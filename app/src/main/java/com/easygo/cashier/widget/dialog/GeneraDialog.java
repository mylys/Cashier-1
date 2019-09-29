package com.easygo.cashier.widget.dialog;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.easygo.cashier.R;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-29
 */
public class GeneraDialog extends MyBaseDialog implements View.OnClickListener {
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
    protected int getLayoutWidth() {
        return 0;
    }

    @Override
    protected int getLayoutHeight() {
        return 0;
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
    protected boolean canCanceledOnTouchOutside() {
        return true;
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

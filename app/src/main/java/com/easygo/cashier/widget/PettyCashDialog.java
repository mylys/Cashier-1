package com.easygo.cashier.widget;

import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseDialog;
import com.niubility.library.utils.ToastUtils;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-27
 */
public class PettyCashDialog extends BaseDialog {
    private EditText editText;
    private ImageView tvCancel;
    private DialogKeyboard key_board;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_petty_cash;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected void initView(View rootView) {
        setCancelable(false);

        editText = rootView.findViewById(R.id.edit_input);
        tvCancel = rootView.findViewById(R.id.dialog_cancel);
        key_board = rootView.findViewById(R.id.key_board);

        key_board.attachEditText(editText);
        key_board.setOnSureClickListener(new DialogKeyboard.OnSureClickListener() {
            @Override
            public void onContent(String string) {
                if (listener != null) {
                    listener.onClick(string);
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private OnDialogClickListener listener;

    public interface OnDialogClickListener {
        void onClick(String content);
    }

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.listener = listener;
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

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_PETTY");
    }
}

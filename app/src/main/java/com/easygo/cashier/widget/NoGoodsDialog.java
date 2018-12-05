package com.easygo.cashier.widget;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseDialog;
import com.niubility.library.widget.keyboard.DialogKeyboard;

/**
 * @Describe：无码商品dialog
 */
public class NoGoodsDialog extends BaseDialog {
    private EditText editInput;
    private ImageView cancel;
    private DialogKeyboard keyboard;
    private OnDialogClickListener onDialogClickListener;

    public interface OnDialogClickListener {
        void onDialogClick(String content);
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_nogoods;
    }

    @Override
    protected int getAnimation() {
        return R.style.DialogStyle;
    }

    @Override
    protected void initView(View rootView) {
        editInput = rootView.findViewById(R.id.edit_input);
        cancel = rootView.findViewById(R.id.dialog_cancel);
        keyboard = rootView.findViewById(R.id.key_board);

        keyboard.setEditText(editInput);
        keyboard.setOnClickSureKeyListener(new DialogKeyboard.OnClickSureKeyListener() {
            @Override
            public void onClickSureKey(EditText editText) {
                if (onDialogClickListener != null) {
                    onDialogClickListener.onDialogClick(editText.getText().toString());
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
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
        showCenter(activity, "DIALOG_NOGOODS");
    }
}

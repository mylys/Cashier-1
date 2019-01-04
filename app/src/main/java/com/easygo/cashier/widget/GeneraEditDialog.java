package com.easygo.cashier.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.SoftKeyboardUtil;
import com.easygo.cashier.TestUtils;
import com.niubility.library.base.BaseDialog;
import com.niubility.library.constants.Events;
import com.niubility.library.utils.EventUtils;
import com.niubility.library.utils.ToastUtils;

/**
 * @Describe：通用输入框弹窗
 * @date：2019-01-03
 */
public class GeneraEditDialog extends BaseDialog {
    private TextView dialog_title;
    private TextView dialog_cancel;
    private TextView dialog_submit;
    private ImageView iv_cancel;
    private EditText editInput;

    private TestUtils testUtils = new TestUtils();

    private OnDialogClickListener listener;
    private String title = "";

    public interface OnDialogClickListener {
        void onContent(String content);
    }

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_edit;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }

    @Override
    protected void initView(View rootView) {
        editInput = rootView.findViewById(R.id.edit_input);
        dialog_title = rootView.findViewById(R.id.tv_name);
        dialog_cancel = rootView.findViewById(R.id.dialog_cancel);
        dialog_submit = rootView.findViewById(R.id.dialog_submit);
        iv_cancel = rootView.findViewById(R.id.iv_cancel);

        if (!TextUtils.isEmpty(title)) {
            dialog_title.setText(title);
        }

        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismiss();
            }
        });
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismiss();
            }
        });
        dialog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInput.getText().toString().length() == 0) {
                    ToastUtils.showToast(getActivity(), "请输入备注");
                    return;
                }
                listener.onContent(editInput.getText().toString().trim());
                dialogDismiss();
            }
        });
    }

    private void dialogDismiss() {
        SoftKeyboardUtil.hideSoftKeyboard(getActivity(),editInput);
        dismiss();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void clearInfo() {
        editInput.setText("");
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
        showCenter(activity, "DIALOG_GENERA_EDIT");
    }
}

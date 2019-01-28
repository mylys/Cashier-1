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
import com.niubility.library.utils.ToastUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

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

    private ConstraintLayout constraintLayout;
    private TextView tvAccount;
    private EditText editAccount;
    private TextView tvPassword;
    private EditText editPassword;

    private OnDialogClickListener listener;
    private String title = "";

    public static final int ENTRY_ORDER = 0x00;
    public static final int USER_ACCOUNT = 0x01;
    public static final int USER_ACCREDIT = 0x02;
    private int type = ENTRY_ORDER;

    public interface OnDialogClickListener {
        void onContent(int type,String account,String password);
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

        constraintLayout = rootView.findViewById(R.id.constraint_center);
        tvAccount = rootView.findViewById(R.id.user_account);
        tvPassword = rootView.findViewById(R.id.user_password);
        editAccount = rootView.findViewById(R.id.edit_account);
        editPassword = rootView.findViewById(R.id.edit_password);

        if (!TextUtils.isEmpty(title)) {
            dialog_title.setText(title);
        }
        setStyle(type);

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
                switch (type){
                    case ENTRY_ORDER:
                        listener.onContent(type,editInput.getText().toString().trim(),"");
                        break;
                    case USER_ACCOUNT:
                        listener.onContent(type,editAccount.getText().toString().trim(),editPassword.getText().toString().trim());
                        break;
                    case USER_ACCREDIT:
                        listener.onContent(type,editAccount.getText().toString().trim(),"");
                        break;
                }
                setTvEmpty();
                dialogDismiss();
            }
        });
    }

    private void setTvEmpty() {
        editPassword.setText("");
        editAccount.setText("");
        editInput.setText("");
    }

    private void setStyle(int type) {
        switch (type){
            case ENTRY_ORDER:
                editInput.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.INVISIBLE);
                break;
            case USER_ACCOUNT:
                editInput.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);
                break;
            case USER_ACCREDIT:
                editInput.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);
                editPassword.setVisibility(View.GONE);
                tvPassword.setVisibility(View.GONE);
                tvAccount.setText("授权密码：");
                break;
        }
    }

    private void dialogDismiss() {
        SoftKeyboardUtil.hideSoftKeyboard(getActivity(),editInput);
        dismiss();
    }

    public void setType(int type){
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
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

package com.easygo.cashier.widget.dialog;

import android.content.DialogInterface;
import androidx.fragment.app.FragmentActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.utils.SoftKeyboardUtil;
import com.niubility.library.utils.ToastUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @Describe：通用输入框弹窗
 * @date：2019-01-03
 */
public class GeneraEditDialog extends MyBaseDialog {
    private TextView dialog_title;
    private TextView dialog_cancel;
    private TextView dialog_submit;
    private ConstraintLayout cl_cancel;
    private EditText editInput;

    private ConstraintLayout constraintLayout;

    private TextView tvAccount;
    private EditText editAccount;
    private TextView tvPassword;
    private EditText editPassword;

    private OnDialogClickListener listener;
    private String title = "";
    private boolean visiable = true;

    public static final int ENTRY_ORDER = 0x00;
    public static final int USER_ACCOUNT = 0x01;
    public static final int USER_ACCREDIT = 0x02;
    private int type = ENTRY_ORDER;

    public void setVisiable(boolean b) {
        visiable = b;
    }

    public interface OnDialogClickListener {
        void onContent(int type, String account, String password);
    }

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog_edit;
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
        editInput = rootView.findViewById(R.id.edit_input);
        dialog_title = rootView.findViewById(R.id.tv_name);
        dialog_cancel = rootView.findViewById(R.id.dialog_cancel);
        dialog_submit = rootView.findViewById(R.id.dialog_submit);
        cl_cancel = rootView.findViewById(R.id.cl_cancel);

        constraintLayout = rootView.findViewById(R.id.constraint_center);
        tvAccount = rootView.findViewById(R.id.user_account);
        tvPassword = rootView.findViewById(R.id.user_password);
        editAccount = rootView.findViewById(R.id.edit_account);
        editPassword = rootView.findViewById(R.id.edit_password);

        if (!TextUtils.isEmpty(title)) {
            dialog_title.setText(title);
        }
        if (!visiable) {
            cl_cancel.setVisibility(View.GONE);
            dialog_cancel.setVisibility(View.GONE);
        }
        setStyle(type);

        cl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismiss();
                dismiss();
            }
        });
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDismiss();
                dismiss();
            }
        });
        dialog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (type) {
                    case ENTRY_ORDER:
                        listener.onContent(type, editInput.getText().toString().trim(), "");
                        break;
                    case USER_ACCOUNT:
                        if (TextUtils.isEmpty(editAccount.getText().toString().trim()) || TextUtils.isEmpty(editPassword.getText().toString().trim())){
                            ToastUtils.showToast(getActivity(),"账号密码不能为空");
                            return;
                        }
                        listener.onContent(type, editAccount.getText().toString().trim(), editPassword.getText().toString().trim());
                        break;
                    case USER_ACCREDIT:
                        if (TextUtils.isEmpty(editAccount.getText().toString().trim())){
                            ToastUtils.showToast(getActivity(),"请输入授权密码");
                            return;
                        }
                        listener.onContent(type, editAccount.getText().toString().trim(), "");
                        break;
                }
                dialogDismiss();
            }
        });
    }

    public void setTvEmpty() {
        editPassword.setText("");
        editAccount.setText("");
        editInput.setText("");
    }

    private void setStyle(int type) {
        switch (type) {
            case ENTRY_ORDER:
                editInput.setVisibility(View.VISIBLE);
                constraintLayout.setVisibility(View.INVISIBLE);
                break;
            case USER_ACCOUNT:
                editInput.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);
                editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
            case USER_ACCREDIT:
                editInput.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.VISIBLE);
                editAccount.setHint("请输入授权密码");
                editAccount.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editPassword.setVisibility(View.GONE);
                tvPassword.setVisibility(View.GONE);
                tvAccount.setText("授权密码：");
                break;
        }
    }

    public void dialogDismiss() {
        SoftKeyboardUtil.hideSoftKeyboard(getActivity(), editInput);
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_GENERA_EDIT");
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        setTvEmpty();
    }
}

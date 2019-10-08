package com.easygo.cashier.widget.dialog;

import androidx.fragment.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.widget.view.DialogKeyboard;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-27
 */
public class PettyCashDialog extends MyBaseDialog {
    private TextView tvTitle;
    private EditText editText;
    private ConstraintLayout clCancel;
    private DialogKeyboard key_board;

    private int mTitleResId = -1;
    private int mHintResId = -1;
    private boolean mTvCancelVisibility;
    private boolean canInputDecimal;


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_petty_cash;
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
        setCancelable(false);

        tvTitle = rootView.findViewById(R.id.tv_title);
        editText = rootView.findViewById(R.id.edit_input);
        clCancel = rootView.findViewById(R.id.cl_cancel);
        key_board = rootView.findViewById(R.id.key_board);

        key_board.setCanInputDecimal(canInputDecimal);
        key_board.attachEditText(editText);
        key_board.setOnSureClickListener(new DialogKeyboard.OnSureClickListener() {
            @Override
            public void onContent(String string) {
                if (listener != null) {
                    listener.onClick(string);
                }
            }
        });

        clCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (mTitleResId != -1 && mHintResId != -1) {
            tvTitle.setText(mTitleResId);
            editText.setHint(mHintResId);
            setCancelable(true);
        }

        clCancel.setVisibility(mTvCancelVisibility ? View.VISIBLE : View.GONE);

    }

    public void setNoCode() {
        mTitleResId = R.string.text_no_barcode_goods;
        mHintResId = R.string.input_price;
        mTvCancelVisibility = true;
    }

    public void setCanInputDecimal(boolean canInputDecimal) {
        this.canInputDecimal = canInputDecimal;
    }

    private OnDialogClickListener listener;

    public interface OnDialogClickListener {
        void onClick(String content);
    }

    public void setOnDialogClickListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_PETTY");
    }
}

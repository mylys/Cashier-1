package com.easygo.cashier.widget.dialog;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.widget.view.DialogKeyboard;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @Describe：
 * @date：2018-12-27
 */
public class GeneraCashDialog extends MyBaseDialog {
    private TextView tvTitle;
    private TextView tvUnit;
    private EditText editText;
    private ConstraintLayout clCancel;
    private DialogKeyboard key_board;

    private int mTitleResId = -1;
    private int mHintResId = -1;
    private int limit = 2;
    private boolean mTvCancelVisibility;
    private boolean canInputDecimal;
    private boolean mTvUnitVisibility;

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
        tvUnit = rootView.findViewById(R.id.text_unit);
        editText = rootView.findViewById(R.id.edit_input);
        clCancel = rootView.findViewById(R.id.cl_cancel);
        key_board = rootView.findViewById(R.id.key_board);

        key_board.setLimit(limit);
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
        tvUnit.setVisibility(mTvUnitVisibility ? View.VISIBLE : View.GONE);
    }

    public void setNoCode() {
        mTitleResId = R.string.text_no_barcode_goods;
        mHintResId = R.string.input_price;
        mTvCancelVisibility = true;
    }

    public void setWeight() {
        mTitleResId = R.string.text_set_tare;
        mHintResId = R.string.input_set_tare;
        mTvCancelVisibility = true;
        mTvUnitVisibility = true;
    }

    public void setCanInputDecimal(boolean canInputDecimal) {
        this.canInputDecimal = canInputDecimal;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setEditText(String msg) {
        editText.setText(msg);
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

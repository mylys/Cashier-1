package com.easygo.cashier.widget.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.niubility.library.utils.ScreenUtils;
import com.niubility.library.utils.ToastUtils;

import java.text.DecimalFormat;

import androidx.constraintlayout.widget.ConstraintLayout;

public class DialogKeyboard extends ConstraintLayout {

    private View mView;
    private TextView mTv1;
    private TextView mTv2;
    private TextView mTv3;
    private TextView mTv4;
    private TextView mTv5;
    private TextView mTv6;
    private TextView mTv7;
    private TextView mTv8;
    private TextView mTv9;
    private TextView mTv0;
    private TextView mTv00;
    private TextView mTvPoint;
    private TextView mTvSure;
    private LinearLayout mTvBack;

    private TextView[] mTvs;
    private EditText mEditText;
    private DecimalFormat df = new DecimalFormat("0.00");
    private int limit;

    public DialogKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        initView(context);

        initAttrs();
    }

    private void initView(final Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_keyboard_view, this, true);
        mTv1 = mView.findViewById(R.id.tv_1);
        mTv2 = mView.findViewById(R.id.tv_2);
        mTv3 = mView.findViewById(R.id.tv_3);
        mTv4 = mView.findViewById(R.id.tv_4);
        mTv5 = mView.findViewById(R.id.tv_5);
        mTv6 = mView.findViewById(R.id.tv_6);
        mTv7 = mView.findViewById(R.id.tv_7);
        mTv8 = mView.findViewById(R.id.tv_8);
        mTv9 = mView.findViewById(R.id.tv_9);
        mTvPoint = mView.findViewById(R.id.tv_point);
        mTv0 = mView.findViewById(R.id.tv_0);
        mTv00 = mView.findViewById(R.id.tv_00);
        mTvSure = mView.findViewById(R.id.tv_sure);
        mTvBack = mView.findViewById(R.id.tv_back);

        mTvs = new TextView[12];
        mTvs[0] = mTv1;
        mTvs[1] = mTv2;
        mTvs[2] = mTv3;
        mTvs[3] = mTv00;
        mTvs[4] = mTv4;
        mTvs[5] = mTv5;
        mTvs[6] = mTv6;
        mTvs[7] = mTv0;
        mTvs[8] = mTv7;
        mTvs[9] = mTv9;
        mTvs[10] = mTv8;
        mTvs[11] = mTvPoint;

        mTvSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText != null && listener != null) {
                    String format = "";
                    if (mEditText.getText().toString().length() != 0) {
                        format = df.format(Float.parseFloat(mEditText.getText().toString().trim()));
                    }
                    listener.onContent(format);
                }
            }
        });

        mTvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditText != null) {
                    Editable editable = mEditText.getText();
                    if (editable.length() == 0) {
                        return;
                    }
                    editable.delete(editable.length() - 1, editable.length());

                    if (onKeyboardClickListener != null) {
                        onKeyboardClickListener.onDeleteClickAfter(editable);
                    }
                }
            }
        });

        for (final TextView tv : mTvs) {

            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onKeyboardClickListener != null) {
                        onKeyboardClickListener.onClickBefore();
                    }

                    TextView textView = (TextView) v;

                    if (mEditText != null && mEditText.isEnabled()) {
                        Editable editable = mEditText.getText();
                        String price = mEditText.getText().toString();//获取输入框内容
                        String text = textView.getText().toString();//获取点击内容
                        if (price.contains(".")) {//如果存在“.”,则不能重复添加，并且小数点后只能2位
                            if (price.substring(price.indexOf(".") + 1).length() == limit) {
                                return;
                            }
                            if (text.equals(".")) {
                                return;
                            }
                        }
                        if (price.length() == 0) {//不能以“0”或者“00”或者“.”开头
                            if (text.startsWith(".") || text.startsWith("00") || (!canInputDecimal && text.startsWith("0"))) {
                                return;
                            }
                        }
                        editable.append(textView.getText());
                        if (onKeyboardClickListener != null) {
                            onKeyboardClickListener.onTextChangedAfter(editable);
                        }

                    }
                }
            });
        }

    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    private boolean canInputDecimal = false;

    public void setCanInputDecimal(boolean canInputDecimal) {
        this.canInputDecimal = canInputDecimal;
    }

    public void setLayoutParams(Context context) {
        for (TextView textView : mTvs) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.width = ScreenUtils.dp2px(context, 33);
            params.height = ScreenUtils.dp2px(context, 33);
            textView.setLayoutParams(params);
        }
    }

    private void initAttrs() {

    }

    public void attachEditText(EditText editText) {
        this.mEditText = editText;

        mEditText.setInputType(InputType.TYPE_NULL);
    }

    private OnSureClickListener listener;

    public interface OnSureClickListener {
        void onContent(String string);

    }

    public void setOnSureClickListener(OnSureClickListener listener) {
        this.listener = listener;
    }

    private OnKeyboardClickListener onKeyboardClickListener;

    public interface OnKeyboardClickListener {
        void onClickBefore();

        void onDeleteClickAfter(Editable editable);

        void onTextChangedAfter(Editable editable);
    }

    public void setOnKeyboardClickListener(OnKeyboardClickListener listener) {
        this.onKeyboardClickListener = listener;
    }


    public View getSureButton() {
        return mTvSure;
    }
}

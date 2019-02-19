package com.easygo.cashier.widget;

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

import androidx.constraintlayout.widget.ConstraintLayout;

public class Keyboard extends ConstraintLayout {

    private View mView;
    private TextView mTv50;
    private TextView mTv100;
    private TextView mTv200;
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

    private TextView[] mTvs;
    private EditText mEditText;

    public Keyboard(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        initView(context);

        initAttrs();
    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_keyboard_view, this, true);
        mTv50 = (TextView) mView.findViewById(R.id.tv_50);
        mTv100 = (TextView) mView.findViewById(R.id.tv_100);
        mTv200 = (TextView) mView.findViewById(R.id.tv_200);
        mTv1 = (TextView) mView.findViewById(R.id.tv_1);
        mTv2 = (TextView) mView.findViewById(R.id.tv_2);
        mTv3 = (TextView) mView.findViewById(R.id.tv_3);
        mTv4 = (TextView) mView.findViewById(R.id.tv_4);
        mTv5 = (TextView) mView.findViewById(R.id.tv_5);
        mTv6 = (TextView) mView.findViewById(R.id.tv_6);
        mTv7 = (TextView) mView.findViewById(R.id.tv_7);
        mTv8 = (TextView) mView.findViewById(R.id.tv_8);
        mTv9 = (TextView) mView.findViewById(R.id.tv_9);
        mTvPoint = (TextView) mView.findViewById(R.id.tv_point);
        mTv0 = (TextView) mView.findViewById(R.id.tv_0);
        mTv00 = (TextView) mView.findViewById(R.id.tv_00);

        mTvs = new TextView[15];
        mTvs[0] = mTv50;
        mTvs[1] = mTv100;
        mTvs[2] = mTv200;
        mTvs[3] = mTv1;
        mTvs[4] = mTv2;
        mTvs[5] = mTv3;
        mTvs[6] = mTv4;
        mTvs[7] = mTv5;
        mTvs[8] = mTv6;
        mTvs[9] = mTv7;
        mTvs[10] = mTv8;
        mTvs[11] = mTv9;
        mTvs[12] = mTvPoint;
        mTvs[13] = mTv0;
        mTvs[14] = mTv00;


        for (TextView tv : mTvs) {

            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;

                    if(mEditText != null && mEditText.isEnabled()) {
                        Editable editable = mEditText.getText();
                        CharSequence text = textView.getText();
                        if(text.equals(".")) {
                            if (editable.toString().contains(".")) {
                                return;
                            }
                        }
                        if (editable.length() <= 10) {
                            editable.append(text);
                        }
                    }
                }
            });
        }

    }

    public void setLayoutParams(Context context){
        for (TextView textView : mTvs) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.width = ScreenUtils.dp2px(context,33);
            params.height = ScreenUtils.dp2px(context,33);
            textView.setLayoutParams(params);
        }
    }

    private void initAttrs() {

    }

    public void attachEditText(EditText editText) {
        this.mEditText = editText;

        mEditText.setInputType(InputType.TYPE_NULL);
    }
}

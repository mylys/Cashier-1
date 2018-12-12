package com.easygo.cashier.widget;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class Keyboard extends ConstraintLayout {

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

        mTvs = new TextView[12];
        mTvs[0] = mTv1;
        mTvs[1] = mTv2;
        mTvs[2] = mTv3;
        mTvs[3] = mTv4;
        mTvs[4] = mTv5;
        mTvs[5] = mTv6;
        mTvs[6] = mTv7;
        mTvs[7] = mTv8;
        mTvs[8] = mTv9;
        mTvs[9] = mTvPoint;
        mTvs[10] = mTv0;
        mTvs[11] = mTv00;


        for (TextView tv : mTvs) {

            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) v;

                    if(mEditText != null && mEditText.isEnabled()) {
                        mEditText.getText().append(textView.getText());
                    }
                }
            });
        }

    }

    private void initAttrs() {

    }

    public void attachEditText(EditText editText) {
        this.mEditText = editText;

        mEditText.setInputType(InputType.TYPE_NULL);
    }
}

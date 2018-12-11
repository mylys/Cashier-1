package com.easygo.cashier.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CountTextView extends ConstraintLayout {

    private View mView;
    private TextView mCountTextView;
    private Button mSubtractBtn;
    private Button mAddBtn;

    /**
     * 数量
     */
    private int mCount;

    public CountTextView(Context context) {
        super(context);
        init(context, null);
    }

    public CountTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        initView(context);

        initAttr(context, attrs);


        mSubtractBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.valueOf(mCountTextView.getText().toString());

                mCount = --count;
                if(mCount < 0)
                    mCount = 0;

                mCountTextView.setText(String.valueOf(mCount));

                if(mListener != null) {
                    mListener.onCountChanged(mCount);
                }
            }
        });
        mAddBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.valueOf(mCountTextView.getText().toString());

                mCount = ++count;

                mCountTextView.setText(String.valueOf(mCount));

                if(mListener != null) {
                    mListener.onCountChanged(mCount);
                }
            }
        });


    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_widget_count, this, true);
        mCountTextView = (TextView) mView.findViewById(R.id.tv_goods_count);
        mSubtractBtn = (Button) mView.findViewById(R.id.btn_subtract);
        mAddBtn = (Button) mView.findViewById(R.id.btn_add);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if(attrs == null) {
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MySearchView);
        if(ta != null) {



            ta.recycle();
        }
    }

    public void setCount(String count) {
        mCountTextView.setText(count);
    }
    public int getCount() {
        return mCount;
    }

    private OnCountListener mListener;

    public void setOnCountListener(OnCountListener listener) {
        this.mListener = listener;
    }

    public interface OnCountListener {
        void onCountChanged(int count);
    }

}
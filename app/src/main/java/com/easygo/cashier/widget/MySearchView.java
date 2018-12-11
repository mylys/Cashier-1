package com.easygo.cashier.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MySearchView extends ConstraintLayout {


    private String mHint;
    private int mSearchBtnWidth;
    private int mSearchBtnHeight;
    private View mView;
    private EditText mEditText;
    private Button mSearchBtn;


    public MySearchView(Context context) {
        super(context);
        init(context, null);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {

        initView(context);

        initAttr(context, attrs);

        //设置搜索按钮大小
        if(mSearchBtnWidth != -1 && mSearchBtnHeight != -1) {
            LayoutParams lp = new LayoutParams(-2, -2);
            lp.width = mSearchBtnWidth;
            lp.height = mSearchBtnHeight;
            mSearchBtn.setLayoutParams(lp);
        }


        if(TextUtils.isEmpty(mHint)) {
            mHint = getResources().getString(R.string.text_search_hint_barcode);
        }
        mEditText.setHint(mHint);

        findViewById(R.id.cl_clear).setOnClickListener(new OnClickListener() {
//        mClearImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
            }
        });

    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_search_view, this, true);
        mEditText = (EditText) mView.findViewById(R.id.et_search);
//        mClearImageView = (ImageView) mView.findViewById(R.id.iv_clear);
        mSearchBtn = (Button) mView.findViewById(R.id.btn_search);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if(attrs == null) {
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MySearchView);
        if(ta != null) {


            mHint = ta.getString(R.styleable.MySearchView_search_hint);
            mSearchBtnWidth = ta.getInt(R.styleable.MySearchView_search_btn_width, -1);
            mSearchBtnHeight = ta.getInt(R.styleable.MySearchView_search_btn_height, -1);

            ta.recycle();
        }
    }

}

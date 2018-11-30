package com.easygo.cashier.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MySearchView extends ConstraintLayout {

    private Drawable mSearchDrawable;
    private int mSearchDrawableWidth;
    private int mSearchDrawableHeight;
    private int mSearchDrawableMarginHorizontal;

    private Drawable mClearDrawable;
    private int mClearDrawableWidth;
    private int mClearDrawableHeight;
    private int mClearDrawableMarginHorizontal;

    private String mHint;
    private View mView;
    private EditText mEditText;
    private ImageView mSearchImageView;
    private ImageView mClearImageView;


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


        if(TextUtils.isEmpty(mHint)) {
            mHint = getResources().getString(R.string.text_search_hint_barcode);
        }
        mEditText.setHint(mHint);

        mClearImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
            }
        });

    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_search_view, this, true);
        mEditText = (EditText) mView.findViewById(R.id.et_search);
        mSearchImageView = (ImageView) mView.findViewById(R.id.iv_search);
        mClearImageView = (ImageView) mView.findViewById(R.id.iv_clear);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if(attrs == null) {
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MySearchView);
        if(ta != null) {
            mSearchDrawable = ta.getDrawable(R.styleable.MySearchView_seach_drawable);
            mSearchDrawableWidth = ta.getInt(R.styleable.MySearchView_seach_width,
                    getResources().getDimensionPixelSize(R.dimen.search_drawable_width));
            mSearchDrawableHeight = ta.getInt(R.styleable.MySearchView_seach_height,
                    getResources().getDimensionPixelSize(R.dimen.search_drawable_height));
            mSearchDrawableMarginHorizontal = ta.getInt(R.styleable.MySearchView_seach_margin_horizontal, 0);

            mClearDrawable = ta.getDrawable(R.styleable.MySearchView_clear_drawable);
            mClearDrawableWidth = ta.getInt(R.styleable.MySearchView_clear_width,
                    getResources().getDimensionPixelSize(R.dimen.clear_drawable_width));
            mClearDrawableHeight = ta.getInt(R.styleable.MySearchView_clear_height,
                    getResources().getDimensionPixelSize(R.dimen.clear_drawable_height));
            mClearDrawableMarginHorizontal = ta.getInt(R.styleable.MySearchView_clear_margin_horizontal, 0);

            mHint = ta.getString(R.styleable.MySearchView_search_hint);

            ta.recycle();
        }
    }

}

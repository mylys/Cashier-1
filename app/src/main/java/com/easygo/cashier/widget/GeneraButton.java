package com.easygo.cashier.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;

/**
 * 图标 文字 上下布局按钮
 */
public class GeneraButton extends ConstraintLayout {

    private View mView;
    private ImageView mImageView;
    private TextView mTextView;
    private int mImageRes;
    private String mText;
    private int mMarginTop;
    private int mImageSize;
    private int mTextSize;

    public GeneraButton(Context context) {
        super(context);
    }

    public GeneraButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initView(context);
        initAttrs(context, attrs);

    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_genera_button, this, true);
        mImageView = mView.findViewById(R.id.iv);
        mTextView = mView.findViewById(R.id.tv);

    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GeneraButton);
        if (ta != null) {

            mImageRes = ta.getResourceId(R.styleable.GeneraButton_image, 0);
            mText = ta.getString(R.styleable.GeneraButton_text);
            mMarginTop = ta.getInt(R.styleable.GeneraButton_margin_top, 0);
            mImageSize = ta.getDimensionPixelSize(R.styleable.GeneraButton_image_size, 0);
            mTextSize = ta.getDimensionPixelSize(R.styleable.GeneraButton_text_size, 0);

            ta.recycle();
        }

        mImageView.setImageResource(mImageRes);
        mTextView.setText(mText);

        if(mImageSize != 0) {
            Constraints.LayoutParams lp = (Constraints.LayoutParams) mImageView.getLayoutParams();
            lp.width = mImageSize;
            lp.height = mImageSize;
            mImageView.setLayoutParams(lp);
        }

        if(mTextSize != 0) {
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        }


    }




}

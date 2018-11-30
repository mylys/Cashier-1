package com.easygo.cashier.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.easygo.cashier.R;

public class PayButton extends android.support.v7.widget.AppCompatTextView {


    /**
     * 默认背景
     */
    private int mNoSelectedBgDrawable = R.drawable.bg_frame;
    /**
     * 选中时的背景
     */
    private Drawable mSelectedBgDrawable;
    /**
     * 默认文本颜色
     */
    private int mNoSelectedColor;
    /**
     * 选中时的背景
     */
    private int mSelectedColor;

    public PayButton(Context context) {
        super(context);
        init(context, null);
    }

    public PayButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void init(Context context, @Nullable AttributeSet attrs) {

        initAttr(context, attrs);

        setBackgroundResource(mNoSelectedBgDrawable);
        setTextColor(mNoSelectedColor);

    }

    private void initAttr(Context context, AttributeSet attrs) {
        if(attrs == null) {
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PayButton);
        if(ta != null) {
            mSelectedBgDrawable = ta.getDrawable(R.styleable.PayButton_color_bg);
            mNoSelectedColor = ta.getColor(R.styleable.PayButton_color_text_no_selected, Color.BLACK);
            mSelectedColor = ta.getColor(R.styleable.PayButton_color_text_selected, Color.WHITE);

            ta.recycle();
        }
    }

    public void setPaySelected(boolean isSelected) {
        if(isSelected) {
            setBackground(mSelectedBgDrawable);
            setTextColor(mSelectedColor);
        } else {
            setBackgroundResource(mNoSelectedBgDrawable);
            setTextColor(mNoSelectedColor);
        }
    }

}

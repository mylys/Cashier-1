package com.easygo.cashier.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-19
 */
public class CountEditText extends ConstraintLayout {
    private EditText editText;

    public CountEditText(Context context) {
        super(context);
        init(context, null);
    }

    public CountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View mView = LayoutInflater.from(context).inflate(R.layout.layout_edit_count, this, true);
        editText = mView.findViewById(R.id.edit_goods_count);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mListener != null) {
                    mListener.onCountChanged(s.toString());
                }
            }
        });
    }

    public void setCount(String count) {
        editText.setText(count);
    }
    private OnCountListener mListener;

    public void setOnCountListener(OnCountListener listener) {
        this.mListener = listener;
    }

    public interface OnCountListener {
        void onCountChanged(String count);
    }
}

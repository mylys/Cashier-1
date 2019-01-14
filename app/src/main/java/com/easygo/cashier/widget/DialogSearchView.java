package com.easygo.cashier.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @Describe：
 * @date：2019-01-08
 */
public class DialogSearchView extends ConstraintLayout {
    private EditText editText;
    private ImageView ivClear;
    private OnSearchChangeListener listener;

    public interface OnSearchChangeListener {
        void onSearchClick(String content);
    }

    public void setOnSearchChangeListener(OnSearchChangeListener listener) {
        this.listener = listener;
    }

    public DialogSearchView(Context context) {
        super(context);
        init(context, null);
    }

    public DialogSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View mView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_search_view, this, true);
        editText = mView.findViewById(R.id.edit_search_phone);
        ivClear = mView.findViewById(R.id.iv_clear);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ivClear.setVisibility(s.toString().length() != 0 ? VISIBLE : GONE);
                if (listener != null) {
                    listener.onSearchClick(editText.getText().toString().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ivClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    public EditText getEditText() {
        if (editText != null) {
            return editText;
        }
        return null;
    }

    public String getContent() {
        return editText.getText().toString().trim();
    }

    public void setHint(String hint) {
        if (editText != null) {
            editText.setHint(hint);
        }
    }

    public void setContent(String string) {
        editText.setText(string);
    }
}

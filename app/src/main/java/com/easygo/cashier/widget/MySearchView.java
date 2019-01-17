package com.easygo.cashier.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MySearchView extends ConstraintLayout {


    private String mHint;
    private int mSearchTextPaddingStart;
    private int mSearchBtnTextSize;
    private int mSearchBtnWidth;
    private int mSearchBtnHeight;
    private View mView;
    private EditText mEditText;
    private Button mSearchBtn;
    private ConstraintLayout mClear;
    private String keyword = "";

    /**时间间隔*/
    private long interval = 1000;


    public MySearchView(Context context) {
        super(context);
        init(context, null);
    }

    public MySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(final Context context, AttributeSet attrs) {

        initView(context);

        initAttr(context, attrs);

        //设置搜索按钮大小
        if (mSearchBtnWidth != -1 && mSearchBtnHeight != -1) {
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) mSearchBtn.getLayoutParams();
            if (lp == null)
                lp = new LayoutParams(-2, -2);
            lp.width = mSearchBtnWidth;
            lp.height = mSearchBtnHeight;
            mSearchBtn.setLayoutParams(lp);
        }

        Resources res = getResources();
        if (mSearchTextPaddingStart != -1) {
            mEditText.setPadding(mSearchTextPaddingStart, 0, 0, 0);
        }
        if (mSearchBtnTextSize != -1) {
            mSearchBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSearchBtnTextSize);
        }


        if (TextUtils.isEmpty(mHint)) {
            mHint = res.getString(R.string.text_search_hint_barcode);
        }
        mEditText.setHint(mHint);

        mClear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditText.setText("");
            }
        });

        final Runnable textRunnable = new Runnable() {
            @Override
            public void run() {
                //两次输入间隔大于 指定时间时
                if(listener != null) {
                    Log.i("test", "run: 开始搜索");
                    listener.onSearch(keyword);
                }
            }
        };
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString();
                mClear.setVisibility(s.length() > 0 ? VISIBLE : GONE);

                //指定时间后调用搜索按钮逻辑
                mEditText.removeCallbacks(textRunnable);
                mEditText.postDelayed(textRunnable, interval);

            }
        });
//        mEditText.setRawInputType(Configuration.KEYBOARD_QWERTY);
        mEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);

        mEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    onScanCode(mEditText.getText().toString().trim());
                }
                return false;
            }
        });

    }

    private void onScanCode(String code) {

    }

    public EditText getEditText() {
        return mEditText;
    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_search_view, this, true);
        mEditText = (EditText) mView.findViewById(R.id.et_search);
        mSearchBtn = (Button) mView.findViewById(R.id.btn_search);
        mClear = (ConstraintLayout) mView.findViewById(R.id.cl_clear);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MySearchView);
        if (ta != null) {


            mHint = ta.getString(R.styleable.MySearchView_search_hint);
            mSearchTextPaddingStart = ta.getDimensionPixelSize(R.styleable.MySearchView_search_text_padding_start, -1);
            mSearchBtnTextSize = (int) ta.getDimensionPixelSize(R.styleable.MySearchView_search_btn_text_size, -1);
            mSearchBtnWidth = ta.getDimensionPixelSize(R.styleable.MySearchView_search_btn_width, -1);
            mSearchBtnHeight = ta.getDimensionPixelSize(R.styleable.MySearchView_search_btn_height, -1);

            ta.recycle();
        }

        mSearchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSearch(keyword);
                }
            }
        });
    }

    OnSearhListenerClick listener;

    public void setFocuable(boolean focuable) {
        mEditText.setFocusable(focuable);
        mEditText.setFocusableInTouchMode(focuable);

        if (focuable) {
            mEditText.requestFocus();
        }else{
            mEditText.clearFocus();
        }
    }

    public interface OnSearhListenerClick {
        void onSearch(String content);
    }

    public void setOnSearchListenerClick(OnSearhListenerClick onSearchListenerClick) {
        this.listener = onSearchListenerClick;
    }


    public void clearText() {
        this.mEditText.setText("");
    }

    public int getSearchResultWidth() {
        return getWidth() - mSearchBtn.getWidth();
    }

    //设置时间间隔
    public void setInterval(long interval) {
        this.interval = interval;
    }

}

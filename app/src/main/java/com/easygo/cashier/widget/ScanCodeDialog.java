package com.easygo.cashier.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;


public class ScanCodeDialog extends Dialog {

    private ImageView mLogo;
    private ProgressBar mLoading;
    private TextView mTextView;
    private ConstraintLayout mClose;
    private EditText etBarcode;

    Handler mHandler = new Handler();

    public ScanCodeDialog(@NonNull Context context) {
        super(context);
    }

    public ScanCodeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_scan_dialog);

        mLogo = findViewById(R.id.iv_logo);
        mLoading = findViewById(R.id.loading);
        mTextView = findViewById(R.id.tv_description);
        mClose = findViewById(R.id.cl_close);
        etBarcode = findViewById(R.id.editText_barcode);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        etBarcode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (mListener != null) {
                        mListener.onScanCode(etBarcode.getText().toString().trim());
                    }
                    etBarcode.setText("");
                }

                return false;
            }
        });

        etBarcode.setFocusable(true);
        etBarcode.setFocusableInTouchMode(true);
        etBarcode.setShowSoftInputOnFocus(false);
        etBarcode.requestFocus();


    }

    public static final int STATUS_SCAN = 0;
    public static final int STATUS_SCANNING = 1;
    public static final int STATUS_SUCCESSFUL_RECEIPT = 2;

    private int mStatus = STATUS_SCAN;
    public void setStatus(int status) {
        switch (status) {
            case STATUS_SCAN:

                mLogo.setImageResource(R.drawable.dialog_scan);
                mLogo.setVisibility(View.VISIBLE);
                mClose.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.GONE);
                mTextView.setText(R.string.text_scan_code);
                break;
            case STATUS_SCANNING:

                mLogo.setVisibility(View.GONE);
                mClose.setVisibility(View.GONE);
                mLoading.setVisibility(View.VISIBLE);
                mTextView.setText(R.string.text_scanning_code);
                break;

            case STATUS_SUCCESSFUL_RECEIPT:

                mLogo.setImageResource(R.drawable.ic_scan_tick);
                mLogo.setVisibility(View.VISIBLE);
                mClose.setVisibility(View.GONE);
                mLoading.setVisibility(View.GONE);
                mTextView.setText(R.string.text_successful_receipt);

//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(isShowing())
//                            dismiss();
//                    }
//                }, 2000);
                break;
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

//        if(event.getAction() == MotionEvent.ACTION_DOWN) {
//            mStatus++;
//            if(mStatus > 2) {
//                mStatus = 0;
//            }
//
//            setStatus(mStatus);
//        }
        return super.onTouchEvent(event);
    }

    private OnScanCodeListener mListener;
    public void setOnScanCodeListener(OnScanCodeListener listener) {
        this.mListener = listener;
    }
    public interface OnScanCodeListener {
        void onScanCode(String barcode);
    }

}

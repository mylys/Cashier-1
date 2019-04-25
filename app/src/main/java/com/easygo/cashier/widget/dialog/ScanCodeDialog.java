package com.easygo.cashier.widget.dialog;

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

    protected ImageView mLogo;
    protected ProgressBar mLoading;
    protected TextView mTextView;
    protected ConstraintLayout mClose;
    protected EditText etBarcode;
    protected String mText;

    /**
     * 是否禁止回调 扫码数据
     */
    private boolean mStopScan = false;

    protected Handler mHandler = new Handler();

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
                    if (!mStopScan && mListener != null) {
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
    public static final int STATUS_MEMBER_NULL = 3;
    public static final int STATUS_COUPON_NULL = 4;
    public static final int STATUS_SCAN_GIFT_CARD = 5;
    public static final int STATUS_GIFT_CARD_NULL = 6;

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

                break;
            case STATUS_MEMBER_NULL:
            case STATUS_COUPON_NULL:
                mLogo.setImageResource(R.drawable.icon_null);
                mLogo.setVisibility(View.VISIBLE);
                mClose.setVisibility(View.GONE);
                mLoading.setVisibility(View.GONE);
                mTextView.setText(status == STATUS_MEMBER_NULL ? R.string.text_no_member : R.string.text_no_coupon);

                delayedDismiss(2000);
                break;

            case STATUS_SCAN_GIFT_CARD:
                mLogo.setImageResource(R.drawable.dialog_scan);
                mLogo.setVisibility(View.VISIBLE);
                mClose.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.GONE);
                mTextView.setText(R.string.text_scan_gift_card_code);
                break;
            case STATUS_GIFT_CARD_NULL:
                mLogo.setImageResource(R.drawable.icon_null);
                mLogo.setVisibility(View.VISIBLE);
                mClose.setVisibility(View.GONE);
                mLoading.setVisibility(View.GONE);
                mTextView.setText(R.string.text_no_gift_card);

                delayedDismiss(2000);
                break;

        }
    }

    private void delayedDismiss(int delayed) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isShowing())
                    dismiss();
            }
        }, delayed);
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

    public void setStopScan(boolean stopScan) {
        this.mStopScan = stopScan;
    }

}

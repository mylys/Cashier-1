package com.easygo.cashier.widget.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.easygo.cashier.R;

import java.util.LinkedList;

import androidx.constraintlayout.widget.ConstraintLayout;


public class ScanCodeDialog extends MyBaseDialog {

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_FRAME, R.style.DialogStyle);
        setCancelable(false);
    }

    @Override
    protected void initView(View rootView) {
        mLogo = rootView.findViewById(R.id.iv_logo);
        mLoading = rootView.findViewById(R.id.loading);
        mTextView = rootView.findViewById(R.id.tv_description);
        mClose = rootView.findViewById(R.id.cl_close);
        etBarcode = rootView.findViewById(R.id.editText_barcode);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

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

    @Override
    protected int getLayoutId() {
        return R.layout.layout_scan_dialog;
    }

    @Override
    protected int getLayoutWidth() {
        return getResources().getDimensionPixelSize(R.dimen.scan_dialog_width);
    }

    @Override
    protected int getLayoutHeight() {
        return getResources().getDimensionPixelSize(R.dimen.scan_dialog_height);
    }

    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        if(window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            lp.y = getResources().getDimensionPixelSize(R.dimen.y411);
            window.setAttributes(lp);
        }
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

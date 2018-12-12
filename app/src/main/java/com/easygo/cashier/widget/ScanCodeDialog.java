package com.easygo.cashier.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;

import androidx.constraintlayout.widget.ConstraintLayout;


public class ScanCodeDialog extends Dialog {

    private ImageView mLogo;
    private TextView mTextView;
    private ConstraintLayout mClose;

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
        mTextView = findViewById(R.id.tv_description);
        mClose = findViewById(R.id.cl_close);

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }

    public static final int STATUS_SCAN = 0;
    public static final int STATUS_SCANNING = 1;
    public static final int STATUS_SUCCESSFUL_RECEIPT = 2;

    private int mStatus = STATUS_SCAN;
    public void setStatus(int status) {
        switch (status) {
            case STATUS_SCAN:

                mLogo.setImageResource(R.mipmap.ic_launcher);
                mTextView.setText(R.string.text_scan_code);
                break;
            case STATUS_SCANNING:

                mLogo.setImageResource(R.mipmap.ic_launcher);
                mTextView.setText(R.string.text_scanning_code);
                break;

            case STATUS_SUCCESSFUL_RECEIPT:

                mLogo.setImageResource(R.mipmap.ic_launcher);
                mTextView.setText(R.string.text_successful_receipt);
                break;
        }
    }

    public int getDialogWidth() {
        return (int) getContext().getResources().getDimension(R.dimen.scan_dialog_width);
    }
    public int getDialogHeight() {
        return (int) getContext().getResources().getDimension(R.dimen.scan_dialog_height);
    }
}

package com.easygo.cashier.module.refund;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.easygo.cashier.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe：
 * @author：hgeson
 * @date：2018-12-18
 */
public class RefundCashView extends FrameLayout {

    @BindView(R.id.tv_cash_maney)
    TextView tvCashManey;
    @BindView(R.id.tv_refund_cash)
    TextView tvRefundCash;
    @BindView(R.id.tv_refound_maney)
    TextView tvRefoundManey;
    @BindView(R.id.cb_print)
    CheckBox cbPrint;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private Unbinder unbinder;
    private View mView;

    public RefundCashView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public static RefundCashView create(Context context) {
        return new RefundCashView(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_refound_cash, this, true);
        unbinder = ButterKnife.bind(this, mView);
        tvRefundCash.setText(scanSpannable());

        cbPrint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mListener != null) {
                    mListener.onPrintClicked(isChecked);
                }
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommitOrderClicked();
                }
            }
        });
    }

    public void setData(float receivable_refund, float refund_cash) {
        DecimalFormat df = new DecimalFormat("0.00");
        tvCashManey.setText("￥" + df.format(receivable_refund));
        tvRefoundManey.setText("￥" + df.format(refund_cash));
    }

    public void release() {
        if (unbinder != null)
            unbinder.unbind();
    }

    private SpannableString scanSpannable() {
        SpannableString spannableString = new SpannableString(tvRefundCash.getText().toString());
        spannableString.setSpan(new ForegroundColorSpan(Color.rgb(214, 68, 68)), spannableString.length() - 4, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    private OnClickListener mListener;

    public void setOnClickListener(OnClickListener listener) {
        this.mListener = listener;
    }

    public interface OnClickListener {
        void onPrintClicked(boolean isChecked);
        void onCommitOrderClicked();
    }
}

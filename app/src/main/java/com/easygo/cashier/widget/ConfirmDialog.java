package com.easygo.cashier.widget;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.easygo.cashier.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 确认对话框
 */
public class ConfirmDialog extends DialogFragment {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_receivable)
    TextView tvReceivable;
    @BindView(R.id.tv_receipts)
    TextView tvReceipts;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_pay_way)
    TextView tvPayWay;
    private Unbinder unbinder;

    private float mReceivable;
    private float mReceipts;
    private float mChange;
    private int mPayWay;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NO_FRAME, R.style.CustomDialogStyle);
    }

    public static Bundle getDataBundle(float receivable, float receipts, float change, int pay_way) {
        Bundle bundle = new Bundle();
        bundle.putFloat("receivable", receivable);
        bundle.putFloat("receipts", receipts);
        bundle.putFloat("change", change);
        bundle.putInt("pay_way", pay_way);
        return bundle;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog, container, false);

        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        Bundle bundle = getArguments();
        if(bundle != null) {
            mReceivable = bundle.getFloat("receivable");
            mReceipts = bundle.getFloat("receipts");
            mChange = bundle.getFloat("change");
            mPayWay = bundle.getInt("pay_way");
        }

        tvReceivable.setText("￥" + mReceivable);
        tvReceipts.setText("￥" + mReceipts);
        tvChange.setText("￥" + mChange);

        switch (mPayWay) {
            case PayWayView.WAY_CASH:
                tvPayWay.setText("（现金）");
                break;
            case PayWayView.WAY_ALIPAY:
                tvPayWay.setText("（支付宝）");
                break;
            case PayWayView.WAY_WECHAT:
                tvPayWay.setText("（微信）");
                break;
        }

    }


    @Override
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        if (window != null) {
            window.setLayout(getResources().getDimensionPixelSize(R.dimen.dialog_width),
                    WindowManager.LayoutParams.WRAP_CONTENT);
        }

        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @OnClick({R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                if (mListener != null) {
                    mListener.onConfirm();
                }
                dismiss();
                break;
        }
    }

    private OnConfirmListenr mListener;

    public interface OnConfirmListenr {
        void onConfirm();
    }

    public void setOnConfirmListener(OnConfirmListenr listener) {
        this.mListener = listener;
    }
}

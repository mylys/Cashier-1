package com.easygo.cashier.widget.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.easygo.cashier.R;

import java.text.DecimalFormat;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 确认对话框
 */
public class ConfirmDialog extends MyBaseDialog {


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
    @BindView(R.id.tv_text_receivable)
    TextView tvTextReceivable;
    @BindView(R.id.tv_text_receipts)
    TextView tvTextReceipts;
    @BindView(R.id.tv_text_change)
    TextView tvTextChange;
    private Unbinder unbinder;

    private float mReceivable;
    private float mReceipts;
    private float mChange;
    private String mPayWay;
    private String mRefundNum = "";


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

    public static Bundle getDataBundle(String refund_num, float receipts, String pay_way, boolean isVisiable, String... str) {
        Bundle bundle = new Bundle();
        bundle.putString("refund_num", refund_num);
        bundle.putFloat("receipts", receipts);
        bundle.putString("pay_way", pay_way);
        bundle.putBoolean("visiable", isVisiable);
        bundle.putStringArray("change_text", str);
        return bundle;
    }

    private void init() {
        DecimalFormat df = new DecimalFormat("0.00");

        boolean visiable = true;
        String[] strings = new String[]{"应收", "实收"};
        Bundle bundle = getArguments();
        if (bundle != null) {
            mReceivable = bundle.getFloat("receivable");
            mReceipts = bundle.getFloat("receipts");
            mChange = bundle.getFloat("change");
            mPayWay = bundle.getString("pay_way");
            mRefundNum = bundle.getString("refund_num");
            visiable = bundle.getBoolean("visiable", true);
            strings = bundle.getStringArray("change_text");
        }

        if (strings != null) {
            tvTextReceivable.setText(strings[0]);
            tvTextReceipts.setText(strings[1]);
        }
        tvTextChange.setVisibility(visiable ? View.VISIBLE : View.GONE);
        tvChange.setVisibility(visiable ? View.VISIBLE : View.GONE);

        tvReceivable.setText("￥" + df.format(mReceivable));
        tvReceipts.setText("￥" + df.format(mReceipts));
        tvChange.setText("￥" + df.format(mChange));
        if (!TextUtils.isEmpty(mRefundNum)){
            tvReceivable.setText(mRefundNum + "件");
            tvTitle.setText(R.string.text_confirm_refund);
        }

        tvPayWay.setText("(" + mPayWay + ")");

//        switch (mPayWay) {
//            case PayWayView.WAY_CASH:
//                tvPayWay.setText("(现金)");
//                break;
//            case PayWayView.WAY_ALIPAY:
//                tvPayWay.setText("(支付宝)");
//                break;
//            case PayWayView.WAY_WECHAT:
//                tvPayWay.setText("(微信)");
//                break;
//            case PayWayView.WAY_MEMBER:
//                tvPayWay.setText("(会员卡)");
//                break;
//        }

        int size = getResources().getDimensionPixelSize(R.dimen.x109);
        if(mPayWay.length() > 8) {
            size = getResources().getDimensionPixelSize(R.dimen.x50);
        } else if(mPayWay.length() > 4) {
            size = getResources().getDimensionPixelSize(R.dimen.x90);
        }
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) tvTextReceivable.getLayoutParams();
        lp.leftMargin = size;
        tvTextReceivable.setLayoutParams(lp);
        lp = (ConstraintLayout.LayoutParams) tvReceivable.getLayoutParams();
        lp.rightMargin = size;
        tvReceivable.setLayoutParams(lp);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    protected void initView(View rootView) {
        unbinder = ButterKnife.bind(this, rootView);

        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_dialog;
    }

    @Override
    protected int getLayoutWidth() {
        return getResources().getDimensionPixelSize(R.dimen.dialog_width);
    }

    @Override
    protected int getLayoutHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }


    public void showCenter(FragmentActivity activity) {
        showCenter(activity, "DIALOG_CONFIRM");
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

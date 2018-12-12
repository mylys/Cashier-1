package com.easygo.cashier.module.settlement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.widget.PayWayView;
import com.easygo.cashier.widget.ScanCodeDialog;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 结算详情（应收、实收等）
 */
public class SettlementView extends FrameLayout {

    @BindView(R.id.tv_receivable)
    TextView tvReceivable;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_text_receipts_way)
    TextView tvReceiptsWay;
    @BindView(R.id.tv_receipts)
    TextView tvReceipts;
    @BindView(R.id.line4)
    View line4;
    @BindView(R.id.tv_text_change)
    TextView tvTextChange;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.cl_scan)
    ConstraintLayout clScan;
    @BindView(R.id.tv_already_settlement)
    TextView tvAlreadySettlement;
    private Unbinder unbinder;
    private View mView;
    private ScanCodeDialog mScanCodeDialog;

    public SettlementView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_settlement_detail, this, true);
        unbinder = ButterKnife.bind(this, mView);


        tvAlreadySettlement.setVisibility(View.GONE);
        clScan.setVisibility(View.GONE);

        setShowSoftInputOnFocus(false);

        setPayType(false, PayWayView.WAY_CASH);

        clScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mListener != null) {
                    mListener.onScanClicked();
                }
                showScanCodeDialog();
            }
        });
    }



    public static SettlementView create(Context context) {
        return new SettlementView(context);
    }


    /**
     * 设置EditText获取焦点时 是否弹出软键盘
     * @param showSoftInputOnFocus
     */
    private void setShowSoftInputOnFocus(boolean showSoftInputOnFocus) {
        tvReceipts.setShowSoftInputOnFocus(showSoftInputOnFocus);
    }

    /**
     * 根据选择的支付方式，改变相应布局
     */
    public void setPayType(boolean isCombinePay, int payType) {
        if(isCombinePay) {
            tvReceiptsWay.setText(R.string.text_cash);
            showAlreadySettlement(false);
            setScanVisibility(false);
            setChangeVisibilty(true);

            if(payType != PayWayView.WAY_CASH) {
                switch (payType) {
                    case PayWayView.WAY_ALIPAY:
                        tvReceiptsWay.setText(R.string.text_alipay);
                        break;
                    case PayWayView.WAY_WECHAT:
                        tvReceiptsWay.setText(R.string.text_wechat);
                        break;
                    case PayWayView.WAY_BANK_CARD:
                        tvReceiptsWay.setText(R.string.text_bank_card);
                        break;
                    case PayWayView.WAY_OTHER:
                        tvReceiptsWay.setText(R.string.text_other);
                        break;
                }
            }

        } else
        switch (payType) {
            case PayWayView.WAY_CASH://现金
                tvReceiptsWay.setText(R.string.text_cash);
                showAlreadySettlement(false);
                setScanVisibility(false);
                setChangeVisibilty(true);

                break;
            case PayWayView.WAY_WECHAT://微信
                tvReceiptsWay.setText(R.string.text_wechat);
                showAlreadySettlement(false);
                setScanVisibility(true);
                setChangeVisibilty(false);

                break;
            case PayWayView.WAY_ALIPAY://支付宝
                tvReceiptsWay.setText(R.string.text_alipay);
                showAlreadySettlement(false);
                setScanVisibility(true);
                setChangeVisibilty(false);

                break;
            case PayWayView.WAY_BANK_CARD://银行卡
                tvReceiptsWay.setText(R.string.text_bank_card);
                showAlreadySettlement(false);
                setScanVisibility(false);
                setChangeVisibilty(false);

                break;
            case PayWayView.WAY_OTHER://其他
                tvReceiptsWay.setText(R.string.text_other);
                showAlreadySettlement(false);
                setScanVisibility(false);
                setChangeVisibilty(false);

                break;
        }
    }

    public void setChangeVisibilty(boolean visibilty) {
        line4.setVisibility(visibilty? View.VISIBLE: View.GONE);
        tvTextChange.setVisibility(visibilty? View.VISIBLE: View.GONE);
        tvChange.setVisibility(visibilty? View.VISIBLE: View.GONE);
    }
    public void setScanVisibility(boolean visibilty) {
        clScan.setVisibility(visibilty? View.VISIBLE: View.GONE);
    }
    public void showAlreadySettlement(boolean visibilty) {
        tvAlreadySettlement.setVisibility(visibilty? View.VISIBLE: View.GONE);
    }



    /**
     * 显示扫码弹窗
     */
    public void showScanCodeDialog() {
        if(mScanCodeDialog == null) {
            mScanCodeDialog = new ScanCodeDialog(getContext(), R.style.DialogStyle);
            WindowManager.LayoutParams lp = mScanCodeDialog.getWindow().getAttributes();
            lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            lp.y = getResources().getDimensionPixelSize(R.dimen.y411);
            mScanCodeDialog.getWindow().setAttributes(lp);
            mScanCodeDialog.setCanceledOnTouchOutside(false);
        }
        mScanCodeDialog.show();

    }

    private OnClickListener mListener;
    public void setOnSettlementClickListener(OnClickListener listener) {
        this.mListener = listener;
    }
    public interface OnClickListener {
        void onScanClicked();
        void onPrintClicked();
        void onCommitOrderClicked();
    }


    public void release() {
        if (unbinder != null)
            unbinder.unbind();
        if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
            mScanCodeDialog.dismiss();
        }
    }
}

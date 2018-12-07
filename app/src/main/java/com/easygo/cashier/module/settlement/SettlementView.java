package com.easygo.cashier.module.settlement;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    @BindView(R.id.et_receipts)
    EditText etReceipts;
    @BindView(R.id.cl_receipts)
    ConstraintLayout clReceipts;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.tv_text_change)
    TextView tvTextChange;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.tv_already_settlement)
    TextView tvAlreadySettlement;
    @BindView(R.id.tv_combine_way)
    TextView tvCombineWay;
    @BindView(R.id.tv_combine_money)
    TextView tvCombineMoney;
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
        ivScan.setVisibility(View.GONE);
        tvCombineWay.setVisibility(View.GONE);
        tvCombineMoney.setVisibility(View.GONE);

        setShowSoftInputOnFocus(false);

        setPayType(false, PayWayView.WAY_CASH);

        ivScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        etReceipts.setShowSoftInputOnFocus(showSoftInputOnFocus);
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
            showReceiptsFrame(true);

            if(payType != PayWayView.WAY_CASH) {
                switch (payType) {
                    case PayWayView.WAY_ALIPAY:
                        tvTextChange.setText(R.string.text_alipay);
                        break;
                    case PayWayView.WAY_WECHAT:
                        tvTextChange.setText(R.string.text_wechat);
                        break;
                    case PayWayView.WAY_BANK_CARD:
                        tvTextChange.setText(R.string.text_alipay);
                        break;
                    case PayWayView.WAY_OTHER:
                        tvTextChange.setText(R.string.text_other);
                        break;
                }
                tvTextChange.setTextColor(getResources().getColor(R.color.color_bg_cc565a));
            }

        } else
        switch (payType) {
            case PayWayView.WAY_CASH://现金
                tvTextChange.setText(R.string.text_change);
                tvTextChange.setTextColor(getResources().getColor(R.color.color_text));
                tvReceiptsWay.setText(R.string.text_cash);
                showAlreadySettlement(false);
                setScanVisibility(false);
                setChangeVisibilty(true);
                showReceiptsFrame(true);

                break;
            case PayWayView.WAY_WECHAT://微信
                tvReceiptsWay.setText(R.string.text_wechat);
                showAlreadySettlement(false);
                setScanVisibility(true);
                setChangeVisibilty(false);
                showReceiptsFrame(false);

                break;
            case PayWayView.WAY_ALIPAY://支付宝
                tvReceiptsWay.setText(R.string.text_alipay);
                showAlreadySettlement(false);
                setScanVisibility(true);
                setChangeVisibilty(false);
                showReceiptsFrame(false);

                break;
            case PayWayView.WAY_BANK_CARD://银行卡
                tvReceiptsWay.setText(R.string.text_bank_card);
                showAlreadySettlement(false);
                setScanVisibility(false);
                setChangeVisibilty(false);
                showReceiptsFrame(false);

                break;
            case PayWayView.WAY_OTHER://其他
                tvReceiptsWay.setText(R.string.text_other);
                showAlreadySettlement(false);
                setScanVisibility(false);
                setChangeVisibilty(false);
                showReceiptsFrame(false);

                break;
        }
    }

    public void setChangeVisibilty(boolean visibilty) {
        line3.setVisibility(visibilty? View.VISIBLE: View.GONE);
        tvTextChange.setVisibility(visibilty? View.VISIBLE: View.GONE);
        tvChange.setVisibility(visibilty? View.VISIBLE: View.GONE);
    }
    public void setScanVisibility(boolean visibilty) {
        ivScan.setVisibility(visibilty? View.VISIBLE: View.GONE);
    }
    public void showAlreadySettlement(boolean visibilty) {
        tvAlreadySettlement.setVisibility(visibilty? View.VISIBLE: View.GONE);
    }

    public void showReceiptsFrame(boolean visibilty) {
        clReceipts.setBackgroundResource(visibilty? R.drawable.bg_frame: Color.TRANSPARENT);
        if(visibilty) {
            etReceipts.setInputType(InputType.TYPE_CLASS_TEXT);
            etReceipts.setSelection(etReceipts.length());
        } else {
            etReceipts.setInputType(InputType.TYPE_NULL);
        }
        etReceipts.setCursorVisible(visibilty);
    }

    public EditText getEditText() {
        return etReceipts;
    }

    /**
     * 显示扫码弹窗
     */
    public void showScanCodeDialog() {
        if(mScanCodeDialog == null) {
            mScanCodeDialog = new ScanCodeDialog(getContext(), R.style.DialogStyle);
            WindowManager.LayoutParams lp = mScanCodeDialog.getWindow().getAttributes();
            lp.gravity = Gravity.START;

            lp.x = (int) (getWidth() / 2f - mScanCodeDialog.getDialogWidth() / 2f);
            lp.y = (int) (getHeight() / 2f - mScanCodeDialog.getDialogHeight() / 2f);
            mScanCodeDialog.getWindow().setAttributes(lp);
        }
        mScanCodeDialog.show();

    }


    public void release() {
        if (unbinder != null)
            unbinder.unbind();
        if (mScanCodeDialog != null && mScanCodeDialog.isShowing()) {
            mScanCodeDialog.dismiss();
        }
    }
}

package com.easygo.cashier.module.settlement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.widget.ChooseCouponsDialog;
import com.easygo.cashier.widget.PayWayView;
import com.easygo.cashier.widget.ScanCodeDialog;

import java.text.DecimalFormat;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 结算详情（应收、实收等）
 */
public class SettlementView extends FrameLayout {

    @BindView(R.id.cb_integral)
    CheckBox cbIntegral;
    @BindView(R.id.tv_text_balance)
    TextView tvTextBalance;
    @BindView(R.id.tv_text_balance_price)
    TextView tvTextBalancePrice;
    @BindView(R.id.tv_text_coupon_colon)
    TextView tvTextCouponColon;
    @BindView(R.id.tv_coupon_colon_price)
    TextView tvCouponColonPrice;
    @BindView(R.id.image_clear)
    ImageView imageClear;
    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tv_receivable)
    TextView tvReceivable;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_text_receipts_way)
    TextView tvReceiptsWay;
    @BindView(R.id.tv_receipts)
    TextView tvReceipts;
    @BindView(R.id.line5)
    View line5;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.tv_text_change)
    TextView tvTextChange;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.cl_scan)
    ConstraintLayout clScan;
    @BindView(R.id.tv_already_settlement)
    TextView tvAlreadySettlement;
    @BindView(R.id.cb_print)
    CheckBox cbPrint;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private Unbinder unbinder;
    private View mView;

    private ChooseCouponsDialog dialog;
    //结算数据
    private float mReceivable;//应收
    private float mCoupon;//优惠
    private float mReceipts;//实收
    private float mChange;//找零
    private String mBalance;//会员钱包余额

    private boolean mAlreadySettlement;
    //结算数据

    //是否有优惠券
    private boolean isCoupon = false;

    public SettlementView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_settlement_detail, this, true);
        unbinder = ButterKnife.bind(this, mView);


        tvAlreadySettlement.setVisibility(View.GONE);
        clScan.setVisibility(View.GONE);


        setPayType(false, PayWayView.WAY_CASH);

        clScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onScanClicked();
                }
            }
        });
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
        imageClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCoupon = false;
                setCouponVisiable(isCoupon);
            }
        });
    }


    public static SettlementView create(Context context) {
        return new SettlementView(context);
    }


    /**
     * 根据选择的支付方式，改变相应布局
     */
    public void setPayType(boolean isCombinePay, int payType) {
        if (isCombinePay) {
            tvReceiptsWay.setText(R.string.text_cash);
            showAlreadySettlement(false);
            setScanVisibility(false);
            setChangeVisibilty(true);

            if (payType != PayWayView.WAY_CASH) {
                switch (payType) {
                    case PayWayView.WAY_ALIPAY:
                        tvReceiptsWay.setText(R.string.text_alipay);
                        break;
                    case PayWayView.WAY_WECHAT:
                        tvReceiptsWay.setText(R.string.text_wechat);
                        break;
                    case PayWayView.WAY_MEMBER:
                        tvReceiptsWay.setText(R.string.text_member_card_wallet);
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
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);

                    break;
                case PayWayView.WAY_WECHAT://微信
                    tvReceiptsWay.setText(R.string.text_wechat);
                    showAlreadySettlement(false);
                    setScanVisibility(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);

                    break;
                case PayWayView.WAY_ALIPAY://支付宝
                    tvReceiptsWay.setText(R.string.text_alipay);
                    showAlreadySettlement(false);
                    setScanVisibility(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);

                    break;
                case PayWayView.WAY_MEMBER://会员钱包
                    tvReceiptsWay.setText(R.string.text_member_card_wallet);
                    showAlreadySettlement(false);
                    setScanVisibility(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(true);
                    setCouponVisiable(isCoupon);

                    break;
                case PayWayView.WAY_BANK_CARD://银行卡
                    tvReceiptsWay.setText(R.string.text_bank_card);
                    showAlreadySettlement(false);
                    setScanVisibility(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);

                    break;
                case PayWayView.WAY_OTHER://其他
                    tvReceiptsWay.setText(R.string.text_other);
                    showAlreadySettlement(false);
                    setScanVisibility(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);

                    break;
            }
        if (payType == PayWayView.WAY_COUPON) {
            if (dialog == null) {
                dialog = new ChooseCouponsDialog();
            }
            dialog.showCenter((CashierActivity) getContext());
            dialog.setTitle(getResources().getString(R.string.text_coupon_coupon));
        }
    }

    public void setCouponVisiable(boolean isCoupon) {
        imageClear.setVisibility(isCoupon ? View.VISIBLE : View.GONE);
        tvTextCouponColon.setVisibility(isCoupon ? View.VISIBLE : View.GONE);
        tvCouponColonPrice.setVisibility(isCoupon ? View.VISIBLE : View.GONE);
        line3.setVisibility(isCoupon ? View.VISIBLE : View.GONE);
    }

    public void setMemberVisiable(boolean visiable) {
        cbIntegral.setVisibility(visiable ? View.VISIBLE : View.GONE);
        view1.setVisibility(visiable ? View.VISIBLE : View.GONE);
        tvTextBalance.setVisibility(visiable ? View.VISIBLE : View.GONE);
        tvTextBalancePrice.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    public void setChangeVisibilty(boolean visibilty) {
        line5.setVisibility(visibilty ? View.VISIBLE : View.GONE);
        tvTextChange.setVisibility(visibilty ? View.VISIBLE : View.GONE);
        tvChange.setVisibility(visibilty ? View.VISIBLE : View.GONE);
    }

    public void setScanVisibility(boolean visibilty) {
        clScan.setVisibility(visibilty ? View.VISIBLE : View.GONE);
    }

    public void showAlreadySettlement(boolean visibilty) {
        tvAlreadySettlement.setVisibility(visibilty ? View.VISIBLE : View.GONE);
    }

    public void setAlreadySettlement() {
        mAlreadySettlement = true;

        tvAlreadySettlement.setVisibility(VISIBLE);
        clScan.setVisibility(GONE);
    }

    public boolean hasAlreadySettlement() {
        return this.mAlreadySettlement;
    }


    public void setData(float receivable, float coupon, float receipts, float change,String balance) {
        mReceivable = receivable;
        mCoupon = coupon;
        mReceipts = receipts;
        mChange = change;
        mBalance = balance;

        DecimalFormat df = new DecimalFormat("#0.00");

        String sign = "￥";
        tvReceivable.setText(sign + df.format(mReceivable));
        tvCoupon.setText(sign + df.format(mCoupon));
        tvReceipts.setText(sign + df.format(mReceipts));
        tvChange.setText(sign + df.format(mChange));
        tvTextBalancePrice.setText(mBalance);
    }


    private OnClickListener mListener;

    public void setOnSettlementClickListener(OnClickListener listener) {
        this.mListener = listener;
    }

    public boolean needPrint() {
        return cbPrint.isChecked();
    }

    public interface OnClickListener {
        void onScanClicked();

        void onPrintClicked(boolean isChecked);

        void onCommitOrderClicked();
    }


    public void release() {
        if (unbinder != null)
            unbinder.unbind();
    }
}

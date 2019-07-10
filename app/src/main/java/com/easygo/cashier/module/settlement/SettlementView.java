package com.easygo.cashier.module.settlement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.easygo.cashier.widget.view.PayWayView;

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
    @BindView(R.id.btn_cancel_coupon)
    Button btnCancelCoupon;
    @BindView(R.id.tv_text_gift_card_colon)
    TextView tvTextGiftCardColon;
    @BindView(R.id.tv_gift_card_price)
    TextView tvGiftCardPrice;

    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.tv_receivable)
    TextView tvReceivable;
    @BindView(R.id.tv_real_receivable)
    TextView tvRealReceivable;
    @BindView(R.id.tv_cancel_temp_promotion)
    TextView tvCancelTempPromotion;
    @BindView(R.id.btn_cancel_temp_promotion)
    Button btnCancelTempPromotion;
    @BindView(R.id.btn_cancel_gift_card)
    Button btnCancelGiftCard;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.tv_text_receipts_way)
    TextView tvReceiptsWay;
    @BindView(R.id.tv_receipts)
    TextView tvReceipts;
    @BindView(R.id.root)
    ConstraintLayout root;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.line4)
    View line4;
    @BindView(R.id.line5)
    View line5;
    @BindView(R.id.line6)
    View line6;
    @BindView(R.id.tv_text_change)
    TextView tvTextChange;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_already_settlement)
    TextView tvAlreadySettlement;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    private Unbinder unbinder;
    private View mView;

    //结算数据
    private float mReceivable;//应收
    private float mCoupon;//优惠
    private float mReceipts;//实收
    private float mChange;//找零
    private String mBalance;//会员钱包余额

    private boolean mAlreadySettlement;
    //结算数据

    private int payType;
    //是否有优惠券
    private boolean isCoupon = false;
    //是否有礼品卡
    private boolean isGiftCard = false;

    public SettlementView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_settlement_detail, this, true);
        unbinder = ButterKnife.bind(this, mView);


        tvAlreadySettlement.setVisibility(View.GONE);


        setPayType(false, PayWayView.WAY_CASH);

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCommitOrderClicked();
                }
            }
        });
        btnCancelCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCancelCoupon();
                }
            }
        });
        btnCancelTempPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCancelTempPromotion();
                }
            }
        });
        btnCancelGiftCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCancelGiftCard();
                }
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
        this.payType = payType;
        if (isCombinePay) {
            tvReceiptsWay.setText(R.string.text_cash);
            showAlreadySettlement(false);
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
                    case PayWayView.WAY_GIFT_CARD:
                        tvReceiptsWay.setText(R.string.text_gift_card);
                        break;
                }
            }

        } else
            switch (payType) {
                case PayWayView.WAY_CASH://现金
                    tvReceiptsWay.setText(R.string.text_cash);
                    showAlreadySettlement(false);
                    setChangeVisibilty(true);
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);
                    setCancleCouponVisibility(false);
                    setCancleTempPromotionVisibility(false);
                    setCancleGiftCardVisibility(false);
                    setGiftCardVisiable(isGiftCard);

                    break;
                case PayWayView.WAY_WECHAT://微信
                    tvReceiptsWay.setText(R.string.text_wechat);
                    showAlreadySettlement(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);
                    setCancleCouponVisibility(true);
                    setCancleTempPromotionVisibility(true);
                    setCancleGiftCardVisibility(true);
                    setGiftCardVisiable(isGiftCard);

                    break;
                case PayWayView.WAY_ALIPAY://支付宝
                    tvReceiptsWay.setText(R.string.text_alipay);
                    showAlreadySettlement(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);
                    setCancleCouponVisibility(true);
                    setCancleTempPromotionVisibility(true);
                    setCancleGiftCardVisibility(true);
                    setGiftCardVisiable(isGiftCard);

                    break;
                case PayWayView.WAY_MEMBER://会员钱包
                    tvReceiptsWay.setText(R.string.text_member_card_wallet);
                    showAlreadySettlement(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(true);
                    setCouponVisiable(isCoupon);
                    setCancleCouponVisibility(true);
                    setCancleTempPromotionVisibility(true);
                    setCancleGiftCardVisibility(true);
                    setGiftCardVisiable(isGiftCard);

                    break;
                case PayWayView.WAY_BANK_CARD://银行卡
                    tvReceiptsWay.setText(R.string.text_bank_card);
                    showAlreadySettlement(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);
                    setCancleCouponVisibility(true);
                    setCancleTempPromotionVisibility(true);
                    setCancleGiftCardVisibility(true);
                    setGiftCardVisiable(isGiftCard);

                    break;
                case PayWayView.WAY_GIFT_CARD://礼品卡
                    tvReceiptsWay.setText(R.string.text_gift_card);
                    showAlreadySettlement(false);
                    setChangeVisibilty(false);
                    setMemberVisiable(false);
                    setCouponVisiable(isCoupon);
                    setCancleCouponVisibility(true);
                    setCancleTempPromotionVisibility(true);
                    setCancleGiftCardVisibility(true);
                    setGiftCardVisiable(isGiftCard);

                    break;
            }
        updateLineMargin(payType);
    }

    public void setCouponVisiable(boolean isCoupon) {
        this.isCoupon = isCoupon;
        tvTextCouponColon.setVisibility(isCoupon ? View.VISIBLE : View.GONE);
        tvCouponColonPrice.setVisibility(isCoupon ? View.VISIBLE : View.GONE);
        line3.setVisibility(isCoupon ? View.VISIBLE : View.GONE);

        if (payType == PayWayView.WAY_MEMBER && isCoupon && isGiftCard) {
            updateLineMargin(payType);
        }
    }

    public void setGiftCardVisiable(boolean isGiftCard) {
        this.isGiftCard = isGiftCard;
        tvTextGiftCardColon.setVisibility(isGiftCard ? View.VISIBLE : View.GONE);
        tvGiftCardPrice.setVisibility(isGiftCard ? View.VISIBLE : View.GONE);
        line4.setVisibility(isGiftCard ? View.VISIBLE : View.GONE);

        if (payType == PayWayView.WAY_MEMBER && isCoupon && isGiftCard) {
            updateLineMargin(payType);
        }
    }

    public void setMemberVisiable(boolean visiable) {
//        cbIntegral.setVisibility(visiable ? View.VISIBLE : View.GONE);
        view1.setVisibility(visiable ? View.VISIBLE : View.GONE);
        tvTextBalance.setVisibility(visiable ? View.VISIBLE : View.GONE);
        tvTextBalancePrice.setVisibility(visiable ? View.VISIBLE : View.GONE);
    }

    public void setChangeVisibilty(boolean visibilty) {
        line6.setVisibility(visibilty ? View.VISIBLE : View.GONE);
        tvTextChange.setVisibility(visibilty ? View.VISIBLE : View.GONE);
        tvChange.setVisibility(visibilty ? View.VISIBLE : View.GONE);
    }

    public void showAlreadySettlement(boolean visibilty) {
        tvAlreadySettlement.setVisibility(visibilty ? View.VISIBLE : View.GONE);
    }

    public void setAlreadySettlement() {
        mAlreadySettlement = true;

        tvAlreadySettlement.setVisibility(VISIBLE);
    }

    public boolean hasAlreadySettlement() {
        return this.mAlreadySettlement;
    }


    public void setData(float receivable, float coupon, float receipts, float change, String balance) {
        setData(receivable, coupon, 0, 0, 0, receipts, change, balance);
    }

    public void setData(float receivable, float coupon, float couponMoney,
                        float giftCardMoney, float tempOrderPromotionMoney,
                        float receipts, float change, String balance) {
        mReceivable = receivable;
        mCoupon = coupon;
        mReceipts = receipts;
        mChange = change;
        mBalance = balance;

        DecimalFormat df = new DecimalFormat("#0.00");

        String sign = "￥";

        String after = sign + df.format(receivable - coupon - couponMoney - giftCardMoney - tempOrderPromotionMoney);
        String before = sign + df.format(receivable);

        if (coupon == 0 && couponMoney == 0 && giftCardMoney == 0 && tempOrderPromotionMoney == 0) {
            tvRealReceivable.setVisibility(GONE);
            tvReceivable.setText(sign + df.format(mReceivable));
        } else {
            tvRealReceivable.setVisibility(VISIBLE);

            SpannableString spannableAfter = new SpannableString(after);
            ForegroundColorSpan colorSpan1 = new ForegroundColorSpan(getResources().getColor(R.color.color_d64444));
            spannableAfter.setSpan(colorSpan1, 0, spannableAfter.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvRealReceivable.setText(spannableAfter);

            SpannableString spannableBefore = new SpannableString(before);
            ForegroundColorSpan colorSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.color_text_ababab));
            StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
            spannableBefore.setSpan(colorSpan2, 0, spannableBefore.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableBefore.setSpan(strikethroughSpan, 0, spannableBefore.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvReceivable.setText(spannableBefore);
        }

        tvCoupon.setText(sign + df.format(mCoupon + tempOrderPromotionMoney));
        tvReceipts.setText(sign + df.format(mReceipts));
        tvChange.setText(sign + df.format(mChange));
        tvTextBalancePrice.setText(mBalance);
    }

    /**
     * 更新 线之间的间距
     *
     * @param payType 支付方式
     */
    public void updateLineMargin(int payType) {
        int root_dimen = getResources().getDimensionPixelSize(R.dimen.y43);
        int line1_dimen = getResources().getDimensionPixelSize(R.dimen.y42);
        int dimen = getResources().getDimensionPixelSize(R.dimen.y120);
        switch (payType) {
            case PayWayView.WAY_CASH:
//                line1_dimen = getResources().getDimensionPixelSize(R.dimen.y42);
                root_dimen = getResources().getDimensionPixelSize(R.dimen.y100);
//                dimen = getResources().getDimensionPixelSize(R.dimen.y120);
                break;
            case PayWayView.WAY_MEMBER:
                if (isCoupon && isGiftCard) {
                    line1_dimen = getResources().getDimensionPixelSize(R.dimen.y33);
                    dimen = getResources().getDimensionPixelSize(R.dimen.y99);
                }
                break;
            default:

                break;
        }

        FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) root.getLayoutParams();
        flp.topMargin = root_dimen;
        root.setLayoutParams(flp);

        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) line1.getLayoutParams();
        lp.topMargin = line1_dimen;
        line1.setLayoutParams(lp);
        lp = (ConstraintLayout.LayoutParams) line2.getLayoutParams();
        lp.topMargin = dimen;
        line2.setLayoutParams(lp);
        lp = (ConstraintLayout.LayoutParams) line3.getLayoutParams();
        lp.topMargin = dimen;
        line3.setLayoutParams(lp);
        lp = (ConstraintLayout.LayoutParams) line4.getLayoutParams();
        lp.topMargin = dimen;
        line4.setLayoutParams(lp);
        lp = (ConstraintLayout.LayoutParams) line5.getLayoutParams();
        lp.topMargin = dimen;
        line5.setLayoutParams(lp);
        lp = (ConstraintLayout.LayoutParams) line6.getLayoutParams();
        lp.topMargin = dimen;
        lp = (ConstraintLayout.LayoutParams) view1.getLayoutParams();
        lp.topMargin = dimen;
        view1.setLayoutParams(lp);

    }

    /**
     * 设置优惠券信息
     */
    public void setCouponInfo(String name, float coupon_discount) {
        tvTextCouponColon.setText(getResources().getString(R.string.text_coupon_coupon_colon) + "(" + name + ")");
        DecimalFormat df = new DecimalFormat("0.00");
        tvCouponColonPrice.setText(getResources().getString(R.string.text_symbol3) + df.format(coupon_discount));
    }

    /**
     * 设置礼品卡信息
     */
    public void setGiftCardInfo(String name, float price) {
        tvTextGiftCardColon.setText(getResources().getString(R.string.text_gift_card_colon) + "(" + name + ")");
        DecimalFormat df = new DecimalFormat("0.00");
        tvGiftCardPrice.setText(getResources().getString(R.string.text_symbol3) + df.format(price));
    }


    private OnClickListener mListener;

    public void setOnSettlementClickListener(OnClickListener listener) {
        this.mListener = listener;
    }


    public void setBottomButtonVisibility(boolean needShowKeyboard) {

//        cbPrint.setVisibility(needShowKeyboard? GONE: VISIBLE);
        btnCommit.setVisibility(needShowKeyboard ? GONE : VISIBLE);
    }

    public void setCancleCouponVisibility(boolean visibility) {
        btnCancelCoupon.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setCancleTempPromotionVisibility(boolean visibility) {
        btnCancelTempPromotion.setVisibility(visibility ? VISIBLE : GONE);
    }

    public void setCancleGiftCardVisibility(boolean visibility) {
        btnCancelGiftCard.setVisibility(visibility ? VISIBLE : GONE);
    }

    public interface OnClickListener {
        void onCommitOrderClicked();

        void onCancelCoupon();

        void onCancelTempPromotion();

        void onCancelGiftCard();
    }


    public void release() {
        if (unbinder != null)
            unbinder.unbind();
    }
}

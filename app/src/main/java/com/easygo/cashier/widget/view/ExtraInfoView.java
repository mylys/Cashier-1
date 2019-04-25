package com.easygo.cashier.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.utils.MemberUtils;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.CouponResponse;
import com.easygo.cashier.bean.GiftCardResponse;
import com.easygo.cashier.bean.MemberInfo;
import com.easygo.cashier.utils.CouponUtils;
import com.easygo.cashier.utils.GiftCardUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 会员、优惠券、礼品卡 信息显示
 */
public class ExtraInfoView extends ConstraintLayout {

    private View mView;
    private ConstraintLayout clMember;
    private ConstraintLayout clCoupon;
    private ConstraintLayout clGiftCard;
    private ImageView ivCancelMember;
    private ImageView ivCancelCoupon;
    private ImageView ivCancelGiftCard;

    private TextView tvMember;
    private TextView tvBalance;
    private TextView tvIntegral;
    private TextView tvCouponNo;
    private TextView tvCouponPrice;
    private TextView tvGiftCardNo;
    private TextView tvGiftCardPrice;

    private View lineCoupon;
    private View lineHorizontal;
    private View lineGiftCard;

    private DecimalFormat df = new DecimalFormat("0.00");


    public ExtraInfoView(Context context) {
        super(context);
        init(context, null);
    }

    public ExtraInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    private void init(final Context context, AttributeSet attrs) {

        initView(context);

        initAttr(context, attrs);


        setListener();

    }

    private void setListener() {

        ivCancelMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null) {
                    mCallback.onCancelMember();
                }
            }
        });
        ivCancelCoupon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null) {
                    mCallback.onCancelCoupon();
                }
            }
        });
        ivCancelGiftCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null) {
                    mCallback.onCancelGiftCard();
                }
            }
        });
    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.include_goods_extra_info, this, true);
        clMember = mView.findViewById(R.id.cl_member);
        clCoupon = mView.findViewById(R.id.cl_coupon);
        clGiftCard = mView.findViewById(R.id.cl_gift_card);
        ivCancelMember = mView.findViewById(R.id.iv_cancel_member);
        ivCancelCoupon = mView.findViewById(R.id.iv_cancel_coupon);
        ivCancelGiftCard = mView.findViewById(R.id.iv_cancel_gift_card);

        tvMember = mView.findViewById(R.id.tv_member);
        tvBalance = mView.findViewById(R.id.tv_balance);
        tvIntegral = mView.findViewById(R.id.tv_integral);

        tvCouponNo = mView.findViewById(R.id.tv_coupon_no);
        tvCouponPrice = mView.findViewById(R.id.tv_coupon_price);

        tvGiftCardNo = mView.findViewById(R.id.tv_gift_card_no);
        tvGiftCardPrice = mView.findViewById(R.id.tv_gift_card_price);

        lineCoupon = mView.findViewById(R.id.line_coupon);
        lineHorizontal = mView.findViewById(R.id.line_horizontal);
        lineGiftCard = mView.findViewById(R.id.line_gift_card);

    }

    private void initAttr(Context context, AttributeSet attrs) {
//        if (attrs == null) {
//            return;
//        }
//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MySearchView);
//        if (ta != null) {
//
//
//            ta.recycle();
//        }
    }


    public void setMemberData(MemberInfo info) {
        if (info == null) {//重置清空会员
            MemberUtils.isMember = false;
            setHide(clMember);
        } else {
            MemberUtils.isMember = true;

            setShow(clMember);
            tvMember.setText(info.getNick_name());
            tvBalance.setText("￥" + df.format(info.getWallet()));
            tvIntegral.setText(info.getIntegral() + "");

        }
    }
    public void setCouponData(CouponResponse info) {
        if(info == null) {
            setHide(clCoupon);
        } else {
            setShow(clCoupon);

            tvCouponNo.setText(info.getName());
            int offer_type = info.getOffer_type();
            float offer_value = info.getOffer_value();
            if (offer_type == 1) {
                tvCouponPrice.setText("-" + offer_value);
            } else if (offer_type == 2) {
                DecimalFormat df = new DecimalFormat("0.0");
                tvCouponPrice.setText(df.format((100 - offer_value) / 10f) + "折");
            }
        }
    }
    public void setGiftCardData(GiftCardResponse info) {
        if(info == null) {
            setHide(clGiftCard);
        } else {
            setShow(clGiftCard);

            tvGiftCardNo.setText(info.getSn());
            tvGiftCardPrice.setText(df.format(info.getBalance_amount()));
        }
    }


    public void setShow(ConstraintLayout constraintLayout) {
        constraintLayout.setVisibility(View.VISIBLE);
        setVisibility(View.VISIBLE);
        if (MemberUtils.isMember) {
            clMember.setVisibility(View.VISIBLE);
        }
        if (CouponUtils.getInstance().getCouponInfo() != null) {
            clCoupon.setVisibility(View.VISIBLE);
        }
        if(GiftCardUtils.getInstance().getGiftCardInfo() != null) {
            clGiftCard.setVisibility(View.VISIBLE);
        }
        updatePosition();
    }

    public void setHide(ConstraintLayout constraintLayout) {
        if (constraintLayout == this) {
            clMember.setVisibility(View.GONE);
            clCoupon.setVisibility(View.GONE);
            clGiftCard.setVisibility(View.GONE);
            return;
        }

        constraintLayout.setVisibility(View.GONE);

        updatePosition();
    }

    private void updatePosition() {
        int size = 3;
        ArrayList<ConstraintLayout> layouts = new ArrayList<>(size);
        layouts.add(clMember);
        layouts.add(clCoupon);
        layouts.add(clGiftCard);

        ArrayList<ConstraintLayout> removes = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            ConstraintLayout layout = layouts.get(i);

            if(!layout.isShown()) {
                removes.add(layout);
            }
        }

        layouts.removeAll(removes);

        int show_size = layouts.size();
        ConstraintLayout layout;
        ConstraintLayout.LayoutParams lp;
        for (int i = 0; i < show_size; i++) {
            layout = layouts.get(i);
            lp = new LayoutParams(layout.getLayoutParams());
            if(i == 0) {
                lp.startToStart = R.id.root;
                lp.topToTop = R.id.root;
            } else if(i == 1) {
                ConstraintLayout left = layouts.get(i - 1);
                int id = left.getId();
                lp.startToEnd = id;
                lp.topToTop = id;
                lp.bottomToBottom = id;
            } else if(i == 2) {
                lp.startToStart = R.id.root;
                lp.topToBottom = layouts.get(i-2).getId();
            }
            layout.setLayoutParams(lp);
        }

        if(show_size == 0) {
            lineCoupon.setVisibility(GONE);
            lineHorizontal.setVisibility(GONE);
            lineGiftCard.setVisibility(GONE);
        } else if(show_size == 1) {
            ConstraintLayout cl = layouts.get(0);
            lineCoupon.setVisibility(cl == clCoupon? VISIBLE: GONE);
            lineHorizontal.setVisibility(GONE);
            lineGiftCard.setVisibility(cl == clGiftCard? VISIBLE: GONE);
        } else if(show_size == 2) {
            lineCoupon.setVisibility(layouts.get(0) == clCoupon? VISIBLE: GONE);
            lineHorizontal.setVisibility(GONE);
            lineGiftCard.setVisibility(GONE);
        } else {
            lineCoupon.setVisibility(layouts.get(0) == clCoupon? VISIBLE: GONE);
            lineHorizontal.setVisibility(layouts.get(2) == clGiftCard? VISIBLE: GONE);
            lineGiftCard.setVisibility(layouts.get(2) == clGiftCard? VISIBLE: GONE);
        }

    }

    public String getMemberBalance() {
        return MemberUtils.isMember ? tvBalance.getText().toString() : "";
    }


    private Callback mCallback;

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public interface Callback {
        void onCancelMember();
        void onCancelCoupon();
        void onCancelGiftCard();

    }

}

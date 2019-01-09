package com.easygo.cashier.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.easygo.cashier.R;
import com.easygo.cashier.module.promotion.base.Goods;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PayWayView extends ConstraintLayout {

    private View mView;
    private CheckBox mCombineCheckBox;
    private Button mCash;
    private Button mAlipay;
    private Button mWechat;
    private Button mBankCard;
    private Button mMember;
    private Button mOther;
    private Button mCoupon;
    public List<View> mButtons;

    public static final int WAY_CASH = 0x0000;
    public static final int WAY_ALIPAY = 0x0001;
    public static final int WAY_WECHAT = 0x0002;
    public static final int WAY_MEMBER = 0x0003;
    public static final int WAY_BANK_CARD = 0x0004;
    public static final int WAY_OTHER = 0x0005;
    public static final int WAY_COUPON = 0x0006;

    private Button mSelected;


    public PayWayView(Context context) {
        super(context);
        init(context, null);
    }

    public PayWayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        initView(context);

        initAttr(context, attrs);
    }

    private void initView(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.layout_pay_way, this, true);

        mCombineCheckBox = ((CheckBox) mView.findViewById(R.id.cb_combine_payment));
        mCash = ((Button) mView.findViewById(R.id.btn_cash));
        mAlipay = ((Button) mView.findViewById(R.id.btn_alipay));
        mWechat = ((Button) mView.findViewById(R.id.btn_wechat));
        mBankCard = ((Button) mView.findViewById(R.id.btn_bank_card));
        mOther = ((Button) mView.findViewById(R.id.btn_other));
        mMember = ((Button) mView.findViewById(R.id.btn_member));
        mCoupon = ((Button) mView.findViewById(R.id.btn_coupon));

        mCash.setOnClickListener(listener);
        mAlipay.setOnClickListener(listener);
        mWechat.setOnClickListener(listener);
        mBankCard.setOnClickListener(listener);
        mOther.setOnClickListener(listener);
        mMember.setOnClickListener(listener);
        mCoupon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPayWaySelected(false, getPayWay(mCoupon));
            }
        });

        mCombineCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    setSelected(mCash, true);

                    if (mSelected == mCash) {
                        setSelected(mAlipay, true);
                        mSelected = mAlipay;
                    }
                } else {
                    setSelected(mCash, true);
                    setSelected(mSelected, false);
                    mSelected = mCash;
                }

                if (mListener != null) {
                    mListener.onPayWaySelected(isChecked, getPayWay(mSelected));
                }
            }
        });

        mMember.setVisibility(GONE);
        mBankCard.setVisibility(GONE);
        mOther.setVisibility(GONE);
        mCoupon.setVisibility(GONE);

        mButtons = new ArrayList<>();
        mButtons.add(mCash);
        mButtons.add(mAlipay);
        mButtons.add(mWechat);
        mButtons.add(mMember);
        mButtons.add(mBankCard);
        mButtons.add(mOther);
        mButtons.add(mCoupon);

        mSelected = mCash;
        mSelected.setSelected(true);
        mSelected.setTextColor(getResources().getColor(R.color.color_text_white));

    }

    private void initAttr(Context context, AttributeSet attrs) {


    }


    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            Button clicked = (Button) view;

            if (mCombineCheckBox.isChecked()) {
                //组合支付
                if (!mCash.isSelected())
                    mCash.setSelected(true);
                if (mSelected != clicked && clicked != mCash) {
                    setSelected(clicked, true);
                    setSelected(mSelected, false);
                    mSelected = clicked;
                }

            } else {
                if (mSelected != clicked) {
                    setSelected(clicked, true);
                    setSelected(mSelected, false);
                    mSelected = clicked;
                }
            }

            if (mListener != null) {
                mListener.onPayWaySelected(mCombineCheckBox.isChecked(), getPayWay(mSelected));
            }
        }
    };

    private void setSelected(Button button, boolean isSelected) {

        Resources res = getResources();
        button.setTextColor(isSelected ? res.getColor(R.color.color_text_white) : res.getColor(R.color.color_text_black));
        button.setSelected(isSelected);
    }

    private int getPayWay(Button button) {
        switch (button.getId()) {
            case R.id.btn_cash:
                return WAY_CASH;
            case R.id.btn_alipay:
                return WAY_ALIPAY;
            case R.id.btn_wechat:
                return WAY_WECHAT;
            case R.id.btn_member:
                return WAY_MEMBER;
            case R.id.btn_bank_card:
                return WAY_BANK_CARD;
            case R.id.btn_other:
                return WAY_OTHER;
            case R.id.btn_coupon:
                return WAY_COUPON;
            default:
                return WAY_CASH;
        }
    }

    public interface OnPayWayListener {
        void onPayWaySelected(boolean isCombinePay, int pay_way);
    }

    private OnPayWayListener mListener;

    public void setOnPayWayListener(OnPayWayListener listener) {
        this.mListener = listener;
    }

    public void setPayWayShow(int[] position) {
        for (View view : mButtons) {
            view.setVisibility(GONE);
        }
        if (position.length <= mButtons.size()) {
            for (int i : position) {
                mButtons.get(i).setVisibility(VISIBLE);
            }
        }
    }
}

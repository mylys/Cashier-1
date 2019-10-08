package com.easygo.cashier.widget.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.easygo.cashier.R;

import java.util.ArrayList;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;

public class PayWayView extends ConstraintLayout {

    private View mView;
    private CheckBox mCombineCheckBox;
    private Button mAlipay;
    private Button mWechat;
    private Button mMember;
    private Button mBankCard;
    private Button mCash;
    private Button mGiftCard;
    public List<View> mButtons;

    public static final int WAY_ALIPAY = 0x0000;
    public static final int WAY_WECHAT = 0x0001;
    public static final int WAY_MEMBER = 0x0002;
    public static final int WAY_BANK_CARD = 0x0003;
    public static final int WAY_CASH = 0x0004;
    public static final int WAY_GIFT_CARD = 0x0005;

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

        mCombineCheckBox = mView.findViewById(R.id.cb_combine_payment);
        mCash = mView.findViewById(R.id.btn_cash);
        mAlipay = mView.findViewById(R.id.btn_alipay);
        mWechat = mView.findViewById(R.id.btn_wechat);
        mBankCard = mView.findViewById(R.id.btn_bank_card);
        mGiftCard = mView.findViewById(R.id.btn_gift_card);
        mMember = mView.findViewById(R.id.btn_member);

        mCash.setOnClickListener(listener);
        mAlipay.setOnClickListener(listener);
        mWechat.setOnClickListener(listener);
        mBankCard.setOnClickListener(listener);
        mGiftCard.setOnClickListener(listener);
        mMember.setOnClickListener(listener);

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
        mGiftCard.setVisibility(GONE);

        mButtons = new ArrayList<>();
        mButtons.add(mAlipay);
        mButtons.add(mWechat);
        mButtons.add(mMember);
        mButtons.add(mBankCard);
        mButtons.add(mCash);
        mButtons.add(mGiftCard);

        mSelected = mAlipay;
//        mSelected = mCash;
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
            case R.id.btn_gift_card:
                return WAY_GIFT_CARD;
            default:
//                return WAY_CASH;
                return WAY_ALIPAY;
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

    /**
     * 设置选中
     * @param position
     */
    public void performSelect(int position) {
        if (position < mButtons.size()) {
            mButtons.get(position).performClick();
        }
    }
}

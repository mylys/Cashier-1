package com.easygo.cashier.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseFragment;

import java.lang.reflect.Method;

import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SettlementFragment extends BaseFragment {

    @BindView(R.id.tv_receivable)
    TextView tvReceivable;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.et_receipts)
    EditText etReceipts;
    @BindView(R.id.cl_receipts)
    ConstraintLayout clReceipts;
    @BindView(R.id.line3)
    View line3;
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


    public static final int TYPE_CASH = 0x0010;
    public static final int TYPE_WECHAT = 0x0001;
    public static final int TYPE_ALIPAY = 0x0002;
    public static final int TYPE_BANK_CARD = 0x0003;
    public static final int TYPE_OTHER = 0x0004;

    private int mPayType = TYPE_CASH;


    public static SettlementFragment newInstance() {
        return new SettlementFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_settlement_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvAlreadySettlement.setVisibility(View.GONE);
        ivScan.setVisibility(View.GONE);
        tvCombineWay.setVisibility(View.GONE);
        tvCombineMoney.setVisibility(View.GONE);

        setShowSoftInputOnFocus(false);

    }

    /**
     * 设置EditText获取焦点时 是否弹出软键盘
     * @param showSoftInputOnFocus
     */
    private void setShowSoftInputOnFocus(boolean showSoftInputOnFocus) {
        try {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                    boolean.class);
            setShowSoftInputOnFocus.setAccessible(true);
            setShowSoftInputOnFocus.invoke(etReceipts, showSoftInputOnFocus);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPayType(int payType) {
        switch (payType) {
            case TYPE_CASH://现金

                break;
            case TYPE_WECHAT://微信

                break;
            case TYPE_ALIPAY://支付宝

                break;
            case TYPE_BANK_CARD://银行卡

                break;
            case TYPE_OTHER://其他

                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }
}

package com.easygo.cashier.module.refund;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.easygo.cashier.Configs;
import com.easygo.cashier.R;
import com.easygo.cashier.widget.ConfirmDialog;
import com.easygo.cashier.widget.Keyboard;
import com.easygo.cashier.widget.PayWayView;
import com.niubility.library.base.BaseMvpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RefundCashFragment extends BaseMvpFragment<RefundCashContract.IView, RefundCashPresenter> implements RefundCashContract.IView {

    @BindView(R.id.pay_way_view)
    PayWayView payWayView;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.kb)
    Keyboard kb;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;

    private RefundCashView refundCashView;
    private ConfirmDialog confirmDialog;
    private int mPayWay = PayWayView.WAY_CASH;

    public static RefundCashFragment newInstance() {
        return new RefundCashFragment();
    }

    @Override
    protected RefundCashPresenter createPresenter() {
        return new RefundCashPresenter();
    }

    @Override
    protected RefundCashContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_refund_cash;
    }

    @Override
    protected void init() {
        payWayView.setPayWayShow(new int[]{0});
        refundCashView = RefundCashView.create(getActivity());
        framelayout.addView(refundCashView);

        setListener();
    }

    private void setListener() {
        refundCashView.setOnClickListener(new RefundCashView.OnClickListener() {
            @Override
            public void onPrintClicked(boolean isChecked) {
                if (!isChecked) {
                    showToast("交易完成后将不打印小票");
                }
//                print();
            }

            @Override
            public void onCommitOrderClicked() {
//                if(mRealPay < mTotalMoney) {
//                    showToast("实收金额小于应收金额， 请确认！");
//                }
                //弹出确认弹窗
                Bundle bundle = ConfirmDialog.getDataBundle(0, 0, 0, PayWayView.WAY_CASH, false, "应退", "实退");
                confirmDialog = new ConfirmDialog();
                confirmDialog.setArguments(bundle);
                confirmDialog.setOnConfirmListener(new ConfirmDialog.OnConfirmListenr() {
                    @Override
                    public void onConfirm() {
                        onCommitOrder();
                    }
                });
                confirmDialog.show(getActivity().getSupportFragmentManager(), "tag_tip_dialog");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.btn_delete)
    public void onViewClicked() {

    }

    public void onCommitOrder() {
        if (mPayWay == PayWayView.WAY_ALIPAY) {
            if (TextUtils.isEmpty(Configs.order_no)) {
                showToast("确认提交订单");
//                createOrder();
            } else {
                showToast("订单已经创建 --> " + Configs.order_no);
            }
        } else {
            showToast("开发中");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (refundCashView != null) {
            refundCashView.release();
        }
    }
}

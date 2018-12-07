package com.easygo.cashier.module.refund;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseRxFragment;

public class RefundCashFragment extends BaseRxFragment<RefundCashContract.IView, RefundCashPresenter> implements RefundCashContract.IView {

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
        return R.layout.layout_refund_cash;
    }

    @Override
    protected void init() {

    }
}

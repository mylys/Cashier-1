package com.easygo.cashier.module.order_history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.easygo.cashier.R;
import com.easygo.cashier.module.order_history.OrderHistoryFragment;
import com.easygo.cashier.module.order_history.OrderHistoryRefundFragment;
import com.niubility.library.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryActivity extends BaseActivity {

    private final String TAG_ORDER_HISTORY = "tag_order_history";
    private final String TAG_ORDER_HISTORY_REFUND = "tag_order_history_refund";

    private Fragment fragment;
    private OrderHistoryFragment orderHistoryFragment;
    private OrderHistoryRefundFragment orderHistoryRefundFragment;

    @BindView(R.id.framelayout)
    FrameLayout framelayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        orderHistoryFragment = OrderHistoryFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, orderHistoryFragment, TAG_ORDER_HISTORY).commit();
    }

    public void toOrderHistoryRefundFragment() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_ORDER_HISTORY_REFUND);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            orderHistoryRefundFragment = OrderHistoryRefundFragment.newInstance();

            transaction.add(R.id.framelayout, orderHistoryRefundFragment, TAG_ORDER_HISTORY_REFUND);

        } else {
            transaction.show(orderHistoryRefundFragment);
        }
        transaction.hide(orderHistoryFragment);
        transaction.addToBackStack(TAG_ORDER_HISTORY_REFUND);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        fragment = getSupportFragmentManager().findFragmentByTag(TAG_ORDER_HISTORY);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = OrderHistoryFragment.newInstance();
            transaction.replace(R.id.framelayout, fragment, TAG_ORDER_HISTORY);
        }
        transaction.commit();
    }
}
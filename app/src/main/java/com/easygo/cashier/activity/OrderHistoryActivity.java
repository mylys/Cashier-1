package com.easygo.cashier.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.easygo.cashier.R;
import com.easygo.cashier.fragment.OrderHistoryFragment;
import com.niubility.library.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryActivity extends BaseActivity {

    private final String TAG_ORDER_HISTORY = "tag_order_history";

    private Fragment fragment;
    private OrderHistoryFragment orderHistoryFragment;

    @BindView(R.id.framelayout)
    FrameLayout framelayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);

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
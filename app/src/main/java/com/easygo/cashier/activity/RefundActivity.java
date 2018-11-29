package com.easygo.cashier.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.easygo.cashier.R;
import com.easygo.cashier.fragment.GoodsFragment;
import com.niubility.library.base.BaseActivity;

import butterknife.ButterKnife;

public class RefundActivity extends BaseActivity {

    private final String TAG_REFUND = "tag_refund";

    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_refund);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment = getSupportFragmentManager().findFragmentByTag(TAG_REFUND);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = GoodsFragment.newInstance();

            transaction.replace(R.id.framelayout, fragment, TAG_REFUND);
        }
        transaction.commit();
    }
}

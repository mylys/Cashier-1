package com.easygo.cashier.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppActivity;
import com.easygo.cashier.fragment.SettlementFragment;

import butterknife.ButterKnife;

public class CashierActivity extends BaseAppActivity {

    private static final String TAG_SETTLEMENT = "tag_settlement";
    private SettlementFragment settlementFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        ButterKnife.bind(this);

        init();
    }

    private void init() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_SETTLEMENT);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            settlementFragment = SettlementFragment.newInstance();

            transaction.replace(R.id.framelayout, settlementFragment, TAG_SETTLEMENT);
        }
        transaction.commit();

    }
}

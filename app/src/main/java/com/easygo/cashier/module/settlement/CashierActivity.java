package com.easygo.cashier.module.settlement;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppActivity;
import com.easygo.cashier.module.settlement.SettlementFragment;
import com.easygo.cashier.widget.MyTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CashierActivity extends BaseAppActivity {

    private static final String TAG_SETTLEMENT = "tag_settlement";
    @BindView(R.id.cl_title)
    MyTitleBar clTitle;
    private SettlementFragment settlementFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        ButterKnife.bind(this);

        init();
    }

    private void init() {
        clTitle.setTitle(getResources().getString(R.string.text_settlement));

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

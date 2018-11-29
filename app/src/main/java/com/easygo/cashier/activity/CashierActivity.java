package com.easygo.cashier.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppActivity;

import butterknife.ButterKnife;

public class CashierActivity extends BaseAppActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashier);

        ButterKnife.bind(this);
    }
}

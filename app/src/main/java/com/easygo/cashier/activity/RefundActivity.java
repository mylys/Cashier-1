package com.easygo.cashier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.easygo.cashier.R;
import com.easygo.cashier.fragment.GoodsFragment;
import com.easygo.cashier.fragment.RefundFragment;
import com.niubility.library.base.BaseActivity;

import butterknife.ButterKnife;

public class RefundActivity extends BaseActivity {

    private final String TAG_REFUND = "tag_refund";
    private final String TAG_REFUND_SETTLEMENT = "tag_refund_settlement";

    private GoodsFragment goodsFragment;
    private RefundFragment refundFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_refund);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        Bundle bundle = new Bundle();
        bundle.putInt(GoodsFragment.KEY_TYPE, GoodsFragment.TYPE_REFUND);
        goodsFragment = GoodsFragment.newInstance(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, goodsFragment, TAG_REFUND).commit();
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    public void toRefundFragment() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_REFUND_SETTLEMENT);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            refundFragment = RefundFragment.newInstance();

            transaction.add(R.id.framelayout, refundFragment, TAG_REFUND_SETTLEMENT);

        } else {
            transaction.show(refundFragment);
        }
        transaction.hide(goodsFragment);
        transaction.addToBackStack(TAG_REFUND_SETTLEMENT);
        transaction.commit();
    }
}

package com.easygo.cashier.module.refund;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.module.goods.GoodsFragment;
import com.easygo.cashier.module.order_history.OrderHistoryRefundFragment;
import com.niubility.library.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能列表弹框 退款选项进入的页面
 */
@Route(path = ModulePath.refund)
public class RefundActivity extends BaseActivity {

    private final String TAG_REFUND = "tag_refund";
    private final String TAG_REFUND_CASH = "tag_refund_cash";

    private GoodsFragment goodsFragment;
    private RefundCashFragment refundCashFragment;

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
    @OnClick({R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_back://返回
                finish();
                break;
        }
    }


    public void toRefundCashFragment() {

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_REFUND_CASH);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            refundCashFragment = RefundCashFragment.newInstance();

            transaction.add(R.id.framelayout, refundCashFragment, TAG_REFUND_CASH);

        } else {
            transaction.show(refundCashFragment);
        }
        transaction.hide(goodsFragment);
        transaction.addToBackStack(TAG_REFUND_CASH);
        transaction.commit();
    }
}

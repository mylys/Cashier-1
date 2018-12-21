package com.easygo.cashier.module.order_history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppActivity;
import com.easygo.cashier.module.order_history.order_history_refund.OrderHistoryRefundFragment;
import com.easygo.cashier.widget.MyTitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ModulePath.order_history)
public class OrderHistoryActivity extends BaseAppActivity {

    private final String TAG_ORDER_HISTORY = "tag_order_history";
    private final String TAG_ORDER_HISTORY_REFUND = "tag_order_history_refund";

    private Fragment fragment;
    private OrderHistoryFragment orderHistoryFragment;
    private OrderHistoryRefundFragment orderHistoryRefundFragment;

    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.cl_title)
    MyTitleBar clTitle;

    @Autowired(name = "admin_name")
    String admin_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        clTitle.setCashierAccount(admin_name);

        fragment = getSupportFragmentManager().findFragmentByTag(TAG_ORDER_HISTORY);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(orderHistoryFragment);
        } else {
            orderHistoryFragment = OrderHistoryFragment.newInstance();
            transaction.replace(R.id.framelayout, orderHistoryFragment, TAG_ORDER_HISTORY);
        }
        transaction.commit();
    }

    public void toOrderHistoryRefundFragment(Bundle bundle) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_ORDER_HISTORY_REFUND);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            orderHistoryRefundFragment = OrderHistoryRefundFragment.getInstance(bundle);
            transaction.add(R.id.framelayout, orderHistoryRefundFragment, TAG_ORDER_HISTORY_REFUND);
        } else {
            transaction.show(orderHistoryRefundFragment);
        }
        transaction.hide(orderHistoryFragment);
        transaction.addToBackStack(TAG_ORDER_HISTORY_REFUND);
        transaction.commit();
    }

    @OnClick({R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_back://返回
                if (orderHistoryRefundFragment != null && orderHistoryRefundFragment.isVisible()) {
                    onBackPressed();
                    return;
                }
                finish();
                break;
        }
    }
}
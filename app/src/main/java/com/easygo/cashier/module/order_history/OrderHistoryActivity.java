package com.easygo.cashier.module.order_history;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.utils.SoftKeyboardUtil;
import com.easygo.cashier.module.order_history.order_history_refund.OrderHistoryRefundFragment;
import com.easygo.cashier.widget.view.MyTitleBar;
import com.niubility.library.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ModulePath.order_history)
public class OrderHistoryActivity extends AppCompatActivity {

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
        ARouter.getInstance().inject(this);
        init();
    }

    private void init() {
        clTitle.setCashierAccount(admin_name);

        fragment = getSupportFragmentManager().findFragmentByTag(TAG_ORDER_HISTORY);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(fragment);
            orderHistoryFragment = (OrderHistoryFragment) fragment;
        } else {
            orderHistoryFragment = OrderHistoryFragment.newInstance();
            transaction.replace(R.id.framelayout, orderHistoryFragment, TAG_ORDER_HISTORY);
        }
        transaction.commit();
    }

    public void printRefundInfo(String info) {
        orderHistoryFragment.printRefundInfo(info);
    }

    /**
     * 跳转退款页
     */
    public void toOrderHistoryRefundFragment(Bundle bundle) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_ORDER_HISTORY_REFUND);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment == null) {
            orderHistoryRefundFragment = OrderHistoryRefundFragment.getInstance(bundle);
            transaction.add(R.id.framelayout, orderHistoryRefundFragment, TAG_ORDER_HISTORY_REFUND);
        } else {
            transaction.show(fragment);
            orderHistoryRefundFragment = (OrderHistoryRefundFragment) fragment;
        }
        transaction.hide(orderHistoryFragment);
        transaction.addToBackStack(TAG_ORDER_HISTORY_REFUND);
        transaction.commit();
    }

    public void toRefresh() {
        if (orderHistoryFragment != null){
            orderHistoryFragment.toRefresh();
        }
    }

    @OnClick({R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_back://返回
                back();
                break;
        }
    }

    public void back() {
        if (orderHistoryRefundFragment != null && orderHistoryRefundFragment.isVisible()) {
            onBackPressed();
            return;
        }
        SoftKeyboardUtil.hideSoftKeyboard(this);
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ScreenUtils.hideNavigationBar(this);
    }
}
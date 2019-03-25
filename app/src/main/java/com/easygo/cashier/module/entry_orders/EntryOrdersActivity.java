package com.easygo.cashier.module.entry_orders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.easygo.cashier.Configs;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.widget.MyTitleBar;
import com.niubility.library.base.BaseActivity;
import com.niubility.library.utils.ScreenUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Describe：挂单页
 * @date：2019-01-03
 */

@Route(path = ModulePath.entry_orders)
public class EntryOrdersActivity extends BaseActivity {

    private final String TAG_ENTRY_ORDERS = "tag_entry_orders";
    private Fragment fragment;
    private EntryOrdersFragment entryOrdersFragment;

    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.cl_title)
    MyTitleBar clTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        clTitle.setCashierAccount(String.valueOf(Configs.cashier_id));
        clTitle.setTitle(getResources().getString(R.string.text_entry_orders_list));

        fragment = getSupportFragmentManager().findFragmentByTag(TAG_ENTRY_ORDERS);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(entryOrdersFragment);
        } else {
            entryOrdersFragment = EntryOrdersFragment.newInstance();
            transaction.replace(R.id.framelayout, entryOrdersFragment, TAG_ENTRY_ORDERS);
        }
        transaction.commit();
    }

    @OnClick({R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_back://返回
                finish();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        ScreenUtils.hideNavigationBar(this);
    }

    //作废
    public void toInvaild() {
        entryOrdersFragment.toInvaild();
    }

    public void toChoose() {
        entryOrdersFragment.toChoose();
    }
}

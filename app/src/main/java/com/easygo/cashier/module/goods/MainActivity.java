package com.easygo.cashier.module.goods;


import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppActivity;
import com.easygo.cashier.module.order_history.OrderHistoryActivity;
import com.easygo.cashier.module.refund.RefundActivity;
import com.easygo.cashier.module.secondary_sreen.SecondaryScreen;
import com.easygo.cashier.module.settlement.CashierActivity;
import com.easygo.cashier.widget.FunctionListDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页
 */
@Route(path = ModulePath.goods)
public class MainActivity extends BaseAppActivity {

    private final String TAG_MAIN = "tag_main";
    private final String TAG_FUNCTION_LIST = "tag_function_list";
    @BindView(R.id.tv_cashier_account)
    TextView tvCashierAcount;
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.network)
    ImageView network;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.tv_cashier)
    TextView tvCashier;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fragment = getSupportFragmentManager().findFragmentByTag(TAG_MAIN);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragment != null) {
            transaction.show(fragment);
        } else {
            fragment = GoodsFragment.newInstance();

            transaction.replace(R.id.framelayout, fragment, TAG_MAIN);
        }
        transaction.commit();
    }

    @OnClick({R.id.setting, R.id.menu, R.id.cl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting://设置

                //临时代码
                //屏幕管理类
                DisplayManager mDisplayManager = (DisplayManager) this.getSystemService(Context.DISPLAY_SERVICE);
                Display[] displays = mDisplayManager.getDisplays();

                SecondaryScreen mPresentation = null;
                if (mPresentation == null) {
                    mPresentation = new SecondaryScreen(this, displays[displays.length - 1]);// displays[1]是副屏

                    mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                }
                mPresentation.show();

                break;
            case R.id.menu://功能列表
                FunctionListDialog functionListDialog = new FunctionListDialog();
                functionListDialog.setOnFunctionListItemListener(mFunctionListItemListener);
                functionListDialog.show(getSupportFragmentManager(), TAG_FUNCTION_LIST);
                break;
            case R.id.cl_back://返回
                break;
        }
    }

    public FunctionListDialog.OnFunctionListItemListener mFunctionListItemListener = new FunctionListDialog.OnFunctionListItemListener() {
        @Override
        public void orderHistory() {
            ARouter.getInstance().build(ModulePath.order_history).navigation();
        }

        @Override
        public void refund() {
            ARouter.getInstance().build(ModulePath.refund).navigation();
        }

        @Override
        public void shift() {

        }

        @Override
        public void enterSystem() {

        }

        @Override
        public void languageSetting() {

        }

        @Override
        public void deviceStatus() {

        }

        @Override
        public void systemSetting() {

        }

        @Override
        public void onItemClickBefore() {

        }

        @Override
        public void onItemClickAfter() {

        }
    };

}

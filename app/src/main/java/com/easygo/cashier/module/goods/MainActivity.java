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
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.base.BaseAppActivity;
import com.easygo.cashier.module.order_history.OrderHistoryActivity;
import com.easygo.cashier.module.refund.RefundActivity;
import com.easygo.cashier.module.secondary_sreen.SecondaryScreen;
import com.easygo.cashier.module.settlement.CashierActivity;
import com.easygo.cashier.widget.FunctionListDialog;
import com.easygo.cashier.widget.HelpDialog;
import com.niubility.library.widget.imageview.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Route(path = ModulePath.goods)
public class MainActivity extends BaseAppActivity {

    private final String TAG_MAIN = "tag_main";
    private final String TAG_FUNCTION_LIST = "tag_function_list";
    @BindView(R.id.civ)
    CircleImageView civ;
    @BindView(R.id.tv_cashier_account)
    TextView tvCashierAcount;
    @BindView(R.id.help)
    ImageView help;
    @BindView(R.id.setting)
    ImageView setting;
    @BindView(R.id.network)
    ImageView network;
    @BindView(R.id.menu)
    ImageView menu;
    @BindView(R.id.iv_back)
    ImageView ivBack;
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

    @OnClick({R.id.help, R.id.setting, R.id.menu, R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.help://帮助
                HelpDialog helpDialog = new HelpDialog(this);
                helpDialog.show();
                break;
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
                functionListDialog.show(getSupportFragmentManager(), TAG_FUNCTION_LIST);
                break;
            case R.id.iv_back://返回
                break;
        }
    }

    /**
     * 收银页面
     */
    public void toCashierActivity() {
        Intent intent = new Intent(this, CashierActivity.class);
        startActivity(intent);
    }

    /**
     * 历史订单页面
     */
    public void toOrderHistoryActivity() {
        Intent intent = new Intent(this, OrderHistoryActivity.class);
        startActivity(intent);
    }

    /**
     * 退款页面
     */
    public void toRefundActivity() {
        Intent intent = new Intent(this, RefundActivity.class);
        startActivity(intent);
    }
}

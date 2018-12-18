package com.easygo.cashier.module.handover;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.ModulePath;
import com.easygo.cashier.R;
import com.easygo.cashier.bean.HandoverResponse;
import com.easygo.cashier.bean.HandoverSaleResponse;
import com.easygo.cashier.module.goods.MainActivity;
import com.easygo.cashier.module.login.LoginActivity;
import com.easygo.cashier.widget.MyTitleBar;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.constants.Constans;
import com.niubility.library.http.exception.HttpExceptionEngine;
import com.niubility.library.utils.SharedPreferencesUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 交接班页面
 */
@Route(path = ModulePath.handover)
public class HandoverActivity extends BaseMvpActivity<HandoverContract.IView, HandoverPresenter> implements HandoverContract.IView {

    private static final String TAG = "HandoverActivity";
    @BindView(R.id.cl_title)
    MyTitleBar clTitle;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.tv_login_time)
    TextView tvLoginTime;

    @Autowired(name = "admin_name")
    String admin_name;

    private HandoverView handoverView;
    private int handover_id;


    @Override
    protected HandoverPresenter createPresenter() {
        return new HandoverPresenter();
    }

    @Override
    protected HandoverContract.IView createView() {
        return this;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_handover;
    }

    @Override
    public void init() {
        ARouter.getInstance().inject(this);

        clTitle.setCashierAccount(admin_name);

        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getApplicationContext());
        handover_id = sp.getInt(Constans.KEY_HANDOVER_ID, -1);

        mPresenter.handover(handover_id);

    }


    @Override
    public void handoverSuccess(HandoverResponse result) {
        if (handoverView == null) {
            handoverView = HandoverView.create(this);
            framelayout.addView(handoverView);
        }

//        handoverView.setData(result);
        tvLoginTime.setText(result.getStart_time());

    }

    @Override
    public void handoverFailed(Map<String, Object> map) {

        if(HttpExceptionEngine.isBussinessError(map)) {

            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);

            showToast("错误信息:" + err_msg);
        }


    }

    @Override
    public void loginoutSuccess(String result) {
        showToast(result);

        SharedPreferences sp = SharedPreferencesUtils.getInstance().getSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sp.edit();
        //登录状态中，清除 session_id 、 admin_name
        if(sp.contains(Constans.KEY_SESSION_ID)) {
            editor.remove(Constans.KEY_SESSION_ID).apply();
        }
        if(sp.contains(Constans.KEY_ADMIN_NAME)) {
            editor.remove(Constans.KEY_ADMIN_NAME).apply();
        }
        if(sp.contains(Constans.KEY_SHOP_SN)) {
            editor.remove(Constans.KEY_SHOP_SN).apply();
        }
        if(sp.contains(Constans.KEY_TIME)) {
            editor.remove(Constans.KEY_TIME).apply();
        }
        if(sp.contains(Constans.KEY_HANDOVER_ID)) {
            editor.remove(Constans.KEY_HANDOVER_ID).apply();
        }

        Intent intent = new Intent(HandoverActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    @Override
    public void loginoutFailed(Map<String, Object> map) {
        if(HttpExceptionEngine.isBussinessError(map)) {

            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);

            showToast("错误信息:" + err_msg);
        }
    }

    @Override
    public void saleListSuccess(HandoverSaleResponse result) {
        showToast("销售列表成功");


    }

    @Override
    public void saleListFailed(Map<String, Object> map) {
        if(HttpExceptionEngine.isBussinessError(map)) {

            String err_msg = (String) map.get(HttpExceptionEngine.ErrorMsg);

            showToast("错误信息:" + err_msg);
        }
    }

    @OnClick({R.id.btn_handover, R.id.btn_sales_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_handover:
                mPresenter.loginout(handover_id);
                break;
            case R.id.btn_sales_list:
                mPresenter.sale_list(handover_id);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handoverView != null) {
            handoverView.release();
        }
    }
}

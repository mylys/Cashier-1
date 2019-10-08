package com.easygo.cashier.base;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.utils.SoftKeyBoardListener;
import com.easygo.cashier.widget.dialog.LoadingDialog;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.mvp.BasePresenter;
import com.niubility.library.mvp.BaseView;
import com.niubility.library.utils.ScreenUtils;

public abstract class BaseAppMvpActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseMvpActivity<V,P> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initLoadingDialog();
        ScreenUtils.hideNavigationBar(this);
        super.onCreate(savedInstanceState);

        ARouter.getInstance().inject(this);

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

            }

            @Override
            public void keyBoardHide(int height) {
                View decorView = getWindow().getDecorView();
                decorView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ScreenUtils.hideNavigationBar(BaseAppMvpActivity.this);
                    }
                }, 200);
            }
        });

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);


        ScreenUtils.hideNavigationBar(this);
    }

    private void initLoadingDialog() {
        mLoadingDialog = new LoadingDialog(BaseAppMvpActivity.this);
        mLoadingDialog.setCanceledOnTouchOutside(true);
        Window window = mLoadingDialog.getWindow();
        if(window != null) {
            window.setGravity(Gravity.CENTER);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    protected LoadingDialog mLoadingDialog;

    @Override
    public void showLoading() {
        super.showLoading();
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v != null && isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                v.clearFocus();
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        return false;
    }
}

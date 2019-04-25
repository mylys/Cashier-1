package com.easygo.cashier.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;

import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.widget.dialog.LoadingDialog;
import com.niubility.library.base.BaseMvpFragment;
import com.niubility.library.mvp.BasePresenter;
import com.niubility.library.mvp.BaseView;

public abstract class BaseAppMvpFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseMvpFragment<V,P> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initLoadingDialog();
        super.onCreate(savedInstanceState);

        ARouter.getInstance().inject(this);

    }

    private void initLoadingDialog() {
        mLoadingDialog = new LoadingDialog(getContext());
        mLoadingDialog.setCanceledOnTouchOutside(true);
        Window window = mLoadingDialog.getWindow();
        if(window != null) {
            window.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public void onDestroy() {
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
}

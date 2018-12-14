package com.easygo.cashier.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.launcher.ARouter;
import com.niubility.library.base.BaseMvpActivity;
import com.niubility.library.mvp.BasePresenter;
import com.niubility.library.mvp.BaseView;

public abstract class BaseAppMvpActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseMvpActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ARouter.getInstance().inject(this);

    }
}

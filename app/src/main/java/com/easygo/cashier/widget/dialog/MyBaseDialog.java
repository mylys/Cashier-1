package com.easygo.cashier.widget.dialog;

import com.easygo.cashier.R;
import com.niubility.library.base.BaseDialog;

public abstract class MyBaseDialog extends BaseDialog {

    @Override
    protected boolean isHideNavigationBar() {
        return true;
    }

    @Override
    protected int getSystemUiVisibility() {
        return 0;
    }

    @Override
    protected boolean shouldHideBackground() {
        return false;
    }

    @Override
    protected boolean canCanceledOnTouchOutside() {
        return false;
    }

    @Override
    protected boolean isWindowWidthMatchParent() {
        return false;
    }

    @Override
    protected int getAnimation() {
        return R.style.CustomDialogStyle;
    }
}

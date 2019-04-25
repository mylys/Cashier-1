package com.easygo.cashier.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.easygo.cashier.R;

public class LoadingDialog extends Dialog {

    private TextView tvDescription;
    private String loadingText;

    public LoadingDialog(Context context) {
        super(context, R.style.Loading);
        this.setContentView(R.layout.layout_loading);
        Window window = this.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = 17;
        window.setAttributes(params);

        tvDescription = findViewById(R.id.tv_description);
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }

    public void show() {
        if (!TextUtils.isEmpty(loadingText)){
            tvDescription.setText(loadingText);
        }

        super.show();
    }


}

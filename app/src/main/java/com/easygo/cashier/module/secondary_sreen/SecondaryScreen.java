package com.easygo.cashier.module.secondary_sreen;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;

import com.easygo.cashier.R;

public class SecondaryScreen extends Presentation {

    public SecondaryScreen(Context outerContext, Display display) {
        super(outerContext, display);
    }

    public SecondaryScreen(Context outerContext, Display display, int theme) {
        super(outerContext, display, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_secondary_screen);
    }
}

package com.easygo.cashier;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;

import java.util.Locale;

/**
 * @Describe：
 * @Date：2019-06-19
 */
public class MyConextWrapper extends ContextWrapper {

    public MyConextWrapper(Context base) {
        super(base);
    }

    public static ContextWrapper wrapper(Context context, Locale locale){
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocale(locale);
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            configuration.setLocales(localeList);
            context = context.createConfigurationContext(configuration);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        }

        return new ContextWrapper(context);
    }
}

package com.easygo.cashier.module.config;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.easygo.cashier.Configs;
import com.easygo.cashier.R;

public class ConfigPreferenceFragment extends PreferenceFragmentCompat {

    public final String TAG = getClass().getSimpleName();
    public static final String mSharedPreferencesName = Configs.sp_config;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        getPreferenceManager().setSharedPreferencesName(mSharedPreferencesName);
        addPreferencesFromResource(R.xml.global_config);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        return super.onPreferenceTreeClick(preference);
    }
}

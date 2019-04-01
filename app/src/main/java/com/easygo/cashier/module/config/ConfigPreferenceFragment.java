package com.easygo.cashier.module.config;

import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.easygo.cashier.R;

public class ConfigPreferenceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.global_config);
    }
}

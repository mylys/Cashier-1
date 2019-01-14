package com.easygo.cashier.module.permission;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.easygo.cashier.module.login.LoginActivity;
import com.niubility.library.base.BaseActivity;

public class PermissionActivity extends BaseActivity {

    public static final String TAG = "PermissionActivity";


    private static final int REQUEST_PERMISSION_CODE_SYSTEM_ALERT_WINDOW = 0;

    private String[] permissions = new String[]{
            Manifest.permission.SYSTEM_ALERT_WINDOW
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //大于等于6.0时 判断悬浮窗权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(PermissionActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_PERMISSION_CODE_SYSTEM_ALERT_WINDOW);
            }else{
                toLogin();
            }
        } else {
            toLogin();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(requestCode == REQUEST_PERMISSION_CODE_SYSTEM_ALERT_WINDOW) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(PermissionActivity.this)) {
                            //已获得悬浮窗权限
                            toLogin();
                        } else {
                            showToast("未获得副屏显示权限，即将退出！");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);
                        }
                    }
                }
            }
        },500);
    }

    //跳转登录页
    public void toLogin() {
        Intent intent = new Intent(PermissionActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}

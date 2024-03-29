package com.easygo.cashier.module.permission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.easygo.cashier.module.login.LoginActivity;
import com.niubility.library.base.BaseActivity;

public class PermissionActivity extends BaseActivity {

    public static final String TAG = "PermissionActivity";


    private static final int REQUEST_PERMISSION_CODE_SYSTEM_ALERT_WINDOW = 0;
    private static final int REQUEST_PERMISSION_CODE_WRITE_EXTERNAL_STORAGE = 1;

    private String[] permissions = new String[]{
//            Manifest.permission.SYSTEM_ALERT_WINDOW,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //大于等于6.0时 判断悬浮窗权限
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION_CODE_WRITE_EXTERNAL_STORAGE);
            } else {
                checkAlertPermission();
            }
        } else {
            toLogin();
        }
    }

    private void checkAlertPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(PermissionActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_PERMISSION_CODE_SYSTEM_ALERT_WINDOW);
            } else {
                toLogin();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_CODE_WRITE_EXTERNAL_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(PermissionActivity.this,
                        permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    //已获得读写权限
                    checkAlertPermission();
                } else {
                    showToast("未获得读写文件权限，即将退出！");
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

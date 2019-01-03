package com.easygo.cashier.module.permission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(PermissionActivity.this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_PERMISSION_CODE_SYSTEM_ALERT_WINDOW);
            }
        }

//        if(ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
//
//            Log.i(TAG, "已经获取悬浮窗权限");
//            toLogin();
//        } else {
//            Log.i(TAG, "没有悬浮窗权限，开始申请");
//            ActivityCompat.requestPermissions(this, new String[]{permissions[0]},
//                    REQUEST_PERMISSION_CODE_SYSTEM_ALERT_WINDOW);
//        }

        toLogin();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_PERMISSION_CODE_SYSTEM_ALERT_WINDOW) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "悬浮窗权限通过");
                toLogin();
            } else {
                Log.i(TAG, "权限没有通过，即将关闭应用！");
                showToast("权限没有通过，即将关闭应用！");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 2000);
            }
        }
    }

    //跳转登录页
    public void toLogin() {
        Intent intent = new Intent(PermissionActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}

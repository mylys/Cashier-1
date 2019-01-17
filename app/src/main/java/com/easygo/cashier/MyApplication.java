package com.easygo.cashier;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;
import com.easygo.cashier.module.permission.PermissionActivity;
import com.niubility.library.base.BaseApplication;
import com.niubility.library.utils.ToastUtils;

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init(this);

        ToastUtils.text_size_times = 1.5f;
    }

    @Override
    public boolean isOpenCrashHandler() {
        return true;
    }

    @Override
    public void onCrash(Thread t, Throwable e) {
        super.onCrash(t, e);

        restartAppDelayed(5000);
    }


    private void restartAppDelayed(long delayMillis) {
        Intent intent = new Intent(sApplication, PermissionActivity.class);
        @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(
                this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        //退出程序
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + delayMillis,
                restartIntent); // 5秒钟后重启应用

        //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}

package cn.demoz.www.base;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.jason.adrlog.AdrLog;
import com.wj.servicelibrary.server.WebService;

import org.mortbay.ijetty.log.AndroidLog;

/**
 * 代表当前应用程序
 *
 * @author jason
 */
public class BaseApplication extends Application {
    private static final String TAG = "BaseApplication";
    private static BaseApplication application;
    private static int mainTid;
    private static Handler handler;
    private static Intent mServiceIntent;

    static {
        // 不使用jetty的XML解析验证
        System.setProperty("org.eclipse.jetty.xml.XmlParser.Validating", "false");
        // 使用android日志类
        System.setProperty("org.eclipse.jetty.util.log.class", "org.mortbay.ijetty.AndroidLog");
        org.eclipse.jetty.util.log.Log.setLog(new AndroidLog());
    }

    private boolean isAppBackground = false;

    @Override
    //  在主线程运行的
    public void onCreate() {
        super.onCreate();
        application = this;
        mainTid = android.os.Process.myTid();
        handler = new Handler();

        AdrLog.getBuilder(getApplicationContext())
                .setDefTag("jason")
                .setLogSwitch(true)
                .setLog2FileSwitch(false)
                .setLogLevel(AdrLog.V)
                .create();

        // 启动提供数据的后台服务
        mServiceIntent = new Intent(this, WebService.class);
        startService(mServiceIntent);
        AdrLog.i(TAG, "Data Service 已启动...");

        listenForForeground();
        listenForScreenTurningOff();
    }

    // 覆写应用的生命周期回调方法，去实现监听的作用
    private void listenForForeground() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            // 回到应用的时候会调用此方法，判断app又回到了前台
            @Override
            public void onActivityResumed(Activity activity) {
                if (isAppBackground) {
                    isAppBackground = false;
                    notifyForeground();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void notifyForeground() {
        // This is where you can notify listeners, handle session tracking, etc
        startService(mServiceIntent);
        Log.i(TAG, "回到前台，数据服务已启动...");
    }

    // 在模拟进程环境下程序终止的时候执行, <注>在真机环境下，仅仅通过杀死进程是不会调用此方法的
//    @Override
//    public void onTerminate() {
//        super.onTerminate();
//        stopService(mServiceIntent);
//        Log.i(TAG, "Data Service is stopped...");
//    }

    // 使用广播监听熄屏事件
    private void listenForScreenTurningOff() {
        IntentFilter screenStateFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                isAppBackground = true;
                notifyBackground();
            }
        }, screenStateFilter);
    }



    // 低内存的时候执行
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    // 程序在内存清理的时候执行
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        // 应用回到后台，或被销毁都会走到这里
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isAppBackground = true;
            notifyBackground();
        }
    }

    private void notifyBackground() {
        stopService(mServiceIntent);
        Log.i(TAG, "回到后台，数据服务已关闭...");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public static Context getApplication() {
        return application;
    }

    public static int getMainTid() {
        return mainTid;
    }

    public static Handler getHandler() {
        return handler;
    }


}

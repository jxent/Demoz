package cn.demoz.j.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;

import com.wj.servicelibrary.server.WebService;

import org.mortbay.ijetty.log.AndroidLog;

/**
 * 代表当前应用程序
 *
 * @author jason
 */
public class BaseApplication extends Application {
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

    @Override
    //  在主线程运行的
    public void onCreate() {
        super.onCreate();
        application = this;
        mainTid = android.os.Process.myTid();
        handler = new Handler();

        // 启动数据服务Service
        mServiceIntent = new Intent(this, WebService.class);
        startService(mServiceIntent);
    }

    // 程序终止的时候执行
    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(mServiceIntent);
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

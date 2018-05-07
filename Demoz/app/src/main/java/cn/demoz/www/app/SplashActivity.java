package cn.demoz.www.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.wj.servicelibrary.server.WebService;

import java.util.List;

import cn.demoz.www.R;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by json on 2018/4/28.
 */

public class SplashActivity extends Activity implements EasyPermissions.PermissionCallbacks{

    private static final String TAG = "jason";
    private static final int REQUEST_EXTERNAL_CODE = 10001;
    private static Intent mServiceIntent;
    private String[] MY_PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ImageView splash_image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        splash_image = findViewById(R.id.splash_image);

        /*一次性请求app要求的所有权限，获取失败，禁止app运行*/
        if(!EasyPermissions.hasPermissions(SplashActivity.this, MY_PERMISSIONS)){

            // 直接请求，内部已经添加了已经授权的判断
            EasyPermissions.requestPermissions(SplashActivity.this,
                    "", REQUEST_EXTERNAL_CODE, MY_PERMISSIONS);
        }else {
            start();
        }

        initThirdService();
    }

    private void initThirdService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 设置线程优先级，不与主线程抢资源，大约只获取10%的cpu时间片
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

                // 启动耗时操作

                // 启动提供数据的后台服务
                mServiceIntent = new Intent(SplashActivity.this, WebService.class);
                startService(mServiceIntent);
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(REQUEST_EXTERNAL_CODE, permissions,
                grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == REQUEST_EXTERNAL_CODE){
            start();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(this, "请打开权限，否则无法使用", Toast.LENGTH_SHORT).show();
    }

    private void start(){
        startAnimation();
        CountDownTimer timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // ticking
            }

            @Override
            public void onFinish() {
                fadeAnimation();
            }
        };
        timer.start();
    }

    private void startAnimation() {
        AlphaAnimation alpha = new AlphaAnimation(0F, 1F);
        alpha.setDuration(800);
        alpha.setInterpolator(new AccelerateInterpolator());
        alpha.setRepeatCount(0);
        splash_image.startAnimation(alpha);
    }

    private void fadeAnimation() {
        AlphaAnimation alpha = new AlphaAnimation(1F, 0F);
        alpha.setDuration(800);
        alpha.setInterpolator(new AccelerateInterpolator());
        alpha.setRepeatCount(0);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                splash_image.setVisibility(View.INVISIBLE);
                finish();       // finish this activity
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash_image.startAnimation(alpha);
    }

}

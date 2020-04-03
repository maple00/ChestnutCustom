package com.rainwood.chestnut.base;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.rainwood.chestnut.other.ActivityStackManager;
import com.rainwood.chestnut.ui.activity.CrashActivity;
import com.rainwood.chestnut.ui.activity.HomeActivity;
import com.rainwood.tools.toast.ToastInterceptor;
import com.rainwood.tools.toast.ToastUtils;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:31
 * @des: Application 基类
 */
public class BaseApplication extends Application {

    public static BaseApplication app;


    @Override
    public void onCreate() {
        super.onCreate();
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        app = this;
        // initActivity 初始化Activity 栈管理
        initActivity();
        // 初始化三方的框架
        initSDK();
    }

    /**
     * 初始化活动
     */
    private void initActivity() {
        ActivityStackManager.getInstance().register(this);
    }

    /**
     * 初始化一些三方框架
     */
    private void initSDK() {

        // 设置 Toast 拦截器
        ToastUtils.setToastInterceptor(new ToastInterceptor() {
            @Override
            public boolean intercept(Toast toast, CharSequence text) {
                boolean intercept = super.intercept(toast, text);
                if (intercept) {
                    Log.e("Toast", "空 Toast");
                } else {
                    Log.i("Toast", text.toString());
                }
                return intercept;
            }
        });
        // 吐司工具类
        ToastUtils.init(this);

        // 本地异常捕捉
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM)
                .enabled(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                // 重启的 Activity
                .restartActivity(HomeActivity.class)
                // 错误的 Activity
                .errorActivity(CrashActivity.class)
                // 设置监听器
                //.eventListener(new YourCustomEventListener())
                .apply();

    }

    public boolean isDetermineNetwork(){
        return true;
    }
}

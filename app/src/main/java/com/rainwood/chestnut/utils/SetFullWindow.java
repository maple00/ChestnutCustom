package com.rainwood.chestnut.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/3/5 12:27
 * @Desc: 设置全屏
 */
public final class SetFullWindow {

    /**
     * 全屏显示-- 显示状态
     * @param activity
     */
    public static void AddFullFlag(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = Objects.requireNonNull(activity).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 清楚
     * @param activity
     */
    public static void ClearFlag(Activity activity){

    }
}

package com.rainwood.chestnut.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.other.ActivityStackManager;
import com.rainwood.chestnut.utils.DialogUtils;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.viewinject.ViewBind;

/**
 * @Author: shearson
 * @time: 2019/11/27 11:23
 * @des: Activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    //protected final String TAG = this.getClass().getSimpleName();
    protected final String TAG = "sxs";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putParcelable("android:support:fragments", null);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);      //禁止横屏
        super.onCreate(savedInstanceState);
        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
        }
        init();
    }

    protected void init() {
        // View 绑定
        ViewBind.inject(this);
        // 沉浸式状态栏
        setStatusBar();
        // 初始化数据
        initData();
        // 初始视图
        initView();
        // 将activity 入栈
        ActivityStackManager.getInstance().addActivity(this);
    }

    /**
     * 状态栏适配
     * 魅族，小米适配需要单独设置
     * setFUI
     * setMiUI
     */
    private void setStatusBar() {
        //  当fistsSystemWindows 设置为true时，会在屏幕的最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        // 设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        // 一般的手机的状态栏文字和图标都是白色的，如果应用是纯白的，则会导致状态栏文字看不清
        // 如果是纯白的状态栏背景，则设置状态栏使用深灰色图标风格，否则可以选择性注释掉if中的内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            // 如果不支持设置深灰色风格，为了兼容，则设置状态栏颜色半透明
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
    }


    /**
     * 布局ID
     *
     * @return 布局
     */
    protected abstract int getLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 获取当前 Activity 对象
     */
    public BaseActivity getActivity() {
        return this;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    protected void openActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }

    /**
     * 显示吐司
     */
    public void toast(CharSequence text) {
        ToastUtils.show(text);
    }

    public void toast(@StringRes int id) {
        ToastUtils.show(id);
    }

    public void toast(Object object) {
        ToastUtils.show(object);
    }

    /**
     * 延迟执行
     */
    private static final Handler HANDLER = new Handler(Looper.getMainLooper());
    public final Object mHandlerToken = hashCode();

    public final boolean post(Runnable r) {
        return postDelayed(r, 0);
    }

    /**
     * 延迟一段时间执行
     */
    public final boolean postDelayed(Runnable r, long delayMillis) {
        if (delayMillis < 0) {
            delayMillis = 0;
        }
        return postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
    }

    /**
     * 在指定的时间执行
     */
    public final boolean postAtTime(Runnable r, long uptimeMillis) {
        // 发送和这个 Activity 相关的消息回调
        return HANDLER.postAtTime(r, mHandlerToken, uptimeMillis);
    }

    /**
     * 软键盘聚焦
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 显示加载中
     */

    private DialogUtils mDialog;

    public void showLoading(String tips) {
        mDialog = new DialogUtils(this);
        mDialog.showDialog(tips);
    }

    public void dismissLoading() {
        mDialog.dismissDialog();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mDialog != null) {
            mDialog.dismissDialog();
        }
    }

    @Override
    protected void onDestroy() {
        ActivityStackManager.getInstance().removeActivity(this);
        super.onDestroy();
        if (mDialog != null) {
            mDialog.dismissDialog();
        }
    }
    /**
     * 隐藏软键盘
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                v.clearFocus();
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (im != null) {
                im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

}
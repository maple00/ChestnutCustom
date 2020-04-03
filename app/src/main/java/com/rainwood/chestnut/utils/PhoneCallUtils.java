package com.rainwood.chestnut.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.toast.ToastUtils;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * @Author: a797s
 * @Date: 2020/3/3 13:31
 * @Desc: 拨打电话
 */
public final class PhoneCallUtils  {

    /**
     * 拨打电话（直接拨打电话） -- 需要申请电话权限
     * @param phoneNum 电话号码
     */
    public static void callPhone(Activity activity, String phoneNum){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        XXPermissions.with(activity)
                .constantRequest()
                .permission(Permission.CALL_PHONE)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermission() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            // toast("获取权限成功");
                            activity.startActivity(intent);
                        } else {
                           Log.d(TAG, "获取权限成功，部分权限未正常授予");
                        }
                    }
                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            ToastUtils.show("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(activity);
                        } else {
                            ToastUtils.show("获取权限失败");
                        }
                    }
                });
    }


    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）-- 此种方式不需要申请权限
     * @param phoneNum 电话号码
     */
    public static void callPhoneDump(Activity activity, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        activity.startActivity(intent);
    }
}

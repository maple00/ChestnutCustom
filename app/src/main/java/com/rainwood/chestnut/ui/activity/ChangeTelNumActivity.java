package com.rainwood.chestnut.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.utils.CountDownTimerUtils;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.InputTextHelper;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/3/1 17:25
 * @Desc: 修改手机号
 */
public final class ChangeTelNumActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_num;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.btn_get_code)
    private Button getCode;
    @ViewById(R.id.et_check_phone)
    private ClearEditText checkPhone;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        getCode.setOnClickListener(this);
        // 设置聚焦
        showSoftInputFromWindow(checkPhone);
        // 手机号格式校验,如果输入的手机号格式不正确，则按钮置灰  -- 当从验证码界面返回的时候，需要多添加一个监听状态
        InputTextHelper.with(this)
                .addView(checkPhone)
                .setMain(getCode)
                .setListener(helper -> checkPhone.getText().toString().length() == 11).build();
    }

    @Override
    protected void initData() {
        super.initData();
        // 获取之前输入过最近的一次手机号,填写在EditTExt上
        if (Contants.telNum != null) {
            checkPhone.setText(Contants.telNum);
        }
        // 如果任务定时器的时间不足一分钟，则不允许再次获取验证码
        if (CountDownTimerUtils.CountDownTimerSize > 0) {
            CountDownTimerUtils.initCountDownTimer(CountDownTimerUtils.CountDownTimerSize / 1000,
                    getCode, "重新发送", getResources().getColor(R.color.white), getResources().getColor(R.color.white));
            CountDownTimerUtils.countDownTimer.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_get_code:
                if (TextUtils.isEmpty(checkPhone.getText())) {
                    toast("请输入手机号");
                    return;
                }
                XXPermissions.with(getActivity())
                        // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                        .constantRequest()
                        .permission(Permission.RECEIVE_SMS, Permission.READ_SMS)    // 短信的读取权限
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                if (isAll) {
                                    // 记录最新填写的手机号
                                    Contants.telNum = checkPhone.getText().toString().trim();
                                    // request
                                    showLoading("loading");
                                    RequestPost.getVerifyCode(checkPhone.getText().toString().trim(), "", ChangeTelNumActivity.this);
                                } else {
                                    toast("权限不足，请开启");
                                }
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                                if (quick) {
                                    toast("被永久拒绝授权，请手动授予权限");
                                    //如果是被永久拒绝就跳转到应用权限系统设置页面
                                    XXPermissions.gotoPermissionSettings(getActivity());
                                } else {
                                    toast("获取权限失败");
                                }
                            }
                        });
                break;
        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取验证码
                if (result.url().contains("wxapi/v1/clientLogin.php?type=getCode")) {
                    toast(body.get("warn"));
                    Intent intent = new Intent(ChangeTelNumActivity.this, CodeVerifyActivity.class);
                    intent.putExtra("tel", checkPhone.getText().toString().trim());
                    startActivity(intent);
                }
            }else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

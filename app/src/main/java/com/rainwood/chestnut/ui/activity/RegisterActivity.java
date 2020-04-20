package com.rainwood.chestnut.ui.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.utils.CountDownTimerUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

import static com.rainwood.chestnut.common.Contants.telNum;

/**
 * @Author: a797s
 * @Date: 2020/3/2 13:28
 * @Desc: 注册
 */
public final class RegisterActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @ViewById(R.id.et_register_account)
    private ClearEditText registerAccount;
    @ViewById(R.id.tv_send_code)
    private TextView sendCode;
    @ViewById(R.id.et_ver_code)
    private ClearEditText verCode;
    @ViewById(R.id.et_register_password)
    private PasswordEditText registerPwd;
    @ViewById(R.id.btn_register)
    private Button register;
    @ViewById(R.id.tv_login_now)
    private TextView loginNow;

    @Override
    protected void initView() {
        sendCode.setOnClickListener(this);
        register.setOnClickListener(this);
        loginNow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_code:
                if (TextUtils.isEmpty(registerAccount.getText())) {
                    toast("请输入密码");
                    return;
                }
                // countDown
                // 初始化定时器
                CountDownTimerUtils.initCountDownTimer(60, sendCode, "重新发送验证码",
                        getResources().getColor(R.color.fontColor), getResources().getColor(R.color.red30));
                CountDownTimerUtils.countDownTimer.start();
                // request -- 获取验证码
                showLoading("");
                RequestPost.getVerifyCode(registerAccount.getText().toString().trim(), "register", this);
                break;
            case R.id.btn_register:
                // 注册成功之后 -- 返回登录页面，输入密码登录
                if (TextUtils.isEmpty(registerAccount.getText())) {
                    toast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(verCode.getText())) {
                    toast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(registerPwd.getText())) {
                    toast("请输入密码");
                    return;
                }
                // request --- 注册
                showLoading("");
                RequestPost.registerUser(verCode.getText().toString().trim(), registerAccount.getText().toString().trim(),
                        registerPwd.getText().toString().trim(), this);
                break;
            case R.id.tv_login_now:
                openActivity(LoginActivity.class);
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
                // 注册
                if (result.url().contains("wxapi/v1/clientLogin.php?type=registerUser")) {
                    toast(body.get("warn"));
                    telNum = registerAccount.getText().toString().trim();
                    postDelayed(this::finish, 1000);
                }
                // 获取验证码
                if (result.url().contains("wxapi/v1/clientLogin.php?type=getCode")) {
                    toast(body.get("warn"));
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

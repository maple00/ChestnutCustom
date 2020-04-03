package com.rainwood.chestnut.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/3/2 13:28
 * @Desc: 注册
 */
public final class RegisterActivity extends BaseActivity implements View.OnClickListener {

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
    private ClearEditText registerPwd;
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
                toast("发送验证码");
                break;
            case R.id.btn_register:
                toast("注册成功");
                openActivity(LoginActivity.class);
                break;
            case R.id.tv_login_now:
                // 注册成功之后 -- 返回登录页面，输入密码登录
                openActivity(LoginActivity.class);
                break;
        }
    }
}

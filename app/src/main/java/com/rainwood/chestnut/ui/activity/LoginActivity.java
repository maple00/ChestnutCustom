package com.rainwood.chestnut.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/3/2 13:18
 * @Desc: 登录
 */
public final class LoginActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @ViewById(R.id.et_login_account)
    private ClearEditText account;
    @ViewById(R.id.et_login_password)
    private PasswordEditText password;
    @ViewById(R.id.tv_forget_pwd)
    private TextView forgetPwd;
    @ViewById(R.id.btn_login_commit)
    private Button confirm;
    @ViewById(R.id.tv_register)
    private TextView register;

    @Override
    protected void initView() {
        forgetPwd.setOnClickListener(this);
        confirm.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_pwd:
                // toast("忘记密码");
                openActivity(RetrievePwdActivity.class);
                break;
            case R.id.btn_login_commit:
                 toast("登录成功");
                openActivity(HomeActivity.class);
                break;
            case R.id.tv_register:
                // toast("注册");
                openActivity(RegisterActivity.class);
                break;
        }
    }

}

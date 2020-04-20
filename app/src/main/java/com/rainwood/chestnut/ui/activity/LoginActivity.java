package com.rainwood.chestnut.ui.activity;

import android.text.TextUtils;
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
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

import static com.rainwood.chestnut.common.Contants.telNum;

/**
 * @Author: a797s
 * @Date: 2020/3/2 13:18
 * @Desc: 登录
 */
public final class LoginActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
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
        //
        if (telNum != null){
            account.setText(telNum);
        }
        account.setText("15847251880");
        password.setText("123456");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget_pwd:
                // toast("忘记密码");
                openActivity(RetrievePwdActivity.class);
                break;
            case R.id.btn_login_commit:
                if (TextUtils.isEmpty(account.getText())) {
                    toast("请输入账号");
                    return;
                }
                if (TextUtils.isEmpty(password.getText())) {
                    toast("请输入密码");
                    return;
                }
                // request -- 登录
                showLoading("");
                RequestPost.login(account.getText().toString().trim(), password.getText().toString().trim(), this);
                break;
            case R.id.tv_register:
                // toast("注册");
                openActivity(RegisterActivity.class);
                break;
        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " ---- result ----- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 登录
                if (result.url().contains("wxapi/v1/clientLogin.php?type=login")) {
                    toast(body.get("warn"));
                    postDelayed(() -> openActivity(HomeActivity.class), 500);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

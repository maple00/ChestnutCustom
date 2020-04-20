package com.rainwood.chestnut.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/2 13:51
 * @Desc: 重置密码
 */
public final class ReSetPwdActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_password;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.cet_password)
    private ClearEditText password;
    @ViewById(R.id.cet_password_again)
    private ClearEditText passwordAgain;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("重置密码");
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(password.getText())){
                    toast("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(passwordAgain.getText())){
                    toast("请输入确认密码");
                    return;
                }
                if (!passwordAgain.getText().toString().trim().equals(password.getText().toString().trim())){
                    toast("两次输入密码不一致");
                    return;
                }
                // request -- 重置密码
                showLoading("");
                RequestPost.resetPwd(password.getText().toString().trim(), passwordAgain.getText().toString().trim(), this);

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
                // 重置密码
                if (result.url().contains("wxapi/v1/clientLogin.php?type=changePwd")) {
                    toast(body.get("warn"));
                    postDelayed(() -> openActivity(HomeActivity.class), 1000);
                }
            }else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

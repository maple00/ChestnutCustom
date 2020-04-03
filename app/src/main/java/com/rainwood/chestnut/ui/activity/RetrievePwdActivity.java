package com.rainwood.chestnut.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/3/2 13:42
 * @Desc: 找回密码
 */
public final class RetrievePwdActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_retrieve_password;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.et_retrieve_account)
    private ClearEditText retrieveAccount;
    @ViewById(R.id.tv_send_code)
    private TextView sendCode;
    @ViewById(R.id.et_ver_code)
    private ClearEditText verCode;
    @ViewById(R.id.btn_next_step)
    private Button nextStep;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("找回密码");
        sendCode.setOnClickListener(this);
        nextStep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_code:
                toast("发送验证码");
                break;
            case R.id.btn_next_step:
                // toast("下一步");
                openActivity(ReSetPwdActivity.class);
                break;
        }
    }
}

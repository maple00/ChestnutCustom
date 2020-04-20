package com.rainwood.chestnut.ui.activity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.utils.CountDownTimerUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/2 13:42
 * @Desc: 找回密码
 */
public final class RetrievePwdActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
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
                if (TextUtils.isEmpty(retrieveAccount.getText())) {
                    toast("请输入手机号");
                    return;
                }
                // request -- 找回密码
                showLoading("");
                RequestPost.getVerifyCode(retrieveAccount.getText().toString().trim(), "pwd", this);
                break;
            case R.id.btn_next_step:                // 校验验证码
                if (TextUtils.isEmpty(retrieveAccount.getText())) {
                    toast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(verCode.getText())) {
                    toast("请输入验证码");
                    return;
                }
                // request
                showLoading("");
                RequestPost.compareCode(retrieveAccount.getText().toString().trim(), verCode.getText().toString().trim(), this);
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        // 获取之前输入过最近的一次手机号,填写在EditTExt上
        if (Contants.telNum != null) {
            retrieveAccount.setText(Contants.telNum);
        }
        // 如果任务定时器的时间不足一分钟，则不允许再次获取验证码 -- 相同手机号时间间隔
        if (Contants.telNum != null && Contants.telNum.equals(retrieveAccount.getText().toString().trim())) {
            if (CountDownTimerUtils.CountDownTimerSize > 0) {
                CountDownTimerUtils.initCountDownTimer(CountDownTimerUtils.CountDownTimerSize / 1000,
                        sendCode, "重新发送", getResources().getColor(R.color.white), getResources().getColor(R.color.white));
                CountDownTimerUtils.countDownTimer.start();
            }
        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " =========== result ======== " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取验证码
                if (result.url().contains("wxapi/v1/clientLogin.php?type=getCode")) {
                    CountDownTimerUtils.initCountDownTimer(60, sendCode, "可重新发送验证码",
                            getResources().getColor(R.color.fontColor), getResources().getColor(R.color.red30));
                    CountDownTimerUtils.countDownTimer.start();
                    toast(body.get("warn"));
                }
                // 比较验证码
                if (result.url().contains("wxapi/v1/clientLogin.php?type=compareCode")) {
                    toast(body.get("warn"));
                    openActivity(ReSetPwdActivity.class);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

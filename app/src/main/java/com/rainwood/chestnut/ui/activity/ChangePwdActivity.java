package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.ComEditBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.ChangePwdAdapter;
import com.rainwood.chestnut.utils.CountDownTimerUtils;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.InputTextHelper;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/2 11:07
 * @Desc: 修改密码
 */
public final class ChangePwdActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.btn_confirm)
    private Button confirm;
    //
    @ViewById(R.id.cet_tel)
    private ClearEditText tel;
    @ViewById(R.id.tv_send_code)
    private TextView sendCode;
    @ViewById(R.id.cet_verify)
    private ClearEditText verify;
    @ViewById(R.id.cet_new_pwd)
    private ClearEditText newPwd;
    @ViewById(R.id.cet_new_pwd_again)
    private ClearEditText newPwdAgain;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("修改密码");
        confirm.setOnClickListener(this);
        sendCode.setOnClickListener(this);

        InputTextHelper.with(this)
                .addView(tel)
                .setMain(sendCode)
                .setListener(helper -> tel.getText().toString().length() == 11).build();
    }

    @Override
    protected void initData() {
        super.initData();
        // 获取之前输入过最近的一次手机号,填写在EditTExt上
        if (Contants.telNum != null) {
            tel.setText(Contants.telNum);
        }
        // 如果任务定时器的时间不足一分钟，则不允许再次获取验证码
        if (CountDownTimerUtils.CountDownTimerSize > 0) {
            CountDownTimerUtils.initCountDownTimer(CountDownTimerUtils.CountDownTimerSize / 1000,
                    sendCode, "重新发送", getResources().getColor(R.color.white), getResources().getColor(R.color.white));
            CountDownTimerUtils.countDownTimer.start();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_code:             // 发送验证码
                if (TextUtils.isEmpty(tel.getText())){
                    toast("请输入手机号");
                    return;
                }
                CountDownTimerUtils.initCountDownTimer(60, sendCode, "可重新发送验证码",
                        getResources().getColor(R.color.fontColor), getResources().getColor(R.color.red30));
                sendCode.setTextColor(getResources().getColor(R.color.fontColor));
                sendCode.setClickable(false);
                CountDownTimerUtils.countDownTimer.start();
                // request -- 发送验证码
                showLoading("");
                RequestPost.getVerifyCode(tel.getText().toString().trim(), "", this);
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(tel.getText())){
                    toast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(verify.getText())){
                    toast("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(newPwd.getText())){
                    toast("请输入新密码");
                    return;
                }
                if (TextUtils.isEmpty(newPwdAgain.getText())){
                    toast("请输入确认密码");
                    return;
                }
                // request
                showLoading("");
                RequestPost.changePassword(tel.getText().toString().trim(), verify.getText().toString().trim(),
                        newPwd.getText().toString().trim(), newPwdAgain.getText().toString().trim(), this);
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
                    ToastUtils.show(body.get("warn"));
                }
                // 修改密码
                if (result.url().contains("wxapi/v1/clientPerson.php?type=changePassword")){
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }
            }else {
                ToastUtils.show(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.domain.ComEditBean;
import com.rainwood.chestnut.ui.adapter.ChangePwdAdapter;
import com.rainwood.chestnut.utils.CountDownTimerUtils;
import com.rainwood.tools.view.InputTextHelper;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/2 11:07
 * @Desc: 修改密码
 */
public final class ChangePwdActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.mlv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    private List<ComEditBean> mList;
    private String[] titles = {"手机号", "验证码", "新密码", "新密码"};
    private String[] labels = {"请输入", "请输入", "请输入新密码", "请再次输入新密码"};
    // mHandler
    private final int INTAIL_SIZE = 0x101;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("修改密码");
        confirm.setOnClickListener(this);

        Message msg = new Message();
        msg.what = INTAIL_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            ComEditBean comEdit = new ComEditBean();
            comEdit.setTitle(titles[i]);
            comEdit.setHint(labels[i]);
            mList.add(comEdit);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                toast("修改成功");
                finish();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case INTAIL_SIZE:
                    ChangePwdAdapter pwdAdapter = new ChangePwdAdapter(ChangePwdActivity.this, mList);
                    contentList.setAdapter(pwdAdapter);
                    break;
            }
        }
    };
}

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
 * @Date: 2020/3/2 12:04
 * @Desc: 意见反馈
 */
public final class FeedBackActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.et_feedback_text)
    private ClearEditText feedbackText;     // 问题描述
    @ViewById(R.id.et_contact_tel)
    private ClearEditText contactTel;       // 联系电话
    @ViewById(R.id.btn_commit)
    private Button commit;                  // 提交

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        commit.setOnClickListener(this);
        pageTitle.setText("意见反馈");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                toast("提交");
                break;
        }
    }
}

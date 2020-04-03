package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/3/1 19:01
 * @Desc: 消息详情
 */
public final class MessageDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_content)
    private TextView content;
    @ViewById(R.id.tv_time)
    private TextView time;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("消息详情");

        // request
        showLoading("loading");
        String msgId = getIntent().getStringExtra("msgId");
        RequestPost.getMessageDetail(msgId, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
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
                // 查看消息详情
                if (result.url().contains("wxapi/v1/clientPerson.php?type=getMessageInfo")) {
                    Map<String, String> msg = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info"));
                    // content
                    content.setText(msg.get("text"));
                    time.setText(msg.get("time"));
                    // 查看之后要更改消息的状态
                    
                }
            }else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

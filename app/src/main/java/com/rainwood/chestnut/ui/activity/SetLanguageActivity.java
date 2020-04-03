package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.domain.LanguageBean;
import com.rainwood.chestnut.ui.adapter.LanguageAdapter;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/2 10:40
 * @Desc: 语言设置
 */
public final class SetLanguageActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_language;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content_list)
    private ListView contentList;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    // mHandler
    private final int INITAIL_SIZE = 0x101;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        pageTitle.setText("语言设置");

        Message msg = new Message();
        msg.what = INITAIL_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < languages.length; i++) {
            LanguageBean language = new LanguageBean();
            language.setName(languages[i]);
            language.setImg(null);
            if (i == 0){
                language.setChecked(true);
            }
            mList.add(language);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                // toast("确定");
                // openActivity(HomeActivity.class);
                finish();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITAIL_SIZE:
                    LanguageAdapter languageAdapter = new LanguageAdapter(SetLanguageActivity.this, mList);
                    contentList.setAdapter(languageAdapter);
                    languageAdapter.setOnClickItem(position -> {
                        toast("语言设置：" + mList.get(position).getName());
                        for (LanguageBean language : mList) {
                            language.setChecked(false);
                        }
                        mList.get(position).setChecked(true);

                        Message msg1 = new Message();
                        msg1.what = INITAIL_SIZE;
                        mHandler.sendMessage(msg1);
                    });
                    break;
            }
        }
    };

    /*
    模拟数据
     */
    private List<LanguageBean> mList;
    private String[] languages = {"中文", "English", "Italiano"};
}

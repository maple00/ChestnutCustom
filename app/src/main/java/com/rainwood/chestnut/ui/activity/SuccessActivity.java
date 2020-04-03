package com.rainwood.chestnut.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2020/3/3 13:42
 * @Desc: 成功页面
 */
public final class SuccessActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_success;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;

    @ViewById(R.id.tv_query)
    private TextView queryOrder;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        queryOrder.setOnClickListener(this);
        pageTitle.setText("提交成功");
        //showLoading("加载中");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:          // 返回到购物车
                openActivity(HomeActivity.class);
                finish();
                break;
            case R.id.tv_query:         // 跳转到我的订单
                openActivity(HomeActivity.class);
                finish();
                break;
        }
    }
}

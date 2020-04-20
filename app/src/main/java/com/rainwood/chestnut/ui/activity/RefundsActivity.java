package com.rainwood.chestnut.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.other.BaseDialog;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.dialog.MessageDialog;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

import static com.rainwood.chestnut.common.Contants.REFUNDS_APPLYING;
import static com.rainwood.chestnut.common.Contants.REFUNDS_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/4/7 14:57
 * @Desc: 退货
 */
public final class RefundsActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mOrderId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_refunds;
    }

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.et_reason)
    private EditText etReason;
    @ViewById(R.id.btn_commit)
    private Button commit;

    @Override
    protected void initView() {
        mPageBack.setOnClickListener(this);
        commit.setOnClickListener(this);
        mOrderId = getIntent().getStringExtra("orderId");
        if (Contants.GOODS_REFUNDS_SIZE == 0x101){
            etReason.setClickable(true);
            etReason.setFocusable(true);
            etReason.setFocusableInTouchMode(true);
            mPageTitle.setText("退货申请");
        }
        if (Contants.GOODS_REFUNDS_SIZE == 0x102){
            mPageTitle.setText("申请中");
            etReason.setClickable(false);
            etReason.setFocusable(false);
            etReason.setFocusableInTouchMode(false);
            // TODO：退货页面数据
            showLoading("");
            RequestPost.getRefundPage(mOrderId, this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_commit:
                if (Contants.GOODS_REFUNDS_SIZE == 0x102){
                    new MessageDialog.Builder(this)
                            .setMessage("确定要撤销退货申请吗？")
                            // 确定文本按钮
                            .setConfirm(getString(R.string.common_confirm))
                            // 取消按钮，设置null之后不显示取消按钮
                            .setCancel("我再想想")
                            // 设置点击按钮后不关闭对话框
                            .setAutoDismiss(false)
                            .setListener(new MessageDialog.OnListener() {
                                @Override
                                public void onConfirm(BaseDialog dialog) {
                                    dialog.dismiss();
                                    // request
                                    showLoading("");
                                    RequestPost.cancelRefund(mOrderId, RefundsActivity.this);
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                }else {
                    if (TextUtils.isEmpty(etReason.getText())) {
                        toast("请输入退货原因");
                        return;
                    }
                    // request
                    showLoading("");
                    RequestPost.commitRefundOrder(mOrderId, etReason.getText().toString().trim(), this);
                }
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
                // 提交退货申请
                if (result.url().contains("wxapi/v1/clientGoods.php?type=commitRefundOrder")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }
                // 获取退货数据
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getRefundPage")){
                    etReason.setText(JsonParser.parseJSONObject(body.get("data")).get("text"));
                }
                // 撤销退货申请
                if (result.url().contains("wxapi/v1/clientGoods.php?type=cancelRefund")){
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

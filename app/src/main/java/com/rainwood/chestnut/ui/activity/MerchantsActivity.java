package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.base.BaseDialogFragment;
import com.rainwood.chestnut.domain.MerchantsBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.MerchantsAdapter;
import com.rainwood.chestnut.ui.fragment.HomeFragment;
import com.rainwood.chestnut.utils.CountDownTimerUtils;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/3/1 19:09
 * @Desc: 我的商家
 */
public final class MerchantsActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchants;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView pageRightTitle;
    @ViewById(R.id.mlv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.drl_refresh)
    private DaisyRefreshLayout mRefreshLayout;

    private List<MerchantsBean> mList;
    private PopupWindow mPopupWindow;
    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int REFRESH_SIZE = 0x102;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("我的商家");
        pageRightTitle.setBackground(getResources().getDrawable(R.drawable.shape_radius_red_full_14));
        pageRightTitle.setPadding(24, 20, 24, 20);
        pageRightTitle.setTextColor(getResources().getColor(R.color.white));
        pageRightTitle.setTextSize(14);
        pageRightTitle.setText("添加商家");
        pageRightTitle.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // request
        showLoading("loading");
        RequestPost.getStoreList("", this);
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right_text:                // 添加商家
                // toast("添加商家");
               /* new BaseDialogFragment.Builder(getActivity())
                        .setContentView(R.layout.dialog_add_merchant)
                        .setText(R.id.tv_title, "添加商家")
                        .setOnClickListener(R.id.btn_confirm_add, (dialog, view) -> {
                            toast("添加成功");
                            dialog.dismiss();
                            postDelayed(() -> {
                                mList.add(mMerchants);
                                Message msg1 = new Message();
                                msg1.what = INITIAL_SIZE;
                                mHandler.sendMessage(msg1);
                            }, 500);
                        })
                        .setOnClickListener(R.id.tv_obtain_pwd, (dialog, view) -> {
                            // 商家编号
                            TextView obtainPwd = view.findViewById(R.id.tv_obtain_pwd);
                            CountDownTimerUtils.initCountDownTimer(60, obtainPwd, "可重新获取", R.color.fontColor, R.color.red30);
                            CountDownTimerUtils.countDownTimer.start();
                        })
                        .setGravity(Gravity.CENTER_VERTICAL)
                        .setOnClickListener(R.id.iv_dismiss, (dialog, view) -> dialog.dismiss())
                        .setCanceledOnTouchOutside(false)
                        .show();*/
                View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_merchant, null);
                mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
                mPopupWindow.setAnimationStyle(R.style.IOSAnimStyle);
                mPopupWindow.setOutsideTouchable(false);
                mPopupWindow.setContentView(view);
                // 获取商家口令
                TextView obtainPwd = view.findViewById(R.id.tv_obtain_pwd);
                obtainPwd.setOnClickListener(v13 -> {
                    EditText etNumber = view.findViewById(R.id.et_number);
                    if (TextUtils.isEmpty(etNumber.getText())) {
                        toast("请输入商家编号");
                        return;
                    }
                    // request
                    showLoading("loading");
                    RequestPost.getStoreCode(etNumber.getText().toString().trim(), this);
                    // 商家编号
                    TextView obtainPwd1 = view.findViewById(R.id.tv_obtain_pwd);
                    CountDownTimerUtils.initCountDownTimer(60, obtainPwd1, "可重新获取",
                            getResources().getColor(R.color.fontColor), getResources().getColor(R.color.red30));
                    CountDownTimerUtils.countDownTimer.start();
                });
                // 口令
                ClearEditText cet_pwd = view.findViewById(R.id.cet_pwd);
                // 确认添加
                Button confirm = view.findViewById(R.id.btn_confirm_add);
                confirm.setOnClickListener(v1 -> {
                    if (TextUtils.isEmpty(cet_pwd.getText())) {
                        toast("请输入口令");
                        return;
                    }
                    mPopupWindow.dismiss();
                    // request
                    showLoading("loading");
                    RequestPost.joinStore(obtainPwd.getText().toString().trim(),
                            cet_pwd.getText().toString().trim(), this);
                });
                ImageView dismiss = view.findViewById(R.id.iv_dismiss);
                dismiss.setOnClickListener(v12 -> mPopupWindow.dismiss());
                mPopupWindow.setOnDismissListener(() -> mPopupWindow.dismiss());
                //显示PopupWindow
                View rootview = LayoutInflater.from(this).inflate(R.layout.fragment_home, null);
                mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
                /**
                 * 点击popupWindow让背景变暗
                 */
                final WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.7f;//代表透明程度，范围为0 - 1.0f
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(lp);
                /**
                 * 退出popupWindow时取消暗背景
                 */
                mPopupWindow.setOnDismissListener(() -> {
                    lp.alpha = 1.0f;
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    getActivity().getWindow().setAttributes(lp);
                });
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_SIZE:
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        Log.i(TAG, "load start");
                       // mList.add(mMerchants);
                        postDelayed(() -> {
                            Log.i(TAG, "response ok");
                            // request
                            showLoading("loading");
                            RequestPost.getStoreList("", MerchantsActivity.this);
                        }, 1000);
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        Log.i(TAG, "refresh start");
                       // mList.add(mMerchants);
                        postDelayed(() -> {
                            Log.i(TAG, "response ok");
                            // request
                            showLoading("loading");
                            RequestPost.getStoreList("", MerchantsActivity.this);
                        }, 1000);
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        Log.i(TAG, " --- 开始加载数据了");
                        postDelayed(() -> {
                            Message msgContent = new Message();
                            msgContent.what = INITIAL_SIZE;
                            mHandler.sendMessage(msgContent);
                            mRefreshLayout.setRefreshing(false);
                        }, 1000 );
                    });
                    mRefreshLayout.autoRefresh();
                    break;
                case INITIAL_SIZE:
                    // content
                    MerchantsAdapter merchantsAdapter = new MerchantsAdapter(MerchantsActivity.this, mList);
                    contentList.setAdapter(merchantsAdapter);
                    // 查看详情
                    merchantsAdapter.setOnClickItem(position -> openActivity(StoreDetailActivity.class));
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        // Log.d(TAG, " ---- result ----- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取商家列表
                if (result.url().contains("wxapi/v1/clientPerson.php?type=getMyStorelist")) {
                    mList = JsonParser.parseJSONArray(MerchantsBean.class, JsonParser.parseJSONObject(body.get("data")).get("storelist"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 获取商家口令
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getStoreCode")) {
                    Log.d(TAG, "口令 ======== " + body.get("data"));
                    toast(body.get("warn"));
                }
                // 添加商家
                if (result.url().contains("wxapi/v1/clientGoods.php?type=joinStore")) {
                    toast(body.get("warn"));
                    // 重新刷新页面
                    RequestPost.getStoreList("", this);
                }
            }else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

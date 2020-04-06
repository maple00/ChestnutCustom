package com.rainwood.chestnut.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.rainwood.chestnut.base.BaseFragment;
import com.rainwood.chestnut.domain.MerchantsBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.activity.SearchViewActivity;
import com.rainwood.chestnut.ui.activity.StoreDetailActivity;
import com.rainwood.chestnut.ui.adapter.MerchantsAdapter;
import com.rainwood.chestnut.utils.CountDownTimerUtils;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/2/29 16:25
 * @Desc: java类作用描述
 */
public final class HomeFragment extends BaseFragment implements View.OnClickListener, OnHttpListener {

    @Override
    protected int initLayout() {
        return R.layout.fragment_home;
    }

    private LinearLayout notMerchants;          // 未添加商家的Item
    private LinearLayout merchants;             // 商家列表Item
    private MeasureListView contentList;
    private DaisyRefreshLayout mRefreshLayout;
    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int REFRESH_SIZE = 0x102;

    private List<MerchantsBean> mList;
    private PopupWindow mPopupWindow;

    @Override
    protected void initView(View view) {
        // 搜索
        ClearEditText cet_search = view.findViewById(R.id.cet_search);
        cet_search.setFocusable(false);
        cet_search.setFocusableInTouchMode(false);
        cet_search.setOnClickListener(this);
        // 没有商家
        notMerchants = view.findViewById(R.id.ll_not_merchants);
        Button addNow = view.findViewById(R.id.btn_add_now);
        addNow.setOnClickListener(this);
        // 有商家
        merchants = view.findViewById(R.id.ll_merchants);
        contentList = view.findViewById(R.id.mlv_content_list);
        TextView addMerchant = view.findViewById(R.id.tv_add_merchants);
        addMerchant.setOnClickListener(this);
        // 下拉刷新，上拉加载
        mRefreshLayout = view.findViewById(R.id.drl_refresh);
    }

    @Override
    public void onResume() {
        super.onResume();
        // request
        showLoading("loading");
        RequestPost.getStoreList("", this);
    }

    @Override
    protected void initData(Context mContext) {
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_now:              // 没有商家，立即添加商家
            case R.id.tv_add_merchants:         // 有商家 -- 弹窗
                View view = LayoutInflater.from(getmContext()).inflate(R.layout.dialog_add_merchant, null);
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
                    RequestPost.getStoreCode(etNumber.getText().toString().trim(), HomeFragment.this);
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
                            cet_pwd.getText().toString().trim(), HomeFragment.this);
                });
                ImageView dismiss = view.findViewById(R.id.iv_dismiss);
                dismiss.setOnClickListener(v12 -> mPopupWindow.dismiss());
                mPopupWindow.setOnDismissListener(() -> mPopupWindow.dismiss());
                //显示PopupWindow
                View rootview = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, null);
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
            case R.id.cet_search:                // 搜索
                startActivity(SearchViewActivity.class);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    if (mList != null && mList.size() != 0) {            // 当有商家的时候
                        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
                        merchants.setVisibility(View.VISIBLE);
                        notMerchants.setVisibility(View.GONE);
                        MerchantsAdapter merchantsAdapter = new MerchantsAdapter(getActivity(), mList);
                        contentList.setAdapter(merchantsAdapter);
                        merchantsAdapter.setOnClickItem(position -> {
                            // toast("查看商铺详情: " + position);
                            // request
                            showLoading("");
                            RequestPost.enterStoreDetail(mList.get(position).getStoreId(), HomeFragment.this);
                        });
                    } else {             // 当没有商家的时候
                        // 设置状态栏背景
                        // StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.gray15));
                        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
                        merchants.setVisibility(View.GONE);
                        notMerchants.setVisibility(View.VISIBLE);
                    }
                    break;
                case REFRESH_SIZE:
                    // 上拉加载
                    /*mRefreshLayout.setOnLoadMoreListener(() -> {
                        Log.i(TAG, "load start");
                        mList.add(merchant);
                        postDelayed(() -> {
                            Log.i(TAG, "response ok");
                            Message msgContent = new Message();
                            msgContent.what = INITIAL_SIZE;
                            mHandler.sendMessage(msgContent);
                            mRefreshLayout.setLoadMore(false);
                        }, 1000 * 3);
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        Log.i(TAG, "refresh start");
                        mList.add(merchant);
                        postDelayed(() -> {
                            Log.i(TAG, "response ok");
                            Message msgContent = new Message();
                            msgContent.what = INITIAL_SIZE;
                            mHandler.sendMessage(msgContent);
                            mRefreshLayout.setRefreshing(false);
                        }, 1000 * 3);
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        Log.i(TAG, " --- 开始加载数据了");
                        postDelayed(() -> {
                            Message msgContent = new Message();
                            msgContent.what = INITIAL_SIZE;
                            mHandler.sendMessage(msgContent);
                            mRefreshLayout.setRefreshing(false);
                        }, 1000 * 3);
                    });
                    mRefreshLayout.autoRefresh();*/
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
                    // initial
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
                // 点击商家进入详情页面时
                if (result.url().contains("wxapi/v1/clientGoods.php?type=enterStoreInfo")) {
                    toast(body.get("warn"));
                    postDelayed(() -> startActivity(StoreDetailActivity.class), 500);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

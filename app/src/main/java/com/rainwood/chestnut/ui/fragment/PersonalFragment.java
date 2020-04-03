package com.rainwood.chestnut.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseFragment;
import com.rainwood.chestnut.domain.CommonListBean;
import com.rainwood.chestnut.domain.CustomBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.other.BaseDialog;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.activity.ChangePwdActivity;
import com.rainwood.chestnut.ui.activity.ChangeTelNumActivity;
import com.rainwood.chestnut.ui.activity.FeedBackActivity;
import com.rainwood.chestnut.ui.activity.LoginActivity;
import com.rainwood.chestnut.ui.activity.MerchantsActivity;
import com.rainwood.chestnut.ui.activity.MessageActivity;
import com.rainwood.chestnut.ui.activity.SetLanguageActivity;
import com.rainwood.chestnut.ui.activity.ShipAddressActivity;
import com.rainwood.chestnut.ui.adapter.PersonalListAdapter;
import com.rainwood.chestnut.ui.adapter.PersonalTopAdapter;
import com.rainwood.chestnut.ui.dialog.MessageDialog;
import com.rainwood.chestnut.ui.dialog.UpdateDialog;
import com.rainwood.chestnut.utils.CleanCacheUtils;
import com.rainwood.chestnut.utils.PhoneCallUtils;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: shearson
 * @Time: 2020/2/29 16:26
 * @Desc: java类作用描述
 */
public final class PersonalFragment extends BaseFragment implements View.OnClickListener, OnHttpListener {

    private TextView mTelNum;
    private ImageView mHeadPhoto;

    @Override
    protected int initLayout() {
        return R.layout.fragment_personal;
    }

    private MeasureGridView topItem;
    private MeasureListView contentList;
    // 缓存
    private String totalCacheSize;
    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int CONTENT_LIST_SIZE = 0x102;

    private List<CommonListBean> mList;
    private String[] moduleName = {"语言设置", "修改密码", "意见反馈", "联系我们", "清理缓存", "版本更新"};
    private int[] moduleIcon = {R.drawable.ic_icon_me_language, R.drawable.ic_icon_me_set_up,
            R.drawable.ic_icon_me_feedback, R.drawable.ic_icon_me_about, R.drawable.ic_icon_me_clear,
            R.drawable.ic_icon_me_upgrade};
    private List<CommonListBean> topList;
    private String[] topNames = {"消息", "我的商家", "收货地址"};
    private int[] topImages = {R.drawable.ic_message, R.drawable.ic_tab_shopping_cart2, R.drawable.ic_goods_location};

    @Override
    protected void initView(View view) {
        // 设置状态栏
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        mHeadPhoto = view.findViewById(R.id.iv_head_img);
        mHeadPhoto.setOnClickListener(this);
        mTelNum = view.findViewById(R.id.tv_tel);
        ImageView edit = view.findViewById(R.id.iv_edit);
        edit.setOnClickListener(this);
        topItem = view.findViewById(R.id.mgv_top_item);
        contentList = view.findViewById(R.id.mlv_content_list);
        Button logout = view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(this);

        // request
        showLoading("loading");
        RequestPost.getPerson(this);
        // setting List
        Message contentMsg = new Message();
        contentMsg.what = CONTENT_LIST_SIZE;
        mHandler.sendMessage(contentMsg);
    }

    @Override
    protected void initData(Context mContext) {
        // 读取缓存大小
        try {
            totalCacheSize = CleanCacheUtils.getTotalCacheSize(Objects.requireNonNull(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mList = new ArrayList<>();
        for (int i = 0; i < moduleName.length; i++) {
            CommonListBean commonList = new CommonListBean();
            commonList.setImgPath(moduleIcon[i]);
            commonList.setName(moduleName[i]);
            commonList.setArrowType(1);
            if (i == 3) {           // 官方电话
                commonList.setNote("10086");
            }
            if (i == 4) {        // 缓存大小
                commonList.setNote(totalCacheSize);
            }
            mList.add(commonList);
        }
        topList = new ArrayList<>();
        for (int i = 0; i < topNames.length; i++) {
            CommonListBean topDesc = new CommonListBean();
            topDesc.setName(topNames[i]);
            topDesc.setImgPath(topImages[i]);
            topList.add(topDesc);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head_img:
                toast("更换头像");
                break;
            case R.id.iv_edit:              // 修改手机号
                // toast("编辑");
                startActivity(ChangeTelNumActivity.class);
                break;
            case R.id.btn_logout:
                new MessageDialog.Builder(getActivity())
                        .setMessage("确认退出登陆？")
                        .setConfirm(getString(R.string.common_exit))
                        .setCancel(getString(R.string.common_cancel))
                        .setAutoDismiss(false)
                        .setCanceledOnTouchOutside(false)
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                // request
                                showLoading("");
                                RequestPost.loginOut(PersonalFragment.this);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
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
                    // TopItem
                    PersonalTopAdapter topAdapter = new PersonalTopAdapter(getContext(), topList);
                    topItem.setAdapter(topAdapter);
                    topItem.setNumColumns(3);
                    topAdapter.setOnClickItem(position -> {
                        // toast("点击了: " + topList.get(position).getName());
                        switch (topList.get(position).getName()) {
                            case "消息":
                                startActivity(MessageActivity.class);
                                break;
                            case "我的商家":
                                startActivity(MerchantsActivity.class);
                                break;
                            case "收货地址":
                                startActivity(ShipAddressActivity.class);
                                break;
                        }
                    });
                    break;
                case CONTENT_LIST_SIZE:
                    PersonalListAdapter listAdapter = new PersonalListAdapter(getContext(), mList);
                    contentList.setAdapter(listAdapter);
                    listAdapter.setOnClickItem(position -> {
                        // toast("模块：" + mList.get(position).getName());
                        switch (mList.get(position).getName()) {
                            case "语言设置":
                                startActivity(SetLanguageActivity.class);
                                break;
                            case "修改密码":
                                startActivity(ChangePwdActivity.class);
                                break;
                            case "意见反馈":
                                startActivity(FeedBackActivity.class);
                                break;
                            case "联系我们":
                                setMessageDialog("是否拨打电话？", mList.get(position).getNote(), position, 0);
                                break;
                            case "清理缓存":
                                setMessageDialog("是否清理缓存？", mList.get(position).getNote(), position, 1);
                                break;
                            case "版本更新":                // 版本热更新
                                String updateUrl = "aaa";
                                // 升级更新对话框
                                new UpdateDialog.Builder(getActivity())
                                        // 版本名
                                        .setVersionName("板栗门店 v 2.0")
                                        // 文件大小
                                        .setFileSize("10 M")
                                        // 是否强制更新
                                        .setForceUpdate(false)
                                        // 更新日志
                                        .setUpdateLog("修复了XXXX\n添加了XX功能\n删减了XX功能\n优化了XX功能")
                                        // 下载地址
                                        .setDownloadUrl(updateUrl)
                                        .show();
                                break;
                        }
                    });
                    break;
            }
        }

        /**
         * 消息提示
         * @param title 标题(可以不写)
         * @param msg   提示消息
         * @param position pos
         * @param flag  flag
         */
        private void setMessageDialog(String title, String msg, int position, int flag) {
            new MessageDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(msg)
                    .setConfirm(getString(R.string.common_confirm))
                    .setCancel(getString(R.string.common_cancel))
                    .setAutoDismiss(false)
                    .setCanceledOnTouchOutside(false)
                    .setListener(new MessageDialog.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog) {
                            dialog.dismiss();
                            switch (flag) {
                                case 0:
                                    PhoneCallUtils.callPhoneDump(Objects.requireNonNull(getActivity()), msg);
                                    break;
                                case 1:
                                    CleanCacheUtils.clearAllCache(Objects.requireNonNull(getContext()));
                                    totalCacheSize = CleanCacheUtils.getTotalCacheSize(Objects.requireNonNull(getContext()));
                                    mList.get(position).setNote(totalCacheSize);
                                    postDelayed(() -> {
                                        Message contentMsg1 = new Message();
                                        contentMsg1.what = CONTENT_LIST_SIZE;
                                        mHandler.sendMessage(contentMsg1);
                                    }, 500);
                                    break;
                            }
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取个人中心
                if (result.url().contains("wxapi/v1/clientPerson.php?type=getPerson")) {
                    Log.d(TAG, "==== body =====" + body.get("data"));
                    CustomBean custom = JsonParser.parseJSONObject(CustomBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    if (custom != null) {
                        topList.get(0).setNote(custom.getMessageNum());
                        mTelNum.setText(custom.getTel());
                        Glide.with(this)
                                .load(custom.getIco())
                                .apply(new RequestOptions().circleCrop())
                                .error(R.mipmap.icon_logo_2x)
                                .placeholder(R.mipmap.icon_logo_2x)
                                .into(mHeadPhoto);
                    }
                    // top Message
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 退出登录  postDelayed(() -> {
                if (result.url().contains("wxapi/v1/clientPerson.php?type=loginOut")) {
                    postDelayed(() -> startActivity(LoginActivity.class), 500);
                }
            }
        } else {
            toast(body.get("warn"));
        }
        dismissLoading();
    }
}

package com.rainwood.chestnut.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseFragment;
import com.rainwood.chestnut.common.App;
import com.rainwood.chestnut.domain.CommonListBean;
import com.rainwood.chestnut.domain.CustomBean;
import com.rainwood.chestnut.domain.VersionBean;
import com.rainwood.chestnut.io.IOUtils;
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
import com.rainwood.chestnut.ui.dialog.MenuDialog;
import com.rainwood.chestnut.ui.dialog.MessageDialog;
import com.rainwood.chestnut.ui.dialog.UpdateDialog;
import com.rainwood.chestnut.utils.CameraUtil;
import com.rainwood.chestnut.utils.CleanCacheUtils;
import com.rainwood.chestnut.utils.DeviceIdUtils;
import com.rainwood.chestnut.utils.PhoneCallUtils;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.rainwood.chestnut.utils.CameraUtil.PHOTO_REQUEST_CAREMA;
import static com.rainwood.chestnut.utils.CameraUtil.RESULT_CAMERA_IMAGE;
import static com.rainwood.chestnut.utils.CameraUtil.uri_;

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
                // toast("更换头像");
                imageSelector();
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
                    PersonalTopAdapter topAdapter = new PersonalTopAdapter(getmContext(), topList);
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
                            case "版本更新":                // 版本更新
                                // 请求版本信息
                                showLoading("");
                                RequestPost.VerisonUpdate(String.valueOf(App.getVersionCode(getContext())), DeviceIdUtils.getDeviceId(getContext()),
                                        "", PersonalFragment.this);
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

    /**
     * 图片选择器
     */
    private String[] selectors = {"相机", "相册"};

    private void imageSelector() {
        List<String> data = new ArrayList<>(Arrays.asList(selectors));
        // 先权限检查
        XXPermissions.with(getActivity())
                .constantRequest()
                .permission(Permission.Group.STORAGE)       // 读写权限
                .permission(Permission.CAMERA)              // 相机权限
                // .permission(Permission.DOWNLOAD_ACCESS)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            new MenuDialog.Builder(getActivity())
                                    // 设置null 表示不显示取消按钮
                                    .setCancel(R.string.common_cancel)
                                    // 设置点击按钮后不关闭弹窗
                                    .setAutoDismiss(false)
                                    // 显示的数据
                                    .setList(data)
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new MenuDialog.OnListener<String>() {
                                        @Override
                                        public void onSelected(BaseDialog dialog, int position, String text) {
                                            dialog.dismiss();
                                            switch (position) {
                                                case 0:         // 拍照
                                                    //toast("相机");
                                                    CameraUtil.openCamera(PersonalFragment.this);
                                                    break;
                                                case 1:         // 相册
                                                    // toast("相册");
                                                    gallery();
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            toast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            toast("获取权限失败");
                        }
                    }
                });
    }

    /**
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, RESULT_CAMERA_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_CAMERA_IMAGE:           // 相册选择图片
                    if (data != null) {
                        // 得到图片的全路径
                        Uri uri = data.getData();
                        Glide.with(getContext())
                                .load(uri)
                                .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                                .into(mHeadPhoto);
                        File file = IOUtils.decodeUri(getActivity(), uri);

                        RequestPost.changeHeadImg(file, this);
                    }
                    break;
                case PHOTO_REQUEST_CAREMA:          // 摄像头照片
                    if (data != null) {          // 相机可能尚未指定intent.puExtra(MediaStore.EXTRA_OUTPUT, uri);
                        if (data.hasExtra("data")) {     // 返回有缩略图
                            // 得到bitmap后处理、如压缩...
                            Bitmap bitmap = data.getParcelableExtra("data");
                            Glide.with(getContext()).load(bitmap)
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                                    .into(mHeadPhoto);
                        } else {     // 如果返回的不是缩略图，则直接获取地址
                            Bitmap bitmap = IOUtils.decodeUri(getContext(), uri_);
                            Glide.with(getContext())
                                    .load(bitmap)
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                                    .into(mHeadPhoto);
                            File file = IOUtils.decodeUri(getActivity(), uri_);
                            RequestPost.changeHeadImg(file, this);
                        }
                        Log.d(TAG, "===== url___ ===== " + uri_);
                    }
                    break;
            }
        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, "====  result " + result);
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
                        Glide.with(getmContext())
                                .load(custom.getIco())
                                .apply(new RequestOptions().circleCrop())
                                .error(R.mipmap.icon_logo_2x)
                                .placeholder(R.mipmap.icon_logo_2x)
                                .into(mHeadPhoto);
                        for (CommonListBean bean : mList) {
                            if (bean.getName().equals("联系我们")) {
                                bean.setNote(custom.getTel());
                                break;
                            }
                        }
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
                // 版本更新
                if (result.url().contains("wxapi/v1/clientLogin.php?type=appVersionEdit")) {
                    VersionBean versionBean = JsonParser.parseJSONObject(VersionBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    if (versionBean.getVersion().equals(String.valueOf(App.getVersionCode(getContext())))) {        // 如果版本一致，则不更新
                        toast("当前是最新版本");
                    } else {         // 弹窗提示更新
                        String updateUrl = "aaaa";
                        // 升级更新对话框
                        new UpdateDialog.Builder(getActivity())
                                // 版本名
                                .setVersionName("板栗门店 v 2.0")
                                // 文件大小
                                .setFileSize("10 M")
                                // 是否强制更新
                                .setForceUpdate(false)
                                // 更新日志
                                .setUpdateLog("修复了bug\n优化用户体验")
                                // 下载地址
                                .setDownloadUrl(updateUrl)
                                .show();
                    }
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}

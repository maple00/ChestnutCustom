package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.GoodsDetailBean;
import com.rainwood.chestnut.domain.ImageBean;
import com.rainwood.chestnut.domain.ParamBean;
import com.rainwood.chestnut.domain.ShopBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.other.BaseDialog;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.ShopDetailAdapter;
import com.rainwood.chestnut.ui.dialog.MessageDialog;
import com.rainwood.chestnut.utils.ListUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/4 10:29
 * @Desc: 商品详情
 */
public final class ShopDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.iv_cart)
    private ImageView pageCart;
    @ViewById(R.id.xb_top_img)          // XBanner
    private XBanner mXBanner;
    @ViewById(R.id.tv_name)
    private TextView name;
    @ViewById(R.id.tv_selected)
    private TextView selected;
    @ViewById(R.id.tv_num)      // 商家编号
    private TextView num;
    @ViewById(R.id.tv_min_reserve)
    private TextView minReserve;
    @ViewById(R.id.tv_discount_price)
    private TextView discountPrice;
    @ViewById(R.id.tv_original_cost)
    private TextView price;
    @ViewById(R.id.mlv_params_list)
    private MeasureListView paramsList;

    private ShopBean mShop;
    private List<ParamBean> mList;
    private List<ImageBean> mImageList;

    // mHandler
    private final int INITIAL_SIZE = 0x101;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.black));
        pageBack.setOnClickListener(this);
        pageCart.setOnClickListener(this);
        // 设置中划线并加清晰
        price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 显示轮播
     */
    private void setXBanner() {
        // 初始化XBanner中展示的数据
        final ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < mImageList.size(); i++) {
            images.add(mImageList.get(i).getIco());
        }
        if (mXBanner != null) {
            mXBanner.removeAllViews();
        }
        // 为XBanner绑定数据
        mXBanner.setData(images, null);
        // XBanner适配数据
        mXBanner.setmAdapter((banner, view, position) -> Glide.with(getActivity()).load(images.get(position)).into((ImageView) view));
        // 设置XBanner的页面切换特效
        mXBanner.setPageTransformer(Transformer.Cube);
        // 设置XBanner页面切换的时间，即动画时长
        mXBanner.setPageChangeDuration(1000);
    }

    @Override
    protected void initData() {
        super.initData();
        mShop = new ShopBean();
        mShop.setMinReserve(String.valueOf(100));
        // request
        String goodsId = getIntent().getStringExtra("goodsId");
        showLoading("");
        RequestPost.getGoodsDetail(goodsId, this);
    }

    @Override
    public void onClick(View v) {
        int count = 0;
        for (ParamBean params : mList) {
            count += Integer.parseInt(params.getNum());
        }
        switch (v.getId()) {
            case R.id.iv_back:
                String[] split = minReserve.getText().toString().trim().split("最小起订量：");
                int i = Integer.parseInt(split[1]);
                if (count < i || count <= 0) {
                    setTips();
                } else {
                    finish();
                }
                break;
            case R.id.iv_cart:              // 弹窗显示
                String[] minBooking = minReserve.getText().toString().trim().split("最小起订量：");
                int minSum = Integer.parseInt(minBooking[1]);
                if (count < minSum || count <= 0) {
                    setTips();
                } else {             // 跳转到购物车
                    postDelayed(() -> {
                        Intent intent = new Intent(ShopDetailActivity.this, HomeActivity.class);
                        intent.putExtra("id", Contants.CART_SIZE);
                        startActivity(intent);
                    }, 500);
                }
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
                    // 设置已选数量
                    int selectedNum = 0;
                    for (ParamBean params : mList) {
                        selectedNum += Integer.parseInt(params.getNum());
                    }
                    // 已选择数量
                    selected.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.red30) + " size='"
                            + FontDisplayUtil.dip2px(ShopDetailActivity.this, 12f) + "'>已选"
                            + selectedNum + "件</font>"));
                    // params
                    ShopDetailAdapter paramsAdapter = new ShopDetailAdapter(ShopDetailActivity.this, mList);
                    paramsList.setAdapter(paramsAdapter);
                    paramsAdapter.setOnClickEdit(new ShopDetailAdapter.OnClickEdit() {
                        @Override
                        public void onClickEditReduce(int position) {
                            // 文本框的数量
                            setTextNumber(position, 0);
                            // TODO:
                            RequestPost.addInCart("add", mList.get(position).getId(), mList.get(position).getNum(), ShopDetailActivity.this);
                        }

                        @Override
                        public void onClickEditPlus(int position) {
                            // 文本框的数量
                            setTextNumber(position, 1);
                            // TODO:
                            RequestPost.addInCart("dec", mList.get(position).getId(), mList.get(position).getNum(), ShopDetailActivity.this);
                        }

                        @Override
                        public void onWatcherEdit(ShopDetailAdapter adapter, int position) {
                            selected.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.red30) + " size='"
                                    + FontDisplayUtil.dip2px(ShopDetailActivity.this, 12f) + "'>已选"
                                    + adapter.sumCount() + "件</font>"));
                            // TODO:
                            RequestPost.addInCart("edit", mList.get(position).getId(),
                                    mList.get(position).getNum() == null ? "0" : mList.get(position).getNum(), ShopDetailActivity.this);
                        }
                    });
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    };

    /**
     * 设置文本框的数值
     *
     * @param position
     */
    private void setTextNumber(int position, int flag) {
        int number;
        if ("".equals(mList.get(position).getNum()) && TextUtils.isEmpty(mList.get(position).getNum())) {
            number = 0;
        } else {
            number = Integer.parseInt(mList.get(position).getNum());
        }
        if (flag == 1) {
            number += 1;
        }
        if (flag == 0) {
            number -= 1;
        }
        if (number <= 0) {
            number = 0;
            toast("已经是可填写的最小数量");
        }
        mList.get(position).setNum(String.valueOf(number));
        Message msg = new Message();
        msg.what = INITIAL_SIZE;
        mHandler.sendMessage(msg);
    }

    /**
     * 显示弹窗
     */
    private void setTips() {
        String[] minBooking = minReserve.getText().toString().trim().split("最小起订量：");
        int minSum = Integer.parseInt(minBooking[1]);
        new MessageDialog.Builder(this)
                .setTitle("您添加的商品未达到起订量")
                .setMessage(Html.fromHtml("<font color=" + getResources().getColor(R.color.textColor)
                        + " size='" + FontDisplayUtil.dip2px(ShopDetailActivity.this, 13f)
                        + "'>最小起订量：" + "</font>" +
                        "<font color=" + getResources().getColor(R.color.red30)
                        + " size='" + FontDisplayUtil.dip2px(ShopDetailActivity.this, 13f) + "'>"
                        + minSum + "</font>"))
                .setConfirm(getString(R.string.dialog_keep_add))
                .setCancel(getString(R.string.dialog_give_up))
                .setAutoDismiss(false)
                .setCanceledOnTouchOutside(false)
                .setAnimStyle(BaseDialog.AnimStyle.IOS)
                .setListener(new MessageDialog.OnListener() {
                    @Override
                    public void onConfirm(BaseDialog dialog) {      // 继续添加
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {       // 我不要了
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " ---- result ----- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 商品详情页
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getGoodsInfo")) {
                    // 规格
                    mList = JsonParser.parseJSONArray(ParamBean.class, JsonParser.parseJSONObject(body.get("data")).get("skulist"));
                    // 轮播图
                    List<ImageBean> imageBeans = JsonParser.parseJSONArray(ImageBean.class, JsonParser.parseJSONObject(body.get("data")).get("icoAll"));
                    GoodsDetailBean goodsDetail = JsonParser.parseJSONObject(GoodsDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    mImageList = new ArrayList<>();
                    ImageBean image = new ImageBean();
                    if (goodsDetail != null) {
                        image.setIco(goodsDetail.getIco());
                    }
                    mImageList.add(image);
                    if (imageBeans != null) {
                        for (int i = 0; i < ListUtils.getSize(imageBeans); i++) {
                            ImageBean imageAll = new ImageBean();
                            imageAll.setIco(imageBeans.get(i).getIco());
                            mImageList.add(imageAll);
                        }
                    }
                    // 显示轮播
                    setXBanner();
                    name.setText(goodsDetail.getName());
                    num.setText(goodsDetail.getModel());
                    minReserve.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.fontColor) + " size='"
                            + FontDisplayUtil.dip2px(ShopDetailActivity.this, 12f) + "'>最小起订量：</font>"
                            + "<font color=" + getResources().getColor(R.color.textColor) + " size='"
                            + FontDisplayUtil.dip2px(ShopDetailActivity.this, 12f) + "'><b>"
                            + goodsDetail.getStartNum() + "</b></font>"));
                    if (goodsDetail.getMinPrice().equals(goodsDetail.getMaxPrice()) && "0".equals(goodsDetail.getMinPrice())) {
                        price.setVisibility(View.GONE);
                        if (goodsDetail.getOldMinPrice().equals(goodsDetail.getOldMaxPrice())) {
                            discountPrice.setText(goodsDetail.getOldMinPrice());
                        } else {
                            discountPrice.setText(goodsDetail.getOldMinPrice() + " - " + goodsDetail.getMaxPrice());
                        }
                    } else {
                        price.setVisibility(View.VISIBLE);
                        if (goodsDetail.getMinPrice().equals(goodsDetail.getMaxPrice())) {
                            discountPrice.setText(goodsDetail.getMinPrice());
                        } else {
                            discountPrice.setText(goodsDetail.getMinPrice() + " - " + goodsDetail.getMaxPrice());
                        }
                        if (goodsDetail.getOldMinPrice().equals(goodsDetail.getOldMaxPrice())) {
                            price.setText(goodsDetail.getOldMinPrice());
                        } else {
                            price.setText(goodsDetail.getOldMinPrice() + " - " + goodsDetail.getMaxPrice());
                        }
                    }
                    // 规格
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 添加商品到购物车
                if (result.url().contains("wxapi/v1/clientGoods.php?type=addInCart")){
                    //toast(body.get("warn"));
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

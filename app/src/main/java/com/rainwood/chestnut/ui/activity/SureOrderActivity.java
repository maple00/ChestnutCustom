package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.AddressBean;
import com.rainwood.chestnut.domain.GoodDetailBean;
import com.rainwood.chestnut.domain.SpecialBean;
import com.rainwood.chestnut.domain.TitleAndHintBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.OrderSummaryAdapter;
import com.rainwood.chestnut.ui.adapter.SureOrderSpecialAdapter;
import com.rainwood.chestnut.utils.PhoneCallUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rainwood.chestnut.common.Contants.ADDRESS_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/3/3 10:38
 * @Desc: 确认订单
 */
public final class SureOrderActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mSendMethodId = "1";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sure_order;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.iv_express)      // 快递   --- 默认设置为快递
    private ImageView ivExpress;
    @ViewById(R.id.tv_express)
    private TextView tvExpress;
    @ViewById(R.id.iv_invite)       // 门店自取
    private ImageView ivInvite;
    @ViewById(R.id.tv_invite)
    private TextView tvInvite;
    @ViewById(R.id.tv_store_name)
    private TextView storeName;     // 门店地址
    @ViewById(R.id.ll_recipient)
    private LinearLayout llRecipient;   // 收货人
    @ViewById(R.id.tv_recipient)
    private TextView tvRecipient;
    @ViewById(R.id.tv_tel)
    private TextView tel;
    @ViewById(R.id.tv_address)
    private TextView address;
    // 商品
    @ViewById(R.id.iv_img)
    private ImageView goodsImage;
    @ViewById(R.id.tv_name)
    private TextView goodsName;
    @ViewById(R.id.tv_number)
    private TextView model;

    @ViewById(R.id.mlv_goods_list)
    private MeasureListView goodsList;
    @ViewById(R.id.mlv_summary_list)
    private MeasureListView mSummaryList;
    // 联系客服
    @ViewById(R.id.ll_service_phone)
    private LinearLayout servicePhone;
    // Bottom
    @ViewById(R.id.tv_order_pay)
    private TextView orderPay;
    @ViewById(R.id.btn_commit_order)
    private Button commitOrder;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private String[] titles = {"商品总价", "折扣", "实付款"};
    private GoodDetailBean mGoodDetailBean;
    private List<SpecialBean> mSpecialList;
    private AddressBean mAddressBean;
    private List<TitleAndHintBean> mTitleAndHintList;

    @Override
    protected void initView() {
        // 初始化点击事件
        initClickView();
        String goodsId = getIntent().getStringExtra("goodsId");
        String storeId = getIntent().getStringExtra("storeId");
        // request
        showLoading("");
        RequestPost.getConfirmOrderPage(goodsId, storeId, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_express:
            case R.id.tv_express:
                //toast("快递");
                storeName.setVisibility(View.GONE);
                llRecipient.setVisibility(View.VISIBLE);
                ivInvite.setImageResource(R.drawable.shape_uncheck_shape);
                ivExpress.setImageResource(R.drawable.shape_checked_shape);
                mSendMethodId = "1";
                break;
            case R.id.tv_invite:
            case R.id.iv_invite:
                // toast("门店自取");
                storeName.setVisibility(View.VISIBLE);
                llRecipient.setVisibility(View.GONE);
                ivInvite.setImageResource(R.drawable.shape_checked_shape);
                ivExpress.setImageResource(R.drawable.shape_uncheck_shape);
                mSendMethodId = "2";
                break;
            case R.id.tv_address:
                // toast("选择地址");
                Contants.ADDRESS_POS_SIZE = 0x101;
                Intent intent = new Intent(this, ShipAddressActivity.class);
                startActivityForResult(intent, ADDRESS_REQUEST);
                break;
            case R.id.ll_service_phone:         // 联系客服
                PhoneCallUtils.callPhoneDump(this, mGoodDetailBean.getTel());
                break;
            case R.id.btn_commit_order:         // 提交订单
                String addId;
                if (addressId != null) {         // 选择默认的地址
                    addId = addressId;
                } else {
                    addId = mAddressBean.getId();
                }
                // request
                showLoading("");
                RequestPost.commitOrder(mGoodDetailBean.getGoodsId(), mGoodDetailBean.getStoreId(), addId, mSendMethodId, this);
                break;
        }
    }

    private String addressId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 确认订单收货地址
        if (requestCode == ADDRESS_REQUEST && resultCode == ADDRESS_REQUEST) {
            addressId = data.getStringExtra("addressId");
            String addressName = data.getStringExtra("address");
            String contact = data.getStringExtra("contact");
            String contactTel = data.getStringExtra("tel");
            //Log.d(TAG, "=========== " + addressId + " ========== " + addressName);
            post(() -> {
                address.setText(addressName);
                tvRecipient.setText(contact);
                tel.setText(contactTel);
            });
        }
    }

    private void initClickView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("确认订单");
        ivExpress.setOnClickListener(this);
        tvExpress.setOnClickListener(this);
        ivInvite.setOnClickListener(this);
        tvInvite.setOnClickListener(this);
        address.setOnClickListener(this);
        servicePhone.setOnClickListener(this);
        commitOrder.setOnClickListener(this);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    tvRecipient.setText(mAddressBean == null ? "" : mAddressBean.getContactName());
                    tel.setText(mAddressBean == null ? "" : mAddressBean.getContactTel());
                    if (mAddressBean == null){
                        address.setHint("请选择收货地址");
                    }else {
                        address.setText(mAddressBean.getAddressMx());
                    }
                    orderPay.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.textColor) + " size='"
                            + FontDisplayUtil.dip2px(SureOrderActivity.this, 12f) + "'>需支付：</font>"
                            + "<font color=" + getResources().getColor(R.color.red30) + " size='"
                            + FontDisplayUtil.dip2px(SureOrderActivity.this, 18f) + "'><b>"
                            + mGoodDetailBean.getRealPayMoney() + "</b></font>"));
                    // 商品
                    Contants.RECORD_POS = 0x102;
                    Glide.with(SureOrderActivity.this).load(mGoodDetailBean.getIco()).into(goodsImage);
                    goodsName.setText(mGoodDetailBean.getGoodsName());
                    model.setText(mGoodDetailBean.getModel());
                    SureOrderSpecialAdapter sureOrderSpecialAdapter = new SureOrderSpecialAdapter(SureOrderActivity.this, mSpecialList);
                    goodsList.setAdapter(sureOrderSpecialAdapter);
                    // 汇总
                    OrderSummaryAdapter summaryList = new OrderSummaryAdapter(SureOrderActivity.this, mTitleAndHintList);
                    mSummaryList.setAdapter(summaryList);
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
                // 获取订单页面的内容
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getConfirmOrderPage")) {
                    mGoodDetailBean = JsonParser.parseJSONObject(GoodDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsInfo"));
                    mSpecialList = JsonParser.parseJSONArray(SpecialBean.class, JsonParser.parseJSONObject(body.get("data")).get("skulist"));
                    mAddressBean = JsonParser.parseJSONObject(AddressBean.class, JsonParser.parseJSONObject(body.get("data")).get("address"));
                    mTitleAndHintList = new ArrayList<>();
                    for (int i = 0; i < titles.length; i++) {
                        TitleAndHintBean hint = new TitleAndHintBean();
                        hint.setTitle(titles[i]);
                        switch (i) {
                            case 0:             // 商品总价
                                hint.setLabel(mGoodDetailBean.getTotalMoney());
                                break;
                            case 1:             // 折扣
                                hint.setLabel(mGoodDetailBean.getDiscountMoney());
                                break;
                            case 2:             // 实际付款
                                hint.setLabel(mGoodDetailBean.getRealPayMoney());
                                break;
                        }
                        mTitleAndHintList.add(hint);
                    }

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 提交订单
                if (result.url().contains("wxapi/v1/clientGoods.php?type=commitOrder")) {
                    toast(body.get("warn"));
                    postDelayed(() -> openActivity(SuccessActivity.class), 1000);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

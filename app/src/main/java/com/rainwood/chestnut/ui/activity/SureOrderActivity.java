package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.CartBean;
import com.rainwood.chestnut.domain.SpecialBean;
import com.rainwood.chestnut.domain.SureOrderBean;
import com.rainwood.chestnut.domain.TitleAndHintBean;
import com.rainwood.chestnut.ui.adapter.CartAdapter;
import com.rainwood.chestnut.ui.adapter.OrderSummaryAdapter;
import com.rainwood.chestnut.utils.PhoneCallUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/3 10:38
 * @Desc: 确认订单
 */
public final class SureOrderActivity extends BaseActivity implements View.OnClickListener {

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

    private SureOrderBean mSureOrder;

    @Override
    protected void initView() {
        // 初始化点击事件
        initClickView();

        Message msg = new Message();
        msg.what = INITIAL_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
        mSureOrder = new SureOrderBean();
        mSureOrder.setExpressName("李亚奇");
        mSureOrder.setExpressTel("13512270415");
        mSureOrder.setAddres("重庆市南岸区弹子石国际商务大厦A座22-2");
        mSureOrder.setServiceTel("10086");      // 客服电话
        List<TitleAndHintBean> summaryList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TitleAndHintBean summary = new TitleAndHintBean();
            summary.setTitle(titles[i]);
            summary.setLabel("50€");
            summaryList.add(summary);
        }
        mSureOrder.setSummaryList(summaryList);
        List<CartBean> goodsList = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            CartBean cart = new CartBean();
            cart.setIco(null);
            cart.setModel("GD-002650");
            List<SpecialBean> specialList = new ArrayList<>();
            if (i == 0) {
                cart.setGoodsName("预售影儿诗篇女装新款双排扣网纱波点袖修身连衣裙预售影儿诗篇女装新款双排扣网纱波点袖修身连衣裙...");
                for (int j = 0; j < 5; j++) {
                    SpecialBean special = new SpecialBean();
                    special.setSkuName("酒红色/M");
                    special.setPrice("90.5€");
                    special.setNum("200");
                    special.setTotalMoney("18100€");
                    specialList.add(special);
                }
            } else {
                cart.setGoodsName("欧根纱印花翻领女上衣");
                for (int j = 0; j < 3; j++) {
                    SpecialBean special = new SpecialBean();
                    special.setSkuName("酒红色/M");
                    special.setPrice("90.5€");
                    special.setNum("200");
                    special.setTotalMoney("18100€");
                    specialList.add(special);
                }
            }
            cart.setSkulist(specialList);
            goodsList.add(cart);
        }
        mSureOrder.setGoodsList(goodsList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_express:
            case R.id.tv_express:
                toast("快递");
                storeName.setVisibility(View.GONE);
                llRecipient.setVisibility(View.VISIBLE);
                ivInvite.setImageResource(R.drawable.shape_uncheck_shape);
                ivExpress.setImageResource(R.drawable.shape_checked_shape);

                break;
            case R.id.tv_invite:
            case R.id.iv_invite:
                toast("门店自取");
                storeName.setVisibility(View.VISIBLE);
                llRecipient.setVisibility(View.GONE);
                ivInvite.setImageResource(R.drawable.shape_checked_shape);
                ivExpress.setImageResource(R.drawable.shape_uncheck_shape);

                break;
            case R.id.tv_address:
                toast("选择地址");
                break;
            case R.id.ll_service_phone:         // 联系客服
                PhoneCallUtils.callPhoneDump(this, mSureOrder.getServiceTel());
                break;
            case R.id.btn_commit_order:         // 提交订单
                openActivity(SuccessActivity.class);
                break;
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
                    tvRecipient.setText(mSureOrder.getExpressName());
                    tel.setText(mSureOrder.getExpressTel());
                    address.setText(mSureOrder.getAddres());
                    orderPay.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.textColor) + " size='"
                            + FontDisplayUtil.dip2px(SureOrderActivity.this, 12f) + "'>需支付：</font>"
                            + "<font color=" + getResources().getColor(R.color.red30) + " size='"
                            + FontDisplayUtil.dip2px(SureOrderActivity.this, 18f) + "'><b>"
                            + "151010.5€" + "</b></font>"));
                    // 商品
                    Contants.RECORD_POS = 0x102;
                    CartAdapter goodsAdapter = new CartAdapter(SureOrderActivity.this, mSureOrder.getGoodsList());
                    goodsList.setAdapter(goodsAdapter);
                    // 汇总
                    OrderSummaryAdapter summaryList = new OrderSummaryAdapter(SureOrderActivity.this, mSureOrder.getSummaryList());
                    mSummaryList.setAdapter(summaryList);
                    break;
            }
        }
    };

    private String[] titles = {"商品总价", "运费", "折扣", "实付款"};
}

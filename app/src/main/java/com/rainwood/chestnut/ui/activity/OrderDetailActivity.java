package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.OrderBean;
import com.rainwood.chestnut.domain.OrderDetailBean;
import com.rainwood.chestnut.domain.OrderSpecialBean;
import com.rainwood.chestnut.domain.SpecialBean;
import com.rainwood.chestnut.domain.TitleAndHintBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.ItemCartAdapter;
import com.rainwood.chestnut.ui.adapter.OrderSummaryAdapter;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/4 15:45
 * @Desc: 订单详情
 */
public final class OrderDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_status)
    private TextView status;
    @ViewById(R.id.tv_address)
    private TextView address;
    @ViewById(R.id.tv_company)
    private TextView company;
    @ViewById(R.id.tv_name_tel)         // 收货人姓名和电话
    private TextView nameAndTel;
    @ViewById(R.id.iv_img)
    private ImageView img;
    @ViewById(R.id.tv_name)
    private TextView name;
    @ViewById(R.id.tv_number)
    private TextView number;
    @ViewById(R.id.tv_discount)
    private TextView discount;
    @ViewById(R.id.tv_rate)
    private TextView rate;
    @ViewById(R.id.lv_params_list)
    private MeasureListView paramsList;
    @ViewById(R.id.lv_summary)
    private MeasureListView summaryLists;
    @ViewById(R.id.lv_order_msg)
    private MeasureListView orderMsgList;
    @ViewById(R.id.ll_service_phone)
    private LinearLayout serviceTel;
    @ViewById(R.id.ll_confirm_order)
    private LinearLayout confirm;
    @ViewById(R.id.btn_confirm)
    private Button btnConfirm;

    private OrderBean mOrder;
    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private String[] titles = {"商品总价", "运费", "折扣", "实付款"};
    private String[] orderTitles = {"订单编号", "创建时间", "发货时间"};
    private String[] orderLabel = {"1545210005300", "2020.02.07 15:17:00", "2020.02.07"};

    @Override
    protected void initView() {
        initContent();

        Message msg = new Message();
        msg.what = INITIAL_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String orderId = getIntent().getStringExtra("orderId");
        // request
        showLoading("");
        RequestPost.getOrderDetail(orderId, this);
    }

    /**
     * 初始化
     */
    private void initContent() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setStatusBarDarkTheme(this, true);
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.ic_page_back_white);
        pageTitle.setText("订单详情");
        pageTitle.setTextColor(getResources().getColor(R.color.white));
        serviceTel.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_service_phone:
                toast("拨打电话");
                break;
            case R.id.btn_confirm:
                // toast("确定了");
                mOrder.setStatus("已完成");
                Message msg = new Message();
                msg.what = INITIAL_SIZE;
                mHandler.sendMessage(msg);
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mOrder = new OrderBean();
        mOrder.setStatus("待收货");
        mOrder.setAddress("重庆市南岸区弹子石国际商务大厦A座22-2");
        mOrder.setCompany("衣香丽影服饰有限公司");
        mOrder.setConsignee("李亚琪");
        mOrder.setTel("13512270415");
        mOrder.setName("预售影儿诗篇女装新款双排扣网纱波点袖修身西装西装连衣裙装西");
        mOrder.setShopId("GD-002650");
        mOrder.setDiscount("25%折扣");
        mOrder.setRate("16%税率");
        List<SpecialBean> specialList = new ArrayList<>();
        for (int j = 0; j < 5; j++) {
            SpecialBean special = new SpecialBean();
            special.setSkuName("酒红色/M");
            special.setPrice("90");
            special.setNum("200");
            special.setTotalMoney("18100€");
            specialList.add(special);
        }
        mOrder.setSpecialList(specialList);
        List<TitleAndHintBean> summaryList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TitleAndHintBean summary = new TitleAndHintBean();
            summary.setTitle(titles[i]);
            summary.setLabel("50€");
            summaryList.add(summary);
        }
        mOrder.setSummaryList(summaryList);
        List<TitleAndHintBean> orderInfoList = new ArrayList<>();
        for (int i = 0; i < orderTitles.length; i++) {
            TitleAndHintBean titleAndHint = new TitleAndHintBean();
            titleAndHint.setTitle(orderTitles[i]);
            titleAndHint.setLabel(orderLabel[i]);
            orderInfoList.add(titleAndHint);
        }
        mOrder.setOrderInfoList(orderInfoList);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    status.setText(mOrder.getStatus());
                    if (mOrder.getStatus().equals("待收货")) {
                        post(() -> confirm.setVisibility(View.VISIBLE));
                    } else {
                        post(() -> confirm.setVisibility(View.GONE));
                    }
                    address.setText(mOrder.getAddress());
                    company.setText(mOrder.getCompany());
                    nameAndTel.setText(mOrder.getConsignee() + "\t" + mOrder.getTel());
                    Glide.with(OrderDetailActivity.this).load(R.drawable.icon_loadding_fail).into(img);
                    name.setText(mOrder.getName());
                    number.setText(mOrder.getShopId());
                    discount.setText(mOrder.getDiscount());
                    rate.setText(mOrder.getRate());
                    // 规格参数         -- 参数的Adapter写错了
                    Contants.RECORD_POS = 0x103;
                    ItemCartAdapter specialAdapter = new ItemCartAdapter(OrderDetailActivity.this, mOrder.getSpecialList());
                    paramsList.setAdapter(specialAdapter);
                    specialAdapter.setOnClickReducePlus(new ItemCartAdapter.OnClickReducePlus() {
                        @Override
                        public void onClickPlus(int parentPos, int position) {

                        }

                        @Override
                        public void onClickReduce(int parentPos, int position) {

                        }

                        @Override
                        public void onWatcherEdit(ItemCartAdapter adapter, int parentPos, int position) {

                        }
                    });
                    // 汇总
                    OrderSummaryAdapter summaryAdapter = new OrderSummaryAdapter(OrderDetailActivity.this, mOrder.getSummaryList());
                    summaryLists.setAdapter(summaryAdapter);
                    // 订单信息
                    OrderSummaryAdapter orderInfoAdapter = new OrderSummaryAdapter(OrderDetailActivity.this, mOrder.getOrderInfoList());
                    orderMsgList.setAdapter(orderInfoAdapter);
                    // NestedScrollView  的滑动监听
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " ---- result ----- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 查看订单详情
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getOrderInfo")) {
                    OrderDetailBean orderDetail = JsonParser.parseJSONObject(OrderDetailBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("info"));
                    List<OrderSpecialBean> sepcialList = JsonParser.parseJSONArray(OrderSpecialBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("skulist"));

                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

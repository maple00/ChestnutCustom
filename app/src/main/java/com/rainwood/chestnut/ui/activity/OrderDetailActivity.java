package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.rainwood.chestnut.domain.OrderDetailBean;
import com.rainwood.chestnut.domain.SpecialBean;
import com.rainwood.chestnut.domain.TitleAndHintBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.CompleteOrderAdapter;
import com.rainwood.chestnut.ui.adapter.OrderSummaryAdapter;
import com.rainwood.chestnut.ui.adapter.SureOrderSpecialAdapter;
import com.rainwood.chestnut.utils.PhoneCallUtils;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rainwood.chestnut.common.Contants.REFUNDS_APPLYING;
import static com.rainwood.chestnut.common.Contants.REFUNDS_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/3/4 15:45
 * @Desc: 订单详情
 */
public final class OrderDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private List<SpecialBean> mSepcialList;
    private OrderDetailBean mOrderDetail;

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

    @ViewById(R.id.ll_item_top)
    private LinearLayout itemTop;
    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private String[] titles = {"商品总价", "折扣", "实付款", "运费"};
    private String[] orderTitles = {"订单编号", "创建时间"};
    private List<TitleAndHintBean> mountList;
    private List<TitleAndHintBean> mOrderAttachList;

    @Override
    protected void initView() {
        initContent();
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
     * initial
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
                // toast("拨打电话");
                PhoneCallUtils.callPhoneDump(this, mOrderDetail.getTel());
                break;
            case R.id.btn_confirm:
                // toast("确定了");
                Message msg = new Message();
                msg.what = INITIAL_SIZE;
                mHandler.sendMessage(msg);
                break;
        }
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    status.setText(mOrderDetail.getWorkFlow());
                    // post(() -> confirm.setVisibility(View.GONE));
                    if (mOrderDetail.getWorkFlow().equals("已完成")) {     // 已完成状态可以选择退货
                        itemTop.setVisibility(View.GONE);
                        CompleteOrderAdapter orderAdapter = new CompleteOrderAdapter(OrderDetailActivity.this, mSepcialList);
                        paramsList.setAdapter(orderAdapter);
                        orderAdapter.setOnClickRefunds(position -> {
                            if ("".equals(mSepcialList.get(position).getRefundState())) {         // 去退货
                                Contants.GOODS_REFUNDS_SIZE = 0x101;
                                Intent intent = new Intent(OrderDetailActivity.this, RefundsActivity.class);
                                intent.putExtra("orderId", mSepcialList.get(position).getId());
                                startActivityForResult(intent, REFUNDS_REQUEST);
                            }
                            if ("applying".equals(mSepcialList.get(position).getRefundState())) { // 申请中
                                Contants.GOODS_REFUNDS_SIZE = 0x102;
                                Intent intent = new Intent(OrderDetailActivity.this, RefundsActivity.class);
                                intent.putExtra("orderId", mSepcialList.get(position).getId());
                                startActivityForResult(intent, REFUNDS_APPLYING);
                            }
                        });
                    } else {
                        itemTop.setVisibility(View.VISIBLE);
                        // 规格参数         -- 参数的Adapter
                        Contants.RECORD_POS = 0x103;
                        SureOrderSpecialAdapter specialAdapter = new SureOrderSpecialAdapter(OrderDetailActivity.this, mSepcialList);
                        paramsList.setAdapter(specialAdapter);
                    }
                    address.setText(mOrderDetail.getAddressMx());
                    company.setText(mOrderDetail.getCompanyName());
                    nameAndTel.setText(mOrderDetail.getContactName() + "\t" + mOrderDetail.getContactTel());
                    Glide.with(OrderDetailActivity.this).load(mOrderDetail.getIco()).into(img);
                    name.setText(mOrderDetail.getGoodsName());
                    number.setText(mOrderDetail.getModel());
                    discount.setText(mOrderDetail.getDiscountMoney() + "%");
                    rate.setText(mOrderDetail.getTaxRate() + "%");

                    // 汇总
                    OrderSummaryAdapter summaryAdapter = new OrderSummaryAdapter(OrderDetailActivity.this, mountList);
                    summaryLists.setAdapter(summaryAdapter);
                    // 订单信息
                    OrderSummaryAdapter orderInfoAdapter = new OrderSummaryAdapter(OrderDetailActivity.this, mOrderAttachList);
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
                    mOrderDetail = JsonParser.parseJSONObject(OrderDetailBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("info"));
                    mSepcialList = JsonParser.parseJSONArray(SpecialBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("skulist"));

                    mountList = new ArrayList<>();
                    for (int i = 0; i < titles.length; i++) {
                        TitleAndHintBean hint = new TitleAndHintBean();
                        hint.setTitle(titles[i]);
                        switch (i) {
                            case 0:         // 商品总价
                                hint.setLabel(mOrderDetail.getTotalMoney());
                                break;
                            case 1:         // 折扣价
                                hint.setLabel(mOrderDetail.getDiscountMoney());
                                break;
                            case 2:         // 实际付款
                                hint.setLabel(mOrderDetail.getRealPayMoney());
                                break;
                            case 3:         // 运费
                                hint.setLabel(mOrderDetail.getFreightMoney());
                                break;
                        }
                        mountList.add(hint);
                    }
                    mOrderAttachList = new ArrayList<>();
                    for (int i = 0; i < orderTitles.length; i++) {
                        TitleAndHintBean hint = new TitleAndHintBean();
                        hint.setTitle(orderTitles[i]);
                        switch (i) {
                            case 0:         // 订单编号
                                hint.setLabel(mOrderDetail.getId());
                                break;
                            case 1:         // 创建时间
                                hint.setLabel(mOrderDetail.getTime());
                                break;
                        }
                        mOrderAttachList.add(hint);
                    }

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

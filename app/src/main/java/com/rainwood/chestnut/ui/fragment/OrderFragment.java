package com.rainwood.chestnut.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseFragment;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.OrdersBean;
import com.rainwood.chestnut.domain.PressBean;
import com.rainwood.chestnut.domain.StoreListBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.activity.OrderDetailActivity;
import com.rainwood.chestnut.ui.activity.SearchViewActivity;
import com.rainwood.chestnut.ui.adapter.CartStoreAdapter;
import com.rainwood.chestnut.ui.adapter.HorSlidingAdapter;
import com.rainwood.chestnut.ui.adapter.OrderListAdapter;
import com.rainwood.chestnut.utils.ListUtils;
import com.rainwood.chestnut.utils.PhoneCallUtils;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.rainwood.chestnut.common.Contants.ORDER_REQUEST_SIZE;

/**
 * @Author: shearson
 * @Time: 2020/2/29 16:29
 * @Desc: 我的订单
 */
public final class OrderFragment extends BaseFragment implements View.OnClickListener, OnHttpListener {

    private MeasureListView mContentList;
    private RecyclerView topType;
    private DaisyRefreshLayout mRefreshLayout;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int REFRESH_SIZE = 0x102;

    private List<PressBean> mTopTypeList;
    // waitSend:待发货 waitRec 待付款 waitSelf 待自提 complete 已完成
    private String[] topTypes = {"全部", "已完成", "待发货", "待付款", "待自提"};
    private List<OrdersBean> mList;
    private List<OrdersBean> mCopyList = new ArrayList<>();
    private TextView mStoreName;
    private LinearLayout mLl_top_store;
    private static int selectedPos = 0;

    private PopupWindow mPopupWindow;
    private List<StoreListBean> mStoreList;
    private StoreListBean mStoreDefault;
    private ImageView mArrow;
    private ImageView mService;
    private String mSearchKeyWord;
    private String mStatus;

    @Override
    protected int initLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView(View view) {
        // 设置状态栏
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        mStoreName = view.findViewById(R.id.tv_store_name);
        mArrow = view.findViewById(R.id.iv_arrow);
        mStoreName.setOnClickListener(this);
        mLl_top_store = view.findViewById(R.id.ll_top_store);
        mArrow.setOnClickListener(this);
        ClearEditText search = view.findViewById(R.id.et_search);
        search.setFocusableInTouchMode(false);
        search.setFocusable(false);
        search.setOnClickListener(this);
        mService = view.findViewById(R.id.iv_service);
        mService.setOnClickListener(this);
        topType = view.findViewById(R.id.rlv_top_type);
        mContentList = view.findViewById(R.id.mlv_content_list);
        // 下拉刷新，上拉加载
        mRefreshLayout = view.findViewById(R.id.drl_refresh);
    }

    private int refreshPage = 0;

    @Override
    public void onResume() {
        super.onResume();
        // request
        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData(Context mContext) {
        // topType
        mTopTypeList = new ArrayList<>();
        for (int i = 0; i < topTypes.length; i++) {
            PressBean press = new PressBean();
            press.setName(topTypes[i]);
            if (i == 0) {
                press.setChoose(true);
            }
            mTopTypeList.add(press);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_service:                   // 拨打客服电话
                PhoneCallUtils.callPhoneDump(Objects.requireNonNull(getActivity()), mStoreDefault.getTel());
                break;
            case R.id.et_search:                    // search
                Contants.RECORD_POS = 0x107;
                Intent intent = new Intent(getmContext(), SearchViewActivity.class);
                startActivityForResult(intent, ORDER_REQUEST_SIZE);
                break;
            case R.id.iv_arrow:             // 门店列表
            case R.id.tv_store_name:
                switchStore();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 订单列表搜索
        if (requestCode == ORDER_REQUEST_SIZE && resultCode == ORDER_REQUEST_SIZE) {
            mSearchKeyWord = data.getStringExtra("searchKeyWord");
            // TODO: request
            Message msg = new Message();
            msg.what = REFRESH_SIZE;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 切换门店
     */
    private void switchStore() {
        View popView = LayoutInflater.from(getContext()).inflate(R.layout.pop_switch_store, null);
        mPopupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setContentView(popView);
        mPopupWindow.setAnimationStyle(R.style.IOSAnimStyle);
        // 加载数据
        ListView storeListView = popView.findViewById(R.id.lv_store_list);
        CartStoreAdapter storeAdapter = new CartStoreAdapter(getContext(), mStoreList);
        storeListView.setAdapter(storeAdapter);
        storeAdapter.setOnClickItem(position -> {
            // toast("选择了：" + position);
            mStoreName.setText(mStoreList.get(position).getName());
            mStoreName.setHint(mStoreList.get(position).getId());
            mPopupWindow.dismiss();
            // TODO:
            Message msg = new Message();
            msg.what = REFRESH_SIZE;
            mHandler.sendMessage(msg);
        });
        ImageView popBack = popView.findViewById(R.id.iv_back);
        popBack.setOnClickListener(v1 -> mPopupWindow.dismiss());
        TextView popTitle = popView.findViewById(R.id.tv_title);
        popTitle.setText(mStoreDefault.getName());
        // dismiss 监听
        mPopupWindow.setOnDismissListener(() -> StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.white10)));
        // 显示popWindow
        StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.white));
        mPopupWindow.showAsDropDown(mLl_top_store, 0, 0);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    mRefreshLayout.setLoadMore(false);
                    mRefreshLayout.setRefreshing(false);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getmContext());
                    //LinearLayoutManager中定制了可扩展的布局排列接口，子类按照接口中的规范来实现就可以定制出不同排雷方式的布局了
                    //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    topType.setLayoutManager(layoutManager);
                    HorSlidingAdapter topTypeAdapter = new HorSlidingAdapter(getmContext());
                    topTypeAdapter.setList(mTopTypeList);
                    topType.setAdapter(topTypeAdapter);
                    topTypeAdapter.setOnClickItem(position -> {
                        for (PressBean press : mTopTypeList) {
                            press.setChoose(false);
                        }
                        mTopTypeList.get(position).setChoose(true);
                        selectedPos = position;
                        // waitSend:待发货 waitRec 待付款 waitSelf 待自提 complete 已完成
                        mStatus = "";
                        switch (position) {
                            case 0:
                                mStatus = "";
                                break;
                            case 1:
                                mStatus = "complete";
                                break;
                            case 2:
                                mStatus = "waitSend";
                                break;
                            case 3:
                                mStatus = "waitRec";
                                break;
                            case 4:
                                mStatus = "waitSelf";
                                break;
                        }
                        // request
                        showLoading("");
                        Message refreshMsg = new Message();
                        refreshMsg.what = REFRESH_SIZE;
                        mHandler.sendMessage(refreshMsg);
                    });
                    OrderListAdapter orderAdapter = new OrderListAdapter(getmContext(), mCopyList);
                    mContentList.setAdapter(orderAdapter);
                    orderAdapter.setOnClickItem(new OrderListAdapter.OnClickItem() {
                        @Override
                        public void onClickItem(int position) {
                            // toast("查看详情:" + position);
                            Intent intent = new Intent(getmContext(), OrderDetailActivity.class);
                            intent.putExtra("orderId", mCopyList.get(position).getId());
                            startActivity(intent);
                        }

                        @Override
                        public void onClickConfirm(int position) {
                            //mList.get(position).setStatus("已完成");
                            postDelayed(() -> {
                                Message msg1 = new Message();
                                //msg1.what = CONTENT_SIZE;
                                mHandler.sendMessage(msg1);
                            }, 1500);

                        }
                    });
                    break;
                case REFRESH_SIZE:
                    // 上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        refreshPage++;
                        RequestPost.getOrderList(String.valueOf(refreshPage), mStoreName.getHint().toString().trim(), mStatus, mSearchKeyWord, OrderFragment.this);
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        refreshPage = 0;
                        mCopyList = new ArrayList<>();
                        RequestPost.getOrderList(String.valueOf(refreshPage), "", "", "", OrderFragment.this);
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        refreshPage = 0;
                        mCopyList = new ArrayList<>();
                        RequestPost.getOrderList(String.valueOf(refreshPage),
                                mStoreName.getHint() == null ? "" : mStoreName.getHint().toString().trim(),
                                mStatus,
                                mSearchKeyWord, OrderFragment.this);
                    });
                    mRefreshLayout.autoRefresh();
                    break;
            }
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
                // 获取订单列表
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getAllOrder")) {
                    mList = JsonParser.parseJSONArray(OrdersBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));
                    mStoreList = JsonParser.parseJSONArray(StoreListBean.class, JsonParser.parseJSONObject(body.get("data")).get("storelist"));
                    mStoreDefault = JsonParser.parseJSONObject(StoreListBean.class, JsonParser.parseJSONObject(body.get("data")).get("storeInfo"));
                    // 数据隐藏
                    if (ListUtils.getSize(mStoreList) == 0) {
                        mArrow.setVisibility(View.GONE);
                        mService.setVisibility(View.GONE);
                        mStoreName.setVisibility(View.GONE);
                    } else {
                        mStoreName.setVisibility(View.VISIBLE);
                        mService.setVisibility(View.VISIBLE);
                        mArrow.setVisibility(View.VISIBLE);
                    }
                    // 默认的门店
                    if (mStoreDefault != null) {
                        mStoreName.setText(mStoreDefault.getName());
                        mStoreName.setHint(mStoreDefault.getId());
                    }
                    // 上拉刷新，下拉加载
                    mCopyList.addAll(mList);

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}

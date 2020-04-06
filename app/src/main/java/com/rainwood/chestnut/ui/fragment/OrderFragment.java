package com.rainwood.chestnut.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseFragment;
import com.rainwood.chestnut.domain.OrderBean;
import com.rainwood.chestnut.domain.OrdersBean;
import com.rainwood.chestnut.domain.PressBean;
import com.rainwood.chestnut.domain.StoreBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.activity.OrderDetailActivity;
import com.rainwood.chestnut.ui.activity.SearchViewActivity;
import com.rainwood.chestnut.ui.adapter.CartStoreAdapter;
import com.rainwood.chestnut.ui.adapter.HorSlidingAdapter;
import com.rainwood.chestnut.ui.adapter.OrderListAdapter;
import com.rainwood.chestnut.utils.PhoneCallUtils;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: shearson
 * @Time: 2020/2/29 16:29
 * @Desc: 我的订单
 */
public final class OrderFragment extends BaseFragment implements View.OnClickListener, OnHttpListener {

    private MeasureListView mContentList;
    private RecyclerView topType;
    private DaisyRefreshLayout mRefreshLayout;
    private OrderBean order;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int CONTENT_SIZE = 0x102;
    private final int REFRESH_SIZE = 0x103;

    private List<PressBean> mTopTypeList;
    private String[] topTypes = {"全部", "已完成", "待发货", "待付款", "已付款", "待收货", "退货/售后"};
    private List<OrdersBean> mList;
    private TextView mStoreName;
    private LinearLayout mLl_top_store;

    @Override
    protected int initLayout() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView(View view) {
        // 设置状态栏
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        mStoreName = view.findViewById(R.id.tv_store_name);
        ImageView arrow = view.findViewById(R.id.iv_arrow);
        mStoreName.setOnClickListener(this);
        mLl_top_store = view.findViewById(R.id.ll_top_store);
        arrow.setOnClickListener(this);
        ClearEditText search = view.findViewById(R.id.et_search);
        search.setFocusableInTouchMode(false);
        search.setFocusable(false);
        search.setOnClickListener(this);
        ImageView service = view.findViewById(R.id.iv_service);
        service.setOnClickListener(this);
        topType = view.findViewById(R.id.rlv_top_type);
        mContentList = view.findViewById(R.id.mlv_content_list);
        // 下拉刷新，上拉加载
        mRefreshLayout = view.findViewById(R.id.drl_refresh);
    }

    @Override
    public void onResume() {
        super.onResume();
        // request
        showLoading("");
        RequestPost.getOrderList("", this);
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
        // store
        mStoreList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            StoreBean store = new StoreBean();
            store.setName("衣香丽影旗舰店");
            store.setTotalAmount("25600€");
            mStoreList.add(store);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_service:                   // 拨打客服电话
                PhoneCallUtils.callPhoneDump(Objects.requireNonNull(getActivity()), "10086");
                break;
            case R.id.et_search:                    // search
                startActivity(SearchViewActivity.class);
                break;
            case R.id.iv_arrow:             // 门店列表
            case R.id.tv_store_name:
                switchStore();
                break;
        }
    }

    private PopupWindow mPopupWindow;
    private List<StoreBean> mStoreList;

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
            mPopupWindow.dismiss();
        });
        ImageView popBack = popView.findViewById(R.id.iv_back);
        popBack.setOnClickListener(v1 -> mPopupWindow.dismiss());
        TextView popTitle = popView.findViewById(R.id.tv_title);
        popTitle.setText("影儿诗篇女装");
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
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    //LinearLayoutManager中定制了可扩展的布局排列接口，子类按照接口中的规范来实现就可以定制出不同排雷方式的布局了
                    //配置布局，默认为vertical（垂直布局），下边这句将布局改为水平布局
                    layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    topType.setLayoutManager(layoutManager);
                    HorSlidingAdapter topTypeAdapter = new HorSlidingAdapter(getContext());
                    topTypeAdapter.setList(mTopTypeList);
                    topType.setAdapter(topTypeAdapter);
                    topTypeAdapter.setOnClickItem(position -> {
                        for (PressBean press : mTopTypeList) {
                            press.setChoose(false);
                        }
                        mTopTypeList.get(position).setChoose(true);
                        // request

                    });

                    // content
                    Message msgRefresh = new Message();
                    msgRefresh.what = REFRESH_SIZE;
                    mHandler.sendMessage(msgRefresh);
                    break;
                case CONTENT_SIZE:
                    Log.i(TAG, " --- Size== " + mList.size());
                    OrderListAdapter orderAdapter = new OrderListAdapter(getContext(), mList);
                    mContentList.setAdapter(orderAdapter);
                    orderAdapter.setOnClickItem(new OrderListAdapter.OnClickItem() {
                        @Override
                        public void onClickItem(int position) {
                            // toast("查看详情:" + position);
                            Intent intent = new Intent(getmContext(), OrderDetailActivity.class);
                            intent.putExtra("orderId", mList.get(position).getId());
                            startActivity(intent);
                        }

                        @Override
                        public void onClickConfirm(int position) {
                            //mList.get(position).setStatus("已完成");
                            postDelayed(() -> {
                                Message msg1 = new Message();
                                msg1.what = CONTENT_SIZE;
                                mHandler.sendMessage(msg1);
                            }, 1500);

                        }
                    });
                    break;
                case REFRESH_SIZE:
                    Message msgContent = new Message();
                    msgContent.what = CONTENT_SIZE;
                    mHandler.sendMessage(msgContent);
                    mRefreshLayout.setLoadMore(false);
                    // 下拉刷新
                    mRefreshLayout.setEnableRefresh(false);         // 停用下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        /*Log.i(TAG, "refresh start");
                        postDelayed(() -> {
                            Log.i(TAG, "response ok");
                            Message msgContent = new Message();
                            msgContent.what = CONTENT_SIZE;
                            mHandler.sendMessage(msgContent);
                            mRefreshLayout.setRefreshing(false);
                        }, 1000 * 3);*/
                    });
                    // 上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        Log.i(TAG, "loadMore start");
                        //mList.add(order);
                        postDelayed(() -> {
                            Log.i(TAG, "response ok");
                            Message msgContent1 = new Message();
                            msgContent1.what = CONTENT_SIZE;
                            mHandler.sendMessage(msgContent1);
                            mRefreshLayout.setLoadMore(false);
                        }, 1000 * 3);
                    });
                    // 第一次进来的时候刷新
                  /*  if (count <= 0) {
                        mRefreshLayout.setOnAutoLoadListener(() -> {
                            Log.i(TAG, " --- 开始加载数据了");
                            postDelayed(() -> {
                                Message msgContent = new Message();
                                msgContent.what = CONTENT_SIZE;
                                mHandler.sendMessage(msgContent);
                                mRefreshLayout.setRefreshing(false);
                            }, 1000 * 3);
                        });
                        mRefreshLayout.autoRefresh();
                        ++count;
                    }*/
                    break;
            }
        }
    };

    private static int count = 0;

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " ---- result ----- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取订单列表
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getAllOrder")) {
                    mList = JsonParser.parseJSONArray(OrdersBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
        }
        dismissLoading();
    }
}

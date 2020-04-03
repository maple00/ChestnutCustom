package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.PressBean;
import com.rainwood.chestnut.domain.ShopBean;
import com.rainwood.chestnut.ui.adapter.ShopAdapter;
import com.rainwood.chestnut.ui.adapter.ShopWaterFallAdapter;
import com.rainwood.chestnut.ui.adapter.TypeListAdapter;
import com.rainwood.chestnut.utils.RecyclerViewSpacesItemUtils;
import com.rainwood.chestnut.utils.TestUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/4 9:21
 * @Desc: 二级分类页面
 */
public final class SecondaryActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_secondary;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.ll_top_type)
    private LinearLayout topType;
    @ViewById(R.id.tv_type_name)
    private TextView typeName;
    @ViewById(R.id.iv_arrow)
    private ImageView arrow;
    @ViewById(R.id.iv_search)
    private ImageView search;
    @ViewById(R.id.iv_cart)
    private ImageView cart;
    @ViewById(R.id.rlv_shop)
    private RecyclerView shopList;
    @ViewById(R.id.drl_refresh)
    private DaisyRefreshLayout mRefreshLayout;

    // popWindow
    private PopupWindow mPopupWindow;
    private ListView typeList;
    private GridView goodsList;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int POP_TYPE_SIZE = 0x102;
    private final int POP_SHOP_SIZE = 0x103;
    private final int REFRESH_SIZE = 0x104;

    private ShopBean mShop;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        topType.setOnClickListener(this);
        search.setOnClickListener(this);
        cart.setOnClickListener(this);

        // 设置RecyclerItem的item之间的间距
        shopList.addItemDecoration(new RecyclerViewSpacesItemUtils(
                FontDisplayUtil.dip2px(this, 5f),
                FontDisplayUtil.dip2px(this, 5f),
                FontDisplayUtil.dip2px(this, 5f),
                FontDisplayUtil.dip2px(this, 10f)));
        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
        // 新品、促销
        mShopList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            mShop = new ShopBean();
            if (i % 3 == 0) {
                mShop.setName("荔枝纹斜挎水桶包荔枝纹斜挎水桶包荔枝纹荔枝纹斜挎水桶包荔枝纹斜挎水桶包荔枝纹");
            } else {
                mShop.setName("荔枝纹斜挎挎枝纹");
            }
            mShop.setImg(null);
            mShop.setNumber(String.valueOf(TestUtils.getRandonNum(1000, 0)));
            mShop.setPrice("110€-149€");
            mShop.setNum("GD-002561");
            if (TestUtils.getRandonNum(100, 1) % 2 == 0) {
                mShop.setLabel("1");
            } else {
                mShop.setLabel(null);
            }
            mShopList.add(mShop);
        }
        // 分类
        newTypeList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            PressBean press = new PressBean();
            if (i == 0) {
                press.setChoose(true);
            }
            press.setName("围巾/帽子");
            newTypeList.add(press);
        }
        mPopShopList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ShopBean shop = new ShopBean();
            shop.setImg(null);
            shop.setName("针织开衫针织");
            mPopShopList.add(shop);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_top_type:
                // toast("选择分类");
                View popView = LayoutInflater.from(this).inflate(R.layout.pop_switch_shop, null);
                mPopupWindow = new PopupWindow(popView,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                mPopupWindow.setContentView(popView);
                mPopupWindow.setAnimationStyle(R.style.TopAnimStyle);
                // 加载数据
                ImageView popBack = popView.findViewById(R.id.iv_back);
                popBack.setOnClickListener(v1 -> mPopupWindow.dismiss());
                TextView popTitle = popView.findViewById(R.id.tv_title);
                popTitle.setText("箱包");
                // initial
                typeList = popView.findViewById(R.id.lv_type_list);
                goodsList = popView.findViewById(R.id.gv_goods_list);
                Message msg = new Message();
                msg.what = POP_TYPE_SIZE;
                mHandler.sendMessage(msg);
                // dismiss 监听
                mPopupWindow.setOnDismissListener(() -> StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.white10)));
                // 显示popWindow
                StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.white));
                mPopupWindow.showAsDropDown(topType, 0, 0);
                break;
            case R.id.iv_search:
                // toast("搜索");
                openActivity(SearchViewActivity.class);
                break;
            case R.id.iv_cart:
                // toast("购物车");
                Intent intent = new Intent(this, HomeActivity.class);
                intent.putExtra("id", Contants.CART_SIZE);
                startActivity(intent);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    // content
                    ShopWaterFallAdapter promoAdapter = new ShopWaterFallAdapter(SecondaryActivity.this);
                    promoAdapter.setList(mShopList);
                    StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    shopList.setLayoutManager(manager);
                    shopList.setAdapter(promoAdapter);
                    // 查看详情
                    promoAdapter.setOnClickItem(position -> openActivity(ShopDetailActivity.class));
                    break;
                case REFRESH_SIZE:
                    // topType
                    typeName.setText("箱包");
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        Log.i(TAG, "load start");
                        mShopList.add(mShop);
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
                        mShopList.add(mShop);
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
                    mRefreshLayout.autoRefresh();
                    break;
                case POP_TYPE_SIZE:
                    TypeListAdapter listAdapter = new TypeListAdapter(SecondaryActivity.this, newTypeList);
                    typeList.setAdapter(listAdapter);
                    listAdapter.setOnClickText(position -> {
                        for (PressBean type : newTypeList) {
                            type.setChoose(false);
                        }
                        newTypeList.get(position).setChoose(true);
                        //数据
                        for (ShopBean shopBean : mPopShopList) {
                            shopBean.setName(newTypeList.get(position).getName() + "/" + position);
                        }
                        Message shopMsg = new Message();
                        shopMsg.what = POP_SHOP_SIZE;
                        mHandler.sendMessage(shopMsg);
                    });
                    // 默认选择第一项
                    if (newTypeList.get(0).isChoose()) {
                        //数据
                        for (ShopBean shopBean : mPopShopList) {
                            shopBean.setName(newTypeList.get(0).getName() + "/0");
                        }
                        Message shopMsg = new Message();
                        shopMsg.what = POP_SHOP_SIZE;
                        mHandler.sendMessage(shopMsg);
                    }
                    break;
                case POP_SHOP_SIZE:
                    ShopAdapter shopAdapter = new ShopAdapter(SecondaryActivity.this, mPopShopList);
                    goodsList.setAdapter(shopAdapter);
                    goodsList.setNumColumns(2);
                    shopAdapter.setOnClickItem(position -> {
                        // toast("您选择了商品：" + mShopList.get(position).getName() + " -- " + position);
                        typeName.setText(mPopShopList.get(position).getName() + " -- " + position);
                        mPopupWindow.dismiss();
                    });
                    break;
            }
        }
    };

    // 商品
    private List<ShopBean> mShopList;
    // 分类
    private List<PressBean> newTypeList;
    private List<ShopBean> mPopShopList;
}

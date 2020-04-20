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
import com.rainwood.chestnut.domain.GoodsBean;
import com.rainwood.chestnut.domain.GoodsParentBean;
import com.rainwood.chestnut.domain.NewShopsBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.ShopAdapter;
import com.rainwood.chestnut.ui.adapter.ShopWaterFallAdapter;
import com.rainwood.chestnut.ui.adapter.TypeListAdapter;
import com.rainwood.chestnut.utils.RecyclerViewSpacesItemUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.toast.ToastUtils;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/4 9:21
 * @Desc: 二级分类页面
 */
public final class SecondaryActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mTypeName;
    private String mTypeId;
    private static int refreshPage = 0;

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

    // 商品
    private List<NewShopsBean> mShopList;
    // 分类
    private List<GoodsParentBean> newTypeList;
    private int parantPos = 0;
    private List<GoodsBean> mPopShopList;
    private int count = 0;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTypeId = getIntent().getStringExtra("typeId");
        mTypeName = getIntent().getStringExtra("typeName");
        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);

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
            case R.id.ll_top_type:
                // request
                RequestPost.getStoreInfo("classify", this);
                // toast("选择分类");
                View popView = LayoutInflater.from(this).inflate(R.layout.pop_switch_shop, null);
                mPopupWindow = new PopupWindow(popView,
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
                mPopupWindow.setContentView(popView);
                mPopupWindow.setAnimationStyle(R.style.IOSAnimStyle);
                // 加载数据
                ImageView popBack = popView.findViewById(R.id.iv_back);
                popBack.setOnClickListener(v1 -> mPopupWindow.dismiss());
                TextView popTitle = popView.findViewById(R.id.tv_title);
                popTitle.setText(mTypeName);
                // initial
                typeList = popView.findViewById(R.id.lv_type_list);
                goodsList = popView.findViewById(R.id.gv_goods_list);
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
                    mRefreshLayout.setLoadMore(false);
                    mRefreshLayout.setRefreshing(false);
                    // content
                    ShopWaterFallAdapter promoAdapter = new ShopWaterFallAdapter(SecondaryActivity.this);
                    promoAdapter.setList(mShopList);
                    StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    shopList.setLayoutManager(manager);
                    shopList.setAdapter(promoAdapter);
                    // 查看详情
                    promoAdapter.setOnClickItem(position -> {
                        Intent intent = new Intent(SecondaryActivity.this, ShopDetailActivity.class);
                        intent.putExtra("goodsId", mShopList.get(position).getId());
                        startActivity(intent);
                    });
                    break;
                case REFRESH_SIZE:
                    // topType
                    typeName.setText(mTypeName);
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        refreshPage++;
                        RequestPost.getClassifyGoods(mTypeId, String.valueOf(refreshPage), SecondaryActivity.this);
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        refreshPage = 0;
                        RequestPost.getClassifyGoods(mTypeId, String.valueOf(refreshPage), SecondaryActivity.this);
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        refreshPage = 0;
                        RequestPost.getClassifyGoods(mTypeId, String.valueOf(refreshPage), SecondaryActivity.this);
                    });
                    mRefreshLayout.autoRefresh();
                    break;
                case POP_TYPE_SIZE:
                    newTypeList.get(parantPos).setSelected(true);
                    TypeListAdapter listAdapter = new TypeListAdapter(SecondaryActivity.this, newTypeList);
                    typeList.setAdapter(listAdapter);
                    listAdapter.setOnClickText(position -> {
                        for (GoodsParentBean type : newTypeList) {
                            type.setSelected(false);
                        }
                        parantPos = position;
                        newTypeList.get(position).setSelected(true);
                        // 查询一级分类下的二级分类 request
                        RequestPost.getClassify(newTypeList.get(position).getGoodsTypeOneId(), SecondaryActivity.this);
                    });
                    // 默认选择第一项
                    if (newTypeList.get(0).isSelected()) {
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
                        mPopupWindow.dismiss();
                        typeName.setText(mPopShopList.get(position).getName());
                        // request
                        mTypeId = mPopShopList.get(position).getGoodsTypeTwoId();
                        Message refreshMsg = new Message();
                        msg.what = REFRESH_SIZE;
                        mHandler.sendMessage(refreshMsg);
                    });
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
                // 获取分类下的二级商品列表
                if (result.url().equals(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getClassifyGoods")) {
                    mShopList = JsonParser.parseJSONArray(NewShopsBean.class, JsonParser.parseJSONObject(body.get("data")).get("list"));
                    count = Math.abs((mShopList == null ? 0 : mShopList.size()) - count);
                    // toast("本次为您更新了" + count + "件商品");
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 获取分类一级列表
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getGoodslist")) {
                    newTypeList = JsonParser.parseJSONArray(GoodsParentBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypeOne"));
                    mPopShopList = JsonParser.parseJSONArray(GoodsBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypeTwo"));

                    Message msg = new Message();
                    msg.what = POP_TYPE_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 获取分类的二级分类
                if (result.url().equals(Contants.ROOT_URL + "wxapi/v1/clientGoods.php?type=getClassify")) {
                    mPopShopList = JsonParser.parseJSONArray(GoodsBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypeTwo"));

                    Message msg = new Message();
                    msg.what = POP_SHOP_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
        }
    }
}

package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.GoodsBean;
import com.rainwood.chestnut.domain.GoodsParentBean;
import com.rainwood.chestnut.domain.NewShopsBean;
import com.rainwood.chestnut.domain.PressBean;
import com.rainwood.chestnut.domain.PromotionGoodsBean;
import com.rainwood.chestnut.domain.ShopBean;
import com.rainwood.chestnut.domain.StoresBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.PromotionWaterFallAdapter;
import com.rainwood.chestnut.ui.adapter.ShopAdapter;
import com.rainwood.chestnut.ui.adapter.ShopWaterFallAdapter;
import com.rainwood.chestnut.ui.adapter.TopTypesAdapter;
import com.rainwood.chestnut.ui.adapter.TypeListAdapter;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/3 15:21
 * @Desc: 商铺详情
 */
public final class StoreDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.iv_cart)         // 购物车
    private ImageView cart;
    @ViewById(R.id.tv_history_order)
    private TextView historyOrder;
    @ViewById(R.id.mgv_top_type)
    private MeasureGridView topType;
    // 分类
    @ViewById(R.id.lv_type_list)
    private ListView typeList;
    @ViewById(R.id.gv_goods_list)
    private GridView goodsList;
    @ViewById(R.id.ll_type)
    private LinearLayout typeContent;
    // 新品、促销
    @ViewById(R.id.rlv_shop)
    private RecyclerView shops;
    // 商家信息
    @ViewById(R.id.tv_name)
    private TextView storeName;
    @ViewById(R.id.iv_img)
    private ImageView storeImg;
    @ViewById(R.id.tv_num)
    private TextView model;
    @ViewById(R.id.tv_tel)
    private TextView tel;
    @ViewById(R.id.tv_desc)
    private TextView desc;

    // mHandler
    private final int INITIAL_SIZE = 0x101;         // 初始化
    private final int TYPES_SIZE = 0x102;           // 分类类型
    private final int SHOP_SIZE = 0x103;           // 分类商品
    private final int NEWSHOP_SIZE = 0x104;         // 新品商品
    private final int PROMOTION_SIZE = 0x105;       // 促销商品
    private static int TYPE_SIZE;
    // 顶部type
    private List<PressBean> mTopTypeList;
    private String[] topTypes = {"分类", "新品", "促销"};
    // 分类
    private List<PressBean> newTypeList;
    private List<GoodsBean> mShopList;
    // 新品、促销
    private List<NewShopsBean> mNewShopList;
    private List<PromotionGoodsBean> mPromoList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        cart.setOnClickListener(this);
        historyOrder.setOnClickListener(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

        // request -- 默认查询分类
        TYPE_SIZE = INITIAL_SIZE;
        showLoading("");
        RequestPost.getStoreInfo("classify", this);
    }

    @Override
    protected void initData() {
        super.initData();
        mTopTypeList = new ArrayList<>();
        for (String topType : topTypes) {
            PressBean press = new PressBean();
            press.setName(topType);
            if (topType.equals("分类")) {
                press.setChoose(true);
            }
            mTopTypeList.add(press);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(StoreDetailActivity.this, HomeActivity.class);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_cart:              // -- 跳转到购物车
                intent.putExtra("id", Contants.CART_SIZE);
                startActivity(intent);
                break;
            case R.id.tv_history_order:     // 跳转到我的订单
                intent.putExtra("id", Contants.ORDER_SIZE);
                startActivity(intent);
                break;
        }
    }


    private int parentPos = 0;
    private int pos;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    TopTypesAdapter typesAdapter = new TopTypesAdapter(StoreDetailActivity.this, mTopTypeList);
                    topType.setAdapter(typesAdapter);
                    topType.setNumColumns(3);
                    typesAdapter.setOnClickItem(position -> {
                        for (PressBean press : mTopTypeList) {
                            press.setChoose(false);
                        }
                        mTopTypeList.get(position).setChoose(true);
                        // addContent
                        switch (position) {
                            case 0:             // 分类
                                typeContent.setVisibility(View.VISIBLE);
                                shops.setVisibility(View.GONE);
                                // request
                                TYPE_SIZE = TYPES_SIZE;
                                showLoading("");
                                RequestPost.getStoreInfo("classify", StoreDetailActivity.this);
                                break;
                            case 1:             // 新品   --- RecyclerView 瀑布式布局
                                typeContent.setVisibility(View.GONE);
                                shops.setVisibility(View.VISIBLE);
                                dumpToMhandler(NEWSHOP_SIZE);
                                // request
                                TYPE_SIZE = NEWSHOP_SIZE;
                                showLoading("");
                                RequestPost.getStoreInfo("new", StoreDetailActivity.this);
                                break;
                            case 2:             // 促销   --- RecyclerView 瀑布式布局
                                typeContent.setVisibility(View.GONE);
                                shops.setVisibility(View.VISIBLE);
                                dumpToMhandler(PROMOTION_SIZE);
                                // request
                                TYPE_SIZE = PROMOTION_SIZE;
                                showLoading("");
                                RequestPost.getStoreInfo("promotion", StoreDetailActivity.this);
                                break;
                        }
                    });
                    // 默认加载分类
                    if (mTopTypeList.get(0).isChoose()) {
                        dumpToMhandler(TYPES_SIZE);
                    }
                    break;
                case TYPES_SIZE:                // 分类
                    newTypeList.get(parentPos).setChoose(true);
                    TypeListAdapter listAdapter = new TypeListAdapter(StoreDetailActivity.this, newTypeList);
                    typeList.setAdapter(listAdapter);
                    listAdapter.setOnClickText(position -> {
                        for (PressBean type : newTypeList) {
                            type.setChoose(false);
                        }
                        newTypeList.get(position).setChoose(true);
                        // request
                        showLoading("");
                        RequestPost.getClassify(newTypeList.get(position).getId(), StoreDetailActivity.this);
                        //
                    });
                    // 默认选择第一项
                    if (newTypeList.get(0).isChoose()) {
                        dumpToMhandler(SHOP_SIZE);
                    }
                    break;
                case SHOP_SIZE:                 // 商品   --- 到二级分类页面
                    ShopAdapter shopAdapter = new ShopAdapter(StoreDetailActivity.this, mShopList);
                    goodsList.setAdapter(shopAdapter);
                    goodsList.setNumColumns(2);
                    // 二级分类页面
                    shopAdapter.setOnClickItem(position -> openActivity(SecondaryActivity.class));
                    break;
                case NEWSHOP_SIZE:          // 新品
                    // setWaterFallShop(mNewShopList);
                    ShopWaterFallAdapter newShopAdapter = new ShopWaterFallAdapter(StoreDetailActivity.this);
                    newShopAdapter.setList(mNewShopList);
                    StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    shops.setLayoutManager(manager);
                    shops.setAdapter(newShopAdapter);
                    newShopAdapter.setOnClickItem(position -> {
                        // toast("查看详情：" + position);
                        Intent intent = new Intent(StoreDetailActivity.this, ShopDetailActivity.class);
                        intent.putExtra("goodsId", mNewShopList.get(position).getId());
                        startActivity(intent);
                    });
                    break;
                case PROMOTION_SIZE:            // 促销
                    PromotionWaterFallAdapter promoAdapter = new PromotionWaterFallAdapter(StoreDetailActivity.this);
                    promoAdapter.setList(mPromoList);
                    StaggeredGridLayoutManager promoManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                    shops.setLayoutManager(promoManager);
                    shops.setAdapter(promoAdapter);
                    promoAdapter.setOnClickItem(position -> {
                        // toast("查看详情：" + position);
                        Intent intent = new Intent(StoreDetailActivity.this, ShopDetailActivity.class);
                        intent.putExtra("goodsId", mPromoList.get(position).getId());
                        startActivity(intent);
                    });
                    break;
            }
        }
    };

    /**
     * 跳转到mHandler
     *
     * @param size
     */
    private void dumpToMhandler(int size) {
        Message shopMsg = new Message();
        shopMsg.what = size;
        mHandler.sendMessage(shopMsg);
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " ---- result ----- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取商家列表
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getGoodslist")) {
                    // 商家信息
                    StoresBean storesBean = JsonParser.parseJSONObject(StoresBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    if (storesBean != null){
                        storeName.setText(storesBean.getName());
                        Glide.with(this).load(storesBean.getIco()).into(storeImg);
                        model.setText(storesBean.getId());
                        tel.setText(storesBean.getTel());
                        desc.setText(storesBean.getText());
                    }
                    // 获取分类信息
                    if (TYPE_SIZE == INITIAL_SIZE || TYPE_SIZE == TYPES_SIZE) {
                        List<GoodsParentBean> goodsParentList = JsonParser.parseJSONArray(GoodsParentBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypeOne"));
                        List<GoodsBean> goodsList = JsonParser.parseJSONArray(GoodsBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypeTwo"));
                        if (goodsParentList != null) {
                            newTypeList = new ArrayList<>();
                            for (GoodsParentBean parentBean : goodsParentList) {
                                PressBean press = new PressBean();
                                press.setChoose(false);
                                press.setName(parentBean.getGoodsTypeOne());
                                press.setId(parentBean.getGoodsTypeOneId());
                                newTypeList.add(press);
                            }
                        }
                        if (goodsList != null) {
                            mShopList = new ArrayList<>();
                            mShopList.addAll(goodsList);
                        }
                        dumpToMhandler(INITIAL_SIZE);
                    }
                    // 获取新品商品
                    if (TYPE_SIZE == NEWSHOP_SIZE) {
                        mNewShopList = JsonParser.parseJSONArray(NewShopsBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));
                        dumpToMhandler(NEWSHOP_SIZE);
                    }
                    // 获取促销商品
                    if (TYPE_SIZE == PROMOTION_SIZE) {
                        mPromoList = JsonParser.parseJSONArray(PromotionGoodsBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));
                        dumpToMhandler(PROMOTION_SIZE);
                    }
                }
                // 点击一级分类获取二级分类
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getClassify")){
                    List<GoodsBean> goodsList = JsonParser.parseJSONArray(GoodsBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypeTwo"));
                    if (goodsList != null) {
                        mShopList = new ArrayList<>();
                        mShopList.addAll(goodsList);
                    }
                    dumpToMhandler(SHOP_SIZE);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

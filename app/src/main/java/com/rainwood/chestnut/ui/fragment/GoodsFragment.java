package com.rainwood.chestnut.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseFragment;
import com.rainwood.chestnut.domain.PressBean;
import com.rainwood.chestnut.domain.ShopBean;
import com.rainwood.chestnut.ui.activity.SearchViewActivity;
import com.rainwood.chestnut.ui.activity.SecondaryActivity;
import com.rainwood.chestnut.ui.activity.ShopDetailActivity;
import com.rainwood.chestnut.ui.adapter.ShopAdapter;
import com.rainwood.chestnut.ui.adapter.ShopWaterFallAdapter;
import com.rainwood.chestnut.ui.adapter.TopTypesAdapter;
import com.rainwood.chestnut.ui.adapter.TypeListAdapter;
import com.rainwood.chestnut.utils.RecyclerViewSpacesItemUtils;
import com.rainwood.chestnut.utils.TestUtils;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.widget.MeasureGridView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author: shearson
 * @Time: 2020/2/29 16:28
 * @Desc: 商品
 */
public final class GoodsFragment extends BaseFragment implements View.OnClickListener {


    @Override
    protected int initLayout() {
        return R.layout.fragment_goods;
    }

    private MeasureGridView topType;
    private ListView typeList;
    private GridView goodsList;
    private LinearLayout typeContent;
    private RecyclerView shops;

    // mHandler
    private final int INITIAL_SIZE = 0x101;         // 初始化
    private final int TYPES_SIZE = 0x102;           // 分类类型
    private final int SHOP_SIZE = 0x103;           // 分类商品
    private final int NEWSHOP_SIZE = 0x104;         // 新品商品
    private final int PROMOTION_SIZE = 0x105;       // 促销商品

    @Override
    protected void initView(View view) {
        ImageView pageBack = view.findViewById(R.id.iv_back);
        pageBack.setVisibility(View.GONE);
        ImageView cart = view.findViewById(R.id.iv_cart);
        cart.setOnClickListener(this);
        TextView historyOrder = view.findViewById(R.id.tv_history_order);
        historyOrder.setOnClickListener(this);
        topType = view.findViewById(R.id.mgv_top_type);
        ClearEditText clearEditText = view.findViewById(R.id.et_search);
        clearEditText.setFocusable(false);
        clearEditText.setFocusableInTouchMode(false);
        clearEditText.setOnClickListener(this);
        // 分类
        typeList = view.findViewById(R.id.lv_type_list);
        goodsList = view.findViewById(R.id.gv_goods_list);
        typeContent = view.findViewById(R.id.ll_type);
        shops = view.findViewById(R.id.rlv_shop);
        // 设置RecyclerItem的item之间的间距
        shops.addItemDecoration(new RecyclerViewSpacesItemUtils(
                FontDisplayUtil.dip2px(Objects.requireNonNull(getContext()), 5f),
                FontDisplayUtil.dip2px(getContext(), 5f),
                FontDisplayUtil.dip2px(getContext(), 5f),
                FontDisplayUtil.dip2px(getContext(), 10f)));

        dumpToMhandler(INITIAL_SIZE);
    }

    @Override
    protected void initData(Context mContext) {

        mTopTypeList = new ArrayList<>();
        for (String topType : topTypes) {
            PressBean press = new PressBean();
            press.setName(topType);
            if (topType.equals("分类")) {
                press.setChoose(true);
            }
            mTopTypeList.add(press);
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
        mShopList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ShopBean shop = new ShopBean();
            shop.setImg(null);
            shop.setName("针织开衫针织");
            mShopList.add(shop);
        }
        // 新品、促销
        mNewShopList = new ArrayList<>();
        mPromoList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            ShopBean shop = new ShopBean();
            if (i % 3 == 0) {
                shop.setName("荔枝纹斜挎水桶包荔枝纹斜挎水桶包荔枝纹荔枝纹斜挎水桶包荔枝纹斜挎水桶包荔枝纹");
            } else {
                shop.setName("荔枝纹斜挎挎枝纹");
            }
            shop.setImg(null);
            shop.setNumber(String.valueOf(10));
            shop.setPrice("110€-149€");
            shop.setNum("GD-002561");
            if (TestUtils.getRandonNum(100, 1) % 2 == 0) {
                shop.setLabel("1");
                mNewShopList.add(shop);
            } else {
                shop.setLabel(null);
                mPromoList.add(shop);
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_cart:      // 跳转到购物车
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new CartFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.tv_history_order:             // 跳转到订单
                Objects.requireNonNull(getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new OrderFragment(), null)
                        .addToBackStack(null)
                        .commit();
                break;
            case R.id.et_search:            // 跳转到搜索
                startActivity(SearchViewActivity.class);
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
                    TopTypesAdapter typesAdapter = new TopTypesAdapter(getActivity(), mTopTypeList);
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
                                dumpToMhandler(TYPES_SIZE);
                                break;
                            case 1:             // 新品   --- RecyclerView 瀑布式布局
                                typeContent.setVisibility(View.GONE);
                                shops.setVisibility(View.VISIBLE);
                                dumpToMhandler(NEWSHOP_SIZE);
                                break;
                            case 2:             // 促销   --- RecyclerView 瀑布式布局
                                typeContent.setVisibility(View.GONE);
                                shops.setVisibility(View.VISIBLE);
                                dumpToMhandler(PROMOTION_SIZE);
                                break;
                        }
                    });
                    // 默认加载分类
                    if (mTopTypeList.get(0).isChoose()) {
                        dumpToMhandler(TYPES_SIZE);
                    }
                    break;
                case TYPES_SIZE:                // 分类
                    TypeListAdapter listAdapter = new TypeListAdapter(getActivity(), newTypeList);
                    typeList.setAdapter(listAdapter);
                    listAdapter.setOnClickText(position -> {
                        for (PressBean type : newTypeList) {
                            type.setChoose(false);
                        }
                        newTypeList.get(position).setChoose(true);
                        //数据
                        for (ShopBean shopBean : mShopList) {
                            shopBean.setName(newTypeList.get(position).getName() + "/" + position);
                        }
                        dumpToMhandler(SHOP_SIZE);
                    });
                    // 默认选择第一项
                    if (newTypeList.get(0).isChoose()) {
                        //数据
                        for (ShopBean shopBean : mShopList) {
                            shopBean.setName(newTypeList.get(0).getName() + "/0");
                        }
                        dumpToMhandler(SHOP_SIZE);
                    }
                    break;
                case SHOP_SIZE:                 // 商品   --- 到二级分类页面
                    ShopAdapter shopAdapter = new ShopAdapter(getActivity(), mShopList);
                    goodsList.setAdapter(shopAdapter);
                    goodsList.setNumColumns(2);
                    shopAdapter.setOnClickItem(position -> {
                        // toast("您选择了商品：" + mShopList.get(position).getName() + " -- " + position);
                        startActivity(SecondaryActivity.class);
                    });
                    break;
                case NEWSHOP_SIZE:          // 新品
                    setWaterFallShop(mNewShopList);
                    break;
                case PROMOTION_SIZE:            // 促销
                    setWaterFallShop(mPromoList);
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

    /**
     * 显示瀑布式布局商品
     *
     * @param promoList
     */
    private void setWaterFallShop(List<ShopBean> promoList) {
        ShopWaterFallAdapter promoAdapter = new ShopWaterFallAdapter(getActivity());
        promoAdapter.setList(promoList);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        shops.setLayoutManager(manager);
        shops.setAdapter(promoAdapter);
        promoAdapter.setOnClickItem(position -> {
            // toast("查看详情：" + position);
            startActivity(ShopDetailActivity.class);
        });
    }

    /*
    模拟数据
     */
    // 顶部type
    private List<PressBean> mTopTypeList;
    private String[] topTypes = {"分类", "新品", "促销"};
    // 分类
    private List<PressBean> newTypeList;
    private List<ShopBean> mShopList;
    // 新品、促销
    private List<ShopBean> mNewShopList;
    private List<ShopBean> mPromoList;
}

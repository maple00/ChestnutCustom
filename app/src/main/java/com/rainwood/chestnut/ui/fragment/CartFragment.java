package com.rainwood.chestnut.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseFragment;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.CartBean;
import com.rainwood.chestnut.domain.StoreBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.activity.SureOrderActivity;
import com.rainwood.chestnut.ui.adapter.CartAdapter;
import com.rainwood.chestnut.ui.adapter.CartStoreAdapter;
import com.rainwood.chestnut.ui.adapter.ItemCartAdapter;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.widget.MeasureListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/2/29 16:29
 * @Desc: 购物车
 */
public final class CartFragment extends BaseFragment implements View.OnClickListener, OnHttpListener {

    @Override
    protected int initLayout() {
        return R.layout.fragment_cart;
    }

    private MeasureListView contentList;
    private LinearLayout ll_top_store;
    private TextView storeName;
    private ImageView arrow;
    private TextView allMoney;
    private ImageView checked;
    private TextView allChoose;

    private List<CartBean> mList;
    private PopupWindow mPopupWindow;
    private List<StoreBean> mStoreList;

    private int count = 0;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private boolean totalChoose;

    @Override
    protected void initView(View view) {
        // 设置状态栏背景
        StatusBarUtil.setStatusBarDarkTheme(getActivity(), true);
        // top
        ll_top_store = view.findViewById(R.id.ll_top_store);
        ll_top_store.setOnClickListener(this);
        storeName = view.findViewById(R.id.tv_name);
        arrow = view.findViewById(R.id.iv_arrow);
        // content
        contentList = view.findViewById(R.id.mlv_content_list);
        // bottom
        checked = view.findViewById(R.id.iv_checked);
        checked.setOnClickListener(this);
        allChoose = view.findViewById(R.id.tv_all_choose);
        allChoose.setOnClickListener(this);
        allMoney = view.findViewById(R.id.tv_all_money);
        Button settle = view.findViewById(R.id.btn_settle_account);
        settle.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // request
        showLoading("");
        RequestPost.getCartlist("", this);
    }

    @Override
    protected void initData(Context mContext) {
        // storeList
        mStoreList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            StoreBean store = new StoreBean();
            store.setName("衣香丽影旗舰店");
            store.setTotalAmount("25600€");
            mStoreList.add(store);
        }
        // content
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_top_store:             // 门店切换 -- popWindow
                switchStore();
                break;
            case R.id.iv_checked:              // 全选
            case R.id.tv_all_choose:
                totalChoose = !totalChoose;
                if (totalChoose) {
                    setChoose(R.drawable.shape_checked_shape, "取消全选", true);
                } else {
                    setChoose(R.drawable.shape_uncheck_shape, "全选", false);
                }
                Message msg = new Message();
                msg.what = INITIAL_SIZE;
                mHandler.sendMessage(msg);
                break;
            case R.id.btn_settle_account:       // 去结算
                for (CartBean cart : mList) {
                    if (cart.isChecked()) {
                        count++;
                    }
                }
                if (count == 0) {
                    toast("您未选择结算商品");
                    return;
                } else {
                    startActivity(SureOrderActivity.class);
                }
                break;
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
            storeName.setText(mStoreList.get(position).getName());
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
        mPopupWindow.showAsDropDown(ll_top_store, 0, 0);
    }

    /**
     * 设置全选
     *
     * @param res
     * @param choose
     * @param isShow
     */
    private void setChoose(int res, String choose, boolean isShow) {
        checked.setImageResource(res);
        allChoose.setText(choose);
        for (CartBean cartBean : mList) {
            cartBean.setChecked(isShow);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    try {
                        if (mList != null && mList.size() == 1) {
                            checked.setVisibility(View.GONE);
                            allChoose.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Contants.RECORD_POS = 0x101;
                    CartAdapter cartAdapter = new CartAdapter(getActivity(), mList);
                    contentList.setAdapter(cartAdapter);
                    cartAdapter.setOnClickChecked(position -> {      // 选择
                        mList.get(position).setChecked(!mList.get(position).isChecked());

                        Message msg1 = new Message();
                        msg1.what = INITIAL_SIZE;
                        mHandler.sendMessage(msg1);
                    });
                    cartAdapter.setOnClickReducePlus(new ItemCartAdapter.OnClickReducePlus() {
                        @Override
                        public void onClickPlus(int parentPos, int position) {
                            // 文本框的数量
                            setTextNumber(parentPos, position, 1);
                        }

                        @Override
                        public void onClickReduce(int parentPos, int position) {
                            // 文本框的数量
                            setTextNumber(parentPos, position, 0);
                        }

                        @Override
                        public void onWatcherEdit(ItemCartAdapter adapter, int parentPos, int position) {
                            allMoney.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.red30) + " size='"
                                    + FontDisplayUtil.dip2px(getContext(), 12f) + "'>已选"
                                    + adapter.sumPrice() + "件</font>"));
                            Log.d(TAG, "动态变化 --- " + mList.toString());
                        }
                    });
                    //保留两位小数
                    DecimalFormat df = new DecimalFormat("#.00");

                    double totalAmount = 0;
                    for (int i = 0; i < mList.size(); i++) {
                        for (int j = 0; j < mList.get(i).getSkulist().size(); j++) {
                            totalAmount += Double.parseDouble(mList.get(i).getSkulist().get(j).getNum()) *
                                    Double.parseDouble(mList.get(i).getSkulist().get(j).getPrice());
                        }
                    }
                    // 总计
                    allMoney.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.textColor) + " size='"
                            + FontDisplayUtil.dip2px(getContext(), 12f) + "'>总计：</font>"
                            + "<font color=" + getResources().getColor(R.color.red30) + " size='"
                            + FontDisplayUtil.dip2px(getContext(), 18f) + "'>"
                            + df.format(totalAmount) + "€" + "</font>"));
                    break;
            }
        }
    };

    /**
     * 设置文本框的数值
     *
     * @param position
     */
    private void setTextNumber(int parentPos, int position, int flag) {
        int number;
        if ("".equals(mList.get(parentPos).getSkulist().get(position).getNum())
                && TextUtils.isEmpty(mList.get(parentPos).getSkulist().get(position).getNum())) {
            number = 0;
        } else {
            number = Integer.parseInt(mList.get(parentPos).getSkulist().get(position).getNum());
        }
        if (flag == 1) {
            number += 1;
        }
        if (flag == 0) {
            number -= 1;
        }
        if (number <= 0) {
            number = 0;
            toast("已经是可填写的最小数量");
        }
        mList.get(parentPos).getSkulist().get(position).setNum(String.valueOf(number));
        Message msg = new Message();
        msg.what = INITIAL_SIZE;
        mHandler.sendMessage(msg);
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
                // 获取购物车列表
                if (result.url().contains("wxapi/v1/clientGoods.php?type=getCartlist")) {
                    mList = JsonParser.parseJSONArray(CartBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));
                    // initial
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

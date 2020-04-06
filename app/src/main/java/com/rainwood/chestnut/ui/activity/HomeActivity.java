package com.rainwood.chestnut.ui.activity;

import android.util.SparseArray;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.ui.fragment.CartFragment;
import com.rainwood.chestnut.ui.fragment.GoodsFragment;
import com.rainwood.chestnut.ui.fragment.HomeFragment;
import com.rainwood.chestnut.ui.fragment.OrderFragment;
import com.rainwood.chestnut.ui.fragment.PersonalFragment;
import com.rainwood.tools.viewinject.ViewById;

public final class HomeActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @ViewById(R.id.tabs_rg)
    private RadioGroup mTabRadioGroup;
    @ViewById(R.id.rb_home)
    private RadioButton home;
    @ViewById(R.id.rb_goods)
    private RadioButton goods;
    @ViewById(R.id.rb_cart)
    private RadioButton cart;
    @ViewById(R.id.rb_order)
    private RadioButton order;
    @ViewById(R.id.rb_my)
    private RadioButton personal;
    // fragment组
    private SparseArray<Fragment> mFragmentSparseArray;

    @Override
    protected void initView() {
        mFragmentSparseArray = new SparseArray<>();
        mFragmentSparseArray.append(R.id.rb_home, new HomeFragment());      // 首页
        mFragmentSparseArray.append(R.id.rb_goods, new GoodsFragment());    // 商品
        mFragmentSparseArray.append(R.id.rb_cart, new CartFragment());    // 购物车
        mFragmentSparseArray.append(R.id.rb_order, new OrderFragment());    // 我的订单
        mFragmentSparseArray.append(R.id.rb_my, new PersonalFragment());    // 个人中心

        // 压栈式跳转
        int id = getIntent().getIntExtra("id", Contants.HOME_SIZE);
        switch (id) {
            case Contants.GOODS_SIZE:                     // GoodsFragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mFragmentSparseArray.get(R.id.rb_goods), null)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                goods.setChecked(true);
                break;
            case Contants.CART_SIZE:                     // CartFragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mFragmentSparseArray.get(R.id.rb_cart), null)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                cart.setChecked(true);
                break;
            case Contants.ORDER_SIZE:                     // OrderFragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mFragmentSparseArray.get(R.id.rb_order), null)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                order.setChecked(true);
                break;
            case Contants.PERSONAL_SIZE:                     // PersonalFragment
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mFragmentSparseArray.get(R.id.rb_my), null)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                personal.setChecked(true);
                break;
            default:                    // 默认的 -- HomeFragment
                // 默认显示首页
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, mFragmentSparseArray.get(R.id.rb_home), null)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                home.setChecked(true);
                break;
        }
        // 逻辑切换
        mTabRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // 具体的fragment切换逻辑可以根据应用调整，例如使用show()/hide()
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, mFragmentSparseArray.get(checkedId))
                    .commitAllowingStateLoss();
        });
    }

    // 再摁一次退出到桌面

}

package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.AddressBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.NewAddressAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;
import java.util.Map;

import static com.rainwood.chestnut.common.Contants.ADDRESS_REQUEST;

/**
 * @Author: shearson
 * @Time: 2020/3/1 20:58
 * @Desc: 收货地址
 */
public final class ShipAddressActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ship_address;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.mlv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.btn_new_address)
    private Button newAddress;

    // mHandler
    private final int SHIP_ADDRESS_SIZE = 0x101;
    private List<AddressBean> mList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        newAddress.setOnClickListener(this);
        pageTitle.setText("收货地址");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // request
        showLoading("loading");
        RequestPost.getClientAddresslist(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_new_address:
                // toast("新增地址");
                Contants.RECORD_POS = 0x105;
                openActivity(ShipNewAddressActivity.class);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHIP_ADDRESS_SIZE:
                    NewAddressAdapter addressAdapter = new NewAddressAdapter(ShipAddressActivity.this, mList);
                    contentList.setAdapter(addressAdapter);
                    addressAdapter.setOnClickContent(new NewAddressAdapter.OnClickContent() {
                        @Override
                        public void setDefault(int position) {
                            for (AddressBean address : mList) {
                                address.setIsDefault("0");
                            }
                            mList.get(position).setIsDefault("1");
                            // request
                            showLoading("loading");
                            RequestPost.setDefaultAddress(mList.get(position).getId(), ShipAddressActivity.this);
                        }

                        @Override
                        public void editAddress(int position) {
                            // toast("编辑地址");
                            Contants.RECORD_POS = 0x104;
                            Intent intent = new Intent(ShipAddressActivity.this, ShipNewAddressActivity.class);
                            intent.putExtra("addressId", mList.get(position).getId());
                            startActivity(intent);
                        }

                        @Override
                        public void deleteAddress(int position) {
                            // mList.remove(position);
                            // request
                            showLoading("loading");
                            RequestPost.delAddress(mList.get(position).getId(), ShipAddressActivity.this);
                        }

                        @Override
                        public void onClickItem(int position) {
                            if (Contants.ADDRESS_POS_SIZE == 0x101) {           // 购物车选择收货地址
                                Intent intent = new Intent();
                                intent.putExtra("addressId", mList.get(position).getId());
                                intent.putExtra("address", mList.get(position).getAddressMx());
                                intent.putExtra("contact", mList.get(position).getContactName());
                                intent.putExtra("tel", mList.get(position).getContactTel());
                                setResult(ADDRESS_REQUEST, intent);
                                finish();
                            } else {
                                Contants.RECORD_POS = 0x104;
                                Intent intent = new Intent(ShipAddressActivity.this, ShipNewAddressActivity.class);
                                intent.putExtra("addressId", mList.get(position).getId());
                                startActivity(intent);
                            }
                        }
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
        Log.d(TAG, " ---- result ----- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取收货地址列表
                if (result.url().contains("wxapi/v1/clientPerson.php?type=getAddresslist")) {
                    mList = JsonParser.parseJSONArray(AddressBean.class, JsonParser.parseJSONObject(body.get("data")).get("addresslist"));

                    Message msg = new Message();
                    msg.what = SHIP_ADDRESS_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 设为默认地址
                if (result.url().contains("wxapi/v1/clientPerson.php?type=setDefaultAddress")) {
                    toast(body.get("warn"));
                    RequestPost.getClientAddresslist(this);
                }
                // 删除地址
                if (result.url().contains("wxapi/v1/clientPerson.php?type=delAddress")) {
                    toast(body.get("warn"));
                    RequestPost.getClientAddresslist(this);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

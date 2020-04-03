package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseActivity;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.AddressBean;
import com.rainwood.chestnut.domain.ComEditBean;
import com.rainwood.chestnut.domain.ShipNewAddressBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.adapter.ShipNewAddressAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/2 9:16
 * @Desc: 新增收货地址
 */
public final class ShipNewAddressActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mAddressId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ship_new_address;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.mlv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.ll_set_default)
    private LinearLayout setDefault;
    @ViewById(R.id.iv_checked)
    private ImageView checked;
    @ViewById(R.id.btn_save)
    private Button save;

    private ShipNewAddressBean mAddress;
    // mHandler
    private final int INITAL_SIZE = 0x101;
    private List<ComEditBean> mList;
    private String[] titles = {"公司", "税号(P.IVA)", "PEC邮箱/SDI", "收件人", "联系电话", "所在地区", "详细地址"};
    private String[] labels = {"请填写公司名称", "请填写公司税号", "请填写", "请填写收件人姓名", "请填写收件人手机号",
            "请选择", "请填写详细地址，方便快递员投递"};

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        setDefault.setOnClickListener(this);
        save.setOnClickListener(this);
        pageTitle.setText("新增收货地址");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddressId = getIntent().getStringExtra("addressId");
        // request -- 收货地址
        if (mAddress != null && Contants.RECORD_POS == 0x104) {
            showLoading("loading");
            RequestPost.getAddressDetail(mAddressId, this);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mAddress = new ShipNewAddressBean();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            ComEditBean common = new ComEditBean();
            common.setTitle(titles[i]);
            common.setHint(labels[i]);
            if (labels[i].equals("请选择")) {
                common.setArrow(1);
            } else {
                common.setArrow(0);
            }
            mList.add(common);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_set_default:
                // toast("设为默认地址");
                mAddress.setOrDefault(!mAddress.isOrDefault());
                if (mAddress.isOrDefault()) {
                    checked.setImageResource(R.drawable.shape_checked_shape);
                } else {
                    checked.setImageResource(R.drawable.shape_uncheck_shape);
                }
                break;
            case R.id.btn_save:
                toast("保存");
                break;

        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITAL_SIZE:
                    ShipNewAddressAdapter newAddressAdapter = new ShipNewAddressAdapter(ShipNewAddressActivity.this, mList);
                    contentList.setAdapter(newAddressAdapter);
                    newAddressAdapter.setOnClickEdit(new ShipNewAddressAdapter.OnClickEdit() {
                        @Override
                        public void onClickEdit(int position) {
                            toast("点击了：" + position);
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
                // 获取收货地址详情
                if (result.url().contains("wxapi/v1/clientPerson.php?type=getAddressInfo")) {
                    AddressBean address = JsonParser.parseJSONObject(AddressBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    if (address != null) {
                        Log.d(TAG, " =======  address ===== " + address.toString());
                        for (int i = 0; i < mList.size(); i++) {
                            switch (i) {
                                case 0:                 // 公司名称
                                    mList.get(i).setText(address.getCompanyName());
                                    break;
                                case 1:                 // 税号
                                    mList.get(i).setText(address.getCompanyName());
                                    break;
                                case 2:                 // 邮箱
                                    mList.get(i).setText(address.getCompanyName());
                                    break;
                                case 3:                 // 收件人
                                    mList.get(i).setText(address.getContactName());
                                    break;
                                case 4:                 // 联系电话
                                    mList.get(i).setText(address.getContactTel());
                                    break;
                                case 5:                 // 所在地区
                                    mList.get(i).setText(address.getRegion());
                                    break;
                                case 6:                 // 详细地址
                                    mList.get(i).setText(address.getAddressMx());
                                    break;
                            }
                        }
                        Message msg = new Message();
                        msg.what = INITAL_SIZE;
                        mHandler.sendMessage(msg);
                    }

                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}

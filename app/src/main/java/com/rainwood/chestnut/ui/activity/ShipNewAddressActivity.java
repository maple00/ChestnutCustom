package com.rainwood.chestnut.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
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
import com.rainwood.chestnut.okhttp.RequestParams;
import com.rainwood.chestnut.other.BaseDialog;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.ui.dialog.MenuDialog;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import org.json.JSONArray;
import org.json.JSONException;

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
    private AddressBean mAddress1;

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
    //
    @ViewById(R.id.cet_company)
    private ClearEditText company;
    @ViewById(R.id.cet_contact)
    private ClearEditText contact;
    @ViewById(R.id.cet_tel)
    private ClearEditText contactTel;
    @ViewById(R.id.tv_region)
    private TextView region;
    @ViewById(R.id.cet_address_detail)
    private ClearEditText addressDetail;

    private ShipNewAddressBean mAddress = new ShipNewAddressBean();
    private List<String> addressList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        setDefault.setOnClickListener(this);
        save.setOnClickListener(this);
        region.setOnClickListener(this);
        if (Contants.RECORD_POS == 0x104){
            pageTitle.setText("编辑收货地址");
            // request -- 收货地址
            mAddressId = getIntent().getStringExtra("addressId");
            RequestPost.getAddressDetail(mAddressId, this);
        }else {
            pageTitle.setText("新增收货地址");
        }
    }

    @Override
    protected void initData() {
        super.initData();
        // request  -- 获取国家列表
        showLoading("");
        RequestPost.getRegion(this);
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
            case R.id.tv_region:        // 选择国家地区
                new MenuDialog.Builder(this)
                        .setCancel(R.string.common_cancel)
                        .setAutoDismiss(false)
                        .setList(addressList)
                        .setCanceledOnTouchOutside(false)
                        .setListener(new MenuDialog.OnListener<String>() {
                            @Override
                            public void onSelected(BaseDialog dialog, int position, String text) {
                                // toast("位置：" + position + ", 文本：" + text);
                                dialog.dismiss();
                                region.setText(text);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.btn_save:
                if (TextUtils.isEmpty(company.getText())){
                    toast("请输入公司名称");
                    return;
                }
                if (TextUtils.isEmpty(contact.getText())){
                    toast("请输入联系人");
                    return;
                }
                if (TextUtils.isEmpty(contactTel.getText())){
                    toast("请输入联系电话");
                    return;
                }
                if (TextUtils.isEmpty(region.getText())){
                    toast("请选择地区");
                    return;
                }
                if (TextUtils.isEmpty(addressDetail.getText())){
                    toast("请输入详细地址");
                    return;
                }
                // request
                showLoading("");
                if (Contants.RECORD_POS == 0x104){      // 编辑地址
                    RequestPost.addressEdit(mAddressId, company.getText().toString().trim(), contact.getText().toString().trim(),
                            contactTel.getText().toString().trim(), region.getText().toString().trim(), addressDetail.getText().toString().trim(),
                            mAddress.isOrDefault() ? "1" : "0", this);
                }else {             // 新增
                    RequestPost.addressEdit("", company.getText().toString().trim(), contact.getText().toString().trim(),
                            contactTel.getText().toString().trim(), region.getText().toString().trim(), addressDetail.getText().toString().trim(),
                            mAddress.isOrDefault() ? "1" : "0", this);
                }
                break;

        }
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
                // 获取收货地址详情
                if (result.url().contains("wxapi/v1/clientPerson.php?type=getAddressInfo")) {
                    mAddress1 = JsonParser.parseJSONObject(AddressBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));

                    company.setText(mAddress1.getCompanyName());
                    contact.setText(mAddress1.getContactName());
                    contactTel.setText(mAddress1.getContactTel());
                    region.setText(mAddress1.getRegion());
                    addressDetail.setText(mAddress1.getAddressMx());
                }
                // 新增/编辑收货地址
                if (result.url().contains("wxapi/v1/clientPerson.php?type=addressEdit")){
                    toast(body.get("warn"));
                }
                // get 国家地区列表
                if (result.url().contains("wxapi/v1/clientPerson.php?type=getRegion")){
                    JSONArray regionList = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("regionlist"));
                    addressList = new ArrayList<>();
                    for (int i = 0; i < regionList.length(); i++) {
                        try {
                            addressList.add(regionList.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else {
            toast(body.get("warn"));
        }
        dismissLoading();
    }
}

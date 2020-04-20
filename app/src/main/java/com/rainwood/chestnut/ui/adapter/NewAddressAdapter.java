package com.rainwood.chestnut.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.AddressBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/3/1 21:11
 * @Desc: 收货地址
 */
public final class NewAddressAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddressBean> mList;

    public NewAddressAdapter(Context mContext, List<AddressBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public AddressBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ship_address, parent, false);
            holder.tv_default = convertView.findViewById(R.id.tv_default);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            holder.tv_set_default = convertView.findViewById(R.id.tv_set_default);
            holder.tv_edit = convertView.findViewById(R.id.tv_edit);
            holder.iv_checked = convertView.findViewById(R.id.iv_checked);
            holder.tv_delete = convertView.findViewById(R.id.tv_delete);
            holder.iv_edit = convertView.findViewById(R.id.iv_edit);
            holder.iv_delete = convertView.findViewById(R.id.iv_delete);
            holder.ll_set_default = convertView.findViewById(R.id.ll_set_default);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 是否是默认地址
        if ("1".equals(getItem(position).getIsDefault())) {
            holder.tv_default.setVisibility(View.VISIBLE);
            holder.iv_checked.setImageResource(R.drawable.shape_checked_shape);
        } else {
            holder.iv_checked.setImageResource(R.drawable.shape_uncheck_shape);
            holder.tv_default.setVisibility(View.GONE);
        }
        // 如果没有收货地址公司名字则显示联系人姓名
        if (TextUtils.isEmpty(getItem(position).getCompanyName())) {
            holder.tv_name.setText(getItem(position).getContactName());
        } else {
            holder.tv_name.setText(getItem(position).getCompanyName());
        }
        holder.tv_address.setText(getItem(position).getRegion() + "-" + getItem(position).getAddressMx());
        // 设为默认地址
        holder.tv_set_default.setOnClickListener(v -> {
            onClickContent.setDefault(position);
            notifyDataSetChanged();
        });
        holder.iv_checked.setOnClickListener(v -> {
            onClickContent.setDefault(position);
            notifyDataSetChanged();
        });
        // 编辑地址
        holder.ll_set_default.setOnClickListener(v -> onClickContent.onClickItem(position));
        holder.iv_edit.setOnClickListener(v -> onClickContent.editAddress(position));
        holder.tv_edit.setOnClickListener(v -> onClickContent.editAddress(position));
        // 删除地址
        holder.iv_delete.setOnClickListener(v -> {
            onClickContent.deleteAddress(position);
            notifyDataSetChanged();
        });
        holder.tv_delete.setOnClickListener(v -> {
            onClickContent.deleteAddress(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickContent {
        // 设为默认地址
        void setDefault(int position);

        // 编辑地址
        void editAddress(int position);

        // 删除地址
        void deleteAddress(int position);
        // 点击地址
        void onClickItem(int position);
    }

    private OnClickContent onClickContent;

    public void setOnClickContent(OnClickContent onClickContent) {
        this.onClickContent = onClickContent;
    }

    private class ViewHolder {
        private LinearLayout ll_set_default;
        private TextView tv_default, tv_name, tv_address, tv_set_default, tv_edit, tv_delete;
        private ImageView iv_checked, iv_edit, iv_delete;
    }
}

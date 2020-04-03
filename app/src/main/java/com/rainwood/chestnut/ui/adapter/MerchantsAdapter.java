package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.MerchantsBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/3/1 19:17
 * @Desc: 我的商家
 */
public final class MerchantsAdapter extends BaseAdapter {

    private Context mContext;
    private List<MerchantsBean> mList;

    public MerchantsAdapter(Context mContext, List<MerchantsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public MerchantsBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_merchants, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.iv_logo = convertView.findViewById(R.id.iv_logo);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            holder.tv_tel = convertView.findViewById(R.id.tv_tel);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getIco())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                    .into(holder.iv_logo);
        } else {
            Glide.with(convertView).load(getItem(position).getIco())
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                    .into(holder.iv_logo);
        }
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_num.setText(getItem(position).getStoreId());
        holder.tv_tel.setText(getItem(position).getTel());
        holder.tv_desc.setText(getItem(position).getText());
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private LinearLayout ll_item;
        private ImageView iv_logo;
        private TextView tv_name, tv_num, tv_tel, tv_desc;
    }
}

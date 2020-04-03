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

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.CommonListBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/3/1 16:39
 * @Desc: 个人中心 topItem
 */
public final class PersonalTopAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonListBean> mList;

    public PersonalTopAdapter(Context mContext, List<CommonListBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CommonListBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_personal_top_item, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_img.setImageResource(getItem(position).getImgPath());
        holder.tv_name.setText(getItem(position).getName());
        // 没有消息通知的时候,即note为null
        if (TextUtils.isEmpty(getItem(position).getNote()) || "0".equals(getItem(position).getNote())) {
            holder.tv_desc.setVisibility(View.GONE);
        } else {
            holder.tv_desc.setVisibility(View.VISIBLE);
            holder.tv_desc.setText(getItem(position).getNote());
        }
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
        private ImageView iv_img;
        private TextView tv_desc, tv_name;
    }
}

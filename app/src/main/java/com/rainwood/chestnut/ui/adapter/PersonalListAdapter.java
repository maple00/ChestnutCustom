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
 * @Time: 2020/2/29 17:20
 * @Desc: 个人中心 --- 列表 adapter
 */
public final class PersonalListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonListBean> mList;

    public PersonalListAdapter(Context mContext, List<CommonListBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_personal_list, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_note = convertView.findViewById(R.id.tv_note);
            holder.iv_icon = convertView.findViewById(R.id.iv_icon);
            holder.iv_arrow = convertView.findViewById(R.id.iv_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_icon.setImageResource(getItem(position).getImgPath());
        holder.tv_name.setText(getItem(position).getName());
        if (TextUtils.isEmpty(getItem(position).getNote())) {
            holder.tv_note.setVisibility(View.GONE);
        } else {
            holder.tv_note.setText(getItem(position).getNote());
        }
        if (getItem(position).getArrowType() == 0) {     // 隐藏箭头
            holder.iv_arrow.setVisibility(View.GONE);
        } else {         // 显示箭头
            holder.iv_arrow.setVisibility(View.VISIBLE);
            holder.iv_arrow.setImageResource(R.drawable.ic_right_arrow);
        }
        // 模块点击事件
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
        private ImageView iv_icon, iv_arrow;
        private TextView tv_name, tv_note;
    }
}

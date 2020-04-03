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
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.LanguageBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/2 10:49
 * @Desc: 语言设置
 */
public final class LanguageAdapter extends BaseAdapter {

    private Context mContext;
    private List<LanguageBean> mList;

    public LanguageAdapter(Context context, List<LanguageBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public LanguageBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_language_set, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.iv_national = convertView.findViewById(R.id.iv_national);
            holder.iv_checked = convertView.findViewById(R.id.iv_checked);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getImg())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_national);
        } else {
            Glide.with(convertView).load(getItem(position).getImg()).into(holder.iv_national);
        }
        holder.tv_name.setText(getItem(position).getName());
        if (getItem(position).isChecked()) {             // 被选中了
            holder.iv_checked.setImageResource(R.drawable.shape_checked_shape);
        } else {
            holder.iv_checked.setImageResource(R.drawable.shape_uncheck_shape);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> {
            mOnClickItem.onClickItem(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    private class ViewHolder {
        private LinearLayout ll_item;
        private ImageView iv_national, iv_checked;
        private TextView tv_name;
    }
}

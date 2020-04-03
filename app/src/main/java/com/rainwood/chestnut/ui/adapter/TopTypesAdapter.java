package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.PressBean;
import com.rainwood.tools.viewinject.OnClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/3 15:32
 * @Desc: 头部type  adapter
 */
public final class TopTypesAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressBean> mList;

    public TopTypesAdapter(Context context, List<PressBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PressBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_top_types, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_line = convertView.findViewById(R.id.tv_line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(getItem(position).getName());
        if (getItem(position).isChoose()) {
            holder.tv_line.setVisibility(View.VISIBLE);
        } else {
            holder.tv_line.setVisibility(View.INVISIBLE);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> {
            mOnClickItem.onClickItem(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    private class ViewHolder {
        private LinearLayout ll_item;
        private TextView tv_type, tv_line;
    }
}

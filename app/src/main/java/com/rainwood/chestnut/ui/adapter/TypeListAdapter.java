package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.GoodsParentBean;
import com.rainwood.chestnut.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/3 16:10
 * @Desc: types列表
 */
public final class TypeListAdapter extends BaseAdapter {

    private Context mContext;
    private List<GoodsParentBean> mList;

    public TypeListAdapter(Context context, List<GoodsParentBean> list) {
        mContext = context;
        mList = list;
    }

    @Override

    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public GoodsParentBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_types_list, parent, false);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_type.setText(getItem(position).getGoodsTypeOne());
        if (getItem(position).isSelected()) {
            holder.tv_type.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.tv_type.setBackground(mContext.getResources().getDrawable(R.drawable.shape_radius_red_full_14));
            holder.tv_type.setPadding(20, 10, 20, 10);
        } else {
            holder.tv_type.setTextColor(mContext.getResources().getColor(R.color.textColor));
            holder.tv_type.setBackground(mContext.getResources().getDrawable(R.drawable.selector_transparent));
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> {
            mOnClickText.onClickText(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickText{
        void onClickText(int position);
    }

    private OnClickText mOnClickText;

    public void setOnClickText(OnClickText onClickText) {
        mOnClickText = onClickText;
    }

    private class ViewHolder {
        private LinearLayout ll_item;
        private TextView tv_type;
    }
}

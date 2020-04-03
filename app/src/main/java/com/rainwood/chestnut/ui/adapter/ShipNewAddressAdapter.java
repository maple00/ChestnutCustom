package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.ComEditBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/2 10:08
 * @Desc: 新增收货地址
 */
public final class ShipNewAddressAdapter extends BaseAdapter {

    private Context mContext;
    private List<ComEditBean> mList;

    public ShipNewAddressAdapter(Context context, List<ComEditBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ComEditBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_ship_new_address, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.et_hint = convertView.findViewById(R.id.et_hint);
            holder.iv_arrow = convertView.findViewById(R.id.iv_arrow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        if (TextUtils.isEmpty(getItem(position).getText())) {
            holder.et_hint.setHint(getItem(position).getHint());
        } else {
            holder.et_hint.setText(getItem(position).getText());
        }
        if (getItem(position).getArrow() == 0) {                // 不显示箭头
            holder.iv_arrow.setVisibility(View.GONE);
            holder.et_hint.setFocusable(true);
            holder.et_hint.setFocusableInTouchMode(true);
        } else {
            holder.iv_arrow.setVisibility(View.VISIBLE);
            // 设置点击事件
            holder.et_hint.setFocusable(false);
            holder.et_hint.setFocusableInTouchMode(false);
            holder.et_hint.setOnClickListener(v -> {
                mOnClickEdit.onClickEdit(position);
                notifyDataSetChanged();
            });
        }
        return convertView;
    }

    public interface OnClickEdit {
        void onClickEdit(int position);
    }

    private OnClickEdit mOnClickEdit;

    public void setOnClickEdit(OnClickEdit onClickEdit) {
        mOnClickEdit = onClickEdit;
    }

    private class ViewHolder {
        private TextView tv_title;
        private EditText et_hint;
        private ImageView iv_arrow;
    }
}

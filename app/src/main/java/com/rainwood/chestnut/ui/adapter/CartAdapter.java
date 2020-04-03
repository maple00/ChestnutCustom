package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.common.Contants;
import com.rainwood.chestnut.domain.CartBean;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/2 17:44
 * @Desc: 购物车 Adapter
 */
public final class CartAdapter extends BaseAdapter {

    private Context mContext;
    private List<CartBean> mList;

    public CartAdapter(Context context, List<CartBean> list) {
        mContext = context;
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CartBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cart_list, parent, false);
            holder.iv_checked = convertView.findViewById(R.id.iv_checked);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_number = convertView.findViewById(R.id.tv_number);
            holder.mlv_special_list = convertView.findViewById(R.id.mlv_special_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getIco())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_img);
        } else {
            Glide.with(convertView).load(getItem(position).getIco()).into(holder.iv_img);
        }
        if (getCount() > 1 && Contants.RECORD_POS == 0x101) { // 当有多个商品的时候  -- 并且是购物车中
            holder.iv_checked.setVisibility(View.VISIBLE);
            holder.iv_checked.setOnClickListener(v -> {
                mOnClickChecked.onClickChecked(position);
                notifyDataSetChanged();
            });
            if (getItem(position).isChecked()) {         // 被选中了
                holder.iv_checked.setImageResource(R.drawable.shape_checked_shape);
            } else {
                holder.iv_checked.setImageResource(R.drawable.shape_uncheck_shape);
            }
        } else {        // 只有一个商品的时候
            holder.iv_checked.setVisibility(View.GONE);
        }
        holder.tv_name.setText(getItem(position).getGoodsName());
        holder.tv_number.setText(getItem(position).getModel());
        // subAdapter
        ItemCartAdapter itemCartAdapter = new ItemCartAdapter(mContext, getItem(position).getSkulist());
        holder.mlv_special_list.setAdapter(itemCartAdapter);
        itemCartAdapter.setOnClickReducePlus(position, mOnClickReducePlus);
        return convertView;
    }

    private ItemCartAdapter.OnClickReducePlus mOnClickReducePlus;

    public void setOnClickReducePlus(ItemCartAdapter.OnClickReducePlus onClickReducePlus) {
        mOnClickReducePlus = onClickReducePlus;
    }

    public interface OnClickChecked {
        void onClickChecked(int position);
    }

    private OnClickChecked mOnClickChecked;

    public void setOnClickChecked(OnClickChecked onClickChecked) {
        mOnClickChecked = onClickChecked;
    }

    private class ViewHolder {
        private ImageView iv_checked, iv_img;
        private TextView tv_name, tv_number;
        private MeasureListView mlv_special_list;
    }
}

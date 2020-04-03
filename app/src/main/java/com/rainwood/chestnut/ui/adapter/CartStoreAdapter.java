package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.StoreBean;
import com.rainwood.tools.viewinject.OnClick;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/3 9:58
 * @Desc: 购物车 -- 切换门店
 */
public final class CartStoreAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoreBean> mList;

    public CartStoreAdapter(Context context, List<StoreBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public StoreBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cart_store_list, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_total_amount = convertView.findViewById(R.id.tv_total_amount);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_total_amount.setText(getItem(position).getTotalAmount());
        holder.ll_item.setOnClickListener(v -> mOnClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    private class ViewHolder{
        private LinearLayout ll_item;
        private TextView tv_name, tv_total_amount;
    }
}

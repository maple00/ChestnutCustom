package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.StoreBean;
import com.rainwood.chestnut.domain.TitleAndHintBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/3 9:58
 * @Desc: 确认订单 -- 汇总信息
 */
public final class OrderSummaryAdapter extends BaseAdapter {

    private Context mContext;
    private List<TitleAndHintBean> mList;

    public OrderSummaryAdapter(Context context, List<TitleAndHintBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public TitleAndHintBean getItem(int position) {
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
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(getItem(position).getTitle());
        holder.tv_total_amount.setText(getItem(position).getLabel());
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_name, tv_total_amount;
    }
}

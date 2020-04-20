package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.SpecialBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/7 10:50
 * @Desc: 确认订单的规格列表
 */
public final class SureOrderSpecialAdapter extends BaseAdapter {

    private Context mContext;
    private List<SpecialBean> mList;

    public SureOrderSpecialAdapter(Context context, List<SpecialBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SpecialBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_special_goods, parent, false);
            holder.tv_special = convertView.findViewById(R.id.tv_special);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            holder.tv_total = convertView.findViewById(R.id.tv_total);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ll_item.setBackground(mContext.getDrawable(R.color.white));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.ll_item.setBackground(mContext.getDrawable(R.color.orange10));
            }
        }
        if (TextUtils.isEmpty(getItem(position).getSkuName())){
            holder.tv_special.setText(getItem(position).getGoodsSkuName());
        }else {
            holder.tv_special.setText(getItem(position).getSkuName());
        }
        holder.tv_price.setText(getItem(position).getPrice());
        holder.tv_num.setText(getItem(position).getNum());
        if (TextUtils.isEmpty(getItem(position).getMoney())) {
            holder.tv_total.setText(getItem(position).getTotalMoney());
        } else {
            holder.tv_total.setText(getItem(position).getMoney());
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_special, tv_price, tv_num, tv_total;
        private LinearLayout ll_item;
    }
}

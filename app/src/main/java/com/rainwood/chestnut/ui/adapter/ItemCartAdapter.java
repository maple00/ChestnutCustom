package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.SpecialBean;
import com.rainwood.tools.common.FontDisplayUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/3 9:05
 * @Desc: 购物车中的规格列表
 */
public final class ItemCartAdapter extends BaseAdapter {

    private Context mContext;
    private List<SpecialBean> mList;
    private final int VALUE = 30000;
    private int parentPos;

    public ItemCartAdapter(Context context, List<SpecialBean> list) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_item_cart_list, parent, false);
            holder.tv_special = convertView.findViewById(R.id.tv_special);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_total_amount = convertView.findViewById(R.id.tv_total_amount);
            holder.tv_input = convertView.findViewById(R.id.tv_input);
            holder.iv_reduce = convertView.findViewById(R.id.iv_reduce);
            holder.iv_plus = convertView.findViewById(R.id.iv_plus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_special.setText(getItem(position).getSkuName());
        holder.tv_price.setText(getItem(position).getPrice());
        holder.tv_input.setText(getItem(position).getNum());

        holder.tv_total_amount.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='"
                + FontDisplayUtil.dip2px(mContext, 12f) + "'>合计：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor) + " size='"
                + FontDisplayUtil.dip2px(mContext, 14f) + "'>"
                + new DecimalFormat("#.00").format(Double.parseDouble(getItem(position).getNum())
                * Double.parseDouble(getItem(position).getPrice())) + "</font>"));
        // 点击事件
        holder.iv_plus.setOnClickListener(v -> mOnClickReducePlus.onClickPlus(parentPos, position));
        holder.iv_reduce.setOnClickListener(v -> mOnClickReducePlus.onClickReduce(parentPos, position));
        return convertView;
    }

    public interface OnClickReducePlus {
        // 增加
        void onClickPlus(int parentPos, int position);

        // 减少
        void onClickReduce(int parentPos, int position);

        // 监听获取的数量
        void onWatcherEdit(ItemCartAdapter adapter, int parentPos, int position);
    }

    private OnClickReducePlus mOnClickReducePlus;

    public void setOnClickReducePlus(int parentPos, OnClickReducePlus onClickReducePlus) {
        mOnClickReducePlus = onClickReducePlus;
        this.parentPos = parentPos;
    }

    public void setOnClickReducePlus(OnClickReducePlus onClickReducePlus) {
        mOnClickReducePlus = onClickReducePlus;
    }

    public int sumPrice() {
        int price = 0;
        for (int i = 0; i < getCount(); i++) {
            String itemPrice = getItem(i).getPrice();
            String itemNumber = getItem(i).getNum();
            itemPrice = itemPrice == null || itemPrice.length() == 0 ? "0" : itemPrice;
            itemNumber = itemNumber == null || itemNumber.length() == 0 ? "0" : itemNumber;
            price += Integer.parseInt(itemPrice) * Integer.parseInt(itemNumber);
        }
        return price;
    }

    private class ViewHolder {
        private TextView tv_special, tv_price, tv_total_amount, tv_input;
        private ImageView iv_reduce, iv_plus;
    }
}

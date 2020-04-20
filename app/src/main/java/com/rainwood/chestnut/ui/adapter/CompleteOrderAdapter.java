package com.rainwood.chestnut.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.SpecialBean;
import com.rainwood.tools.common.FontDisplayUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/7 14:34
 * @Desc: 已完成订单adapter
 */
public final class CompleteOrderAdapter extends BaseAdapter {

    private Context mContext;
    private List<SpecialBean> mList;

    public CompleteOrderAdapter(Context context, List<SpecialBean> list) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_complete_order, parent, false);
            holder.tv_special = convertView.findViewById(R.id.tv_special);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            holder.tv_refunds = convertView.findViewById(R.id.tv_refunds);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getSkuName())) {
            holder.tv_special.setText(getItem(position).getGoodsSkuName());
        } else {
            holder.tv_special.setText(getItem(position).getSkuName());
        }
        holder.tv_price.setText(getItem(position).getPrice());
        holder.tv_desc.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='"
                + FontDisplayUtil.dip2px(mContext, 12f) + "'>数量：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor) + ">" + getItem(position).getNum() + "</font>"
                + "\t\t<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='"
                + FontDisplayUtil.dip2px(mContext, 12f) + "'>合计：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor) + ">" + getItem(position).getNum() + "</font>"));
        if ("complete".equals(getItem(position).getRefundState())) {
            holder.tv_refunds.setClickable(false);
        } else if ("applying".equals(getItem(position).getRefundState())) {
            holder.tv_refunds.setClickable(true);
        } else {
            holder.tv_refunds.setClickable(true);
        }
        holder.tv_refunds.setText(getItem(position).getRefundText());
        // 点击事件
        holder.tv_refunds.setOnClickListener(v -> mOnClickRefunds.onClickRefunds(position));
        return convertView;
    }

    public interface OnClickRefunds {
        // 退货
        void onClickRefunds(int position);
    }

    private OnClickRefunds mOnClickRefunds;

    public void setOnClickRefunds(OnClickRefunds onClickRefunds) {
        mOnClickRefunds = onClickRefunds;
    }

    private class ViewHolder {
        private TextView tv_special, tv_price, tv_desc, tv_refunds;
    }
}

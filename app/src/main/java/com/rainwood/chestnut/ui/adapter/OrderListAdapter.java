package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.OrdersBean;
import com.rainwood.tools.common.FontDisplayUtil;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/4 14:29
 * @Desc: 订单列表
 */
public final class OrderListAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrdersBean> mList;

    public OrderListAdapter(Context context, List<OrdersBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public OrdersBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_list, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.tv_order_num = convertView.findViewById(R.id.tv_order_num);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_shop_num = convertView.findViewById(R.id.tv_shop_num);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_summary = convertView.findViewById(R.id.tv_summary);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.btn_confirm_order = convertView.findViewById(R.id.btn_confirm_order);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getIco())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_img);
        } else {
            Glide.with(convertView).load(getItem(position).getIco()).into(holder.iv_img);
        }
        holder.tv_order_num.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.textColor)
                + " size='" + FontDisplayUtil.dip2px(mContext, 12f) + "'>订单编号：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor)
                + " size='" + FontDisplayUtil.dip2px(mContext, 12f) + "'>"
                + getItem(position).getId() + "</font>"));
        holder.tv_status.setText(getItem(position).getWorkFlow());
        holder.tv_name.setText(getItem(position).getGoodsName());
        holder.tv_shop_num.setText(getItem(position).getModel());
        holder.tv_time.setText(getItem(position).getOrderTime());
        holder.tv_summary.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.textColor) + " size='"
                + FontDisplayUtil.dip2px(mContext, 11f) + "'>共" + getItem(position).getNum() + "件，合计</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.red30)
                + " size='" + FontDisplayUtil.dip2px(mContext, 14f) + "'><b>"
                + getItem(position).getMoney() + "€</b></font>"));
        // 待收货订单显示收货
        if (getItem(position).getWorkFlow().equals("待收货")) {
            holder.btn_confirm_order.setVisibility(View.VISIBLE);
        } else {
            holder.btn_confirm_order.setVisibility(View.GONE);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> mOnClickItem.onClickItem(position));
        holder.btn_confirm_order.setOnClickListener(v -> {
            mOnClickItem.onClickConfirm(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickItem {
        // 查看详情
        void onClickItem(int position);

        // 确认收货
        void onClickConfirm(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    private class ViewHolder {
        private LinearLayout ll_item;
        private TextView tv_order_num, tv_status, tv_name, tv_shop_num, tv_time, tv_summary;
        private ImageView iv_img;
        private Button btn_confirm_order;
    }
}

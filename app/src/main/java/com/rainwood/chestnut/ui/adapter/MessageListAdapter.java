package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.MessageBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/3/1 18:30
 * @Desc: 消息列表
 */
public final class MessageListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MessageBean> mList;

    public MessageListAdapter(Context mContext, List<MessageBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public MessageBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message_list, parent, false);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            holder.tv_detail = convertView.findViewById(R.id.tv_detail);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_content.setText(getItem(position).getText());
        holder.tv_time.setText(getItem(position).getTime());
        if ("1".equals(getItem(position).getState())) {        // 已读
            holder.tv_content.setTextColor(mContext.getResources().getColor(R.color.fontColor));
        } else {
            holder.tv_content.setTextColor(mContext.getResources().getColor(R.color.textColor));
        }
        // 是否省略了
        holder.tv_content.post(() -> {
            Layout contentLayout = holder.tv_content.getLayout();
            if (contentLayout != null) {
                int lines = contentLayout.getLineCount();
                if (lines > 0) {
                    if (contentLayout.getEllipsisCount(lines - 1) > 0) {    // 省略了，即超出了4行
                        holder.tv_detail.setVisibility(View.VISIBLE);
                    }else {
                        holder.tv_detail.setVisibility(View.GONE);
                    }
                }
            }
        });
        // 点击事件
        holder.tv_detail.setOnClickListener(v -> onClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private TextView tv_content, tv_detail, tv_time;
    }

}

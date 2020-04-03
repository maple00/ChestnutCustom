package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/4 14:13
 * @Desc: top -- 横向滑动 --- pressBean
 */
public final class HorSlidingAdapter extends RecyclerView.Adapter<HorSlidingAdapter.ViewHolder> {

    private Context mContext;
    private List<PressBean> mList;

    public HorSlidingAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<PressBean> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_top_types, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_type.setText(mList.get(position).getName());
        if (mList.get(position).isChoose()){
            holder.tv_line.setVisibility(View.VISIBLE);
        }else {
            holder.tv_line.setVisibility(View.INVISIBLE);
        }

        holder.ll_item.setOnClickListener(v -> {
            mOnClickItem.onClickItem(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_item;
        TextView tv_type, tv_line;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ll_item = itemView.findViewById(R.id.ll_item);
            tv_line = itemView.findViewById(R.id.tv_line);
            tv_type = itemView.findViewById(R.id.tv_type);
        }
    }
}

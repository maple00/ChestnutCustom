package com.rainwood.chestnut.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.NewShopsBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/3 17:39
 * @Desc: 商品瀑布式布局
 */
public final class ShopWaterFallAdapter extends RecyclerView.Adapter<ShopWaterFallAdapter.ViewHolder> {

    private Context mContext;
    private List<NewShopsBean> mList;

    public ShopWaterFallAdapter(Context context) {
        mContext = context;
    }

    public void setList(List<NewShopsBean> list) {
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_img_waterfall, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 赋值
        if (TextUtils.isEmpty(mList.get(position).getIco())) {
            Glide.with(mContext).load(R.drawable.icon_loadding_fail)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(4))
                            .override(175, 175)).into(holder.iv_img);
        } else {
            Glide.with(mContext).load(mList.get(position).getIco())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(4))
                            .override(175, 175)).into(holder.iv_img);
        }
        holder.tv_name.setText(mList.get(position).getName());
        holder.tv_num.setText(mList.get(position).getModel());
        holder.tv_number.setText(mList.get(position).getCartNum());
        // 是否是新商品
        if ("1".equals(mList.get(position).getNewGoods())) {
            holder.iv_label.setVisibility(View.VISIBLE);
        }else {
            holder.iv_label.setVisibility(View.GONE);
        }
        // 如果最大价格和最小价格相等
        if (mList.get(position).getMinPrice().equals(mList.get(position).getMaxPrice())) {
            holder.tv_price.setText(mList.get(position).getMinPrice());
        } else {
            holder.tv_price.setText(mList.get(position).getMinPrice() + " - " + mList.get(position).getMaxPrice());
        }
        //
        if (TextUtils.isEmpty(mList.get(position).getMaxPrice())) {
            holder.iv_label.setVisibility(View.GONE);
        } else {
            holder.iv_label.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(mList.get(position).getCartNum())) {
            holder.tv_number.setVisibility(View.GONE);
        } else {
            holder.tv_number.setVisibility(View.VISIBLE);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> mOnClickItem.onClickItem(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout ll_item;
        ImageView iv_img, iv_label;
        TextView tv_number, tv_name, tv_num, tv_price;

        public ViewHolder(@NonNull View view) {
            super(view);
            ll_item = view.findViewById(R.id.ll_item);
            iv_img = view.findViewById(R.id.iv_img);
            iv_label = view.findViewById(R.id.iv_label);
            tv_number = view.findViewById(R.id.tv_number);
            tv_name = view.findViewById(R.id.tv_name);
            tv_num = view.findViewById(R.id.tv_num);
            tv_price = view.findViewById(R.id.tv_price);
        }
    }
}

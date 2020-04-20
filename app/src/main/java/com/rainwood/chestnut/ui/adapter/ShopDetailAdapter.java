package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.ParamBean;
import com.rainwood.chestnut.domain.ParamsBean;
import com.rainwood.tools.toast.ToastUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/4 10:57
 * @Desc: 商品详情 ---- 参数列表
 */
public final class ShopDetailAdapter extends BaseAdapter {

    private final int VALUE = 30000;
    private Context mContext;
    private List<ParamBean> mList;

    public ShopDetailAdapter(Context context, List<ParamBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ParamBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_detail_params, parent, false);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.iv_plus = convertView.findViewById(R.id.iv_plus);
            holder.iv_reduce = convertView.findViewById(R.id.iv_reduce);
            holder.et_input = convertView.findViewById(R.id.et_input);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.et_input.setTag(position + VALUE);
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_price.setText(getItem(position).getPrice());
        if (TextUtils.isEmpty(getItem(position).getNum())
                || Integer.parseInt(getItem(position).getNum()) <= 0) {
            holder.et_input.setText("0");
            holder.iv_reduce.setClickable(false);
        } else {
            holder.et_input.setText(getItem(position).getNum());
        }
        // 点击事件
        holder.iv_reduce.setOnClickListener(v -> {
            mOnClickEdit.onClickEditReduce(position);
            notifyDataSetChanged();
        });
        holder.iv_plus.setOnClickListener(v -> {
            mOnClickEdit.onClickEditPlus(position);
            notifyDataSetChanged();
        });
        // 输入框焦点的监听  holder.et_input
        holder.et_input.setFocusable(true);
        holder.et_input.addTextChangedListener(new TextChangeListener(this, holder, position, holder.et_input));
        return convertView;
    }

    public interface OnClickEdit {
        // 减少
        void onClickEditReduce(int position);

        // 增加
        void onClickEditPlus(int position);

        // 监听获取的数量
        void onWatcherEdit(ShopDetailAdapter adapter, int position);
    }

    private OnClickEdit mOnClickEdit;

    public void setOnClickEdit(OnClickEdit onClickEdit) {
        mOnClickEdit = onClickEdit;
    }

    private class ViewHolder {
        private TextView tv_name, tv_price;
        private ImageView iv_reduce, iv_plus;
        private EditText et_input;
    }

    private class TextChangeListener implements TextWatcher {

        private ShopDetailAdapter adapter;
        private int position;
        private ViewHolder holder;

        public TextChangeListener(ShopDetailAdapter adapter, ViewHolder holder, int position, EditText editText) {
            this.adapter = adapter;
            this.holder = holder;
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int tag = (int) holder.et_input.getTag();
            if (tag == position + VALUE) {
                adapter.getItem(position).setNum(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            //回调把焦点抢了
            mOnClickEdit.onWatcherEdit(adapter, position);
        }
    }


    /**
     * 计算总个数
     *
     * @return
     */
    public int sumCount() {
        int count = 0;
        try {
            for (int i = 0; i < getCount(); i++) {
                String number = getItem(i).getNum();
                number = number == null || number.length() == 0 ? "0" : number;
                count += Integer.parseInt(number);
            }
        }catch (Exception e){
            ToastUtils.show("请输入合理数量");
            count = 0;
        }
        return count;
    }

    /**
     * 计算总价格
     *
     * @return
     */
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


}

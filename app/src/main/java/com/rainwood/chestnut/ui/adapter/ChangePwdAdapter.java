package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.ComEditBean;
import com.rainwood.chestnut.utils.CountDownTimerUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/2 11:17
 * @Desc: 修改密码
 */
public final class ChangePwdAdapter extends BaseAdapter {

    private Context mContext;
    private List<ComEditBean> mList;

    public ChangePwdAdapter(Context context, List<ComEditBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ComEditBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_change_password, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.et_hint = convertView.findViewById(R.id.et_hint);
            holder.tv_send_code = convertView.findViewById(R.id.tv_send_code);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 输入限制
        if (position == 0) {
            holder.et_hint.setInputType(InputType.TYPE_CLASS_PHONE);
        } else if (position == 1) {
            holder.et_hint.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            holder.et_hint.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        holder.tv_title.setText(getItem(position).getTitle());
        if (TextUtils.isEmpty(getItem(position).getText())) {
            holder.et_hint.setHint(getItem(position).getHint());
        } else {
            holder.et_hint.setText(getItem(position).getText());
        }
        if (position == 0) {
            holder.tv_send_code.setVisibility(View.VISIBLE);
            // 发送验证码
            holder.tv_send_code.setOnClickListener(v -> {
                CountDownTimerUtils.initCountDownTimer(60, holder.tv_send_code, "可重新发送验证码", R.color.fontColor, R.color.red30);
                holder.tv_send_code.setTextColor(mContext.getResources().getColor(R.color.fontColor));
                holder.tv_send_code.setClickable(false);
                CountDownTimerUtils.countDownTimer.start();
                notifyDataSetChanged();
            });
        } else {
            holder.tv_send_code.setVisibility(View.GONE);
        }
        return convertView;
    }

    public interface OnClickCode {
        // 发送验证码
        void onClickCode(int position);
    }

    private OnClickCode mOnClickCode;

    public void setOnClickCode(OnClickCode onClickCode) {
        mOnClickCode = onClickCode;
    }

    private class ViewHolder {
        private TextView tv_title;
        private EditText et_hint;
        private TextView tv_send_code;
    }
}

package com.rainwood.chestnut.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.domain.ComEditBean;
import com.rainwood.chestnut.json.JsonParser;
import com.rainwood.chestnut.okhttp.HttpResponse;
import com.rainwood.chestnut.okhttp.OnHttpListener;
import com.rainwood.chestnut.request.RequestPost;
import com.rainwood.chestnut.utils.CountDownTimerUtils;
import com.rainwood.tools.toast.ToastUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/3/2 11:17
 * @Desc: 修改密码
 */
public final class ChangePwdAdapter extends BaseAdapter implements OnHttpListener {

    private Context mContext;
    private List<ComEditBean> mList;
    private final int VALUE = 30000;

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
            holder.et_hint.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        }
        holder.tv_title.setText(getItem(position).getTitle());
        holder.et_hint.setTag(position + VALUE);
        if (TextUtils.isEmpty(getItem(position).getText())) {
            holder.et_hint.setHint(getItem(position).getHint());
        } else {
            holder.et_hint.setText(getItem(position).getText());
        }
        if (position == 0) {
            holder.tv_send_code.setVisibility(View.VISIBLE);
            // 发送验证码
            holder.tv_send_code.setOnClickListener(v -> {
                CountDownTimerUtils.initCountDownTimer(60, holder.tv_send_code, "可重新发送验证码",
                        mContext.getResources().getColor(R.color.fontColor), mContext.getResources().getColor(R.color.red30));
                holder.tv_send_code.setTextColor(mContext.getResources().getColor(R.color.fontColor));
                holder.tv_send_code.setClickable(false);
                CountDownTimerUtils.countDownTimer.start();
                // request -- 发送验证码
                RequestPost.getVerifyCode(getItem(position).getText().toLowerCase().trim(), "", this);
                notifyDataSetChanged();
            });
        } else {
            holder.tv_send_code.setVisibility(View.GONE);
        }
        // 设置监听回调
        holder.et_hint.setFocusable(true);
        holder.et_hint.addTextChangedListener(new TextChangeListener(this, position, holder));
        return convertView;
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取验证码
                if (result.url().contains("wxapi/v1/clientLogin.php?type=getCode")) {
                    ToastUtils.show(body.get("warn"));
                }
            }else {
                ToastUtils.show(body.get("warn"));
            }
        }
    }

    public interface OnTextChangeListener {
        void onTextChange(ChangePwdAdapter adapter, int position);
    }

    private OnTextChangeListener mOnTextChangeListener;

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        mOnTextChangeListener = onTextChangeListener;
    }

    private class TextChangeListener implements TextWatcher {

        private ChangePwdAdapter adapter;
        private int position;
        private ViewHolder holder;

        public TextChangeListener(ChangePwdAdapter adapter, int position, ViewHolder holder) {
            this.adapter = adapter;
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int tag = (int) holder.et_hint.getTag();
            if (tag == position + VALUE) {
                adapter.getItem(position).setText(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            mOnTextChangeListener.onTextChange(adapter, position);
        }
    }

    private class ViewHolder {
        private TextView tv_title;
        private EditText et_hint;
        private TextView tv_send_code;
    }
}

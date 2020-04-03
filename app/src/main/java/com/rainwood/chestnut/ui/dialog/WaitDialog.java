package com.rainwood.chestnut.ui.dialog;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;

import com.rainwood.chestnut.R;
import com.rainwood.chestnut.base.BaseDialogFragment;
import com.rainwood.chestnut.other.BaseDialog;

/**
 * @Author: shearson
 * @time: 2019/12/3 10:37
 * @des: 等待加载对话框 重写了进度条控件
 */
public final class WaitDialog {

    public static final class Builder
            extends BaseDialogFragment.Builder<Builder> {

        private final TextView mMessageView;

        public Builder(FragmentActivity activity) {
            super(activity);
            setContentView(R.layout.dialog_wait);
            setAnimStyle(BaseDialog.AnimStyle.TOAST);
            setBackgroundDimEnabled(false);
            setCancelable(false);

            mMessageView = findViewById(R.id.tv_wait_message);
        }

        public Builder setMessage(@StringRes int id) {
            return setMessage(getString(id));
        }

        public Builder setMessage(CharSequence text) {
            mMessageView.setText(text);
            mMessageView.setVisibility(text == null ? View.GONE : View.VISIBLE);
            return this;
        }
    }
}

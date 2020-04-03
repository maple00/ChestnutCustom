package com.rainwood.chestnut.utils;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.widget.TextView;

import com.rainwood.chestnut.R;

import java.util.Objects;

/**
 * @Author: sxs797
 * @Date: 2020/1/2 17:36
 * @Desc: Dialog 工具类
 */
public final class DialogUtils {

    private static Dialog dialog;
    private static CharSequence tips;

    public DialogUtils(Context context) {
        dialog = new Dialog(context, R.style.progress_dialog);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_dialog_loadding);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        TextView tvMsg = dialog.findViewById(R.id.tv_wait_message);
        tvMsg.setText(tips);
    }

    public void showDialog(CharSequence tip) {
        tips = tip;
        dialog.show();
    }


    public void dismissDialog() {
        dialog.dismiss();
    }
}

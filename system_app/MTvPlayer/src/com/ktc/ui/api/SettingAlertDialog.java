package com.ktc.ui.api;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;

import com.horion.tv.R;

import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

public class SettingAlertDialog extends Dialog implements SkyDialogView.OnDialogOnKeyListener {
    SkyDialogView dialogView;
    OnDialogOnKeyListener listener;

    public interface OnDialogOnKeyListener {
        boolean onDialogOnKeyEvent(int i, KeyEvent keyEvent);

        void onFirstBtnOnClickEvent();

        void onSecondBtnOnClickEvent();
    }

    public SettingAlertDialog(Context context) {
        super(context, R.style.Theme_Setting_dialog);
        getWindow().setGravity(17);
        getWindow().setType(TYPE_SYSTEM_ALERT);
        this.dialogView = new SkyDialogView(context, true);
        setContentView(this.dialogView);
    }

    public void dismiss() {
        this.dialogView.dismiss();
        super.dismiss();
    }

    public boolean onDialogOnKeyEvent(int i, KeyEvent keyEvent) {
        return this.listener.onDialogOnKeyEvent(i, keyEvent);
    }

    public void onFirstBtnOnClickEvent() {
        this.listener.onFirstBtnOnClickEvent();
    }

    public void onSecondBtnOnClickEvent() {
        this.listener.onSecondBtnOnClickEvent();
    }

    public void setBtnString(String str, String str2) {
        this.dialogView.setBtnString(str, str2);
    }

    public void setListener(OnDialogOnKeyListener onDialogOnKeyListener) {
        this.listener = onDialogOnKeyListener;
        this.dialogView.setOnDialogOnKeyListener(this);
    }

    public void setTipsString(String str, String str2) {
        this.dialogView.setTipsString(str, str2);
    }

    public void show() {
        super.show();
        this.dialogView.show();
    }
}

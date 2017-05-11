package com.ktc.panasonichome;

import com.ktc.panasonichome.view.api.SkyDialogView;
import com.ktc.panasonichome.view.api.SkyDialogView.OnDialogOnKeyListener;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.ViewGroup;

public class SkyRemoveAppDialog implements OnDialogOnKeyListener {
    private String btn1;
    private String btn2;
    private OnDialogOnKeyExtraListener dialogExtraListener;
    private OnDialogOnKeyListener dialogListener;
    private boolean isInitted;
    private boolean isShowed;
    private Context mContext;
    private SkyDialogView mSkyDialogView;
    private ViewGroup rootView;
    private String showType;
    private String tips1;
    private String tips2;
    private Handler uiHandler;

    public interface OnDialogOnKeyExtraListener {
        void onBtnOnClickEndEvent();

        void onBtnOnClickStartEvent();
    }

    public SkyRemoveAppDialog() {
        this.dialogListener = null;
        this.mSkyDialogView = null;
        this.rootView = null;
        this.tips1 = "";
        this.tips2 = "";
        this.btn1 = "";
        this.btn2 = "";
        this.isShowed = false;
        this.showType = null;
        this.isInitted = false;
        this.dialogExtraListener = null;
        this.uiHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        SkyRemoveAppDialog.this.mSkyDialogViewShow();
                        return;
                    case 2:
                        SkyRemoveAppDialog.this.mSkyDialogViewHide();
                        return;
                    default:
                        return;
                }
            }
        };
        this.isShowed = false;
        this.isInitted = false;
    }

    private void mSkyDialogViewHide() {
        if (this.mSkyDialogView != null) {
            this.isShowed = false;
            this.mSkyDialogView.dismiss();
        }
    }

    private void mSkyDialogViewShow() {
        if (this.mSkyDialogView == null) {
            this.mSkyDialogView = new SkyDialogView(this.mContext, true);
            this.mSkyDialogView.setOnDialogOnKeyListener(this);
            this.rootView.addView(this.mSkyDialogView);
        }
        ///if (!(this.tips1 == null || this.tips2 == null)) {
            this.mSkyDialogView.setTipsString(this.tips1, this.tips2);
       /// }
        if (!(this.btn1 == null || this.btn2 == null)) {
            this.mSkyDialogView.setBtnString(this.btn1, this.btn2);
        }
        this.isShowed = true;
        this.mSkyDialogView.show();
    }

    public String getShowType() {
        return this.showType;
    }

    public void hideTvDialog() {
        if (!this.isInitted) {
            return;
        }
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mSkyDialogViewHide();
            return;
        }
        Message message = new Message();
        message.what = 2;
        this.uiHandler.sendMessage(message);
    }

    public void init(ViewGroup viewGroup, Context context) {
        this.rootView = viewGroup;
        this.mContext = context;
        this.isInitted = true;
    }

    public boolean isTvDialogShowed() {
        return this.isShowed;
    }

    public boolean onDialogOnKeyEvent(int i, KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0) {
            return false;
        }
        ///SkyTVDebug.debug("langlang onDialogOnKeyEvent keyCode = " + i);
        switch (i) {
            case 4:
                mSkyDialogViewHide();
                if (this.dialogListener != null) {
                    this.dialogListener.onDialogOnKeyEvent(i, keyEvent);
                }
                return true;
            case 23:
            case 66:
                if (this.dialogExtraListener == null) {
                    return false;
                }
                this.dialogExtraListener.onBtnOnClickStartEvent();
                return false;
            default:
                return false;
        }
    }

    public void onFirstBtnOnClickEvent() {
       /// SkyTVDebug.debug("langlang onFirstBtnOnClickEvent");
        if (this.dialogListener != null) {
            this.dialogListener.onFirstBtnOnClickEvent();
        }
        mSkyDialogViewHide();
        if (this.dialogExtraListener != null) {
            this.dialogExtraListener.onBtnOnClickEndEvent();
        }
    }

    public void onSecondBtnOnClickEvent() {
        ///SkyTVDebug.debug("langlang onSecondBtnOnClickEvent");
        if (this.dialogListener != null) {
            this.dialogListener.onSecondBtnOnClickEvent();
        }
        mSkyDialogViewHide();
        if (this.dialogExtraListener != null) {
            this.dialogExtraListener.onBtnOnClickEndEvent();
        }
    }

    public void setDialogExtraListener(OnDialogOnKeyExtraListener onDialogOnKeyExtraListener) {
        this.dialogExtraListener = onDialogOnKeyExtraListener;
    }

    public void showTvDialog(String tips1, String tips2, String btn1, String btn2, OnDialogOnKeyListener onDialogOnKeyListener, String str5) {
        if (this.isInitted) {
            this.tips1 = tips1;
            this.tips2 = tips2;
            this.btn1 = btn1;
            this.btn2 = btn2;
            this.dialogListener = onDialogOnKeyListener;
            this.showType = str5;
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mSkyDialogViewShow();
                return;
            }
            Message message = new Message();
            message.what = 1;
            this.uiHandler.sendMessage(message);
        }
    }
}

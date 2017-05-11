package com.ktc.ui.api;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.ktc.ui.blurbg.BlurBgLayout;
import com.ktc.ui.blurbg.BlurBgLayout.PAGETYPE;
import com.ktc.util.KtcScreenParams;
import com.horion.tv.R;

import java.lang.ref.WeakReference;

import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;

public class KtcToastView extends Dialog {
    private BlurBgLayout blurBgLayout;
    private FrameLayout contentLayout;
    private Context mContext;
    private MyHandler mHandler;
    private ShowTime mShowTime;
    private FrameLayout shadowLayout;
    private LayoutParams shadowParams;
    private int showTime;
    private TextView toastTextView;

    private static class MyHandler extends Handler {
        private final WeakReference<KtcToastView> mView;

        public MyHandler(KtcToastView ktcToastView) {
            this.mView = new WeakReference(ktcToastView);
        }

        public void handleMessage(Message message) {
            try {
                KtcToastView ktcToastView = (KtcToastView) this.mView.get();
                if (ktcToastView != null) {
                    switch (message.what) {
                        case 0:
                            if (ktcToastView.isShowing()) {
                                ktcToastView.dismiss();
                                return;
                            }
                            return;
                        default:
                            return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public enum ShowTime {
        LONGTIME,
        SHOTTIME
    }

    public KtcToastView(Context context) {
        super(context, R.style.Lgxdialog_tran);
        getWindow().setGravity(17);
        getWindow().setType(TYPE_SYSTEM_OVERLAY);
        getWindow().setFlags(ViewCompat.MEASURED_STATE_TOO_SMALL, ViewCompat.MEASURED_STATE_TOO_SMALL);
        initView(context);
    }

    private void initView(Context context) {
        if (this.mHandler == null) {
            this.mHandler = new MyHandler(this);
        }
        this.mContext = context;
        this.contentLayout = new FrameLayout(this.mContext);
        this.contentLayout.setFocusable(false);
        this.shadowLayout = new FrameLayout(this.mContext);
        this.shadowParams = new LayoutParams(-2, -2);
        this.shadowLayout.setBackgroundResource(R.drawable.ui_sdk_toast_shadow_no_bg);
        this.blurBgLayout = new BlurBgLayout(this.mContext);
        this.blurBgLayout.setPageType(PAGETYPE.OTHER_PAGE);
        this.blurBgLayout.setBgAlpha(1.0f);
        this.blurBgLayout.setOverLayoutCurAlpha(51);
        this.blurBgLayout.setShadowLayoutAlpha(0.2f);
        this.blurBgLayout.setVisibility(View.INVISIBLE);
        this.shadowLayout.addView(this.blurBgLayout, new LayoutParams(-2, -2));
        this.toastTextView = new TextView(this.mContext);
        ViewGroup.LayoutParams layoutParams = new LayoutParams(-2, -2);
        this.toastTextView.setSingleLine(true);
        this.toastTextView.setFocusable(false);
        this.toastTextView.setGravity(17);
        this.toastTextView.setPadding(KtcScreenParams.getInstence(this.mContext).getResolutionValue(70), KtcScreenParams.getInstence(this.mContext).getResolutionValue(12), KtcScreenParams.getInstence(this.mContext).getResolutionValue(70), KtcScreenParams.getInstence(this.mContext).getResolutionValue(12));
        this.toastTextView.setTextSize((float) KtcScreenParams.getInstence(this.mContext).getTextDpiValue(32));
        this.toastTextView.setTextColor(this.mContext.getResources().getColor(R.color.ff5b5b5b));
        this.toastTextView.setVisibility(View.INVISIBLE);
        this.shadowLayout.addView(this.toastTextView, layoutParams);
        this.contentLayout.addView(this.shadowLayout, this.shadowParams);
        setContentView(this.contentLayout, new ViewGroup.LayoutParams(KtcScreenParams.getInstence(this.mContext).getResolutionValue(1920), KtcScreenParams.getInstence(this.mContext).getResolutionValue(1080)));
    }

    public void dismiss() {
        if (this.mHandler.hasMessages(0)) {
            this.mHandler.removeMessages(0);
        }
        super.dismiss();
    }

    public void setTostString(String str) {
        this.toastTextView.setText(str);
        this.toastTextView.post(new Runnable() {
            public void run() {
                LayoutParams layoutParams = (LayoutParams) KtcToastView.this.blurBgLayout.getLayoutParams();
                layoutParams.width = KtcToastView.this.toastTextView.getWidth();
                layoutParams.height = KtcToastView.this.toastTextView.getHeight();
                KtcToastView.this.blurBgLayout.setLayoutParams(layoutParams);
                KtcToastView.this.blurBgLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void setTostString(String str, int i, int i2, int i3) {
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(i3), i, i2, 33);
        this.toastTextView.setText(spannableString);
        this.toastTextView.post(new Runnable() {
            public void run() {
                LayoutParams layoutParams = (LayoutParams) KtcToastView.this.blurBgLayout.getLayoutParams();
                layoutParams.width = KtcToastView.this.toastTextView.getWidth();
                layoutParams.height = KtcToastView.this.toastTextView.getHeight();
                KtcToastView.this.blurBgLayout.setLayoutParams(layoutParams);
                KtcToastView.this.blurBgLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @SuppressLint({"RtlHardcoded"})
    public void showToast(ShowTime showTime, LayoutParams layoutParams) {
        this.mShowTime = showTime;
        if (layoutParams != null) {
            this.shadowParams = layoutParams;
        } else {
            this.shadowParams = new LayoutParams(-2, -2);
            this.shadowParams.gravity = 81;
            this.shadowParams.bottomMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(17);
        }
        this.shadowLayout.setLayoutParams(this.shadowParams);
        this.toastTextView.setVisibility(View.VISIBLE);
        show();
        if (this.mShowTime.equals(ShowTime.LONGTIME)) {
            this.showTime = 4000;
        } else if (this.mShowTime.equals(ShowTime.SHOTTIME)) {
            this.showTime = 2000;
        }
        if (this.mHandler.hasMessages(0)) {
            this.mHandler.removeMessages(0);
        }
        this.mHandler.sendEmptyMessageDelayed(0, (long) this.showTime);
    }
}

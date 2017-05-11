package com.ktc.panasonichome.view.api;

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

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout.PAGETYPE;

import java.lang.ref.WeakReference;

public class SkyToastView extends Dialog {
    private BlurBgLayout blurBgLayout;
    private FrameLayout contentLayout;
    private Context mContext;
    private MyHandler mHandler;
    private ShowTime mShowTime;
    private FrameLayout shadowLayout;
    private LayoutParams shadowParams;
    private int showTime;
    private TextView toastTextView;

    class C03031 implements Runnable {
        C03031() {
        }

        public void run() {
            LayoutParams blue_p = (LayoutParams) SkyToastView.this.blurBgLayout.getLayoutParams();
            blue_p.width = SkyToastView.this.toastTextView.getWidth();
            blue_p.height = SkyToastView.this.toastTextView.getHeight();
            SkyToastView.this.blurBgLayout.setLayoutParams(blue_p);
            SkyToastView.this.blurBgLayout.setVisibility(View.VISIBLE);
        }
    }

    class C03042 implements Runnable {
        C03042() {
        }

        public void run() {
            LayoutParams blue_p = (LayoutParams) SkyToastView.this.blurBgLayout.getLayoutParams();
            blue_p.width = SkyToastView.this.toastTextView.getWidth();
            blue_p.height = SkyToastView.this.toastTextView.getHeight();
            SkyToastView.this.blurBgLayout.setLayoutParams(blue_p);
            SkyToastView.this.blurBgLayout.setVisibility(View.VISIBLE);
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<SkyToastView> mView;

        public MyHandler(SkyToastView view) {
            this.mView = new WeakReference(view);
        }

        public void handleMessage(Message msg) {
            try {
                SkyToastView view = (SkyToastView) this.mView.get();
                if (view != null) {
                    switch (msg.what) {
                        case 0:
                            if (view.isShowing()) {
                                view.dismiss();
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

    public SkyToastView(Context context) {
        super(context, R.style.Lgxdialog_tran);
        getWindow().setGravity(17);
        getWindow().setType(2006);
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
        this.shadowParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        this.shadowLayout.setBackgroundResource(R.drawable.ui_sdk_toast_shadow_no_bg);
        this.blurBgLayout = new BlurBgLayout(this.mContext);
        this.blurBgLayout.setPageType(PAGETYPE.OTHER_PAGE);
        this.blurBgLayout.setBgAlpha(1.0f);
        this.blurBgLayout.setOverLayoutCurAlpha(51);
        this.blurBgLayout.setShadowLayoutAlpha(0.2f);
        this.blurBgLayout.setVisibility(View.INVISIBLE);
        this.shadowLayout.addView(this.blurBgLayout, new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        this.toastTextView = new TextView(this.mContext);
        LayoutParams toastTextViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        this.toastTextView.setSingleLine(true);
        this.toastTextView.setFocusable(false);
        this.toastTextView.setGravity(17);
        this.toastTextView.setPadding(ScreenParams.getInstence(this.mContext).getResolutionValue(70), ScreenParams.getInstence(this.mContext).getResolutionValue(12), ScreenParams.getInstence(this.mContext).getResolutionValue(70), ScreenParams.getInstence(this.mContext).getResolutionValue(12));
        this.toastTextView.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(32));
        this.toastTextView.setTextColor(this.mContext.getResources().getColor(R.color.ff5b5b5b));
        this.toastTextView.setVisibility(View.INVISIBLE);
        this.shadowLayout.addView(this.toastTextView, toastTextViewParams);
        this.contentLayout.addView(this.shadowLayout, this.shadowParams);
        setContentView(this.contentLayout, new ViewGroup.LayoutParams(ScreenParams.getInstence(this.mContext).getResolutionValue(1920), ScreenParams.getInstence(this.mContext).getResolutionValue(1080)));
    }

    @SuppressLint({"RtlHardcoded"})
    public void showToast(ShowTime time, LayoutParams layoutParams) {
        this.mShowTime = time;
        if (layoutParams != null) {
            this.shadowParams = layoutParams;
        } else {
            this.shadowParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            this.shadowParams.gravity = 81;
            this.shadowParams.bottomMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(17);
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

    public void dismiss() {
        if (this.mHandler.hasMessages(0)) {
            this.mHandler.removeMessages(0);
        }
        super.dismiss();
    }

    public void setTostString(String toastStr) {
        this.toastTextView.setText(toastStr);
        this.toastTextView.post(new C03031());
    }

    public void setTostString(String str, int start, int end, int color) {
        SpannableString name = new SpannableString(str);
        name.setSpan(new ForegroundColorSpan(color), start, end, 33);
        this.toastTextView.setText(name);
        this.toastTextView.post(new C03042());
    }
}

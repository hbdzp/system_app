package com.horion.tv.ui.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.horion.tv.R;
///import com.ktc.framework.skysdk.logger.SkyLogger;
import com.ktc.framework.ktcsdk.message.MessageInfo.MessageInfoData;
import com.ktc.util.KtcScreenParams;
///import com.ktc.webSDK.webservice.RestCallResult;

public class SkyLeftTitleView {
    private static SkyLeftTitleView mInstance = null;
    private Handler mScreenHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    ///SkyLogger.d("lwr", "show:" + message.getData().getString(MessageInfoData.TITLE));
                    SkyLeftTitleView.this.titleView.setText(message.getData().getString(MessageInfoData.TITLE));
                    SkyLeftTitleView.this.titleLayout.setVisibility(View.VISIBLE);
                    return;
                case 1:
                    SkyLeftTitleView.this.titleView.setText("");
                    SkyLeftTitleView.this.titleLayout.setVisibility(View.GONE);
                    return;
                default:
                    return;
            }
        }
    };
    private RelativeLayout titleLayout = null;
    private TextView titleView = null;

    private SkyLeftTitleView(Context context) {
        initView(context);
    }

    public static SkyLeftTitleView getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SkyLeftTitleView(context);
        }
        return mInstance;
    }

    private void initView(Context context) {
        this.titleLayout = new RelativeLayout(context);
        this.titleView = new TextView(context);
        this.titleView.setId(R.id.skylefttitleview_titleView);
        this.titleView.setFocusable(false);
        this.titleView.setText("");
        this.titleView.setTextSize((float) KtcScreenParams.getInstence(context).getTextDpiValue(62));
        KtcScreenParams.getInstence(context).settextAlpha(this.titleView, 255, "5b5b5b");
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.leftMargin = KtcScreenParams.getInstence(context).getResolutionValue(78);
        layoutParams.topMargin = KtcScreenParams.getInstence(context).getResolutionValue(36);
        this.titleLayout.addView(this.titleView, layoutParams);
        View imageView = new ImageView(context);
        imageView.setFocusable(false);
        imageView.setBackgroundResource(R.drawable.ui_sdk_menu_title_line);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(KtcScreenParams.getInstence(context).getResolutionValue(600), -2);
        layoutParams2.leftMargin = KtcScreenParams.getInstence(context).getResolutionValue(0);
        layoutParams2.topMargin = KtcScreenParams.getInstence(context).getResolutionValue(7);
        layoutParams2.addRule(3, 1);
        layoutParams2.addRule(7, 1);
        this.titleLayout.addView(imageView, layoutParams2);
        this.titleLayout.setVisibility(View.INVISIBLE);
    }

    public RelativeLayout getRootView() {
        return this.titleLayout;
    }

    public void hideLeftTitleView() {
        Message obtain = Message.obtain();
        obtain.what = 1;
        this.mScreenHandler.sendMessage(obtain);
    }

    public void showLeftTitleView(String str) {
        Bundle bundle = new Bundle();
        bundle.putString(MessageInfoData.TITLE, str);
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.setData(bundle);
        this.mScreenHandler.sendMessage(obtain);
    }
}

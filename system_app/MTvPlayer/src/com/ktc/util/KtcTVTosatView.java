package com.ktc.util;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.FrameLayout.LayoutParams;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
import com.ktc.ui.api.KtcToastView;
import com.ktc.ui.api.KtcToastView.ShowTime;

public class KtcTVTosatView {
    private Context mContext = null;
    private Handler mScreenHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    ////KtcLogger.d("lwr", "show:" + message.getData().getInt("info"));
                    LayoutParams layoutParams = new LayoutParams(-2, -2);
                    layoutParams.gravity = 49;
                    layoutParams.topMargin = UiManager.getResolutionValue(40);
                    KtcTVTosatView.this.mToastView.setTostString(KtcTVTosatView.this.mContext.getResources().getString(message.getData().getInt("info")));
                    KtcTVTosatView.this.mToastView.showToast(ShowTime.LONGTIME, layoutParams);
                    return;
                case 1:
                    try {
                        if (KtcTVTosatView.this.mToastView.isShowing()) {
                            KtcTVTosatView.this.mToastView.dismiss();
                            return;
                        }
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private KtcToastView mToastView = null;

    public KtcTVTosatView(Context context) {
        this.mContext = context;
        this.mToastView = new KtcToastView(context);
    }

    public void hideTVToastView() {
        Message obtain = Message.obtain();
        obtain.what = 1;
        this.mScreenHandler.sendMessage(obtain);
    }

    public void showTVToastView(int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("info", i);
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.setData(bundle);
        this.mScreenHandler.sendMessage(obtain);
    }
}

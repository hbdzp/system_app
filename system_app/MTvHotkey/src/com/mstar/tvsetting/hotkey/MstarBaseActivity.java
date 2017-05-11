
package com.mstar.tvsetting.hotkey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MstarBaseActivity extends Activity implements MstarBaseInterface {

    private int delayMillis = 10000;

    private int delayMessage = 88888888;

    private int refreshUI = 99999999;

    private Handler hander = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == delayMessage) {
                onTimeOut();
            }
            if (msg.what == refreshUI) {
                onRefreshUI();
            }

        }
    };

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        hander.sendEmptyMessage(refreshUI);
        hander.sendEmptyMessageDelayed(delayMessage, delayMillis);
    }

    @SuppressLint("NewApi")
	@Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        if (hander.hasMessages(delayMessage)) {
            hander.removeMessages(delayMessage);
            hander.sendEmptyMessageDelayed(delayMessage, delayMillis);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        hander.removeMessages(delayMessage);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    public void onTimeOut() {
        // TODO Auto-generated method stub
    }

    @Override
    public void onRefreshUI() {
        // TODO Auto-generated method stub

    }

}

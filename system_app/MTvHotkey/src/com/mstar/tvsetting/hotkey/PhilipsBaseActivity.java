package com.mstar.tvsetting.hotkey;



import com.mstar.android.MIntent;
import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.tvsetting.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

public class PhilipsBaseActivity extends Activity implements MstarBaseInterface {

	private int delayMillis = 10000;

	private int delayMessage = 88888888;
	
	private int refreshUI = 99999999;

	protected boolean alwaysTimeout = false;
	
	public static  final String TV_KEYPAD_NAME = "MStar Smart TV Keypad";
	
	private Handler timerHander = new Handler() {
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
	public boolean onKeyDown(int keyCode, KeyEvent arg1) {
		// TODO Auto-generated method stub

		if (SwitchPageHelper.goToSourceInfo(this, keyCode) == true) {
			this.finish();
			return true;
		}else if (SwitchPageHelper.goToPhilipsPicFormatDialog(this, keyCode) == true) {
			this.finish();
			return true;
		} else if (KeyEvent.KEYCODE_VOLUME_MUTE == keyCode
				|| KeyEvent.KEYCODE_VOLUME_UP == keyCode
				|| KeyEvent.KEYCODE_VOLUME_DOWN == keyCode) {
			//finish();
		} else if (MKeyEvent.KEYCODE_MTS == keyCode) {
			finish();
		} else if (SwitchPageHelper.goToPhilipsPicFormatDialog(this, keyCode) == true) {
			finish();
			return true;
		} else if (KeyEvent.KEYCODE_CHANNEL_DOWN == keyCode
				|| KeyEvent.KEYCODE_CHANNEL_UP == keyCode) {
			if(!TV_KEYPAD_NAME.equals(arg1.getDevice().getName()))
			ktcChannalChange(keyCode);
		}  else if (SwitchPageHelper.goToOptionMenuPage(this, keyCode) == true) {
			finish();
			return true;
		} else if (SwitchPageHelper.goToProgrameListInfo(this, keyCode) == true) {
			this.finish();
			return true;
		} else if(SwitchPageHelper.goToEpgPage(this, keyCode) == true)
		{
			finish();
			return true;
		} 
		return super.onKeyDown(keyCode, arg1);
	}

	private boolean ktcChannalChange(int keyCode) {
		int inputSrc = TvCommonManager.getInstance()
				.getCurrentTvInputSource();
		TvChannelManager mTvChannelManager = TvChannelManager.getInstance();
		if (inputSrc == TvCommonManager.INPUT_SOURCE_ATV
				|| inputSrc == TvCommonManager.INPUT_SOURCE_DTV) {
			if (keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) {
				mTvChannelManager.programDown();
				this.finish();
				Intent intent = new Intent("com.mstar.tv.tvplayer.ui.intent.action.SOURCE_INFO");
				this.startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,0);				
				return true;
			} else if (keyCode == KeyEvent.KEYCODE_CHANNEL_UP) {
				mTvChannelManager.programUp();
				this.finish();
				Intent intent = new Intent("com.mstar.tv.tvplayer.ui.intent.action.SOURCE_INFO");
				this.startActivity(intent);
				overridePendingTransition(R.anim.slide_in_right,0);				
				return true;
			}
		}
		return false;
	}

	@Override
	public void onTimeOut() {
		// TODO Auto-generated method stub
		if (alwaysTimeout)
			timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
	}
	@Override
    public void onRefreshUI() {
        // TODO Auto-generated method stub

    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		timerHander.sendEmptyMessage(refreshUI);
		timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		timerHander.removeMessages(delayMessage);
		finish();
	}

	@Override
	public void onUserInteraction() {
		// TODO Auto-generated method stub
		super.onUserInteraction();
		if (timerHander.hasMessages(delayMessage)) {
			timerHander.removeMessages(delayMessage);
			timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
		}
	}

	public void onRemoveMessage() {
		timerHander.removeMessages(delayMessage);
	}

	public void onSendMessage() {
		timerHander.sendEmptyMessageDelayed(delayMessage, delayMillis);
	}
	
}

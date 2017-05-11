/**
 * MStar Software
 * Copyright (c) 2011 - 2012 MStar Semiconductor, Inc. All rights reserved.
 *
 * All software, firmware and related documentation herein (?銉﹀彂Star Software?鐕傛嫹 are
 * intellectual property of MStar Semiconductor, Inc. (?銉﹀彂Star?鐕傛嫹 and protected by
 * law, including, but not limited to, copyright law and international treaties.
 * Any use, modification, reproduction, retransmission, or republication of all
 * or part of MStar Software is expressly prohibited, unless prior written
 * permission has been granted by MStar.
 *
 * By accessing, browsing and/or using MStar Software, you acknowledge that you
 * have read, understood, and agree, to be bound by below terms (?銉︾炕erms?鐕傛嫹 and to
 * comply with all applicable laws and regulations:
 *
 * 1. MStar shall retain any and all right, ownership and interest to MStar
 * Software and any modification/derivatives thereof.  No right, ownership,
 * or interest to MStar Software and any modification/derivatives thereof is
 * transferred to you under Terms.
 *
 * 2. You understand that MStar Software might include, incorporate or be supplied
 * together with third party?銉? software and the use of MStar Software may require
 * additional licenses from third parties.  Therefore, you hereby agree it is your
 * sole responsibility to separately obtain any and all third party right and
 * license necessary for your use of such third party?銉? software.
 *
 * 3. MStar Software and any modification/derivatives thereof shall be deemed as
 * MStar?銉? confidential information and you agree to keep MStar?銉? confidential
 * information in strictest confidence and not disclose to any third party.
 *
 * 4. MStar Software is provided on an ?銉?S IS?鐕傛嫹basis without warranties of any kind.
 * Any warranties are hereby expressly disclaimed by MStar, including without
 * limitation, any warranties of merchantability, non-infringement of intellectual
 * property rights, fitness for a particular purpose, error free and in conformity
 * with any international standard.  You agree to waive any claim against MStar for
 * any loss, damage, cost or expense that you may incur related to your use of MStar
 * Software.  In no event shall MStar be liable for any direct, indirect, incidental
 * or consequential damages, including without limitation, lost of profit or revenues,
 * lost or damage of data, and unauthorized system use.  You agree that this Section 4
 * shall still apply without being affected even if MStar Software has been modified
 * by MStar in accordance with your request or instruction for your use, except
 * otherwise agreed by both parties in writing.
 *
 * 5. If requested, MStar may from time to time provide technical supports or
 * services in relation with MStar Software to you for your use of MStar Software
 * in conjunction with your or your customer?銉? product (?銉﹁棭ervices?鐕傛嫹.  You understand
 * and agree that, except otherwise agreed by both parties in writing, Services are
 * provided on an ?銉?S IS?鐕傛嫹basis and the warranty disclaimer set forth in Section 4
 * above shall apply.
 *
 * 6. Nothing contained herein shall be construed as by implication, estoppels or
 * otherwise: (a) conferring any license or right to use MStar name, trademark,
 * service mark, symbol or any other identification; (b) obligating MStar or any
 * of its affiliates to furnish any person, including without limitation, you and
 * your customers, any assistance of any kind whatsoever, or any information; or
 * (c) conferring any license or right under any intellectual property right.
 *
 * 7. These terms shall be governed by and construed in accordance with the laws
 * of Taiwan, R.O.C., excluding its conflict of law rules.  Any and all dispute
 * arising out hereof or related hereto shall be finally settled by arbitration
 * referred to the Chinese Arbitration Association, Taipei in accordance with
 * the ROC Arbitration Law and the Arbitration Rules of the Association by three (3)
 * arbitrators appointed in accordance with the said Rules.  The place of
 * arbitration shall be in Taipei, Taiwan and the language shall be English.
 * The arbitration award shall be final and binding to both parties.
 */
package com.mstar.tvsetting.hotkey;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mstar.tvsetting.hotkey.K_DataBaseHelper;
import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tvapi.common.PvrManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.Enum3dType;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideoDisplayFormat;
import com.mstar.tvsetting.R;
import com.mstar.tvsetting.switchinputsource.InputSourceRecycleViewAdapter;
import com.mstar.tvsetting.switchinputsource.InputSourceRecycleViewAdapter.OnItemClickListener;
import com.mstar.tvsetting.switchinputsource.InputSourceRecycleViewAdapter.OnItemFocusChangeListener;

import java.util.ArrayList;
import java.util.List;

//import com.mstar.android.tv.TvCecManager;
//import com.mstar.android.tvapi.common.CecManager;

public class InputSourceListViewActivity extends PhilipsBaseActivity {
	private String[] data;
	private InputSourceRecycleViewAdapter adapter = null;
	// ArrayList<CecDeviceItem> cecData = new ArrayList<CecDeviceItem>();
	// private CecDeviceAdapter cecAdapter = null;
	private HorizontalGridView mHorizontalGridView;
	// private ListView cecListView;
	private Intent intent = null;
	private boolean switchFlag = true;
	private int inputSource = TvCommonManager.INPUT_SOURCE_DTV;
	private String[] tmpData = null;
	// the password of entrying Design menu
	private final static String GOODKEYCODES = String.valueOf(KeyEvent.KEYCODE_2) + String.valueOf(KeyEvent.KEYCODE_5)
			+ String.valueOf(KeyEvent.KEYCODE_8) + String.valueOf(KeyEvent.KEYCODE_0);
	// the password of entrying factory menu
	private final static String FACTORYEYCODES = String.valueOf(KeyEvent.KEYCODE_8) + String.valueOf(KeyEvent.KEYCODE_2)
			+ String.valueOf(KeyEvent.KEYCODE_0) + String.valueOf(KeyEvent.KEYCODE_2);
	final static int SETIS_START = -100;
	final static int SETIS_END_COMPLETE = -101;
	private ArrayList<Integer> keyQueue;
	private Dialog progressDialog = null;
	private final String IS_POWER_ON_PROPERTY = "mstar.launcher.1stinit";
	/* for source changes */
	private TvCommonManager tvCommonmanager = null;
	private TvChannelManager tvChannelManager = null;
	private TvS3DManager tvS3DManager = null;
	/* for prompt user to stop recording */
	private PvrManager pvrManager = null;
	private static int nSelectedItemIdx = 0;
	private static AlertDialog stopRecordDialog = null;
	private static final String DEBUG_TAG = InputSourceListViewActivity.class.getCanonicalName();
	static boolean active = false;
	SourceViewBroadcastReceiver sourceViewBroadcastReceiver = null;
	private static final String debugTag = "InputSourceListViewActivity";
	private ThreeDModeObserver observer_ThreeDMode = null;
	private boolean isSourceNeedSwitch = false;
	private static int systemAutoTime = 0;
	// private TvCecManager tvCecManager;
	private boolean hasDtmb;
	private int hdmiNum = 4;
	private TvPipPopManager tvPipPopManager = null;
	private boolean isChangingSrc = false;
	private boolean hasKeydown = false;
	public static final String TV_KEYPAD_NAME = "MStar Smart TV Keypad";
	private int[] mImageRess = { R.drawable.selector_item_source_hdmi, R.drawable.selector_item_source_hdmi,
			R.drawable.selector_item_source_hdmi, R.drawable.selector_item_source_av,
			R.drawable.selector_item_source_atv, R.drawable.selector_item_source_dtv };
	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == SETIS_START) {
				isChangingSrc = true;
				if (sourceViewBroadcastReceiver != null) {
					InputSourceListViewActivity.this.unregisterReceiver(sourceViewBroadcastReceiver);
					sourceViewBroadcastReceiver = null;
				}
				progressDialog = getProgressDialog();
				progressDialog.show();
			} else if (msg.what == SETIS_END_COMPLETE) {
				finish();
				if (progressDialog != null) {
					if (progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
				/*intent = new Intent("com.mstar.tv.tvplayer.ui.intent.action.SOURCE_INFO");
				intent.putExtra("task_tag", "input_source_changed");
				startActivity(intent);
				overridePendingTransition(R.anim.slide_bottom_in, 0);*/
				isChangingSrc = true;
			}
		};
	};

	private Dialog getProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new BlackBGProgess(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
			progressDialog.setCancelable(false);
		}
		return progressDialog;
	}

	class BlackBGProgess extends Dialog {
		public BlackBGProgess(Context context, int theme) {
			super(context, theme);
		}

		public BlackBGProgess(Context context) {
			super(context);
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			setContentView(R.layout.progress_layout);
			super.onCreate(savedInstanceState);
		}

		@Override
		public void show() {
			// TODO Auto-generated method stub
			super.show();
			// mengwt 20141117
			Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 1);
			// mengwt 20141117
		}

		@Override
		public void dismiss() {
			// TODO Auto-generated method stub
			// mengwt 20141117
			Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 0);
			// mengwt 20141117
			super.dismiss();

		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(debugTag, "--onCreate--...");
		super.onCreate(savedInstanceState);

		tvCommonmanager = TvCommonManager.getInstance();
		tvChannelManager = TvChannelManager.getInstance();
		tvS3DManager = TvS3DManager.getInstance();
		pvrManager = TvManager.getInstance().getPvrManager();
		setContentView(R.layout.input_source_grid_view);
		observer_ThreeDMode = new ThreeDModeObserver(handler);
		String value = Utils.readPropValue("/system/build.prop", "ktc.dtmb");
		hasDtmb = value != null && value.equals("true");
		// tvCecManager = TvCecManager.getInstance();
		tvPipPopManager = TvPipPopManager.getInstance();
		inputSource = tvCommonmanager.getCurrentTvInputSource();
	}

	@Override
	protected void onResume() {
		Log.d(debugTag, "--onResume--");
		super.onResume();
		isChangingSrc = false;
		getContentResolver().registerContentObserver(
				Uri.parse("content://mstar.tv.usersetting/threedvideomode/inputsrc/"), true, observer_ThreeDMode);
	}

	@Override
	public void onStart() {
		Log.d(debugTag, "--onStart--");
		active = true;
		super.onStart();
		// getContentResolver().registerContentObserver(Uri.parse(
		// "content://mstar.tv.usersetting/threedvideomode/inputsrc/"),
		// true, observer_ThreeDMode);

	}

	@Override
	public void onStop() {
		Log.d(debugTag, "--onStop--");
		active = false;
		super.onStop();
		getContentResolver().unregisterContentObserver(observer_ThreeDMode);
	}

	@SuppressWarnings("deprecation")
	private void init() {
		mHorizontalGridView = (HorizontalGridView) findViewById(R.id.hgv_source);
		data = getResources().getStringArray(R.array.str_arr_songxia_input_source);
		adapter = new InputSourceRecycleViewAdapter(this,data,mImageRess);
		mHorizontalGridView.setAdapter(adapter);
		mHorizontalGridView.setHasFixedSize(true);
		mHorizontalGridView.setExtraLayoutSpace(
				(int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics())
						+ 0.5f));
		mHorizontalGridView.setSelectedPosition(getselectByInputSource(inputSource));
		setListener();
	}

	private void setListener() {
		adapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				switch(position){
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					if (switchSourceHandler!=null && switchSourceHandler.hasMessages(SWITCH_SOURCE)){
						switchSourceHandler.removeMessages(SWITCH_SOURCE);
					}
					changeInputSource(getInputSourceByPosition(position),position);
				}
			}

		});
		adapter.setItemFocusChangeListener(new OnItemFocusChangeListener(){
			@Override
			public void onItemFocusChange(TextView text, ImageView image, int position, boolean hasFocus) {
				if(hasFocus)
				{
					text.setTextColor(Color.BLACK);
					switchSourceWhenFocused(position);
				}
				else {
					text.setTextColor(R.color.ff061527);
				}
			}
		});
		
	}
	private static final int SWITCH_SOURCE = 0x01;
	private Handler switchSourceHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what)
			{
				case SWITCH_SOURCE:
					int position = (int)msg.obj;
					changeInputSource(getInputSourceByPosition(position),position);
					break;
			}
		}
	};
	private void switchSourceWhenFocused(int position)
	{
		if (switchSourceHandler.hasMessages(SWITCH_SOURCE))
		{
			switchSourceHandler.removeMessages(SWITCH_SOURCE);
		}
		Message msg = Message.obtain();
		msg.what = SWITCH_SOURCE;
		msg.obj = position;
		switchSourceHandler.sendMessageDelayed(msg,5000);
	}


	@Override
	protected void onPause() {
		finish();
		super.onPause();
	}

	@Override
	public void onUserInteraction() {
		super.onUserInteraction();
	}

	@Override
	protected void onDestroy() {
		if (switchSourceHandler.hasMessages(SWITCH_SOURCE))
		{
			switchSourceHandler.removeMessages(SWITCH_SOURCE);
		}
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		if (stopRecordDialog != null && stopRecordDialog.isShowing()) {
			stopRecordDialog.dismiss();
		}

		if (sourceViewBroadcastReceiver != null) {
			this.unregisterReceiver(sourceViewBroadcastReceiver);
			sourceViewBroadcastReceiver = null;
		}
		super.onDestroy();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		hasKeydown = true;
		if (TV_KEYPAD_NAME.equals(event.getDevice().getName())){
			Log.d("dzp","equals");
			MapKeyPadToIR(keyCode, event);
		}
		if (isChangingSrc && keyCode == MKeyEvent.KEYCODE_TV_SETTING) {
			return true;
		}
		if (keyQueue == null) {
			keyQueue = new ArrayList<Integer>();
		}
		keyQueue.add(keyCode);
		if (keyQueue.size() == 4) {
			String keystr = intArrayListToString(keyQueue);
			if (keystr.equals(GOODKEYCODES)) {
				keyQueue.clear();
				intent = new Intent("mstar.tvsetting.factory.intent.action.MainmenuActivity");
				startActivity(intent);
				finish();
				return true;
			} else {
				keyQueue.remove(0);
			}
		}

		switch (keyCode) {
			case MKeyEvent.KEYCODE_SOUND_MODE:
			case MKeyEvent.KEYCODE_PICTURE_MODE:
			case MKeyEvent.KEYCODE_ASPECT_RATIO:
			case KeyEvent.KEYCODE_BACK:
				finish();
				overridePendingTransition(0, R.anim.slide_bottom_out);
				return true;
			case KeyEvent.KEYCODE_TV_INPUT:
				if (mHorizontalGridView.getVisibility() == View.VISIBLE) {
					int nextSelectedPostion = (mHorizontalGridView.getSelectedPosition() + 1) % mImageRess.length;
					mHorizontalGridView.setSelectedPosition(nextSelectedPostion);
					return true;
				} else {
					finish();
					overridePendingTransition(0, R.anim.slide_bottom_out);
					return true;
				}
			case KeyEvent.KEYCODE_MENU:
				return true;
			default:
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	private Boolean MapKeyPadToIR(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((KeyEvent.KEYCODE_CHANNEL_UP == keyCode) || (KeyEvent.KEYCODE_CHANNEL_DOWN == keyCode)) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_CHANNEL_UP:
				keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
				return true;
			case KeyEvent.KEYCODE_CHANNEL_DOWN:
				keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
				return true;
			default:
				return false;
			}
		}
		return false;

	}

	private void keyInjection(final int keyCode) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			new Thread() {
				public void run() {
					try {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(keyCode);
					} catch (Exception e) {
						Log.e("Exception when sendPointerSync", e.toString());
					}
				}
			}.start();
		}

	}

	private String intArrayListToString(ArrayList<Integer> al) {
		String str = "";
		for (int i = 0; i < al.size(); ++i) {
			str += al.get(i).toString();
		}
		return str;
	}

	private class OnStopRecordCancelClickListener implements DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			if (stopRecordDialog != null && stopRecordDialog.isShowing()) {
				stopRecordDialog.dismiss();
			}
			nSelectedItemIdx = tvCommonmanager.getCurrentTvInputSource();
			Log.i(DEBUG_TAG, "OnStopRecordCancelClickListener onClick");
		}

	}

	private class OnStopRecordConfirmClickListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (stopRecordDialog != null && stopRecordDialog.isShowing()) {
				stopRecordDialog.dismiss();
			}
			if (tvCommonmanager.getCurrentTvInputSource() >= TvCommonManager.INPUT_SOURCE_STORAGE) {
				try {
					if (pvrManager.isPlaybacking()) {
						pvrManager.stopPlayback();
						pvrManager.stopPlaybackLoop();
					}
					pvrManager.stopRecord();
				} catch (TvCommonException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Intent source_switch_from_storage = new Intent("source.switch.from.storage");
				sendBroadcast(source_switch_from_storage);
				intent = new Intent(InputSourceListViewActivity.this, ProgressActivity.class);
				intent.putExtra("task_tag", "input_source_changed");
				// intent.putExtra("inputSource",
				// data.get(nSelectedItemIdx).getPositon());
				try {
					startActivity(intent);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: tip activity not found.
				}
			} else {

				new Thread(new Runnable() {
					@SuppressWarnings("deprecation")
					@Override
					public void run() {
						// to ensure input-source is switched after database has
						// been saved, we'll do it later
						handler.sendEmptyMessage(SETIS_START);
						isSourceNeedSwitch = true;
						UpdateSourceInputType(inputSource);

						tvS3DManager.setDisplayFormat(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE);
					}
				}).start();
			}
			dialog.dismiss();
			finish();
		}
	}

	private boolean showStopRecordDialog() {
		boolean bRet = true;
		do {
			if (stopRecordDialog != null && stopRecordDialog.isShowing()) {
				Log.e(DEBUG_TAG, "stopRecordDialog already exist");
				bRet = false;
				break;
			}
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder = dialogBuilder.setTitle(R.string.str_stop_record_dialog_title);
			dialogBuilder = dialogBuilder.setMessage(R.string.str_stop_record_dialog_message);
			dialogBuilder = dialogBuilder.setPositiveButton(R.string.str_stop_record_dialog_confirm,
					new OnStopRecordConfirmClickListener());
			dialogBuilder = dialogBuilder.setNegativeButton(R.string.str_stop_record_dialog_cancel,
					new OnStopRecordCancelClickListener());
			if (dialogBuilder == null) {
				Log.e(DEBUG_TAG, "showStopRecordDialog -- AlertDialog.Builder init fail");
				bRet = false;
				break;
			}
			stopRecordDialog = dialogBuilder.create();
			if (stopRecordDialog == null) {
				Log.e(DEBUG_TAG, "showStopRecordDialog -- AlertDialog.Builder create dialog fail");
				bRet = false;
				break;
			}
			stopRecordDialog.show();
		} while (false);
		return bRet;
	}

	// Mute Receiver Receive Broadcast form system
	private class SourceViewBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// mute
			if (action.equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_STATUS_UPDATE")) {
			} else {
				Log.e(debugTag, "Unknown Key Event!");
			}
		}
	}

	@Override
	public void onTimeOut() {
		super.onTimeOut();
		if (tvPipPopManager.isPipModeEnabled() == false) {
			finish();
		}

	}

	private Handler focusHandler = new Handler();

	@Override
	public void onRefreshUI() {
		super.onRefreshUI();
		if (inputSource == TvCommonManager.INPUT_SOURCE_STORAGE) {
			//inputSource = SystemProperties.getInt("persist.sys.presource", TvCommonManager.INPUT_SOURCE_DTV);
			inputSource = K_DataBaseHelper.getInstance(this).K_getValueDatabase_systemsetting("PreSource");
		}
		init();
		sourceViewBroadcastReceiver = new SourceViewBroadcastReceiver();
		keyQueue = new ArrayList<Integer>();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mstar.tv.service.COMMON_EVENT_SIGNAL_STATUS_UPDATE");
		InputSourceListViewActivity.this.registerReceiver(sourceViewBroadcastReceiver, filter);
	}

	private class ThreeDModeObserver extends ContentObserver {

		public ThreeDModeObserver(Handler handler) {
			super(handler);
		}

		public void onChange(boolean selfChange) {
			if (!isSourceNeedSwitch) {
				return;
			}
			isSourceNeedSwitch = false;
			if (systemAutoTime > 0) {
				Settings.System.putInt(getContentResolver(), Settings.Global.AUTO_TIME, systemAutoTime);
			}

			// disable dualview
			if ((tvPipPopManager.isPipModeEnabled() == true)
					&& (tvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_POP)) {
				Enum3dType formatType = Enum3dType.EN_3D_TYPE_NUM;
				try {
					formatType = TvManager.getInstance().getThreeDimensionManager().getCurrent3dFormat();
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (formatType == Enum3dType.EN_3D_DUALVIEW) {
					tvPipPopManager.disable3dDualView();
				}
			}

			if (inputSource == TvCommonManager.INPUT_SOURCE_ATV) {
				tvCommonmanager.setInputSource(inputSource);
				int curChannelNumber = tvChannelManager.getCurrentChannelNumber();
				if (curChannelNumber > 0xFF) {
					curChannelNumber = 0;
				}
				tvChannelManager.setAtvChannel(curChannelNumber);
			} else if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
				try {
					systemAutoTime = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME);
				} catch (SettingNotFoundException e) {
					systemAutoTime = 0;
				}

				if (systemAutoTime > 0) {
					Settings.System.putInt(getContentResolver(), Settings.Global.AUTO_TIME, 0);
				}
				tvCommonmanager.setInputSource(inputSource);
				tvChannelManager.playDtvCurrentProgram();
			} else {
				tvCommonmanager.setInputSource(inputSource);
			}
			handler.sendEmptyMessageDelayed(SETIS_END_COMPLETE, 400);
		}
	}

	public void UpdateSourceInputType(int inputSourceTypeIdex) {
		long ret = -1;
		ContentValues vals = new ContentValues();
		vals.put("enInputSourceType", inputSourceTypeIdex);
		try {
			ret = getContentResolver().update(Uri.parse("content://mstar.tv.usersetting/systemsetting"), vals, null,
					null);
		} catch (SQLException e) {
		}
		if (ret == -1) {
			System.out.println("update tbl_PicMode_Setting ignored");
		}
	}

	private Intent targetIntent;

	private void executePreviousTask(final int position) {

		targetIntent = new Intent("mstar.tvsetting.ui.intent.action.RootActivity");
		targetIntent.putExtra("task_tag", "input_source_changed");
		targetIntent.putExtra("inputSrc", inputSource);
		if (isPowerOn()){
			targetIntent.putExtra("isPowerOn", true);
		}
		
		targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		new Thread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				tvS3DManager.setDisplayFormat(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE);
				/*if (inputSource == TvCommonManager.INPUT_SOURCE_ATV) {
					tvCommonmanager.setInputSource(inputSource);
					int curChannelNumber = tvChannelManager.getCurrentChannelNumber();
					if (curChannelNumber > 0xFF) {
						curChannelNumber = 0;
					}
					tvChannelManager.setAtvChannel(curChannelNumber);
				} else if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
					tvCommonmanager.setInputSource(inputSource);
					tvChannelManager.playDtvCurrentProgram();
				} else {
					tvCommonmanager.setInputSource(inputSource);
				}
*/
			Log.d("Maxs250","MTvHotKey-->");	
			try {
					if (targetIntent != null){
						if (progressDialog != null && progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
						startActivity(targetIntent);
						finish();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public boolean isPowerOn() {
		Log.d("Maxs250", "Is Fist Power On: " + (SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)));
		Log.d("Maxs250","---->MTvHotKey--:isPowerOn = " + (SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)));
		if (!SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)) {
			SystemProperties.set(IS_POWER_ON_PROPERTY, "true");
			return true;
		} else {
			return false;
		}
	}
	private void changeInputSource(int inpSource, int position) {
		inputSource = inpSource;
		//SystemProperties.set("persist.sys.presource",inputSource+"");
		K_DataBaseHelper.getInstance(this).K_updateDatabase_systemsetting("PreSource", inputSource);
		
		
		if (tvCommonmanager.getCurrentTvInputSource() >= TvCommonManager.INPUT_SOURCE_STORAGE) {
			Log.i(debugTag, ">= TvCommonManager.INPUT_SOURCE_STORAGE");
			Intent source_switch_from_storage = new Intent("source.switch.from.storage");
			sendBroadcast(source_switch_from_storage);
			//handler.sendEmptyMessage(SETIS_START);
			executePreviousTask(position);
		} else {
			Log.i(debugTag, "<TvCommonManager.INPUT_SOURCE_STORAGE");
			new Thread(new Runnable() {
				@Override
				public void run() {
					// to ensure input-source is switched after database has
					// been saved, we'll do it later
					//handler.sendEmptyMessage(SETIS_START);
					isSourceNeedSwitch = true;
					UpdateSourceInputType(inputSource);
					tvS3DManager.setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
					ActivityManager             activityManager = (ActivityManager)
							InputSourceListViewActivity.this.getSystemService(Context
							.ACTIVITY_SERVICE);
					List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
					for (RunningAppProcessInfo runprocessInfo : appProcesses) {
						if (runprocessInfo.processName.equals("com.android.tv.settings")) {
							Log.i("InputSource","importance="+runprocessInfo.importance);
							Log.i("InputSource","InputSourceListViewActivity run  com.android.tv.settings");
							if(runprocessInfo.importance==RunningAppProcessInfo.IMPORTANCE_FOREGROUND||
									runprocessInfo.importance==RunningAppProcessInfo.IMPORTANCE_VISIBLE){
								//设置菜单在前端
								targetIntent = new Intent("mstar.tvsetting.ui.intent.action.RootActivity");
								targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
								InputSourceListViewActivity.this.startActivity(targetIntent);
							}
							break;
						}
					}
					finish();
				}
			}).start();
		}
	}

	private int getInputSourceByPosition(int position) {
		int input = 0;
		switch (position) {
		case 0:
			input = TvCommonManager.INPUT_SOURCE_HDMI;
	
			break;
		case 1:
			input = TvCommonManager.INPUT_SOURCE_HDMI2;
			
			break;
		case 2:
			input = TvCommonManager.INPUT_SOURCE_HDMI3;
			break;
		case 3:
			input = TvCommonManager.INPUT_SOURCE_CVBS;
			break;
		case 4:
			input = TvCommonManager.INPUT_SOURCE_ATV;
			break;
		case 5:
			input = TvCommonManager.INPUT_SOURCE_DTV;
			break;
		default:
			break;

		}
		return input;
	}

	private int getselectByInputSource(int inputSource) {
		int selectPosition = 0;
		switch (inputSource) {
		case TvCommonManager.INPUT_SOURCE_HDMI:
			selectPosition = 0;
			break;
		case TvCommonManager.INPUT_SOURCE_HDMI2:
			selectPosition = 1;
			break;
		case TvCommonManager.INPUT_SOURCE_HDMI3:
			selectPosition = 2;
			break;
		case TvCommonManager.INPUT_SOURCE_CVBS:
			selectPosition = 3;
			break;
		case TvCommonManager.INPUT_SOURCE_ATV:
			selectPosition = 4;
			break;
		case TvCommonManager.INPUT_SOURCE_DTV:
			selectPosition = 5;
			break;
		default:
			break;
		}
		return selectPosition;
	}
}

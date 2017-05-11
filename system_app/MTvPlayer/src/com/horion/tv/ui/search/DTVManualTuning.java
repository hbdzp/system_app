//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>

package com.horion.tv.ui.search;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.horion.tv.R;
import com.horion.tv.framework.MstarBaseActivity;
import com.horion.tv.framework.ui.TvIntent;
import com.horion.tv.util.Tools;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.android.tvapi.common.vo.DtvProgramSignalInfo;
import com.mstar.android.tvapi.dtv.dvb.dvbs.vo.SatelliteInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbMuxInfo;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.vo.EnumDtvScanStatus;
import com.mstar.android.tvapi.dtv.vo.RfInfo;
import com.mstar.android.tv.TvChannelManager.DvbcScanParam;
import com.mstar.android.tvapi.common.TvManager;
public class DTVManualTuning extends MstarBaseActivity {
	/** Called when the activity is first created. */

	private static final String TAG = "DTVManualTuning";

	private int mMinCh = 0;

	private int mMaxCh = 0;

	private boolean mDirectChangeCh = false;

	private boolean mOnAutoSwitch = false;

	private static final int CHANNEL_MIN_AIR = 1;

	private static final int CHANNEL_MAX_AIR = 63;

	private static final int CHANNEL_MIN_CABLE = 1;

	private static final int CHANNEL_MAX_CABLE = 135;

	private static final int USER_CHANGE_CHANNEL_TIMEOUT = 2000;

	private static final int INVALID_PHYSICAL_CHANNEL_NUMBER = 0xFF;

	private ViewHolder viewholder_dtvmanualtuning;

	private int currChannelNum = 0;
	private String currChannelNumName = null;
    private int currChannelFrequenery = 0;
    private int mPreviousChannelNumber = -1;
    private int mPreviousFrequency = 0;
	private String mPreviousChannelNumName = null;
	private int modulationindex = 2;

	private int mSymbolRate;

	private int mFrequency;

	private int inputfreq = 0;

	private int inputsymbol = 0;

	private int mDvbtRouteIndex = TvChannelManager.TV_ROUTE_NONE;

	private int mDtmbRouteIndex = TvChannelManager.TV_ROUTE_NONE;

	private int mDvbcRouteIndex = TvChannelManager.TV_ROUTE_NONE;



	private int mDvbsRouteIndex = TvChannelManager.TV_ROUTE_NONE;

	private String strfreq = new String();

	private String strsymbol = new String();

	private int CAB_Type = DvbcScanParam.DVBC_CAB_TYPE_QAM_64;

	private int mMaxFrequencyNumber = 874000;

	private int mFrequencyDigitsBound = 7;

	private int mSymbolRateDigitsBound = 5;

	private int mDefaultFrequency = 394;

	private int mDefaultSymbolRate = 6875;

	private String[] modulationtype = { "16 QAM", "32 QAM", "64 QAM",
			"128 QAM", "256 QAM" };

	private boolean bManualScanByUser = false;

	private final static short DTV_SIGNAL_REFRESH_UI = 0x01;

	private DtvProgramSignalInfo signalInfo;

	private boolean mIsDtvTuning = false;

	private boolean mRunthread = true;


	private OnDtvPlayerEventListener mDtvEventListener = null;

	private Handler mDtvUiEventHandler = null;

	private Handler mDirectChangeChTimeout = new Handler();

	private boolean[] mSatelliteList = null;

	private String[] mSatelliteStrList = null;

	private String[] mDvbsLNBType = null;

	private BroadcastReceiver mReceiver = null;
	private int inputDigit = 0; // zjd,20140711
	private int channelNumberInput = 0; // zjd,20140711
	private Runnable mDirectChangeChTimeoutRunnable = new Runnable() {
		@Override
		public void run() {
			mDirectChangeCh = false;
		}
	};

	private class DtvEventListener implements OnDtvPlayerEventListener {

		@Override
		public boolean onAudioModeChange(int arg0, boolean arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onChangeTtxStatus(int arg0, boolean arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onCiLoadCredentialFail(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDtvAutoTuningScanInfo(int what, DtvEventScan extra) {
			Message msg = mDtvUiEventHandler.obtainMessage(what, extra);
			mDtvUiEventHandler.sendMessage(msg);
			return true;
		}

		@Override
		public boolean onDtvAutoUpdateScan(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDtvChannelNameReady(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDtvPriComponentMissing(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDtvProgramInfoReady(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onEpgTimerSimulcast(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onGingaStatusMode(int arg0, boolean arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onHbbtvStatusMode(int arg0, boolean arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onMheg5EventHandler(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onMheg5ReturnKey(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onMheg5StatusMode(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onOadDownload(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onOadHandler(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onOadTimeout(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPopupScanDialogFrequencyChange(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPopupScanDialogLossSignal(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPopupScanDialogNewMultiplex(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onRctPresence(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onSignalLock(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onSignalUnLock(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onTsChange(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onUiOPExitServiceList(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onUiOPRefreshQuery(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onUiOPServiceList(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	private final Runnable runnable = new Runnable() {
		@Override
		public void run() {
			while (true) {
				if (!mRunthread)
					break;
				signalInfo = TvChannelManager.getInstance().getCurrentSignalInformation();

				dtvSignalHandler.sendEmptyMessageDelayed(DTV_SIGNAL_REFRESH_UI,
						500);
				try {
					Thread.sleep(600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// dtvSignalHandler.postDelayed(runnable, 600);
		}
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dtvmanualtuning);
		viewholder_dtvmanualtuning = new ViewHolder(DTVManualTuning.this);
		viewholder_dtvmanualtuning.findViewForDtvManualTuning();
		viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_start_state
				.setTextColor(Color.GRAY);
		viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_start_state
				.setText(R.string.str_cha_atvmanualtuning_starttuning_state_reminder);
		InitialProgressValueForSignalQuality();
		InitialProgressValueForSignalStrengh();

		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(
						TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE)) {
					Log.i(TAG, "Receive ACTION_CIPLUS_TUNER_UNAVAIABLE...");
					finish();
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE);
		registerReceiver(mReceiver, filter);

		if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
			TvCommonManager.getInstance().setInputSource(
					TvCommonManager.INPUT_SOURCE_DTV);
		}
		isSearchByUser = false;
		mDvbtRouteIndex = TvChannelManager.getInstance().getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT);
		if (mDvbtRouteIndex < 0) {
			mDvbtRouteIndex = TvChannelManager.getInstance().getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBT2);
		}
		mDtmbRouteIndex = TvChannelManager.getInstance().getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DTMB);
		mDvbcRouteIndex = TvChannelManager.getInstance().getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBC);
		mDvbsRouteIndex = TvChannelManager.getInstance().getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBS);
		if (mDvbsRouteIndex < 0) {
			mDvbsRouteIndex = TvChannelManager.getInstance().getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBS2);
		}

		getCurrentFreqAndSymRate();
		TvChannelManager.getInstance().stopDtvScan();
		updatedtvManualtuningComponents(false);
		bManualScanByUser = false;
			int currentIndex = TvChannelManager.getInstance().getCurrentDtvRouteIndex();
			if (mDvbsRouteIndex == currentIndex) {
				mMaxFrequencyNumber = 99999;
				mFrequencyDigitsBound = 6;
				mSymbolRateDigitsBound = 6;
				mDefaultFrequency = 0;
				mDefaultSymbolRate = 0;
				mDvbsLNBType = getResources().getStringArray(
						R.array.str_arr_dtvmanualtuning_lnbtype_vals);
				// init satellite list used for selecting satellite
				int satCount = TvDvbChannelManager.getInstance()
						.getCurrentSatelliteCount();
				mSatelliteStrList = new String[satCount];
				mSatelliteList = new boolean[satCount];
				for (int i = 0; i < satCount; i++) {
					// update satellite list
					SatelliteInfo satInfo = TvDvbChannelManager.getInstance()
							.getSatelliteInfo(i);
					mSatelliteStrList[i] = i + " " + satInfo.satName + " "
							+ mDvbsLNBType[satInfo.lnbType];
					// update default enable list to false
					mSatelliteList[i] = false;
				}
				// show current satellite naming as default one
				int satNumber = TvDvbChannelManager.getInstance()
						.getCurrentSatelliteNumber();
				mSatelliteList[satNumber] = true;
			}
		setOnFocusChangeListeners();
		mRunthread = true;
		new Thread(runnable).start();
		TvManager.getInstance().getMhlManager()
				.setOnMhlEventListener(new OnMhlEventListener() {

					@Override
					public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
						Log.d(TAG, "onAutoSwitch");
						mOnAutoSwitch = true;
						finish();
						return false;
					}

					@Override
					public boolean onKeyInfo(int arg0, int arg1, int arg2) {
						// TODO Auto-generated method stub
						return false;
					}
				});

		mDtvUiEventHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				updateDtvTuningScanInfo((DtvEventScan) msg.obj);
			}
		};
	}

	private Handler dtvSignalHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case DTV_SIGNAL_REFRESH_UI:
				if (null == signalInfo) {
					return;
				}
				if (signalInfo.quality <= 0) {
					setProgressValueForSignalQuality(0);
					setProgressValueForSignalStrengh(0);
				} else {
					setProgressValueForSignalQuality(signalInfo.quality / 10);
					setProgressValueForSignalStrengh(signalInfo.strength / 10);
				}
				break;

			default:
				break;
			}

		}
	};

	public boolean MapKeyPadToIR(int keyCode, KeyEvent event) {
		String deviceName = InputDevice.getDevice(event.getDeviceId())
				.getName();
		if (!deviceName.equals("MStar Smart TV Keypad"))
			return false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_CHANNEL_UP:
			keyInjection(KeyEvent.KEYCODE_DPAD_UP);
			return true;
		case KeyEvent.KEYCODE_CHANNEL_DOWN:
			keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
			return true;
		default:
			return false;
		}

	}

	private void keyInjection(final int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
				|| keyCode == KeyEvent.KEYCODE_DPAD_UP
				|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			new Thread() {
				public void run() {
					try {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(keyCode);
					} catch (Exception e) {
						Log.e(TAG, e.toString());
					}
				}
			}.start();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// If MapKeyPadToIR returns true,the previous keycode has been changed
		// to responding
		// android d_pad keys,just return.
		if (MapKeyPadToIR(keyCode, event))
			return true;
		Intent intent = new Intent();
		int currentid = getCurrentFocus().getId();
		switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				switch (currentid) {
					case R.id.linearlayout_cha_dtvmanualtuning_channelnum: {
						inputDigit = 0;// zjd,20140711
						if ((TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDvbtRouteIndex)
								|| (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDtmbRouteIndex)) {
							RfInfo rfInfo = null;
							rfInfo = TvChannelManager.getInstance().getRfInfo(
									TvChannelManager.NEXT_RF, currChannelNum);
							Log.d("Maxs202","onKeyDown:RIGHT:rfInfo == null = " + (rfInfo ==null));
							Log.d("Maxs202","onKeyDown:RIGHT:rfInfo.rfPhyNum = " + rfInfo.rfPhyNum + " / rfInfo.frequency = " +  rfInfo.frequency+ " /rfInfo.rfName = " + rfInfo.rfName);

							if (INVALID_PHYSICAL_CHANNEL_NUMBER == rfInfo.rfPhyNum){//无效的频道号
								currChannelFrequenery = mPreviousFrequency;
								currChannelNum = mPreviousChannelNumber;
								currChannelNumName = mPreviousChannelNumName;
							}else{
								currChannelNum = rfInfo.rfPhyNum;
								currChannelFrequenery = rfInfo.frequency;
								currChannelNumName = rfInfo.rfName;
								mPreviousFrequency = currChannelFrequenery;
								mPreviousChannelNumber = currChannelNum;
								mPreviousChannelNumName = currChannelNumName;
							}

							viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val
									.setText(Integer.toString(currChannelFrequenery));
						}
                        Log.d("Maxs107","-----2");
						viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
								.setText(getString(R.string.str_cha_dtvmanualtuning_ch) + mPreviousChannelNumName);
						return true;
					}

					case R.id.linearlayout_cha_dtvmanualtuning_modulation:
						if (modulationindex == DvbcScanParam.DVBC_CAB_TYPE_QAM_256)
							modulationindex = 0;
						else
							modulationindex++;
						CAB_Type = modulationindex;
						viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_modulation_val
								.setText(modulationtype[modulationindex]);
						break;
					case R.id.linearlayout_cha_dtvmanualtuning_symbol:
						break;
			/*
			 * case R.id.linearlayout_cha_dtvmanualtuning_frequency: {
			 * mFrequency = (mFrequency + 1) % mMaxFrequencyNumber;
			 * updatedtvManualtuningComponents(); } break;
			 */
					case R.id.linearlayout_cha_dtvmanualtuning_starttuning: {
						if (Tools.isBox()) {
							Log.d(TAG, "dtv manual tuning for box");
							// wait for the tuning done
							if (mIsDtvTuning) {
								Toast.makeText(this, R.string.wait_for_tuning_hint,
										Toast.LENGTH_SHORT).show();
							} else {
								startdtvmanutuning();
							}
							return super.onKeyDown(keyCode, event);
						}
						Log.d(TAG, "dtv manual tuning");
						isSearchByUser = true;
						// Hisa 2016.03.04 add Freeze function start
				/*Intent intentCancel = new Intent();//È¡Ïû¾²Ïñ²Ëµ¥
				intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				sendBroadcast(intentCancel);  */
						// Hisa 2016.03.04 add Freeze function end
						startdtvmanutuning();
					}
					return true;
					default:
						break;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_LEFT:
				switch (currentid) {
					case R.id.linearlayout_cha_dtvmanualtuning_channelnum:
						inputDigit = 0;// zjd,20140711
						if ((TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDvbtRouteIndex)
								|| (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDtmbRouteIndex)) {
							RfInfo rfInfo = null;
							rfInfo = TvChannelManager.getInstance().getRfInfo(
									TvChannelManager.PREVIOUS_RF, currChannelNum);
							Log.d("Maxs202","onKeyDown:LEFT:rfInfo == null = " + (rfInfo ==null));
							Log.d("Maxs202","onKeyDown:LEFT:rfInfo.rfPhyNum = " + rfInfo.rfPhyNum + " / rfInfo.frequency = " +  rfInfo.frequency + " /rfInfo.rfName = " + rfInfo.rfName);
							if (INVALID_PHYSICAL_CHANNEL_NUMBER == rfInfo.rfPhyNum){//无效的频道号
								currChannelFrequenery = mPreviousFrequency;
								currChannelNum = mPreviousChannelNumber;
								currChannelNumName = mPreviousChannelNumName;
							}else{
								currChannelNum = rfInfo.rfPhyNum;
								currChannelFrequenery = rfInfo.frequency;
								currChannelNumName = rfInfo.rfName;
								mPreviousFrequency = currChannelFrequenery;
								mPreviousChannelNumber = currChannelNum;
								mPreviousChannelNumName = currChannelNumName;
							}


							viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val
									.setText(Integer.toString(currChannelFrequenery));
						}
                        Log.d("Maxs107","-----3");
						viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
								.setText(getString(R.string.str_cha_dtvmanualtuning_ch) + currChannelNumName);
						return true;
					case R.id.linearlayout_cha_dtvmanualtuning_modulation:
						if (modulationindex == DvbcScanParam.DVBC_CAB_TYPE_QAM_16)
							modulationindex = DvbcScanParam.DVBC_CAB_TYPE_QAM_256;
						else
							modulationindex--;
						CAB_Type = modulationindex;
						viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_modulation_val
								.setText(modulationtype[modulationindex]);
						break;
					case R.id.linearlayout_cha_dtvmanualtuning_symbol:
						break;
			/*
			 * case R.id.linearlayout_cha_dtvmanualtuning_frequency: mFrequency
			 * = (mFrequency + mMaxFrequencyNumber - 1) % mMaxFrequencyNumber;
			 * updatedtvManualtuningComponents(); break;
			 */
					case R.id.linearlayout_cha_dtvmanualtuning_starttuning: {
						if (Tools.isBox()) {
							Log.d(TAG, "dtv manual tuning for box");
							// wait for the tuning done
							if (mIsDtvTuning) {
								Toast.makeText(this, R.string.wait_for_tuning_hint,
										Toast.LENGTH_SHORT).show();
							} else {
								startdtvmanutuning();
							}
							return super.onKeyDown(keyCode, event);
						}
						Log.d(TAG, "dtv manual tuning");
						// Hisa 2016.03.04 add Freeze function start
				/*Intent intentCancel = new Intent();//È¡Ïû¾²Ïñ²Ëµ¥
				intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				sendBroadcast(intentCancel);  */
						// Hisa 2016.03.04 add Freeze function end
						isSearchByUser = true;
						startdtvmanutuning();
					}
					return true;
					default:
						break;
				}
				break;
			case KeyEvent.KEYCODE_0:
			case KeyEvent.KEYCODE_1:
			case KeyEvent.KEYCODE_2:
			case KeyEvent.KEYCODE_3:
			case KeyEvent.KEYCODE_4:
			case KeyEvent.KEYCODE_5:
			case KeyEvent.KEYCODE_6:
			case KeyEvent.KEYCODE_7:
			case KeyEvent.KEYCODE_8:
			case KeyEvent.KEYCODE_9:
				switch (currentid) {
					case R.id.linearlayout_cha_dtvmanualtuning_channelnum:
						inputDigit++;
						if (inputDigit > 2) {
							inputDigit = 0;
							channelNumberInput = 0;
                            currChannelNum = channelNumberInput;
							updatedtvManualtuningComponents(false);
							return true;
						}

						int inputnum = keyCode - KeyEvent.KEYCODE_0;

						if (inputDigit == 1) {
							channelNumberInput = inputnum;
						} else if (inputDigit == 2) {
							channelNumberInput = channelNumberInput * 10 + inputnum;
						}
						String str_num;
						str_num = Integer.toString(channelNumberInput);
                        currChannelNum = channelNumberInput;
                        Log.d("Maxs107","-----4");
						viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
								.setText(getString(R.string.str_cha_dtvmanualtuning_ch) + str_num);
						mDirectChangeChTimeout
								.removeCallbacks(mDirectChangeChTimeoutRunnable);
						mDirectChangeChTimeout.postDelayed(
								mDirectChangeChTimeoutRunnable,
								USER_CHANGE_CHANNEL_TIMEOUT);
						return true;
					case R.id.linearlayout_cha_dtvmanualtuning_frequency:
						inputfreq = keyCode - KeyEvent.KEYCODE_0;
						//inputFrequencyNumber(inputfreq);
						return true;
					case R.id.linearlayout_cha_dtvmanualtuning_symbol:
						inputsymbol = keyCode - KeyEvent.KEYCODE_0;
						inputSymbolNumber(inputsymbol);
						return true;
					default:
						break;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				switch (currentid) {
					case R.id.linearlayout_cha_dtvmanualtuning_frequency: {
						Log.d("Maxs106", "<----KEYCODE_DPAD_UP:frequency--->");
						int freq = 0;
						if (strfreq != null) {
							if (strfreq.length() > 3 && strfreq.length() < 7) {
								freq = Integer.parseInt(strfreq);
								if (freq > 858000L || strfreq.length() >= 7) {
									mFrequency = 858000;
								} else if (freq < 52000) {
									mFrequency = 52000;
								} else {
									mFrequency = freq;
								}
							}
							strfreq = null;
							fretonu(mFrequency);
						}
						break;
					}
					case R.id.linearlayout_cha_dtvmanualtuning_channelnum:
						Log.d("Maxs106", "<----KEYCODE_DPAD_UP:channelnum--->");
						break;
				}


			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			switch (currentid) {
				case R.id.linearlayout_cha_dtvmanualtuning_frequency: {
					Log.d("Maxs106", "<----KEYCODE_DPAD_DOWN:frequency--->");
					int freq = 0;
					if (strfreq != null) {
						if (strfreq.length() > 3 && strfreq.length() < 7) {
							freq = Integer.parseInt(strfreq);
							if (freq > 858000L || strfreq.length() >= 7) {
								mFrequency = 858000;
							} else if (freq < 52000) {
								mFrequency = 52000;
							} else {
								mFrequency = freq;
							}
						}
						strfreq = null;
						fretonu(mFrequency);
					}
					break;
				}
				case R.id.linearlayout_cha_dtvmanualtuning_channelnum:
					RfInfo rfInfo = TvChannelManager.getInstance().getRfInfo(
							TvChannelManager.NEXT_RF, currChannelNum);
					Log.d("Maxs106", "<----KEYCODE_DPAD_DOWN:channelnum--->before:currChannelNum = " +currChannelNum);
					if (rfInfo != null) {
                        updatedtvManualtuningComponents(true);
						///currChannelNum = rfInfo.rfPhyNum;
						Log.d("Maxs106", "<----KEYCODE_DPAD_DOWN:channelnum--->after:currChannelNum = " +currChannelNum);
					/*viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val
							.setText(Integer.toString(rfInfo.frequency));*/
						///mFrequency = rfInfo.frequency;
						Log.d("Maxs106", "<----KEYCODE_DPAD_DOWN:channelnum--->currChannelNum = " +currChannelNum);
					}
					break;
			}
			break;

		case KeyEvent.KEYCODE_ENTER:
		case KeyEvent.KEYCODE_TV_INPUT:
			switch (currentid) {
			case R.id.linearlayout_cha_dtvmanualtuning_starttuning: {
				if (Tools.isBox()) {
					Log.d(TAG, "dtv manual tuning for box");
					// wait for the tuning done
					if (mIsDtvTuning) {
						Toast.makeText(this, R.string.wait_for_tuning_hint,
								Toast.LENGTH_SHORT).show();
					} else {
						startdtvmanutuning();
					}
					return super.onKeyDown(keyCode, event);
				}
				Log.d(TAG, "dtv manual tuning");
				// Hisa 2016.03.04 add Freeze function start
				///Intent intentCancel = new Intent();//È¡Ïû¾²Ïñ²Ëµ¥
				///intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				///sendBroadcast(intentCancel);
				// Hisa 2016.03.04 add Freeze function end
				isSearchByUser = true;
				startdtvmanutuning();
			}
				return true;
			case R.id.linearlayout_cha_dtvmanualtuning_frequency: {
				int freq = 0;
				if (strfreq != null) {
					if (strfreq.length() > 3 && strfreq.length() < 7) {
						freq = Integer.parseInt(strfreq);
						if (freq > 858000L || strfreq.length() >= 7) {
							mFrequency = 858000;
						} else if (freq < 52000) {
							mFrequency = 52000;
						} else {
							mFrequency = freq;
						}
					}
					strfreq = null;
					fretonu(mFrequency);
				}
			}
				return true;
			// ////////////////////////////////////////////zjd,20140711
			case R.id.linearlayout_cha_dtvmanualtuning_channelnum:
				if (inputDigit > 0) {
					inputDigit = 0;
					if (channelNumberInput <= CHANNEL_MAX_AIR && channelNumberInput > 0) {
						if ((TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDvbtRouteIndex)
								|| (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDtmbRouteIndex)) {
							RfInfo rfInfo = null;
							rfInfo = TvChannelManager.getInstance().getRfInfo(
									TvChannelManager.RF_INFO, channelNumberInput);
							if (INVALID_PHYSICAL_CHANNEL_NUMBER == rfInfo.rfPhyNum){//无效的频道号
								currChannelFrequenery = mPreviousFrequency;
								currChannelNum = mPreviousChannelNumber;
								currChannelNumName = mPreviousChannelNumName;
							}else{
								currChannelNum = rfInfo.rfPhyNum;
								currChannelFrequenery = rfInfo.frequency;
								currChannelNumName = rfInfo.rfName;
								mPreviousFrequency = currChannelFrequenery;
								mPreviousChannelNumber = currChannelNum;
								mPreviousChannelNumName = currChannelNumName;
							}

							viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val
									.setText(Integer.toString(currChannelFrequenery));
						}
                        Log.d("Maxs107","-----5");
						viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
								.setText(getString(R.string.str_cha_dtvmanualtuning_ch) + currChannelNumName);
					}else
					{
                        Log.d("Maxs107","-----6");
						viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
						.setText(getString(R.string.str_cha_dtvmanualtuning_ch) + currChannelNumName);
					}
						return true;
					
				}

				return true;
			// ///////////////////////////////////////////////////
			default:
				break;
			}
			break;
		case KeyEvent.KEYCODE_BACK:
		case KeyEvent.KEYCODE_MENU:
			intent.setAction(TvIntent.MAINMENU);
			///intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
			///startActivity(intent);
			finish();
			return true;
		default:
			break;
		}
		if (KeyEvent.KEYCODE_TV_INPUT == keyCode)
			if (bManualScanByUser)
			return true;
			else
			finish();
		return super.onKeyDown(keyCode, event);
	}

	private static final int FINISH_DELAY_FOR_IDLE = 60 * 1000;

	private static final int FINISH_DELAY_FOR_PROG_FOUND = 5 * 1000;

	// Whether search is triggered by user or auto start when enter this screen.
	private boolean isSearchByUser = false;

	private void startdtvmanutuning() {
		mIsDtvTuning = true;
		String Sfreq = viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val
				.getText().toString();
		String Ssymbol = viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_symbol_val
				.getText().toString();
		mSymbolRate = (short) Integer.parseInt(Ssymbol);
		mFrequency = Integer.parseInt(Sfreq);
		viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_start_state
				.setText(R.string.str_cha_atvmanualtuning_starttuning_state);
		Log.d(TAG, "symb=" + mSymbolRate + " freq=" + mFrequency);
			if (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDvbcRouteIndex) {
				TvChannelManager.getInstance().setDvbcScanParam((short) mSymbolRate,
						CAB_Type, 0, 0, (short) 0x0000);
				TvChannelManager.getInstance().setDtvManualScanByFreq(mFrequency * 1000);
				TvChannelManager.getInstance().startDtvManualScan();
			} else if (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDvbtRouteIndex) {
				TvChannelManager.getInstance().setDtvManualScanByRF(currChannelNum);
				TvChannelManager.getInstance().startDtvManualScan();
			} else if (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDtmbRouteIndex) {
				TvChannelManager.getInstance().setDtvManualScanByRF(currChannelNum);
				TvChannelManager.getInstance().startDtvManualScan();
			} 
		bManualScanByUser = true;
		mPreviousChannelNumber = currChannelNum;
        mPreviousFrequency = currChannelFrequenery;
		mPreviousChannelNumName = currChannelNumName;
	}

	private void getCurrentFreqAndSymRate() {
		/*
		 * if current dtv program count is not zero, use current frequency and
		 * symbol rate otherwise using default frequency and default symbol
		 * rate.
		 */
		int m_nServiceNum =TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
		if (m_nServiceNum > 0) {
			DvbMuxInfo dmi = TvChannelManager.getInstance().getCurrentMuxInfo();
			if (dmi != null) {
				// dvbs frequency needn't to divide , bacause it's already MHz.
				if (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDvbsRouteIndex) {
					mFrequency = dmi.frequency;
				} else {
					mFrequency = (dmi.frequency / 1000);
				}
				mSymbolRate = dmi.symbRate;
			} else {
				// use default value if get muxinfo error
				mFrequency = mDefaultFrequency;
				mSymbolRate = mDefaultSymbolRate;
				Log.e(TAG, "getCurrentMuxInfo error");
			}

		} else {
			mFrequency = mDefaultFrequency;
			mSymbolRate = mDefaultSymbolRate;
		}

	}

	private void updatedtvManualtuningComponents(boolean isExistInputNumber) {
		// set item show/hide
		viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_symbol_val
				.setText(Integer.toString(mSymbolRate));
		strfreq = null;
		inputDigit = 0; 
	    channelNumberInput = 0;
        Log.d("Maxs107","updatedtvManualtuningComponents:currChannelNum = " + currChannelNum);
        Log.d("Maxs107","updatedtvManualtuningComponents:isExistInputNumber = " + isExistInputNumber);
        Log.d("Maxs107","updatedtvManualtuningComponents:mPreviousChannelNumber = " + mPreviousChannelNumber);
        Log.d("Maxs107","updatedtvManualtuningComponents:currChannelFrequenery = " + currChannelFrequenery);
        Log.d("Maxs107","updatedtvManualtuningComponents:mPreviousFrequency = " + mPreviousFrequency);
		/*viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val
				.setText(Integer.toString(mFrequency));*/
		if ((TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDvbtRouteIndex)
				|| (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDtmbRouteIndex)) {
			RfInfo rfInfo = null;
            if (isExistInputNumber){
                //判断当前频道号是否有效
                rfInfo = TvChannelManager.getInstance().getRfInfo(
                        TvChannelManager.RF_INFO, currChannelNum);
            }else{
                //获取当前频道号
                rfInfo = TvChannelManager.getInstance().getRfInfo(
                        TvChannelManager.FIRST_TO_SHOW_RF, 0);

            }
			if (rfInfo != null) {
                Log.d("Maxs107","--->updatedtvManualtuningComponents:rfInfo.rfPhyNum = " + rfInfo.rfPhyNum);
                if (rfInfo.rfPhyNum == INVALID_PHYSICAL_CHANNEL_NUMBER){
                    currChannelNum = mPreviousChannelNumber;
                    currChannelFrequenery = mPreviousFrequency;
					currChannelNumName = mPreviousChannelNumName;
                }else{
                    currChannelNum = rfInfo.rfPhyNum;
                    currChannelFrequenery = rfInfo.frequency;
					currChannelNumName = rfInfo.rfName;
                    mPreviousChannelNumber = currChannelNum;
                    mPreviousFrequency = currChannelFrequenery;
					mPreviousChannelNumName = currChannelNumName;
                }

                /*currChannelNum = rfInfo.rfPhyNum;
                currChannelFrequenery =rfInfo.frequency;*/
                if ((TvChannelManager.getInstance().getCurrentDtvRouteIndex() != mDvbtRouteIndex) &&
                        (TvChannelManager.getInstance().getCurrentDtvRouteIndex() != mDtmbRouteIndex)) {

                } else {

                }
				viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val
				.setText("" +currChannelFrequenery);
                Log.d("Maxs107","-----7");
				viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
						.setText(getString(R.string.str_cha_dtvmanualtuning_ch) + currChannelNumName);
				mPreviousChannelNumber = currChannelNum;
                mPreviousFrequency = currChannelFrequenery;
				mPreviousChannelNumName = currChannelNumName;
			}
			/*LinearLayout ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_frequency);
			ln.setVisibility(View.GONE);*/
			LinearLayout ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_modulation);
			ln.setVisibility(View.GONE);
			ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_symbol);
			ln.setVisibility(View.GONE);
		} else if (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDvbsRouteIndex) {
			LinearLayout ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_channelnum);
			ln.setVisibility(View.GONE);
			ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_modulation);
			ln.setVisibility(View.GONE);
		} else {
			LinearLayout ln = (LinearLayout) findViewById(R.id.linearlayout_cha_dtvmanualtuning_channelnum);
			ln.setVisibility(View.GONE);
		}
	}

	private void setProgressValueForSignalQuality(int val) {
		if (val <= 10 && val > 0) {
			for (int i = 0; i <= val - 1; i++) {
				ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
						.getChildAt(i));
				searchImage.setImageDrawable(getResources().getDrawable(
						R.drawable.picture_serchprogressbar_solid));
			}
			for (int i = val; i <= 9; i++) {
				ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
						.getChildAt(i));
				searchImage.setImageDrawable(getResources().getDrawable(
						R.drawable.picture_serchprogressbar_empty));
			}
		} else if (val > 10) {
			for (int i = 0; i <= 9; i++) {
				ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
						.getChildAt(i));
				searchImage.setImageDrawable(getResources().getDrawable(
						R.drawable.picture_serchprogressbar_solid));
			}
		} else if (val == 0) {
			for (int i = 0; i <= 9; i++) {
				ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
						.getChildAt(i));
				searchImage.setImageDrawable(getResources().getDrawable(
						R.drawable.picture_serchprogressbar_empty));
			}
		}
	}

	private void InitialProgressValueForSignalQuality() {
		for (int i = 0; i <= 9; i++) {
			ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalquality_val
					.getChildAt(i));
			searchImage.setImageDrawable(getResources().getDrawable(
					R.drawable.picture_serchprogressbar_empty));
		}
	}

	private void setProgressValueForSignalStrengh(int val) {

		if (val <= 10 && val > 0) {
			for (int i = 0; i <= val - 1; i++) {
				ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
						.getChildAt(i));
				searchImage.setImageDrawable(getResources().getDrawable(
						R.drawable.picture_serchprogressbar_solid));
			}
			for (int i = val; i <= 9; i++) {
				ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
						.getChildAt(i));
				searchImage.setImageDrawable(getResources().getDrawable(
						R.drawable.picture_serchprogressbar_empty));
			}
		} else if (val > 10) {
			for (int i = 0; i <= 9; i++) {
				ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
						.getChildAt(i));
				searchImage.setImageDrawable(getResources().getDrawable(
						R.drawable.picture_serchprogressbar_solid));
			}
		} else if (val == 0) {
			for (int i = 0; i <= 9; i++) {
				ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
						.getChildAt(i));
				searchImage.setImageDrawable(getResources().getDrawable(
						R.drawable.picture_serchprogressbar_empty));
			}
		}
	}

	private void InitialProgressValueForSignalStrengh() {
		for (int i = 0; i <= 9; i++) {
			ImageView searchImage = (ImageView) (viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_signalstrength_val
					.getChildAt(i));
			searchImage.setImageDrawable(getResources().getDrawable(
					R.drawable.picture_serchprogressbar_empty));
		}
	}

	private void inputFrequencyNumber(int inputno) {
		int freq = 0;
		if(strfreq != null)
			strfreq = strfreq + Integer.toString(inputno);
			else strfreq = Integer.toString(inputno);
		freq = Integer.parseInt(strfreq);
		if (freq > mMaxFrequencyNumber) {
			strfreq = Integer.toString(inputno);
			// mFrequency = inputno;
		} else if (strfreq.length() >= mFrequencyDigitsBound) {
			strfreq = Integer.toString(inputno);
			// mFrequency = inputno;
		} else {
			// mFrequency = freq;
		}
		viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val
				.setText(strfreq);
		return;
	}

	private void inputSymbolNumber(int inputno) {
		short symbol = 0;
		strsymbol = strsymbol + Integer.toString(inputno);
		symbol = (short) Integer.parseInt(strsymbol);
		if (strsymbol.length() >= mSymbolRateDigitsBound) {
			strsymbol = Integer.toString(inputno);
			mSymbolRate = (short) inputno;
		} else {
			mSymbolRate = symbol;
		}
		viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_symbol_val
				.setText(strsymbol);
		return;
	}

	@Override
	protected void onResume() {
		super.onResume();

		mDtvEventListener = new DtvEventListener();
		TvChannelManager.getInstance().registerOnDtvPlayerEventListener(
				mDtvEventListener);
	}

	@Override
	protected void onPause() {
		// When the DTV searching is interrupted
		if (bManualScanByUser) {
			TvChannelManager.getInstance().stopDtvScan();
			if (!mOnAutoSwitch) {
				TvChannelManager.getInstance().changeToFirstService(
						TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
						TvChannelManager.FIRST_SERVICE_DEFAULT);
			}
		}
		TvChannelManager.getInstance().unregisterOnDtvPlayerEventListener(
				mDtvEventListener);
		mDtvEventListener = null;

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mRunthread = false;
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	private void updateDtvTuningScanInfo(DtvEventScan extra) {
		String str;

		if (isSearchByUser) {
			str = "" + extra.dtvSrvCount;
			viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_tuningresult_dtv_val
					.setText(str);

			str = "" + extra.radioSrvCount;
			viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_tuningresult_radio_val
					.setText(str);

			str = "" + extra.dataSrvCount;
			viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_tuningresult_data_val
					.setText(str);
		}

		setProgressValueForSignalQuality((extra.signalQuality / 10));
		setProgressValueForSignalStrengh((extra.signalStrength / 10));

		if (extra.scanStatus == EnumDtvScanStatus.E_STATUS_SCAN_END.ordinal()) {
			viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_start_state
					.setTextColor(Color.GRAY);
			viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_start_state
					.setText(R.string.str_cha_atvmanualtuning_starttuning_state_reminder);
				if ((extra.dtvSrvCount + extra.radioSrvCount + extra.dataSrvCount) > 0) {
					TvChannelManager.getInstance().changeToFirstService(
							TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
							TvChannelManager.FIRST_SERVICE_DEFAULT);
					bManualScanByUser = false;

					int delay = FINISH_DELAY_FOR_IDLE;
					if ((extra.dtvSrvCount + extra.radioSrvCount + extra.dataSrvCount) > 0
							&& isSearchByUser) {
						delay = FINISH_DELAY_FOR_PROG_FOUND;
					}
				}
				isSearchByUser = false;
		}
	}

	private void fretonu(int u32Frequency) {
		Log.e("mstar.tvsetting.ui", "u32Frequency:" + u32Frequency);
		if(u32Frequency <= 52500L )currChannelNum = 1;
		else if(u32Frequency <= 60500L )currChannelNum = 2;
		else if(u32Frequency <= 68500L )currChannelNum = 3;
		else if(u32Frequency <= 80000L )currChannelNum = 4;
		else if(u32Frequency <= 88000L )currChannelNum = 5;
		else if(u32Frequency <= 171000L )currChannelNum = 6;
		else if(u32Frequency <= 179000L )currChannelNum = 7;
		
		else if(u32Frequency <= 187000L )currChannelNum = 8;
		else if(u32Frequency <= 195000L )currChannelNum = 9;
		else if(u32Frequency <= 203000L )currChannelNum = 10;
		else if(u32Frequency <= 211000L )currChannelNum = 11;
		else if(u32Frequency <= 219000L )currChannelNum = 12;
		else if(u32Frequency <= 474000L )currChannelNum = 13;
		else //if(u32Frequency <= 610000L )
        {
        	currChannelNum = (int)(((u32Frequency - 474000L) / 8000L) + 13);
        }
//        else
//        {
//        	currChannelNum = (int)(((u32Frequency - 610000L) / 8000L) + 25);
//        }
        Log.e("TvApp", "currChannelNum:" + currChannelNum);
        if ((TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDvbtRouteIndex)
				|| (TvChannelManager.getInstance().getCurrentDtvRouteIndex() == mDtmbRouteIndex)) {
        	RfInfo rfInfo = null;
			rfInfo = TvChannelManager.getInstance().getRfInfo(
					TvChannelManager.RF_INFO, currChannelNum);

			if (INVALID_PHYSICAL_CHANNEL_NUMBER == rfInfo.rfPhyNum){//无效的频道号
				currChannelFrequenery = mPreviousFrequency;
				currChannelNum = mPreviousChannelNumber;
				currChannelNumName = mPreviousChannelNumName;
			}else{
				currChannelNum = rfInfo.rfPhyNum;
				currChannelFrequenery = rfInfo.frequency;
				currChannelNumName= rfInfo.rfName;
				mPreviousFrequency = currChannelFrequenery;
				mPreviousChannelNumber = currChannelNum;
				mPreviousChannelNumName = currChannelNumName;
			}

			/*currChannelNum = rfInfo.rfPhyNum;
            mPreviousChannelNumber = currChannelNum;
            currChannelFrequenery = rfInfo.frequency;
            mPreviousFrequency = currChannelFrequenery;*/
			viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_frequency_val
					.setText(Integer.toString(currChannelFrequenery));
		}
		Log.d("Maxs107","-----1");
		viewholder_dtvmanualtuning.text_cha_dtvmanualtuning_channelnum_val
				.setText(getString(R.string.str_cha_dtvmanualtuning_ch) + currChannelNumName);
		return;
	}
	
	private void setOnFocusChangeListeners() {
		OnFocusChangeListener FocuschangesListener = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				LinearLayout linear = (LinearLayout) v;
				if (linear.getId() == R.id.linearlayout_cha_dtvmanualtuning_channelnum){
						if (!linear.isSelected()) {
							linear.getChildAt(1).setVisibility(View.VISIBLE);
							linear.getChildAt(linear.getChildCount() - 1)
									.setVisibility(View.VISIBLE);
							linear.setSelected(true);
						} else {
							linear.getChildAt(1).setVisibility(View.INVISIBLE);
							linear.getChildAt(linear.getChildCount() - 1)
									.setVisibility(View.INVISIBLE);
							linear.setSelected(false);
						}
				}



				if(inputDigit != 0 && channelNumberInput != 0) {
                    ///updatedtvManualtuningComponents(false);
                }
			}
		};
		viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_channelfreq.setOnFocusChangeListener(FocuschangesListener);
		viewholder_dtvmanualtuning.linear_cha_dtvmanualtuning_channelnum.setOnFocusChangeListener(FocuschangesListener);
		
	}
}

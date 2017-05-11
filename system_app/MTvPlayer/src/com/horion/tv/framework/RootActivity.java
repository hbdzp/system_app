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

package com.horion.tv.framework;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.os.SystemProperties;

import com.horion.tv.framework.ui.dialog.SkyTVDialog;
import com.horion.tv.ui.MainMenuActivity;
import com.horion.tv.ui.search.ChannelTuning;
import com.horion.tv.ui.search.FirstSearchActivity;
import com.horion.tv.util.Constants;
import com.horion.tv.util.DataBaseHelper;
import com.horion.tv.util.PropHelp;
import com.horion.tv.util.Tools;
import com.horion.tv.util.TvEvent;
import com.horion.tv.util.Utility;
import com.horion.tv.widget.SkyImgLoadingView;
import com.ktc.framework.ktcsdk.ipc.KtcApplication;
import com.ktc.ui.api.SkyDialogView;
import com.ktc.ui.api.SkyDialogView.OnDialogOnKeyListener;
import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvCcManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCiManager.OnCiStatusChangeEventListener;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.horion.tv.R;
import com.horion.tv.framework.ui.TvIntent;
import com.horion.tv.ui.search.LittleDownTimer;
import com.horion.tv.util.DataBaseUtil;
import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvCiManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCountry;
import com.mstar.android.tv.TvGingaManager;
import com.mstar.android.tv.TvHbbTVManager;
import com.mstar.android.tv.TvLanguage;
import com.mstar.android.tv.TvMhlManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tv.widget.TvView;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.android.tvapi.common.listener.OnTvEventListener;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.common.vo.EnumPipModes;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tv.TvCiManager.OnUiEventListener;
import com.mstar.android.tv.TvCecManager.OnCecCtrlEventListener;
import com.horion.tv.util.Constants.ScreenSaverMode;
import com.horion.tv.util.Constants.SignalProgSyncStatus;
import com.mstar.android.tvapi.common.vo.EnumThreeDVideoDisplayFormat;
public class RootActivity extends MstarUIActivity implements

OnKeyListener{
	private static final String TAG = "Maxs";
	private boolean log = true;

	// 15min.
	private static final int NO_SIGNAL_SHUTDOWN_TIME = 15 * 60 * 1000;

	private static final int SCREENSAVER_DEFAULT_STATUS = -1;

	public boolean bCmd_TvApkExit = false;

	private TvView tvView = null;

	private boolean mIsSignalLock = true;

	private static boolean mIsActive = false;

	private static boolean mIsBackKeyPressed = false;

	private volatile int mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;

	private static boolean mIsScreenSaverShown = false;

	private static boolean mIsMsrvStarted = false;

	private static String TV_APK_START = "com.mstar.tv.ui.tvstart";

	private static String TV_APK_END = "com.mstar.tv.ui.tvend";

	private TvPictureManager mTvPictureManager = null;

	private TvS3DManager mTvS3DManager = null;

	private TvMhlManager mTvMhlManager = null;
	private boolean is_source_chanage =false;
	private TvCecManager mTvCecManager = null;
	
	private TvCommonManager mTvCommon = null;

	private static boolean isFirstPowerOn = true;

	private static int systemAutoTime = 0;

	private boolean mIsExiting = false;

	private final int mCecStatusOn = 1;

	private int mPreviousInputSource = TvCommonManager.INPUT_SOURCE_NONE;

	protected static AlertDialog mExitDialog;
	private SkyTVDialog skyTVDialog;
	private RelativeLayout rootView;
	// now close 3D function, when open, it
	private boolean _3Dflag = false;

	// shall be deleted
	private static RootActivity rootActivity = null;

	private static boolean isReboot = false;

	private boolean mIskeyLocked = false;

	private PowerManager mPowerManager = null;

	private AlertDialog mCiPlusOPRefreshDialog = null;

	private BroadcastReceiver mReceiver = null;

	private OnDtvPlayerEventListener mDtvPlayerEventListener = null;

	private OnAtvPlayerEventListener mAtvPlayerEventListener = null;

	private OnTvPlayerEventListener mTvPlayerEventListener = null;
	private OnMhlEventListener mMhlEventListener = null;

	private OnTvEventListener mTvEventListener = null;

	private OnUiEventListener mUiEventListener = null;

	private OnCiStatusChangeEventListener mCiStatusChangeEventListener = null;

	private OnCecCtrlEventListener mCecCtrlEventListener = null;

	private Toast mCcKeyToast;

	private LinearLayout signalstate = null;
	private SkyImgLoadingView mSignalSearch = null;
	private TextView mNosignalText = null;

	//private TextView mFreezeView = null;

	private boolean mIsPowerOn = false;

	private boolean mIsToPromptPassword = false;

	private DataBaseUtil mDataBaseUtil;
	private final int CEC_INFO_DISPLAY_TIMEOUT = 1000;
//==================ktc add ==============
	private boolean isOnPauseFlag = false;
	private int SCREEN_WIDTH;
	private int SCREEN_HEIGHT;
	private int TEXT_WIDTH;
	private int TEXT_HEIGHT;
	private int posX;
	private int posY;
	private int X_STEP = 2;
	private int Y_STEP = 2;
	private static final int REFRESH_POS = 1001;
	private Timer mRefreshTimer = null;
	//ktc nathan.liao 20140905 for hotelmenu start
	private View rllyPassword;
	private LinearLayout llyPasswdContainer;
	private String[] mEnterPassword;
	private Button[] mPasswdViews;
	private int[] mPasswdIds;
	boolean isShowPsd = false;
	//mengwt 20141014 
	boolean disableNoSignal=false;
	//mengwt 20141014
	boolean isHotelMenuLockFlag = false;//nathan.liao 2014.11.11
	//ktc nathan.liao 20140905 for hotelmenu end
		boolean isFromUSB = false;
		
		private boolean mIsCecDialogCanceled = false;
		private ProgressDialog mCecInfoDialog = null;
	private final String IS_POWER_ON_PROPERTY = "mstar.launcher.1stinit";

//==================ktc end ==============
	private static boolean isRootActive = false;

	private class DtvPlayerEventListener implements OnDtvPlayerEventListener {

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
		public boolean onDtvAutoTuningScanInfo(int arg0, DtvEventScan arg1) {
			// TODO Auto-generated method stub
			return false;
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
			return true;
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
			if(log)
				Log.d(TAG, "SIGNAL Lock***");
            Log.d("Maxs105","DtvPlayerEventListener:onSignalLock ");
				Message m = Message.obtain();
				m.arg1 = TvEvent.SIGNAL_LOCK;
				signalLockHandler.sendMessage(m);
				return true;
		}

		@Override
		public boolean onSignalUnLock(int arg0) {
			if(log)
				Log.d(TAG, "SIGNAL UnLock***");
            Log.d("Maxs105","DtvPlayerEventListener:onSignalUnLock ");
				Message m = Message.obtain();
				m.arg1 = TvEvent.SIGNAL_UNLOCK;
				signalLockHandler.sendMessage(m);
				// Hisa 2016.03.04 add Freeze function start
				Log.d("Jason","无信号状态");	
				Intent intent = new Intent();//取消静像菜单
					///intent.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
					TvPictureManager.getInstance().unFreezeImage();
					sendBroadcast(intent);  
				// Hisa 2016.03.04 add Freeze function end
				return true;
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
			if(log)
				Log.i(TAG, "get CI+ OP event EV_CI_OP_REFRESH_QUERY");
				TvCiManager.getInstance().ciClearOPSearchSuspended();
				displayOpRefreshconfirmation();
				return true;
		}

		@Override
		public boolean onUiOPServiceList(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}
	}

	private class AtvPlayerEventListener implements OnAtvPlayerEventListener {

		@Override
		public boolean onAtvProgramInfoReady(int what) {
			return false;
		}

		@Override
		public boolean onAtvAutoTuningScanInfo(int arg0, AtvEventScan arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onAtvManualTuningScanInfo(int arg0, AtvEventScan arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onSignalLock(int arg0) {
			Log.d(TAG, "K_SIGNAL Lock***");
            Log.d("Maxs105","AtvPlayerEventListener:onSignalLock ");
			Message m = Message.obtain();
			m.arg1 = TvEvent.SIGNAL_LOCK;
			signalLockHandler.sendMessage(m);
			return false;
		}

		@Override
		public boolean onSignalUnLock(int arg0) {
			if(log)
			Log.d(TAG, "K_SIGNAL UnLock***");
            Log.d("Maxs105","AtvPlayerEventListener:onSignalUnLock ");
			Message m = Message.obtain();
			m.arg1 = TvEvent.SIGNAL_UNLOCK;
			signalLockHandler.sendMessage(m);
			return true;
		}
	}
	private class MhlEventListener implements OnMhlEventListener{
		Intent intent;
		@Override
		public boolean onAutoSwitch(int arg0, final int arg1, int arg2) {
			if(log)
				Log.d(TAG, "onAutoSwitch");
				intent = new Intent(TvIntent.ACTION_START_ROOTACTIVITY);
				intent.putExtra("task_tag", "input_source_changed");
				new Thread(new Runnable() {
					@Override
					public void run() {
						mTvS3DManager
								.setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
						Log.d("Maxs250","RootActivity:onAutoSwitch:setInputSource:inputsource = " + arg1);
						TvCommonManager.getInstance().setInputSource(arg1);
						startActivity(intent);
					}
				}).start();
				return false;
		}

		@Override
		public boolean onKeyInfo(int arg0, int arg1, int arg2) {
			if(log)
				Log.d(TAG, "onKeyInfo");
				return false;
		}
		
	}
	private class TvPlayerEventListener implements OnTvPlayerEventListener {

		@Override
		public boolean on4k2kHDMIDisableDualView(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean on4k2kHDMIDisablePip(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean on4k2kHDMIDisablePop(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean on4k2kHDMIDisableTravelingMode(int arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDtvChannelInfoUpdate(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDtvPsipTsUpdate(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onEmerencyAlert(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onEpgUpdateList(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onHbbtvUiEvent(int arg0, HbbtvEventInfo arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPopupDialog(int arg0, int arg1, int arg2) {
			Log.d(TAG, "onPopupDialog(" + arg1 + "," + arg2 + ")");
			if (TvCommonManager.POPUP_DIALOG_SHOW == arg1) {
				mIsToPromptPassword = true;
				if (true == mIsActive) {
				}
			} else if (TvCommonManager.POPUP_DIALOG_HIDE == arg1) {
				mIsToPromptPassword = false;
			}
			return true;
		}

		@Override
		public boolean onPvrNotifyAlwaysTimeShiftProgramNotReady(int arg0) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean onPvrNotifyAlwaysTimeShiftProgramReady(int arg0) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean onPvrNotifyCiPlusProtection(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyCiPlusRetentionLimitUpdate(int arg0,
				int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyOverRun(int arg0) {
			RootActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast toast = Toast.makeText(RootActivity.this,
							R.string.str_disk_too_slow, Toast.LENGTH_LONG);
					toast.show();
				}
			});
			return true;
		}

		@Override
		public boolean onPvrNotifyParentalControl(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyPlaybackBegin(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyPlaybackSpeedChange(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyPlaybackStop(int arg0) {
			RootActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast toast = Toast.makeText(RootActivity.this,
							"pvr playback is stopped", Toast.LENGTH_SHORT);
					toast.show();
				}
			});
			return true;
		}

		@Override
		public boolean onPvrNotifyPlaybackTime(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyRecordSize(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyRecordStop(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyRecordTime(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyTimeShiftOverwritesAfter(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyTimeShiftOverwritesBefore(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onPvrNotifyUsbRemoved(int arg0, int arg1) {
			RootActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast toast = Toast.makeText(RootActivity.this,
							"The disk is full, please check the disk capacity",
							Toast.LENGTH_LONG);
					toast.show();
				}
			});
			return true;
		}

		@Override
		public boolean onScreenSaverMode(int arg0, int arg1) {
			// TODO Auto-generated method stub
			mScreenSaverStatus = arg1;
			if(log)
			{
			Log.d(TAG, "EV_SCREEN_SAVER_MODE***");
			Log.d("Jason", "onScreenSaverMode receive status:" + mScreenSaverStatus);
			}
			// getCurrentInputSource takes much time so leave it to subthread.
			new Thread() {
				public void run() {
					int curInputSource = TvCommonManager.getInstance()
							.getCurrentTvInputSource();
					Message m = Message.obtain();
					m.arg1 = mScreenSaverStatus;
					m.arg2 = curInputSource;
					if (!isHotelMenuLockFlag) {	
						screenSaverHandler.sendMessage(m);
					}
				};
			}.start();

			return true;
		}

		@Override
		public boolean onSignalLock(int arg0) {
			//if(log)
			Log.d(TAG, "K_SIGNAL Lock***");
			Message m = Message.obtain();
			m.arg1 = TvEvent.SIGNAL_LOCK;
			signalLockHandler.sendMessage(m);
			return true;
		}

		@Override
		public boolean onSignalUnLock(int arg0) {
			//if(log)
			Log.d(TAG, "K_SIGNAL UnLock***");
			Message m = Message.obtain();
			m.arg1 = TvEvent.SIGNAL_UNLOCK;
			signalLockHandler.sendMessage(m);
			return true;
		}

		@Override
		public boolean onTvProgramInfoReady(int arg0) {
			Message msg = mHandler.obtainMessage();
            msg.what = Constants.ROOTACTIVITY_TVPROMINFOREADY_MESSAGE;
            mHandler.sendMessage(msg);
            return true;
		}
	}

	private class TvEventListener implements OnTvEventListener {

		@Override
		public boolean on4k2kHDMIDisableDualView(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean on4k2kHDMIDisablePip(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean on4k2kHDMIDisablePop(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean on4k2kHDMIDisableTravelingMode(int arg0, int arg1,
				int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onAtscPopupDialog(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDeadthEvent(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onDtvReadyPopupDialog(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScartMuteOsdMode(int arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onScreenSaverMode(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onSignalLock(int arg0) {
			if(log)
				Log.d(TAG, "SIGNAL Lock***");
            Log.d("Maxs105","TvEventListener:onSignalLock ");
				Message m = Message.obtain();
				m.arg1 = TvEvent.SIGNAL_LOCK;
				signalLockHandler.sendMessage(m);
				return true;
		}

		@Override
		public boolean onSignalUnlock(int arg0) {
			if(log)
				Log.d(TAG, "SIGNAL UnLock***");
            Log.d("Maxs105","TvEventListener:onSignalUnlock ");
				Message m = Message.obtain();
				m.arg1 = TvEvent.SIGNAL_UNLOCK;
				signalLockHandler.sendMessage(m);
				return true;
		}

		@Override
		public boolean onUnityEvent(int arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public static RootActivity getInstance() {
		return rootActivity;
	}

	/**
	 * 1. update No signal activity 2. start TV apk
	 */
	protected Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == Constants.ROOTACTIVITY_RESUME_MESSAGE) {
				/*
				 * Modified by gerard.jiang for "0386249" in 2013/04/28. Add
				 * reboot flag When system is suspending (isScreenOn == false),
				 * do not start No Signal Activity. by Desmond Pu 2013/12/18
				 */
				Log.d("Maxs250","onReusme()5 = " + TvCommonManager.getInstance().getCurrentTvInputSource());
				if (_3Dflag == false && !isReboot
						&& (mPowerManager.isScreenOn()))
				/***** Ended by gerard.jiang 2013/04/28 *****/
				{
					Log.d("Maxs250","onReusme6() = " + TvCommonManager.getInstance().getCurrentTvInputSource());
					updateTvSourceSignal();
				}
				// Notfiy event queue to start sending pending event
				try {
					mTvCommon
							.setTvosCommonCommand(Constants.TV_EVENT_LISTENER_READY);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (msg.what == 900) {
				executePreviousTask(getIntent());

				SharedPreferences settings = getSharedPreferences(
						Constants.PREFERENCES_TV_SETTING, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("_3Dflag", _3Dflag);
				editor.commit();
				checkSystemAutoTime();
			} else if (msg.what == Constants.ROOTACTIVITY_TVPROMINFOREADY_MESSAGE) {
                int curInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
                if ((TvCommonManager.INPUT_SOURCE_CVBS <= curInputSource)
                        && (TvCommonManager.INPUT_SOURCE_CVBS_MAX > curInputSource)
                        || (TvCommonManager.INPUT_SOURCE_YPBPR <= curInputSource)
                        && (TvCommonManager.INPUT_SOURCE_YPBPR_MAX > curInputSource)
                        || (TvCommonManager.INPUT_SOURCE_HDMI <= curInputSource)
                        && (TvCommonManager.INPUT_SOURCE_HDMI_MAX > curInputSource)) {
                    Log.d("Maxs29","startSourceInfo:8");
                    Utility.startSourceInfo(RootActivity.this);
                }
            } else if (msg.what == Constants.ROOTACTIVITY_CANCEL_DIALOG) {
                if (null != mCecInfoDialog) {
                    if (false == mIsCecDialogCanceled) {
                        mHandler.sendEmptyMessageDelayed(Constants.ROOTACTIVITY_CANCEL_DIALOG, CEC_INFO_DISPLAY_TIMEOUT);
                    } else {
                        mCecInfoDialog.dismiss();
                        mCecInfoDialog = null;
                        mIsCecDialogCanceled = false;
                    }
                }
            } else if (msg.what == Constants.ROOTACITIVYT_DELAY_SEARCH_CHANNLE){
				startChannelTunning();
				int curInputSource = mTvCommon.getCurrentTvInputSource();
				if(TvCommonManager.INPUT_SOURCE_STORAGE!=curInputSource)
				{
					mIsSignalLock = mTvCommon.isSignalStable(curInputSource);
					if(isOnPauseFlag = true)
					{
						isOnPauseFlag = false;
						if(!mIsSignalLock)
						{
							Log.d("Jason23","showNoSignalView:true:11");
							showNoSignalView(true);
						}
					}
				}
			}

		};
	};

	public boolean isBackKeyPressed() {
		return mIsBackKeyPressed;
	}
	/*public boolean isPowerOn() {
		Log.d(TAG, "Is Fist Power On: " + (SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)));
		Log.d("Maxs250","---->RootActivity:isPowerOn = " + (SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)));
		if (!SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)) {
			SystemProperties.set(IS_POWER_ON_PROPERTY, "true");
			return true;
		} else {
			return false;
		}
	}*/
	// ---------------------------------------------------
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(log)
		Log.i(TAG, "onCreate()");
		Log.d("Maxs250","RootActivity:onCreate");
		setContentView(R.layout.root);
		rootView = (RelativeLayout)findViewById(R.id.linear_layout_root);
		isRootActive = false;
		//ktc nathan.liao 20140905 for hotelmenu start
		mDataBaseUtil = DataBaseUtil.getInstance(this);
		rllyPassword = findViewById(R.id.rllyPassword);
		llyPasswdContainer = (LinearLayout)findViewById(R.id.llyPasswdContainer);
		int passwdLength = PropHelp.newInstance().getHotelChlockPasswd().length();

		mEnterPassword = new String[passwdLength];
		mPasswdIds = new int[passwdLength];
		mPasswdViews = new Button[passwdLength];
		isHotelMenuLockFlag = isCurrentChannelLock();
		LayoutInflater inflater = LayoutInflater.from(this);
		int passwdId = 10020;
		for (int i = 0; i < passwdLength; i++) {
			View view = inflater.inflate(R.layout.btn_passwd_item, null);
			Button button = (Button) view.findViewById(R.id.edtPasswd);
			int id = passwdId + i;
			button.setId(id);
			mPasswdIds[i] = id;
			mPasswdViews[i] = button;
			button.setOnKeyListener((OnKeyListener) this);
			int width = getResources().getDimensionPixelSize(
					R.dimen.hotel_passwd_item_width);
			LayoutParams params = new LayoutParams(width, width);
			llyPasswdContainer.addView(view, params);
		}
		isShowPsd = false;
		//ktc nathan.liao 20140905 for hotelmenu end
		rootActivity = this;
		_3Dflag = false;
		super.onCreate(savedInstanceState);
		//ktc nathan.liao 2016.05.17 add for issue ERP Q160513003 start
		mIsPowerOn = getIntent().getBooleanExtra("isPowerOn", false);
		getIntent().removeExtra("isPowerOn");

		/*if (isPowerOn()){
			mIsPowerOn = true;
		}*/
		//ktc nathan.liao 2016.05.17 add for issue ERP Q160513003 end
		SCREEN_HEIGHT = getWindowManager().getDefaultDisplay().getHeight();
		SCREEN_WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		mTvCommon = TvCommonManager.getInstance();
		mTvPictureManager = TvPictureManager.getInstance();
		mTvS3DManager = TvS3DManager.getInstance();
		// tvAudioManager =TvAudioManager.getInstance();
		mTvCecManager = TvCecManager.getInstance();
		signalstate = (LinearLayout)findViewById(R.id.signalstate);
		mSignalSearch = (SkyImgLoadingView)findViewById(R.id.nosignalsearch);
		mNosignalText = (TextView) findViewById(R.id.nosiganltext);
		mNosignalText.setText(R.string.str_no_signal);
		///mNosignalText.setBackground(getResources().getDrawable(R.drawable.ui_sdk_other_page_bg_pe));
		////mNosignalText.setText(R.string.str_null);
		createSurfaceView();
		mIsMsrvStarted = true;
		mHandler.sendEmptyMessage(900);
		mTvMhlManager = TvMhlManager.getInstance();
		mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		
		mRefreshTimer = new Timer();

		Log.d("Maxs250","RootCreate:getIntent == null = " + (getIntent() == null));
		if (getIntent() != null){
			Log.d("Maxs250","RootCreate:getIntent().getAction() = " + getIntent().getAction());
			Log.d("Maxs250","RootCreate:getIntent().getStringExtra(\"task_tag\")) = " + getIntent().getStringExtra("task_tag"));
		}
		// for support input source change intent send from source hot key
		if (getIntent() != null && getIntent().getAction() != null) {
			if ("input_source_changed".equals(getIntent().getStringExtra("task_tag"))) {
				/*
				 * clear screen saver status after changing input source and
				 * hide NoSignalView before change input source.
				 */
				is_source_chanage = true;
				Log.d("Maxs250","RootCreate:--->111");
				mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;
				Log.d("Jason23","showNoSignalView:false42");
				showNoSignalView(false);
				int inputsource = getIntent().getIntExtra("inputSrc",
						TvCommonManager.INPUT_SOURCE_ATV);
				if(log)
				Log.i(TAG, "onCreate:inputsource = " + inputsource);
				startSourceChange(inputsource);
				}
		}

		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(TvIntent.ACTION_EXIT_TV_APK)) {
					tvApkExitHandler();
				}
				//mengwt 20141014
				else if(intent.getAction().equals("com.mstar.tvsetting.hotkey.DisableNoSignal")){
					disableNoSignal=true;
				}
				//mengwt 20141014
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(TvIntent.ACTION_EXIT_TV_APK);
		//mengwt 20141014
		filter.addAction("com.mstar.tvsetting.hotkey.DisableNoSignal");
		//mengwt 20141014
		registerReceiver(mReceiver, filter);

		LayoutInflater factory = LayoutInflater.from(this);
		final View layout = factory.inflate(
				R.layout.ciplus_oprefresh_confirmation_dialog, null);

		mCiPlusOPRefreshDialog = new AlertDialog.Builder(this)
				.setTitle(getString(R.string.str_ciplus_op_confirmation_title))
				.setView(layout)
				.setIconAttribute(android.R.attr.alertDialogIcon)
				.setPositiveButton(getString(android.R.string.yes),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								TvCiManager.getInstance().sendCiOpSearchStart(
										false);
								updateScreenSaver();
							}
						})
				.setNegativeButton(getString(android.R.string.no),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								updateScreenSaver();
							}
						}).setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						updateScreenSaver();
					}
				}).create();

		mExitDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.str_root_alert_dialog_title)
				.setMessage(R.string.str_root_alert_dialog_message)
				.setPositiveButton(R.string.str_root_alert_dialog_confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								LittleDownTimer.destory();
								mIsBackKeyPressed = false;
								dialog.dismiss();
								mIsExiting = true;
								//mwt 20140825
								try {
									TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOffStatus");
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								////
								//mengwt 20141015
								Settings.System.putInt(getContentResolver(), "source_hot_key_disable", 1);
								//mengwt 20141015
								finish();
							}
						})
				.setNegativeButton(R.string.str_root_alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mIsActive = true;
								dialog.dismiss();
								mIsBackKeyPressed = false;
								bCmd_TvApkExit = false;
								updateScreenSaver();
								mIskeyLocked = false;
							}
						}).setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						mIskeyLocked = false;
						mIsActive = true;
						dialog.dismiss();
						mIsBackKeyPressed = false;
						bCmd_TvApkExit = false;
						updateScreenSaver();

						// ***add by allen.sun 2013-5-27
						// Adaptation different resolutions in STB
						if (Tools.isBox()) {
							new Thread() {
								@Override
								public void run() {
									try {
										Thread.sleep(500);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									Intent pipIntent = new Intent(
											"com.mstar.pipservice");
									pipIntent.putExtra("cmd", "visible");
									RootActivity.this.startService(pipIntent);
								}
							}.start();
						}
						// ***and end
					}

				}).create();
		mMhlEventListener = new MhlEventListener();
		TvManager.getInstance().getMhlManager().setOnMhlEventListener(mMhlEventListener);

		mDtvPlayerEventListener = new DtvPlayerEventListener();
		TvChannelManager.getInstance().registerOnDtvPlayerEventListener(
				mDtvPlayerEventListener);
		mTvPlayerEventListener = new TvPlayerEventListener();

		TvChannelManager.getInstance().registerOnTvPlayerEventListener(
				mTvPlayerEventListener);
		mAtvPlayerEventListener = new AtvPlayerEventListener();
		TvChannelManager.getInstance().registerOnAtvPlayerEventListener(
				mAtvPlayerEventListener);
		mTvEventListener = new TvEventListener();
		TvCommonManager.getInstance().registerOnTvEventListener(
				mTvEventListener);
		mUiEventListener = new UiEventListener();
		TvCiManager.getInstance().registerOnUiEventListener(mUiEventListener);
		mCiStatusChangeEventListener = new CiStatusChangeEventListener();
		TvCiManager.getInstance().registerOnCiStatusChangeEventListener(
				mCiStatusChangeEventListener);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(log)
		Log.i(TAG, "onConfigurationChanged(), newConfig:" + newConfig);
		if(mNosignalText!=null){
			mNosignalText.setText(R.string.str_no_signal);
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "onStart()");
		super.onStart();
		mCecCtrlEventListener = new CecCtrlEventListener();
		TvCecManager.getInstance().registerOnCecCtrlEventListener(mCecCtrlEventListener);
		disableNoSignal=false;
	}

	private void executePreviousTask(final int inputSource) {

		/*final Intent targetIntent = new Intent("mstar.tvsetting.ui.intent.action.RootActivity");
		targetIntent.putExtra("task_tag", "input_source_changed");
		targetIntent.putExtra("inputSrc", inputSource);
		targetIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);*/
		new Thread(new Runnable() {

			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				mTvS3DManager.setDisplayFormat(EnumThreeDVideoDisplayFormat.E_ThreeD_Video_DISPLAYFORMAT_NONE);
				if (inputSource == TvCommonManager.INPUT_SOURCE_ATV) {
					Log.d("Maxs250","setInputSource 1111");
					mTvCommon.setInputSource(inputSource);
					int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
					if (curChannelNumber > 0xFF) {
						curChannelNumber = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(curChannelNumber);
				} else if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
					Log.d("Maxs250","setInputSource 222");
					mTvCommon.setInputSource(inputSource);
					TvChannelManager.getInstance().playDtvCurrentProgram();
				} else {
					Log.d("Maxs250","setInputSource 333");
					mTvCommon.setInputSource(inputSource);
				}

				///startActivity(targetIntent);
			}
		}).start();
	}

	@Override
	protected void onResume() {
		if(log)
		Log.i(TAG, "onResume()");
		try {
			TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOnStatus");
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("Maxs250","onReusme() = " + TvCommonManager.getInstance().getCurrentTvInputSource());
		isRootActive = true;
		isHotelMenuLockFlag = isCurrentChannelLock();
		//mengwt 20141015
		//////Settings.System.putInt(getContentResolver(), "source_hot_key_disable", 0); Maxs delete
		//mengwt 20141015
		int curInputSource = mTvCommon.getCurrentTvInputSource();
		if(TvCommonManager.INPUT_SOURCE_STORAGE!=curInputSource)
		{
			/*mIsSignalLock = mTvCommon.isSignalStable(curInputSource);
			if(isOnPauseFlag = true)
			{
				isOnPauseFlag = false;
				if(!mIsSignalLock)
				{
					Log.d("Jason23","showNoSignalView:true:11");
					showNoSignalView(true);
				}
			}*/
		}
		//ktc nathan.liao 20150314 for show nosignl when from usb start
		else 
		{
			if(isOnPauseFlag = true)
			{
				isFromUSB = true;
			}
		}
		//ktc nathan.liao 20150314 for show nosignl when from usb end
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mTvPictureManager.is4K2KMode(true)) {
					sendBroadcast(new Intent("enter.4k2k"));
				}
			}
		}, 1000);
		super.onResume();

		Log.d(TAG, "onResume() mScreenSaverStatus = " + mScreenSaverStatus);
		sendBroadcast(new Intent(TV_APK_START));
		Log.d("Maxs250","onReusme1() = " + TvCommonManager.getInstance().getCurrentTvInputSource());
		// get previous inputsource from preferences start
		SharedPreferences settings = getSharedPreferences(Constants.PREFERENCES_INPUT_SOURCE, Context.MODE_PRIVATE);
		mPreviousInputSource = settings.getInt(Constants.PREFERENCES_PREVIOUS_INPUT_SOURCE,TvCommonManager.INPUT_SOURCE_ATV);
		// get previous inputsource from preferences end
		if(log)
		Log.d(TAG, "get previous input source from preference:"
				+ mPreviousInputSource);

		mIskeyLocked = false;
		tvView.setBackgroundColor(Color.TRANSPARENT);
		mIsBackKeyPressed = false;
		Log.d("Maxs250","onReusme3() = " + TvCommonManager.getInstance().getCurrentTvInputSource());
		mHandler.sendEmptyMessage(Constants.ROOTACTIVITY_RESUME_MESSAGE);
		Log.d("Maxs250","onReusme4() = " + TvCommonManager.getInstance().getCurrentTvInputSource());
		if (TvCommonManager.INPUT_SOURCE_ATV == mTvCommon
				.getCurrentTvInputSource()) {
			SharedPreferences share = getSharedPreferences("atv_ttx",
					Context.MODE_PRIVATE);
			boolean atvttx = share.getBoolean("ATV_TTXOPEN", false);
			if (atvttx) {
				TvChannelManager.getInstance().openTeletext(
						TvChannelManager.TTX_MODE_SUBTITLE_NAVIGATION);
			}
		}
		if (KtcApplication.isSourceDirtyPre) {
			KtcApplication.isSourceDirtyPre = false;
			KtcApplication.isSourceDirty = true;
		}

		if ((curInputSource == TvCommonManager.INPUT_SOURCE_ATV)
			|| (curInputSource == TvCommonManager.INPUT_SOURCE_DTV)) {
			mHandler.sendEmptyMessageDelayed(Constants.ROOTACITIVYT_DELAY_SEARCH_CHANNLE, 1400);
		}else{
			mHandler.sendEmptyMessageDelayed(Constants.ROOTACITIVYT_DELAY_SEARCH_CHANNLE, 1500);
		}
	}

	private void startChannelTunning(){
		SharedPreferences settings = getSharedPreferences(Constants.PREFERENCES_INPUT_SOURCE, Context.MODE_PRIVATE);
		if (!settings.getBoolean("persist.sys.searchchannel",false)) {
			int currInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();
			Log.d("Maxs52", "startChannelTunning currInputSource = " + currInputSource);
			if ((currInputSource == TvCommonManager.INPUT_SOURCE_ATV)
					|| (currInputSource == TvCommonManager.INPUT_SOURCE_DTV)) {
				///SystemProperties.set("persist.sys.searchchannel", "true");
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("persist.sys.searchchannel",true);
				editor.commit();
				Intent intent = new Intent(RootActivity.this, FirstSearchActivity.class);
				intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
				startActivity(intent);

			}
		}
	}

	private void checkSystemAutoTime() {
		try {
			systemAutoTime = Settings.System.getInt(getContentResolver(),
					Settings.Global.AUTO_TIME);
		} catch (SettingNotFoundException e) {
			systemAutoTime = 0;
		}

		if (systemAutoTime > 0) {
			int curInputSource = TvCommonManager.getInstance()
					.getCurrentTvInputSource();
			if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
				Settings.System.putInt(getContentResolver(),
						Settings.Global.AUTO_TIME, 0);
			}
		}
	}

	@Override
	protected void onStop() {
		if(log)
		Log.d(TAG, "--------onStop()---------");
		//save the previous source(INPUT_SOURCE_STORAGE) when leave start
		mPreviousInputSource = TvCommonManager.INPUT_SOURCE_STORAGE;
		SharedPreferences settings = getSharedPreferences(Constants.PREFERENCES_INPUT_SOURCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(Constants.PREFERENCES_PREVIOUS_INPUT_SOURCE , mPreviousInputSource);
		editor.commit();
		//save the previous source(INPUT_SOURCE_STORAGE) when leave end
		
		sendBroadcast(new Intent(TV_APK_END));
		if (mCiPlusOPRefreshDialog != null
				&& mCiPlusOPRefreshDialog.isShowing()) {
			mCiPlusOPRefreshDialog.dismiss();
		}

		if (mExitDialog != null) {
			mExitDialog.dismiss();
		}
		if (skyTVDialog != null && skyTVDialog.isTvDialogShowed()){
			skyTVDialog.hideTvDialog();
		}
		mIsPowerOn = false;
		TvCecManager.getInstance().unregisterOnCecCtrlEventListener(mCecCtrlEventListener);
		mCecCtrlEventListener = null;

		// switch input source to storage for releasing TV relative resrouce
		if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_STORAGE) {
			
			if(log)
			Log.i(TAG, "onStop(): Switch input source to storage...");
			Log.d("Maxs250","RootActivity:onStop:setInputSource:STORAGE");
			TvCommonManager.getInstance().setInputSource(
					TvCommonManager.INPUT_SOURCE_STORAGE);
		}
		super.onStop();
	}

	private void updateTvSourceSignal() {
		int curInputSource = TvCommonManager.INPUT_SOURCE_NONE;
		curInputSource = TvCommonManager.getInstance()
				.getCurrentTvInputSource();
		if(log)
		Log.i(TAG, "curInputSource is :" + curInputSource);
		Log.d("Maxs250","onReusme7() = " + TvCommonManager.getInstance().getCurrentTvInputSource());
		boolean noChangeSource = getIntent().getBooleanExtra(
				"no_change_source", false);
		getIntent().removeExtra("no_change_source");
		if(log)
		Log.d(TAG, "mIsMsrvStarted:" + mIsMsrvStarted);
		if (mIsMsrvStarted == true) {
			/**
			 * If current inputsource is storage, it means apk resume from mm.
			 * We need to change inputsource to previous tv inputsource.
			 */
			if ((TvCommonManager.INPUT_SOURCE_STORAGE == curInputSource)
					|| (TvCommonManager.INPUT_SOURCE_NONE == curInputSource)) {
				if (!noChangeSource && !mIsPowerOn) {
					/*new Thread(new Runnable() {
						public void run() {
							if(log)
							Log.d(TAG, "Change InputSource to previous :"
									+ mPreviousInputSource);
							Log.d("Maxs250","RootActivity:updateTvSourceSignal:setInputSource:mPreviousInputSource = " + mPreviousInputSource);
							TvCommonManager.getInstance().setInputSource(
									mPreviousInputSource);
							KtcApplication.setSourceDirty(true);
							if (mPreviousInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
								*//*K_ChannelModel
										.getInstance()
										.K_changeToFirstService(
												TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
												TvChannelManager.FIRST_SERVICE_DEFAULT);*//*
								TvChannelManager.getInstance().setAtvChannel(TvChannelManager.getInstance().getCurrentChannelNumber());
							} else if (mPreviousInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
								*//*K_ChannelModel
										.getInstance()
										.K_changeToFirstService(
												TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
												TvChannelManager.FIRST_SERVICE_DEFAULT);*//*
								TvChannelManager.getInstance().playDtvCurrentProgram();
							}
						}
					}).start();*/
					TvPictureManager.getInstance()
							.setDynamicBackLightThreadSleep(false);
				}
			} else {
		//nathan.liao 2014.10.14 for menu reset power on source error start
                if (mIsPowerOn == true) {
                    new Thread(new Runnable() {
                            public void run() {
                            Log.d(TAG, "Change InputSource to previous :" + mPreviousInputSource);
                            int curInputSource = TvCommonManager.getInstance()
        							.getCurrentTvInputSource();
                            // acquire resource control from resource manager
								Log.d("Maxs250","RootActivity:updateTvSourceSignal:setInputSource:curInputSource = " + curInputSource);
                            TvCommonManager.getInstance().setInputSource(curInputSource);
                            }
                            }).start();
                }
		
		//nathan.liao 2014.10.14 for menu reset power on source error end

				if (isFirstPowerOn == false) {
					if (curInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
						startThread(true);
					} else if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
						startThread(false);
					}
				}
				isFirstPowerOn = false; // move here for first power on
			}
			int swMode = TvChannelManager.getInstance().getAtvChannelSwitchMode();
			if (swMode == TvChannelManager.CHANNEL_SWITCH_MODE_FREEZESCREEN) {
				TvChannelManager.getInstance().setChannelChangeFreezeMode(true);
			} else {
				TvChannelManager.getInstance().setChannelChangeFreezeMode(false);
			}
			mIsMsrvStarted = false;
		}
		curInputSource = TvCommonManager.getInstance()
				.getCurrentTvInputSource();
		mIsActive = true;
		if (curInputSource == TvCommonManager.INPUT_SOURCE_ATV
				|| curInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
			//nathan.liao 2014.10.14 add for pass word menu and nosigal menu overlay
			if (isHotelMenuLockFlag) {
				isShowPsd = true;
				for (Button btn : mPasswdViews) {
					btn.setFocusable(true);
				}
				resetInput();
				
				rllyPassword.setVisibility(View.VISIBLE);
			} else {
				for (Button btn : mPasswdViews) {
					btn.setFocusable(false);
				}
				isShowPsd = false;
				rllyPassword.setVisibility(View.GONE);
			}
		} else {
			isShowPsd = false;
			for (Button btn : mPasswdViews) {
				btn.setFocusable(false);
			}
			rllyPassword.setVisibility(View.GONE);
		}
		//ktc nathan.liao 20150314 for show nosignl when from usb start
		final int mCurInputSource = curInputSource;
		if(isFromUSB&&!isShowPsd)
		{
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					isFromUSB = false;
					isOnPauseFlag = false;
					if(!mTvCommon.isSignalStable(mCurInputSource))
					{
						Log.d("Jason23","showNoSignalView:true12");
						showNoSignalView(true);
					}
				}
			}, 3000);

		}
		if (!isShowPsd)
		updateScreenSaver();
		if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
			Log.d("Maxs","RootActitity:setUserScanType:DTV");
			////TvChannelManager.getInstance().setUserScanType(TvChannelManager.TV_SCAN_DTV);
		} else {
			Log.d("Maxs","RootActitity:setUserScanType:ATV");
			////TvChannelManager.getInstance().setUserScanType(TvChannelManager.TV_SCAN_ATV);
		}
		//to show info when change source
		if(mPreviousInputSource != curInputSource){
			Utility.startSourceInfo(RootActivity.this);
		}
	}

	@Override
	protected void onPause() {
		if(log)
		Log.d(TAG, "onPause()");
		isRootActive = false;
		if (null != mCecInfoDialog) {
            mHandler.removeMessages(Constants.ROOTACTIVITY_CANCEL_DIALOG);
            mCecInfoDialog.dismiss();
        }
        mCecInfoDialog = null;
		if (mCcKeyToast != null) {
			mCcKeyToast.cancel();
		}
		mIsCecDialogCanceled = false;
		if (true == mIsExiting) {
			if(log)
			Log.i(TAG, "Exiting, prepare to change souce");
			mIsExiting = false;
		}
		mIsActive = false;
		KtcApplication.setSourceDirty(false);

		
		if (mExitDialog != null) {
			mExitDialog.dismiss();
		}
		if (skyTVDialog != null && skyTVDialog.isTvDialogShowed()){
			skyTVDialog.hideTvDialog();
		}
		//if(mNosignalText.getVisibility() == TextView.VISIBLE)
		//{
			Log.d("Jason","13");
			//mNosignalText.setVisibility(TextView.INVISIBLE);
	//		showNoSignalView(false);
		//}
		isOnPauseFlag = true;
		rllyPassword.setVisibility(View.GONE);
		super.onPause();
		//save the previous source start
		SharedPreferences settings = getSharedPreferences(Constants.PREFERENCES_INPUT_SOURCE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(Constants.PREFERENCES_PREVIOUS_INPUT_SOURCE , mTvCommon.getCurrentTvInputSource());
		editor.commit();
		//save the previous source end
        DataBaseHelper.getInstance(this).K_updateDatabase_systemsetting("PreSource", mTvCommon.getCurrentTvInputSource());
		///SystemProperties.set("persist.sys.presource", mTvCommon.getCurrentTvInputSource()+"");
	}

	@Override
	protected void onRestart() {
		if(log)
		Log.i(TAG, "onRestart()");
		Log.d("Maxs250","--->onRestart");
		isFirstPowerOn = true;
		mIsMsrvStarted = true;
		super.onRestart();
		int inputSource = mTvCommon.getCurrentTvInputSource();
		if (inputSource == TvCommonManager.INPUT_SOURCE_STORAGE) {
			//inputSource = SystemProperties.getInt("persist.sys.presource", TvCommonManager.INPUT_SOURCE_DTV);
			inputSource = DataBaseHelper.getInstance(this).K_getValueDatabase_systemsetting("PreSource");
			Intent source_switch_from_storage = new Intent("source.switch.from.storage");
			sendBroadcast(source_switch_from_storage);
			//handler.sendEmptyMessage(SETIS_START);
			executePreviousTask(inputSource);
		}
	}

	@Override
	public void onDestroy() {
		if(log)
		Log.i(TAG, "onDestroy()");

		if (mCiPlusOPRefreshDialog != null
				&& mCiPlusOPRefreshDialog.isShowing()) {
			mCiPlusOPRefreshDialog.dismiss();
		}

		if (mExitDialog != null && mExitDialog.isShowing()) {
			mExitDialog.dismiss();
		}
		if (skyTVDialog != null && skyTVDialog.isTvDialogShowed()){
			skyTVDialog.hideTvDialog();
		}
		if (systemAutoTime > 0) {
			int curInputSource = TvCommonManager.getInstance()
					.getCurrentTvInputSource();
			if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
				Settings.System.putInt(getContentResolver(),
						Settings.Global.AUTO_TIME, systemAutoTime);
			}
		}
		
		///K_TvManager.getInstance().K_unregisterOnMhlEventListener(mMhlEventListener);
		TvPictureManager.getInstance().setDynamicBackLightThreadSleep(true);
		TvChannelManager.getInstance().unregisterOnAtvPlayerEventListener(
				mAtvPlayerEventListener);
		mAtvPlayerEventListener = null;
		TvChannelManager.getInstance().unregisterOnDtvPlayerEventListener(
				mDtvPlayerEventListener);
		mDtvPlayerEventListener = null;
		TvChannelManager.getInstance().unregisterOnTvPlayerEventListener(
				mTvPlayerEventListener);
		mTvPlayerEventListener = null;
		TvCommonManager.getInstance().unregisterOnTvEventListener(
				mTvEventListener);
		mTvEventListener = null;
		TvCiManager.getInstance().unregisterOnUiEventListener(mUiEventListener);
		mUiEventListener = null;
		TvCiManager.getInstance().unregisterOnCiStatusChangeEventListener(
				mCiStatusChangeEventListener);
		mCiStatusChangeEventListener = null;
		unregisterReceiver(mReceiver);
		mIsActive = false;
		KtcApplication.setSourceDirty(false);
		super.onDestroy();
	}

	@Override
	protected void onNewIntent(final Intent intent) {
		super.onNewIntent(intent);
		executePreviousTask(intent);
		setIntent(intent);
		//ktc nathan.liao 2016.05.17 add for issue ERP Q160513003 start
		mIsPowerOn = (intent != null) ? (intent.getBooleanExtra(
				"isPowerOn", false) ) : false;
		intent.removeExtra("isPowerOn");
		//ktc nathan.liao 2016.05.17 add for issue ERP Q160513003 end
		// In case RootActivity under onStop() stage, we have to handle input
		// source change intent here
		Log.d("Maxs250","onNewIntent()");
		Log.d("Maxs250","onNewIntent() = " + TvCommonManager.getInstance().getCurrentTvInputSource());
		Log.d("Maxs250","onNewIntent()getIntent() == null = " + (getIntent() == null));
		if (getIntent() != null){
			Log.d("Maxs250","onNewIntent()getIntent().getAction() = " + getIntent().getAction());
		}
		if (getIntent() != null && getIntent().getAction() != null) {
			Log.d("Maxs250","onNewIntent():getIntent().getAction() = " + getIntent().getAction());
			///if (getIntent().getAction().equals(TvIntent.ACTION_SOURCE_CHANGE)) {
				/*
				 * clear screen saver status after changing input source and
				 * hide NoSignalView before change input source.
				 */
			Log.d("Maxs250","RootCreate:--->111");
				mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;
				Log.d("Jason23","showNoSignalView:false:14");
				showNoSignalView(false);
				final int inputsource = getIntent().getIntExtra("inputSrc",
						TvCommonManager.INPUT_SOURCE_ATV);
				if(log)
				Log.i("Maxs250", "onNewIntent:inputsource = " + inputsource);
				///startSourceChange(inputsource);

			new Thread(new Runnable() {
				@Override
				public void run() {
					Log.d("Maxs250","RootActivity: = " + inputsource);
					TvCommonManager.getInstance().setInputSource(inputsource);

					if (inputsource == TvCommonManager.INPUT_SOURCE_ATV) {
						int curChannelNumber = TvChannelManager.getInstance()
								.getCurrentChannelNumber();
						if (curChannelNumber > 0xFF) {
							curChannelNumber = 0;
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						TvChannelManager.getInstance().setAtvChannel(
								curChannelNumber);
					} else if (inputsource == TvCommonManager.INPUT_SOURCE_DTV) {

						TvChannelManager.getInstance().playDtvCurrentProgram();
					}
				}
			}).start();
			//}
		}
	}

	private void executePreviousTask(final Intent paramIntent) {
		if (paramIntent != null) {
			String taskTag = paramIntent.getStringExtra("task_tag");
			if ("input_source_changed".equals(taskTag)) {
				KtcApplication.setSourceDirty(true);
			}
		}
	}

	public ArrayList<ProgramInfo> getAllProgramList() {
		ProgramInfo pgi = null;
		int indexBase = 0;
		ArrayList<ProgramInfo> progInfoList = new ArrayList<ProgramInfo>();
		int currInputSource = TvCommonManager.getInstance()
				.getCurrentTvInputSource();
		int m_nServiceNum = 0;
		if (currInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
			indexBase = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
			if (indexBase == 0xFFFFFFFF) {
				indexBase = 0;
			}
			m_nServiceNum = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);
		} else {
			indexBase = 0;
			m_nServiceNum = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
		}
		if(log)
		{
		Log.d(TAG, "indexBase:" + indexBase);
		Log.d(TAG, "m_nServiceNum:" + m_nServiceNum);
		}
		for (int k = indexBase; k < m_nServiceNum; k++) {
				pgi = TvChannelManager.getInstance().getProgramInfoByIndex(k);
			if (pgi != null) {
				if ((pgi.isDelete == true) || (pgi.isVisible == false)) {
					continue;
				} else {
					progInfoList.add(pgi);
				}
			}
		}
		return progInfoList;
	}

	public ProgramInfo getNextProgramm() {

		ArrayList<ProgramInfo> progInfoList = getAllProgramList();
		if (progInfoList == null || progInfoList.size() == 0) {
			return null;
		}
		ProgramInfo currentProgInfo = TvChannelManager.getInstance().getCurrentProgramInfo();
		if (currentProgInfo == null) {
			return null;
		}
		ProgramInfo next = null;
		for (int i = 0; i < progInfoList.size(); i++) {
			ProgramInfo pi = progInfoList.get(i);
			if (currentProgInfo.frequency == pi.frequency
					&& currentProgInfo.serviceId == pi.serviceId
					&& currentProgInfo.number == pi.number
					&& currentProgInfo.progId == pi.progId) {
				if (i < progInfoList.size() - 1) {
					next = progInfoList.get(i + 1);
					break;
				}
				if (i == progInfoList.size() - 1) {
					next = progInfoList.get(0);
				}

			}
		}
		return next;

	}

	public ProgramInfo getPreviousProgram() {
		ArrayList<ProgramInfo> progInfoList = getAllProgramList();
		ProgramInfo currentProgInfo = TvChannelManager.getInstance().getCurrentProgramInfo();
		if (currentProgInfo == null) {
			return null;
		}
		ProgramInfo previous = null;
		for (int i = 0; i < progInfoList.size(); i++) {
			ProgramInfo pi = progInfoList.get(i);
			if (currentProgInfo.number == pi.number
					&& currentProgInfo.serviceType == pi.serviceType) {

				if (i == 0) {
					previous = progInfoList.get(progInfoList.size() - 1);
					break;
				}
				if (i > 0) {
					previous = progInfoList.get(i - 1);
				}

			}
		}
		return previous;
	}



	static boolean canRepeatKey = true;

	static int preKeyCode = KeyEvent.KEYCODE_UNKNOWN;

	Handler mRepeatHandler = new Handler();

	Runnable mRepeatRun = new Runnable() {
		public void run() {
			canRepeatKey = true;
		}
	};

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		if (sendGingaKey(keyCode, event)) {
			if(log)
			Log.i(TAG, "onKeyUp:sendGingaKey success!");
			return true;
		}

		if (mTvMhlManager.CbusStatus() == true
				&& mTvMhlManager.IsMhlPortInUse() == true) {
			if (mTvMhlManager.IRKeyProcess(keyCode, true) == true) {
				SystemClock.sleep(140);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	};
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d("Maxs9","RootActivity:keyCode = " + keyCode);
		 if(PropHelp.newInstance().hasHotel())
	        {
	        	if(isShowPsd)
	        	{
	        		if(keyCode == MKeyEvent.KEYCODE_ASPECT_RATIO)
	        			return true;
	        	}
	        }

		final boolean down = event.getAction() == KeyEvent.ACTION_DOWN;
		if (down
				&& (keyCode == KeyEvent.KEYCODE_M || keyCode == KeyEvent.KEYCODE_MENU)) {

			if (!canRepeatKey && !(preKeyCode == keyCode)) {
				preKeyCode = keyCode;
			} else {
				preKeyCode = keyCode;
				mRepeatHandler.removeCallbacks(mRepeatRun);
				canRepeatKey = false;
				mRepeatHandler.postDelayed(mRepeatRun, 2000);
			}
		}

		if (sendHbbTVKey(keyCode)) {
			if(log)
			Log.i(TAG, "onKeyDown:sendHbbTVKey success!");
			return true;
		}

		// arrange CEC key
		if (sendCecKey(keyCode)) {
			if(log)
			Log.i(TAG, "onKeyDown:sendCecKey success!");
			return true;
		}

		// arrange Mheg5 key
		if (sendMheg5Key(keyCode)) {
			if(log)
			Log.i(TAG, "onKeyDown:sendMhegKey success!");
			return true;
		}

		// arrange Ginga key
		if (sendGingaKey(keyCode, event)) {
			if(log)
			Log.i(TAG, "onKeyDown:sendGingaKey success!");
			return true;
		}

		if (mTvMhlManager.CbusStatus() == true
				&& mTvMhlManager.IsMhlPortInUse() == true) {
			if (mTvMhlManager.IRKeyProcess(keyCode, false) == true) {
				SystemClock.sleep(140);
				return true;
			}
		}

		if (!Constants.lockKey && keyCode != KeyEvent.KEYCODE_BACK) {
			return true;
		}


		Log.d(TAG, " RootActivity:onKeyDown  keyCode = " + keyCode);
		switch (keyCode) {
		case MKeyEvent.KEYCODE_CC:
			if (mTvCommon.isSupportModule(TvCommonManager.MODULE_CC)
					|| mTvCommon
							.isSupportModule(TvCommonManager.MODULE_BRAZIL_CC)) {
				if (mCcKeyToast == null) {
					mCcKeyToast = new Toast(this);
					mCcKeyToast.setGravity(Gravity.CENTER, 0, 0);
				}
				TextView tv = new TextView(RootActivity.this);
				tv.setTextSize(Constants.CCKEY_TEXTSIZE);
				tv.setTextColor(Color.WHITE);
				tv.setAlpha(Constants.CCKEY_ALPHA);
				mCcKeyToast.setView(tv);
				mCcKeyToast.setDuration(Toast.LENGTH_SHORT);

				int closedCaptionMode = TvCcManager.getInstance()
						.getNextClosedCaptionMode();
				TvCcManager.getInstance().setClosedCaptionMode(
						closedCaptionMode);
				TvCcManager.getInstance().stopCc();
				if (TvCcManager.CLOSED_CAPTION_OFF != closedCaptionMode) {
					TvCcManager.getInstance().startCc();
				}
				String[] closedCaptionStrings = getResources().getStringArray(
						R.array.str_arr_option_closed_caption);
				if (0 <= closedCaptionMode
						&& closedCaptionStrings.length > closedCaptionMode) {
					tv.setText(getResources().getString(
							R.string.str_option_caption)
							+ " " + closedCaptionStrings[closedCaptionMode]);
				}
				mCcKeyToast.show();
			}
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			String deviceName = InputDevice.getDevice(event.getDeviceId())
					.getName();
			if (deviceName.equals("MStar Smart TV IR Receiver")
					|| deviceName.equals("MStar Smart TV Keypad")) {
				AudioManager audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
				/*
				 * Adjust the volume in on key down since it is more responsive
				 * to the user.
				 */
				if (audiomanager != null) {
					int flags = AudioManager.FLAG_SHOW_UI
							| AudioManager.FLAG_VIBRATE;
					audiomanager
							.adjustVolume(
									keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ? AudioManager.ADJUST_RAISE
											: AudioManager.ADJUST_LOWER, flags);
				}
			} else {
				if(log)
				Log.d(TAG, "deviceName is:" + deviceName);
			}
			break;
		case KeyEvent.KEYCODE_PROG_YELLOW:
		case KeyEvent.KEYCODE_PROG_BLUE:
		case KeyEvent.KEYCODE_PROG_GREEN:
		case KeyEvent.KEYCODE_PROG_RED:
		case KeyEvent.KEYCODE_ENTER:
			break;
		case KeyEvent.KEYCODE_BACK:
			if (mIsBackKeyPressed == false) {
				mIsActive = false;
				mIsBackKeyPressed = true;
				///if (!mIskeyLocked) {
				///	mIskeyLocked = true;
					showExitDialog();
				////}
			}
			return true;
		case KeyEvent.KEYCODE_M:
		case KeyEvent.KEYCODE_MENU:
			Intent intent = new Intent(RootActivity.this,MainMenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return true;
		}

		/****
		if (!mIskeyLocked
				&& SwitchPageHelper.goToMenuPage(this, keyCode) == true) {
			mIskeyLocked = true;
			if(log)
			Log.d(TAG, "onKeyDown->goToMenuPage  keyCode = " + keyCode);
			return true;
		} else
		 */if (SwitchPageHelper.goToSourceInfo(this, keyCode) == true) {
			return true;
		} else if (SwitchPageHelper.goToProgrameListInfo(this, keyCode) == true) {
			return true;
		} else if (SwitchPageHelper.goToFavorateListInfo(this, keyCode) == true) {
			return true;
		} else if (goToChannelChange(this,keyCode,mIsActive) == true) {
			return true;
		}
		/*else if (SwitchPageHelper.goToEpgPage(this, keyCode) == true) {
			return true;
		}*/ /*else if (SwitchPageHelper.goToSubtitleLangPage(this, keyCode) == true) {
			return true;
		} else if (SwitchPageHelper.goToAudioLangPage(this, keyCode) == true) {
			return true;
		} else if (SwitchPageHelper.goToSleepMode(this, keyCode) == true) {
			return true;
		} else if (SwitchPageHelper.goToTeletextPage(this, keyCode) == true) {
			return true;
		}  else if (SwitchPageHelper.goTo3DMenuPage(this, keyCode) == true){
				return true;
		}else if (SwitchPageHelper.factoryControl(this, keyCode) == true) {
			return true;
        	}
		 ****/
		Log.d("Maxs9", " RootActivity:After:onKeyDown  keyCode = " + keyCode);
		return super.onKeyDown(keyCode, event);
	}
	/**
	 * handle the up, down, return and 0-9 key
	 *
	 */
	private  boolean goToChannelChange(Activity from, int keyCodeToTrans, boolean isRootActivity) {
		if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV
				|| TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
			if (keyCodeToTrans == KeyEvent.KEYCODE_CHANNEL_DOWN
					|| keyCodeToTrans == KeyEvent.KEYCODE_DPAD_DOWN) {
				// Hisa 2016.03.04 add Freeze function start
				Log.d("Jason","切换频道");
				////Intent intent = new Intent();//取消静像菜单
				///intent.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				//K_TvPictureManager.getInstance().K_unFreezeImage();
				////sendBroadcast(intent);
				// Hisa 2016.03.04 add Freeze function end
				TvChannelManager.getInstance().programDown();
				startSourceInfo(from,isRootActivity);
				return true;

			} else if (keyCodeToTrans == KeyEvent.KEYCODE_CHANNEL_UP
					|| keyCodeToTrans == KeyEvent.KEYCODE_DPAD_UP) {
				// Hisa 2016.03.04 add Freeze function start
				Log.d("Jason","切换频道");
				///Intent intent = new Intent();//取消静像菜单
				////intent.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				//K_TvPictureManager.getInstance().K_unFreezeImage();
				///sendBroadcast(intent);
				// Hisa 2016.03.04 add Freeze function end
				TvChannelManager.getInstance().programUp();
				startSourceInfo(from,isRootActivity);
				return true;
			} else if (keyCodeToTrans == MKeyEvent.KEYCODE_CHANNEL_RETURN) {
				TvChannelManager.getInstance().returnToPreviousProgram();
				///Intent intentFreeze = new Intent();//取消静像菜单
				////intentFreeze.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				////TvPictureManager.getInstance().unFreezeImage();
				////sendBroadcast(intentFreeze);
				Intent intent = new Intent(TvIntent.ACTION_SOURCEINFO);
				intent.putExtra("info_key", true);
				from.startActivity(intent);
				return true;
			}
			switch (keyCodeToTrans) {
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
					Intent intentChannelControl = new Intent(from,
							ChannelControlActivity.class);
					intentChannelControl.putExtra("KeyCode", keyCodeToTrans);
					from.startActivity(intentChannelControl);
					return true;
			}
		}
		return false;
	}

	private static void startSourceInfo(Activity from, boolean isRootAcitivy) {
		int curInput = TvCommonManager.getInstance().getCurrentTvInputSource();
			/*
			 * when RootActivity is not running, we don't start activity to
			 * interrupt other menu, so we send SIGNAL_LOCK action to source
			 * info for updating content if SourceInfoActivity is alive, its
			 * BoradcastReceiver will handle this event.
			 */
		/// if (false == isTopActivity(from,RootActivity.class.getName())) {
		///    from.sendBroadcast(new Intent(TvIntent.ACTION_SIGNAL_LOCK));
		///} else {
		///  Intent intent = new Intent(TvIntent.ACTION_SOURCEINFO);
		///   from.startActivity(intent);
		///}
		if (isRootAcitivy){
			Intent intent = new Intent(TvIntent.ACTION_SOURCEINFO);
			from.startActivity(intent);
		}else{
			from.sendBroadcast(new Intent(TvIntent.ACTION_SIGNAL_LOCK));
		}
	}
	private void createSurfaceView() {
		tvView = (TvView) findViewById(R.id.tranplentview);

		//ktc nathan.liao 2016.05.17 add for issue ERP Q160513003 start
		if(log)
		Log.d(TAG, "isPowerOn = " + mIsPowerOn);
		String taskTag = getIntent().getStringExtra("task_tag");
		// true means don't set window size which will cause black screen
		tvView.openView(mIsPowerOn || "input_source_changed".equals(taskTag));
		//ktc nathan.liao 2016.05.17 add for issue ERP Q160513003 end
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case Constants.CHANNEL_LOCK_RESULT_CODE:
			if (data.getExtras().getBoolean("result")) {
				////mNosignalText.setVisibility(TextView.INVISIBLE);
				signalstate.setVisibility(View.INVISIBLE);
			}
			break;
		default:
			if(log)
			Log.w(TAG, "onActivityResult resultCode not match");
			break;
		}
	}

	/*
	 * update NoSignalView Text String by Screen Saver Status and input source.
	 */
	private Handler signalLockHandler = new Handler() {
		public void handleMessage(Message msg) {
			int lockStatus = msg.arg1;
			if (TvEvent.SIGNAL_LOCK == lockStatus) {
				mIsSignalLock = true;
			} else if (TvEvent.SIGNAL_UNLOCK == lockStatus) {
				mIsSignalLock = false;
			}

			int curInputSource = TvCommonManager.getInstance()
					.getCurrentTvInputSource();
			mIsToPromptPassword = false;
            Log.d("Maxs105","signalLockHandler:mIsToPromptPassword = " + mIsToPromptPassword);
			if (mIsSignalLock) {
				/*
				 * send broadcast to those who need to know signal lock status.
				 * ex: SourceInfoActivity will get this intent when it is alive,
				 * and update source info content.
				 */
				sendBroadcast(new Intent(TvIntent.ACTION_SIGNAL_LOCK));

				switch (curInputSource) {
				case TvCommonManager.INPUT_SOURCE_DTV: {
					/* show SourceInfo before updating NoSignalView */
					Log.d("Maxs29","startSourceInfo1");
					startSourceInfo();
					/* update NoSignalView string by screen saver status */
					if (ScreenSaverMode.DTV_SS_INVALID_SERVICE == mScreenSaverStatus) {
						mNosignalText.setText(R.string.str_invalid_service);
					} else if (ScreenSaverMode.DTV_SS_NO_CI_MODULE == mScreenSaverStatus) {
						mNosignalText.setText(R.string.str_no_ci_module);
					} else if (ScreenSaverMode.DTV_SS_CI_PLUS_AUTHENTICATION == mScreenSaverStatus) {
						mNosignalText
								.setText(R.string.str_ci_plus_authentication);
					} else if (ScreenSaverMode.DTV_SS_SCRAMBLED_PROGRAM == mScreenSaverStatus) {
						mNosignalText.setText(R.string.str_scrambled_program);
					} else if (ScreenSaverMode.DTV_SS_UNSUPPORTED_FORMAT == mScreenSaverStatus) {
						/*
						 * FIXME: atsc screen saver status is separate by flag
						 * in supernova enum of screen saver should be sync
						 * between each tv system.
						 */
						mNosignalText.setText(R.string.str_unsupported);
					} else if (ScreenSaverMode.DTV_SS_CH_BLOCK == mScreenSaverStatus) {
						mIsToPromptPassword = true;
						mNosignalText.setText(R.string.str_channel_block);
					} else if (ScreenSaverMode.DTV_SS_PARENTAL_BLOCK == mScreenSaverStatus) {
						mIsToPromptPassword = true;
						mNosignalText.setText(R.string.str_parental_block);
					} else if (ScreenSaverMode.DTV_SS_AUDIO_ONLY == mScreenSaverStatus) {
						mNosignalText.setText(R.string.str_audio_only);
					} else if (ScreenSaverMode.DTV_SS_DATA_ONLY == mScreenSaverStatus) {
						mNosignalText.setText(R.string.str_data_only);
					} else if (ScreenSaverMode.DTV_SS_COMMON_VIDEO == mScreenSaverStatus) {
						// Reset NoSignalView string to default string : No
						// Signal
						mNosignalText.setText(R.string.str_no_signal);
						////mNosignalText.setBackground(getResources().getDrawable(R.drawable.ui_sdk_other_page_bg_pe));
						/////mNosignalText.setText(R.string.str_null);
					} else if (ScreenSaverMode.DTV_SS_INVALID_PMT == mScreenSaverStatus) {
						mNosignalText.setText(R.string.str_invalid_pmt);
					} else if (SCREENSAVER_DEFAULT_STATUS == mScreenSaverStatus) {
						/*
						 * Status fall into a default value case
						 * (SCREENSAVER_DEFAULT_STATUS) skip updating
						 * NoSignalView, it will be updated when
						 * ScreenSaverStatus be updated.
						 */
						if(log)
						Log.i(TAG,
								"Default ScreenSaver status, wait screenSaverHandler updating NoSignalView.");
						break;
					} else {
						if(log)
						Log.w(TAG,
								"Current Screen Saver Status is unrecognized");
						if(log)
						Log.w(TAG, "status: " + mScreenSaverStatus);
						break;
					}
					if(log)
					Log.d(TAG, "screen saver status is " + mScreenSaverStatus);
					/*
                     * update NoSignalView Visibility by signal lock flag
                     * and screen saver status
                     */
				//liaomz 2015.08.28 自动搜台DTV信源无信号菜单和图像同时在屏 start
				if (ScreenSaverMode.DTV_SS_COMMON_VIDEO != mScreenSaverStatus ) {
					if(ScreenSaverMode.DTV_SS_CH_BLOCK != mScreenSaverStatus)
					{
						if(PropHelp.newInstance().hasHotel())
						{	
							if(!(isShowPsd||isHotelMenuLockFlag))
							{
								mIsScreenSaverShown = true;
								Log.d("Jason23","showNoSignalView:true78");
								showNoSignalView(true);
							} else {
								Log.d("Jason23","showNoSignalView:false:79");
								mIsScreenSaverShown = false;
								showNoSignalView(false);
							}

						} else {
							Log.d("Jason23","showNoSignalView:true:80");
							mIsScreenSaverShown = true;
							showNoSignalView(true);
						}
					} else {
						Log.d("Jason23","showNoSignalView:false:81");
					  mIsScreenSaverShown = false;
					  showNoSignalView(false);
					}
                } else {
                	Log.d("Jason23","showNoSignalView:false;82");
                    mIsScreenSaverShown = false;
                    showNoSignalView(false);
                 }
				 //liaomz 2015.08.28 自动搜台DTV信源无信号菜单和图像同时在屏 end
					break;
				}
				case TvCommonManager.INPUT_SOURCE_HDMI:
				case TvCommonManager.INPUT_SOURCE_HDMI2:
				case TvCommonManager.INPUT_SOURCE_HDMI3:
				case TvCommonManager.INPUT_SOURCE_HDMI4:
					if ((SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE == mScreenSaverStatus)
							|| (SignalProgSyncStatus.UNSTABLE == mScreenSaverStatus)) {
						mNosignalText.setText(R.string.str_unsupported);
						mIsScreenSaverShown = true;
						Log.d("Jason23","showNoSignalView:true:18");
						showNoSignalView(true);
					} else if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == mScreenSaverStatus) {
						mIsScreenSaverShown = false;
						Log.d("Jason23","showNoSignalView:false:19");
						showNoSignalView(false);
					}
                    Log.d("Maxs29","startSourceInfo2");
					//startSourceInfo();
					break;
				case TvCommonManager.INPUT_SOURCE_VGA:
					if ((SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE == mScreenSaverStatus)
							|| (SignalProgSyncStatus.UNSTABLE == mScreenSaverStatus)) {
						mNosignalText.setText(R.string.str_unsupported);
						mIsScreenSaverShown = true;
						Log.d("Jason23","showNoSignalView:true20");
						showNoSignalView(true);
					} else if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == mScreenSaverStatus) {
						mIsScreenSaverShown = false;
						Log.d("Jason23","showNoSignalView:false:21");
						showNoSignalView(false);
                        Log.d("Maxs29","startSourceInfo3");
						startSourceInfo();
					} else if (SignalProgSyncStatus.AUTO_ADJUST == mScreenSaverStatus) {
						mNosignalText.setText(R.string.str_auto_adjust);
						mIsScreenSaverShown = true;
						Log.d("Jason23","showNoSignalView:ture:22");
						showNoSignalView(true);
						signalLockHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								mIsScreenSaverShown = false;
								Log.d("Jason23","showNoSignalView:false:23");
								showNoSignalView(false);
								startSourceInfo();
							}
						}, 3000);
					}
					break;
				case TvCommonManager.INPUT_SOURCE_CVBS:
				case TvCommonManager.INPUT_SOURCE_CVBS2:
				case TvCommonManager.INPUT_SOURCE_CVBS3:
				case TvCommonManager.INPUT_SOURCE_CVBS4:
					mIsScreenSaverShown = false;
					Log.d("Jason23","showNoSignalView:false;24");
					showNoSignalView(false);
					startSourceInfo();
					break;
				case TvCommonManager.INPUT_SOURCE_YPBPR:
				case TvCommonManager.INPUT_SOURCE_YPBPR2:
				case TvCommonManager.INPUT_SOURCE_YPBPR3:
					mIsScreenSaverShown = false;
					Log.d("Jason23","showNoSignalView:false:25");
					showNoSignalView(false);
					startSourceInfo();
					break;
				case TvCommonManager.INPUT_SOURCE_STORAGE:
					mIsScreenSaverShown = false;
					Log.d("Jason23","showNoSignalView:false:26");
					showNoSignalView(false);
					break;
				default:
					mIsScreenSaverShown = false;
					Log.d("Jason23","showNoSignalView:false:27");
					showNoSignalView(false);
					startSourceInfo();
					break;
				}
			} else {
				// signal unlock case
				switch (curInputSource) {
				case TvCommonManager.INPUT_SOURCE_ATV:
					// atv would not show nosignal text even signal unlock.
					mIsScreenSaverShown = false;
					Log.d("Jason23","showNoSignalView:false:28");
					showNoSignalView(true);
					mIsSignalLock = false;
					return;
				case TvCommonManager.INPUT_SOURCE_DTV:
					return;
				case TvCommonManager.INPUT_SOURCE_HDMI:
				case TvCommonManager.INPUT_SOURCE_HDMI2:
				case TvCommonManager.INPUT_SOURCE_HDMI3:
					Log.d("Maxs80","HDMI:No signal!");
						//startSourceInfo();
						return;
				default:
					mScreenSaverStatus = SCREENSAVER_DEFAULT_STATUS;
					mNosignalText.setText(R.string.str_no_signal);
					////mNosignalText.setBackground(getResources().getDrawable(R.drawable.ui_sdk_other_page_bg_pe));
					////mNosignalText.setText(R.string.str_null);
					if(log)
					Log.d(TAG, "unlock show nosignal");
					mIsScreenSaverShown = true;
					if (!isOnPauseFlag)
					{	
						Log.d("Jason23","showNoSignalView:true:29");
					showNoSignalView(true);
					}
				}
			}
		};
	};

	/**
	 * Used to handle scrren saver mode, decide if we need to show NoSignal
	 * Text. Each inputsource will have itself situation. Status can be
	 * referenced in two enum: EnumScreenMode, EnumSignalProgSyncStatus.
	 */
	private Handler screenSaverHandler = new Handler() {
		public void handleMessage(Message msg) {
			int curInputSource = TvCommonManager.getInstance()
					.getCurrentTvInputSource();
			int status = msg.arg1;
			switch (curInputSource) {
			//case TvCommonManager.INPUT_SOURCE_ATV:
			case TvCommonManager.INPUT_SOURCE_DTV:
				/* update NoSignalView string by screen saver status */
				if (ScreenSaverMode.DTV_SS_INVALID_SERVICE == status) {
					mIsScreenSaverShown = true;
					mNosignalText.setText(R.string.str_invalid_service);
				} else if (ScreenSaverMode.DTV_SS_NO_CI_MODULE == status) {
					mIsScreenSaverShown = true;
					mNosignalText.setText(R.string.str_no_ci_module);
				} else if (ScreenSaverMode.DTV_SS_CI_PLUS_AUTHENTICATION == status) {
					mIsScreenSaverShown = true;
					mNosignalText.setText(R.string.str_ci_plus_authentication);
				} else if (ScreenSaverMode.DTV_SS_SCRAMBLED_PROGRAM == status) {
					mIsScreenSaverShown = true;
					mNosignalText.setText(R.string.str_scrambled_program);
				} else if (ScreenSaverMode.DTV_SS_UNSUPPORTED_FORMAT == status) {
					/*
					 * FIXME: atsc screen saver status is separate by flag in
					 * supernova enum of screen saver should be sync between
					 * each tv system.
					 */
					mIsScreenSaverShown = true;
					mNosignalText.setText(R.string.str_unsupported);
				} else if (ScreenSaverMode.DTV_SS_AUDIO_ONLY == status) {
					mIsScreenSaverShown = true;
					mNosignalText.setText(R.string.str_audio_only);
				} else if (ScreenSaverMode.DTV_SS_DATA_ONLY == status) {
					mIsScreenSaverShown = true;
					mNosignalText.setText(R.string.str_data_only);
				} else if (ScreenSaverMode.DTV_SS_COMMON_VIDEO == status) {
					// Reset NoSignalView string to default string : No Signal
					mIsScreenSaverShown = false;
					mNosignalText.setText(R.string.str_no_signal);
					////mNosignalText.setBackground(getResources().getDrawable(R.drawable.ui_sdk_other_page_bg_pe));
					///mNosignalText.setText(R.string.str_null);
				} else if (ScreenSaverMode.DTV_SS_INVALID_PMT == status) {
					mIsScreenSaverShown = true;
					mNosignalText.setText(R.string.str_invalid_pmt);
				}

				/*
                 * update NoSignalView Visibility by signal lock flag
                 * and screen saver status
                 */
				if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV){
	                if (ScreenSaverMode.DTV_SS_COMMON_VIDEO != mScreenSaverStatus) {
	                    mIsScreenSaverShown = true;
	                    Log.d("Jason23","showNoSignalView:true:91");
	                    showNoSignalView(true);
	                } else {
	                	Log.d("Jason23","showNoSignalView:false:92");
	                    mIsScreenSaverShown = false;
	                    showNoSignalView(false);
	                }
				}
				break;
			case TvCommonManager.INPUT_SOURCE_VGA:
				if (SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE == status) {
					mNosignalText.setText(R.string.str_unsupported);
					mIsScreenSaverShown = true;
					Log.d("Jason23","showNoSignalView:true30");
					showNoSignalView(true);
				} else if (SignalProgSyncStatus.AUTO_ADJUST == status) {
					mIsScreenSaverShown = true;
					Log.d("Jason23","showNoSignalView:true:31");
					showNoSignalView(true);
					screenSaverHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							mIsScreenSaverShown = false;
							Log.d("Jason23","showNoSignalView:false:32");
							showNoSignalView(false);
							startSourceInfo();
						}
					}, 3000);
				} else if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == status) {
					mIsScreenSaverShown = false;
					Log.d("Jason23","showNoSignalView:false:33");
					showNoSignalView(false);
					startSourceInfo();
				}
				break;
			case TvCommonManager.INPUT_SOURCE_CVBS:
			case TvCommonManager.INPUT_SOURCE_CVBS2:
			case TvCommonManager.INPUT_SOURCE_CVBS3:
			case TvCommonManager.INPUT_SOURCE_CVBS4:
				if (ScreenSaverMode.DTV_SS_PARENTAL_BLOCK == mScreenSaverStatus) {
					mNosignalText.setText(R.string.str_parental_block);
					if (true == mIsActive) {
					}
					mIsScreenSaverShown = true;
					Log.d("Jason23","showNoSignalView:true:35");
					showNoSignalView(true);
				} else if (ScreenSaverMode.DTV_SS_COMMON_VIDEO == mScreenSaverStatus) {
					mNosignalText.setText(R.string.str_no_signal);
					////mNosignalText.setBackground(getResources().getDrawable(R.drawable.ui_sdk_other_page_bg_pe));
					////mNosignalText.setText(R.string.str_null);
					mIsScreenSaverShown = false;
					Log.d("Jason23","showNoSignalView:false:36");
					showNoSignalView(false);
				}
				break;
			case TvCommonManager.INPUT_SOURCE_YPBPR:
			case TvCommonManager.INPUT_SOURCE_YPBPR2:
			case TvCommonManager.INPUT_SOURCE_YPBPR3:
				break;
			case TvCommonManager.INPUT_SOURCE_HDMI:
			case TvCommonManager.INPUT_SOURCE_HDMI2:
			case TvCommonManager.INPUT_SOURCE_HDMI3:
			case TvCommonManager.INPUT_SOURCE_HDMI4:
				if ((SignalProgSyncStatus.UNSTABLE == status)
						|| (SignalProgSyncStatus.STABLE_UN_SUPPORT_MODE == status)) {
					mIsScreenSaverShown = true;
					Log.d("Jason23","showNoSignalView:ture:37");
					showNoSignalView(true);
				} else if (SignalProgSyncStatus.STABLE_SUPPORT_MODE == status) {
					mIsScreenSaverShown = false;
					Log.d("Jason23","showNoSignalView:false:38");
					showNoSignalView(false);
				}
				break;
			default:
				break;
			}
			if(mIsScreenSaverShown)
			startNoSignal();
		};
	};

	private void tvApkExitHandler() {
		mIsActive = false;
		bCmd_TvApkExit = true;
		mIsBackKeyPressed = true;
		showExitDialog();
		return;
	}

	private class CecCtrlEventListener implements TvCecManager.OnCecCtrlEventListener {

		@Override
		public boolean onCecCtrlEvent(int what, int arg1, int arg2) {
			switch (what) {
			case TvCecManager.TVCEC_STANDBY: {
				if(log)
				Log.i(TAG, "&&&&&&&&&&______________EV_CEC_STANDBY");
				TvCommonManager.getInstance().standbySystem("cec");
			}
				break;
            case TvCecManager.TVCEC_SET_MENU_LANGUAGE: {
                Log.i(TAG, "EV_CEC_SET_MENU_LANGUAGE");
                try {
					IActivityManager am = ActivityManagerNative.getDefault();
					Configuration config = am.getConfiguration();
					config.locale = TvLanguage.getLocale(arg1, config.locale);

					// indicate this isn't some passing default - the user
					// wants
					// this remembered
					config.userSetLocale = true;
					am.updateConfiguration(config);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
                break;
            case TvCecManager.TVCEC_SOURCE_SWITCH: {
                Log.i(TAG, "EV_CEC_SOURCE_SWITCH");
                startSourceChange(arg1);
            }
                break;
            case TvCecManager.TVCEC_SEL_DIGITAL_SERVICE_DVB: {
                Log.i(TAG, "EV_CEC_SEL_DIGITAL_SERVICE_DVB");
                Log.i(TAG, "msg.arg1: " + arg1 + " msg.arg2: " + arg2);
				TvChannelManager.getInstance().selectProgram(arg1, TvChannelManager.SERVICE_TYPE_DTV);
            }
                break;
            case TvCecManager.TVCEC_UPDATE_EDID: {
				/*
                if (null == mCecInfoDialog) {
                    if (TvCecManager.CEC_DIALOG_SHOW == arg1) {
                        mCecInfoDialog = ProgressDialog.show(RootActivity.this, "", getString(R.string.str_updating_edid), true, false);
                        mIsCecDialogCanceled = false;
                        mHandler.sendEmptyMessageDelayed(Constants.ROOTACTIVITY_CANCEL_DIALOG, CEC_INFO_DISPLAY_TIMEOUT);
                    }
                } else {
                    if (TvCecManager.TVCEC_STANDBY == arg1) {
                        mIsCecDialogCanceled = true;
                    }
                }
                */
            }
                break;
            default: {
				if(log)
                Log.i(TAG, "Unknown message type " + what);
            }
                break;
        
			}
			return true;
		}
	}

	protected class UiEventListener implements TvCiManager.OnUiEventListener {

		@Override
		public boolean onUiEvent(int what) {
			switch (what) {
			case TvCiManager.TVCI_UI_DATA_READY: {
				if (TvCommonManager.INPUT_SOURCE_STORAGE != mTvCommon
						.getCurrentTvInputSource()) {
					ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
					String foreGroundActivity = am.getRunningTasks(1).get(0).topActivity
							.getClassName();
					/****
					if (foreGroundActivity
							.equals(CimmiActivity.class.getName()) == false) {
						Intent intent = new Intent(RootActivity.this,
								CimmiActivity.class);
						RootActivity.this.startActivity(intent);
					}*****/
				}
			}
				break;
			case TvCiManager.TVCI_UI_CARD_INSERTED: {
				Toast toast = Toast.makeText(RootActivity.this,
						R.string.str_cimmi_hint_ci_inserted, Toast.LENGTH_LONG);
				toast.show();
			}
				break;
			case TvCiManager.TVCI_UI_CARD_REMOVED: {
				Toast toast = Toast.makeText(RootActivity.this,
						R.string.str_cimmi_hint_ci_removed, Toast.LENGTH_SHORT);
				toast.show();
				if (mScreenSaverStatus == ScreenSaverMode.DTV_SS_SCRAMBLED_PROGRAM) {
					mScreenSaverStatus = ScreenSaverMode.DTV_SS_NO_CI_MODULE;
					sendBroadcast(new Intent(TvIntent.ACTION_REDRAW_NOSIGNAL));
				}
			}
				break;
			default: {
			}
				break;
			}
			return true;
		}
	}

	private class CiStatusChangeEventListener implements
			OnCiStatusChangeEventListener {

		@Override
		public boolean onCiStatusChanged(int what, int arg1, int arg2) {
			Log.i(TAG, "onCiStatusChanged(), what:" + what);
			switch (what) {
			case TvCiManager.TVCI_STATUS_CHANGE_TUNER_OCCUPIED: {
				switch (arg1) {
				case TvCiManager.CI_NOTIFY_CU_IS_PROGRESS: {
					openNotifyMessage(getString(R.string.str_cam_upgrade_alarm));
				}
					break;
				case TvCiManager.CI_NOTIFY_OP_IS_TUNING: {
					openNotifyMessage(getString(R.string.str_op_tuning_alarm));
				}
					break;
				default: {
					Log.d(TAG, "Unknown CI occupied status, arg1 = " + arg1);
				}
					break;
				}
				Log.i(TAG,
						"sendBroadcast CIPLUS_TUNER_UNAVAIABLE intent: status = "
								+ arg1);
				Intent intent = new Intent(
						TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE);
				intent.putExtra(Constants.TUNER_AVAIABLE, false);
				sendBroadcast(intent);
			}
				break;
			default: {
			}
				break;

			}
			return true;
		}
	}

	Thread t_atv = null;

	Thread t_dtv = null;

	private void startThread(boolean type) {
		if (type) {
			// atv
			if (t_atv == null) {
				t_atv = new Thread(new Runnable() {
					@Override
					public void run() {
						/*mK_ChannelModel.K_changeToFirstService(
								K_Constants.FIRST_SERVICE_INPUT_TYPE_ATV,
								K_Constants.FIRST_SERVICE_DEFAULT);*/
						TvChannelManager.getInstance().setAtvChannel(TvChannelManager.getInstance().getCurrentChannelNumber());
						t_atv = null;
					}
				});
				t_atv.start();
			}
		} else {
			if (t_dtv == null) {
				// dtv
				t_dtv = new Thread(new Runnable() {
					@Override
					public void run() {
						/*mK_ChannelModel.K_changeToFirstService(
								K_Constants.FIRST_SERVICE_INPUT_TYPE_DTV,
								K_Constants.FIRST_SERVICE_DEFAULT);*/
						TvChannelManager.getInstance().playDtvCurrentProgram();
						t_dtv = null;
					}
				});
				t_dtv.start();
			}
		}
	}

	private void startNoSignal() {
		if(log)
		Log.d(TAG, "mIsSignalLock: " + mIsSignalLock + " mIsScreenSaverShown: "
				+ mIsScreenSaverShown);

		if (mIsSignalLock) {

			if (mIsScreenSaverShown) {
				Log.d("Jason23","showNoSignalView:true:39");
				showNoSignalView(true);
			} else {
				Log.d("Jason23","showNoSignalView:false:40");
				showNoSignalView(false);
			}
		} else {
			Log.d("Jason23","showNoSignalView:true41");
			showNoSignalView(true);
		}
	}

	private void startSourceChange(final int inputsource) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d("Maxs250","RootActivity:startSourceChange:setInputSource:inputsource = " + inputsource);
				TvCommonManager.getInstance().setInputSource(inputsource);
				if (inputsource == TvCommonManager.INPUT_SOURCE_ATV) {
					int curChannelNumber = TvChannelManager.getInstance()
							.getCurrentChannelNumber();
					if (curChannelNumber > 0xFF) {
						curChannelNumber = 0;
					}
					TvChannelManager.getInstance().setAtvChannel(
							curChannelNumber);
				} else if (inputsource == TvCommonManager.INPUT_SOURCE_DTV) {

					TvChannelManager.getInstance().playDtvCurrentProgram();
				}
			}
		}).start();
	}

	/*
	 * FIXME: By Android Api Guide, getRunningTasks should not be used in our
	 * code's core section. It is only using for debugging. But we do not have a
	 * better method to determinant whether RootActivity is in top or not, So we
	 * used this method temporarily.
	 */
	private boolean isTopActivity(String className) {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		ComponentName cn = manager.getRunningTasks(1).get(0).topActivity;
		String topActivityName = cn.getClassName();
		return topActivityName.equals(className);
	}

	/**
	 * get back key status in root activity
	 * 
	 * @return boolean , true is back key pressed.
	 */
	public static boolean getBackKeyStatus() {
		return mIsBackKeyPressed;
	}

	/**
	 * get root activity status
	 * 
	 * @return boolean , true means root activity is active now.
	 */
	public static boolean getActiveStatus() {
		return mIsActive;
	}

	/**
	 * Add API when system is reboot.
	 * 
	 * @author gerard.jiang
	 * @serialData 2013/04/28
	 * @param flag
	 */
	public static void setRebootFlag(boolean flag) {
		isReboot = flag;
	}

	/**
	 * Show the exit dialog.
	 * 
	 * @author gerard.jiang
	 * @param
	 */
	private void showExitDialog() {
		// ***add by allen.sun 2013-5-27
		// Adaptation different resolutions in STB
		if (Tools.isBox()) {
			if(log)
			Log.i(TAG, "start com.mstar.pipservice");
			Intent pipIntent = new Intent("com.mstar.pipservice");
			pipIntent.putExtra("cmd", "invisible");
			RootActivity.this.startService(pipIntent);
		}
		// ***and end
		mExitDialog.setOwnerActivity(rootActivity);
		///mExitDialog.show();
		skyTVDialog = new SkyTVDialog();
		skyTVDialog.init(rootView,this);
		skyTVDialog.showTvDialog(getString(R.string.str_root_alert_dialog_message), null, getString(R.string.str_root_alert_dialog_confirm), getString(R.string.str_root_alert_dialog_cancel), new OnDialogOnKeyListener() {
			@Override
			public boolean onDialogOnKeyEvent(int keyCode, KeyEvent keyEvent) {
				mIsBackKeyPressed = false;
				skyTVDialog.hideTvDialog();
				if (keyCode == KeyEvent.KEYCODE_BACK){
					mIskeyLocked = false;
					mIsActive = true;
					bCmd_TvApkExit = false;
					updateScreenSaver();
				}
				Log.d("Maxs59","RootActivity:onDialogOnKeyEvent!");
				return false;
			}

			@Override
			public void onFirstBtnOnClickEvent() {
				LittleDownTimer.destory();
				mIsBackKeyPressed = false;
				mIsExiting = true;
				skyTVDialog.hideTvDialog();
				//mwt 20140825
				try {
					TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOffStatus");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				////
				//mengwt 20141015
				Settings.System.putInt(getContentResolver(), "source_hot_key_disable", 1);
				//mengwt 20141015
				finish();

				Log.d("Maxs59","RootActivity:onFirstBtnOnClickEvent!");
			}

			@Override
			public void onSecondBtnOnClickEvent() {
				mIsActive = true;
				skyTVDialog.hideTvDialog();
				mIsBackKeyPressed = false;
				bCmd_TvApkExit = false;
				updateScreenSaver();
				mIskeyLocked = false;
				mIsBackKeyPressed = false;

				Log.d("Maxs59","RootActivity:onSecondBtnOnClickEvent!");
			}
		},null);

	}

	/**
	 * Open SourceInfoActivity after checking program lock. if input source is
	 * not tv, this function works as starting source info activity.
	 */
	private void startSourceInfo() {
		int curInput = TvCommonManager.getInstance().getCurrentTvInputSource();
			/*
			 * when RootActivity is not running, we don't start activity to
			 * interrupt other menu, so we send SIGNAL_LOCK action to source
			 * info for updating content if SourceInfoActivity is alive, its
			 * BoradcastReceiver will handle this event.
			 */
			///if (false == isTopActivity(RootActivity.class.getName())) {
			///	sendBroadcast(new Intent(TvIntent.ACTION_SIGNAL_LOCK));
			///} else {

				//Intent intent = new Intent(TvIntent.ACTION_SOURCEINFO);
				//startActivity(intent);
			///}
		Log.d("Maxs29","--->isRootActive = " + isRootActive);
		if (!isRootActive){
			sendBroadcast(new Intent(TvIntent.ACTION_SIGNAL_LOCK));
		}else{
			Intent intent = new Intent(TvIntent.ACTION_SOURCEINFO);
			startActivity(intent);
		}
	}

	private void displayOpRefreshconfirmation() {
		this.runOnUiThread(new Runnable() {
			public void run() {
				mCiPlusOPRefreshDialog.show();
			}
		});
	}

	private void showNoSignalView(boolean isShown) {
		if(log)
		Log.d(TAG, "show nosignal view :" + isShown);
		int curInputSource = mTvCommon.getCurrentTvInputSource();
		//nathan.liao 2014.10.14 add for pass word menu and nosigal menu overlay start
		if (isShown && !isHotelMenuLockFlag) {
				if(!isOnPauseFlag&&!disableNoSignal){
					//if(curInputSource!=K_Constants.INPUT_SOURCE_ATV){
					if (signalstate.getVisibility() == View.INVISIBLE){
						////if (TextView.INVISIBLE == mNosignalText.getVisibility()) {
							if (isRootActive == true){
								if ((DataBaseHelper.getInstance(this).K_getValueDatabase_systemsetting("bAgeModeFlag") == 0) && (
										DataBaseHelper.getInstance(this).K_getValueDatabase_factoryextern("TestPatternMode") == 0)){
									///mNosignalText.setVisibility(TextView.VISIBLE);
									signalstate.setVisibility(View.VISIBLE);
								}
							}
						}
					//}
				}
			} else {
				////if (TextView.VISIBLE == mNosignalText.getVisibility()) {
			if (View.VISIBLE == signalstate.getVisibility()){
					////mNosignalText.setVisibility(TextView.INVISIBLE);
				signalstate.setVisibility(View.INVISIBLE);

				}
			}
		
	}

	private boolean sendCecKey(int keyCode) {
		int curInputSource = TvCommonManager.getInstance()
				.getCurrentTvInputSource();
		if (TvCecManager.getInstance().getCecConfiguration().cecStatus == mCecStatusOn) {
			if (curInputSource == TvCommonManager.INPUT_SOURCE_HDMI
					|| curInputSource == TvCommonManager.INPUT_SOURCE_HDMI2
					|| curInputSource == TvCommonManager.INPUT_SOURCE_HDMI3
					|| curInputSource == TvCommonManager.INPUT_SOURCE_HDMI4) {
				if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
	                            mTvCecManager.enableDeviceMenu();
					if (mTvCecManager.sendCecKey(keyCode)) {
						if(log)
						Log.d(TAG, "send Cec key,keyCode is " + keyCode
								+ ", tv don't handl the key");
						return true;
					}
				}
			} else if (curInputSource == TvCommonManager.INPUT_SOURCE_DTV
					|| curInputSource == TvCommonManager.INPUT_SOURCE_ATV
					|| curInputSource == TvCommonManager.INPUT_SOURCE_CVBS
					|| curInputSource == TvCommonManager.INPUT_SOURCE_CVBS2
					|| curInputSource == TvCommonManager.INPUT_SOURCE_CVBS3
					|| curInputSource == TvCommonManager.INPUT_SOURCE_CVBS4
					|| curInputSource == TvCommonManager.INPUT_SOURCE_YPBPR
					|| curInputSource == TvCommonManager.INPUT_SOURCE_YPBPR2
					|| curInputSource == TvCommonManager.INPUT_SOURCE_YPBPR3) {
				if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
						|| keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
						|| keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
						|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT
						|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
					if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
						keyCode = KeyEvent.KEYCODE_VOLUME_DOWN;
					}else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
						keyCode = KeyEvent.KEYCODE_VOLUME_UP;
					}
					if (mTvCecManager.sendCecKey(keyCode)) {
						if(log)
						Log.d(TAG, "send Cec key,keyCode is " + keyCode
								+ ", tv don't handl the key");
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean sendGingaKey(int keyCode, KeyEvent event) {
		final boolean down = event.getAction() == KeyEvent.ACTION_DOWN;

		if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
			if (TvGingaManager.getInstance().isGingaRunning()) {
				if (down) {
					if (TvGingaManager.getInstance().processKey(keyCode, true)) {
						if(log)
						Log.i(TAG, "onKeyDown GingaStatusMode:processKey");
						return true;
					}
				} else {
					if (TvGingaManager.getInstance().processKey(keyCode, false)) {
						if(log)
						Log.i(TAG, "onKeyUp GingaStatusMode:processKey");
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean sendHbbTVKey(int keyCode) {
		if (TvCommonManager.getInstance().getCurrentTvSystem() <= TvCommonManager.TV_SYSTEM_DTMB
				&& TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
			if (TvHbbTVManager.getInstance().isHbbTVEnabled()) {
				if (TvHbbTVManager.getInstance().hbbtvKeyHandler(keyCode)) {
					if(log)
					Log.i(TAG, "onKeyDown HbbTV:sendHbbTVKey");
					return true;
				}
			}
		}
		return false;
	}

	private boolean sendMheg5Key(int keyCode) {
		boolean ret = false;
		try{
		if (TvManager.getInstance().getPlayerManager().isMheg5Running()) {
			ret = TvManager.getInstance().getPlayerManager().sendMheg5Key(keyCode);
		} else {
			if(log)
				Log.i(TAG, "isMheg5Running return fali!");
		}
		}catch (TvCommonException e){}
		return ret;
	}

	private void updateScreenSaver() {

		/*
		 * FIXME: This function is called only when tv first boot up. FIXME:
		 * because unlock event maybe post before tvapk is ready. FIXME: so we
		 * use this function to draw nosignal when first boot up.
		 */
		//ktc nathan.liao 2016.05.17 add for issue ERP Q160513003 start
		/*Boolean isPowerOn = getIntent() != null ? getIntent().getBooleanExtra(
				"isPowerOn", false) : false;*/
		if (!mIsPowerOn) {
			if(log)
			Log.d(TAG,
					"only use updateScreenSaver to update NoSignalTextView when first boot up");
			return;
		}
		//ktc nathan.liao 2016.05.17 add for issue ERP Q160513003 end
		/**
		 * if apk is not exiting or need to update screen saver, send screen
		 * saver status or signal lock status to handler for updating screen.
		 */
		int curInputSource = mTvCommon.getCurrentTvInputSource();
		int curSubInputSource = mTvCommon.getCurrentSubInputSource()
				.ordinal();
		mIsSignalLock = mTvCommon.isSignalStable(curInputSource);

		if (!mIsSignalLock && !bCmd_TvApkExit) {
			boolean bSubPopSignalLock = false;

				if (TvPipPopManager.getInstance().isPipModeEnabled()) {
					if (TvPipPopManager.getInstance().getPipMode() == EnumPipModes.E_PIP_MODE_POP) {
						if (mTvCommon.isSignalStable(curSubInputSource)) {
							bSubPopSignalLock = true;
						}
					}
				}

			/**
			 * Send Signal Unlock to signalLock Handler if sub inputsource is
			 * signal stabled and inputsource is not changing now.
			 */
			if (!bSubPopSignalLock && !KtcApplication.getSourceDirty()) {
				Message msgLock = Message.obtain();
				msgLock.arg1 = TvEvent.SIGNAL_UNLOCK;
				signalLockHandler.sendMessage(msgLock);
			}
		}
		
		/**
		 * update screen saver status to screenSaverHandler if screen saver need
		 * to show and apk is not exiting.
		 */
		if (mIsScreenSaverShown && !bCmd_TvApkExit && !mIsBackKeyPressed) {
			Message msgSaver = Message.obtain();
			msgSaver.arg1 = mScreenSaverStatus;
			msgSaver.arg2 = curInputSource;
			if(log)
			Log.d(TAG, "update screen saver source :" + curInputSource
					+ " status :" + msgSaver.arg1);
			screenSaverHandler.sendMessage(msgSaver);
		}
	}

	private void openNotifyMessage(final String msg) {
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(RootActivity.this, msg, Toast.LENGTH_LONG)
						.show();
			}
		});
	}
		//ktc nathan.liao 20140905 for hotelmenu start
		
		private void checkInput() {
	    	boolean correctedRule = checkInputRule();
	    	if (correctedRule) {
	    		String password = getInputPassword();
	    		boolean correctedPassword = verifyPasswd(password);
	    		if (correctedPassword){
	    			rllyPassword.setVisibility(View.GONE);
					for (Button btn : mPasswdViews) {
						btn.setFocusable(false);
					}
	    			resetInput();
	    			mDataBaseUtil.updateDatabase_systemsetting("bIsBlocked", (short) 0);
					try {
						TvManager.getInstance().setTvosCommonCommand("SetChannelLock");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			isHotelMenuLockFlag = isCurrentChannelLock();
	    		} else {
	    			resetInput();
	    			Toast.makeText(RootActivity.this,
	    					R.string.password_incorrect_hint, Toast.LENGTH_SHORT).show();
	    		}
	    	}
	    }

	    /**
	     * 检查是否输入完�?	     * @return
	     */
	    private boolean checkInputRule() {
	    	for (String passwd : mEnterPassword) {
	    		if (TextUtils.isEmpty(passwd))
	    			return false;
	    	}

	    	return true;
	    }

	    private String getInputPassword() {
	    	StringBuffer sb = new StringBuffer();
	    	for (String passwd : mEnterPassword) {
	    		sb.append(passwd);
	    	}

	    	return sb.toString();
	    }

	    private void resetInput() {
	    	for (int i = 0; i < mEnterPassword.length; i++) {
	    		mEnterPassword[i] = null;
	    		mPasswdViews[i].setText("");
	    	}

	    	mPasswdViews[0].requestFocus();
	    }

	    /**
	     * 验证密码
	     * @param password
	     * @return
	     */
	    private boolean verifyPasswd(String password) {
	    	String hotelPasswd = PropHelp.newInstance().getHotelChlockPasswd();
	    	return hotelPasswd.equals(password);
	    }
	    

		//ktc nathan.liao 20140905 for hotelmenu end

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP
					|| keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
				return true;
			}
			if ((keyCode == KeyEvent.KEYCODE_1
	    			|| keyCode == KeyEvent.KEYCODE_2
	    			|| keyCode == KeyEvent.KEYCODE_3
	    			|| keyCode == KeyEvent.KEYCODE_4
	    			|| keyCode == KeyEvent.KEYCODE_5
	    			|| keyCode == KeyEvent.KEYCODE_6
	    			|| keyCode == KeyEvent.KEYCODE_7
	    			|| keyCode == KeyEvent.KEYCODE_8
	    			|| keyCode == KeyEvent.KEYCODE_9
	    			|| keyCode == KeyEvent.KEYCODE_0)) {
				if (event.getAction() != KeyEvent.ACTION_DOWN)
					return true;

				String value = String.valueOf(keyCode - KeyEvent.KEYCODE_0);
				for (int i = 0; i < mPasswdIds.length; i++) {
					if (mPasswdIds[i] == v.getId()) {
						mPasswdViews[i].setText("*");
						mEnterPassword[i] = value;
						if (i < mPasswdIds.length - 1)
							mPasswdViews[i + 1].requestFocus();
						else
							checkInput();
						return true;
					}
				}
	    	} else if (event.getAction() == KeyEvent.ACTION_DOWN &&
	    			keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
	    		for (int i = 0; i < mPasswdIds.length; i++) {
					if (mPasswdIds[i] == v.getId()) {
						resetInput();
						return true;
					}
				}
	    	} else if (event.getAction() == KeyEvent.ACTION_DOWN &&
	    			keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
	    		for (int i = 0; i < mPasswdIds.length; i++) {
					if (mPasswdIds[i] == v.getId()) {
						resetInput();
						return true;
					}
				}
	    	}

			return false;
		}
		//nathan.liao 2014.10.14 add for pass word menu and nosigal menu overlay start
		private boolean isCurrentChannelLock()
		{
			boolean flag = false;
			int curInputSource = TvCommonManager.getInstance()
					.getCurrentTvInputSource();
			if(log)
			Log.v(TAG, "======nathan=====curInputSource="+curInputSource);
			if (curInputSource == TvCommonManager.INPUT_SOURCE_ATV
					|| curInputSource == TvCommonManager.INPUT_SOURCE_DTV)
			{
				if(!isReboot)
				{
				boolean hotelMode = mDataBaseUtil.getValueDatabase_systemsetting("Hotelmode") > 0 ? true : false;
				boolean isBlocked = mDataBaseUtil.getValueDatabase_systemsetting("bIsBlocked") > 0 ? true : false;
				ProgramInfo info = TvChannelManager.getInstance().getCurrentProgramInfo();
					if(log)
					Log.v(TAG, "hotelMode="+hotelMode+"=====isBlocked="+isBlocked+"========info.isLock"+info.isLock);
				if(hotelMode && isBlocked && info.isLock)
				{
					flag = true;
				}
			}
			}
			return flag;
		}
		//nathan.liao 2014.10.14 add for pass word menu and nosigal menu overlay end
}

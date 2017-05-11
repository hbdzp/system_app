//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.horion.tv.util.Tools;
import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvPictureManager;
import com.mstar.android.tvapi.common.vo.CecSetting;
import com.mstar.android.tvapi.common.vo.EnumAvdVideoStandardType;
import com.mstar.android.tvapi.common.vo.VideoInfo.EnumScanType;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.horion.tv.R;
import com.horion.tv.framework.ui.TvIntent;
import com.horion.tv.util.PropHelp;
import com.horion.tv.util.TvEvent;
import com.mstar.android.MIntent;
import android.os.SystemClock;
import android.text.format.Time;

import com.mstar.android.tv.TvCecManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvDvbChannelManager;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tv.TvTimerManager;
import com.mstar.android.tvapi.atv.AtvManager;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.atv.vo.EnumGetProgramInfo;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.listener.OnTvPlayerEventListener;
import com.mstar.android.tvapi.common.vo.HbbtvEventInfo;
import com.mstar.android.tvapi.common.vo.PresentFollowingEventInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfoQueryCriteria;
import com.mstar.android.tvapi.common.vo.VideoInfo;
import com.mstar.android.tvapi.dtv.listener.OnDtvPlayerEventListener;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEitInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;
import com.mstar.android.tvapi.dtv.vo.DtvSubtitleInfo;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.dtv.vo.EpgEventInfo;

import org.w3c.dom.Text;

public class SourceInfoActivity extends MstarBaseActivity {

    private static final String TAG = "SourceInfoActivity";

    private static final int CMD_SET_CURRENT_TIME = 0x00;

    private static final int CMD_UPDATE_SOURCE_INFO = 0X01;

    private static final int CMD_SIGNAL_LOCK = 0x02;

    private static final int SHOW_EPG_INFO_DELAY_TIME = 0xFF;

    private final int MAX_TIMES = 10;

    private final int mCecStatusOn = 1;

    private int mCount = 0;

    private static final int MAX_VALUE_OF_ONE_DIGIT = 9;

    private static final int MAX_VALUE_OF_TWO_DIGIT = 99;

    private static final int MAX_VALUE_OF_THREE_DIGIT = 999;

    private TvChannelManager mTvChannelManager = null;

    private int mInputSource = TvCommonManager.INPUT_SOURCE_NONE;

    private int mPreviousInputSource = TvCommonManager.INPUT_SOURCE_NONE;

    private VideoInfo mVideoInfo = null;

    private String mStr_video_info = null;

    private int mCurChannelNumber = 1;



    private TextView mTitle = null;

    private TextView mInfo = null;

    private TextView mFirstTvNumberIcon = null;

    ///private TextView mSecondTvNumberIcon = null;

    ///private TextView mThirdTvNumberIcon = null;

    private TextView mSource_info_tvname;

    private TextView mSource_info_imageformat;


    private TextView mSource_info_audioformat;

    private LinearLayout dtvEpgInfo;

    private TextView currProgTimeName;
    private TextView nextProgTimeName;

    private String mStr;


    private TvEpgManager mTvEpgManager = null;
    private TvCecManager mTvCecManager = null;

    protected TimerTask mTimerTask;

    // protected ST_Time curDateTime;
    public static boolean isDTVChannelNameReady = true;

    // private static boolean isATVProgramInfoReady = true;
    protected Timer mTimer;

    private Intent mIntent = null;

    private DtvEitInfo mDtveitinfo;

    private int[] mNumberResIds = { R.drawable.popup_img_number_0,
            R.drawable.popup_img_number_1, R.drawable.popup_img_number_2,
            R.drawable.popup_img_number_3, R.drawable.popup_img_number_4,
            R.drawable.popup_img_number_5, R.drawable.popup_img_number_6,
            R.drawable.popup_img_number_7, R.drawable.popup_img_number_8,
            R.drawable.popup_img_number_9 };
    private int[] mNumberResIds_aft = { R.drawable.aft_popup_img_number_0,
            R.drawable.aft_popup_img_number_1, R.drawable.aft_popup_img_number_2,
            R.drawable.aft_popup_img_number_3, R.drawable.aft_popup_img_number_4,
            R.drawable.aft_popup_img_number_5, R.drawable.aft_popup_img_number_6,
            R.drawable.aft_popup_img_number_7, R.drawable.aft_popup_img_number_8,
            R.drawable.aft_popup_img_number_9 };

    private String[] mAudioTypeDisplayString = { "MPEG", "AC3", "AAC", "AC3P" };

    private String[] mServiceTypeDisplayString = { "", "DTV", "RADIO", "DATA",
            "UNITED_TV", "INVALID" };

    private String[] mVideoTypeDisplayString = { "", "MPEG", "H.264", "AVS",
            "VC1" };

    private OnAtvPlayerEventListener mAtvPlayerEventListener = null;

    private OnDtvPlayerEventListener mDtvPlayerEventListener = null;

    private OnTvPlayerEventListener mTvPlayerEventListener = null;
    private String preTaskTag = "";
    private LinearLayout infoLinear = null;
    private boolean isStop=false;

    private static long PreSendTime = 0;
    private static long CurrentTime = 0;
    private ArrayList<EpgEventInfo> epgInfos;
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
            Log.i(TAG, "onEpgUpdateList()");
            SourceInfoActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
                        updateSourceInFo();
                    }
                }
            });
            return true;
        }

        @Override
        public boolean onHbbtvUiEvent(int arg0, HbbtvEventInfo arg1) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onPopupDialog(int arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramNotReady(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onPvrNotifyAlwaysTimeShiftProgramReady(int arg0) {
            // TODO Auto-generated method stub
            return false;
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
            // TODO Auto-generated method stub
            return false;
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
            // TODO Auto-generated method stub
            return false;
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
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onScreenSaverMode(int arg0, int arg1) {
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
        public boolean onTvProgramInfoReady(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }
    }

    private class AtvPlayerEventListener implements OnAtvPlayerEventListener {

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
        public boolean onAtvProgramInfoReady(int arg0) {
            Bundle b = new Bundle();
            Message msg = myHandler.obtainMessage();
            msg.what = CMD_UPDATE_SOURCE_INFO;
            b.putInt("CmdIndex", TvEvent.ATV_PROGRAM_INFO_READY);
            myHandler.sendMessage(msg);
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
    }

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
            SourceInfoActivity.isDTVChannelNameReady = true;

            Bundle b = new Bundle();
            Message msg = myHandler.obtainMessage();
            msg.what = CMD_UPDATE_SOURCE_INFO;
            b.putInt("CmdIndex", TvEvent.DTV_CHANNELNAME_READY);
            myHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onDtvPriComponentMissing(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onDtvProgramInfoReady(int arg0) {
            Bundle b = new Bundle();
            Message msg = myHandler.obtainMessage();
            msg.what = CMD_UPDATE_SOURCE_INFO;
            b.putInt("CmdIndex", (int) (TvEvent.DTV_PROGRAM_INFO_READY));
            msg.setData(b);
            myHandler.sendMessage(msg);
            return true;
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

    protected Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case CMD_SET_CURRENT_TIME:
                    break;

                case CMD_UPDATE_SOURCE_INFO:
                    int CmdIndex = bundle.getInt("CmdIndex");
                    onMSrvMsgCmd(CmdIndex);
                    break;

                case CMD_SIGNAL_LOCK:
                    updateSourceInFo();
                    break;
                case SHOW_EPG_INFO_DELAY_TIME:
                    currProgTimeName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    break;
                default:
                    break;
            }
        }
    };

    private void onMSrvMsgCmd(int index) {
        if (index == TvEvent.DTV_CHANNELNAME_READY) {
            updateChannelInfo();
        } else if (index == TvEvent.DTV_PROGRAM_INFO_READY) {
            updateSourceInFo();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent paraIntent = getIntent();
        if (paraIntent != null) {
            preTaskTag = paraIntent.getStringExtra("task_tag");

        }
        mTvEpgManager = TvEpgManager.getInstance();
        mTvCecManager = TvCecManager.getInstance();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TvIntent.ACTION_SOURCE_INFO_DISAPPEAR);
        filter.addAction(TvIntent.ACTION_SIGNAL_LOCK);
        registerReceiver(mReceiver, filter);
        mTvChannelManager = TvChannelManager.getInstance();
        checkInputSourceAndInitView();
        setInvisible();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        // receive dtvlistener call back
        mCount = 0;

        mAtvPlayerEventListener = new AtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnAtvPlayerEventListener(
                mAtvPlayerEventListener);
        mDtvPlayerEventListener = new DtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnDtvPlayerEventListener(
                mDtvPlayerEventListener);
        mTvPlayerEventListener = new TvPlayerEventListener();
        TvChannelManager.getInstance().registerOnTvPlayerEventListener(
                mTvPlayerEventListener);
        //zjf 20160531 add for sync system time start
        if(mInputSource == TvCommonManager.INPUT_SOURCE_DTV
                && !isNetworkConnected(this)){//DTV信源无网络时同步码流时间
            boolean bIsNetAutoTime = getAutoState(Settings.Global.AUTO_TIME);
            boolean bIsTvAutoTime = getAutoState(Settings.Global.TV_AUTO_TIME);
            if(!(bIsNetAutoTime || bIsTvAutoTime))
                return;
            if(bIsNetAutoTime){
                Time currTime = TvTimerManager.getInstance().getCurrentTvTime();
                SystemClock.setCurrentTimeMillis(currTime.toMillis(true));
            }
        }
        //zjf 20160531 add for sync system time end
        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                || mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            try {
                if (mTimer == null)
                    mTimer = new Timer();
                if (mTimerTask == null)
                    mTimerTask = getTimerTask();
                if (mTimer != null && mTimerTask != null)
                    mTimer.schedule(mTimerTask, 10, 1000);
            } catch (Exception e) {
            }
        }
        if (mInputSource == TvCommonManager.INPUT_SOURCE_HDMI
                || mInputSource == TvCommonManager.INPUT_SOURCE_HDMI2
                || mInputSource == TvCommonManager.INPUT_SOURCE_HDMI3
                || mInputSource == TvCommonManager.INPUT_SOURCE_HDMI4) {
            // this will call updateSourceInFo() delay.
            myHandler.sendEmptyMessageDelayed(CMD_SIGNAL_LOCK, 1000);
        } else {
            // this will call updateSourceInFo() delay.
            myHandler.sendEmptyMessageDelayed(CMD_SIGNAL_LOCK, 300);
        }
        if (mInputSource == TvCommonManager.INPUT_SOURCE_YPBPR) {
            try {
                if (mTimer == null)
                    mTimer = new Timer();
                if (mTimerTask == null)
                    mTimerTask = getTimerTask();
                if (mTimer != null && mTimerTask != null)
                    mTimer.schedule(mTimerTask, 10, 1000);
            } catch (Exception e) {
            }
        }
        isStop=false;

    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        if (preTaskTag != null) {
            Log.d("SourceInFoActivity", preTaskTag);
            if ("input_source_changed".equals(preTaskTag)) {
                ////TVRootApp.isSourceDirty = true;
            }
        }
        TvChannelManager.getInstance().unregisterOnAtvPlayerEventListener(
                mAtvPlayerEventListener);
        mAtvPlayerEventListener = null;
        TvChannelManager.getInstance().unregisterOnDtvPlayerEventListener(
                mDtvPlayerEventListener);
        mDtvPlayerEventListener = null;
        TvChannelManager.getInstance().unregisterOnTvPlayerEventListener(
                mTvPlayerEventListener);
        mTvPlayerEventListener = null;

        try {
            if (mTimerTask != null) {
                mTimerTask.cancel();
                mTimerTask = null;
            }
            mTimer.cancel();
            mTimer = null;
        } catch (Exception e) {
        }

        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void setInvisible() {
        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                || mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            mFirstTvNumberIcon.setVisibility(View.INVISIBLE);
           /// mSecondTvNumberIcon.setVisibility(View.INVISIBLE);
           //// mThirdTvNumberIcon.setVisibility(View.INVISIBLE);

        }
    }

    private void checkInputSourceAndInitView() {
        mPreviousInputSource = mInputSource;
        mInputSource = TvCommonManager.getInstance().getCurrentTvInputSource();

        if (mInputSource == mPreviousInputSource) {
            return;
        }

        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            setContentView(R.layout.source_info_atv);
            mTitle = (TextView) findViewById(R.id.source_info_title);
            mFirstTvNumberIcon = (TextView) findViewById(R.id.source_info_textview1);
            ///mSecondTvNumberIcon = (TextView) findViewById(R.id.source_info_textview2);
            ///mThirdTvNumberIcon = (TextView) findViewById(R.id.source_info_textview3);
            mSource_info_tvname = (TextView) findViewById(R.id.source_info_tvname);
            mSource_info_imageformat = (TextView) findViewById(R.id.source_info_imageformat);
            mSource_info_audioformat = (TextView) findViewById(R.id.source_info_audioformat);
            mTitle.setText(R.string.TV_STRING_ATV_CHANNEL);
        } else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            setContentView(R.layout.source_info_dtv);
            ///mTitle = (TextView) findViewById(R.id.source_info_title);
            mFirstTvNumberIcon = (TextView) findViewById(R.id.source_info_textview1);
            ///mSecondTvNumberIcon = (TextView) findViewById(R.id.source_info_textview2);
            ///mThirdTvNumberIcon = (TextView) findViewById(R.id.source_info_textview3);
            mSource_info_tvname = (TextView) findViewById(R.id.source_info_tvname);
            dtvEpgInfo = (LinearLayout)findViewById(R.id.dtv_epg_info);
            dtvEpgInfo.setVisibility(View.INVISIBLE);
            currProgTimeName = (TextView)findViewById(R.id.currprotimename);
            nextProgTimeName = (TextView)findViewById(R.id.nextprotimename);
            ///mTitle.setText("DTV");
        } else {
            setContentView(R.layout.source_info);
            mInfo = (TextView) findViewById(R.id.source_info_textview);
            mTitle = (TextView) findViewById(R.id.source_info_title);
        }
    }

    private void clearTvPartialSourceInfo() {
        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            mSource_info_imageformat.setText("");
            mSource_info_audioformat.setText("");
        } else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            currProgTimeName.setText("");
        }
    }

    private String getCurProgrameName() {
        int pgNum = mTvChannelManager.getCurrentChannelNumber();
        int st = TvChannelManager.SERVICE_TYPE_ATV;
        if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            st = TvChannelManager.SERVICE_TYPE_DTV;
        }
        String pgName = mTvChannelManager.getProgramName(pgNum, st, 0x00);
        return pgName;
    }

    private String getCurProgrameName(short num) {
        int pgNum = mTvChannelManager.getCurrentChannelNumber();
        int st = -1;
        switch ((int) num) {
            case 0:
                st = TvChannelManager.SERVICE_TYPE_ATV;
                break;
            case 3:
                st = TvChannelManager.SERVICE_TYPE_DATA;
                break;
            case 1:
                st = TvChannelManager.SERVICE_TYPE_DTV;
                break;
            case 5:
                st = TvChannelManager.SERVICE_TYPE_INVALID;
                break;
            case 2:
                st = TvChannelManager.SERVICE_TYPE_RADIO;
                break;
            case 4:
                st = TvChannelManager.SERVICE_TYPE_UNITED_TV;
                break;
        }
        String pgName = mTvChannelManager.getProgramName(pgNum, st, 0x00);
        return pgName;

    }

    private void updateChannelInfo() {
        int videostandard = TvChannelManager.AVD_VIDEO_STANDARD_PAL_BGHI;
        checkInputSourceAndInitView();
        clearTvPartialSourceInfo();
        mCurChannelNumber = mTvChannelManager.getCurrentChannelNumber();

        if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            ProgramInfo pinfo = TvChannelManager.getInstance().getCurrentProgramInfo();

            epgInfos = Tools.getEpgEventInfos(pinfo);
            if(null != epgInfos && epgInfos.size()>1) {
                Log.d("Maxs68","epgInfos.get(0).name = " + epgInfos.get(0).name);
                Log.d("Maxs68","epgInfos.get(1).name = " + epgInfos.get(1).name);
                Log.d("Maxs68","Tools.getDate(epgInfos.get(0)) = " + Tools.getDate(epgInfos.get(0)));
                Log.d("Maxs68","Tools.getDate(epgInfos.get(1)) = " + Tools.getDate(epgInfos.get(1)));
                currProgTimeName.setText(Tools.getDate(epgInfos.get(0)) + " " + epgInfos.get(0).name);
                nextProgTimeName.setText(Tools.getDate(epgInfos.get(1)) + " " + epgInfos.get(1).name);
                dtvEpgInfo.setVisibility(View.VISIBLE);
                currProgTimeName.setEllipsize(null);
                if (myHandler.hasMessages(SHOW_EPG_INFO_DELAY_TIME)){
                    myHandler.removeMessages(SHOW_EPG_INFO_DELAY_TIME);
                }
                myHandler.sendEmptyMessageDelayed(SHOW_EPG_INFO_DELAY_TIME, 1000);

            }else{
                dtvEpgInfo.setVisibility(View.INVISIBLE);
            }
            if (pinfo != null) {
                mCurChannelNumber = pinfo.number;
                mSource_info_tvname
                        .setText(getCurProgrameName((pinfo.serviceType)));
                numberToIcon(mCurChannelNumber);

            } else {
                return;
            }



        } else if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            ProgramInfo pinfo = mTvChannelManager.getCurrentProgramInfo();
            int dispCh = mCurChannelNumber;
            if (pinfo == null) {
                return;
            }
            mCurChannelNumber = pinfo.number;
            mSource_info_tvname
                    .setText( getCurProgrameName());
            dispCh = mCurChannelNumber + 1;
            int IsNeedAft = 1;
            int IsSkippedSign = 0;
            try {
                IsNeedAft = AtvManager.getAtvScanManager().getAtvProgramInfo(EnumGetProgramInfo.E_IS_AFT_NEED, pinfo.number);
                IsSkippedSign = AtvManager.getAtvScanManager().getAtvProgramInfo(EnumGetProgramInfo.E_IS_PROGRAM_SKIPPED, pinfo.number);
            }catch (TvCommonException e){

            }


            if((0 == IsNeedAft)||(1 == IsSkippedSign))
                numberToIcon_aft(dispCh);
            else
                numberToIcon(dispCh);
            //ktc nathan.liao 2014.09.03 add for channel aft and skip channel start

            videostandard = mTvChannelManager.getAvdVideoStandard();
            Log.d("Maxs9","videostandard = " + videostandard);
            // get video standard
            switch (videostandard) {
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_BGHI:
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_M:
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_N:
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_60:
                    mStr = "PAL";
                    break;
                case TvChannelManager.AVD_VIDEO_STANDARD_NTSC_44:
                case TvChannelManager.AVD_VIDEO_STANDARD_NTSC_M:
                    mStr = "NTSC";
                    break;
                case TvChannelManager.AVD_VIDEO_STANDARD_SECAM:
                    mStr = "SECAM";
                    break;
                default:
                    mStr = "";
            }
            mSource_info_imageformat
                    .setText( mStr);
            mStr = null;
            Log.d("Maxs9","mTvChannelManager.getAtvAudioStandard() = " + mTvChannelManager.getAtvAudioStandard() + " /mStr = " + mStr);
            if (TvChannelManager.getInstance().isSignalStabled() == true){
                switch (mTvChannelManager.getAtvAudioStandard()) {
                    case TvChannelManager.ATV_AUDIO_STANDARD_BG:
                        mStr = "BG";
                        break;
                    case TvChannelManager.ATV_AUDIO_STANDARD_DK:
                        mStr = "DK";
                        break;
                    case TvChannelManager.ATV_AUDIO_STANDARD_I:
                        mStr = " I";
                        break;
                    case TvChannelManager.ATV_AUDIO_STANDARD_L:
                        mStr = " L";
                        break;
                    case TvChannelManager.ATV_AUDIO_STANDARD_M:
                        mStr = " M ";
                        break;
                    default:
                        mStr = "BG";
                }
            }else{
                ////mStr = getResources().getString(R.string.str_sound_format_unknown);
            }


            mSource_info_audioformat
                    .setText( mStr);

        }
    }






    /**
     * Select source icon according to the input source
     */
    private void selectIconByInputSource() {
        selectVideoInfo();
        switch (mInputSource) {
            case TvCommonManager.INPUT_SOURCE_VGA:
                setSourceInfo(
                        getResources().getString(R.string.str_sourceinfo_VGA));
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS:
                //nathan.liao 2015.03.03 add for T4C1 board start
                if(PropHelp.newInstance().isHasT4C1Board())
                    setSourceInfo(
                            getResources().getString(R.string.str_sourceinfo_CVBS));
                else
                    //nathan.liao 2015.03.03 add for T4C1 board end
                    setSourceInfo(
                            getResources().getString(R.string.str_sourceinfo_CVBS1));
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS2:
                setSourceInfo(
                        getResources().getString(R.string.str_sourceinfo_CVBS2));
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS3:
                setSourceInfo( "CVBS3");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS4:
                setSourceInfo( "CVBS4");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS5:
                setSourceInfo( "CVBS5");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS6:
                setSourceInfo( "CVBS6");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS7:
                setSourceInfo( "CVBS7");
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS8:
                setSourceInfo( "CVBS8");
                break;
            case TvCommonManager.INPUT_SOURCE_SVIDEO:
                setSourceInfo( "SVIDEO");
                break;
            case TvCommonManager.INPUT_SOURCE_SVIDEO2:
                setSourceInfo( "SVIDEO2");
                break;
            case TvCommonManager.INPUT_SOURCE_SVIDEO3:
                setSourceInfo( "SVIDEO3");
                break;
            case TvCommonManager.INPUT_SOURCE_SVIDEO4:
                setSourceInfo("SVIDEO4");
                break;
            case TvCommonManager.INPUT_SOURCE_YPBPR:
                setSourceInfo(
                        getResources().getString(R.string.str_sourceinfo_YPBPR));
                break;
            case TvCommonManager.INPUT_SOURCE_YPBPR2:
                setSourceInfo( "YPBPR2");
                break;
            case TvCommonManager.INPUT_SOURCE_YPBPR3:
                setSourceInfo( "YPBPR3");
                break;
            case TvCommonManager.INPUT_SOURCE_SCART:
                setSourceInfo("SCART");
                break;
            case TvCommonManager.INPUT_SOURCE_SCART2:
                setSourceInfo( "SCART2");
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI:
                if (TvCommonManager.getInstance().isHdmiSignalMode() == false)
                    setSourceInfo( "HDMI1");
                else {

                    if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                        setSourceInfo( "HDMI1");
                    } else {
                        setSourceInfo( "DVI");
                    }
                }
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI2:
                if (TvCommonManager.getInstance().isHdmiSignalMode() == false)
                    setSourceInfo( "HDMI2");
                else {
                    if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                        setSourceInfo( "HDMI2");
                    } else {
                        setSourceInfo( "DVI2");
                    }
                }
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI3:
                if (TvCommonManager.getInstance().isHdmiSignalMode() == false)
                    setSourceInfo( "HDMI3");
                else {
                    if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                        setSourceInfo("HDMI3");
                    } else {
                        setSourceInfo( "DVI3");
                    }
                }
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI4:
                if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                    setSourceInfo( "HDMI4");
                } else {
                    setSourceInfo( "DVI4");
                }
                break;
            case TvCommonManager.INPUT_SOURCE_ATV:
            case TvCommonManager.INPUT_SOURCE_DTV:
                updateChannelInfo();
                break;
            case TvCommonManager.INPUT_SOURCE_DVI:
                setSourceInfo( "DVI");
                break;
            case TvCommonManager.INPUT_SOURCE_DVI2:
                setSourceInfo( "DVI2");
                break;
            case TvCommonManager.INPUT_SOURCE_DVI3:
                setSourceInfo( "DVI3");
                break;
            case TvCommonManager.INPUT_SOURCE_DVI4:
                setSourceInfo( "DVI4");
                break;
            default:
                break;
        }
    }

    private void setSourceInfo( String strtitle) {
        mTitle.setText(strtitle);
        Log.d("Maxs9","setSourceInfo:mStr_video_info = " + mStr_video_info);
        mInfo.setText(mStr_video_info);
    }

    private void selectVideoInfo() {
        Log.d("Maxs250","--->mVideoInfo == null = " + (mVideoInfo == null));
        if (mVideoInfo != null && mVideoInfo.vResolution != 0) {
            int s16FrameRateShow = (mVideoInfo.frameRate + 5) / 10;
            int scanType = EnumScanType.E_PROGRESSIVE.ordinal();
            try {
                scanType = mVideoInfo.getVideoScanType();
            }catch (TvCommonException e){

            }
            switch (mInputSource) {
                case TvCommonManager.INPUT_SOURCE_ATV:
                case TvCommonManager.INPUT_SOURCE_DTV:
                    if (scanType == EnumScanType.E_PROGRESSIVE.ordinal()) {
                        mStr_video_info = mVideoInfo.vResolution + "P";
                    } else {
                        mStr_video_info = mVideoInfo.vResolution + "I";
                    }
                    Log.d("Maxs9","mStr_videio_info = " + mStr_video_info);
                    break;
                case TvCommonManager.INPUT_SOURCE_VGA:
                case TvCommonManager.INPUT_SOURCE_DVI:
                case TvCommonManager.INPUT_SOURCE_DVI2:
                case TvCommonManager.INPUT_SOURCE_DVI3:
                case TvCommonManager.INPUT_SOURCE_DVI4:
                    mStr_video_info = mVideoInfo.hResolution + "X"
                            + mVideoInfo.vResolution + "@" + s16FrameRateShow
                            + "Hz";
                    break;
                case TvCommonManager.INPUT_SOURCE_HDMI:
                case TvCommonManager.INPUT_SOURCE_HDMI2:
                case TvCommonManager.INPUT_SOURCE_HDMI3:
                case TvCommonManager.INPUT_SOURCE_HDMI4:
                    if (TvCommonManager.getInstance().isHdmiSignalMode() == true) {
                        if (mVideoInfo.vResolution == 1080
                                || mVideoInfo.vResolution == 720
                                || mVideoInfo.vResolution == 480
                                || mVideoInfo.vResolution == 576) {
                            if (scanType == EnumScanType.E_PROGRESSIVE.ordinal()) {
                                mStr_video_info = mVideoInfo.vResolution + "P";
                            } else {
                                mStr_video_info = mVideoInfo.vResolution + "I";
                            }
                            mStr_video_info += "@" + s16FrameRateShow + "Hz";
                        }else{
                            mStr_video_info = mVideoInfo.hResolution + "X"
                                    + mVideoInfo.vResolution + "@" + s16FrameRateShow
                                    + "Hz";
                        }
                    } else {
                        mStr_video_info = mVideoInfo.hResolution + "X"
                                + mVideoInfo.vResolution + "@" + s16FrameRateShow
                                + "Hz";
                    }
                    break;
                case TvCommonManager.INPUT_SOURCE_YPBPR:
                case TvCommonManager.INPUT_SOURCE_YPBPR2:
                case TvCommonManager.INPUT_SOURCE_YPBPR3:
                    if (scanType == EnumScanType.E_PROGRESSIVE.ordinal()) {
                        mStr_video_info = mVideoInfo.vResolution + "P";
                    } else {
                        mStr_video_info = mVideoInfo.vResolution + "I";
                    }
                    mStr_video_info += "@" + s16FrameRateShow + "Hz";
                    break;
                case TvCommonManager.INPUT_SOURCE_CVBS:
                case TvCommonManager.INPUT_SOURCE_CVBS2:
                case TvCommonManager.INPUT_SOURCE_CVBS3:
                    EnumAvdVideoStandardType videoStandard = null;
                    try {
                        videoStandard = TvManager.getInstance().getPlayerManager().getVideoStandard();
                    }catch (TvCommonException e){

                    }
                    if(videoStandard != null)
                    {
                        if( videoStandard == EnumAvdVideoStandardType.SECAM)
                        {
                            mStr_video_info = "SECAM";
                        }else if(videoStandard == EnumAvdVideoStandardType.PAL_60 || videoStandard == EnumAvdVideoStandardType.PAL_BGHI
                                ||videoStandard == EnumAvdVideoStandardType.PAL_M || videoStandard == EnumAvdVideoStandardType.PAL_N)
                        {
                            mStr_video_info = "PAL";
                        }else if(videoStandard == EnumAvdVideoStandardType.NTSC_44 || videoStandard == EnumAvdVideoStandardType.NTSC_M)
                        {
                            mStr_video_info = "NTSC";
                        }else
                        {
                            mStr_video_info = "AUTO";
                        }
                    }
                    break;
                default:
                    if (scanType == EnumScanType.E_PROGRESSIVE.ordinal()) {
                        mStr_video_info = mVideoInfo.vResolution + "P";
                    } else {
                        mStr_video_info = mVideoInfo.vResolution + "I";
                    }
                    break;
            }
        }
        if (TvChannelManager.getInstance().isSignalStabled() == false)
            mStr_video_info = "";
        if (mStr_video_info == "X") {
            mStr_video_info = "";
        }
    }

    public ArrayList<String> numberToPicture(int num) {
        ArrayList<String> strArray = new ArrayList<String>();
        String mStr = num + "";
        for (int i = 0; i < mStr.length(); i++) {
            char ch = mStr.charAt(i);
            strArray.add("" + ch);
        }
        return strArray;
    }
    //ktc nathan.liao 2014.09.03 add for channel aft and skip channel start
    public ArrayList<String> numberToPicture_aft(int num) {
        ArrayList<String> strArray = new ArrayList<String>();
        String str = num + "";
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            strArray.add("" + ch);
        }
        return strArray;
    }
    public void numberToIcon_aft(int number) {
        ArrayList<Integer> n = getResoulseID_aft(numberToPicture_aft(number));
        mFirstTvNumberIcon.setText(number+"");
        if (number <= MAX_VALUE_OF_ONE_DIGIT) {
            Log.d("Maxs66","n.get(0) = " + n.get(0));
            ////mFirstTvNumberIcon.setImageResource(n.get(0));
            ///mSecondTvNumberIcon.setVisibility(View.GONE);
           /// mThirdTvNumberIcon.setVisibility(View.GONE);
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);

        } else if ((MAX_VALUE_OF_ONE_DIGIT < number)
                && (number <= MAX_VALUE_OF_TWO_DIGIT)) {
            Log.d("Maxs66","n.get(0) = " + n.get(0));
            Log.d("Maxs66","n.get(1) = " + n.get(1));
            ////mFirstTvNumberIcon.setImageResource(n.get(0));
            ////mSecondTvNumberIcon.setImageResource(n.get(1));
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
           /// mSecondTvNumberIcon.setVisibility(View.VISIBLE);
           /// mThirdTvNumberIcon.setVisibility(View.GONE);

        } else if ((MAX_VALUE_OF_TWO_DIGIT < number)
                && (number <= MAX_VALUE_OF_THREE_DIGIT)) {
           /// mFirstTvNumberIcon.setImageResource(n.get(0));
           /// mSecondTvNumberIcon.setImageResource(n.get(1));
           //// mThirdTvNumberIcon.setImageResource(n.get(2));
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
            ///mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            ///mThirdTvNumberIcon.setVisibility(View.VISIBLE);

        } else {
            Log.d("Maxs66","--------------");
           /// mFirstTvNumberIcon.setImageResource(n.get(0));
            ///mSecondTvNumberIcon.setImageResource(n.get(1));
           /// mThirdTvNumberIcon.setImageResource(n.get(2));
           /// mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            ///mThirdTvNumberIcon.setVisibility(View.VISIBLE);

        }
    }

    public ArrayList<Integer> getResoulseID_aft(ArrayList<String> mStr) {
        ArrayList<Integer> n = new ArrayList<Integer>();
        for (String string : mStr) {
            Integer resId = mNumberResIds_aft[Integer.parseInt(string)];
            n.add(resId);
        }
        return n;
    }
    //ktc nathan.liao 2014.09.03 add for channel aft and skip channel end
    public ArrayList<Integer> getResoulseID(ArrayList<String> mStr) {
        ArrayList<Integer> n = new ArrayList<Integer>();
        for (String string : mStr) {
            Integer resId = mNumberResIds[Integer.parseInt(string)];
            n.add(resId);
        }
        return n;
    }

    public void numberToIcon(int number) {
        Log.d("Maxs67","number = " + number);
        ArrayList<Integer> n = getResoulseID(numberToPicture(number));
        mFirstTvNumberIcon.setText(number+"");
        if (number <= MAX_VALUE_OF_ONE_DIGIT) {
            Log.d("Maxs67","n.get(0) = " + n.get(0));
           /// mFirstTvNumberIcon.setImageResource(n.get(0));
            ///mSecondTvNumberIcon.setVisibility(View.GONE);
            ///mThirdTvNumberIcon.setVisibility(View.GONE);
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
        } else if ((MAX_VALUE_OF_ONE_DIGIT < number)
                && (number <= MAX_VALUE_OF_TWO_DIGIT)) {
            Log.d("Maxs67","n.get(0) = " + n.get(0));
            Log.d("Maxs67","n.get(1) = " + n.get(1));
            ///mFirstTvNumberIcon.setImageResource(n.get(1));
           /// mSecondTvNumberIcon.setImageResource(n.get(0));
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
            ///mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            ///mThirdTvNumberIcon.setVisibility(View.GONE);

        } else if ((MAX_VALUE_OF_TWO_DIGIT < number)
                && (number <= MAX_VALUE_OF_THREE_DIGIT)) {
            ///mFirstTvNumberIcon.setImageResource(n.get(2));
           /// mSecondTvNumberIcon.setImageResource(n.get(1));
           // mThirdTvNumberIcon.setImageResource(n.get(0));
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
            ///mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            ///mThirdTvNumberIcon.setVisibility(View.VISIBLE);

        } else {
            Log.d("Maxs67","------------");
           /// mFirstTvNumberIcon.setImageResource(n.get(0));
           /// mSecondTvNumberIcon.setImageResource(n.get(1));
          ///  mThirdTvNumberIcon.setImageResource(n.get(2));
            //mFourthTvNumberIcon.setImageResource(n.get(3));
           /// mSecondTvNumberIcon.setVisibility(View.VISIBLE);
           /// mThirdTvNumberIcon.setVisibility(View.VISIBLE);

        }

    }



    private TimerTask getTimerTask() {
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                        || (mInputSource == TvCommonManager.INPUT_SOURCE_DTV)) {
                    myHandler.sendEmptyMessage(CMD_SET_CURRENT_TIME);
                }
                mCount++;
                if (mCount < MAX_TIMES && mCount % 2 == 0// the period is 2s
                        && mInputSource == TvCommonManager.INPUT_SOURCE_YPBPR) {
                    myHandler.sendEmptyMessage(CMD_SIGNAL_LOCK);
                }
            }
        };
        return mTimerTask;
    }

    public void updateSourceInFo() {
        super.onRemoveMessage();
        checkInputSourceAndInitView();
        clearTvPartialSourceInfo();
        if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                || mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
            mFirstTvNumberIcon.setVisibility(View.VISIBLE);
            ///mSecondTvNumberIcon.setVisibility(View.VISIBLE);
            ///mThirdTvNumberIcon.setVisibility(View.VISIBLE);
            if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV){

            }
            else {
            }
        } else
        {
            /*infoLinear.setVisibility(View.VISIBLE);*/
        }
        mVideoInfo = TvPictureManager.getInstance().getVideoInfo();
        selectIconByInputSource();
        super.onSendMessage();
    }

    public ArrayList<ProgramInfo> getAllProgramList() {
        ProgramInfo pgi = null;
        int indexBase = 0;
        ArrayList<ProgramInfo> progInfoList = new ArrayList<ProgramInfo>();
        int currInputSource = TvCommonManager.getInstance()
                .getCurrentTvInputSource();
        int m_nServiceNum = 0;
        if (currInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
            indexBase = mTvChannelManager
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            if (indexBase == 0xFFFFFFFF) {
                indexBase = 0;
            }
            m_nServiceNum = mTvChannelManager
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV_DTV);

        } else {
            indexBase = 0;
            m_nServiceNum = mTvChannelManager
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
        }

        for (int k = indexBase; k < m_nServiceNum; k++) {
            pgi = mTvChannelManager.getProgramInfoByIndex(k);
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



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        mCount = 0;
        CecSetting setting = mTvCecManager.getCecConfiguration();
        switch (mInputSource) {
            case TvCommonManager.INPUT_SOURCE_HDMI:
            case TvCommonManager.INPUT_SOURCE_HDMI2:
            case TvCommonManager.INPUT_SOURCE_HDMI3:
            case TvCommonManager.INPUT_SOURCE_HDMI4: {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    break;
                }
                if ((setting.cecStatus == mCecStatusOn)
                        && (TvCommonManager.getInstance().isHdmiSignalMode() == true)) {
                    if (mTvCecManager.sendCecKey(keyCode)) {
                        Log.d(TAG, "onKeyDown return TRUE");
                        return true;
                    }
                }
                break;
            }
            case TvCommonManager.INPUT_SOURCE_CVBS:
            case TvCommonManager.INPUT_SOURCE_CVBS2:
            case TvCommonManager.INPUT_SOURCE_CVBS3:
            case TvCommonManager.INPUT_SOURCE_CVBS4:
            case TvCommonManager.INPUT_SOURCE_YPBPR:
            case TvCommonManager.INPUT_SOURCE_YPBPR2:
            case TvCommonManager.INPUT_SOURCE_YPBPR3:
            case TvCommonManager.INPUT_SOURCE_DTV:
            case TvCommonManager.INPUT_SOURCE_ATV: {
                if (keyCode == KeyEvent.KEYCODE_VOLUME_UP
                        || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                        || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) {
                    if (setting.cecStatus == mCecStatusOn) {
                        if (mTvCecManager.sendCecKey(keyCode)) {
                            Log.d(TAG, "onKeyDown return TRUE");
                            return true;
                        }
                    }
                }
                break;
            }
            default:
                break;
        }

        if (SwitchPageHelper.goToMenuPage(this, keyCode) == true) {
            finish();
            return true;
        } else if (SwitchPageHelper.goToEpgPage(this, keyCode , mCurChannelNumber) == true) {
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_CHANNEL_DOWN) {
            CurrentTime = System.currentTimeMillis();
            if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                if (Math.abs(CurrentTime - PreSendTime) >= 400){
                    mTvChannelManager.programDown();
                    updateChannelInfo();
                    PreSendTime = System.currentTimeMillis();
                }
                return true;
            } else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                if (isDTVChannelNameReady == true) {
                    if (Math.abs(CurrentTime - PreSendTime) >= 400){
                        isDTVChannelNameReady = false;
                        mTvChannelManager.programDown();
                        startSourceInfo();
                        PreSendTime = System.currentTimeMillis();
                    }
                    return true;

                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP
            ||keyCode == KeyEvent.KEYCODE_CHANNEL_UP) {
            CurrentTime = System.currentTimeMillis();
            if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV) {
                if (Math.abs(CurrentTime - PreSendTime) >= 400){
                    mTvChannelManager.programUp();
                    updateChannelInfo();
                    PreSendTime = System.currentTimeMillis();
                }
                return true;
            } else if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                if (isDTVChannelNameReady == true) {
                    if (Math.abs(CurrentTime - PreSendTime) >= 400){
                        isDTVChannelNameReady = false;
                        mTvChannelManager.programUp();
                        startSourceInfo();
                        PreSendTime = System.currentTimeMillis();
                    }
                    return true;

                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else if (keyCode == MKeyEvent.KEYCODE_CHANNEL_RETURN) {
            if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                    || mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                CurrentTime = System.currentTimeMillis();
                if (Math.abs(CurrentTime - PreSendTime) >= 400){
                    mTvChannelManager.returnToPreviousProgram();
                    updateChannelInfo();
                    PreSendTime = System.currentTimeMillis();
                }
                return true;
            } else {
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_MENU) {
            finish();
            return true;
        } else if (keyCode >= KeyEvent.KEYCODE_1
                && keyCode <= KeyEvent.KEYCODE_9) {
            if (mInputSource == TvCommonManager.INPUT_SOURCE_ATV
                    || mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
                Intent intentChannelControl = new Intent(this,
                        ChannelControlActivity.class);
                intentChannelControl.putExtra("KeyCode", keyCode);
                this.startActivity(intentChannelControl);
                finish();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            String deviceName = InputDevice.getDevice(event.getDeviceId())
                    .getName();
            if (deviceName.equals("MStar Smart TV IR Receiver")
                    || deviceName.equals("MStar Smart TV Keypad")) {
                updateChannelInfo();
            } else {
                Log.d(TAG, "deviceName is:" + deviceName);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            String deviceName = InputDevice.getDevice(event.getDeviceId())
                    .getName();
            if (deviceName.equals("MStar Smart TV IR Receiver")
                    || deviceName.equals("MStar Smart TV Keypad")) {
                updateChannelInfo();
            } else {
                Log.d(TAG, "deviceName is:" + deviceName);
            }
            return true;
        }else if (keyCode == KeyEvent.KEYCODE_KTC_ATV
                || keyCode == KeyEvent.KEYCODE_KTC_AV
                || keyCode == KeyEvent.KEYCODE_KTC_YPBPR
                || keyCode == KeyEvent.KEYCODE_KTC_HDMI
                || keyCode == KeyEvent.KEYCODE_KTC_VGA
                || keyCode == KeyEvent.KEYCODE_KTC_USB) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        isStop=true;
    }

    /**
     * Invoked by MstarBaseActivity in which send the 10s delay.
     */
    @Override
    public void onTimeOut() {
        super.onTimeOut();
        if(!isStop){
            mIntent = new Intent(this, RootActivity.class);
            startActivity(mIntent);
        }
        finish();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TvIntent.ACTION_SIGNAL_LOCK)) {
                myHandler.sendEmptyMessageDelayed(CMD_SIGNAL_LOCK, 1000);// this
            } else if (intent.getAction().equals(
                    TvIntent.ACTION_SOURCE_INFO_DISAPPEAR)) {
                finish();
            }
        }
    };

    /**
     * Open SourceInfoActivity Added by gerard.jiang for "0396073" in 2013/05/18
     */
    private void startSourceInfo() {
        boolean isCurrentProgramLocked = mTvChannelManager
                .getCurrentProgramInfo().isLock;
        if (isCurrentProgramLocked) {
            Intent intent = new Intent(this, SourceInfoActivity.class);
            intent.putExtra("info_key", true);
            startActivity(intent);
        }
    }
    //zjf 20160531 add for sync system time start
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    private boolean getAutoState(String name) {
        try {
            return Settings.Global.getInt(getContentResolver(), name) > 0;
        } catch (SettingNotFoundException snfe) {
            return false;
        }
    }
    //zjf 20160531 add for sync system time end
}

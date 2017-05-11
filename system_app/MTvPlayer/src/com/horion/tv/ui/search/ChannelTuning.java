//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2013 MStar Semiconductor, Inc. All rights reserved.
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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.horion.tv.R;
import com.horion.tv.framework.ui.TvIntent;
import com.ktc.ui.api.SettingAlertDialog;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvChannelManager.DvbcScanParam;
import com.mstar.android.tv.TvChannelManager.OnAtvScanEventListener;
import com.mstar.android.tv.TvChannelManager.OnDtvScanEventListener;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvMhlManager;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.common.vo.TvTypeInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbCountryInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbMuxInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbPrimaryRegionInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbSecondaryRegionInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbTargetRegionInfo;
import com.mstar.android.tvapi.dtv.dvb.vo.DvbTeritaryRegionInfo;
import com.mstar.android.tvapi.dtv.vo.DtvEventScan;

import java.util.ArrayList;

public class ChannelTuning extends Activity {
    /** Called when the activity is first created. */

    private static final String TAG = "ChannelTuning";

    private int mTvSystem = 0;

    private static int ATV_MIN_FREQ = 48250;

    private static int ATV_MAX_FREQ = 863250;

    private static int ATV_EVENTINTERVAL = 500 * 1000;// every 500ms to show

    private final int INVALID_SYMBOL = 0;

    private final int INVALID_FREQUENCY = 0;

    private final int INVALID_NETWORKID = 0;

    private final int DVBS_AUTO_SCAN = 0;

    private final int DVBS_BLIND_SCAN = 1;

    private static int dtvServiceCount = 0;

    private boolean isDtvAutoUpdateScan = false;

    private ViewHolder viewholder_channeltune;

    private TvCommonManager mTvCommonManager = null;

    private Time scanStartTime = new Time();

    private boolean isMhlOpen = false;

    private int scanPercent = -1;

    private int mCurrentRoute = TvChannelManager.TV_ROUTE_NONE;

    private TvChannelManager mTvChannelManager = null;

    private OnAtvScanEventListener mAtvScanEventListener = null;

    private OnDtvScanEventListener mDtvScanEventListener = null;

    private Handler mAtvUiEventHandler = null;

    private Handler mDtvUiEventHandler = null;

    private BroadcastReceiver mReceiver = null;

    private DvbTargetRegionInfo mTargetRegionInfo = null;

    private int mTargetRegionCountryIndex = 0;

    private int mTargetRegionPrimaryIndex = 0;

    private int mTargetRegionSecondaryIndex = 0;

    private int mTargetRegionTertiaryIndex = 0;

    private String mTargetRegionCountryCode = null;

    private short mTargetRegionPrimaryCode = 0;

    private short mTargetRegionSecondaryCode = 0;

    private int mTargetRegionTertiaryCode = 0;

    private int mDtvCount = 0;

    private int mAtvCount = 0;
    private SettingAlertDialog dialogView;
    private setDtvConfigTask task;
    private class AtvScanEventListener implements OnAtvScanEventListener {
        @Override
        public boolean onAtvScanEvent(int what, int arg1, int arg2, Object info) {
            if ((what == TvChannelManager.TVPLAYER_ATV_MANUAL_TUNING_SCAN_INFO)
                || (what == TvChannelManager.TVPLAYER_ATV_AUTO_TUNING_SCAN_INFO)) {
                Message msg = mAtvUiEventHandler.obtainMessage(what, info);
                mAtvUiEventHandler.sendMessage(msg);
                return true;
            }
            return false;
        }
    }

    private class DtvScanEventListener implements OnDtvScanEventListener {
        @Override
        public boolean onDtvScanEvent(int what, int arg1, int arg2, Object info) {
            if (what == TvChannelManager.TVPLAYER_DTV_AUTO_TUNING_SCAN_INFO) {
                Message msg = mDtvUiEventHandler.obtainMessage(what, info);
                mDtvUiEventHandler.sendMessage(msg);
                return true;
            }
            return false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.channeltuning);
        Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 1);
        Settings.System.putInt(getContentResolver(), "setting_hot_key_disable", 1);
        mAtvUiEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateAtvTuningScanInfo((AtvEventScan) msg.obj);
            }
        };

        mDtvUiEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateDtvTuningScanInfo((DtvEventScan) msg.obj);
            }
        };

        viewholder_channeltune = new ViewHolder(ChannelTuning.this);
         viewholder_channeltune.findViewForChannelTuning();
        mTvCommonManager = TvCommonManager.getInstance();
        mTvChannelManager = TvChannelManager.getInstance();

        dtvServiceCount = 0;
        scanStartTime.setToNow();

        mTvSystem = mTvCommonManager.getCurrentTvSystem();
        Log.d("Maxs","mTvSystemww = " + mTvSystem + " /mTvChannelManager.getUserScanType() = " + mTvChannelManager.getUserScanType());

        ///if (false == Utility.isSupportATV()) {
           //// viewholder_channeltune.lineaR_cha_tvprogram.setVisibility(View.GONE);
        ////}
        if ((getIntent() != null) && (getIntent().getExtras() != null)) {
            isDtvAutoUpdateScan = getIntent().getBooleanExtra("DtvAutoUpdateScan", false);
        }

        if (isDtvAutoUpdateScan) {

            /////viewholder_channeltune.text_cha_tuningprogress_type.setText("DTV");
            viewholder_channeltune.textView_search_title.setText(getString(R.string.dtv_search_channel));
            Log.e(TAG, "switchMSrvDtvRouteCmd 1");
            int m_nServiceNum = mTvChannelManager
                    .getProgramCount(TvChannelManager.PROGRAM_COUNT_DTV);
            DvbcScanParam sp = mTvChannelManager.new DvbcScanParam();
            int dvbcRouteIndex = mTvChannelManager
                    .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DVBC);
            if (dvbcRouteIndex < 0) {
                Log.e(TAG, "get route index error");
                return;
            }
            mTvChannelManager.switchMSrvDtvRouteCmd(dvbcRouteIndex);
            mTvChannelManager.dvbcgetScanParam(sp);

            if (m_nServiceNum > 0) {
                DvbMuxInfo dmi = mTvChannelManager.getCurrentMuxInfo();
                if (dmi != null) {
                    sp.u32NITFrequency = dmi.frequency;
                    sp.CAB_Type = dmi.modulationMode;
                    sp.u16SymbolRate = (short) dmi.symbRate;
                    Log.e(TAG, "dmi.u32NITFrequencye: " + sp.u32NITFrequency);
                    Log.e(TAG, "dmi.CAB_Type: " + sp.CAB_Type);
                    Log.e(TAG, "dmi.u16SymbolRate: " + sp.u16SymbolRate);
                    Log.d("Maxs","ChannelRunning:setUserScanType:DTV");
                    mTvChannelManager.setUserScanType(TvChannelManager.TV_SCAN_DTV);
                    mTvChannelManager.setDvbcScanParam(sp.u16SymbolRate, sp.CAB_Type,
                            sp.u32NITFrequency, 0, (short) 0x0000);
                    mTvChannelManager.startQuickScan();
                } else {
                    Log.e(TAG, "getCurrentMuxInfo error");
                    return;
                }
            } else {
                Log.e(TAG, "m_nServiceNum = 0");
                return;
            }
        } else if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL
                || mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV) {
            if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                if ((mTvSystem != TvCommonManager.TV_SYSTEM_ATSC)
                    && (mTvSystem != TvCommonManager.TV_SYSTEM_ISDB)) {
                    /* mantis: 1099862, for DVB auto tuning:
                     * DTV + ATV => remove all ATV channel list
                     * DTV only => no need to remove ATV channel list
                     * ATV only => remove all ATV channel list
                     */
                    mTvChannelManager.deleteAtvMainList();
                }
            }
            /////viewholder_channeltune.text_cha_tuningprogress_type.setText("DTV");
            viewholder_channeltune.textView_search_title.setText(getString(R.string.dtv_search_channel));
            TvTypeInfo tvinfo = mTvCommonManager.getTvInfo();
            int currentRouteIndex = mTvChannelManager.getCurrentDtvRouteIndex();
            mCurrentRoute = tvinfo.routePath[currentRouteIndex];
            int dtmbRouteIndex = mTvChannelManager
                    .getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DTMB);


                    mTvChannelManager.switchMSrvDtvRouteCmd(dtmbRouteIndex);
            Log.d("Maxs","TV_SCAN_ALL:startDTvAutoScan");
                mTvChannelManager.startDtvAutoScan();

        } else {
            /* Auto scan ATV only */
            ///// viewholder_channeltune.text_cha_tuningprogress_type.setText("ATV");
            viewholder_channeltune.textView_search_title.setText(getString(R.string.atv_search_channel));
            String str = "0%49.25";
            if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                TvIsdbChannelManager.getInstance().setAntennaType(
                        TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR);
                ///// viewholder_channeltune.text_cha_tuningprogress_type.setText("AIR");
            } else if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
                TvAtscChannelManager.getInstance().deleteAtvMainList();
            } else {
                /* mantis: 1099862, for DVB auto tuning:
                 * DTV + ATV => remove all ATV channel list
                 * DTV only => no need to remove ATV channel list
                 * ATV only => remove all ATV channel list
                 */
                mTvChannelManager.deleteAtvMainList();
            }
            ////viewholder_channeltune.text_cha_tuningprogress_val.setText(str);
            viewholder_channeltune.textView_autochannel_dtvsearchprogressbarvalue.setText(str);
            Log.d("Maxs","TV_SCAN_ALL:changeLayoutATV");
            changeLayoutATV();
            mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
        }
    }

    @Override
    protected void onResume() {
        isMhlOpen = TvMhlManager.getInstance().getAutoSwitch();
        if (isMhlOpen)
            TvMhlManager.getInstance().setAutoSwitch(false);
        /////viewholder_channeltune.linear_cha_mainlinear.setVisibility(View.VISIBLE);
        viewholder_channeltune.relativeLayout_auto_mainmenu.setVisibility(View.VISIBLE);
        mAtvScanEventListener = new AtvScanEventListener();
        TvChannelManager.getInstance().registerOnAtvScanEventListener(mAtvScanEventListener);

        mDtvScanEventListener = new DtvScanEventListener();
        TvChannelManager.getInstance().registerOnDtvScanEventListener(mDtvScanEventListener);

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (isMhlOpen) {
            TvMhlManager.getInstance().setAutoSwitch(true);
        }
        if (mTvCommonManager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE) {
            return;
        }

        if (scanPercent <= 100) {
            channetuningActivityLeave();
            pauseChannelTuning();
        }

        if (null != mAtvScanEventListener) {
            TvChannelManager.getInstance().unregisterOnAtvScanEventListener(mAtvScanEventListener);
            mAtvScanEventListener = null;
        }

        if (null != mDtvScanEventListener) {
            TvChannelManager.getInstance().unregisterOnDtvScanEventListener(mDtvScanEventListener);
            mDtvScanEventListener = null;
        }

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        ///unregisterReceiver(mReceiver);
        Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 0);
        Settings.System.putInt(getContentResolver(), "setting_hot_key_disable", 0);
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU: {
                Time curTime = new Time();
                curTime.setToNow();
                if (curTime.after(scanStartTime)) {
                    if ((curTime.toMillis(true) - scanStartTime.toMillis(true)) < 1000) {
                        /*Toast toast = Toast.makeText(ChannelTuning.this,
                                "Wait for a moment please!", Toast.LENGTH_SHORT);
                        toast.show();*/
                        return false;
                    }
                }
                channetuningActivityLeave();
                dialogView = new SettingAlertDialog(this);
                dialogView.setCancelable(false);
                dialogView.setListener(new SettingAlertDialog.OnDialogOnKeyListener(){
                    @Override
                    public void onFirstBtnOnClickEvent() {
                        Log.d("Maxs28","onFirstBtnOnClickEvent");
                        ExitTuningActivityExit(false);
                        viewholder_channeltune.relativeLayout_auto_mainmenu.setVisibility(View.VISIBLE);
                        dialogView.dismiss();
                    }

                    @Override
                    public void onSecondBtnOnClickEvent() {
                        Log.d("Maxs28","onSecondBtnOnClickEvent");

                        task = new setDtvConfigTask();
                        task.execute();
                        viewholder_channeltune.relativeLayout_auto_mainmenu.setVisibility(View.VISIBLE);
                        dialogView.dismiss();
                    }

                    @Override
                    public boolean onDialogOnKeyEvent(int i, KeyEvent keyEvent) {
                        return false;
                    }
                });
                Log.d("Maxs30","onKeyDown:mTvChannelManager.getTuningStatus() = " + mTvChannelManager.getTuningStatus());
                if(mTvChannelManager.getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING){
                    dialogView.setTipsString(getString(R.string.search_atv_auto_exit),getString(R.string.search_now_auto_exit));
                }else{
                    dialogView.setTipsString(getString(R.string.search_dtv_auto_exit),getString(R.string.search_now_auto_exit));
                }

                dialogView.setBtnString(getString(R.string.str_program_edit_dialog_cancel),getString(R.string.str_program_edit_dialog_ok));
                if (!dialogView.isShowing()){
                    dialogView.show();
                    viewholder_channeltune.relativeLayout_auto_mainmenu.setVisibility(View.GONE);
                    return true;
                }
            }
                break;
            case KeyEvent.KEYCODE_TV_INPUT:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void channetuningActivityLeave() {
        switch (mTvChannelManager.getTuningStatus()) {
            case TvChannelManager.TUNING_STATUS_ATV_AUTO_TUNING:
                mTvChannelManager.pauseAtvAutoTuning();
                break;
            case TvChannelManager.TUNING_STATUS_DTV_AUTO_TUNING:
            case TvChannelManager.TUNING_STATUS_DTV_FULL_TUNING:
                mTvChannelManager.pauseDtvScan();
                break;
            default:
                break;
        }
    }

    private void channetuningActivityExit() {
        finish();
        Log.e(TAG, "channetuningActivityExit11");
       //// Intent intent = new Intent(TvIntent.MAINMENU);
        ////intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
       //// if (intent.resolveActivity(getPackageManager()) != null) {
       ////     startActivity(intent);
       //// }
    }

    private void pauseChannelTuning() {
        switch (mTvChannelManager.getTuningStatus()) {
            case TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING:
                mTvChannelManager.stopAtvAutoTuning();
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                break;
            case TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING:
                mTvChannelManager.stopDtvScan();
                if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                    boolean res = mTvChannelManager.stopAtvAutoTuning();
                    if (res == false) {
                        Log.e(TAG, "atvSetAutoTuningStart Error!!!");
                    }
                } else {
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
                break;
            default:
                break;
        }
    }

    private void updateAtvTuningScanInfo(AtvEventScan extra) {
        String str = new String();
        int percent = extra.percent;
        int frequencyKHz = extra.frequencyKHz;
        int scannedChannelNum = extra.scannedChannelNum;
        int curScannedChannel = extra.curScannedChannel;
        boolean bIsScaningEnable = extra.bIsScaningEnable;

        scanPercent = percent;

        str = "" + scannedChannelNum;
        /////viewholder_channeltune.text_cha_tvprogram_val.setText(str);
        viewholder_channeltune.textView_autochannel_atvchannelnumber.setText(str);

        str = "" + curScannedChannel;
        /////viewholder_channeltune.text_cha_tuningprogress_num.setText(str);
        String sFreq = String.format(" %.2fMHz", (((double)frequencyKHz)/1000));
        str = "" + percent + '%' + sFreq;
        /////viewholder_channeltune.text_cha_tuningprogress_val.setText(str);
        /////viewholder_channeltune.progressbar_cha_tuneprogress.setProgress(percent);
        viewholder_channeltune.textView_autochannel_atvcurrentfrq.setText(sFreq);
        viewholder_channeltune.progressBarCustom_autochannel_atvsearchprogressbar.setProgress(percent);
        viewholder_channeltune.textView_autochannel_atvsearchprogressbarvalue.setText(percent+"%");
        if (frequencyKHz < 140000) {
            viewholder_channeltune.textView_autochannel_atvfrqsegment.setText(getString(R.string.str_cha_tuningprogress_vhf));
        }
        else {
            viewholder_channeltune.textView_autochannel_atvfrqsegment.setText(getString(R.string.str_cha_tuningprogress_uhf));
        }
        if ((percent == 100 && bIsScaningEnable == false) || (percent > 100)
                || (frequencyKHz > (ATV_MAX_FREQ+1000))) {
            mTvChannelManager.stopAtvAutoTuning();

            if (mTvSystem == TvCommonManager.TV_SYSTEM_ISDB
                    && TvIsdbChannelManager.getInstance().getAntennaType() == TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR) {
                TvIsdbChannelManager.getInstance().genMixProgList(false);
                TvIsdbChannelManager.getInstance().setAntennaType(
                        TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE);
                /////viewholder_channeltune.text_cha_tuningprogress_type.setText("CABLE");
                changeLayoutATV();
                mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
            } else {
                if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                    if (dtvServiceCount > 0) {
                        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
                            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_DTV);
                        }
                        mTvChannelManager.changeToFirstService(
                                TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                    } else {
                        if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
                            mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                        }
                        mTvChannelManager.changeToFirstService(
                                TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                                TvChannelManager.FIRST_SERVICE_DEFAULT);
                    }
                } else {
                    if (mTvCommonManager.getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
                        mTvCommonManager.setInputSource(TvCommonManager.INPUT_SOURCE_ATV);
                    }
                    mTvChannelManager.changeToFirstService(
                            TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                            TvChannelManager.FIRST_SERVICE_DEFAULT);
                }
                channetuningActivityExit();
            }
        }

    }

    private void updateDtvTuningScanInfo(DtvEventScan extra) {
        int scan_status = extra.scanStatus;
        int dtv = extra.dtvSrvCount;
        int radio = extra.radioSrvCount;
        int data = extra.dataSrvCount;

        Log.e(TAG, "updateDtvTuningScanInfo(), scan_status =  " + scan_status);

        if (TvChannelManager.DTV_SCAN_STATUS_AUTOTUNING_PROGRESS == scan_status) {
            String str;
            int percent = extra.scanPercentageNum;
            int currRFCh = extra.currRFCh;
            int currFrequency = extra.currFrequency;
            boolean isVHF = extra.isVHF;
            String rfSignalName = extra.rfSignalName;

            int resId;
            scanPercent = percent;
            Log.d("Maxs51","currFrequency = " + currFrequency);
            Log.d("Maxs51","currRFCh = " + currRFCh);
            Log.d("Maxs51","rfSignalName = " + rfSignalName);
            str = "" + (dtv + radio + data);
            ////viewholder_channeltune.text_cha_airdtv_scanned_channels_val.setText(str);

            str = "" + dtv;
            ///// viewholder_channeltune.text_cha_dtvprogram_val.setText(str);
            viewholder_channeltune.textView_autochannel_dtvchannelnumber.setText(str);
            str = "" + radio;
            ///// viewholder_channeltune.text_cha_radioprogram_val.setText(str);
            viewholder_channeltune.textView_autochannel_dtvradionumber.setText(str);
            str = "" + data;
            ///// viewholder_channeltune.text_cha_dataprogram_val.setText(str);
            viewholder_channeltune.textView_autochannel_dtvdatanumber.setText(str);
            str = "" + percent + '%';
            /////viewholder_channeltune.text_cha_tuningprogress_val.setText(str);
            viewholder_channeltune.textView_autochannel_dtvsearchprogressbarvalue.setText(str);

            float mFreq = 0.0f;
            if ((TvChannelManager.TV_ROUTE_DVBS == mCurrentRoute)
                    || (TvChannelManager.TV_ROUTE_DVBS2 == mCurrentRoute)) {
                mFreq = currFrequency ;
            } else if (TvChannelManager.TV_ROUTE_DVBC == mCurrentRoute) {
                mFreq = currFrequency / 1000 ;
            } else {
                /*if (((TvChannelManager.TV_ROUTE_DVBT == mCurrentRoute)
                        || (TvChannelManager.TV_ROUTE_DVBT2 == mCurrentRoute) || (TvChannelManager.TV_ROUTE_DTMB == mCurrentRoute))
                        && (!rfSignalName.isEmpty())) {
                    str = "" + rfSignalName;
                } else {*/
                    mFreq = currFrequency / 1000 ;
                //}
            }
            if(mFreq == 0.0f){
            	mFreq = 52.50f;
            }
            DecimalFormat decimalFormat =new DecimalFormat("0.00");
        	str = "" + decimalFormat.format(mFreq);
            
            Log.d(TAG, "updateDtvTuningScanInfo(), " + str + ", " + percent + "%");

            if (isVHF) {
                resId = R.string.str_cha_tuningprogress_vhf;
            } else {
                resId = R.string.str_cha_tuningprogress_uhf;
            }
            /////viewholder_channeltune.text_cha_tuningprogress_rf.setText(getResources().getString(
            /////resId));
            /////viewholder_channeltune.text_cha_tuningprogress_num.setText(str);
            /////viewholder_channeltune.progressbar_cha_tuneprogress.setProgress(percent);
            viewholder_channeltune.textView_autochannel_dtvcurrentfrq.setText(str+"MHz");
            viewholder_channeltune.progressBarCustom_autochannel_dtvsearchprogressbar.setProgress(percent);

        } else if (scan_status == TvChannelManager.DTV_SCAN_STATUS_END) {
            if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                /////if (View.VISIBLE != viewholder_channeltune.linear_cha_mainlinear.getVisibility()) {
                /////     viewholder_channeltune.linear_cha_mainlinear.setVisibility(View.VISIBLE);
                /////  }

                if (View.VISIBLE != viewholder_channeltune.relativeLayout_auto_mainmenu.getVisibility()) {
                     viewholder_channeltune.relativeLayout_auto_mainmenu.setVisibility(View.VISIBLE);
                  }
                dtvServiceCount = dtv + radio + data;

                ///// viewholder_channeltune.text_cha_tuningprogress_type.setText("ATV");

                viewholder_channeltune.textView_search_title.setText(getString(R.string.atv_search_channel));
                changeLayoutATV();
                mTvChannelManager.startAtvAutoTuning(ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
            } else if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_DTV) {
                if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
                    TvIsdbChannelManager.getInstance().genMixProgList(false);
                }
                mTvChannelManager.changeToFirstService(
                        TvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                        TvChannelManager.FIRST_SERVICE_DEFAULT);
                if (isDtvAutoUpdateScan) {
                    finish();
                } else {
                    channetuningActivityExit();
                }
            }
        }
    }

    private void changeLayoutATV() {
        ////if ((true != Utility.isATSC()) && (true != Utility.isISDB())) {
        ///// viewholder_channeltune.text_cha_tuningprogress_num.setVisibility(View.INVISIBLE);
        ///// viewholder_channeltune.text_cha_tuningprogress_ch.setVisibility(View.INVISIBLE);
        /////   viewholder_channeltune.text_cha_tuningprogress_rf.setVisibility(View.INVISIBLE);
        ////}
        viewholder_channeltune.linearLayout_dtv_autochannel.setVisibility(View.GONE);
        viewholder_channeltune.linearLayout_atv_autochannel.setVisibility(View.VISIBLE);
    }

    private ArrayList<String> getCountryNameList() {
        ArrayList<String> countryNameList = new ArrayList<String>();
        for (DvbCountryInfo countryInfo : mTargetRegionInfo.countryInfos) {
            if (countryInfo != null) {
                String countryCode = new String(countryInfo.countryCode);
                countryNameList.add(countryCode);
            }
        }
        return countryNameList;
    }

    private ArrayList<String> getPrimaryNameList() {
        ArrayList<String> primaryNameList = new ArrayList<String>();
        DvbCountryInfo countryInfo = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex];
        for (DvbPrimaryRegionInfo primaryInfo : countryInfo.primaryRegionInfos) {
            if (primaryInfo != null) {
                primaryNameList.add(primaryInfo.name);
            }
        }
        return primaryNameList;
    }

    private ArrayList<String> getSecondaryNameList() {
        ArrayList<String> secondaryNameList = new ArrayList<String>();
        DvbPrimaryRegionInfo primaryInfo = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex].primaryRegionInfos[mTargetRegionPrimaryIndex];
        for (DvbSecondaryRegionInfo secondaryInfo : primaryInfo.secondaryRegionInfos) {
            if (secondaryInfo != null) {
                secondaryNameList.add(secondaryInfo.regionName);
            }
        }
        return secondaryNameList;
    }

    private ArrayList<String> getTertiaryNameList() {
        ArrayList<String> tertiaryNameList = new ArrayList<String>();
        DvbSecondaryRegionInfo secondaryInfo = mTargetRegionInfo.countryInfos[mTargetRegionCountryIndex].primaryRegionInfos[mTargetRegionPrimaryIndex].secondaryRegionInfos[mTargetRegionSecondaryIndex];
        for (DvbTeritaryRegionInfo tertiaryInfo : secondaryInfo.tertiaryRegionInfos) {
            if (tertiaryInfo != null) {
                tertiaryNameList.add(tertiaryInfo.regionName);
            }
        }
        return tertiaryNameList;
    }

    private void ExitTuningActivityExit(boolean flag) {
        if (flag == true)// stop tuning
        {
            Log.d("Maxs30","ExitTuningActivityExit:mTvChannelManager.getTuningStatus() = " + mTvChannelManager.getTuningStatus());
            switch (mTvChannelManager.getTuningStatus()) {
                case TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING:
                    mTvChannelManager.stopAtvAutoTuning();
                    mTvChannelManager.changeToFirstService(
                            mTvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                            mTvChannelManager.FIRST_SERVICE_DEFAULT);
                    finish();
                    break;
                case TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING:
                    mTvChannelManager.stopDtvScan();
                    if (mTvChannelManager.getUserScanType() == TvChannelManager.TV_SCAN_ALL) {
                        boolean res = mTvChannelManager.startAtvAutoTuning(
                                ATV_EVENTINTERVAL, ATV_MIN_FREQ, ATV_MAX_FREQ);
                        if (res == false) {
                            Log.e("TuningService", "atvSetAutoTuningStart Error!!!");
                        }
                    } else {
                        mTvChannelManager.changeToFirstService(
                                mTvChannelManager.FIRST_SERVICE_INPUT_TYPE_DTV,
                                mTvChannelManager.FIRST_SERVICE_DEFAULT);
                    }
                    break;
                default:
                    break;
            }
        } else
        // resume tuning
        {
            switch (mTvChannelManager.getTuningStatus()) {
                case TvChannelManager.TUNING_STATUS_ATV_SCAN_PAUSING:
                    mTvChannelManager.resumeAtvAutoTuning();
                    break;
                case TvChannelManager.TUNING_STATUS_DTV_SCAN_PAUSING:
                    mTvChannelManager.resumeDtvScan();
                    break;
                default:
                    break;
            }
        }

    }
    //nathan.liao 2014.11.04 add for auto tuning ANR error start
    class setDtvConfigTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            ExitTuningActivityExit(true);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            super.onPostExecute(result);
        }
    }
    //nathan.liao 2014.11.04 add for auto tuning ANR error end
}

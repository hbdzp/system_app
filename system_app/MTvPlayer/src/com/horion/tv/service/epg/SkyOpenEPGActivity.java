package com.horion.tv.service.epg;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.horion.tv.framework.MstarBaseActivity;
import com.horion.tv.framework.ui.TvIntent;
import com.horion.tv.framework.ui.util.EpgThread;
import com.ktc.framework.skycommondefine.SkyBroadcast;
import com.ktc.framework.skycommondefine.SkyworthBroadcastKey;
import com.ktc.framework.ktcsdk.ipc.SkyContext;
///import com.ktc.framework.skysdk.logger.SkyLogger;
///import com.ktc.framework.skysdk.logger.SkyServerLogger.LOGTYPE;
import com.ktc.ui.api.KtcToastView;
import com.horion.tv.R;
import com.horion.tv.define.object.Category;
import com.horion.tv.define.object.Channel;
import com.horion.tv.define.object.Channel.CHANNEL_TYPE;
import com.horion.tv.define.object.Source;
import com.horion.tv.define.object.Source.SOURCE_NAME_ENUM;
import com.horion.tv.define.object.TvTime;
import com.horion.tv.framework.epg.SkyLeftEPGConstant.CATTYPE;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGCurProgItemData;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGListDataBase;
import com.horion.tv.framework.ui.uidata.epg.SkyEPGTVBriefData;
import com.horion.tv.framework.ui.util.UiManager;
import com.horion.tv.logic.ExternalApi;
////import com.horion.tv.logic.utils.EPGLoader;
////import com.horion.tv.logic.utils.ScreenDisplayManager;
import com.horion.tv.service.base.KtcTvController;
import com.horion.tv.service.epg.SkyLeftEPGDataGet.OnEpgDataGetListener;
import com.horion.tv.service.epg.SkyLeftEPGDataGet.SkyLeftEPGDataCategory;
import com.horion.tv.service.epg.SkyLeftEPGDataGet.SkyLeftEPGDataChannel;
import com.horion.tv.service.epg.SkyLeftEPGDataGet.onUpdateEPGFromNowListener;
import com.horion.tv.service.epg.SkyLeftEPGParam.GET_REMAINCHANNEL_MODE;
import com.horion.tv.service.epg.SkyLeftEPGParam.GET_REMAINCHANNEL_PARAM;
import com.horion.tv.service.epg.SkyLeftEPGParam.RESUME_CHANNELLIST_PARAM;
import com.horion.tv.service.epg.SkyLeftEPGParam.SORT_CHANNELLIST_PARAM;
import com.horion.tv.ui.leftepg.EPGMODE;
import com.horion.tv.ui.leftepg.SkyCommenEPG;
import com.horion.tv.ui.leftepg.SkyCommenEPG.OnEPGOnKeyEventListener;
import com.horion.tv.ui.leftepg.SkyEPGData;
import com.horion.tv.ui.leftepg.SkyEPGData.SECONDTITLE_STATE;
import com.horion.tv.ui.leftepg.SkyEPGItem_2;
import com.horion.tv.ui.rightepg.SkyEventData;
import com.horion.tv.ui.rightepg.SkyRightEPGView;
import com.horion.tv.ui.rightepg.SkyRightEPGView.OnRightEPGViewItemOnkeyListener;
import com.horion.tv.ui.util.SkyLeftTitleView;
///import com.horion.tv.utils.SkyTVDebug;
import com.horion.tv.utils.SkyTvAsyncTask;
import com.horion.tv.utils.SkyTvAsyncTask.IAsyncTask;
import com.horion.tv.utils.SkyTvSingleAsyncTask;
///import com.horion.tv.utils.SkyTvSubmitLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkyOpenEPGActivity extends MstarBaseActivity implements OnEPGOnKeyEventListener, OnRightEPGViewItemOnkeyListener, onUpdateEPGFromNowListener {
    private static int[] CATTYPESwitchesValues;
    private static int[] GET_REMAINCHANNEL_MODESwitchesValues;
    private static int[] EPGMODESwitchesValues;
    public static boolean bStartActivity = false;
    private final int FOCUSINDEX_INVALID = -1;
    public final int MSG_CAT_HAS_FOCUS = 1;
    public final int MSG_CAT_KEY_RIGHT = 2;
    private final int SHOWFIRSTEPG = 0;
    private final int SHOWSECONDEPG = 2;
    private final int SHOWSECONDEPG_FIRST = 1;
    private final int SHOWSORTEPG = 4;
    private final boolean TESTCACHE = false;
    private final int UPDATESECONDEPG = 3;
    private SkyEPGListDataBase categoryListdata;
    private SkyEPGTVBriefData chanelEvent;
    private Context context = null;
    private Channel curChannel = null;
    private volatile int curEventIndex = 0;
    private SkyLeftEPGDataChannel currentLeftEPGDataChannel;
    private Channel currentSortChannel = null;
    public volatile Source currentSource = null;
    public volatile Category current_category = null;
    public volatile Channel current_channel = null;
    private int default_index = -1;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TvIntent.ACTION_SIGNAL_LOCK)) {
                Log.d("Maxs55","SkyOpenEPGActivity:ACTION_SIGNAL_LOCK:");
                Intent intent1 = new Intent(TvIntent.ACTION_SOURCEINFO);
                startActivity(intent1);

            }
        }
    };
    private BroadcastReceiver eventHandlerReveiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SkyBroadcast.SKY_BCT_SEND_HOTKEYS)) {
                switch (intent.getIntExtra("specialKey", 0)) {
                    case 3:
                    case 228:
                    case 745:
                    case SkyworthBroadcastKey.SKY_BROADCAST_KEY_OLDER_COOL /*805*/:
                    case SkyworthBroadcastKey.SKY_BROADCAST_KEY_CHILD_COOL /*806*/:
                    case SkyworthBroadcastKey.SKY_BROADCAST_KEY_OLDER_HOME /*809*/:
                    case SkyworthBroadcastKey.SKY_BROADCAST_KEY_CHILD_HOME /*810*/:
                    case SkyworthBroadcastKey.SKY_BROADCAST_KEY_COOL /*813*/:
                        SkyOpenEPGActivity.this.stopOpenEPGActivty();
                        return;
                    default:
                        return;
                }
            } else if (intent.getAction().equals(OpenEPGActivityApi.BROADCAST_TVOPENEPGACTIVITY)) {
                SkyOpenEPGActivity.this.stopOpenEPGActivty();
            }
        }
    };
    private List<SkyEventData> eventItemDatas = null;
    private SkyEPGListDataBase eventListData;
    private int focusedCategoryItemIndexID = -1;
    private int foucusIndex = 0;
    public SkyTvSingleAsyncTask getChannelAsyncTask = new SkyTvSingleAsyncTask() {
        public void done(Object... objArr) throws InterruptedException {
           //// SkyTVDebug.debug("getTimeDataAsyncTask hasdone");
            SkyOpenEPGActivity.this.getChannelAsyncTask.stop();
        }

        public Object[] run(Object... objArr) throws InterruptedException {
           //// SkyTVDebug.debug("getChannelAsyncTask run:" + objArr[0]);
            Channel channel = (Channel) objArr[0];
            SkyDataCache.getInstance().setCurChannel(channel);
            ///SkyEPGApi.getInstance().systemApi.switchChannel(channel);
            return null;
        }
    };
    public SkyTvSingleAsyncTask getEventDataAsyncTask = new SkyTvSingleAsyncTask() {
        public void done(Object... objArr) throws InterruptedException {
            SkyOpenEPGActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (SkyOpenEPGActivity.this.eventItemDatas.size() > 0) {
                        ///SkyTVDebug.debug(">>>>>>>>>>>>showrightView<<<<<<<<<<<<:" + SkyOpenEPGActivity.this.curEventIndex);
                        SkyOpenEPGActivity.this.rightView.showRightEPGView(SkyOpenEPGActivity.this.eventItemDatas, 0);
                        return;
                    }
                    SkyOpenEPGActivity.this.SwitchToScreenShow();
                }
            });
            SkyDataCache.getInstance().setEventItemDataCache(SkyOpenEPGActivity.this.eventItemDatas, 0);
            SkyOpenEPGActivity.this.getEventDataAsyncTask.stop();
        }

        public Object[] run(Object... objArr) throws InterruptedException {
            try {
                SkyDataCache.getInstance().setCurChannel(SkyOpenEPGActivity.this.curChannel);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SkyOpenEPGActivity.this.cleanEventListData();
            try {
                SkyOpenEPGActivity.this.chanelEvent = SkyEPGApi.getInstance().getEPGEventFromNow(SkyOpenEPGActivity.this.curChannel);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                /*for (TvTime tvTime : SkyOpenEPGActivity.this.getEventTimeData(SkyEPGApi.getInstance().epgApi.getTimeEvent(SkyOpenEPGActivity.this.curChannel))) {
                   /// SkyTVDebug.debug(">>>Hour:" + tvTime.getDay());
                    SkyOpenEPGActivity.this.eventListData = SkyEPGApi.getInstance().getEPGEvent(SkyOpenEPGActivity.this.curChannel, tvTime);
                    ///SkyTVDebug.debug(">>>eventListDataSize:" + SkyOpenEPGActivity.this.eventListData.getEventListData().size());
                    for (int i = 0; i < SkyOpenEPGActivity.this.eventListData.getEventListData().size(); i++) {
                        SkyEventData skyEventData = new SkyEventData();
                        if (SkyOpenEPGActivity.this.chanelEvent.current_time.equals(((SkyEPGCurProgItemData) SkyOpenEPGActivity.this.eventListData.getEventListData().get(i)).progTimeStr)) {
                            SkyOpenEPGActivity.this.isValidValueFlag = true;
                        }
                        if (SkyOpenEPGActivity.this.isValidValueFlag) {
                            if (SkyOpenEPGActivity.this.curEventIndex >= 25) {
                                SkyOpenEPGActivity.this.isExistCirleFlag = true;
                                break;
                            }
                          ////  SkyTVDebug.debug(">>>>>:" + ((SkyEPGCurProgItemData) SkyOpenEPGActivity.this.eventListData.getEventListData().get(i)).progTimeStr);
                            skyEventData.setItemTitle(((SkyEPGCurProgItemData) SkyOpenEPGActivity.this.eventListData.getEventListData().get(i)).progNameStr);
                            skyEventData.setItemSecondTitleTitle(((SkyEPGCurProgItemData) SkyOpenEPGActivity.this.eventListData.getEventListData().get(i)).progTimeStr);
                            SkyOpenEPGActivity.this.eventItemDatas.add(skyEventData);
                            SkyOpenEPGActivity skyOpenEPGActivity = SkyOpenEPGActivity.this;
                            skyOpenEPGActivity.curEventIndex = skyOpenEPGActivity.curEventIndex + 1;
                        }
                    }
                    if (SkyOpenEPGActivity.this.isExistCirleFlag) {
                      ////  SkyTVDebug.debug(">>isExistCirleFlag");
                        break;
                    }
                }*/
            } catch (Exception e22) {
                e22.printStackTrace();
            }
            return null;
        }
    };

    private boolean isCurrent = false;
    private volatile boolean isExistCirleFlag = false;
    private boolean isOnSecondEPGItemOnKeyLeft = false;
    private boolean isOpenFist = true;
    private volatile boolean isValidValueFlag = false;
    public Long lastSwitchSourceToTime = Long.valueOf(-1);
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case SHOWFIRSTEPG:
                    ///SkyTVDebug.debug("SHOWFIRSTEPG");
                    Log.d("Maxs25","---->mHandler:SHOWFIRSTEPG");
                    SkyOpenEPGActivity.this.mOpenEPGView.showEPG(SkyOpenEPGActivity.this.uiCategoryItem);
                    return;
                case SHOWSECONDEPG_FIRST:
                    Log.d("Maxs25","---->mHandler:SHOWSECONDEPG_FIRST");
                    SkyOpenEPGActivity.this.foucusIndex = message.arg1;
                   //// SkyTVDebug.debug("SHOWSECONDEPG_FIRST foucusIndex:" + SkyOpenEPGActivity.this.foucusIndex);
                    Log.d("Maxs24","SHOWSECONDEPG_FIRST foucusIndex:" + SkyOpenEPGActivity.this.foucusIndex);
                    Log.d("Maxs24","----->uiChannelItemList.size() = " + uiChannelItemList.size());
                    ///foucusIndex = 2;
                    isCurrent = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getCurrent();
                    Log.d("Maxs110","--->SHOWSECONDEPG_FIRST:isCurrent = " + isCurrent);
                    SkyOpenEPGActivity.this.mOpenEPGView.showSecondEPG(EPGMODE.EPGMODE_DIS, SkyOpenEPGActivity.this.uiChannelItemList, SkyOpenEPGActivity.this.foucusIndex, SkyOpenEPGActivity.this.isCurrent);
                    return;
                case SHOWSECONDEPG:
                    Log.d("Maxs25","---->mHandler:SHOWSECONDEPG");
                    SkyOpenEPGActivity.this.foucusIndex = message.arg1;
                    SkyOpenEPGActivity.this.foucusIndex = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getDefault_index();
                    SkyOpenEPGActivity.this.needUseChannellistOrigin = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getNeedUseChannellistOrigin();
                    SkyOpenEPGActivity.this.isCurrent = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getCurrent();
                    SkyOpenEPGActivity.this.uiChannelItemList = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getUiChannelItemList();
                    ////SkyTVDebug.debug("SHOWSECONDEPG foucusIndex:" + SkyOpenEPGActivity.this.foucusIndex + "  needUseChannellistOrigin:" + SkyOpenEPGActivity.this.needUseChannellistOrigin + "   focusedCategoryItemIndexID:" + SkyOpenEPGActivity.this.focusedCategoryItemIndexID);
                    Log.d("Maxs110","--->SHOWSECONDEPG:isCurrent = " + isCurrent);
                    if (SkyOpenEPGActivity.this.needUseChannellistOrigin == SkyOpenEPGActivity.this.focusedCategoryItemIndexID) {
                        SkyOpenEPGActivity.this.mOpenEPGView.showSecondEPG(EPGMODE.EPGMODE_DIS, SkyOpenEPGActivity.this.uiChannelItemList, SkyOpenEPGActivity.this.foucusIndex, SkyOpenEPGActivity.this.isCurrent);
                        return;
                    }
                    return;
                case UPDATESECONDEPG:

                    SkyOpenEPGActivity.this.foucusIndex = message.arg1;
                    SkyOpenEPGActivity.this.foucusIndex = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getDefault_index();
                    SkyOpenEPGActivity.this.needUseChannellistOrigin = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getNeedUseChannellistOrigin();
                    SkyOpenEPGActivity.this.isCurrent = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getCurrent();
                    SkyOpenEPGActivity.this.uiChannelItemList = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getUiChannelItemList();
                    Log.d("Maxs25","UPDATESECONDEPG foucusIndex:" + SkyOpenEPGActivity.this.foucusIndex + "  needUseChannellistOrigin:" + SkyOpenEPGActivity.this.needUseChannellistOrigin + "   focusedCategoryItemIndexID:" + SkyOpenEPGActivity.this.focusedCategoryItemIndexID);
                   /// SkyTVDebug.debug("UPDATESECONDEPG foucusIndex:" + SkyOpenEPGActivity.this.foucusIndex + "  needUseChannellistOrigin:" + SkyOpenEPGActivity.this.needUseChannellistOrigin + "   focusedCategoryItemIndexID:" + SkyOpenEPGActivity.this.focusedCategoryItemIndexID);
                    if (SkyOpenEPGActivity.this.needUseChannellistOrigin == SkyOpenEPGActivity.this.focusedCategoryItemIndexID) {
                       //// SkyTVDebug.debug("-------------------------------- UPDATESECONDEPG: " + SkyOpenEPGActivity.this.uiChannelItemList.size());
                        SkyOpenEPGActivity.this.mOpenEPGView.upDataSecondEPG(SkyOpenEPGActivity.this.uiChannelItemList, SkyOpenEPGActivity.this.foucusIndex, SkyOpenEPGActivity.this.isCurrent);
                        return;
                    }
                    return;
                case SHOWSORTEPG:
                    if (SkyOpenEPGActivity.this.currentLeftEPGDataChannel != null) {
                        SkyOpenEPGActivity.this.titleView.showLeftTitleView(SkyOpenEPGActivity.this.context.getResources().getString(R.string.KTC_CFG_TV_CHANNEL_SORT));
                        SkyOpenEPGActivity.this.foucusIndex = SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getDefault_index();
                        Log.d("Maxs28","SHOWSORTEPG foucusIndex:" + SkyOpenEPGActivity.this.foucusIndex);
                       //// SkyTVDebug.debug("SHOWSORTEPG foucusIndex:" + SkyOpenEPGActivity.this.foucusIndex);
                        ///foucusIndex = 1;
                        SkyOpenEPGActivity.this.mOpenEPGView.showSecondEPG(EPGMODE.EPGMODE_SORT, SkyOpenEPGActivity.this.uiChannelItemList, SkyOpenEPGActivity.this.foucusIndex, SkyOpenEPGActivity.this.isCurrent);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private SkyCommenEPG mOpenEPGView = null;
    private volatile int mTimeIndex = 0;
    private final boolean needExit_OnSecondEPGItemOnKeyBack = true;
    private int needUseChannellistOrigin = -1;
    private Channel preChannel = null;
    private SkyRightEPGView rightView = null;
    private FrameLayout root = null;
    private LayoutParams rootLayout = null;
    private int secondSelectItemID = 0;
    public Byte switchChannelLock = Byte.valueOf((byte) 0);
    private SkyLeftTitleView titleView = null;
    private KtcToastView toastView = null;
    private List<SkyEPGData> uiCategoryItem = new ArrayList();
    private List<SkyEPGData> uiChannelItemList = new ArrayList();
    private String viewType = null;

    private static  int[] getCATTYPESwitchesValues() {
        if (CATTYPESwitchesValues != null) {
            return CATTYPESwitchesValues;
        }
        int[] iArr = new int[CATTYPE.values().length];
        try {
            iArr[CATTYPE.CAT_ALL.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[CATTYPE.CAT_FIRST.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        CATTYPESwitchesValues = iArr;
        return iArr;
    }

    private static  int[] getGET_REMAINCHANNEL_MODESwitchesValues() {
        if (GET_REMAINCHANNEL_MODESwitchesValues != null) {
            return GET_REMAINCHANNEL_MODESwitchesValues;
        }
        int[] iArr = new int[GET_REMAINCHANNEL_MODE.values().length];
        try {
            iArr[GET_REMAINCHANNEL_MODE.KEYDOWN.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[GET_REMAINCHANNEL_MODE.KEYRIGHT.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[GET_REMAINCHANNEL_MODE.KEYUP.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        GET_REMAINCHANNEL_MODESwitchesValues = iArr;
        return iArr;
    }

    private static int[] getEPGMODESwitchesValues() {
        if (EPGMODESwitchesValues != null) {
            return EPGMODESwitchesValues;
        }
        int[] iArr = new int[EPGMODE.values().length];
        try {
            iArr[EPGMODE.EPGMODE_DELETED.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[EPGMODE.EPGMODE_DIS.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[EPGMODE.EPGMODE_SORT.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        EPGMODESwitchesValues = iArr;
        return iArr;
    }

    private void SwitchToScreenShow() {
        ////SkyTVDebug.debug(">>>>SwitchToScreenShow<<<<:" + ScreenDisplayManager.getInstance().getScreenDisplayShowFlag());
        finish();
        ////ScreenDisplayManager.getInstance().showScreenDisplay(this.currentSource, SkyEPGApi.getInstance().getCurrentChannel(), false, true, false, true);
    }

    private boolean checkNeedUpdateSecondEpg(GET_REMAINCHANNEL_PARAM get_remainchannel_param) {
        boolean z = true;
        int i = 8;
        if (this.uiChannelItemList != null && this.uiChannelItemList.size() <= 6) {
            return false;
        }
        switch (getGET_REMAINCHANNEL_MODESwitchesValues()[get_remainchannel_param.mode.ordinal()]) {
            case 1:
                z = true;
                i = 2;
                break;
            case 2:
                z = true;
                break;
            case 3:
                break;
            default:
                i = 0;
                z = false;
                break;
        }
        ///for (boolean z2 = 0 - i; z2 <= z; z2++) {
        ///    SkyTVDebug.debug("index:" + z2);
       ///     i = get_remainchannel_param.currentIndex + z2;
        ///    i = i < 0 ? i + this.uiChannelItemList.size() : i % this.uiChannelItemList.size();
        ///    if (i < this.uiChannelItemList.size() && i >= 0 && ((SkyEPGData) this.uiChannelItemList.get(i)).getItemSecondTitleState() == SECONDTITLE_STATE.NOTTRY) {
        ///        return true;
        ///    }
       /// }

        for (int z2 = 0; z2 <= i; z2++) {
           //// SkyTVDebug.debug("index:" + z2);
            i = get_remainchannel_param.currentIndex + z2;
            i = i < 0 ? i + this.uiChannelItemList.size() : i % this.uiChannelItemList.size();
            if (i < this.uiChannelItemList.size() && i >= 0 && ((SkyEPGData) this.uiChannelItemList.get(i)).getItemSecondTitleState() == SECONDTITLE_STATE.NOTTRY) {
                return true;
            }
        }
        return false;
    }

    private void eventHandler() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SkyBroadcast.SKY_BCT_SEND_HOTKEYS);
        intentFilter.addAction(OpenEPGActivityApi.BROADCAST_TVOPENEPGACTIVITY);
        registerReceiver(this.eventHandlerReveiver, intentFilter);
    }

    private List<TvTime> getEventTimeData(List<TvTime> list) {
        List<TvTime> arrayList = new ArrayList();
        if (list.size() != 0) {
            if (arrayList.size() != 0) {
                arrayList.clear();
            }
            int i = 0;
            while (i < list.size()) {
                ///SkyTVDebug.debug("timeList:" + ((TvTime) list.get(i)).getYear() + " || " + ((TvTime) list.get(i)).getMonth() + " || " + ((TvTime) list.get(i)).getDay());
                if (((TvTime) list.get(i)).getYear() > new TvTime().getYear()) {
                 ///   SkyTVDebug.debug("NEXT YEAR" + i);
                    arrayList.add((TvTime) list.get(i));
                } else if (((TvTime) list.get(i)).getYear() == new TvTime().getYear() && ((TvTime) list.get(i)).getMonth() > new TvTime().getMonth()) {
                  ///  SkyTVDebug.debug("NEXT MONTH" + i);
                    arrayList.add((TvTime) list.get(i));
                } else if (((TvTime) list.get(i)).getYear() == new TvTime().getYear() && ((TvTime) list.get(i)).getMonth() == new TvTime().getMonth() && ((TvTime) list.get(i)).getDay() >= new TvTime().getDay()) {
                  ///  SkyTVDebug.debug("NEXT DAY" + i);
                    arrayList.add((TvTime) list.get(i));
                }
                i++;
            }
        }
        return arrayList;
    }

    private void getLeftEpgDataOnStart(CATTYPE cattype) {
        SkyLeftEPGDataGet.getInstance().clearEventType();
        SkyLeftEPGDataCategory leftEPGDataCat0yFirstSync = SkyLeftEPGDataGet.getInstance().getLeftEPGDataCat0yFirstSync();
        if (leftEPGDataCat0yFirstSync != null) {
            this.uiCategoryItem = leftEPGDataCat0yFirstSync.getUiCategoryItem();
            this.categoryListdata = leftEPGDataCat0yFirstSync.getCategoryListdata();
            switch (getCATTYPESwitchesValues()[cattype.ordinal()]) {
                case 1:
                    this.currentLeftEPGDataChannel = SkyLeftEPGDataGet.getInstance().getLeftEPGDataCatallChannelSync();
                    break;
                case 2:
                    this.currentLeftEPGDataChannel = SkyLeftEPGDataGet.getInstance().getLeftEPGDataChannelOfCatfirstSync();
                    break;
            }
            if (this.currentLeftEPGDataChannel != null) {
                this.uiChannelItemList = this.currentLeftEPGDataChannel.getUiChannelItemList();
                this.default_index = this.currentLeftEPGDataChannel.getDefault_index();
                this.isCurrent = this.currentLeftEPGDataChannel.getCurrent();
                this.needUseChannellistOrigin = this.currentLeftEPGDataChannel.getNeedUseChannellistOrigin();
                try {
                    Log.d("Maxs24","uiCategoryItem:" + this.uiCategoryItem.size());
                    Log.d("Maxs24","uiChannelItemList:" + this.uiChannelItemList.size());
                    Log.d("Maxs24","default_index:" + this.default_index);
                    Log.d("Maxs24","isCurrent:" + this.isCurrent);
                    Log.d("Maxs24","categoryListdata:" + this.categoryListdata);
                   /// SkyTVDebug.debug("uiCategoryItem:" + this.uiCategoryItem.size());
                  ///  SkyTVDebug.debug("uiChannelItemList:" + this.uiChannelItemList.size());
                  ///  SkyTVDebug.debug("default_index:" + this.default_index);
                   /// SkyTVDebug.debug("isCurrent:" + this.isCurrent);
                  ///  SkyTVDebug.debug("categoryListdata:" + this.categoryListdata);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean getRemainingChannelData(GET_REMAINCHANNEL_PARAM get_remainchannel_param) {
        int i = 2;
        int i2 = 8;
        switch (getGET_REMAINCHANNEL_MODESwitchesValues()[get_remainchannel_param.mode.ordinal()]) {
            case 1:
                i = 8;
                i2 = 2;
                break;
            case 2:
                i = 8;
                break;
            case 3:
                break;
            default:
                i2 = 0;
                i = 0;
                break;
        }
        boolean z = false;
        for (int i3 = 0 - i2; i3 <= i; i3++) {
            ///boolean z2;
            boolean z2 = false;
            ///SkyTVDebug.debug("index:" + i3);
            i2 = get_remainchannel_param.currentIndex + i3;
            int size = i2 < 0 ? i2 + this.uiChannelItemList.size() : i2 % this.uiChannelItemList.size();
            if (size < this.uiChannelItemList.size()) {
                if (size < 0) {
                    z2 = z;
                } else if (((SkyEPGData) this.uiChannelItemList.get(size)).getItemSecondTitleState() == SECONDTITLE_STATE.NOTTRY) {
                    SkyEPGTVBriefData ePGEventFromNow = SkyEPGApi.getInstance().getEPGEventFromNow((Channel) ((SkyEPGData) this.uiChannelItemList.get(size)).getData());
                    if (ePGEventFromNow == null) {
                       /// SkyTVDebug.debug("EventInfo is null");
                        ((SkyEPGData) this.uiChannelItemList.get(size)).setItemSecondTitleState(SECONDTITLE_STATE.TRYERROR);
                        z2 = z;
                    } else {
                        ((SkyEPGData) this.uiChannelItemList.get(size)).setItemSecondTitleState(SECONDTITLE_STATE.TRYSUCCESS);
                        ((SkyEPGData) this.uiChannelItemList.get(size)).setItemSecondTitleTitle(ePGEventFromNow.current_time + " " + ePGEventFromNow.current_info);
                        z2 = true;
                    }
                }
                z = z2;
            }
            z2 = z;
            z = z2;
        }
        return z;
    }

    private void startMonitorThread(int i) {
       /// SkyLogger.d("lwr", ">>startMonitorThread");
        this.mTimeIndex = i;
        SkyThreadPool.shutDownScheduledThreadPool();
        SkyThreadPool.addScheduledTask(new Runnable() {
            public void run() {
                ///SkyLogger.d("lwr", ">>ThreadPool_Run:" + SkyOpenEPGActivity.this.mTimeIndex);
                if (SkyOpenEPGActivity.this.mTimeIndex > 5) {
                    SkyThreadPool.shutDownScheduledThreadPool();
                    SkyOpenEPGActivity.this.stopOpenEPGActivty();
                }
                SkyOpenEPGActivity skyOpenEPGActivity = SkyOpenEPGActivity.this;
                skyOpenEPGActivity.mTimeIndex = skyOpenEPGActivity.mTimeIndex + 1;
            }
        }, 1000);
    }

    private void stopOpenEPGActivty() {
       /// SkyLogger.d("lwr", "stopOpenEPGActivty");
        finish();
    }

    public void cleanCategoryListData() {
       /// SkyTVDebug.debug("cleanCategoryListData");
        this.uiCategoryItem.clear();
    }

    public void cleanChannelListData() {
        ///SkyTVDebug.debug("cleanChannelListData");
        this.default_index = -1;
        this.uiChannelItemList.clear();
    }

    public void cleanEventListData() {
        ///SkyTVDebug.debug("cleanEventListData");
        this.curEventIndex = 0;
        this.isValidValueFlag = false;
        try {
            if (this.eventItemDatas.size() > 0) {
                this.eventItemDatas.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       /// SkyTVDebug.debug("eventItemDatas:::" + this.eventItemDatas.size());
    }

    public void onCallBack(List<SkyEPGData> list) {
        if (this.currentLeftEPGDataChannel != null && this.currentLeftEPGDataChannel.getNeedUseChannellistOrigin() != -1 && this.focusedCategoryItemIndexID != -1 && this.currentLeftEPGDataChannel.getNeedUseChannellistOrigin() == this.focusedCategoryItemIndexID && list != null && !SkyLeftEPGDataGet.getInstance().needStopGetEventData && SkyLeftEPGDataGet.getInstance().isEpgSource(this.currentSource)) {
          ///  SkyTVDebug.debug("onCallBack: " + list.size());
            this.currentLeftEPGDataChannel.setUiChannelItemList(list);
          ///  SkyTVDebug.debug("onEpgSecondDataUpdated focusedCategoryItemIndexID:" + this.focusedCategoryItemIndexID + "  currentLeftEPGDataChannel.getNeedUseChannellistOrigin():" + this.currentLeftEPGDataChannel.getNeedUseChannellistOrigin());
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = UPDATESECONDEPG;
            obtainMessage.obj = this.currentLeftEPGDataChannel;
            this.mHandler.sendMessage(obtainMessage);
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
       /// SkyTVDebug.debug("SkyOpenEPGActivity_onCreate");
        UiManager.initDiv();

        this.context = this;
        this.isOpenFist = true;
        this.isOnSecondEPGItemOnKeyLeft = false;
        SkyLeftEPGDataGet.getInstance().needStopGetEventData = false;
        this.currentSource = KtcTvController.getInstance().getCurrentSource();
       //// SkyTVDebug.debug("currentSource" + this.currentSource.displayName + "|| " + this.currentSource.name);
        Intent intent = getIntent();
        this.viewType = intent.getStringExtra("viewtype");
        if (viewType == null) {
            this.viewType = "leftview";
        }
          ///  SkyTVDebug.debug("viewType:" + this.viewType);
        Log.d("Maxs28","SkyOpenEPGActivity:viewType = " + viewType);
        if (this.root == null) {
            this.root = new FrameLayout(this);
        }
        this.root.removeAllViews();
        this.rootLayout = new LayoutParams(-1, -1);
        if ("leftview".equals(this.viewType)) {
            this.mOpenEPGView = new SkyCommenEPG((Context) this, EPGMODE.EPGMODE_DIS);
            this.mOpenEPGView.setOnEPGOnKeyEventListener(this);
            this.mOpenEPGView.setVisibility(View.INVISIBLE);
            ///this.mOpenEPGView.setVisibility(View.VISIBLE);
            this.root.addView(this.mOpenEPGView);
        } else if ("rightview".equals(this.viewType)) {
            this.eventItemDatas = new ArrayList();
            this.rightView = new SkyRightEPGView(this);
            this.rightView.setRightEPGViewItemOnkeyListener(this);
            this.root.addView(this.rightView);
        } else if ("channelEditSort".equals(this.viewType)) {
            this.mOpenEPGView = new SkyCommenEPG((Context) this, EPGMODE.EPGMODE_SORT);
            this.mOpenEPGView.setOnEPGOnKeyEventListener(this);
            this.mOpenEPGView.setVisibility(View.INVISIBLE);
            this.root.addView(this.mOpenEPGView);
            this.titleView = SkyLeftTitleView.getInstance(this);
            this.titleView.getRootView().setVisibility(View.INVISIBLE);
            this.toastView = new KtcToastView(this);
            this.root.addView(this.titleView.getRootView());
        } else if ("channelEditDeleted".equals(this.viewType)) {
            this.mOpenEPGView = new SkyCommenEPG((Context) this, EPGMODE.EPGMODE_DELETED);
            this.mOpenEPGView.setOnEPGOnKeyEventListener(this);
            this.mOpenEPGView.setVisibility(View.INVISIBLE);
            this.root.addView(this.mOpenEPGView);
            this.titleView = SkyLeftTitleView.getInstance(this);
            this.titleView.getRootView().setVisibility(View.INVISIBLE);
            this.toastView = new KtcToastView(this);
            this.root.addView(this.titleView.getRootView());
        }
        setContentView(this.root, this.rootLayout);
        SkyLeftEPGDataGet.getInstance().setOnEpgDataGetListener(new OnEpgDataGetListener() {
            public void onEpgCatallDataGet(boolean z) {
               /// SkyTVDebug.debug(">>>>>>>>>>>>onEpgCatallDataGet<<<<<<<<<<<<");
                Log.d("Maxs24",">>>>>>>>>>>>onEpgCatallDataGet<<<<<<<<<<<<");
                SkyOpenEPGActivity.this.getLeftEpgDataOnStart(CATTYPE.CAT_ALL);
                if (SkyOpenEPGActivity.this.uiChannelItemList == null || SkyOpenEPGActivity.this.uiChannelItemList.size() == 0) {
                    SkyOpenEPGActivity.this.finish();
                    return;
                }
                Message obtainMessage = SkyOpenEPGActivity.this.mHandler.obtainMessage();
                obtainMessage.what = SHOWSORTEPG;
                SkyOpenEPGActivity.this.mHandler.sendMessage(obtainMessage);
            }

            public void onEpgCatfirstDataGet() {
                SkyOpenEPGActivity.this.getLeftEpgDataOnStart(CATTYPE.CAT_FIRST);
                ///SkyTVDebug.debug(">>>>>>>>>>>>onEpgCatfirstDataGet<<<<<<<<<<<<");
                Message obtainMessage = SkyOpenEPGActivity.this.mHandler.obtainMessage();
                obtainMessage.what = SHOWFIRSTEPG;
                SkyOpenEPGActivity.this.mHandler.sendMessage(obtainMessage);
            }

            public void onEpgSecondDataGetted(SkyLeftEPGDataChannel skyLeftEPGDataChannel) {
                SkyOpenEPGActivity.this.currentLeftEPGDataChannel = skyLeftEPGDataChannel;
                Message obtainMessage = SkyOpenEPGActivity.this.mHandler.obtainMessage();
                obtainMessage.what = SHOWSECONDEPG;
                obtainMessage.obj = SkyOpenEPGActivity.this.currentLeftEPGDataChannel;
                SkyOpenEPGActivity.this.mHandler.sendMessage(obtainMessage);
            }

            public void onEpgSecondDataUpdated() {
               /// SkyTVDebug.debug("onEpgSecondDataUpdated focusedCategoryItemIndexID:" + SkyOpenEPGActivity.this.focusedCategoryItemIndexID + "  currentLeftEPGDataChannel.getNeedUseChannellistOrigin():" + SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getNeedUseChannellistOrigin());
                if (SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getNeedUseChannellistOrigin() == SkyOpenEPGActivity.this.focusedCategoryItemIndexID) {
                   /// SkyTVDebug.debug("onEpgSecondDataUpdated focusedCategoryItemIndexID:" + SkyOpenEPGActivity.this.focusedCategoryItemIndexID + "  currentLeftEPGDataChannel.getNeedUseChannellistOrigin():" + SkyOpenEPGActivity.this.currentLeftEPGDataChannel.getNeedUseChannellistOrigin());
                    Message obtainMessage = SkyOpenEPGActivity.this.mHandler.obtainMessage();
                    obtainMessage.what = UPDATESECONDEPG;
                    obtainMessage.obj = SkyOpenEPGActivity.this.currentLeftEPGDataChannel;
                    SkyOpenEPGActivity.this.mHandler.sendMessage(obtainMessage);
                }
            }
        });
        EpgThread.getInstance();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("Maxs25","SkyOpenEPGActivity:onKeyDown:keyCode = " + keyCode);
        return super.onKeyDown(keyCode, event);
    }

    protected void onDestroy() {
        super.onDestroy();

        Log.d("Maxs28","SkyOpenEPGActivity:onDestroy");
        ///SkyTVDebug.debug("onDestroy");
        SkyLeftEPGDataGet.getInstance().needStopGetEventData = true;
        bStartActivity = false;
        try {
            this.root.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"leftview".equals(this.viewType)) {
            if ("rightview".equals(this.viewType)) {
                cleanEventListData();
                try {
                    if (this.eventListData != null) {
                        this.eventListData.getEventListData().clear();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else if ("channelEditSort".equals(this.viewType)) {
                Channel currentChannel = SkyEPGApi.getInstance().getCurrentChannel();
                if (!(currentChannel == null || this.currentSortChannel == null || currentChannel.equals(this.currentSortChannel))) {
                    SkyLeftEPGDataGet.getInstance().updateDefaultIndexAndIsCurrent(currentChannel);
                }
                this.currentSortChannel = null;
            }
        }
        unregisterReceiver(this.eventHandlerReveiver);
        finish();
    }

    public void onFirstEPGItemFocusChangeListener(int i, View view, SkyEPGData skyEPGData, boolean z) {
        Log.d("Maxs24",">>>>onFirstEPGItemFocusChangeListener:" + skyEPGData.getItemTitle() + "  getItemIndexID:" + skyEPGData.getItemIndexID() + "  isOnSecondEPGItemOnKeyLeft:" + this.isOnSecondEPGItemOnKeyLeft + "z = " + z);
        if (z) {
          ///  SkyTVDebug.debug(">>>>onFirstEPGItemFocusChangeListener:" + skyEPGData.getItemTitle() + "  getItemIndexID:" + skyEPGData.getItemIndexID() + "  isOnSecondEPGItemOnKeyLeft:" + this.isOnSecondEPGItemOnKeyLeft);
            this.focusedCategoryItemIndexID = skyEPGData.getItemIndexID();
            if (this.isOnSecondEPGItemOnKeyLeft) {
                this.isOnSecondEPGItemOnKeyLeft = false;
                return;
            }
            SkyCommenEPG.isRefreshSencond = true;
            if (this.isOpenFist) {
                this.isOpenFist = false;
                Message obtainMessage = this.mHandler.obtainMessage();
                obtainMessage.what = SHOWSECONDEPG_FIRST;
                obtainMessage.arg1 = this.default_index;
                this.mHandler.sendMessage(obtainMessage);
                return;
            }
            SkyLeftEPGDataGet.getInstance().getChannelDataByCategory(skyEPGData.getItemIndexID());
        }
    }

    public boolean onFirstEPGItemOnClick(int i, View view, SkyEPGData skyEPGData) {
        ///SkyTVDebug.debug("onFirstEPGItemOnClick");
        try {
            Map hashMap = new HashMap();
            hashMap.put("source", this.currentSource.toString());
            hashMap.put("cur_model", "leftEpg");
            hashMap.put("category_name", skyEPGData.getItemTitle());
            ////SkyTvSubmitLog.submitAppLog(this.context, null, LOGTYPE.Action, "SkyTv", 3, hashMap, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean onFirstEPGItemOnKeyBack(int i, View view) {
        ///SkyTVDebug.debug("onFirstEPGItemOnKeyBack, secondSelectItemID:" + this.secondSelectItemID);
        this.isOnSecondEPGItemOnKeyLeft = true;
        this.mOpenEPGView.dismissMenuBySecond(this.secondSelectItemID);
        return false;
    }

    public boolean onFirstEPGItemOnKeyDown(int i, View view) {
        return false;
    }

    public boolean onFirstEPGItemOnKeyLeft(int i, View view) {
        ///SkyTVDebug.debug("onFirstEPGItemOnKeyLeft");
        return false;
    }

    public boolean onFirstEPGItemOnKeyRight(int i, View view, SkyEPGData skyEPGData) {
       /// SkyTVDebug.debug(">>>onFirstEPGItemOnKeyRight:");
        if (this.uiChannelItemList != null && this.uiChannelItemList.size() > 6) {
            SkyLeftEPGDataGet.getInstance().getChannelDataRemanin(new GET_REMAINCHANNEL_PARAM(GET_REMAINCHANNEL_MODE.KEYRIGHT, this.foucusIndex, this.uiChannelItemList, EPGMODE.EPGMODE_DIS));
        }
        return false;
    }

    public boolean onFirstEPGItemOnKeyUp(int i, View view) {
        return false;
    }

    protected void onPause() {
        if ("leftview".equals(this.viewType)) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        } else if ("rightview".equals(this.viewType)) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        }
        unregisterReceiver(mReceiver);
        super.onPause();
        finish();
    }

    @Override
    protected void onRestart() {
        Log.d("Maxs28","SkyOpenEPGActivity:onRestart");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.d("Maxs28","SkyOpenEPGActivity:onResume");
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TvIntent.ACTION_SOURCE_INFO_DISAPPEAR);
        filter.addAction(TvIntent.ACTION_SIGNAL_LOCK);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onStop() {
        Log.d("Maxs28","SkyOpenEPGActivity:onStop");
        super.onStop();
    }

    public void onRecallbackHideRightEPGView() {
        stopOpenEPGActivty();
    }

    public void onResetRightEPGViewMonitorTimer() {
        startMonitorThread(0);
    }

    public boolean onRightEPGViewItemOnKeySlide(int i, View view) {
        ///SkyTVDebug.debug("onRightEPGViewItemOnKeySlide:" + i);
        try {
            Map hashMap = new HashMap();
            hashMap.put("source", this.currentSource.toString());
            hashMap.put("cur_model", "rightEpg");
            hashMap.put("channel_name", this.curChannel.displayName);
           /// SkyTvSubmitLog.submitAppLog(this.context, null, LOGTYPE.Action, "SkyTv", 3, hashMap, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean onSecondEPGItemOnClick(EPGMODE epgmode, int itemIndex, View view, SkyEPGData skyEPGData, int sortTo) {
        Log.d("Maxs26","onSecondEPGItemOnClick epgMode" + epgmode + "  getItemTitle:" + skyEPGData.getItemTitle() + "  itemIndex:" + itemIndex + "  sortTo:" + sortTo);
       // SkyTVDebug.debug("onSecondEPGItemOnClick epgMode" + epgmode + "  getItemTitle:" + skyEPGData.getItemTitle() + "  itemIndex:" + itemIndex + "  sortTo:" + sortTo);
        Channel channel;
        Log.d("Maxs26","---<onSecondEPGItemOnClick" + getEPGMODESwitchesValues()[epgmode.ordinal()]);
        switch (getEPGMODESwitchesValues()[epgmode.ordinal()]) {
            case 1:
                channel = (Channel) skyEPGData.getData();
                Log.d("Maxs31","EPGMODE_DELETED:skyEPGData.getItemIndexID() = " + skyEPGData.getItemIndexID());
                Log.d("Maxs31","EPGMODE_DELETED:skyEPGData.getServiceType() = " + skyEPGData.getServiceType());
                Log.d("Maxs31","EPGMODE_DELETED:skyEPGData.getChannelNumber() = " + skyEPGData.getChannelNumber());
                Log.d("Maxs31","EPGMODE_DELETED:skyEPGData.getItemTitle() = " + skyEPGData.getItemTitle());
                SkyEPGApi.getInstance().restoreDeleteChannel(skyEPGData);
                SkyLeftEPGDataGet.getInstance().getDeleteChannelBySQ();
                Log.d("Maxs203","SkyLeftEPGDataGet.getInstance().deleteEPGDataList.size() = " + SkyLeftEPGDataGet.getInstance().deleteEPGDataList.size());
                if (SkyLeftEPGDataGet.getInstance().deleteEPGDataList.size()<= 0){
                    finish();
                }
                mOpenEPGView.showSecondEPG(EPGMODE.EPGMODE_DELETED, SkyLeftEPGDataGet.getInstance().deleteEPGDataList, 0, false);
                /*List<Channel> dBTableSortList = ExternalApi.getInstance().epgApi.getDBTableSortList(this.currentSource);
                List arrayList = new ArrayList();
                List arrayList2 = new ArrayList();
                if (dBTableSortList == null || dBTableSortList.size() == 0) {
                    arrayList.add(channel);
                    arrayList2.add(Integer.valueOf(1));
                } else {
                    Channel channel2 = null;
                    for (Channel channel3 : dBTableSortList) {
                        if (channel3.type == channel.type) {
                            if (channel2 == null) {
                                channel2 = channel3;
                            } else if (channel2.mapindex < channel3.mapindex) {
                                channel2 = channel3;
                            }
                        }
                    }
                    if (channel2 != null) {
                        arrayList.add(channel);
                        arrayList2.add(Integer.valueOf(channel2.mapindex + 1));
                    } else {
                        arrayList.add(channel);
                        arrayList2.add(Integer.valueOf(1));
                    }
                }
                ExternalApi.getInstance().epgApi.resumeChannelListCache(arrayList, arrayList2, this.currentSource);
                SkyLeftEPGDataGet.getInstance().resumeChannelList(new RESUME_CHANNELLIST_PARAM(arrayList, arrayList2, KtcTvController.getInstance().getCurrentSource()));
                SkyLeftEPGDataGet.getInstance().setCatallDataGetted(false);
                SkyLeftEPGDataGet.getInstance().clear();
                ////EPGLoader.getInstance().clear(SkyTvController.getInstance().getCurrentSource());
                List deleteChannelBySQ = SkyLeftEPGDataGet.getInstance().getDeleteChannelBySQ();
                if (deleteChannelBySQ != null && deleteChannelBySQ.size() != 0) {
                    this.mOpenEPGView.showSecondEPG(EPGMODE.EPGMODE_DELETED, deleteChannelBySQ, 0, false);
                    break;
                }*/
                ///finish();
                break;
                ///break;
            case 2:
                this.mOpenEPGView.upDateSecondEPG(true);
                SkyEPGApi.getInstance().swithcChannel(Integer.parseInt(skyEPGData.getChannelNumber()),skyEPGData.getServiceType());
                /*if (this.currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.ATV) {
                    Long valueOf = Long.valueOf(System.currentTimeMillis());
                    long longValue = valueOf.longValue();
                    long longValue2 = this.lastSwitchSourceToTime.longValue();
                    this.lastSwitchSourceToTime = valueOf;
                    int i3 = Math.abs(Long.valueOf(longValue - longValue2).longValue()) < 800 ? 700 : 0;
                   /// SkyTVDebug.debug("onSecondEPGItemOnClick delay:" + i3);
                    channel = (Channel) skyEPGData.getData();
                    Channel channel4 = new Channel(channel.id, channel.name);
                    channel4.index = channel.index;
                    channel4.bSkip = channel.bSkip;
                    channel4.bAfcEnable = channel.bAfcEnable;
                    channel4.source = channel.source;
                    channel4.mapindex = channel.mapindex;
                    channel4.invalid = channel.invalid;
                    channel4.type = CHANNEL_TYPE.valueOf(channel.type.toString());
                    SkyTvAsyncTask.getInstance().runAsyncTask(399, new IAsyncTask() {
                        public void doAsyncTask(Object obj) {
                            synchronized (SkyOpenEPGActivity.this.switchChannelLock) {
                                Channel channel = (Channel) obj;
                                SkyDataCache.getInstance().setCurChannel(channel);
                                ///SkyEPGApi.getInstance().systemApi.switchChannel(channel);
                            }
                        }
                    }, channel4, (long) i3, true);
                } else {
                    this.getChannelAsyncTask.start(skyEPGData.getData());
                }
                try {
                    Map hashMap = new HashMap();
                    hashMap.put("source", this.currentSource.toString());
                    hashMap.put("cur_model", "leftEpg");
                    hashMap.put("channel_name", skyEPGData.getItemTitle());
                    ///SkyTvSubmitLog.submitAppLog(this.context, null, LOGTYPE.Action, "SkyTv", 3, hashMap, false);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }*/
                Log.d("Maxs28","titleView == null = " + titleView);
                ///SkyLeftEPGDataGet.getInstance().setCatallDataGetted(false);
                 SkyLeftEPGDataGet.getInstance().clear();
                ////EPGLoader.getInstance().clear(SkyTvController.getInstance().getCurrentSource());
                break;
            case 3:
               /// SkyTVDebug.debug("begin sort");
                if (this.titleView != null) {
                    this.titleView.showLeftTitleView(getResources().getString(R.string.KTC_CFG_TV_CHANNEL_SORT
                    ));
                } else {
                  ///  SkyTVDebug.error("EPGMODE_SORT titleView null");
                }
                SkyLeftEPGDataGet.getInstance().setCatallDataGetted(false);
               /// SkyLeftEPGDataGet.getInstance().clear();
                ////EPGLoader.getInstance().clear(SkyTvController.getInstance().getCurrentSource());
                this.currentSortChannel = (Channel) skyEPGData.getData();
                ///SkyTVDebug.debug("onSecondEPGItemOnClick  currentSortChannel type:" + this.currentSortChannel.type + "  currentChannel.mapindex:" + this.currentSortChannel.mapindex + "  sortTo:" + i2);
                /*Iterable<Channel> dBTableSortList2 = ExternalApi.getInstance().epgApi.getDBTableSortList(KtcTvController.getInstance().getCurrentSource());
                List arrayList3 = new ArrayList();
                List arrayList4 = new ArrayList();
                if (i2 >= 1 && dBTableSortList2 != null) {
                    if (this.currentSortChannel.mapindex != i2) {
                        arrayList3.add(this.currentSortChannel);
                        arrayList4.add(Integer.valueOf(i2));
                       //// SkyTVDebug.debug("sortChannel type:" + this.currentSortChannel.type + "  orignal:" + this.currentSortChannel.mapindex + "  sortTo:" + i2);
                        if (this.currentSortChannel.mapindex > i2) {
                            for (Channel channel5 : dBTableSortList2) {
                                if (channel5.mapindex < this.currentSortChannel.mapindex && channel5.mapindex >= i2 && channel5.type == this.currentSortChannel.type) {
                                    arrayList3.add(channel5);
                                    arrayList4.add(Integer.valueOf(channel5.mapindex + 1));
                                }
                                if (channel5.mapindex <= this.currentSortChannel.mapindex || channel5.type != this.currentSortChannel.type) {
                                }
                            }
                        } else {
                            for (Channel channel52 : dBTableSortList2) {
                                if (channel52.mapindex > this.currentSortChannel.mapindex && channel52.mapindex <= i2 && channel52.type == this.currentSortChannel.type) {
                                    arrayList3.add(channel52);
                                    arrayList4.add(Integer.valueOf(channel52.mapindex - 1));
                                }
                            }
                        }
                        ExternalApi.getInstance().epgApi.sortChannelListCache(arrayList3, arrayList4, KtcTvController.getInstance().getCurrentSource());
                        SkyLeftEPGDataGet.getInstance().sortChannelList(new SORT_CHANNELLIST_PARAM(arrayList3, arrayList4, KtcTvController.getInstance().getCurrentSource()));
                        SkyLeftEPGDataGet.getInstance().setCatallDataGetted(false);
                    }
                   /// SkyTVDebug.debug(">>>>>>>>>>>>SHOWSORTEPG<<<<<<<<<<<< isCatallDataGetted:" + SkyLeftEPGDataGet.getInstance().isCatallDataGetted());
                    if (!SkyLeftEPGDataGet.getInstance().isCatallDataGetted()) {
                        SkyLeftEPGDataGet.getInstance().getLeftEpgCatallAsync(true, this.currentSortChannel);
                        break;
                    }
                    int itemIndexID = ((SkyEPGItem_2) view).getItemData().getItemIndexID();
                   /// SkyTVDebug.debug("onSecondEPGItemOnClick，currentFocusId:" + itemIndexID);
                    getLeftEpgDataOnStart(CATTYPE.CAT_ALL);
                    this.currentLeftEPGDataChannel.setDefault_index(itemIndexID);
                    Message obtainMessage = this.mHandler.obtainMessage();
                    obtainMessage.what = SHOWSORTEPG;
                    this.mHandler.sendMessage(obtainMessage);
                    break;
                }*/
                int itemIndexID = ((SkyEPGItem_2) view).getItemData().getItemIndexID();
                SkyEPGApi.getInstance().sortChannel(skyEPGData, sortTo);
                ///SkyLeftEPGDataGet.getInstance().getCatFirstEpgDataAsync();
                SkyLeftEPGDataGet.getInstance().getLeftEpgCatallAsync(false, null);
                // itemIndex sortTo
                Log.d("Maxs28","----->itemIndexID = " + itemIndexID);
                /// SkyTVDebug.debug("onSecondEPGItemOnClick，currentFocusId:" + itemIndexID);
                getLeftEpgDataOnStart(CATTYPE.CAT_ALL);
                Log.d("Maxs28","currentLeftEPGDataChannel == null = " + (currentLeftEPGDataChannel == null));
                this.currentLeftEPGDataChannel.setDefault_index(itemIndexID);
                Message obtainMessage = this.mHandler.obtainMessage();
                obtainMessage.what = SHOWSORTEPG;
                this.mHandler.sendMessage(obtainMessage);
                break;
        }
        return false;
    }

    public boolean onSecondEPGItemOnKeyBack(int i, View view) {
        ////SkyTVDebug.debug("onSecondEPGItemOnKeyBack itemID:" + i);
        this.secondSelectItemID = i;
        this.isOnSecondEPGItemOnKeyLeft = true;
        if (this.titleView != null) {
            this.titleView.hideLeftTitleView();
        }
        return false;
    }

    public boolean onSecondEPGItemOnKeyDown(EPGMODE epgmode, int i, View view) {

        int itemIndexID = ((SkyEPGItem_2) view).getItemData().getItemIndexID();
        boolean checkNeedUpdateSecondEpg = checkNeedUpdateSecondEpg(new GET_REMAINCHANNEL_PARAM(GET_REMAINCHANNEL_MODE.KEYDOWN, itemIndexID, this.uiChannelItemList, epgmode));
        Log.d("Maxs25","onSecondEPGItemOnKeyDown itemIndexID:" + itemIndexID + "  needUpdateSecondEpg:" + checkNeedUpdateSecondEpg);
        ///SkyTVDebug.debug("onSecondEPGItemOnKeyDown itemIndexID:" + itemIndexID + "  needUpdateSecondEpg:" + checkNeedUpdateSecondEpg);
        if (checkNeedUpdateSecondEpg) {
            SkyLeftEPGDataGet.getInstance().getChannelDataRemanin(new GET_REMAINCHANNEL_PARAM(GET_REMAINCHANNEL_MODE.KEYDOWN, itemIndexID, this.uiChannelItemList, epgmode));
        }
        return false;
    }

    public boolean onSecondEPGItemOnKeyLeft(int i, View view) {
        ///SkyTVDebug.debug("onSecondEPGItemOnKeyLeft");
        this.isOnSecondEPGItemOnKeyLeft = true;
        this.secondSelectItemID = i;
        return false;
    }

    public boolean onSecondEPGItemOnKeyRight(int i, View view, SkyEPGData skyEPGData) {
        ///SkyTVDebug.debug("onSecondEPGItemOnKeyRight");
        return false;
    }

    public boolean onSecondEPGItemOnKeyUp(EPGMODE epgmode, int id, View view) {

        int itemIndexID = ((SkyEPGItem_2) view).getItemData().getItemIndexID();
        ///itemIndexID = id;
        boolean checkNeedUpdateSecondEpg = checkNeedUpdateSecondEpg(new GET_REMAINCHANNEL_PARAM(GET_REMAINCHANNEL_MODE.KEYUP, itemIndexID, this.uiChannelItemList, epgmode));
        Log.d("Maxs25","onSecondEPGItemOnKeyUp itemIndexID:" + itemIndexID + "  needUpdateSecondEpg:" + checkNeedUpdateSecondEpg);
        ///SkyTVDebug.debug("onSecondEPGItemOnKeyUp itemIndexID:" + itemIndexID + "  needUpdateSecondEpg:" + checkNeedUpdateSecondEpg);
        if (checkNeedUpdateSecondEpg) {
            SkyLeftEPGDataGet.getInstance().getChannelDataRemanin(new GET_REMAINCHANNEL_PARAM(GET_REMAINCHANNEL_MODE.KEYUP, itemIndexID, this.uiChannelItemList, epgmode));
        }
        return false;
    }

    protected void onStart() {
        super.onStart();
        SkyEPGApi.getInstance().init(SkyContext.getListener(), this);
        SkyLeftEPGDataGet.getInstance().setOnUpdateEPGFromNowListener(this);
        if ("leftview".equals(this.viewType)) {
            startLoadEPGUIData(true);
        } else if ("rightview".equals(this.viewType)) {
           ///// if (ScreenDisplayManager.getInstance().getIsExistEPGFlag()) {
                ////SkyTVDebug.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onStart_show:");
                try {
                    this.curChannel = SkyEPGApi.getInstance().getCurrentChannel();
                   //// SkyTVDebug.debug("onstart_currentChannel: " + SkyDataCache.getInstance().getCurChannel().displayName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (this.curChannel.source.equals(Source.ATV())) {
                        this.rightView.showChannelInfo("" + this.curChannel.mapindex, "模拟电视" + this.curChannel.displayName);
                    } else {
                        this.rightView.showChannelInfo("" + this.curChannel.mapindex, this.curChannel.displayName);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                  ///  SkyTVDebug.debug(">>>DataCacheSize:: " + SkyDataCache.getInstance().getEventItemDataCache().size());
                    if (SkyDataCache.getInstance().getEventItemDataCache().size() == 0) {
                        SkyDataCache.getInstance().clear();
                        startLoadEPGUIData(false);
                    } else {
                      /////  ScreenDisplayManager.getInstance().hideScreenDisplay();
                        this.rightView.showRightEPGView(SkyDataCache.getInstance().getEventItemDataCache(), SkyDataCache.getInstance().getEventFocusIndex());
                    }
                } catch (Exception e22) {
                    e22.printStackTrace();
                }
           /// } else {
                ////SkyTVDebug.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<onStart_show:");
                SwitchToScreenShow();
           /// }
            startMonitorThread(0);
        } else if ("channelEditSort".equals(this.viewType)) {
            Log.d("Maxs28","SkyLeftEPGDataGet.getInstance().isCatallDataGetted() = " + SkyLeftEPGDataGet.getInstance().isCatallDataGetted());
            if (SkyLeftEPGDataGet.getInstance().isCatallDataGetted()) {

                /*getLeftEpgDataOnStart(CATTYPE.CAT_ALL);
                /////SkyTVDebug.debug(">>>>>>>>>>>>SHOWSORTEPG<<<<<<<<<<<<");
                Message obtainMessage = this.mHandler.obtainMessage();
                obtainMessage.what = SHOWSORTEPG;
                this.mHandler.sendMessage(obtainMessage);*/
                SkyLeftEPGDataGet.getInstance().getLeftEpgCatallAsync(false, null);
            } else {
                Log.d("Maxs28","<-----getLeftEpgCatallAsync ----->");
                SkyLeftEPGDataGet.getInstance().getLeftEpgCatallAsync(false, null);
            }
        } else if ("channelEditDeleted".equals(this.viewType)) {
            this.titleView.showLeftTitleView(getResources().getString(R.string.KTC_CFG_TV_CHANNEL_DELET_HAVE));
            this.mOpenEPGView.showSecondEPG(EPGMODE.EPGMODE_DELETED, SkyLeftEPGDataGet.getInstance().deleteEPGDataList, 0, false);
        }
        eventHandler();
    }

    public void startLoadEPGUIData(boolean z) {
        if (z) {
            SkyLeftEPGDataGet.getInstance().getCatFirstEpgDataAsync();
            return;
        }
        this.getEventDataAsyncTask.start("");
    }
}

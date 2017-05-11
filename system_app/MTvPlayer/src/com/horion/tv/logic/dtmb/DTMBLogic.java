package com.horion.tv.logic.dtmb;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.horion.tv.R;
import com.horion.tv.framework.ChannelListActivity;
import com.horion.tv.framework.ProgramListViewActivity;
import com.horion.tv.framework.ui.TvIntent;
import com.horion.tv.ui.ChannelInfoActivity;
import com.horion.tv.ui.search.SearchSelectActivity;
import com.horion.tv.util.Constants;
import com.horion.tv.widget.PanasonicToast;
import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
////import com.ktc.framework.ktcsdk.logger.KtcServerLogger.LOGTYPE;
import com.horion.system.data.TCEnumSetData;
import com.horion.system.data.TCRetData;
import com.horion.system.data.TCSetData;
import com.horion.system.data.TCSwitchSetData;
////import com.horion.tv.api.dtmb.DTMBApiParamsDtvChannelInfo;
////import com.horion.tv.api.dtmb.DTMBApiParamsProcessManualSearch;
////import com.horion.tv.api.dtmb.DTMBApiParamsSearchResult;
////import com.horion.tv.api.dtmb.DTMBAutoSearchListener;
////import com.horion.tv.api.dtmb.DTMBManualSearchListener;
////import com.horion.tv.api.dvbc.DVBCApiParamsDtvFrequencySetting;
////import com.horion.tv.api.system.SystemApiParamShowInfoView.InfoViewData;
////import com.horion.tv.define.object.Channel;
////import com.horion.tv.define.object.Source;
////import com.horion.tv.define.uilogic.KtcUILogicTvCommand;
////import com.horion.tv.define.uilogic.UILOGIC_TVMENU_COMMAND;
import com.horion.tv.define.uilogic.UILOGIC_TVMENU_COMMAND;
import com.horion.tv.framework.logicapi.framework.DTMBLogicApi;
import com.horion.tv.framework.logicapi.framework.TvLogicGetFunction;
import com.horion.tv.framework.logicapi.framework.TvLogicSetFunction;
import com.horion.tv.framework.logicapi.framework.UiCmdParams;
////import com.horion.tv.framework.plugin.interfaces.IDVBC.DtvSubTitle;
////import com.horion.tv.framework.plugin.interfaces.IDVBC.ModulationMode;
import com.horion.tv.framework.ui.IDTMBSearchBackView;
////import com.horion.tv.framework.ui.uidata.TCNoneSetData;
////import com.horion.tv.framework.ui.uidata.TvUIData;
////import com.horion.tv.framework.ui.uidata.dtv.DTVChannelSearchUIData.SEARCH_TYPE;
////import com.horion.tv.framework.ui.uidata.dtv.DTVChannelUIData.ModuleMode;
////import com.horion.tv.framework.ui.uidata.epg.ChannelCollectManager;
////import com.horion.tv.framework.ui.uidata.selectmenu.SelectMenuData;
////import com.horion.tv.framework.ui.uidata.selectmenu.SelectMenuData.CHANNEL;
////import com.horion.tv.framework.ui.uidata.selectmenu.SelectMenuData.SELECTMENU_CMD;
////import com.horion.tv.framework.ui.uidata.selectmenu.SelectMenuItem;
////import com.horion.tv.logic.ExternalApi;
////import com.horion.tv.logic.TvLogicManager;
////import com.horion.tv.logic.channeledit.KtcTVDeleteChannelUtils;
import com.horion.tv.logic.framework.TvLogic;
import com.ktc.util.KtcTVTosatView;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
////import com.horion.tv.logic.system.NoSignalManager;
////import com.horion.tv.logic.system.SystemLogic;
////import com.horion.tv.logic.utils.BroadcastKeyManager;
////import com.horion.tv.logic.utils.EPGLoader;
////import com.horion.tv.logic.utils.ScreenDisplayManager;
////import com.horion.tv.logic.utils.SwitchChannelManagerL;
////import com.horion.tv.logic.utils.TvLogicUtils;
////import com.horion.tv.service.base.KtcTvController;
////import com.horion.tv.service.epg.KtcLeftEPGDataGet;
////import com.horion.tv.ui.forlogic.KtcTvUI;
////import com.horion.tv.ui.forlogic.SystemUI;
////import com.horion.tv.ui.searchview.SearchBackView;
////import com.horion.tv.ui.searchview.popup.KtcTVPopupUIManager;
////import com.horion.tv.utils.UITextUtilInterface.UITextUtil;
////import com.horion.tv.utils.ChannelEditApiParamsInfo;
////import com.horion.tv.utils.DolbyConfig;
////import com.horion.tv.utils.KtcTVDebug;
////import com.horion.tv.utils.KtcTvConfig;
////import com.horion.tv.utils.KtcTvLogicAsyncTask;
////import com.horion.tv.utils.KtcTvLogicAsyncTask.IAsyncTask;
////import com.horion.tv.utils.KtcTvSubmitLog;
////import com.horion.tv.utils.KtcTvUtils;
////import java.util.ArrayList;
////import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DTMBLogic extends TvLogic implements DTMBLogicApi, IDTMBSearchBackView {
    private static DTMBLogic instance = null;
    ////public DTMBAutoSearchListener autoSearchListener = new DTMBAutoSearchListener() {
        /////private static /* synthetic */ int[] -com_tianci_tv_framework_plugin_interfaces_IDVBC$ModulationModeSwitchesValues;
        ////final /* synthetic */ int[] $SWITCH_TABLE$com$horion$tv$framework$plugin$interfaces$IDVBC$ModulationMode;
       //// private final byte[] RET = KtcTvUtils.toBytes(Boolean.TRUE);

       //// private static /* synthetic */ int[] -getcom_tianci_tv_framework_plugin_interfaces_IDVBC$ModulationModeSwitchesValues() {
        /*    if (-com_tianci_tv_framework_plugin_interfaces_IDVBC$ModulationModeSwitchesValues != null) {
                return -com_tianci_tv_framework_plugin_interfaces_IDVBC$ModulationModeSwitchesValues;
            }
            int[] iArr = new int[ModulationMode.values().length];
            try {
                iArr[ModulationMode.QAM_128.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ModulationMode.QAM_16.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ModulationMode.QAM_256.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ModulationMode.QAM_32.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ModulationMode.QAM_64.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            -com_tianci_tv_framework_plugin_interfaces_IDVBC$ModulationModeSwitchesValues = iArr;
            return iArr;
        }

        public byte[] onDTMBAutoSearchEnd() {
            NoSignalManager.getInstance().allowScreenSaverFuncFromLauncher();
            KtcTvUI.getInstance().getDTMBUI().hideSearchDialog(SEARCH_TYPE.DTMB_AUTO_SEARCH);
            TvLogicManager.getInstance().updateCurrentSourceEPG();
            Source currentSource = TvLogicManager.getInstance().getCurrentSource();
            Channel currentChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
            DTMBLogic.this.isDtmbExiting = false;
            BroadcastKeyManager.getInstance().enableTvSearchKeys();
            if (currentChannel != null) {
                KtcTVDebug.debug("onDTMBAutoSearchEnd currentChannel:" + currentChannel.name + "@" + currentChannel.mapindex);
            } else {
                KtcTVDebug.debug("onDTMBAutoSearchEnd currentChannel null:");
            }
            NoSignalManager.getInstance().updateSearchGuideFlag(currentSource);
            List channelList = EPGLoader.getInstance().getChannelList(currentSource);
            if (currentSource != null) {
                ScreenDisplayManager.getInstance().showScreenDisplay(currentSource, currentChannel, false, true, false, false);
            }
            try {
                Map hashMap = new HashMap();
                hashMap.put("source", currentSource.toString());
                hashMap.put("search_number", channelList.size() + "");
                KtcTvSubmitLog.submitAppLog(MainMenuActivity.getInstance(), null, LOGTYPE.Action, "KtcTv", 3, hashMap, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SystemLogic.getInstance().setSearchFlag(false);
            return this.RET;
        }

        public byte[] onDTMBAutoSearchProcess(DTMBApiParamsSearchResult dTMBApiParamsSearchResult) {
            KtcTVDebug.debug("<<<<<<<<<<<<<onDTMBAutoSearchProcess");
            KtcTvUI.getInstance().getDTMBUI().updateSearchDialog(dTMBApiParamsSearchResult, SEARCH_TYPE.DTMB_AUTO_SEARCH);
            return this.RET;
        }

        public byte[] onDTMBAutoSearchStart() {
            ModuleMode moduleMode;
            KtcTVDebug.debug("<<<<<<<<<<<<<onDTMBAutoSearchStart");
            BroadcastKeyManager.getInstance().disableTvSearchKeys();
            DVBCApiParamsDtvFrequencySetting dtvFrequency = ExternalApi.getInstance().dtmbApi.getDtvFrequency();
            ModuleMode moduleMode2 = ModuleMode.QAM64;
            switch (AnonymousClass1.-getcom_tianci_tv_framework_plugin_interfaces_IDVBC$ModulationModeSwitchesValues()[dtvFrequency.mode.ordinal()]) {
                case 1:
                    moduleMode = ModuleMode.QAM128;
                    break;
                case 2:
                    moduleMode = ModuleMode.QAM16;
                    break;
                case 3:
                    moduleMode = ModuleMode.QAM256;
                    break;
                case 4:
                    moduleMode = ModuleMode.QAM32;
                    break;
                case 5:
                    moduleMode = ModuleMode.QAM64;
                    break;
                default:
                    moduleMode = ModuleMode.QAM64;
                    break;
            }
            KtcTvUI.getInstance().getDTMBUI().showAutoSearchDialog(moduleMode);
            KtcLeftEPGDataGet.getInstance().clear();
            ChannelCollectManager.getInstance().clearCollectList(Source.DTMB());
            return this.RET;
        }
    };

    private DTMBApiParamsProcessManualSearch currentDTMBApiParamsProcessManualSearch = null;
    */
    private Timer dtmbLogicTimer = null;
    private TvLogicSetFunction dtmbShowChannelInfoMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.DTMB_SHOW_CHANNELINFO_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs","DTMBLoigc.dtmbShowChannelInfoMenu!");
            return new TCRetData(DTMBLogic.this.showChannelInfo());
        }
    };

    private TvLogicSetFunction atvShowChannelInfoMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.ATV_SHOW_CHANNELINFO_INFO.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs","atvLoigc.dtmbShowChannelInfoMenu!");
            return new TCRetData(DTMBLogic.this.showChannelInfo());
        }
    };
    private TvLogicSetFunction dtmbShowDtvInfo = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.DTMB_SHOW_DTVINFO_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs","DTMBLoigc.dtmbShowDtvInfo!");
            return new TCRetData(DTMBLogic.this.showDtvInfo());
        }
    };
    private TvLogicSetFunction dtmbShowSearchMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.DTMB_SHOW_SEARCH_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs","DTMBLoigc.dtmbShowSearchMenu!");
            Intent intent = new Intent(mContext,SearchSelectActivity.class);
            intent.putExtra("searchType","DTV");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("Maxs","-------------------------------DTVLogic:DtvShowSearcMenu:-------------------------------");
            mContext.startActivity(intent);
            return null;
        }
    };

    private TvLogicSetFunction dtmbChannelMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.DTMB_SHOW_CHANNEL_EDIT.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs26","DTV Channel Edit");
            Intent intent = new Intent(mContext,ProgramListViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return null;
        }
    };

    private TvLogicSetFunction dtmLoveMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.DTMB_LOVECHANNEL_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs26","DTV Channel Love");
            Intent intent = new Intent(mContext,ChannelListActivity.class);
            intent.putExtra("ListId", Constants.SHOW_FAVORITE_LIST);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return null;
        }
    };

    private TvLogicSetFunction DtvChanneSort = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.DTV_CHANNEL_SORT.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs28","Dtv Channel Sort");

            Intent intent = new Intent(TvIntent.CHANNEL_LIST);
            intent.putExtra("viewtype", "channelEditSort");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return null;
        }
    };
   /* private TvLogicSetFunction dvbcDeleteChannelMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.DTMB_CHANNELDELET_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            return new TCRetData(DTMBLogic.this.showDeleteChannelDialog());
        }
    };*/
    private String exitType = null;
    /*private TvLogicGetFunction getDTMBSubTitle = new TvLogicGetFunction(UILOGIC_TVMENU_COMMAND.DTMB_MENU_GET_SUBTITLE.toString()) {
        public TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams) {
            return DTMBLogic.this.getDTMBSubTitle();
        }
    };*/
    private List<String> subtitleValues = null;

    private class HideDisplayTimerTask extends TimerTask {
        private HideDisplayTimerTask() {
        }

        public void run() {
        }
    }

    public static DTMBLogic getInstance() {
        if (instance == null) {
            instance = new DTMBLogic();
        }
        return instance;
    }

    private void showSelectView() {
    }

    private void startDtmbLogicTimer() {
    }

    public void backToDTMBSelectView() {
    }

    public boolean dtmbExitSearch(String str) {
        return false;
    }

    public boolean dtmbExitSearch(String str, boolean z) {
        return false;
    }

    public TCSwitchSetData dtmbMenuGetChannelSkip() {
        return null;
    }

    public boolean dtmbMenuSetChannelSkip(int i) {
        return false;
    }

    public TCEnumSetData getDTMBSubTitle() {
        return null;
    }

    public void init(Context context, KtcCmdConnectorListener ktcCmdConnectorListener) {
        super.init(context, ktcCmdConnectorListener);
        ////this.isDtmbExiting = false;
        ////SearchBackView.getInstance().setDTMBViewBackListner(this);
    }

    public boolean setDTMBSubTitle(int i) {
        /*KtcTVDebug.debug("langlang SubTitletype index = " + i);
        if (this.subtitleValues == null || this.subtitleValues.size() <= 1) {
            return false;
        }
        return ExternalApi.getInstance().dtmbApi.setSubtitleType(DtvSubTitle.valueOf(((String) this.subtitleValues.get(i)).substring(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD.length())));
    */return false;
    }

    public boolean showChannelInfo() {
        ProgramInfo currentProgInfo = TvChannelManager.getInstance().getCurrentProgramInfo();
        Log.d("Maxs104","currentProgInfo.serviceName == null " + (currentProgInfo.serviceName == null));
        if (currentProgInfo != null && !("".equals(currentProgInfo.serviceName))) {
            Intent intent = new Intent(mContext, ChannelInfoActivity.class);
            intent.putExtra("channelinfo", "DTV");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("Maxs", "-------------------------------DTVLogic:showChannelInfo:-------------------------------");
            mContext.startActivity(intent);
        }else{
            KtcTVTosatView ktcTVTosatView = new KtcTVTosatView(mContext);
            ktcTVTosatView.showTVToastView(R.string.show_no_tv);
        }
       return false;
    }

    public boolean showDeleteChannelDialog() {
        return false;
    }

    public boolean showDtvInfo() {
        return false;
    }

    public boolean startAutoSearch() {
        return false;
    }

    public boolean startDTMBManualSearchFromUi(float f, float f2, float f3) {
        return false;
    }

    public boolean startManualSearch() {
        return false;
    }

    public boolean stopAutoSearch() {
        /*return ExternalApi.getInstance().dtmbApi.stopDTMBAutoSearch();*/return false;
    }

    public String stopManualSearch() {
        return null;
    }
}

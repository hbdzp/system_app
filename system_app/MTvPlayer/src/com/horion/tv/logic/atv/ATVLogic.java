package com.horion.tv.logic.atv;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.horion.tv.framework.ChannelListActivity;
import com.horion.tv.framework.ProgramListViewActivity;
import com.horion.tv.framework.ui.TvIntent;
import com.horion.tv.ui.search.SearchSelectActivity;
import com.horion.tv.util.Constants;
import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
/////import com.ktc.framework.ktcsdk.logger.KtcLogger;
/////import com.ktc.framework.ktcsdk.logger.KtcServerLogger.LOGTYPE;
import com.horion.system.data.TCEnumSetData;
import com.horion.system.data.TCRangeSetData;
import com.horion.system.data.TCRetData;
import com.horion.system.data.TCSwitchSetData;
////import com.horion.system.define.KtcConfigDefs;
////import com.horion.tv.api.atv.ATVApiParamsSetSkipChannel;
////import com.horion.tv.api.atv.ATVAutoSearchListener;
////import com.horion.tv.api.atv.ATVFrequencyTrimListener;
////import com.horion.tv.api.atv.ATVManualSearchListener;
////import com.horion.tv.define.KtcTvDefine.ATVAUDIONICAM;
////import com.horion.tv.define.KtcTvDefine.COLORSYSTEM;
////import com.horion.tv.define.KtcTvDefine.SOUNDSYSTEM;
////import com.horion.tv.define.object.Channel;
////import com.horion.tv.define.object.Source;
////import com.horion.tv.define.uilogic.KtcUILogicTvCommand;
////import com.horion.tv.define.uilogic.UILOGIC_TVMENU_COMMAND;
import com.horion.tv.define.uilogic.UILOGIC_TVMENU_COMMAND;
import com.horion.tv.framework.logicapi.framework.ATVLogicApi;
import com.horion.tv.framework.logicapi.framework.TvLogicSetFunction;
import com.horion.tv.framework.logicapi.framework.UiCmdParams;
import com.horion.tv.framework.ui.IATVSearchBackView;
////import com.horion.tv.framework.ui.uidata.TCNoneSetData;
/////import com.horion.tv.framework.ui.uidata.TvUIData;
////import com.horion.tv.framework.ui.uidata.atv.ATVChannelSearchData.SEARCH_TYPE;
////import com.horion.tv.framework.ui.uidata.epg.ChannelCollectManager;
////import com.horion.tv.framework.ui.uidata.selectmenu.SelectMenuData;
////import com.horion.tv.framework.ui.uidata.selectmenu.SelectMenuData.CHANNEL;
////import com.horion.tv.framework.ui.uidata.selectmenu.SelectMenuData.SELECTMENU_CMD;
////import com.horion.tv.framework.ui.uidata.selectmenu.SelectMenuItem;
////import com.horion.tv.logic.channeledit.KtcTVDeleteChannelUtils;
import com.horion.tv.logic.framework.TvLogic;
////import com.horion.tv.logic.system.NoSignalManager;
////import com.horion.tv.logic.system.SystemLogic;
////import com.horion.tv.logic.utils.BroadcastKeyManager;
////import com.horion.tv.logic.utils.EPGLoader;
////import com.horion.tv.logic.utils.ScreenDisplayManager;
////import com.horion.tv.logic.utils.SwitchChannelManagerL;
////import com.horion.tv.logic.utils.TvLogicUtils;
////import com.horion.tv.service.base.KtcTvController;
////import com.horion.tv.service.epg.KtcEPGApi;
////import com.horion.tv.service.epg.KtcLeftEPGDataGet;
////import com.horion.tv.ui.forlogic.KtcTvUI;
////import com.horion.tv.ui.forlogic.SystemUI;
////import com.horion.tv.ui.searchview.SearchBackView;
////import com.horion.tv.ui.searchview.popup.KtcTVPopupUIManager;
////import com.horion.tv.utils.UITextUtilInterface.UITextUtil;
////import com.horion.tv.utils.ChannelEditApiParamsInfo;
////import com.horion.tv.utils.KtcTVDebug;
////import com.horion.tv.utils.KtcTvConfig;
////import com.horion.tv.utils.KtcTvLogicAsyncTask;
////import com.horion.tv.utils.KtcTvLogicAsyncTask.IAsyncTask;
////import com.horion.tv.utils.KtcTvSubmitLog;


public class ATVLogic extends TvLogic implements ATVLogicApi, IATVSearchBackView {
    private static ATVLogic instance = null;
    ////private final int ADD_1M;
    ////private final int ATV_EVENTINTERVAL;
    ////private final int ATV_MAX_FREQ;
    ////private final int ATV_MIN_FREQ;
   //// private final int ATV_SEARCH_BAND;
    ////private final String HIGH_FREQ;
    ////private final String LOW_FREQ;
   //// private final String MID_FREQ;
    ////private final int UHF_LOW_FREQ;
    ////private final int VHF_HIGH_FREQ;
   /* private TvLogicSetFunction atvDeleteChannelMenu  = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.ATV_CHANNELDELET_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            return new TCRetData(ATVLogic.this.showDeleteChannelDialog());
        }
    };*/
    ///private Timer atvLogicTimer;
    ///private List<String> atvNicamValues;
    private TvLogicSetFunction atvShowSearchMenu= new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.ATV_SHOW_SEARCH_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Intent intent = new Intent(mContext,SearchSelectActivity.class);
            intent.putExtra("searchType","ATV");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d("Maxs","-------------------------------ATVLogic:atvShowSearcMenu:-------------------------------");
            mContext.startActivity(intent);
            return null;
        }
    };
    private TvLogicSetFunction AtvChanneSort = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.ATV_CHANNEL_SORT.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs28","ATV:Channel Sort");
            Intent intent = new Intent(TvIntent.CHANNEL_LIST);
            intent.putExtra("viewtype", "channelEditSort");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return null;
        }
    };

    private TvLogicSetFunction atvChanelEditMenu= new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.ATV_SHOW_CHANNEL_EDIT.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs26","atvChanelEditMenu");
            Intent intent = new Intent(mContext,ProgramListViewActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);

            return null;
        }
    };

    private TvLogicSetFunction chanelLoveMenu= new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.ATV_LOVECHANNEL_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs26","chanelLoveMenu");
            Intent intent = new Intent(mContext,ChannelListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ListId", Constants.SHOW_FAVORITE_LIST);
            mContext.startActivity(intent);
            return null;
        }
    };
    ////public ATVAutoSearchListener autoSearchListener;
    ////private List<String> colorSystemValues;
    ////private Channel currentAtvManualSearchChannel;
   //// private String exitType;
   //// private ATVFrequencyTrimListener frequencyTrimListener;
   /// private volatile boolean isAtvAutoStopSearching;
    ////public ATVManualSearchListener manualSearchListener;
    ///private List<String> soundSystemValues;
/*
    private class HideDisplayTimerTask extends TimerTask {
        private HideDisplayTimerTask() {
        }

        public void run() {
            if (ATVLogic.this.exitType != null) {
                if (ATVLogic.this.exitType.equals(SEARCH_TYPE.AUTO_SEARCH.toString())) {
                    KtcTvUI.getInstance().getATVUI().hideAtvChannelSearchDialog(SEARCH_TYPE.AUTO_SEARCH);
                } else if (ATVLogic.this.exitType.equals(SEARCH_TYPE.MANUAL_SEARCH.toString())) {
                    KtcTvUI.getInstance().getATVUI().hideAtvChannelSearchDialog(SEARCH_TYPE.MANUAL_SEARCH);
                } else if (ATVLogic.this.exitType.equals(SEARCH_TYPE.FREQUENCY_ADJUST.toString())) {
                    KtcTvUI.getInstance().getATVUI().hideAtvChannelSearchDialog(SEARCH_TYPE.FREQUENCY_ADJUST);
                }
                ATVLogic.this.isAtvAutoStopSearching = false;
            }
        }
    }*/
/*
    private ATVLogic() {
        this.atvLogicTimer = null;
        this.exitType = null;
        this.ATV_MIN_FREQ = 45200;
        this.ATV_MAX_FREQ = 866200;
        this.ATV_SEARCH_BAND = 821000;
        this.ATV_EVENTINTERVAL = 1000000;
        this.ADD_1M = 900;
        this.VHF_HIGH_FREQ = 140250;
        this.UHF_LOW_FREQ = 428250;
        this.LOW_FREQ = "VHF-L";
        this.MID_FREQ = "VHF-H";
        this.HIGH_FREQ = "UHF";
        this.atvNicamValues = null;
        ////this.currentAtvManualSearchChannel = null;
        this.isAtvAutoStopSearching = false;*/
        /*
        this.autoSearchListener = new ATVAutoSearchListener() {
            private final byte[] RET = Boolean.TRUE.toString().getBytes();

            public byte[] onATVAutoSearchEnd() {
                NoSignalManager.getInstance().allowScreenSaverFuncFromLauncher();
                TvLogicManager.getInstance().updateCurrentSourceEPG();
                Channel currentChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
                Source currentSource = TvLogicManager.getInstance().getCurrentSource();
                NoSignalManager.getInstance().updateSearchGuideFlag(currentSource);
                List channelList = EPGLoader.getInstance().getChannelList(currentSource);
                KtcTVDebug.debug("onATVAutoSearchEnd currentChannel:" + currentChannel.name + "@" + currentChannel.mapindex);
                if (currentSource != null) {
                    ScreenDisplayManager.getInstance().showScreenDisplay(currentSource, currentChannel, false, true, false, false);
                }
                KtcTvUI.getInstance().getATVUI().hideAtvChannelSearchDialog(SEARCH_TYPE.AUTO_SEARCH);
                ATVLogic.this.isAtvAutoStopSearching = false;
                BroadcastKeyManager.getInstance().enableTvSearchKeys();
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

            public byte[] onATVAutoSearchProcess(int i, int i2, float f) {
                KtcTvUI.getInstance().getATVUI().updateAtvChannelSearchDialog(i, i2, f, ATVLogic.this.getfrequencyband((int) f), SEARCH_TYPE.AUTO_SEARCH);
                return this.RET;
            }

            public byte[] onATVAutoSearchStart() {
                SystemLogic.getInstance().setSearchFlag(true);
                BroadcastKeyManager.getInstance().disableTvSearchKeys();
                KtcTvUI.getInstance().getATVUI().showAtvAutoSearchDialog(45200.0f, "VHF-L");
                ChannelCollectManager.getInstance().clearCollectList(Source.ATV());
                KtcLeftEPGDataGet.getInstance().clear();
                return this.RET;
            }
        };
        this.manualSearchListener = new ATVManualSearchListener() {
            private final byte[] RET = Boolean.TRUE.toString().getBytes();

            public byte[] onATVManualSearchEnd() {
                KtcTVDebug.debug("onATVManualSearchEnd in ATVLogic");
                NoSignalManager.getInstance().allowScreenSaverFuncFromLauncher();
                KtcTvUI.getInstance().getATVUI().hideAtvChannelSearchDialog(SEARCH_TYPE.MANUAL_SEARCH);
                TvLogicManager.getInstance().updateCurrentSourceEPG();
                NoSignalManager.getInstance().updateSearchGuideFlag(TvLogicManager.getInstance().getCurrentSource());
                BroadcastKeyManager.getInstance().enableTvSearchKeys();
                SystemLogic.getInstance().setSearchFlag(false);
                KtcLeftEPGDataGet.getInstance().clear();
                return this.RET;
            }

            public byte[] onATVManualSearchProcess(int i, int i2, float f) {
                if (ATVLogic.this.currentAtvManualSearchChannel != null) {
                    KtcTvUI.getInstance().getATVUI().updateAtvChannelSearchDialog(ATVLogic.this.currentAtvManualSearchChannel.mapindex, i2, f, ATVLogic.this.getfrequencyband((int) f), SEARCH_TYPE.MANUAL_SEARCH);
                } else {
                    KtcTvUI.getInstance().getATVUI().updateAtvChannelSearchDialog(i, i2, f, ATVLogic.this.getfrequencyband((int) f), SEARCH_TYPE.MANUAL_SEARCH);
                }
                return this.RET;
            }

            public byte[] onATVManualSearchStart() {
                SystemLogic.getInstance().setSearchFlag(true);
                int i = 0;
                BroadcastKeyManager.getInstance().disableTvSearchKeys();
                ATVLogic.this.currentAtvManualSearchChannel = ExternalApi.getInstance().systemApi.getCurrentChannel();
                if (ATVLogic.this.currentAtvManualSearchChannel != null) {
                    i = ATVLogic.this.currentAtvManualSearchChannel.mapindex;
                }
                float frequencyPoint = ExternalApi.getInstance().atvApi.getFrequencyPoint();
                KtcTvUI.getInstance().getATVUI().showAtvManualSearchDialog(i, ATVLogic.this.getProgress((int) frequencyPoint), frequencyPoint, ATVLogic.this.getfrequencyband((int) frequencyPoint));
                return this.RET;
            }

            public byte[] onATVManualSearchStart(int i, float f) {
                SystemLogic.getInstance().setSearchFlag(true);
                int i2 = 0;
                BroadcastKeyManager.getInstance().disableTvSearchKeys();
                ATVLogic.this.currentAtvManualSearchChannel = ExternalApi.getInstance().systemApi.getCurrentChannel();
                if (ATVLogic.this.currentAtvManualSearchChannel != null) {
                    i2 = ATVLogic.this.currentAtvManualSearchChannel.mapindex;
                }
                KtcTvUI.getInstance().getATVUI().showAtvManualSearchDialog(i2, ATVLogic.this.getProgress((int) f), f, ATVLogic.this.getfrequencyband((int) f));
                return this.RET;
            }
        };
        this.frequencyTrimListener = new ATVFrequencyTrimListener() {
            private final byte[] RET = Boolean.TRUE.toString().getBytes();

            public byte[] onATVFrequencyTrimEnd() {
                KtcTVDebug.debug("onATVFrequencyTrimEnd in ATVLogic");
                NoSignalManager.getInstance().allowScreenSaverFuncFromLauncher();
                KtcTvUI.getInstance().getATVUI().hideAtvChannelSearchDialog(SEARCH_TYPE.FREQUENCY_ADJUST);
                BroadcastKeyManager.getInstance().enableTvSearchKeys();
                return this.RET;
            }

            public byte[] onATVFrequencyTrimProcess(int i, float f) {
                KtcTVDebug.debug(">>>>>>>>>onATVFrequencyTrimProcess");
                int i2 = 0;
                Channel currentChannel = ExternalApi.getInstance().systemApi.getCurrentChannel();
                if (currentChannel != null) {
                    i2 = currentChannel.mapindex;
                }
                KtcTvUI.getInstance().getATVUI().updateAtvChannelSearchDialog(i2, i, f, ATVLogic.this.getfrequencyband((int) f), SEARCH_TYPE.FREQUENCY_ADJUST);
                return this.RET;
            }

            public byte[] onATVFrequencyTrimStart() {
                KtcTVDebug.debug(">>>>>>>>>onATVFrequencyTrimStart");
                BroadcastKeyManager.getInstance().disableTvSearchKeys();
                int i = 0;
                float frequencyPoint = ExternalApi.getInstance().atvApi.getFrequencyPoint();
                Channel currentChannel = ExternalApi.getInstance().systemApi.getCurrentChannel();
                if (currentChannel != null) {
                    i = currentChannel.mapindex;
                }
                KtcTvUI.getInstance().getATVUI().showAtvFrequencyFineDialog(i, ATVLogic.this.getProgress((int) frequencyPoint), frequencyPoint, ATVLogic.this.getfrequencyband((int) frequencyPoint));
                return this.RET;
            }

            public byte[] onATVFrequencyTrimStart(int i, float f) {
                KtcTVDebug.debug(">>>>>>>>>onATVFrequencyTrimStart freq:" + f);
                BroadcastKeyManager.getInstance().disableTvSearchKeys();
                int i2 = 0;
                Channel currentChannel = ExternalApi.getInstance().systemApi.getCurrentChannel();
                if (currentChannel != null) {
                    i2 = currentChannel.mapindex;
                }
                KtcTvUI.getInstance().getATVUI().showAtvFrequencyFineDialog(i2, ATVLogic.this.getProgress((int) f), f, ATVLogic.this.getfrequencyband((int) f));
                return this.RET;
            }
        };
        *//*
        this.soundSystemValues = null;
        this.colorSystemValues = null;
        this.atvShowSearchMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.ATV_SHOW_SEARCH_MENU.toString()) {
            public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
                Log.d("Maxs","-------------------------------ATVLogic:atvShowSearcMenu:-------------------------------");

                SelectMenuData selectMenuData = new SelectMenuData();
                selectMenuData.channelType = CHANNEL.ATV;
                selectMenuData.addData(new SelectMenuItem(ATVLogic.this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_AUTO_SEARCH), SELECTMENU_CMD.ATV_AUTO_SEARCH.toString(), new TCNoneSetData()));
                selectMenuData.addData(new SelectMenuItem(ATVLogic.this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_MANUAL_SEARCH), SELECTMENU_CMD.ATV_MANUAL_SEARCH.toString(), new TCNoneSetData()));
                selectMenuData.addData(new SelectMenuItem(ATVLogic.this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_FREQ_FINE), SELECTMENU_CMD.ATV_FREQ_FINE.toString(), new TCNoneSetData()));
                selectMenuData.addData(new SelectMenuItem(ATVLogic.this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_COLOR_SYSTEM), SELECTMENU_CMD.ATV_COLORSYSTEM.toString(), UITextUtil.getInstance().forTCEnumSetData(ATVLogic.this.atvMenuGetColorSystem())));
                selectMenuData.addData(new SelectMenuItem(ATVLogic.this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_SOUND_SYSTEM), SELECTMENU_CMD.ATV_SOUNDSYSTEM.toString(), UITextUtil.getInstance().forTCEnumSetData(ATVLogic.this.atvMenuGetSoundSystem())));
                if (KtcTVPopupUIManager.getInstance() != null) {
                    KtcTVPopupUIManager.getInstance().show(selectMenuData);
                }
                return new TCRetData(true);
            }
        };
        this.atvDeleteChannelMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.ATV_CHANNELDELET_MENU.toString()) {
            public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
                return new TCRetData(ATVLogic.this.showDeleteChannelDialog());
            }
        };
        this.isAtvAutoStopSearching = false;

    }
*/
    public static ATVLogic getInstance() {
        if (instance == null) {
            instance = new ATVLogic();
        }
        return instance;
    }
/*
    private int getProgress(int i) {
        if (i < 45200) {
            i = 45200;
        } else if (i > 866200) {
            i = 866200;
        }
        return ((i - 45200) * 100) / 821000;
    }

    private String getfrequencyband(int i) {
        return i < 140250 ? "VHF-L" : i < 428250 ? "VHF-H" : "UHF";
    }*/

    ///private void showSelectView() {
        /*TvUIData selectMenuData = new SelectMenuData();
        selectMenuData.channelType = CHANNEL.ATV;
        selectMenuData.addData(new SelectMenuItem(this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_AUTO_SEARCH), SELECTMENU_CMD.ATV_AUTO_SEARCH.toString(), new TCNoneSetData()));
        selectMenuData.addData(new SelectMenuItem(this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_MANUAL_SEARCH), SELECTMENU_CMD.ATV_MANUAL_SEARCH.toString(), new TCNoneSetData()));
        selectMenuData.addData(new SelectMenuItem(this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_FREQ_FINE), SELECTMENU_CMD.ATV_FREQ_FINE.toString(), new TCNoneSetData()));
        selectMenuData.addData(new SelectMenuItem(this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_COLOR_SYSTEM), SELECTMENU_CMD.ATV_COLORSYSTEM.toString(), UITextUtil.getInstance().forTCEnumSetData(atvMenuGetColorSystem())));
        selectMenuData.addData(new SelectMenuItem(this.mContext.getResources().getString(R.string.SKY_CFG_TV_ATV_SOUND_SYSTEM), SELECTMENU_CMD.ATV_SOUNDSYSTEM.toString(), UITextUtil.getInstance().forTCEnumSetData(atvMenuGetSoundSystem())));
        if (KtcTVPopupUIManager.getInstance() != null) {
            KtcTVPopupUIManager.getInstance().show(selectMenuData);
        }*/
   //// }
/*
    private void startAtvLogicTimer() {
        if (this.atvLogicTimer != null) {
            try {
                this.atvLogicTimer.cancel();
            } catch (Exception e) {
            }
        }
        this.atvLogicTimer = new Timer();
        this.atvLogicTimer.schedule(new HideDisplayTimerTask(), 1000);
    }
*/
    public boolean atvExitSearch(String str, boolean z) {
        /*Boolean valueOf = Boolean.valueOf(false);
        if (str != null) {
            this.exitType = str;
        }
        ////KtcTVDebug.debug("exitType:" + this.exitType);
        if (this.exitType != null) {
            if (z) {
                if (this.exitType.equals(SEARCH_TYPE.AUTO_SEARCH.toString())) {
                    if (this.isAtvAutoStopSearching) {
                        ////KtcTVDebug.debug("isAtvAutoStopSearching:" + this.isAtvAutoStopSearching);
                        return true;
                    }
                    this.isAtvAutoStopSearching = true;
                }
                KtcTvLogicAsyncTask.getInstance().runAsyncTask(new IAsyncTask() {
                    public void doAsyncTask(Object obj) {
                        String str = (String) obj;
                        if (str.equals(SEARCH_TYPE.AUTO_SEARCH.toString())) {
                            ExternalApi.getInstance().atvApi.stopATVAutoSearch();
                        } else if (str.equals(SEARCH_TYPE.MANUAL_SEARCH.toString())) {
                            ExternalApi.getInstance().atvApi.stopATVManualSearch();
                        } else if (str.equals(SEARCH_TYPE.FREQUENCY_ADJUST.toString())) {
                            ExternalApi.getInstance().atvApi.stopATVFrequencyTrim();
                        }
                    }
                }, this.exitType);
            } else if (this.exitType.equals(SEARCH_TYPE.AUTO_SEARCH.toString())) {
                valueOf = Boolean.valueOf(ExternalApi.getInstance().atvApi.stopATVAutoSearch());
            } else if (this.exitType.equals(SEARCH_TYPE.MANUAL_SEARCH.toString())) {
                valueOf = Boolean.valueOf(ExternalApi.getInstance().atvApi.stopATVManualSearch());
            } else if (this.exitType.equals(SEARCH_TYPE.FREQUENCY_ADJUST.toString())) {
                valueOf = Boolean.valueOf(ExternalApi.getInstance().atvApi.stopATVFrequencyTrim());
            }
        }
        return valueOf.booleanValue();*/return false;
    }

    public TCSwitchSetData atvMenuGetChannelSkip() {
        /*Channel currentChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
        boolean z = false;
        if (currentChannel != null) {
            z = currentChannel.bSkip;
        }
        TCSwitchSetData tCSwitchSetData = new TCSwitchSetData();
        tCSwitchSetData.setOn(z);
        return tCSwitchSetData;*/return  null;
    }

    public TCEnumSetData atvMenuGetColorSystem() {
        /*if (this.colorSystemValues == null) {
            Iterable<COLORSYSTEM> colorSystemValues = ExternalApi.getInstance().atvApi.getColorSystemValues();
            this.colorSystemValues = new ArrayList();
            for (COLORSYSTEM colorsystem : colorSystemValues) {
                this.colorSystemValues.add(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + colorsystem.toString());
                KtcTVDebug.debug("KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + color.toString():TV_STRING_" + colorsystem.toString());
            }
        }
        String str = KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + ExternalApi.getInstance().atvApi.getColorSystem().toString();
        KtcTVDebug.debug("currentValue:" + str);
        TCEnumSetData tCEnumSetData = new TCEnumSetData();
        tCEnumSetData.setEnumList(this.colorSystemValues);
        tCEnumSetData.setEnumCount(this.colorSystemValues.size());
        tCEnumSetData.setCurrent(str);
        return tCEnumSetData;*/return null;
    }

    public TCEnumSetData atvMenuGetSoundSystem() {
        /*if (this.soundSystemValues == null) {
            Iterable<SOUNDSYSTEM> soundSystemValues = ExternalApi.getInstance().atvApi.getSoundSystemValues();
            this.soundSystemValues = new ArrayList();
            for (SOUNDSYSTEM soundsystem : soundSystemValues) {
                this.soundSystemValues.add(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + soundsystem.toString());
            }
        }
        String str = KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + ExternalApi.getInstance().atvApi.getSoundSystem().toString();
        TCEnumSetData tCEnumSetData = new TCEnumSetData();
        tCEnumSetData.setEnumList(this.soundSystemValues);
        tCEnumSetData.setEnumCount(this.soundSystemValues.size());
        tCEnumSetData.setCurrent(str);
        return tCEnumSetData;*/return null;
    }

    public TCRangeSetData atvMenuGetVolumeCompensation() {
        /*int volumeCompensation = ExternalApi.getInstance().atvApi.getVolumeCompensation();
        TCRangeSetData tCRangeSetData = new TCRangeSetData();
        tCRangeSetData.setMin(-10);
        tCRangeSetData.setCurrent(volumeCompensation);
        tCRangeSetData.setMax(10);
        return tCRangeSetData;*/return null;
    }

    public boolean atvMenuSetChannelSkip(int i) {
        /*Channel currentChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
        boolean z = i == 1;
        boolean skipChannel = ExternalApi.getInstance().atvApi.setSkipChannel(new ATVApiParamsSetSkipChannel(currentChannel, z));
        for (Channel channel : EPGLoader.getInstance().getChannelList(Source.ATV())) {
            if (channel.equals(currentChannel)) {
                channel.bSkip = z;
                break;
            }
        }
        SwitchChannelManagerL.getInstance().getCurrentChannel().bSkip = z;
        return skipChannel;*/return false;
    }

    public boolean atvMenuSetColorSystem(int i) {
        /*return ExternalApi.getInstance().atvApi.setColorSystem(COLORSYSTEM.valueOf(((String) this.colorSystemValues.get(i)).substring(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD.length())));*/return false;
    }

    public boolean atvMenuSetSoundSystem(int i) {
        /*boolean soundSystem = ExternalApi.getInstance().atvApi.setSoundSystem(SOUNDSYSTEM.valueOf(((String) this.soundSystemValues.get(i)).substring(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD.length())));
        TCSetData setData = ExternalApi.getInstance().systemServiceApi.getSetData(KtcConfigDefs.KTC_CFG_TV_AUDIO_MUTE);
        if (setData != null) {
            boolean isOn = ((TCSwitchSetData) setData).isOn();
            KtcTVDebug.debug("langlang isMute = " + isOn);
            if (isOn) {
                setData = new TCSwitchSetData();
                setData.setName(KtcConfigDefs.KTC_CFG_TV_AUDIO_MUTE);
                setData.setOn(false);
                ExternalApi.getInstance().systemServiceApi.setData(setData);
            }
        }
        return soundSystem;*/return false;
    }

    public boolean atvMenuSetVolumeCompensation(int i) {
        /*return ExternalApi.getInstance().atvApi.setVolumeCompensation(i);*/return false;
    }

    public void backToATVSelectView() {
        /*KtcLogger.d("lwr", "ATV_backToSelectView: " + KtcTvConfig.getInstance().isStandByFlag);
        KtcTvConfig.getInstance().isFirstRefresh = true;
        if (!KtcTvConfig.getInstance().isStandByFlag) {
            showSelectView();
        }*/
    }

    public TCEnumSetData getATVNicamType() {
        /*KtcTVDebug.debug("!@# ATV_MENU_GET_AUDIONIACM");
        List atvAudioNicam = ExternalApi.getInstance().atvApi.getAtvAudioNicam();
        this.atvNicamValues = new ArrayList();
        if (atvAudioNicam.size() == 0) {
            KtcTVDebug.debug("langlang get atvNicam size = 0");
            this.atvNicamValues.add(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + ATVAUDIONICAM.ATV_AUDIOMODE_NICAM_MONO.toString());
        }
        for (int i = 0; i < atvAudioNicam.size(); i++) {
            KtcTVDebug.debug("langlang atvNice: " + ((ATVAUDIONICAM) atvAudioNicam.get(i)).toString());
            this.atvNicamValues.add(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + ((ATVAUDIONICAM) atvAudioNicam.get(i)).toString());
        }
        String str = KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD + ExternalApi.getInstance().atvApi.getAtvCurAudioNicam().toString();
        TCEnumSetData tCEnumSetData = new TCEnumSetData();
        tCEnumSetData.setCurrent(str);
        tCEnumSetData.setEnumList(this.atvNicamValues);
        tCEnumSetData.setEnumCount(this.atvNicamValues.size());
        return tCEnumSetData;*/return null;
    }

    public void init(Context context, KtcCmdConnectorListener ktcCmdConnectorListener) {
        super.init(context, ktcCmdConnectorListener);
        /*this.isAtvAutoStopSearching = false;
        SearchBackView.getInstance().setATVViewBackListner(this);*/
    }

    public boolean processFreqFine(int i) {
        /*
        KtcTVDebug.debug("processManualSearch forwardIndex:" + i);
        return ExternalApi.getInstance().atvApi.processATVFrequencyTrim(i != 0, 12.5f);
        */return false;
    }

    public boolean processManualSearch(int i) {
        /*
        KtcTVDebug.debug("processManualSearch forwardIndex:" + i);
        return ExternalApi.getInstance().atvApi.processATVManualSearch(i != 0, 12.5f);
        */return false;
    }

    public boolean setAtvNicamType(int i) {
        /*
        ATVAUDIONICAM valueOf = ATVAUDIONICAM.valueOf(((String) this.atvNicamValues.get(i)).substring(KtcUILogicTvCommand.TV_TEXT_RESOURCE_HEAD.length()));
        KtcTVDebug.debug("langlang set atvNicem = " + valueOf.toString());
        return Boolean.valueOf(ExternalApi.getInstance().atvApi.setAtvAudioNicam(valueOf)).booleanValue();
        */return false;
    }

    public boolean showDeleteChannelDialog() {
        /*
        Channel currentChannel = KtcEPGApi.getInstance().getCurrentChannel();
        if (currentChannel == null || currentChannel.invalid || currentChannel.bSkip) {
            KtcLogger.d("lwr", ">>>>Not showATVDeleteChannelDialog:");
            KtcTvApp.getInstance().getKtcTVTosatView().showTVToastView(R.string.nochannelinfo);
        } else {
            List dBTableSortList = ExternalApi.getInstance().epgApi.getDBTableSortList(KtcTvController.getInstance().getCurrentSource());
            if (dBTableSortList != null) {
                KtcLogger.d("lwr", ">>>>dbSortChannelList = " + dBTableSortList.size());
                if (dBTableSortList.size() == 1) {
                    KtcTvApp.getInstance().getKtcTVTosatView().showTVToastView(2131230742);
                } else {
                    ChannelEditApiParamsInfo channelEditApiParamsInfo = new ChannelEditApiParamsInfo(currentChannel.id, "模拟电视 " + currentChannel.displayName, currentChannel.type.toString(), currentChannel.mapindex);
                    Source currentSource = TvLogicManager.getInstance().getCurrentSource();
                    KtcLogger.d("lwr", ">>>>showATVDeleteChannelDialog:" + channelEditApiParamsInfo + " currentSource: " + currentSource);
                    KtcTVDeleteChannelUtils.getInstance().initCurSource(currentSource);
                    SystemUI.getInstance().showChannelDeleteDialog(channelEditApiParamsInfo);
                }
            }
        }
        return false;
        */return false;
    }

    public boolean startAutoSearch() {
        /*
        ScreenDisplayManager.getInstance().hideScreenDisplay();
        NoSignalManager.getInstance().disableScreenSaverFunc();
        TvLogicUtils.set3DmodeOff();
        return ExternalApi.getInstance().atvApi.startATVAutoSearch(this.autoSearchListener);
        */return false;
    }

    public boolean startFreqFine() {
        /*
        NoSignalManager.getInstance().disableScreenSaverFunc();
        TvLogicUtils.set3DmodeOff();
        return ExternalApi.getInstance().atvApi.startATVFrequencyTrim(this.frequencyTrimListener);
        */return false;
    }

    public boolean startManualSearch() {
        /*
        ScreenDisplayManager.getInstance().hideScreenDisplay();
        NoSignalManager.getInstance().disableScreenSaverFunc();
        TvLogicUtils.set3DmodeOff();
        return ExternalApi.getInstance().atvApi.startATVManualSearch(this.manualSearchListener);
        */return false;
    }

    public boolean stopAutoSearch() {
        /*
        return ExternalApi.getInstance().atvApi.stopATVAutoSearch();
        */return false;
    }

    public boolean stopFreqFine() {
        /*
        return ExternalApi.getInstance().atvApi.stopATVFrequencyTrim();
        */return false;
    }

    public boolean stopManualSearch() {
        /*
        return ExternalApi.getInstance().atvApi.stopATVManualSearch();
        */return false;
    }
}

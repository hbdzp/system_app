package com.horion.tv.logic.system;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
////import com.ktc.framework.ktccommondefine.KtcBroadcast;
////import com.ktc.framework.ktccommondefine.KtcworthBroadcastKey;
import com.horion.tv.KtcTvApp;
import com.horion.tv.framework.ui.TvIntent;
import com.horion.tv.service.epg.SkyLeftEPGDataGet;
import com.horion.tv.ui.MainMenuActivity;
import com.horion.tv.R;
import com.horion.tv.ui.leftepg.SkyEPGData;
import com.horion.tv.widget.PanasonicToast;
import com.ktc.util.KtcTVTosatView;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvPictureManager;
import com.ktc.framework.ktcsdk.ipc.KtcApplication.KtcCmdConnectorListener;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
////import com.ktc.framework.ktcsdk.screensaver.ScreenSaverMgr;
import com.horion.system.data.TCEnumSetData;
import com.horion.system.data.TCRetData;
import com.horion.system.data.TCSetData;
////import com.horion.system.define.TCEnvKey;
////import com.horion.tv.api.system.KtcTvSystemListener;
////import com.horion.tv.define.KtcTvConfigDefs.KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE;
import com.horion.tv.define.KtcTvDefine.SOURCE_SIGNAL_STATE;
////import com.horion.tv.define.object.Channel;
import com.horion.tv.define.object.Source;
import com.horion.tv.define.uilogic.UILOGIC_TVMENU_COMMAND;
////import com.horion.tv.define.uilogic.params.TVOnKeydownParams;
import com.horion.tv.framework.logicapi.framework.SystemLogicApi;
import com.horion.tv.framework.logicapi.framework.TvLogicGetFunction;
import com.horion.tv.framework.logicapi.framework.TvLogicSetFunction;
import com.horion.tv.framework.logicapi.framework.UiCmdParams;
////import com.horion.tv.framework.ui.uidata.atv.ATVChannelSearchData.SEARCH_TYPE;
////import com.horion.tv.framework.ui.uidata.dtv.DTVChannelSearchUIData;
////import com.horion.tv.framework.ui.uidata.epg.ChannelCollectManager;
////import com.horion.tv.logic.channeledit.KtcTVHaveDeleteChannelUtils;
////import com.horion.tv.logic.epg.EPGChannelSearcher;
import com.horion.tv.logic.framework.TvLogic;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;
import com.mstar.android.tvapi.dtv.vo.DtvSubtitleInfo;
import com.mstar.android.tvapi.dtv.vo.MenuSubtitleService;
////import com.horion.tv.logic.utils.BroadcastKeyManager;
////import com.horion.tv.logic.utils.EPGLoader;
////import com.horion.tv.logic.utils.ScreenDisplayManager;
////import com.horion.tv.logic.utils.SwitchChannelManagerL;
////import com.horion.tv.logic.utils.TCBCKeysControler;
////import com.horion.tv.service.base.KtcTvController;
////import com.horion.tv.service.dtv.DtvDigitalUiManager;
////import com.horion.tv.service.epg.KtcLeftEPGDataGet;
////import com.horion.tv.ui.forlogic.KtcTvUI;
////import com.horion.tv.utils.UITextUtilInterface.UITextUtil;
////import com.horion.tv.util.KtcTVDialogType;
////import com.horion.tv.utils.KtcTVDebug;
////import com.horion.tv.utils.KtcTvAsyncTask;
////import com.horion.tv.utils.KtcTvAsyncTask.IAsyncTask;
////import com.horion.tv.utils.KtcTvConfig;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SystemLogic extends TvLogic implements SystemLogicApi {
    private static final int TV_LOGIC_ON_KEY_DOWN_TAG = 1390;
    private static SOURCE_SIGNAL_STATE currentSourceSignalState = SOURCE_SIGNAL_STATE.PLAY;
    private static SystemLogic instance = null;
    public static boolean launcherHasFocus = true;
    private final boolean ISAUTOSWITCH = true;
    private final Source[] canChannelUpDownSource = new Source[]{Source.ATV(), Source.DVBC(), Source.DTMB(), Source.IPLive()};
    /*private TvLogicSetFunction channelSort = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_CHANNEL_SORT.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Intent intent = new Intent(TvIntent.CHANNEL_LIST);
            intent.putExtra("viewtype", "channelEditSort");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            return new TCRetData(true);
        }
    };*/
    private HashMap<String, List<String>> displayModeCacheMap = null;
    private TCEnumSetData displayModeData = null;
    private TvLogicGetFunction getCollectChannel = new TvLogicGetFunction(UILOGIC_TVMENU_COMMAND.TV_MENU_GET_COLLECT_CHANNEL.toString()) {
        public TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams) {
            return SystemLogic.this.getCollectChannel();
        }
    };
    private TvLogicGetFunction getDisplayMode = new TvLogicGetFunction(UILOGIC_TVMENU_COMMAND.TV_MENU_GET_DISPLAY_MODE.toString()) {
        public TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams) {
            return SystemLogic.this.tvMenuGetDisplayMode();
        }
    };
    private TvLogicGetFunction getPicturMode = new TvLogicGetFunction(UILOGIC_TVMENU_COMMAND.TV_MENU_GET_PICTURE_MODE.toString()) {
        public TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams) {
            return SystemLogic.this.tvMenuGetPictureMode();
        }
    };
    private TvLogicGetFunction getSoundMode = new TvLogicGetFunction(UILOGIC_TVMENU_COMMAND.TV_MENU_GET_SOUND_MODE.toString()) {
        public TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams) {
            return SystemLogic.this.tvMenuGetSoundMode();
        }
    };

    private TvLogicGetFunction getSoundChannel = new TvLogicGetFunction(UILOGIC_TVMENU_COMMAND.DTV_MENU_GET_SOUNDCHANNEL.toString()) {
        public TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams) {
            return SystemLogic.this.tvMenuGetSoundChannel();
        }
    };

    private TvLogicGetFunction getSubtitleLanguage = new TvLogicGetFunction(UILOGIC_TVMENU_COMMAND.DTMB_MENU_GET_SUBTITLE.toString()) {
        public TCSetData uiGetCmd(String str, UiCmdParams uiCmdParams) {
            return SystemLogic.this.tvMenuGetSubtitle();
        }
    };

    private boolean isSearchTvChannel = false;
    private int mCollectStatus = 0;
    private TCEnumSetData pictureModeData = null;
    private TvLogicSetFunction setCollectChannel = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_MENU_SET_COLLECT_CHANNEL.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Log.d("Maxs","SystemLogic:TV_MENU_SET_COLLECT_CHANNEL");
            ProgramInfo currentProgInfo = TvChannelManager.getInstance().getCurrentProgramInfo();
            if (currentProgInfo != null && !"".equals(currentProgInfo.serviceName)){
                short bfav = currentProgInfo.favorite;

                Log.d("Maxs","currentProgInfo.favorite = " + currentProgInfo.favorite);
                if (bfav == 1){
                    TvChannelManager.getInstance().deleteProgramFromFavorite(
                            TvChannelManager.PROGRAM_FAVORITE_ID_1,
                            currentProgInfo.number, currentProgInfo.serviceType, 0x00);
                    KtcTVTosatView ktcTVTosatView = new KtcTVTosatView(mContext);
                    ktcTVTosatView.showTVToastView(R.string.USER_UNCOLLECT_SUCCESS);
                }else {
                    TvChannelManager.getInstance().addProgramToFavorite(
                            TvChannelManager.PROGRAM_FAVORITE_ID_1,
                            currentProgInfo.number, currentProgInfo.serviceType, 0x00);
                    TvChannelManager.getInstance().setProgramAttribute(
                            TvChannelManager.PROGRAM_ATTRIBUTE_SKIP,
                            currentProgInfo.number, currentProgInfo.serviceType, 0x00,
                            false);
                    KtcTVTosatView ktcTVTosatView = new KtcTVTosatView(mContext);
                    ktcTVTosatView.showTVToastView(R.string.USER_COLLECT_SUCCESS);
                }

            }else{
                KtcTVTosatView ktcTVTosatView = new KtcTVTosatView(mContext);
                ktcTVTosatView.showTVToastView(R.string.nochannelinfo);
            }
            return null;
        }
    };
    private TvLogicSetFunction setDisplayMode = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_MENU_SET_DISPLAY_MODE.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            return new TCRetData(SystemLogic.this.tvMenuSetDisplayMode(uiCmdParams.selectIndex));
        }
    };
    private TvLogicSetFunction setIntelligentMode = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_MENU_SET_INTELLIGENT_MODE.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            ////KtcTVDebug.debug("~~~~~~~~~~~~~getIntelligentMode~~~~~~~");
            ////Source currentSource = KtcTvController.getInstance().getCurrentSource();
            ////if (currentSource != null) {
               //// KtcTvApp.getInstance().getStbSettingApi().startStbSmartRcSetting(MainMenuActivity.getInstance(), currentSource.getSourceNameEnum().toString() + currentSource.index);
            ////}
            return null;
        }
    };
    private TvLogicSetFunction setPicturMode = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_MENU_SET_PICTURE_MODE.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            return new TCRetData(SystemLogic.this.tvMenuSetPictureMode(uiCmdParams.selectIndex));
        }
    };
    private TvLogicSetFunction setSoundChannel= new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.DTV_MENU_SET_SOUNDCHANNEL.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            boolean tvMenuSetSoundMode = SystemLogic.this.tvMenuSetSoundChannel(uiCmdParams.selectIndex);
            return null;
        }
    };

    private TvLogicSetFunction setSubtitleLanguage= new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.DTMB_MENU_SET_SUBTITLE.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            boolean tvMenuSetSoundMode = SystemLogic.this.tvMenuSetSubtitle(uiCmdParams.selectIndex);
            return null;
        }
    };

    private TvLogicSetFunction setSoundMode = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_MENU_SET_SOUND_MODE.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            boolean tvMenuSetSoundMode = SystemLogic.this.tvMenuSetSoundMode(uiCmdParams.selectIndex);
            return null;
        }
    };
    private TvLogicSetFunction show3DMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_SHOW_3D_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            Intent intent = new Intent("android.settings.THREED_SETTINGS");
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            SystemLogic.this.mContext.startActivity(intent);
            return new TCRetData(true);
        }
    };
    private TvLogicSetFunction show4KImageDemo = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_SHOW_4K_IMAGE_DEMO.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            SystemLogic.this.showQuckDemo("KTC_CFG_TV_4K_IMAGE_ENHANCEMENT_PROCESSING_ENGINE_DEMO");
            return new TCRetData(true);
        }
    };
    private TvLogicSetFunction showDemo = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_SHOW_DEMO.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            SystemLogic.this.showQuckDemo();
            return new TCRetData(true);
        }
    };
    private TvLogicSetFunction showDimmingDemo = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_SHOW_LOCAL_DIMMING_DEMO.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            SystemLogic.this.showQuckDemo("KTC_CFG_TV_LOCAL_DIMMING_MODE_DEMO");
            return new TCRetData(true);
        }
    };
    private TvLogicSetFunction showPanelColorDemo = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_SHOW_PANEL_COLOR_DEMO.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            SystemLogic.this.showQuckDemo("KTC_CFG_TV_INFORMATION_GET_PANEL_COLOR_DEPTH_DEMO_DEMO");
            return new TCRetData(true);
        }
    };
    private TCEnumSetData soundModeData = null;
   //// private Channel switchChannel = null;
    private Boolean switchChannelLock = Boolean.valueOf(true);
   //// public KtcTvSystemListener systemListener = new KtcTvSystemListener() {
       /// private static /* synthetic */ int[] -com_tianci_tv_define_KtcTvDefine$AUDIOSTREAM_TYPESwitchesValues;
     ////   private static /* synthetic */ int[] -com_tianci_tv_define_KtcTvDefine$SOURCE_SIGNAL_STATESwitchesValues;
      ////  private static /* synthetic */ int[] -com_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues;
       //// final /* synthetic */ int[] $SWITCH_TABLE$com$horion$tv$define$KtcTvDefine$AUDIOSTREAM_TYPE;
      ////  final /* synthetic */ int[] $SWITCH_TABLE$com$horion$tv$define$KtcTvDefine$SOURCE_SIGNAL_STATE;
     ////   final /* synthetic */ int[] $SWITCH_TABLE$com$horion$tv$define$object$Source$SOURCE_NAME_ENUM;

       //// private static /* synthetic */ int[] -getcom_tianci_tv_define_KtcTvDefine$AUDIOSTREAM_TYPESwitchesValues() {
       ////     if (-com_tianci_tv_define_KtcTvDefine$AUDIOSTREAM_TYPESwitchesValues != null) {
      ////          return -com_tianci_tv_define_KtcTvDefine$AUDIOSTREAM_TYPESwitchesValues;
      ////      }
      /*      int[] iArr = new int[AUDIOSTREAM_TYPE.values().length];
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_AAC.ordinal()] = 11;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_AAC_PLUS.ordinal()] = 12;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_AC3.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_AC3_PLUS.ordinal()] = 2;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_AVS.ordinal()] = 13;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_COOK.ordinal()] = 14;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA.ordinal()] = 15;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA10.ordinal()] = 16;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA11.ordinal()] = 17;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA20.ordinal()] = 18;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA21.ordinal()] = 19;
            } catch (NoSuchFieldError e11) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA30.ordinal()] = 20;
            } catch (NoSuchFieldError e12) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA31.ordinal()] = 21;
            } catch (NoSuchFieldError e13) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA40.ordinal()] = 22;
            } catch (NoSuchFieldError e14) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA41.ordinal()] = 23;
            } catch (NoSuchFieldError e15) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA50.ordinal()] = 24;
            } catch (NoSuchFieldError e16) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DRA51.ordinal()] = 25;
            } catch (NoSuchFieldError e17) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_DTS.ordinal()] = 26;
            } catch (NoSuchFieldError e18) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_INVALID.ordinal()] = 27;
            } catch (NoSuchFieldError e19) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_LPCM_BLURAY.ordinal()] = 28;
            } catch (NoSuchFieldError e20) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_MP3.ordinal()] = 29;
            } catch (NoSuchFieldError e21) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_MPEG.ordinal()] = 30;
            } catch (NoSuchFieldError e22) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_MPEG1.ordinal()] = 31;
            } catch (NoSuchFieldError e23) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_PCM.ordinal()] = 32;
            } catch (NoSuchFieldError e24) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_UNKOWN.ordinal()] = 33;
            } catch (NoSuchFieldError e25) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_WAVE.ordinal()] = 34;
            } catch (NoSuchFieldError e26) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_WMA_PRO.ordinal()] = 35;
            } catch (NoSuchFieldError e27) {
            }
            try {
                iArr[AUDIOSTREAM_TYPE.AV_AUD_STREAM_TYPE_WMA_STD.ordinal()] = 36;
            } catch (NoSuchFieldError e28) {
            }
            -com_tianci_tv_define_KtcTvDefine$AUDIOSTREAM_TYPESwitchesValues = iArr;
            return iArr;
        }*/

        /////private static /* synthetic */ int[] -getcom_tianci_tv_define_KtcTvDefine$SOURCE_SIGNAL_STATESwitchesValues() {
        /*    if (-com_tianci_tv_define_KtcTvDefine$SOURCE_SIGNAL_STATESwitchesValues != null) {
                return -com_tianci_tv_define_KtcTvDefine$SOURCE_SIGNAL_STATESwitchesValues;
            }
            int[] iArr = new int[SOURCE_SIGNAL_STATE.values().length];
            try {
                iArr[SOURCE_SIGNAL_STATE.CURRENTCHANNEL_NO_PLAY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SOURCE_SIGNAL_STATE.NET.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SOURCE_SIGNAL_STATE.NOEPG.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[SOURCE_SIGNAL_STATE.NONET.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[SOURCE_SIGNAL_STATE.NOSIGNAL.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[SOURCE_SIGNAL_STATE.PLAY.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            -com_tianci_tv_define_KtcTvDefine$SOURCE_SIGNAL_STATESwitchesValues = iArr;
            return iArr;
        }
*/
       //// private static /* synthetic */ int[] -getcom_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues() {
           /* if (-com_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues != null) {
                return -com_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues;
            }
            int[] iArr = new int[SOURCE_NAME_ENUM.values().length];
            try {
                iArr[SOURCE_NAME_ENUM.ATV.ordinal()] = 11;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.AV.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.DTMB.ordinal()] = 12;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.DVBC.ordinal()] = 13;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.EXTERNAL.ordinal()] = 14;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.HDMI.ordinal()] = 2;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.IPLIVE.ordinal()] = 15;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.REMEMBER.ordinal()] = 16;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.VGA.ordinal()] = 17;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.VOD.ordinal()] = 18;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[SOURCE_NAME_ENUM.YUV.ordinal()] = 19;
            } catch (NoSuchFieldError e11) {
            }
            -com_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues = iArr;
            return iArr;
        }

        public byte[] onKtcTvReleased(Source source) {
            KtcTVDebug.debug("onKtcTvReleased:");
            NoSignalManager.getInstance().disableScreenSaverFunc();
            ScreenDisplayManager.getInstance().hideScreenDisplay();
            if (!KtcTvController.getInstance().isKtcLauncherExist) {
                return Boolean.TRUE.toString().getBytes();
            }
            KtcTvUI.getInstance().getATVUI().hideAtvChannelSearchDialog(SEARCH_TYPE.AUTO_SEARCH);
            KtcTvUI.getInstance().getDVBCUI().hideSearchDialog(DTVChannelSearchUIData.SEARCH_TYPE.DVBC_AUTO_SEARCH);
            KtcTvUI.getInstance().getDVBCUI().hideSearchDialog(DTVChannelSearchUIData.SEARCH_TYPE.DVBC_ALL_CHANNEL_SEARCH);
            KtcTvUI.getInstance().getDTMBUI().hideSearchDialog(DTVChannelSearchUIData.SEARCH_TYPE.DTMB_AUTO_SEARCH);
            KtcTvUI.getInstance().getDTMBUI().hideSearchDialog(DTVChannelSearchUIData.SEARCH_TYPE.DTMB_MANUAL_SEARCH);
            BroadcastKeyManager.getInstance().enableTvSearchKeys();
            return Boolean.TRUE.toString().getBytes();
        }*/

      /*  public byte[] onKtcTvRestart(Source source) {
            ////KtcTVDebug.debug("onKtcTvRestart:" + source.getDisplayName());
            Channel currentChannel = ExternalApi.getInstance().systemApi.getCurrentChannel();
            ScreenDisplayManager.getInstance().showScreenDisplay(source, currentChannel, false, true, false, false);
            SwitchChannelManagerL.getInstance().setCurrentChannel(currentChannel);
            return Boolean.TRUE.toString().getBytes();
        }*/

        public byte[] onKtcTvServiceStart(Source source) {
            ////KtcTVDebug.debug("onKtcTvServiceStart " + source.getDisplayName());
            return Boolean.TRUE.toString().getBytes();
        }

       /* public byte[] onKtcTvSignalChanged(Source source, SOURCE_SIGNAL_STATE source_signal_state) {
            if (source == null) {
                return Boolean.TRUE.toString().getBytes();
            }
            Source currentSource = TvLogicManager.getInstance().getCurrentSource();
            if (currentSource == null) {
                return Boolean.TRUE.toString().getBytes();
            }
            KtcTVDebug.debug("onKtcTvSignalChanged in SystemLogic source.id:" + source.id + "  signalState:" + source_signal_state + "  currentSource.id:" + currentSource.id + "  currentTimeMillis:" + System.currentTimeMillis());
            if (NoSignalManager.getInstance().getAllowScreenSaverFromLauncher()) {
                NoSignalManager.getInstance().allowScreenSaverFunc();
                if (source.equals(currentSource)) {
                    NoSignalManager.getInstance().currentSourceSignalState = source_signal_state;
                    switch (AnonymousClass1.-getcom_tianci_tv_define_KtcTvDefine$SOURCE_SIGNAL_STATESwitchesValues()[source_signal_state.ordinal()]) {
                        case 1:
                            NoSignalManager.getInstance().showNosignalMenu(source);
                            break;
                        case 2:
                        case 6:
                            if (NoSignalManager.getInstance().checkNeedShowScreensaverWhenPlay(currentSource)) {
                                NoSignalManager.getInstance().showNosignalMenu(source);
                            } else {
                                NoSignalManager.getInstance().hideSignalState(true);
                            }
                            NoSignalManager.getInstance().hideCountdown();
                            NoSignalManager.getInstance().setScreenSaverApkStarted(false);
                            ScreenSaverMgr.getInstance().exitScreenSaver();
                            ScreenDisplayManager.getInstance().checkUpdateScreenDisplay(source, source_signal_state);
                            if (!KtcTvController.getInstance().isDolbyAudioSupported()) {
                                if (!KtcTvController.getInstance().isApkSource(currentSource)) {
                                    AUDIOSTREAM_TYPE audioStreamType = KtcTvController.getInstance().getAudioStreamType();
                                    KtcTVDebug.debug("onKtcTvSignalChanged in SystemLogic audioStreamType:" + audioStreamType);
                                    if (audioStreamType != null) {
                                        switch (AnonymousClass1.-getcom_tianci_tv_define_KtcTvDefine$AUDIOSTREAM_TYPESwitchesValues()[audioStreamType.ordinal()]) {
                                            case 1:
                                            case 2:
                                                String string = SystemLogic.this.mContext.getResources().getString(R.string.channelAudioFormatNotSupported);
                                                KtcTVDebug.debug("notSupportedAudio");
                                                KtcTvUI.getInstance().getSystemUI().showToast(string);
                                                KtcTvUI.getInstance().getSystemUI().showToast(string);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }
                                return Boolean.TRUE.toString().getBytes();
                            }
                            break;
                        case 4:
                            NoSignalManager.getInstance().showNosignalMenu(source);
                            break;
                        case 5:
                            NoSignalManager.getInstance().showNosignalMenu(source);
                            break;
                    }
                }
                return Boolean.TRUE.toString().getBytes();
            }
            /////KtcTVDebug.debug("onKtcTvSignalChanged in SystemLogic not getAllowScreenSaverFromLauncher():");
            return Boolean.TRUE.toString().getBytes();
        }*/

        /*public byte[] onKtcTvSignalFormatChanged(List<String> list) {
            Source currentSource;
            Channel currentChannel;
            ////KtcTVDebug.debug("onKtcTvSignalFormatChanged in SystemLogic ");
            if (list != null) {
                currentSource = TvLogicManager.getInstance().getCurrentSource();
                currentChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
                ////KtcTVDebug.debug("currentSource:" + currentSource + " currentChannel:" + currentChannel);
            } else {
                currentSource = TvLogicManager.getInstance().getCurrentSource();
                currentChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
                ///KtcTVDebug.debug("currentSource:" + currentSource + " currentChannel:" + currentChannel);
            }
            if (currentSource != null) {
                ScreenDisplayManager.getInstance().showScreenDisplay(currentSource, currentChannel, false, true, false, true);
            }
            return null;
        }

        public byte[] onKtcTvSignalPlugStateChange(Source source, SOURCE_SIGNAL_STATE source_signal_state) {
            if (source_signal_state == SOURCE_SIGNAL_STATE.PLAY) {
                ////KtcTVDebug.debug("  onKtcTvSignalPlugStateChange,source:" + source.displayName);
                try {
                    KtcTvController.getInstance().mServiceContext.sendBroadcast(new Intent(KtcBroadcast.KTC_BCT_QUIT_WAKE_LOCK));
                } catch (Exception e) {
                }
                ExternalApi.getInstance().systemApi.switchSource(source);
            }
            return Boolean.TRUE.toString().getBytes();
        }

        public byte[] onKtcTvWindowFocusChanged(boolean z) {
            ////KtcTVDebug.debug("onKtcTvWindowFocusChanged in SystemLogic hasFocus:" + z);
            SystemLogic.launcherHasFocus = z;
            if (z) {
                NoSignalManager.getInstance().setAllowScreenSaverNoSignalText(true);
                NoSignalManager.getInstance().setScreenSaverApkStarted(false);
                NoSignalManager.getInstance().checkToShowScreenSaver();
            } else {
                NoSignalManager.getInstance().setAllowScreenSaverNoSignalText(false);
                if (!(NoSignalManager.getInstance().isScreenSaverApkStarted() || KtcTvController.getInstance().isSwitchSource())) {
                    NoSignalManager.getInstance().hideCountdown();
                }
            }
            return null;
        }

        public byte[] onSwitchChannelDone(Channel channel) {
            synchronized (SystemLogic.this.switchChannelLock) {
                NoSignalManager.getInstance().allowScreenSaverFuncFromLauncher();
                if (channel != null) {
                    ////KtcTVDebug.debug("onSwitchChannelDone:" + channel.name);
                }
                KtcLeftEPGDataGet.getInstance().clearEventType();
                SystemLogic.this.switchChannel = null;
                ScreenDisplayManager.getInstance().showScreenDisplay(TvLogicManager.getInstance().getCurrentSource(), channel, false, true, false, false);
                SwitchChannelManagerL.getInstance().onSwitchChannelDone(channel);
                KtcLeftEPGDataGet.getInstance().updateDefaultIndexAndIsCurrent(channel);
            }
            return Boolean.TRUE.toString().getBytes();
        }

        public byte[] onSwitchChannelStart(Channel channel) {
            ////KtcTVDebug.debug("onSwitchChannelStart in SystemLogic ");
            NoSignalManager.getInstance().disableScreenSaverFunc();
            NoSignalManager.getInstance().hideSignalState(false);
            ScreenDisplayManager.getInstance().showScreenDisplay(TvLogicManager.getInstance().getCurrentSource(), channel, true, false, false, false);
            return null;
        }

        public byte[] onSwitchSourceDone(Source source, Source source2) {
            KtcTvApp.getInstance().recycleSplash();
            NoSignalManager.getInstance().allowScreenSaverFuncFromLauncher();
            KtcLeftEPGDataGet.getInstance().clear();
            if (ExternalApi.getInstance().epgApi.hasEPG(source2)) {
                EPGLoader.getInstance().getChannelList(source2);
            }
            Channel currentChannel = ExternalApi.getInstance().systemApi.getCurrentChannel();
            SwitchChannelManagerL.getInstance().setCurrentChannel(currentChannel);
            TvLogicManager.getInstance().setCurrentSource(source2);
           //// KtcTVDebug.debug("onSwitchSourceDone launcherHasFocus:" + SystemLogic.launcherHasFocus);
            ////KtcTVDebug.debug("langlang <<<<<<<>>>>>>> type = " + ExternalApi.getInstance().systemApi.getSwitchSourceType());
            ScreenDisplayManager.getInstance().showScreenDisplay(source2, currentChannel, false, true, false, false);
            if (currentChannel == null) {
                ////KtcTVDebug.debug("onSwitchSourceDone from:" + source.getDisplayName() + " to:" + source2.getDisplayName() + " currentChannel:" + null);
            } else {
               //// KtcTVDebug.debug("onSwitchSourceDone from:" + source.getDisplayName() + " to:" + source2.getDisplayName() + " currentChannel:" + currentChannel.getDisplayName() + "@" + currentChannel.mapindex);
            }
            ////KtcTVDebug.debug("lwr", ">>>KtcTvConfig.getInstance().isExistTVFlag: " + KtcTvConfig.getInstance().isExistTVFlag + "||" + source2.index);
            switch (AnonymousClass1.-getcom_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues()[source2.getSourceNameEnum().ordinal()]) {
                case 1:
                case 2:
                    boolean isStbSmartRcActive = KtcTvApp.getInstance().getStbSettingApi().isStbSmartRcActive(source2.getSourceNameEnum().toString() + source2.index);
                    ////KtcTVDebug.debug("onSwitchSourceDone isStbSmartRcActive:" + isStbSmartRcActive);
                    if (KtcTvController.getInstance().canPopupInteractionNow().booleanValue() && !KtcTvConfig.getInstance().isExistTVFlag && isStbSmartRcActive) {
                        KtcTvUI.getInstance().getSystemUI().showToast(SystemLogic.this.mContext.getResources().getString(R.string.enterstbmode));
                        break;
                    }
            }
            if (KtcTvConfig.getInstance().isExistTVFlag) {
                KtcTvConfig.getInstance().isExistTVFlag = false;
            }
            return Boolean.TRUE.toString().getBytes();
        }

        public byte[] onSwitchSourceStart(Source source, Source source2) {
            ////KtcTVDebug.debug("onSwitchSourceStart in SystemLogic ");
            NoSignalManager.getInstance().notAllowCheckScreensaver();
            KtcTvApp.getInstance().recycleSplash();
            NoSignalManager.getInstance().hideSearchGuide();
            if (NoSignalManager.getInstance().needShowSignalstateMenu(source2)) {
                NoSignalManager.getInstance().showSignalSearchMenu();
            } else {
                NoSignalManager.getInstance().hideSignalState(false);
            }
            ////KtcTVDebug.debug("langlang <<<<<<<>>>>>>> type = " + ExternalApi.getInstance().systemApi.getSwitchSourceType());
            ScreenDisplayManager.getInstance().showScreenDisplay(source2, null, false, false, true, false);
            return null;
        }
    };*/
    private TvLogicSetFunction tvHaveDeleteChannelMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_CHANNELDELETHAVE_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            List<SkyEPGData> list = SkyLeftEPGDataGet.getInstance().getDeleteChannelBySQ();
            Log.d("Maxs33","tvHaveDeleteChannelMenu:list == null = " + (list == null));
            Log.d("Maxs33","tvHaveDeleteChannelMenu:list = " + list.size());
            if ((list == null) || list.size() <= 0){
                ///KtcTvApp.getInstance().getSkyTVTosatView().showTVToastView(R.string.deleteinfo);
                KtcTVTosatView ktcTVTosatView = new KtcTVTosatView(mContext);
                ktcTVTosatView.showTVToastView(R.string.deleteinfo);
            }else {
                Intent intent = new Intent(TvIntent.CHANNEL_LIST);
                intent.putExtra("viewtype", "channelEditDeleted");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }

            return  null;
           //// return new TCRetData(SystemLogic.this.showDeleteHaveChannelDialog());
        }
    };

    private TvLogicSetFunction tvWillDeleteChannelMenu = new TvLogicSetFunction(UILOGIC_TVMENU_COMMAND.TV_CHANNELDELET_MENU.toString()) {
        public TCRetData uiSetCmd(String str, UiCmdParams uiCmdParams) {
            ProgramInfo currentProgInfo = TvChannelManager.getInstance().getCurrentProgramInfo();
            if (currentProgInfo == null){
                ///KtcTvApp.getInstance().getSkyTVTosatView().showTVToastView(R.string.deleteinfo);
                KtcTVTosatView ktcTVTosatView = new KtcTVTosatView(mContext);
                ktcTVTosatView.showTVToastView(R.string.nochannelinfo);
            }else {
                Intent intent = new Intent(TvIntent.DELETE_CHANNEL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }

            return  null;
            //// return new TCRetData(SystemLogic.this.showDeleteHaveChannelDialog());
        }
    };
/*
    private SystemLogic() {
    }

    private boolean appIsInstall(String str) {
        PackageInfo packageInfo;
        try {
            packageInfo = this.mContext.getPackageManager().getPackageInfo(str, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            packageInfo = null;
        }
        if (packageInfo == null) {
            System.out.println(str + " not install.");
            return false;
        }
        System.out.println(str + " install.");
        return true;
    }*/

 /*   private boolean canChannelUpDown() {
        List channelList = EPGLoader.getInstance().getChannelList(TvLogicManager.getInstance().getCurrentSource());
        if (SwitchChannelManagerL.getInstance().getCurrentChannel() == null || channelList == null || channelList.size() <= 0) {
            return false;
        }
        for (Source equals : this.canChannelUpDownSource) {
            if (equals.equals(TvLogicManager.getInstance().getCurrentSource())) {
                return true;
            }
        }
        return false;
    }*/

   /* private Channel channelDown() {
        if (!canChannelUpDown()) {
            return null;
        }
        if (this.switchChannel == null) {
            this.switchChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
        }
        this.switchChannel = SwitchChannelManagerL.getInstance().channelDown(this.switchChannel, EPGLoader.getInstance().getChannelList(TvLogicManager.getInstance().getCurrentSource()));
        return this.switchChannel;
    }*/

    /*private Channel channelUp() {
        if (!canChannelUpDown()) {
            return null;
        }
        if (this.switchChannel == null) {
            this.switchChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
        }
        this.switchChannel = SwitchChannelManagerL.getInstance().channelUp(this.switchChannel, EPGLoader.getInstance().getChannelList(TvLogicManager.getInstance().getCurrentSource()));
        return this.switchChannel;
    }*/

    public static SystemLogic getInstance() {
        if (instance == null) {
            instance = new SystemLogic();
        }
        return instance;
    }

    private void showChannelSort() {
        /*try {
            ////KtcTVDebug.debug("showChannelSort");
            Source currentSource = TvLogicManager.getInstance().getCurrentSource();
            List dBTableSortList = ExternalApi.getInstance().epgApi.getDBTableSortList(KtcTvController.getInstance().getCurrentSource());
            ////KtcLogger.d("lwr", "showChannelSort channelList.size() 11:" + dBTableSortList.size());
            if (dBTableSortList == null || dBTableSortList.size() == 0) {
                KtcTvApp.getInstance().getKtcTVTosatView().showTVToastView(R.string.nochannelinfo);
            }
            dBTableSortList = EPGLoader.getInstance().getChannelList(currentSource);
            if (!(currentSource == null || dBTableSortList == null || dBTableSortList.size() <= 0)) {
                ////KtcTVDebug.debug("showChannelSort channelList.size():" + dBTableSortList.size());
            }
            /////KtcTVDebug.debug("KEYCODE_DPAD_CENTER");
            ExternalApi.getInstance().openEPGActivityApi.startShowChannelSortView();
        } catch (Exception e) {
            e.printStackTrace();
            ////KtcTVDebug.debug("showChannelSort exception");
        }*/
    }

    private boolean showDeleteHaveChannelDialog() {
        /*Source currentSource = TvLogicManager.getInstance().getCurrentSource();
        KtcLogger.d("lwr", ">>>>showDTMBDeleteHaveChannelDialog: currentSource: " + currentSource);
        KtcTVHaveDeleteChannelUtils.getInstance().initCurSource(currentSource);
        KtcTVHaveDeleteChannelUtils.getInstance().showDeletedChannelListView();
        return false;*/return false;
    }

    private void showQuckDemo() {
        /*try {
            KtcTVDebug.debug("showQuckDemo");
            Intent intent = new Intent();
            intent.setAction("com.ktc.intent.action.CommonDemo");
            MainMenuActivity.getInstance().startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            KtcTVDebug.debug("showQuckDemo exception");
        }*/
    }

    private void showQuckDemo(String str) {
        ComponentName componentName = new ComponentName("com.ktc.commondemo", "com.ktc.commondemo.KtcCommonDemo");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        intent.putExtra("params", str);
        MainMenuActivity.getInstance().startActivity(intent);
    }

    public boolean addPopUp(View view) {
        /*return ExternalApi.getInstance().systemApi.addPopUp(view);*/return false;
    }

    public boolean addPopUp(View view, LayoutParams layoutParams) {
       /* return ExternalApi.getInstance().systemApi.addPopUp(view, layoutParams);*/return false;
    }

    protected boolean checkChannelInfo() {
        /*Source currentSource = TvLogicManager.getInstance().getCurrentSource();
        SOURCE_NAME_ENUM sourceNameEnum = currentSource.getSourceNameEnum();
        Channel currentChannel = ExternalApi.getInstance().systemApi.getCurrentChannel();
        if (!(sourceNameEnum.equals(SOURCE_NAME_ENUM.AV) || sourceNameEnum.equals(SOURCE_NAME_ENUM.HDMI) || sourceNameEnum.equals(SOURCE_NAME_ENUM.YUV) || sourceNameEnum.equals(SOURCE_NAME_ENUM.VGA) || currentChannel == null)) {
            if (currentChannel == null) {
                return true;
            }
            if (!(currentChannel.bSkip || currentChannel.invalid)) {
                return true;
            }
        }
        ////ScreenDisplayManager.getInstance().showScreenDisplay(currentSource, null, false, true, false, true);
        return false;*/return false;
    }

    public boolean closeSearchGuide() {
        /////NoSignalManager.getInstance().closeSearchGuide();
        return true;
    }

    public void disableBroadcastKeys(List<String> list) {
        /*if (list == null) {
            KtcTVDebug.debug("keys=null");
        } else {
            TCBCKeysControler.getInstance().disableBCKeys(list);
        }*/
    }

    public boolean dismissPopUp(View view, boolean z) {
       /* return ExternalApi.getInstance().systemApi.dismissPopUp(view, z);*/return false;
    }

    public void enableBroadcastKeys(List<String> list) {
        /*if (list == null) {
            KtcTVDebug.debug("keys=null");
        } else {
            TCBCKeysControler.getInstance().enableBCKeys(list);
        }*/
    }

    public String formatToString(long j) {
        return new SimpleDateFormat("mm:ss:ms").format(new Date(j));
    }

    public TCEnumSetData getCollectChannel() {
        Log.d("Maxs","111getCollectChannel");
        ProgramInfo currentProgInfo = TvChannelManager.getInstance().getCurrentProgramInfo();
        if (currentProgInfo != null) {
            TCEnumSetData tCEnumSetData = new TCEnumSetData();
            short bfav = currentProgInfo.favorite;
            if (bfav == 1){
                tCEnumSetData.setCurrent("KTC_CFG_TV_CHANNEL_COLLECT_CANCEL");
            }else{
                tCEnumSetData.setCurrent("KTC_CFG_TV_CHANNEL_COLLECT");
            }
            return tCEnumSetData;
        }else{
            return null;
        }

    }

    public String getCollectChannelValue() {
        ////KtcTVDebug.debug("langlang", "mCollectStatus = " + this.mCollectStatus);
        switch (this.mCollectStatus) {
            case -1:
                return "KTC_CFG_TV_CHANNEL_COLLECT";
            case 0:
                return "KTC_CFG_TV_NO_CHANNEL_COLLECT";
            case 1:
                return "KTC_CFG_TV_CHANNEL_COLLECT_CANCEL";
            default:
                return "";
        }
    }

    public String getLocation() {
        /*return ExternalApi.getInstance().systemApi.getLocation();*/return null;
    }

    public boolean getQuickDemoStatus() {
        /*TCSwitchSetData tCSwitchSetData = (TCSwitchSetData) ExternalApi.getInstance().systemServiceApi.getSetData(TCEnvKey.KTC_SYSTEM_ENV_QUICK_DEMO_MODE);
        if (tCSwitchSetData == null) {
            return false;
        }
        KtcTVDebug.debug("langlang isOn = " + tCSwitchSetData.isOn());
        return tCSwitchSetData.isOn();*/return false;
    }

    public void init(Context context, KtcCmdConnectorListener ktcCmdConnectorListener) {
        super.init(context, ktcCmdConnectorListener);
       ///// ExternalApi.getInstance().systemApi.registerSystemListener(this.systemListener);
    }

    ///public boolean onCollectInfoSelect(Channel channel) {
        /*KtcTVDebug.debug("TV_SYSTEM_ON_COLLECTINFO_SELECT channel:" + channel);
        return Boolean.valueOf(ChannelCollectManager.getInstance().playCollectChannel(channel)).booleanValue();*/
    ////}
/*
    public boolean onKeyDown(TVOnKeydownParams tVOnKeydownParams) {
        KtcTvAsyncTask.getInstance().runAsyncTask(TV_LOGIC_ON_KEY_DOWN_TAG, new IAsyncTask() {*/
     ////       private static /* synthetic */ int[] -com_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues;
     ///       final /* synthetic */ int[] $SWITCH_TABLE$com$horion$tv$define$object$Source$SOURCE_NAME_ENUM;

       ////     private static /* synthetic */ int[] -getcom_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues() {
       /*         if (-com_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues != null) {
                    return -com_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues;
                }
                int[] iArr = new int[SOURCE_NAME_ENUM.values().length];
                try {
                    iArr[SOURCE_NAME_ENUM.ATV.ordinal()] = 1;
                } catch (NoSuchFieldError e) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.AV.ordinal()] = 6;
                } catch (NoSuchFieldError e2) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.DTMB.ordinal()] = 2;
                } catch (NoSuchFieldError e3) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.DVBC.ordinal()] = 3;
                } catch (NoSuchFieldError e4) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.EXTERNAL.ordinal()] = 4;
                } catch (NoSuchFieldError e5) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.HDMI.ordinal()] = 7;
                } catch (NoSuchFieldError e6) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.IPLIVE.ordinal()] = 5;
                } catch (NoSuchFieldError e7) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.REMEMBER.ordinal()] = 8;
                } catch (NoSuchFieldError e8) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.VGA.ordinal()] = 9;
                } catch (NoSuchFieldError e9) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.VOD.ordinal()] = 10;
                } catch (NoSuchFieldError e10) {
                }
                try {
                    iArr[SOURCE_NAME_ENUM.YUV.ordinal()] = 11;
                } catch (NoSuchFieldError e11) {
                }
                -com_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues = iArr;
                return iArr;
            }

            public void doAsyncTask(Object obj) {
                Source currentSource;
                boolean z = true;
                TVOnKeydownParams tVOnKeydownParams = (TVOnKeydownParams) obj;
                KtcTVDebug.debug("ON_KEY_DOWN in SystemLogic keyCode:" + tVOnKeydownParams.keyCode);
                NoSignalManager.getInstance().resetNoSignalCountText();
                Channel channel;
                Source currentSource2;
                switch (tVOnKeydownParams.keyCode) {
                    case 4:
                        if (ExternalApi.getInstance().systemApi.isReleased()) {
                            return;
                        }
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        currentSource = TvLogicManager.getInstance().getCurrentSource();
                        if (!(currentSource.equals(Source.ATV()) || currentSource.equals(Source.DTMB()) || currentSource.equals(Source.DVBC()))) {
                            z = currentSource.equals(Source.IPLive());
                        }
                        if (z) {
                            SwitchChannelManagerL.getInstance().numberSwitch(tVOnKeydownParams.keyCode - 7);
                            return;
                        }
                        return;
                    case 19:
                    case 20:
                        synchronized (SystemLogic.this.switchChannelLock) {
                            channel = (Channel) tVOnKeydownParams.params;
                            currentSource2 = TvLogicManager.getInstance().getCurrentSource();
                            if (currentSource2 != null) {
                                ScreenDisplayManager.getInstance().showScreenDisplay(currentSource2, channel, true, false, false, false);
                            }
                        }
                        return;
                    case 22:*/
             ////       case KtcworthBroadcastKey.KTC_KEY_DTV_GUIDE /*229*/:
             ////       case KtcworthBroadcastKey.KTC_KEY_DTV_GUIDE_ANDROID_L /*746*/:
             /*           currentSource = TvLogicManager.getInstance().getCurrentSource();
                        if (currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.IPLIVE || currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.DTMB || currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.DVBC || currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.ATV || currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.EXTERNAL) {
                            if (!ScreenDisplayManager.getInstance().getScreenDisplayShowFlag()) {
                                ScreenDisplayManager.getInstance().hideScreenDisplay();
                                ExternalApi.getInstance().openEPGActivityApi.startShowRightEpgView();
                                return;
                            }
                            return;
                        } else if (currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.HDMI || currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.AV) {
                            try {
                                Intent intent = new Intent();
                                intent.setAction("coocaa.intent.action.third.EPG");
                                MainMenuActivity.getInstance().startActivity(intent);
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                        } else {
                            return;
                        }
                    case 23:
                    case 66:
                        SwitchChannelManagerL.getInstance().getCurrentChannel();
                        currentSource = TvLogicManager.getInstance().getCurrentSource();
                        if (currentSource != null && KtcTvConfig.getInstance().isNotShowMenuorSearchFlag) {
                            if ((currentSource.getSourceNameEnum() != SOURCE_NAME_ENUM.DVBC || !DtvDigitalUiManager.getInstance().getShowDigitalUI()) && !KtcTvController.getInstance().isSwitchChannel() && !KtcTvController.getInstance().isSwitchSource() && SystemLogic.this.checkChannelInfo()) {
                                if (!(currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.IPLIVE || currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.DTMB || currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.DVBC || currentSource.getSourceNameEnum() == SOURCE_NAME_ENUM.ATV)) {
                                    if (currentSource.getSourceNameEnum() != SOURCE_NAME_ENUM.EXTERNAL) {
                                        return;
                                    }
                                }
                                KtcTVDebug.debug("KEYCODE_DPAD_CENTER_Show_openEPGActivity,KtcTvController.getInstance().isWaitingKtcDialogHide:" + KtcTvController.getInstance().isWaitingKtcDialogHide);
                                if (!KtcTvController.getInstance().isWaitingKtcDialogHide) {
                                    ExternalApi.getInstance().openEPGActivityApi.startShowLeftEpgView();
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        return;*/
               ////     case KtcworthBroadcastKey.KTC_BROADCAST_KEY_SCREEN_DISPLAY /*165*/:
                /*        if (!KtcTvController.getInstance().isSwitchChannel() && !KtcTvController.getInstance().isSwitchSource()) {
                            channel = SwitchChannelManagerL.getInstance().getCurrentChannel();
                            currentSource2 = TvLogicManager.getInstance().getCurrentSource();
                            if (currentSource2 != null) {
                                ScreenDisplayManager.getInstance().showScreenDisplay(currentSource2, channel, false, true, false, true);
                                return;
                            }
                            return;
                        }
                        return;*/
             ////       case KtcworthBroadcastKey.KTC_KEY_ALTERNATE /*226*/:
             ////       case KtcworthBroadcastKey.KTC_KEY_ALTERNATE_ANDROID_L /*743*/:
             ////           break;
             ////       case KtcworthBroadcastKey.KTC_KEY_SHORT_SHUTTLE_LEFT /*764*/:
              ////      case KtcworthBroadcastKey.KTC_KEY_SHORT_SHUTTLE_RIGHT /*765*/:
              ////          if (!SystemLogic.this.isSearchTvChannel) {
             ////               EPGChannelSearcher.getInstance().show(TvLogicManager.getInstance().getCurrentSource());
             ///               return;
             ///           }
            ////            return;
           ///         default:
          ////              return;
          ////      }
   /*             currentSource = TvLogicManager.getInstance().getCurrentSource();
                switch (AnonymousClass18.-getcom_tianci_tv_define_object_Source$SOURCE_NAME_ENUMSwitchesValues()[currentSource.getSourceNameEnum().ordinal()]) {
                    case 1:
                    case 2:
                    case 3:
                    case 5:
                        SwitchChannelManagerL.getInstance().backChannel();
                        return;
                    case 4:
                        if (currentSource.bILiveSource) {
                            SwitchChannelManagerL.getInstance().backChannel();
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }, tVOnKeydownParams, 0, true);
        return true;
    }
*/
    public boolean onSignalPlugChangeUICB(Source source, int i) {
        /*if (i == 1) {
            ExternalApi.getInstance().systemApi.switchSource(source);
        }
        return (i == 1 || i == 2) ? true : true;*/return false;
    }

    public boolean removePopUp(View view, boolean z) {
        /*return ExternalApi.getInstance().systemApi.removePopUp(view, z);*/return false;
    }

    public boolean resetNosignalCout() {
        /*NoSignalManager.getInstance().resetNoSignalCountText();
        return true;*/return false;
    }

    public boolean screenDisplay() {
        /*Source currentSource = TvLogicManager.getInstance().getCurrentSource();
        Channel currentChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
       ///// KtcTVDebug.debug("currentSource:" + currentSource + " currentChannel:" + currentChannel);
        if (currentSource != null) {
            ScreenDisplayManager.getInstance().showScreenDisplay(currentSource, currentChannel, false, true, false, true);
        }
        return true;*/return false;
    }

    public boolean setCollectChannel() {
        /*Source currentSource = TvLogicManager.getInstance().getCurrentSource();
        /////KtcTVDebug.debug("currentSource:" + currentSource);
        boolean z = false;
        if (currentSource != null && NoSignalManager.getInstance().hasChannel(currentSource)) {
            Channel currentChannel = SwitchChannelManagerL.getInstance().getCurrentChannel();
            boolean collectChannel = ChannelCollectManager.getInstance().collectChannel(currentChannel, currentSource);
            ////KtcTVDebug.debug("setCollectChannel ret:" + collectChannel);
            String textByKey = collectChannel ? ChannelCollectManager.getInstance().getCollectStatus(currentChannel, currentSource) ? UITextUtil.getInstance().getTextByKey("USER_COLLECT_SUCCESS") : UITextUtil.getInstance().getTextByKey("USER_UNCOLLECT_SUCCESS") : ChannelCollectManager.getInstance().getCollectStatus(currentChannel, currentSource) ? UITextUtil.getInstance().getTextByKey("USER_UNCOLLECT_FAILED") : UITextUtil.getInstance().getTextByKey("USER_COLLECT_FAILED");
            KtcTvUI.getInstance().getSystemUI().showToast(textByKey);
            z = collectChannel;
        }
        if (z) {
            KtcLeftEPGDataGet.getInstance().clear();
        }
        return z;*/return false;
    }

    public boolean setLocation(String str, boolean z) {
       /* return ExternalApi.getInstance().systemApi.setLocation(str, z);*/return false;
    }

    public void setSearchFlag(boolean z) {
        this.isSearchTvChannel = z;
    }

    public boolean showMenu(Source source) {
        /*KtcTvMenuManager.getInstance().onKeyMenu(source);
        return true;*/return false;
    }

    public boolean startShowScreenSaver(boolean z) {
        /*////KtcTVDebug.debug("startShowScreenSaver launcherHasFocus = " + launcherHasFocus);
        if (launcherHasFocus) {
            if (!ExternalApi.getInstance().systemApi.isKtcTVDiglogShowened()) {
                return NoSignalManager.getInstance().getFocusWhenSignalStateBgMenuShowed();
            }
            String ktcTVDiglogType = ExternalApi.getInstance().systemApi.getKtcTVDiglogType();
           /////KtcTVDebug.debug("startShowScreenSaver isKtcTVDiglogShowened,ktcTVDiglogType" + ktcTVDiglogType);
            if (ktcTVDiglogType != null && ktcTVDiglogType.equalsIgnoreCase(KtcTVDialogType.SEARCHGUIDE)) {
                NoSignalManager.getInstance().hideSearchGuide();
                NoSignalManager.getInstance().showSignalState();
            }
        }
        return true;*/return false;
    }

    public TCEnumSetData tvMenuGetDisplayMode() {
        Log.d("Maxs","tvMenuGetDisplay");

        int currZoomModeIdx = TvPictureManager.getInstance().getVideoArcType();
        Log.d("Maxs71","before:tvMenuSetDisplayMode--->currZoomModeIdx = " + currZoomModeIdx);
        String currentStr = null;
        switch(currZoomModeIdx){
            case TvPictureManager.VIDEO_ARC_16x9:
                currentStr = "KTC_CFG_TV_DISPLAY_MODE_16_9";
                break;
            case TvPictureManager.VIDEO_ARC_4x3:
                currentStr = "KTC_CFG_TV_DISPLAY_MODE_4_3";
                break;
            case TvPictureManager.VIDEO_ARC_AUTO:
                currentStr = "KTC_CFG_TV_DISPLAY_MODE_AUTO";
                break;
            case TvPictureManager.VIDEO_ARC_DOTBYDOT:
                currentStr = "KTC_CFG_TV_DISPLAY_MODE_PC_MODE";
                break;
            case TvPictureManager.VIDEO_ARC_ZOOM1:
                currentStr = "KTC_CFG_TV_DISPLAY_MODE_MOVIE";
                break;
            case TvPictureManager.VIDEO_ARC_ZOOM2:
                currentStr = "KTC_CFG_TV_DISPLAY_MODE_SUBTITLE";
                break;
        }
        int inputSrc = TvCommonManager.getInstance().getCurrentTvInputSource();
        Log.d("Maxs","inputSrc = " + inputSrc);
        switch (inputSrc){
            case TvCommonManager.INPUT_SOURCE_DTV:
                TCEnumSetData tCEnumSetDataDTV = new TCEnumSetData();
                tCEnumSetDataDTV.setCurrent(currentStr);
                tCEnumSetDataDTV.setEnumCount(5);
                List arrayListDTV = new ArrayList();
                arrayListDTV.add("KTC_CFG_TV_DISPLAY_MODE_16_9");
                arrayListDTV.add("KTC_CFG_TV_DISPLAY_MODE_4_3");
                arrayListDTV.add("KTC_CFG_TV_DISPLAY_MODE_MOVIE");
                arrayListDTV.add("KTC_CFG_TV_DISPLAY_MODE_SUBTITLE");
                arrayListDTV.add("KTC_CFG_TV_DISPLAY_MODE_AUTO");
                tCEnumSetDataDTV.setEnumList(arrayListDTV);
                return tCEnumSetDataDTV;
            case TvCommonManager.INPUT_SOURCE_ATV:
                TCEnumSetData tCEnumSetDataATV = new TCEnumSetData();
                tCEnumSetDataATV.setCurrent(currentStr);
                tCEnumSetDataATV.setEnumCount(4);
                List arrayListATV = new ArrayList();
                arrayListATV.add("KTC_CFG_TV_DISPLAY_MODE_16_9");
                arrayListATV.add("KTC_CFG_TV_DISPLAY_MODE_4_3");
                arrayListATV.add("KTC_CFG_TV_DISPLAY_MODE_MOVIE");
                arrayListATV.add("KTC_CFG_TV_DISPLAY_MODE_SUBTITLE");;
                tCEnumSetDataATV.setEnumList(arrayListATV);
                return tCEnumSetDataATV;
            case TvCommonManager.INPUT_SOURCE_CVBS:
                TCEnumSetData tCEnumSetDataAV = new TCEnumSetData();
                tCEnumSetDataAV.setCurrent(currentStr);
                tCEnumSetDataAV.setEnumCount(4);
                List arrayListAV = new ArrayList();
                arrayListAV.add("KTC_CFG_TV_DISPLAY_MODE_16_9");
                arrayListAV.add("KTC_CFG_TV_DISPLAY_MODE_4_3");
                arrayListAV.add("KTC_CFG_TV_DISPLAY_MODE_MOVIE");
                arrayListAV.add("KTC_CFG_TV_DISPLAY_MODE_SUBTITLE");
                tCEnumSetDataAV.setEnumList(arrayListAV);
                return tCEnumSetDataAV;
            case TvCommonManager.INPUT_SOURCE_HDMI:
            case TvCommonManager.INPUT_SOURCE_HDMI2:
            case TvCommonManager.INPUT_SOURCE_HDMI3:
                Log.d("Maxs71","HDMI:currentStr = " + currentStr);
                TCEnumSetData tCEnumSetDataHDMI = new TCEnumSetData();
                tCEnumSetDataHDMI.setCurrent(currentStr);
                tCEnumSetDataHDMI.setEnumCount(5);
                List arrayListHDMI = new ArrayList();
                arrayListHDMI.add("KTC_CFG_TV_DISPLAY_MODE_16_9");
                arrayListHDMI.add("KTC_CFG_TV_DISPLAY_MODE_4_3");
                arrayListHDMI.add("KTC_CFG_TV_DISPLAY_MODE_MOVIE");
                arrayListHDMI.add("KTC_CFG_TV_DISPLAY_MODE_SUBTITLE");
                try {
                    if (TvPictureManager.getInstance().getVideoInfo().getVideoScanType() != 1)
                        arrayListHDMI.add("KTC_CFG_TV_DISPLAY_MODE_PC_MODE");
                } catch (TvCommonException e) {
                    e.printStackTrace();
                }
                tCEnumSetDataHDMI.setEnumList(arrayListHDMI);
                return tCEnumSetDataHDMI;
        }
        return null;
    }

    public TCEnumSetData tvMenuGetPictureMode() {
        int picModeIdx = TvPictureManager.getInstance().getPictureMode();
        String currentStr = null;
        switch(picModeIdx){
            case TvPictureManager.PICTURE_MODE_DYNAMIC:
                currentStr = "KTC_CFG_TV_PICTURE_MODE_VIVID";
                break;
            case TvPictureManager.PICTURE_MODE_NORMAL:
                currentStr = "KTC_CFG_TV_PICTURE_MODE_STANDARD";
                break;
            case TvPictureManager.PICTURE_MODE_SOFT:
                currentStr = "KTC_CFG_TV_PICTURE_MODE_GENTLE";
                break;
            case TvPictureManager.PICTURE_MODE_USER:
                currentStr = "KTC_CFG_TV_PICTURE_MODE_USER";
                break;
        }

        TCEnumSetData tCEnumSetData = new TCEnumSetData();
        tCEnumSetData.setCurrent(currentStr);
        tCEnumSetData.setEnumCount(4);
        List arrayList = new ArrayList();
        arrayList.add("KTC_CFG_TV_PICTURE_MODE_VIVID");
        arrayList.add("KTC_CFG_TV_PICTURE_MODE_STANDARD");
        arrayList.add("KTC_CFG_TV_PICTURE_MODE_GENTLE");
        arrayList.add("KTC_CFG_TV_PICTURE_MODE_USER");
        tCEnumSetData.setEnumList(arrayList);
        return tCEnumSetData;
    }

    public TCEnumSetData tvMenuGetSoundMode() {
        Log.d("Maxs","tvMenuGetSoundMode");
        int soundModeIdx = TvAudioManager.getInstance().getAudioSoundMode();
        String currStr = "";
        Log.d("Maxs","getSoundMode = " +soundModeIdx);
        switch(soundModeIdx){
            case TvAudioManager.SOUND_MODE_STANDARD:
                currStr = "KTC_CFG_TV_SOUND_MODE_STANDARD";
                break;
            case TvAudioManager.SOUND_MODE_MUSIC:
                currStr = "KTC_CFG_TV_SOUND_MODE_MUSIC";
                break;
            case TvAudioManager.SOUND_MODE_MOVIE:
                currStr = "KTC_CFG_TV_SOUND_MODE_MOVIE";
                break;
            case TvAudioManager.SOUND_MODE_USER:
                currStr = "KTC_CFG_TV_SOUND_MODE_USER";
                break;
        }
        TCEnumSetData tCEnumSetData = new TCEnumSetData();
        tCEnumSetData.setCurrent(currStr);
        tCEnumSetData.setEnumCount(4);
        List arrayList = new ArrayList();
        arrayList.add("KTC_CFG_TV_SOUND_MODE_STANDARD");
        arrayList.add("KTC_CFG_TV_SOUND_MODE_MUSIC");
        arrayList.add("KTC_CFG_TV_SOUND_MODE_MOVIE");
        arrayList.add("KTC_CFG_TV_SOUND_MODE_USER");
        tCEnumSetData.setEnumList(arrayList);
        return tCEnumSetData;
    }

    public boolean tvMenuSetDisplayMode(int index) {
        String currentStr = null;
        int CurrentIndex = 0;
        Log.d("Maxs71","before:tvMenuSetDisplayMode--->index = " + index);
        switch(index){
            case 0:
                CurrentIndex = TvPictureManager.VIDEO_ARC_16x9;
                break;
            case 1:
                CurrentIndex = TvPictureManager.VIDEO_ARC_4x3;
                break;
            case 2:
                CurrentIndex = TvPictureManager.VIDEO_ARC_ZOOM1;
                break;
            case 3:
                CurrentIndex = TvPictureManager.VIDEO_ARC_ZOOM2;
                break;
        }
        int inputSrc = TvCommonManager.getInstance().getCurrentTvInputSource();
        Log.d("Maxs","inputSrc = " + inputSrc);
        switch (inputSrc){
            case TvCommonManager.INPUT_SOURCE_DTV:
                switch(index){
                    case 4:
                        CurrentIndex = TvPictureManager.VIDEO_ARC_AUTO;
                        break;

                }
                break;
            case TvCommonManager.INPUT_SOURCE_ATV:
                break;
            case TvCommonManager.INPUT_SOURCE_CVBS:
                break;
            case TvCommonManager.INPUT_SOURCE_HDMI:
            case TvCommonManager.INPUT_SOURCE_HDMI2:
            case TvCommonManager.INPUT_SOURCE_HDMI3:
                Log.d("Maxs71","HMDI1:index = " + index);
                switch (index){
                    case 4:
                        CurrentIndex = TvPictureManager.VIDEO_ARC_DOTBYDOT;
                        break;
                }
                break;
        }
        Log.d("Maxs71","after:tvMenuSetDisplayMode--->CurrentIndex = " + CurrentIndex);
        return TvPictureManager.getInstance().setVideoArcType(CurrentIndex);
    }

    public boolean tvMenuSetPictureMode(int index) {
        Log.d("Maxs","SystemLogic:tvMenuSetPictureMode: i = " + index);
        int currentIndex = TvPictureManager.PICTURE_MODE_NORMAL;
        switch(index){
            case 0:
                currentIndex = TvPictureManager.PICTURE_MODE_DYNAMIC;
                break;
            case 1:
                currentIndex = TvPictureManager.PICTURE_MODE_NORMAL;
                break;
            case 2:
                currentIndex = TvPictureManager.PICTURE_MODE_SOFT;
                break;
            case 3:
                currentIndex = TvPictureManager.PICTURE_MODE_USER;
                break;
        }
        Log.d("Maxs","SetSound:currentIndex = " + currentIndex);
        return TvPictureManager.getInstance().setPictureMode(currentIndex);
    }

    public boolean tvMenuSetSoundMode(int index) {
        Log.d("Maxs","SoundMode index = " + index);
        int currentIndex = TvAudioManager.SOUND_MODE_STANDARD;
        switch(index){
            case 0:
                currentIndex = TvAudioManager.SOUND_MODE_STANDARD;
                break;
            case 1:
                currentIndex = TvAudioManager.SOUND_MODE_MUSIC;
                break;
            case 2:
                currentIndex = TvAudioManager.SOUND_MODE_MOVIE;
                break;
            case 3:
                currentIndex = TvAudioManager.SOUND_MODE_USER;
                break;

        }

        return TvAudioManager.getInstance().setAudioSoundMode(currentIndex);
    }

    public boolean tvMenuSetSoundChannel(int index) {
        DtvAudioInfo dtvAudioInfo = TvChannelManager.getInstance().getAudioInfo();
        if ((dtvAudioInfo.audioLangNum == 0) || (dtvAudioInfo.audioLangNum == 1)){
            return true;
        }else{
            TvChannelManager.getInstance().switchAudioTrack(index);
            return true;
        }
    }

    public TCEnumSetData tvMenuGetSoundChannel(){
        Log.d("Maxs27","tvMenuGetSoundChannel");
        DtvAudioInfo dtvAudioInfo = TvChannelManager.getInstance().getAudioInfo();
        TCEnumSetData tCEnumSetData = new TCEnumSetData();
        List arrayList = new ArrayList();
        arrayList.clear();
        String currStr = "";
        if (dtvAudioInfo.audioLangNum == 0){
            tCEnumSetData.setEnumCount(1);
            tCEnumSetData.setCurrent("TV_STRING_DTV_SOUND_TRACE");
            arrayList.add("TV_STRING_DTV_SOUND_TRACE");
            tCEnumSetData.setEnumList(arrayList);
            return tCEnumSetData;
        }else{

            int index = dtvAudioInfo.currentAudioIndex;
            if (index < 0 || index >= dtvAudioInfo.audioLangNum) {
                index = 0;
            }

            tCEnumSetData.setCurrent("TV_STRING_DTV_SOUND_TRACE" + "_" +  (index + 1));
            tCEnumSetData.setEnumCount(dtvAudioInfo.audioLangNum);
            for (short i = 0; i < dtvAudioInfo.audioLangNum; i++) {
                arrayList.add("TV_STRING_DTV_SOUND_TRACE" + "_" + (i + 1));
            }
            tCEnumSetData.setEnumList(arrayList);
            return tCEnumSetData;
        }


    }

    public boolean tvMenuSetSubtitle(int subtitleIndex){
        Log.d("Maxs72","================tvMenuSetSubtitle:" + subtitleIndex);
        DtvSubtitleInfo subtitleInfo = TvChannelManager.getInstance().getSubtitleInfo();
        Log.e("zjf", "subtitleInfo=" + subtitleInfo.currentSubtitleIndex);
        int currentSubtitleIndex = subtitleInfo.currentSubtitleIndex;

        TCEnumSetData tCEnumSetDataSubtitle = new TCEnumSetData();
        String currSubtitle = null;
        List arrayListSubtitle = new ArrayList();
        arrayListSubtitle.clear();
        String[] LanguageText = mContext.getResources().getStringArray(
                R.array.str_array_language_dtmb);
        if (subtitleInfo.subtitleServiceNumber > 0) {

            MenuSubtitleService list[] = subtitleInfo.subtitleServices;

            String[] data = new String[subtitleInfo.subtitleServiceNumber + 1];
            Log.d("Maxs72","data.length = " + data.length);
            Log.d("Maxs72","subtitleIndex = " + subtitleIndex);
            if (data.length >= 0){
                if (subtitleIndex == 0){
                    TvChannelManager.getInstance().closeSubtitle();
                }else{
                    TvChannelManager.getInstance().closeSubtitle();
                    Log.d("Maxs72","================openSubtitle:" + (subtitleIndex - 1));
                    TvChannelManager.getInstance().openSubtitle(subtitleIndex - 1);
                }
            }
            /*for (int i = 0; i < data.length; i++) {
                if (i == 0) {
                    ///data[i] = mContext.getString(R.string.str_tvsetting_subtitleLanguage_close);
                    Log.d("Maxs72","str_tvsetting_subtitleLanguage_close" );
                    currSubtitle = "str_tvsetting_subtitleLanguage_close";
                    arrayListSubtitle.add("str_tvsetting_subtitleLanguage_close");
                } else {
                    ///Log.e("zjf", "subtitleIlanguage=" +list[i - 1].getLanguage().name());
                    int index = getposition_language(list[i - 1].getLanguage().name());
                    Log.d("Maxs72","---list[i - 1].getLanguage().name() = " + list[i - 1].getLanguage().name());
                    Log.d("Maxs72","----?DTMB_SUBTILTE_LANGUAGE_" + (index + 1));
                    if(index>=0){
                        ////data[i] = LanguageText[index];
                        Log.d("Maxs72","DTMB_SUBTILTE_LANGUAGE_" + (index + 1));
                        currSubtitle = "DTMB_SUBTILTE_LANGUAGE_" + (index + 1);
                        arrayListSubtitle.add("DTMB_SUBTILTE_LANGUAGE_" + (index + 1));
                    }else{
                        ///data[i] = mContext.getString(R.string.str_sound_format_unknown);
                        currSubtitle = "str_sound_format_unknown";
                        Log.d("Maxs72","str_sound_format_unknown");
                        arrayListSubtitle.add("str_sound_format_unknown");
                    }

                }
            }*/

        } else {
            /*tCEnumSetDataSubtitle.setCurrent("str_tvsetting_subtitleLanguage_null");
            tCEnumSetDataSubtitle.setEnumCount(1);

            arrayListSubtitle.add("str_tvsetting_subtitleLanguage_null");
            tCEnumSetDataSubtitle.setEnumList(arrayListSubtitle);
*/
        }
        return false;
    }
    private int getposition_language(String name) {
        String[] LanguageTag  = mContext.getResources().getStringArray(
                R.array.str_arr_subtitle_constants);;
        for (int i = 0; i < LanguageTag.length; i++) {
            if (LanguageTag[i].equals(name))
                return i;
        }
        return -100;
    }

    public TCEnumSetData tvMenuGetSubtitle(){
        Log.d("Maxs80","getDTMBSubtilte:");
        DtvSubtitleInfo subtitleInfo = TvChannelManager.getInstance().getSubtitleInfo();
        Log.e("zjf", "subtitleInfo=" + subtitleInfo.currentSubtitleIndex);

        TCEnumSetData tCEnumSetDataSubtitle = new TCEnumSetData();
        String currSubtitle = null;
        ArrayList<String> arrayListSubtitle = new ArrayList();
        arrayListSubtitle.clear();
        int currentSubtitleIndex = subtitleInfo.currentSubtitleIndex;
        Log.d("Maxs72","currentSubtitleIndex = " + currentSubtitleIndex);
        if (currentSubtitleIndex == 255) {
            currentSubtitleIndex = 0;
            currSubtitle = "str_tvsetting_subtitleLanguage_close";
        }/* else {
            currSubtitle = "DTMB_SUBTILTE_LANGUAGE_" + (currentSubtitleIndex + 1);
        }*/
        String[] LanguageText = mContext.getResources().getStringArray(
                R.array.str_array_language_dtmb);
        if (subtitleInfo.subtitleServiceNumber > 0) {

            MenuSubtitleService list[] = subtitleInfo.subtitleServices;

            String[] data = new String[subtitleInfo.subtitleServiceNumber + 1];
            Log.d("Maxs72","data.length = " + data.length);
            for (int i = 0; i < data.length; i++) {
                if (i == 0) {
                    ///data[i] = mContext.getString(R.string.str_tvsetting_subtitleLanguage_close);
                    Log.d("Maxs72","str_tvsetting_subtitleLanguage_close" );
                    ///currSubtitle = "str_tvsetting_subtitleLanguage_close";
                    arrayListSubtitle.add("str_tvsetting_subtitleLanguage_close");
                } else {
                    ///Log.e("zjf", "subtitleIlanguage=" +list[i - 1].getLanguage().name());
                    int index = getposition_language(list[i - 1].getLanguage().name());
                    Log.d("Maxs72","---list[i - 1].getLanguage().name() = " + list[i - 1].getLanguage().name());
                    Log.d("Maxs72","----?DTMB_SUBTILTE_LANGUAGE_" + (index + 1));
                    if(index>=0){
                        ////data[i] = LanguageText[index];
                        Log.d("Maxs72","DTMB_SUBTILTE_LANGUAGE_" + (index + 1));
                        ///currSubtitle = "DTMB_SUBTILTE_LANGUAGE_" + (index + 1);
                        arrayListSubtitle.add("DTMB_SUBTILTE_LANGUAGE_" + (index + 1));
                    }else{
                        ///data[i] = mContext.getString(R.string.str_sound_format_unknown);
                        ///currSubtitle = "str_sound_format_unknown";
                        Log.d("Maxs72","str_sound_format_unknown");
                        arrayListSubtitle.add("str_sound_format_unknown");
                    }

                }
            }

            for (int j = 0; j < arrayListSubtitle.size(); j++ ){
                Log.d("Maxs72","######" + arrayListSubtitle.get(j));
            }
            Log.d("Maxs72","---------subtitleInfo.currentSubtitleIndex------->" + subtitleInfo.currentSubtitleIndex);
            if (subtitleInfo.currentSubtitleIndex != 255){
                Log.d("Maxs72","---------------->" + arrayListSubtitle.get(subtitleInfo.currentSubtitleIndex+ 1));
                tCEnumSetDataSubtitle.setCurrent(arrayListSubtitle.get(subtitleInfo.currentSubtitleIndex + 1));
                tCEnumSetDataSubtitle.setCurrentIndex(subtitleInfo.currentSubtitleIndex + 1);
            }else{
                Log.d("Maxs72","--11->str_tvsetting_subtitleLanguage_close");
                tCEnumSetDataSubtitle.setCurrent("str_tvsetting_subtitleLanguage_close");
                tCEnumSetDataSubtitle.setCurrentIndex(0);
            }

            tCEnumSetDataSubtitle.setEnumCount(data.length);
            tCEnumSetDataSubtitle.setEnumList(arrayListSubtitle);
            return tCEnumSetDataSubtitle;
        } else {
            currSubtitle ="str_tvsetting_subtitleLanguage_null";
            tCEnumSetDataSubtitle.setCurrent(currSubtitle);
            tCEnumSetDataSubtitle.setEnumCount(1);

            arrayListSubtitle.add("str_tvsetting_subtitleLanguage_null");
            tCEnumSetDataSubtitle.setEnumList(arrayListSubtitle);
            return tCEnumSetDataSubtitle;

        }

        /*TCEnumSetData tCEnumSetDataAV = new TCEnumSetData();
        tCEnumSetDataAV.setCurrent(currentStr);
        tCEnumSetDataAV.setEnumCount(4);
        List arrayListAV = new ArrayList();
        arrayListAV.add("KTC_CFG_TV_DISPLAY_MODE_16_9");
        arrayListAV.add("KTC_CFG_TV_DISPLAY_MODE_4_3");
        arrayListAV.add("KTC_CFG_TV_DISPLAY_MODE_MOVIE");
        arrayListAV.add("KTC_CFG_TV_DISPLAY_MODE_SUBTITLE");
        tCEnumSetDataAV.setEnumList(arrayListAV);
        return tCEnumSetDataAV;*/
        //return null;
    }
}

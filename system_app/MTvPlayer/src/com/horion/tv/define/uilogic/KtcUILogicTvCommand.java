package com.horion.tv.define.uilogic;

public class KtcUILogicTvCommand {
    private static final String CMD = "?cmd=";
    private static final String NEEDACK = "&needack=";
    private static final String OVERRIDE = "&override=";
    public static final String SERVICE_NAME = "com.ktc.colorful.uilogic/com.ktc.service.scenceservice.TVScenceService";
    public static final String TV_CMD_HEADER = "horion://com.ktc.colorful.uilogic/com.ktc.service.scenceservice.TVScenceService";
    public static final String TV_TEXT_RESOURCE_HEAD = "TV_STRING_";

    public enum UILOGIC_TV_COMMAND {
        ON_KEY_DOWN,
        SHOW_MENU,
        SCREEN_DISPLAY,
        TV_SYSTEM_MENU_GET_DISPLAYMODE,
        TV_SYSTEM_MENU_SET_DISPLAYMODE,
        TV_RESET_NOSIGNAL_COUNT,
        TV_SYSTEM_ON_COLLECTINFO_SELECT,
        IPLIVE_MENU_SHOW_RESOURCE_LIST,
        ATV_MENU_START_AUTOSEARCH,
        ATV_MENU_STOP_AUTOSEARCH,
        ATV_MENU_START_MANUALSEARCH,
        ATV_MENU_STOP_MANUALSEARCH,
        ATV_MENU_GET_SOUNDSYSTEM,
        ATV_MENU_SET_SOUNDSYSTEM,
        ATV_MENU_GET_COLORSYSTEM,
        ATV_MENU_SET_COLORSYSTEM,
        ATV_MENU_GET_CHANNEL_SKIP,
        ATV_MENU_SET_CHANNEL_SKIP,
        ATV_MENU_START_FREQUENCY_FINE,
        ATV_MENU_STOP_FREQUENCY_FINE,
        ATV_MENU_GET_VOLUME_COMPENSATION,
        ATV_MENU_SET_VOLUME_COMPENSATION,
        ATV_MENU_GET_AUDIONIACM,
        ATV_MENU_SET_AUDIONIACM,
        EPG_MENU_SHOW_EPG,
        DVBC_MENU_COLLECT_CHANNEL,
        DVBC_MENU_START_AUTOSEARCH,
        DVBC_MENU_STOP_AUTOSEARCH,
        DVBC_MENU_START_MANUALSEARCH,
        DVBC_MENU_STOP_MANUALSEARCH,
        DVBC_MENU_START_NITSEARCH,
        DVBC_MENU_STOP_NITSEARCH,
        DVBC_MENU_FREQUENCY_SET,
        DVBC_MENU_GET_CAOPERATORLIST,
        DVBC_MENU_START_SERVICE_TV,
        DVBC_MENU_START_SERVICE_BROADCAST,
        DVBC_MENU_GET_SOUNDCHANNEL,
        DVBC_MENU_SET_SOUNDCHANNEL,
        DVBC_MENU_GET_SOUNDTRACK,
        DVBC_MENU_SET_SOUNDTRACK,
        DVBC_MENU_START_MAIL,
        DVBC_MENU_GET_SWITCHCHANNELMODE,
        DVBC_MENU_SET_SWITCHCHANNELMODE,
        DVBC_MENU_START_CASELECTOR,
        DVBC_MENU_GET_VOLUMECOMPENSATION,
        DVBC_MENU_SET_VOLUMECOMPENSATION,
        DVBC_MENU_SET_CAOPERATOR,
        DVBC_MENU_START_SELECTCAREGION,
        DVBC_MENU_SHOW_CACARD_INFO,
        DVBC_MENU_SHOW_CHANNEL_INFO,
        DVBC_MENU_SHOW_CA_SETTING,
        DVBC_MENU_SHOW_IRDETOPROGRAM_INFO,
        DVBC_MENU_SHOW_IRDETOLOAD_INFO,
        DVBC_MENU_SHOW_IRDETOEMM_INFO,
        DVBC_MENU_SHOW_IRDETOECM_INFO,
        DVBC_MENU_START_QHTFOTHER,
        DVBC_MENU_START_CONAXOTHER,
        DVBC_MENU_START_CAPWD,
        DVBC_MENU_START_CAMATURITY,
        DVBC_MENU_START_CAPURSE_INFO,
        DVBC_MENU_START_CAIPPV_INFO,
        DVBC_MENU_START_CAMOTHER_INFO,
        DVBC_MENU_START_QHTFWORKTIME,
        DVBC_MENU_START_NEATHOINFO,
        DVBC_MENU_START_ATHOINFO,
        DVBC_MENU_START_QHTFFEATURE,
        DVBC_MENU_START_CAPAIR,
        DVBC_MENU_START_DVNPINMODIFY,
        DVBC_MENU_START_WATCHLEVEL,
        DVBC_MENU_START_DVNTRADEINFO,
        DVBC_MENU_START_CAIPPV_SET,
        DVBC_MENU_START_CAFACTORY_SET,
        DVBC_MENU_GET_CHANNEL_SKIP,
        DVBC_MENU_SET_CHANNEL_SKIP,
        DVBC_MENU_GET_SUBTITLE,
        DVBC_MENU_SET_SUBTITLE,
        DVBC_MENU_SHOW_SERVICE,
        DTMB_MENU_COLLECT_CHANNEL,
        DTMB_MENU_START_AUTOSEARCH,
        DTMB_MENU_STOP_AUTOSEARCH,
        DTMB_MENU_START_MANUALSEARCH,
        DTMB_MENU_STOP_MANUALSEARCH,
        DTMB_MENU_SHOW_CHANNEL_INFO,
        DTMB_MENU_GET_CHANNEL_SKIP,
        DTMB_MENU_SET_CHANNEL_SKIP,
        DTMB_MENU_GET_SUBTITLE,
        DTMB_MENU_SET_SUBTITLE,
        DTV_MENU_SHOW_DTV_INFO,
        DTV_MENU_SHOW_DTMB_INFO,
        VGA_MENU_AUTO_ADJUST,
        VGA_MENU_GET_HPOSITION,
        VGA_MENU_SET_HPOSITION,
        VGA_MENU_GET_VPOSITION,
        VGA_MENU_SET_VPOSITION,
        VGA_MENU_GET_PHASE,
        VGA_MENU_SET_PHASE,
        VGA_MENU_GET_REFRESHRATE,
        VGA_MENU_SET_REFRESHRATE,
        AV_MENU_GET_COLORSYSTEM,
        AV_MENU_SET_COLORSYSTEM,
        ON_SIGNAL_PLUG_STATE_CHANGE_UI_CB,
        CLOSE_SEARCHGUIDE
    }

    public static String getCmd(String str) {
        return "horion://com.ktc.colorful.uilogic/com.ktc.service.scenceservice.TVScenceService?cmd=" + str;
    }

    public static String getCmd(String str, String str2) {
        return str + CMD + str2;
    }

    public static String getCmd(String str, boolean z, boolean z2) {
        return "horion://com.ktc.colorful.uilogic/com.ktc.service.scenceservice.TVScenceService?cmd=" + str + NEEDACK + z + OVERRIDE + z2;
    }
}

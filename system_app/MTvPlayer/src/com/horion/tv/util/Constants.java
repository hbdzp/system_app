package com.horion.tv.util;

/**
 * Created by xiacf on 2016/12/31.
 */

public class Constants {
    public  enum SOURCE_NAME_ENUM {
        HDMI,
        AV,
        ATV,
        DVBC,
        DTMB,
        YUV,
        VGA,
        VOD,
        REMEMBER,
        EXTERNAL,
        IPLIVE
    }

    //for save previous source
    public final static String PREFERENCES_INPUT_SOURCE = "INPUT_SOURCE";
    public final static String PREFERENCES_PREVIOUS_INPUT_SOURCE = "PREVIOUS_INPUT_SOURCE";
	
    // short
    public static final short CDCA_RC_OK = 0x00;
    public static final short CDCA_RC_UNKNOWN = 0x01;
    public static final short CDCA_RC_POINTER_INVALID = 0x02;
    public static final short CDCA_RC_CARD_INVALID = 0x03;
    public static final short CDCA_RC_PIN_INVALID = 0x04;
    public static final short CDCA_RC_DATASPACE_SMALL = 0x06;
    public static final short CDCA_RC_CARD_PAIROTHER = 0x07;
    public static final short CDCA_RC_DATA_NOT_FIND = 0x08;
    public static final short CDCA_RC_PROG_STATUS_INVALID = 0x09;
    public static final short CDCA_RC_CARD_NO_ROOM = 0x0A;
    public static final short CDCA_RC_WORKTIME_INVALID = 0x0B;
    public static final short CDCA_RC_IPPV_CANNTDEL = 0x0C;
    public static final short CDCA_RC_CARD_NOPAIR = 0x0D;
    public static final short CDCA_RC_WATCHRATING_INVALID = 0x0E;
    public static final short CDCA_RC_CARD_NOTSUPPORT = 0x0F;
    public static final short CDCA_RC_DATA_ERROR = 0x10;
    public static final short CDCA_RC_FEEDTIME_NOT_ARRIVE = 0x11;
    public static final short CDCA_Detitle_All_Read = 0x00;
    public static final short CDCA_Detitle_Received = 0x01;
    public static final short CDCA_Detitle_Space_Small = 0x02;
    public static final short CDCA_Detitle_Ignore = 0x03;
    // boolean
    public static boolean lockKey = true;
    // String
    public static final String OPMODE = "OP_MODE";
    public static final String TUNER_AVAIABLE = "TUNER_AVAILABLE";
    public static final String LNBOPTION_PAGETYPE = "LNBOPTION_PAGETYPE";
    public static final String LNBOPTION_EDITOR_PAGETYPE = "LNBOPTION_EDITOR_PAGETYPE";
    public static final String LNBOPTION_EDITOR_ACTIONTYPE = "LNBOPTION_EDITOR_ACTIONTYPE";
    public static final String LNBOPTION_EDITOR_INDEX = "LNBOPTION_EDITOR_INDEX";
    public static final String LNBOPTION_MOTOR_ACTIONTYPE = "LNBOPTION_MOTOR_ACTIONTYPE";
    public static final String LNBMOTOR_EDITOR_DISEQC_VERSION = "DISEQC_VERSION";
    public static final String LNBMOTOR_EDITOR_DISEQC_1_2 = "1.2";
    public static final String LNBMOTOR_EDITOR_DISEQC_1_3 = "1.3";
    public static final String LNBOPTION_MOTOR_FOCUS = "LNBOPTION_MOTOR_FOCUS";
    public static final String PREFERENCES_TV_SETTING = "TvSetting";
    public static final String PREFERENCES_IS_AUTOSCAN_LAUNCHED = "autoTuningLaunchedBefore";
    public static final String TV_EVENT_LISTENER_READY = "TVEventListenerReady";
    // float
    public static final float CCKEY_TEXTSIZE = 40.0f;
    public static final float CCKEY_ALPHA = 0.6f;
    // int
    public static final int TV_SCREENSAVER_NOSIGNAL = 80;
    public static final int ROOTACTIVITY_OAD_DOWNLOAD_TIMEOUT = 600;
    public static final int ROOTACTIVITY_OAD_DOWNLOAD_UI_TIMEOUT = 601;
    public static final int ROOTACTIVITY_TVPROMINFOREADY_MESSAGE = 700;
    public static final int ROOTACTIVITY_RESUME_MESSAGE = 800;
    public static final int ROOTACTIVITY_CREATE_MESSAGE = 900;
    public static final int ROOTACTIVITY_CANCEL_DIALOG = 8000;
    public static final int ROOTACITIVYT_DELAY_SEARCH_CHANNLE=3000;
    public static final int CEC_STATUS_ON = 1;
    public static final int CHANNEL_LOCK_RESULT_CODE = 100;
    public static final int LNBOPTION_PAGETYPE_INVALID = -1;
    public static final int LNBOPTION_PAGETYPE_SATELLITE = 0;
    public static final int LNBOPTION_PAGETYPE_TRANSPONDER = 1;
    public static final int LNBOPTION_PAGETYPE_FREQUENCIES = 2;
    public static final int LNBOPTION_PAGETYPE_MOTOR = 3;
    public static final int LNBOPTION_PAGETYPE_SINGLECABLE = 4;
    public static final int LNBOPTION_EDITOR_ACTION_INVALID = -1;
    public static final int LNBOPTION_EDITOR_ACTION_ADD = 0;
    public static final int LNBOPTION_EDITOR_ACTION_EDIT = 1;
    public static final int LNBOPTION_MOTOR_NONE = 0;
    public static final int LNBOPTION_MOTOR_1_2 = 1;
    public static final int LNBOPTION_MOTOR_1_3 = 2;
    public static final int LNBOPTION_MOTOR_ACTION_INVALID = -1;
    public static final int LNBOPTION_MOTOR_ACTION_POSITION = 0;
    public static final int LNBOPTION_MOTOR_ACTION_LIMIT = 1;
    public static final int LNBOPTION_MOTOR_ACTION_LOCATION = 2;

    /*PVR PVR_CREATE_MODE extra int contant, NONE*/
    public static final int PVR_NONE = 0;
    /*PVR PVR_CREATE_MODE extra int contant, stop playback*/
    public static final int PVR_PLAYBACK_STOP = 1;
    /*PVR PVR_CREATE_MODE extra int contant, playback*/
    public static final int PVR_PLAYBACK_START = 2;
    /*PVR PVR_CREATE_MODE extra int contant, playback from browser*/
    public static final int PVR_PLAYBACK_FROM_BROWSER = 3;
    /*PVR PVR_CREATE_MODE extra int contant, (always)timeshift playback pause*/
    public static final int PVR_PLAYBACK_PAUSE = 4;
    /*PVR PVR_CREATE_MODE extra int contant, always timeshift jump forward*/
    public static final int PVR_PLAYBACK_PREVIOUS = 5;
    /*PVR PVR_CREATE_MODE extra int contant, always timeshift fastbackward*/
    public static final int PVR_PLAYBACK_REWIND = 6;
    /*PVR PVR_CREATE_MODE extra int contant, stop record*/
    public static final int PVR_RECORD_STOP = 10;
    /*PVR PVR_CREATE_MODE extra int contant, schedule record or one touch record*/
    public static final int PVR_RECORD_START = 11;

    // class
    public class SignalProgSyncStatus {

        public static final int NOSYNC = 0x00;

        public static final int STABLE_SUPPORT_MODE = 0x01;

        public static final int STABLE_UN_SUPPORT_MODE = 0x02;

        public static final int UNSTABLE = 0x03;

        public static final int AUTO_ADJUST = 0x04;
    };

    public class ScreenSaverMode {

        public static final int DTV_SS_INVALID_SERVICE = 0x00;

        public static final int DTV_SS_NO_CI_MODULE = 0x01;

        public static final int DTV_SS_CI_PLUS_AUTHENTICATION = 0x02;

        public static final int DTV_SS_SCRAMBLED_PROGRAM = 0x03;

        public static final int DTV_SS_CH_BLOCK = 0x04;

        public static final int DTV_SS_PARENTAL_BLOCK = 0x05;

        public static final int DTV_SS_AUDIO_ONLY = 0x06;

        public static final int DTV_SS_DATA_ONLY = 0x07;

        public static final int DTV_SS_COMMON_VIDEO = 0x08;

        public static final int DTV_SS_UNSUPPORTED_FORMAT = 0x09;

        public static final int DTV_SS_INVALID_PMT = 0x0A;

        public static final int DTV_SS_MAX = 0x0B;

        public static final int DTV_SS_CA_NOTIFY = 0x0C;
    };

    /*
     * Set extra int ListId = SHOW_FAVORITE_LIST To show only favorite programs
     * @see com.mstar.tv.tvplayer.ui.channel.ChannelListActivity
     */
    public final static int SHOW_FAVORITE_LIST = 1;

    /*
     * Set extra int ListId = SHOW_PROGRAM_LIST To show all programs
     * @see com.mstar.tv.tvplayer.ui.channel.ChannelListActivity
     */
    public final static int SHOW_PROGRAM_LIST = 2;

}

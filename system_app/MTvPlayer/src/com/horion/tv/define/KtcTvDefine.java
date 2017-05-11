package com.horion.tv.define;

public class KtcTvDefine {
    public static final String KTCLAUNCHER_STARTTYPE = "startType";
    public static final String KTCLAUNCHER_STARTTYPE_ONBOOT = "onBoot";
    public static final String KTCLAUNCHER_STARTTYPE_ONSTR = "onStr";
    public static final String KTCLAUNCHER_STARTTYPE_OTHER = "other";
    public static final String KTC_SHARE_DATA_KEY_CALOCATION = "env_location";

    public enum ATVAUDIONICAM {
        ATV_AUDIOMODE_NICAM_MONO,
        ATV_AUDIOMODE_NICAM_STEREO,
        ATV_AUDIOMODE_NICAM_DUAL_A,
        ATV_AUDIOMODE_NICAM_DUAL_B,
        ATV_AUDIOMODE_NICAM_DUAL_AB
    }

    public enum AUDIOSTREAM_TYPE {
        AV_AUD_STREAM_TYPE_UNKOWN,
        AV_AUD_STREAM_TYPE_MPEG1,
        AV_AUD_STREAM_TYPE_MPEG,
        AV_AUD_STREAM_TYPE_MP3,
        AV_AUD_STREAM_TYPE_AAC,
        AV_AUD_STREAM_TYPE_AAC_PLUS,
        AV_AUD_STREAM_TYPE_AC3,
        AV_AUD_STREAM_TYPE_AC3_PLUS,
        AV_AUD_STREAM_TYPE_DTS,
        AV_AUD_STREAM_TYPE_LPCM_BLURAY,
        AV_AUD_STREAM_TYPE_PCM,
        AV_AUD_STREAM_TYPE_WMA_STD,
        AV_AUD_STREAM_TYPE_WMA_PRO,
        AV_AUD_STREAM_TYPE_DRA,
        AV_AUD_STREAM_TYPE_COOK,
        AV_AUD_STREAM_TYPE_AVS,
        AV_AUD_STREAM_TYPE_WAVE,
        AV_AUD_STREAM_TYPE_INVALID,
        AV_AUD_STREAM_TYPE_DRA10,
        AV_AUD_STREAM_TYPE_DRA11,
        AV_AUD_STREAM_TYPE_DRA20,
        AV_AUD_STREAM_TYPE_DRA21,
        AV_AUD_STREAM_TYPE_DRA30,
        AV_AUD_STREAM_TYPE_DRA31,
        AV_AUD_STREAM_TYPE_DRA40,
        AV_AUD_STREAM_TYPE_DRA41,
        AV_AUD_STREAM_TYPE_DRA50,
        AV_AUD_STREAM_TYPE_DRA51
    }

    public enum COLORSYSTEM {
        PAL,
        NTSC,
        PAL_BGHI,
        NTSC_M,
        SECAM,
        NTSC_44,
        PAL_M,
        PAL_N,
        PAL_60,
        NOTSTANDARD,
        AUTO
    }

    public enum SOUNDSYSTEM {
        I,
        DK,
        BG,
        M,
        L,
        AUTO
    }

    public enum SOURCE_SIGNAL_STATE {
        PLAY,
        NOSIGNAL,
        NOEPG,
        NET,
        NONET,
        CURRENTCHANNEL_NO_PLAY
    }

    public enum SwitchSourceType {
        BY_COMMON,
        BY_KEY_SOURCE,
        BY_KEY_BACK,
        BY_STANDBY
    }
}

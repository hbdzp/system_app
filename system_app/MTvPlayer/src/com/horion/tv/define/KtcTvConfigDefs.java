package com.horion.tv.define;

import com.horion.framework.player.kernel.parameter.KtcPlayerParameter.KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE;

public class KtcTvConfigDefs {
    private static  int[] KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPESwitchesValues;
    private static  int[] KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPESwitchesValues;

    public enum KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE {
        KTC_CFG_TVSDK_DISPLAY_MODE_16_9,
        KTC_CFG_TVSDK_DISPLAY_MODE_4_3,
        KTC_CFG_TVSDK_DISPLAY_MODE_AUTO,
        KTC_CFG_TVSDK_DISPLAY_MODE_MOVIE,
        KTC_CFG_TVSDK_DISPLAY_MODE_CAPTION,
        KTC_CFG_TVSDK_DISPLAY_MODE_PANORAMA,
        KTC_CFG_TVSDK_DISPLAY_MODE_PERSON,
        KTC_CFG_TVSDK_DISPLAY_MODE_INVALID
    }

    public enum KTC_CFG_TV_SOURCE_SELECT_ENUM_TYPE {
        KTC_CFG_TV_SOURCE_REMEMBER,
        KTC_CFG_TV_SOURCE_ATV,
        KTC_CFG_TV_SOURCE_DTV,
        KTC_CFG_TV_SOURCE_AV,
        KTC_CFG_TV_SOURCE_AV0,
        KTC_CFG_TV_SOURCE_AV1,
        KTC_CFG_TV_SOURCE_AV2,
        KTC_CFG_TV_SOURCE_YUV,
        KTC_CFG_TV_SOURCE_HDMI1,
        KTC_CFG_TV_SOURCE_HDMI2,
        KTC_CFG_TV_SOURCE_HDMI3,
        KTC_CFG_TV_SOURCE_PC,
        KTC_CFG_TV_SOURCE_IPLIVE,
        KTC_CFG_TV_SOURCE_LOOKBACK,
        KTC_CFG_TV_SOURCE_TIMESHIFT,
        KTC_CFG_TV_SOURCE_DTVAPK,
        KTC_CFG_TV_SOURCE_DTVDTMB,
        KTC_CFG_TV_SOURCE_DVBC,
        KTC_CFG_TV_SOURCE_DTVDTMBAPK,
        KTC_CFG_TV_SOURCE_IPDEMO,
        KTC_CFG_TV_SOURCE_IPTV,
        KTC_CFG_TV_SOURCE_EXTERNAL,
        KTC_CFG_TV_SOURCE_INVALID
    }

    private static  int[] getKTC_CFG_TV_DISPLAY_MODE_ENUM_TYPESwitchesValues() {
        if (KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPESwitchesValues != null) {
            return KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPESwitchesValues;
        }
        int[] iArr = new int[KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.values().length];
        try {
            iArr[KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_16_9.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_4_3.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_AUTO.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_CAPTION.ordinal()] = 15;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_INVALID.ordinal()] = 4;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_MOVIE.ordinal()] = 5;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_PANORAMA.ordinal()] = 6;
        } catch (NoSuchFieldError e7) {
        }
        try {
            iArr[KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_PERSON.ordinal()] = 7;
        } catch (NoSuchFieldError e8) {
        }
        KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPESwitchesValues = iArr;
        return iArr;
    }

    private static int[] getKTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPESwitchesValues() {
        if (KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPESwitchesValues != null) {
            return KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPESwitchesValues;
        }
        int[] iArr = new int[KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.values().length];
        try {
            iArr[KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_16_9.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_4_3.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_AUTO.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_CAPTION.ordinal()] = 15;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_INVALID.ordinal()] = 4;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_MOVIE.ordinal()] = 5;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_PANORAMA.ordinal()] = 6;
        } catch (NoSuchFieldError e7) {
        }
        try {
            iArr[KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_PERSON.ordinal()] = 7;
        } catch (NoSuchFieldError e8) {
        }
        KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPESwitchesValues = iArr;
        return iArr;
    }

    public static KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE getPlayerSDKDisplayModeEnumTypeFromTVSDK(KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE ktc_cfg_tvsdk_display_mode_enum_type) {
        KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE ktc_cfg_tv_display_mode_enum_type = KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_16_9;
        switch (getKTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPESwitchesValues()[ktc_cfg_tvsdk_display_mode_enum_type.ordinal()]) {
            case 1:
                return KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_16_9;
            case 2:
                return KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_4_3;
            case 3:
                return KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_AUTO;
            case 4:
                return KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_INVALID;
            case 5:
                return KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_MOVIE;
            case 6:
                return KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_PANORAMA;
            case 7:
                return KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TV_DISPLAY_MODE_PERSON;
            default:
                return ktc_cfg_tv_display_mode_enum_type;
        }
    }

    public static KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE getTvSDKDisplayModeEnumTypeFromPlayerSDK(KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE ktc_cfg_tv_display_mode_enum_type) {
        KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE ktc_cfg_tvsdk_display_mode_enum_type = KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_16_9;
        switch (getKTC_CFG_TV_DISPLAY_MODE_ENUM_TYPESwitchesValues()[ktc_cfg_tv_display_mode_enum_type.ordinal()]) {
            case 1:
                return KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_16_9;
            case 2:
                return KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_4_3;
            case 3:
                return KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_AUTO;
            case 4:
                return KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_INVALID;
            case 5:
                return KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_MOVIE;
            case 6:
                return KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_PANORAMA;
            case 7:
                return KTC_CFG_TVSDK_DISPLAY_MODE_ENUM_TYPE.KTC_CFG_TVSDK_DISPLAY_MODE_PERSON;
            default:
                return ktc_cfg_tvsdk_display_mode_enum_type;
        }
    }
}

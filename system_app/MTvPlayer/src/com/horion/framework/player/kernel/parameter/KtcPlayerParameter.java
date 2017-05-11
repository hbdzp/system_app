package com.horion.framework.player.kernel.parameter;

public class KtcPlayerParameter {

    public enum KTC_CFG_TV_DISPLAY_MODE_ENUM_TYPE {
        KTC_CFG_TV_DISPLAY_MODE_16_9,
        KTC_CFG_TV_DISPLAY_MODE_4_3,
        KTC_CFG_TV_DISPLAY_MODE_AUTO,
        KTC_CFG_TV_DISPLAY_MODE_MOVIE,
        KTC_CFG_TV_DISPLAY_MODE_CAPTION,
        KTC_CFG_TV_DISPLAY_MODE_PANORAMA,
        KTC_CFG_TV_DISPLAY_MODE_PERSON,
        KTC_CFG_TV_DISPLAY_MODE_INVALID
    }

    public enum KtcPlayerDecoder {
        SOFTMPEG,
        HARDMPEG,
        MEDIAPLAYER
    }

    public enum KtcPlayerError {
        VIDEO_FORMAT_UNSUPPORT,
        AUDIO_FORMAT_UNSUPPORT,
        OPEN_FILE_ERROR,
        ERROR_HTTP_SERVER,
        ERROR_IO,
        ERROR_DECODE,
        ERROR_CANNOT_CONNECT,
        ERROR_UNKNOWN_HOST,
        URL_NULL_I,
        URL_NULL_II,
        URL_NULL_III,
        ERROR_UNKNOWN,
        NO_SUPPORT_URL,
        NO_PLAY_URL,
        PARSE_ERROR,
        NET_EXCEPTION,
        NO_PARSER_PLUGIN
    }

    public enum KtcPlayerInfo {
        SUBTITLE_UPDATA,
        BUFFERING_START,
        BUFFERING_END,
        BUFFERING_PROCESS,
        NO_SUPPORT_AUDIO_DECODE,
        NO_SUPPORT_VIDEO_DECODE,
        LOAD_PLAYER_PLUGIN_ERROR,
        OTHER_INFO
    }

    public enum KtcPlayerKernelType {
        KERNEL_BJ,
        KERNEL_PLATFORM
    }

    public enum KtcPlayerSoundChannel {
        SOUND_LEFT,
        SOUND_STEREO,
        SOUND_RIGHT
    }

    public enum KtcPlayerState {
        STATE_IDLE,
        STATE_CHECKING_DATABASE,
        STATE_CHECKED_DATABASE,
        STATE_PARSING_URL,
        STATE_PARSED_URL,
        STATE_ERROR,
        STATE_PREPARING,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED,
        STATE_BUFFERING_START,
        STATE_BUFFERING_PROCESS,
        STATE_BUFFERING_END,
        STATE_COMPLETED,
        STATE_STOPED,
        STATE_INITIATIVE_EXITED,
        STATE_PASSIVE_EXITED
    }
}

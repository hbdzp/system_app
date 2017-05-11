package com.horion.tv.service.epg;

import com.horion.tv.define.object.Channel;
import com.horion.tv.define.object.Source;
import com.horion.tv.ui.leftepg.EPGMODE;
import com.horion.tv.ui.leftepg.SkyEPGData;

import java.util.List;

public class SkyLeftEPGParam {

    public enum GET_REMAINCHANNEL_MODE {
        KEYRIGHT,
        KEYUP,
        KEYDOWN
    }

    public static class GET_REMAINCHANNEL_PARAM {
        public int currentIndex = -1;
        public EPGMODE epgMode = EPGMODE.EPGMODE_DIS;
        public GET_REMAINCHANNEL_MODE mode = GET_REMAINCHANNEL_MODE.KEYRIGHT;
        public List<SkyEPGData> uiChannelItem;

        public GET_REMAINCHANNEL_PARAM(GET_REMAINCHANNEL_MODE get_remainchannel_mode, int i, List<SkyEPGData> list, EPGMODE epgmode) {
            this.mode = get_remainchannel_mode;
            this.currentIndex = i;
            this.uiChannelItem = list;
            this.epgMode = epgmode;
        }
    }

    public static class RESUME_CHANNELLIST_PARAM {
        public List<Channel> resumeChannelList;
        public List<Integer> resumeIndexList;
        public Source sourceIn;

        public RESUME_CHANNELLIST_PARAM(List<Channel> list, List<Integer> list2, Source source) {
            this.resumeChannelList = list;
            this.resumeIndexList = list2;
            this.sourceIn = source;
        }
    }

    public static class SORT_CHANNELLIST_PARAM {
        public List<Channel> sortChannelList;
        public List<Integer> sortIndexList;
        public Source sourceIn;

        public SORT_CHANNELLIST_PARAM(List<Channel> list, List<Integer> list2, Source source) {
            this.sortChannelList = list;
            this.sortIndexList = list2;
            this.sourceIn = source;
        }
    }
}

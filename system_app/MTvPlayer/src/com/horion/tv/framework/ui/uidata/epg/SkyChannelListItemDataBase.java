package com.horion.tv.framework.ui.uidata.epg;

public class SkyChannelListItemDataBase {
    private String channelName = null;
    private String eventInfo = null;
    private Object obj = null;

    public String getChannelName() {
        return this.channelName;
    }

    public String getEventInfo() {
        return this.eventInfo;
    }

    public Object getObject() {
        return this.obj;
    }

    public void setChannelName(String str) {
        this.channelName = str;
    }

    public void setEventInfo(String str) {
        this.eventInfo = str;
    }

    public void setObject(Object obj) {
        this.obj = obj;
    }
}

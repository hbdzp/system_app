package com.horion.tv.framework.ui.uidata.epg;

public class SkyEPGCurProgItemData extends SkyEPGListItemDataBase {
    public String progNameStr;
    public String progTimeStr;
    public ProgType progType;

    public enum ProgType {
        LOOKBACK,
        PLAY,
        ATTENTIONED,
        NULL
    }

    public SkyEPGCurProgItemData(String str, String str2, ProgType progType) {
        this.progTimeStr = str;
        this.progNameStr = str2;
        this.progType = progType;
    }

    public void setProgType(ProgType progType) {
        this.progType = progType;
    }
}

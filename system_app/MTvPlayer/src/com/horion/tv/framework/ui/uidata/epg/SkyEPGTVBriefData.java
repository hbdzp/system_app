package com.horion.tv.framework.ui.uidata.epg;

public class SkyEPGTVBriefData {
    public String current_info;
    public String current_time;
    public String next_info;
    public String next_time;

    public SkyEPGTVBriefData(String str, String str2, String str3, String str4) {
        this.current_info = str;
        this.current_time = str2;
        this.next_info = str3;
        this.next_time = str4;
    }
}

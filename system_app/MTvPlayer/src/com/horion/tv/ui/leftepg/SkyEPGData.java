package com.horion.tv.ui.leftepg;

import com.horion.tv.define.object.Channel.CHANNEL_TYPE;

import java.io.Serializable;

public class SkyEPGData implements Serializable {
    private static final long serialVersionUID = 1;
    public String channelNumber;
    public CHANNEL_TYPE channelType = CHANNEL_TYPE.TV;
    private Object data;
    private int indexID = 0;
    public SkyEPGData next;
    public SkyEPGData pre;
    private String secondTitle = null;
    private SECONDTITLE_STATE secondTitleState = SECONDTITLE_STATE.NOTTRY;
    private String title = null;
    private int serviceType;

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public enum SECONDTITLE_STATE {
        NOTTRY,
        TRYSUCCESS,
        TRYERROR
    }

    public String getChannelNumber() {
        return this.channelNumber;
    }

    public CHANNEL_TYPE getChannelType() {
        return this.channelType;
    }

    public Object getData() {
        return this.data;
    }

    public int getItemIndexID() {
        return this.indexID;
    }

    public String getItemSecondTitle() {
        return this.secondTitle;
    }

    public SECONDTITLE_STATE getItemSecondTitleState() {
        return this.secondTitleState;
    }

    public String getItemTitle() {
        return this.title;
    }

    public void setChannelNumber(String str) {
        this.channelNumber = str;
    }

    public void setChannelType(CHANNEL_TYPE channel_type) {
        this.channelType = channel_type;
    }

    public void setData(Object obj) {
        this.data = obj;
    }

    public void setItemIndexID(int i) {
        this.indexID = i;
    }

    public void setItemSecondTitleState(SECONDTITLE_STATE secondtitle_state) {
        this.secondTitleState = secondtitle_state;
    }

    public void setItemSecondTitleTitle(String str) {
        this.secondTitle = str;
    }

    public void setItemTitle(String str) {
        this.title = str;
    }
}

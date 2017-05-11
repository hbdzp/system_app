package com.horion.tv.define.object;

import java.util.ArrayList;
import java.util.List;

public class Channel extends SkyTvObject {
    private static final transient int FLAG_FALSE = 0;
    private static final transient int FLAG_TRUE = 1;
    private static final long serialVersionUID = 7376786160341585049L;
    public boolean bAfcEnable = true;
    public boolean bSkip = false;
    public int ch_type = 0;
    private int flag = 0;
    public String icon = "";
    public boolean isDeleted = false;
    public List<OnlineResource> liveOnlineResourceList = new ArrayList();
    public int mapindex = -1;
    public String search_key_word = "";
    public List<OnlineResource> seekResourceList = new ArrayList();
    public int service_id = -1;
    public Source source = Source.ATV();
    public CHANNEL_TYPE type = CHANNEL_TYPE.TV;

    public enum CHANNEL_TYPE {
        TV,
        RADIO
    }

    private enum FLAG_CHANNEL {
        FLAG_ENABLE,
        FLAG_SKIP
    }

    public Channel(String str) {
        super(str);
        setDefaultFlag();
    }

    public Channel(String id, String name) {
        super(id, name);
        setDefaultFlag();
    }

    private boolean getFlag(FLAG_CHANNEL flag_channel) {
        boolean z = true;
        synchronized (this) {
            if (((this.flag & (1 << flag_channel.ordinal())) >> flag_channel.ordinal()) != 1) {
                z = false;
            }
        }
        return z;
    }

    private void setDefaultFlag() {
        setEnable(true);
        setSkip(false);
    }

    private void setFlag(boolean z, FLAG_CHANNEL flag_channel) {
        synchronized (this) {
            int ordinal = this.flag & (1 << flag_channel.ordinal());
            if (z) {
                this.flag = ordinal | (1 << flag_channel.ordinal());
            } else {
                this.flag = ordinal | (0 << flag_channel.ordinal());
            }
        }
    }

    protected void doAfterDeserialize() {
        this.source.afterDeserialize();
    }

    public boolean getEnable() {
        return getFlag(FLAG_CHANNEL.FLAG_ENABLE);
    }

    public boolean getSkip() {
        return getFlag(FLAG_CHANNEL.FLAG_SKIP);
    }

    public Source getSource() {
        return this.source;
    }

    public void setEnable(boolean z) {
        setFlag(z, FLAG_CHANNEL.FLAG_ENABLE);
    }

    public void setSkip(boolean z) {
        setFlag(z, FLAG_CHANNEL.FLAG_SKIP);
    }
}

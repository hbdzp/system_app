package com.ktc.panasonichome.view.ktc;

import java.io.Serializable;

public abstract class TCSetData implements Serializable {
    protected boolean enable                = true;
    protected long    endProcessTimestamp   = 0;
    protected String  name                  = null;
    protected long    startProcessTimestamp = 0;
    protected String  type                  = null;
    protected String  value                 = null;

    public TCSetData(String type) {
        this.type = type;
    }

    public abstract byte[] toBytes();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getStartProcessTimestamp() {
        return this.startProcessTimestamp;
    }

    public void setStartProcessTimestamp(long startProcessTimestamp) {
        this.startProcessTimestamp = startProcessTimestamp;
    }

    public long getEndProcessTimestamp() {
        return this.endProcessTimestamp;
    }

    public void setEndProcessTimestamp(long endProcessTimestamp) {
        this.endProcessTimestamp = endProcessTimestamp;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public enum SkyConfigType {
        SKY_CONFIG_NONE,
        SKY_CONFIG_SINGLE,
        SKY_CONFIG_RANGE,
        SKY_CONFIG_ENUM,
        SKY_CONFIG_INPUT_VALUE,
        SKY_CONFIG_INFO,
        SKY_CONFIG_SWITCH,
        SKY_CONFIG_RET
    }


}

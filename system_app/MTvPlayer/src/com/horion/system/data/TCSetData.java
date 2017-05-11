package com.horion.system.data;

import com.ktc.framework.ktcsdk.plugins.KtcPluginParam;
import com.ktc.framework.ktcsdk.util.KtcDataDecomposer;
import java.io.Serializable;

public abstract class TCSetData implements Serializable {
    protected boolean enable = true;
    protected long endProcessTimestamp = 0;
    protected String name = null;
    protected KtcPluginParam pluginValue = null;
    protected long startProcessTimestamp = 0;
    protected String type = null;
    protected String value = null;

    public enum KtcConfigType {
        KTC_CONFIG_NONE,
        KTC_CONFIG_SINGLE,
        KTC_CONFIG_RANGE,
        KTC_CONFIG_ENUM,
        KTC_CONFIG_INPUT_VALUE,
        KTC_CONFIG_INFO,
        KTC_CONFIG_SWITCH,
        KTC_CONFIG_RET
    }

    public TCSetData(String str) {
        this.type = str;
    }

    protected static KtcConfigType getType(byte[] bArr) {
        try {
            String str = new String(bArr);
            if (str != null) {
                return KtcConfigType.valueOf(new KtcDataDecomposer(str).getStringValue("type"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getEndProcessTimestamp() {
        return this.endProcessTimestamp;
    }

    public String getName() {
        return this.name;
    }

    public KtcPluginParam getPluginValue() {
        return this.pluginValue;
    }

    public long getStartProcessTimestamp() {
        return this.startProcessTimestamp;
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public void setEnable(boolean z) {
        this.enable = z;
    }

    public void setEndProcessTimestamp(long j) {
        this.endProcessTimestamp = j;
    }

    public void setName(String str) {
        this.name = str;
    }

    public void setPluginValue(KtcPluginParam ktcPluginParam) {
        this.pluginValue = ktcPluginParam;
    }

    public void setStartProcessTimestamp(long j) {
        this.startProcessTimestamp = j;
    }

    public void setType(String str) {
        this.type = str;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public abstract byte[] toBytes();
}

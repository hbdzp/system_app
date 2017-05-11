package com.horion.system.data;

import com.ktc.framework.ktcsdk.plugins.KtcPluginParam;
import com.ktc.framework.ktcsdk.util.KtcDataComposer;
import com.ktc.framework.ktcsdk.util.KtcDataDecomposer;

public class TCRetData extends TCSetData {
    public static TCRetData FALSE = new TCRetData(false);
    public static TCRetData TRUE = new TCRetData(true);
    boolean mSuccess = false;

    public TCRetData() {
        super(KtcConfigType.KTC_CONFIG_RET.toString());
    }

    public TCRetData(KtcPluginParam ktcPluginParam) {
        super(KtcConfigType.KTC_CONFIG_RET.toString());
        deserialize(ktcPluginParam);
    }

    public TCRetData(String str) {
        super(KtcConfigType.KTC_CONFIG_RET.toString());
        deserialize(str);
    }

    public TCRetData(boolean z) {
        super(KtcConfigType.KTC_CONFIG_RET.toString());
        this.mSuccess = z;
    }

    public TCRetData(byte[] bArr) {
        super(KtcConfigType.KTC_CONFIG_RET.toString());
        if (bArr != null) {
            deserialize(new String(bArr));
        }
    }

    private void deserialize(KtcPluginParam ktcPluginParam) {
        this.pluginValue = ktcPluginParam;
        this.type = (String) ktcPluginParam.get("type", String.class);
        this.mSuccess = ((Boolean) ktcPluginParam.get("success", Boolean.class)).booleanValue();
    }

    private void deserialize(String str) {
        KtcDataDecomposer ktcDataDecomposer = new KtcDataDecomposer(str);
        this.value = str;
        this.type = ktcDataDecomposer.getStringValue("type");
        this.name = ktcDataDecomposer.getStringValue("name");
        this.mSuccess = ktcDataDecomposer.getBooleanValue("success");
        if (ktcDataDecomposer.getStringValue("start") != null) {
            this.startProcessTimestamp = Long.valueOf(ktcDataDecomposer.getStringValue("start")).longValue();
            this.endProcessTimestamp = Long.valueOf(ktcDataDecomposer.getStringValue("end")).longValue();
        }
    }

    public boolean isSuccess() {
        return this.mSuccess;
    }

    public void setSuccess(boolean z) {
        this.mSuccess = z;
    }

    public byte[] toBytes() {
        String tCRetData = toString();
        return tCRetData != null ? tCRetData.getBytes() : null;
    }

    public String toString() {
        KtcDataComposer ktcDataComposer = new KtcDataComposer();
        ktcDataComposer.addValue("type", this.type);
        ktcDataComposer.addValue("name", this.name);
        ktcDataComposer.addValue("success", this.mSuccess);
        ktcDataComposer.addValue("start", String.valueOf(this.startProcessTimestamp));
        ktcDataComposer.addValue("end", String.valueOf(this.endProcessTimestamp));
        return ktcDataComposer.toString();
    }
}

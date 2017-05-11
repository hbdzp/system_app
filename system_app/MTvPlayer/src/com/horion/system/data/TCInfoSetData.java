package com.horion.system.data;

import com.ktc.framework.ktcsdk.plugins.KtcPluginParam;
import com.ktc.framework.ktcsdk.util.KtcDataComposer;
import com.ktc.framework.ktcsdk.util.KtcDataDecomposer;
import com.horion.user.data.UserCmdDefine.UserKeyDefine;

public class TCInfoSetData extends TCSetData {
    private String current = null;

    public TCInfoSetData() {
        super(KtcConfigType.KTC_CONFIG_INFO.toString());
    }

    public TCInfoSetData(KtcPluginParam ktcPluginParam) {
        super(KtcConfigType.KTC_CONFIG_INFO.toString());
        if (ktcPluginParam != null) {
            deserialize(ktcPluginParam);
        }
    }

    public TCInfoSetData(String str) {
        super(KtcConfigType.KTC_CONFIG_INFO.toString());
        if (str != null) {
            deserialize(str);
        }
    }

    public TCInfoSetData(byte[] bArr) {
        super(KtcConfigType.KTC_CONFIG_INFO.toString());
        if (bArr != null) {
            deserialize(new String(bArr));
        }
    }

    private void deserialize(KtcPluginParam ktcPluginParam) {
        this.pluginValue = ktcPluginParam;
        this.type = (String) ktcPluginParam.get("type", String.class);
        this.current = (String) ktcPluginParam.get(UserKeyDefine.KEY_ACCOUNT_CURRENT, String.class);
    }

    private void deserialize(String str) {
        KtcDataDecomposer ktcDataDecomposer = new KtcDataDecomposer(str);
        this.value = str;
        this.type = ktcDataDecomposer.getStringValue("type");
        this.name = ktcDataDecomposer.getStringValue("name");
        this.current = ktcDataDecomposer.getStringValue(UserKeyDefine.KEY_ACCOUNT_CURRENT);
        String stringValue = ktcDataDecomposer.getStringValue("enable");
        if (stringValue == null || stringValue.equals("true")) {
            this.enable = true;
        } else {
            this.enable = false;
        }
        if (ktcDataDecomposer.getStringValue("start") != null) {
            this.startProcessTimestamp = Long.valueOf(ktcDataDecomposer.getStringValue("start")).longValue();
            this.endProcessTimestamp = Long.valueOf(ktcDataDecomposer.getStringValue("end")).longValue();
        }
    }

    public static void main(String[] strArr) {
        TCInfoSetData tCInfoSetData = new TCInfoSetData();
        tCInfoSetData.setCurrent(UserKeyDefine.KEY_ACCOUNT_CURRENT);
        System.out.println("data0=" + tCInfoSetData.toString());
        System.out.println("data1=" + TCSetDataFactory.createSetData(tCInfoSetData.toBytes()).toString());
    }

    public String getCurrent() {
        return this.current;
    }

    public void setCurrent(String str) {
        this.current = str;
    }

    public byte[] toBytes() {
        String tCInfoSetData = toString();
        return tCInfoSetData != null ? tCInfoSetData.getBytes() : null;
    }

    public String toString() {
        KtcDataComposer ktcDataComposer = new KtcDataComposer();
        ktcDataComposer.addValue("type", this.type);
        ktcDataComposer.addValue("name", this.name);
        if (this.current != null) {
            ktcDataComposer.addValue(UserKeyDefine.KEY_ACCOUNT_CURRENT, this.current.toString());
        } else {
            ktcDataComposer.addValue(UserKeyDefine.KEY_ACCOUNT_CURRENT, "");
        }
        ktcDataComposer.addValue("enable", this.enable);
        ktcDataComposer.addValue("start", String.valueOf(this.startProcessTimestamp));
        ktcDataComposer.addValue("end", String.valueOf(this.endProcessTimestamp));
        return ktcDataComposer.toString();
    }
}

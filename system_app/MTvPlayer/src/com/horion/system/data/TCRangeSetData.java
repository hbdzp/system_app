package com.horion.system.data;

import com.ktc.framework.ktcsdk.plugins.KtcPluginParam;
import com.ktc.framework.ktcsdk.util.KtcDataDecomposer;
import com.ktc.framework.ktcsdk.util.KtcDataComposer;
import com.horion.user.data.UserCmdDefine.UserKeyDefine;

public class TCRangeSetData extends TCSetData {
    private int current = 0;
    private int max = 0;
    private int min = 0;

    public TCRangeSetData() {
        super(KtcConfigType.KTC_CONFIG_RANGE.toString());
    }

    public TCRangeSetData(KtcPluginParam ktcPluginParam) {
        super(KtcConfigType.KTC_CONFIG_RANGE.toString());
        if (ktcPluginParam != null) {
            deserialize(ktcPluginParam);
        }
    }

    public TCRangeSetData(String str) {
        super(KtcConfigType.KTC_CONFIG_RANGE.toString());
        if (str != null) {
            deserialize(str);
        }
    }

    public TCRangeSetData(byte[] bArr) {
        super(KtcConfigType.KTC_CONFIG_RANGE.toString());
        if (bArr != null) {
            deserialize(new String(bArr));
        }
    }

    private void deserialize(KtcPluginParam ktcPluginParam) {
        this.max = ((Integer) ktcPluginParam.get("max", Integer.class)).intValue();
        this.min = ((Integer) ktcPluginParam.get("min", Integer.class)).intValue();
        this.current = ((Integer) ktcPluginParam.get(UserKeyDefine.KEY_ACCOUNT_CURRENT, Integer.class)).intValue();
    }

    private void deserialize(String str) {
        KtcDataDecomposer ktcDataDecomposer = new KtcDataDecomposer(str);
        this.max = ktcDataDecomposer.getIntValue("max");
        this.min = ktcDataDecomposer.getIntValue("min");
        this.current = ktcDataDecomposer.getIntValue(UserKeyDefine.KEY_ACCOUNT_CURRENT);
        this.name = ktcDataDecomposer.getStringValue("name");
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
        TCRangeSetData tCRangeSetData = new TCRangeSetData();
        tCRangeSetData.setMax(100);
        tCRangeSetData.setMin(0);
        tCRangeSetData.setCurrent(45);
        System.out.println("data0=" + tCRangeSetData.toString());
        System.out.println("data1=" + TCSetDataFactory.createSetData(tCRangeSetData.toBytes()).toString());
    }

    public int getCurrent() {
        return this.current;
    }

    public int getMax() {
        return this.max;
    }

    public int getMin() {
        return this.min;
    }

    public void setCurrent(int i) {
        this.current = i;
    }

    public void setMax(int i) {
        this.max = i;
    }

    public void setMin(int i) {
        this.min = i;
    }

    public byte[] toBytes() {
        String tCRangeSetData = toString();
        return tCRangeSetData != null ? tCRangeSetData.getBytes() : null;
    }

    public String toString() {
        KtcDataComposer ktcDataComposer = new KtcDataComposer();
        ktcDataComposer.addValue("type", this.type);
        ktcDataComposer.addValue("max", this.max);
        ktcDataComposer.addValue("min", this.min);
        ktcDataComposer.addValue(UserKeyDefine.KEY_ACCOUNT_CURRENT, this.current);
        ktcDataComposer.addValue("name", this.name);
        ktcDataComposer.addValue("enable", this.enable);
        ktcDataComposer.addValue("start", String.valueOf(this.startProcessTimestamp));
        ktcDataComposer.addValue("end", String.valueOf(this.endProcessTimestamp));
        return ktcDataComposer.toString();
    }
}

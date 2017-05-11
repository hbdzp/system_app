package com.horion.system.data;

import android.util.Log;
import com.ktc.framework.ktcsdk.plugins.KtcPluginParam;
import com.ktc.framework.ktcsdk.util.KtcDataDecomposer;
import com.ktc.framework.ktcsdk.util.KtcDataComposer;
import com.horion.user.data.UserCmdDefine.UserKeyDefine;
import java.util.ArrayList;
import java.util.List;

public class TCSwitchSetData extends TCSetData {
    private List<String> associtates = null;
    private boolean isOn = false;
    private boolean isUserDefinedOn = false;
    private int switchType = 0;

    public TCSwitchSetData() {
        super(KtcConfigType.KTC_CONFIG_SWITCH.toString());
    }

    public TCSwitchSetData(KtcPluginParam ktcPluginParam) {
        super(KtcConfigType.KTC_CONFIG_SWITCH.toString());
        if (ktcPluginParam != null) {
            deserialize(ktcPluginParam);
        }
    }

    public TCSwitchSetData(String str) {
        super(KtcConfigType.KTC_CONFIG_SWITCH.toString());
        if (str != null) {
            deserialize(str);
        }
    }

    public TCSwitchSetData(byte[] bArr) {
        super(KtcConfigType.KTC_CONFIG_SWITCH.toString());
        if (bArr != null) {
            deserialize(new String(bArr));
        }
    }

    private void deserialize(KtcPluginParam ktcPluginParam) {
        String str;
        this.pluginValue = ktcPluginParam;
        this.type = (String) ktcPluginParam.get("type", String.class);
        this.associtates = (List) ktcPluginParam.get("associate", ArrayList.class);
        try {
            str = (String) ktcPluginParam.get(UserKeyDefine.KEY_ACCOUNT_CURRENT, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            str = null;
        }
        if (str == null) {
            this.isOn = ((Boolean) ktcPluginParam.get(UserKeyDefine.KEY_ACCOUNT_CURRENT, Boolean.class)).booleanValue();
        } else {
            this.isOn = ((String) ktcPluginParam.get(UserKeyDefine.KEY_ACCOUNT_CURRENT, String.class)).contains("ON");
        }
        Log.d("SystemRecon", "switchtype data=" + ktcPluginParam);
        try {
            this.switchType = ((Integer) ktcPluginParam.get("switchtype", Integer.class)).intValue();
        } catch (Exception e2) {
            this.switchType = 0;
        }
    }

    private void deserialize(String str) {
        KtcDataDecomposer ktcDataDecomposer = new KtcDataDecomposer(str);
        this.value = str;
        this.type = ktcDataDecomposer.getStringValue("type");
        this.name = ktcDataDecomposer.getStringValue("name");
        this.associtates = ktcDataDecomposer.getStringListValue("associate");
        this.isOn = ktcDataDecomposer.getBooleanValue(UserKeyDefine.KEY_ACCOUNT_CURRENT);
        this.switchType = ktcDataDecomposer.getIntValue("switchtype");
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
        TCSwitchSetData tCSwitchSetData = new TCSwitchSetData();
        tCSwitchSetData.setOn(true);
        tCSwitchSetData.setName("switchname");
        List arrayList = new ArrayList();
        arrayList.add("acenum0");
        arrayList.add("acenum1");
        tCSwitchSetData.setAssocitates(arrayList);
        System.out.println("data0=" + tCSwitchSetData.toString());
        byte[] toBytes = tCSwitchSetData.toBytes();
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println("start=" + currentTimeMillis);
        TCSetData createSetData = TCSetDataFactory.createSetData(toBytes);
        long currentTimeMillis2 = System.currentTimeMillis();
        System.out.println("end=" + currentTimeMillis2);
        System.out.println("expire=" + (currentTimeMillis2 - currentTimeMillis));
        System.out.println("data1=" + createSetData.toString());
        System.out.println("time1=" + (System.currentTimeMillis() - currentTimeMillis2));
    }

    public List<String> getAssocitates() {
        return this.associtates;
    }

    public int getSwitchType() {
        return this.switchType;
    }

    public boolean isOn() {
        return this.isOn;
    }

    public boolean isUserDefinedOn() {
        return this.isUserDefinedOn;
    }

    public void setAssocitates(List<String> list) {
        this.associtates = list;
    }

    public void setOn(boolean z) {
        this.isOn = z;
    }

    public void setSwitchType(int i) {
        this.switchType = i;
    }

    public void setUserDefinedOn(boolean z) {
        this.isUserDefinedOn = z;
    }

    public byte[] toBytes() {
        String tCSwitchSetData = toString();
        return tCSwitchSetData != null ? tCSwitchSetData.getBytes() : null;
    }

    public String toString() {
        KtcDataComposer ktcDataComposer = new KtcDataComposer();
        ktcDataComposer.addValue("type", this.type);
        ktcDataComposer.addValue("name", this.name);
        if (this.associtates != null) {
            ktcDataComposer.addValue("associate", this.associtates);
        }
        ktcDataComposer.addValue(UserKeyDefine.KEY_ACCOUNT_CURRENT, this.isOn);
        ktcDataComposer.addValue("switchtype", this.switchType);
        ktcDataComposer.addValue("enable", this.enable);
        ktcDataComposer.addValue("start", String.valueOf(this.startProcessTimestamp));
        ktcDataComposer.addValue("end", String.valueOf(this.endProcessTimestamp));
        return ktcDataComposer.toString();
    }
}

package com.horion.system.data;

import android.util.Log;
import com.ktc.framework.ktcsdk.plugins.KtcPluginParam;
import com.ktc.framework.ktcsdk.util.KtcDataComposer;
import com.ktc.framework.ktcsdk.util.KtcDataDecomposer;
import com.horion.user.data.UserCmdDefine.UserKeyDefine;
import java.util.ArrayList;
import java.util.List;

public class TCEnumSetData extends TCSetData {
    private String current;
    private int currentIndex;
    private int enumCount;
    private List<String> enumList;
    private boolean isItemID;
    private String userData;

    public TCEnumSetData() {
        super(KtcConfigType.KTC_CONFIG_ENUM.toString());
        this.enumCount = 0;
        this.enumList = null;
        this.current = null;
        this.currentIndex = -1;
        this.isItemID = true;
        this.userData = null;
    }

    public TCEnumSetData(KtcPluginParam ktcPluginParam) {
        super(KtcConfigType.KTC_CONFIG_ENUM.toString());
        this.enumCount = 0;
        this.enumList = null;
        this.current = null;
        this.currentIndex = -1;
        this.isItemID = true;
        this.userData = null;
        if (ktcPluginParam != null) {
            this.enumList = new ArrayList();
            deserialize(ktcPluginParam);
        }
    }

    public TCEnumSetData(String str) {
        super(KtcConfigType.KTC_CONFIG_ENUM.toString());
        this.enumCount = 0;
        this.enumList = null;
        this.current = null;
        this.currentIndex = -1;
        this.isItemID = true;
        this.userData = null;
        if (str != null) {
            this.enumList = new ArrayList();
            deserialize(str);
        }
    }

    public TCEnumSetData(byte[] bArr) {
        super(KtcConfigType.KTC_CONFIG_ENUM.toString());
        this.enumCount = 0;
        this.enumList = null;
        this.current = null;
        this.currentIndex = -1;
        this.isItemID = true;
        this.userData = null;
        this.enumList = new ArrayList();
        if (bArr != null) {
            deserialize(new String(bArr));
        }
    }

    private void deserialize(KtcPluginParam ktcPluginParam) {
        this.pluginValue = ktcPluginParam;
        this.type = (String) ktcPluginParam.get("type", String.class);
        this.enumCount = ((Integer) ktcPluginParam.get("enumCount", Integer.class)).intValue();
        this.current = (String) ktcPluginParam.get(UserKeyDefine.KEY_ACCOUNT_CURRENT, String.class);
        this.userData = (String) ktcPluginParam.get("userdata", String.class);
        for (int i = 0; i < this.enumCount; i++) {
            String str = (String) ktcPluginParam.get("enum" + i, String.class);
            this.enumList.add(str);
            if (this.current == null) {
                this.currentIndex = 0;
            } else if (this.current.equals(str)) {
                this.currentIndex = i;
            }
        }
    }

    private void deserialize(String str) {
        KtcDataDecomposer ktcDataDecomposer = new KtcDataDecomposer(str);
        this.value = str;
        this.type = ktcDataDecomposer.getStringValue("type");
        this.name = ktcDataDecomposer.getStringValue("name");
        this.enumCount = ktcDataDecomposer.getIntValue("enumCount");
        this.current = ktcDataDecomposer.getStringValue(UserKeyDefine.KEY_ACCOUNT_CURRENT);
        this.userData = ktcDataDecomposer.getStringValue("userdata");
        this.enumList = ktcDataDecomposer.getStringListValue("enumlist");
        this.currentIndex = ktcDataDecomposer.getIntValue("currentIndex");
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
        for (int i = 0; i < this.enumCount && this.enumList != null; i++) {
            String str2 = (String) this.enumList.get(i);
            if (this.current == null) {
                this.currentIndex = 0;
            } else if (this.current.equals(str2)) {
                this.currentIndex = i;
            }
        }
    }

    public static void main(String[] strArr) {
        TCEnumSetData tCEnumSetData = new TCEnumSetData();
        tCEnumSetData.setCurrent(UserKeyDefine.KEY_ACCOUNT_CURRENT);
        tCEnumSetData.setEnumCount(2);
        List arrayList = new ArrayList();
        arrayList.add("enum0");
        arrayList.add("enum1");
        tCEnumSetData.setEnumList(arrayList);
        System.out.println("data0=" + tCEnumSetData.toString());
        System.out.println("data1=" + TCSetDataFactory.createSetData(tCEnumSetData.toBytes()).toString());
    }

    public String getCurrent() {
        return this.current;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public int getEnumCount() {
        return this.enumCount;
    }

    public List<String> getEnumList() {
        return this.enumList;
    }

    public String getUserData() {
        return this.userData;
    }

    public boolean isItemID() {
        return this.isItemID;
    }

    public void setCurrent(String str) {
        this.current = str;
        if (this.enumList != null) {
            for (int i = 0; i < this.enumList.size(); i++) {
                if (((String) this.enumList.get(i)).equals(str)) {
                    this.currentIndex = i;
                    return;
                }
            }
        }
    }

    public void setCurrentIndex(int i) {
        Log.d("uilogic", "currentindex=" + i);
        if (i > -1) {
            this.currentIndex = i;
            if (this.enumList != null) {
                try {
                    this.current = (String) this.enumList.get(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setEnumCount(int i) {
        this.enumCount = i;
    }

    public void setEnumList(List<String> list) {
        this.enumList = list;
        if (list != null) {
            this.enumCount = list.size();
        }
    }

    public void setItemID(boolean z) {
        this.isItemID = z;
    }

    public void setUserData(String str) {
        this.userData = str;
    }

    public byte[] toBytes() {
        String tCEnumSetData = toString();
        return tCEnumSetData != null ? tCEnumSetData.getBytes() : null;
    }

    public String toString() {
        KtcDataComposer ktcDataComposer = new KtcDataComposer();
        ktcDataComposer.addValue("type", this.type);
        ktcDataComposer.addValue("name", this.name);
        ktcDataComposer.addValue("enumCount", this.enumCount);
        ktcDataComposer.addValue(UserKeyDefine.KEY_ACCOUNT_CURRENT, this.current);
        ktcDataComposer.addValue("userdata", this.userData);
        ktcDataComposer.addValue("enable", this.enable);
        ktcDataComposer.addValue("currentIndex", this.currentIndex);
        ktcDataComposer.addValue("start", String.valueOf(this.startProcessTimestamp));
        ktcDataComposer.addValue("end", String.valueOf(this.endProcessTimestamp));
        if (this.enumList != null && this.enumList.size() >= 1) {
            ktcDataComposer.addValue("enumlist", this.enumList);
        }
        return ktcDataComposer.toString();
    }
}

package com.horion.system.data;

import com.ktc.framework.ktcsdk.plugins.KtcPluginParam;
import com.horion.system.data.TCSetData.KtcConfigType;
////import com.horion.system.utils.SysLog;

public class TCSetDataFactory {
    private static  int[] KtcConfigType2Values;

    private static  int[] getKtcConfigType2Values() {
        if (KtcConfigType2Values != null) {
            return KtcConfigType2Values;
        }
        int[] iArr = new int[KtcConfigType.values().length];
        try {
            iArr[KtcConfigType.KTC_CONFIG_ENUM.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[KtcConfigType.KTC_CONFIG_INFO.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[KtcConfigType.KTC_CONFIG_INPUT_VALUE.ordinal()] = 5;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[KtcConfigType.KTC_CONFIG_NONE.ordinal()] = 6;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[KtcConfigType.KTC_CONFIG_RANGE.ordinal()] = 3;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[KtcConfigType.KTC_CONFIG_RET.ordinal()] = 7;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[KtcConfigType.KTC_CONFIG_SINGLE.ordinal()] = 8;
        } catch (NoSuchFieldError e7) {
        }
        try {
            iArr[KtcConfigType.KTC_CONFIG_SWITCH.ordinal()] = 4;
        } catch (NoSuchFieldError e8) {
        }
        KtcConfigType2Values = iArr;
        return iArr;
    }

    public static TCSetData createSetData(KtcPluginParam ktcPluginParam) {
        if (ktcPluginParam == null) {
            ////SysLog.warn("createSetData: null by param is null");
            return null;
        }
        try {
            String str = (String) ktcPluginParam.get("type", String.class);
            if (str == null) {
                return null;
            }
            switch (getKtcConfigType2Values()[KtcConfigType.valueOf(str).ordinal()]) {
                case 1:
                    return new TCEnumSetData(ktcPluginParam);
                case 2:
                    return new TCInfoSetData(ktcPluginParam);
                case 3:
                    return new TCRangeSetData(ktcPluginParam);
                case 4:
                    return new TCSwitchSetData(ktcPluginParam);
                default:
                    return null;
            }
            ///e.printStackTrace();
           //// return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TCSetData createSetData(byte[] bArr) {
        try {
            KtcConfigType type = TCSetData.getType(bArr);
            if (type == null) {
                return null;
            }
            switch (getKtcConfigType2Values()[type.ordinal()]) {
                case 1:
                    return new TCEnumSetData(bArr);
                case 2:
                    return new TCInfoSetData(bArr);
                case 3:
                    return new TCRangeSetData(bArr);
                case 4:
                    return new TCSwitchSetData(bArr);
                default:
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

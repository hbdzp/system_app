package com.horion.tv.ui.utils;

import android.text.TextUtils;
import android.util.Log;

import com.ktc.framework.ktcsdk.util.KtcAddonTextUtils;
////import com.horion.system.data.TCEnumSetData;
import com.horion.system.data.TCSetData;
////import com.horion.tv.define.object.Source;
///.import com.horion.tv.define.object.Source.SOURCE_NAME_ENUM;
import com.horion.tv.util.Constants.SOURCE_NAME_ENUM;
////import com.horion.tv.utils.KtcTVDebug;
import com.horion.tv.utils.UITextUtilInterface;

public class UITextUtil implements UITextUtilInterface {
    private static int[] sourceEnum2Array;
    private static UITextUtil instance = null;

    private static  int[] getsourceEnum2Array() {
        if (sourceEnum2Array != null) {
            return sourceEnum2Array;
        }
        int[] iArr = new int[SOURCE_NAME_ENUM.values().length];
        try {
            iArr[SOURCE_NAME_ENUM.ATV.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.AV.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.DTMB.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.DVBC.ordinal()] = 4;
        } catch (NoSuchFieldError e4) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.EXTERNAL.ordinal()] = 8;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.HDMI.ordinal()] = 5;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.IPLIVE.ordinal()] = 9;
        } catch (NoSuchFieldError e7) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.REMEMBER.ordinal()] = 10;
        } catch (NoSuchFieldError e8) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.VGA.ordinal()] = 6;
        } catch (NoSuchFieldError e9) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.VOD.ordinal()] = 11;
        } catch (NoSuchFieldError e10) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.YUV.ordinal()] = 7;
        } catch (NoSuchFieldError e11) {
        }
        sourceEnum2Array = iArr;
        return iArr;
    }

    private UITextUtil() {
    }

    public static UITextUtil getInstance() {
        synchronized (UITextUtil.class) {
            try {
                if (instance == null) {
                    instance = new UITextUtil();
                }
                UITextUtil uITextUtil = instance;
                return uITextUtil;
            } finally {
                Object obj = UITextUtil.class;
            }
        }
    }
/*
    public TCEnumSetData forTCEnumSetData(TCEnumSetData tCEnumSetData) {
        if (tCEnumSetData == null) {
            return null;
        }
        Iterable<String> enumList = tCEnumSetData.getEnumList();
        List arrayList = new ArrayList();
        for (String textByKey : enumList) {
            arrayList.add(getTextByKey(textByKey));
        }
        tCEnumSetData.setEnumList(arrayList);
        ///KtcTVDebug.debug(" enumSetData" + tCEnumSetData.getCurrentIndex());
        ////KtcTVDebug.debug(" enumSetData current:" + tCEnumSetData.getCurrent());
        return tCEnumSetData;
    }*/

    public TCSetData getMenuDataByCommand(String str, String str2) {
        return null;
    }

    public String getTextByKey(String str) {
        try {
            String text = KtcAddonTextUtils.getInstance().getText(str);
            Log.d("Maxs50","UITextUtil:getTextByKey:text = " + text);
            if (!TextUtils.isEmpty(text)) {
                return text;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ////return KtcTvApp.getInstance().getLogicTextResource() != null ? KtcTvApp.getInstance().getLogicTextResource().getText(str) : "";
        return null;
    }

    /*public String getTextByResId(int i) {
        return KtcTvApp.getInstance().getLogicTextResource() != null ? KtcTvApp.getInstance().getLogicTextResource().getText(i) : "";
    }*/

    /*public String loadNameFromAddon(Source source) {
        String str;
        switch (getsourceEnum2Array()[source.getSourceNameEnum().ordinal()]) {
            case 1:
                str = "KTC_CFG_TV_SOURCE_ATV";
                break;
            case 2:
                if (source.index == -1) {
                    str = "KTC_CFG_TV_SOURCE_AV";
                    break;
                }
                str = "KTC_CFG_TV_SOURCE_AV" + (source.index + 1);
                break;
            case 3:
                str = "KTC_CFG_TV_SOURCE_DTMB";
                break;
            case 4:
                str = "KTC_CFG_TV_SOURCE_DVBC";
                break;
            case 5:
                if (source.index == -1) {
                    str = "KTC_CFG_TV_SOURCE_HDMI";
                    break;
                }
                str = "KTC_CFG_TV_SOURCE_HDMI" + (source.index + 1);
                break;
            case 6:
                str = "KTC_CFG_TV_SOURCE_VGA";
                break;
            case 7:
                str = "KTC_CFG_TV_SOURCE_YUV";
                break;
            default:
                str = null;
                break;
        }
        return str == null ? null : KtcAddonTextUtils.getInstance().getText(str);
    }*/
}

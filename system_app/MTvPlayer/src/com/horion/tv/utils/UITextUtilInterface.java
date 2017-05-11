package com.horion.tv.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.horion.system.data.TCSetData;
import com.horion.tv.R;
import com.horion.tv.util.Constants;
import com.ktc.framework.ktcsdk.util.KtcAddonTextUtils;

import java.lang.reflect.Field;

public interface UITextUtilInterface {
    ////TCSetData getMenuDataByCommand(String str, String str2);

    String getTextByKey(String str);

   //// String getTextByResId(int i);

    class UITextUtil implements UITextUtilInterface {
        private static int[] sourceEnum2Array;
        private static UITextUtil instance = null;

        private static  int[] getsourceEnum2Array() {
            if (sourceEnum2Array != null) {
                return sourceEnum2Array;
            }
            int[] iArr = new int[Constants.SOURCE_NAME_ENUM.values().length];
            try {
                iArr[Constants.SOURCE_NAME_ENUM.ATV.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.AV.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.DTMB.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.DVBC.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.EXTERNAL.ordinal()] = 8;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.HDMI.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.IPLIVE.ordinal()] = 9;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.REMEMBER.ordinal()] = 10;
            } catch (NoSuchFieldError e8) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.VGA.ordinal()] = 6;
            } catch (NoSuchFieldError e9) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.VOD.ordinal()] = 11;
            } catch (NoSuchFieldError e10) {
            }
            try {
                iArr[Constants.SOURCE_NAME_ENUM.YUV.ordinal()] = 7;
            } catch (NoSuchFieldError e11) {
            }
            sourceEnum2Array = iArr;
            return iArr;
        }

        public UITextUtil() {
            
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

    class LogicTextResource {
        Context context = null;
        Field[] fields = null;
        R.string obj = new R.string();

        public LogicTextResource(Context context) {
            this.context = context;
        }

        private boolean contains(String str) {
            if (this.fields == null) {
                return false;
            }
            for (Field name : this.fields) {
                if (name.getName().equals(str)) {
                    return true;
                }
            }
            return false;
        }

        private Field getField(String str) {
            if (!(str == null || this.fields == null)) {
                for (Field field : this.fields) {
                    if (field.getName().equals(str)) {
                        return field;
                    }
                }
            }
            return null;
        }

        private int getResidByReskey(String str) {
            if (str != null) {
                try {
                    Field field = getField(str);
                    if (field != null) {
                        return field.getInt(this.obj);
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                }
            }
            return 0;
        }

        public String getText(int i) {
            return this.context != null ? this.context.getString(i) : null;
        }

        public String getText(String str) {
            if (str == null) {
                ////KtcTVDebug.debug("getText resid=null");
                return null;
            }
            if (this.fields == null) {
                init();
            }
            int i = 0;
            try {
                i = getResidByReskey(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i != 0) {
                return this.context != null ? this.context.getString(i) : null;
            } else {
                ////KtcTVDebug.debug("not found " + str + " in xml");
                return "";
            }
        }

        public String getText1(String str) {
            if (str == null) {
                ////KtcTVDebug.debug("getText resid=null");
                return null;
            }
            if (this.fields == null) {
                init();
            }
            int i = 0;
            try {
                i = getResidByReskey(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i != 0) {
                return this.context != null ? this.context.getString(i) : null;
            } else {
                ////KtcTVDebug.debug("not found " + str + " in xml");
                return str;
            }
        }

        public void init() {
            this.fields = R.string.class.getDeclaredFields();
        }

        public void setContext(Context context) {
            this.context = context;
        }
    }
}

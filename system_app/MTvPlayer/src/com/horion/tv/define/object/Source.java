package com.horion.tv.define.object;

////import com.horion.tv.define.KtcTvDefine.SOURCE_SIGNAL_STATE;
import com.horion.tv.utils.KtcTvCache;

import java.util.List;

public class Source extends KtcTvObject {
    private static int[] SOURCE_NAME_ENUMSwitchesValues = null;
    public static final int DEFAULT_INDEX = -1;
    public static final String EXTERNAL_DEFAULT = "external_default";
    private static final long serialVersionUID = 5022920626572037799L;
    private static KtcTvCache<String, Source> sourceCache = new KtcTvCache();
    public boolean bILiveSource;
    public boolean bOnlySourceNameEnum;
    public boolean bRecognizedSource;
    public List<String> dependencyPkgName;
    public int index;
    public boolean isApk;
    public boolean isFakeSource;
    public String pkgName;
    ////public SOURCE_SIGNAL_STATE signalState;

    public enum SOURCE_NAME_ENUM {
        HDMI,
        AV,
        ATV,
        DVBC,
        DTMB,
        YUV,
        VGA,
        VOD,
        REMEMBER,
        EXTERNAL,
        IPLIVE
    }

    private static int[] getSOURCE_NAME_ENUMSwitchesValues() {
        if (SOURCE_NAME_ENUMSwitchesValues != null) {
            return SOURCE_NAME_ENUMSwitchesValues;
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
            iArr[SOURCE_NAME_ENUM.EXTERNAL.ordinal()] = 10;
        } catch (NoSuchFieldError e5) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.HDMI.ordinal()] = 5;
        } catch (NoSuchFieldError e6) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.IPLIVE.ordinal()] = 6;
        } catch (NoSuchFieldError e7) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.REMEMBER.ordinal()] = 11;
        } catch (NoSuchFieldError e8) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.VGA.ordinal()] = 7;
        } catch (NoSuchFieldError e9) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.VOD.ordinal()] = 8;
        } catch (NoSuchFieldError e10) {
        }
        try {
            iArr[SOURCE_NAME_ENUM.YUV.ordinal()] = 9;
        } catch (NoSuchFieldError e11) {
        }
        SOURCE_NAME_ENUMSwitchesValues = iArr;
        return iArr;
    }

    public Source(String str) {
        super(Source.class.getName() + str + -1, str);
        this.bILiveSource = false;
        this.bRecognizedSource = false;
        this.bOnlySourceNameEnum = false;
        this.isApk = false;
        this.pkgName = null;
        this.dependencyPkgName = null;
        this.index = -1;
        ////this.signalState = SOURCE_SIGNAL_STATE.NOSIGNAL;
        this.isFakeSource = false;
        this.bILiveSource = isILiveSource();
        this.bRecognizedSource = isRecognizedSource();
        this.bOnlySourceNameEnum = false;
    }

    public Source(String str, int i) {
        super(Source.class.getName() + str + i, str);
        this.bILiveSource = false;
        this.bRecognizedSource = false;
        this.bOnlySourceNameEnum = false;
        this.isApk = false;
        this.pkgName = null;
        this.dependencyPkgName = null;
        this.index = -1;
        ////this.signalState = SOURCE_SIGNAL_STATE.NOSIGNAL;
        this.isFakeSource = false;
        this.index = i;
        this.bILiveSource = isILiveSource();
        this.bRecognizedSource = isRecognizedSource();
        this.bOnlySourceNameEnum = false;
    }

    public Source(String str, String str2) {
        super(Source.class.getName() + str + str2 + -1, str);
        this.bILiveSource = false;
        this.bRecognizedSource = false;
        this.bOnlySourceNameEnum = false;
        this.isApk = false;
        this.pkgName = null;
        this.dependencyPkgName = null;
        this.index = -1;
        /////this.signalState = SOURCE_SIGNAL_STATE.NOSIGNAL;
        this.isFakeSource = false;
        this.bILiveSource = isILiveSource();
        this.bRecognizedSource = isRecognizedSource();
        this.bOnlySourceNameEnum = false;
    }

    public static Source ATV() {
        return getSource(SOURCE_NAME_ENUM.ATV.toString());
    }

    public static Source AV(int i) {
        return getSource(SOURCE_NAME_ENUM.AV.toString(), i);
    }

    public static Source DTMB() {
        return getSource(SOURCE_NAME_ENUM.DTMB.toString());
    }

    public static Source DVBC() {
        return getSource(SOURCE_NAME_ENUM.DVBC.toString());
    }

    public static Source External(String str) {
        return getSource(SOURCE_NAME_ENUM.EXTERNAL.toString(), str);
    }

    public static Source HDMI(int i) {
        return getSource(SOURCE_NAME_ENUM.HDMI.toString(), i);
    }

    public static Source IPLive() {
        return getSource(SOURCE_NAME_ENUM.IPLIVE.toString());
    }

    public static Source Remember() {
        return getSource(SOURCE_NAME_ENUM.REMEMBER.toString());
    }

    public static Source VGA(int i) {
        return getSource(SOURCE_NAME_ENUM.VGA.toString(), i);
    }

    public static Source VOD() {
        Source source = getSource(SOURCE_NAME_ENUM.VOD.toString());
        source.isFakeSource = true;
        return source;
    }

    public static Source YUV(int i) {
        return getSource(SOURCE_NAME_ENUM.YUV.toString(), i);
    }

    private static Source getSource(String str) {
        Source source = null;
        synchronized (Source.class) {
            try {
                source = (Source) sourceCache.get(str);
                if (source == null) {
                    source = new Source(str);
                    sourceCache.add(str, source);
                }
            } catch (Throwable th) {
                Class cls = Source.class;
            }
        }
        return source;
    }

    private static Source getSource(String str, int i) {
        Source source = (Source) sourceCache.get(str + i);
        if (source != null) {
            return source;
        }
        source = new Source(str, i);
        sourceCache.add(str + i, source);
        return source;
    }

    private static Source getSource(String str, String str2) {
        Source source = (Source) sourceCache.get(str + str2);
        if (source != null) {
            return source;
        }
        source = new Source(str, str2);
        sourceCache.add(str + str2, source);
        return source;
    }

    public static Source getSourceByName(String str) {
        int intValue;
        int lastIndexOf = str.lastIndexOf("@");
        if (lastIndexOf != -1) {
            intValue = Integer.valueOf(str.substring(lastIndexOf + 1)).intValue();
            str = str.substring(0, lastIndexOf);
        } else {
            intValue = -1;
        }
        SOURCE_NAME_ENUM source_name_enum = SOURCE_NAME_ENUM.ATV;
        try {
            switch (getSOURCE_NAME_ENUMSwitchesValues()[SOURCE_NAME_ENUM.valueOf(str).ordinal()]) {
                case 1:
                    return ATV();
                case 2:
                    return intValue != -1 ? AV(intValue) : AV(-1);
                case 3:
                    return DTMB();
                case 4:
                    return DVBC();
                case 5:
                    return intValue != -1 ? HDMI(intValue) : HDMI(-1);
                case 6:
                    return IPLive();
                case 7:
                    return intValue != -1 ? VGA(intValue) : VGA(-1);
                case 9:
                    return intValue != -1 ? YUV(intValue) : YUV(-1);
                default:
                    return null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isExternalSource(Source source) {
        return SOURCE_NAME_ENUM.valueOf(source.name) == SOURCE_NAME_ENUM.EXTERNAL;
    }

    private boolean isILiveSource() {
        switch (getSOURCE_NAME_ENUMSwitchesValues()[getSourceNameEnum().ordinal()]) {
            case 1:
            case 3:
            case 4:
            case 6:
            case 8:
                this.bILiveSource = true;
                break;
            default:
                this.bILiveSource = false;
                break;
        }
        return this.bILiveSource;
    }

    public static boolean isInternalSource(Source source) {
        SOURCE_NAME_ENUM[] values = SOURCE_NAME_ENUM.values();
        if (source.equals(External("iplive"))) {
            return true;
        }
        for (SOURCE_NAME_ENUM source_name_enum : values) {
            if (source_name_enum != SOURCE_NAME_ENUM.EXTERNAL && source_name_enum.toString().equals(source.name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isRecognizedSource() {
        switch (getSOURCE_NAME_ENUMSwitchesValues()[getSourceNameEnum().ordinal()]) {
            case 1:
                this.bRecognizedSource = true;
                break;
            default:
                this.bRecognizedSource = false;
                break;
        }
        return this.bRecognizedSource;
    }

    protected void doAfterDeserialize() {
    }

    public SOURCE_NAME_ENUM getSourceNameEnum() {
        try {
            return SOURCE_NAME_ENUM.valueOf(this.name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPackageName(String str) {
        this.pkgName = str;
        if (str != null) {
            this.isApk = true;
        } else {
            this.isApk = false;
        }
    }
}

package com.horion.tv.framework.ui.util;

///import com.horion.tv.utils.SkyTVDebug;
///import com.ktc.framework.skysdk.android.SkySystemUtil;

public class UiManager {
    public static float dipDiv = 1.0f;
    public static int height;
    public static float resolutionDiv = 1.0f;
    public static int width;

    public static int getHeight() {
        return height;
    }

    public static int getResolutionValue(int i) {
        int i2 = (int) (((float) i) / resolutionDiv);
        return i2 < 1 ? 1 : i2;
    }

    public static int getScaledValue(int i) {
        int i2 = (int) (((float) (i * 1)) / resolutionDiv);
        return i2 < 1 ? 1 : i2;
    }

    public static int getTextDpiValue(int i) {
        int i2 = (int) (((float) i) / dipDiv);
        return i2 < 1 ? 1 : i2;
    }

    public static int getTextScaledValue(int i) {
        int i2 = (int) (((float) (i * 1)) / dipDiv);
        return i2 < 1 ? 1 : i2;
    }

    public static int getWidth() {
        return width;
    }

    public static void initDiv() {
        setResolutionDiv();
        setDpiDiv();
    }

    private static void setDpiDiv() {
        ///dipDiv = resolutionDiv * SkySystemUtil.getDisplayDensity(null);
        dipDiv = 0.5f * 240;
        dipDiv = 1.5f;
        ///SkyTVDebug.debug("dipDiv============" + dipDiv);
    }

    private static void setResolutionDiv() {
        ///width = SkySystemUtil.getDisplayWidth(null);
        ///SkyTVDebug.debug("width============" + SkySystemUtil.getDisplayWidth(null));
        width = 1920;
        switch (width) {
            case 1280:
                resolutionDiv = 1.5f;
                break;
            case 1366:
                resolutionDiv = 1.4f;
                break;
            case 1920:
                resolutionDiv = 1.0f;
                break;
            case 3840:
                resolutionDiv = 0.5f;
                break;
            default:
                resolutionDiv = 1.0f;
                break;
        }
        //height = SkySystemUtil.getDisplayHeight(null);
        height = 2160;
    }
}

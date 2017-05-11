package com.ktc.panasonichome.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import com.android.volley.DefaultRetryPolicy;

public class Util {
    private static DisplayMetrics dm;
    private static float mDiv = 1.0f;
    private static float mDpi = 1.0f;
    private static Util mUtil;

    public static Util instence(Context context) {
        if (mUtil == null) {
            mUtil = new Util();
        }
        if (dm == null) {
            dm = context.getResources().getDisplayMetrics();
        }
        mDiv = ((float) dm.widthPixels) / 1920.0f;
        mDpi = mDiv / dm.density;
        return mUtil;
    }

    public static Util instance() {
        return mUtil;
    }

    public int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public DisplayMetrics getDm() {
        return dm;
    }

    public static int dip2px(float dpValue) {
        return (int) ((dpValue * dm.density) + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) ((pxValue / dm.density) + 0.5f);
    }

    public static int Div(int x) {
        return (int) (((float) x) * mDiv);
    }

    public static int Dpi(int x) {
        return (int) (((float) x) * mDpi);
    }

    public <T extends View> T findViewById(Context context, int id) {
        return (T) ((Activity) context).findViewById(id);
    }

    public <T extends View> T findViewById(View view, int id) {
        return (T) view.findViewById(id);
    }
}

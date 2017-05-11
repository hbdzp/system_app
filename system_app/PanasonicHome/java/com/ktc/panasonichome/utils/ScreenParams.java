package com.ktc.panasonichome.utils;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.math.BigDecimal;

/**
 * Created by dinzhip on 2016/12/28.
 */


public class ScreenParams {
    private static ScreenParams screenParams;
    private        float        dipDiv;
    private        float        resolutionDiv;

    public static ScreenParams getInstence(Context context) {
        if (screenParams == null) {
            screenParams = new ScreenParams(context);
        }
        return screenParams;
    }

    public ScreenParams(Context context) {
        setDpiDiv_Resolution(context);
    }

    private void setDpiDiv_Resolution(Context context) {
        switch (getDisplayWidth(context)) {
            case 800:
                this.resolutionDiv = 2.4f;
                break;
            case 1280:
                this.resolutionDiv = 1.5f;
                break;
            case 1366:
                this.resolutionDiv = 1.4f;
                break;
            case 1920:
                this.resolutionDiv = 1.0f;
                break;
            case 3840:
                this.resolutionDiv = 0.5f;
                break;
            default:
                this.resolutionDiv = 1.0f;
                break;
        }
        this.dipDiv = this.resolutionDiv * getDisplayDensity(context);
    }

    public float getDisplayDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        return context.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public int getDisplayWidth(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (display == null) {
            return 1920;
        }
        return display.getWidth();
    }

    public int getDisplayHeight(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    public ObjectAnimator nope_X(View view) {
        int                  delta         = getResolutionValue(8);
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X, new Keyframe[]{Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(0.1f, (float) (-delta)), Keyframe.ofFloat(0.26f, (float) delta), Keyframe.ofFloat(0.42f, (float) (-delta)), Keyframe.ofFloat(0.58f, (float) delta), Keyframe.ofFloat(0.74f, (float) (-delta)), Keyframe.ofFloat(0.9f, (float) delta), Keyframe.ofFloat(1.0f, 0.0f)});
        return ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{pvhTranslateX}).setDuration(500);
    }

    public ObjectAnimator nope_Y(View view) {
        int                  delta         = getResolutionValue(8);
        PropertyValuesHolder pvhTranslateY = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y, new Keyframe[]{Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(0.1f, (float) (-delta)), Keyframe.ofFloat(0.26f, (float) delta), Keyframe.ofFloat(0.42f, (float) (-delta)), Keyframe.ofFloat(0.58f, (float) delta), Keyframe.ofFloat(0.74f, (float) (-delta)), Keyframe.ofFloat(0.9f, (float) delta), Keyframe.ofFloat(1.0f, 0.0f)});
        return ObjectAnimator.ofPropertyValuesHolder(view, new PropertyValuesHolder[]{pvhTranslateY}).setDuration(500);
    }

    public int getResolutionValue(int value) {
        if (value < 0) {
            return 0;
        }
        return new BigDecimal(String.valueOf(((float) value) / this.resolutionDiv)).setScale(0, 4).intValue();
    }

    public int getTextDpiValue(int value) {
        if (value < 0) {
            return 0;
        }
        return (int) (((float) value) / this.dipDiv);
    }

    public TextView settextAlpha(TextView view, int alpha, String color) {
        view.setTextColor(Color.argb(alpha, Integer.valueOf(color.substring(0, 2), 16).intValue(), Integer.valueOf(color.substring(2, 4), 16).intValue(), Integer.valueOf(color.substring(4, 6), 16).intValue()));
        return view;
    }

    public int getScreenWidth(Context context) {
        WindowManager  wm         = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public int getScreenHeight(Context context) {
        WindowManager  wm         = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    public Bitmap getScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bp = Bitmap.createBitmap(view.getDrawingCache(), 0, 0, getScreenWidth(activity), getScreenHeight(activity));
        view.destroyDrawingCache();
        return bp;
    }

    public Bitmap readBitMap(Context context, int resId) {
        Options opt = new Options();
        opt.inPreferredConfig = Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(resId), null, opt);
    }

    public Drawable readDrawable(Context context, int resId) {
        return new BitmapDrawable(context.getResources(), readBitMap(context, resId));
    }
}

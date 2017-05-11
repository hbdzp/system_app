package com.ktc.util;

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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import java.math.BigDecimal;

public class KtcScreenParams {
    private static KtcScreenParams screenParams;
    private float dipDiv;
    private float resolutionDiv;

    public KtcScreenParams(Context context) {
        setDpiDiv_Resolution(context);
    }

    public static KtcScreenParams getInstence(Context context) {
        Log.d("Maxs","screenParams == null == " + (screenParams == null));
        if (screenParams == null) {
            screenParams = new KtcScreenParams(context);
        }
        return screenParams;
    }

    private void setDpiDiv_Resolution(Context context) {
        Log.d("Maxs","getDisplayWidth(context)");
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
        Log.d("Maxs","resolutionDiv = " + resolutionDiv);
        this.dipDiv = this.resolutionDiv * getDisplayDensity(context);
    }

    public float getDisplayDensity(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        return context.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public int getDisplayHeight(Context context) {
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    public int getDisplayWidth(Context context) {
        Display defaultDisplay = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        return defaultDisplay == null ? 1920 : defaultDisplay.getWidth();
    }

    public int getResolutionValue(int i) {
        return i < 0 ? 0 : new BigDecimal(String.valueOf(((float) i) / this.resolutionDiv)).setScale(0, 4).intValue();
    }

    public int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public Bitmap getScreenShot(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setDrawingCacheEnabled(true);
        decorView.buildDrawingCache();
        Bitmap createBitmap = Bitmap.createBitmap(decorView.getDrawingCache(), 0, 0, getScreenWidth(activity), getScreenHeight(activity));
        decorView.destroyDrawingCache();
        return createBitmap;
    }

    public int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public int getTextDpiValue(int i) {
        return i < 0 ? 0 : (int) (((float) i) / this.dipDiv);
    }

    public ObjectAnimator nope_X(View view) {
        int resolutionValue = getResolutionValue(8);
        PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[1];
        propertyValuesHolderArr[0] = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X, new Keyframe[]{Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(0.1f, (float) (-resolutionValue)), Keyframe.ofFloat(0.26f, (float) resolutionValue), Keyframe.ofFloat(0.42f, (float) (-resolutionValue)), Keyframe.ofFloat(0.58f, (float) resolutionValue), Keyframe.ofFloat(0.74f, (float) (-resolutionValue)), Keyframe.ofFloat(0.9f, (float) resolutionValue), Keyframe.ofFloat(1.0f, 0.0f)});
        return ObjectAnimator.ofPropertyValuesHolder(view, propertyValuesHolderArr).setDuration(500);
    }

    public ObjectAnimator nope_Y(View view) {
        int resolutionValue = getResolutionValue(8);
        PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[1];
        propertyValuesHolderArr[0] = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y, new Keyframe[]{Keyframe.ofFloat(0.0f, 0.0f), Keyframe.ofFloat(0.1f, (float) (-resolutionValue)), Keyframe.ofFloat(0.26f, (float) resolutionValue), Keyframe.ofFloat(0.42f, (float) (-resolutionValue)), Keyframe.ofFloat(0.58f, (float) resolutionValue), Keyframe.ofFloat(0.74f, (float) (-resolutionValue)), Keyframe.ofFloat(0.9f, (float) resolutionValue), Keyframe.ofFloat(1.0f, 0.0f)});
        return ObjectAnimator.ofPropertyValuesHolder(view, propertyValuesHolderArr).setDuration(500);
    }

    public Bitmap readBitMap(Context context, int i) {
        Options options = new Options();
        options.inPreferredConfig = Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(i), null, options);
    }

    public Drawable readDrawable(Context context, int i) {
        return new BitmapDrawable(context.getResources(), readBitMap(context, i));
    }

    public TextView settextAlpha(TextView textView, int i, String str) {
        textView.setTextColor(Color.argb(i, Integer.valueOf(str.substring(0, 2), 16).intValue(), Integer.valueOf(str.substring(2, 4), 16).intValue(), Integer.valueOf(str.substring(4, 6), 16).intValue()));
        return textView;
    }
}

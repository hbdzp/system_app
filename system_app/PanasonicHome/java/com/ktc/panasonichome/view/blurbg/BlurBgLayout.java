package com.ktc.panasonichome.view.blurbg;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.view.blurbg.BlurringView.OnBlurFinishListener;
////import com.skyworth.framework.skysdk.properties.SkySystemProperties;

public class BlurBgLayout extends FrameLayout implements OnBlurFinishListener {
    ////private static /* synthetic */ int[] -com_skyworth_ui_blurbg_BlurBgLayout$PAGETYPESwitchesValues;
    private static int[] PAGETYPESwitchsValues;
    private Drawable bgDrawable;
    private BlurBgView bgView;
    private LayoutParams bgViewP;
    private float curAlpha;
    private int curBlurRadiu;
    private String curOverColor;
    private int curOverLayoutAlpha;
    private float curShadowAlpha;
    private ImageView mBlurView;
    private BlurringView mBlurringView;
    private Context mContext;
    private int mDownsampleFactor;
    private LowMemoryBlurView mLowMemoryBlurView;
    private boolean mSaveBlurBitmap;
    private ImageView mShadowLayout;
    private String pe;
    private int version;

    public enum PAGETYPE {
        FIRST_PAGE,
        SECONDE_PAGE,
        OTHER_PAGE
    }

    ////private static /* synthetic */ int[] -getcom_skyworth_ui_blurbg_BlurBgLayout$PAGETYPESwitchesValues() {
    private static /* synthetic */ int[] getPAGETYPESwitchsValues() {
        /////if (-com_skyworth_ui_blurbg_BlurBgLayout$PAGETYPESwitchesValues != null) {
        /////    return -com_skyworth_ui_blurbg_BlurBgLayout$PAGETYPESwitchesValues;
        /////}
        if (PAGETYPESwitchsValues != null) {
            return PAGETYPESwitchsValues;
        }
        int[] iArr = new int[PAGETYPE.values().length];
        try {
            iArr[PAGETYPE.FIRST_PAGE.ordinal()] = 1;
        } catch (NoSuchFieldError e) {
        }
        try {
            iArr[PAGETYPE.OTHER_PAGE.ordinal()] = 2;
        } catch (NoSuchFieldError e2) {
        }
        try {
            iArr[PAGETYPE.SECONDE_PAGE.ordinal()] = 3;
        } catch (NoSuchFieldError e3) {
        }
        /////-com_skyworth_ui_blurbg_BlurBgLayout$PAGETYPESwitchesValues = iArr;
        PAGETYPESwitchsValues = iArr;
        return iArr;
    }

    public BlurBgLayout(Context context) {
        this(context, null);
    }

    public BlurBgLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BlurBgLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.curBlurRadiu = 1;
        this.curOverLayoutAlpha = 125;
        this.curAlpha = 1.0f;
        this.curShadowAlpha = 1.0f;
        this.curOverColor = "ffffff";
        this.version = VERSION.SDK_INT;
        this.mSaveBlurBitmap = false;
        this.mContext = context;
        initView();
    }

    private void initView() {
        ////this.pe = SkySystemProperties.getProperty("ro.build.PE");
        this.pe = "PE";
        if (!TextUtils.isEmpty(this.pe)) {
            this.mBlurView = new ImageView(this.mContext);
            addView(this.mBlurView, new LayoutParams(-1, -1));
            this.mBlurView.setVisibility(View.INVISIBLE);
        } else if (this.version >= 19) {
            this.bgView = new BlurBgView(this.mContext);
            this.bgViewP = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            this.bgView.setLayoutParams(this.bgViewP);
            addView(this.bgView);
            this.mBlurringView = new BlurringView(this.mContext);
            this.mDownsampleFactor = 10;
            this.mBlurringView.setDownsampleFactor(this.mDownsampleFactor);
            this.mBlurringView.setOnBlurFinishListener(this);
            addView(this.mBlurringView, new LayoutParams(-1, -1));
            this.mBlurringView.setBlurredView(this.bgView);
            this.bgView.setBlurringView(this.mBlurringView);
            this.mShadowLayout = new ImageView(this.mContext);
            addView(this.mShadowLayout, new LayoutParams(-1, -1));
            this.mShadowLayout.setBackgroundResource(R.drawable.ui_sdk_app_bg_blur_shadow);
            this.mShadowLayout.setVisibility(View.INVISIBLE);
        } else {
            this.mLowMemoryBlurView = new LowMemoryBlurView(this.mContext);
            addView(this.mLowMemoryBlurView, new LayoutParams(-1, -1));
        }
    }

    public BlurBgView getBlurBgView() {
        return this.bgView;
    }

    public String getCurOverColor() {
        return this.curOverColor;
    }

    public int getOverLayoutCurAlpha() {
        return this.curOverLayoutAlpha;
    }

    public void onBlurFinish(Bitmap bitmap) {
        if (this.mSaveBlurBitmap && bitmap != null) {
            Paint paint = new Paint(3);
            Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Matrix matrix = new Matrix();
            matrix.postScale((float) this.mDownsampleFactor, (float) this.mDownsampleFactor);
            canvas.drawBitmap(bitmap, matrix, paint);
            canvas.save();
            Paint paint2 = new Paint();
            paint2.setShader(new BitmapShader(BitmapFactory.decodeResource(getResources(), R.drawable.ui_sdk_page_bg_shadow_theme2), TileMode.REPEAT, TileMode.REPEAT));
            paint2.setAlpha((int) (this.curShadowAlpha * 255.0f));
            canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), paint2);
            canvas.restore();
            ThemeUtil.getInstance().savaBitmap(getContext(), createBitmap);
        }
    }

    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    public void setBgAlpha(float f) {
        this.curAlpha = f;
        setAlpha(this.curAlpha);
    }

    public void setBlurRadius(int i) {
        this.curBlurRadiu = i;
        if (this.version >= 19 && this.mBlurringView != null) {
            this.mBlurringView.setBlurRadius(this.curBlurRadiu);
            this.mBlurringView.invalidate();
        }
    }

    public void setBlurRes(Drawable drawable) {
        if (this.version >= 19) {
            if (this.bgView != null) {
                this.bgView.setBlurRes(drawable);
            }
            if (this.mBlurringView != null) {
                this.mBlurringView.invalidate();
            }
        }
    }

    public void setCurTheme(boolean z) {
        this.mSaveBlurBitmap = z;
        try {
            CharSequence curTheme = ThemeUtil.getInstance().getCurTheme(getContext());
            if (TextUtils.equals(curTheme, "theme_1")) {
                this.bgDrawable = getResources().getDrawable(R.drawable.ui_sdk_main_page_bg);
                this.mShadowLayout.setBackgroundResource(R.drawable.ui_sdk_app_bg_blur_shadow);
                this.curBlurRadiu = 6;
                this.curShadowAlpha = 0.4f;
            } else if (TextUtils.equals(curTheme, "theme_2")) {
                this.bgDrawable = getResources().getDrawable(R.drawable.ui_sdk_main_page_bg_theme_2);
                this.mShadowLayout.setBackgroundResource(R.drawable.ui_sdk_app_bg_blur_shadow_theme_2);
                this.curBlurRadiu = 22;
                this.curShadowAlpha = 0.22f;
            }
            setBlurRadius(this.curBlurRadiu);
            setBlurRes(this.bgDrawable);
            setShadowLayoutAlpha(this.curShadowAlpha);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setOverLayoutColor(String str) {
        if (this.version >= 19) {
            this.curOverColor = str;
            int argb = Color.argb(this.curOverLayoutAlpha, Integer.valueOf(str.substring(0, 2), 16).intValue(), Integer.valueOf(str.substring(2, 4), 16).intValue(), Integer.valueOf(str.substring(4, 6), 16).intValue());
            if (this.mBlurringView != null) {
                this.mBlurringView.setOverlayColor(argb);
                this.mBlurringView.invalidate();
            }
        }
    }

    public void setOverLayoutCurAlpha(int i) {
        this.curOverLayoutAlpha = i;
    }

    public void setPageType(PAGETYPE pagetype) {
        if (TextUtils.isEmpty(this.pe)) {
            this.bgDrawable = this.mContext.getResources().getDrawable(R.drawable.ui_sdk_main_page_bg);
            if (this.version >= 19) {
                ////switch (-getcom_skyworth_ui_blurbg_BlurBgLayout$PAGETYPESwitchesValues()[pagetype.ordinal()]) {
                switch (getPAGETYPESwitchsValues()[pagetype.ordinal()]) {
                    case 1:
                        this.bgDrawable = getResources().getDrawable(R.drawable.ui_sdk_main_page_bg_theme_2);
                        this.mShadowLayout.setBackgroundResource(R.drawable.ui_sdk_app_bg_blur_shadow_theme_2);
                        this.curBlurRadiu = 22;
                        this.curShadowAlpha = 0.22f;
                        setBgAlpha(1.0f);
                        break;
                    case 2:
                        this.curBlurRadiu = 1;
                        this.curShadowAlpha = 0.0f;
                        this.bgDrawable = this.mContext.getResources().getDrawable(R.drawable.ui_sdk_other_page_bg_theme_2);
                        setBgAlpha(0.95f);
                        break;
                    case 3:
                        this.bgDrawable = getResources().getDrawable(R.drawable.ui_sdk_main_page_bg_theme_2);
                        this.mShadowLayout.setBackgroundResource(R.drawable.ui_sdk_app_bg_blur_shadow_theme_2);
                        this.curBlurRadiu = 22;
                        this.curShadowAlpha = 0.22f;
                        setBgAlpha(1.0f);
                        break;
                }
                this.curOverLayoutAlpha = 0;
                setBlurRadius(this.curBlurRadiu);
                setOverLayoutColor(this.curOverColor);
                setBlurRes(this.bgDrawable);
                this.mShadowLayout.setVisibility(View.VISIBLE);
                setShadowLayoutAlpha(this.curShadowAlpha);
            } else {
                ////switch (-getcom_skyworth_ui_blurbg_BlurBgLayout$PAGETYPESwitchesValues()[pagetype.ordinal()]) {
                switch (getPAGETYPESwitchsValues()[pagetype.ordinal()]) {
                    case 1:
                        this.bgDrawable = this.mContext.getResources().getDrawable(R.drawable.ui_sdk_main_page_bg_blur);
                        setBgAlpha(1.0f);
                        break;
                    case 2:
                        this.bgDrawable = this.mContext.getResources().getDrawable(R.drawable.ui_sdk_other_page_bg_blur);
                        setBgAlpha(0.95f);
                        break;
                    case 3:
                        this.bgDrawable = this.mContext.getResources().getDrawable(R.drawable.ui_sdk_main_page_bg_blur);
                        setBgAlpha(1.0f);
                        break;
                }
                this.mLowMemoryBlurView.setBackgroundDrawable(this.bgDrawable);
            }
            post(new Runnable() {
                public void run() {
                    if (BlurBgLayout.this.version >= 19) {
                        int[] iArr = new int[2];
                        BlurBgLayout.this.getLocationInWindow(iArr);
                        BlurBgLayout.this.bgViewP.leftMargin = -iArr[0];
                        BlurBgLayout.this.bgViewP.topMargin = -iArr[1];
                        BlurBgLayout.this.bgView.setLayoutParams(BlurBgLayout.this.bgViewP);
                        BlurBgLayout.this.mBlurringView.invalidate();
                    }
                }
            });
            return;
        }
        ////switch (-getcom_skyworth_ui_blurbg_BlurBgLayout$PAGETYPESwitchesValues()[pagetype.ordinal()]) {
        switch (getPAGETYPESwitchsValues()[pagetype.ordinal()]) {
            case 1:
                this.mBlurView.setBackgroundResource(R.drawable.ui_sdk_app_first_pe_bg);
                setBgAlpha(1.0f);
                break;
            case 2:
                this.mBlurView.setBackgroundResource(R.drawable.ui_sdk_app_other_pe_bg);
                setBgAlpha(0.95f);
                break;
            case 3:
                this.mBlurView.setBackgroundResource(R.drawable.ui_sdk_app_second_pe_bg);
                setBgAlpha(1.0f);
                break;
        }
        this.mBlurView.setVisibility(View.VISIBLE);
    }

    public void setShadowLayoutAlpha(float f) {
        if (f >= 0.0f && f <= 1.0f && this.mShadowLayout != null && this.mShadowLayout.getBackground() != null) {
            this.mShadowLayout.setAlpha(f);
        }
    }
}

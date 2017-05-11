package com.ktc.panasonichome.explosionfield;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by dinzhip on 2016/12/28.
 */

public class Utils {
    private static final float  DENSITY = Resources.getSystem().getDisplayMetrics().density;
    private static final Canvas sCanvas = new Canvas();

    private Utils() {
    }

    public static int dp2Px(int dp) {
        return Math.round(((float) dp) * DENSITY);
    }

    public static Bitmap createBitmapFromView(View view) {
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable != null && (drawable instanceof BitmapDrawable)) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        }
        Bitmap bitmap = createBitmapSafely(view.getWidth(), view.getHeight(), Config.ARGB_4444, 1);
        if (bitmap != null) {
            synchronized (sCanvas) {
                Canvas canvas = sCanvas;
                canvas.setBitmap(bitmap);
                view.draw(canvas);
                canvas.setBitmap(null);
            }
        }
        return bitmap;
    }

    public static Bitmap createBitmapSafely(int width, int height, Config config, int retryCount) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            if (retryCount <= 0) {
                return null;
            }
            System.gc();
            return createBitmapSafely(width, height, config, retryCount - 1);
        }
    }
}

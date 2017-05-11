package com.ktc.panasonichome.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.android.volley.DefaultRetryPolicy;

public class BitmapHelp {
    private static BitmapHelp instance = null;

    private BitmapHelp() {
    }

    public static BitmapHelp getInstance() {
        if (instance == null) {
            instance = new BitmapHelp();
        }
        return instance;
    }

    public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int    w           = bitmap.getWidth();
        int    h           = bitmap.getHeight();
        Matrix matrix      = new Matrix();
        float  scaleWidth  = ((float) width) / ((float) w);
        float  scaleHeight = ((float) height) / ((float) h);
        matrix.postScale(Math.max(scaleWidth, scaleHeight), Math.max(scaleWidth, scaleHeight));
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    }

    public Bitmap extractMiniThumb(Bitmap source, int width, int height) {
        return extractMiniThumb(source, width, height, true);
    }

    public Bitmap extractMiniThumb(Bitmap source, int width, int height, boolean recycle) {
        if (source == null) {
            return null;
        }
        float scale;
        if (source.getWidth() < source.getHeight()) {
            scale = ((float) width) / ((float) source.getWidth());
        } else {
            scale = ((float) height) / ((float) source.getHeight());
        }
        Matrix matrix = new Matrix();
        matrix.postScale(((float) width) / ((float) source.getWidth()), ((float) height) / (
                (float) source.getHeight()));
        Bitmap miniThumbnail = transform(matrix, source, width, height, false);
        if (recycle && miniThumbnail != source) {
            source.recycle();
        }
        return miniThumbnail;
    }

    public Bitmap createRoundConerImage(Bitmap bitmapSource, int iconWidth, int iconHeight, float
            radio) {
        int   rect_w;
        int   rect_h;
        Paint paint = new Paint(5);
        paint.setFilterBitmap(true);
        int bitmapWidth  = bitmapSource.getWidth();
        int bitmapHeight = bitmapSource.getHeight();
        int temp_width   = 0;
        int temp_height  = 0;
        int rectStarX    = 0;
        int rectStarY    = 0;
        if (bitmapWidth < iconWidth) {
            rect_w = iconWidth;
        } else {
            temp_width = bitmapWidth - iconWidth;
            rect_w = bitmapWidth;
        }
        if (bitmapHeight < iconHeight) {
            rect_h = iconHeight;
        } else {
            temp_height = bitmapHeight - iconHeight;
            rect_h = bitmapHeight;
        }
        if (temp_width > 0) {
            rectStarX = temp_width / 2;
        }
        if (temp_height > 0) {
            rectStarY = temp_height / 2;
        }
        Bitmap target_bm = Bitmap.createBitmap(rect_w, rect_h, Config.ARGB_8888);
        Canvas canvas    = new Canvas(target_bm);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        canvas.drawRoundRect(new RectF((float) rectStarX, (float) rectStarY, (float) (rect_w -
                rectStarX), (float) (rect_h - rectStarY)), radio, radio, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmapSource, 0.0f, 0.0f, paint);
        return target_bm;
    }

    public Bitmap createCircleImage(Bitmap bitmapSource, float radius, int iconWidth, int
            iconHeight) {
        Paint paint = new Paint(5);
        paint.setFilterBitmap(true);
        Bitmap target_bm = Bitmap.createBitmap(iconWidth, iconHeight, Config.ARGB_8888);
        Canvas canvas    = new Canvas(target_bm);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        canvas.drawCircle((float) (iconWidth / 2), (float) (iconHeight / 2), radius, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmapSource, 0.0f, 0.0f, paint);
        return target_bm;
    }

    public Bitmap createRectImage(Bitmap bitmapSource, int iconWidth, int iconHeight) {
        int   rect_w;
        int   rect_h;
        Paint paint = new Paint(5);
        paint.setFilterBitmap(true);
        int bitmapWidth  = bitmapSource.getWidth();
        int bitmapHeight = bitmapSource.getHeight();
        int temp_width   = 0;
        int temp_height  = 0;
        int rectStarX    = 0;
        int rectStarY    = 0;
        if (bitmapWidth < iconWidth) {
            rect_w = iconWidth;
        } else {
            temp_width = bitmapWidth - iconWidth;
            rect_w = bitmapWidth;
        }
        if (bitmapHeight < iconHeight) {
            rect_h = iconHeight;
        } else {
            temp_height = bitmapHeight - iconHeight;
            rect_h = bitmapHeight;
        }
        if (temp_width > 0) {
            rectStarX = temp_width / 2;
        }
        if (temp_height > 0) {
            rectStarY = temp_height / 2;
        }
        Bitmap target_bm = Bitmap.createBitmap(rect_w, rect_h, Config.ARGB_8888);
        Canvas canvas    = new Canvas(target_bm);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, 3));
        canvas.drawRect(new RectF((float) rectStarX, (float) rectStarY, (float) (rect_w -
                rectStarX), (float) (rect_h - rectStarY)), paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmapSource, 0.0f, 0.0f, paint);
        return target_bm;
    }

    public static Bitmap transform(Matrix scaler, Bitmap source, int targetWidth, int
            targetHeight, boolean scaleUp) {
        int deltaX = source.getWidth() - targetWidth;
        int deltaY = source.getHeight() - targetHeight;
        if (scaleUp || (deltaX >= 0 && deltaY >= 0)) {
            Bitmap b1;
            Bitmap b2;
            float  bitmapWidthF  = (float) source.getWidth();
            float  bitmapHeightF = (float) source.getHeight();
            float  scale;
            if (bitmapWidthF / bitmapHeightF > ((float) targetWidth) / ((float) targetHeight)) {
                scale = ((float) targetHeight) / bitmapHeightF;
                if (scale < 0.9f || scale > 1.0f) {
                    scaler.setScale(scale, scale);
                } else {
                    scaler = null;
                }
            } else {
                scale = ((float) targetWidth) / bitmapWidthF;
                if (scale < 0.9f || scale > 1.0f) {
                    scaler.setScale(scale, scale);
                } else {
                    scaler = null;
                }
            }
            if (scaler != null) {
                b1 = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                        scaler, true);
            } else {
                b1 = source;
            }
             b2 = Bitmap.createBitmap(b1, Math.max(0, b1.getWidth() - targetWidth) / 2,
                    Math.max(0, b1.getHeight() - targetHeight) / 2, targetWidth, targetHeight);
            if (b1 != source) {
                b1.recycle();
            }
            return b2;
        }
        Bitmap  b2 = Bitmap.createBitmap(targetWidth, targetHeight, Config.ARGB_8888);
        Canvas c          = new Canvas(b2);
        int    deltaXHalf = Math.max(0, deltaX / 2);
        int    deltaYHalf = Math.max(0, deltaY / 2);
        int    i          = deltaXHalf;
        int    i2         = deltaYHalf;
        Rect   rect       = new Rect(i, i2, Math.min(targetWidth, source.getWidth()) +
                deltaXHalf, Math.min(targetHeight, source.getHeight()) + deltaYHalf);
        int    dstX       = (targetWidth - rect.width()) / 2;
        int    dstY       = (targetHeight - rect.height()) / 2;
        c.drawBitmap(source, rect, new Rect(dstX, dstY, targetWidth - dstX, targetHeight - dstY), null);
        return b2;
    }
}

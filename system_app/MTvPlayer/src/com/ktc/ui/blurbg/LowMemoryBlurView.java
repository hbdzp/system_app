package com.ktc.ui.blurbg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class LowMemoryBlurView extends View {
    private Bitmap curBitmap;

    public LowMemoryBlurView(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.curBitmap != null) {
            canvas.translate(-getX(), -getY());
            canvas.drawBitmap(this.curBitmap, 0.0f, 0.0f, null);
        }
    }

    public void setCurBitmap(Drawable drawable) {
        this.curBitmap = ((BitmapDrawable) drawable).getBitmap();
        postInvalidate();
    }
}

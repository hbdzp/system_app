package com.ktc.panasonichome.view.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

public class ShaderTextView extends FrameLayout {
    private Paint mPaint;
    private TextView mTextView;

    public ShaderTextView(Context context) {
        super(context);
        initPaint(context);
    }

    public ShaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public ShaderTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint(context);
    }

    private void initPaint(Context context) {
        this.mTextView = new TextView(context);
        this.mPaint = this.mTextView.getPaint();
        this.mPaint.setFlags(1);
        addView(this.mTextView, new LayoutParams(-2, -2, 17));
    }

    public void setTextColor(int color) {
        this.mTextView.setTextColor(color);
    }

    public void setText(String text) {
        this.mTextView.setText(text);
    }

    public void setTextSize(float size) {
        this.mTextView.setTextSize(size);
    }

    public void setShader(float x0, float y0, float x1, float y1, int color0, int color1, int bg_id) {
        Bitmap res = BitmapFactory.decodeResource(getResources(), bg_id);
        Bitmap overlay = Bitmap.createBitmap(res.getWidth(), res.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        Paint paint = new Paint();
        paint.setFlags(2);
        paint.setShader(new LinearGradient(x0, y0, x1, y1, color0, color1, TileMode.CLAMP));
        canvas.drawBitmap(res, 0.0f, 0.0f, null);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawRect(0.0f, 0.0f, (float) res.getWidth(), (float) res.getHeight(), paint);
        setBackgroundDrawable(new BitmapDrawable(overlay));
        this.mPaint.setShader(new LinearGradient(x0, y0, x1, y1, color0, color1, TileMode.CLAMP));
        postInvalidate();
    }
}

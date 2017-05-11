package com.ktc.ui.blurbg;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Allocation.MipmapControl;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.view.View;
////import com.ktc.commen.ui.R;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
import com.horion.tv.R;
public class BlurringView extends View {
    private Bitmap mBitmapToBlur;
    private OnBlurFinishListener mBlurFinishListener;
    private Allocation mBlurInput;
    private Allocation mBlurOutput;
    private ScriptIntrinsicBlur mBlurScript;
    private Bitmap mBlurredBitmap;
    private View mBlurredView;
    private int mBlurredViewHeight;
    private int mBlurredViewWidth;
    private Canvas mBlurringCanvas;
    private int mDownsampleFactor;
    private boolean mDownsampleFactorChanged;
    private int mOverlayColor;
    private RenderScript mRenderScript;

    public interface OnBlurFinishListener {
        void onBlurFinish(Bitmap bitmap);
    }

    public BlurringView(Context context) {
        this(context, null);
    }

    public BlurringView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Resources resources = getResources();
        int integer = resources.getInteger(R.integer.default_blur_radius);
        int integer2 = resources.getInteger(R.integer.default_downsample_factor);
        int color = resources.getColor(R.color.default_overlay_color);
        initializeRenderScript(context);
        ////TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.PxBlurringView);
        ////setBlurRadius(obtainStyledAttributes.getInt(0, integer));
        ////setDownsampleFactor(obtainStyledAttributes.getInt(1, integer2));
        ////setOverlayColor(obtainStyledAttributes.getColor(2, color));
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.PxBlurringView);
        setBlurRadius(obtainStyledAttributes.getInt(R.styleable.PxBlurringView_blur_radius, integer));
        setDownsampleFactor(obtainStyledAttributes.getInt(R.styleable.PxBlurringView_downsample_factor, integer2));
        setOverlayColor(obtainStyledAttributes.getColor(R.styleable.PxBlurringView_overlay_color, color));
        obtainStyledAttributes.recycle();
    }

    private void initializeRenderScript(Context context) {
        this.mRenderScript = RenderScript.create(context);
        this.mBlurScript = ScriptIntrinsicBlur.create(this.mRenderScript, Element.U8_4(this.mRenderScript));
    }

    protected void blur() {
        this.mBlurInput.copyFrom(this.mBitmapToBlur);
        this.mBlurScript.setInput(this.mBlurInput);
        this.mBlurScript.forEach(this.mBlurOutput);
        this.mBlurOutput.copyTo(this.mBlurredBitmap);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mBlurredView != null) {
            if (prepare()) {
                if (this.mBlurredView.getBackground() == null || !(this.mBlurredView.getBackground() instanceof ColorDrawable)) {
                    this.mBitmapToBlur.eraseColor(0);
                } else {
                    this.mBitmapToBlur.eraseColor(((ColorDrawable) this.mBlurredView.getBackground()).getColor());
                }
                this.mBlurredView.draw(this.mBlurringCanvas);
                blur();
                canvas.save();
                canvas.translate(this.mBlurredView.getX() - getX(), this.mBlurredView.getY() - getY());
                canvas.scale((float) this.mDownsampleFactor, (float) this.mDownsampleFactor);
                canvas.drawBitmap(this.mBlurredBitmap, 0.0f, 0.0f, null);
                canvas.restore();
            }
            canvas.drawColor(this.mOverlayColor);
            if (this.mBlurFinishListener != null) {
                this.mBlurFinishListener.onBlurFinish(this.mBlurredBitmap);
            }
            ////KtcLogger.v("lgx", "---------this   isHardware----------->" + canvas.isHardwareAccelerated());
        }
    }

    protected boolean prepare() {
        int width = this.mBlurredView.getWidth();
        int height = this.mBlurredView.getHeight();
        if (this.mBlurringCanvas != null && !this.mDownsampleFactorChanged && this.mBlurredViewWidth == width && this.mBlurredViewHeight == height) {
            return true;
        }
        this.mDownsampleFactorChanged = false;
        this.mBlurredViewWidth = width;
        this.mBlurredViewHeight = height;
        width /= this.mDownsampleFactor;
        height /= this.mDownsampleFactor;
        if (width % 4 > 0) {
            width = (width - (width % 4)) + 4;
        }
        if (height % 4 > 0) {
            height = (height - (height % 4)) + 4;
        }
        if (!(this.mBlurredBitmap != null && this.mBlurredBitmap.getWidth() == width && this.mBlurredBitmap.getHeight() == height)) {
            this.mBitmapToBlur = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            if (this.mBitmapToBlur == null) {
                return false;
            }
            this.mBlurredBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            if (this.mBlurredBitmap == null) {
                return false;
            }
        }
        this.mBlurringCanvas = new Canvas(this.mBitmapToBlur);
        this.mBlurringCanvas.scale(1.0f / ((float) this.mDownsampleFactor), 1.0f / ((float) this.mDownsampleFactor));
        this.mBlurInput = Allocation.createFromBitmap(this.mRenderScript, this.mBitmapToBlur, MipmapControl.MIPMAP_NONE, 1);
        this.mBlurOutput = Allocation.createTyped(this.mRenderScript, this.mBlurInput.getType());
        return true;
    }

    public void setBlurRadius(int i) {
        if (this.mBlurScript != null) {
            this.mBlurScript.setRadius((float) i);
        }
    }

    public void setBlurredView(View view) {
        this.mBlurredView = view;
    }

    public void setDownsampleFactor(int i) {
        if (i <= 0) {
            throw new IllegalArgumentException("Downsample factor must be greater than 0.");
        } else if (this.mDownsampleFactor != i) {
            this.mDownsampleFactor = i;
            this.mDownsampleFactorChanged = true;
        }
    }

    public void setOnBlurFinishListener(OnBlurFinishListener onBlurFinishListener) {
        this.mBlurFinishListener = onBlurFinishListener;
    }

    public void setOverlayColor(int i) {
        this.mOverlayColor = i;
    }
}

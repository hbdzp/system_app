package com.ktc.panasonichome.view.api;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.ScreenParams;

public class SkyLoadingView extends View {
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator SWEEP_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private int angleAnimatorDuration;
    private int arcColor;
    private int circleColor;
    private Paint circlePaint;
    private final RectF fBounds;
    private Property<SkyLoadingView, Float> mAngleProperty;
    private float mBorderWidth;
    private float mCurrentGlobalAngle;
    private float mCurrentGlobalAngleOffset;
    private float mCurrentSweepAngle;
    private boolean mModeAppearing;
    private ObjectAnimator mObjectAnimatorAngle;
    private ObjectAnimator mObjectAnimatorSweep;
    private Paint mPaint;
    private boolean mRunning;
    private Property<SkyLoadingView, Float> mSweepProperty;
    private int minSweepAngle;
    private float radius;
    private int sweepAnimatorDuration;

    class C03023 extends AnimatorListenerAdapter {
        C03023() {
        }

        public void onAnimationRepeat(Animator animation) {
            super.onAnimationRepeat(animation);
            SkyLoadingView.this.toggleAppearingMode();
        }
    }

    public SkyLoadingView(Context context) {
        this(context, null);
    }

    public SkyLoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkyLoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.fBounds = new RectF();
        this.mModeAppearing = true;
        this.mAngleProperty = new Property<SkyLoadingView, Float>(Float.class, "angle") {
            public Float get(SkyLoadingView object) {
                return Float.valueOf(object.getCurrentGlobalAngle());
            }

            public void set(SkyLoadingView object, Float value) {
                object.setCurrentGlobalAngle(value.floatValue());
            }
        };
        this.mSweepProperty = new Property<SkyLoadingView, Float>(Float.class, "arc") {
            public Float get(SkyLoadingView object) {
                return Float.valueOf(object.getCurrentSweepAngle());
            }

            public void set(SkyLoadingView object, Float value) {
                object.setCurrentSweepAngle(value.floatValue());
            }
        };
        initValue(context, attrs, defStyle);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.fBounds.left = (this.mBorderWidth / 2.0f) + 0.5f;
        this.fBounds.right = (((float) w) - (this.mBorderWidth / 2.0f)) - 0.5f;
        this.fBounds.top = (this.mBorderWidth / 2.0f) + 0.5f;
        this.fBounds.bottom = (((float) h) - (this.mBorderWidth / 2.0f)) - 0.5f;
        this.radius = (((float) (w / 2)) - (this.mBorderWidth / 2.0f)) + 0.5f;
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE) {
            stopAnim();
        } else {
            startAnim();
        }
    }

    protected void onDetachedFromWindow() {
        stopAnim();
        super.onDetachedFromWindow();
    }

    private void initValue(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircularLoadingView,
                defStyleAttr, 0);
        this.mBorderWidth = a.getDimension(R.styleable.CircularLoadingView_loadingviewborderWidth, (float) ScreenParams.getInstence
                (context)
                .getResolutionValue(4));
        this.circleColor = a.getColor(R.styleable.CircularLoadingView_loadingviewcircleBackgroundColor,
                Color.TRANSPARENT);
        this.arcColor = a.getColor(R.styleable.CircularLoadingView_loadingviewcircleRunningColor, getResources().getColor(R.color
                .circlular_default_running_color));
        this.sweepAnimatorDuration = a.getInt(R.styleable.CircularLoadingView_loadingviewsweepAnimationDurationMillis, getResources().getInteger(R.integer
                .circular_default_sweepAnimationDuration));
        this.angleAnimatorDuration = a.getInt(R.styleable.CircularLoadingView_loadingviewangleAnimationDurationMillis, getResources().getInteger(R.integer.circular_default_angleAnimationDurationMillis));
        this.minSweepAngle = a.getInt(R.styleable.CircularLoadingView_loadingviewminSweepAngle, getResources().getInteger(R.integer
                .circular_default_miniSweepAngle));
        a.recycle();
        this.circlePaint = new Paint(5);
        this.circlePaint.setColor(this.circleColor);
        this.circlePaint.setStyle(Style.STROKE);
        this.circlePaint.setStrokeCap(Cap.ROUND);
        this.circlePaint.setStrokeWidth(this.mBorderWidth);
        this.mPaint = new Paint(5);
        this.mPaint.setColor(this.arcColor);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeWidth(this.mBorderWidth);
        initAnim();
    }

    private void initAnim() {
        this.mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, this.mAngleProperty, new float[]{360.0f});
        this.mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        this.mObjectAnimatorAngle.setDuration((long) this.angleAnimatorDuration);
        this.mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);
        this.mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);
        this.mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, this.mSweepProperty, new float[]{360.0f - ((float) (this.minSweepAngle * 2))});
        this.mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        this.mObjectAnimatorSweep.setDuration((long) this.sweepAnimatorDuration);
        this.mObjectAnimatorSweep.setRepeatMode(ValueAnimator.RESTART);
        this.mObjectAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        this.mObjectAnimatorSweep.addListener(new C03023());
    }

    private void toggleAppearingMode() {
        this.mModeAppearing = !this.mModeAppearing;
        if (this.mModeAppearing) {
            this.mCurrentGlobalAngleOffset = (this.mCurrentGlobalAngleOffset + ((float) (this.minSweepAngle * 2))) % 360.0f;
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), this.radius, this.circlePaint);
        float startAngle = this.mCurrentGlobalAngle - this.mCurrentGlobalAngleOffset;
        float sweepAngle = this.mCurrentSweepAngle;
        if (this.mModeAppearing) {
            sweepAngle += (float) this.minSweepAngle;
        } else {
            startAngle += sweepAngle;
            sweepAngle = (360.0f - sweepAngle) - ((float) this.minSweepAngle);
        }
        canvas.drawArc(this.fBounds, startAngle, sweepAngle, false, this.mPaint);
    }

    public float getCurrentGlobalAngle() {
        return this.mCurrentGlobalAngle;
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        this.mCurrentGlobalAngle = currentGlobalAngle;
        invalidate();
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        this.mCurrentSweepAngle = currentSweepAngle;
        invalidate();
    }

    public float getCurrentSweepAngle() {
        return this.mCurrentSweepAngle;
    }

    public boolean isSpinning() {
        return this.mRunning;
    }

    public void startAnim() {
        if (!isSpinning()) {
            this.mRunning = true;
            setLayerType(2, null);
            this.mObjectAnimatorAngle.start();
            this.mObjectAnimatorSweep.start();
            invalidate();
        }
    }

    public void stopAnim() {
        if (isSpinning()) {
            this.mRunning = false;
            setLayerType(0, null);
            this.mObjectAnimatorAngle.cancel();
            this.mObjectAnimatorSweep.cancel();
            invalidate();
        }
    }

    public void setStrokeWidth(int width) {
        this.mBorderWidth = (float) width;
        this.mPaint.setStrokeWidth(this.mBorderWidth);
        this.circlePaint.setStrokeWidth(this.mBorderWidth);
    }

    public float getStokeWidth() {
        return this.mBorderWidth;
    }

    public int getArcColor() {
        return this.arcColor;
    }

    public void setArcColor(int arcColor) {
        this.arcColor = arcColor;
        this.mPaint.setColor(this.arcColor);
    }

    public int getCircleColor() {
        return this.circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        this.circlePaint.setColor(this.circleColor);
    }
}

package com.ktc.panasonichome.explosionfield;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.Random;

/**
 * Created by dinzhip on 2016/12/28.
 */
public class ExplosionAnimator extends ValueAnimator {
    static long DEFAULT_DURATION = 0x400;
    private static final Interpolator DEFAULT_INTERPOLATOR = new AccelerateInterpolator(0.6f);
    private static final float END_VALUE = 1.4f;
    private static final float f73V = ((float) Utils.dp2Px(2));
    private static final float f74W = ((float) Utils.dp2Px(1));
    private static final float f75X = ((float) Utils.dp2Px(5));
    private static final float f76Y = ((float) Utils.dp2Px(20));
    private Rect mBound;
    private View mContainer;
    private Paint mPaint = new Paint();
    private Particle[] mParticles;

    private class Particle {
        float alpha;
        float baseCx;
        float baseCy;
        float baseRadius;
        float bottom;
        int color;
        float cx;
        float cy;
        float life;
        float mag;
        float neg;
        float overflow;
        float radius;
        float top;

        private Particle() {
        }

        public void advance(float factor) {
            float f = 0.0f;
            float normalization = factor / ExplosionAnimator.END_VALUE;
            if (normalization < this.life || normalization > 1.0f - this.overflow) {
                this.alpha = 0.0f;
                return;
            }
            normalization = (normalization - this.life) / ((1.0f - this.life) - this.overflow);
            float f2 = normalization * ExplosionAnimator.END_VALUE;
            if (normalization >= 0.7f) {
                f = (normalization - 0.7f) / 0.3f;
            }
            this.alpha = 1.0f - f;
            f = this.bottom * f2;
            this.cx = this.baseCx + f;
            this.cy = ((float) (((double) this.baseCy) - (((double) this.neg) * Math.pow((double) f, 2.0d)))) - (this.mag * f);
            this.radius = ExplosionAnimator.f73V + ((this.baseRadius - ExplosionAnimator.f73V) * f2);
        }
    }

    public ExplosionAnimator(View container, Bitmap bitmap, Rect bound) {
        this.mBound = new Rect(bound);
        this.mParticles = new Particle[225];
        Random random = new Random(System.currentTimeMillis());
        int w = bitmap.getWidth() / 17;
        int h = bitmap.getHeight() / 17;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                this.mParticles[(i * 15) + j] = generateParticle(bitmap.getPixel((j + 1) * w, (i + 1) * h), random);
            }
        }
        this.mContainer = container;
        setFloatValues(new float[]{0.0f, END_VALUE});
        setInterpolator(DEFAULT_INTERPOLATOR);
        setDuration(DEFAULT_DURATION);
    }

    private Particle generateParticle(int color, Random random) {
        Particle particle = new Particle();
        particle.color = color;
        particle.radius = f73V;
        if (random.nextFloat() < 0.2f) {
            particle.baseRadius = f73V + ((f75X - f73V) * random.nextFloat());
        } else {
            particle.baseRadius = f74W + ((f73V - f74W) * random.nextFloat());
        }
        float nextFloat = random.nextFloat();
        particle.top = ((float) this.mBound.height()) * ((random.nextFloat() * 0.18f) + 0.2f);
        particle.top = nextFloat < 0.2f ? particle.top : particle.top + ((particle.top * 0.2f) * random.nextFloat());
        particle.bottom = (((float) this.mBound.height()) * (random.nextFloat() - 0.5f)) * 1.8f;
        float f = nextFloat < 0.2f ? particle.bottom : nextFloat < 0.8f ? particle.bottom * 0.6f : particle.bottom * 0.3f;
        particle.bottom = f;
        particle.mag = (particle.top * 4.0f) / particle.bottom;
        particle.neg = (-particle.mag) / particle.bottom;
        f = ((float) this.mBound.centerX()) + (f76Y * (random.nextFloat() - 0.5f));
        particle.baseCx = f;
        particle.cx = f;
        f = ((float) this.mBound.centerY()) + (f76Y * (random.nextFloat() - 0.5f));
        particle.baseCy = f;
        particle.cy = f;
        particle.life = random.nextFloat() * 0.14f;
        particle.overflow = random.nextFloat() * 0.4f;
        particle.alpha = 1.0f;
        return particle;
    }

    public boolean draw(Canvas canvas) {
        if (!isStarted()) {
            return false;
        }
        for (Particle particle : this.mParticles) {
            particle.advance(((Float) getAnimatedValue()).floatValue());
            if (particle.alpha > 0.0f) {
                this.mPaint.setColor(particle.color);
                this.mPaint.setAlpha((int) (((float) Color.alpha(particle.color)) * particle.alpha));
                canvas.drawCircle(particle.cx, particle.cy, particle.radius, this.mPaint);
            }
        }
        this.mContainer.invalidate();
        return true;
    }

    public void start() {
        super.start();
        this.mContainer.invalidate(this.mBound);
    }
}

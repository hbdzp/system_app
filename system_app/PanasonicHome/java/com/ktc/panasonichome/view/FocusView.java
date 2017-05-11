package com.ktc.panasonichome.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

import com.ktc.panasonichome.utils.LogUtils;

public class FocusView extends ImageView {
    private int destHeight;
    private int destWidth;
    private boolean executeAnim = false;
    private int posX;
    private int posY;
    private float stepCount = 4.0f;
    private int stepHeight;
    private int stepWidth;
    private int stepX;
    private int stepY;

    public FocusView(Context context) {
        super(context);
    }

    public void changeFocusPos(int posX, int posY, int destWidth, int destHeight) {
        this.executeAnim = true;
        this.posX = posX;
        this.posY = posY;
        this.destWidth = destWidth;
        this.destHeight = destHeight;
        initStep();
        LogUtils.i("dzp", "-------width = " + destWidth + ", destHeight = " + destHeight + "," +
                " posX" +
                " = " + posX + ", posY = " + posY);
         LogUtils.i("dzp", "-------stepWidth = " + this.stepWidth + ", stepHeight = " + this.stepHeight);
        invalidate();
    }

    public void changeFocusPos(int destWidth, int destHeight) {
        this.executeAnim = true;
        this.destWidth = destWidth;
        this.destHeight = destHeight;
        initStep();
         LogUtils.i("dzp", "-------width = " + destWidth + ", destHeight = " + destHeight + ", posX = " + this.posX + ", posY = " + this.posY);
         LogUtils.i("dzp", "-------stepWidth = " + this.stepWidth + ", stepHeight = " + this.stepHeight);
        invalidate();
    }

    public void setFocusPos(int posX, int posY, int destWidth, int destHeight) {
        this.executeAnim = false;
        LayoutParams frameParams = (LayoutParams) getLayoutParams();
        frameParams.leftMargin = posX;
        frameParams.topMargin = posY;
        frameParams.width = destWidth;
        frameParams.height = destHeight;
        setLayoutParams(frameParams);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.executeAnim) {
            ViewGroup.LayoutParams params = getLayoutParams();
            if (params instanceof LayoutParams) {
                LayoutParams frameParams = (LayoutParams) params;
                 LogUtils.i("dzp", "w = " + getWidth() + ", paramWidth = " + frameParams.width + ", h = " + getHeight() + ", paramHeight = " + frameParams.height);
                 LogUtils.i("dzp", "left = " + getLeft() + ", paramLeft = " + frameParams.leftMargin + ", top = " + getTop() + ", paramTop = " + frameParams.topMargin);
                if (getWidth() != this.destWidth || getHeight() != this.destHeight || getLeft() != this.posX || getTop() != this.posY) {
                    if (getWidth() != this.destWidth) {
                        if (Math.abs(getWidth() - this.destWidth) < Math.abs(this.stepWidth)) {
                            frameParams.width = this.destWidth;
                        } else {
                            frameParams.width = getWidth() + this.stepWidth;
                        }
                    }
                    if (getHeight() != this.destHeight) {
                        if (Math.abs(frameParams.height - this.destHeight) < Math.abs(this.stepHeight)) {
                            frameParams.height = this.destHeight;
                        } else {
                            frameParams.height = getHeight() + this.stepHeight;
                        }
                    }
                    if (frameParams.leftMargin != this.posX) {
                        if (Math.abs(getLeft() - this.posX) < Math.abs(this.stepX)) {
                            frameParams.leftMargin = this.posX;
                        } else {
                            frameParams.leftMargin += this.stepX;
                        }
                    }
                    if (frameParams.topMargin != this.posY) {
                        if (Math.abs(getTop() - this.posY) < Math.abs(this.stepY)) {
                            frameParams.topMargin = this.posY;
                        } else {
                            frameParams.topMargin += this.stepY;
                        }
                    }
                    setLayoutParams(frameParams);
                    invalidate();
                }
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        initStep();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void initStep() {
        if (this.posX == getLeft()) {
            this.stepX = 0;
        } else {
            this.stepX = (int) (((float) (this.posX - getLeft())) / this.stepCount);
            if (this.stepX == 0) {
                this.stepX = (this.posX - getLeft()) / Math.abs(this.posX - getLeft());
            }
        }
        if (this.posY == getTop()) {
            this.stepY = 0;
        } else {
            this.stepY = (int) (((float) (this.posY - getTop())) / this.stepCount);
            if (this.stepY == 0) {
                this.stepY = (this.posY - getTop()) / Math.abs(this.posY - getTop());
            }
        }
        if (this.destWidth == getWidth()) {
            this.stepWidth = 0;
        } else {
            this.stepWidth = (int) (((float) (this.destWidth - getWidth())) / this.stepCount);
            if (this.stepWidth == 0) {
                this.stepWidth = (this.destWidth - getWidth()) / Math.abs(this.destWidth - getWidth());
            }
        }
        if (this.destHeight == getHeight()) {
            this.stepHeight = 0;
            return;
        }
        this.stepHeight = (int) (((float) (this.destHeight - getHeight())) / this.stepCount);
        if (this.stepHeight == 0) {
            this.stepHeight = (this.destHeight - getHeight()) / Math.abs(this.destHeight - getHeight());
        }
    }
}

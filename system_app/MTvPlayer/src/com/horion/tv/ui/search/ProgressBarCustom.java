package com.horion.tv.ui.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.horion.tv.R;
////import com.skyworth.framework.skysdk.logger.SkyLogger;
////import com.tianci.tv.ui.searchview.popup.SkyUITvViewServer;

public class ProgressBarCustom extends FrameLayout {
    ////private static float div = SkyUITvViewServer.getResolutionDiv();
    private static float div = 1.0f;
    private ImageView barBgImage;
    private int barBgPicResId = R.drawable.progressbg;
    private ImageView barImage;
    private LayoutParams barPicLp = null;
    private int barPicResId = R.drawable.progress_green;
    private ImageView bgImage;
    int bgheight = ((int) (31.0f / div));
    int bgwidth = ((int) (623.0f / div));
    int firstwidth = ((int) (15.0f / div));
    int height = ((int) (10.0f / div));
    private int max = 100;
    private int min = 0;
    private int progress = 0;
    int width = ((int) (600.0f / div));

    public ProgressBarCustom(Context context) {
        super(context);
        this.barImage = new ImageView(context);
        this.barImage.setBackgroundResource(this.barPicResId);
        this.barBgImage = new ImageView(context);
        this.barBgImage.setBackgroundResource(this.barBgPicResId);
        this.bgImage = new ImageView(context);
        addView(this.bgImage, new LayoutParams(this.bgwidth, this.bgheight, 3));
        addView(this.barBgImage, new LayoutParams(this.bgwidth, this.bgheight, 3));
        this.barPicLp = new LayoutParams(this.width, this.height);
        this.barPicLp.gravity = Gravity.CENTER_VERTICAL;
        this.barPicLp.leftMargin = (int) (10.0f / div);
        addView(this.barImage, this.barPicLp);
    }

    public ProgressBarCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.barImage = new ImageView(context);
        this.barImage.setBackgroundResource(this.barPicResId);
        this.barBgImage = new ImageView(context);
        this.barBgImage.setBackgroundResource(this.barBgPicResId);
        this.bgImage = new ImageView(context);
        addView(this.bgImage, new LayoutParams(this.bgwidth, this.bgheight, 3));
        addView(this.barBgImage, new LayoutParams(this.bgwidth, this.bgheight, 3));
        this.barPicLp = new LayoutParams(this.width, this.height);
        this.barPicLp.gravity = Gravity.CENTER_VERTICAL;
        this.barPicLp.leftMargin = (int) (10.0f / div);
        addView(this.barImage, this.barPicLp);
    }

    private void refreshView() {
        if (this.progress == 0) {
            this.barPicLp.width = 0;
        } else if (this.progress == 1) {
            this.barPicLp.width = this.firstwidth;
        } else if (this.progress > 1) {
            float f = ((float) ((this.progress - this.min) - 1)) / ((float) ((this.max - 1) - this.min));
            this.barPicLp.width = ((int) (f * ((float) (this.width - this.firstwidth)))) + this.firstwidth;
        }
        updateViewLayout(this.barImage, this.barPicLp);
    }

    public void setDrawableResId(int i, int i2) {
        this.barPicResId = i;
        this.barBgPicResId = i2;
        this.barImage.setBackgroundResource(this.barPicResId);
        this.barBgImage.setBackgroundResource(this.barBgPicResId);
    }

    public void setProgress(int i) {
        if (i >= this.min && i <= this.max) {
            ///SkyLogger.d("lwr", "progress>>>: " + i);
            this.progress = i;
            refreshView();
        }
    }

    public void setRange(int i, int i2) {
        this.min = i;
        this.max = i2;
        refreshView();
    }

    public void setSize(int i, int i2) {
        ////SkyLogger.d("lwr", "ProgressBarCustom:" + i + " || " + i2);
        this.width = i;
        this.height = i2;
        updateViewLayout(this.barImage, new LayoutParams(i, i2, 17));
    }
}

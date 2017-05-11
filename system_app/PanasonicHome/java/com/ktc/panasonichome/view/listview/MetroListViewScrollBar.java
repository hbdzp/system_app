package com.ktc.panasonichome.view.listview;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.view.customview.SlideFocusView;
import com.ktc.panasonichome.view.customview.SlideFocusView.FocusChangedEvent;
import com.ktc.panasonichome.view.customview.SlideFocusView.FocusViewRevision;

public class MetroListViewScrollBar extends FrameLayout {
    private OnFocusChangeListener focusChangeListener;
    private FocusChangedEvent     focusChangedEvent;
    private FocusViewRevision     focusViewRevision;
    private int                   lastPosition;
    private boolean mScrollBarFocusble = false;
    private ImageView mScrollBarView;
    private int mScrollBarViewHeight = 0;
    private SlideFocusView mSlideFocusView;
    private LayoutParams   scrollBarParams;

    class C03321 implements Runnable {
        C03321() {
        }

        public void run() {
            LogUtils.v("dzp", "setScrollBar_focusViewPosition=====>");
            if (MetroListViewScrollBar.this.mSlideFocusView != null) {
                MetroListViewScrollBar.this.mSlideFocusView.moveFocusTo(MetroListViewScrollBar.this.mScrollBarView, MetroListViewScrollBar.this.focusViewRevision);
            }
        }
    }

    public MetroListViewScrollBar(Context context) {
        super(context);
        this.mScrollBarView = new ImageView(context);
        this.scrollBarParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        this.mScrollBarView.setLayoutParams(this.scrollBarParams);
        addView(this.mScrollBarView);
    }

    public void setSlidFocusView(SlideFocusView focusView) {
        this.mSlideFocusView = focusView;
    }

    public ImageView getScrollBarView() {
        return this.mScrollBarView;
    }

    public void setFocusChangedEvent(FocusChangedEvent focusChangedEvent, FocusViewRevision rev, OnFocusChangeListener focusChangeListener) {
        this.focusChangedEvent = focusChangedEvent;
        this.focusViewRevision = rev;
        this.focusChangeListener = focusChangeListener;
    }

    public void setScrollBarFocusble(boolean mScrollBarFocusble) {
        this.mScrollBarFocusble = mScrollBarFocusble;
        if (mScrollBarFocusble) {
            this.mScrollBarView.setFocusable(true);
            this.mScrollBarView.setFocusableInTouchMode(true);
            this.focusChangedEvent.registerView(this.mScrollBarView, this.focusViewRevision, this.focusChangeListener);
            return;
        }
        this.mScrollBarView.setFocusable(false);
    }

    public boolean getScrollBarFocusBle() {
        return this.mScrollBarFocusble;
    }

    public boolean hasFocused() {
        return this.mScrollBarView.hasFocus();
    }

    public void setScrollBarBg(int resid) {
        setBackgroundResource(resid);
    }

    public void setScrollBarIcon(int resid) {
        BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(resid);
        if (bd != null) {
            this.mScrollBarViewHeight = bd.getBitmap().getHeight();
        }
        this.mScrollBarView.setBackgroundResource(resid);
    }

    public void setScrollBarPosition(int position) {
        LayoutParams layoutParams = this.scrollBarParams;
        layoutParams.topMargin += position - this.lastPosition;
        int temp = 0;
        if (this.scrollBarParams.topMargin < 0) {
            temp = this.scrollBarParams.topMargin;
            this.scrollBarParams.topMargin = 0;
        }
        this.mScrollBarView.setLayoutParams(this.scrollBarParams);
        if (this.mSlideFocusView != null && this.mScrollBarView.hasFocus()) {
            this.mSlideFocusView.stopAnimationOnce();
            this.mSlideFocusView.moveFocusByCurrent(0, (position - this.lastPosition) - temp);
        }
        this.lastPosition = position;
    }

    public void setScrollBarToPosition(int position) {
        this.lastPosition = 0;
        this.scrollBarParams.topMargin = position;
        this.mScrollBarView.setLayoutParams(this.scrollBarParams);
    }

    public void setScrollBar_focusViewPosition(int position) {
        this.lastPosition = 0;
        this.scrollBarParams.topMargin = position;
        this.mScrollBarView.setLayoutParams(this.scrollBarParams);
        this.mScrollBarView.post(new C03321());
    }

    public void setDefaultLastPosition() {
        this.lastPosition = 0;
    }

    public int getScrollBarHeight() {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null) {
            return params.height;
        }
        return 0;
    }

    public int getScrollBarViewHeight() {
        return this.mScrollBarViewHeight;
    }

    public int getScrollBarCurrentY() {
        return this.scrollBarParams.topMargin;
    }
}

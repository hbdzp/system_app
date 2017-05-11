package com.ktc.panasonichome.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
//import android.view.KeyEvent;
import android.widget.Scroller;

import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;

public class CustomRecycleView extends RecyclerView {
    private Context mContext;
    private int mLastx = 0;
    private Scroller mScroller;

    public CustomRecycleView(Context context) {
        super(context);
        this.mContext = context;
        init(context);
    }

    public CustomRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mScroller = new Scroller(context);
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller != null && this.mScroller.computeScrollOffset()) {
            scrollBy(this.mLastx - this.mScroller.getCurrX(), 0);
            this.mLastx = this.mScroller.getCurrX();
            postInvalidate();
        }
    }

    public void autoAdjustScroll(int position) {
        int childCount = getChildCount();
        int firstvisiableposition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        int lastvisiableposition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        LogUtils.d("dzp", "childCount:" + childCount + "  firstvisiableposition" + firstvisiableposition + "   lastvisiableposition" + lastvisiableposition + "  position" + position);
        if (position - firstvisiableposition == childCount - 1) {
            this.mScroller.startScroll(this.mScroller.getFinalX(), this.mScroller.getFinalY(), -ScreenParams.getInstence(this.mContext).getResolutionValue(90), 0);
        }
        if (lastvisiableposition - position == childCount - 1) {
            this.mScroller.startScroll(this.mScroller.getFinalX(), this.mScroller.getFinalY(), ScreenParams.getInstence(this.mContext).getResolutionValue(90), 0);
        }
        invalidate();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        LogUtils.d("dzp","CustomRecycleView onKeyDown:" +keyCode);
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        LogUtils.d("dzp","CustomRecycleView dispatchKeyEvent:" +event.getKeyCode());
//        return super.dispatchKeyEvent(event);
//    }
}

package com.ktc.panasonichome.view.listview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.view.customview.SlideFocusView;
import com.ktc.panasonichome.view.listview.MetroAdapter.ObserverListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

public class MetroListView extends AdapterView<Adapter> implements ObserverListener, OnKeyListener, OnClickListener {
    private static final int LAYOUT_MODE_ABOVE = 1;
    private static final int LAYOUT_MODE_BELOW = 0;
    private static final int ON_SCROLL_END = 111;
    private int SCROLL_DURING = 300;
    Runnable changeTotalRun = new MyRunnable();
    private int colmusNum = 1;
    private int curSelectPosition;
    private int duration = 300;
    private boolean isOnKeyUp = false;
    private boolean isOnePage = false;
    private OnItemOnKeyListener itemOnKeyListener;
    private int key = 0;
    private Adapter mAdapter;
    private boolean mAttached = false;
    private OnScrollBarOnKeyListener mBarOnKeyListener;
    private final LinkedList<View> mCachedItemViews = new LinkedList();
    private int                    mFirstItemPosition;
    private MyHandler              mHandler;
    private OnItemClickListener    mItemClickListener;
    private int                    mLastItemPosition;
    private int                    mListTop;
    private int                    mListTopOffset;
    private int                    mListTopStart;
    private OnItemSelectedListener mListener;
    private RecycleBin             mRecycleBin;
    private SlideFocusView         mSlideFocusView;
    private long onKeyTime = 0;
    private boolean scrollBarHasFocus = false;
    OnKeyListener scrollBarKeyListener = new MyOnKeyListener();
    private MetroListViewScrollBar scrollBarView;
    private OnScrollStateListener scrollListener;
    private boolean scrollState = false;

    public interface OnItemSelectedListener {
        void onItemSelected(MetroListView metroListView, View view, int i);

        void onItemUnSelected(MetroListView metroListView, View view);
    }

    public interface OnScrollStateListener {
        void onScrollEnd(MetroListView metroListView, int i, int i2);
    }

    public interface OnItemOnKeyListener {
        boolean onBorderItemOnKeyEvent(View view, int i, int i2);

        boolean onItemOnKeyEvent(View view, int i, int i2);
    }

    public interface OnScrollBarOnKeyListener {
        boolean onScrollBarOnKey(View view, int i);
    }

    class MyRunnable implements Runnable {

        public void run() {
            final View view = MetroListView.this.findViewById(MetroListView.this.curSelectPosition);
            if (view != null && !view.hasFocus()) {
                view.postDelayed(new Runnable() {
                    public void run() {
                        if (!(MetroListView.this.scrollBarView == null || MetroListView.this.scrollBarView.hasFocused())) {
                            if (MetroListView.this.mSlideFocusView != null) {
                                MetroListView.this.mSlideFocusView.stopAnimationOnce();
                            }
                            view.requestFocus();
                        }
                        MetroListView.this.resetScrollBarPosition();
                    }
                }, 300);
            } else if (view == null && MetroListView.this.getChildAt(0) != null) {
                MetroListView.this.getChildAt(0).post(new Runnable() {
                    @Override
                    public void run() {
                        MetroListView.this.resetScrollBarPosition();
                    }
                });
            }
        }
    }

    class MyOnKeyListener implements OnKeyListener {

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            boolean z = false;
            if (event.getAction() == KeyEvent.ACTION_UP) {
                MetroListView.this.isOnKeyUp = true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (!MetroListView.this.scrollState) {
                    int firstPosition = MetroListView.this.getFirstPosition();
                    if (MetroListView.this.getChildAt(0) != null) {
                        int pageRow = MetroListView.this.getHeight() / MetroListView.this
                                .getChildAt(0).getHeight();
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_BACK:
                                MetroListView.this.scrollBarHasFocus = false;
                                if (MetroListView.this.mBarOnKeyListener != null) {
                                    z = MetroListView.this.mBarOnKeyListener.onScrollBarOnKey(v,
                                            keyCode);
                                }
                                return z;
                            case KeyEvent.KEYCODE_DPAD_UP:
                                MetroListView.this.isOnKeyUp = false;
                                if (MetroListView.this.mBarOnKeyListener != null) {
                                    MetroListView.this.mBarOnKeyListener.onScrollBarOnKey(v,
                                            keyCode);
                                }
                                if (firstPosition < MetroListView.this.colmusNum) {
                                    MetroListView.this.isOnKeyUp = true;
                                    return true;
                                }
                                if (firstPosition - (MetroListView.this.colmusNum * pageRow) < 0) {
                                    MetroListView.this.smoothScrollTo(0, MetroListView.this
                                            .getChildAt(0).getHeight() * (MetroListView.this
                                            .getFirstPosition() / MetroListView.this.colmusNum));
                                } else {
                                    MetroListView.this.smoothScrollTo(0, MetroListView.this
                                            .getChildAt(0).getHeight() * pageRow);
                                }
                                return true;
                            case KeyEvent.KEYCODE_DPAD_DOWN:
                                MetroListView.this.isOnKeyUp = false;
                                if (MetroListView.this.mBarOnKeyListener != null) {
                                    MetroListView.this.mBarOnKeyListener.onScrollBarOnKey(v,
                                            keyCode);
                                }
                                if (((MetroListView.this.colmusNum * pageRow) * 2) +
                                        firstPosition > MetroListView.this.mAdapter.getCount() -
                                        1) {
                                    MetroListView.this.isOnKeyUp = true;
                                    MetroListView.this.smoothScrollTo(0, (-((((((MetroListView
                                            .this.mAdapter.getCount() - 1) - ((MetroListView.this
                                            .mAdapter.getCount() - 1) % MetroListView.this
                                            .colmusNum)) - firstPosition) / MetroListView.this
                                            .colmusNum) - pageRow) + 1)) * MetroListView.this
                                            .getChildAt(0).getHeight());
                                } else {
                                    MetroListView.this.smoothScrollTo(0, (-pageRow) *
                                            MetroListView.this.getChildAt(0).getHeight());
                                }
                                return true;
                            case KeyEvent.KEYCODE_DPAD_LEFT:
                                int targetPosition = (MetroListView.this.getFirstPosition() +
                                        MetroListView.this.colmusNum) - 1;
                                if (targetPosition > MetroListView.this.mAdapter.getCount() - 1) {
                                    targetPosition = MetroListView.this.mAdapter.getCount() - 1;
                                }
                                View view = MetroListView.this.findViewById(targetPosition);
                                if (view != null) {
                                    MetroListView.this.scrollBarHasFocus = false;
                                    view.requestFocus();
                                    MetroListView.this.curSelectPosition = targetPosition;
                                    if (MetroListView.this.mListener != null) {
                                        MetroListView.this.mListener.onItemSelected(MetroListView
                                                .this, view, targetPosition);
                                    }
                                    if (MetroListView.this.mBarOnKeyListener != null) {
                                        MetroListView.this.mBarOnKeyListener.onScrollBarOnKey(v,
                                                keyCode);
                                    }
                                    return true;
                                }
                                if (MetroListView.this.mBarOnKeyListener != null) {
                                    z = MetroListView.this.mBarOnKeyListener.onScrollBarOnKey(v,
                                            keyCode);
                                }
                                return z;
                            case KeyEvent.KEYCODE_DPAD_RIGHT:
                                if (MetroListView.this.mBarOnKeyListener != null) {
                                    z = MetroListView.this.mBarOnKeyListener.onScrollBarOnKey(v,
                                            keyCode);
                                }
                                return z;
                            default:
                                if (MetroListView.this.mBarOnKeyListener != null) {
                                    return MetroListView.this.mBarOnKeyListener.onScrollBarOnKey
                                            (v, keyCode);
                                }
                                break;
                        }
                    }
                    return true;
                }
                return true;
            }
            return false;
        }
    }

    class C03233 implements Runnable {

        class C03221 implements Runnable {
            C03221() {
            }

            public void run() {
                if (MetroListView.this.scrollBarView != null && MetroListView.this.getChildAt(0) != null) {
                    int gridTotalHeight = (MetroListView.this.getCol(MetroListView.this.mAdapter.getCount()) * MetroListView.this.getChildAt(0).getHeight()) - (MetroListView.this.getChildAt(0).getHeight() * (MetroListView.this.getHeight() / MetroListView.this.getChildAt(0).getHeight()));
                    MetroListView.this.scrollBarView.setScrollBarToPosition(0);
                    if (gridTotalHeight > 0) {
                        if (MetroListView.this.scrollBarView.getVisibility() != View.VISIBLE) {
                            MetroListView.this.scrollBarView.setVisibility(View.VISIBLE);
                        }
                        MetroListView.this.isOnePage = false;
                    } else if (gridTotalHeight <= 0) {
                        if (MetroListView.this.scrollBarView.getVisibility() == View.VISIBLE) {
                            MetroListView.this.scrollBarView.setVisibility(View.GONE);
                        }
                        MetroListView.this.isOnePage = true;
                    }
                }
            }
        }

        C03233() {
        }

        public void run() {
            if (MetroListView.this.getChildAt(0) != null) {
                MetroListView.this.getChildAt(0).post(new C03221());
            }
        }
    }

    class C03265 implements Runnable {
        C03265() {
        }

        public void run() {
            if (MetroListView.this.scrollBarView != null) {
                MetroListView.this.scrollBarView.setVisibility(View.GONE);
            }
            if (MetroListView.this.getChildCount() >= 1) {
                MetroListView.this.removeViewInLayout(MetroListView.this.getChildAt(MetroListView.this.getChildCount() - 1));
            }
            MetroListView metroListView = MetroListView.this;
            metroListView.mLastItemPosition = metroListView.mLastItemPosition - 1;
            if (MetroListView.this.getChildCount() >= 1) {
                MetroListView.this.getChildAt(MetroListView.this.getChildCount() - 1).requestFocus();
            }
            MetroListView.this.resetScrollBarPosition();
            for (int j = 0; j < MetroListView.this.getChildCount(); j++) {
                ((MetroListItem) MetroListView.this.getChildAt(j)).refreshView();
            }
        }
    }

    class C03286 implements Runnable {

        class C03271 implements Runnable {
            C03271() {
            }

            public void run() {
                if (MetroListView.this.scrollBarView != null) {
                    int gridTotalHeight = (MetroListView.this.getCol(MetroListView.this.mAdapter.getCount()) * MetroListView.this.getChildAt(0).getHeight()) - (MetroListView.this.getChildAt(0).getHeight() * (MetroListView.this.getHeight() / MetroListView.this.getChildAt(0).getHeight()));
                    if (gridTotalHeight > 0) {
                        if (MetroListView.this.scrollBarView.getVisibility() != View.VISIBLE) {
                            MetroListView.this.scrollBarView.setVisibility(View.VISIBLE);
                        }
                        MetroListView.this.scrollBarView.setScrollBarToPosition(0);
                        MetroListView.this.isOnePage = false;
                    } else if (gridTotalHeight <= 0) {
                        if (MetroListView.this.scrollBarView.getVisibility() == View.VISIBLE) {
                            MetroListView.this.scrollBarView.setVisibility(View.GONE);
                        }
                        MetroListView.this.isOnePage = true;
                    }
                }
            }
        }

        C03286() {
        }

        public void run() {
            if (MetroListView.this.getChildAt(0) != null) {
                MetroListView.this.getChildAt(0).post(new C03271());
            }
        }
    }

    class C03308 implements AnimatorUpdateListener {
        C03308() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            float value = ((Float) animation.getAnimatedValue()).floatValue();
            MetroListView.this.mListTop = (int) (((float) MetroListView.this.mListTopStart) + value);
            MetroListView.this.setScrollBarPosition(-value);
            MetroListView.this.requestLayout();
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MetroListView> mLayout;

        public MyHandler(MetroListView layout) {
            this.mLayout = new WeakReference(layout);
        }

        public void handleMessage(Message msg) {
            MetroListView layout = (MetroListView) this.mLayout.get();
            if (layout != null) {
                int what = msg.what;
                View curFocusView = (View) msg.obj;
                if (what == 19) {
                    layout.onKeyUpFocused(curFocusView);
                } else if (what == 20) {
                    layout.onKeyDownFocused(curFocusView);
                } else if (what == MetroListView.ON_SCROLL_END) {
                    LogUtils.v("dzp", "isOnKeyUp == " + layout.isOnKeyUp);
                    if (layout.scrollListener != null && layout.isOnKeyUp) {
                        layout.scrollListener.onScrollEnd(layout, layout.mFirstItemPosition, layout.mLastItemPosition);
                    }
                    if (layout.isOnKeyUp) {
                        for (int i = 0; i < layout.getChildCount(); i++) {
                            ((MetroListItem) layout.getChildAt(i)).refreshView();
                        }
                    }
                }
            }
        }
    }

    public class RecycleBin {
        private LinkedList<View>[] mCachedItemViews;
        private LinkedList<View> mCurrentScrap;
        private int mViewTypeCount;

        public void setViewTypeCount(int viewTypeCount) {
            if (viewTypeCount < 1) {
                throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
            }
            this.mCachedItemViews = new LinkedList[viewTypeCount];
            for (int i = 0; i < viewTypeCount; i++) {
                this.mCachedItemViews[i] = new LinkedList();
            }
            this.mCurrentScrap = this.mCachedItemViews[0];
            this.mViewTypeCount = viewTypeCount;
        }

        public void clear() {
            if (this.mViewTypeCount == 1) {
                this.mCurrentScrap.clear();
                return;
            }
            int typeCount = this.mViewTypeCount;
            for (int i = 0; i < typeCount; i++) {
                this.mCachedItemViews[i].clear();
            }
        }

        public void addScrapView(Integer type, View view) {
            if (this.mViewTypeCount == 1) {
                this.mCurrentScrap.add(view);
            } else {
                this.mCachedItemViews[type.intValue()].add(view);
            }
        }

        public View getScrapView(int position) {
            if (this.mViewTypeCount != 1) {
                int whichScrap = MetroListView.this.mAdapter.getItemViewType(position);
                if (whichScrap >= 0 && whichScrap < this.mCachedItemViews.length && this.mCachedItemViews[whichScrap].size() > 0) {
                    return (View) this.mCachedItemViews[whichScrap].removeFirst();
                }
            } else if (this.mCurrentScrap.size() != 0) {
                return (View) this.mCurrentScrap.removeFirst();
            }
            return null;
        }
    }

    public MetroListView(Context context) {
        super(context);
        setFocusable(false);
        this.mHandler = new MyHandler(this);
        this.mRecycleBin = new RecycleBin();
    }

    public void setSlidFocusView(SlideFocusView focusView) {
        this.mSlideFocusView = focusView;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.mListener = listener;
    }

    public void setOnScrollStateListener(OnScrollStateListener listener) {
        this.scrollListener = listener;
    }

    public void setOnItemOnKeyListener(OnItemOnKeyListener listener) {
        this.itemOnKeyListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setOnScrollBarOnKeyListener(OnScrollBarOnKeyListener listener) {
        this.mBarOnKeyListener = listener;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttached = true;
    }

    protected void onDetachedFromWindow() {
        if (this.mAdapter != null) {
            ((MetroAdapter) this.mAdapter).registObserver(null);
        }
        this.mAttached = false;
        super.onDetachedFromWindow();
    }

    public Adapter getAdapter() {
        return this.mAdapter;
    }

    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
        ((MetroAdapter) this.mAdapter).registObserver(this);
        if (this.scrollBarView != null) {
            this.scrollBarView.setVisibility(View.GONE);
        }
        this.scrollBarHasFocus = false;
        removeAllViewsInLayout();
        this.mListTop = 0;
        this.mListTopOffset = 0;
        this.mListTopStart = 0;
        this.mFirstItemPosition = 0;
        this.mLastItemPosition = -1;
        this.mCachedItemViews.clear();
        this.mRecycleBin.clear();
        this.mRecycleBin.setViewTypeCount(this.mAdapter.getViewTypeCount());
        requestLayout();
        post(new C03233());
    }

    public void setAdapter(Adapter adapter, final int startPosition) {
        this.mAdapter = adapter;
        ((MetroAdapter) this.mAdapter).registObserver(this);
        if (this.scrollBarView != null) {
            this.scrollBarView.setVisibility(View.GONE);
        }
        this.scrollBarHasFocus = false;
        removeAllViewsInLayout();
        this.mListTop = 0;
        this.mListTopOffset = 0;
        this.mListTopStart = 0;
        int mode = startPosition % getColmusNum();
        this.mFirstItemPosition = startPosition - mode;
        this.mLastItemPosition = (startPosition - mode) - 1;
        this.mCachedItemViews.clear();
        this.mRecycleBin.clear();
        this.mRecycleBin.setViewTypeCount(this.mAdapter.getViewTypeCount());
        requestLayout();
        post(new Runnable() {
            public void run() {
                if (MetroListView.this.getChildAt(0) != null) {
                    View childAt = MetroListView.this.getChildAt(0);
                    final int i = startPosition;
                    childAt.post(new Runnable() {
                        public void run() {
                            if (MetroListView.this.scrollBarView != null && MetroListView.this.getChildAt(0) != null) {
                                int value = (i / MetroListView.this.getColmusNum()) * MetroListView.this.getChildAt(0).getHeight();
                                int scrollHeight = MetroListView.this.scrollBarView.getScrollBarHeight() - MetroListView.this.scrollBarView.getScrollBarViewHeight();
                                int gridTotalHeight = (MetroListView.this.getCol(MetroListView.this.mAdapter.getCount()) * MetroListView.this.getChildAt(0).getHeight()) - (MetroListView.this.getChildAt(0).getHeight() * (MetroListView.this.getHeight() / MetroListView.this.getChildAt(0).getHeight()));
                                if (gridTotalHeight > 0) {
                                    if (MetroListView.this.scrollBarView.getVisibility() != View.VISIBLE) {
                                        MetroListView.this.scrollBarView.setVisibility(View.VISIBLE);
                                    }
                                    MetroListView.this.scrollBarView.setScrollBarToPosition((int) ((((float) (value * scrollHeight)) / ((float) gridTotalHeight)) + 0.5f));
                                    MetroListView.this.isOnePage = false;
                                } else if (gridTotalHeight <= 0) {
                                    if (MetroListView.this.scrollBarView.getVisibility() == View.VISIBLE) {
                                        MetroListView.this.scrollBarView.setVisibility(View.GONE);
                                    }
                                    MetroListView.this.isOnePage = true;
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public void onChanaged() {
        if (this.mAdapter != null) {
            if (getChildAt(0) != null) {
                fillListDown(getChildAt(getChildCount() - 1).getTop(), (this.mListTop + this.mListTopOffset) - getChildAt(0).getTop());
                positioinItems();
                invalidate();
                int i = 0;
                while (i < getChildCount()) {
                    int position = getChildAt(i).getId();
                    if (position > this.mAdapter.getCount() - 1) {
                        LogUtils.v("dzp", "change  delete position ==   " + position);
                        boolean isPageFirst = position == getFirstPosition();
                        if (!(position != getChildAt(getChildCount() - 1).getId() || !getChildAt(i).hasFocus() || isPageFirst || getChildAt(0) == null || getChildAt((position - getChildAt(0).getId()) - 1) == null)) {
                            getChildAt((position - getChildAt(0).getId()) - 1).requestFocus();
                        }
                        if (isPageFirst) {
                            int scrollY;
                            int pageRow = getHeight() / getChildAt(0).getHeight();
                            this.mListTopStart = getChildAt(0).getTop() - this.mListTopOffset;
                            if (position - (this.colmusNum * pageRow) < 0) {
                                scrollY = (getFirstPosition() / this.colmusNum) * getChildAt(0).getHeight();
                            } else {
                                scrollY = pageRow * getChildAt(0).getHeight();
                            }
                            this.mListTop = this.mListTopStart + scrollY;
                            requestLayout();
                            post(new C03265());
                        } else {
                            removeViewInLayout(getChildAt(i));
                            this.mLastItemPosition--;
                            if (getChildCount() > 0) {
                                if ((getCol(this.mAdapter.getCount()) * getChildAt(0).getHeight()) - (getChildAt(0).getHeight() * (getHeight() / getChildAt(0).getHeight())) <= 0) {
                                    if (this.scrollBarView != null && this.scrollBarView.getVisibility() ==View.VISIBLE) {
                                        if (this.scrollBarView.hasFocused() && getChildCount() >= 1) {
                                            this.scrollBarHasFocus = false;
                                            getChildAt(getChildCount() - 1).requestFocus();
                                        }
                                        this.scrollBarView.setVisibility(View.GONE);
                                    }
                                    this.isOnePage = true;
                                }
                            } else {
                                if (this.scrollBarView != null && this.scrollBarView.getVisibility() ==View.VISIBLE) {
                                    if (this.scrollBarView.hasFocused() && getChildCount() >= 1) {
                                        this.scrollBarHasFocus = false;
                                        getChildAt(getChildCount() - 1).requestFocus();
                                    }
                                    this.scrollBarView.setVisibility(View.GONE);
                                }
                                this.isOnePage = true;
                            }
                        }
                        return;
                    }
                    LogUtils.v("dzp", "change  start position ==   " + position);
                    MetroAdapter.onChangeNeedClearFlag = false;
                    ((MetroListItem) this.mAdapter.getView(position, getChildAt(i), this)).refreshView();
                    MetroAdapter.onChangeNeedClearFlag = true;
                    i++;
                }
                resetScrollBarPosition();
            } else {
                this.mListTop = 0;
                this.mListTopOffset = 0;
                this.mListTopStart = 0;
                this.mFirstItemPosition = 0;
                this.mLastItemPosition = -1;
                requestLayout();
            }
        }
    }

    public void onChanagedTotal() {
        if (this.mAdapter != null) {
            if (!(this.mSlideFocusView == null || this.scrollBarView == null || this.scrollBarView.hasFocused())) {
                this.mSlideFocusView.getFocusView().setVisibility(View.INVISIBLE);
                this.scrollBarView.setVisibility(View.GONE);
                this.scrollBarView.getScrollBarView().setFocusable(false);
                requestFocus();
            }
            this.mListTop = 0;
            this.mListTopOffset = 0;
            this.mListTopStart = 0;
            this.mFirstItemPosition = getFirstPosition();
            this.mLastItemPosition = getFirstPosition() - 1;
            this.mCachedItemViews.clear();
            this.mRecycleBin.clear();
            removeAllViewsInLayout();
            requestLayout();
            if (this.mAttached) {
                post(this.changeTotalRun);
            } else {
                this.mHandler.post(this.changeTotalRun);
            }
        }
    }

    private void resetScrollBarPosition() {
        if (this.scrollBarHasFocus && this.scrollBarView != null) {
            this.scrollBarView.getScrollBarView().setFocusable(true);
        }
        if (!(this.mSlideFocusView == null || this.mSlideFocusView.getFocusView() == null || this
                .mSlideFocusView.getFocusView().getVisibility() == View.VISIBLE)) {
            this.mSlideFocusView.getFocusView().setVisibility(View.VISIBLE);
        }
        if (this.scrollBarView != null && getChildAt(0) != null) {
            int value           = (getFirstPosition() / getColmusNum()) * getChildAt(0).getHeight();
            int scrollHeight    = this.scrollBarView.getScrollBarHeight() - this.scrollBarView
                    .getScrollBarViewHeight();
            int gridTotalHeight = (getCol(this.mAdapter.getCount()) * getChildAt(0).getHeight()) 
                    - (getChildAt(0).getHeight() * (getHeight() / getChildAt(0).getHeight()));
            if (gridTotalHeight > 0) {
                if (this.scrollBarView.getVisibility() !=View.VISIBLE) {
                    this.scrollBarView.setVisibility(View.VISIBLE);
                }
                int scrollPostion = (int) ((((float) (value * scrollHeight)) / ((float) 
                        gridTotalHeight)) - 0.5f);
                if (this.scrollBarHasFocus) {
                    this.scrollBarView.setScrollBar_focusViewPosition(scrollPostion);
                } else {
                    this.scrollBarView.setScrollBarToPosition(scrollPostion);
                }
                this.isOnePage = false;
            } else if (gridTotalHeight <= 0) {
                if (this.scrollBarView.getVisibility() == View.VISIBLE) {
                    this.scrollBarView.setVisibility(View.GONE);
                }
                this.isOnePage = true;
            }
        }
    }

    public void onChanagedAll() {
        if (this.mAdapter != null) {
            this.mListTop = 0;
            this.mListTopOffset = 0;
            this.mListTopStart = 0;
            this.mFirstItemPosition = 0;
            this.mLastItemPosition = -1;
            this.mCachedItemViews.clear();
            if (this.scrollBarView != null) {
                this.scrollBarView.setVisibility(View.GONE);
            }
            this.mRecycleBin.clear();
            removeAllViewsInLayout();
            requestLayout();
            post(new C03286());
        }
    }

    public void setScrollDuration(int duration) {
        this.duration = duration;
        this.SCROLL_DURING = duration;
    }

    public void smoothScrollTo(int fx, int fy) {
        if (fy != 0) {
            this.mListTopStart = getChildAt(0).getTop() - this.mListTopOffset;
            smoothScrollBy(fx, fy);
        }
    }

    public int getFirstPosition() {
        return getFirstVisibleView() != null ? getFirstVisibleView().getId() : this.mFirstItemPosition;
    }

    public View getFirstVisibleView() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            if (view.getTop() == 0) {
                return view;
            }
        }
        return null;
    }

    public View getSelectedView() {
        return getFocusedChild();
    }

    public int getSelectedItemPosition() {
        return this.curSelectPosition;
    }

    public void setSelection(int position) {
        if (this.mAdapter == null || this.mAdapter.getCount() <= 0) {
            throw new UnsupportedOperationException("Adapter is null!!!");
        } else if (position > this.mAdapter.getCount() - 1 || position < 0) {
            throw new UnsupportedOperationException("outOfBounds  position = " + position);
        } else {
            int tempPostion = position - this.curSelectPosition;
            int moveCol = getCol(Math.abs(position - getFirstPosition()));
            View view = findViewById(position);
            int scrollSize;
            if (view != null) {
                scrollSize = 0;
                if (tempPostion > 0) {
                    if (view.getBottom() > getHeight()) {
                        if (getChildAt(0) != null) {
                            scrollSize = -getChildAt(0).getHeight();
                        }
                        this.key = 20;
                    } else {
                        if (!view.hasFocus()) {
                            view.requestFocus();
                        }
                        this.curSelectPosition = position;
                        if (this.mListener != null) {
                            this.mListener.onItemSelected(this, view, position);
                        }
                    }
                } else if (tempPostion >= 0) {
                    if (!view.hasFocus()) {
                        view.requestFocus();
                    }
                    this.curSelectPosition = position;
                    if (this.mListener != null) {
                        this.mListener.onItemSelected(this, view, position);
                    }
                    return;
                } else if (view.getTop() < 0) {
                    if (getChildAt(0) != null) {
                        scrollSize = getChildAt(0).getHeight();
                    }
                    this.key = 19;
                } else {
                    if (!view.hasFocus()) {
                        view.requestFocus();
                    }
                    this.curSelectPosition = position;
                    if (this.mListener != null) {
                        this.mListener.onItemSelected(this, view, position);
                    }
                }
                if (moveCol != 0) {
                    this.SCROLL_DURING = this.duration * moveCol;
                }
                this.curSelectPosition = position;
                if (this.mSlideFocusView != null) {
                    this.mSlideFocusView.stopAnimationOnce();
                }
                smoothScrollTo(0, scrollSize);
            } else {
                scrollSize = 0;
                if (tempPostion > 0) {
                    if (getChildAt(0) != null) {
                        scrollSize = (-getChildAt(0).getHeight()) * moveCol;
                    }
                    this.key = 20;
                } else if (tempPostion < 0) {
                    if (getChildAt(0) != null) {
                        scrollSize = getChildAt(0).getHeight() * moveCol;
                    }
                    this.key = 19;
                } else {
                    return;
                }
                if (moveCol != 0) {
                    this.SCROLL_DURING = this.duration * moveCol;
                }
                this.curSelectPosition = position;
                if (this.mSlideFocusView != null) {
                    this.mSlideFocusView.stopAnimationOnce();
                }
                smoothScrollTo(0, scrollSize);
            }
        }
    }

    public int getColmusNum() {
        return this.colmusNum;
    }

    public void setColmusNum(int colmusNum) {
        this.colmusNum = colmusNum;
    }

    public void setScrollBarView(MetroListViewScrollBar scrollBarView) {
        this.scrollBarView = scrollBarView;
        scrollBarView.setVisibility(View.GONE);
        if (scrollBarView.getScrollBarFocusBle()) {
            scrollBarView.getScrollBarView().setOnKeyListener(this.scrollBarKeyListener);
        }
    }

    public boolean isLastCol(int position) {
        if (getCol(position + 1) == getCol(getAdapter().getCount())) {
            return true;
        }
        return false;
    }

    public int getCol(int pos) {
        int temp = pos / getColmusNum();
        if (pos % getColmusNum() != 0) {
            return temp + 1;
        }
        return temp;
    }

    private void smoothScrollBy(int dx, int dy) {
        final ValueAnimator transAnim = ValueAnimator.ofFloat(new float[]{0.0f, (float) dy});
        transAnim.setInterpolator(new DecelerateInterpolator());
        transAnim.setDuration((long) this.SCROLL_DURING);
        transAnim.start();
        this.scrollState = true;
        transAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (MetroListView.this.mHandler.hasMessages(MetroListView.ON_SCROLL_END)) {
                    MetroListView.this.mHandler.removeMessages(MetroListView.ON_SCROLL_END);
                }
                MetroListView.this.mHandler.sendEmptyMessageDelayed(MetroListView.ON_SCROLL_END, 100);
                View curFocusView = MetroListView.this.getFocusedChild();
                if (MetroListView.this.mHandler.hasMessages(MetroListView.this.key)) {
                    MetroListView.this.mHandler.removeMessages(MetroListView.this.key);
                }
                Message msg = MetroListView.this.mHandler.obtainMessage(MetroListView.this.key);
                msg.obj = curFocusView;
                MetroListView.this.mHandler.sendMessage(msg);
                if (MetroListView.this.scrollBarView != null) {
                    MetroListView.this.scrollBarView.setDefaultLastPosition();
                }
                MetroListView.this.scrollState = false;
                transAnim.cancel();
                transAnim.removeAllListeners();
            }
        });
        transAnim.addUpdateListener(new C03308());
    }

    private void setScrollBarPosition(float value) {
        if (!(this.scrollBarView == null || getChildAt(0) == null)) {
            int scrollHeight = this.scrollBarView.getScrollBarHeight() - this.scrollBarView
                    .getScrollBarViewHeight();
            if (getChildAt(0) != null) {
                this.scrollBarView.setScrollBarPosition((int) ((((float) scrollHeight) * value) /
                        ((float) ((getCol(this.mAdapter.getCount()) * getChildAt(0).getHeight())
                                - (getChildAt(0).getHeight() * (getHeight() / getChildAt(0)
                                .getHeight()))))));
            }
        }
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mAdapter != null) {
            if (getChildCount() == 0) {
                fillListDown(this.mListTop, 0);
                if (getChildCount() != 0) {
                    if (getHeight() / getChildAt(0).getMeasuredHeight() >= 4) {
                        this.duration = 150;
                        this.SCROLL_DURING = this.duration;
                    }
                    for (int i = 0; i < getChildCount(); i++) {
                        ((MetroListItem) getChildAt(i)).refreshView();
                    }
                }
            } else {
                int offset = (this.mListTop + this.mListTopOffset) - getChildAt(0).getTop();
                removeNonVisibleViews(offset);
                fillList(offset);
            }
            positioinItems();
            invalidate();
        }
    }

    private void fillList(int offset) {
        fillListDown(getChildAt(getChildCount() - 1).getBottom(), offset);
        fillListUp(getChildAt(0).getTop(), offset);
    }

    private void fillListUp(int topEdge, int offset) {
        int tempColmus = 1;
        while (topEdge + offset > 0 && this.mFirstItemPosition > 0) {
            this.mFirstItemPosition--;
            View newTopChild = this.mAdapter.getView(this.mFirstItemPosition, getCachedView(this.mFirstItemPosition), this);
            newTopChild.setOnKeyListener(this);
            newTopChild.setOnClickListener(this);
            newTopChild.setId(this.mFirstItemPosition);
            addAndMeasureChild(newTopChild, 1);
            int childHeight = newTopChild.getMeasuredHeight();
            if (tempColmus == this.colmusNum) {
                topEdge -= childHeight;
                tempColmus = 0;
                this.mListTopOffset -= childHeight;
            }
            tempColmus++;
        }
    }

    private void fillListDown(int bottomEdge, int offset) {
        int tempColmus = 1;
        while (bottomEdge + offset < getHeight() && this.mLastItemPosition < this.mAdapter.getCount() - 1) {
            this.mLastItemPosition++;
            View newBottomChild = this.mAdapter.getView(this.mLastItemPosition, getCachedView(this.mLastItemPosition), this);
            newBottomChild.setOnKeyListener(this);
            newBottomChild.setOnClickListener(this);
            newBottomChild.setId(this.mLastItemPosition);
            addAndMeasureChild(newBottomChild, 0);
            if (tempColmus == this.colmusNum) {
                bottomEdge += newBottomChild.getMeasuredHeight();
                tempColmus = 0;
            }
            tempColmus++;
        }
    }

    private void addAndMeasureChild(View child, int layoutMode) {
        LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        }
        addViewInLayout(child, layoutMode == 1 ? 0 : -1, params, true);
        child.measure(0, 0);
    }

    private void positioinItems() {
        int top = this.mListTop + this.mListTopOffset;
        int left = 0;
        int tempColmus = 1;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(left, top, left + width, top + height);
            left += width;
            if (tempColmus == this.colmusNum) {
                top += height;
                left = 0;
                tempColmus = 0;
            }
            tempColmus++;
        }
    }

    private void setScrollBarVisibleState(final boolean show) {
        if (!this.isOnePage) {
            AlphaAnimation alphaAnimation;
            if (show) {
                alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation.setDuration(100);
                alphaAnimation.setFillAfter(true);
            } else {
                alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(100);
                alphaAnimation.setFillAfter(true);
            }
            alphaAnimation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (show) {
                        MetroListView.this.scrollBarView.setVisibility(View.VISIBLE);
                    } else {
                        MetroListView.this.scrollBarView.setVisibility(View.INVISIBLE);
                    }
                    MetroListView.this.scrollBarView.clearAnimation();
                }
            });
            this.scrollBarView.startAnimation(alphaAnimation);
        }
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        int action = event.getAction();
        if (action == 1) {
            this.isOnKeyUp = true;
            if (this.scrollState) {
                return true;
            }
        }
        if (action == 0) {
            LogUtils.v("dzp", "-----KEYCODE_DPAD_RIGHT-----" + keyCode);
            if (this.scrollState) {
                return true;
            }
            this.key = keyCode;
            int curPosition = v.getId();
            int focusId = getFocusedChild().getId();
            View nextFocusView;
            switch (keyCode) {
                case 4:
                    this.scrollBarHasFocus = false;
                    if (this.itemOnKeyListener != null) {
                        return this.itemOnKeyListener.onItemOnKeyEvent(v, keyCode, curPosition);
                    }
                    break;
                case 19:
                    this.isOnKeyUp = false;
                    if (System.currentTimeMillis() - this.onKeyTime >= 110) {
                        this.SCROLL_DURING = this.duration;
                    } else if (this.duration > 200) {
                        this.SCROLL_DURING = this.duration / 2;
                    } else {
                        this.SCROLL_DURING = this.duration;
                    }
                    this.onKeyTime = System.currentTimeMillis();
                    if (v.getTop() == 0 && curPosition >= this.colmusNum) {
                        if (this.mSlideFocusView != null) {
                            this.mSlideFocusView.stopAnimationOnce();
                        }
                        smoothScrollTo(0, getChildAt(0).getHeight());
                        if (getCol(curPosition + 1) == 1) {
                            this.isOnKeyUp = true;
                        }
                    } else if (!onKeyUpFocused(v)) {
                        this.isOnKeyUp = true;
                        if (this.itemOnKeyListener != null) {
                            return this.itemOnKeyListener.onBorderItemOnKeyEvent(v, keyCode, curPosition);
                        }
                    }
                    return true;
                case 20:
                    this.isOnKeyUp = false;
                    if (System.currentTimeMillis() - this.onKeyTime >= 110) {
                        this.SCROLL_DURING = this.duration;
                    } else if (this.duration > 200) {
                        this.SCROLL_DURING = this.duration / 2;
                    } else {
                        this.SCROLL_DURING = this.duration;
                    }
                    this.onKeyTime = System.currentTimeMillis();
                    if ((v.getHeight() + v.getBottom()) - (v.getPaddingTop() * 2) > getHeight() && !isLastCol(curPosition)) {
                        if (this.mSlideFocusView != null) {
                            this.mSlideFocusView.stopAnimationOnce();
                        }
                        smoothScrollTo(0, -getChildAt(0).getHeight());
                        if (getCol(curPosition + 1) == getCol(getAdapter().getCount()) - 1) {
                            this.isOnKeyUp = true;
                        }
                    } else if (isLastCol(curPosition)) {
                        this.isOnKeyUp = true;
                        return true;
                    } else {
                        onKeyDownFocused(v);
                    }
                    return true;
                case 21:
                    if (focusId >= 0) {
                        nextFocusView = findViewById(focusId - 1);
                        if (getColmusNum() == 1) {
                            if (this.itemOnKeyListener != null) {
                                return this.itemOnKeyListener.onBorderItemOnKeyEvent(v, keyCode, curPosition);
                            }
                        } else if (focusId % getColmusNum() == 0 && this.itemOnKeyListener != null) {
                            return this.itemOnKeyListener.onBorderItemOnKeyEvent(v, keyCode, curPosition);
                        }
                        if (!(this.mListener == null || nextFocusView == null)) {
                            this.curSelectPosition = nextFocusView.getId();
                            this.mListener.onItemSelected(this, nextFocusView, nextFocusView.getId());
                            this.mListener.onItemUnSelected(this, v);
                            break;
                        }
                    }
                    break;
                case 22:
                    LogUtils.v("dzp", "-----KEYCODE_DPAD_RIGHT-----");
                    if (focusId <= this.mAdapter.getCount() - 1) {
                        if (getColmusNum() == 1) {
                            if (this.scrollBarView != null && this.scrollBarView.getScrollBarFocusBle() && this.scrollBarView.getVisibility() ==View.VISIBLE) {
                                LogUtils.v("dzp", "-----------scrollBarView requestFocus-------------");
                                this.scrollBarHasFocus = true;
                                this.scrollBarView.getScrollBarView().requestFocus();
                            }
                            LogUtils.v("dzp", "-----------itemOnKeyListener onBorderItemOnKeyEvent-------------" + this.itemOnKeyListener);
                            if (this.itemOnKeyListener != null) {
                                return this.itemOnKeyListener.onBorderItemOnKeyEvent(v, keyCode, curPosition);
                            }
                        } else if ((focusId + 1) % getColmusNum() == 0 || focusId == this.mAdapter.getCount() - 1) {
                            if (this.scrollBarView != null && this.scrollBarView.getScrollBarFocusBle() && this.scrollBarView.getVisibility() ==View.VISIBLE) {
                                this.scrollBarHasFocus = true;
                                this.scrollBarView.getScrollBarView().requestFocus();
                            }
                            LogUtils.v("dzp", "-----------itemOnKeyListener onBorderItemOnKeyEvent-------------" + this.itemOnKeyListener);
                            if (this.itemOnKeyListener != null) {
                                return this.itemOnKeyListener.onBorderItemOnKeyEvent(v, keyCode, curPosition);
                            }
                        }
                        nextFocusView = findViewById(focusId + 1);
                        if (!(this.mListener == null || nextFocusView == null)) {
                            this.curSelectPosition = nextFocusView.getId();
                            this.mListener.onItemSelected(this, nextFocusView, nextFocusView.getId());
                            this.mListener.onItemUnSelected(this, v);
                            break;
                        }
                    }
                    return true;
            }
            if (this.itemOnKeyListener != null) {
                return this.itemOnKeyListener.onItemOnKeyEvent(v, keyCode, curPosition);
            }
        }
        return false;
    }

    private boolean onKeyUpFocused(View curFocusView) {
        if (curFocusView == null) {
            return false;
        }
        int nextFocusId = curFocusView.getId() - this.colmusNum;
        View nextFocusView;
        if (nextFocusId >= 0) {
            int i = 0;
            while (i < getChildCount()) {
                if (getChildAt(i) == null || getChildAt(i).getId() != nextFocusId) {
                    i++;
                } else {
                    nextFocusView = getChildAt(i);
                    nextFocusView.requestFocus();
                    if (this.mListener != null) {
                        this.curSelectPosition = nextFocusId;
                        this.mListener.onItemSelected(this, nextFocusView, nextFocusId);
                        this.mListener.onItemUnSelected(this, curFocusView);
                    }
                    return true;
                }
            }
            return true;
        } else if (nextFocusId <= this.colmusNum) {
            return false;
        } else {
            nextFocusView = getChildAt(0);
            nextFocusView.requestFocus();
            this.curSelectPosition = 0;
            if (this.mListener != null) {
                this.mListener.onItemSelected(this, nextFocusView, 0);
            }
            return true;
        }
    }

    private void onKeyDownFocused(View curFocusView) {
        if (curFocusView != null) {
            int nextFocusId = curFocusView.getId() + this.colmusNum;
            if (nextFocusId > this.mAdapter.getCount() - 1) {
                nextFocusId = this.mAdapter.getCount() - 1;
            }
            int i = 0;
            while (i < getChildCount()) {
                if (getChildAt(i) == null || getChildAt(i).getId() != nextFocusId) {
                    i++;
                } else {
                    View nextFocusView = getChildAt(i);
                    nextFocusView.requestFocus();
                    if (this.mListener != null) {
                        this.curSelectPosition = nextFocusId;
                        this.mListener.onItemSelected(this, nextFocusView, nextFocusId);
                        this.mListener.onItemUnSelected(this, curFocusView);
                    }
                    //dzp add 3-7 一行代码值千金
                    return;
                    //dzp add 3-7 一行代码值千金
                }
            }
        }
    }

    private void removeNonVisibleViews(int offset) {
        int childCount = getChildCount();
        int tempColums = 1;
        if (this.mLastItemPosition != this.mAdapter.getCount() - 1 && childCount > 1) {
            View childAt = getChildAt(0);
            while (childAt != null && childAt.getBottom() + offset < 0) {
                removeViewInLayout(childAt);
                childAt.setOnKeyListener(null);
                childAt.setOnClickListener(null);
                childCount--;
                this.mCachedItemViews.addLast(childAt);
                this.mRecycleBin.addScrapView(Integer.valueOf(((MetroListItem) childAt).mViewType), childAt);
                this.mFirstItemPosition++;
                if (tempColums == this.colmusNum) {
                    tempColums = 0;
                    this.mListTopOffset += childAt.getMeasuredHeight();
                }
                tempColums++;
                if (childCount > 1) {
                    childAt = getChildAt(0);
                } else {
                    childAt = null;
                }
            }
        }
        if (this.mFirstItemPosition != 0 && childCount > 1) {
            View childAt2 = getChildAt(childCount - 1);
            while (childAt2 != null && childAt2.getTop() + offset > getHeight()) {
                removeViewInLayout(childAt2);
                childAt2.setOnKeyListener(null);
                childAt2.setOnClickListener(null);
                childCount--;
                this.mCachedItemViews.addLast(childAt2);
                this.mRecycleBin.addScrapView(Integer.valueOf(((MetroListItem) childAt2).mViewType), childAt2);
                this.mLastItemPosition--;
                if (childCount > 1) {
                    childAt2 = getChildAt(childCount - 1);
                } else {
                    childAt2 = null;
                }
            }
        }
    }

    private View getCachedView(int position) {
        return this.mRecycleBin.getScrapView(position);
    }

    public void onClick(View v) {
        if (this.mItemClickListener != null) {
            this.mItemClickListener.onItemClick(this, v, v.getId(), (long) v.getId());
        }
    }
}

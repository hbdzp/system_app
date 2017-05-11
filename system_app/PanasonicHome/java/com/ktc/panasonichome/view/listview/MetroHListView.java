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
import android.view.animation.DecelerateInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;

import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.view.customview.SlideFocusView;
import com.ktc.panasonichome.view.listview.MetroAdapter.ObserverListener;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

public class MetroHListView extends AdapterView<Adapter> implements ObserverListener, OnKeyListener, OnClickListener {
    private static final int LAYOUT_MODE_ABOVE = 1;
    private static final int LAYOUT_MODE_BELOW = 0;
    private static final int ON_SCROLL_END = 111;
    private static final int SCROLL_STATE = 110;
    private int SCROLL_DURING = 100;
    private int curSelectPosition;
    private int duration = 100;
    private boolean isOnKeyUp = false;
    private OnHItemOnKeyListener itemOnKeyListener;
    private int key = 0;
    private Adapter mAdapter;
    private boolean mAttached = false;
    private final LinkedList<View> mCachedItemViews = new LinkedList();
    private int                     mFirstItemPosition;
    private MyHandler               mHandler;
    private OnItemClickListener     mItemClickListener;
    private int                     mLastItemPosition;
    private int                     mListLeft;
    private int                     mListLeftOffset;
    private int                     mListLeftStart;
    private OnHItemSelectedListener mListener;
    private RecycleBin              mRecycleBin;
    private SlideFocusView          mSlideFocusView;
    private long onKeyTime = 0;
    private OnHScrollStateListener scrollListener;
    private boolean scrollState = false;

    class C03172 implements AnimatorUpdateListener {
        C03172() {
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            MetroHListView.this.mListLeft = (int) (((float) MetroHListView.this.mListLeftStart) + ((Float) animation.getAnimatedValue()).floatValue());
            MetroHListView.this.requestLayout();
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<MetroHListView> mLayout;

        public MyHandler(MetroHListView layout) {
            this.mLayout = new WeakReference(layout);
        }

        public void handleMessage(Message msg) {
            MetroHListView layout = (MetroHListView) this.mLayout.get();
            if (layout != null) {
                int what = msg.what;
                View curFocusView = (View) msg.obj;
                if (what == 21) {
                    layout.onKeyLeftFocused(curFocusView);
                } else if (what == 22) {
                    layout.onKeyRightFocused(curFocusView);
                } else if (what != MetroHListView.SCROLL_STATE && what == MetroHListView.ON_SCROLL_END) {
                    LogUtils.v("dzp", "isOnKeyUp == " + layout.isOnKeyUp);
                    if (layout.scrollListener != null && layout.isOnKeyUp) {
                        layout.scrollListener.onHScrollEnd(layout, layout.mFirstItemPosition, layout.mLastItemPosition);
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

    public interface OnHItemOnKeyListener {
        boolean onHBorderItemOnKeyEvent(View view, int i, int i2);

        boolean onHItemOnKeyEvent(View view, int i, int i2);
    }

    public interface OnHItemSelectedListener {
        void onHItemSelected(MetroHListView metroHListView, View view, int i);

        void onHItemUnSelected(MetroHListView metroHListView, View view);
    }

    public interface OnHScrollStateListener {
        void onHScrollEnd(MetroHListView metroHListView, int i, int i2);
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
                int whichScrap = MetroHListView.this.mAdapter.getItemViewType(position);
                if (whichScrap >= 0 && whichScrap < this.mCachedItemViews.length && this.mCachedItemViews[whichScrap].size() > 0) {
                    return (View) this.mCachedItemViews[whichScrap].removeFirst();
                }
            } else if (this.mCurrentScrap.size() != 0) {
                return (View) this.mCurrentScrap.removeFirst();
            }
            return null;
        }
    }

    public MetroHListView(Context context) {
        super(context);
        setFocusable(false);
        this.mHandler = new MyHandler(this);
        this.mRecycleBin = new RecycleBin();
    }

    public void setSlidFocusView(SlideFocusView focusView) {
        this.mSlideFocusView = focusView;
    }

    public void setOnHItemSelectedListener(OnHItemSelectedListener listener) {
        this.mListener = listener;
    }

    public void setOnHScrollStateListener(OnHScrollStateListener listener) {
        this.scrollListener = listener;
    }

    public void setOnHItemOnKeyListener(OnHItemOnKeyListener listener) {
        this.itemOnKeyListener = listener;
    }

    public void setOnHItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
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
        removeAllViewsInLayout();
        this.mListLeft = 0;
        this.mListLeftOffset = 0;
        this.mListLeftStart = 0;
        this.mFirstItemPosition = 0;
        this.mLastItemPosition = -1;
        this.mCachedItemViews.clear();
        this.mRecycleBin.clear();
        this.mRecycleBin.setViewTypeCount(this.mAdapter.getViewTypeCount());
        requestLayout();
    }

    public void setAdapter(Adapter adapter, int startPosition) {
        this.mAdapter = adapter;
        ((MetroAdapter) this.mAdapter).registObserver(this);
        removeAllViewsInLayout();
        this.mListLeft = 0;
        this.mListLeftOffset = 0;
        this.mListLeftStart = 0;
        this.mFirstItemPosition = startPosition;
        this.mLastItemPosition = startPosition - 1;
        this.mCachedItemViews.clear();
        this.mRecycleBin.clear();
        this.mRecycleBin.setViewTypeCount(this.mAdapter.getViewTypeCount());
        requestLayout();
    }

    public void onChanaged() {
        if (this.mAdapter != null) {
            for (int i = 0; i < getChildCount(); i++) {
                this.mAdapter.getView(getChildAt(i).getId(), getChildAt(i), this);
                ((MetroListItem) getChildAt(i)).refreshView();
            }
            invalidate();
        }
    }

    public void onChanagedTotal() {
        if (this.mAdapter != null) {
            this.mListLeft = 0;
            this.mListLeftOffset = 0;
            this.mListLeftStart = 0;
            this.mFirstItemPosition = getFirstPosition();
            this.mLastItemPosition = getFirstPosition() - 1;
            this.mCachedItemViews.clear();
            this.mRecycleBin.clear();
            removeAllViewsInLayout();
            requestLayout();
        }
    }

    public void onChanagedAll() {
        if (this.mAdapter != null) {
            this.mListLeft = 0;
            this.mListLeftOffset = 0;
            this.mListLeftStart = 0;
            this.mFirstItemPosition = 0;
            this.mLastItemPosition = -1;
            this.mCachedItemViews.clear();
            this.mRecycleBin.clear();
            removeAllViewsInLayout();
            requestLayout();
        }
    }

    public void setScrollDuration(int duration) {
        this.duration = duration;
        this.SCROLL_DURING = duration;
    }

    public void smoothScrollTo(int fx, int fy) {
        if (fx != 0) {
            this.mListLeftStart = getChildAt(0).getLeft() - this.mListLeftOffset;
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
            if (view.getLeft() == 0) {
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
        if (position > this.mAdapter.getCount() - 1 || position < 0) {
            throw new UnsupportedOperationException("outOfBounds  position = " + position);
        }
        int tempPostion = position - this.curSelectPosition;
        int moveCol = Math.abs(position - getFirstPosition());
        View view = findViewById(position);
        int scrollSize;
        if (view != null) {
            scrollSize = 0;
            if (tempPostion > 0) {
                if (view.getBottom() > getHeight()) {
                    scrollSize = -getChildAt(0).getWidth();
                    this.key = 20;
                } else {
                    if (!view.hasFocus()) {
                        view.requestFocus();
                    }
                    this.curSelectPosition = position;
                    if (this.mListener != null) {
                        this.mListener.onHItemSelected(this, view, position);
                    }
                }
            } else if (tempPostion >= 0) {
                if (!view.hasFocus()) {
                    view.requestFocus();
                }
                this.curSelectPosition = position;
                if (this.mListener != null) {
                    this.mListener.onHItemSelected(this, view, position);
                }
                return;
            } else if (view.getLeft() < 0) {
                scrollSize = getChildAt(0).getWidth();
                this.key = 19;
            } else {
                if (!view.hasFocus()) {
                    view.requestFocus();
                }
                this.curSelectPosition = position;
                if (this.mListener != null) {
                    this.mListener.onHItemSelected(this, view, position);
                }
            }
            if (moveCol != 0) {
                this.SCROLL_DURING = this.duration * moveCol;
            }
            this.curSelectPosition = position;
            if (this.mSlideFocusView != null) {
                this.mSlideFocusView.stopAnimationOnce();
            }
            smoothScrollTo(scrollSize, 0);
        } else {
            if (tempPostion > 0) {
                scrollSize = (-getChildAt(0).getWidth()) * moveCol;
                this.key = 20;
            } else if (tempPostion < 0) {
                scrollSize = getChildAt(0).getWidth() * moveCol;
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
            smoothScrollTo(scrollSize, 0);
        }
    }

    public boolean isLastCol(int position) {
        if (position == getAdapter().getCount() - 1) {
            return true;
        }
        return false;
    }

    private void smoothScrollBy(int dx, int dy) {
        final ValueAnimator transAnim = ValueAnimator.ofFloat(new float[]{0.0f, (float) dx});
        transAnim.setInterpolator(new DecelerateInterpolator());
        transAnim.setDuration((long) this.SCROLL_DURING);
        transAnim.start();
        this.scrollState = true;
        transAnim.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (MetroHListView.this.mHandler.hasMessages(MetroHListView.ON_SCROLL_END)) {
                    MetroHListView.this.mHandler.removeMessages(MetroHListView.ON_SCROLL_END);
                }
                MetroHListView.this.mHandler.sendEmptyMessageDelayed(MetroHListView.ON_SCROLL_END, 100);
                View curFocusView = MetroHListView.this.getFocusedChild();
                if (MetroHListView.this.mHandler.hasMessages(MetroHListView.this.key)) {
                    MetroHListView.this.mHandler.removeMessages(MetroHListView.this.key);
                }
                Message msg = MetroHListView.this.mHandler.obtainMessage(MetroHListView.this.key);
                msg.obj = curFocusView;
                MetroHListView.this.mHandler.sendMessage(msg);
                MetroHListView.this.scrollState = false;
                transAnim.cancel();
                transAnim.removeAllListeners();
            }
        });
        transAnim.addUpdateListener(new C03172());
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mAdapter != null) {
            if (getChildCount() == 0) {
                fillListRight(this.mListLeft, 0);
                if (getChildCount() != 0) {
                    if (getWidth() / getChildAt(0).getMeasuredWidth() >= 4) {
                        this.duration = 100;
                        this.SCROLL_DURING = this.duration;
                    }
                    for (int i = 0; i < getChildCount(); i++) {
                        ((MetroListItem) getChildAt(i)).refreshView();
                    }
                }
            } else {
                int offset = (this.mListLeft + this.mListLeftOffset) - getChildAt(0).getLeft();
                removeNonVisibleViews(offset);
                fillList(offset);
            }
            positioinItems();
            invalidate();
        }
    }

    private void fillList(int offset) {
        fillListRight(getChildAt(getChildCount() - 1).getRight(), offset);
        fillListLeft(getChildAt(0).getLeft(), offset);
    }

    private void fillListLeft(int leftEdge, int offset) {
        while (leftEdge + offset > 0 && this.mFirstItemPosition > 0) {
            this.mFirstItemPosition--;
            View newTopChild = this.mAdapter.getView(this.mFirstItemPosition, getCachedView(this.mFirstItemPosition), this);
            newTopChild.setOnKeyListener(this);
            newTopChild.setOnClickListener(this);
            newTopChild.setId(this.mFirstItemPosition);
            addAndMeasureChild(newTopChild, 1);
            int childWidth = newTopChild.getMeasuredWidth();
            leftEdge -= childWidth;
            this.mListLeftOffset -= childWidth;
        }
    }

    private void fillListRight(int rightEdge, int offset) {
        while (rightEdge + offset < getWidth() && this.mLastItemPosition < this.mAdapter.getCount() - 1) {
            this.mLastItemPosition++;
            View newBottomChild = this.mAdapter.getView(this.mLastItemPosition, getCachedView(this.mLastItemPosition), this);
            newBottomChild.setOnKeyListener(this);
            newBottomChild.setOnClickListener(this);
            newBottomChild.setId(this.mLastItemPosition);
            addAndMeasureChild(newBottomChild, 0);
            rightEdge += newBottomChild.getMeasuredWidth();
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
        int left = this.mListLeft + this.mListLeftOffset;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            child.layout(left, 0, left + width, child.getMeasuredHeight() + 0);
            left += width;
        }
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        int action = event.getAction();
        if (action == 1) {
            this.isOnKeyUp = true;
        }
        if (action == 0) {
            if (!this.scrollState) {
                this.key = keyCode;
                int curPosition = v.getId();
                int focusId = getFocusedChild().getId();
                switch (keyCode) {
                    case 19:
                        if (this.itemOnKeyListener != null) {
                            return this.itemOnKeyListener.onHBorderItemOnKeyEvent(v, keyCode, curPosition);
                        }
                        return false;
                    case 20:
                        if (this.itemOnKeyListener != null) {
                            return this.itemOnKeyListener.onHBorderItemOnKeyEvent(v, keyCode, curPosition);
                        }
                        return false;
                    case 21:
                        this.isOnKeyUp = false;
                        if (System.currentTimeMillis() - this.onKeyTime < 110) {
                            this.SCROLL_DURING = this.duration;
                        } else {
                            this.SCROLL_DURING = this.duration;
                        }
                        this.onKeyTime = System.currentTimeMillis();
                        if (v.getLeft() != 0 || curPosition <= 0) {
                            onKeyLeftFocused(v);
                            if (curPosition == 0) {
                                this.isOnKeyUp = true;
                                if (this.itemOnKeyListener != null) {
                                    return this.itemOnKeyListener.onHBorderItemOnKeyEvent(v, keyCode, curPosition);
                                }
                            }
                        }
                        if (this.mSlideFocusView != null) {
                            this.mSlideFocusView.stopAnimationOnce();
                        }
                        smoothScrollTo(getChildAt(0).getWidth(), 0);
                        if (curPosition == 1) {
                            this.isOnKeyUp = true;
                        }
                        return true;
                    case 22:
                        this.isOnKeyUp = false;
                        if (System.currentTimeMillis() - this.onKeyTime < 110) {
                            this.SCROLL_DURING = this.duration;
                        } else {
                            this.SCROLL_DURING = this.duration;
                        }
                        this.onKeyTime = System.currentTimeMillis();
                        if ((v.getWidth() + v.getRight()) - (v.getPaddingLeft() * 2) > getWidth() && !isLastCol(curPosition)) {
                            if (this.mSlideFocusView != null) {
                                this.mSlideFocusView.stopAnimationOnce();
                            }
                            smoothScrollTo(-getChildAt(0).getWidth(), 0);
                            if (curPosition == this.mAdapter.getCount() - 2) {
                                this.isOnKeyUp = true;
                            }
                        } else if (isLastCol(curPosition)) {
                            this.isOnKeyUp = true;
                            return true;
                        } else {
                            onKeyRightFocused(v);
                        }
                        return true;
                    default:
                        if (this.itemOnKeyListener != null) {
                            return this.itemOnKeyListener.onHItemOnKeyEvent(v, keyCode, curPosition);
                        }
                        break;
                }
            }
            return true;
        }
        return false;
    }

    private boolean onKeyLeftFocused(View curFocusView) {
        if (curFocusView == null) {
            return false;
        }
        int nextFocusId = curFocusView.getId() - 1;
        if (nextFocusId < 0) {
            return false;
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
                    this.mListener.onHItemSelected(this, nextFocusView, nextFocusId);
                    this.mListener.onHItemUnSelected(this, curFocusView);
                }
                return true;
            }
        }
        return true;
    }

    private void onKeyRightFocused(View curFocusView) {
        if (curFocusView != null) {
            int nextFocusId = curFocusView.getId() + 1;
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
                        this.mListener.onHItemSelected(this, nextFocusView, nextFocusId);
                        this.mListener.onHItemUnSelected(this, curFocusView);
                    }
                }
            }
        }
    }

    private void removeNonVisibleViews(int offset) {
        int childCount = getChildCount();
        if (this.mLastItemPosition != this.mAdapter.getCount() - 1 && childCount > 1) {
            View childAt = getChildAt(0);
            while (childAt != null && childAt.getRight() + offset < 0) {
                removeViewInLayout(childAt);
                childAt.setOnKeyListener(null);
                childAt.setOnClickListener(null);
                childCount--;
                this.mCachedItemViews.addLast(childAt);
                this.mRecycleBin.addScrapView(Integer.valueOf(((MetroListItem) childAt).mViewType), childAt);
                this.mFirstItemPosition++;
                this.mListLeftOffset += childAt.getMeasuredWidth();
                if (childCount > 1) {
                    childAt = getChildAt(0);
                } else {
                    childAt = null;
                }
            }
        }
        if (this.mFirstItemPosition != 0 && childCount > 1) {
            View childAt2 = getChildAt(childCount - 1);
            while (childAt2 != null && childAt2.getLeft() + offset > getWidth()) {
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

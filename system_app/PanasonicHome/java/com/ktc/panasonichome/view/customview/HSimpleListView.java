package com.ktc.panasonichome.view.customview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ktc.panasonichome.view.customview.SimpleListView.FILL_MODE;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HSimpleListView extends HorizontalScrollView implements ISimpleListView {
    OnKeyListener focusKeyListener = new C03121();
    private SimpleListView listView = null;
    private Rect rect = new Rect();
    private ScrollNextListener scrollNextListener = null;

    class C03121 implements OnKeyListener {
        C03121() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            View next;
            int len;
            switch (keyCode) {
                case 21:
                    next = HSimpleListView.this.listView.getChildAt(v.getId() - 1);
                    if (next != null) {
                        len = HSimpleListView.this.getNeeScrollDelta(next);
                        if (HSimpleListView.this.scrollNextListener != null) {
                            HSimpleListView.this.scrollNextListener.willScrollTo(v, next, len);
                            break;
                        }
                    }
                    break;
                case 22:
                    next = HSimpleListView.this.listView.getChildAt(v.getId() + 1);
                    if (next != null) {
                        len = HSimpleListView.this.getNeeScrollDelta(next);
                        if (HSimpleListView.this.scrollNextListener != null) {
                            HSimpleListView.this.scrollNextListener.willScrollTo(v, next, len);
                            break;
                        }
                    }
                    break;
            }
            return false;
        }
    }

    public interface ScrollNextListener {
        void willScrollTo(View view, View view2, int i);
    }

    public HSimpleListView(Context context) {
        super(context);
        init(context);
    }

    private void scrollToChild(View child) {
        int scrollDelta = getNeeScrollDelta(child);
        Log.i("scroll", "scrollDelta " + scrollDelta);
        if (scrollDelta != 0) {
            smoothScrollBy(scrollDelta, 0);
        }
    }

    public void setScrollNextListener(ScrollNextListener listener) {
        this.scrollNextListener = listener;
    }

    private int getNeeScrollDelta(View child) {
        child.getDrawingRect(this.rect);
        offsetDescendantRectToMyCoords(child, this.rect);
        return computeScrollDeltaToGetChildRectOnScreen(this.rect);
    }

    public void setAlwaysFocusPos(int pos) {
        this.listView.setAlwaysFocusPos(pos);
    }

    public void setBoundaryListener(OnBoundaryListener listener) {
        this.listView.setBoundaryListener(listener);
    }

    public HSimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public int getFocusPos() {
        return this.listView.getFocusPos();
    }

    public int getSelectedPos() {
        return this.listView.getSelectedPos();
    }

    public HSimpleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void shieldKey(boolean left, boolean up, boolean right, boolean down) {
        this.listView.shieldKey(left, up, right, down);
    }

    private void init(Context context) {
        this.listView = new SimpleListView(context);
        addView(this.listView, new LayoutParams(-1, -1));
        this.listView.setOrientation(LinearLayout.HORIZONTAL);
        this.listView.setFocusKeyListener(this.focusKeyListener);
    }

    public void setFillMode(FILL_MODE mode) {
        this.listView.setFillMode(mode);
    }

    public void setDivide(int divide) {
        this.listView.setDivide(divide);
    }

    public void setDivideImageResource(int id) {
        this.listView.setDivideImageResource(id);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.listView.setAdapter(adapter);
    }

    public void setAnimation(int id) {
        this.listView.setAnimation(id);
    }

    public void setScrollBar(int id) {
        try {
            Field mScrollCacheField = View.class.getDeclaredField("mScrollCache");
            mScrollCacheField.setAccessible(true);
            Object mScrollCache   = mScrollCacheField.get(this);
            Field  scrollBarField = mScrollCache.getClass().getDeclaredField("scrollBar");
            scrollBarField.setAccessible(true);
            Object scrollBar = scrollBarField.get(mScrollCache);
            Method method    = scrollBar.getClass().getDeclaredMethod
                    ("setHorizontalThumbDrawable", new Class[]{Drawable.class});
            method.setAccessible(true);
            method.invoke(scrollBar, new Object[]{getResources().getDrawable(id)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BaseAdapter getAdapter() {
        return this.listView.getAdapter();
    }

    public void setOnItemFocusChangeListener(OnItemFocusChangeListener listener) {
        this.listView.setOnItemFocusChangeListener(listener);
    }

    public void setClickListener(OnClickListener listener) {
        this.listView.setClickListener(listener);
    }

    public void setFocus(int pos) {
        this.listView.setFocus(pos);
    }

    public void setSelected(int pos, boolean selected) {
        this.listView.setSelected(pos, selected);
        if (selected && this.listView.getChildAt(pos) != null) {
            scrollToChild(this.listView.getChildAt(pos));
        }
    }

    public void shieldAllKey(boolean key) {
        this.listView.shieldAllKey(key);
    }

    public void enableFocus(boolean focus) {
        this.listView.enableFocus(focus);
    }
}

package com.ktc.panasonichome.view.customview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class VSimpleListView extends ScrollView implements ISimpleListView {
    private SimpleListView listView = null;

    public VSimpleListView(Context context) {
        super(context);
        init(context);
    }

    public void setAlwaysFocusPos(int pos) {
        this.listView.setAlwaysFocusPos(pos);
    }

    public int getFocusPos() {
        return this.listView.getFocusPos();
    }

    public int getSelectedPos() {
        return this.listView.getSelectedPos();
    }

    public void setBoundaryListener(OnBoundaryListener listener) {
        this.listView.setBoundaryListener(listener);
    }

    public VSimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VSimpleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.listView = new SimpleListView(context);
        addView(this.listView, new LayoutParams(-1, -1));
        this.listView.setOrientation(LinearLayout.VERTICAL);
    }

    public void setDivide(int divide) {
        this.listView.setDivide(divide);
    }

    public void setDivideImageResource(int id) {
        this.listView.setDivideImageResource(id);
    }

    public void shieldKey(boolean left, boolean up, boolean right, boolean down) {
        this.listView.shieldKey(left, up, right, down);
    }

    public void setAdapter(BaseAdapter adapter) {
        this.listView.setAdapter(adapter);
    }

    public void setAnimation(int id) {
        this.listView.setAnimation(id);
    }

    public void setFocus(int pos) {
        this.listView.setFocus(pos);
    }

    public void setSelected(int pos, boolean selected) {
        this.listView.setSelected(pos, selected);
    }

    public void setScrollBar(int id) {
        try {
            Field mScrollCacheField = View.class.getDeclaredField("mScrollCache");
            mScrollCacheField.setAccessible(true);
            Object mScrollCache = mScrollCacheField.get(this);
            Field scrollBarField = mScrollCache.getClass().getDeclaredField("scrollBar");
            scrollBarField.setAccessible(true);
            Object scrollBar = scrollBarField.get(mScrollCache);
            Method method = scrollBar.getClass().getDeclaredMethod("setVerticalThumbDrawable", new Class[]{Drawable.class});
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
}

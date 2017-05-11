package com.ktc.panasonichome.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.ktc.panasonichome.view.customview.BaseAdapter.ObserverListener;

public class SimpleListView extends LinearLayout implements ObserverListener, OnFocusChangeListener, OnKeyListener, ISimpleListView {
    private BaseAdapter adapter = null;
    private boolean alwaysFocus = false;
    private int alwaysFocusPos = 0;
    private OnBoundaryListener boundaryListener = null;
    private OnClickListener clickListener = null;
    private Context context = null;
    private int current = 0;
    private int divide = 0;
    private int divideResource = 0;
    private FILL_MODE fillMode = FILL_MODE.FILL_PARENT;
    private OnKeyListener focusKeyListener = null;
    private boolean forbidOutBound = true;
    private OnItemFocusChangeListener itemFocusChangeListener = null;
    private View lastView = null;
    private boolean shieldAll = false;
    private boolean shieldDown = false;
    private boolean shieldLeft = false;
    private boolean shieldRight = false;
    private boolean shieldUp = false;

    public enum FILL_MODE {
        FILL_PARENT,
        SCROLL
    }

    public void shieldKey(boolean left, boolean up, boolean right, boolean down) {
        this.shieldLeft = left;
        this.shieldUp = up;
        this.shieldRight = right;
        this.shieldDown = down;
    }

    public void setClickListener(OnClickListener listener) {

        this.clickListener = listener;
    }

    public void setFocusKeyListener(OnKeyListener keyListener) {
        this.focusKeyListener = keyListener;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        Log.i("shield", "shield all " + this.shieldAll);
        if (this.shieldAll) {
            return true;
        }
        if (event.getAction() == KeyEvent.ACTION_DOWN && this.forbidOutBound) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (getOrientation() == VERTICAL) {
                        if (v.equals(getChildAt(0)) && this.boundaryListener != null) {
                            return this.boundaryListener.onTopBoundary(v);
                        }
                        if (this.shieldUp && v.equals(getChildAt(0))) {
                            return true;
                        }
                    } else if (this.boundaryListener != null) {
                        return this.boundaryListener.onTopBoundary(v);
                    } else {
                        if (this.shieldUp) {
                            return true;
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (getOrientation() == VERTICAL) {
                        if (v.equals(getChildAt(getChildCount() - 1)) && this.boundaryListener != null) {
                            return this.boundaryListener.onDownBoundary(v);
                        }
                        if (this.shieldDown && v.equals(getChildAt(getChildCount() - 1))) {
                            return true;
                        }
                    } else if (this.boundaryListener != null) {
                        return this.boundaryListener.onDownBoundary(v);
                    } else {
                        if (this.shieldDown) {
                            return true;
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    if (getOrientation() == VERTICAL) {
                        if (this.boundaryListener != null) {
                            return this.boundaryListener.onLeftBoundary(v);
                        }
                        if (this.shieldLeft) {
                            return true;
                        }
                    } else if (v.equals(getChildAt(0)) && this.boundaryListener != null) {
                        return this.boundaryListener.onLeftBoundary(v);
                    } else {
                        if (this.shieldLeft && v.equals(getChildAt(0))) {
                            return true;
                        }
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    if (getOrientation() == VERTICAL) {
                        if (this.boundaryListener != null) {
                            return this.boundaryListener.onRightBoundary(v);
                        }
                        if (this.shieldRight) {
                            return true;
                        }
                    } else if (v.equals(getChildAt(getChildCount() - 1)) && this.boundaryListener != null) {
                        return this.boundaryListener.onRightBoundary(v);
                    } else {
                        if (this.shieldRight && v.equals(getChildAt(getChildCount() - 1))) {
                            return true;
                        }
                    }
                    break;
            }
            if (this.focusKeyListener != null) {
                this.focusKeyListener.onKey(v, keyCode, event);
            }
        }
        return false;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            v.setSelected(false);
            if (!(getChildCount() == 0 || isChildFocus())) {
                if (!(this.itemFocusChangeListener == null || v.equals(this))) {
                    this.itemFocusChangeListener.onNothingFocus(v);
                }
                enableChildFocus(false);
                enableFocus(true);
            }
        } else if (!v.equals(this)) {
            this.current = v.getId();
            v.setSelected(true);
            if (this.itemFocusChangeListener != null) {
                this.itemFocusChangeListener.onFocusItem(v.getId(), v, hasFocus);
            }
        } else if (getChildCount() != 0) {
            enableChildFocus(true);
            if (!this.alwaysFocus) {
                Log.i("category", "current " + this.current);
                if (getChildAt(this.current) != null) {
                    Log.i("category", "category focus " + this.current);
                    getChildAt(this.current).requestFocus();
                }
            } else if (getChildAt(this.alwaysFocusPos) != null) {
                getChildAt(this.alwaysFocusPos).requestFocus();
            }
            enableFocus(false);
            if (this.itemFocusChangeListener != null) {
                this.itemFocusChangeListener.onFocus();
            }
        }
    }

    public void setOnItemFocusChangeListener(OnItemFocusChangeListener listener) {
        this.itemFocusChangeListener = listener;
    }

    public SimpleListView(Context context) {
        super(context);
        init(context);
    }

    public SimpleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        enableFocus(true);
        setOnFocusChangeListener(this);
    }

    public void enableFocus(boolean focus) {
        setFocusable(focus);
        setFocusableInTouchMode(focus);
    }

    public void setDivide(int divide) {
        this.divide = divide;
    }

    public void setDivideImageResource(int id) {
        this.divideResource = id;
    }

    protected void setFillMode(FILL_MODE mode) {
        this.fillMode = mode;
    }

    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        adapter.registObserver(this);
        adapter.notifyDataSetChanaged();
    }

    public void setAnimation(int id) {
        LayoutAnimationController controller = new LayoutAnimationController((AnimationSet) AnimationUtils.loadAnimation(this.context, id), 0.5f);
        controller.setOrder(0);
        setLayoutAnimation(controller);
    }

    public void onChanaged() {
        int count = this.adapter.getCount();
        this.current = 0;
        removeAllViews();
        this.adapter.destroy();
        for (int i = 0; i < count; i++) {
            View view = this.adapter.getView(i, null, this);
            view.setId(i);
            view.setOnClickListener(this.clickListener);
            view.setOnKeyListener(this);
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            if (i != 0) {
                int marginLeft = 0;
                int marginTop = 0;
                if (getOrientation() == VERTICAL) {
                    marginLeft = this.divide;
                } else {
                    marginTop = this.divide;
                }
                params.topMargin = marginTop;
                params.leftMargin = marginLeft;
            }
            addView(view);
        }
        enableChildFocus(false);
    }

    public int getSelectedPos() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (getChildAt(i).isSelected()) {
                return i;
            }
        }
        return -1;
    }

    public int getFocusPos() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (getChildAt(i).isFocused()) {
                return i;
            }
        }
        return -1;
    }

    private boolean isChildFocus() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (getChildAt(i).isFocused()) {
                return true;
            }
        }
        return false;
    }

    public OnFocusChangeListener getOnFocusChangeListener() {
        return this;
    }

    private void enableChildFocus(boolean focus) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).setFocusable(focus);
            getChildAt(i).setFocusableInTouchMode(focus);
            getChildAt(i).setEnabled(focus);
        }
    }

    public void setFocus(int pos) {
        enableChildFocus(true);
        if (pos < getChildCount()) {
            if (!(isChildFocus() || this.itemFocusChangeListener == null)) {
                this.itemFocusChangeListener.onFocus();
            }
            getChildAt(pos).requestFocus();
        }
    }

    public void setSelected(int pos, boolean selected) {
        if (pos < getChildCount()) {
            getChildAt(pos).setSelected(selected);
            if (selected) {
                this.current = pos;
            }
        }
    }

    public void setBoundaryListener(OnBoundaryListener listener) {
        this.boundaryListener = listener;
    }

    public BaseAdapter getAdapter() {
        return this.adapter;
    }

    public void setTranslationX(float translationX) {

        super.setTranslationX(translationX);
    }

    public void shieldAllKey(boolean key) {

        this.shieldAll = key;
    }

    public void setAlwaysFocusPos(int pos) {
        this.alwaysFocusPos = pos;
        this.alwaysFocus = true;
    }
}

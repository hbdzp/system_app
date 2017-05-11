package com.ktc.panasonichome.view.customview;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.ktc.panasonichome.utils.Util;

import java.util.HashMap;
import java.util.Map;

public class SlideFocusView extends FrameLayout {
    public Context context = null;
    private View currentFocusedView = null;
    private int focusBg = 0;
    public final FocusChangedEvent focusChangedEvent = new FocusChangedEvent();
    protected FocusView focusView = null;
    public final ItemSelectedEvent itemSelectedEvent = new ItemSelectedEvent();
    private final Map<Object, FocusViewRevision> revMap = new HashMap();

    private abstract class IEvent<V, T> {
        protected final Map<V, T> eventListenerMap;

        private IEvent() {
            this.eventListenerMap = new HashMap();
        }

        public void registerViewWithoutEvent(View view, FocusViewRevision rev) {
            if (rev != null) {
                synchronized (SlideFocusView.this.revMap) {
                    SlideFocusView.this.revMap.put(view, rev);
                }
            }
        }

        public void registerView(V view) {
            registerView(view, null, null);
        }

        public void registerView(V view, FocusViewRevision rev) {
            registerView(view, rev, null);
        }

        public void registerView(V view, FocusViewRevision rev, T listener) {
            if (rev != null) {
                synchronized (SlideFocusView.this.revMap) {
                    SlideFocusView.this.revMap.put(view, rev);
                }
            }
            if (listener != null) {
                synchronized (this.eventListenerMap) {
                    this.eventListenerMap.put(view, listener);
                }
            }
        }

        public void clearViewEventListener(V view) {
            if (view != null) {
                synchronized (this.eventListenerMap) {
                    this.eventListenerMap.remove(view);
                }
            }
        }
    }

    public class FocusChangedEvent extends IEvent<View, OnFocusChangeListener> implements OnFocusChangeListener {
        
/*
        public *//* bridge *//* *//* synthetic *//* void clearViewEventListener(Object view) {
            super.clearViewEventListener(view);
        }

        public *//* bridge *//* *//* synthetic *//* void registerView(Object view) {
            super.registerView(view);
        }

        public *//* bridge *//* *//* synthetic *//* void registerView(Object view, FocusViewRevision rev) {
            super.registerView(view, rev);
        }

        public *//* bridge *//* *//* synthetic *//* void registerViewWithoutEvent(View view, FocusViewRevision rev) {
            super.registerViewWithoutEvent(view, rev);
        }*/

        public FocusChangedEvent() {
            super();
        }

        public void registerView(View view, FocusViewRevision rev, OnFocusChangeListener listener) {
            view.setOnFocusChangeListener(this);
            super.registerView(view, rev, listener);
        }

        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                SlideFocusView.this.updateFocusView(v);
            }
            OnFocusChangeListener listener=null;
            synchronized (this.eventListenerMap) {
                 listener = (OnFocusChangeListener) this.eventListenerMap.get(v);
            }
            if (listener != null) {
                listener.onFocusChange(v, hasFocus);
            }
        }
    }

    public static class FocusView extends FrameLayout {
        private int bgId = 0;
        private Context context = null;
        private int destHeight;
        private int destWidth;
        private boolean executeAnim = true;
        private boolean focusAnimation = true;
        private View lastFocus = null;
        private OnMoveStateListener moveStateListener = null;
        private int posX;
        private int posY;
        private boolean stopMoveOnce = false;

        class C03131 implements AnimatorListener {
            C03131() {
            }

            public void onAnimationStart(Animator animation) {
                if (FocusView.this.moveStateListener != null) {
                    FocusView.this.moveStateListener.beginMove();
                }
            }

            public void onAnimationEnd(Animator animation) {
                if (FocusView.this.moveStateListener != null) {
                    FocusView.this.moveStateListener.endMove();
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        }

        public void setMoveStateListener(OnMoveStateListener listener) {
            this.moveStateListener = listener;
        }

        public FocusView(Context context) {
            super(context);
            this.context = context;
        }

        public void changeFocusPos(int posX, int posY, int destWidth, int destHeight) {
            if (this.stopMoveOnce) {
                this.stopMoveOnce = false;
                return;
            }
            this.posX = posX;
            this.posY = posY;
            this.destWidth = destWidth;
            this.destHeight = destHeight;
            Log.i("focuspos", "x " + posX + " y " + posY + " w " + destWidth + " h " + destHeight);
            beginAnimation();
        }

        public void stopMoveOnce() {
            this.stopMoveOnce = true;
        }

        public void setBackgroundResource(int resid) {
            this.bgId = resid;
            if (this.lastFocus != null) {
                this.lastFocus.setBackgroundResource(resid);
            }
            this.executeAnim = false;
        }

        private void startAlphaAnimation(View v, float from, float to, long during, AnimatorListener listener) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(v, "alpha", new float[]{from, to});
            animator.setDuration(during);
            if (listener != null) {
                animator.addListener(listener);
            }
            animator.start();
        }

        public void moveFocusByCurrent(int x, int y) {
            if (this.stopMoveOnce) {
                this.stopMoveOnce = false;
                return;
            }
            this.posX += x;
            this.posY += y;
            beginAnimation();
        }

        public void moveFocusByCurrent(int x, int y, int w, int h) {
            if (this.stopMoveOnce) {
                this.stopMoveOnce = false;
                return;
            }
            this.posX += x;
            this.posY += y;
            this.destWidth += w;
            this.destHeight += h;
            beginAnimation();
        }

        private void beginAnimation() {
            if (this.lastFocus != null) {
                if (this.executeAnim && this.focusAnimation) {
                    startAlphaAnimation(this.lastFocus, 1.0f, 0.0f, 200, null);
                } else {
                    this.lastFocus.clearAnimation();
                    this.lastFocus.setVisibility(View.GONE);
                }
            }
            for (int i = 0; i < getChildCount() - 3; i++) {
                removeView(getChildAt(i));
            }
            View focus = new ImageView(this.context);
            focus.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
            Log.i("dzp", "focus count is " + getChildCount());
            addView(focus);
            focus.setVisibility(View.VISIBLE);
            focus.setBackgroundResource(this.bgId);
            setFocusPos(focus, this.posX, this.posY, this.destWidth, this.destHeight);
            this.lastFocus = focus;
        }

        public void setFocusPos(View view, int posX, int posY, int destWidth, int destHeight) {
            LayoutParams frameParams = (LayoutParams) view.getLayoutParams();
            frameParams.leftMargin = posX;
            frameParams.topMargin = posY;
            frameParams.width = destWidth;
            frameParams.height = destHeight;
            view.setLayoutParams(frameParams);
            if (this.executeAnim && this.focusAnimation) {
                startAlphaAnimation(view, 0.0f, 1.0f, 200, new C03131());
                return;
            }
            this.executeAnim = true;
            view.clearAnimation();
            view.setVisibility(View.VISIBLE);
            if (view.getAlpha() != 1.0f) {
                view.setAlpha(1.0f);
            }
        }

        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            initStep();
            super.onSizeChanged(w, h, oldw, oldh);
        }

        private void initStep() {
        }
    }

    public static class FocusViewRevision {
        public int bottom;
        public int left;
        public int right;
        public int top;

        public FocusViewRevision(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    public interface ISlideFocusView {
        void initSlideFocusView(SlideFocusView slideFocusView);
    }

    public class ItemSelectedEvent extends IEvent<AdapterView<?>, OnItemSelectedListener> implements OnItemSelectedListener {
        /*public *//* bridge *//* *//* synthetic *//* void clearViewEventListener(Object view) {
            super.clearViewEventListener(view);
        }

        public *//* bridge *//* *//* synthetic *//* void registerView(Object view) {
            super.registerView(view);
        }

        public *//* bridge *//* *//* synthetic *//* void registerView(Object view, FocusViewRevision rev) {
            super.registerView(view, rev);
        }

        public *//* bridge *//* *//* synthetic *//* void registerViewWithoutEvent(View view, FocusViewRevision rev) {
            super.registerViewWithoutEvent(view, rev);
        }*/

        public ItemSelectedEvent() {
            super();
        }

        public void registerView(AdapterView<?> view, FocusViewRevision rev, OnItemSelectedListener listener) {
            view.setOnItemSelectedListener(this);
            super.registerView(view, rev, listener);
        }

        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            OnItemSelectedListener listener=null;
            synchronized (this.eventListenerMap) {
                listener = (OnItemSelectedListener) this.eventListenerMap.get(parent);
            }
            if (listener != null) {
                listener.onItemSelected(parent, view, position, id);
            }
            if (view != null) {
                SlideFocusView.this.updateFocusView((View) parent, view);
            }
        }

        public void onNothingSelected(AdapterView<?> parent) {
            OnItemSelectedListener listener=null;
            synchronized (this.eventListenerMap) {
                 listener = (OnItemSelectedListener) this.eventListenerMap.get(parent);
            }
            if (listener != null) {
                listener.onNothingSelected(parent);
            }
        }
    }

    public interface OnMoveStateListener {
        void beginMove();

        void endMove();
    }

    public void setMoveStateListener(OnMoveStateListener listener) {
        this.focusView.setMoveStateListener(listener);
    }

    public SlideFocusView(Context context, int focusDrawable) {
        super(context);
        this.context = context;
        Util.instence(context);
        this.focusBg = focusDrawable;
        setLayoutParams(new LayoutParams(-1, -1));
        this.focusView = new FocusView(context);
        this.focusView.setBackgroundResource(focusDrawable);
        this.focusView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
        this.focusView.setFocusable(false);
        this.focusView.setFocusableInTouchMode(false);
        addView(this.focusView);
    }

    public void changeFocusBg(int id) {
        this.focusBg = id;
        this.focusView.setBackgroundResource(id);
    }

    public boolean isSameFocusBg(int id) {
        if (id == this.focusBg) {
            return true;
        }
        return false;
    }

    public void initChildView(ISlideFocusView i) {
        i.initSlideFocusView(this);
    }

    public void updateFocusView(View v) {
        FocusViewRevision rev;
        synchronized (this.revMap) {
            rev = (FocusViewRevision) this.revMap.get(v);
        }
        updateFocusView(v, rev);
    }

    private void updateFocusView(View parent, View v) {
        FocusViewRevision rev;
        synchronized (this.revMap) {
            rev = (FocusViewRevision) this.revMap.get(parent);
        }
        updateFocusView(v, rev);
    }

    private void updateFocusView(View v, FocusViewRevision rev) {
        int[] to = new int[2];
        v.getLocationInWindow(to);
        int _focusViewWidth = v.getWidth();
        int _focusViewHeight = v.getHeight();
        if (rev != null) {
            _focusViewWidth += rev.left + rev.right;
            _focusViewHeight += rev.top + rev.bottom;
            to[0] = to[0] - rev.left;
            to[1] = to[1] - rev.top;
        }
        Log.i("OOO", "to:" + to[0] + "," + to[1] + " view : " + v.toString());
        this.focusView.changeFocusPos(to[0], to[1], _focusViewWidth, _focusViewHeight);
    }

    public void stopAnimationOnce() {
        this.focusView.executeAnim = false;
    }

    public void moveFocusTo(View view, FocusViewRevision revision) {
        updateFocusView(view, revision);
    }

    public void moveFocusByCurrent(int x, int y) {
        this.focusView.moveFocusByCurrent(x, y);
    }

    public void moveFocusByCurrent(int x, int y, int w, int h) {
        this.focusView.moveFocusByCurrent(x, y, w, h);
    }

    public void moveFocusTo(Rect rect) {
        this.focusView.changeFocusPos(rect.left, rect.top, rect.width(), rect.height());
    }

    public FocusView getFocusView() {
        return this.focusView;
    }

    public void setFocusAnimation(boolean animation) {
        this.focusView.focusAnimation = animation;
    }
}

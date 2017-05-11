package com.ktc.panasonichome.view.customview;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ktc.panasonichome.view.customview.BaseAdapter.ObserverListener;

import java.util.HashMap;

public class ExpandMenuView extends ScrollView implements ObserverListener {
    private ExpandAdapter adapter = null;
    private Context context = null;
    private boolean expand = true;
    private HashMap<View, Integer> heightCache = new HashMap();
    private LinearLayout mainLayout = null;
    private OnMenuClickListener menuClickListener = null;
    OnClickListener oneMenuClickListener = new C03081();
    private HashMap<View, View> oneSubMenuCache = new HashMap();
    private int subMarginLeft = 0;
    private HashMap<View, View> subMenuCache = new HashMap();
    OnClickListener subMenuClickListener = new C03092();

    class C03081 implements OnClickListener {
        C03081() {
        }

        public void onClick(View v) {
            if (ExpandMenuView.this.expand) {
                View view = (View) ExpandMenuView.this.subMenuCache.get(v);
                if (view != null) {
                    LayoutParams params = (LayoutParams) view.getLayoutParams();
                    if (params.height == 0) {
                        ExpandMenuView.this.expandByAnimation((ViewGroup) view, params, 0, ((Integer) ExpandMenuView.this.heightCache.get(view)).intValue());
                    } else {
                        ExpandMenuView.this.expandByAnimation((ViewGroup) view, params, ((Integer) ExpandMenuView.this.heightCache.get(view)).intValue(), 0);
                    }
                }
            }
            if (ExpandMenuView.this.menuClickListener != null) {
                ExpandMenuView.this.menuClickListener.oneMenuClick(v.getId());
            }
        }
    }

    class C03092 implements OnClickListener {
        C03092() {
        }

        public void onClick(View v) {
            if (ExpandMenuView.this.menuClickListener != null) {
                ExpandMenuView.this.menuClickListener.subMenuClick(((View) ExpandMenuView.this.oneSubMenuCache.get(v)).getId(), v.getId());
            }
        }
    }

    public interface OnMenuClickListener {
        void oneMenuClick(int i);

        void subMenuClick(int i, int i2);
    }

    public ExpandMenuView(Context context) {
        super(context);
        init(context);
    }

    public ExpandMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExpandMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setOneMenuClickListener(OnMenuClickListener listener) {
        this.menuClickListener = listener;
    }

    public void supportExpand(boolean expand) {
        this.expand = expand;
    }

    public void setSubMarginLeft(int marginLeft) {
        this.subMarginLeft = marginLeft;
    }

    private void init(Context context) {
        this.context = context;
        this.mainLayout = new LinearLayout(context);
        this.mainLayout.setOrientation(LinearLayout.VERTICAL);
        addView(this.mainLayout, new LayoutParams(-2, -1));
    }

    public void setAdapter(ExpandAdapter adapter) {
        this.adapter = adapter;
        this.adapter.registObserver(this);
    }

    public void onChanaged() {
        int count = this.adapter.getCount();
        for (int i = 0; i < count; i++) {
            View view = this.adapter.getOneView(i, null, this.mainLayout);
            view.setOnClickListener(this.oneMenuClickListener);
            view.setId(i);
            this.mainLayout.addView(view);
            int subCount = this.adapter.getSubCount(i);
            if (subCount > 0) {
                LinearLayout subLayout = new LinearLayout(this.context);
                subLayout.setOrientation(LinearLayout.VERTICAL);
                int height = 0;
                for (int j = 0; j < subCount; j++) {
                    View subView = this.adapter.getView(j, null, subLayout);
                    this.oneSubMenuCache.put(subView, view);
                    subView.setId(j);
                    height += subView.getLayoutParams().height;
                    subView.setOnClickListener(this.subMenuClickListener);
                    subLayout.addView(subView);
                }
                int subHeight = 0;
                if (this.expand) {
                    enableSubMenuFocus(false, subLayout);
                } else {
                    subHeight = -2;
                }
                LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(-2, subHeight);
                subLayout.setPadding(this.subMarginLeft, 0, 0, 0);
                this.mainLayout.addView(subLayout, subParams);
                this.subMenuCache.put(view, subLayout);
                this.heightCache.put(subLayout, Integer.valueOf(height));
            }
        }
    }

    private void expandByAnimation(final ViewGroup view, final LayoutParams params, final int start, final int end) {
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{(float) start, (float) end});
        if (start < end) {
            animator.setInterpolator(new OvershootInterpolator());
        }
        animator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                params.height = ((Float) animation.getAnimatedValue()).intValue();
                view.setLayoutParams(params);
            }
        });
        animator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                if (start > end) {
                    ExpandMenuView.this.enableSubMenuFocus(false, view);
                }
            }

            public void onAnimationEnd(Animator animation) {
                if (start < end) {
                    ExpandMenuView.this.enableSubMenuFocus(true, view);
                }
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        animator.setDuration(200);
        animator.start();
    }

    private void enableSubMenuFocus(boolean focus, ViewGroup subLayout) {
        int count = subLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            subLayout.getChildAt(i).setFocusable(focus);
            subLayout.getChildAt(i).setFocusableInTouchMode(focus);
        }
    }
}

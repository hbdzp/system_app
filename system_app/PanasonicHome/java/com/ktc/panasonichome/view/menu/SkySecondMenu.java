package com.ktc.panasonichome.view.menu;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.android.volley.DefaultRetryPolicy;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.MyFocusFrame;

import java.util.ArrayList;
import java.util.List;

public class SkySecondMenu extends FrameLayout {
    public static int preViewWidth = 0;
    private LayoutParams contentParams;
    private int dataCount = 0;
    public boolean dismissBySecondLeft_back = false;
    private boolean isAnimStart = false;
    public boolean isDismisAnimStart = false;
    private int itemHeight = ScreenParams.getInstence(this.mContext).getResolutionValue(90);
    private int itemMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(0);
    OnKeyListener itemOnKeyListener = new C03492();
    private int lastFocusItemID = -1;
    private SkyMenuAdapter                mAdapter;
    private OnSecondItemAnimListener      mAnimListener;
    private Context                       mContext;
    private OnSecondMenuItemOnkeyListener mListener;
    private MyFocusFrame                  menuFocusView;
    private int menuHeight = ScreenParams.getInstence(this.mContext).getResolutionValue(630);
    private List<SkyMenuItem_2> menuItems;
    private int menuWidth = ScreenParams.getInstence(this.mContext).getResolutionValue(270);
    private long onKeyTime = 0;
    public boolean show_disAnimStart = false;
    private int startOffset = 160;
    OnTouchListener touchListener = new C03481();

    public interface OnSecondMenuItemOnkeyListener {
        boolean onSecondMenuItemOnClick(int i, View view, SkyMenuData skyMenuData);

        boolean onSecondMenuItemOnKeyBack(int i, View view);

        boolean onSecondMenuItemOnKeyLeft(int i, View view);

        boolean onSecondMenuItemOnKeyRight(int i, View view, SkyMenuData skyMenuData);

        boolean onSecondMenuOnKeyOther(int i, View view, int i2);
    }

    public interface OnSecondItemAnimListener {
        void onSecondItemAnimEnd(boolean z);
    }

    class C03481 implements OnTouchListener {
        C03481() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() != 0) {
                return false;
            }
            if (SkySecondMenu.this.isAnimStart) {
                return true;
            }
            for (int i = 0; i < SkySecondMenu.this.dataCount; i++) {
                if (v.getId() != i) {
                    ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(i)).setSelectState(false);
                    ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(i)).reSetItemState();
                }
            }
            ((SkyMenuItem_2) v).setSelectState(true);
            if (SkySecondMenu.this.mListener == null) {
                return false;
            }
            SkySecondMenu.this.mListener.onSecondMenuItemOnClick(v.getId(), v, ((SkyMenuItem_2) v).getItemData());
            return true;
        }
    }

    class C03492 implements OnKeyListener {
        C03492() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != 0) {
                return false;
            }
            SkySecondMenu.this.lastFocusItemID = -1;
            int id = v.getId();
            if (keyCode == 19) {
                if (System.currentTimeMillis() - SkySecondMenu.this.onKeyTime < 150) {
                    SkySecondMenu.this.startOffset = 60;
                } else {
                    SkySecondMenu.this.startOffset = 160;
                }
                SkySecondMenu.this.onKeyTime = System.currentTimeMillis();
                if (SkySecondMenu.this.isAnimStart) {
                    return true;
                }
                if (id == 0) {
                    return true;
                }
                SkySecondMenu.this.menuFocusView.needAnimtion(false);
                SkySecondMenu.this.onKeyItemMoveEvent(keyCode, v);
                if (SkySecondMenu.this.mListener != null) {
                    SkySecondMenu.this.mListener.onSecondMenuOnKeyOther(id, v, keyCode);
                }
                return true;
            } else if (keyCode == 20) {
                long downTime = System.currentTimeMillis();
                LogUtils.v("dzp", "downTime == " + downTime);
                LogUtils.v("dzp", "onKeyTime == " + SkySecondMenu.this.onKeyTime);
                LogUtils.v("dzp", "(downTime - onKeyTime) == " + (downTime - SkySecondMenu.this.onKeyTime));
                if (downTime - SkySecondMenu.this.onKeyTime < 150) {
                    SkySecondMenu.this.startOffset = 60;
                } else {
                    SkySecondMenu.this.startOffset = 160;
                }
                SkySecondMenu.this.onKeyTime = System.currentTimeMillis();
                if (SkySecondMenu.this.isAnimStart) {
                    return true;
                }
                if (id == SkySecondMenu.this.dataCount - 1) {
                    return true;
                }
                SkySecondMenu.this.menuFocusView.needAnimtion(false);
                SkySecondMenu.this.onKeyItemMoveEvent(keyCode, v);
                if (SkySecondMenu.this.mListener != null) {
                    SkySecondMenu.this.mListener.onSecondMenuOnKeyOther(id, v, keyCode);
                }
                return true;
            } else if (keyCode == 21) {
                LogUtils.v("dzp", "isShowAnimStart == " + SkySecondMenu.this.isDismisAnimStart);
                if (SkySecondMenu.this.isAnimStart) {
                    return true;
                }
                if (SkySecondMenu.this.mListener == null) {
                    return true;
                }
                SkySecondMenu.this.menuFocusView.needAnimtion(false);
                return SkySecondMenu.this.mListener.onSecondMenuItemOnKeyLeft(id, v);
            } else if (keyCode == 22) {
                if (SkySecondMenu.this.isAnimStart) {
                    return true;
                }
                if (SkySecondMenu.this.mListener != null) {
                    return SkySecondMenu.this.mListener.onSecondMenuItemOnKeyRight(id, v, ((SkyMenuItem_2) v).getItemData());
                }
                return true;
            } else if (keyCode == 4) {
                if (SkySecondMenu.this.isAnimStart) {
                    return true;
                }
                if (SkySecondMenu.this.mListener != null) {
                    return SkySecondMenu.this.mListener.onSecondMenuItemOnKeyBack(id, v);
                }
                return false;
            } else if (keyCode == 23 || keyCode == 66) {
                if (SkySecondMenu.this.isAnimStart) {
                    return true;
                }
                for (int i = 0; i < SkySecondMenu.this.dataCount; i++) {
                    if (v.getId() != i) {
                        ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(i)).setSelectState(false);
                        ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(i)).reSetItemState();
                    }
                }
                ((SkyMenuItem_2) v).setSelectState(true);
                if (SkySecondMenu.this.mListener == null) {
                    return true;
                }
                SkySecondMenu.this.mListener.onSecondMenuItemOnClick(v.getId(), v, ((SkyMenuItem_2) v).getItemData());
                return true;
            } else if (SkySecondMenu.this.mListener != null) {
                return SkySecondMenu.this.mListener.onSecondMenuOnKeyOther(id, v, keyCode);
            } else {
                return false;
            }
        }
    }

    public SkySecondMenu(Context context) {
        super(context);
        this.mContext = context;
        initView();
        setFocusable(false);
    }

    public void setSecondMenuOnItemOnkeyListener(OnSecondMenuItemOnkeyListener listener) {
        this.mListener = listener;
    }

    public void setOnSecondItemAnimListener(OnSecondItemAnimListener listener) {
        this.mAnimListener = listener;
    }

    public void setFocusView(MyFocusFrame menuFocusView) {
        this.menuFocusView = menuFocusView;
    }

    private void initView() {
        this.menuItems = new ArrayList();
        this.contentParams = new LayoutParams(-2, this.menuHeight);
        setLayoutParams(this.contentParams);
    }

    public void setPreViewWidth(int width) {
        preViewWidth = width;
    }

    public void refreshItemData(SkyMenuData data, int index) {
        if (this.menuItems != null && this.menuItems.size() >= index) {
            ((SkyMenuItem_2) this.menuItems.get(index)).refreshItemValue(data);
        }
    }

    public void setMenuItemFocus(int id) {
        if (id > 0 && this.menuItems != null && id < this.menuItems.size() && this.menuItems.get(id) != null) {
            LogUtils.v("dzp", "focus == " + ((SkyMenuItem_2) this.menuItems.get(id)).requestFocus());
        }
    }

    public void setMenuAdapter(final SkyFirstMenu menu1, SkyMenuAdapter adapter, final int focuseID) {
        removeAllViews();
        this.lastFocusItemID = -1;
        this.isAnimStart = false;
        this.isDismisAnimStart = false;
        this.dismissBySecondLeft_back = false;
        if (this.menuItems != null) {
            this.menuItems.clear();
        }
        if (this.mAdapter != null) {
            this.mAdapter = null;
        }
        this.dataCount = 0;
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            this.dataCount = this.mAdapter.getCount();
            if (this.dataCount > 0) {
                for (int i = 0; i < this.dataCount; i++) {
                    SkyMenuItem_2 item = new SkyMenuItem_2(this.mContext);
                    item.setTextAttribute(ScreenParams.getInstence(this.mContext).getTextDpiValue(36), -1);
                    item.setFocusView(this.menuFocusView);
                    item.setId(i);
                    item.setSelectState(false);
                    item.setOnKeyListener(this.itemOnKeyListener);
                    item.setOnTouchListener(this.touchListener);
                    item.updataItemValue(this.mAdapter.getMenuItemData(i));
                    LayoutParams itemParams = new LayoutParams(-2, this.itemHeight);
                    itemParams.topMargin = ((i + 3) - focuseID) * (this.itemHeight + this.itemMargin);
                    if (itemParams != null) {
                        item.setLayoutParams(itemParams);
                    }
                    this.menuItems.add(item);
                    addView(item);
                    item.setVisibility(View.INVISIBLE);
                }
                if (getVisibility() != View.VISIBLE) {
                    setVisibility(View.VISIBLE);
                    show_dismissAnimation(menu1, true, focuseID, false);
                } else if (this.menuItems != null && this.menuItems.size() - 1 >= focuseID) {
                    ((SkyMenuItem_2) this.menuItems.get(focuseID)).post(new Runnable() {
                        public void run() {
                            ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(focuseID)).setSelectState(true);
                            ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(focuseID)).requestFocus();
                            menu1.setItemFocusble(true, 0, false);
                            menu1.setShowingSecondMenu(false);
                        }
                    });
                }
            }
        }
    }

    public void show_dismissAnimation(SkyFirstMenu menu1, boolean isShow, int index, boolean needRecoll) {
        this.show_disAnimStart = true;
        final int i = index;
        final boolean z = isShow;
        final boolean z2 = needRecoll;
        final SkyFirstMenu skyFirstMenu = menu1;
        post(new Runnable() {
            public void run() {
                int start;
                int end;
                int midleIndex = 0;
                if (i - 3 >= 0) {
                    start = i - 3;
                } else {
                    start = 0;
                }
                if (i + 3 <= SkySecondMenu.this.menuItems.size() - 1) {
                    end = i + 4;
                } else {
                    end = SkySecondMenu.this.menuItems.size();
                }
                List<SkyMenuItem_2> targetItems = SkySecondMenu.this.menuItems.subList(start, end);
                if (targetItems != null) {
                    int count = targetItems.size();
                    if (count == 7) {
                        midleIndex = 3;
                    } else if (count < 7) {
                        if (i < 3) {
                            midleIndex = i;
                        } else {
                            midleIndex = 3;
                        }
                    }
                    for (int i = 0; i < count; i++) {
                        TranslateAnimation transAnimation;
                        AlphaAnimation alphInAnimation;
                        AnimationSet animset = new AnimationSet(false);
                        if (z) {
                            animset.setFillAfter(false);
                            transAnimation = new TranslateAnimation((float) (-SkySecondMenu.this.getWidth()), 0.0f, 0.0f, 0.0f);
                            alphInAnimation = new AlphaAnimation(0.2f, 1.0f);
                            transAnimation.setInterpolator(new AccelerateInterpolator());
                            alphInAnimation.setInterpolator(new AccelerateInterpolator());
                        } else {
                            animset.setFillAfter(true);
                            transAnimation = new TranslateAnimation(0.0f, (float) (-SkySecondMenu.this.getWidth()), 0.0f, 0.0f);
                            alphInAnimation = new AlphaAnimation(1.0f, 0.0f);
                            transAnimation.setInterpolator(new DecelerateInterpolator());
                            alphInAnimation.setInterpolator(new DecelerateInterpolator());
                        }
                        transAnimation.setStartOffset((long) ((i * 200) / count));
                        transAnimation.setDuration(200);
                        animset.addAnimation(transAnimation);
                        alphInAnimation.setStartOffset((long) ((i * 200) / count));
                        alphInAnimation.setDuration(200);
                        animset.addAnimation(alphInAnimation);
                        if (i == midleIndex) {
                            final int i2 = i;
                            final boolean z = z2;
                            final boolean z2 = z;
//                            final SkyFirstMenu skyFirstMenu = skyFirstMenu;
                            animset.setAnimationListener(new AnimationListener() {
                                public void onAnimationStart(Animation animation) {
                                    if (i2 == SkySecondMenu.this.dataCount - 1 && SkySecondMenu.this.mAnimListener != null && z) {
                                        SkySecondMenu.this.mAnimListener.onSecondItemAnimEnd(z2);
                                    }
                                    if (z2 && z2 && SkySecondMenu.this.menuItems != null && SkySecondMenu.this.menuItems.size() - 1 >= i2) {
                                        ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(i2)).setSelectState(true);
                                        ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(i2)).requestFocus();
                                        int[] location = new int[2];
                                        ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(i2)).getLocationInWindow(location);
                                        int shaderSize = ScreenParams.getInstence(SkySecondMenu.this.mContext).getResolutionValue(83);
                                        SkySecondMenu.this.menuFocusView.changeFocusPos(location[0] - shaderSize, location[1] - shaderSize, ScreenParams.getInstence(SkySecondMenu.this.mContext).getResolutionValue(344) + (shaderSize * 2), ScreenParams.getInstence(SkySecondMenu.this.mContext).getResolutionValue(90) + (shaderSize * 2));
                                        skyFirstMenu.setItemFocusble(true, 0, false);
                                        skyFirstMenu.setShowingSecondMenu(false);
                                    }
                                }

                                public void onAnimationRepeat(Animation animation) {
                                }

                                public void onAnimationEnd(Animation animation) {
                                    if (i2 == SkySecondMenu.this.dataCount - 1) {
                                        SkySecondMenu.this.show_disAnimStart = false;
                                        if (!z2) {
                                            SkySecondMenu.this.setVisibility(View.INVISIBLE);
                                            SkySecondMenu.this.isDismisAnimStart = false;
                                        }
                                    }
                                }
                            });
                        }
                        if (i == count - 1 && i != SkySecondMenu.this.dataCount - 1) {
                            final boolean z3 = z2;
                            final boolean z4 = z;
                            animset.setAnimationListener(new AnimationListener() {
                                public void onAnimationStart(Animation animation) {
                                    if (SkySecondMenu.this.mAnimListener != null && z3) {
                                        SkySecondMenu.this.mAnimListener.onSecondItemAnimEnd(z4);
                                    }
                                }

                                public void onAnimationRepeat(Animation animation) {
                                }

                                public void onAnimationEnd(Animation animation) {
                                    SkySecondMenu.this.show_disAnimStart = false;
                                    if (!z4) {
                                        SkySecondMenu.this.setVisibility(View.INVISIBLE);
                                        SkySecondMenu.this.isDismisAnimStart = false;
                                    }
                                }
                            });
                        }
                        ((SkyMenuItem_2) targetItems.get(i)).setVisibility(View.VISIBLE);
                        ((SkyMenuItem_2) targetItems.get(i)).startAnimation(animset);
                    }
                }
            }
        });
    }

    public void setLastFocusItemID(int id) {
        this.lastFocusItemID = id;
    }

    public int getLastFocusID() {
        return this.lastFocusItemID;
    }

    private void onKeyItemMoveEvent(int keyCode, View v) {
        this.isAnimStart = true;
        final int index = v.getId();
        int moveY = this.itemHeight + this.itemMargin;
        final int itemCount = this.mAdapter.getCount();
        int i;
        SkyMenuItem_2 item;
        LayoutParams item_p;
        AnimationSet animset;
        TranslateAnimation transAnimation;
        AlphaAnimation alphInAnimation;
        AlphaAnimation alphOutAnimation;
        if (keyCode == 19) {
            for (i = itemCount - 1; i >= 0; i--) {
                item = (SkyMenuItem_2) this.menuItems.get(i);
                item_p = (LayoutParams) item.getLayoutParams();
                item_p.topMargin += moveY;
                item.setLayoutParams(item_p);
                item.clearAnimation();
                animset = new AnimationSet(false);
                transAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-moveY), 0.0f);
                transAnimation.setStartOffset((long) ((((itemCount - 1) - i) * this.startOffset) / itemCount));
                transAnimation.setDuration(100);
                transAnimation.setInterpolator(new AccelerateInterpolator());
                animset.addAnimation(transAnimation);
                if (item.getVisibility() != View.VISIBLE) {
                    item.setVisibility(View.VISIBLE);
                }
                if (i == index - 4) {
                    alphInAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphInAnimation.setFillAfter(true);
                    alphInAnimation.setDuration(100);
                    alphInAnimation.setStartOffset((long) ((this.startOffset * i) / itemCount));
                    animset.addAnimation(alphInAnimation);
                }
                if (i == index + 3) {
                    alphOutAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphOutAnimation.setFillAfter(true);
                    alphOutAnimation.setDuration(100);
                    alphOutAnimation.setStartOffset((long) ((((itemCount - 1) - i) * this.startOffset) / itemCount));
                    animset.addAnimation(alphOutAnimation);
                }
                item.startAnimation(animset);
                if (i == 0) {
                    transAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            if (index + 3 < itemCount && SkySecondMenu.this.menuItems.get(index + 3) != null) {
                                ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(index + 3)).setVisibility(View.INVISIBLE);
                            }
                            if (index - 1 == 0) {
                                ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(index - 1)).requestFocus();
                            }
                            SkySecondMenu.this.isAnimStart = false;
                        }
                    });
                } else if (i == index - 1) {
                    transAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(index - 1)).requestFocus();
                            if (index - 1 == 0) {
                                SkySecondMenu.this.isAnimStart = false;
                            }
                        }
                    });
                }
            }
        } else if (keyCode == 20) {
            for (i = 0; i < itemCount; i++) {
                item = (SkyMenuItem_2) this.menuItems.get(i);
                item_p = (LayoutParams) item.getLayoutParams();
                item_p.topMargin -= moveY;
                item.setLayoutParams(item_p);
                item.clearAnimation();
                animset = new AnimationSet(false);
                transAnimation = new TranslateAnimation(0.0f, 0.0f, (float) moveY, 0.0f);
                transAnimation.setStartOffset((long) ((this.startOffset * i) / itemCount));
                transAnimation.setDuration(100);
                transAnimation.setInterpolator(new AccelerateInterpolator());
                animset.addAnimation(transAnimation);
                if (item.getVisibility() != View.VISIBLE) {
                    item.setVisibility(View.VISIBLE);
                }
                if (i == index - 3) {
                    alphOutAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphOutAnimation.setFillAfter(true);
                    alphOutAnimation.setDuration(100);
                    alphOutAnimation.setStartOffset((long) ((this.startOffset * i) / itemCount));
                    animset.addAnimation(alphOutAnimation);
                }
                if (i == index + 4) {
                    alphInAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphInAnimation.setFillAfter(true);
                    alphInAnimation.setDuration(100);
                    alphInAnimation.setStartOffset((long) ((((itemCount - 1) - i) * this.startOffset) / itemCount));
                    animset.addAnimation(alphInAnimation);
                }
                item.startAnimation(animset);
                if (i == itemCount - 1) {
                    transAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            if (index - 3 >= 0 && SkySecondMenu.this.menuItems.get(index - 3) != null) {
                                ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(index - 3)).setVisibility(View.INVISIBLE);
                            }
                            if (index + 1 == itemCount - 1) {
                                ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(index + 1)).requestFocus();
                            }
                            SkySecondMenu.this.isAnimStart = false;
                        }
                    });
                } else if (i == index + 1) {
                    transAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            ((SkyMenuItem_2) SkySecondMenu.this.menuItems.get(index + 1)).requestFocus();
                        }
                    });
                }
            }
        }
    }
}

package com.ktc.ui.menu;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
////import com.ktc.framework.ktccommondefine.KtcworthBroadcastKey;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
import com.ktc.ui.api.KtcCommenMenu;
import com.ktc.util.KtcScreenParams;
import com.ktc.util.MyFocusFrame;

import java.util.ArrayList;
import java.util.List;
//import org.apache.http4.HttpStatus;

public class KtcFirstMenu extends FrameLayout {
    private int dataCount = 0;
    public boolean endDismiss = true;
    private boolean isAnimStart = false;
    private boolean isShowingSecondMenu = false;
    private int itemHeight = KtcScreenParams.getInstence(this.mContext).getResolutionValue(100);
    private int itemMargin = KtcScreenParams.getInstence(this.mContext).getResolutionValue(18);
    OnKeyListener itemOnKeyListener = new OnKeyListener() {
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            if (keyEvent.getAction() != 0) {
                return false;
            }
            int i2;
            for (i2 = 0; i2 < KtcFirstMenu.this.dataCount; i2++) {
                ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(i2)).setSelectState(false);
            }
            KtcFirstMenu.this.lastFocusItemID = -1;
            int id = view.getId();
            i2 = id < 0 ? 0 : id;
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if (System.currentTimeMillis() - KtcFirstMenu.this.onKeyTime < 150) {
                    KtcFirstMenu.this.startOffset = 60;
                } else {
                    KtcFirstMenu.this.startOffset = 160;
                }
                KtcFirstMenu.this.onKeyTime = System.currentTimeMillis();
                if (KtcFirstMenu.this.isAnimStart) {
                    return true;
                }
                if (KtcFirstMenu.this.isShowingSecondMenu()) {
                    return true;
                }
                if (i2 == 0) {
                    return true;
                }
                KtcFirstMenu.this.menuFocusView.needAnimtion(false);
                KtcFirstMenu.this.onKeyItemMoveEvent(keyCode, view);
                if (KtcFirstMenu.this.mListener != null) {
                    KtcFirstMenu.this.mListener.onItemOnKeyOther(i2, view, keyCode);
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                long currentTimeMillis = System.currentTimeMillis();
              ////KtcLogger.v("xw", "downTime == " + currentTimeMillis);
              //// KtcLogger.v("xw", "onKeyTime == " + KtcFirstMenu.this.onKeyTime);
              ////KtcLogger.v("xw", "(downTime - onKeyTime) == " + (currentTimeMillis - KtcFirstMenu.this.onKeyTime));
                if (currentTimeMillis - KtcFirstMenu.this.onKeyTime < 150) {
                    KtcFirstMenu.this.startOffset = 60;
                } else {
                    KtcFirstMenu.this.startOffset = 160;
                }
                KtcFirstMenu.this.onKeyTime = System.currentTimeMillis();
                if (KtcFirstMenu.this.isAnimStart) {
                    return true;
                }
                if (KtcFirstMenu.this.isShowingSecondMenu()) {
                    return true;
                }
                KtcFirstMenu.this.menuFocusView.clearAnimation();
                KtcFirstMenu.this.menuFocusView.setVisibility(View.VISIBLE);
                KtcFirstMenu.this.showAnimStart = false;
                if (i2 == KtcFirstMenu.this.dataCount - 1) {
                    return true;
                }
                KtcFirstMenu.this.menuFocusView.needAnimtion(false);
                KtcFirstMenu.this.onKeyItemMoveEvent(keyCode, view);
                if (KtcFirstMenu.this.mListener != null) {
                    KtcFirstMenu.this.mListener.onItemOnKeyOther(i2, view, keyCode);
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                return KtcFirstMenu.this.isAnimStart ? true : KtcFirstMenu.this.showAnimStart ? true : KtcFirstMenu.this.isShowingSecondMenu() ? true : KtcFirstMenu.this.mListener != null ? KtcFirstMenu.this.mListener.onItemOnKeyLeft(i2, view) : true;
            } else {
                if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    if (KtcFirstMenu.this.isAnimStart) {
                        return true;
                    }
                    if (KtcFirstMenu.this.showAnimStart) {
                        return true;
                    }
                    if (KtcFirstMenu.this.isShowingSecondMenu()) {
                        return true;
                    }
                    if (KtcFirstMenu.this.mListener == null) {
                        return true;
                    }
                    KtcMenuData itemData = ((KtcMenuItem_1) view).getItemData();
                    KtcFirstMenu.this.menuFocusView.needAnimtion(false);
                    KtcFirstMenu.this.lastFocusItemID = i2;
                    return KtcFirstMenu.this.mListener.onItemOnKeyRight(i2, view, itemData);
                } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return KtcFirstMenu.this.isAnimStart ? true : KtcFirstMenu.this.mListener != null ? KtcFirstMenu.this.mListener.onItemOnKeyBack(i2, view) : false;
                } else {
                    if (keyCode != KeyEvent.KEYCODE_DPAD_CENTER && keyCode != KeyEvent.KEYCODE_ENTER) {
                        return KtcFirstMenu.this.mListener != null ? KtcFirstMenu.this.mListener.onItemOnKeyOther(i2, view, keyCode) : false;
                    } else {
                        if (KtcFirstMenu.this.isAnimStart) {
                            return true;
                        }
                        if (KtcFirstMenu.this.showAnimStart) {
                            return true;
                        }
                        if (KtcFirstMenu.this.isShowingSecondMenu()) {
                            return true;
                        }
                        KtcMenuData itemData2 = ((KtcMenuItem_1) view).getItemData();
                        ((KtcMenuItem_1) view).setSelectState(true);
                        if (KtcFirstMenu.this.mListener == null) {
                            return true;
                        }
                        KtcFirstMenu.this.lastFocusItemID = i2;
                        KtcFirstMenu.this.mListener.onItemOnClick(i2, view, itemData2);
                      //// KtcLogger.v("lgx", "lastFocusItemID ----------=== " + KtcFirstMenu.this.lastFocusItemID);
                        return true;
                    }
                }
            }
        }
    };
    OnTouchListener itemTouchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            if (KtcFirstMenu.this.isAnimStart) {
                return true;
            }
            ((KtcMenuItem_1) view).setSelectState(true);
            if (KtcFirstMenu.this.mListener != null) {
                KtcFirstMenu.this.mListener.onItemOnClick(view.getId(), view, ((KtcMenuItem_1) view).getItemData());
            }
            return true;
        }
    };
    private int lastFocusItemID = -1;
    private KtcMenuAdapter mAdapter;
    private Context mContext;
    private OnItemOnkeyListener mListener;
    private MyFocusFrame menuFocusView;
    private int menuHeight = KtcScreenParams.getInstence(this.mContext).getResolutionValue(572);
    private List<KtcMenuItem_1> menuItems;
    //KtcworthBroadcastKey.KTC_BROADCAST_KEY_SENSE_BACK = 250
    //private int menuWidth = KtcScreenParams.getInstence(this.mContext).getResolutionValue(KtcworthBroadcastKey.KTC_BROADCAST_KEY_SENSE_BACK);
    private int menuWidth = KtcScreenParams.getInstence(this.mContext).getResolutionValue(250);
    private long onKeyTime = 0;
    private boolean showAnimStart = false;
    private int startOffset = 160;

    public interface OnItemOnkeyListener {
        boolean onItemOnClick(int i, View view, KtcMenuData ktcMenuData);

        boolean onItemOnKeyBack(int i, View view);

        boolean onItemOnKeyLeft(int i, View view);

        boolean onItemOnKeyOther(int i, View view, int i2);

        boolean onItemOnKeyRight(int i, View view, KtcMenuData ktcMenuData);
    }

    public KtcFirstMenu(Context context) {
        super(context);
        this.mContext = context;
        initView();
        setFocusable(false);
    }

    private void initView() {
        this.menuItems = new ArrayList();
        setLayoutParams(new LayoutParams(-2, this.menuHeight));
    }

    private void onKeyItemMoveEvent(int keyCode, View view) {
        Log.d("Maxs","KtcFirstMenu:onKeyItemMoveEvent:keyCode " + keyCode);
        this.isAnimStart = true;
        final int id = view.getId();
        int i2 = this.itemHeight + this.itemMargin;
        final int count = this.mAdapter.getCount();
        int i3;
        KtcMenuItem_1 ktcMenuItem1;
        LayoutParams layoutParams;
        AnimationSet animationSet;
        TranslateAnimation translateAnimation;
        AlphaAnimation alphaAnimation;
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            for (i3 = count - 1; i3 >= 0; i3--) {
                ktcMenuItem1 = (KtcMenuItem_1) this.menuItems.get(i3);
                layoutParams = (LayoutParams) ktcMenuItem1.getLayoutParams();
                layoutParams.topMargin += i2;
                ktcMenuItem1.setLayoutParams(layoutParams);
                ktcMenuItem1.clearAnimation();
                animationSet = new AnimationSet(false);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-i2), 0.0f);
                translateAnimation.setStartOffset((long) ((((count - 1) - i3) * this.startOffset) / count));
                translateAnimation.setDuration(100);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation);
                if (ktcMenuItem1.getVisibility() != View.VISIBLE) {
                    ktcMenuItem1.setVisibility(View.VISIBLE);
                }
                if (i3 == id - 3) {
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(100);
                    alphaAnimation.setStartOffset((long) ((this.startOffset * i3) / count));
                    animationSet.addAnimation(alphaAnimation);
                }
                if (i3 == id + 2) {
                    alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(100);
                    alphaAnimation.setStartOffset((long) ((((count - 1) - i3) * this.startOffset) / count));
                    animationSet.addAnimation(alphaAnimation);
                }
                ktcMenuItem1.startAnimation(animationSet);
                if (i3 == 0) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (id + 2 < count && id + 2 < KtcFirstMenu.this.menuItems.size() && KtcFirstMenu.this.menuItems.get(id + 2) != null) {
                                ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(id + 2)).setVisibility(View.INVISIBLE);
                            }
                            if (id - 1 == 0) {
                                KtcFirstMenu.this.setItemFocusble(false, id - 1, true);
                                ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(id - 1)).requestFocus();
                            }
                            KtcFirstMenu.this.isAnimStart = false;
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                } else if (i3 == id - 1) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            KtcFirstMenu.this.setItemFocusble(false, id - 1, true);
                            ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(id - 1)).requestFocus();
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                }
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            for (i3 = 0; i3 < count; i3++) {
                ktcMenuItem1 = (KtcMenuItem_1) this.menuItems.get(i3);
                layoutParams = (LayoutParams) ktcMenuItem1.getLayoutParams();
                layoutParams.topMargin -= i2;
                ktcMenuItem1.setLayoutParams(layoutParams);
                ktcMenuItem1.clearAnimation();
                animationSet = new AnimationSet(false);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) i2, 0.0f);
                translateAnimation.setStartOffset((long) ((this.startOffset * i3) / count));
                translateAnimation.setDuration(100);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation);
                if (ktcMenuItem1.getVisibility() != View.VISIBLE) {
                    ktcMenuItem1.setVisibility(View.VISIBLE);
                }
                if (i3 == id - 2) {
                    alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(100);
                    alphaAnimation.setStartOffset((long) ((this.startOffset * i3) / count));
                    animationSet.addAnimation(alphaAnimation);
                }
                if (i3 == id + 3) {
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(100);
                    alphaAnimation.setStartOffset((long) ((((count - 1) - i3) * this.startOffset) / count));
                    animationSet.addAnimation(alphaAnimation);
                }
                ktcMenuItem1.startAnimation(animationSet);
                if (i3 == count - 1) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (id - 2 >= 0 && KtcFirstMenu.this.menuItems.get(id - 2) != null) {
                                ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(id - 2)).setVisibility(View.INVISIBLE);
                            }
                            if (id + 1 == count - 1) {
                                KtcFirstMenu.this.setItemFocusble(false, id + 1, true);
                                ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(id + 1)).requestFocus();
                            }
                            KtcFirstMenu.this.isAnimStart = false;
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                } else if (i3 == id + 1) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            KtcFirstMenu.this.setItemFocusble(false, id + 1, true);
                            ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(id + 1)).requestFocus();
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                }
            }
        }
    }

    private void setMenuAdapter(KtcMenuAdapter ktcMenuAdapter) {
        removeAllViews();
        Log.d("Maxs","setMenuAdapter-----");
        this.isAnimStart = false;
        this.endDismiss = true;
        this.showAnimStart = true;
        if (this.menuItems != null) {
            this.menuItems.clear();
        }
        if (this.mAdapter != null) {
            this.mAdapter = null;
        }
        this.dataCount = 0;
        this.mAdapter = ktcMenuAdapter;
        if (this.mAdapter != null) {
            this.dataCount = this.mAdapter.getCount();
            if (this.dataCount > 0) {
                for (int i = 0; i < this.dataCount; i++) {
                    Log.d("Maxs","KtcFirstMenu:dataCount = " + dataCount);
                	KtcMenuItem_1 ktcMenuItem1 = new KtcMenuItem_1(this.mContext);
                    ktcMenuItem1.setTextAttribute(KtcScreenParams.getInstence(this.mContext).getTextDpiValue(36), -16777216);
                    ktcMenuItem1.setFocusView(this.menuFocusView);
                    ktcMenuItem1.setFocusable(true);
                    ktcMenuItem1.setFocusableInTouchMode(true);
                    ktcMenuItem1.setSelectState(false);
                    ktcMenuItem1.setId(i);
                    ktcMenuItem1.setOnKeyListener(this.itemOnKeyListener);
                    ktcMenuItem1.setOnTouchListener(this.itemTouchListener);
                    LayoutParams layoutParams = new LayoutParams(-2, this.itemHeight);
                    layoutParams.topMargin = (i + 2) * (this.itemHeight + this.itemMargin);
                    if (layoutParams != null) {
                        ktcMenuItem1.setLayoutParams(layoutParams);
                    }
                    ktcMenuItem1.setVisibility(View.INVISIBLE);
                    ktcMenuItem1.updataItemValue(this.mAdapter.getMenuItemData(i));
                    this.menuItems.add(ktcMenuItem1);
                    addView(ktcMenuItem1);
                }
                Log.d("Maxs","KtcFirstMenu:this -----");
                showAnimation();
            }
        }
    }

    private void showAnimation() {

        Log.d("Maxs","xiiisisisi ");
        post(new Runnable() {
            public void run() {
                Log.d("Maxs","xiiis2222isisi ");
                //int -get0 = KtcFirstMenu.this.dataCount > 3 ? 3 : KtcFirstMenu.this.dataCount;
            	////dataCount = KtcFirstMenu.this.dataCount > 3 ? 3 : KtcFirstMenu.this.dataCount;
                dataCount = KtcFirstMenu.this.dataCount > 3 ? KtcFirstMenu.this.dataCount : 3;
                Log.d("Maxs","KtcFirstMenu:dataCount = " + dataCount);
                ////for (int i = 0; i < -get0; i++) {
            	for (int i = 0; i < dataCount; i++) {
                	AnimationSet animationSet = new AnimationSet(false);
                	TranslateAnimation translateAnimation = new TranslateAnimation((float) (getWidth()), 0.0f, 0.0f, 0.0f);
                    ////translateAnimation.setStartOffset((long) ((i * HttpStatus.SC_MULTIPLE_CHOICES) / 3));
                    ////HttpStatus.SC_MULTIPLE_CHOICES = 300
                    translateAnimation.setStartOffset((long) ((i * 300) / 3));
                    translateAnimation.setDuration(200);
                    translateAnimation.setInterpolator(new OvershootInterpolator(1.0f));
                    animationSet.addAnimation(translateAnimation);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    ////translateAnimation.setStartOffset((long) ((i * HttpStatus.SC_MULTIPLE_CHOICES) / 3));
                    alphaAnimation.setStartOffset((long) ((i * 300) / 3));
                    alphaAnimation.setDuration(200);
                    alphaAnimation.setInterpolator(new AccelerateInterpolator());
                    animationSet.addAnimation(alphaAnimation);
                    ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(i)).setVisibility(View.VISIBLE);
                    ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(i)).startAnimation(animationSet);
                    if (i == 0) {
                        animationSet.setAnimationListener(new AnimationListener() {
                            public void onAnimationEnd(Animation animation) {
                                Log.d("Maxs","KtcFirstMenu:showAnimal:onAnimaltionStart");
                                Log.d("Maxs","KtcFirstMenu:showAnimal:KtcFirstMenu.this.menuItems != null = " + (KtcFirstMenu.this.menuItems != null));
                                Log.d("Maxs","KtcFirstMenu:showAnimal:KtcFirstMenu.this.menuItems.size() = " + (KtcFirstMenu.this.menuItems.size()));
                                if (KtcFirstMenu.this.menuItems != null && KtcFirstMenu.this.menuItems.size() > 0) {
                                    ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(0)).post(new Runnable() {
                                        public void run() {
                                            KtcFirstMenu.this.menuFocusView.initStarPosition(-KtcScreenParams.getInstence(KtcFirstMenu.this.mContext).getResolutionValue(31), KtcScreenParams.getInstence(KtcFirstMenu.this.mContext).getResolutionValue(496), KtcScreenParams.getInstence(KtcFirstMenu.this.mContext).getResolutionValue(358) + (KtcScreenParams.getInstence(KtcFirstMenu.this.mContext).getResolutionValue(83) * 2), (KtcScreenParams.getInstence(KtcFirstMenu.this.mContext).getResolutionValue(83) * 2) + KtcScreenParams.getInstence(KtcFirstMenu.this.mContext).getResolutionValue(100));
                                            if (KtcFirstMenu.this.menuFocusView.getVisibility() != View.VISIBLE) {
                                                KtcFirstMenu.this.menuFocusView.setVisibility(View.VISIBLE);
                                            }
                                            Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                                            alphaAnimation.setDuration(200);
                                            KtcFirstMenu.this.menuFocusView.startAnimation(alphaAnimation);
                                          ////KtcLogger.v("lgx", "hasFocus == " + ((KtcMenuItem) KtcFirstMenu.this.menuItems.get(0)).hasFocus());
                                            if (!((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(0)).hasFocus()) {
                                                KtcFirstMenu.this.setItemFocusble(false, 0, true);
                                              //// KtcLogger.v("lgx", "focus == " + ((KtcMenuItem) KtcFirstMenu.this.menuItems.get(0)).requestFocus());
                                                ((KtcMenuItem_1) KtcFirstMenu.this.menuItems.get(0)).requestFocus();
                                            }
                                            KtcFirstMenu.this.showAnimStart = false;
                                        }
                                    });
                                }
                            }

                            public void onAnimationRepeat(Animation animation) {
                            }

                            public void onAnimationStart(Animation animation) {
                                Log.d("Maxs","KtcFirstMenu:showAnimal:onAnimaltionStart");
                            }
                        });
                    }
                }
            }
        });
    }

    public void dismissAnimtion(final KtcCommenMenu ktcCommenMenu, int i) {
        this.endDismiss = false;
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(200);
        this.menuFocusView.startAnimation(alphaAnimation);
        List subList = this.menuItems.subList(i + -2 >= 0 ? i - 2 : 0, i + 2 <= this.menuItems.size() + -1 ? i + 3 : this.menuItems.size());
        if (subList != null) {
            int size = subList.size();
            for (int i2 = 0; i2 < size; i2++) {
                ((KtcMenuItem_1) subList.get(i2)).clearAnimation();
                AnimationSet animationSet = new AnimationSet(false);
                animationSet.setFillAfter(true);
                alphaAnimation = new TranslateAnimation(0.0f, (float) (-getWidth()), 0.0f, 0.0f);
                //alphaAnimation.setStartOffset((long) ((i2 * HttpStatus.SC_OK) / size));
                alphaAnimation.setStartOffset((long) ((i2 * 200) / size));
                alphaAnimation.setDuration(200);
                alphaAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(alphaAnimation);
                alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                ////HttpStatus.SC_OK
                //alphaAnimation.setStartOffset((long) ((i2 * HttpStatus.SC_OK) / size));
                alphaAnimation.setStartOffset((long) ((i2 * 200) / size));
                alphaAnimation.setDuration(100);
                alphaAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(alphaAnimation);
                ((KtcMenuItem_1) subList.get(i2)).startAnimation(animationSet);
                if (i2 == size - 1) {
                    animationSet.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            ktcCommenMenu.setVisibility(View.GONE);
                            KtcFirstMenu.this.endDismiss = true;
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                }
            }
        }
    }

    public int getLastFocusID() {
        return this.lastFocusItemID;
    }

    public boolean isShowingSecondMenu() {
    	////KtcLogger.v("lgx", "isShowingSecondMenu--->  " + this.isShowingSecondMenu);
        return this.isShowingSecondMenu;
    }

    public void refreshItemData(KtcMenuData ktcMenuData, int i) {
        if (this.menuItems != null && this.menuItems.size() >= i) {
            ((KtcMenuItem_1) this.menuItems.get(i)).refreshItemValue(ktcMenuData);
        }
    }

    public void resetOnKeyListener() {
        for (int i = 0; i < this.menuItems.size(); i++) {
            ((KtcMenuItem_1) this.menuItems.get(i)).setOnKeyListener(null);
        }
    }

    public void setFocusView(MyFocusFrame myFocusFrame) {
        this.menuFocusView = myFocusFrame;
    }

    public void setItemFocusble(boolean z, int i, boolean z2) {
        if (z) {
            for (int i2 = 0; i2 < this.dataCount; i2++) {
                ((KtcMenuItem_1) this.menuItems.get(i2)).setFocusable(z2);
            }
        } else if (i < this.dataCount && this.menuItems.get(i) != null) {
            ((KtcMenuItem_1) this.menuItems.get(i)).setFocusable(z2);
        }
    }

    public void setItemStateShowSecond(boolean z) {
        int i = 0;
        int i2 = this.lastFocusItemID;
        List subList = this.menuItems.subList(i2 + -2 >= 0 ? i2 - 2 : 0, i2 + 2 <= this.menuItems.size() + -1 ? i2 + 3 : this.menuItems.size());
        while (i < subList.size()) {
            ((KtcMenuItem_1) subList.get(i)).setItemStateForSecond(z);
            i++;
        }
    }

    public void setLastFocusItemID(int i) {
        this.lastFocusItemID = i;
    }

    public void setMenuItemFocus(int i) {
    	//// KtcLogger.v("lgx", "back focus!!!!!  id == " + i);
        if (i == -1) {
            setMenuAdapter(this.mAdapter);
        } else if (this.menuItems != null && i < this.menuItems.size() && this.menuItems.get(i) != null) {
            setItemFocusble(false, i, true);
            ((KtcMenuItem_1) this.menuItems.get(i)).resetvIconBgState();
            boolean requestFocus = ((KtcMenuItem_1) this.menuItems.get(i)).requestFocus();
            this.menuFocusView.initStarPosition(-KtcScreenParams.getInstence(this.mContext).getResolutionValue(31), KtcScreenParams.getInstence(this.mContext).getResolutionValue(496), KtcScreenParams.getInstence(this.mContext).getResolutionValue(358) + (KtcScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2), (KtcScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2) + KtcScreenParams.getInstence(this.mContext).getResolutionValue(100));
          ////KtcLogger.v("lgx", "focus == " + requestFocus);
        }
    }

    public void setOnItemOnkeyListener(OnItemOnkeyListener onItemOnkeyListener) {
        this.mListener = onItemOnkeyListener;
    }

    public void setShowingSecondMenu(boolean z) {
        this.isShowingSecondMenu = z;
      ////KtcLogger.v("lgx", "setShowingSecondMenu--->  " + z);
    }

    public void showFitstMenu(KtcMenuAdapter ktcMenuAdapter) {
        setMenuAdapter(ktcMenuAdapter);
    }
}

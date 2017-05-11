package com.ktc.panasonichome.view.menu;

import android.content.Context;
import android.graphics.Color;
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

import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.MyFocusFrame;
import com.ktc.panasonichome.view.api.SkyCommenMenu;

import java.util.ArrayList;
import java.util.List;


public class SkyFirstMenu extends FrameLayout {
    private int dataCount = 0;
    public boolean endDismiss = true;
    private boolean isAnimStart = false;
    private boolean isShowingSecondMenu = false;
    private int itemHeight = ScreenParams.getInstence(this.mContext).getResolutionValue(100);
    private int itemMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(18);
    OnKeyListener itemOnKeyListener = new C03342();
    OnTouchListener itemTouchListener = new C03331();
    private int lastFocusItemID = -1;
    private SkyMenuAdapter      mAdapter;
    private Context             mContext;
    private OnItemOnkeyListener mListener;
    private MyFocusFrame        menuFocusView;
    private int menuHeight = ScreenParams.getInstence(this.mContext).getResolutionValue(572);
    private List<SkyMenuItem> menuItems;
    private int menuWidth = ScreenParams.getInstence(this.mContext).getResolutionValue(250);
    private long onKeyTime = 0;
    private boolean showAnimStart = false;
    private int startOffset = 160;
    private SkyMenuData   menuData=null;

    public interface OnItemOnkeyListener {
        boolean onItemOnClick(int i, View view, SkyMenuData skyMenuData);

        boolean onItemOnKeyBack(int i, View view);

        boolean onItemOnKeyLeft(int i, View view);

        boolean onItemOnKeyOther(int i, View view, int i2);

        boolean onItemOnKeyRight(int i, View view, SkyMenuData skyMenuData);
    }

    class C03331 implements OnTouchListener {
        C03331() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() != 0) {
                return false;
            }
            if (SkyFirstMenu.this.isAnimStart) {
                return true;
            }
            ((SkyMenuItem) v).setSelectState(true);
            if (SkyFirstMenu.this.mListener != null) {
                SkyFirstMenu.this.mListener.onItemOnClick(v.getId(), v, ((SkyMenuItem) v).getItemData());
            }
            return true;
        }
    }

    class C03342 implements OnKeyListener {
        C03342() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() != 0) {
                return false;
            }
            for (int i = 0; i < SkyFirstMenu.this.dataCount; i++) {
                ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(i)).setSelectState(false);
            }
            SkyFirstMenu.this.lastFocusItemID = -1;
            int id = v.getId();
            if (id < 0) {
                id = 0;
            }
            if (keyCode == 19) {
                if (System.currentTimeMillis() - SkyFirstMenu.this.onKeyTime < 150) {
                    SkyFirstMenu.this.startOffset = 60;
                } else {
                    SkyFirstMenu.this.startOffset = 160;
                }
                SkyFirstMenu.this.onKeyTime = System.currentTimeMillis();
                if (SkyFirstMenu.this.isAnimStart) {
                    return true;
                }
                if (SkyFirstMenu.this.isShowingSecondMenu()) {
                    return true;
                }
                if (id == 0) {
                    return true;
                }
                SkyFirstMenu.this.menuFocusView.needAnimtion(false);
                SkyFirstMenu.this.onKeyItemMoveEvent(keyCode, v);
                if (SkyFirstMenu.this.mListener != null) {
                    SkyFirstMenu.this.mListener.onItemOnKeyOther(id, v, keyCode);
                }
                return true;
            } else if (keyCode == 20) {
                long downTime = System.currentTimeMillis();
               LogUtils.v("dzp", "downTime == " + downTime);
               LogUtils.v("dzp", "onKeyTime == " + SkyFirstMenu.this.onKeyTime);
               LogUtils.v("dzp", "(downTime - onKeyTime) == " + (downTime - SkyFirstMenu.this.onKeyTime));
                if (downTime - SkyFirstMenu.this.onKeyTime < 150) {
                    SkyFirstMenu.this.startOffset = 60;
                } else {
                    SkyFirstMenu.this.startOffset = 160;
                }
                SkyFirstMenu.this.onKeyTime = System.currentTimeMillis();
                if (SkyFirstMenu.this.isAnimStart) {
                    return true;
                }
                if (SkyFirstMenu.this.isShowingSecondMenu()) {
                    return true;
                }
                SkyFirstMenu.this.menuFocusView.clearAnimation();
                SkyFirstMenu.this.menuFocusView.setVisibility(View.VISIBLE);
                SkyFirstMenu.this.showAnimStart = false;
                if (id == SkyFirstMenu.this.dataCount - 1) {
                    return true;
                }
                SkyFirstMenu.this.menuFocusView.needAnimtion(false);
                SkyFirstMenu.this.onKeyItemMoveEvent(keyCode, v);
                if (SkyFirstMenu.this.mListener != null) {
                    SkyFirstMenu.this.mListener.onItemOnKeyOther(id, v, keyCode);
                }
                return true;
            } else if (keyCode == 21) {
                if (SkyFirstMenu.this.isAnimStart) {
                    return true;
                }
                if (SkyFirstMenu.this.showAnimStart) {
                    return true;
                }
                if (SkyFirstMenu.this.isShowingSecondMenu()) {
                    return true;
                }
                if (SkyFirstMenu.this.mListener != null) {
                    return SkyFirstMenu.this.mListener.onItemOnKeyLeft(id, v);
                }
                return true;
            } else if (keyCode == 22) {
                if (SkyFirstMenu.this.isAnimStart) {
                    return true;
                }
                if (SkyFirstMenu.this.showAnimStart) {
                    return true;
                }
                if (SkyFirstMenu.this.isShowingSecondMenu()) {
                    return true;
                }
                if (SkyFirstMenu.this.mListener == null) {
                    return true;
                }
                menuData = ((SkyMenuItem) v).getItemData();
                SkyFirstMenu.this.menuFocusView.needAnimtion(false);
                SkyFirstMenu.this.lastFocusItemID = id;
                return SkyFirstMenu.this.mListener.onItemOnKeyRight(id, v, menuData);
            } else if (keyCode == 4) {
                if (SkyFirstMenu.this.isAnimStart) {
                    return true;
                }
                if (SkyFirstMenu.this.mListener != null) {
                    return SkyFirstMenu.this.mListener.onItemOnKeyBack(id, v);
                }
                return false;
            } else if (keyCode == 23 || keyCode == 66) {
                if (SkyFirstMenu.this.isAnimStart) {
                    return true;
                }
                if (SkyFirstMenu.this.showAnimStart) {
                    return true;
                }
                if (SkyFirstMenu.this.isShowingSecondMenu()) {
                    return true;
                }
                menuData = ((SkyMenuItem) v).getItemData();
                ((SkyMenuItem) v).setSelectState(true);
                if (SkyFirstMenu.this.mListener == null) {
                    return true;
                }
                SkyFirstMenu.this.lastFocusItemID = id;
                SkyFirstMenu.this.mListener.onItemOnClick(id, v, menuData);
               LogUtils.v("dzp", "lastFocusItemID ----------=== " + SkyFirstMenu.this.lastFocusItemID);
                return true;
            } else if (SkyFirstMenu.this.mListener != null) {
                return SkyFirstMenu.this.mListener.onItemOnKeyOther(id, v, keyCode);
            } else {
                return false;
            }
        }
    }

    class C03373 implements Runnable {

        class C03361 implements AnimationListener {

            class C03351 implements Runnable {
                C03351() {
                }

                public void run() {
                    SkyFirstMenu.this.menuFocusView.initStarPosition(-ScreenParams.getInstence(SkyFirstMenu.this.mContext).getResolutionValue(31), ScreenParams.getInstence(SkyFirstMenu.this.mContext).getResolutionValue(496), ScreenParams.getInstence(SkyFirstMenu.this.mContext).getResolutionValue(358) + (ScreenParams.getInstence(SkyFirstMenu.this.mContext).getResolutionValue(83) * 2), ScreenParams.getInstence(SkyFirstMenu.this.mContext).getResolutionValue(100) + (ScreenParams.getInstence(SkyFirstMenu.this.mContext).getResolutionValue(83) * 2));
                    if (SkyFirstMenu.this.menuFocusView.getVisibility() !=View.VISIBLE) {
                        SkyFirstMenu.this.menuFocusView.setVisibility(View.VISIBLE);
                    }
                    AlphaAnimation alphInAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphInAnimation.setDuration(200);
                    SkyFirstMenu.this.menuFocusView.startAnimation(alphInAnimation);
                   LogUtils.v("dzp", "hasFocus == " + ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(0)).hasFocus());
                    if (!((SkyMenuItem) SkyFirstMenu.this.menuItems.get(0)).hasFocus()) {
                        SkyFirstMenu.this.setItemFocusble(false, 0, true);
                       LogUtils.v("dzp", "focus == " + ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(0)).requestFocus());
                    }
                    SkyFirstMenu.this.showAnimStart = false;
                }
            }

            C03361() {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if (SkyFirstMenu.this.menuItems != null && SkyFirstMenu.this.menuItems.size() > 0) {
                    ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(0)).post(new C03351());
                }
            }
        }

        C03373() {
        }

        public void run() {
            int size;
            if (SkyFirstMenu.this.dataCount > 3) {
                size = 3;
            } else {
                size = SkyFirstMenu.this.dataCount;
            }
            for (int i = 0; i < size; i++) {
                AnimationSet animset = new AnimationSet(false);
                TranslateAnimation transAnimation = new TranslateAnimation((float) (-SkyFirstMenu.this.getWidth()), 0.0f, 0.0f, 0.0f);
                transAnimation.setStartOffset((long) ((i * 300) / 3));
                transAnimation.setDuration(200);
                transAnimation.setInterpolator(new OvershootInterpolator(1.0f));
                animset.addAnimation(transAnimation);
                AlphaAnimation alphInAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphInAnimation.setStartOffset((long) ((i * 300) / 3));
                alphInAnimation.setDuration(200);
                alphInAnimation.setInterpolator(new AccelerateInterpolator());
                animset.addAnimation(alphInAnimation);
                ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(i)).setVisibility(View.VISIBLE);
                ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(i)).startAnimation(animset);
                if (i == 0) {
                    animset.setAnimationListener(new C03361());
                }
            }
        }
    }

    public SkyFirstMenu(Context context) {
        super(context);
        this.mContext = context;
        initView();
        setFocusable(false);
    }

    public void setOnItemOnkeyListener(OnItemOnkeyListener listener) {
        this.mListener = listener;
    }

    private void initView() {
        this.menuItems = new ArrayList();
        setLayoutParams(new LayoutParams(-2, this.menuHeight));
    }

    public void setFocusView(MyFocusFrame menuFocusView) {
        this.menuFocusView = menuFocusView;
    }

    public boolean isShowingSecondMenu() {
       LogUtils.v("dzp", "isShowingSecondMenu--->  " + this.isShowingSecondMenu);
        return this.isShowingSecondMenu;
    }

    public void setShowingSecondMenu(boolean isShowingSecondMenu) {
        this.isShowingSecondMenu = isShowingSecondMenu;
       LogUtils.v("dzp", "setShowingSecondMenu--->  " + isShowingSecondMenu);
    }

    public void showFitstMenu(SkyMenuAdapter adapter) {
        setMenuAdapter(adapter);
    }

    public void refreshItemData(SkyMenuData data, int index) {
        if (this.menuItems != null && this.menuItems.size() >= index) {
            ((SkyMenuItem) this.menuItems.get(index)).refreshItemValue(data);
        }
    }

    private void setMenuAdapter(SkyMenuAdapter adapter) {
        removeAllViews();
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
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            this.dataCount = this.mAdapter.getCount();
            if (this.dataCount > 0) {
                for (int i = 0; i < this.dataCount; i++) {
                    SkyMenuItem item = new SkyMenuItem(this.mContext);
                    item.setTextAttribute(ScreenParams.getInstence(this.mContext).getTextDpiValue(36), Color.BLACK);
                    item.setFocusView(this.menuFocusView);
                    item.setFocusable(true);
                    item.setFocusableInTouchMode(true);
                    item.setSelectState(false);
                    item.setId(i);
                    item.setOnKeyListener(this.itemOnKeyListener);
                    item.setOnTouchListener(this.itemTouchListener);
                    LayoutParams itemParams = new LayoutParams(-2, this.itemHeight);
                    itemParams.topMargin = (i + 2) * (this.itemHeight + this.itemMargin);
                    if (itemParams != null) {
                        item.setLayoutParams(itemParams);
                    }
                    item.setVisibility(View.INVISIBLE);
                    item.updataItemValue(this.mAdapter.getMenuItemData(i));
                    this.menuItems.add(item);
                    addView(item);
                }
                showAnimation();
            }
        }
    }

    private void showAnimation() {
        post(new C03373());
    }

    public void setItemFocusble(boolean isAll, int id, boolean focusble) {
        if (isAll) {
            for (int i = 0; i < this.dataCount; i++) {
                ((SkyMenuItem) this.menuItems.get(i)).setFocusable(focusble);
            }
        } else if (id < this.dataCount && this.menuItems.get(id) != null) {
            ((SkyMenuItem) this.menuItems.get(id)).setFocusable(focusble);
        }
    }

    public void setItemStateShowSecond(boolean show) {
        int start;
        int end;
        int index = this.lastFocusItemID;
        if (index - 2 >= 0) {
            start = index - 2;
        } else {
            start = 0;
        }
        if (index + 2 <= this.menuItems.size() - 1) {
            end = index + 3;
        } else {
            end = this.menuItems.size();
        }
        List<SkyMenuItem> targetItems = this.menuItems.subList(start, end);
        for (int i = 0; i < targetItems.size(); i++) {
            ((SkyMenuItem) targetItems.get(i)).setItemStateForSecond(show);
        }
    }

    public void dismissAnimtion(final SkyCommenMenu menu, int index) {
        int start;
        int end;
        this.endDismiss = false;
        AlphaAnimation focusAnimation = new AlphaAnimation(1.0f, 0.0f);
        focusAnimation.setFillAfter(true);
        focusAnimation.setDuration(200);
        this.menuFocusView.startAnimation(focusAnimation);
        if (index - 2 >= 0) {
            start = index - 2;
        } else {
            start = 0;
        }
        if (index + 2 <= this.menuItems.size() - 1) {
            end = index + 3;
        } else {
            end = this.menuItems.size();
        }
        List<SkyMenuItem> targetItems = this.menuItems.subList(start, end);
        if (targetItems != null) {
            int count = targetItems.size();
            for (int i = 0; i < count; i++) {
                ((SkyMenuItem) targetItems.get(i)).clearAnimation();
                AnimationSet animset = new AnimationSet(false);
                animset.setFillAfter(true);
                TranslateAnimation transAnimation = new TranslateAnimation(0.0f, (float) (-getWidth()), 0.0f, 0.0f);
                transAnimation.setStartOffset((long) ((i * 200) / count));
                transAnimation.setDuration(200);
                transAnimation.setInterpolator(new AccelerateInterpolator());
                animset.addAnimation(transAnimation);
                AlphaAnimation alphInAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphInAnimation.setStartOffset((long) ((i * 200) / count));
                alphInAnimation.setDuration(100);
                alphInAnimation.setInterpolator(new AccelerateInterpolator());
                animset.addAnimation(alphInAnimation);
                ((SkyMenuItem) targetItems.get(i)).startAnimation(animset);
                if (i == count - 1) {
                    animset.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            menu.setVisibility(View.GONE);
                            SkyFirstMenu.this.endDismiss = true;
                        }
                    });
                }
            }
        }
    }

    public void resetOnKeyListener() {
        for (int j = 0; j < this.menuItems.size(); j++) {
            ((SkyMenuItem) this.menuItems.get(j)).setOnKeyListener(null);
        }
    }

    public void setMenuItemFocus(int id) {
       LogUtils.v("dzp", "back focus!!!!!  id == " + id);
        if (id == -1) {
            setMenuAdapter(this.mAdapter);
        } else if (this.menuItems != null && id < this.menuItems.size() && this.menuItems.get(id) != null) {
            setItemFocusble(false, id, true);
            ((SkyMenuItem) this.menuItems.get(id)).resetvIconBgState();
            boolean focus = ((SkyMenuItem) this.menuItems.get(id)).requestFocus();
            this.menuFocusView.initStarPosition(-ScreenParams.getInstence(this.mContext).getResolutionValue(31), ScreenParams.getInstence(this.mContext).getResolutionValue(496), ScreenParams.getInstence(this.mContext).getResolutionValue(358) + (ScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2), ScreenParams.getInstence(this.mContext).getResolutionValue(100) + (ScreenParams.getInstence(this.mContext).getResolutionValue(83) * 2));
           LogUtils.v("dzp", "focus == " + focus);
        }
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
        SkyMenuItem item;
        LayoutParams item_p;
        AnimationSet animset;
        TranslateAnimation transAnimation;
        AlphaAnimation alphInAnimation;
        AlphaAnimation alphOutAnimation;
        if (keyCode == 19) {
            for (i = itemCount - 1; i >= 0; i--) {
                item = (SkyMenuItem) this.menuItems.get(i);
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
                if (i == index - 3) {
                    alphInAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphInAnimation.setFillAfter(true);
                    alphInAnimation.setDuration(100);
                    alphInAnimation.setStartOffset((long) ((this.startOffset * i) / itemCount));
                    animset.addAnimation(alphInAnimation);
                }
                if (i == index + 2) {
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
                            if (index + 2 < itemCount && index + 2 < SkyFirstMenu.this.menuItems.size() && SkyFirstMenu.this.menuItems.get(index + 2) != null) {
                                ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(index + 2)).setVisibility(View.INVISIBLE);
                            }
                            if (index - 1 == 0) {
                                SkyFirstMenu.this.setItemFocusble(false, index - 1, true);
                                ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(index - 1)).requestFocus();
                            }
                            SkyFirstMenu.this.isAnimStart = false;
                        }
                    });
                } else if (i == index - 1) {
                    transAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            SkyFirstMenu.this.setItemFocusble(false, index - 1, true);
                            ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(index - 1)).requestFocus();
                        }
                    });
                }
            }
        } else if (keyCode == 20) {
            for (i = 0; i < itemCount; i++) {
                item = (SkyMenuItem) this.menuItems.get(i);
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
                if (i == index - 2) {
                    alphOutAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphOutAnimation.setFillAfter(true);
                    alphOutAnimation.setDuration(100);
                    alphOutAnimation.setStartOffset((long) ((this.startOffset * i) / itemCount));
                    animset.addAnimation(alphOutAnimation);
                }
                if (i == index + 3) {
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
                            if (index - 2 >= 0 && SkyFirstMenu.this.menuItems.get(index - 2) != null) {
                                ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(index - 2)).setVisibility(View.INVISIBLE);
                            }
                            if (index + 1 == itemCount - 1) {
                                SkyFirstMenu.this.setItemFocusble(false, index + 1, true);
                                ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(index + 1)).requestFocus();
                            }
                            SkyFirstMenu.this.isAnimStart = false;
                        }
                    });
                } else if (i == index + 1) {
                    transAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationStart(Animation animation) {
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationEnd(Animation animation) {
                            SkyFirstMenu.this.setItemFocusble(false, index + 1, true);
                            ((SkyMenuItem) SkyFirstMenu.this.menuItems.get(index + 1)).requestFocus();
                        }
                    });
                }
            }
        }
    }
}

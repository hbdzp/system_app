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
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
////import com.ktc.framework.ktcsdk.logger.KtcLogger;
import com.ktc.util.KtcScreenParams;
import com.ktc.util.MyFocusFrame;

import java.util.ArrayList;
import java.util.List;
////import org.apache.http4.HttpStatus;

public class KtcSecondMenu extends FrameLayout {
    public static int preViewWidth = 0;
    private LayoutParams contentParams;
    private int dataCount = 0;
    public boolean dismissBySecondLeft_back = false;
    /*isAnimStart
     * 判断二级菜单当UP和DOWN按键时动画是否开始,
     * 也就是说,isAnimStart为false:此时没有上下按键动画;isAnimStart为true时,此时按键菜单正在进行上下按键动画
     */
    private boolean isAnimStart = false;//

    /*isDismisAnimStart
     * 判断二级菜单是否执行了消失动画,false表示没有执行过，比如只显示了一级菜单和二级菜单
     * true表示执行过二级菜单动画,比如从二级菜单返回到一级菜单,这时值为true
     */
    public boolean isDismisAnimStart = false;
    private int lastFocusItemID = -1;
    private KtcMenuAdapter mAdapter;
    private OnSecondItemAnimListener mAnimListener;
    private Context mContext;
    private OnSecondMenuItemOnkeyListener mListener;
    private MyFocusFrame menuFocusView;
    private int menuHeight = KtcScreenParams.getInstence(this.mContext).getResolutionValue(630);
    private List<KtcMenuItem_2> menuItems;
    private int menuWidth = KtcScreenParams.getInstence(this.mContext).getResolutionValue(270);
    private long onKeyTime = 0;
    public boolean show_disAnimStart = false;
    private int startOffset = 160;

    /* 二级菜单最多显示7个item,对于中间的item，也即是第四个item，其数值是3(menuMiddlePostion),
     */
    private final int menuMiddlePostion = 3;
    OnTouchListener touchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() != 0) {
                return false;
            }
            if (KtcSecondMenu.this.isAnimStart) {
                return true;
            }
            for (int i = 0; i < KtcSecondMenu.this.dataCount; i++) {
                if (view.getId() != i) {
                    ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(i)).setSelectState(false);
                    ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(i)).reSetItemState();
                }
            }
            ((KtcMenuItem_2) view).setSelectState(true);
            if (KtcSecondMenu.this.mListener == null) {
                return false;
            }
            KtcSecondMenu.this.mListener.onSecondMenuItemOnClick(view.getId(), view, ((KtcMenuItem_2) view).getItemData());
            return true;
        }
    };

    public interface OnSecondMenuItemOnkeyListener {
        boolean onSecondMenuItemOnClick(int i, View view, KtcMenuData ktcMenuData);

        boolean onSecondMenuItemOnKeyBack(int id, View view);

        boolean onSecondMenuItemOnKeyLeft(int id, View view);

        boolean onSecondMenuItemOnKeyRight(int i, View view, KtcMenuData ktcMenuData);

        boolean onSecondMenuOnKeyOther(int id, View view, int keyCode);
    }
    private int itemHeight = KtcScreenParams.getInstence(mContext).getResolutionValue(90);
    private int itemMargin = KtcScreenParams.getInstence(mContext).getResolutionValue(0);
    OnKeyListener itemOnKeyListener = new OnKeyListener() {
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            Log.d("Maxs50","------->KtcSecondMenu:itemOnKeyListener:onKey:keyCode = " + keyCode);
            if (keyEvent.getAction() != 0) {
                return false;
            }
            KtcSecondMenu.this.lastFocusItemID = -1;
            int id = view.getId();
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                if (System.currentTimeMillis() - KtcSecondMenu.this.onKeyTime < 150) {
                    KtcSecondMenu.this.startOffset = 60;
                } else {
                    KtcSecondMenu.this.startOffset = 160;
                }
                KtcSecondMenu.this.onKeyTime = System.currentTimeMillis();
                if (KtcSecondMenu.this.isAnimStart) {
                    return true;
                }
                if (id == 0) {
                    return true;
                }
                KtcSecondMenu.this.menuFocusView.needAnimtion(false);
                KtcSecondMenu.this.onKeyItemMoveEvent(keyCode, view);
                if (KtcSecondMenu.this.mListener != null) {
                    KtcSecondMenu.this.mListener.onSecondMenuOnKeyOther(id, view, keyCode);
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                long currentTimeMillis = System.currentTimeMillis();
              ////KtcLogger.v("xw", "downTime == " + currentTimeMillis);
              //// KtcLogger.v("xw", "onKeyTime == " + KtcSecondMenu.this.onKeyTime);
              ////KtcLogger.v("xw", "(downTime - onKeyTime) == " + (currentTimeMillis - KtcSecondMenu.this.onKeyTime));
                if (currentTimeMillis - KtcSecondMenu.this.onKeyTime < 150) {
                    KtcSecondMenu.this.startOffset = 60;
                } else {
                    KtcSecondMenu.this.startOffset = 160;
                }
                KtcSecondMenu.this.onKeyTime = System.currentTimeMillis();
                if (KtcSecondMenu.this.isAnimStart) {
                    return true;
                }
                if (id == KtcSecondMenu.this.dataCount - 1) {
                    return true;
                }
                KtcSecondMenu.this.menuFocusView.needAnimtion(false);
                KtcSecondMenu.this.onKeyItemMoveEvent(keyCode, view);
                if (KtcSecondMenu.this.mListener != null) {
                    KtcSecondMenu.this.mListener.onSecondMenuOnKeyOther(id, view, keyCode);
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            	////KtcLogger.v("xw", "isShowAnimStart == " + KtcSecondMenu.this.isDismisAnimStart);
                if (KtcSecondMenu.this.isAnimStart) {
                    return true;
                }
                if (KtcSecondMenu.this.mListener == null) {
                    return true;
                }
                KtcSecondMenu.this.menuFocusView.needAnimtion(false);
                return KtcSecondMenu.this.mListener.onSecondMenuItemOnKeyLeft(id, view);
            } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                return KtcSecondMenu.this.isAnimStart ? true : KtcSecondMenu.this.mListener != null ? KtcSecondMenu.this.mListener.onSecondMenuItemOnKeyRight(id, view, ((KtcMenuItem_2) view).getItemData()) : true;
            } else {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return KtcSecondMenu.this.isAnimStart ? true : KtcSecondMenu.this.mListener != null ? KtcSecondMenu.this.mListener.onSecondMenuItemOnKeyBack(id, view) : false;
                } else {
                    if (keyCode != KeyEvent.KEYCODE_DPAD_CENTER && keyCode != KeyEvent.KEYCODE_ENTER) {
                        return KtcSecondMenu.this.mListener != null ? KtcSecondMenu.this.mListener.onSecondMenuOnKeyOther(id, view, keyCode) : false;
                    } else {
                        if (KtcSecondMenu.this.isAnimStart) {
                            return true;
                        }
                        for (id = 0; id < KtcSecondMenu.this.dataCount; id++) {
                            if (view.getId() != id) {
                                ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(id)).setSelectState(false);
                                ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(id)).reSetItemState();
                            }
                        }
                        ((KtcMenuItem_2) view).setSelectState(true);
                        if (KtcSecondMenu.this.mListener == null) {
                            return true;
                        }
                        KtcSecondMenu.this.mListener.onSecondMenuItemOnClick(view.getId(), view, ((KtcMenuItem_2) view).getItemData());
                        return true;
                    }
                }
            }
        }
    };


    public interface OnSecondItemAnimListener {
        void onSecondItemAnimEnd(boolean z);
    }

    public KtcSecondMenu(Context context) {
        super(context);
        this.mContext = context;
        initView();
        setFocusable(false);
    }

    private void initView() {
        this.menuItems = new ArrayList();
        this.contentParams = new LayoutParams(-2, this.menuHeight);
        setLayoutParams(this.contentParams);
    }

    /* onKeyItemMoveEvent
     * 在二级菜单上UP或者Down时,二级菜单item的上下动画效果
     *
     */
    private void onKeyItemMoveEvent(int kyeCode, View view) {
        this.isAnimStart = true;
        final int id = view.getId();
        Log.d("Maxs","<<<<<<<<<<<<<<<<<<<<<id=" + id);
        int i2 = this.itemHeight + this.itemMargin;
        final int count = this.mAdapter.getCount();
        int i3;
        KtcMenuItem_2 ktcMenuItem_2;
        LayoutParams layoutParams;
        AnimationSet animationSet;
        TranslateAnimation translateAnimation;
        AlphaAnimation alphaAnimation;

        if (kyeCode == KeyEvent.KEYCODE_DPAD_UP) {
            /* 当按键是UP时,二级菜单的Item是从下层开始动画的,并且从下到上
             *
             */
            for (i3 = count - 1; i3 >= 0; i3--) {
                ktcMenuItem_2 = (KtcMenuItem_2) this.menuItems.get(i3);
                layoutParams = (LayoutParams) ktcMenuItem_2.getLayoutParams();
                layoutParams.topMargin += i2;
                ktcMenuItem_2.setLayoutParams(layoutParams);
                ktcMenuItem_2.clearAnimation();
                animationSet = new AnimationSet(false);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-i2), 0.0f);
                translateAnimation.setStartOffset((long) ((((count - 1) - i3) * this.startOffset) / count));
                translateAnimation.setDuration(100);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation);
                if (ktcMenuItem_2.getVisibility() != View.VISIBLE) {
                    ktcMenuItem_2.setVisibility(View.VISIBLE);
                }

                /* 注意这里两个if语句,通过样机对比,其二级菜单最多只能显示5个item
                 * 这里的i3 ==id - 4类似于listview往下滑动,假设这样的时候,此时二级菜单已经显示了4个item,同时获得焦点的item(也就是高亮的item)在第三个位置(但是其值是2)，注意在最上方还有一个
                 * item(其实不可见的),这个时候按up,这是相对于listview下滑,这个i3 ==id - 4判断就是当达到这种情况时,把顶层的item显示出来
                 * 同理i3 == id + 3,是隐藏最下方的item
                 *
                 */
                if (i3 == id - 4) {
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);//显示
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(100);
                    alphaAnimation.setStartOffset((long) ((this.startOffset * i3) / count));
                    animationSet.addAnimation(alphaAnimation);
                }
                if (i3 == id + 3) {
                    alphaAnimation = new AlphaAnimation(1.0f, 0.0f);//隐藏
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(100);
                    alphaAnimation.setStartOffset((long) ((((count - 1) - i3) * this.startOffset) / count));
                    animationSet.addAnimation(alphaAnimation);
                }


                ktcMenuItem_2.startAnimation(animationSet);
                if (i3 == 0) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (id + 3 < count && KtcSecondMenu.this.menuItems.get(id + 3) != null) {
                                ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(id + 3)).setVisibility(View.INVISIBLE);
                            }
                            if (id - 1 == 0) {
                                ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(id - 1)).requestFocus();
                            }
                            KtcSecondMenu.this.isAnimStart = false;
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                } else if (i3 == id - 1) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(id - 1)).requestFocus();
                            if (id - 1 == 0) {
                                KtcSecondMenu.this.isAnimStart = false;
                            }
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                }
            }
        } else if (kyeCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            for (i3 = 0; i3 < count; i3++) {
                ktcMenuItem_2 = (KtcMenuItem_2) this.menuItems.get(i3);
                layoutParams = (LayoutParams) ktcMenuItem_2.getLayoutParams();
                layoutParams.topMargin -= i2;
                ktcMenuItem_2.setLayoutParams(layoutParams);
                ktcMenuItem_2.clearAnimation();
                animationSet = new AnimationSet(false);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) i2, 0.0f);
                translateAnimation.setStartOffset((long) ((this.startOffset * i3) / count));
                translateAnimation.setDuration(100);
                translateAnimation.setInterpolator(new AccelerateInterpolator());
                animationSet.addAnimation(translateAnimation);
                if (ktcMenuItem_2.getVisibility() != View.VISIBLE) {
                    ktcMenuItem_2.setVisibility(View.VISIBLE);
                }
                if (i3 == id - 3) {
                    alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(100);
                    alphaAnimation.setStartOffset((long) ((this.startOffset * i3) / count));
                    animationSet.addAnimation(alphaAnimation);
                }
                if (i3 == id + 4) {
                    alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setFillAfter(true);
                    alphaAnimation.setDuration(100);
                    alphaAnimation.setStartOffset((long) ((((count - 1) - i3) * this.startOffset) / count));
                    animationSet.addAnimation(alphaAnimation);
                }
                ktcMenuItem_2.startAnimation(animationSet);
                if (i3 == count - 1) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            if (id - 3 >= 0 && KtcSecondMenu.this.menuItems.get(id - 3) != null) {
                                ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(id - 3)).setVisibility(View.INVISIBLE);
                            }
                            if (id + 1 == count - 1) {
                                ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(id + 1)).requestFocus();
                            }
                            KtcSecondMenu.this.isAnimStart = false;
                        }

                        public void onAnimationRepeat(Animation animation) {
                        }

                        public void onAnimationStart(Animation animation) {
                        }
                    });
                } else if (i3 == id + 1) {
                    translateAnimation.setAnimationListener(new AnimationListener() {
                        public void onAnimationEnd(Animation animation) {
                            ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(id + 1)).requestFocus();
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

    public void refreshItemData(KtcMenuData ktcMenuData, int i) {
        if (this.menuItems != null && this.menuItems.size() >= i) {
            ((KtcMenuItem_2) this.menuItems.get(i)).refreshItemValue(ktcMenuData);
        }
    }

    public void setFocusView(MyFocusFrame myFocusFrame) {
        this.menuFocusView = myFocusFrame;
    }

    public void setLastFocusItemID(int i) {
        this.lastFocusItemID = i;
    }

    public void setMenuAdapter(final KtcFirstMenu ktcFirstMenu, KtcMenuAdapter ktcMenuAdapter, final int secondMenuIndex) {
        Log.d("Maxs50","setMenuAdapter:secondMenuIndex = " + secondMenuIndex);
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
        this.mAdapter = ktcMenuAdapter;
        if (this.mAdapter != null) {
            this.dataCount = this.mAdapter.getCount();
            Log.d("Maxs50","setMenuAdapter:dataCount = " + dataCount);
            if (this.dataCount > 0) {
                for (int i2 = 0; i2 < this.dataCount; i2++) {
                	KtcMenuItem_2 ktcMenuItem_2 = new KtcMenuItem_2(this.mContext);
                    ktcMenuItem_2.setTextAttribute(KtcScreenParams.getInstence(this.mContext).getTextDpiValue(36), -1);
                    ktcMenuItem_2.setFocusView(this.menuFocusView);
                    ktcMenuItem_2.setId(i2);
                    ktcMenuItem_2.setSelectState(false);
                    ktcMenuItem_2.setOnKeyListener(this.itemOnKeyListener);
                    ktcMenuItem_2.setOnTouchListener(this.touchListener);
                    ktcMenuItem_2.updataItemValue(this.mAdapter.getMenuItemData(i2));
                    LayoutParams layoutParams = new LayoutParams(-2, this.itemHeight);
                    layoutParams.topMargin = ((i2 + 3) - secondMenuIndex) * (this.itemHeight + this.itemMargin);
                    if (layoutParams != null) {
                        ktcMenuItem_2.setLayoutParams(layoutParams);
                    }
                    this.menuItems.add(ktcMenuItem_2);
                    addView(ktcMenuItem_2);
                    ktcMenuItem_2.setVisibility(View.INVISIBLE);
                }
                Log.d("Maxs50","---->KtcSecondMenu:setMenuAdapter:getVisibility() = " + getVisibility());
                if (getVisibility() != View.VISIBLE) {
                    setVisibility(View.VISIBLE);
                    show_dismissAnimation(ktcFirstMenu, true, secondMenuIndex, false);
                } else if (this.menuItems != null && this.menuItems.size() - 1 >= secondMenuIndex) {
                    ((KtcMenuItem_2) this.menuItems.get(secondMenuIndex)).post(new Runnable() {
                        public void run() {
                            ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(secondMenuIndex)).setSelectState(true);
                            ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(secondMenuIndex)).requestFocus();
                            ktcFirstMenu.setItemFocusble(true, 0, false);
                            ktcFirstMenu.setShowingSecondMenu(false);
                        }
                    });
                }
            }
        }
    }

    public void setMenuItemFocus(int i) {
        Log.d("Maxs","---->KtcSecondMenu:setMenuItemFocus:i = " + i);
        if (i > 0 && this.menuItems != null && i < this.menuItems.size() && this.menuItems.get(i) != null) {
        	////KtcLogger.v("lgx", "focus == " + ((KtcMenuItem_2) this.menuItems.get(i)).requestFocus());
            Log.d("Maxs","---->KtcSecondMenu:setMenuItemFocus:focus == " + ((KtcMenuItem_2) this.menuItems.get(i)).requestFocus());

        }
    }

    public void setOnSecondItemAnimListener(OnSecondItemAnimListener onSecondItemAnimListener) {
        this.mAnimListener = onSecondItemAnimListener;
    }

    public void setPreViewWidth(int i) {
        preViewWidth = i;
    }

    public void setSecondMenuOnItemOnkeyListener(OnSecondMenuItemOnkeyListener onSecondMenuItemOnkeyListener) {
        this.mListener = onSecondMenuItemOnkeyListener;
    }

    /* 二级菜单的显示和消失动画(还包括焦点框view的移动)
     *
     * 第二个参数：表示是否淡入和淡出:true:表示从左到右淡入;false:表示从右到左淡出
     * 第三个参数：表示二级菜单哪个item被选中,其值是从0开始
     * 第四个参数：表示一级菜单是否小时,false表示一级菜单不小时;true:表示一级菜单消失.这里之所以有这个是因为对于点击二级菜单跳转到第三方菜单时,一级和二级菜单都消失
     */
    public void show_dismissAnimation(KtcFirstMenu ktcFirstMenu, boolean isFadeIn, int secondMenuIndex, boolean isThird) {
        this.show_disAnimStart = true;
        final int secondMenuIndexTemp = secondMenuIndex;
        final boolean isFadeInTemp = isFadeIn;
       //// Log.d("Maxs","-------------->KtcSecondMenu:show_dismissAnimation:secondMenuIndex = " + secondMenuIndex);
        final boolean isThirdTemp = isThird;
        final KtcFirstMenu ktcFirstMenu2 = ktcFirstMenu;
        post(new Runnable() {
            public void run() {
                ////List subList = KtcSecondMenu.this.menuItems.subList(i2 + -menuMiddlePostion >= 0 ? i2 - menuMiddlePostion : 0, i2 + menuMiddlePostion <= KtcSecondMenu.this.menuItems.size() + -1 ? i2 + 4 : KtcSecondMenu.this.menuItems.size());
                ////if (subList != null) {
                ////    boolean size = subList.size();
                ////    boolean z = size ? true : size < true ? i2 < menuMiddlePostion ? i2 : true : false;
                ////    for (boolean z2 = false; z2 < size; z2++) {

                /* 这个地方是当第二级的菜单item很多时,而选中的又在中间的某一项,这里是为了截取选中的item的上下两个item，也就是显示5个view
                 * 1.itemStart = i2 + -menuMiddlePostion >= 0 ? i2 - menuMiddlePostion : 0;获得焦点view的上面两个view
                 */
            	List<KtcMenuItem_2> subList = KtcSecondMenu.this.menuItems.subList(((secondMenuIndexTemp + -menuMiddlePostion) >= 0) ? (secondMenuIndexTemp - menuMiddlePostion) : 0, (secondMenuIndexTemp + menuMiddlePostion) <= KtcSecondMenu.this.menuItems.size() + -1 ? (secondMenuIndexTemp + 4) : KtcSecondMenu.this.menuItems.size());
                ////Log.d("Maxs","---------->KtcSecondMenu:show_dismissAnimal:i2 =" + i2 );
                if (subList != null) {
                    int size = subList.size();
                    int z = (secondMenuIndexTemp > menuMiddlePostion) ? menuMiddlePostion  : secondMenuIndexTemp;//变量z应该是获得焦点viewID转化为截取后的ViewId
                    /*
                     * 这里是二级菜单的所有item的淡入和淡出动画,控制是否淡入和淡出的参数是show_dismissAnimation的第二个参数，true:表示从左到右淡入;false:表示从右到左淡出
                     * 1.在一级菜单上按Enter或者Right,则表示二级菜单从左到右淡入
                     * 2.在二级菜单上按Back或者Left，则表示二级菜单从右到左淡出
                     */
                    Log.d("Maxs50","isSeleted = " + z);
                    for (int m = 0; m < size; m++) {
                        Log.d("Maxs50","subList.get( + " + m + ").getItemData().getItemTitle() = " + subList.get(m).getItemData().getItemTitle());
                    	TranslateAnimation translateAnimation;
                    	AlphaAnimation alphaAnimation;
                        AnimationSet animationSet = new AnimationSet(false);
                        ////Log.d("Maxs","----------->KtcSecondMenu:isFadeIn1 = " + isFadeIn1);

                        if (isFadeInTemp) {//表示从左到右淡入
                            Log.d("Maxs50","isFadeInTemp = " + isFadeInTemp);
                            animationSet.setFillAfter(false);
                            translateAnimation = new TranslateAnimation((float) (-KtcSecondMenu.this.getWidth()), 0.0f, 0.0f, 0.0f);
                            alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
                            translateAnimation.setInterpolator(new AccelerateInterpolator());
                            alphaAnimation.setInterpolator(new AccelerateInterpolator());
                        } else {//表示从右到左淡出
                            Log.d("Maxs50","isFadeInTemp = " + isFadeInTemp);
                            animationSet.setFillAfter(true);
                            translateAnimation = new TranslateAnimation(0.0f, (float) (-KtcSecondMenu.this.getWidth()), 0.0f, 0.0f);
                            alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                            translateAnimation.setInterpolator(new DecelerateInterpolator());
                            alphaAnimation.setInterpolator(new DecelerateInterpolator());
                        }
                        ////HttpStatus.SC_OK = 200
                        ////translateAnimation.setStartOffset((long) ((m * HttpStatus.SC_OK) / size));
                        translateAnimation.setStartOffset((long) ((m * 200) / size));
                        translateAnimation.setDuration(200);
                        animationSet.addAnimation(translateAnimation);
                        ////HttpStatus.SC_OK = 200
                        ////alphaAnimation.setStartOffset((long) ((m * HttpStatus.SC_OK) / size));
                        alphaAnimation.setStartOffset((long) ((m * 200) / size));
                        alphaAnimation.setDuration(200);
                        animationSet.addAnimation(alphaAnimation);
                        if (m == z) {//我猜？当显示获得焦点的item2时,这时把获取焦点的framlayout移到二级菜单来,变量z应该是获得焦点viewID转化为截取后的ViewId
                            Log.d("Maxs50","isSeleted == m =" + m);
                            final int i = secondMenuIndexTemp;
                            ////final boolean z3 = z4;
                           //// final boolean z4 = z3;
                            final KtcFirstMenu ktcFirstMenu = ktcFirstMenu2;
                            animationSet.setAnimationListener(new AnimationListener() {
                                public void onAnimationEnd(Animation animation) {
                                    if (i == KtcSecondMenu.this.dataCount - 1) {
                                        KtcSecondMenu.this.show_disAnimStart = false;
                                        if (!isFadeInTemp) {
                                            Log.d("Maxs50","++++++++++++++++++++++++");
                                            KtcSecondMenu.this.setVisibility(View.INVISIBLE);//这个地方不知有什么用?
                                            KtcSecondMenu.this.isDismisAnimStart = false;
                                        }
                                    }
                                }

                                public void onAnimationRepeat(Animation animation) {
                                }

                                public void onAnimationStart(Animation animation) {

                                    if (i == KtcSecondMenu.this.dataCount - 1 && KtcSecondMenu.this.mAnimListener != null && isThirdTemp) {
                                       //// Log.d("Maxs","******11*******");
                                        KtcSecondMenu.this.mAnimListener.onSecondItemAnimEnd(isFadeInTemp);//二级菜单item动画消失后,是否隐藏所有一级二级菜单
                                    }
                                    //Log.d("Maxs","---------->KtcSecondMenu:show_dismissAnimal:isFadeIn1 = " + isFadeIn1);
                                    ////Log.d("Maxs","---------->KtcSecondMenu:show_dismissAnimal:(menuItems == null) = "  + (menuItems == null));
                                    ////Log.d("Maxs","---------->KtcSecondMenu:show_dismissAnimal:(menuItems == null) = "  + (menuItems == null));
                                    ////if (z4 && z4 && KtcSecondMenu.this.menuItems != null && KtcSecondMenu.this.menuItems.size() - 1 >= i) {
                                    if (isFadeInTemp && KtcSecondMenu.this.menuItems != null && KtcSecondMenu.this.menuItems.size() - 1 >= i) {
                                    ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(i)).setSelectState(true);
                                        ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(i)).requestFocus();
                                        int[] iArr = new int[2];
                                        ((KtcMenuItem_2) KtcSecondMenu.this.menuItems.get(i)).getLocationInWindow(iArr);
                                        int resolutionValue = KtcScreenParams.getInstence(KtcSecondMenu.this.mContext).getResolutionValue(83);
                                       //// Log.d("Maxs","<<<<<<<<---------->KtcSecondMenu:show_dismissAnimal:(onAnimationStart)" );
                                        KtcSecondMenu.this.menuFocusView.changeFocusPos(iArr[0] - resolutionValue, iArr[1] - resolutionValue, KtcScreenParams.getInstence(KtcSecondMenu.this.mContext).getResolutionValue(344) + (resolutionValue * 2), (resolutionValue * 2) + KtcScreenParams.getInstence(KtcSecondMenu.this.mContext).getResolutionValue(90));
                                        ktcFirstMenu.setItemFocusble(true, 0, false);
                                        ktcFirstMenu.setShowingSecondMenu(false);
                                    }
                                }
                            });
                        }
                        if (m == size - 1 && secondMenuIndexTemp != KtcSecondMenu.this.dataCount - 1) {
                            final boolean z5 = isThirdTemp;
                            final boolean z6 = isFadeInTemp;
                            animationSet.setAnimationListener(new AnimationListener() {
                                public void onAnimationEnd(Animation animation) {
                                    KtcSecondMenu.this.show_disAnimStart = false;
                                    if (!z6) {
                                        KtcSecondMenu.this.setVisibility(View.INVISIBLE);
                                        KtcSecondMenu.this.isDismisAnimStart = false;
                                    }
                                }

                                public void onAnimationRepeat(Animation animation) {
                                }

                                public void onAnimationStart(Animation animation) {
                                    if (KtcSecondMenu.this.mAnimListener != null && z5) {//判断一级菜单是否消失?
                                        ////Log.d("Maxs","******11*******");
                                        KtcSecondMenu.this.mAnimListener.onSecondItemAnimEnd(z6);
                                    }
                                }
                            });
                        }
                        //显示二级菜单(动画效果)
                        ((KtcMenuItem_2) subList.get(m)).setVisibility(View.VISIBLE);
                        ((KtcMenuItem_2) subList.get(m)).startAnimation(animationSet);
                    }
                }
            }
        });
    }
}

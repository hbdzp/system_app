package com.ktc.panasonichome.view;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.panasonichome.LauncherActivity;
import com.ktc.panasonichome.R;
import com.ktc.panasonichome.SimpleHomeApplication;
import com.ktc.panasonichome.SimpleHomeApplication.AppDataUIChange;
import com.ktc.panasonichome.explosionfield.ExplosionField;
import com.ktc.panasonichome.model.AppInfo;
import com.ktc.panasonichome.model.CItem;
import com.ktc.panasonichome.model.Category;
import com.ktc.panasonichome.model.Language;
import com.ktc.panasonichome.model.PocketItem;
import com.ktc.panasonichome.model.Pockets;
import com.ktc.panasonichome.utils.LocalAppHelper;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.GroupAppLayout.IGroupAppDataChangeListener;
import com.ktc.panasonichome.view.api.SkyToastView;
import com.ktc.panasonichome.view.api.SkyToastView.ShowTime;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout.PAGETYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by dinzhip on 2016/12/28.
 */
public class HomeMainPageGroup extends FrameLayout implements AppDataUIChange,
        IGroupAppDataChangeListener {

    private static ArrayList<BaseItem[]> result = new ArrayList();
    private int         a_a;
    private int         b_w;
    private int         c_w;
    private FrameLayout contentLayout;
    private int          curLeft     = 0;
    private PositionType curPosition = PositionType.VERTICAL;
    public MyFocusFrame   focusView;
    public GroupAppLayout groupAppLayout;
    private int itemMargin = 0;
    OnKeyListener itemOnkeyListener = new C02711();
    private List<BaseItem>     items;
    private LocalAppHelper     mAppHelper;
    private Context            mContext;
    private ExplosionField     mExplosionField;
    public  SkyToastView       mToastView;
    private LinearLayout       mainLayout;
    public  MyFocusFrame       smallFocusView;
    public  HomeStateBarLayout stateBarLayout;
    private LayoutParams       toastParams;

    class C02711 implements OnKeyListener {
        C02711() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (LauncherActivity.time != -1) {
                    //                    ScreenSaverMgr.getInstance().startTimeKeeper
                    // (LauncherActivity.time, TimeUnit.SECONDS);
                }
                BaseItem     item         = (BaseItem) v;
                PositionType positionType = item.getItemPositionType();
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        if (!positionType.equals(PositionType.BOTTOM)) {
                            HomeMainPageGroup.this.focusView.setImgResourse(R.drawable
                                    .home_focus_app);
                        }
                        return false;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        return !positionType.equals(PositionType.TOP);
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        LogUtils.d("dzp",item.getId()+"");
                        if (item.getId() == 0) {
                            return true;
                        }
                        int preIndex = item.getId() - 2;
                        if ((item.getItemPositionType() != PositionType.VERTICAL && item
                                .getItemPositionType() != PositionType.CENTER) || preIndex <= 0
                                || HomeMainPageGroup.this.items.get(preIndex) == null || (
                                (BaseItem) HomeMainPageGroup.this.items.get(preIndex))
                                .getItemPositionType() != PositionType.TOP) {
                            return false;
                        }
                        ((BaseItem) HomeMainPageGroup.this.items.get(preIndex)).requestFocus();
                        return true;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        LogUtils.d("dzp",item.getId()+"");
                        if (item.getId() == HomeMainPageGroup.this.items.size() - 1 ||
                                (item.getId() == HomeMainPageGroup.this.items.size() - 2 &&
                                        item.getItemPositionType() != PositionType.VERTICAL &&
                                        item.getItemPositionType() != PositionType.CENTER)) {
                            return true;
                        }
                        int nextIndex = item.getId() + 1;
                        if ((item.getItemPositionType() != PositionType.VERTICAL &&
                                item.getItemPositionType() != PositionType.CENTER) ||
                                HomeMainPageGroup.this.items.get(nextIndex) == null ||
                                ((BaseItem) HomeMainPageGroup.this.items.get(nextIndex))
                                .getItemPositionType() != PositionType.TOP) {
                            return false;
                        }
                        ((BaseItem) HomeMainPageGroup.this.items.get(nextIndex)).requestFocus();
                        return true;
                }
            }
            return false;
        }
    }

    class C02722 implements Runnable {
        C02722() {
        }

        public void run() {
            HomeMainPageGroup.this.focusView.setImgResourse(R.drawable.home_focus);
            HomeMainPageGroup.this.focusView.initStarPosition((View) HomeMainPageGroup.this.items
                    .get(0));
            if (!((BaseItem) HomeMainPageGroup.this.items.get(0)).hasFocus()) {
                ((BaseItem) HomeMainPageGroup.this.items.get(0)).requestFocus();
            }
            if (HomeMainPageGroup.this.smallFocusView.getVisibility() == View.VISIBLE) {
                HomeMainPageGroup.this.smallFocusView.setVisibility(View.INVISIBLE);
            }
            if (HomeMainPageGroup.this.focusView.getVisibility() == View.INVISIBLE) {
                HomeMainPageGroup.this.focusView.setVisibility(View.VISIBLE);
            }
        }
    }

    class C02733 implements Runnable {
        C02733() {
        }

        public void run() {
            HomeMainPageGroup.this.focusView.setImgResourse(R.drawable.home_focus);
            HomeMainPageGroup.this.focusView.initStarPosition((View) HomeMainPageGroup.this.items
                    .get(0));
            if (!((BaseItem) HomeMainPageGroup.this.items.get(0)).hasFocus()) {
                ((BaseItem) HomeMainPageGroup.this.items.get(0)).requestFocus();
            }
            if (HomeMainPageGroup.this.smallFocusView.getVisibility() == View.VISIBLE) {
                HomeMainPageGroup.this.smallFocusView.setVisibility(View.INVISIBLE);
            }
            if (HomeMainPageGroup.this.focusView.getVisibility() == View.INVISIBLE) {
                HomeMainPageGroup.this.focusView.setVisibility(View.VISIBLE);
            }
        }
    }

    public static class Model {
        public String data;
        public int    f78x;
        public int    f79y;
    }

    public enum PositionType {
        TOP,
        BOTTOM,
        CENTER,
        VERTICAL
    }

    public HomeMainPageGroup(Context context) {
        super(context);
        this.mContext = context;
        BlurBgLayout blurBgLayout = new BlurBgLayout(context);
        blurBgLayout.setPageType(PAGETYPE.FIRST_PAGE);
        addView(blurBgLayout, new LayoutParams(-1, -1));
        SimpleHomeApplication.getInstance().setAppDataUIChange(this);
        this.a_a = ScreenParams.getInstence(this.mContext).getResolutionValue(220);
        this.b_w = ScreenParams.getInstence(this.mContext).getResolutionValue(470);
        this.c_w = ScreenParams.getInstence(this.mContext).getResolutionValue(302);
        this.itemMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(30);
        this.mainLayout = new LinearLayout(this.mContext);
        this.mainLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(-1, -1);
        this.mainLayout.setPadding(0, ScreenParams.getInstence(this.mContext).getResolutionValue
                (424), 0, 0);
        addView(this.mainLayout, params);
        this.focusView = new MyFocusFrame(this.mContext, ScreenParams.getInstence(this.mContext)
                .getResolutionValue(135));
        this.focusView.setImgResourse(R.drawable.home_focus);
        this.focusView.setInterpolator(new DecelerateInterpolator());
        this.focusView.setVisibility(View.VISIBLE);
        this.focusView.setAnimDuration(200);
        this.smallFocusView = new MyFocusFrame(this.mContext, ScreenParams.getInstence(this
                .mContext).getResolutionValue(83));
        this.smallFocusView.setImgResourse(R.drawable.ui_sdk_btn_focus_shadow_no_bg);
        this.smallFocusView.setInterpolator(new DecelerateInterpolator());
        this.smallFocusView.setVisibility(View.INVISIBLE);
        addView(this.smallFocusView);
        this.stateBarLayout = new HomeStateBarLayout(this.mContext, this.smallFocusView, this
                .focusView);
        LayoutParams state_p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        state_p.gravity = 53;
        state_p.rightMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(120);
        state_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(55);
        addView(this.stateBarLayout, state_p);
        this.mExplosionField = new ExplosionField(this.mContext);
        initAppBar();
        SlowHScrollView mScrollView = new SlowHScrollView(this.mContext);
        mScrollView.setHorizontalScrollBarEnabled(false);
        this.mainLayout.addView(mScrollView, new LinearLayout.LayoutParams(-1, -2));
        this.contentLayout = new FrameLayout(this.mContext);
        LinearLayout scrollayout = new LinearLayout(this.mContext);
        scrollayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView headView = new TextView(this.mContext);
        TextView footView = new TextView(this.mContext);
        LinearLayout.LayoutParams textViewLp = new LinearLayout.LayoutParams(ScreenParams
                .getInstence(this.mContext).getResolutionValue(108), -1);
        scrollayout.addView(headView, textViewLp);
        scrollayout.addView(this.contentLayout);
        scrollayout.addView(footView, textViewLp);
        mScrollView.addView(scrollayout);
        this.items = new ArrayList();
        addView(this.focusView);
        LayoutParams field_p = new LayoutParams(-1, -1);
        addView(this.mExplosionField, field_p);
    }

    public void initAppBar() {
        this.groupAppLayout = new GroupAppLayout(this.mContext, this.focusView, this
                .smallFocusView, this.mExplosionField);
        this.groupAppLayout.setStateLayout(this.stateBarLayout);
        this.groupAppLayout.setIGroupAppDataChangeListener(this);
        LayoutParams params = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            params = new LayoutParams(new LayoutParams(-1, ScreenParams.getInstence(this
                    .mContext).getResolutionValue(240)));
        }
        params.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(149);
        addView(this.groupAppLayout, params);
    }

    public void setCustomApp(boolean hasFocus) {
        this.groupAppLayout.update(SimpleHomeApplication.getInstance().getCurAddedAppInfo(),
                hasFocus);
    }

    public void setEndFocusApp() {
        this.groupAppLayout.setFocus();
    }

    private void initFocusView_Position() {
        if (((BaseItem) this.items.get(0)).getVisibility() == View.INVISIBLE) {
            ((BaseItem) this.items.get(0)).postDelayed(new C02722(), 5000);
        } else {
            ((BaseItem) this.items.get(0)).post(new C02733());
        }
    }

    public void refreshView(List<Pockets> pocketItems, Category categoryItem, Language language) {
        this.contentLayout.removeAllViews();
        if (this.items != null) {
            this.items.clear();
        }
        this.curLeft = 0;
        initPocketItem(pocketItems, language);
        initCategoreItem(categoryItem, language, this.items.size());
    }
    public void refreshViewForKTC(List<Pockets> pocketItems) {
        this.contentLayout.removeAllViews();
        if (this.items != null) {
            this.items.clear();
        }
        this.curLeft = 0;
    }

    private void initCategoreItem(Category category, Language language, int itemSize) {
        if (category != null) {
            Stack       current = null;
            int         cx      = this.curLeft;
            List<CItem> c_items = category.citems;
            result.clear();
            if (c_items != null && c_items.size() > 0) {
                BaseItem item;
                int      i = 0;
                while (i < c_items.size()) {
                    String type = ((CItem) c_items.get(i)).layout.shape;
                    if (current != null) {
                        if (current.size() >= 2) {
                            if ("shape1".equals(type)) {
                                current.push(type);
                                cx = addResult(current, cx);
                                current = null;
                            } else if ("shape3".equals(type) || "shape4".equals(type)) {
                                i--;
                                cx = addResult(current, cx);
                                current = null;
                            } else {
                                current.push(type);
                            }
                        } else if ("shape1".equals(current.peek()) && "shape1".equals(type)) {
                            current.push(type);
                            cx = addResult(current, cx);
                            current = null;
                        } else if ("shape2".equals(current.peek()) && "shape2".equals(type)) {
                            current.push(type);
                            cx = addResult(current, cx);
                            current = null;
                        } else if ("shape2".equals(type) || "shape1".equals(type)) {
                            current.push(type);
                        } else {
                            i--;
                            cx = addResult(current, cx);
                            current = null;
                        }
                    } else if ("shape1".equals(type) || "shape2".equals(type)) {
                        current = new Stack();
                        current.push(type);
                    } else if ("shape3".equals(type) || "shape4".equals(type)) {
                        item = null;
                        if ("shape3".equals(type)) {
                            item = new BasePosterItem_V(this.mContext, this.focusView);
                            item.setItemLeft(cx);
                            item.setItemTop(0);
                            item.setPositionType(PositionType.VERTICAL);
                            cx += this.c_w + this.itemMargin;
                        } else if ("shape4".equals(type)) {
                            item = new BasePostItem_Big(this.mContext, this.focusView);
                            item.setItemLeft(cx);
                            item.setItemTop(0);
                            cx += this.b_w + this.itemMargin;
                            item.setPositionType(PositionType.CENTER);
                        }
                        result.add(new BaseItem[]{item});
                    }
                    i++;
                }
                if (current != null && current.size() > 0) {
                    cx = addResult(current, cx);
                }
                int j = 0;
                for (BaseItem[] items : result) {
                    for (BaseItem item2 : items) {
                        item2.setId(itemSize + j);
                        this.items.add(item2);
                        LayoutParams item_p = new LayoutParams(item2.getItemWidth(), item2
                                .getItemHeight());
                        item_p.topMargin = item2.getItemTop();
                        item_p.leftMargin = item2.getItemLeft();
                        System.out.println("--- left -> " + item2.getItemLeft() + "; top --> " +
                                item2.getItemTop());
                        if (item2.getItemPositionType() == null) {
                            if (item2.getItemTop() == 0) {
                                item2.setPositionType(PositionType.TOP);
                            } else {
                                item2.setPositionType(PositionType.BOTTOM);
                            }
                        }
                        item2.setOnKeyListener(this.itemOnkeyListener);
                        item2.setItemValue(((CItem) c_items.get(j)).detail.icon, item2
                                        .getItemTitle(((CItem) c_items.get(j)).detail.titleid,
                                                language),
                                ((CItem) c_items.get(j)).action.value);
                        this.contentLayout.addView(item2, item_p);
                        j++;
                    }
                }
            }
        }
    }

    private void initPocketItem(List<Pockets> pockets, Language language) {
        if (pockets != null && pockets.size() > 0) {
            int size = pockets.size();
            for (int i = 0; i < size; i++) {
                PocketItem pocketItem = (PocketItem) ((Pockets) pockets.get(i)).pocketitems.get(0);
                String shadowPicType=pocketItem.detail.foreground;
                LogUtils.d("dzp","pocketItem.detail.foreground:~~~"+shadowPicType);
                BaseItem   item       = new BasePosterItem_V(this.mContext, this.focusView,
                        shadowPicType);
                item.setPositionType(PositionType.VERTICAL);
                item.setItemValue(pocketItem.detail.mixicon, item.getItemTitle(((Pockets) pockets
                        .get(i)).titleid, language), pocketItem.action.value);
                this.items.add(item);
                LayoutParams item_p = new LayoutParams(item.getItemWidth(), item.getItemHeight());
                item_p.topMargin = 0;
                item_p.leftMargin = this.curLeft;
                item.setId(i);
                item.setItemTop(item_p.topMargin);
                item.setItemLeft(item_p.leftMargin);
                item.setPositionType(this.curPosition);
                item.setOnKeyListener(this.itemOnkeyListener);
                this.contentLayout.addView(item, item_p);
                this.curLeft = (item.getItemWidth() + item.getItemLeft()) + this.itemMargin;
                if (i == 0) {
                    initFocusView_Position();
                }
            }
        }
    }

    public int addResult(Stack<String> a, int x) {
        BaseItem[] r        = new BaseItem[a.size()];
        int        cx       = x;
        int        countB   = 0;
        int        countA   = 0;
        int        yA       = 0;
        boolean    firstIsA = "shape1".equals(a.get(0));
        for (int i = 0; i < a.size(); i++) {
            BaseItem model = null;
            String   data  = (String) a.get(i);
            if ("shape1".equals(data)) {
                model = new BasePostItem_Smail(this.mContext, this.focusView);
                if (countA == 0) {
                    model.setItemLeft(cx);
                    if (countB > 0) {
                        model.setItemTop((this.a_a * 1) + this.itemMargin);
                    } else {
                        model.setItemTop(0);
                    }
                    yA = model.getItemTop();
                } else if (countB <= 0) {
                    model.setItemLeft(cx);
                    model.setItemTop(this.b_w - (this.a_a + yA));
                } else if (countB % 2 == 0) {
                    model.setItemLeft(((this.a_a + this.itemMargin) * countB) + cx);
                    model.setItemTop(this.b_w - yA);
                } else {
                    model.setItemLeft(((this.a_a + this.itemMargin) * countB) + cx);
                    model.setItemTop(yA);
                }
                countA++;
            } else if ("shape2".equals(data)) {
                model = new BasePostItem_H(this.mContext, this.focusView);
                model.setItemLeft(((this.a_a + this.itemMargin) * countB) + cx);
                if (countA <= 0) {
                    model.setItemLeft(cx);
                }
                if (countB % 2 == 0) {
                    if (firstIsA) {
                        model.setItemTop((this.a_a * 1) + this.itemMargin);
                    } else {
                        model.setItemTop(0);
                    }
                } else if (firstIsA) {
                    model.setItemTop(0);
                } else {
                    model.setItemTop((this.a_a * 1) + this.itemMargin);
                }
                countB++;
            }
            r[i] = model;
        }
        if (countA > 0) {
            cx += ((countB + 1) * this.a_a) + (this.itemMargin * countB);
        } else {
            cx += (this.a_a * countB) + this.itemMargin;
        }
        cx += this.itemMargin;
        result.add(r);
        System.out.println("----" + cx);
        return cx;
    }

    public void onDataChange(List<AppInfo> appInfos) {
        SimpleHomeApplication.getInstance().setCurAddedAppInfo(appInfos);
        if (this.mAppHelper == null) {
            this.mAppHelper = new LocalAppHelper(this.mContext);
        }
        this.mAppHelper.saveData(appInfos);
    }

    public void removeData(int index) {
        this.groupAppLayout.removeData(index);
    }

    public void addData(int index) {
        this.groupAppLayout.addData(index);
    }

    public void toastNotEdit(String msg, ShowTime time) {
        initToast();
        this.mToastView.setTostString(msg);
        this.mToastView.showToast(ShowTime.LONGTIME, this.toastParams);
    }

    public void toastStick(String msg, ShowTime time) {
        initToast();
        this.mToastView.setTostString(msg);
        this.mToastView.showToast(ShowTime.LONGTIME, this.toastParams);
    }

    public void initToast() {
        if (this.mToastView == null) {
            this.mToastView = new SkyToastView(this.mContext);
            this.toastParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            this.toastParams.gravity = 81;
            this.toastParams.bottomMargin = ScreenParams.getInstence(this.mContext)
                    .getResolutionValue(100);
        }
    }
}

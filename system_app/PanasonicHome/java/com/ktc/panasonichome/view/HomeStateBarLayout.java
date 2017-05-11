package com.ktc.panasonichome.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.ktc.panasonichome.LauncherActivity;
import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.utils.StartApi;
import com.ktc.panasonichome.view.statebar.StateBarItem;

import java.util.ArrayList;
import java.util.List;

public class HomeStateBarLayout extends LinearLayout {
    private String STATE_DISABLE = "disable";
    private String STATE_ENABLE  = "enable";
    private MyFocusFrame bigFocusFrame;
    private StateBarItem blueControlItem;
    private StateBarItem blueItem;
    private StateBarItem diskItem;
    private MyFocusFrame focusView;
    private StateBarItem hotItem;
    private List<View>   items;
    private int lastItemId = 4;
    private ImageView    lineView;
    private Context      mContext;
    private StateBarItem netItem;
    private boolean netState = false;
    OnClickListener onClickListener = new C02741();
    OnKeyListener   onKeyListener   = new C02752();
    private StateBarItem sdItem;
    private AppIconItem  upItem;


    class C02741 implements OnClickListener {
        C02741() {
        }

        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                //TODO 处理状态栏跳转逻辑
                case 0:
//                    StartApi.getInstence(HomeStateBarLayout.this.mContext)
//                            .launchBlueContro();
                    return;
                case 1: //diskItem
                    StartApi.getInstence(HomeStateBarLayout.this.mContext)
                            .launchLocalMedia();
                    return;
                case 2: //sditem
                    StartApi.getInstence(HomeStateBarLayout.this.mContext)
                            .launchLocalMedia();
                    return;
                case 3: //hotItem
                    StartApi.getInstence(HomeStateBarLayout.this.mContext)
                            .launchNetworking();
                    return;
                case 4://netItem
                    StartApi.getInstence(HomeStateBarLayout.this.mContext)
                            .launchNetworking();
                    return;
                case 5://blueItem
                    StartApi.getInstence(HomeStateBarLayout.this.mContext).launchBlueTooth();
                    return;
                default:
                    return;
            }
        }
    }

    class C02752 implements OnKeyListener {
        C02752() {
        }

        @SuppressWarnings("ResourceType")
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (LauncherActivity.time != -1) {
                    //TODO 处理跳转到自动屏保的逻辑
                    //                    ScreenSaverMgr.getInstance().startTimeKeeper
                    // (LauncherActivity.time, TimeUnit.SECONDS);
                }
                int i;
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        return true;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        if (HomeStateBarLayout.this.upItem != null) {
                            HomeStateBarLayout.this.upItem.requestFocus();
                        }
                        HomeStateBarLayout.this.bigFocusFrame.setVisibility(View.VISIBLE);
                        HomeStateBarLayout.this.focusView.setVisibility(View.INVISIBLE);
                        for (i = 0; i < HomeStateBarLayout.this.items.size(); i++) {
                            ((View) HomeStateBarLayout.this.items.get(i)).setFocusable(false);
                        }
                        return true;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        //TODO 处理焦点逻辑
                        for (int j = 0; j < v.getId(); j++) {
                            if (((View) HomeStateBarLayout.this.items.get(j)).getVisibility() ==
                                    View.VISIBLE) {
                                for (i = 0; i < HomeStateBarLayout.this.items.size(); i++) {
                                    ((View) HomeStateBarLayout.this.items.get(i)).setFocusable
                                            (true);
                                }
                                return false;
                            }
                        }
                        return true;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if (v.getId() != HomeStateBarLayout.this.lastItemId) {
                            for (i = 0; i < HomeStateBarLayout.this.items.size(); i++) {
                                ((View) HomeStateBarLayout.this.items.get(i)).setFocusable(true);
                            }
                            break;
                        }
                        return true;
                }
            }
            return false;
        }
    }

    class C02763 implements Runnable {
        C02763() {
        }

        public void run() {
            HomeStateBarLayout.this.focusView.setVisibility(View.VISIBLE);
        }
    }

    class C02774 implements Runnable {
        C02774() {
        }

        public void run() {
            HomeStateBarLayout.this.bigFocusFrame.setVisibility(View.VISIBLE);
        }
    }

    public HomeStateBarLayout(Context context, MyFocusFrame focusView, MyFocusFrame bigFocusFrame) {
        super(context);
        this.mContext = context;
        this.focusView = focusView;
        this.bigFocusFrame = bigFocusFrame;
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(16);
        this.items = new ArrayList();
        initView();
    }

    @SuppressWarnings("ResourceType")
    private void initView() {
        LayoutParams item_p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        item_p.leftMargin = -ScreenParams.getInstence(this.mContext).getResolutionValue(8);
        this.blueControlItem = new StateBarItem(this.mContext, this.focusView);
        this.blueControlItem.setItemValue(R.drawable.bluetooth_tip, R.string
                .state_bar_blue_control, null);
        addView(this.blueControlItem);
        this.blueControlItem.setId(0);
        this.blueControlItem.setOnClickListener(this.onClickListener);
        this.blueControlItem.setOnKeyListener(this.onKeyListener);
        this.items.add(this.blueControlItem);
        this.blueControlItem.setVisibility(View.GONE);
        this.diskItem = new StateBarItem(this.mContext, this.focusView);
        this.diskItem.setItemValue(R.drawable.usb_icon, R.string.state_bar_disk, null);
        addView(this.diskItem, item_p);
        this.diskItem.setId(1);
        this.diskItem.setOnClickListener(this.onClickListener);
        this.diskItem.setOnKeyListener(this.onKeyListener);
        this.items.add(this.diskItem);
        this.diskItem.setVisibility(View.GONE);
        this.sdItem = new StateBarItem(this.mContext, this.focusView);
        this.sdItem.setItemValue(R.drawable.sd_icon, R.string.state_bar_sdcard, null);
        addView(this.sdItem, item_p);
        this.sdItem.setId(2);
        this.sdItem.setOnClickListener(this.onClickListener);
        this.sdItem.setOnKeyListener(this.onKeyListener);
        this.items.add(this.sdItem);
        this.sdItem.setVisibility(View.GONE);
        this.hotItem = new StateBarItem(this.mContext, this.focusView);
        this.hotItem.setItemValue(R.drawable.wifi_hotspot, R.string.state_bar_hot, null);
        addView(this.hotItem, item_p);
        this.hotItem.setId(3);
        this.hotItem.setOnClickListener(this.onClickListener);
        this.hotItem.setOnKeyListener(this.onKeyListener);
        this.items.add(this.hotItem);
        this.hotItem.setVisibility(View.GONE);
        this.netItem = new StateBarItem(this.mContext, this.focusView);
        addView(this.netItem, item_p);
        this.netItem.setId(4);
        this.netItem.setOnClickListener(this.onClickListener);
        this.netItem.setOnKeyListener(this.onKeyListener);
        this.items.add(this.netItem);
        this.blueItem = new StateBarItem(this.mContext, this.focusView);
        this.blueItem.setItemValue(R.drawable.bluetooth, R.string.state_bar_blue, null);
        addView(this.blueItem, item_p);
        this.blueItem.setId(5);
        this.blueItem.setOnClickListener(this.onClickListener);
        this.blueItem.setOnKeyListener(this.onKeyListener);
        this.items.add(this.blueItem);
        this.blueItem.setVisibility(View.GONE);
        this.lineView = new ImageView(this.mContext);
        this.lineView.setImageResource(R.drawable.ic_static);
        LayoutParams line_p = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        line_p.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(16);
        addView(this.lineView, line_p);
        LayoutParams line_c = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        line_c.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(36);
        TextClock timeText = new TextClock(this.mContext);
        timeText.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(36));
        timeText.setTypeface(Typeface.DEFAULT_BOLD);
        timeText.setTextColor(Color.BLACK);
        timeText.setFormat24Hour("k:mm");
        timeText.setFormat12Hour("k:mm");
        addView(timeText, line_c);
    }

    public void setBlueControlItemValue(String state) {
        state = state.split(":")[1];
        LogUtils.v("dzp", "BlueControl  state ==  " + state);
        if (state.equals(this.STATE_ENABLE)) {
            this.blueControlItem.setVisibility(View.VISIBLE);
            return;
        }
        if (this.blueControlItem.hasFocus()) {
            changeFocusView();
        }
        this.blueControlItem.setVisibility(View.GONE);
    }

    public void setBlueItemValue(String state) {
        state = state.split(":")[1];
        if (state.equals(this.STATE_DISABLE)) {
            this.lastItemId = 4;
            if (this.blueItem.hasFocus()) {
                changeFocusView();
            }
            this.blueItem.setVisibility(View.GONE);
        } else if (state.equals(this.STATE_ENABLE)) {
            this.blueItem.setVisibility(View.VISIBLE);
            this.lastItemId = 5;
        }
    }

    public void setNetValue(String state) {
        this.lastItemId = 4;
        state = state.split(":")[1];
        if (state.equals(this.STATE_DISABLE)) {
            this.netState = false;
            this.netItem.setItemValue(R.drawable.wifi_error, R.string.state_bar_net, null);
        } else if (state.equals("ethernet")) {
            this.netState = true;
            this.netItem.setItemValue(R.drawable.wired, R.string.state_bar_net, null);
        } else {
            this.netState = true;
            int count = Integer.parseInt(state.split("_")[1]);
            if (count > 2) {
                this.netItem.setItemValue(R.drawable.wifi_4, R.string.state_bar_net, null);
            } else if (count == 0) {
                this.netItem.setItemValue(R.drawable.wifi_1, R.string.state_bar_net, null);
            } else if (count == 1) {
                this.netItem.setItemValue(R.drawable.wifi_2, R.string.state_bar_net, null);
            } else if (count == 2) {
                this.netItem.setItemValue(R.drawable.wifi_3, R.string.state_bar_net, null);
            }
        }
    }

    public void setHotItemValue(String state) {
        state = state.split(":")[1];
        if (state.equals(this.STATE_DISABLE)) {
            if (this.hotItem.hasFocus()) {
                changeFocusView();
            }
            this.hotItem.setVisibility(View.GONE);
        } else if (!state.equals(this.STATE_ENABLE)) {
        } else {
            if (this.netState) {
                this.hotItem.setVisibility(View.VISIBLE);
            } else {
                this.hotItem.setVisibility(View.GONE);
            }
        }
    }

    public void setDiskValue(String state) {
        LogUtils.v("dzp", "setDiskValue state ==  " + state);
        state = state.split(":")[1];
        if (state.equals(this.STATE_DISABLE)) {
            if (this.diskItem.hasFocus()) {
                changeFocusView();
            }
            this.diskItem.setVisibility(View.GONE);
        } else if (state.equals(this.STATE_ENABLE)) {
            this.diskItem.setVisibility(View.VISIBLE);
        }
    }

    public void setSDCardValue(String state) {
        LogUtils.v("dzp", "setDiskValue state ==  " + state);
        state = state.split(":")[1];
        if (state.equals(this.STATE_DISABLE)) {
            if (this.sdItem.hasFocus()) {
                changeFocusView();
            }
            this.sdItem.setVisibility(View.GONE);
        } else if (state.equals(this.STATE_ENABLE)) {
            this.sdItem.setVisibility(View.VISIBLE);
        }
    }

    public void setMsgItemFocus(AppIconItem item) {
        int i;
        for (i = 0; i < this.items.size(); i++) {
            if (((View) this.items.get(i)).getVisibility() == View.VISIBLE) {
                ((View) this.items.get(i)).setFocusable(true);
            }
        }
        this.upItem = item;
        int size = getChildCount();
        for (i = 0; i < size; i++) {
            if (getChildAt(i).getVisibility() == View.VISIBLE) {
                getChildAt(i).requestFocus();
                this.bigFocusFrame.setVisibility(View.INVISIBLE);
                this.focusView.post(new C02763());
                return;
            }
        }
    }

    private void changeFocusView() {
        this.focusView.setVisibility(View.INVISIBLE);
        postDelayed(new C02774(), 200);
    }
}

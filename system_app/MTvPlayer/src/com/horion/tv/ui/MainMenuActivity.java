package com.horion.tv.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.FrameLayout;

import com.horion.tv.R;
import com.horion.tv.framework.MstarBaseActivity;
import com.horion.tv.framework.ui.TvIntent;
import com.horion.tv.service.menu.KtcTvMenuImple;
import com.horion.tv.util.Utility;
import com.ktc.util.KtcTVTosatView;
import com.horion.tv.service.menu.KtcTvMenuItem;

import java.util.ArrayList;
import java.util.List;
import com.horion.tv.service.menu.KtcTvMenuManager;

public class MainMenuActivity extends MstarBaseActivity {
    List<KtcTvMenuItem> firstList = new ArrayList<KtcTvMenuItem>() ;
    List<KtcTvMenuItem> secondList = new ArrayList<KtcTvMenuItem>() ;
    private FrameLayout rootMenuView;
    private KtcTvMenuImple ktcTvMenuImple;
	    private static MainMenuActivity instance = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // If MapKeyPadToIR returns true,the previous keycode has been changed
        // to responding
        // android d_pad keys,just return.
        if (MapKeyPadToIR(keyCode, event))
            return true;

        return super.onKeyDown(keyCode, event);
    }
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(TvIntent.ACTION_SIGNAL_LOCK)) {
                Log.d("Maxs55","MainMenuActivity:ACTION_SIGNAL_LOCK:");
                Intent intent1 = new Intent(TvIntent.ACTION_SOURCEINFO);
                startActivity(intent1);

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main_menu);
        instance = this;
        //new Thread(run2).start();
        rootMenuView = (FrameLayout) findViewById(R.id.rootFL);
      /// ktcTvMenuImple = new KtcTvMenuImple();
       /// ktcTvMenuImple.setRootMenuView(rootMenuView, this);
        //ktcTvMenuImple.showMainMenu();



/*
        for (int i = 0; i < 8; i++) {
            KtcTvMenuItem ktcTvMenuItem7 = new KtcTvMenuItem();
            ktcTvMenuItem7.setDisplay(false);//设置为显示
            ktcTvMenuItem7.setFlipOutStatus(false);//设置没有第二级菜单
            ktcTvMenuItem7.setContent(getString(R.string.KTC_CFG_TV_PICTURE_MODE_VIVID));
            ktcTvMenuItem7.setNodeKey(getString(R.string.KTC_CFG_TV_PICTURE_MODE_VIVID));//设置为图像模式
            secondList.add(ktcTvMenuItem7);
        }*/

        /*ImageView imageView = new ImageView(getApplicationContext());
        imageView.setBackgroundResource(R.drawable.ui_sdk_menu_leftside_shadow);
        FrameLayout.LayoutParams mShadowP = new FrameLayout.LayoutParams(-2, -1);
        mShadowP.leftMargin = KtcScreenParams.getInstence(getApplicationContext()).getResolutionValue(460);
        rootMenuView.addView(imageView, mShadowP);*/

        Log.d("Maxs", "---MainMenuActivity--");
         ////ktcTvMenuImple.setChildMenuList(secondList);
        KtcTvMenuManager.getInstance().init(this.getApplicationContext());
        KtcTvMenuManager.getInstance().loadAssetsMenufile();
        ////firstList = KtcTvMenuManager.getInstance().processKey(KtcTvControl.getInstance().getSourceNameEnum());
        KtcTvMenuManager.getInstance().onKeyMenu();
        Log.d("Maxs", "------MainActivty:firstList.size() = " + firstList.size());
        ////ktcTvMenuImple.showMainMenu(firstList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TvIntent.ACTION_SOURCE_INFO_DISAPPEAR);
        filter.addAction(TvIntent.ACTION_SIGNAL_LOCK);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("Maxs","-----MainActivity:22onDestory-------");
    }

    public static MainMenuActivity getInstance() {
        return instance;
    }

    public FrameLayout getRootMenuView(){
        Log.d("Maxs","-----MainActivity:rootMenuView == null = " + (rootMenuView == null));
        if (rootMenuView != null)
            return rootMenuView;
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent) {


        super.onNewIntent(intent);
        Log.d("Maxs","-----MainActivity:onNewIntent-------"+intent.getAction());
    }


    private void keyInjection(final int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            new Thread() {
                public void run() {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(keyCode);
                    } catch (Exception e) {
                        Log.e("Maxs90", e.toString());
                    }
                }
            }.start();
        }
    }
    public boolean MapKeyPadToIR(int keyCode, KeyEvent event) {
        String deviceName = InputDevice.getDevice(event.getDeviceId())
                .getName();
        Log.d("Maxs90", "deviceName" + deviceName);
        if (!deviceName.equals("MStar Smart TV Keypad")) {
            return false;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_CHANNEL_UP:
                keyInjection(KeyEvent.KEYCODE_DPAD_UP);
                return true;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
                return true;
            case KeyEvent.KEYCODE_TV_INPUT:
                keyInjection(KeyEvent.KEYCODE_DPAD_CENTER);
                return true;
            default:
                return false;
        }

    }
}

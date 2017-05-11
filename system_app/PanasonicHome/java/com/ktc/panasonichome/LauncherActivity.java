package com.ktc.panasonichome;

import android.app.Activity;
import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.ktc.panasonichome.model.AppInfo;
import com.ktc.panasonichome.model.HomePageData;
import com.ktc.panasonichome.model.HomePageDataCached;
import com.ktc.panasonichome.model.HomePageDataCategory;
import com.ktc.panasonichome.update.ApkUpdateUtil;
import com.ktc.panasonichome.utils.LocalAppHelper;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.AppIconItem;
import com.ktc.panasonichome.view.AppIconItem.AppType;
import com.ktc.panasonichome.view.HomeMainPageGroup;
import com.ktc.panasonichome.view.MarqueeText;
import com.ktc.panasonichome.view.api.SkyToastView;
import com.ktc.panasonichome.view.api.SkyToastView.ShowTime;
import com.ktc.panasonichome.view.ktc.TCSetData;
import com.ktc.panasonichome.view.statebar.StateBarItem;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.TvOsType.EnumInputSource;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LauncherActivity extends Activity {
    public static final  String               TAG                  = "LauncherActivity";
    //    private static final String             BLE_REMOTE_BROADCAST = "com.skyworth.broadcast
    // .bluetooth.rc";
    private static final String               BLE_REMOTE_BROADCAST = "com.ktc.broadcast.bluetooth" +
            ".rc";
    private static final String               BLE_RREMOTE_PROP_KEY = "third.get.bluetooth.rc";
    public static        View                 lastFocusView        = null;
    public static        long                 time                 = -1;
    //dzp add 2-28 for 海报 start
    private              HomePageDataCached   cachedHomePageData   = null;
    private              HomePageDataCategory homePageDataCategory = null;
    //dzp add 2-28 for 海报 end
    private BroadcastReceiver coocaaBoardcastReceiver;
    private Boolean isChange = Boolean.valueOf(false);

    private boolean isOnCreateRun = false;

    private LocalAppHelper            mAppHelper;
    private OnStatueBarNotifyListener mStatueBarListener;
    private SkyToastView              mToastView;
    private HomeMainPageGroup         mainPage;
    private MyHandler                 myHandler;
    //    private NetApiForCommon netApi     = null;
    private boolean                     netChanged   = true;
    //    ScreenSaverListener saverListener = new C02811();
    private Map<NeedRefreshKey, String> statueBarMap = new HashMap();
    private ExecutorService statusBarExcutor;


    public final static String PPPOE_STATE_ACTION     = "android.net.pppoe.PPPOE_STATE_ACTION";
    public final static String PPPOE_STATE_STATUE     = "PppoeStatus";
    public static final String PPPOE_STATE_CONNECT    = "connect";
    public static final String PPPOE_STATE_DISCONNECT = "disconnect";
    public static final String PPPOE_STATE_AUTHFAILED = "authfailed";
    public static final String PPPOE_STATE_FAILED     = "failed";

    public        int     usbDeviceCount   = 0;

    //dzp add 3-7 start
    private String  netStatus    = "network:disable";
    private boolean isNetConnect = false;
    private boolean isETHERNETConnect = false;
    private boolean isWifiConnect = false;

    //dzp add 3-7 end
    //    private TCSystemService sysApi  = null;
    //    private NetApiForWifi   wifiApi = null;
    //
    //    class C02811 implements ScreenSaverListener {
    //        C02811() {
    //        }
    //
    //        public void onTimeUp() {
    //            LauncherActivity.this.sysApi.startDream();
    //        }
    //    }

    //dpz add 2-15 for tv
    private       boolean activityIsRun        = false;
    private final String  IS_POWER_ON_PROPERTY = "mstar.launcher.1stinit";
    Handler handlertv = new Handler();
    private TvCommonManager commonService;
    WifiManager mWifiManager = null;
    // delay enableHomekey
    Runnable enable_homekey = new Runnable() {
        @Override
        public void run() {
            Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 0);
        }
    };
    //dpz add 2-15 for tv

    class BLE_REMOTE_BroadcastReceiver extends BroadcastReceiver {
        BLE_REMOTE_BroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();
            LogUtils.v("dzp", "onReceive!!!!!!!!  str ==  " + str);
            if (str == null) {
                return;
            }
            if (LauncherActivity.BLE_REMOTE_BROADCAST.equals(str)) {
                int remoteConnected = intent.getIntExtra("STATE", 0);
                LogUtils.v("dzp", "onReceive!!!!!!!!  remoteConnected ==  " + remoteConnected);
                if (remoteConnected == 1) {
                    LauncherActivity.this.mainPage.stateBarLayout.setBlueControlItemValue
                            ("blerc:enable");
                } else {
                    LauncherActivity.this.mainPage.stateBarLayout.setBlueControlItemValue
                            ("blerc:disable");
                }
                //            } else if (XPackageManager.X_ACTION_REMOVE_PACKAGE_END.equals(str)) {
                //                String packageName = intent.getStringExtra(XPackageManager
                //                        .X_ACTION_REMOVE_PACKAGE_EXTRA_PACKAGE);
                //                if (XPackageManager
                // .X_ACTION_REMOVE_PACKAGE_END_EXTRA_RESULT_SUCCESS.equals
                //                        (intent.getStringExtra(XPackageManager
                //                                .X_ACTION_REMOVE_PACKAGE_END_EXTRA_RESULT))) {
                //                    SimpleHomeApplication.getInstance().removeAppInfo
                // (packageName);
                //                    LauncherActivity.this.mAppHelper.saveData
                // (SimpleHomeApplication.getInstance()
                //                            .getCurAddedAppInfo());
                //                }
            }
        }
    }

    private static class MyHandler extends Handler {
        private final WeakReference<LauncherActivity> mActivity;

        public MyHandler(LauncherActivity activity) {
            this.mActivity = new WeakReference(activity);
        }

        public void handleMessage(Message msg) {
            LauncherActivity activity = (LauncherActivity) this.mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 0:
                        if (msg.arg1 == 1 || msg.arg1 == 2) {
                            //                            activity.cachedHomePageData = new
                            // HomePageDataCached(activity);
                            activity.homePageDataCategory = new HomePageDataCategory(activity);
                        }
                        if (msg.arg1 == 1 || msg.arg1 == 3) {
                            //                            HomePageData data = activity
                            // .cachedHomePageData.getHomePageData();
                            HomePageData data = activity.homePageDataCategory.getHomePageData();
                            if (data != null) {
                                activity.mainPage.refreshView(data.pockets, data.category, data
                                        .language);
                                return;
                            }
                            return;
                        }
                        return;
                    case 1:
                        Map<NeedRefreshKey, String> toRefreshMap = (Map<NeedRefreshKey, String>)
                                msg.obj;
                        activity.mainPage.stateBarLayout.setNetValue((String) toRefreshMap.get
                                (NeedRefreshKey.NET_KEY));
                        activity.mainPage.stateBarLayout.setHotItemValue((String) toRefreshMap
                                .get(NeedRefreshKey.HOTSPOT_KEY));
                        activity.mainPage.stateBarLayout.setBlueItemValue((String) toRefreshMap
                                .get(NeedRefreshKey.BLE_KEY));
                        activity.mainPage.stateBarLayout.setDiskValue((String) toRefreshMap.get
                                (NeedRefreshKey.USB_KEY));
                        activity.mainPage.stateBarLayout.setSDCardValue((String) toRefreshMap.get
                                (NeedRefreshKey.SDCARD_KEY));
                        activity.mainPage.stateBarLayout.setBlueControlItemValue((String)
                                toRefreshMap.get(NeedRefreshKey.BLE_REMOTE_KEY));
                        return;
                    case 2:
                        //更新添加的应用
                        activity.mainPage.setCustomApp(false);
                        return;
                    case 3:
                        activity.showToast(activity.getResources().getString(R.string
                                .local_add_tips));
                        return;
                    case 4:
                        if (activity.mainPage != null) {
                            activity.mainPage.setVisibility(View.INVISIBLE);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public enum NeedRefreshKey {
        NET_KEY,
        BLE_KEY,
        USB_KEY,
        SDCARD_KEY,
        DOWNLOADSTATUE_KEY,
        HOTSPOT_KEY,
        BLE_REMOTE_KEY
    }

    public interface OnStatueBarNotifyListener {
        void onStatueBarDataChanged(Map<NeedRefreshKey, String> map);
    }

    private void initData() {
        if (LauncherActivity.this.myHandler == null) {
            LauncherActivity.this.myHandler = new MyHandler(LauncherActivity.this);
        }
        Message mes = new Message();
        mes.what = 0;
        mes.arg1 = 1;
        LauncherActivity.this.myHandler.sendMessage(mes);
        List<String>  addedPackages = LauncherActivity.this.mAppHelper.getAllData();
        List<AppInfo> addedInfos    = new ArrayList();
        if (addedPackages != null) {
            for (String appDataString : addedPackages) {
                AppInfo appInfo = LauncherActivity.this.mAppHelper.getInstalledAppInfo(LauncherActivity
                        .this, appDataString);
                boolean apkExist = LauncherActivity.this.mAppHelper.checkApkExist(LauncherActivity.this, appInfo
                        .getPackageName());
                if(apkExist){
                    addedInfos.add(LauncherActivity.this.mAppHelper.getInstalledAppInfo
                            (LauncherActivity.this, appDataString));
                }
            }
        }
        SimpleHomeApplication.getInstance().setCurAddedAppInfo(addedInfos);
        LauncherActivity.this.mAppHelper.saveData(addedInfos);
        if (LauncherActivity.this.myHandler.hasMessages(2)) {
            LauncherActivity.this.myHandler.removeMessages(2);
        }
        LauncherActivity.this.myHandler.sendMessage(LauncherActivity.this.myHandler
                .obtainMessage(2));
        if (LauncherActivity.this.statusBarExcutor != null && !LauncherActivity.this
                .statusBarExcutor.isShutdown()) {
            LauncherActivity.this.statusBarExcutor.execute(new StatusBarRunnable
                    (LauncherActivity.this));
        }
    }

    private class StatusBarRunnable implements Runnable {
        private WeakReference<LauncherActivity> mContext;

        public StatusBarRunnable(LauncherActivity context) {
            this.mContext = new WeakReference(context);
        }

        public void run() {
            if (this.mContext != null && this.mContext.get() != null && !LauncherActivity.this
                    .isFinishing()) {
                try {
                    final Map<NeedRefreshKey, String> toRefreshMap = ((LauncherActivity) this
                            .mContext.get()).getStateBarStatus();
                    for (NeedRefreshKey key : toRefreshMap.keySet()) {
                        LogUtils.v("dzp", "key".toString() + ((String) toRefreshMap.get(key)));
                    }
                    Message msg = LauncherActivity.this.myHandler.obtainMessage(1);
                    msg.obj = toRefreshMap;
                    LauncherActivity.this.myHandler.sendMessage(msg);
                    if (((LauncherActivity) this.mContext.get()).mStatueBarListener != null) {
                        ((LauncherActivity) this.mContext.get()).runOnUiThread(new Runnable() {
                            public void run() {
                                ((LauncherActivity) StatusBarRunnable.this.mContext.get())
                                        .mStatueBarListener.onStatueBarDataChanged(toRefreshMap);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private enum SKY_SYSTEM_ENV_START_SCREEN_SAVER_MODE_ENUM_TYPE {
        FIVE_MIN,
        ONE_MIN,
        TEN_MIN,
        THIRTY_MIN,
        TWO_MIN,
        NONE
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("dzp", ">ZC<  onCreate!!!!!!!!");
        FrameLayout mainLayout = new FrameLayout(this);
        mainLayout.setLayoutParams(new LayoutParams(-1, -1));
        setContentView(mainLayout);
        this.mainPage = new HomeMainPageGroup(this);
        mainLayout.addView(this.mainPage);
        this.mAppHelper = new LocalAppHelper(this);
        this.statusBarExcutor = Executors.newSingleThreadExecutor();
        //dzp add 1-18   begin
        //初始化监听
        mWifiManager=(WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        getNetState();  //位置不要变
        initData();
        registNetReceiver();
        registUsbReceiver();
        registApkFreshReceiver();
        //dzp add 1-18   end
        this.isOnCreateRun = true;
        //dzp add 2-15 for tv--start
        commonService = TvCommonManager.getInstance();
        //dzp add 2-15 for tv--end
    }

    protected void onResume() {
        super.onResume();
		Log.d("Maxs16","onResume:" + SystemProperties.getBoolean("persist.sys.oobeset",
                        false));
		if (!SystemProperties.getBoolean("persist.sys.oobeset",
                        false)){
			ComponentName componentName = new ComponentName(
				"com.android.tv.settings", "com.android.setting.guidesetting.pss.PssGuideWelcome");
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setComponent(componentName);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			startActivity(intent);
		}
        LogUtils.d("dzp", ">ZC< Oversea Home onResume");
        if (this.mainPage != null) {
            if (lastFocusView != null && lastFocusView.getVisibility() == View.VISIBLE) {
                lastFocusView.requestFocus();
            }
            if (this.myHandler != null) {
                this.myHandler.removeMessages(4);
            }
            this.mainPage.setVisibility(View.VISIBLE);
        }
        if (SimpleHomeApplication.getInstance().getDataChanedFlag() && this.mainPage != null) {
            if (this.myHandler != null) {
                Message msg = new Message();
                msg.what = 3;
                this.myHandler.sendMessageDelayed(msg, 200);
            }
            this.mainPage.setEndFocusApp();
            this.isChange = Boolean.valueOf(true);
            SimpleHomeApplication.getInstance().setDataChangedFlag(false);
        }
        if (!(this.statusBarExcutor == null || this.statusBarExcutor.isShutdown() || this
                .isOnCreateRun)) {
            this.statusBarExcutor.execute(new StatusBarRunnable(this));
        }
        if (!this.isOnCreateRun) {
            if (this.myHandler == null) {
                this.myHandler = new MyHandler(this);
            }
            Message message = new Message();
            message.what = 0;
            message.arg1 = 2;
            this.myHandler.sendMessage(message);
        }
        if (this.isOnCreateRun) {
            this.isOnCreateRun = false;
        }
        if (time != -1) {
            //            ScreenSaverMgr.getInstance().startTimeKeeper(time, TimeUnit.SECONDS);
        }
        //dzp add 2-15 for tv
        //开机自动进tv
        //        if (isPowerOn() == true ) {
        //            ComponentName componentName = new ComponentName(
        //                    "com.mstar.tv.tvplayer.ui", "com.mstar.tv.tvplayer.ui.RootActivity");
        //            Intent intent = new Intent(Intent.ACTION_MAIN);
        //            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        //            intent.setComponent(componentName);
        //            intent.putExtra("isPowerOn", true);
        //            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
        //                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        //            LauncherActivity.this.startActivity(intent);
        //            handlertv.postDelayed(enable_homekey, 800);
        //        }
        //2015.3.5 zjf add end
        // mDisplayManager.disconnectWifiDisplay();
        // 设置不待机
        try {
            TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOffStatus");
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
        SystemProperties.set("mstar.str.storage", "1");
        commonService.setInputSource(EnumInputSource.E_INPUT_SOURCE_STORAGE);

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = activityManager
                .getRunningServices(100);
        for (int i = 0; i < runningServiceInfos.size(); i++) {
            ActivityManager.RunningServiceInfo runningItem = runningServiceInfos
                    .get(i);
            if ("com.android.music.MediaPlaybackService"
                    .equals(runningItem.service.getClassName())) {
                Intent intent = new Intent();
                intent.setAction("com.android.music.musicservicecommand.pause");
                intent.putExtra("command", "pause");
                sendBroadcast(intent);
                break;
            }
        }

        activityIsRun = true;
        //禁止home键
//        Settings.System.putInt(getContentResolver(), "home_hot_key_disable", 1);
//        //dzp add 2-15 for tv
    }

    protected void onPause() {
        super.onPause();
        LogUtils.d("dzp", "onPause!!!!!!!!");
        if (this.mToastView != null && this.mToastView.isShowing()) {
            this.mToastView.dismiss();
        }
        if (this.mainPage.mToastView != null && this.mainPage.mToastView.isShowing()) {
            this.mainPage.mToastView.dismiss();
        }
        if (!SimpleHomeApplication.getInstance().isStartLocalAppGrid) {
            if (this.myHandler == null) {
                this.myHandler = new MyHandler(this);
            }
            //            this.myHandler.sendEmptyMessageDelayed(4, 500);
            //            ScreenSaverMgr.getInstance().stopTimeKeeper();
        }
        if (isFinishing()) {
            LogUtils.v("dzp", "onPause + isFinishing");
            lastFocusView = null;
            //            if (this._cmdInstance != null) {
            //                this._cmdInstance.onDestory();
            //                this._cmdInstance = null;
            //            }
            if (!(this.statusBarExcutor == null || this.statusBarExcutor.isShutdown())) {
                this.statusBarExcutor.shutdown();
            }
            try {
                if (this.coocaaBoardcastReceiver != null) {
                    unregisterReceiver(this.coocaaBoardcastReceiver);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        //dzp add 2-15 for tv api--start
        activityIsRun = false;
        //使能home键
        handlertv.postDelayed(enable_homekey, 800);
        //dzp add 2-15 for tv api--end

    }

    protected void onDestroy() {
        LogUtils.v("dzp", "onDestroy!!!!!!!!");
        if (!(this.statusBarExcutor == null || this.statusBarExcutor.isShutdown())) {
            lastFocusView = null;
            this.statusBarExcutor.shutdown();
        }
        this.mAppHelper.close();
        unregisterReceiver(mNetReceiver);
        unregisterReceiver(mUsbReceiver);
        unregisterReceiver(apkFreshReceiver);
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.d("dzp", "LauncherActivity onKeyDown:" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME:
                //跳转到当前任务列表
                //                Intent itt2 = new Intent();
                //                itt2.setPackage("com.ktc.taskmanager");
                //                itt2.setAction("com.ktc.taskmanager.recent.action
                // .TOGGLE_RECENTS");
                //                if (getPackageManager().resolveActivity(itt2, 0) != null) {
                //                    try {
                //                        itt2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //                        startActivity(itt2);
                //                    } catch (Exception e) {
                //                        e.printStackTrace();
                //                    }
                //                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                //                if (SkySystemUtil.isBackToLauncher(this)) {
                //                    return true;
                //                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        LogUtils.d("dzp", "onWindowFocusChanged hasFocus:"+hasFocus);
        if (hasFocus) {
            if (time != -1) {
                //                ScreenSaverMgr.getInstance().startTimeKeeper(time, TimeUnit
                // .SECONDS);
            }
            if (this.mainPage != null && lastFocusView != null && lastFocusView.getVisibility()
                    == View.VISIBLE) {
                if ((lastFocusView instanceof StateBarItem) || (lastFocusView instanceof
                        MarqueeText)) {
                    LogUtils.d("dzp", "lastFocusView instanceof StateBarItem");
                    this.mainPage.focusView.setVisibility(View.INVISIBLE);
                    this.mainPage.smallFocusView.setVisibility(View.VISIBLE);
                    lastFocusView.setBackgroundColor(-1);
                    return;
                } else if (lastFocusView instanceof AppIconItem) {
                    LogUtils.d("dzp", "onWindowFocusChanged  lastFocusView AppIconItem");
                    if (((AppIconItem) lastFocusView).getAppType().equals(AppType.ADDICON) && this
                            .isChange
                            .booleanValue()) {
                        LogUtils.d("dzp", "onWindowFocusChanged  lastFocusView AppIconItem " +
                                "ADDICON ");
                        this.mainPage.focusView.setVisibility(View.INVISIBLE);
                        this.isChange = Boolean.valueOf(false);
                    } else {
                        this.mainPage.focusView.setVisibility(View.VISIBLE);
                    }
                    this.mainPage.smallFocusView.setVisibility(View.INVISIBLE);
                    return;
                } else {

                    LogUtils.d("dzp", "onWindowFocusChanged  lastFocusView  else");
                    this.mainPage.smallFocusView.setVisibility(View.INVISIBLE);
                    this.mainPage.focusView.setVisibility(View.VISIBLE);
                    return;
                }
            }
            return;
        }
        //        ScreenSaverMgr.getInstance().stopTimeKeeper();
        if (this.mainPage != null) {
            this.mainPage.focusView.setVisibility(View.INVISIBLE);
            this.mainPage.smallFocusView.setVisibility(View.INVISIBLE);
            if (lastFocusView == null) {
                return;
            }
            if ((lastFocusView instanceof StateBarItem) || (lastFocusView instanceof MarqueeText)) {
                lastFocusView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private int getScreensaverTimeInMs(TCSetData setData) {
        if (setData == null) {
            return -1;
        }
        int screensaverTime = 0;
        //        switch (m48x5406a33()[SKY_SYSTEM_ENV_START_SCREEN_SAVER_MODE_ENUM_TYPE.valueOf((
        //                (TCEnumSetData) setData).getCurrent()).ordinal()]) {
        //            case 1:
        //                screensaverTime = 300;
        //                break;
        //            case 2:
        //                screensaverTime = 60;
        //                break;
        //            case 3:
        //                screensaverTime = 600;
        //                break;
        //            case 4:
        //                screensaverTime = 1800;
        //                break;
        //            case 5:
        //                screensaverTime = 120;
        //                break;
        //            default:
        //                screensaverTime = -1;
        //                break;
        //        }
        return screensaverTime;
    }

    public void setStatueBarNotifyListener(OnStatueBarNotifyListener listener) {
        this.mStatueBarListener = listener;
    }

    public boolean isNetConnected() {
        //        if (this.netApi == null || !this.netChanged) {
        //            return this.isConnect;
        //        }
        //        this.isConnect = this.netApi.isConnect();
        //        this.netChanged = false;
        //        return this.;
        return isNetConnect;
    }

    public String getQueryUrl() {
        String defaultentry  = "http://api.home.hw.skysrt.com";
        String defaultdomain = "/Framework/tvos/getPanel.php";
        //        String requesturl    = "";
        //        String entry         = SkyGeneralProperties.getProperty(GeneralPropKey
        // .CURRENT_HOME_SERVER);
        //        if (entry == null || entry.length() <= 0) {
        //            entry = defaultentry;
        //        } else if (!entry.startsWith("http")) {
        //            entry = "http://" + entry;
        //        }
        //        requesturl = (requesturl + entry) + defaultdomain;
        //        if (this.cachedHomePageData == null) {
        //            return requesturl;
        //        }
        //        String fileMd5 = this.cachedHomePageData.getMd5();
        //        if (fileMd5 == null) {
        //            return requesturl;
        //        }
        //        return requesturl + ("?_md5=" + fileMd5);
        return defaultentry + defaultdomain;
    }


    private Map<NeedRefreshKey, String> getStateBarStatus() {
        this.statueBarMap.put(NeedRefreshKey.NET_KEY, networkStatus());
        this.statueBarMap.put(NeedRefreshKey.BLE_KEY, bluetoothStates());
        for (String strokey : strogeState()) {
            if (strokey.contains("usb")) {
                this.statueBarMap.put(NeedRefreshKey.USB_KEY, strokey);
            } else if (strokey.contains("sdcard")) {
                this.statueBarMap.put(NeedRefreshKey.SDCARD_KEY, strokey);
            }
        }
        this.statueBarMap.put(NeedRefreshKey.DOWNLOADSTATUE_KEY, downloadState());
        this.statueBarMap.put(NeedRefreshKey.HOTSPOT_KEY, networkHotspotStatus());
        this.statueBarMap.put(NeedRefreshKey.BLE_REMOTE_KEY, currentBleRemote());
        return this.statueBarMap;
    }

    private String networkStatus() {
        //        if (!(this.netApi == null || this._cmdInstance == null || !Boolean.valueOf(this
        // .netApi
        //                .isConnect()).booleanValue())) {
        //            String connectStatusStr = this.netApi.getNetType();
        //            if (connectStatusStr != null && connectStatusStr.trim().toUpperCase().equals
        //                    ("ETHERNET")) {
        //                return "network:ethernet";
        //            }
        //            if (connectStatusStr != null && connectStatusStr.trim().toUpperCase()
        // .equals("WIFI")) {
        //                int quality = 2;
        //                try {
        //                    quality = this.wifiApi.getWifiRssi();
        //                    if (quality < 0) {
        //                        quality = 0;
        //                    } else if (quality > 3) {
        //                        quality = 3;
        //                    }
        //                } catch (Exception e) {
        //                    e.printStackTrace();
        //                }
        //                return "network:wifi_" + quality;
        //            }
        //        }
//        return "network:disable";
        return  netStatus;
    }

    private String bluetoothStates() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return "bluetooth:disable";
        }
        return "bluetooth:enable";
    }

    private List<String> strogeState() {
        ArrayList<String> strogeList = new ArrayList(2);
        StorageManager storageManager = (StorageManager) this
                .getSystemService(Context.STORAGE_SERVICE);
        StorageVolume[] volumes = storageManager.getVolumeList();
        if (volumes != null && volumes.length > 0) {
            String path               = "";
            int    mountedDeviceCount = 0;
            // List all your mount disk
            for (StorageVolume item : volumes) {
                path = item.getPath();
                String state = storageManager.getVolumeState(path);
                // Mount is not successful
                if (state != null && state.equals(Environment.MEDIA_MOUNTED)) {
                    mountedDeviceCount++;
                }
                if(path.startsWith("/storage/emulated/0")){
//                    strogeList.add("sdcard:enable");
                     strogeList.add("sdcard:disable");
                }
            }
            usbDeviceCount = mountedDeviceCount;
        }
        if(usbDeviceCount<1){
            strogeList.add("sdcard:disable");
            strogeList.add("usb:disable");
        }else if(usbDeviceCount==1&&strogeList.size()==1){
            strogeList.clear();
//            strogeList.add("sdcard:enable");
              strogeList.add("sdcard:disable");
              strogeList.add("usb:disable");
        }
        if(usbDeviceCount>=2){
            strogeList.clear();
//            strogeList.add("sdcard:enable");
            strogeList.add("sdcard:disable");
            strogeList.add("usb:enable");
        }
        return strogeList;
    }

    private String downloadState() {
        //        if (this.loadApi == null || this._cmdInstance == null) {
        //            return "download:disable";
        //        }
        //        if (this.loadApi.getDownloadPercent() > 0) {
        //            return "download:enable";
        //        }
        return "download:disable";
    }

    private String networkHotspotStatus() {
      if(mWifiManager!=null){
          if(mWifiManager.getWifiApState()==WifiManager.WIFI_AP_STATE_ENABLED){
              return "Hotspot:enable";
          }
      }
        return "Hotspot:disable";

        //        if (this.wifiApi == null || this._cmdInstance == null) {
        //            return "Hotspot:disable";
        //        }
        //        if (this.wifiApi.getHotspotState() == HotspotState.AP_STATE_ENABLED) {
        //            return "Hotspot:enable";
        //        }
//        return "Hotspot:disable";
    }

    private boolean isDeviceSupportBleRemote() {
        //        String blesupport = SkyGeneralProperties.getProperty(GeneralPropKey
        // .SUPPORT_BLE_REMOTE);
        //        if (blesupport == null || !"true".equals(blesupport)) {
        //            return false;
        //        }
        //        return true;
        return false;
    }

    private boolean isBleRemoteConnected() {
        //        if (!isDeviceSupportBleRemote()) {
        //            return false;
        //        }
        //        if (UserKeyDefine.VALUE_THIRD_ACCOUNT_TRUE.equals(SkySystemProperties.getProperty
        //                (BLE_RREMOTE_PROP_KEY))) {
        //            return true;
        //        }
        return false;
    }

    private String currentBleRemote() {
        if (!isDeviceSupportBleRemote()) {
            return "blerc:none";
        }
        if (isBleRemoteConnected()) {
            return "blerc:enable";
        }
        return "blerc:disable";
    }

    public HashMap<String, String> getHomeWebData() {
        //        return CommonHeader.getInstance().getCommonHeaderMap(this, null);

        return null;
    }


    public void showToast(String msg) {
        FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(LayoutParams
                .WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lParams.gravity = 81;
        lParams.bottomMargin = ScreenParams.getInstence(this).getResolutionValue(25);
        this.mToastView = new SkyToastView(this);
        this.mToastView.setTostString(msg);
        if (!isFinishing()) {
            this.mToastView.showToast(ShowTime.LONGTIME, lParams);
        }
    }

    public void downloadSuccess() {
        if (this.myHandler == null) {
            this.myHandler = new MyHandler(this);
        }
        Message message = new Message();
        message.what = 0;
        message.arg1 = 3;
        this.myHandler.sendMessage(message);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getAction()==KeyEvent.ACTION_DOWN){
            if(event.getKeyCode()==KeyEvent.KEYCODE_TV_INPUT){
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    //dzp add for keyevent degbug end

    //dzp add  for listener  begin*/

    /**
     * 注册网络状态改变广播
     *
     * @ param
     * @ return void
     */
    private void registNetReceiver() {
        // ethernet status changed
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        // wifi status changed
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        // pppoe status changed
        intentFilter.addAction(PPPOE_STATE_ACTION);
        registerReceiver(mNetReceiver, intentFilter);
    }

    private void registUsbReceiver() {
        IntentFilter usbFilter = new IntentFilter();
        usbFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        usbFilter.addAction(Intent.ACTION_MEDIA_EJECT);
        usbFilter.addDataScheme("file");
        registerReceiver(mUsbReceiver, usbFilter);
    }

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_MEDIA_MOUNTED)||action.equals(Intent
                    .ACTION_MEDIA_EJECT)) {
                final Map<NeedRefreshKey, String> toRefreshMap = LauncherActivity.this.getStateBarStatus();
                for (NeedRefreshKey key : toRefreshMap.keySet()) {
                    LogUtils.v("dzp", "key".toString() + ((String) toRefreshMap.get(key)));
                }
                Message msg = LauncherActivity.this.myHandler.obtainMessage(1);
                msg.obj = toRefreshMap;
                LauncherActivity.this.myHandler.sendMessage(msg);
            }
        }
    };
    /**
     * 网络状态发生改变的广播
     */
    private BroadcastReceiver mNetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.i("dzp", "------mNetReceiver");
            String action = intent.getAction();
            Log.i("dzp", "------action:   " + action);
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) { // for android MM
                getNetState();
            } else if (action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)
                    || action.equals(WifiManager.RSSI_CHANGED_ACTION)
                    || action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                getNetState();
            } else if (action.equals(PPPOE_STATE_STATUE)) {
                getNetState();
            }
            final Map<NeedRefreshKey, String> toRefreshMap = LauncherActivity.this.getStateBarStatus();
            for (NeedRefreshKey key : toRefreshMap.keySet()) {
                LogUtils.v("dzp", "key".toString() + ((String) toRefreshMap.get(key)));
            }
            Message msg = LauncherActivity.this.myHandler.obtainMessage(1);
            msg.obj = toRefreshMap;
            LauncherActivity.this.myHandler.sendMessage(msg);
        }
    };

    public void getNetState() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null&&networkInfo.isConnected()&&networkInfo.isAvailable()) {
            isNetConnect=true;
            if(networkInfo.getType()==ConnectivityManager.TYPE_ETHERNET&&networkInfo.isAvailable()){
                isETHERNETConnect=true;
                netStatus = "network:ethernet";
            }else if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
                int i = WifiManager.calculateSignalLevel(mWifiManager
                        .getConnectionInfo()
                        .getRssi(), 4);
                netStatus = "network:wifi_"+i;
            }
        }else {
            isNetConnect=false;
            netStatus = "network:disable";
        }
    }


    //dzp add  for listener  end
    //dzp add 2-15 for tv--start
    //lixq 20151119 start
    public boolean isPowerOn() {
        Log.d(TAG, "Is Fist Power On: " + (SystemProperties.getBoolean(IS_POWER_ON_PROPERTY,
                false)));

        if (!SystemProperties.getBoolean(IS_POWER_ON_PROPERTY, false)) {
            SystemProperties.set(IS_POWER_ON_PROPERTY, "true");
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onStart() {
        /**
         * apk升级程序调用，需要设置升级URL的属性名称
         * 属性名称
         * ro.product. + apk应用名称+.update
         * 例如Launcher：ro.product.launcher.update
         */
        new ApkUpdateUtil(this,"ro.product.launcher.update").update();
        super.onStart();
    }

    /**
     * 系统卸载和安装后页面刷新的广播
     */
    private BroadcastReceiver apkFreshReceiver  = new BroadcastReceiver(){

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            if(action.equals(Intent.ACTION_PACKAGE_ADDED) ||action.equals(Intent.ACTION_PACKAGE_REMOVED)){
                //添加刷新动作
                List<String>  addedPackages = LauncherActivity.this.mAppHelper.getAllData();
                List<AppInfo> addedInfos    = new ArrayList();
                if (addedPackages != null) {
                    for (String appDataString : addedPackages) {
                        AppInfo appInfo = LauncherActivity.this.mAppHelper.getInstalledAppInfo(LauncherActivity
                                .this, appDataString);
                        boolean apkExist = LauncherActivity.this.mAppHelper.checkApkExist(LauncherActivity.this, appInfo
                                .getPackageName());
                        if(apkExist){
                            addedInfos.add(LauncherActivity.this.mAppHelper.getInstalledAppInfo
                                    (LauncherActivity.this, appDataString));
                        }
                    }
                }
                SimpleHomeApplication.getInstance().setCurAddedAppInfo(addedInfos);
                LauncherActivity.this.mAppHelper.saveData(addedInfos);
                if (LauncherActivity.this.myHandler.hasMessages(2)) {
                    LauncherActivity.this.myHandler.removeMessages(2);
                }
                LauncherActivity.this.myHandler.sendMessage(LauncherActivity.this.myHandler
                        .obtainMessage(2));
            }
        }

    };

    private void registApkFreshReceiver() {
        IntentFilter apkFreshFilter = new IntentFilter();
        apkFreshFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        apkFreshFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        apkFreshFilter.addDataScheme("package");
        this.registerReceiver(apkFreshReceiver, apkFreshFilter);
    }
}

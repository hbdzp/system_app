package com.ktc.panasonichome;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.ktc.panasonichome.model.AppInfo;
import com.ktc.panasonichome.utils.LocalAppHelper;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.api.SkyWithBGLoadingView;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout;
import com.ktc.panasonichome.view.blurbg.BlurBgLayout.PAGETYPE;
import com.ktc.panasonichome.view.customview.app.CustomAppGridView;
import com.ktc.panasonichome.view.customview.app.CustomAppGridView.OnAppGridItemOnClikListener;
import com.ktc.panasonichome.view.customview.app.CustomAppInfo;
import com.ktc.panasonichome.view.customview.app.CustomAppItemView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinzhip on 2016/12/28.
 */


public class LocalAppGridActivity extends Activity implements OnAppGridItemOnClikListener {
    private CustomAppGridView    appGridLayout;
    private LocalAppHelper       mAppHelper;
    private SkyWithBGLoadingView mLoadingView;
    private FrameLayout          mainLayout;
    private MyHandler            myHandler;

    private static class MyHandler extends Handler {
        private final WeakReference<LocalAppGridActivity> mActivity;

        public MyHandler(LocalAppGridActivity activity) {
            this.mActivity = new WeakReference(activity);
        }

        public void handleMessage(Message msg) {
            LocalAppGridActivity activity = (LocalAppGridActivity) this.mActivity.get();
            if (activity != null && msg.what == 0) {
                List<AppInfo> addedInfos = SimpleHomeApplication.getInstance().getCurAddedAppInfo();
                List<AppInfo> localInfos = activity.mAppHelper.getInstalledAppList(activity);
                activity.appGridLayout = new CustomAppGridView(activity);
                activity.appGridLayout.setOnAppGridItemOnClikListener(activity);
                activity.mLoadingView.dismissLoading();
                activity.mainLayout.addView(activity.appGridLayout);
                List<CustomAppInfo> appList = new ArrayList();
                for (int i = 0; i < localInfos.size(); i++) {
                    AppInfo mInfo     = (AppInfo) localInfos.get(i);
                    CustomAppInfo  obj       = null;
                    boolean isChoosed = false;
                    for (int j = 0; j < addedInfos.size(); j++) {
                        if (((AppInfo) addedInfos.get(j)).packageName.equals(mInfo.packageName)) {
                            isChoosed = true;
                            obj = new CustomAppInfo(mInfo.appName, mInfo.appDataString, mInfo
                                    .appIcon, true);
                            break;
                        }
                    }
                    if (!isChoosed) {
                        obj = new CustomAppInfo(mInfo.appName, mInfo.appDataString, mInfo
                                .appIcon, false);
                    }
                    appList.add(obj);
                }
                activity.appGridLayout.refreshValue(appList);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainLayout = new FrameLayout(this);
        BlurBgLayout blurBgLayout = new BlurBgLayout(this);
        blurBgLayout.setPageType(PAGETYPE.SECONDE_PAGE);
        this.mainLayout.addView(blurBgLayout, new LayoutParams(-1, -1));
        this.mLoadingView = new SkyWithBGLoadingView(this);
        LayoutParams loading_p = new LayoutParams(ScreenParams.getInstence(this)
                .getResolutionValue(125), ScreenParams.getInstence(this).getResolutionValue
                (125));
        loading_p.gravity = 17;
        this.mainLayout.addView(this.mLoadingView, loading_p);
        this.mLoadingView.showLoading();
        this.mAppHelper = new LocalAppHelper(this);
        setContentView(this.mainLayout);
        this.myHandler = new MyHandler(this);
        this.myHandler.sendEmptyMessageDelayed(0, 200);
    }

    protected void onPause() {
        super.onPause();
        SimpleHomeApplication.getInstance().isStartLocalAppGrid = false;
    }

    protected void onStop() {
        super.onStop();
        this.appGridLayout.setOnAppGridItemOnClikListener(null);
        SimpleHomeApplication.getInstance().isStartLocalAppGrid = false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4 || keyCode == 3) {
            this.mAppHelper.saveData(SimpleHomeApplication.getInstance().getCurAddedAppInfo());
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onGridItemClickEvent(View view, CustomAppInfo curInfo, int position) {
        List<AppInfo>     curAddedInfos = SimpleHomeApplication.getInstance().getCurAddedAppInfo();
        CustomAppItemView gridView      = (CustomAppItemView) view;

        //        MyAppData         myAppData     = null;
        //        if (!(curInfo == null || TextUtils.isEmpty(curInfo.tag))) {
        //            myAppData = (MyAppData) JObject.parseJObject(curInfo.tag, MyAppData.class);
        //        }
        //        if (myAppData == null) {
        //            return;
        //        }
    //        int i;
    //        if (curInfo.isChoosed) {
    //            gridView.setRightFlagState(false);
    //            AppInfo removeInfo = null;
    //            i = 0;
    //            while (i < curAddedInfos.size()) {
    //                if (curAddedInfos.get(i) != null && ((AppInfo) curAddedInfos.get(i)).packageName
    //                        .equals(myAppData.component.packageName)) {
    //                    removeInfo = (AppInfo) curAddedInfos.get(i);
    //                }
    //                i++;
    //            }
    //            if (removeInfo != null) {
    //                SimpleHomeApplication.getInstance().setDataChangedFlag(true);
    //                SimpleHomeApplication.getInstance().removeAppInfo(removeInfo);
    //                this.mAppHelper.saveData(SimpleHomeApplication.getInstance().getCurAddedAppInfo());
    //                return;
    //            }
    //            return;
    //        }
    //        AppInfo info = new AppInfo();
    //        info.appIcon = curInfo.icon;
    //        info.appName = curInfo.title;
    //        info.packageName = myAppData.component.packageName;
    //        info.appDataString = curInfo.tag;
    //        AppInfo addInfo = info;
    //        i = 0;
    //        while (i < curAddedInfos.size()) {
    //            if (!(curAddedInfos.get(i) == null || info == null || !((AppInfo) curAddedInfos.get
    //                    (i)).packageName.equals(info.packageName))) {
    //                addInfo = null;
    //            }
    //            i++;
    //        }
    //        if (addInfo != null) {
    //            SimpleHomeApplication.getInstance().setDataChangedFlag(true);
    //            SimpleHomeApplication.getInstance().addAppInfo(addInfo);
    //            this.mAppHelper.saveData(SimpleHomeApplication.getInstance().getCurAddedAppInfo());
    //        }
    //        gridView.setRightFlagState(true);

        // 处理点击
        AppInfo appInfoFromString=null;
        if (!(curInfo == null || TextUtils.isEmpty(curInfo.tag))) {
            appInfoFromString = mAppHelper.getInstalledAppInfo(this, curInfo.tag);
        }
        if (appInfoFromString == null) {
            return;
        }
        if (curInfo.isChoosed) {
            gridView.setRightFlagState(false);
            AppInfo removeInfo = null;
            int i = 0;
            while (i < curAddedInfos.size()) {
                if (curAddedInfos.get(i) != null && ((AppInfo) curAddedInfos.get(i)).packageName
                        .equals(appInfoFromString.packageName)) {
                    removeInfo = (AppInfo) curAddedInfos.get(i);
                }
                i++;
            }
            if (removeInfo != null) {
                SimpleHomeApplication.getInstance().setDataChangedFlag(true);
                SimpleHomeApplication.getInstance().removeAppInfo(removeInfo);
                this.mAppHelper.saveData(SimpleHomeApplication.getInstance().getCurAddedAppInfo());
            }
        }else {
            gridView.setRightFlagState(true);
            appInfoFromString.appDataString = curInfo.tag;
            AppInfo addInfo = appInfoFromString;
            int i = 0;
            while (i < curAddedInfos.size()) {
                if ((curAddedInfos.get(i) != null &&appInfoFromString != null && ((AppInfo)
                        curAddedInfos.get(i)).packageName.equals(appInfoFromString.packageName))) {
                    addInfo = appInfoFromString;
                }
                i++;
            }
            if (addInfo != null) {
                SimpleHomeApplication.getInstance().setDataChangedFlag(true);
                SimpleHomeApplication.getInstance().addAppInfo(addInfo);
                this.mAppHelper.saveData(SimpleHomeApplication.getInstance().getCurAddedAppInfo());
            }
        }
    }
}

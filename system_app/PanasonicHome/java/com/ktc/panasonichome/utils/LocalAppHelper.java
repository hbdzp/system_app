package com.ktc.panasonichome.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ktc.panasonichome.R;
import com.ktc.panasonichome.model.AppInfo;
import com.ktc.panasonichome.model.CacheData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressLint({"CommitPrefEdits"})
public class LocalAppHelper {
    private AppDataCache   appDataCache;
    private PackageManager pm;

    public LocalAppHelper(Context context) {
        this.appDataCache = new AppDataCache(context, "home");
    }

    public void saveData(List<AppInfo> list) {
        List mList = new ArrayList();
        for (AppInfo info : list) {
            mList.add(info.appDataString);
        }
        this.appDataCache.put(new CacheData(mList));
        this.appDataCache.flush();
    }

    public List<String> getAllData() {
        if (this.appDataCache.get() != null) {
            return this.appDataCache.get().list;
        }
        return null;
    }

    public void close() {
        this.appDataCache.close();
    }

    public synchronized Drawable getInstalledAppIcon(Context context, String packageName) {
        Drawable icon;
        icon = null;
        if (this.pm == null) {
            this.pm = context.getPackageManager();
        }
        try {
            icon = this.pm.getApplicationIcon(this.pm.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return icon;
    }

    public synchronized AppInfo getInstalledAppInfo(Context context, String appDataString) {
        //        AppInfo  info = new AppInfo();
        //
        //        MyAppData data = (MyAppData) JObject.parseJObject(appDataString, MyAppData.class);
        //        info.appName = data.component.label;
        //        info.packageName = data.component.packageName;
        //        try {
        //            info.appIcon = Drawable.createFromStream(context.getContentResolver()
        // .openInputStream(Uri.parse(data.component.icon)), null);
        //        } catch (Exception e) {
        //            info.appIcon = new BitmapDrawable(BitmapFactory.decodeResource(context
        // .getResources()
        //                    , R.drawable.web_app_default));
        //            e.printStackTrace();
        //        }
        //        info.appDataString = appDataString;
        //        return info;

        AppInfo appInfo =null;
        try {
            appInfo = JSON.parseObject(appDataString, AppInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(appInfo!=null){
            appInfo.appDataString = appDataString;
            appInfo.appIcon=getInstalledAppIcon(context,appInfo.packageName);
            try {
                if(appInfo.appIcon==null){
                    appInfo.appIcon = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources()
                            , R.drawable.web_app_default));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return appInfo;
    }

    public synchronized List<AppInfo> getInstalledAppList(Context mContext) {
       /* List<AppInfo> installedApps;
        installedApps = new ArrayList();
        Cursor c = context.getContentResolver().query(SuperXFinder.CC_APPSTORE_AUTH_FINDER
        .findXProviderAuthority().create("app/as/myapp/myapp").toUri(), null, null, null, null);
        List<MyAppData> appList = new ArrayList();
        if (c != null) {
            c.moveToFirst();
            Log.i("bonfiy", "cursor:" + c.getCount());
            do {
                appList.add((MyAppData) MyAppData.CURSOR_CREATER.createFromCursor(CursorCreater
                .NO_PARENT, c));
            } while (c.moveToNext());
            c.close();
        }
        if (appList.size() > 0) {
            for (MyAppData myAppData : appList) {
                AppInfo appInfo = new AppInfo();
                appInfo.appName = myAppData.component.label;
                appInfo.packageName = myAppData.component.packageName;
                try {
                    appInfo.appIcon = Drawable.createFromStream(context.getContentResolver()
                    .openInputStream(Uri.parse(myAppData.component.icon)), null);
                } catch (Exception e) {
                    appInfo.appIcon = new BitmapDrawable(BitmapFactory.decodeResource(context
                    .getResources(), C0285R.drawable.web_app_default));
                    e.printStackTrace();
                }
                appInfo.appDataString = SkyJSONUtil.getInstance().compile(myAppData);
                installedApps.add(appInfo);
            }
        }
        return installedApps;*/

        PackageManager localPackageManager = mContext.getPackageManager();
        Intent         localIntent         = new Intent("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo>     localList      = localPackageManager.queryIntentActivities
                (localIntent, 0);
        ArrayList<AppInfo>    installedApps  = new ArrayList<>();
        Iterator<ResolveInfo> localIterator  = null;
        if (localList.size() != 0) {
            localIterator = localList.iterator();
        }
        while (true) {
            if (!localIterator.hasNext()) {
                break;
            }
            ResolveInfo localResolveInfo = (ResolveInfo) localIterator.next();
            AppInfo     localAppBean     = new AppInfo();
            localAppBean.setAppName(localResolveInfo.activityInfo.loadLabel(localPackageManager)
                    .toString
                    ());
            localAppBean.setPackageName(localResolveInfo.activityInfo.packageName);
            localAppBean.setLauncherName(localResolveInfo.activityInfo.name);
            String      pkgName = localResolveInfo.activityInfo.packageName;
            PackageInfo mPackageInfo;
            try {
                mPackageInfo = mContext.getPackageManager().getPackageInfo(pkgName, 0);
                if ((mPackageInfo.applicationInfo.flags & mPackageInfo.applicationInfo
                        .FLAG_SYSTEM) > 0) {// 系统预装
                    localAppBean.setSystemApp(true);
                } else {
                    localAppBean.setSystemApp(false);
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            //json 序列化   序列化信息不包含图片
            String jsonStrng = JSON.toJSONString(localAppBean);
            localAppBean.setAppIcon(localResolveInfo.activityInfo.loadIcon(localPackageManager));
            localAppBean.setAppDataString(jsonStrng);
            if (filterApk(pkgName)) {
                continue;
            }
            installedApps.add(localAppBean);
        }
        return installedApps;
    }

    /**
     * 过滤掉不需要显示的应用 @ packagenName @ return boolean
     */
    public boolean filterApk(String packagenName) {
        if (packagenName.equals("com.android.gallery3d")) {
            return true;
        } else if (packagenName.equals("com.android.contacts")) {
            return true;
        } else if (packagenName.equals("com.android.phone")) {
            return true;
        } else if (packagenName.equals("mstar.factorymenu.ui")) {
            return true;
        } else if (packagenName.equals("com.broadcom.bluetoothmonitor")) {
            return true;
        } else if (packagenName.equals("com.awox.quickcontrolpoint")) {
            return true;
        } else if (packagenName.equals("com.awox.renderer3")) {
            return true;
        } else if (packagenName.equals("com.android.dummyactivity")) {
            return true;
        } else if (packagenName.equals("com.awox.server")) {
            return true;
        } else if (packagenName.equals("com.android.camera2")) {
            return true;
        } else if (packagenName.equals("com.android.deskclock")) {
            return true;
        } else if (packagenName.equals("com.android.calculator2")) {
            return true;
        } else if (packagenName.equals("com.android.music")) {
            return true;
        } else if (packagenName.equals("com.mstar.tvsetting")) {
            return true;
        } else if (packagenName.equals("com.android.providers.downloads.ui")) {
            return true;
        } else if (packagenName.equals("com.ktc.panasonichome")) {
            return true;
        } else if (packagenName.equals("com.mstar.android.tv.app")) {
            return true;
        }else if (packagenName.equals("com.android.exchange")) {
            return true;
        }else if (packagenName.equals("com.android.email")) {
            return true;

        }else if (packagenName.equals("com.android.quicksearchbox")) {
            //搜索
            return true;
        }else if (packagenName.equals("com.android.browser")) {
            return true;
        }
        else if (packagenName.equals("com.horion.tv")) {
            return true;
        }
        else if (packagenName.equals("cn.ktc.android.epg")) {
            return true;
        }
        else if (packagenName.equals("com.widevine.demo")) {
            return true;
        }
        else if (packagenName.equals("com.android.tv.settings")) {
            return true;
        }
        else if (packagenName.equals("com.widevine.test")) {
            return true;
        }
        else if (packagenName.equals("com.jrm.localmm")) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param context
     * @param packageName
     * @return 根据包名判断应用是否安装
     */
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || TextUtils.isEmpty(packageName))
            return false;
        try {
             context.getPackageManager().getApplicationInfo(packageName,
                            PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

}

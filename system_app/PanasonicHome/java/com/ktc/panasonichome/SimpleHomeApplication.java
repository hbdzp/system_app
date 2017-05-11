package com.ktc.panasonichome;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.ktc.panasonichome.model.AppInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dinzhip on 2016/12/28.
 */

public class SimpleHomeApplication extends Application {
    private static Context mContext;
    private static Handler mHandler;
    private static long    mMainThreadId;

    private static SimpleHomeApplication application         = null;
    private        List<AppInfo>         curAddedInfos       = null;
    private        boolean               dataChangedFlag     = false;
    public         boolean               isStartLocalAppGrid = false;
    private AppDataUIChange listener;
    //保存协议的内容
    private Map<String, String> mProtocolMap = new HashMap<>();

    public Map<String, String> getProtocolMap() {
        return mProtocolMap;
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 程序的入口方法
        // 1.上下文
        mContext = getApplicationContext();

        // 2.主线程的Handler
        mHandler = new Handler();

        // 3.得到主线程的id
        mMainThreadId = android.os.Process.myTid();
        /**
         myTid: thread
         myPid: process
         myUid: user
         */
        application = this;
        this.curAddedInfos = new ArrayList();
    }


    public interface AppDataUIChange {
        void addData(int i);

        void removeData(int i);
    }

    public static SimpleHomeApplication getInstance() {
        return application;
    }

    public void setCurAddedAppInfo(List<AppInfo> infos) {
        this.curAddedInfos = infos;
    }

    public void setDataChangedFlag(boolean changed) {
        this.dataChangedFlag = changed;
    }

    public boolean getDataChanedFlag() {
        return this.dataChangedFlag;
    }

    public List<AppInfo> getCurAddedAppInfo() {
        return this.curAddedInfos;
    }

    public void addAppInfo(AppInfo info) {
        if (this.curAddedInfos != null) {
            int i = 0;
            while (i < this.curAddedInfos.size()) {
                if (!(this.curAddedInfos.get(i) == null || info == null || !((AppInfo) this.curAddedInfos.get(i)).packageName.equals(info.packageName))) {
                    info = null;
                }
                i++;
            }
            if (info != null) {
                this.listener.addData(this.curAddedInfos.size() + 5);
                this.curAddedInfos.add(info);
            }
        }
    }

    public void removeAppInfo(String packageName) {
        Object removeInfo = null;
        if (this.curAddedInfos != null) {
            int i = 0;
            while (i < this.curAddedInfos.size()) {
                if (!(this.curAddedInfos.get(i) == null || TextUtils.isEmpty(packageName) ||
                        !packageName.equals(((AppInfo) this.curAddedInfos.get(i)).packageName))) {
                    AppInfo removeInfo2 = (AppInfo) this.curAddedInfos.get(i);
                }
                i++;
            }
            if (removeInfo != null) {
                this.listener.removeData(this.curAddedInfos.indexOf(removeInfo) + 5);
                this.curAddedInfos.remove(removeInfo);
            }
        }
    }

    public void removeAppInfo(AppInfo info) {
        Object removeInfo = null;
        if (this.curAddedInfos != null) {
            int i = 0;
            while (i < this.curAddedInfos.size()) {
                if (!(this.curAddedInfos.get(i) == null || info == null || !((AppInfo) this.curAddedInfos.get(i)).packageName.equals(info.packageName))) {
                    removeInfo = (AppInfo) this.curAddedInfos.get(i);
                }
                i++;
            }
            if (removeInfo != null) {
                this.listener.removeData(this.curAddedInfos.indexOf(removeInfo) + 5);
                this.curAddedInfos.remove(removeInfo);
            }
        }
    }

    public void setAppDataUIChange(AppDataUIChange listener) {
        this.listener = listener;
    }




    //dzp add 3-1 start
    public static Display getCurrentDisplay(Context context) {
        if (context == null) {
            return null;
        }
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public static float getDisplayDensity(Context context) {
        if (context == null) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        return context.getApplicationContext().getResources().getDisplayMetrics().density;
    }

    public static int getDisplayWidth(Context context) {
        if (context == null) {
            return 0;
        }
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        if (display == null) {
            return 1920;
        }
        return display.getWidth();
    }

    public static int getDisplayHeight(Context context) {
        if (context == null) {
            return 0;
        }
        return ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    //dzp add 3-1 end

}

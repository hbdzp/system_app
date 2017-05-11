package com.ktc.panasonichome.model;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import com.ktc.panasonichome.LauncherActivity;
import com.ktc.panasonichome.SimpleHomeApplication;
import com.ktc.panasonichome.model.SimpleHomeWebQuery.QueryUrlListener;
import com.ktc.panasonichome.utils.LogUtils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class HomePageDataCached implements QueryUrlListener {
    private String cachedPath = null;
    private HomePageXmlParse parse = null;
    private WeakReference<LauncherActivity> wfActivity;

    public HomePageDataCached(LauncherActivity context) {
        this.wfActivity = new WeakReference(context);
        this.cachedPath = context.getCacheDir().getAbsolutePath();
        String cachedFolderPath = this.cachedPath + File.separator + "cached";
        File cachedFolderFile = new File(cachedFolderPath, "simplehomepage.xml");
        if (!cachedFolderFile.exists()) {
            loadHomePageDataDefault();
        } else if (new File(cachedFolderPath, "flag").exists()) {
            cachedFolderFile.delete();
            loadHomePageDataDefault();
        } else if (cachedFolderFile.length() < 10) {
            cachedFolderFile.delete();
            loadHomePageDataDefault();
        } else {
            loadHomePageData(cachedFolderFile.getAbsolutePath());
        }
    }

    public String getMd5() {
        String cachedFolderPath = this.cachedPath + File.separator + "cached";
        File cachedFolderFile = new File(cachedFolderPath, "simplehomepage.xml");
        if (cachedFolderFile.exists() && cachedFolderFile.length() >= 10 && !new File(cachedFolderPath, "flag").exists()) {
            return Md5DigestUtil.getCurrentFileMd5(cachedFolderFile);
        }
        return null;
    }

    private void loadHomePageDataDefault() {
        if (!(this.wfActivity == null || this.wfActivity.get() == null)) {
            String path = "1080p";
            switch (SimpleHomeApplication.getDisplayWidth((Context) this.wfActivity.get())) {
                case 1280:
                    path = "720p";
                    break;
                case 1920:
                    path = "1080p";
                    break;
            }
            String defaultxmlpath = "/system/vendor/res/homepage5/" + path;
            if (new File(defaultxmlpath).exists()) {
                String localXmlFileName = defaultxmlpath + "/normal" + File.separator + "MainPageLocalXml_panasonic.xml";
                this.parse = new HomePageXmlParse(localXmlFileName);
                if (!this.parse.parseNow()) {
                   LogUtils.v("dzp", " parse " + localXmlFileName + "  failed!");
                }
                DefaultMainPageConfigParse configParse = new DefaultMainPageConfigParse(defaultxmlpath + File.separator + "MainPageConfig.xml");
                if (!configParse.parseNow()) {
                   LogUtils.v("dzp", " parse configParse xml failed!");
                }
                SimpleHomeWebQuery.getSimpleHomeWebQuery().init(((LauncherActivity) this.wfActivity.get()).getMainLooper(), this, configParse.getDefaultConfig());
            } else {
               LogUtils.v("dzp", " local xml not found at " + defaultxmlpath);
            }
        }
    }

    public HomePageData getHomePageData() {
        if (this.parse != null) {
            return this.parse.getHomePageData();
        }
        return null;
    }

    public void cleanUp() {
        SimpleHomeWebQuery.getSimpleHomeWebQuery().cleanup();
    }

    public void startSyncNetNow() {
        SimpleHomeWebQuery.getSimpleHomeWebQuery().startSyncNetNow();
    }

    private void loadHomePageData(String cachedFile) {
        if (!(this.wfActivity == null || this.wfActivity.get() == null)) {
            String path = "1080p";
//            switch (SkySystemUtil.getDisplayWidth((Context) this.wfActivity.get())) {
//                case 1280:
//                    path = "720p";
//                    break;
//                case 1920:
//                    path = "1080p";
//                    break;
//            }
            String defaultxmlpath = "/system/vendor/res/homepage5/" + path;
            if (new File(defaultxmlpath).exists()) {
                this.parse = new HomePageXmlParse(cachedFile);
                if (this.parse.parseNow()) {
                    DefaultMainPageConfigParse configParse = new DefaultMainPageConfigParse(defaultxmlpath + File.separator + "MainPageConfig.xml");
                    if (!configParse.parseNow()) {
                       LogUtils.v("dzp", " parse configParse xml failed!");
                    }
                    SimpleHomeWebQuery.getSimpleHomeWebQuery().init(((LauncherActivity) this.wfActivity.get()).getMainLooper(), this, configParse.getDefaultConfig());
                } else {
                   LogUtils.v("dzp", " parse cachedFile + " + cachedFile + " failed!");
                    new File(this.cachedPath + File.separator + "cached", "simplehomepage.xml").delete();
                    loadHomePageDataDefault();
                    return;
                }
            }
           LogUtils.v("dzp", " local xml not found at " + defaultxmlpath);
        }
    }

    public String getLocalCachedPath() {
        if (this.wfActivity == null || this.wfActivity.get() == null) {
            return null;
        }
        return ((LauncherActivity) this.wfActivity.get()).getCacheDir().getAbsolutePath();
    }

    public String getQueryUrl() {
        if (this.wfActivity == null || this.wfActivity.get() == null) {
            return null;
        }
        return ((LauncherActivity) this.wfActivity.get()).getQueryUrl();
    }

    public boolean isNetConnected() {
        if (this.wfActivity == null || this.wfActivity.get() == null) {
            return false;
        }
        return ((LauncherActivity) this.wfActivity.get()).isNetConnected();
    }

    public boolean isOutOfRefreshTime() {
        if (this.wfActivity == null || this.wfActivity.get() == null) {
            return false;
        }
        long time = ((LauncherActivity) this.wfActivity.get()).getSharedPreferences("REFRESH", 0).getLong("refreshtime", 0);
        long currenttime = System.currentTimeMillis();
        if (time == 0) {
            return true;
        }
        long duration = currenttime - time;
        return duration > 0 && duration - 300000 > 0;
    }

    public void commitRefreshTime() {
        if (this.wfActivity != null && this.wfActivity.get() != null) {
            Editor localEditor = ((LauncherActivity) this.wfActivity.get()).getSharedPreferences("REFRESH", 0).edit();
            localEditor.putLong("refreshtime", System.currentTimeMillis());
            localEditor.commit();
        }
    }

    public HashMap<String, String> getHomeWebData() {
        if (this.wfActivity == null || this.wfActivity.get() == null) {
            return null;
        }
        return ((LauncherActivity) this.wfActivity.get()).getHomeWebData();
    }

    public void downloadResult(boolean success) {
        cleanUp();
        if (success && this.wfActivity != null && this.wfActivity.get() != null) {
            this.parse = new HomePageXmlParse(new File(this.cachedPath + File.separator + "cached", "simplehomepage.xml").getAbsolutePath());
            if (!this.parse.parseNow()) {
               LogUtils.v("dzp", " parse cachedFile +  failed!");
            }
            ((LauncherActivity) this.wfActivity.get()).downloadSuccess();
        }
    }
}

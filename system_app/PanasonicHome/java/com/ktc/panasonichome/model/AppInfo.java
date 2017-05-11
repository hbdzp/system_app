package com.ktc.panasonichome.model;

import android.graphics.drawable.Drawable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class AppInfo implements Serializable {
    private static final long serialVersionUID = -5500889203256585266L;
    public String appDataString;
    public Drawable appIcon;
    public String appName;
    public String packageName;
    public boolean  isSystemApp;
    public String launcherName;
    public AppInfo(){

    }
    public AppInfo(byte[] byteData) {
        try {
            AppInfo node = (AppInfo) new ObjectInputStream(new ByteArrayInputStream(byteData)).readObject();
            this.appName = node.appName;
            this.appIcon = node.appIcon;
            this.packageName = node.packageName;
            this.appDataString = node.appDataString;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public byte[] toBytes() {
        try {
            ByteArrayOutputStream byteArrayIn = new ByteArrayOutputStream();
            new ObjectOutputStream(byteArrayIn).writeObject(this);
            return byteArrayIn.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAppDataString() {
        return appDataString;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setAppDataString(String appDataString) {
        this.appDataString = appDataString;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public String getLauncherName() {
        return launcherName;
    }

    public void setLauncherName(String launcherName) {
        this.launcherName = launcherName;
    }
}

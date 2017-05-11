package com.ktc.panasonichome.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.SimpleHomeApplication;

import java.util.List;

import static com.ktc.panasonichome.utils.UIUtils.getResources;

/**
 * Created by dinzhip on 2016/12/28.
 */


public class StartApi {
    private static StartApi api;
    private        Context  mConetxt;

    public StartApi(Context context) {
        this.mConetxt = context.getApplicationContext();
    }

    public static StartApi getInstence(Context context) {
        if (api == null) {
            api = new StartApi(context);
        }
        return api;
    }

    public String getImgLocalPath() {
        String path = "720p";
        switch (SimpleHomeApplication.getDisplayWidth(this.mConetxt)) {
            case 1280:
                path = "720p";
                break;
            case 1920:
                path = "1080p";
                break;
        }
//        return "/mnt/usb/7C66-4A06/res/homepage5/" + path + "/normal" + File.separator;
        return  "";
    }

    public boolean isZh() {
        if (this.mConetxt.getResources().getConfiguration().locale.getLanguage().endsWith("zh")) {
            return true;
        }
        return false;
    }

    public boolean launchCustomApp(String packageName) {
        try {
            Intent intent = this.mConetxt.getPackageManager().getLaunchIntentForPackage(packageName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.mConetxt.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void launchLocalMedia() {
        Intent        intentMEDIA        = new Intent();
        ComponentName componentNameMEDIA = new ComponentName("com.jrm.localmm", "com.jrm.localmm" +
                ".HomeActivity");
        intentMEDIA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intentMEDIA.setComponent(componentNameMEDIA);
        if (isIntentAvailable(mConetxt, intentMEDIA)) {
            mConetxt.startActivity(intentMEDIA);
        } else {
            Toast.makeText(mConetxt, getResources().getString(R.string
                    .toast_app_not_installed), Toast.LENGTH_SHORT).show();
        }
    }

//    public boolean launchSourceList() {
//        try {
//            Intent intent = new Intent("startSourceList");
//            intent.putExtra("specialKey", SkyworthBroadcastKey.SKY_BROADCAST_KEY_SIGNAL);
//            this.mConetxt.sendBroadcast(intent);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public void launchTvSetting() {
        Intent        intent        = new Intent();
        ComponentName componentNameMEDIA = new ComponentName("com.android.tv.settings", "com.android.tv.settings.MainActivity");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setComponent(componentNameMEDIA);
        if (isIntentAvailable(mConetxt, intent)) {
            mConetxt.startActivity(intent);
        } else {
            Toast.makeText(mConetxt, getResources().getString(R.string
                    .toast_app_not_installed), Toast.LENGTH_SHORT).show();
        }
    }

//    public boolean launchQrcode() {
//        try {
//            Intent mIntent = new Intent();
//            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            mIntent.setClassName("com.tianci.qrcode", "com.tianci.qrcode.SkyQrcode");
//            this.mConetxt.startActivity(mIntent);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean launchMovieRecord() {
//        SkyMediaApiParam param   = new SkyMediaApiParam();
//        String           pkgName = "com.tianci.movieplatform";
//        param.setStartAppActionName("coocaa.intent.movie.history");
//        try {
//            SkyMediaApi.getInstance().setContext(this.mConetxt);
//            SkyMediaApi.getInstance().startApp(param);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

//    public boolean launchMyLocalGame() {
//        try {
//            Intent intent = new Intent("coocaa.intent.action.GAME_CENTER_MYGAME");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.mConetxt.startActivity(intent);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public boolean launchMyLocalApps() {
//        try {
//            Intent intent = new Intent("coocaa.intent.action.APP_STORE_MYAPPS");
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.mConetxt.startActivity(intent);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

//    public boolean launchScreenSaverApp() {
//        try {
//            Intent intent = new Intent("com.tianci.ad.screensaver");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            this.mConetxt.startActivity(intent);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public boolean launchNetworking() {
        try {
            Intent intent = new Intent("android.panasonic.settings.NETWORK_SETTING");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            if (this.mConetxt != null) {
                this.mConetxt.startActivity(intent);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean launchBlueTooth() {
        try {
            Intent intent = new Intent("android.settings.BLUETOOTH_SETTINGS");
            intent.setClassName("com.android.tv.settings","com.android.setting.connectsetting.bluetooth.BluetoothActivity");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (this.mConetxt != null) {
                this.mConetxt.startActivity(intent);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

//    public boolean launchBlueContro() {
//        try {
//            Intent intent = new Intent("coocaa.intent.guide.onekey");
//            intent.putExtra(NetworkDefs.KEY_NET_STATE_FROM, "homepage");
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (this.mConetxt != null) {
//                this.mConetxt.startActivity(intent);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
    @SuppressWarnings("WrongConstant")
    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                1);
        return list.size() > 0;
    }
}
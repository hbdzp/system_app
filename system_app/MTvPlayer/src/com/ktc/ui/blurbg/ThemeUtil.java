package com.ktc.ui.blurbg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.text.TextUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/////import org.apache.http4.cookie.ClientCookie;

public class ThemeUtil {
    public static final String TARGET_PACKAGE_NAME = "com.ktc.ktcgrid";
    private static final String THEME_CHAGE_BROADCAST = "com.ktc.broadcast.themechange";
    public static final String THEME_SHARE_NAME = "theme_bg";
    private String curTheme;
    private ExecutorService mImageThreadPool;

    private static class ThemeUtilHolder {
        private static final ThemeUtil INSTANCE = new ThemeUtil();

        private ThemeUtilHolder() {
        }
    }

    private ThemeUtil() {
        this.curTheme = "theme_1";
        this.mImageThreadPool = null;
    }

    public static final ThemeUtil getInstance() {
        return ThemeUtilHolder.INSTANCE;
    }

    private Context getTargetContext(Context context) throws NameNotFoundException {
        ////return context.createPackageContext(TARGET_PACKAGE_NAME, 2);
        return context.createPackageContext(TARGET_PACKAGE_NAME, Context.CONTEXT_IGNORE_SECURITY);
    }

    public String getCurTheme(Context context) throws NameNotFoundException {
        return getTargetPreferences(context).getString("cur_theme", "theme_1");
    }

    public SharedPreferences getTargetPreferences(Context context) throws NameNotFoundException {
        return getTargetContext(context).getSharedPreferences(THEME_SHARE_NAME, 5);
    }

    public ExecutorService getThreadPool() {
        if (this.mImageThreadPool == null) {
            synchronized (ExecutorService.class) {
                try {
                    if (this.mImageThreadPool == null) {
                        this.mImageThreadPool = Executors.newFixedThreadPool(1);
                    }
                } catch (Throwable th) {
                    Class cls = ExecutorService.class;
                }
            }
        }
        return this.mImageThreadPool;
    }

    public void savaBitmap(final Context context, final Bitmap bitmap) {
        if (bitmap != null) {
            getThreadPool().execute(new Runnable() {
                public void run() {
                    try {
                        String absolutePath = context.getApplicationContext().getFilesDir().getAbsolutePath();
                        File file = new File(absolutePath);
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        absolutePath = absolutePath + File.separator + ThemeUtil.this.curTheme + ".png";
                        file = new File(absolutePath);
                        if (!file.exists()) {
                            OutputStream fileOutputStream = new FileOutputStream(file);
                            bitmap.compress(CompressFormat.PNG, 100, fileOutputStream);
                            file.createNewFile();
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            bitmap.recycle();
                        }
                        Intent intent = new Intent();
                        intent.setAction(ThemeUtil.THEME_CHAGE_BROADCAST);
                        ////intent.putExtra(ClientCookie.PATH_ATTR, absolutePath);
                        context.sendBroadcast(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public boolean setCurTheme(Context context, String str) throws NameNotFoundException {
        SharedPreferences targetPreferences = getTargetPreferences(context);
        this.curTheme = targetPreferences.getString("cur_theme", "theme_1");
        if (TextUtils.equals(this.curTheme, str)) {
            return true;
        }
        Editor edit = targetPreferences.edit();
        edit.putString("cur_theme", str);
        this.curTheme = str;
        return edit.commit();
    }
}

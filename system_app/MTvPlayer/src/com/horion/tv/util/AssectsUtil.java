package com.horion.tv.util;

import android.content.Context;
import android.util.Log;
////import com.horion.tv.utils.KtcTVDebug;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AssectsUtil {
    private static void copyAssets(String str, String str2, String str3, Context context) {
        try {
            File file = new File(str);
            if (!file.isDirectory()) {
                file.mkdirs();
            }
            if (!new File(str + File.separator + str3).exists()) {
                InputStream open = context.getAssets().open(str2 + File.separator + str3);
                FileOutputStream fileOutputStream = new FileOutputStream(str + File.separator + str3);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = open.read(bArr);
                    if (read != -1) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        open.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private static void deleteFilesByDirectory(File file) {
        if (file != null && file.exists()) {
            ////KtcTVDebug.debug("delete : " + file.getName());
            if (file.isDirectory()) {
                for (File file2 : file.listFiles()) {
                    if (file2.isDirectory()) {
                        deleteFilesByDirectory(file2);
                    } else {
                        file2.delete();
                    }
                }
                return;
            }
            file.delete();
        }
    }

    public static String getAssetsMenufile(Context context, String str, String str2) {
        String str3 = getTvDir(context, str) + File.separator + str2;
        Log.d("Maxs","AssectsUitls:getAssetsMenuFile:str3 = " + str3 + " /");
        File file = new File(str3);
        Log.d("Maxs","AssectsUitls:getAssetsMenuFile:file.exists() =" + file.exists());
        return file.exists() ? str3 : null;
    }

    private static String getTvDir(Context context, String str) {
        Log.d("Maxs","str = " + str + " /context == null = " + (context == null));
        return context.getDir(str, 0).getAbsolutePath();
    }

    public static void loadAssetsDirfile(Context context, String str) {
        Log.d("Maxs","AssectsUtil.1111");
        if (context == null) {

            ////KtcTVDebug.debug("LoadAssetsMenufile context = null");
            return;
        }
        String tvDir = getTvDir(context, str);
        Log.d("Maxs","tvDri = " + getTvDir(context,str));
        deleteFilesByDirectory(new File(tvDir));
        try {
            String[] list = context.getAssets().list(str);
            Log.d("Maxs","list.length = " + list.length + " /str = " + str);
            ////KtcTVDebug.debug("str.length is ---" + list.length);
            if (list.length > 0) {
                for (String copyAssets : list) {
                    copyAssets(tvDir, str, copyAssets, context);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

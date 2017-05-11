package com.ktc.panasonichome.utils;

import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;

import com.ktc.panasonichome.model.CacheData;
import com.ktc.panasonichome.view.io.DiskLruCache;
import com.ktc.panasonichome.view.io.DiskLruCache.Editor;
import com.ktc.panasonichome.view.io.DiskLruCache.Snapshot;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.security.MessageDigest;

public class AppDataCache {
    private DiskLruCache mAppLruCache = null;

    public AppDataCache(Context context, String cacheName) {
        try {
            File cacheDir = getDiskCacheDir(context, cacheName);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            this.mAppLruCache = DiskLruCache.open(cacheDir, 1, 1, 20971520);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void put(CacheData data) {
        try {
            Editor editor = this.mAppLruCache.edit(hashKeyForDisk("homepage"));
            if (editor == null) {
                return;
            }
            if (browserDataToStream(data, editor.newOutputStream(0))) {
                editor.commit();
            } else {
                editor.abort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove() {
        try {
            this.mAppLruCache.remove(hashKeyForDisk("homepage"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CacheData get() {
        try {
            Snapshot snapShot = this.mAppLruCache.get(hashKeyForDisk("homepage"));
            if (snapShot != null) {
                byte[] byteData = ByteToInputStream.input2byte(snapShot.getInputStream(0));
                if (byteData != null) {
                    return new CacheData(byteData);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            this.mAppLruCache.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            if (((int) ((this.mAppLruCache.size() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID)) > 20) {
                this.mAppLruCache.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        try {
            this.mAppLruCache.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        return new File(context.getCacheDir().getPath() + File.separator + uniqueName);
    }

    private boolean browserDataToStream(CacheData data, OutputStream outputStream) {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        try {
            bufferedInputStream  = new BufferedInputStream(ByteToInputStream.byte2Input(data.toBytes()), 8192);
            bufferedOutputStream = new BufferedOutputStream(outputStream, 8192);
            int b=0;
            while((b=bufferedInputStream.read())!=-1){
                bufferedOutputStream.write(b);
            }
            if(null!=bufferedOutputStream){
                bufferedOutputStream.close();
            }
            if(null!=bufferedInputStream){
                bufferedInputStream.close();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String hashKeyForDisk(String key) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            mDigest.update(key.getBytes());
            return bytesToHexString(mDigest.digest());
        } catch (Exception e) {
            return String.valueOf(key.hashCode());
        }
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 255);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}

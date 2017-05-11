package com.ktc.panasonichome.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

/**
 * Created by xiacf on 2017/1/19.
 */

public class DataBaseHelper {
    private Context mContext = null;
    private ContentResolver cr = null;
    private static DataBaseHelper instanBaseHelper;
    //user_setting.db systemsetting table index
    public final static short T_SystemSetting_IDX = 0x19;

    public static DataBaseHelper getInstance(Context context) {
        if (instanBaseHelper == null) {
            instanBaseHelper = new DataBaseHelper(context);
        }
        return instanBaseHelper;
    }

    private DataBaseHelper(Context context) {
        mContext = context;
    }

    private ContentResolver getContentResolver() {
        if (cr == null) {
            cr = mContext.getContentResolver();
        }
        return cr;
    }

    public void updateDatabase_systemsetting(String tag, int values) {
        int ret = -1;
        ContentValues vals = new ContentValues();
        vals.put(tag, values);
        try {
            ret = getContentResolver().update(
                    Uri.parse("content://mstar.tv.usersetting/systemsetting"),
                    vals, null, null);
        } catch (SQLException e) {
        }
        if (ret == -1) {
            System.out.println("update tbl_SystemSetting ignored");
        }

        try {
            TvManager.getInstance().getDatabaseManager()
                    .setDatabaseDirtyByApplication(T_SystemSetting_IDX);
        } catch (TvCommonException e) {
            e.printStackTrace();
        }
    }

    public int getValueDatabase_systemsetting(String tag) {
        int value = 0;
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://mstar.tv.usersetting/systemsetting"),
                null, null, null, null);
        if (cursor.moveToNext())
            value = cursor.getInt(cursor.getColumnIndex(tag));
        cursor.close();
        return value;
    }
}

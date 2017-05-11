package com.ktc.panasonichome;

import java.io.File;

import com.ktc.panasonichome.utils.StringUtils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareAppInfoUtils {
	private Context mContext;
	private static final String FILE_NAME  = "/data/data/com.ktc.panasonichome/shared_prefs/AppInfos.xml";
	private File file = new File(FILE_NAME);
	private SharedPreferences share = null;
	private SharedPreferences.Editor editor = null;

	public ShareAppInfoUtils(Context mContext) {
		super();
		this.mContext = mContext;
    	share = mContext.getSharedPreferences("AppInfos", Context.MODE_PRIVATE);
    	editor = share.edit();
	}

    public boolean put(String key, Object value) {
        if (StringUtils.isEmpty(key) || null == value) {
            throw new RuntimeException("key or value cannot be null.");
        }
        if (value instanceof String) {
            editor.putString(key, String.valueOf(value));
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, Boolean.parseBoolean(value.toString()));
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        return editor.commit();
    }

    public int getInt(String key) {
        return share.getInt(key, 0);
    }

    public boolean isfileExit() {
        return file.exists();
    }
    
    public boolean deleteFile(){
    	return file.delete();
    }
    
    public boolean contains(String key) {
        return share.contains(key);
    }
    
    public boolean remove(String key) {
        editor.remove(key);
        return editor.commit();
    }

    public boolean clear() {
        editor.clear();
        return editor.commit();
    }

}

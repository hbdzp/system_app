package com.horion.tv.ui.utils;

import android.content.Context;
import com.horion.tv.R.string;
////import com.horion.tv.utils.KtcTVDebug;
import java.lang.reflect.Field;

public class LogicTextResource {
    Context context = null;
    Field[] fields = null;
    string obj = new string();

    public LogicTextResource(Context context) {
        this.context = context;
    }

    private boolean contains(String str) {
        if (this.fields == null) {
            return false;
        }
        for (Field name : this.fields) {
            if (name.getName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private Field getField(String str) {
        if (!(str == null || this.fields == null)) {
            for (Field field : this.fields) {
                if (field.getName().equals(str)) {
                    return field;
                }
            }
        }
        return null;
    }

    private int getResidByReskey(String str) {
        if (str != null) {
            try {
                Field field = getField(str);
                if (field != null) {
                    return field.getInt(this.obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        return 0;
    }

    public String getText(int i) {
        return this.context != null ? this.context.getString(i) : null;
    }

    public String getText(String str) {
        if (str == null) {
            ////KtcTVDebug.debug("getText resid=null");
            return null;
        }
        if (this.fields == null) {
            init();
        }
        int i = 0;
        try {
            i = getResidByReskey(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (i != 0) {
            return this.context != null ? this.context.getString(i) : null;
        } else {
            ////KtcTVDebug.debug("not found " + str + " in xml");
            return "";
        }
    }

    public String getText1(String str) {
        if (str == null) {
            ////KtcTVDebug.debug("getText resid=null");
            return null;
        }
        if (this.fields == null) {
            init();
        }
        int i = 0;
        try {
            i = getResidByReskey(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (i != 0) {
            return this.context != null ? this.context.getString(i) : null;
        } else {
            ////KtcTVDebug.debug("not found " + str + " in xml");
            return str;
        }
    }

    public void init() {
        this.fields = string.class.getDeclaredFields();
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

package com.ktc.framework.ktcsdk.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class KtcPluginParam {
    private static HashMap<String, Object> params = new HashMap();
    private String id = UUID.randomUUID().toString();
    private ArrayList<String> keys = new ArrayList();

    private String createKey(String str) {
        return this.id + str;
    }

    public boolean add(String str, Object obj) {
        String createKey = createKey(str);
        if (params.containsKey(createKey)) {
            return false;
        }
        params.put(createKey, obj);
        this.keys.add(createKey);
        return true;
    }

    protected void finalize() throws Throwable {
        for (String remove : this.keys) {
            params.remove(remove);
        }
        this.keys.clear();
        this.keys = null;
        super.finalize();
    }

    public Object get(String str) {
        return params.get(createKey(str));
    }

    public <T> T get(String str, Class<T> cls) {
        ////T t = params.get(createKey(str));
        ////if (t == null) {
            return null;
        ////}
       //// try {
        ////    return cls.isInstance(t) ? t : null;
       //// } catch (Exception e) {
        ////    e.printStackTrace();
        ///    return null;
      ////  }
    }
}

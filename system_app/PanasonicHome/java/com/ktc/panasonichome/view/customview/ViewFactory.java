package com.ktc.panasonichome.view.customview;

import java.util.HashMap;

public class ViewFactory {
    private static HashMap<Class, Object> factoryCache = new HashMap();

    public static synchronized <T> T getFactory(Class<? extends T> clazz) {
        T t;
        synchronized (ViewFactory.class) {
            if (factoryCache.get(clazz) == null) {
                try {
                    factoryCache.put(clazz, clazz.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("cannot init factory by clazz");
                }
            }
            t = (T) factoryCache.get(clazz);
        }
        return t;
    }

    public static synchronized void destroy() {
        synchronized (ViewFactory.class) {
            if (factoryCache != null) {
                factoryCache.clear();
            }
        }
    }
}

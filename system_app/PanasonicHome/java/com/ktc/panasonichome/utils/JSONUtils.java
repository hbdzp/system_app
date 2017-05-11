package com.ktc.panasonichome.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;

import java.util.List;

/**
 * Created by dinzhip on 2016/12/28.
 */

public class JSONUtils {
    private static volatile JSONUtils instance = null;
    private JSONUtils() {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
    }

    public static JSONUtils getInstance() {
        if (instance == null) {
            synchronized (JSONUtils.class) {
                if (instance == null) {
                    instance = new JSONUtils();
                }
            }
        }
        return instance;
    }

    public String compile(Object value) {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        try {
            return JSON.toJSONString(value);
        } catch (Exception e) {
            return null;
        }
    }

    public Object parse(String jsonString, Class<?> cls) {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        try {
            return JSON.parseObject(jsonString, (Class) cls);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T parseObject(String text, TypeReference<T> type) {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        if (text == null || text.length() <= 0) {
            return null;
        }
        return JSON.parseObject(text, (TypeReference<T>) type, new Feature[0]);
    }

    public <T> T parseObject(String text, Class<T> clazz) {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        if (text == null || text.length() <= 0) {
            return null;
        }
        return JSON.parseObject(text,(Class<T>) clazz);
    }

    public <T> List<T> parseArray(String text, Class<T> clazz) {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        if (text == null || text.length() <= 0) {
            return null;
        }
        return JSON.parseArray(text, (Class) clazz);
    }
}

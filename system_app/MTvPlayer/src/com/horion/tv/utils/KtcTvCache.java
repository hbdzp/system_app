package com.horion.tv.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KtcTvCache<Key, Value> {
    private ArrayList<CacheItem<Key, Value>> itemList = new ArrayList();
    private ArrayList<Key> keyList = new ArrayList();
    private ArrayList<Value> list = new ArrayList();
    private HashMap<Key, Value> map = new HashMap();

    public static class CacheItem<Key1, Value1> {
        public Key1 key = null;
        public Value1 value = null;

        public CacheItem(Key1 key1, Value1 value1) {
            this.key = key1;
            this.value = value1;
        }
    }

    public void add(Key key, Value value) {
        remove(key);
        this.map.put(key, value);
        this.list.add(value);
        this.keyList.add(key);
        this.itemList.add(new CacheItem(key, value));
    }

    public void clear() {
        this.keyList.clear();
        this.list.clear();
        this.map.clear();
        this.itemList.clear();
    }

    public boolean contains(Key key) {
        return this.map.containsKey(key);
    }

    public Value get(Key key) {
        return this.map.get(key);
    }

    public List<CacheItem<Key, Value>> getItemList() {
        return this.itemList;
    }

    public List<Key> getKeyList() {
        return this.keyList;
    }

    public List<Value> getList() {
        return this.list;
    }

    public void remove(Key key) {
        if (this.map.containsKey(key)) {
            this.list.remove(this.map.get(key));
            this.map.remove(key);
            this.keyList.remove(key);
        }
    }

    public int size() {
        return this.list.size();
    }
}

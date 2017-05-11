package com.horion.tv.service.epg;

import com.horion.tv.define.object.Channel;
import com.horion.tv.ui.rightepg.SkyEventData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class SkyDataCache {
    private static SkyDataCache mInstance = null;
    private Channel channelCache;
    private int envetFcousIndex;
    private List<SkyEventData> eventItemDataCache;
    private ReentrantLock lock;

    private SkyDataCache() {
        this.lock = new ReentrantLock();
        this.eventItemDataCache = null;
        this.envetFcousIndex = 0;
        this.eventItemDataCache = new ArrayList();
    }

    public static SkyDataCache getInstance() {
        if (mInstance == null) {
            mInstance = new SkyDataCache();
        }
        return mInstance;
    }

    public void clear() {
        if (this.eventItemDataCache.size() > 0) {
            this.eventItemDataCache.clear();
        }
    }

    public Channel getCurChannel() {
        return this.channelCache;
    }

    public int getEventFocusIndex() {
        return this.envetFcousIndex;
    }

    public List<SkyEventData> getEventItemDataCache() {
        return this.eventItemDataCache;
    }

    public void setCurChannel(Channel channel) {
        this.lock.lock();
        this.channelCache = channel;
        this.lock.unlock();
    }

    public void setEventItemDataCache(List<SkyEventData> list, int i) {
        this.lock.lock();
        this.envetFcousIndex = i;
        this.eventItemDataCache.clear();
        for (SkyEventData add : list) {
            this.eventItemDataCache.add(add);
        }
        this.lock.unlock();
    }
}

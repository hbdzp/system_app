package com.horion.tv.framework.ui.uidata.epg;

import com.horion.tv.define.object.Channel;
///import com.horion.tv.utils.SkyTVDebug;

import java.util.ArrayList;
import java.util.List;

/* EPG相关数据：类别，频道，节目，时间
 *
 */
public class SkyEPGListDataBase {
    public static final String categoryList = "category";
    public static final String channelList = "channel";
    public static final String enventList = "event";
    private static SkyEPGListDataBase mInstance = null;
    public static final String timeList = "time";
    public List<SkyChannelListItemDataBase> channelListData;
    public int default_index;
    public List<SkyEPGCurProgItemData> evntListdata;
    public String listType;
    public List<SkyEPGListItemDataBase> listdata;
    public List<Channel> mChannelListData;
    public List<SkyEPGTimeItemData> timeListdata;
    public int totalCount;

    public SkyEPGListDataBase() {
        this.totalCount = -1;
        this.default_index = 0;
        this.listType = "";
        this.listdata = new ArrayList();
        this.channelListData = new ArrayList();
        this.timeListdata = new ArrayList();
        this.evntListdata = new ArrayList();
    }

    public SkyEPGListDataBase(String str) {
        this.totalCount = -1;
        this.default_index = 0;
        this.listType = "";
        if (str.equals(categoryList)) {
            ///SkyTVDebug.debug(">>>>categoryList<<<<<");
            this.listdata = new ArrayList();
        } else if (str.equals(channelList)) {
           /// SkyTVDebug.debug(">>>>channelList<<<<<");
            this.channelListData = new ArrayList();
            this.mChannelListData = new ArrayList();
        } else if (str.equals(enventList)) {
            ///SkyTVDebug.debug(">>>>enventList<<<<<");
            this.evntListdata = new ArrayList();
        } else if (str.equals(timeList)) {
           /// SkyTVDebug.debug(">>>>timeList<<<<<");
            this.timeListdata = new ArrayList();
        }
        this.listType = str;
    }

    public static SkyEPGListDataBase getInstance() {
        if (mInstance == null) {
            mInstance = new SkyEPGListDataBase();
        }
        return mInstance;
    }

    public void addCategoryItem(SkyEPGListItemDataBase skyEPGListItemDataBase) {
        this.listdata.add(skyEPGListItemDataBase);
    }

    public void addChannelItem(SkyChannelListItemDataBase skyChannelListItemDataBase) {
        this.channelListData.add(skyChannelListItemDataBase);
    }

    public void addChannelListData(List<Channel> list) {
        this.mChannelListData = list;
    }

    public void addEventItem(SkyEPGCurProgItemData skyEPGCurProgItemData) {
        this.evntListdata.add(skyEPGCurProgItemData);
    }

    public void addTimeItem(SkyEPGTimeItemData skyEPGTimeItemData) {
        this.timeListdata.add(skyEPGTimeItemData);
    }

    public List<SkyEPGListItemDataBase> getCategoryListData() {
        return this.listdata;
    }

    public List<Channel> getChannelList() {
        return this.mChannelListData;
    }

    public List<SkyChannelListItemDataBase> getChannelListData() {
        return this.channelListData;
    }

    public List<SkyEPGCurProgItemData> getEventListData() {
        return this.evntListdata;
    }

    public String getListType() {
        return this.listType;
    }

    public List<SkyEPGTimeItemData> getTimeListData() {
        return this.timeListdata;
    }

    public void setDefaultIndex(int i, int i2) {
        this.totalCount = i;
        this.default_index = i2;
    }
}

package com.horion.tv.framework.ui.util;

import java.util.ArrayList;
import java.util.List;

public class EPGListdataBase {
    public int default_index = 0;
    public String listType = "";
    public List<EPGListItemDataBase> listdata = new ArrayList();
    public int totalCount = -1;

    public enum EPGListDataType {
        sortList,
        programeList,
        timeList,
        liveList,
        demandList
    }

    public EPGListdataBase(String str) {
        this.listType = str;
    }

    public void addItem(EPGListItemDataBase ePGListItemDataBase) {
        this.listdata.add(ePGListItemDataBase);
    }

    public List<EPGListItemDataBase> getListData() {
        return this.listdata;
    }

    public String getListType() {
        return this.listType;
    }

    public void setDefaultIndex(int i, int i2) {
        this.totalCount = i;
        this.default_index = i2;
    }
}

package com.horion.tv.ui.leftepg;

import java.util.List;

public class SkyEPGAdapter {
    private List<SkyEPGData> mDatas;

    public SkyEPGAdapter(List<SkyEPGData> list) {
        this.mDatas = list;
    }

    public List<SkyEPGData> getAdapterData() {
        return this.mDatas;
    }

    public int getCount() {
        return this.mDatas == null ? 0 : this.mDatas.size();
    }

    public SkyEPGData getEPGItemData(int i) {
        return (SkyEPGData) this.mDatas.get(i);
    }

    public Object getItem(int i) {
        return this.mDatas.get(i);
    }
}

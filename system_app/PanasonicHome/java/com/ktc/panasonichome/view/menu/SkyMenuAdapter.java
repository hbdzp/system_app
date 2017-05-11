package com.ktc.panasonichome.view.menu;

import java.util.List;

public class SkyMenuAdapter {
    private List<SkyMenuData> mDatas;

    public SkyMenuAdapter(List<SkyMenuData> datas) {
        this.mDatas = datas;
    }

    public SkyMenuData getMenuItemData(int position) {
        return (SkyMenuData) this.mDatas.get(position);
    }

    public Object getItem(int position) {
        return this.mDatas.get(position);
    }

    public int getCount() {
        return this.mDatas == null ? 0 : this.mDatas.size();
    }
}

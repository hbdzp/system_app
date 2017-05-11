package com.ktc.ui.menu;

import java.util.List;

public class KtcMenuAdapter {
    private List<KtcMenuData> mDatas;

    public KtcMenuAdapter(List<KtcMenuData> list) {
        this.mDatas = list;
    }

    public int getCount() {
        return this.mDatas == null ? 0 : this.mDatas.size();
    }

    public Object getItem(int i) {
        return this.mDatas.get(i);
    }

    public KtcMenuData getMenuItemData(int i) {
        return (KtcMenuData) this.mDatas.get(i);
    }
}

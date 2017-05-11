package com.ktc.ui.menu;

import java.io.Serializable;

public class KtcMenuData implements Serializable {
    private static final long serialVersionUID = 1;
    private Object data;
    private boolean hasSecondMenu = false;
    private boolean isClickItem;
    private int itemFocusIcon = -1;//条目 失去焦点的坐标icon
    private int itemIconBg = -1;//条目的背景图片，也就是那个圆圈
    private int itemUnFocusIcon = -1;//条目 获得焦点的icon
    private String title = null;//条目的名称

    public Object getData() {
        return this.data;
    }

    public int getItemFocusIcon() {
        return this.itemFocusIcon;
    }

    public int getItemIconBg() {
        return this.itemIconBg;
    }

    public boolean getItemState() {
        return this.isClickItem;
    }

    public String getItemTitle() {
        return this.title;
    }

    public int getItemUnFocusIcon() {
        return this.itemUnFocusIcon;
    }

    public boolean isHasSecondMenu() {
        return this.hasSecondMenu;
    }

    public void setData(Object obj) {
        this.data = obj;
    }

    public void setHasSecondMenu(boolean z) {
        this.hasSecondMenu = z;
    }

    public void setItemFocusIcon(int i) {
        this.itemFocusIcon = i;
    }

    public void setItemIconBg(int i) {
        this.itemIconBg = i;
    }

    public void setItemState(boolean z) {
        this.isClickItem = z;
    }

    public void setItemTitle(String str) {
        this.title = str;
    }

    public void setItemUnFocusIcon(int i) {
        this.itemUnFocusIcon = i;
    }
}

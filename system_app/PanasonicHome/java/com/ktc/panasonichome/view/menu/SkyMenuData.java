package com.ktc.panasonichome.view.menu;

import java.io.Serializable;

public class SkyMenuData implements Serializable {
    private static final long serialVersionUID = 1;
    private Object data;
    private boolean hasSecondMenu = false;
    private boolean isClickItem;
    private int itemFocusIcon = -1;
    private int itemIconBg = -1;
    private int itemUnFocusIcon = -1;
    private String title = null;

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public void setItemIconBg(int iconBg) {
        this.itemIconBg = iconBg;
    }

    public int getItemIconBg() {
        return this.itemIconBg;
    }

    public void setItemFocusIcon(int focusID) {
        this.itemFocusIcon = focusID;
    }

    public int getItemFocusIcon() {
        return this.itemFocusIcon;
    }

    public void setItemUnFocusIcon(int unfocusID) {
        this.itemUnFocusIcon = unfocusID;
    }

    public int getItemUnFocusIcon() {
        return this.itemUnFocusIcon;
    }

    public void setItemTitle(String title) {
        this.title = title;
    }

    public String getItemTitle() {
        return this.title;
    }

    public void setItemState(boolean isClick) {
        this.isClickItem = isClick;
    }

    public boolean getItemState() {
        return this.isClickItem;
    }

    public boolean isHasSecondMenu() {
        return this.hasSecondMenu;
    }

    public void setHasSecondMenu(boolean hasSecondMenu) {
        this.hasSecondMenu = hasSecondMenu;
    }
}

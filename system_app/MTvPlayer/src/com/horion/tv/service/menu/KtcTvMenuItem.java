package com.horion.tv.service.menu;

import java.util.ArrayList;
import java.util.List;

public class KtcTvMenuItem {
    private boolean bDisplay = true;
    private boolean bFlipOutMenu = false;
    private List<KtcTvMenuItem> childMenuList = new ArrayList();
    private String content = null;
    private String flipOutCommand = null;
    private int foucsIconID = -1;
    private String getValueCommand = null;
    private boolean needRefresh = false;
    private String nodeKey = null;
    private KtcTvMenuItem parentMenu = null;
    private String setValueCommand = null;
    private int unFoucsIconID = -1;

    public List<KtcTvMenuItem> getChildMenuList() {
        return this.childMenuList;
    }

    public String getContent() {
        return this.content;
    }

    public boolean getDisplay() {
        return this.bDisplay;
    }

    public String getFlipOutCommand() {
        return this.flipOutCommand;
    }

    public boolean getFlipOutStatus() {
        return this.bFlipOutMenu;
    }

    public String getGetValueCommand() {
        return this.getValueCommand;
    }

    public boolean getNeedRefresh() {
        return this.needRefresh;
    }

    public String getNodeKey() {
        return this.nodeKey;
    }

    public KtcTvMenuItem getParentMenu() {
        return this.parentMenu;
    }

    public String getSetValueCommand() {
        return this.setValueCommand;
    }

    public int getfoucsIconID() {
        return this.foucsIconID;
    }

    public int getunFoucsIconID() {
        return this.unFoucsIconID;
    }

    public void setChildMenuList(List<KtcTvMenuItem> list) {
        this.childMenuList = list;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public void setDisplay(boolean z) {
        this.bDisplay = z;
    }

    public void setFlipOutCommand(String str) {
        this.flipOutCommand = str;
    }

    public void setFlipOutStatus(boolean z) {
        this.bFlipOutMenu = z;
    }

    public void setGetValueCommand(String str) {
        this.getValueCommand = str;
    }

    public void setNeedRefresh(boolean z) {
        this.needRefresh = z;
    }

    public void setNodeKey(String str) {
        this.nodeKey = str;
    }

    public void setParentMenu(KtcTvMenuItem ktcTvMenuItem) {
        this.parentMenu = ktcTvMenuItem;
    }

    public void setSetValueCommand(String str) {
        this.setValueCommand = str;
    }

    public void setfoucsIconID(int i) {
        this.foucsIconID = i;
    }

    public void setunFoucsIconID(int i) {
        this.unFoucsIconID = i;
    }
}

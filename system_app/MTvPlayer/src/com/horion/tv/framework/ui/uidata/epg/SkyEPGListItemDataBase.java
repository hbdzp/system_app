package com.horion.tv.framework.ui.uidata.epg;

public class SkyEPGListItemDataBase {
    String content;
    public int index = 0;
    public int indexOfPage = -1;
    private Object obj = null;

    public String getContent() {
        return this.content;
    }

    public Object getObject() {
        return this.obj;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public void setIndex(int i) {
    }

    public void setObject(Object obj) {
        this.obj = obj;
    }
}

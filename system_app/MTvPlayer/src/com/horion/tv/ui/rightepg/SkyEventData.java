package com.horion.tv.ui.rightepg;

import java.io.Serializable;

public class SkyEventData implements Serializable {
    private static final long serialVersionUID = 1;
    private Object data;
    private String secondTitle = null;
    private String title = null;

    public Object getData() {
        return this.data;
    }

    public String getItemSecondTitle() {
        return this.secondTitle;
    }

    public String getItemTitle() {
        return this.title;
    }

    public void setData(Object obj) {
        this.data = obj;
    }

    public void setItemSecondTitleTitle(String str) {
        this.secondTitle = str;
    }

    public void setItemTitle(String str) {
        this.title = str;
    }
}

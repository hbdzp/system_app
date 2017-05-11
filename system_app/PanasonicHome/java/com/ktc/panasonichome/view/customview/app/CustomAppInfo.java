package com.ktc.panasonichome.view.customview.app;

import android.graphics.drawable.Drawable;

public class CustomAppInfo {
    public Drawable icon;
    public boolean isChoosed;
    public String tag;
    public String title;

    public CustomAppInfo(String title, String tag, Drawable icon, boolean isChoosed) {
        this.title = title;
        this.tag = tag;
        this.icon = icon;
        this.isChoosed = isChoosed;
    }
}

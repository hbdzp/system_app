package com.ktc.panasonichome.view.customview;

import android.view.View;
import android.view.ViewGroup;

public abstract class ExpandAdapter<T> extends BaseAdapter<T> {
    public abstract View getOneView(int i, View view, ViewGroup viewGroup);

    public abstract int getSubCount(int i);
}

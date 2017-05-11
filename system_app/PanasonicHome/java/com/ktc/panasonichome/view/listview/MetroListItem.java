package com.ktc.panasonichome.view.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class MetroListItem extends FrameLayout {
    protected int mViewType;

    public abstract void refreshView();

    public MetroListItem(Context context) {
        super(context);
    }

    public MetroListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MetroListItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}

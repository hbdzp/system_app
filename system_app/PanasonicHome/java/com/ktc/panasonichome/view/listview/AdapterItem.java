package com.ktc.panasonichome.view.listview;

import android.view.View;

public interface AdapterItem<T> {
    void clearItem();

    View getLayout();

    void onUpdateItemValue(T t, int i, int i2);
}

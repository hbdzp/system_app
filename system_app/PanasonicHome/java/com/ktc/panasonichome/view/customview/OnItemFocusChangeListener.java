package com.ktc.panasonichome.view.customview;

import android.view.View;

public interface OnItemFocusChangeListener {
    void onFocus();

    void onFocusItem(int i, View view, boolean z);

    void onNothingFocus(View view);
}

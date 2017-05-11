package com.ktc.panasonichome.view.customview;

import android.view.View;

public interface OnBoundaryListener {
    boolean onDownBoundary(View view);

    boolean onLeftBoundary(View view);

    boolean onRightBoundary(View view);

    boolean onTopBoundary(View view);
}

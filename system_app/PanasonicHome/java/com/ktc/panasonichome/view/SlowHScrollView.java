package com.ktc.panasonichome.view;

import android.content.Context;
import android.graphics.Rect;
import android.widget.HorizontalScrollView;

import com.ktc.panasonichome.utils.ScreenParams;

public class SlowHScrollView extends HorizontalScrollView {
    private int range;

    public SlowHScrollView(Context context) {
        super(context);
        this.range = ScreenParams.getInstence(context).getResolutionValue(108);
    }

    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;
        int fadingEdge = getHorizontalFadingEdgeLength();
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }
        int scrollXDelta = 0;
        if (rect.right > screenRight - 20 && rect.left > screenLeft) {
            if (rect.width() > width) {
                scrollXDelta = (rect.left - screenLeft) + 0;
            } else {
                scrollXDelta = (rect.right - screenRight) + 0;
            }
            scrollXDelta = Math.min(scrollXDelta, getChildAt(0).getRight() - screenRight) + this.range;
        } else if (rect.left < screenLeft + 20 && rect.right < screenRight) {
            if (rect.width() > width) {
                scrollXDelta = 0 - (screenRight - rect.right);
            } else {
                scrollXDelta = 0 - (screenLeft - rect.left);
            }
            scrollXDelta = Math.max(scrollXDelta, -getScrollX()) - this.range;
        }
        return scrollXDelta;
    }
}

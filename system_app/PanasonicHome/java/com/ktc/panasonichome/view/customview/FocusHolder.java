package com.ktc.panasonichome.view.customview;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import com.ktc.panasonichome.utils.LogUtils;

public class FocusHolder extends View implements OnKeyListener {
    public FocusHolder(Context context) {
        super(context);
    }

    public void enableFocus() {
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void disableFocus() {
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    public void shieldKey() {
        setOnKeyListener(this);
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
       LogUtils.i("focusholder", "current focus is focus holder");
        return true;
    }
}

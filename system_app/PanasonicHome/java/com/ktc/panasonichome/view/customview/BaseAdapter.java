package com.ktc.panasonichome.view.customview;

import android.view.View;
import android.view.ViewGroup;

import com.ktc.panasonichome.view.customview.SlideFocusView.FocusChangedEvent;
import com.ktc.panasonichome.view.customview.SlideFocusView.FocusViewRevision;

import java.util.List;

public abstract class BaseAdapter<T> {
    protected FocusChangedEvent focusChangedEvent = null;
    protected ObserverListener  mObserver         = null;
    protected FocusViewRevision revision          = null;

    public interface ObserverListener {
        void onChanaged();
    }

    public abstract void destroy();

    public abstract int getCount();

    public abstract T getItemData(int i);

    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public abstract void refreshUI(List<T> list);

    public void setFocusEvent(FocusChangedEvent focusChangedEvent, FocusViewRevision revision) {
        this.focusChangedEvent = focusChangedEvent;
        this.revision = revision;
    }

    public void notifyDataSetChanaged() {
        if (this.mObserver != null) {
            this.mObserver.onChanaged();
        }
    }

    public void registObserver(ObserverListener listener) {
        this.mObserver = listener;
    }
}

package com.ktc.panasonichome.view.listview;

import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ktc.panasonichome.view.customview.SlideFocusView.FocusChangedEvent;
import com.ktc.panasonichome.view.customview.SlideFocusView.FocusViewRevision;

import java.util.List;

public abstract class MetroAdapter<T> extends BaseAdapter implements IAdapter<T> {
    public static boolean onChangeNeedClearFlag = true;
    private   OnFocusChangeListener focusChangeListener;
    protected FocusChangedEvent     focusChangedEvent;
    private   FocusViewRevision     focusViewRevision;
    private   List<T>               mDataList;
    protected ObserverListener      mObserver;
    private   Object                mViewType;
    private   int                   mViewTypeCount;
    private   ItemTypeUtil          util;

    public interface ObserverListener {
        void onChanaged();

        void onChanagedAll();

        void onChanagedTotal();
    }

    protected MetroAdapter(List<T> data) {
        this(data, 1);
    }

    protected MetroAdapter(List<T> data, int viewTypeCount) {
        this.mViewTypeCount = 1;
        this.mObserver = null;
        this.mDataList = data;
        this.mViewTypeCount = viewTypeCount;
        this.util = new ItemTypeUtil();
    }

    public void changeData(List<T> data) {
        this.mDataList = data;
        notifyDataSetChanagedAll();
    }

    public void setFocusChangedEvent(FocusChangedEvent focusChangedEvent, FocusViewRevision rev, OnFocusChangeListener focusChangeListener) {
        this.focusChangedEvent = focusChangedEvent;
        this.focusViewRevision = rev;
        this.focusChangeListener = focusChangeListener;
    }

    public void registObserver(ObserverListener listener) {
        this.mObserver = listener;
    }

    public List<T> getData() {
        return this.mDataList;
    }

    public int getCount() {
        return this.mDataList != null ? this.mDataList.size() : 0;
    }

    public T getItem(int position) {
        if (this.mDataList == null || this.mDataList.size() <= 0 || position < 0 || position >= this.mDataList.size()) {
            return null;
        }
        return this.mDataList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public Object getItemType(T t) {
        return Integer.valueOf(-1);
    }

    public int getItemViewType(int position) {
        if (this.mDataList == null || this.mDataList.size() <= position) {
            this.mViewType = Integer.valueOf(-1);
        } else {
            this.mViewType = getItemType(this.mDataList.get(position));
        }
        return this.util.getIntType(this.mViewType);
    }

    public int getViewTypeCount() {
        return this.mViewTypeCount;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterItem<T> item;
        int viewType = getItemViewType(position);
        if (convertView == null) {
            item = onCreateItem(this.mViewType);
            convertView = item.getLayout();
            convertView.setTag(item);
            if (this.focusChangedEvent != null) {
                this.focusChangedEvent.registerView(convertView, this.focusViewRevision, this.focusChangeListener);
            }
        } else {
            item = (AdapterItem) convertView.getTag();
        }
        item.clearItem();
        if (this.mDataList != null && this.mDataList.size() > position) {
            item.onUpdateItemValue(this.mDataList.get(position), position, viewType);
        }
        return convertView;
    }

    public void notifyDataSetChanaged() {
        if (this.mObserver != null) {
            this.mObserver.onChanaged();
        }
    }

    public void notifyDataSetChanagedAll() {
        if (this.mObserver != null) {
            this.mObserver.onChanagedAll();
        }
    }

    public void notifyDataSetChanagedTotal() {
        if (this.mObserver != null) {
            this.mObserver.onChanagedTotal();
        }
    }
}

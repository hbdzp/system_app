package com.ktc.panasonichome.view.listview;

import java.util.List;

public interface IAdapter<T> {
    List<T> getData();

    T getItem(int i);

    Object getItemType(T t);

    AdapterItem<T> onCreateItem(Object obj);
}

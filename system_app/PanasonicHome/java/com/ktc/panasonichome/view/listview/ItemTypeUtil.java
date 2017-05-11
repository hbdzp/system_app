package com.ktc.panasonichome.view.listview;

import android.util.SparseArray;

public class ItemTypeUtil {
    private SparseArray<Object> typeSArr = new SparseArray();

    public int getIntType(Object type) {
        int index = this.typeSArr.indexOfValue(type);
        if (index != -1) {
            return index;
        }
        index = this.typeSArr.size();
        this.typeSArr.put(index, type);
        return index;
    }
}

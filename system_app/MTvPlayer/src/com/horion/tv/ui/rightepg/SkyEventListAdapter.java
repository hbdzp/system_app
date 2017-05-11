package com.horion.tv.ui.rightepg;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

//import com.horion.tv.ui.util.TVUIDebug;

import java.util.List;

public class SkyEventListAdapter extends BaseAdapter {
    private Context cont;
    private int mCurIndex = 0;
    private List<SkyEventData> mDatas;

    public SkyEventListAdapter(Context context, List<SkyEventData> list, int i) {
        this.cont = context;
        this.mDatas = list;
        this.mCurIndex = i;
    }

    public int getCount() {
        return this.mDatas.size();
    }

    public Object getItem(int i) {
        return this.mDatas.get(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View skyEventItem = view == null ? new SkyEventItem(this.cont) : view;
        try {
            ((SkyEventItem) skyEventItem).setTextAttribute(36, Color.RED);
        } catch (Exception e) {
            try {
                e.printStackTrace();
            } catch (Exception e2) {
                //TVUIDebug.debug("SkyEventListAdapter is exception");
                e2.printStackTrace();
            }
        }
        try {
            ((SkyEventItem) skyEventItem).setId(i);
        } catch (Exception e22) {
           /// TVUIDebug.debug("----Exception 33----");
            e22.printStackTrace();
        }
        try {
            if (this.mCurIndex == i) {
                ((SkyEventItem) skyEventItem).updataItemImage(17);
            } else if (this.mCurIndex + 1 == i) {
                ((SkyEventItem) skyEventItem).updataItemImage(19);
            } else if (this.mCurIndex + 2 == i) {
                ((SkyEventItem) skyEventItem).updataItemImage(20);
            } else if (this.mCurIndex + 3 == i) {
                ((SkyEventItem) skyEventItem).updataItemImage(21);
            } else if (this.mCurIndex + 4 == i) {
                ((SkyEventItem) skyEventItem).updataItemImage(22);
            }
        } catch (Exception e222) {
            //TVUIDebug.debug("----Exception 66----");
            e222.printStackTrace();
        }
        try {
            ((SkyEventItem) skyEventItem).updataItemValue((SkyEventData) this.mDatas.get(i));
        } catch (Exception e2222) {
           // TVUIDebug.debug("----Exception 77----");
            e2222.printStackTrace();
        }
        return skyEventItem;
    }
}

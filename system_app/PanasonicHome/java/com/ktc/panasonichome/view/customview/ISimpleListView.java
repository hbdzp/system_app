package com.ktc.panasonichome.view.customview;

public interface ISimpleListView {
    BaseAdapter getAdapter();

    int getFocusPos();

    int getSelectedPos();

    void setAdapter(BaseAdapter baseAdapter);

    void setAlwaysFocusPos(int i);

    void setAnimation(int i);

    void setBoundaryListener(OnBoundaryListener onBoundaryListener);

    void setDivide(int i);

    void setDivideImageResource(int i);

    void setFocus(int i);

    void setSelected(int i, boolean z);

    void shieldKey(boolean z, boolean z2, boolean z3, boolean z4);
}

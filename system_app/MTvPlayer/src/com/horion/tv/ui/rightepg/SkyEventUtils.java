package com.horion.tv.ui.rightepg;

public class SkyEventUtils {
    public static int[] imageIds = new int[]{18, 19, 20, 21, 22};
    public static final int isCurrentIcon = 17;
    public static final int isImage1Icon = 18;
    public static final int isImage2Icon = 19;
    public static final int isImage3Icon = 20;
    public static final int isImage4Icon = 21;
    public static final int isImage5Icon = 22;
    private static SkyEventUtils mIstance = null;
    private volatile boolean isFristFlag = false;
    private volatile int listSelectItem = 0;

    public static SkyEventUtils getInstance() {
        if (mIstance == null) {
            mIstance = new SkyEventUtils();
        }
        return mIstance;
    }

    public boolean getFristFlag() {
        return this.isFristFlag;
    }

    public int getSelectItemIndex() {
        return this.listSelectItem;
    }

    public void setFristFlag(boolean z) {
        this.isFristFlag = z;
    }

    public void setSelectItemIndex(int i) {
        this.listSelectItem = i;
    }
}

package com.ktc.panasonichome.view.customview;


import com.ktc.panasonichome.view.customview.SlideFocusView.FocusViewRevision;

public class FocusViewParams {
    public int               bgId     = 0;
    public FOCUS_POS         focusPos = FOCUS_POS.FRONT;
    public FocusViewRevision revision = null;

    public enum FOCUS_POS {
        FRONT,
        BACK
    }

    public FocusViewParams(int bgId, FOCUS_POS pos) {
        this.bgId = bgId;
        this.focusPos = pos;
    }

    public FocusViewParams(int bgId, FOCUS_POS pos, FocusViewRevision revision) {
        this.bgId = bgId;
        this.focusPos = pos;
        this.revision = revision;
    }
}

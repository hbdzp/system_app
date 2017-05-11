package com.ktc.panasonichome.view.customview.app;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.customview.SlideFocusView;
import com.ktc.panasonichome.view.listview.AdapterItem;
import com.ktc.panasonichome.view.listview.MetroAdapter;
import com.ktc.panasonichome.view.listview.MetroListView;
import com.ktc.panasonichome.view.listview.MetroListView.OnItemOnKeyListener;
import com.ktc.panasonichome.view.listview.MetroListView.OnItemSelectedListener;
import com.ktc.panasonichome.view.listview.MetroListView.OnScrollBarOnKeyListener;
import com.ktc.panasonichome.view.listview.MetroListView.OnScrollStateListener;
import com.ktc.panasonichome.view.listview.MetroListViewScrollBar;

import java.util.List;

public class CustomAppGridView extends SlideFocusView implements OnItemSelectedListener,
        OnScrollStateListener, OnItemOnKeyListener, OnItemClickListener, OnScrollBarOnKeyListener {
    private List<CustomAppInfo> curInfos;
    private int focusMargin = 0;
    private MetroAdapter<CustomAppInfo> mAdapter;
    private OnAppGridItemOnClikListener mAppGridItemOnClikListener;
    private Context                     mContext;
    private MetroListView               mGridView;
    private MetroListViewScrollBar      mScrollBar;

    public interface OnAppGridItemOnClikListener {
        void onGridItemClickEvent(View view, CustomAppInfo customAppInfo, int i);
    }

    class C03152 implements Runnable {
        C03152() {
        }

        public void run() {
            CustomAppGridView.this.mGridView.setSelection(0);
        }
    }

    public CustomAppGridView(Context context) {
        super(context, R.drawable.ui_sdk_btn_focus_shadow_no_bg);
        this.mContext = context;
        initScrollBar();
        initGridView();
    }

    public void setOnAppGridItemOnClikListener(OnAppGridItemOnClikListener listener) {
        this.mAppGridItemOnClikListener = listener;
    }

    private void initScrollBar() {
        this.mScrollBar = new MetroListViewScrollBar(this.mContext);
        this.mScrollBar.setScrollBarBg(R.drawable.ic_custom_scrollbar_bg);
        this.mScrollBar.setScrollBarIcon(R.drawable.ic_custom_scrollbar_focus_icon);
        this.mScrollBar.setSlidFocusView(this);
        this.focusMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(75);
        this.mScrollBar.setFocusChangedEvent(this.focusChangedEvent, new FocusViewRevision(this
                .focusMargin, this.focusMargin, this.focusMargin, this.focusMargin), null);
        this.mScrollBar.setScrollBarFocusble(true);
        LayoutParams scrollBar_p = new LayoutParams(-2, ScreenParams.getInstence(this.mContext)
                .getResolutionValue(787));
        scrollBar_p.gravity = 5;
        scrollBar_p.rightMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(72);
        scrollBar_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(189);
        addView(this.mScrollBar, scrollBar_p);
    }

    private void initGridView() {
        this.mGridView = new MetroListView(this.mContext);
        this.mGridView.setSlidFocusView(this);
        this.mGridView.setColmusNum(6);
        this.mGridView.setScrollDuration(200);
        this.mGridView.setScrollBarView(this.mScrollBar);
        LayoutParams list_p = new LayoutParams(ScreenParams.getInstence(this.mContext)
                .getResolutionValue(1645), ScreenParams.getInstence(this.mContext)
                .getResolutionValue(824));
        list_p.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue
                (135);
        list_p.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue
                (165);
        addView(this.mGridView, list_p);
        this.mGridView.setOnItemSelectedListener(this);
        this.mGridView.setOnScrollStateListener(this);
        this.mGridView.setOnItemOnKeyListener(this);
        this.mGridView.setOnItemClickListener(this);
        this.mGridView.setOnScrollBarOnKeyListener(this);
    }

    public void refreshValue(List<CustomAppInfo> infos) {
        this.curInfos = infos;
        this.mAdapter = new MetroAdapter<CustomAppInfo>(this.curInfos) {
            public AdapterItem<CustomAppInfo> onCreateItem(Object type) {
                return new CustomAppItemView(CustomAppGridView.this.mContext);
            }
        };
        this.focusMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(66);
        this.mAdapter.setFocusChangedEvent(this.focusChangedEvent, new FocusViewRevision(this
                .focusMargin, this.focusMargin, this.focusMargin, this.focusMargin), null);
        this.mGridView.setAdapter(this.mAdapter);
        stopAnimationOnce();
        this.mGridView.post(new C03152());
    }

    public boolean onScrollBarOnKey(View view, int keyCode) {
        if (keyCode == 21) {
            getFocusView().setBackgroundResource(R.drawable.ui_sdk_btn_focus_shadow_no_bg);
            this.mScrollBar.setScrollBarIcon(R.drawable.ic_custom_scrollbar_unfocus_icon);
        }
        return false;
    }

    public boolean onBorderItemOnKeyEvent(View view, int keyCode, int position) {
        if (keyCode == 22 && this.mScrollBar.getVisibility() == View.VISIBLE) {
            getFocusView().setBackgroundResource(R.drawable.ic_custom_scrollbar_focus_bg);
            this.mScrollBar.setScrollBarIcon(R.drawable.ic_custom_scrollbar_focus_icon);
            ((CustomAppItemView) view).unfocus();
        }
        return false;
    }

    public boolean onItemOnKeyEvent(View view, int keyCode, int position) {
        return false;
    }

    public void onScrollEnd(MetroListView parent, int firstPostion, int endPosition) {
    }

    public void onItemSelected(MetroListView parent, View selectView, int selectPosition) {
        ((CustomAppItemView) selectView).focus();
    }

    public void onItemUnSelected(MetroListView parent, View unSelectView) {
        ((CustomAppItemView) unSelectView).unfocus();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        CustomAppItemView appItemView = (CustomAppItemView) view;
        if (this.mAppGridItemOnClikListener != null) {
            this.mAppGridItemOnClikListener.onGridItemClickEvent(view, appItemView.getCurInfo(),
                    position);
        }
    }
}

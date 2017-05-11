package com.ktc.panasonichome.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
//import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.panasonichome.R;
import com.ktc.panasonichome.explosionfield.ExplosionField;
import com.ktc.panasonichome.model.AppInfo;
import com.ktc.panasonichome.model.GalleryAdapter;
import com.ktc.panasonichome.model.GalleryAdapter.IGalleryAdapter;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.view.api.SkyToastView.ShowTime;

import java.util.List;

public class GroupAppLayout extends FrameLayout implements IGalleryAdapter {
    public static TextView                    fuckView;
    public        int                         curPos;
    private       RecyclerDivider             divider;
    private       MyFocusFrame                focusView;
    private       LinearLayoutManager         linearLayoutManager;
    private       IGroupAppDataChangeListener listener;
    private       GalleryAdapter              mAdapter;
    private       Context                     mContext;
    private       CustomRecycleView           mRecyclerView;
    OnScrollListener onScrollListener = new C02671();
    private MyFocusFrame smallFocusView;
    private HomeStateBarLayout stateLayout;

    class C02671 extends OnScrollListener {
        C02671() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            LogUtils.d("dzp", "mRecyclerView scroll state is change !!!");
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == 0 && recyclerView.getFocusedChild() != null) {
                GroupAppLayout.this.focusView.changeFocusPos(((AppIconItem) recyclerView.getFocusedChild()).contentLayout);
            }
        }
    }

    class C02682 implements Runnable {
        C02682() {
        }

        public void run() {
            View view = GroupAppLayout.this.mRecyclerView.getChildAt(GroupAppLayout.this.linearLayoutManager.getChildCount() - 2);
            view.requestFocus();
            GroupAppLayout.this.focusView.changeFocusPos(((AppIconItem) view).contentLayout);
        }
    }

    class C02693 implements Runnable {
        C02693() {
        }

        public void run() {
            GroupAppLayout.this.focusView.setVisibility(View.VISIBLE);
        }
    }

    class C02704 implements Runnable {
        C02704() {
        }

        public void run() {
            GroupAppLayout.this.mRecyclerView.getChildAt(GroupAppLayout.this.curPos).requestFocus();
            GroupAppLayout.this.focusView.setVisibility(View.VISIBLE);
        }
    }

    public interface IGroupAppDataChangeListener {
        void onDataChange(List<AppInfo> list);

        void toastNotEdit(String str, ShowTime showTime);

        void toastStick(String str, ShowTime showTime);
    }

    public GroupAppLayout(Context context, MyFocusFrame focusView, MyFocusFrame smallFocusView, ExplosionField mExplosionField) {
        super(context);
        this.mContext = context;
        this.focusView = focusView;
        this.smallFocusView = smallFocusView;
        initView(mExplosionField);
    }

    private void initView(ExplosionField mExplosionField) {
        this.mAdapter = new GalleryAdapter(this.mContext, this.focusView, this.smallFocusView, mExplosionField);
        this.mAdapter.setIGalleryAdapter(this);
        this.linearLayoutManager = new LinearLayoutManager(this.mContext);
        this.linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        this.divider = new RecyclerDivider(this.mContext, 0);
        this.mRecyclerView = new CustomRecycleView(this.mContext);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(this.linearLayoutManager);
        this.mRecyclerView.addItemDecoration(this.divider);
        this.mRecyclerView.setFocusable(false);
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.mRecyclerView.addOnScrollListener(this.onScrollListener);
        LayoutParams params = new LayoutParams(-1, -2);
        params.gravity = 16;
        fuckView = new TextView(this.mContext);
        fuckView.setFocusable(true);
        LayoutParams fuckViewLp = new LayoutParams(10, 10);
        addView(this.mRecyclerView, params);
        addView(fuckView, fuckViewLp);
        setBackgroundResource(R.drawable.appbar_bg);
    }

    public void setIGroupAppDataChangeListener(IGroupAppDataChangeListener lis) {
        this.listener = lis;
    }

    public void setStateLayout(HomeStateBarLayout stateLayout) {
        this.stateLayout = stateLayout;
    }

    public void update(List<AppInfo> appInfos, boolean hasfocus) {
        this.mAdapter.setDatas(appInfos, hasfocus);
    }

    public void addData(int positionStart) {
        this.mAdapter.addData(positionStart);
    }

    public void removeData(int positionStart) {
        this.mAdapter.removeData(positionStart);
    }

    public void setFocus() {
        postDelayed(new C02682(), 300);
        this.focusView.postDelayed(new C02693(), 500);
    }

    public void onItemSelect(View view, int position) {
        this.mRecyclerView.autoAdjustScroll(position);
        if (this.mRecyclerView.getScrollState() == 0) {
            this.focusView.changeFocusPos(((AppIconItem) view).contentLayout);
        }
    }

    public void onStickItem(int position, List<AppInfo> appInfos) {
        this.listener.onDataChange(appInfos);
        new Handler().postDelayed(new C02704(), 150);
    }

    public void onDeleteItem(int position, List<AppInfo> appInfos) {
        this.listener.onDataChange(appInfos);
        this.mRecyclerView.getChildAt(this.curPos).requestFocus();
        this.focusView.setVisibility(View.VISIBLE);
    }

    public void onUpKeyDown(AppIconItem appIconItem) {
        if (this.stateLayout != null) {
            this.stateLayout.setMsgItemFocus(appIconItem);
        }
    }

    public void savePos(int position) {
        this.curPos = position - this.linearLayoutManager.findFirstVisibleItemPosition();
    }

    public void toastNotEdit(String msg, ShowTime time) {
        this.listener.toastNotEdit(msg, time);
    }

    public void toastStick(String msg, ShowTime time) {
        this.listener.toastStick(msg, time);
    }


    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.d("dzp","GroupAppLayout onKeyDown:" +keyCode);
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        LogUtils.d("dzp", "GroupAppLayout dispatchKeyEvent:" + event.getKeyCode());
//                return super.dispatchKeyEvent(event);
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_CENTER || event.getKeyCode() ==
                    KeyEvent.KEYCODE_ENTER) {
                LogUtils.d("dzp", "GroupAppLayout dispatchKeyEvent ACTION_DOWN " +
                        "KEYCODE_DPAD or CENTER_KEYCODE_ENTER:" + event
                        .getKeyCode());
                LogUtils.d("dzp", "GroupAppLayout dispatchKeyEvent: curPos" + GroupAppLayout.this.curPos);
                return super.dispatchKeyEvent(event);
            } else if(event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() ==
                    KeyEvent.KEYCODE_DPAD_RIGHT){
                return false;
            }
        }
        return super.dispatchKeyEvent(event);
    }*/
}

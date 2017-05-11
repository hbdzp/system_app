package com.ktc.panasonichome.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ktc.panasonichome.LauncherActivity;
import com.ktc.panasonichome.R;
import com.ktc.panasonichome.explosionfield.ExplosionField;
import com.ktc.panasonichome.explosionfield.ExplosionField.OnExPlosionEndListener;
import com.ktc.panasonichome.model.GalleryAdapter.ViewHolder;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.AppIconItem;
import com.ktc.panasonichome.view.AppIconItem.AppType;
import com.ktc.panasonichome.view.AppIconItem.IAppIconItem;
import com.ktc.panasonichome.view.MyFocusFrame;
import com.ktc.panasonichome.view.api.SkyToastView.ShowTime;

import java.util.ArrayList;
import java.util.List;


public class GalleryAdapter extends Adapter<ViewHolder> implements IAppIconItem {
    private static final int FOOTER = 3;
    private static final int HEADER = 2;
    private static final int NORMAL = 1;
    private int             currentPosition;
    private MyFocusFrame    focusView;
    private IGalleryAdapter listener;
    private Context         mContext;
    private List<AppInfo> mDatas = new ArrayList();
    private ExplosionField mExplosionField;
    private MyFocusFrame   smallFocusView;

    public interface IGalleryAdapter {
        void onDeleteItem(int i, List<AppInfo> list);

        void onItemSelect(View view, int i);

        void onStickItem(int i, List<AppInfo> list);

        void onUpKeyDown(AppIconItem appIconItem);

        void savePos(int i);

        void toastNotEdit(String str, ShowTime showTime);

        void toastStick(String str, ShowTime showTime);
    }

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public int viewType;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
        }
    }

    public void setIGalleryAdapter(IGalleryAdapter listener) {
        this.listener = listener;
    }

    public GalleryAdapter(Context context, MyFocusFrame focusFrame, MyFocusFrame smallFocusView,
                          ExplosionField mExplosionField) {
        this.mContext = context;
        this.mExplosionField = mExplosionField;
        this.focusView = focusFrame;
        this.smallFocusView = smallFocusView;
    }

    public void setDatas(List<AppInfo> datas, boolean focus) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void addData(int positionStart) {

        notifyItemInserted(positionStart);
    }

    public void removeData(int positionStart) {

        notifyItemRemoved(positionStart);
    }

    public List<AppInfo> getDatas() {
        return this.mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            TextView headView = new TextView(this.mContext);
            headView.setLayoutParams(new LayoutParams(ScreenParams.getInstence(this.mContext)
                    .getResolutionValue(90), LayoutParams.MATCH_PARENT));
            return new ViewHolder(headView, 2);
        } else if (viewType == 1) {
            return new ViewHolder(new AppIconItem(this.mContext, this.focusView, this
                    .smallFocusView), 1);
        } else {
            if (viewType != 3) {
                return null;
            }
            TextView footView = new TextView(this.mContext);
            footView.setLayoutParams(new LayoutParams(ScreenParams.getInstence(this.mContext)
                    .getResolutionValue(84), LayoutParams.MATCH_PARENT));
            return new ViewHolder(footView, 3);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.viewType == 1) {
            final AppIconItem view = (AppIconItem) holder.itemView;
            if (position > 0 && position < 5) {
                updateSystemItem(view, position);
            } else if (position >= 5 && position < this.mDatas.size() + 5) {
                view.setBackgroundResource(R.drawable.appbar_app_bg);
                view.setAppType(AppType.USER);
                view.updataView((AppInfo) this.mDatas.get(position - 5));
            } else if (position == this.mDatas.size() + 5) {
                view.setBackgroundResource(R.drawable.appbar_add_icon);
                view.setAppType(AppType.ADDICON);
                view.Icon.setImageDrawable(null);
                view.txt.setText("");
            }
            view.setIAppIconItem(this);
            view.setFocusable(true);
            view.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFous) {
                    if (hasFous) {
                        LauncherActivity.lastFocusView = v;
                        view.txt.setSelected(true);
                        GalleryAdapter.this.currentPosition = holder.getLayoutPosition();
                        GalleryAdapter.this.listener.onItemSelect(view, GalleryAdapter.this
                                .currentPosition);
                        return;
                    }
                    view.txt.setSelected(false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {

        return this.mDatas.size() + 7;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 2;
        }
        if (position == this.mDatas.size() + 6) {
            return 3;
        }
        return 1;
    }

    public void updateSystemItem(AppIconItem item, int position) {
        item.Icon.setImageDrawable(null);
        switch (position) {
            case 1:
                item.setAppType(AppType.SOURCE);
                item.setBackgroundResource(R.drawable.appbar_source);
                item.txt.setText(R.string.appbar_txt_source);
                return;
            case 2:
                item.setAppType(AppType.MEDIA);
                item.setBackgroundResource(R.drawable.appbar_media);
                item.txt.setText(R.string.appbar_txt_media);
                return;
            case 3:
                item.setAppType(AppType.SETTING);
                item.setBackgroundResource(R.drawable.appbar_setting);
                item.txt.setText(R.string.appbar_txt_setting);
                return;
            case 4:
                item.setAppType(AppType.MYAPP);
                item.setBackgroundResource(R.drawable.appbar_appstore);
                item.txt.setText(R.string.appbar_txt_myapp);
                return;
            default:
                return;
        }
    }

    public void deleteItem(final View v) {
        this.mDatas.remove(this.currentPosition - 5);
        notifyItemRemoved(this.currentPosition);
        this.mExplosionField.explode(v);
        this.mExplosionField.setOnExPlosionEndListener(new OnExPlosionEndListener() {
            public void onExplosionEnd() {
                GalleryAdapter.this.listener.onDeleteItem(GalleryAdapter.this.currentPosition,
                        GalleryAdapter.this.mDatas);
                ((AppIconItem) v).endAnim();
            }
        });
    }

    public void stickItem() {
        AppInfo appInfo = (AppInfo) this.mDatas.get(this.currentPosition - 5);
        this.mDatas.remove(this.currentPosition - 5);
        this.mDatas.add(0, appInfo);
        notifyDataSetChanged();
        this.listener.onStickItem(this.currentPosition, this.mDatas);
    }

    public void onUpKeyDown(AppIconItem appIconItem) {

        this.listener.onUpKeyDown(appIconItem);
    }

    public void savePos() {
        this.listener.savePos(this.currentPosition);
    }

    public void toastNotEdit(String msg, ShowTime time) {
        this.listener.toastNotEdit(msg, time);
    }

    public void toastStick(String msg, ShowTime time) {
        this.listener.toastStick(msg, time);
    }
}

package com.ktc.panasonichome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.IPackageDeleteObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.ktc.panasonichome.model.AppInfo;
import com.ktc.panasonichome.utils.LocalAppHelper;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.api.SkyDialogView.OnDialogOnKeyListener;
import com.ktc.panasonichome.view.api.SkyToastView;
import com.ktc.panasonichome.view.api.SkyToastView.ShowTime;
import com.mstar.android.tvapi.common.TvManager;

import java.util.ArrayList;
import java.util.List;
import android.view.View.OnKeyListener;


public class AllAppAdapter extends BaseAdapter{
	private Context mContext;
	private List<AppInfo> mList;
	private LocalAppHelper localAppHelper=null;
	private int focusIndex = 0;
	private boolean toShowPopu = false ;
	private ViewGroup rootLayout ;
	private  SkyToastView     mToastView;
	private  LayoutParams toastParams;
	private ShareAppInfoUtils shareUtils ;
	
	public AllAppAdapter(Context context, List<AppInfo> list , ViewGroup viewGroup) {
		mContext = context;
		rootLayout = viewGroup ;
		shareUtils = new ShareAppInfoUtils(mContext);
		localAppHelper=new LocalAppHelper(mContext);
		mList = new ArrayList<AppInfo>();
		for(int i = 0; i<list.size();i++)
		{
			mList.add(list.get(i));
		}
	}
	
	@Override
	public int getCount() {
		if(mList!=null){
			return mList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfo appInfo = mList.get(position);
		final AppItem appItem;
		if (convertView == null) {
			View v = LayoutInflater.from(mContext).inflate(R.layout.appicon, null);
			appItem = new AppItem();
			appItem.mAppIcon = (ImageView)v.findViewById(R.id.appicon);
			appItem.mAppTitle = (TextView)v.findViewById(R.id.apptitle);
			appItem.mappLayout=(RelativeLayout)v.findViewById(R.id.appLayout);
			appItem.mappFrame = (RelativeLayout)v.findViewById(R.id.appicon_frame);
			appItem.mFrameBtnOne = (Button)v.findViewById(R.id.appicon_frame_one);
			appItem.mFrameBtnTwo = (Button)v.findViewById(R.id.appicon_frame_two);
			
			appItem.mFrameBtnOne.setTextColor(Color.BLACK);
			appItem.mFrameBtnOne.setBackgroundColor(Color.WHITE);
			Drawable drawableOne = mContext.getResources().getDrawable(R.drawable.appbar_stick_focus);  
			drawableOne.setBounds(25, 0, drawableOne.getMinimumWidth()+25, drawableOne.getMinimumHeight());  
			appItem.mFrameBtnOne.setCompoundDrawables(drawableOne,null,null,null);
			
			appItem.mFrameBtnTwo.setTextColor(Color.WHITE);
			appItem.mFrameBtnTwo.setBackgroundColor(Color.TRANSPARENT);
			Drawable drawableTwo = mContext.getResources().getDrawable(R.drawable.appbar_remove_unfocus);  
			drawableTwo.setBounds(25, 0, drawableTwo.getMinimumWidth()+25, drawableTwo.getMinimumHeight());  
			appItem.mFrameBtnTwo.setCompoundDrawables(drawableTwo,null,null,null);
			
			v.setTag(appItem);
			convertView = v;
		} else {
			appItem = (AppItem)convertView.getTag();
		}
		String name = appInfo.getPackageName();
		boolean isOtaNew = SystemProperties.getBoolean("persist.sys.ota.available", false);
		Drawable installedAppIcon = localAppHelper.getInstalledAppIcon(mContext, appInfo.getPackageName());
		appItem.mAppIcon.setBackgroundDrawable(installedAppIcon);
		appItem.mAppTitle.setText(appInfo.getAppName());
		
		if(toShowPopu && position == focusIndex){
			appItem.mappLayout.setVisibility(View.INVISIBLE);
			appItem.mappFrame.setVisibility(View.VISIBLE);
			appItem.mFrameBtnOne.requestFocus();
			//OnKeyListener
			appItem.mFrameBtnOne.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(event.getAction() == KeyEvent.ACTION_UP){
						return true ;
					}
					if(keyCode == KeyEvent.KEYCODE_BACK){
						setShowPopu(false);
					}else if(keyCode == KeyEvent.KEYCODE_ENTER
							|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
						setToTop();
					}
					return false;
				}
			});
			appItem.mFrameBtnTwo.setOnKeyListener(new OnKeyListener() {
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(event.getAction() == KeyEvent.ACTION_UP){
						return true ;
					}
					if(keyCode == KeyEvent.KEYCODE_BACK){
						setShowPopu(false);
					}else if(keyCode == KeyEvent.KEYCODE_ENTER
							|| keyCode == KeyEvent.KEYCODE_DPAD_CENTER){
						if(mList.get(focusIndex).isSystemApp()){
							toastStick(mContext.getResources().getString(R.string.toast_stick_system_remove_fail));
						}else{
							showRemoveDialog();
						}
					}
					return false;
				}
			});
			
			appItem.mFrameBtnOne.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(hasFocus){
						appItem.mFrameBtnOne.setTextColor(Color.BLACK);
						appItem.mFrameBtnOne.setBackgroundColor(Color.WHITE);
						Drawable drawable= mContext.getResources().getDrawable(R.drawable.appbar_stick_focus);  
						drawable.setBounds(25, 0, drawable.getMinimumWidth()+25, drawable.getMinimumHeight());  
						appItem.mFrameBtnOne.setCompoundDrawables(drawable,null,null,null);
					}else{
						appItem.mFrameBtnOne.setTextColor(Color.WHITE);
						appItem.mFrameBtnOne.setBackgroundColor(Color.TRANSPARENT);
						Drawable drawable= mContext.getResources().getDrawable(R.drawable.appbar_stick_unfocus);  
						drawable.setBounds(25, 0, drawable.getMinimumWidth()+25, drawable.getMinimumHeight());  
						appItem.mFrameBtnOne.setCompoundDrawables(drawable,null,null,null);
					}
				}
			});
			
			appItem.mFrameBtnTwo.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if(hasFocus){
						appItem.mFrameBtnTwo.setTextColor(Color.BLACK);
						appItem.mFrameBtnTwo.setBackgroundColor(Color.WHITE);
						Drawable drawable= mContext.getResources().getDrawable(R.drawable.appbar_remove_focus);  
						drawable.setBounds(25, 0, drawable.getMinimumWidth()+25, drawable.getMinimumHeight());  
						appItem.mFrameBtnTwo.setCompoundDrawables(drawable,null,null,null);
					}else{
						appItem.mFrameBtnTwo.setTextColor(Color.WHITE);
						appItem.mFrameBtnTwo.setBackgroundColor(Color.TRANSPARENT);
						Drawable drawable= mContext.getResources().getDrawable(R.drawable.appbar_remove_unfocus);  
						drawable.setBounds(25, 0, drawable.getMinimumWidth()+25, drawable.getMinimumHeight());  
						appItem.mFrameBtnTwo.setCompoundDrawables(drawable,null,null,null);
					}
				}
			});
			
		}else{
			appItem.mappLayout.setVisibility(View.VISIBLE);
			appItem.mappFrame.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private class AppItem{
		ImageView mAppIcon;
		TextView mAppTitle;
		RelativeLayout mappLayout;
		RelativeLayout mappFrame ;
		Button mFrameBtnOne ;
		Button mFrameBtnTwo ;
	}
	
	public boolean isPopuShow(){
		return toShowPopu ;
	}
	
	public void setShowPopu(boolean toShowPopu){
		this.toShowPopu = toShowPopu ;
		notifyDataSetChanged();
	}
	
	public void setfocusIndex(int focusIndex){
		this.focusIndex = focusIndex ;
		notifyDataSetChanged();
	}
	
	private void sortAppInfos() {
		try {
			shareUtils.clear();
			for(int i = 0 ; i < mList.size() ; i++){
				shareUtils.put(mList.get(i).packageName, i);
			}
			shareUtils.put("appCount", mList.size());
		} catch (Exception e) {
			shareUtils.deleteFile();
			e.printStackTrace();
		}
	}
	
	private void setToTop(){
		try {
			if(shareUtils.getInt(mList.get(focusIndex).getPackageName()) == 0){
				toastStick(mContext.getResources().getString(R.string.toast_stick));
				return ;
			}
			List<AppInfo> tmpList = new ArrayList<AppInfo>();
			tmpList.addAll(mList);
			AppInfo focusInfo = new AppInfo();
			//add focusInfo
			for(int i = 0 ; i< mList.size() ; i++){
				if(i < focusIndex){
					tmpList.remove(i+1);
					tmpList.add(i+1, mList.get(i));
				}else if(i == focusIndex){
					focusInfo.appName = mList.get(i).getAppName();
					focusInfo.isSystemApp = mList.get(i).isSystemApp();
					focusInfo.launcherName = mList.get(i).getLauncherName();
					focusInfo.packageName = mList.get(i).getPackageName();
					focusInfo.appIcon =mList.get(i).getAppIcon();
					tmpList.remove(0);
					tmpList.add(0, focusInfo);
				}else{
					tmpList.remove(i);
					tmpList.add(i, mList.get(i));
				}
			}
			mList.clear();
			mList.addAll(tmpList);
			sortAppInfos();
			setShowPopu(false);
		} catch (Exception e) {
			shareUtils.deleteFile();
			e.printStackTrace();
		}
	}
	
	private void deleteApp(){
		IPackageDeleteObserver mDeleteObserver = new PackageDeleteObserver ();
		mContext.getPackageManager().deletePackage(mList.get(focusIndex).getPackageName(),mDeleteObserver,0);
	}
	
	private class PackageDeleteObserver extends IPackageDeleteObserver.Stub { 
		@Override
		public void packageDeleted(String arg0, int returnCode) throws RemoteException {
			if (returnCode == 1) {//success 
				try {
					mList.remove(mList.get(focusIndex));
					sortAppInfos();
				} catch (Exception e) {
					shareUtils.deleteFile();
					e.printStackTrace();
				}
	        }else{//fail
	        	toastStick(mContext.getResources().getString(R.string.toast_stick_remove_fail));
	        }  
		} 
    } 
	
	private void showRemoveDialog() {
		SkyRemoveAppDialog skyRemoveAppDialog = new SkyRemoveAppDialog();
		skyRemoveAppDialog.init(rootLayout,mContext);
		skyRemoveAppDialog.showTvDialog(mContext.getResources().getString(R.string.str_app_remove_message), null,
				mContext.getResources().getString(R.string.dialog_btn_1), 
				mContext.getResources().getString(R.string.dialog_btn_2), new OnDialogOnKeyListener() {
			@Override
			public boolean onDialogOnKeyEvent(int keyCode, KeyEvent keyEvent) {
				return false;
			}

			@Override
			public void onFirstBtnOnClickEvent() {
				setShowPopu(false);
				deleteApp();
			}

			@Override
			public void onSecondBtnOnClickEvent() {
				setShowPopu(false);
			}
		},null);

	}
	
	private void toastStick(String msg) {
		if (mToastView == null) {
            mToastView = new SkyToastView(this.mContext);
            toastParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            toastParams.gravity = 81;
            toastParams.bottomMargin = ScreenParams.getInstence(this.mContext)
                    .getResolutionValue(100);
        }
        mToastView.setTostString(msg);
        mToastView.showToast(ShowTime.SHOTTIME, toastParams);
    }
}

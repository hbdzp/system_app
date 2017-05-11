package com.ktc.panasonichome;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnKeyListener;

import com.ktc.panasonichome.model.AppInfo;
import com.ktc.panasonichome.utils.LocalAppHelper;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;

import java.util.ArrayList;
import java.util.List;

public class AllAppListActivity extends Activity {
	private static final String TAG = "AllAppListActivity";
	private RelativeLayout appcenter_root;
	private GridView          appGridView;
	private TextView          mAppinfo_count;
	private List<AppInfo>     listValidApps;
	private AllAppAdapter     mAllAppAdapter;
	private static final int TOTALICON = 12;
	private Context mContext;
	private ShareAppInfoUtils shareUtils ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.allapplication);
		appcenter_root = (RelativeLayout)findViewById(R.id.appcenter_root);
		appGridView = (GridView) findViewById(R.id.appgridview);
		mAppinfo_count = (TextView) findViewById(R.id.appinfo_count);
		shareUtils = new ShareAppInfoUtils(this) ;
		listValidApps = new ArrayList<AppInfo>();
		mContext = this;
		registApkFreshReceiver();
		initGridView();
		appGridView.setSelection(0);

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(false){
            event.setSource(KeyEvent.ACTION_MULTIPLE); 
            //lixq 20151119 Add for TV to shut down when TV no single start
            {
				short sourcestatus[] = null;
				try {
					sourcestatus = TvManager.getInstance()
							.setTvosCommonCommand("SetAutoSleepOnStatus");
				} catch (TvCommonException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
          //lixq 20151119 Add for TV to shut down when TV no single end
            ComponentName componentName = new ComponentName(
                    "com.horion.tv", "com.horion.tv.framework.RootActivity");
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setComponent(componentName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            startActivity(intent);
            return true;
        }
		return super.onKeyDown(keyCode, event);
	}
	
	private void initGridView() {
		LocalAppHelper localAppHelper=new LocalAppHelper(this);
		listValidApps.clear();
		listValidApps = localAppHelper.getInstalledAppList(this);
		mAppinfo_count.setText("("+listValidApps.size()+")");
		initSortAppInfos(listValidApps);//sort
		mAllAppAdapter = new AllAppAdapter(this, listValidApps ,appcenter_root);
		appGridView.setAdapter(mAllAppAdapter);
		appGridView.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				mAllAppAdapter.setfocusIndex(position);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
		appGridView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if(event.getAction()==KeyEvent.ACTION_UP){
					return true ;
				}
				switch (keyCode) {
				case KeyEvent.KEYCODE_MENU:
					if(mAllAppAdapter.isPopuShow()){
						mAllAppAdapter.setShowPopu(false);
					}else{
						mAllAppAdapter.setShowPopu(true);
					}
					break;
				case KeyEvent.KEYCODE_BACK:
					if(mAllAppAdapter.isPopuShow()){
						mAllAppAdapter.setShowPopu(false);
					}else{
						finish();
					}	
					break;
				case KeyEvent.KEYCODE_DPAD_LEFT:
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					if(mAllAppAdapter.isPopuShow()){
						mAllAppAdapter.setShowPopu(false);
					}
					break;
				case KeyEvent.KEYCODE_DPAD_CENTER:
				case KeyEvent.KEYCODE_ENTER:
					AppInfo appInfo = (AppInfo) appGridView.getItemAtPosition(appGridView.getSelectedItemPosition());
					Intent mIntent = getPackageManager().getLaunchIntentForPackage(appInfo.getPackageName());
					if (mIntent != null) {
						mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						if(appInfo.getPackageName().equals("com.horion.tv")){
							short sourcestatus[] = null;
							try {
								sourcestatus = TvManager.getInstance().setTvosCommonCommand("SetAutoSleepOnStatus");
							} catch (TvCommonException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
		
						try {
							startActivity(mIntent);
						} catch (ActivityNotFoundException anf) {
							Toast.makeText(AllAppListActivity.this, "package not find",Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(AllAppListActivity.this, "package not find",Toast.LENGTH_SHORT).show();
					}
					break;

				default:
					break;
				}
				return false;
			}
		});
	}
	
	/**
	 * 系统卸载和安装后页面刷新的广播
	 */
	private BroadcastReceiver apkFreshReceiver  = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if(action.equals(Intent.ACTION_PACKAGE_ADDED) ||action.equals(Intent.ACTION_PACKAGE_REMOVED)){
				initGridView();
				appGridView.setSelection(0);
			}
		}
		
	};
	
	private void registApkFreshReceiver()
	{
		IntentFilter apkFreshFilter = new IntentFilter();
		apkFreshFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		apkFreshFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		apkFreshFilter.addDataScheme("package");
		mContext.registerReceiver(apkFreshReceiver, apkFreshFilter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(apkFreshReceiver);
		super.onDestroy();
	}
	
	private void initSortAppInfos(List<AppInfo> listApps) {
		try {
			int appCount = 0 ;
			if (!shareUtils.isfileExit()) {
				for(int i = 0 ; i < listApps.size() ; i++){
					shareUtils.put(listApps.get(i).packageName, i);
				}
				appCount = listApps.size() ;
			}else{
				appCount = shareUtils.getInt("appCount");
				for(int i = 0 ; i < listApps.size() ; i++){
					String pkgName = listApps.get(i).getPackageName();
					if(!shareUtils.contains(pkgName)){
						shareUtils.put(pkgName, appCount++);
					}
				}
			}
			shareUtils.put("appCount", appCount);
			//refactor listValidApps sort
			refactorAppList();
		} catch (Exception e) {
			shareUtils.deleteFile();
			e.printStackTrace();
		}
	}
	
	private void refactorAppList(){
		try{
			List<AppInfo> tmpList = new ArrayList<AppInfo>();
			tmpList.addAll(listValidApps);
			for(AppInfo tmpApp : listValidApps){
				if(shareUtils.contains(tmpApp.getPackageName())){
					int index = shareUtils.getInt(tmpApp.getPackageName());
					tmpList.remove(index);
					tmpList.add(index, tmpApp);
				}
			}
			listValidApps.clear();
			listValidApps.addAll(tmpList);
		}catch(Exception e){
			shareUtils.deleteFile();
			e.printStackTrace();
		}
	}
}

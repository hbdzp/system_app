package com.ktc.panasonichome.update;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.SystemProperties;

import com.ktc.panasonichome.R;

/**
 * apk升级，使用方法在要升级的apk程序的启动Activity的onStart或onCreate函数中加入如下代码：
 * new ApkUpdateUtil(mContext,mupdate_url_property).update();
 * mupdate_url_property为apk升级URL的属性名称，例如Launcher升级对应为ro.product.launcher.update
 * 需要加入以下权限
 * <uses-permission android:name="android.permission.INTERNET"/>
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
 * @author liaomz
 *
 */
public class ApkUpdateUtil {
	private CheckNewVersionTask mCheckNewVersionTask;
	private Dialog alertDialog;
	private Context mContext;
	private String mupdate_url_property="";
	
	
	
	public ApkUpdateUtil(Context appContent,String update_url_property) {
		mContext = appContent;
		mupdate_url_property = update_url_property;
	}

	public void update() {
		if (isNetworkConnected(mContext)) {
			updateApk();
		}
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	private boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * Apk升级方法
	 */
	private void updateApk() {
		if (mCheckNewVersionTask != null
				&& mCheckNewVersionTask.getStatus() != AsyncTask.Status.FINISHED) {
			mCheckNewVersionTask.cancel(true);
		}

		mCheckNewVersionTask = new CheckNewVersionTask();
		mCheckNewVersionTask.execute();
	}

	/**
	 * 检查新版本异步任务
	 */
	private class CheckNewVersionTask extends AsyncTask<Void, Void, Version> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Version doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return new Update(mContext, getUpdateUrl())
					.hasNewVersion();
		}

		@Override
		protected void onPostExecute(Version result) {
			// TODO Auto-generated method stub
			if (result != null) {
				if (result != null) {
					showNewVersionDialog(result);

				}
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 显示是否下载新版本对话框
	 * 
	 * @param version
	 */

	private void showNewVersionDialog(final Version version) {
		if (alertDialog == null) {
			alertDialog = new AlertDialog.Builder(mContext)
					.setTitle(R.string.new_version_text)
					.setMessage(version.getIntroduction())
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Update update = new Update(mContext,
											getUpdateUrl());
									update.setVersion(version);
									update.checkUpdate();
								}
							})
					.setNeutralButton(R.string.skip_this_version,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).create();

		}
		if (!alertDialog.isShowing()) {
			alertDialog.show();
		}
	}

	private String getUpdateUrl() {
		return SystemProperties.get(mupdate_url_property, "");
	}
}

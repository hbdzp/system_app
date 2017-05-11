package com.ktc.panasonichome.update;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ktc.panasonichome.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * 软件升级类 使用方法：1、升级当前的应用程序：new Update(context).checkUpdate(); 2、升级指定包名的应用程序：new
 * Update(context， pkgName).checkUpdate();
 * 
 * 在AndroidManifest.xml中添加以下两个权限<uses-permission
 * android:name="android.permission.INTERNET"/> <uses-permission
 * android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
 * 
 * @author yejb
 * 
 */
public class Update {

	private static final String LOG_TAG = Update.class.getSimpleName();

	private static final boolean DEBUG = false;

	private static final String NOT_USB_DEVICE = "notusddevice";

	private static final String DATA_DRI_AND_USB_FULL = "datadirandusbfull";
	
	private static final String NEW_VERSION_DOWNLOAD_SUCCESS = "newversionapkexits";

	private String mServerVersionUrl;
	private String mCurrentPkg;

	private Context mContext;

	private DownloadTask mDownloadTask;

	private ProgressDialog mProgressDialog;

	private Version mVersion = null;
	// 下载文件总大小
	private long needloadfileSize = 0;
	// 下载起始大小
	private long offsetSize = 0;
	// 建立的下载连接
	private URLConnection urlConnetion = null;
	// 下载的文件
	File downloadFile = null;

	/**
	 * 默认升级调用该类的应用程序
	 * 
	 * @param context
	 */
	public Update(Context context, String serverURL) {
		this(context, context.getApplicationInfo().packageName, serverURL);
	}

	/**
	 * 升级指定包名的应用程序
	 * 
	 * @param context
	 * @param pkgName
	 *            包名
	 */
	public Update(Context context, String pkgName, String serverURL) {
		this.mContext = context;
		this.mCurrentPkg = pkgName;
		this.mServerVersionUrl = serverURL;
	}

	/**
	 * 设置升级的服务器地址
	 * 
	 * @param url
	 */
	public void setServerUrl(String url) {
		mServerVersionUrl = url;
	}

	/**
	 * 设置版本信息
	 * 
	 * @param version
	 */
	public void setVersion(Version version) {
		this.mVersion = version;
	}

	/**
	 * 判断是否有新版本 注意：该方法需要访问网络，在UI中需要异步调用，否则会造成UI线程阻塞
	 * 
	 * @return 有则返回新版本信息，否则返回null
	 */
	public Version hasNewVersion() {
		Version lastVersion = getUpdateVersion();
		if (lastVersion != null) {
			PackageVersion currentVersion = new PackageVersion(mContext,
					mCurrentPkg);
			int lastVerCode = lastVersion.getVerCode();
			int currentCode = currentVersion.getVerCode();
			if (currentCode > 0 && lastVerCode > currentCode) {
				return lastVersion;
			}
			else//当更新完成后第一次进入应用时，判断先前是否有下载的apk文件，有则删除。
			{
				File file = new File(mContext.getFilesDir()+"/"+
						lastVersion.getFileName());
				if(file.exists())
				{
					Log.i("lixq", "file.delete()");
					file.delete();
				}
			}
		}
		return null;
	}

	public void checkUpdate() {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(mContext.getResources().getString(
				R.string.str_progress_title));
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setMax(100);
		mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext
				.getResources().getString(R.string.str_cancel),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						if (DEBUG)
							Log.d(LOG_TAG, "dialog dismiss");
						if (mDownloadTask != null
								&& mDownloadTask.getStatus() != AsyncTask.Status.FINISHED) {
							mDownloadTask.cancel(true);
						}
					}
				});
		mProgressDialog.show();

		// 显示下载界面
		if (mVersion != null) {
			if (mDownloadTask != null
					&& mDownloadTask.getStatus() != AsyncTask.Status.FINISHED) {
				mDownloadTask.cancel(true);
			}
			mDownloadTask = new DownloadTask();
			mDownloadTask.execute(mVersion.getUrl());
		}
	}

	public static String exec(String[] args) {
		String result = "";
		ProcessBuilder processBuilder = new ProcessBuilder(args);
		Process process = null;
		InputStream errIs = null;
		InputStream inIs = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = -1;
			process = processBuilder.start();
			errIs = process.getErrorStream();
			while ((read = errIs.read()) != -1) {
				baos.write(read);
			}
			baos.write('n');
			inIs = process.getInputStream();
			while ((read = inIs.read()) != -1) {
				baos.write(read);
			}
			byte[] data = baos.toByteArray();
			result = new String(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (errIs != null) {
					errIs.close();
				}
				if (inIs != null) {
					inIs.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (process != null) {
				process.destroy();
			}
		}
		System.out.println(result);
		return result;
	}

	/**
	 * 文件下载异步任务
	 * 
	 * @author yejb
	 * 
	 */
	private class DownloadTask extends AsyncTask<String, Integer, String> {

		private boolean taskCancelled = false;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			RandomAccessFile savedFile = null;
			String fileUrl = params[0];
			String checkResult = checkStorageDir(fileUrl);
			if(null == checkResult)
			{
				return null;
			}
			//新版本已经存在
			else if(NEW_VERSION_DOWNLOAD_SUCCESS.equals(checkResult))
			{
				File file = getDownloadFile();
				return file.getPath();
			}
			//存储空间不足
			else if((DATA_DRI_AND_USB_FULL.equals(checkResult)) || (NOT_USB_DEVICE.equals(checkResult)))
			{
				return checkResult;
			}
			URLConnection con = getUrlConnetion();
			File file = getDownloadFile();
			Long offset = getOffsetSize();
			Long fileSize = getNeedloadfileSize();
			String[] args = { "chmod", "604", file.getAbsolutePath() };
			try {
				InputStream raw = con.getInputStream();
				InputStream is = new BufferedInputStream(raw);
				savedFile = new RandomAccessFile(file, "rw");
				// 从文件尾追加
				savedFile.seek(offset);
				int bytesRead = -1;
				byte[] buf = new byte[1024 * 5];
				int percent = 0;
				while (!taskCancelled) {
					bytesRead = is.read(buf);
					if (bytesRead < 0) {
						break;
					}
					savedFile.write(buf, 0, bytesRead);
					offset += bytesRead;
					percent = (int) (offset * 100.0 / fileSize);
					publishProgress(percent);
					if (DEBUG)
						Log.d(LOG_TAG, "bytesRead: " + bytesRead);
				}
				if ((offset-fileSize)==0){
					String result = exec(args);
					if (DEBUG)
						Log.i(LOG_TAG, "chmod,result," + result);
					return file.getPath();
				}else{
					return null;
				}

			} catch (Exception e) {
				// TODO: handle exception
			} finally {
				if (savedFile != null) {
					try {
						savedFile.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			taskCancelled = true;
			if (DEBUG)
				Log.d(LOG_TAG, "download cancelled");
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			setProgressPercent(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			mProgressDialog.dismiss();
			if (NOT_USB_DEVICE.equals(result)) {
				Toast.makeText(mContext, R.string.download_not_usb_device,
						Toast.LENGTH_LONG).show();
			} else if(DATA_DRI_AND_USB_FULL.equals(result))
			{
				Toast.makeText(mContext, R.string.download_not_usb_device_full,
						Toast.LENGTH_LONG).show();
			}
			else {
				if (result!= null && !TextUtils.isEmpty(result)) {
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setDataAndType(Uri.fromFile(new File(result)),
							"application/vnd.android.package-archive");
					mContext.startActivity(intent);
				} else {
					Toast.makeText(mContext, R.string.download_failed,
							Toast.LENGTH_LONG).show();
				}
			}

			super.onPostExecute(result);
		}
	}

	/**
	 * 判断data目录和USB存储设备是否有空间
	 */
	private String checkStorageDir(String fileUrl) {
		File file;
		file = new File(mContext.getFilesDir() + "/" + mVersion.getFileName());
		long offset = 0;
		// 判断当前路径下是否有同名的Apk文件
		if (file.exists()) {
			try {
				// 判断APK文件是否完整
				int apkVersion = getApkVersion(file.getPath());
				if (DEBUG)
					Log.d(LOG_TAG, "apkVersion: " + apkVersion);
				// 完整，再判断是否和服务器更新文件相同
				String md5 = MD5.md5sum(file.getPath());
				String sda = SystemProperties.get("ro.product.serial","");
				if (DEBUG)
					Log.d(LOG_TAG, "md5: " + md5);
				if (md5.equals(mVersion.getMD5()) && sda.equals(mVersion.getSda())) {
					setDownloadFile(file);
					return NEW_VERSION_DOWNLOAD_SUCCESS;
					//return file.getPath();
				} else {
					file.delete();
				}
			} catch (Exception e) {
				// TODO: handle exception
				file.delete();
				e.printStackTrace();
			}

			offset = file.length();
		}

		if (DEBUG)
			Log.d(LOG_TAG, "offset: " + offset);
		try {
			URL url = new URL(fileUrl);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.setRequestProperty("RANGE", "bytes=" + offset + "-");
			con.connect();
			long fileSize = con.getContentLength();

			if (DEBUG)
				Log.d(LOG_TAG, "fileSize: " + fileSize);
			fileSize += offset;
			double sdFree = freeSpaceOnDataDir();
			if(sdFree <= fileSize) 
			{
				if (DEBUG)
					Log.e(LOG_TAG, "data dir is not enough free space");
				return loadUpdateFileFromUSB(fileUrl);
			} else {
				setDownloadFile(file);
				setNeedloadfileSize(fileSize);
				setOffsetSize(offset);
				setUrlConnetion(con);
				return file.getPath();

			}
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	/**
	 * 判断USB存储设备是否有空间
	 * 
	 * @param fileUrl
	 * @param taskCancelled
	 * @return
	 */
	private String loadUpdateFileFromUSB(String fileUrl) {
		String sdState = Environment.getExternalStorageState();
		File file;
		// 系统为2.2及以上版本并且系统已挂载SD卡，将文件存放到SD中
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
				&& Environment.MEDIA_MOUNTED.equals(sdState)) {
			file = new File(mContext.getExternalCacheDir(),
					mVersion.getFileName());
			long offset = 0;
			if (file.exists()) {
				try {
					// 判断APK文件是否完整
					int apkVersion = getApkVersion(file.getPath());
					if (DEBUG)
						Log.d(LOG_TAG, "apkVersion: " + apkVersion);
					// 完整，再判断是否和服务器更新文件相同
					String md5 = MD5.md5sum(file.getPath());
				    String sda = SystemProperties.get("ro.product.serial","");
					if (DEBUG)
						Log.d(LOG_TAG, "md5: " + md5);
					if (md5.equals(mVersion.getMD5()) && sda.equals(mVersion.getSda())) {
						return file.getPath();
					} else {
						file.delete();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				offset = file.length();
			}
			if (DEBUG)
				Log.d(LOG_TAG, "offset: " + offset);
			try {
				URL url = new URL(fileUrl);
				URLConnection con = url.openConnection();
				con.setConnectTimeout(5000);
				con.setReadTimeout(5000);
				con.setRequestProperty("RANGE", "bytes=" + offset + "-");
				con.connect();
				long fileSize = con.getContentLength();

				if (DEBUG)
					Log.d(LOG_TAG, "fileSize: " + fileSize);
				fileSize += offset;
				double sdFree = freeSpaceOnSd();
				if (sdFree <= fileSize) {
					if (DEBUG)
						Log.e(LOG_TAG, "SD card is not enough free space");
					return DATA_DRI_AND_USB_FULL;
				} else {
					setDownloadFile(file);
					setNeedloadfileSize(fileSize);
					setOffsetSize(offset);
					setUrlConnetion(con);
					return file.getPath();
				}
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}

		} else {
			return NOT_USB_DEVICE;
		}
	}

	/**
	 * 计算data目录剩余空间
	 * 
	 * @return
	 */
	private double freeSpaceOnDataDir() {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		return (double) stat.getAvailableBlocks()
				* (double) stat.getBlockSize();
	}

	/**
	 * 计算SD卡剩余空间
	 * 
	 * @return
	 */
	private double freeSpaceOnSd() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		return (double) stat.getAvailableBlocks()
				* (double) stat.getBlockSize();
	}

	/**
	 * 获取Apk文件的版本
	 * 
	 * @param path
	 *            Apk文件路径
	 * @return
	 */
	private int getApkVersion(String path) {
		PackageManager pm = mContext.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(path,
				PackageManager.GET_ACTIVITIES);
		return info.versionCode;
	}

	/**
	 * 设置进度百分比
	 * 
	 * @param percent
	 */
	private void setProgressPercent(int percent) {
		mProgressDialog.setProgress(percent);
	}

	/**
	 * 从服务器获取远程当前程序版本信息
	 * 
	 * @param serverUrl
	 * @param pkgName
	 * @return
	 */
	public Version getUpdateVersion() {
		DefaultHttpClient http = new DefaultHttpClient();
		HttpResponse response = null;

		try {
			if (mServerVersionUrl != null && !mServerVersionUrl.equals("")) {
				HttpGet method = new HttpGet(mServerVersionUrl);
				response = http.execute(method);
				InputStream data = response.getEntity().getContent();

				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(data));
				StringBuffer sb = new StringBuffer();
				String line = bufferedReader.readLine();
				while (line != null) {
					sb.append(line).append("\r\n");
					line = bufferedReader.readLine();
				}
				bufferedReader.close();
				String rlt = sb.toString();
				if (DEBUG)
					Log.i(LOG_TAG, rlt);
				List<Version> versions = Version.constructVersion(rlt);
				if (versions != null) {
					for (Version version : versions) {
						if (mCurrentPkg.equals(version.getPkgName())) {
							return version;
						}
					}
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据包名获取指定应用程序的版本信息
	 * 
	 * @author yejb
	 * 
	 */
	private static class PackageVersion {

		private Context mContext;
		private String mPkgName;
		private int mVerCode;

		public PackageVersion(Context context, String pkgName) {
			this.mContext = context;
			this.mPkgName = pkgName;

			init();
		}

		private void init() {
			try {
				mVerCode = mContext.getPackageManager().getPackageInfo(
						mPkgName, 0).versionCode;
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				mVerCode = -1;
			}
		}

		/**
		 * 获取版本号
		 * 
		 * @return
		 */
		public int getVerCode() {
			return this.mVerCode;
		}
	}

	public long getNeedloadfileSize() {
		return needloadfileSize;
	}

	public void setNeedloadfileSize(long needloadfileSize) {
		this.needloadfileSize = needloadfileSize;
	}

	public long getOffsetSize() {
		return offsetSize;
	}

	public void setOffsetSize(long offsetSize) {
		this.offsetSize = offsetSize;
	}

	public URLConnection getUrlConnetion() {
		return urlConnetion;
	}

	public void setUrlConnetion(URLConnection urlConnetion) {
		this.urlConnetion = urlConnetion;
	}

	public File getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(File downloadFile) {
		this.downloadFile = downloadFile;
	}

}

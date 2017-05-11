package com.ktc.panasonichome.model;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimpleHomeWebQuery {
    private static SimpleHomeWebQuery _webquery = null;
    public static QueryUrlListener queryListener = null;
    public static final String tag = "HOME";
    private boolean inited = false;
    private ScheduleHandler schedulehandler = null;
    ScheduledExecutorService submitLogThreadPool = null;

    public interface QueryUrlListener {
        void commitRefreshTime();

        void downloadResult(boolean z);

        HashMap<String, String> getHomeWebData();

        String getLocalCachedPath();

        String getQueryUrl();

        boolean isNetConnected();

        boolean isOutOfRefreshTime();
    }

    private static class ScheduleHandler extends Handler implements Runnable {
        public ScheduleHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                startSyncNet();
            }
        }

        public void startSyncNet() {
            new Thread(new WebQueryRunnable()).start();
        }

        public void run() {
            sendEmptyMessage(0);
        }
    }

    private static class WebQueryRunnable implements Runnable {
        private WebQueryRunnable() {
        }

        public void run() {
            synchronized (this) {
                String cachedPath = SimpleHomeWebQuery.getSimpleHomeWebQuery().cachedPath();
                String queryUrl = SimpleHomeWebQuery.getSimpleHomeWebQuery().queryUrl();
                boolean isConnected = SimpleHomeWebQuery.getSimpleHomeWebQuery().netConnected();
                boolean isOutOfRefresh = SimpleHomeWebQuery.getSimpleHomeWebQuery().outOfRefresh();
                if (cachedPath != null) {
                    String savedFolderPath = cachedPath + File.separator + "cached";
                    File tempFolderFile = new File(savedFolderPath);
                    if (!tempFolderFile.exists()) {
                        tempFolderFile.mkdir();
                    }
                    if (isConnected && isOutOfRefresh) {
                        download(queryUrl, savedFolderPath, SimpleHomeWebQuery.getSimpleHomeWebQuery().getHomeWebData());
                    }
                }
            }
        }

        private String getCachedFileMd5(File cachedFile) {
            //处理getCachedFileMd5
    /*        if (cachedFile.length() < 10) {
                return null;
            }
            try {
                return MD5Util.getFileMD5String(cachedFile);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }*/
            return null;
        }

        public void download(String url, String cachedpath, HashMap<String, String> data) {
            //TODO download
        }
           /* LogUtils.d("dzp", "url == " + url + "  cached file = " + cachedpath + File.separator + "temphomepage.xml");
            if (url != null && url.startsWith("http")) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(url);
                for (Entry entry : data.entrySet()) {
                    try {
                        String key = (String) entry.getKey();
                        String val = (String) entry.getValue();
                        httpget.addHeader(key, val);
                        LogUtils.d("dzp", ">ZC<  download addHeader key =" + key + "/val=" + val);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                HttpResponse response = client.execute(httpget);
                if (response.getStatusLine().getStatusCode() == 202 || response.getStatusLine().getStatusCode() == 200) {
                    InputStream content = response.getEntity().getContent();
                    File flagfile = new File(cachedpath, "flag");
                    if (!flagfile.exists()) {
                        flagfile.createNewFile();
                    }
                    File file = new File(cachedpath, "temphomepage.xml");
                    OutputStream fileOutputStream = new FileOutputStream(file);
                    while (true) {
                        try {
                            int ch = content.read();
                            if (ch == -1) {
                                break;
                            }
                            fileOutputStream.write(ch);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } finally {
                            fileOutputStream.close();
                            content.close();
                        }
                    }
                    if (file.length() < 10) {
                        LogUtils.v("dzp", "Download NULL ~");
                        file.delete();
                        if (SimpleHomeWebQuery.queryListener != null) {
                            SimpleHomeWebQuery.queryListener.downloadResult(false);
                        }
                    } else if (!new HomePageXmlParse(file.getAbsolutePath()).parseNow()) {
                        LogUtils.v("dzp", "Parse Download XML Error ~");
                        file.delete();
                        if (SimpleHomeWebQuery.queryListener != null) {
                            SimpleHomeWebQuery.queryListener.downloadResult(false);
                        }
                        fileOutputStream.close();
                        content.close();
                    } else if (file.length() < 10) {
                        LogUtils.v("dzp", "Download tempfile.length() < 10");
                        if (SimpleHomeWebQuery.queryListener != null) {
                            SimpleHomeWebQuery.queryListener.downloadResult(false);
                        }
                        fileOutputStream.close();
                        content.close();
                    } else {
                        String downloadMd5 = Md5DigestUtil.getCurrentFileMd5(file);
                        file = new File(cachedpath, "simplehomepage.xml");
                        if (file.exists()) {
                            String cachedMd5 = Md5DigestUtil.getCurrentFileMd5(file);
                            if (cachedMd5 == null) {
                                file.delete();
                                file.renameTo(file);
                                if (SimpleHomeWebQuery.queryListener != null) {
                                    SimpleHomeWebQuery.queryListener.downloadResult(true);
                                }
                                LogUtils.d("dzp", ">ZC<  this is oversea home downloadSuccess");
                            } else if (cachedMd5.equals(downloadMd5)) {
                                LogUtils.v("dzp", "Download MD5 is equals");
                                if (SimpleHomeWebQuery.queryListener != null) {
                                    SimpleHomeWebQuery.queryListener.downloadResult(false);
                                }
                            } else {
                                file.delete();
                                file.renameTo(file);
                                if (SimpleHomeWebQuery.queryListener != null) {
                                    SimpleHomeWebQuery.queryListener.downloadResult(true);
                                }
                                LogUtils.d("dzp", ">ZC<  this is oversea home downloadSuccess");
                            }
                        } else {
                            file.renameTo(file);
                            if (SimpleHomeWebQuery.queryListener != null) {
                                SimpleHomeWebQuery.queryListener.downloadResult(true);
                            }
                            LogUtils.d("dzp", ">ZC<  this is oversea home downloadSuccess");
                        }
                        flagfile.delete();
                        SimpleHomeWebQuery.getSimpleHomeWebQuery().commitRefreshTime();
                        fileOutputStream.close();
                        content.close();
                    }
                }
            }
        }*/
    }

    private SimpleHomeWebQuery() {
    }

    public void startSyncNetNow() {
        if (this.schedulehandler != null && queryListener != null && queryListener.isOutOfRefreshTime()) {
            this.schedulehandler.sendEmptyMessage(0);
        }
    }

    public boolean outOfRefresh() {
        if (queryListener == null || !queryListener.isOutOfRefreshTime()) {
            return false;
        }
        return queryListener.isOutOfRefreshTime();
    }

    public void init(Looper looper, QueryUrlListener listener, HashMap<String, String> maps) {
        if (!this.inited) {
            this.submitLogThreadPool = Executors.newScheduledThreadPool(1);
            this.schedulehandler = new ScheduleHandler(looper);
            int interval = 20;
            if (maps != null) {
                String timestr = (String) maps.get("UpdateXmlInterval");
                if (timestr != null) {
                    interval = Integer.valueOf(timestr).intValue();
                }
            }
            this.submitLogThreadPool.scheduleWithFixedDelay(this.schedulehandler, 0, (long) interval, TimeUnit.MINUTES);
            queryListener = listener;
            this.inited = true;
        }
    }

    public void cleanup() {
        this.submitLogThreadPool.shutdown();
        queryListener = null;
        this.inited = false;
    }

    public void setQueryUrlListener(QueryUrlListener listener) {
        if (queryListener != null) {
            queryListener = null;
        }
        queryListener = listener;
    }

    private String cachedPath() {
        if (queryListener != null) {
            return queryListener.getLocalCachedPath();
        }
        return null;
    }

    private String queryUrl() {
        if (queryListener != null) {
            return queryListener.getQueryUrl();
        }
        return null;
    }

    private HashMap<String, String> getHomeWebData() {
        if (queryListener != null) {
            return queryListener.getHomeWebData();
        }
        return null;
    }

    private boolean netConnected() {
        if (queryListener != null) {
            return queryListener.isNetConnected();
        }
        return false;
    }

    public void commitRefreshTime() {
        if (queryListener != null) {
            queryListener.commitRefreshTime();
        }
    }

    public static SimpleHomeWebQuery getSimpleHomeWebQuery() {
        if (_webquery == null) {
            _webquery = new SimpleHomeWebQuery();
        }
        return _webquery;
    }
}

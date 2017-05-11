package com.horion.tv.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.horion.tv.R;
import com.horion.tv.framework.ui.dialog.SkyTVDialog;
import com.ktc.framework.ktcsdk.ipc.KtcApplication;
import com.ktc.ui.api.SkyDialogView;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCountry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiacf on 2017/1/7.
 */

public class FirstSearchActivity extends Activity {
    private FrameLayout rootView;
    private final Intent mIntent = new Intent();
    private SkyTVDialog skyTVDialog;
    private String tip1;
    private String tip2;
    private String btn1;
    private String btn2;
    private setDtvConfigTask task = new setDtvConfigTask();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        rootView = (FrameLayout)findViewById(R.id.delete_main);


        tip1 = getString(R.string.nosignal_needsearch);
        ///tip2 = getString(R.string.nosignal_needsearch);
        btn1 = getString(R.string.ok);
        btn2 = getString(R.string.cancel);
        skyTVDialog = new SkyTVDialog();
        skyTVDialog.init(rootView,this);
        skyTVDialog.showTvDialog(tip1, tip2, btn1, btn2, new SkyDialogView.OnDialogOnKeyListener() {
            @Override
            public boolean onDialogOnKeyEvent(int keyCode, KeyEvent keyEvent) {
                Log.d("Maxs34","onDialogOnKeyEvent:keyCode = " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK){
                    finish();
                }
                return false;
            }

            @Override
            public void onFirstBtnOnClickEvent() {
                task.execute();
                finish();
                Log.d("Maxs34","--->onFirstBtnOnClickEvent = ");
            }

            @Override
            public void onSecondBtnOnClickEvent() {

                Log.d("Maxs34","--->onSecondBtnOnClickEvent = ");
                finish();
            }
        },null);
    }


    //nathan.liao 2014.11.04 add for auto tuning ANR error start
    class setDtvConfigTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            /*progressDialog = new AutoTuningInitDialog(mChannelActivity, R.style.dialog);
            progressDialog.show();*/
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            ////if (PropHelp.newInstance().hasDtmb()) {
            if (true) {
                if (KtcApplication.isSourceDirty) {
                    KtcApplication.isSourceDirtyPre = true;
                    KtcApplication.isSourceDirty = false;
                }
                if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_DTV) {
                    TvCommonManager.getInstance().setInputSource(
                            TvCommonManager.INPUT_SOURCE_DTV);
                }
                Log.d("Maxs","SearchSelectActivity:setUserScanType:Tv_SCAN_ALL");
                TvChannelManager.getInstance().setUserScanType(TvChannelManager.TV_SCAN_ALL);
                int dtmbRouteIndex = TvChannelManager.getInstance().getSpecificDtvRouteIndex(TvChannelManager.TV_ROUTE_DTMB);
                TvChannelManager.getInstance().switchMSrvDtvRouteCmd(dtmbRouteIndex);
                Log.d("Maxs","SearchSelectActivity:TvChannelManager.getInstance().getUserScanType = " + TvChannelManager.getInstance().getUserScanType());
            }
            TvChannelManager.getInstance().setSystemCountryId(TvCountry.CHINA);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            /*if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            Intent intent = new Intent();
            intent.setClass(FirstSearchActivity.this, ChannelTuning.class);
            startActivity(intent);
            finish();
            super.onPostExecute(result);
        }
    }
    //nathan.liao 2014.11.04 add for auto tuning ANR error end

    @Override
    protected void onResume() {
        super.onResume();
    }

}

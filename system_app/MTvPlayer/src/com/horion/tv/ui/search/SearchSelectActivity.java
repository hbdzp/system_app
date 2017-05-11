package com.horion.tv.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.horion.tv.R;
import com.horion.tv.framework.RootActivity;
import com.horion.tv.ui.MainMenuActivity;
import com.ktc.framework.ktcsdk.ipc.KtcApplication;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvCountry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiacf on 2017/1/7.
 */

public class SearchSelectActivity extends Activity {
    private String searchType;
    private ListView listView;
    private SearchAdapter baseAdapter;
    private ArrayList<SearchTypeBean> mData;
    private static final int TYPE_UNOPTION = 0;
    private static final int TYPE_OPTION = 1;
    private int mSoundSystemIndex = 0;
    private int mColorSystemIndex = 0;
    private String mSoundSystem[] = {"BG", "DK", "I", "L", "M"};
    private setDtvConfigTask task = new setDtvConfigTask();
    private String mColorSystem[] = {"PAL", "NTSC", "SECAM", "NTSC_44",
            "PAL_M", "PAL_N", "PAL_60", "NO_STAND", "AUTO"};
    private final Intent mIntent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.searchselect);

        listView = (ListView) findViewById(R.id.searchlistview);
        mData = new ArrayList<SearchTypeBean>();
        SearchTypeBean searchTypeBean1 = new SearchTypeBean();
        searchTypeBean1.setItemType(TYPE_UNOPTION);
        searchTypeBean1.setSearchType(getString(R.string.str_auto_search));
        mData.add(searchTypeBean1);
        searchType = getIntent().getExtras().getString("searchType");
        Log.d("Maxs", "searchType = " + searchType);
        SearchTypeBean searchTypeBean2 = new SearchTypeBean();
        searchTypeBean2.setItemType(TYPE_UNOPTION);
        searchTypeBean2.setSearchType(getString(R.string.str_manual_search));
        mData.add(searchTypeBean2);
       /* if ("ATV".equals(searchType)) {
            SearchTypeBean searchTypeBean3 = new SearchTypeBean();
            searchTypeBean3.setItemType(TYPE_OPTION);
            searchTypeBean3.setSearchType(getString(R.string.KTC_CFG_TV_ATV_SOUND_SYSTEM));
            searchTypeBean3.setSearchTypeValueArr(mSoundSystem);
            mColorSystemIndex = TvChannelManager.getInstance().getAtvVideoStandard() % (mColorSystem.length);
            searchTypeBean3.setSearchTypeValue(mColorSystem[mColorSystemIndex]);
            mData.add(searchTypeBean3);

            SearchTypeBean searchTypeBean4 = new SearchTypeBean();
            searchTypeBean4.setItemType(TYPE_OPTION);
            searchTypeBean4.setSearchType(getString(R.string.KTC_CFG_TV_ATV_COLOR_SYSTEM));
            searchTypeBean4.setSearchTypeValueArr(mColorSystem);
            mData.add(searchTypeBean4);
        }*/


        baseAdapter = new SearchAdapter(this, mData);
        listView.setAdapter(baseAdapter);

        listView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            	if (keyEvent.getAction() == KeyEvent.ACTION_UP){
            		return true ;
            	}
                int selectPostion = listView.getSelectedItemPosition();
                keyCode = MapKeyPadToIR(keyCode, keyEvent);
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    if (mData.get(selectPostion).getItemType() == TYPE_OPTION) {
                        Log.d("Maxs", "TYPE_OPTION");
                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            baseAdapter.updateItemView(selectPostion, listView, keyCode);
                        }
                        Log.d("Maxs", "onKey:listView.getSelectedItemPosition(); =  " + listView.getSelectedItemPosition());
                        return false;
                    } else {
                        if ((keyCode == KeyEvent.KEYCODE_ENTER) 
                        		|| (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
                        		||(keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
                            switch (selectPostion) {
                                case 0:
                                    Log.d("Maxs","Auto Chnanel");
                                    task.execute();
                                    return true;
                                case 1:
                                    Log.d("Maxs","Manual Chnanel");
                                    Log.d("Maxs27","searchType = " + searchType);
                                    if ("ATV".equals(searchType)) {
                                        mIntent.setClass(SearchSelectActivity.this, ATVManualTuning.class);
                                        startActivity(mIntent);
                                        finish();
                                    }else{
                                        Log.d("Maxs27","DTVManualTuning startActivity");
                                        mIntent.setClass(SearchSelectActivity.this, DTVManualTuning.class);
                                        startActivity(mIntent);
                                        finish();
                                    }
                                    return true;
                            }
                        }else if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
                        	if(selectPostion == 1){
                        		listView.setSelection(0);
                        	}
                        }else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                        	if(selectPostion == 0){
                        		listView.setSelection(1);
                        	}
                        }
                    }

                }
                return false;
            }
        });
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
            intent.setClass(SearchSelectActivity.this, ChannelTuning.class);
            startActivity(intent);
            finish();
            super.onPostExecute(result);
        }
    }
    //nathan.liao 2014.11.04 add for auto tuning ANR error end

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode){
            case KeyEvent.KEYCODE_MENU:
                Intent intent = new Intent(this,MainMenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class SearchAdapter extends BaseAdapter {
        private List<SearchTypeBean> mData;
        private LayoutInflater mInflater;
        private Context mContext;

        @Override
        public int getCount() {
            return mData.size();
        }

        public SearchAdapter(Context context, ArrayList<SearchTypeBean> data) {
            mContext = context;
            mData = data;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolderUnoption viewHolderUnoption = null;
            ViewHolderOption viewHolderOption = null;
            int type = getItemViewType(position);
            if (convertView == null) {
                switch (type) {
                    case TYPE_UNOPTION:
                        viewHolderUnoption = new ViewHolderUnoption();
                        convertView = mInflater.inflate(R.layout.search_item_unoption, null);
                        viewHolderUnoption.searchType = (TextView) convertView.findViewById(R.id.searchtype1);
                        convertView.setTag(viewHolderUnoption);


                        break;
                    case TYPE_OPTION:
                        viewHolderOption = new ViewHolderOption();
                        convertView = mInflater.inflate(R.layout.search_item_option, null);

                        viewHolderOption.searchType = (TextView) convertView.findViewById(R.id.searchtype2);
                        viewHolderOption.searchTypeValue = (TextView) convertView.findViewById(R.id.optiontypevalue);
                        convertView.setTag(viewHolderOption);

                        break;
                }

            } else {
                switch (type) {
                    case TYPE_UNOPTION:
                        viewHolderUnoption = (ViewHolderUnoption) convertView.getTag();

                        break;
                    case TYPE_OPTION:
                        viewHolderOption = (ViewHolderOption) convertView.getTag();

                        break;
                }


            }
            switch (type) {
                case TYPE_UNOPTION:
                    viewHolderUnoption.searchType.setText(mData.get(position).getSearchType());
                    break;
                case TYPE_OPTION:

                    viewHolderOption.searchType.setText(mData.get(position).getSearchType());
                    viewHolderOption.searchTypeValue.setText(mData.get(position).getSearchTypeValue());
                    break;
            }

            return convertView;
        }

        @Override
        public int getItemViewType(int position) {
            SearchTypeBean searchTypeBean = mData.get(position);
            return searchTypeBean.getItemType();
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }


        public void updateItemView(int position, ListView listView, int keyCode) {
            int visibleFirstPosi = listView.getFirstVisiblePosition();
            int visibleLastPosi = listView.getLastVisiblePosition();
            if (position >= visibleFirstPosi && position <= visibleLastPosi) {
                View view = listView.getChildAt(position - visibleFirstPosi);
                ViewHolderOption viewHolderUnoption = (ViewHolderOption) view.getTag();
                Log.d("Maxs", "MyAdapter:updateItemView;11");
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        if (mData.get(position).getSearchType().equals(getString(R.string.KTC_CFG_TV_ATV_SOUND_SYSTEM))) {
                            mSoundSystemIndex = (mSoundSystemIndex + mSoundSystem.length - 1) % (mSoundSystem.length);
                            if (mSoundSystemIndex == 3) {
                                mSoundSystemIndex = 2;
                            }

                            TvChannelManager.getInstance().setAtvAudioStandard(mSoundSystemIndex);
                            viewHolderUnoption.searchTypeValue.setText(mSoundSystem[mSoundSystemIndex]);
                        }
                        if (mData.get(position).getSearchType().equals(getString(R.string.KTC_CFG_TV_ATV_COLOR_SYSTEM))) {
                            mColorSystemIndex = (mColorSystemIndex + mColorSystem.length - 1) % (mColorSystem.length);
                            if (mColorSystemIndex >= 3
                                    && mColorSystemIndex <= 7) {
                                mColorSystemIndex = 2;
                            }

                            TvChannelManager.getInstance().setAtvVideoStandard(mColorSystemIndex);
                            viewHolderUnoption.searchTypeValue.setText(mColorSystem[mColorSystemIndex]);
                        }

                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        if (mData.get(position).getSearchType().equals(getString(R.string.KTC_CFG_TV_ATV_SOUND_SYSTEM))) {
                            mSoundSystemIndex = (mSoundSystemIndex + 1) % (mSoundSystem.length);
                            if (mSoundSystemIndex == 3) {
                                mSoundSystemIndex = 4;
                            }
                            TvChannelManager.getInstance().setAtvAudioStandard(mSoundSystemIndex);
                            viewHolderUnoption.searchTypeValue.setText(mSoundSystem[mSoundSystemIndex]);


                        }
                        if (mData.get(position).getSearchType().equals(getString(R.string.KTC_CFG_TV_ATV_COLOR_SYSTEM))) {
                            mColorSystemIndex = (mColorSystemIndex + 1) % (mColorSystem.length);
                            if (mColorSystemIndex >= 3
                                    && mColorSystemIndex <= 7) {
                                mColorSystemIndex = 8;
                            }
                            TvChannelManager.getInstance().setAtvVideoStandard(mColorSystemIndex);
                            viewHolderUnoption.searchTypeValue.setText(mColorSystem[mColorSystemIndex]);


                        }

                        break;
                }

                /*

                String txt = holder.strText.getText().toString();
                txt = txt + "++;";
                holder.strText.setText(txt);
                strList.set(posi, txt);*/
            } else {
                Log.d("Maxs", "MyAdapter:updateItemView;12");
                /*String txt = strList.get(posi);
                txt = txt + "++;";
                strList.set(posi, txt);*/
            }
        }
    }

    public class ViewHolderUnoption {
        public TextView searchType;
    }

    public class ViewHolderOption {
        public TextView searchType;
        public TextView searchTypeValue;
    }
    
    public int MapKeyPadToIR(int keyCode, KeyEvent event) {
        String deviceName = InputDevice.getDevice(event.getDeviceId())
                .getName();
        Log.d("Maxs90", "deviceName" + deviceName);
        if (!deviceName.equals("MStar Smart TV Keypad")) {
            return keyCode;
        }
        switch (keyCode) {
            case KeyEvent.KEYCODE_CHANNEL_UP:
            	return KeyEvent.KEYCODE_DPAD_UP;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
            	return KeyEvent.KEYCODE_DPAD_DOWN ;
            case KeyEvent.KEYCODE_VOLUME_UP:
            	return  KeyEvent.KEYCODE_DPAD_RIGHT;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
            	return  KeyEvent.KEYCODE_DPAD_LEFT;
            case KeyEvent.KEYCODE_TV_INPUT:
            	return  KeyEvent.KEYCODE_ENTER;
            default:
                return keyCode;
        }

    }
}

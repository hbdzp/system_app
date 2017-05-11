package com.horion.tv.ui.search;

import android.app.Instrumentation;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.horion.tv.R;
import com.horion.tv.framework.MstarBaseActivity;
import com.horion.tv.framework.ui.TvIntent;
import com.horion.tv.util.PropHelp;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tvapi.atv.AtvManager;
import com.mstar.android.tvapi.atv.listener.OnAtvPlayerEventListener;
import com.mstar.android.tvapi.atv.vo.AtvEventScan;
import com.mstar.android.tvapi.atv.vo.EnumSetProgramInfo;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.listener.OnMhlEventListener;
import com.mstar.android.tvapi.common.TvManager;

public class ATVManualTuning extends MstarBaseActivity {
    /**
     * Called when the activity is first created.
     */
    private static final String TAG = "ATVManualTuning";

    private ViewHolder viewholder_atvmanualtuning;

    private int mColorSystemIndex = 0;

    private int mSoundSystemIndex = 0;

    private String mSoundSystem[] = {"BG", "DK", "I", "L", "M"};

    private String mColorSystem[] = {"PAL", "NTSC", "SECAM", "NTSC_44",
            "PAL_M", "PAL_N", "PAL_60", "NO_STAND", "AUTO"};


    private OnAtvPlayerEventListener mAtvPlayerEventListener = null;

    private Handler mAtvUiEventHandler = null;

    private BroadcastReceiver mReceiver = null;

    private boolean manualtuningEndFlag = true;

    private LinearLayout linear_channalNum;
    private LinearLayout linear_channalColorSystem;
    private LinearLayout linear_channalSoundSystem;
    private LinearLayout linear_channalSearch;
    private LinearLayout linear_channalFreq;
    private TextView tvChaAtvManualtuning;

    private int inputDigit = 0;
    private int channelNumberInput = 0;
    private int currentchannelNumber = 0;

    /**
     * For showing one arrow both left and right side
     */
    private static final int DIRECTION_NONE = 0;

    /**
     * For showing 2 arrows in the left side, no arrow in the right side
     */
    private static final int DIRECTION_LEFT = 1;

    /**
     * For showing no arrows in the left side, 2 arrow in the right side
     */
    private static final int DIRECTION_RIGHT = 2;


    private class AtvPlayerEventListener implements OnAtvPlayerEventListener {

        @Override
        public boolean onAtvAutoTuningScanInfo(int what, AtvEventScan extra) {
            Message msg = mAtvUiEventHandler.obtainMessage(what, extra);
            mAtvUiEventHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onAtvManualTuningScanInfo(int what, AtvEventScan extra) {
            Message msg = mAtvUiEventHandler.obtainMessage(what, extra);
            mAtvUiEventHandler.sendMessage(msg);
            return true;
        }

        @Override
        public boolean onAtvProgramInfoReady(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onSignalLock(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean onSignalUnLock(int arg0) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atvmanualtuning);

        viewholder_atvmanualtuning = new ViewHolder(ATVManualTuning.this);
        viewholder_atvmanualtuning.findViewForAtvManualTuning();
        // registerListeners();

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(
                        TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE)) {
                    Log.i(TAG, "Receive ACTION_CIPLUS_TUNER_UNAVAIABLE...");
                    finish();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(TvIntent.ACTION_CIPLUS_TUNER_UNAVAIABLE);
        registerReceiver(mReceiver, filter);
        tvChaAtvManualtuning = (TextView) findViewById(R.id.textview_cha_atvmanualtuning);
        linear_channalNum = (LinearLayout) findViewById(
                R.id.linearlayout_cha_atvmanualtuning_channelnum);
        linear_channalColorSystem = (LinearLayout) findViewById(
                R.id.linearlayout_cha_atvmanualtuning_colorsystem);
        linear_channalSoundSystem = (LinearLayout) findViewById(
                R.id.linearlayout_cha_atvmanualtuning_soundsystem);
        linear_channalSearch = (LinearLayout) findViewById(
                R.id.linearlayout_cha_atvmanualtuning_starttuning);
        linear_channalFreq = (LinearLayout) findViewById(
                R.id.linearlayout_cha_atvmanualtuning_frequency);
        // registerListeners();
        boolean hasDtmb = PropHelp.newInstance().hasDtmb();
        if (hasDtmb) {
            tvChaAtvManualtuning.setText(R.string.str_cha_atvmanualtuning);
        } else {
            tvChaAtvManualtuning.setText(R.string.str_cha_manualtuning);
        }
        setOnFocusChangeListeners();
        if (TvCommonManager.getInstance().getCurrentTvInputSource() != TvCommonManager.INPUT_SOURCE_ATV) {
            TvCommonManager.getInstance().setInputSource(
                    TvCommonManager.INPUT_SOURCE_ATV);
            TvChannelManager.getInstance().changeToFirstService(
                    TvChannelManager.FIRST_SERVICE_INPUT_TYPE_ATV,
                    TvChannelManager.FIRST_SERVICE_DEFAULT);
        }

        currentchannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber() + 1;
        if (currentchannelNumber > 199)
            currentchannelNumber = 1;
        updateAtvManualtuningComponents();

        TvManager.getInstance().getMhlManager()
                .setOnMhlEventListener(new OnMhlEventListener() {

                    @Override
                    public boolean onAutoSwitch(int arg0, int arg1, int arg2) {
                        Log.d("ATVManualTuning", "onAutoSwitch");
                        if (TvChannelManager.getInstance().getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                            TvChannelManager.getInstance().stopAtvManualTuning();
                        }
                        finish();

                        return false;
                    }

                    @Override
                    public boolean onKeyInfo(int arg0, int arg1, int arg2) {
                        Log.d("ATVManualTuning", "onKeyInfo");
                        return false;
                    }
                });

        mAtvUiEventHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                updateAtvTuningScanInfo((AtvEventScan) msg.obj);
            }
        };
        //manualtuningEndFlag = true;
        linear_channalSearch.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                LinearLayout container = (LinearLayout) view;
                int nCurrentFrequency = TvChannelManager.getInstance().getAtvCurrentFrequency();
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                        if (TvChannelManager.getInstance().getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_LEFT) {
                            TvChannelManager.getInstance().stopAtvManualTuning();
                        }
                        manualtuningEndFlag = false;
                        if (TvChannelManager.getInstance().getTuningStatus() != TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_RIGHT) {
                            TvChannelManager.getInstance().startAtvManualTuning(
                                            5 * 1000,
                                            nCurrentFrequency,
                                    TvChannelManager.ATV_MANUAL_TUNE_MODE_SEARCH_UP);
                        }
                        // Hisa 2016.03.04 add Freeze function start
                        Log.d("HotKeyService", "Cancel Free Menu6");
                        /*Intent intentCancel = new Intent();//取消静像菜单
                        intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
                        sendBroadcast(intentCancel);*/
                        // Hisa 2016.03.04 add Freeze function end
                        updateTuneIcon((LinearLayout) view, DIRECTION_RIGHT);
                        return true;
                    } else if (KeyEvent.KEYCODE_DPAD_LEFT == keyCode) {
                        if (TvChannelManager.getInstance().getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_RIGHT) {
                            TvChannelManager.getInstance().stopAtvManualTuning();
                        }
                        manualtuningEndFlag = false;
                        if (TvChannelManager.getInstance().getTuningStatus() != TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_LEFT) {
                            TvChannelManager.getInstance().startAtvManualTuning(
                                            5 * 1000,
                                            nCurrentFrequency,
                                    TvChannelManager.ATV_MANUAL_TUNE_MODE_SEARCH_DOWN);
                        }
                        // Hisa 2016.03.04 add Freeze function start
                        Log.d("HotKeyService", "Cancel Free Menu7");
                        /*Intent intentCancel = new Intent();//取消静像菜单
                        intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
                        sendBroadcast(intentCancel);*/
                        // Hisa 2016.03.04 add Freeze function end
                        updateTuneIcon((LinearLayout) view, DIRECTION_LEFT);
                        return true;
                    } else if (KeyEvent.KEYCODE_ENTER == keyCode) {
                        if (TvChannelManager.getInstance().getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                            TvChannelManager.getInstance().stopAtvManualTuning();
                        }
                        manualtuningEndFlag = true;
                        updateTuneIcon((LinearLayout) view, DIRECTION_NONE);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void updateTuneIcon(LinearLayout container, int nDirection) {
        switch (nDirection) {
            case DIRECTION_LEFT:
                container.getChildAt(1).setBackgroundResource(
                        R.drawable.left_delta_point);
                container.getChildAt(1).setVisibility(View.VISIBLE);
                container.getChildAt(2).setVisibility(View.INVISIBLE);
                break;
            case DIRECTION_RIGHT:
                container.getChildAt(2).setBackgroundResource(
                        R.drawable.right_delta_point);
                container.getChildAt(1).setVisibility(View.INVISIBLE);
                container.getChildAt(2).setVisibility(View.VISIBLE);
                break;
            case DIRECTION_NONE:
            default:
                container.getChildAt(1).setBackgroundResource(
                        R.drawable.ui_sdk_arrow_left);
                container.getChildAt(2).setBackgroundResource(
                        R.drawable.ui_sdk_arrow_right);
                container.getChildAt(1).setVisibility(View.VISIBLE);
                container.getChildAt(2).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAtvPlayerEventListener = new AtvPlayerEventListener();
        TvChannelManager.getInstance().registerOnAtvPlayerEventListener(
                mAtvPlayerEventListener);
    }

    public boolean MapKeyPadToIR(int keyCode, KeyEvent event) {
        String deviceName = InputDevice.getDevice(event.getDeviceId())
                .getName();
        Log.d(TAG, "deviceName" + deviceName);
        if (!deviceName.equals("MStar Smart TV Keypad"))
            return false;
        switch (keyCode) {
            case KeyEvent.KEYCODE_CHANNEL_UP:
                keyInjection(KeyEvent.KEYCODE_DPAD_UP);
                return true;
            case KeyEvent.KEYCODE_CHANNEL_DOWN:
                keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
                return true;
            default:
                return false;
        }

    }

    private void keyInjection(final int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_DPAD_UP
                || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
                || keyCode == KeyEvent.KEYCODE_DPAD_LEFT
                || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            new Thread() {
                public void run() {
                    try {
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(keyCode);
                    } catch (Exception e) {
                        ///Log.e("Exception when sendPointerSync", e.toString());
                    }
                }
            }.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // If MapKeyPadToIR returns true,the previous keycode has been changed
        // to responding
        // android d_pad keys,just return.
        if (MapKeyPadToIR(keyCode, event))
            return true;
        Intent intent = new Intent();
        int currentid = getCurrentFocus().getId();
        int maxv = mColorSystem.length;
        int maxs = mSoundSystem.length;
        int nCurrentFrequency = TvChannelManager.getInstance().getAtvCurrentFrequency();
        int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                switch (currentid) {
                    case R.id.linearlayout_cha_atvmanualtuning_frequency:
                        TvChannelManager.getInstance().startAtvManualTuning(3 * 1000,
                                nCurrentFrequency,
                                TvChannelManager.ATV_MANUAL_TUNE_MODE_FINE_TUNE_UP);
                        updateAtvManualtuningfreq();
                        Log.d("Maxs230","----->KEYCODE_DPAD_RIGHT:frequency");
                        TvChannelManager.getInstance().saveAtvProgram(currentchannelNumber - 1);
                        return true;
                    case R.id.linearlayout_cha_atvmanualtuning_starttuning:
                        if (linear_channalSearch.isSelected() && manualtuningEndFlag) {
                            if (TvChannelManager.getInstance().getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_LEFT) {
                                TvChannelManager.getInstance().stopAtvManualTuning();
                            }
                            // Hisa 2016.03.04 add Freeze function start
                            Log.d("HotKeyService", "Cancel Free Menu");
                /*Intent intentCancel = new Intent();//取消静像菜单
				intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				//K_TvPictureManager.getInstance().K_unFreezeImage();
				sendBroadcast(intentCancel);  */
                            // Hisa 2016.03.04 add Freeze function end
                            manualtuningEndFlag = false;
                            ImageView img = (ImageView) linear_channalSearch
                                    .getChildAt(1);
                            img.setBackgroundResource(R.drawable.ui_sdk_arrow_right);
                            TvChannelManager.getInstance().startAtvManualTuning(5 * 1000,
                                    nCurrentFrequency,
                                    TvChannelManager.ATV_MANUAL_TUNE_MODE_SEARCH_UP);
                        }
                        return true;
                    case R.id.linearlayout_cha_atvmanualtuning_channelnum:
                        if (currentchannelNumber < 199)
                            currentchannelNumber++;
                        else
                            currentchannelNumber = 1;
                        // Hisa 2016.03.04 add Freeze function start
                        Log.d("HotKeyService", "Cancel Free Menu1");
				/*Intent intentCancel = new Intent();//取消静像菜单
				intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				sendBroadcast(intentCancel); */
                        // Hisa 2016.03.04 add Freeze function end
                        TvChannelManager.getInstance().selectProgram((currentchannelNumber - 1),
                                TvChannelManager.SERVICE_TYPE_ATV);
                        updateAtvManualtuningComponents();
                        return true;
                    case R.id.linearlayout_cha_atvmanualtuning_colorsystem:
                        mColorSystemIndex = (mColorSystemIndex + 1) % (maxv);
                        if (mColorSystemIndex >= 3
                                && mColorSystemIndex <= 7) {
                            mColorSystemIndex = 8;
                        }
                        TvChannelManager.getInstance().setAtvVideoStandard(mColorSystemIndex);
                        viewholder_atvmanualtuning.text_cha_atvmanualtuning_colorsystem_val
                                .setText(mColorSystem[mColorSystemIndex]);
                        return true;
                    case R.id.linearlayout_cha_atvmanualtuning_soundsystem:
                        mSoundSystemIndex = (mSoundSystemIndex + 1) % (maxs);
                        if (mSoundSystemIndex == 3) {
                            mSoundSystemIndex = 4;
                        }
                        TvChannelManager.getInstance().setAtvAudioStandard(mSoundSystemIndex);
                        viewholder_atvmanualtuning.text_cha_atvmanualtuning_soundsystem_val
                                .setText(mSoundSystem[mSoundSystemIndex]);
                        //clark.chiu 20140929 begin
                        if (AtvManager.getAtvScanManager() != null) {
                             /*   return K_AtvManager.getInstance().K_setAtvProgramInfo( 
                            	K_EnumSetProgramInfo.K_E_SET_AUDIO_STANDARD, curChannelNumber, (EnumAtvSystemStandard.values()[mSoundSystemIndex]).getValue());  
                            */
                            try {
                                return AtvManager.getAtvScanManager().setAtvProgramInfo(
                                        EnumSetProgramInfo.E_SET_AUDIO_STANDARD, currentchannelNumber - 1, mSoundSystemIndex);
                            } catch (TvCommonException e) {

                            }

                        }

                        //clark.chiu 20140929 end

                        return true;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                switch (currentid) {
                    case R.id.linearlayout_cha_atvmanualtuning_frequency:
                        TvChannelManager.getInstance().startAtvManualTuning(3 * 1000,
                                nCurrentFrequency,
                                TvChannelManager.ATV_MANUAL_TUNE_MODE_FINE_TUNE_DOWN);
                        updateAtvManualtuningfreq();
                        Log.d("Maxs230","----->KEYCODE_DPAD_LEFT:frequency");
                        TvChannelManager.getInstance().saveAtvProgram(currentchannelNumber);
                        return true;
                    case R.id.linearlayout_cha_atvmanualtuning_starttuning:
                        if (linear_channalSearch.isSelected() && manualtuningEndFlag) {
                            if (TvChannelManager.getInstance().getTuningStatus() == TvChannelManager.TUNING_STATUS_ATV_MANUAL_TUNING_RIGHT) {
                                TvChannelManager.getInstance().stopAtvManualTuning();
                            }
                            // Hisa 2016.03.04 add Freeze function start
                            Log.d("HotKeyService", "Cancel Free Menu3");
				/*Intent intentCancel = new Intent();//取消静像菜单
				intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				//K_TvPictureManager.getInstance().K_unFreezeImage();
				sendBroadcast(intentCancel);  */
                            // Hisa 2016.03.04 add Freeze function end
                            manualtuningEndFlag = false;
                            ImageView img = (ImageView) linear_channalSearch
                                    .getChildAt(2);
                            img.setBackgroundResource(R.drawable.ui_sdk_arrow_left);
                            TvChannelManager.getInstance().startAtvManualTuning(5 * 1000,
                                    nCurrentFrequency,
                                    TvChannelManager.ATV_MANUAL_TUNE_MODE_SEARCH_DOWN);
                        }
                        return true;
                    case R.id.linearlayout_cha_atvmanualtuning_channelnum:
                        if (currentchannelNumber == 1)
                            currentchannelNumber = 199;
                        else
                            currentchannelNumber--;
                        Log.d("HotKeyService", "Cancel Free Menu4");
                        // Hisa 2016.03.04 add Freeze function start
				/*Intent intentCancel = new Intent();//取消静像菜单
				intentCancel.setAction(MIntent.ACTION_FREEZE_CANCEL_BUTTON);
				sendBroadcast(intentCancel);  */
                        // Hisa 2016.03.04 add Freeze function end
                        TvChannelManager.getInstance().selectProgram((currentchannelNumber - 1),
                                TvChannelManager.SERVICE_TYPE_ATV);
                        updateAtvManualtuningComponents();
                        return true;
                    case R.id.linearlayout_cha_atvmanualtuning_colorsystem:
                        mColorSystemIndex = (mColorSystemIndex + maxv - 1) % (maxv);
                        if (mColorSystemIndex >= 3
                                && mColorSystemIndex <= 7) {
                            mColorSystemIndex = 2;
                        }
                        TvChannelManager.getInstance().setAtvVideoStandard(mColorSystemIndex);
                        viewholder_atvmanualtuning.text_cha_atvmanualtuning_colorsystem_val
                                .setText(mColorSystem[mColorSystemIndex]);
                        return true;
                    case R.id.linearlayout_cha_atvmanualtuning_soundsystem:
                        mSoundSystemIndex = (mSoundSystemIndex + maxs - 1) % (maxs);
                        if (mSoundSystemIndex == 3) {
                            mSoundSystemIndex = 2;
                        }
                        TvChannelManager.getInstance().setAtvAudioStandard(mSoundSystemIndex);
                        viewholder_atvmanualtuning.text_cha_atvmanualtuning_soundsystem_val
                                .setText(mSoundSystem[mSoundSystemIndex]);
                        //clark.chiu 20140929 begin
                        try {
                            if (AtvManager.getAtvScanManager() != null) {
                                return AtvManager.getAtvScanManager().setAtvProgramInfo(
                                        EnumSetProgramInfo.E_SET_AUDIO_STANDARD, currentchannelNumber - 1 , mSoundSystemIndex);
                            }
                        } catch (TvCommonException e) {

                        }


                        //clark.chiu 20140929 end                        

                        return true;
                    default:
                        break;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (!manualtuningEndFlag) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (!manualtuningEndFlag) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_BACK:
            case KeyEvent.KEYCODE_MENU:
                if (TvChannelManager.getInstance().getTuningStatus() != TvChannelManager.TUNING_STATUS_NONE) {
                    TvChannelManager.getInstance().stopAtvManualTuning();
                }
                /*intent.setAction(TvIntent.MAINMENU);
                intent.putExtra("currentPage", MainMenuActivity.CHANNEL_PAGE);
                startActivity(intent);*/
                finish();
                return true;
            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_1:
            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_3:
            case KeyEvent.KEYCODE_4:
            case KeyEvent.KEYCODE_5:
            case KeyEvent.KEYCODE_6:
            case KeyEvent.KEYCODE_7:
            case KeyEvent.KEYCODE_8:
            case KeyEvent.KEYCODE_9:
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_TV_INPUT:
                if (currentid == R.id.linearlayout_cha_atvmanualtuning_channelnum) {
                    if (linear_channalNum.isSelected()) {
                        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_TV_INPUT) {
                            if (inputDigit > 0) {
                                inputDigit = 0;
                                if (channelNumberInput <= 199
                                        && channelNumberInput > 0) {
                                    currentchannelNumber = channelNumberInput;

                                    TvChannelManager.getInstance().selectProgram((currentchannelNumber - 1),
                                            TvChannelManager.SERVICE_TYPE_ATV);
                                }
                                updateAtvManualtuningComponents();
                                return true;
                            }

                        } else {
                            inputDigit++;
                            if (inputDigit > 3) {
                                inputDigit = 0;
                                channelNumberInput = 0;
                                updateAtvManualtuningComponents();
                                return true;
                            }

                            int inputnum = keyCode - KeyEvent.KEYCODE_0;

                            if (inputDigit == 1) {
                                channelNumberInput = inputnum;
                            } else if (inputDigit == 2) {
                                channelNumberInput = channelNumberInput * 10
                                        + inputnum;
                            } else if (inputDigit == 3) {
                                channelNumberInput = channelNumberInput * 10
                                        + inputnum;
                            }
                            String str_val;
                            str_val = Integer.toString(channelNumberInput);
                            viewholder_atvmanualtuning.text_cha_atvmanualtuning_channelnum_val
                                    .setText(str_val);
                        }

                    }
                }
                return true;
            default:
                break;
        }
        if (KeyEvent.KEYCODE_TV_INPUT == keyCode)
            if (!manualtuningEndFlag)
                return true;
            else
                finish();
        return super.onKeyDown(keyCode, event);
    }

    /*
     * private void registerListeners() { } private OnClickListener listener =
     * new OnClickListener() { Intent intent = new Intent();
     *
     * @Override public void onClick(View view) { // TODO Auto-generated method
     * stub switch (view.getId()) { } } };
     */
    private void updateAtvManualtuningfreq() {
        String str_val;
        int freqKhz = TvChannelManager.getInstance().getAtvCurrentFrequency();
        //str_val = String.format("%.2f", freqKhz / 1000.0);
        int minteger = freqKhz / 1000;
        int mfraction = (freqKhz % 1000) / 10;
        if (mfraction < 5) {
            mfraction = 0;
        } else if ((mfraction >= 5) && (mfraction < 10)) {
            mfraction = 5;
        } else if ((mfraction >= 10) && (mfraction < 15)) {
            mfraction = 10;
        } else if ((mfraction >= 15) && (mfraction < 20)) {
            mfraction = 15;
        } else if ((mfraction >= 20) && (mfraction < 25)) {
            mfraction = 20;
        } else if ((mfraction >= 25) && (mfraction < 30)) {
            mfraction = 25;
        } else if ((mfraction >= 30) && (mfraction < 35)) {
            mfraction = 30;
        } else if ((mfraction >= 35) && (mfraction < 40)) {
            mfraction = 35;
        } else if ((mfraction >= 40) && (mfraction < 45)) {
            mfraction = 40;
        } else if ((mfraction >= 45) && (mfraction < 50)) {
            mfraction = 45;
        } else if ((mfraction >= 50) && (mfraction < 55)) {
            mfraction = 50;
        } else if ((mfraction >= 55) && (mfraction < 60)) {
            mfraction = 55;
        } else if ((mfraction >= 60) && (mfraction < 65)) {
            mfraction = 60;
        } else if ((mfraction >= 65) && (mfraction < 70)) {
            mfraction = 65;
        } else if ((mfraction >= 70) && (mfraction < 75)) {
            mfraction = 70;
        } else if ((mfraction >= 75) && (mfraction < 80)) {
            mfraction = 75;
        } else if ((mfraction >= 80) && (mfraction < 85)) {
            mfraction = 80;
        } else if ((mfraction >= 85) && (mfraction < 90)) {
            mfraction = 85;
        } else if ((mfraction >= 90) && (mfraction < 95)) {
            mfraction = 90;
        } else if ((mfraction >= 95) && (mfraction < 100)) {
            mfraction = 95;
        }
        if (mfraction < 10)
            str_val = Integer.toString(minteger) + ".0"
                    + Integer.toString(mfraction);
        else
            str_val = Integer.toString(minteger) + "."
                    + Integer.toString(mfraction);
        viewholder_atvmanualtuning.text_cha_atvmanualtuning_freqency_val
                .setText(str_val);
    }

    private void updateAtvManualtuningComponents() {
        String str_val;
		/*int curChannelNum = mTvChannelManager.getCurrentChannelNumber() + 1;*/
        str_val = Integer.toString(currentchannelNumber/* curChannelNum */);

        // 0.250M
        str_val = Integer.toString(currentchannelNumber);
        // Hch 20160309 change for ATV manual color system display  // K_getAvdVideoStandard()
        mColorSystemIndex = TvChannelManager.getInstance().getAtvVideoSystem().ordinal()
                % (mColorSystem.length);
        // change end
        mSoundSystemIndex = TvChannelManager.getInstance().getAtvAudioStandard()
                % mSoundSystem.length;
        viewholder_atvmanualtuning.text_cha_atvmanualtuning_channelnum_val
                .setText(str_val);
        viewholder_atvmanualtuning.text_cha_atvmanualtuning_colorsystem_val
                .setText(mColorSystem[mColorSystemIndex]);
        viewholder_atvmanualtuning.text_cha_atvmanualtuning_soundsystem_val
                .setText(mSoundSystem[mSoundSystemIndex]);
        updateAtvManualtuningfreq();
    }

    @Override
    protected void onPause() {
        TvChannelManager.getInstance().unregisterOnAtvPlayerEventListener(
                mAtvPlayerEventListener);
        mAtvPlayerEventListener = null;

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private void updateAtvTuningScanInfo(AtvEventScan extra) {
        String str_val;
        int frequency = extra.frequencyKHz;
        int percent = extra.percent;
        int minteger = frequency / 1000;
        int mfraction = (frequency % 1000) / 10;
        str_val = Integer.toString(minteger) + "."
                + Integer.toString(mfraction);
        viewholder_atvmanualtuning.text_cha_atvmanualtuning_freqency_val
                .setText(str_val);
        Log.d("Maxs230","updateAtvTuningScanInfo:percent = " +percent);
        if (percent >= 100) {
            updateTuneIcon(linear_channalSearch, DIRECTION_NONE);
            TvChannelManager.getInstance().stopAtvManualTuning();
            manualtuningEndFlag = true;
            ImageView img1 = (ImageView) linear_channalSearch
                    .getChildAt(1);
            ImageView img2 = (ImageView) linear_channalSearch
                    .getChildAt(2);
            img1.setBackgroundResource(R.drawable.ui_sdk_arrow_left);
            img2.setBackgroundResource(R.drawable.ui_sdk_arrow_right);
            int atvProgCount = TvChannelManager.getInstance().getProgramCount(TvChannelManager.PROGRAM_COUNT_ATV);
            int curProgNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
            if ((atvProgCount == 0) || (curProgNumber == 0xFF)) {
                curProgNumber = 0;
            }

            Log.i("ATVManualTuning", "count:" + atvProgCount + "---current:"
                    + curProgNumber);
            Log.d("Maxs230","----->updateAtvTuningScanInfo:percent >= 100, currentchannelNumber - 1 = " + (currentchannelNumber - 1));
            TvChannelManager.getInstance().saveAtvProgram(currentchannelNumber - 1);
            int curChannelNumber = TvChannelManager.getInstance().getCurrentChannelNumber();
            TvChannelManager.getInstance().selectProgram(currentchannelNumber - 1,
                    TvChannelManager.SERVICE_TYPE_ATV);
            updateAtvManualtuningComponents();
        }
    }

    private void setOnFocusChangeListeners() {
        OnFocusChangeListener linearFocusChange = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean arg1) {
                // TODO Auto-generated method stub
                LinearLayout linear = (LinearLayout) view;
                if (manualtuningEndFlag) {
                    if (!linear.isSelected()) {
                        linear.getChildAt(1).setVisibility(View.VISIBLE);
                        linear.getChildAt(linear.getChildCount() - 1)
                                .setVisibility(View.VISIBLE);
                        linear.setSelected(true);
                    } else {
                        linear.getChildAt(1).setVisibility(View.INVISIBLE);
                        linear.getChildAt(linear.getChildCount() - 1)
                                .setVisibility(View.INVISIBLE);
                        linear.setSelected(false);
                    }
                    if (inputDigit != 0) {
                        inputDigit = 0;
                        updateAtvManualtuningComponents();
                    }
                }
            }
        };
        linear_channalNum.setOnFocusChangeListener(linearFocusChange);
        linear_channalColorSystem.setOnFocusChangeListener(linearFocusChange);
        linear_channalSoundSystem.setOnFocusChangeListener(linearFocusChange);
        linear_channalSearch.setOnFocusChangeListener(linearFocusChange);
        linear_channalFreq.setOnFocusChangeListener(linearFocusChange);
    }
}

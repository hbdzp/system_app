package com.horion.tv.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.horion.tv.R;
import com.horion.tv.ui.search.ViewHolder;
import com.mstar.android.tv.TvAudioManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvEpgManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.common.vo.DTVSpecificProgInfo;
import com.mstar.android.tvapi.common.vo.DtvProgramSignalInfo;
import com.mstar.android.tvapi.common.vo.PresentFollowingEventInfo;
import com.mstar.android.tvapi.common.vo.ProgramInfo;
import com.mstar.android.tvapi.dtv.vo.DtvAudioInfo;

public class ChannelInfoActivity extends Activity {
    private ViewHolder channelInfoActivity;
    private String[] mAudioTypeDisplayString = { "MPEG", "AC3", "AAC", "AC3P", "DRA1" };

    private String[] mVideoTypeDisplayString = { "", "MPEG", "H.264", "AVS",
            "VC1","HEVC" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV){
            setContentView(R.layout.atv_channel_info);
            channelInfoActivity = new ViewHolder(ChannelInfoActivity.this);
            channelInfoActivity.findAtvViewForChannelInfo();
            String mStr = null;
            ProgramInfo pinfo = TvChannelManager.getInstance().getCurrentProgramInfo();
            channelInfoActivity.textview_atv_channelinfo_channelnumber.setText((pinfo.number + 1)+"");
            channelInfoActivity.textview_atv_channelinfo_channelname.setText(getString(R.string.str_cha_autotuning_tuningtype_atv) + (pinfo.number + 1));
            if (TvChannelManager.getInstance().isSignalStabled() == true){
                switch (TvChannelManager.getInstance().getAtvAudioStandard()) {
                    case TvChannelManager.ATV_AUDIO_STANDARD_BG:
                        mStr = "BG";
                        break;
                    case TvChannelManager.ATV_AUDIO_STANDARD_DK:
                        mStr = "DK";
                        break;
                    case TvChannelManager.ATV_AUDIO_STANDARD_I:
                        mStr = " I";
                        break;
                    case TvChannelManager.ATV_AUDIO_STANDARD_L:
                        mStr = " L";
                        break;
                    case TvChannelManager.ATV_AUDIO_STANDARD_M:
                        mStr = " M ";
                        break;
                    default:
                        mStr = "BG";
                }
            }else{
                mStr = getResources().getString(R.string.str_sound_format_unknown);
            }

            channelInfoActivity.textview_atv_channelinfo_colorsystem.setText(mStr);

            switch (TvChannelManager.getInstance().getAvdVideoStandard()) {
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_BGHI:
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_M:
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_N:
                case TvChannelManager.AVD_VIDEO_STANDARD_PAL_60:
                    mStr = "PAL";
                    break;
                case TvChannelManager.AVD_VIDEO_STANDARD_NTSC_44:
                case TvChannelManager.AVD_VIDEO_STANDARD_NTSC_M:
                    mStr = "NTSC";
                    break;
                case TvChannelManager.AVD_VIDEO_STANDARD_SECAM:
                    mStr = "SECAM";
                    break;
                default:
                    mStr = "";
            }
            channelInfoActivity.textview_atv_channelinfo_soundsystem.setText(mStr);
        }else{
            setContentView(R.layout.dtv_channel_info);
            channelInfoActivity = new ViewHolder(ChannelInfoActivity.this);
            channelInfoActivity.findDtvViewForChannelInfo();
            ProgramInfo currentProgInfo = TvChannelManager.getInstance().getCurrentProgramInfo();
            channelInfoActivity.textview_dtv_channelinfo_channelnumber.setText(currentProgInfo.number+"");


            channelInfoActivity.textview_dtv_channelinfo_channelname.setText(currentProgInfo.serviceName);
            channelInfoActivity.textview_dtv_channelinfo_sigfrequency.setText(currentProgInfo.frequency/1000 + "MHz");
            String mStr = null;
            DtvAudioInfo audioInfo = TvChannelManager.getInstance().getAudioInfo();
            if (audioInfo.audioInfos.length > 0) {
                if (audioInfo.audioInfos[audioInfo.currentAudioIndex] != null) {
                    mStr = "";
                    Log.d("Maxs150","audioInfo.currentAudioIndex = " + audioInfo.currentAudioIndex);
                    Log.d("Maxs150","audioInfo.audioInfos[audioInfo.currentAudioIndex].audioTypex = " + audioInfo.audioInfos[audioInfo.currentAudioIndex].audioType);
                    Log.d("Maxs150","audioInfo.audioInfos[audioInfo.currentAudioIndex].toString() = " + audioInfo.audioInfos[audioInfo.currentAudioIndex].toString());
                    Log.d("Maxs150","audioInfo.currentAudioIndex = " + audioInfo.currentAudioIndex);
                    ///if (audioInfo.audioInfos[audioInfo.currentAudioIndex].audioType < mAudioTypeDisplayString.length) {
                        ///mStr = mAudioTypeDisplayString[audioInfo.audioInfos[audioInfo.currentAudioIndex].audioType];
                        Log.d("Maxs150","audioInfo.currentAudioIndex mStr = " + mStr);
                        switch (audioInfo.audioInfos[audioInfo.currentAudioIndex].audioType){
                            case TvAudioManager.AUDIO_TYPE_MPEG:
                                mStr = mAudioTypeDisplayString[0];
                                break;
                            case TvAudioManager.AUDIO_TYPE_Dolby_D:
                                mStr = mAudioTypeDisplayString[1];
                                break;
                            case TvAudioManager.AUDIO_TYPE_AAC:
                                mStr = mAudioTypeDisplayString[2];
                                break;
                            case TvAudioManager.AUDIO_TYPE_AC3P:
                                mStr = mAudioTypeDisplayString[3];
                                break;
                            case TvAudioManager.AUDIO_TYPE_DRA1:

                                mStr = mAudioTypeDisplayString[4];
                                Log.d("Maxs150","audioInfo.currentAudioIndex mStr = " + mStr);
                                break;
                        }
                    //}
                    channelInfoActivity.textview_dtv_channelinfo_soundformat.setText(mStr);
                }
            }
            PresentFollowingEventInfo pfEvtInfo = TvEpgManager.getInstance().getEpgPresentFollowingEventInfo(
                    currentProgInfo.serviceType, currentProgInfo.number,
                    true, TvEpgManager.EPG_DETAIL_DESCRIPTION);
            DTVSpecificProgInfo dvtSpecProgInfo = TvChannelManager.getInstance().getCurrentProgramSpecificInfo();
            mStr = "";
            if (dvtSpecProgInfo != null){
                int type =dvtSpecProgInfo.videoType;
                if (type < mVideoTypeDisplayString.length) {
                    mStr = mVideoTypeDisplayString[type];
                }
            }

            channelInfoActivity.textview_dtv_channelinfo_imagformat.setText(mStr);
            DtvProgramSignalInfo tDtvProgramSignalInfo = TvChannelManager.getInstance().getCurrentSignalInformation();
           /// Log.d("Maxs55","tDtvProgramSignalInfo == null = " + tDtvProgramSignalInfo.quality);
            if (tDtvProgramSignalInfo != null){
                channelInfoActivity.textview_dtv_channelinfo_sigquality.setText(tDtvProgramSignalInfo.quality+"");
                channelInfoActivity.textview_dtv_channelinfo_sigintensity.setText(tDtvProgramSignalInfo.strength+"");
            }

                    /*        textview_dtv_channelinfo_sigintensity
            textview_dtv_channelinfo_sigquality =*/
        }

    }

    private String getCurProgrameName() {
        int pgNum = TvChannelManager.getInstance().getCurrentChannelNumber();
        int st = TvChannelManager.SERVICE_TYPE_ATV;
        if (TvCommonManager.getInstance().getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV) {
            st = TvChannelManager.SERVICE_TYPE_DTV;
        }
        String pgName = TvChannelManager.getInstance().getProgramName(pgNum, st, 0x00);
        return pgName;
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

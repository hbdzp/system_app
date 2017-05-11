//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2014 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>

package com.horion.tv.ui.search;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.horion.tv.R;
import com.horion.tv.ui.ChannelInfoActivity;

public class ViewHolder {

    private ChannelTuning channeltune;
    private DTVManualTuning dtvmanualtune;
    private ATVManualTuning atvmanualtune;
    private ChannelInfoActivity channelInfoActivity;
    /*---------------for channeltuning-----------------------*/
    public TextView textView_search_title;
    public RelativeLayout relativeLayout_auto_mainmenu;
    public LinearLayout linearLayout_dtv_autochannel;

    public TextView textView_autochannel_dtvchannelnumber;
    public TextView textView_autochannel_dtvcurrentfrq;
    public TextView textView_autochannel_dtvradionumber;
    public TextView textView_autochannel_dtvdatanumber;
    public TextView textView_autochannel_dtvsearchprogressbarvalue;
    public ProgressBarCustom progressBarCustom_autochannel_dtvsearchprogressbar;

    public LinearLayout linearLayout_atv_autochannel;

    public TextView textView_autochannel_atvchannelnumber;
    public TextView textView_autochannel_atvcurrentfrq;
    public TextView textView_autochannel_atvfrqsegment;
    public TextView textView_autochannel_atvsearchprogressbarvalue;
    public ProgressBarCustom progressBarCustom_autochannel_atvsearchprogressbar;


    /*-------------for dtvmanualtuning----------------------*/
    public LinearLayout linear_cha_dtvmanualtuning_channelnum;

    public TextView text_cha_dtvmanualtuning_channelnum_val;

    public TextView text_cha_dtvmanualtuning_modulation_val;

    public LinearLayout linear_cha_dtvmanualtuning_channelfreq;

    public LinearLayout linear_cha_dtvmanualtuning_signalstrength_val;

    public LinearLayout linear_cha_dtvmanualtuning_signalquality_val;

    public TextView text_cha_dtvmanualtuning_tuningresult_dtv_val;

    public TextView text_cha_dtvmanualtuning_tuningresult_data_val;

    public TextView text_cha_dtvmanualtuning_tuningresult_radio_val;

    public TextView text_cha_dtvmanualtuning_symbol_val;

    public TextView text_cha_dtvmanualtuning_frequency_val;

    public LinearLayout linear_cha_dtvmanualtuning_tuningresult;
    //===============ktc add start==========
    public TextView text_cha_tv;
    public TextView text_cha_curch_val;
    public TextView text_cha_dtvmanualtuning_start_state;

    /*-------------for atvmanualtuning----------------------*/
    public TextView text_cha_atvmanualtuning_channelnum_val;

    public LinearLayout linear_cha_atvmanualtuning_colorsystem;

    public TextView text_cha_atvmanualtuning_colorsystem_val;

    public LinearLayout linear_cha_atvmanualtuning_soundsystem;

    public TextView text_cha_atvmanualtuning_soundsystem_val;

    public TextView text_cha_atvmanualtuning_freqency_val;


    public ViewHolder(ChannelTuning activity) {
        this.channeltune = activity;
    }

    public ViewHolder(DTVManualTuning activity) {
        this.dtvmanualtune = activity;
    }

    public ViewHolder(ATVManualTuning activity) {
        this.atvmanualtune = activity;
    }




    /*------------------------for DTV ChannelInfo-------------------------*/
    public TextView textview_dtv_channelinfo_channelnumber;
    public TextView textview_dtv_channelinfo_channelname;
    public TextView textview_dtv_channelinfo_sigfrequency;
    public TextView textview_dtv_channelinfo_soundformat;
    public TextView textview_dtv_channelinfo_imagformat;
    public TextView textview_dtv_channelinfo_sigintensity;
    public TextView textview_dtv_channelinfo_sigquality;

    /*------------------------for ATV ChannelInfo-------------------------*/
    public TextView textview_atv_channelinfo_channelnumber;
    public TextView textview_atv_channelinfo_channelname;
    public TextView textview_atv_channelinfo_colorsystem;
    public TextView textview_atv_channelinfo_soundsystem;

    public ViewHolder(ChannelInfoActivity activity){
        this.channelInfoActivity = activity;
    }


    public void findViewForChannelTuning() {
        textView_search_title = (TextView)channeltune.findViewById(R.id.search_title);
        relativeLayout_auto_mainmenu = (RelativeLayout) channeltune
                .findViewById(R.id.auto_mainmenu);
        linearLayout_dtv_autochannel = (LinearLayout) channeltune.findViewById(R.id.dtv_autochannel);
        textView_autochannel_dtvchannelnumber = (TextView)channeltune.findViewById(R.id.autochannel_dtvchannelnumber);

        textView_autochannel_dtvcurrentfrq = (TextView)channeltune.findViewById(R.id.autochannel_dtvcurrentfrq);
        textView_autochannel_dtvradionumber = (TextView)channeltune.findViewById(R.id.autochannel_dtvradionumber);
        textView_autochannel_dtvdatanumber = (TextView)channeltune.findViewById(R.id.autochannel_dtvdatanumber);
        textView_autochannel_dtvsearchprogressbarvalue = (TextView)channeltune.findViewById(R.id.autochannel_dtvsearchprogressbarvalue);
        progressBarCustom_autochannel_dtvsearchprogressbar = (ProgressBarCustom)channeltune.findViewById(R.id.autochannel_dtvsearchprogressbar);

        linearLayout_atv_autochannel = (LinearLayout) channeltune.findViewById(R.id.atv_autochannel);
        textView_autochannel_atvchannelnumber = (TextView)channeltune.findViewById(R.id.autochannel_atvchannelnumber);

        textView_autochannel_atvcurrentfrq = (TextView)channeltune.findViewById(R.id.autochannel_atvcurrentfrq);
        textView_autochannel_atvfrqsegment = (TextView)channeltune.findViewById(R.id.autochannel_atvfrqsegment);
        textView_autochannel_atvsearchprogressbarvalue = (TextView)channeltune.findViewById(R.id.autochannel_atvsearchprogressbarvalue);
        progressBarCustom_autochannel_atvsearchprogressbar = (ProgressBarCustom)channeltune.findViewById(R.id.autochannel_atvsearchprogressbar);

    }


    public void findViewForDtvManualTuning() {
        linear_cha_dtvmanualtuning_channelnum = (LinearLayout) dtvmanualtune
                .findViewById(R.id.linearlayout_cha_dtvmanualtuning_channelnum);
        linear_cha_dtvmanualtuning_channelfreq = (LinearLayout) dtvmanualtune
                .findViewById(R.id.linearlayout_cha_dtvmanualtuning_frequency);
        text_cha_dtvmanualtuning_channelnum_val = (TextView) dtvmanualtune
                .findViewById(R.id.textview_cha_dtvmanualtuning_channelnum_val);
        text_cha_dtvmanualtuning_modulation_val = (TextView) dtvmanualtune
                .findViewById(R.id.textview_cha_dtvmanualtuning_modulation_val);
        linear_cha_dtvmanualtuning_signalstrength_val = (LinearLayout) dtvmanualtune
                .findViewById(R.id.linearlayout_cha_dtvmanualtuning_signalstrength_val);
        linear_cha_dtvmanualtuning_signalquality_val = (LinearLayout) dtvmanualtune
                .findViewById(R.id.linearlayout_cha_dtvmanualtuning_signalquality_val);
        text_cha_dtvmanualtuning_tuningresult_dtv_val = (TextView) dtvmanualtune
                .findViewById(R.id.textview_cha_dtvmanualtuning_tuningresult_dtv_val);
        text_cha_dtvmanualtuning_tuningresult_data_val = (TextView) dtvmanualtune
                .findViewById(R.id.textview_cha_dtvmanualtuning_tuningresult_data_val);
        text_cha_dtvmanualtuning_tuningresult_radio_val = (TextView) dtvmanualtune
                .findViewById(R.id.textview_cha_dtvmanualtuning_tuningresult_radio_val);
        text_cha_dtvmanualtuning_symbol_val = (TextView) dtvmanualtune
                .findViewById(R.id.textview_cha_dtvmanualtuning_symbol_val);
        text_cha_dtvmanualtuning_frequency_val = (TextView) dtvmanualtune
                .findViewById(R.id.textview_cha_dtvmanualtuning_frequency_val);
        text_cha_dtvmanualtuning_start_state = (TextView)dtvmanualtune.findViewById(R.id.textview_cha_dtvmanualtuning_starttuning_state);
    }


    /*--------------for atvmanualtuning-----------------------*/
    public void findViewForAtvManualTuning() {
        text_cha_atvmanualtuning_channelnum_val = (TextView) atvmanualtune
                .findViewById(R.id.textview_cha_atvmanualtuning_channelnum_val);
        linear_cha_atvmanualtuning_colorsystem = (LinearLayout) atvmanualtune
                .findViewById(R.id.linearlayout_cha_atvmanualtuning_colorsystem);
        text_cha_atvmanualtuning_colorsystem_val = (TextView) atvmanualtune
                .findViewById(R.id.textview_cha_atvmanualtuning_colorsystem_val);
        linear_cha_atvmanualtuning_soundsystem = (LinearLayout) atvmanualtune
                .findViewById(R.id.linearlayout_cha_atvmanualtuning_soundsystem);
        text_cha_atvmanualtuning_soundsystem_val = (TextView) atvmanualtune
                .findViewById(R.id.textview_cha_atvmanualtuning_soundsystem_val);
        text_cha_atvmanualtuning_freqency_val = (TextView) atvmanualtune
                .findViewById(R.id.textview_cha_atvmanualtuning_frequency_val);
    }


    /*------------------for DTVChannelInfo---------------------------------*/
    public void findDtvViewForChannelInfo(){
        textview_dtv_channelinfo_channelnumber = (TextView)channelInfoActivity.findViewById(R.id.dtv_channelinfo_channelnumber);
        textview_dtv_channelinfo_channelname = (TextView)channelInfoActivity.findViewById(R.id.dtv_channelinfo_channelname);
        textview_dtv_channelinfo_sigfrequency = (TextView)channelInfoActivity.findViewById(R.id.dtv_channelinfo_sigfrequency);
        textview_dtv_channelinfo_soundformat = (TextView)channelInfoActivity.findViewById(R.id.dtv_channelinfo_soundformat);
        textview_dtv_channelinfo_imagformat = (TextView)channelInfoActivity.findViewById(R.id.dtv_channelinfo_imagformat);
        textview_dtv_channelinfo_sigintensity = (TextView)channelInfoActivity.findViewById(R.id.dtv_channelinfo_sigintensity);
        textview_dtv_channelinfo_sigquality = (TextView)channelInfoActivity.findViewById(R.id.dtv_channelinfo_sigquality);

    }

    /*------------------for DTVChannelInfo---------------------------------*/
    public void findAtvViewForChannelInfo(){
        textview_atv_channelinfo_channelnumber = (TextView)channelInfoActivity.findViewById(R.id.atv_channelinfo_channelnumber);
        textview_atv_channelinfo_channelname = (TextView)channelInfoActivity.findViewById(R.id.atv_channelinfo_channelname);
        textview_atv_channelinfo_colorsystem = (TextView)channelInfoActivity.findViewById(R.id.atv_channelinfo_colorsystem);
        textview_atv_channelinfo_soundsystem = (TextView)channelInfoActivity.findViewById(R.id.atv_channelinfo_soundsystem);
    }
}

/**
 * MStar Software
 * Copyright (c) 2011 - 2012 MStar Semiconductor, Inc. All rights reserved.
 *
 * All software, firmware and related documentation herein (“MStar Software”) are
 * intellectual property of MStar Semiconductor, Inc. (“MStar”) and protected by
 * law, including, but not limited to, copyright law and international treaties.
 * Any use, modification, reproduction, retransmission, or republication of all
 * or part of MStar Software is expressly prohibited, unless prior written
 * permission has been granted by MStar.
 *
 * By accessing, browsing and/or using MStar Software, you acknowledge that you
 * have read, understood, and agree, to be bound by below terms (“Terms”) and to
 * comply with all applicable laws and regulations:
 *
 * 1. MStar shall retain any and all right, ownership and interest to MStar
 * Software and any modification/derivatives thereof.  No right, ownership,
 * or interest to MStar Software and any modification/derivatives thereof is
 * transferred to you under Terms.
 *
 * 2. You understand that MStar Software might include, incorporate or be supplied
 * together with third party’s software and the use of MStar Software may require
 * additional licenses from third parties.  Therefore, you hereby agree it is your
 * sole responsibility to separately obtain any and all third party right and
 * license necessary for your use of such third party’s software.
 *
 * 3. MStar Software and any modification/derivatives thereof shall be deemed as
 * MStar’s confidential information and you agree to keep MStar’s confidential
 * information in strictest confidence and not disclose to any third party.
 *
 * 4. MStar Software is provided on an “AS IS” basis without warranties of any kind.
 * Any warranties are hereby expressly disclaimed by MStar, including without
 * limitation, any warranties of merchantability, non-infringement of intellectual
 * property rights, fitness for a particular purpose, error free and in conformity
 * with any international standard.  You agree to waive any claim against MStar for
 * any loss, damage, cost or expense that you may incur related to your use of MStar
 * Software.  In no event shall MStar be liable for any direct, indirect, incidental
 * or consequential damages, including without limitation, lost of profit or revenues,
 * lost or damage of data, and unauthorized system use.  You agree that this Section 4
 * shall still apply without being affected even if MStar Software has been modified
 * by MStar in accordance with your request or instruction for your use, except
 * otherwise agreed by both parties in writing.
 *
 * 5. If requested, MStar may from time to time provide technical supports or
 * services in relation with MStar Software to you for your use of MStar Software
 * in conjunction with your or your customer’s product (“Services”).  You understand
 * and agree that, except otherwise agreed by both parties in writing, Services are
 * provided on an “AS IS” basis and the warranty disclaimer set forth in Section 4
 * above shall apply.
 *
 * 6. Nothing contained herein shall be construed as by implication, estoppels or
 * otherwise: (a) conferring any license or right to use MStar name, trademark,
 * service mark, symbol or any other identification; (b) obligating MStar or any
 * of its affiliates to furnish any person, including without limitation, you and
 * your customers, any assistance of any kind whatsoever, or any information; or
 * (c) conferring any license or right under any intellectual property right.
 *
 * 7. These terms shall be governed by and construed in accordance with the laws
 * of Taiwan, R.O.C., excluding its conflict of law rules.  Any and all dispute
 * arising out hereof or related hereto shall be finally settled by arbitration
 * referred to the Chinese Arbitration Association, Taipei in accordance with
 * the ROC Arbitration Law and the Arbitration Rules of the Association by three (3)
 * arbitrators appointed in accordance with the said Rules.  The place of
 * arbitration shall be in Taipei, Taiwan and the language shall be English.
 * The arbitration award shall be final and binding to both parties.
 */
package com.mstar.tvsetting.hotkey;


import com.mstar.tvsetting.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InputSourceAdapter extends BaseAdapter {
	private Context con;
	private String[] mData;
	private int focusPosition = -1;
	private int currentSource = 0;
	private int[] sourceImageRes_n = { R.drawable.icon_73_watch_tv_n_18x18
			, R.drawable.icon_73_watch_tv_n_18x18
			, R.drawable.icon_168_browse_pc_n_18x18
			, R.drawable.icon_193_usb_n_18x18
			, R.drawable.icon_211_video_input_n_18x18
			, R.drawable.icon_203_hdmi_n_18x18
			, R.drawable.icon_203_hdmi_n_18x18
			, R.drawable.icon_203_hdmi_n_18x18 };
	private int[] sourceImageRes_d = { R.drawable.icon_73_watch_tv_d_18x18
			,R.drawable.icon_73_watch_tv_d_18x18
			, R.drawable.icon_168_browse_pc_d_18x18
			, R.drawable.icon_193_usb_d_18x18
			, R.drawable.icon_211_video_input_d_18x18
			, R.drawable.icon_203_hdmi_d_18x18
			, R.drawable.icon_203_hdmi_d_18x18
			, R.drawable.icon_203_hdmi_d_18x18 };
	private int[] sourceImageRes_hl = { R.drawable.icon_73_watch_tv_hl_18x18
			, R.drawable.icon_73_watch_tv_hl_18x18
			, R.drawable.icon_168_browse_pc_hl_18x18
			, R.drawable.icon_193_usb_hl_18x18
			, R.drawable.icon_211_video_input_hl_18x18
			, R.drawable.icon_203_hdmi_hl_18x18
			, R.drawable.icon_203_hdmi_hl_18x18
			, R.drawable.icon_203_hdmi_hl_18x18 };
	public InputSourceAdapter(Context context, String[] data) {
		mData = data;
		con = context;
	}

	@Override
	public int getCount() {
		return mData.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int index, View view, ViewGroup parent) {
		viewHolder holder;
		if (view == null) {
			view = LayoutInflater.from(con).inflate(
					R.layout.input_source_list_view_item, null);
			holder = new viewHolder();
			holder.img_circle = (LinearLayout) view
					.findViewById(R.id.input_source_img_circle);
			holder.inputSourceName = (TextView) view
					.findViewById(R.id.input_source_data);
			holder.inputSourceImage = (TextView) view
					.findViewById(R.id.input_source_img);
			view.setTag(holder);
		} else {
			holder = (viewHolder) view.getTag();
		}	
		holder.inputSourceName.setText(mData[index]);
		if(focusPosition == index){
			holder.img_circle.setBackgroundResource(R.drawable.topmenubar_app_icon_circle_highlighted);
			holder.inputSourceName.setTextColor(0xFF76D7D6);
			holder.inputSourceImage.setBackgroundResource(sourceImageRes_hl[index]);
		}else{
			if(currentSource == index){
				holder.img_circle.setBackgroundResource(0);
				holder.inputSourceName.setTextColor(0xFFFFFFFF);
				holder.inputSourceImage.setBackgroundResource(sourceImageRes_n[index]);
			}else{
				holder.img_circle.setBackgroundResource(0);
				holder.inputSourceName.setTextColor(0x70FFFFFF);
				holder.inputSourceImage.setBackgroundResource(sourceImageRes_d[index]);
			}

		}

		return view;
	}

	static class viewHolder {
		LinearLayout img_circle;
		TextView inputSourceName;
		TextView inputSourceImage;
	}
	public void setSelectViewPosition(int position) {
		focusPosition = position;
		notifyDataSetChanged();
	}
	public void setCurrentSourcePosition(int position) {
		currentSource = position;
		notifyDataSetChanged();
	}
}

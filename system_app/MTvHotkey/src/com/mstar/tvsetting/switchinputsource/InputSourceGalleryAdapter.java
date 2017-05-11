//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2012 MStar Semiconductor, Inc. All rights reserved.
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

package com.mstar.tvsetting.switchinputsource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;

import com.mstar.tvsetting.R;
import com.mstar.tvsetting.hotkey.InputSourceItem;
import com.mstar.android.tv.TvCommonManager;

public class InputSourceGalleryAdapter extends PagerAdapter {
    private final static String TAG = "InputSourceGalleryAdapter";

    private final static String INPUT_SOURCE_IMAGE_PATH = "/var/tmp/";

    private final int IMG_DECODE_TASK_ID1 = 1;

    private final int IMG_DECODE_TASK_ID2 = 2;

    private final int IMG_DECODE_POSITION_AUTO = -1;

    private Context mContext;

    private ArrayList<InputSourceItem> mData;

    private int mGalleryItemBackground;

    private HashMap<Integer, View> mViewMaps;
    private int mCount;

    private int mPosition = 0;

    private HashMap<String, String> mImagePaths = new HashMap<String, String>();

    private final static int defaultImg = R.drawable.no_signal;

    private final static int unPreviewImg = R.drawable.no_preview;

    private boolean mIsPreviewOn = false;

    private LayoutInflater mInflater;

    private int mGalleryItemWidth;

    private int mGalleryItemHeight;

    private boolean mIsOfflineDetectSupport = false;

    static class viewHolder {
        String inputSourceEnuName;

        TextView inputSourceName;

        ImageView inputSourceImage;

        int position;

        boolean needAsyncDecode;

        int inputSource;
    }

    public InputSourceGalleryAdapter(Context context,
            ArrayList<InputSourceItem> data, boolean isPreviewOn,
            int galleryItemWidth, int galleryItemHeight) {
        mData = data;
        mCount = data.size();
        mContext = context;
        mIsPreviewOn = isPreviewOn;
        mGalleryItemWidth = galleryItemWidth;
        mGalleryItemHeight = galleryItemHeight;
        getInSDPhoto();
        mViewMaps = new HashMap<Integer, View>(data.size());
        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(R.styleable.Gallery1);
        mGalleryItemBackground = a.getResourceId(
                R.styleable.Gallery1_android_galleryItemBackground, 0);
        a.recycle();

        if (TvCommonManager.getInstance().isSupportModule(TvCommonManager.MODULE_OFFLINE_DETECT)) {
            mIsOfflineDetectSupport = true;
        }
    }

    private BitmapWorkerTask mImgDecodeTask1 = null;

    private BitmapWorkerTask mImgDecodeTask2 = null;

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        String filePath;
        int position;
        int taskId;

        public BitmapWorkerTask(int id, int pos, ImageView imageView,
                String path) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            taskId = id;
            position = pos;
            filePath = path;
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            Bitmap bp = null;

            if (filePath != null) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(filePath, options);

                // Calculate inSampleSize
                options.inSampleSize = calculateInSampleSize(options,
                        mGalleryItemWidth, mGalleryItemHeight);

                // Decode bitmap with inSampleSize set
                options.inJustDecodeBounds = false;

                bp = BitmapFactory.decodeFile(filePath, options);
            }

            return bp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (!isCancelled()) {
                final ImageView imageView = imageViewReference.get();

                if (imageViewReference != null && bitmap != null) {
                    if (imageView != null) {
                        imageView.setImageBitmap(bitmap);
                        startNextAsyncDecodeTask(taskId,
                                IMG_DECODE_POSITION_AUTO);
                    }
                } else {
                    setDefaultImageResource(imageView);
                }
            }
        }

        private int calculateInSampleSize(BitmapFactory.Options options,
                int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2
                // and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        viewHolder holder;
        InputSourceItem inputSourceItem = mData.get(position);
        View sourceGalleryItemView = null;
        ImageView imageView = null;
        TextView textView = null;
        LinearLayout ll;
        String inputSourceEnuName;
        String imageFilePath;

        holder = new viewHolder();
        sourceGalleryItemView = mInflater.inflate(R.layout.gallery_item, null);
        sourceGalleryItemView
                .setOnClickListener((View.OnClickListener) mContext);
        ll = (LinearLayout) sourceGalleryItemView
                .findViewById(R.id.linear_image);
        imageView = (ImageView) ll.findViewById(R.id.item_gallery_image);
        imageView.setBackgroundResource(mGalleryItemBackground);
        inputSourceEnuName = inputSourceItem.getInputSourceName();
        imageFilePath = getInputSourceImagePath(inputSourceItem.getPositon());
        holder.needAsyncDecode = false;
        holder.inputSourceEnuName = inputSourceEnuName;
        holder.inputSource = inputSourceItem.getPositon();
        setDefaultImageResource(imageView);
        if (mIsPreviewOn == true) {
            if (imageFilePath != null) {
                if ((mImgDecodeTask1 == null)
                        || ((mImgDecodeTask1.getStatus() == AsyncTask.Status.FINISHED) || (mImgDecodeTask1
                                .isCancelled() == true))) {
                    mImgDecodeTask1 = new BitmapWorkerTask(IMG_DECODE_TASK_ID1,
                            position, imageView, imageFilePath);
                    mImgDecodeTask1.execute(0);
                } else {
                    if ((mImgDecodeTask2 == null)
                            || ((mImgDecodeTask2.getStatus() == AsyncTask.Status.FINISHED) || (mImgDecodeTask2
                                    .isCancelled() == true))) {
                        mImgDecodeTask2 = new BitmapWorkerTask(
                                IMG_DECODE_TASK_ID2, position, imageView,
                                imageFilePath);
                        mImgDecodeTask2.execute(0);
                    } else {
                        holder.needAsyncDecode = true;
                    }
                }

            }
        }
        holder.inputSourceImage = imageView;
        textView = (TextView) sourceGalleryItemView
                .findViewById(R.id.item_gallery_text);
        String inputSourceName = inputSourceItem.getInputSourceName();
        if (inputSourceName.equals("DTV2")) {
            if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ISDB) {
                inputSourceName = "AIR";
            } else if (TvCommonManager.getInstance().getCurrentTvSystem() == TvCommonManager.TV_SYSTEM_ATSC) {
                inputSourceName = "TV";
            } else {
                inputSourceName = "DTV";
            }
        }
        textView.setText(inputSourceName);
        holder.inputSourceName = textView;
        mViewMaps.put(position, sourceGalleryItemView);
        holder.position = position;
        sourceGalleryItemView.setTag(holder);

        updateGalleryItemInfo(sourceGalleryItemView, position);

        container.addView(sourceGalleryItemView);
        return sourceGalleryItemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (View) object);
    }

    @Override
    public void notifyDataSetChanged() {
        for (int i = 0; i < mViewMaps.size(); i++) {
            View sourceGalleryItemView = mViewMaps.get(i);
            updateGalleryItemInfo(sourceGalleryItemView, i);
        }
        super.notifyDataSetChanged();
    }

    private void setDefaultImageResource(ImageView imageView) {
        if (mIsPreviewOn == false) {
            imageView.setImageResource(unPreviewImg);
        } else {
            imageView.setImageResource(defaultImg);
        }
    }

    private void startNextAsyncDecodeTask(int taskId, int postion) {
        LinearLayout ll;
        ImageView imageView;
        viewHolder holder;
        View view;
        String imageFilePath;

        for (int i = 0; i < mViewMaps.size(); i++) {
            if (postion >= 0) {
                if (postion != i) {
                    continue;
                }
            }
            view = mViewMaps.get(i);
            holder = (viewHolder) view.getTag();
            if (holder.needAsyncDecode == true) {
                ll = (LinearLayout) view.findViewById(R.id.linear_image);
                imageView = (ImageView) ll
                        .findViewById(R.id.item_gallery_image);
                imageFilePath = getInputSourceImagePath(holder.inputSource);
                if (imageFilePath == null) {
                    setDefaultImageResource(imageView);
                    continue;
                }
                if (taskId == IMG_DECODE_TASK_ID1) {
                    holder.needAsyncDecode = false;
                    mImgDecodeTask1 = new BitmapWorkerTask(IMG_DECODE_TASK_ID1,
                            i, imageView, imageFilePath);
                    mImgDecodeTask1.execute(0);
                } else {
                    holder.needAsyncDecode = false;
                    mImgDecodeTask2 = new BitmapWorkerTask(IMG_DECODE_TASK_ID2,
                            i, imageView, imageFilePath);
                    mImgDecodeTask2.execute(0);
                }
                break;
            }
        }
    }

    public int getGalleryItemPositionFromView(View v) {
        viewHolder holder = (viewHolder) v.getTag();
        return holder.position;
    }

    private void updateGalleryItemInfo(View sourceGalleryItemView, int position) {
        LinearLayout ll;
        ImageView imageView = null;
        TextView textView = null;
        InputSourceItem inputSourceItem = mData.get(position);
        int imgResId;

        ll = (LinearLayout) sourceGalleryItemView
                .findViewById(R.id.linear_image);
        imageView = (ImageView) ll.findViewById(R.id.item_gallery_image);
        textView = (TextView) sourceGalleryItemView
                .findViewById(R.id.item_gallery_text);
        if (mPosition == position) {
            /* set display parameters for selected item */
            imgResId = inputSourceItem.getSelectedResId();
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    mGalleryItemWidth, mGalleryItemHeight));
            imageView.setImageAlpha(255);
            imageView.getBackground().setAlpha(255);
            textView.setTextColor(Color.WHITE);
        } else {
            imgResId = inputSourceItem.getUnselectedResId();
            imageView
                    .setLayoutParams(new LinearLayout.LayoutParams(
                            mGalleryItemWidth * 86 / 100,
                            mGalleryItemHeight * 86 / 100));
            if (mIsOfflineDetectSupport == true) {
                if (inputSourceItem.isSignalFlag()) {
                    /* set display parameters for has-signal item */
                    imageView.setImageAlpha(230);
                    imageView.getBackground().setAlpha(230);
                    textView.setTextColor(Color.WHITE);
                } else {
                    /* set display parameters for no-signal item */
                    imageView.setImageAlpha(150);
                    imageView.getBackground().setAlpha(150);
                    textView.setTextColor(Color.BLACK);
                }
            } else {
                imageView.setImageAlpha(170);
                imageView.getBackground().setAlpha(170);
                textView.setTextColor(Color.BLACK);
            }
        }
        if (mIsPreviewOn == false) {
            imageView.setImageResource(imgResId);
        }
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    /**
     * get image from system path
     *
     * @return
     */
    public void getInSDPhoto() {
        File f = new File(INPUT_SOURCE_IMAGE_PATH);
        File[] files = f.listFiles();

        mImagePaths.clear();
        if (mIsPreviewOn == true) {
            if (f.exists()) {
                for (File file : files) {
                    if (isPreviewThumbnail(file.getPath())) {
                        String fileName;
                            fileName = file.getName()
                                .substring(0, file.getName().lastIndexOf('.'));
                        mImagePaths.put(fileName, file.getPath());
                    }
                }
            }
        }
    }

    private boolean isPreviewThumbnail(String fileFullPathName) {
        boolean ret = false;

        if (fileFullPathName.lastIndexOf(".") >= 0) {

            String fileExt = fileFullPathName.substring(
                fileFullPathName.lastIndexOf(".") + 1,
                fileFullPathName.length()).toLowerCase();

            if (fileExt.equalsIgnoreCase("jpg")
                || fileExt.equalsIgnoreCase("gif")
                || fileExt.equalsIgnoreCase("png")
                || fileExt.equalsIgnoreCase("jpeg")
                || fileExt.equalsIgnoreCase("bmp")) {
                ret = true;
            }
        }

        return ret;
    }

    private String getInputSourceImagePath(int pos) {
        String imagePath = null;
        try {
            if (mImagePaths.containsKey(""+pos)) {
                imagePath = mImagePaths.get(""+pos);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imagePath;
    }

    public void refreshInputSurceImage(boolean isPreviewOn) {
        LinearLayout ll;
        ImageView imageView;
        viewHolder holder;
        View view;

        getInSDPhoto();

        if (isPreviewOn == true) {
            if (mImgDecodeTask1 != null) {
                mImgDecodeTask1.cancel(true);
            }
            if (mImgDecodeTask2 != null) {
                mImgDecodeTask2.cancel(true);
            }

            for (int i = 0; i < mViewMaps.size(); i++) {
                view = mViewMaps.get(i);
                holder = (viewHolder) view.getTag();
                holder.needAsyncDecode = true;
            }

            startNextAsyncDecodeTask(IMG_DECODE_TASK_ID1, mPosition);
            startNextAsyncDecodeTask(IMG_DECODE_TASK_ID2,
                    IMG_DECODE_POSITION_AUTO);
        }
    }
}

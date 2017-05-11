package com.ktc.panasonichome.view.api;

import android.text.TextUtils;

import com.ktc.panasonichome.R;

public class SkyLogo {
    private static SkyLogo mLogo;
    private boolean isOversea;
    private String pe;

    public static SkyLogo getInstence() {
        if (mLogo == null) {
            mLogo = new SkyLogo();
        }
        return mLogo;
    }

    public SkyLogo() {
        this.pe = null;
        this.isOversea = false;
        //TODO SkySystemProperties
//        this.pe = SkySystemProperties.getProperty("ro.build.PE");
//        this.isOversea = SkySystemUtil.isOverseasProduct();
    }

    public int getLogoRes(boolean isLogo) {
        if (!TextUtils.isEmpty(this.pe)) {
            return isLogo ? R.drawable.coocaa_logo_pe : R.drawable.coocaa_logo_bg_pe;
        } else {
            if (this.isOversea) {
                return isLogo ? R.drawable.coocaa_logo_pe : R.drawable.coocaa_logo_bg_pe;
            } else {
                if (isLogo) {
                    return R.drawable.coocaa_logo;
                }
                return R.drawable.coocaa_logo_bg;
            }
        }
    }
}

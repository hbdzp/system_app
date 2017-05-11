package com.ktc.panasonichome.view;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.ktc.panasonichome.LocalAppGridActivity;
import com.ktc.panasonichome.R;
import com.ktc.panasonichome.SimpleHomeApplication;
import com.ktc.panasonichome.model.AppInfo;
import com.ktc.panasonichome.utils.LogUtils;
import com.ktc.panasonichome.utils.ScreenParams;
import com.ktc.panasonichome.view.MarqueeText.IMarqueeText;
import com.ktc.panasonichome.view.MarqueeText.PosType;
import com.ktc.panasonichome.view.api.SkyToastView.ShowTime;

import java.util.List;


public class AppIconItem extends FrameLayout implements IMarqueeText {
    public enum AppType {
        ADDICON,
        MEDIA,
        MYAPP,
        SETTING,
        SOURCE,
        USER
    }

    public  ImageView   Icon;
    private AppType     appType;
    public  FrameLayout contentLayout;
    private AppInfo     curAppInfo;
    private boolean deleAnimEnd = true;
    private MyFocusFrame focusView;
    private IAppIconItem listener;
    private Context      mContext;
    private FrameLayout  menuLayout;
    public  MarqueeText  menuOne;
    public  MarqueeText  menuTwo;
    OnClickListener onClickListener = new MyOnClickListener();
    OnKeyListener   onKeyListener   = new MyOnKeyListener();
    private MyFocusFrame smallFocusView;
    public  TextView     txt;

    public interface IAppIconItem {
        void deleteItem(View view);

        void onUpKeyDown(AppIconItem appIconItem);

        void savePos();

        void stickItem();

        void toastNotEdit(String str, ShowTime showTime);

        void toastStick(String str, ShowTime showTime);
    }

    class MyOnClickListener implements OnClickListener {

        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.menuOneId:
                case 111:
                    GroupAppLayout.fuckView.requestFocus();
                    AppIconItem.this.listener.stickItem();
                    AppIconItem.this.onItemChange();
                    AppIconItem.this.listener.toastStick(AppIconItem.this.getResources()
                            .getString(R.string.toast_stick), ShowTime.SHOTTIME);
                    return;
//                case R.id.menuTwoId:
                case 222:
                    GroupAppLayout.fuckView.requestFocus();
                    AppIconItem.this.onItemChange();
                    AppIconItem.this.deleAnimEnd = false;
                    AppIconItem.this.listener.deleteItem(AppIconItem.this);
                    return;
//                case R.id.appIconItemId:
                case 333:
                    AppIconItem.this.startApp();
                    return;
                default:
                    return;
            }
        }
    }

    class MyOnKeyListener implements OnKeyListener {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                LogUtils.d("dzp", "AppIconItem onKey:" + event.getKeyCode());
                if (AppIconItem.this.deleAnimEnd) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_UP:
                            AppIconItem.this.listener.onUpKeyDown(AppIconItem.this);
                            return true;
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            AppIconItem.this.focusView.setImgResourse(R.drawable.home_focus);
                            return false;
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            if (AppType.SOURCE == AppIconItem.this.appType) {
                                return true;
                            }else {
                                return  false;
                            }
//                            break;
                        case KeyEvent.KEYCODE_DPAD_RIGHT:
                            if (AppType.ADDICON == AppIconItem.this.appType) {
                                return true;
                            }else {
                                return  false;
                            }
//                            break;
                        case KeyEvent.KEYCODE_MENU:
                            AppIconItem.this.popMenu();
                            return true;
                        case KeyEvent.KEYCODE_ENTER:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                            AppIconItem.this.callOnClick();
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public AppIconItem(Context context, MyFocusFrame focusView, MyFocusFrame smallFocusView) {
        super(context);
        this.mContext = context;
        this.smallFocusView = smallFocusView;
        this.focusView = focusView;
        initDefaultView();
        initView();
    }

    public void initDefaultView() {
        setLayoutParams(new LayoutParams(ScreenParams.getInstence(this.mContext)
                .getResolutionValue(194), ScreenParams.getInstence(this.mContext)
                .getResolutionValue(194)));
        this.contentLayout = new FrameLayout(this.mContext);
        LayoutParams contentViewLp = new LayoutParams(ScreenParams.getInstence(this.mContext)
                .getResolutionValue(170), ScreenParams.getInstence(this.mContext)
                .getResolutionValue(170));
        contentViewLp.gravity = Gravity.CENTER;
        addView(this.contentLayout, contentViewLp);
        this.txt = new TextView(this.mContext);
        LayoutParams paramName = new LayoutParams(ScreenParams.getInstence(this.mContext)
                .getResolutionValue(124), LayoutParams.WRAP_CONTENT);
        paramName.gravity = Gravity.CENTER_HORIZONTAL;
        paramName.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(120);
        this.txt.setTextColor(Color.WHITE);
        this.txt.setSingleLine(true);
        this.txt.setEllipsize(TruncateAt.MARQUEE);
        this.txt.setMarqueeRepeatLimit(-1);
        this.txt.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(28));
        this.txt.setGravity(Gravity.CENTER);
        this.contentLayout.addView(this.txt, paramName);
        this.Icon = new ImageView(this.mContext);
        this.Icon.setScaleType(ScaleType.CENTER_CROP);
        LayoutParams imageLp = new LayoutParams(ScreenParams.getInstence(this.mContext)
                .getResolutionValue(85), ScreenParams.getInstence(this.mContext)
                .getResolutionValue(85));
        imageLp.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(25);
        imageLp.gravity = Gravity.CENTER_HORIZONTAL;
        this.contentLayout.addView(this.Icon, imageLp);
    }

    @SuppressWarnings("ResourceType")
    private void initView() {
        this.menuLayout = new FrameLayout(this.mContext);
        this.menuLayout.setVisibility(View.INVISIBLE);
        this.menuLayout.setBackgroundResource(R.drawable.appbar_shade_bg);
        LayoutParams menuLayoutLp = new LayoutParams(ScreenParams.getInstence(this.mContext)
                .getResolutionValue(182), LayoutParams.MATCH_PARENT);
        menuLayoutLp.gravity = Gravity.CENTER;
        addView(this.menuLayout, menuLayoutLp);
        this.menuOne = new MarqueeText(this.mContext);
        this.menuOne.setId(111);
        this.menuOne.setIMarqueeText(this);
        this.menuOne.setPosType(PosType.TOP);
        this.menuOne.setOnClickListener(this.onClickListener);
        this.menuOne.setImg(R.drawable.appbar_stick_focus);
        this.menuOne.setTxtColor(Color.BLACK);
        this.menuOne.setTxt(this.mContext.getResources().getString(R.string.menu_txt_one));
        this.menuTwo = new MarqueeText(this.mContext);
        this.menuTwo.setId(222);
        this.menuTwo.setIMarqueeText(this);
        this.menuTwo.setPosType(PosType.BOTTOM);
        this.menuTwo.setOnClickListener(this.onClickListener);
        this.menuTwo.setImg(R.drawable.appbar_remove_unfocus);
        this.menuTwo.setTxtColor(Color.WHITE);
        this.menuTwo.setTxt(this.mContext.getResources().getString(R.string.menu_txt_two));
        LayoutParams menuOneLp = new LayoutParams(LayoutParams.MATCH_PARENT, ScreenParams
                .getInstence(this
                .mContext).getResolutionValue(50));
        menuOneLp.topMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(40);
        LayoutParams menuTwoLp = new LayoutParams(LayoutParams.MATCH_PARENT, ScreenParams
                .getInstence(this.mContext).getResolutionValue(50));
        menuTwoLp.gravity = Gravity.BOTTOM;
        menuTwoLp.bottomMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(40);
        this.menuLayout.addView(this.menuOne, menuOneLp);
        this.menuLayout.addView(this.menuTwo, menuTwoLp);
        setOnKeyListener(this.onKeyListener);
        setOnClickListener(this.onClickListener);
        setId(333);
    }

    public void setIAppIconItem(IAppIconItem lis) {
        this.listener = lis;
    }

    public void setAppType(AppType appType) {
        this.appType = appType;
    }

    public AppType getAppType() {
        return this.appType;
    }

    public void updataView(AppInfo appInfo) {
        if (appInfo != null) {
            this.curAppInfo = appInfo;
            this.Icon.setImageDrawable(appInfo.appIcon);
            if (!TextUtils.isEmpty(appInfo.appName)) {
                this.txt.setText(appInfo.appName);
            }
        }
    }

    public AppInfo getAppInfo() {
        return this.curAppInfo;
    }

    public void popMenu() {
        if (this.appType == AppType.USER) {
            this.listener.savePos();
            this.menuLayout.setVisibility(View.VISIBLE);
            this.menuOne.requestFocus();
            this.focusView.setVisibility(View.INVISIBLE);
            this.txt.setVisibility(View.INVISIBLE);
        } else if (this.appType != AppType.ADDICON) {
            this.listener.toastNotEdit(getResources().getString(R.string.toast_not_edit),
                    ShowTime.SHOTTIME);
        }
    }

    public void dismissMenu() {
        requestFocus();
        this.focusView.setVisibility(View.VISIBLE);
        this.smallFocusView.setVisibility(View.INVISIBLE);
        this.menuLayout.setVisibility(View.INVISIBLE);
        this.txt.setVisibility(View.VISIBLE);
    }

    public void onItemChange() {
        this.smallFocusView.setVisibility(View.INVISIBLE);
        this.menuLayout.setVisibility(View.INVISIBLE);
        this.txt.setVisibility(View.VISIBLE);
    }

    public void startApp() {
        switch (appType) {
            case ADDICON:
                this.listener.savePos();
                Intent addIntent = new Intent(this.mContext, LocalAppGridActivity.class);
                addIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SimpleHomeApplication.getInstance().isStartLocalAppGrid = true;
                this.mContext.startActivity(addIntent);
//                //TODO
//                Toast.makeText(mContext,"功能待修改",Toast.LENGTH_SHORT).show();
                return;
            case MEDIA:
                Intent intentMEDIA = new Intent();
                ComponentName componentNameMEDIA = new ComponentName("com.jrm.localmm","com.jrm.localmm.HomeActivity");
                intentMEDIA.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intentMEDIA.setComponent(componentNameMEDIA);
                if (isIntentAvailable(mContext, intentMEDIA)) {
                    mContext.startActivity(intentMEDIA);
                } else {
                    Toast.makeText(mContext,getResources().getString(R.string
                            .toast_app_not_installed),Toast.LENGTH_SHORT).show();
                }
                return;
            case MYAPP:
                Intent intentMYAPP = new Intent();
                ComponentName componentNameMYAPP = new ComponentName("com.ktc.panasonichome","com.ktc.panasonichome.AllAppListActivity");
                intentMYAPP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intentMYAPP.setComponent(componentNameMYAPP);
                if (isIntentAvailable(mContext, intentMYAPP)) {
                    mContext.startActivity(intentMYAPP);
                } else {
                    Toast.makeText(mContext,getResources().getString(R.string
                            .toast_app_not_installed),Toast.LENGTH_SHORT).show();
                }
                return;
            case SETTING:
                Intent intentSETTING = new Intent();
                ComponentName componentNameSETTING1 = new ComponentName("com.android.tv.settings","com.android.tv.settings.MainActivity");
		ComponentName componentNameSETTING2 = new ComponentName("com.android.tv.settings","com.android.tv.settings.MainSettings");
                intentSETTING.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intentSETTING.setComponent(componentNameSETTING1);
                if (!isIntentAvailable(mContext, intentSETTING)) {
		    intentSETTING.setComponent(componentNameSETTING2);
                }
		try {
		    mContext.startActivity(intentSETTING);
		} catch (ActivityNotFoundException e) {
		    Toast.makeText(mContext,getResources().getString(R.string
                            .toast_app_not_installed),Toast.LENGTH_SHORT).show();
		} 
                return;
            case SOURCE:
                Intent intentSOURCE = new Intent();
                ComponentName componentNameSOURCE = new ComponentName("com.mstar.tvsetting","com.mstar.tvsetting.hotkey.InputSourceListViewActivity");
                intentSOURCE.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                        .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                intentSOURCE.setComponent(componentNameSOURCE);
                if (isIntentAvailable(mContext, intentSOURCE)) {
                    mContext.startActivity(intentSOURCE);
                } else {
                    Toast.makeText(mContext,getResources().getString(R.string
                            .toast_app_not_installed),Toast.LENGTH_SHORT).show();
                }
                return;
            case USER:
                //TODO  createBuilde
                //                try {
                //                    StartComponentCustomActionBuilder.createBuilder()
                // .setLaunchComponent(((MyAppData) JObject.parseJObject(this.curAppInfo
                // .appDataString, MyAppData.class)).component).build().start(this.mContext);
                //                    return;
                //                } catch (Exception e) {
                //                    e.printStackTrace();
                //                    return;
                //                }
                launcherAppAccordingToPackageName(mContext,getAppInfo().getPackageName());
                        return;
            default:
                //TODO 正式版移除
                Toast.makeText(mContext,getResources().getString(R.string
                        .toast_app_not_installed),Toast.LENGTH_SHORT).show();
                return;
        }
    }

    public void reqdismissMenu() {

        dismissMenu();
    }

    public void onfocusChange(View v, boolean hasFocus) {
        MarqueeText marqueeText = (MarqueeText) v;
        if (hasFocus) {
            marqueeText.setTxtColor(Color.BLACK);
            this.smallFocusView.changeFocusPos(v);
            marqueeText.setBackgroundColor(Color.WHITE);
            switch (marqueeText.getId()) {
                case 111:
                    marqueeText.setImg(R.drawable.appbar_stick_focus);
                    break;
                case 222:
                    marqueeText.setImg(R.drawable.appbar_remove_focus);
                    break;
            }
            this.smallFocusView.setVisibility(View.VISIBLE);
            return;
        }
        marqueeText.setBackgroundColor(Color.TRANSPARENT);
        marqueeText.setTxtColor(Color.WHITE);
        switch (marqueeText.getId()) {
            case 111:
                marqueeText.setImg(R.drawable.appbar_stick_unfocus);
                return;
            case 222:
                marqueeText.setImg(R.drawable.appbar_remove_unfocus);
                return;
            default:
                return;
        }
    }

    public void endAnim() {

        this.deleAnimEnd = true;
    }

    public static void launcherAppAccordingToPackageName(Context context, String packageName) {
        Intent mIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (mIntent != null) {
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                context.startActivity(mIntent);
            } catch (ActivityNotFoundException anf) {
                Toast.makeText(context, context.getResources().getString(R.string
                        .toast_app_not_installed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_ACTIVITIES);
        return list.size() > 0;
    }
}

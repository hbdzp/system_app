package com.ktc.panasonichome.view;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ktc.panasonichome.LauncherActivity;
import com.ktc.panasonichome.utils.ScreenParams;

public class MarqueeText extends FrameLayout implements OnKeyListener, OnFocusChangeListener {
    private LinearLayout contentView;
    private int          id;
    private ImageView    image;
    private IMarqueeText listener;
    private Context      mContext;
    private PosType      posType;
    private TextView     txt;

    public interface IMarqueeText {
        void onfocusChange(View view, boolean z);

        void reqdismissMenu();
    }

    public enum PosType {
        TOP,
        BOTTOM
    }

    public MarqueeText(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    private void initView() {
        this.image = new ImageView(this.mContext);
        this.txt = new TextView(this.mContext);
        this.txt.setSingleLine();
        this.txt.setEllipsize(TruncateAt.MARQUEE);
        this.txt.setGravity(3);
        this.txt.setMarqueeRepeatLimit(-1);
        this.txt.setTextSize((float) ScreenParams.getInstence(this.mContext).getTextDpiValue(28));
        LayoutParams imgLp = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        imgLp.gravity = 16;
        LayoutParams txtLp = new LayoutParams(ScreenParams.getInstence(this.mContext)
                .getResolutionValue(100), LayoutParams.WRAP_CONTENT);
        txtLp.leftMargin = ScreenParams.getInstence(this.mContext).getResolutionValue(10);
        txtLp.gravity = 16;
        this.contentView = new LinearLayout(this.mContext);
        this.contentView.setOrientation(LinearLayout.HORIZONTAL);
        this.contentView.addView(this.image, imgLp);
        this.contentView.addView(this.txt, txtLp);
        LayoutParams contentParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        contentParams.gravity = 17;
        addView(this.contentView, contentParams);
        setFocusable(true);
        setOnKeyListener(this);
        setOnFocusChangeListener(this);
    }

    public void setIMarqueeText(IMarqueeText listener) {
        this.listener = listener;
    }

    public void setTxt(String txt) {
        this.txt.setText(txt);
    }

    public void setTxtColor(int color) {
        this.txt.setTextColor(color);
    }

    public void setImg(int i) {
        this.image.setBackgroundResource(i);
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setPosType(PosType positionType) {
        this.posType = positionType;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        this.listener.onfocusChange(v, hasFocus);
        LauncherActivity.lastFocusView = v;
        if (hasFocus) {
            this.txt.setSelected(true);
        } else {
            this.txt.setSelected(false);
        }
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    this.listener.reqdismissMenu();
                    return true;
                case KeyEvent.KEYCODE_DPAD_UP:
                    if (PosType.TOP == this.posType) {
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    if (PosType.BOTTOM == this.posType) {
                        return true;
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT:
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    this.listener.reqdismissMenu();
                    break;
            }
        }
        return false;
    }
}

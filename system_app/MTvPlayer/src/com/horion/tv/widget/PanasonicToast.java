package com.horion.tv.widget;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.horion.tv.R;

import static android.security.KeyStore.getApplicationContext;

/**
 * Created by xiacf on 2017/1/12.
 */

public class PanasonicToast {
    private static Toast toast;
    public static Toast makeText(Context context, String text){
        toast = new Toast(context);
        LayoutInflater inflater =  LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.panasnic_toast,
                null);
        Log.d("Maxs","PanasonicToast:text = " + text);
        TextView textView = (TextView) layout.findViewById(R.id.tvTextToast);
        textView.setText(text);
        ///toast.setGravity(Gravity.RIGHT | Gravity.TOP, 12, 40);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        return toast;
    }

   public void show(){
       if (toast != null)
            toast.show();
   }
}

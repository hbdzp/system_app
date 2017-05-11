package com.mstar.tvsetting.switchinputsource;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mstar.tvsetting.R;


/**
 * Created by xiacf on 2017/1/12.
 */

public class PanasonicToast {
    private static Toast toast;
    public static Toast makeText(Context context, int text){
        toast = new Toast(context);
        LayoutInflater inflater =  LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.panasnic_toast,
                null);
        Log.d("Maxs","PanasonicToast:text = " + context.getString(text));
        TextView textView = (TextView) layout.findViewById(R.id.tvTextToast);
        textView.setText(context.getString(text));
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

package rottenstudentertainment.hyperfitness.AndroidUtils;

import android.content.Context;
import android.widget.Toast;

public class makeToast {

    public static void makeToast(String text, Context context){
        int duration = Toast.LENGTH_SHORT;
        makeToast( text, context, duration);
    }


    public static void makeToast(String text, Context context, int duration){
        Toast toast = Toast.makeText( context, text, duration);
        toast.show();
    }
}

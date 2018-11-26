package rottenstudentertainment.hyperfitness.AndroidUtils;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import rottenstudentertainment.hyperfitness.OpenGLES20Activity;

public class ViewUtils {

    public static View findViewByString( Context context, String id){
        try{
            OpenGLES20Activity activity = (OpenGLES20Activity) context;
            int resID = activity.getResources().getIdentifier(id, "id", activity.getPackageName());
            View view =  activity.findViewById( resID);
            return view;

        } catch( Exception e){
        }
        return null;
    }
}

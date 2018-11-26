package rottenstudentertainment.hyperfitness.util;

import android.content.Context;
import android.widget.Button;

import rottenstudentertainment.hyperfitness.AndroidUtils.ContextUtils;
import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.test.Page_number;
import rottenstudentertainment.hyperfitness.workout.Workout;

public class ButtonFunctions {

    public static final String GO = "go";

    public static void doFunction(Context context, Button button, String function){
        if( function.equals(GO)){
            go( context, button);
        }
    }


    public static void go( Context context, Button button){
        //change button color + wait half a second
        Page_number.next_page();
    }
}

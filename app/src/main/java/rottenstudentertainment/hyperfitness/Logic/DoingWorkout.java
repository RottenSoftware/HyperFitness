package rottenstudentertainment.hyperfitness.Logic;
/**
 * klasse zum verwalten der android ui prozesse
 */

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;

import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;

public class DoingWorkout {
    public static void initDoingWorkout(OpenGLES20Activity activity){
        View skipButton =  activity.findViewById( R.id.skipButton);
        skipButton.bringToFront();
        activity.findViewById(android.R.id.content).requestLayout();

    }

}

package rottenstudentertainment.hyperfitness.Logic;
/**
 * klasse zum verwalten der android ui prozesse
 */

import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import rottenstudentertainment.hyperfitness.Fitness.Page;
import rottenstudentertainment.hyperfitness.MyGLSurfaceView;
import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.Runnables.SetExerciseTitleRunnable;

public class DoingWorkout {
    public static void initDoingWorkout(OpenGLES20Activity activity, GLSurfaceView mGLView){

        //seperate Fkt!!
        //mGLView = new MyGLSurfaceView(this);
        ViewGroup container = (ViewGroup) activity.findViewById( R.id.container);
        container.addView( mGLView);
        View skipButton =  activity.findViewById( R.id.skipButton);
        skipButton.bringToFront();
        View titleText =  activity.findViewById( R.id.titleText);
        titleText.bringToFront();
    }

    public static void setTitle( OpenGLES20Activity activity, Page curPage){
        Runnable runnable = new SetExerciseTitleRunnable(activity, curPage);
        activity.runOnUiThread( runnable);

    }

    public static void setUpExerciseStartQueque(){
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
               long curTime = System.currentTimeMillis();
               long endTime = curTime;
               while( endTime - curTime < 3000){ // three seconds
                   endTime = System.currentTimeMillis();
                   try {
                       Thread.sleep(300);
                   } catch(Exception e) {
                   }
                }
                rottenstudentertainment.hyperfitness.workout.Page.paused = false;
            }
        });
        thread.start();
    }


}

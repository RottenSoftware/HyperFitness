package rottenstudentertainment.hyperfitness.Logic;
/**
 * klasse zum verwalten der android ui prozesse
 */

import android.opengl.GLSurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import rottenstudentertainment.hyperfitness.Fitness.Page;
import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.Runnables.SetExerciseTitleRunnable;
import rottenstudentertainment.hyperfitness.globals.Globals;
import rottenstudentertainment.hyperfitness.globals.Logger;
import rottenstudentertainment.hyperfitness.workout.PageDisplayer;

public class DoingWorkout {
    public static void initDoingWorkout(OpenGLES20Activity activity, GLSurfaceView mGLView){

        ViewGroup container = (ViewGroup) activity.findViewById( R.id.container);
        container.addView( mGLView);

        View skipButton =  activity.findViewById( R.id.skipButton);
        skipButton.bringToFront();
        View titleText =  activity.findViewById( R.id.titleText);
        titleText.bringToFront();

        View prepareText = activity.findViewById( R.id.prepareText);
        prepareText.bringToFront();
    }

    public static void setTitle( OpenGLES20Activity activity, Page curPage){
        Runnable runnable = new SetExerciseTitleRunnable(activity, curPage);
        activity.runOnUiThread( runnable);
    }

    public static void setUpExerciseStartQueque( OpenGLES20Activity context){
        final OpenGLES20Activity activity = context;
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
               final TextView prepareText = (TextView) activity.findViewById( R.id.prepareText);
               hideView( activity, prepareText, false);
               String basetext = activity.getResources().getString(R.string.prepare_text);
               long curTime = System.currentTimeMillis();
               long endTime = curTime;
               while( endTime - curTime < Globals.timeBetweenExecises){ // three seconds
                   endTime = System.currentTimeMillis();
                   long remainingTime = Globals.timeBetweenExecises - (endTime - curTime); //cendtime- curtime zählt hoch
                   setPreparetext( activity, prepareText, basetext, remainingTime);
                   try {
                       Thread.sleep(30);
                   } catch(Exception e) {
                   }
               }
               setText( activity, prepareText, activity.getResources().getString((int) R.string.start_text));
               try{
                   Thread.sleep(1000);
               } catch(Exception e){

               }
               setText( activity, prepareText, "");
               hideView( activity, prepareText, true);
               PageDisplayer.paused = false;
            }
        });
        thread.start();
    }

    public static void hideView( OpenGLES20Activity activity, final View view, final boolean state){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(state)view.setVisibility( View.GONE);
                else view.setVisibility(View.VISIBLE);
            }
        });

    }

    public static void setText( OpenGLES20Activity activity, final TextView textView, final String text){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText( text);
            }
        });
    }

    private static void setPreparetext(final OpenGLES20Activity activity, final TextView prepareText, String basetext, long remainingTime){
        final int remainingSeconds = (int) remainingTime/1000;
        final String text = basetext + " " + remainingSeconds;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              prepareText.setText( text);
              if( remainingSeconds == 0){
                  prepareText.setVisibility( (int)0);
              }
            }
        });
    }
}

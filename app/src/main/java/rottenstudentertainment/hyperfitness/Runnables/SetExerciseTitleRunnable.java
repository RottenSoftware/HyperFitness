package rottenstudentertainment.hyperfitness.Runnables;

import android.widget.TextView;

import rottenstudentertainment.hyperfitness.Fitness.Page;
import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;

public class SetExerciseTitleRunnable implements Runnable {

    private OpenGLES20Activity activity;
    private Page curPage;

    public SetExerciseTitleRunnable( OpenGLES20Activity activity, Page curPage){
        this.activity = activity;
        this.curPage = curPage;
    }

    @Override
    public void run() {
        TextView titleText =  (TextView) activity.findViewById( R.id.titleText);
        String title = curPage.title.toString();
        if( titleText != null){
            titleText.setText( title);
        }

    }
}

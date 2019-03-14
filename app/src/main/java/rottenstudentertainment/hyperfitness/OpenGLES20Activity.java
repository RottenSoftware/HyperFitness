package rottenstudentertainment.hyperfitness;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;

import rottenstudentertainment.hyperfitness.AdMob.AdMob;
import rottenstudentertainment.hyperfitness.Fitness.WorkoutWrapper;
import rottenstudentertainment.hyperfitness.Logic.DoingWorkout;
import rottenstudentertainment.hyperfitness.Logic.InspectWorkout;
import rottenstudentertainment.hyperfitness.Logic.SearchWorkouts;
import rottenstudentertainment.hyperfitness.globals.AppState;
import rottenstudentertainment.hyperfitness.globals.CurView;
import rottenstudentertainment.hyperfitness.globals.Globals;
import rottenstudentertainment.hyperfitness.globals.Logger;

public class OpenGLES20Activity extends Activity {

    private GLSurfaceView mGLView;

    //for resuming activity
    public static int resume_page;
    public static int resumetime;
    public WorkoutWrapper workoutWrapper;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        workoutWrapper = new WorkoutWrapper();
        super.onCreate( savedInstanceState);
        // final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        if( savedInstanceState != null)
        {
            resume_page = savedInstanceState.getInt("resume_page");
            AppState.set_i(resume_page);
            Logger.LogError( "openglesactivity", "restored state: resume_page: " + resume_page);
        }
        mGLView = new MyGLSurfaceView(this);
        Globals.setDebugView( this); // erste seite überschreiben
        switchView();
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("resume_page", resume_page);
    }


    @Override
    public void finish()
    {
        super.finish();
    }


    @Override
    protected void onPause() {
        super.onPause();
        // The following call pauses the rendering thread.
        // If your OpenGL application is memory intensive,
        // you should consider de-allocating objects that
        // consume significant memory here.
        if(mGLView != null) mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        if(mGLView != null) mGLView.onResume();
    }


    @Override
    public void onBackPressed() {        // menu cruising
        if ( !AppState.backPress(this)){
            super.onBackPressed();       // bye
        }
    }


    public void switchView(){
        //mGLView = null;
        if ( AppState.curView.equals( CurView.SEARCH_WORKOUTS)) {
            setContentView( R.layout.search_workouts);
            SearchWorkouts.initSearchWorkouts( this);
        } else if( AppState.curView.equals( CurView.INSPECT_WORKOUT)){
            setContentView( R.layout.inspect_workout);
            InspectWorkout.initInspectWorkout( this);
        } else if( AppState.curView.equals( CurView.DOING_WORKOUT)){
            setContentView( R.layout.app_container);
            DoingWorkout.initDoingWorkout(this, mGLView);

            AdMob.initializeAdMob( this);
            AdMob.loadAdBanner(this);
        }
    }


    public void optionClicked( View v){
        String filename = SearchWorkouts.getWorkoutFileName( v, this);
        if( filename == null){
            Logger.LogError( "optionClicked", "filename == null");
            return;
        } else{
            workoutWrapper.getWorkoutFromFile( this, filename);
            AppState.curView = CurView.INSPECT_WORKOUT;
            switchView();
        }
    }

    public void buttonClicked( View v){
        if( AppState.curView.equals( CurView.INSPECT_WORKOUT)){
            AppState.curView = CurView.DOING_WORKOUT;
            switchView();
            workoutWrapper.loadModel( this);
        } else if( AppState.curView.equals(CurView.DOING_WORKOUT)){
            AppState.next_page( this);
        }
    }
}

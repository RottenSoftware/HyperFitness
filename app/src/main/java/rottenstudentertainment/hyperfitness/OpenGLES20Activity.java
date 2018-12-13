package rottenstudentertainment.hyperfitness;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import rottenstudentertainment.hyperfitness.AdMob.AdMob;
import rottenstudentertainment.hyperfitness.Logic.SearchWorkouts;
import rottenstudentertainment.hyperfitness.globals.Globals;
import rottenstudentertainment.hyperfitness.globals.Logger;
import rottenstudentertainment.hyperfitness.serialize.Serializetest;
import rottenstudentertainment.hyperfitness.test.Page_number;

public class OpenGLES20Activity extends Activity {

    private GLSurfaceView mGLView;

    //for resuming activity
    public static int resume_page;
    public static int resumetime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // final ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        if(savedInstanceState != null)
        {
            resume_page = savedInstanceState.getInt("resume_page");
            Page_number.set_i(resume_page);
            Logger.LogError( "openglesactivity", "restored state: resume_page: " + resume_page);
        }



        Serializetest.doTest( this);
        //setContentView( R.layout.search_workouts);
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
    public void onBackPressed() {        // to prevent irritating accidental logouts
        if ( Page_number.get_page() > 0 )
        {
            Page_number.set_i(Page_number.get_page() -1 );
            return;
        } else {    // this guy is serious
            // clean up
            super.onBackPressed();       // bye
        }
    }


    public void switchView(){
        mGLView = null;
        if ( Globals.curView == 1) {
            setContentView( R.layout.search_workouts);
            SearchWorkouts.initSearchWorkouts( this);
        } else if(Globals.curView == 2){
            setContentView( R.layout.inspect_workout);
        } else if(Globals.curView == 3){
            setContentView( R.layout.app_container);
            //seperate Fkt!!
            mGLView = new MyGLSurfaceView(this);
            ViewGroup container = (ViewGroup) findViewById( R.id.container);
            container.addView(mGLView);

            AdMob.initializeAdMob( this);
            AdMob.loadAdBanner(this);
        }
    }


}

package rottenstudentertainment.hyperfitness;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import rottenstudentertainment.hyperfitness.test.Page_number;

public class OpenGLES20Activity extends Activity {

    private GLSurfaceView mGLView;
    private Page_number page_number;

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
            page_number.set_i(resume_page);
            Log.e("openglesactivity", "restored state: resume_page: " + resume_page);
        }

        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

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
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // The following call resumes a paused rendering thread.
        // If you de-allocated graphic objects for onPause()
        // this is a good place to re-allocate them.
        mGLView.onResume();
    }


    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        if ( page_number.get_page() > 0 )
        {
            page_number.set_i(Page_number.get_page() -1 );
            return;
        } else {    // this guy is serious
            // clean up
            super.onBackPressed();       // bye
        }
    }


}

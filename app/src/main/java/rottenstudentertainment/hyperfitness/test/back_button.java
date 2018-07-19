package rottenstudentertainment.hyperfitness.test;

import android.app.Activity;

/**
 * Created by Merty on 07.11.2017.
 */

public class back_button extends Activity {

    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        if ( true )
        {

            return;
        } else {    // this guy is serious
            // clean up
            super.onBackPressed();       // bye
        }
    }
}

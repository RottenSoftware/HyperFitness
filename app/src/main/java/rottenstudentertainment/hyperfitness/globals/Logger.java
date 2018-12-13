package rottenstudentertainment.hyperfitness.globals;

import android.util.Log;

public class Logger {

    public static void LogError( String tag, String message){
        if( Globals.useLogs) Log.e(tag, message);
    }
}

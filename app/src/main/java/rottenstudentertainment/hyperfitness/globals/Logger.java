package rottenstudentertainment.hyperfitness.globals;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Logger {

    private static Map<String,Long> times = new HashMap<>();
    public static void LogError( String tag, String message){
        if( Globals.useLogs) Log.e(tag, message);
    }

    public static void log( String tag, String message){
        if( Globals.useLogs) Log.i(tag, message);
    }

    public static void startTimeLog( String tag, String key){
        if( Globals.useLogs){
            long curTime = System.currentTimeMillis();
            times.put(key, curTime);
            String message = "time logged at: " + curTime;
            Log.i(tag, message);
        }
    }

    public static void endTimeLog( String tag, String key){
        if( Globals.useLogs){
            long startTime = times.get( key);
            times.remove(key);
            long curTime = System.currentTimeMillis();
            long duration = curTime-startTime;
            String message = "took " + duration + "ms";
            Log.i(tag, message);
        }
    }
}

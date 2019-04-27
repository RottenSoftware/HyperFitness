package rottenstudentertainment.hyperfitness.globals;

import android.content.Context;

import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;

import static rottenstudentertainment.hyperfitness.globals.Tester.initUsed;
import static rottenstudentertainment.hyperfitness.globals.Tester.initialView;

/**
 * Created by Merty on 02.06.2018.
 */

public class Globals {
    public final static boolean DEVMODE = true;

    public final static boolean useGPUStorage = false;
    public final static boolean useLightning = true;
    public static int timeBetweenExecises  = 3000;
    public static int curView =1;

    public final static boolean useLogs = true;
    public final static boolean useFpsDisplay = true;

    /**
     * To set pages/views for debugging/development
     * @param activity
     */
    public static void setDebugView( OpenGLES20Activity activity){
        if( DEVMODE && !initUsed){
            initUsed = true;
            AppState.curView = Tester.initialView;
            if(  AppState.curView.equals( CurView.INSPECT_WORKOUT)){
                Tester.setInspectWorkoutDummy( activity);
            }
        }
    }
}

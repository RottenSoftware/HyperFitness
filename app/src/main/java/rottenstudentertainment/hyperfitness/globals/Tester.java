package rottenstudentertainment.hyperfitness.globals;

import rottenstudentertainment.hyperfitness.OpenGLES20Activity;

public class Tester {
    public static String initialView = CurView.INSPECT_WORKOUT;
    public static boolean initUsed = false;

public static void setInspectWorkoutDummy( OpenGLES20Activity activity){
    activity.workoutWrapper.getWorkoutFromFile( activity, "Rotten Stud Workout__nerty__190216__rotten,stud.xml");
}

}

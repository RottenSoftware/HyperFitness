package rottenstudentertainment.hyperfitness.globals;

import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.globals.Globals;

/**
 * Created by Merty on 07.11.2017.
 */

public class AppState
{
    private static int pageNumber;
    private static int maxPages;
    public static String curView = CurView.SEARCH_WORKOUTS;


    public static void set_i( int new_i)
    {
        pageNumber = new_i;
    }
    public static int get_page()
    {
        return pageNumber;
    }


    public static void next_page( OpenGLES20Activity activity) {
        if( curView.equals( CurView.DOING_WORKOUT)){
            int maxPages = activity.workoutWrapper.workout.pages.size();
            if( pageNumber < maxPages -1){
               pageNumber++;
            } else{
                //workout zuende
                curView = CurView.INSPECT_WORKOUT;
                pageNumber = 0;
                activity.switchView();
            }
        }
    }

    public static boolean backPress(OpenGLES20Activity activity) {
        if( curView.equals( CurView.DOING_WORKOUT)){
            if( pageNumber == 0){
                curView = CurView.INSPECT_WORKOUT;
                activity.switchView();
            } else{
                pageNumber--;
            }
        } else if( curView.equals( CurView.INSPECT_WORKOUT)){
            curView = CurView.SEARCH_WORKOUTS;
            activity.switchView();
        } else if( curView.equals( CurView.SEARCH_WORKOUTS)){
            return false;
        }
        return true;
    }
}

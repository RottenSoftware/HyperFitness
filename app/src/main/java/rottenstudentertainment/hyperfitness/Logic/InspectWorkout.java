package rottenstudentertainment.hyperfitness.Logic;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rottenstudentertainment.hyperfitness.AndroidUtils.makeToast;
import rottenstudentertainment.hyperfitness.Fitness.Page;
import rottenstudentertainment.hyperfitness.Fitness.Workout;
import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.new_animation.Model;

public class InspectWorkout {

    public static void initInspectWorkout( OpenGLES20Activity activity){
        Workout workout = activity.workoutWrapper.workout;
        setTitle( activity, workout);
        setDescription( activity, workout);
        setExerciseEntries( activity, workout);
        activity.workoutWrapper.loadModel( activity);
    }

    public static void setExerciseEntries(final OpenGLES20Activity activity, Workout workout){
        final int numberOfExercises = workout.pages.size();
        List<Page> exercises = workout.pages;
        int total_duration = 0;
        final List<LinearLayout> uiExerciseEntries = new ArrayList <LinearLayout>();
        for( int i = 0; i < exercises.size(); i++){
            Page exercise = exercises.get(i);
            LinearLayout exerciseUiUnit = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.inspect_workout_exercise_entry, null);
            String exercise_number = (i + 1) + "";
            String title = exercise.title.toString();
            int time = exercise.timer.time;
            total_duration += exercise.timer.time;
            setString( exerciseUiUnit, R.id.exercise_time, getDurationString(activity, time));
            setString( exerciseUiUnit, R.id.exercise_title, title);
            setString( exerciseUiUnit, R.id.exercise_number, exercise_number);
            uiExerciseEntries.add( exerciseUiUnit);
        }
        final String totalDuration = getDurationString( activity, total_duration);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setText( activity, R.id.totalWorkoutTime, totalDuration);
                setText( activity, R.id.number_of_exercises, numberOfExercises + " ");
                LinearLayout container = activity.findViewById(R.id.exercises_container);
                for( int i = 0; i < uiExerciseEntries.size(); i++){
                    LinearLayout uiExerciseEntry = uiExerciseEntries.get(i);
                    container.addView( uiExerciseEntry);
                }
            }
        });

    }

    public static String getDurationString( OpenGLES20Activity activity, int duration){
        int minutes = duration / 60;
        int seconds = duration % 60;
        String total_duration = "";
        if( minutes > 0){
            total_duration += minutes + activity.getString(R.string.time_unit_min);
        }
        if( minutes < 3 && seconds != 0){
            total_duration += " " + seconds + activity.getString(R.string.time_unit_s);
        }
        return total_duration;
    }

    public static void setString( View exerciseUiUnit, int id, String value){
        TextView textView = (TextView) exerciseUiUnit.findViewById( id);
        if( textView != null && value != null) {
            textView.setText(value);
        }
    }

    public static void setText( OpenGLES20Activity activity, int id, String value){
        TextView textView = activity.findViewById( id);
        if( textView != null && value != null){
            textView.setText( value);
        }
    }

    public static void setTitle( OpenGLES20Activity activity, Workout workout){

        Thread thread = new Thread( new SetTitleRunnable( activity, workout));
        thread.start();
    }

    public static void setDescription( OpenGLES20Activity activity, Workout workout){

        Thread thread = new Thread( new SetDescriptionRunnable( activity, workout));
        thread.start();
    }

    public static boolean checkModelLoaded( OpenGLES20Activity activity){
        Model model = activity.workoutWrapper.model;
        if( model.isLoaded()){
            return true;
        } else{
            makeToast.makeToast("model not yet loaded", activity);
            return false;
        }
    }


    static class SetDescriptionRunnable implements Runnable{
        public OpenGLES20Activity activity;
        public Workout workout;
        SetDescriptionRunnable( OpenGLES20Activity activity, Workout workout){
            this.activity = activity;
            this.workout = workout;
        }
        @Override
        public void run() {
            final TextView descriptionText =  (TextView) activity.findViewById( R.id.description_text);
            final String description = workout.description.toString();
            if( descriptionText != null){
                activity.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                descriptionText.setText( description);
                            }
                        }
                );
            }
        }
    }

    static class SetTitleRunnable implements Runnable{
        public OpenGLES20Activity activity;
        public Workout workout;
        SetTitleRunnable( OpenGLES20Activity activity, Workout workout){
            this.activity = activity;
            this.workout = workout;
        }

        @Override
        public void run() {
            final TextView titleText =  (TextView) activity.findViewById( R.id.workout_title);
            final String title = workout.title.toString();
            if( titleText != null){
                activity.runOnUiThread(
                        new Runnable() {
                            @Override
                            public void run() {
                                titleText.setText( title);
                            }
                        }
                );
            }
        }
    }
}
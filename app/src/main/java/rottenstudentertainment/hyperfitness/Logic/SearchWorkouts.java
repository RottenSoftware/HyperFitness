package rottenstudentertainment.hyperfitness.Logic;

import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.globals.Logger;
import rottenstudentertainment.hyperfitness.util.TextResourceReader;

public class SearchWorkouts {

    private static final String workoutDirectory = "workouts";
    private static String[] curWorkoutArr = null;
    private static Map< Integer, String> filenameMap;

    public static void initSearchWorkouts(OpenGLES20Activity activity){
        filenameMap = new HashMap<>();
        String[] fileList = getFileList( activity);
        curWorkoutArr = fileList;
        List<RelativeLayout> options = generateOptions( activity, fileList);
        addOptions( activity, options);
    }

    private static void addOptions( OpenGLES20Activity activity, List<RelativeLayout> options){
        LinearLayout selectList=  (LinearLayout) activity.findViewById(R.id.workout_list);
        for(int i = 0; i < options.size(); i++){
            selectList.addView( options.get(i));
        }
    }

    private static String[] getFileList( OpenGLES20Activity activity){
        return TextResourceReader.readFilesFromFolder( activity, workoutDirectory);
    }

    private static List<RelativeLayout> generateOptions( OpenGLES20Activity activity, String[] fileList){
        List<RelativeLayout> options = new ArrayList<>();
        for( int i = 0; i < fileList.length; i++){
            String filename = fileList[i];
            int id = ViewCompat.generateViewId();
            filenameMap.put(id, filename);
            String[] fileNameParts = filename.split("__");
            String text = fileNameParts[0] + " " + activity.getString( R.string.by) + " " + fileNameParts[1];
            RelativeLayout option = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.search_workouts_option, null);
            //getTextView
            option.setId( id);
            TextView textView = (TextView) option.getChildAt(0);
            textView.setText( text);
            options.add(option);
        }
        return options;
    }

    public static String getWorkoutFileName( View option, OpenGLES20Activity activity){
        String fileName = null;
        try{
            int id = option.getId();
//            TextView textView = ( TextView) ( ( (RelativeLayout) option).getChildAt(0));
//            String text = textView.getText().toString();
//            String by =  activity.getString(R.string.by);
//            String[] textArr = text.split( " "+ by +" ");
//            String titel = textArr[0];
//            String author = textArr[1]
            //fileName = getWorkouFileName( activity, titel, author);
            fileName = filenameMap.get( id);
        } catch( Exception e){
            Logger.LogError( "getWorkoutFileName", e.getMessage());
        }
        return fileName;
    }

    private static String getWorkouFileName( OpenGLES20Activity activity, String titel, String author){
        String filename = null;
        if( curWorkoutArr == null || curWorkoutArr.length == 0){
            curWorkoutArr = getFileList(activity);
        }
        for(int i = 0; i < curWorkoutArr.length; i++){
            String workoutFileName = curWorkoutArr[i];
            String[] filenameParts = workoutFileName.split("__");
            if( titel.equals( filenameParts[0]) && author.equals( filenameParts[1])){
                filename = workoutFileName;
                break;
            }
        }
        return filename;
    }
}

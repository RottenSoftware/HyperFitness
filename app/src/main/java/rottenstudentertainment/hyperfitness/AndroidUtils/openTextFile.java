package rottenstudentertainment.hyperfitness.AndroidUtils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class openTextFile {

    public static String getFileContent(String fileName,Context context){
        File file = new File( context.getFilesDir(), fileName);
        String fileContents = null;

        FileInputStream inputStream = null;

        try {
            inputStream = context.openFileInput(fileName);
            fileContents = new java.util.Scanner(inputStream).useDelimiter("\\A").next();
        } catch(Exception e){
            return null;
        }

        /*
        FileInputStream inputStream;

        StringBuilder builder = null;
        try {

            inputStream = context.openFileInput( fileName);
            fileContents = new java.util.Scanner(inputStream).useDelimiter("\\A").next();
            inputStream.close();
        } catch (Exception e) {
            Log.e( "openTextFile", "file:" + fileName + "could not be found");
            return null;
        }
        */
        return fileContents;
    }

    public static boolean setFileContent( String fileContents, String fileName, Context context){

        File file = new File( context.getFilesDir(), fileName);

        try {
/*
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.print(fileContents);
            printWriter.close();
*/
            FileOutputStream outputStream;  // binary files
            outputStream =  context.openFileOutput( fileName, Context.MODE_PRIVATE);
            outputStream.write( fileContents.getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.e( "openTextFile", "fileContent in file:" + fileName + "could not be set");
            return false;
        }
        return true;
    }

}

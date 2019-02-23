package rottenstudentertainment.hyperfitness.serialize;

import android.content.Context;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.StringWriter;

import rottenstudentertainment.hyperfitness.Fitness.Workout;
import rottenstudentertainment.hyperfitness.util.TextResourceReader;

public class XmlSerializer {

    public static Workout getWorkout( Context context, String filename){

        String xml = TextResourceReader.readTextFromAssets( context, filename);
        Workout deserialized = null;
        StringWriter writer = new StringWriter();
        try
        {
            Serializer serializer = new Persister();
            deserialized = serializer.read(Workout.class, xml);
            Workout workout = new Workout();
            try
            {
                serializer.write( deserialized, writer);
            }
            catch (Exception e)
            {
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return deserialized;





    }


}

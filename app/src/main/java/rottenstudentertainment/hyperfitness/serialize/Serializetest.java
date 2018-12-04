package rottenstudentertainment.hyperfitness.serialize;

import android.content.Context;
import android.text.TextUtils;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;

import rottenstudentertainment.hyperfitness.Fitness.Workout;
import rottenstudentertainment.hyperfitness.util.TextResourceReader;

public class Serializetest {

    public String name;
    public int counter;
    public float price;

    public Serializetest(){
        this.counter = 5;
        this.name = "arthur";
        this.price = 55.5f;
    }

    public static void doTest( Context context){
        //XmlSerializer xmlSerializer = Xml.newSerializer();

        String xml = TextResourceReader.readTextFromAssets( context, "workouts/example_workout.xml");
        Workout deserialized;
        StringWriter writer = new StringWriter();
        // Deserialize the Person
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





    }

}

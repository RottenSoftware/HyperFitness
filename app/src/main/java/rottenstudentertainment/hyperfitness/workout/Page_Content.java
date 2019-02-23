package rottenstudentertainment.hyperfitness.workout;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import rottenstudentertainment.hyperfitness.util.Accessor;
import rottenstudentertainment.hyperfitness.workout.page_objects.AndroidButton_Object;
import rottenstudentertainment.hyperfitness.workout.page_objects.Anim_Object;
import rottenstudentertainment.hyperfitness.workout.page_objects.Button_Object;
import rottenstudentertainment.hyperfitness.workout.page_objects.Fast_Anim_Object;
import rottenstudentertainment.hyperfitness.workout.page_objects.Image_Object;
import rottenstudentertainment.hyperfitness.workout.page_objects.Static_Object_Object;
import rottenstudentertainment.hyperfitness.workout.page_objects.Timer_Object;
import rottenstudentertainment.hyperfitness.new_animation.Static_Object;
import rottenstudentertainment.hyperfitness.workout.page_objects.animator_object;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Merty on 31.10.2017.
 */

public class Page_Content
{

    public  static ArrayList<Image_Object> images;
    public  static ArrayList<Button_Object> buttons;
    public  static ArrayList<Anim_Object> animations;
    public  static ArrayList<Timer_Object> timers;
    public  static ArrayList<Static_Object_Object> statics;
    public  static ArrayList<Fast_Anim_Object> fast_anims;
    public static ArrayList<animator_object> animators;
    public static ArrayList<AndroidButton_Object> androidButtons;



    public void add_image(Image_Object image)
    {
        images.add(image);
    }

    public void add_button(Button_Object button)
    {
        buttons.add(button);
    }



    public Page_Content(Context context, String file_name)
    {
        images = new ArrayList<>();
        buttons = new ArrayList<>();
        animations = new ArrayList<>();
        timers = new ArrayList<>();
        statics = new ArrayList<>();
        fast_anims = new ArrayList<>();

        animators = new ArrayList<>(); // to store keyframes and force on animations
        androidButtons = new ArrayList<>();

        page_reader(context, file_name);
    }

    private void page_reader(Context context, String file_name)
    {
        //Page_Content page_content = new Page_Content();


        String button_tag = "button";
        String image_tag = "image";
        String anim_tag = "anim";
        String static_tag = "static_object";
        String timer_tag = "timer";
        String fast_anim_tag = "fast_anim";
        String animator_tag = "animator";
        String androidButton_tag = "androidButton";

        file_name = "pages/" + file_name + ".txt";

        boolean skip_first_line = false;
        try
        {

            InputStream inputStream = context.getResources().getAssets().open(file_name);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;


            while ( (nextLine = bufferedReader.readLine()) != null )
            {


                String[] type_data = nextLine.split(" ");


                for(int i = 0; i <  type_data.length; i++)
                {
                    if(type_data[i].length() == 0) continue;
                    else if(!skip_first_line) {skip_first_line = true; continue;}   //skip first line
                    else if (type_data[i].equals(image_tag))
                    {
                        Image_Object image = new Image_Object((float) Float.parseFloat(type_data[i+1]), (float) Float.parseFloat(type_data[i+2]), (float) Float.parseFloat(type_data[i+3]), (float) Float.parseFloat(type_data[i+4]), type_data[i+5] );
                        //Log.e("image_test", "last image pos_x: " + image.get_pos_x());
                        add_image(image);
                    }
                    else if (type_data[i].equals(button_tag))
                    {
                        Button_Object button = new Button_Object((float) Float.parseFloat(type_data[i+1]), (float) Float.parseFloat(type_data[i+2]), (float) Float.parseFloat(type_data[i+3]), (float) Float.parseFloat(type_data[i+4]), type_data[i+5] );
                        add_button(button);
                        //Log.e("button data read: ", " x: " + type_data[i+1] + "y: "  + type_data[i+2] + "width: " + type_data[i+3] + "height: " + type_data[i+4]);
                    }
                    else if (type_data[i].equals(anim_tag))
                    {
                        Anim_Object anim = new Anim_Object((float) Float.parseFloat(type_data[i+1]), (float) Float.parseFloat(type_data[i+2]), (float) Float.parseFloat(type_data[i+3]), (float) Float.parseFloat(type_data[i+4]), type_data[i+5] );
                        animations.add(anim);
                        //Log.e("button data read: ", " x: " + type_data[i+1] + "y: "  + type_data[i+2] + "width: " + type_data[i+3] + "height: " + type_data[i+4]);
                    }
                    else if (type_data[i].equals(timer_tag))
                    {
                        Timer_Object timer_object = new Timer_Object((float) Float.parseFloat(type_data[i+1]), (float) Float.parseFloat(type_data[i+2]), (float) Float.parseFloat(type_data[i+3]), (float) Float.parseFloat(type_data[i+4]), type_data[i+5], (int) Float.parseFloat(type_data[i+6])  );
                        timers.add(timer_object);
                        //Log.e("button data read: ", " x: " + type_data[i+1] + "y: "  + type_data[i+2] + "width: " + type_data[i+3] + "height: " + type_data[i+4]);
                    }
                    else if (type_data[i].equals(static_tag))
                    {
                        // static objects like trees or whatever
                        Static_Object_Object static_object = new Static_Object_Object((float) Float.parseFloat(type_data[i+1]), (float) Float.parseFloat(type_data[i+2]), (float) Float.parseFloat(type_data[i+3]), (float) Float.parseFloat(type_data[i+4]), type_data[i+5] );
                        statics.add(static_object);
                    }
                    else if (type_data[i].equals(fast_anim_tag))
                    {
                        //
                        Fast_Anim_Object fast_anim_object = new Fast_Anim_Object((float) Float.parseFloat(type_data[i+1]), (float) Float.parseFloat(type_data[i+2]), (float) Float.parseFloat(type_data[i+3]), (float) Float.parseFloat(type_data[i+4]), type_data[i+5], type_data[i+6], type_data[i+7], type_data[i+8] );
                        fast_anims.add(fast_anim_object);
                    }
                    else if (type_data[i].equals(animator_tag))
                    {
                        // keyframes and bones for syncronisation
                        animator_object animator = new animator_object(type_data[i+1], type_data[i+2] );
                        animators.add(animator);
                    }
                    else if (type_data[i].equals(androidButton_tag))
                    {
                        // button in android ui
                        AndroidButton_Object androidButton = new AndroidButton_Object(type_data[i+1], type_data[i+2], type_data[i+3] );
                        androidButtons.add( androidButton);
                    }
                }
            }


        } catch (IOException e)
        {
            throw new RuntimeException("Could not open Resource: " + file_name, e);
        } catch (Resources.NotFoundException nfe)
        {
            throw new RuntimeException("Resource not found: " + file_name, nfe);
        }
        //return null;


        //Log.e("image_test", "pos_x 1: " + page_content.images.get(0).get_pos_x() + " pos_x 2: " + page_content.images.get(1).get_pos_x());

    }
//    //copy contructor
//    public Page_Content(Page_Content content)
//    {
//
//
//         ArrayList<Image_Object> images_clone =new ArrayList<>(content.images);
//         ArrayList<Button_Object> buttons_clone = new ArrayList<>(content.buttons);
//
//
//        //Log.e("Page_Content", "Contructor, content.images.size(): " + content.images.size());
//        //Log.e("Page_Content", "Contructor, content.buttons.size(): " + content.buttons.size());
//
//        for(int i = 0; i < content.images.size(); i++) images_clone.add( new Image_Object( content.images.get(i)));
//        for(int i = 0; i < content.buttons.size(); i++) buttons_clone.add( new Button_Object( content.buttons.get(i)));
//
//       // Log.e("Page_Content", "Contructor, images.size(): " + images.size());
//        //Log.e("Page_Content", "Contructor, buttons.size(): " + buttons.size());
//        this.images = images_clone;
//        this.buttons = buttons_clone;
//
//    }

    //contents of one page
    // class shall read contents from file given as signature for the constructor
//    public Page_Content()
//    {
//        images = new ArrayList<>();
//        buttons = new ArrayList<>();
//        animations = new ArrayList<>();
//        timers = new ArrayList<>();
//    }

}

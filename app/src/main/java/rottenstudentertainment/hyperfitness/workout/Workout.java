package rottenstudentertainment.hyperfitness.workout;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import rottenstudentertainment.hyperfitness.OpenGL.background_image;
import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.globals.AppState;
import rottenstudentertainment.hyperfitness.new_animation.Model;
import rottenstudentertainment.hyperfitness.util.Touch_point_parser;


/**
 * Created by Merty on 29.10.2017.
 * Adminitrates Logic and Page
 */

public class Workout
{
    private Context context;
    private Model model;
    private Touch_point_parser touchpoint;
    private Page page;
    private ArrayList<Page_Content> content;


    private background_image background;

    private List<rottenstudentertainment.hyperfitness.Fitness.Page> pages;
    private int i;

    //MediaPlayer testplayer;

    public Workout(Context context)
    {
        this.context = context;
        touchpoint = new Touch_point_parser(0f,0f);
        i = OpenGLES20Activity.resume_page;
        background = new background_image(context, "background/images/background_green_11.png");
        model = ((OpenGLES20Activity) context).workoutWrapper.model;

    //testplayer = MediaPlayer.create(context, R.raw.wayne);
    //testplayer.start();

        //model + bones laden + seiten übergeben
        pages = ((OpenGLES20Activity) context).workoutWrapper.workout.pages;

        //page_content = new Page_Content( context, file_names.get(i));

        content = new ArrayList<>();

        //content.add( new Page_Content(Page_reader.page_reader(context, "testpage")) );
        //content.add( new Page_Content(Page_reader.page_reader(context, "testpage2") ) );
        page = new Page( context, model, pages.get( i));  //anpassen für xml variante

        //Log.e("start_page", "start_page: content 0 button size  :" + content.get(0).buttons.size());
        //Log.e("start_page", "from assets  :" + TextResourceReader.readTextFromAssets(context, "stuff/more_stuff/animation_vertex_shader.glsl"));



    }

    public void draw_page(float[] m, float[] m3d)
    {
        set_page();
        background.draw_background();
        page.draw_page(m, m3d);



        //Log.e("start_page", "draw_start_page: i :" + i);
    }

    public void update_input(Touch_point_parser touch_point)
    {
        page.update_input(touch_point);
    }

    private void set_page()
    {
        if( i != AppState.get_page() )
        {
            i = AppState.get_page();
            page = new Page(context, model, pages.get( i));
        }

        /*
        else if(page.status() && i < file_names.size()-1)
        {
            page = new Page(context, new Page_Content( context, file_names.get(++i)));
            page_number.set_i(i);
        }
        */

        // set page for saved instance object
        OpenGLES20Activity.resume_page = i;
    }

}

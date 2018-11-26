package rottenstudentertainment.hyperfitness.workout;

import android.content.Context;

import java.util.ArrayList;

import rottenstudentertainment.hyperfitness.OpenGL.background_image;
import rottenstudentertainment.hyperfitness.OpenGLES20Activity;
import rottenstudentertainment.hyperfitness.test.Page_number;
import rottenstudentertainment.hyperfitness.util.Touch_point_parser;


/**
 * Created by Merty on 29.10.2017.
 * Adminitrates Logic and Page
 */

public class Workout
{
    private Context context;
    private Touch_point_parser touchpoint;
    private Page page;
    private ArrayList<Page_Content> content;


    private background_image background;

    private  ArrayList<String> file_names;
    Page_Content page_content;
    private int i;

    //MediaPlayer testplayer;

    public Workout(Context context)
    {
        this.context = context;
        touchpoint = new Touch_point_parser(0f,0f);
        i = OpenGLES20Activity.resume_page;

        background = new background_image(context, "background/images/background_green_11.png");


    //testplayer = MediaPlayer.create(context, R.raw.wayne);
    //testplayer.start();


        file_names = new ArrayList<>();
        file_names.add("testpage");
        file_names.add("testpage2");
        file_names.add("testpage3");
        file_names.add("testpage4");
        file_names.add("testpage5");
        file_names.add("testpage6");




        page_content = new Page_Content(context, file_names.get(i));

        content = new ArrayList<>();

        //content.add( new Page_Content(Page_reader.page_reader(context, "testpage")) );
        //content.add( new Page_Content(Page_reader.page_reader(context, "testpage2") ) );



        page = new Page(context, page_content);

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
        if(i != Page_number.get_page() )
        {
            i = Page_number.get_page();
            page = new Page(context, new Page_Content( context, file_names.get(i)));
        }
        else if(i == file_names.size()) System.exit(1);
        else if( i > Page_number.get_page() ){

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

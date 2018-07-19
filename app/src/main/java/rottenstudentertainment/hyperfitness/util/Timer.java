package rottenstudentertainment.hyperfitness.util;

import android.content.Context;

import rottenstudentertainment.hyperfitness.OpenGL.Disc;
import rottenstudentertainment.hyperfitness.OpenGL.Sprite;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.TextureHelper;
import rottenstudentertainment.hyperfitness.workout.page_objects.Timer_Object;

/**
 * Created by Merty on 06.11.2017.
 */


public class Timer
{
    private Context context;
    private Sprite sprite;
    private Disc disc;
    private Disc disc1;
    private Disc disc2;

    //logic
    private long start_time;
    private long current_time;
    private float sprite_x;
    private float sprite_y;

    //timer_object data
    private float height;
    private float width;
    private float pos_x;
    private float pos_y;
    private int Time;

    public Timer(Context context, Timer_Object timer_object)
    {
        this.context=context;
        //sprite = new Sprite(context, TextureHelper.loadTexture(context, Accessor.all_resource_ints(timer_object.get_name())), timer_object.get_width(), timer_object.get_height());
        sprite = new Sprite(context, TextureHelper.loadAssetTexture(context, "timer/atlas/sq_number_atlas.png"), timer_object.get_width(), timer_object.get_height());
        this.height = timer_object.get_height();
        this.width = timer_object.get_width();
        this.pos_x= timer_object.get_pos_x();
        this.pos_y = timer_object.get_pos_y();
        this.Time = timer_object.get_time();


        start_time = System.currentTimeMillis();
        current_time = System.currentTimeMillis();

        float inner_r = 0.15f;
        float outer_r = 0.2f;
        int res = 72;
        disc = new Disc(res,0f, outer_r,0.5f, 0.5f, 0.5f);
        disc1 = new Disc(res,inner_r, outer_r,0.5f, 0.0f, 0.5f);
        disc2 = new Disc(res,inner_r*1.1f ,outer_r* 0.92f,0.2f, 0.8f, 0.2f);
    }

    public void draw_timer(float[] m)
    {
        current_time = System.currentTimeMillis();
        float elapsed_time =  Time - (int) (current_time-start_time)/1000f;
        if(elapsed_time<0) elapsed_time = 0;

       // disc.draw(m, 0.15f, 0.2f, 0f);
        disc1.draw(m, 0.15f, 0.2f, 0f);
        disc2.draw(m, 0.15f, 0.2f, ((float) elapsed_time ) / (float) Time ) ;
        draw_clock(m, elapsed_time);

    }

    private void draw_clock(float[] m, float elapsed_time)
    {


       if(elapsed_time > 10f)
       {
           draw_number(m, ((int)elapsed_time) / 10, -0.12f);
           draw_number(m, ((int)elapsed_time) % 10, 0.12f);
       }
        else  draw_number(m, ((int)elapsed_time) % 10, 0f);


    }

    private void draw_number(float[] m, float rest_time, float x_offset)
    {
        //Log.e("Timer", "draw_timer, rest_time: " + rest_time);
        if( (int ) rest_time == 0 ) {sprite_x = 0f; sprite_y =0f;}
        else if( (int) rest_time == 1 ) {sprite_x = 1f; sprite_y =0f;}
        else if( (int) rest_time == 2 ) {sprite_x = 2f; sprite_y =0f;}
        else if( (int) rest_time == 3) {sprite_x = 3f; sprite_y =0f;}
        else if( (int) rest_time == 4) {sprite_x = 4f; sprite_y =0f;}
        else if( (int) rest_time == 5) {sprite_x = 0f; sprite_y =1f;}
        else if( (int) rest_time == 6 ) {sprite_x = 1f; sprite_y =1f;}
        else if( (int) rest_time == 7 ) {sprite_x = 2f; sprite_y =1f;}
        else if( (int) rest_time == 8 ) {sprite_x = 3f; sprite_y =1f;}
        else if( (int) rest_time == 9 ) {sprite_x = 4f; sprite_y =1f;}

        sprite.draw( MatrixHelper.move_rot_objects(m,1f,x_offset,0f,0f), sprite_x, sprite_y, 5f, 2f);
    }


}

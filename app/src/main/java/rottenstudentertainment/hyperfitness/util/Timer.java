package rottenstudentertainment.hyperfitness.util;

import android.content.Context;

import rottenstudentertainment.hyperfitness.OpenGL.Disc;
import rottenstudentertainment.hyperfitness.OpenGL.Sprite;
import rottenstudentertainment.hyperfitness.TextureHelper;

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
    private int startTime;
    private int curTime;

    public Timer(Context context, int startTime)
    {
        this.context=context;
        this.height = 0.15f/2.0f; //harcdcoded position/size for now
        this.width = 0.15f/2.0f;
        this.pos_x= 0.6f;
        this.pos_y = 0.4f;
        this.startTime = startTime;
        this.curTime = startTime;

        sprite = new Sprite( context, TextureHelper.loadAssetTexture( context, "timer/atlas/sq_number_atlas.png"), width, height);

        start_time = System.currentTimeMillis();
        current_time = System.currentTimeMillis();

        float inner_r = width;
        float outer_r = inner_r * 1.33f;
        int res = 72;
        disc = new Disc(res,0f, outer_r,0.5f, 0.5f, 0.5f);
        disc1 = new Disc(res,inner_r, outer_r,0.5f, 0.0f, 0.5f);
        disc2 = new Disc(res,inner_r*1.1f ,outer_r* 0.92f,0.2f, 0.8f, 0.2f);
    }

    public int getTime(){
        return curTime;
    }

    public void draw_timer( float[] m, boolean paused)
    {
        m = MatrixHelper.move_rot_objects( m,1.0f, pos_x, pos_y,0f);
        current_time = System.currentTimeMillis();
        if( paused){
            start_time = current_time - curTime;
        }
        float elapsed_time =  paused ? curTime : startTime - (int) (current_time-start_time)/1000;
        if(elapsed_time<0) elapsed_time = 0;
        curTime = (int) elapsed_time;

       // disc.draw(m, 0.15f, 0.2f, 0f);
        disc1.draw(m, 0.15f, 0.2f, 0f);
        disc2.draw(m, 0.15f, 0.2f, ((float) elapsed_time ) / (float) startTime) ;
        draw_clock( m);

    }

    private void draw_clock(float[] m)
    {


       if( curTime >= 10)
       {
           draw_number(m, curTime / 10, -width/1.25f);
           draw_number(m, curTime % 10, width/1.25f);
       }
        else  draw_number(m, curTime % 10, 0f);


    }

    private void draw_number( float[] m, int rest_time, float x_offset)
    {
        //Log.e("Timer", "draw_timer, rest_time: " + rest_time);
        if(  rest_time == 0 ) {sprite_x = 0f; sprite_y =0f;}
        else if(  rest_time == 1 ) {sprite_x = 1f; sprite_y =0f;}
        else if(  rest_time == 2 ) {sprite_x = 2f; sprite_y =0f;}
        else if(  rest_time == 3) {sprite_x = 3f; sprite_y =0f;}
        else if(  rest_time == 4) {sprite_x = 4f; sprite_y =0f;}
        else if(  rest_time == 5) {sprite_x = 0f; sprite_y =1f;}
        else if(  rest_time == 6 ) {sprite_x = 1f; sprite_y =1f;}
        else if(  rest_time == 7 ) {sprite_x = 2f; sprite_y =1f;}
        else if(  rest_time == 8 ) {sprite_x = 3f; sprite_y =1f;}
        else if(  rest_time == 9 ) {sprite_x = 4f; sprite_y =1f;}

        sprite.draw( MatrixHelper.move_rot_objects(m,1f,x_offset,0f,0f), sprite_x, sprite_y, 5f, 2f);
    }


}

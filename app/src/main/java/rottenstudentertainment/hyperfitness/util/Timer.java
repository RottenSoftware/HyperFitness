package rottenstudentertainment.hyperfitness.util;

import android.content.Context;

import rottenstudentertainment.hyperfitness.AndroidUtils.Color;
import rottenstudentertainment.hyperfitness.OpenGL.Disc;
import rottenstudentertainment.hyperfitness.OpenGL.Matrix4x4;
import rottenstudentertainment.hyperfitness.OpenGL.Sprite;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.TextureHelper;
import rottenstudentertainment.hyperfitness.globals.AppState;

/**
 * Created by Merty on 06.11.2017.
 */


public class Timer
{
    private Context context;
    private Sprite sprite;
    private Disc frontDisc;
    private Disc backDisc;
    private Color discBackColor;
    private Color discFrontColor;
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
    private int minutes;
    private final int secondsOfMinute = 60;
    private float outerInnerRatio = 1.33f;

    public Timer( Context context, int startTime)
    {
        this.context = context;
        this.height = 0.15f/2.0f; //harcdcoded position/size for now
        this.width = 0.15f/2.0f;
        this.pos_x= 0.6f;
        this.pos_y = 0.4f;
        this.startTime = startTime;
        AppState.pageTime = startTime;
        AppState.curPageTime = startTime;
        AppState.curMaxSeconds = startTime%60;
        this.curTime = startTime;
        this.minutes = startTime/60;

        sprite = new Sprite( context, TextureHelper.loadAssetTexture( context, "timer/atlas/sq_number_atlas.png"), width, height);

        start_time = System.currentTimeMillis();
        current_time = System.currentTimeMillis();

        float inner_r = width;
        float outer_r = inner_r * outerInnerRatio;
        int res = 72;
        discBackColor = getColor(  R.color.hyperBlue);
        //discBackColor = new Color(0,53,67);
        discFrontColor = getColor(  R.color.hyperGreen);
        frontDisc  = new Disc( res,inner_r, outer_r, discBackColor.getR(), discBackColor.getG(), discBackColor.getB());
        backDisc = new Disc( res,inner_r*1.1f ,outer_r* 0.92f, discFrontColor.getR(), discFrontColor.getG(), discFrontColor.getB());
    }

    private Color getColor( int resourceColor){
        String hexColor = context.getResources().getString( resourceColor);
        Color color = new Color( hexColor);
        return color;
    }



    public int getTime(){
        return curTime;
    }

    public void draw_timer( float[] m, boolean paused)
    {
        current_time = System.currentTimeMillis();
        if( paused){
            start_time = current_time - curTime;
        }
        float elapsed_time =  paused ? curTime : startTime - (int) (current_time-start_time)/1000;
        if(elapsed_time<0) elapsed_time = 0;
        curTime = (int) elapsed_time;

        m = MatrixHelper.move_rot_objects( m,1.0f, pos_x, pos_y,0f);
        if( startTime >= 60){
            //draw draw
            float shift = width * outerInnerRatio*1.15f; //width == inner radius
            float[] minuteMatrix = Matrix4x4.moveMatrix(0f, shift,0f, m);
            draw_timers( minuteMatrix, curTime/60, (float)curTime/ (float) AppState.pageTime); //minutes
            float[] secondsMatrix = Matrix4x4.moveMatrix(0f, -shift,0f, m);
            draw_timers( secondsMatrix, curTime%60, (float)(curTime%60)/ (float)secondsOfMinute); //minutes

        } else{
            draw_timers( m,curTime%60,  (float)(curTime%60)/ (float)secondsOfMinute);
        }
    }

    private void draw_timers( float[] m, int displayedTime, float progress){
        frontDisc.draw(m, 0.15f, 0.2f, 0f);
        backDisc.draw(m, 0.15f, 0.2f, progress) ;
        draw_clock( m, displayedTime);
    }

    private void draw_clock(float[] m, int displayedTime)
    {
       if( displayedTime >= 10)
       {
           draw_number(m, displayedTime / 10, -width/1.25f);
           draw_number(m, displayedTime % 10, width/1.25f);
       }
        else  draw_number(m, displayedTime % 10, 0f);
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

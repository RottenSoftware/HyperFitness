package rottenstudentertainment.hyperfitness.util;

import android.content.Context;

import rottenstudentertainment.hyperfitness.AndroidUtils.Color;
import rottenstudentertainment.hyperfitness.OpenGL.Disc;
import rottenstudentertainment.hyperfitness.OpenGL.Matrix4x4;
import rottenstudentertainment.hyperfitness.OpenGL.Sprite;
import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.TextureHelper;
import rottenstudentertainment.hyperfitness.globals.AppState;
import rottenstudentertainment.hyperfitness.globals.Logger;

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
    private float sprite_x;
    private float sprite_y;

    //timer_object data
    private float height;
    private float width;
    private float pos_x;
    private float pos_y;
    private final int secondsOfMinute = 60;
    private float outerInnerRatio = 1.33f;

    public Timer( Context context, int startTime){
        this.context = context;
        this.height = 0.15f/2.0f; //harcdcoded position/size for now
        this.width = 0.15f/2.0f;
        this.pos_x= 0.6f;
        this.pos_y = 0.4f;
        AppState.pageTime = startTime;
        AppState.curMillis = System.currentTimeMillis();
         if( AppState.curPageTime <= 0){
            AppState.curPageTime = startTime;
            AppState.startMillis = System.currentTimeMillis();
         } else{
            AppState.startMillis = AppState.curMillis - AppState.curPageTime*1000;
         }
        if(  60 > AppState.curPageTime - startTime && AppState.curPageTime/60 == startTime/60){
            AppState.curMaxSeconds = AppState.pageTime%60; //reset progress max to current time left of one minute
        }

        sprite = new Sprite( context, TextureHelper.loadAssetTexture( context, "timer/atlas/sq_number_atlas.png"), width, height);

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
        return AppState.curPageTime;
    }

    public void draw_timer( float[] m, boolean paused)
    {
        Logger.log("draw_timer", "pageTime: " + AppState.pageTime + " curTime: " + AppState.curPageTime + " curSeconds: " + AppState.curMaxSeconds);
        if( AppState.curPageTime%60 == 0) AppState.curMaxSeconds = 60; //reset progress to 1 minute
        AppState.curMillis = System.currentTimeMillis();
        float elapsed_time;
        if( paused){
            AppState.startMillis = AppState.curMillis - AppState.curPageTime;
            elapsed_time = AppState.curPageTime;
        } else{
            elapsed_time = AppState.pageTime - (int) (AppState.curMillis - AppState.startMillis)/1000;
            Logger.log("draw_timer", AppState.curMillis + " " + AppState.startMillis);
        }
        if(elapsed_time<0) elapsed_time = 0;
        AppState.curPageTime = (int) elapsed_time;

        m = MatrixHelper.move_rot_objects( m,1.0f, pos_x, pos_y,0f);
        if( AppState.pageTime >= 60){
            //draw draw
            float shift = width * outerInnerRatio*1.15f; //width == inner radius
            float[] minuteMatrix = Matrix4x4.moveMatrix(0f, shift,0f, m);
            draw_timers( minuteMatrix, AppState.curPageTime/60, (float)AppState.curPageTime/ (float) AppState.pageTime); //minutes
            float[] secondsMatrix = Matrix4x4.moveMatrix(0f, -shift,0f, m);
            draw_timers( secondsMatrix, AppState.curPageTime%60, (float)(AppState.curPageTime%60)/ (float)AppState.curMaxSeconds); //minutes

        } else{
            draw_timers( m,AppState.curPageTime%60,  (float)(AppState.curPageTime%60)/ (float)AppState.curMaxSeconds);
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

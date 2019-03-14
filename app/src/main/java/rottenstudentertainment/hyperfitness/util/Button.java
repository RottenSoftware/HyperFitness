package rottenstudentertainment.hyperfitness.util;

import android.content.Context;

import rottenstudentertainment.hyperfitness.R;
import rottenstudentertainment.hyperfitness.Texture;
import rottenstudentertainment.hyperfitness.TextureHelper;

/**
 * Created by Merty on 29.10.2017.
 */

public class Button
{
    /*
    private Texture texture;
    private Context context;
    private Touch_point_parser touch_point;
    private float height;
    private float width;
    private float pos_x;
    private float pos_y;


    public boolean activated;


    public Button(Context context, int which_button, float width, float height, float pos_x, float pos_y)  //0 = start
    {
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.height = height;
        this.width = width;
        this.context = context;
        texture = new Texture(context, TextureHelper.loadTexture(context, R.drawable.sq_start_blue),  R.drawable.start_blue, width, height);
        touch_point = new Touch_point_parser(0f,0f);
        activated = false;
    }

    public void draw_button(float[] m)
    {
       // m[7] =0.3f;
        if(activated) texture = new Texture(context, TextureHelper.loadTexture(context, R.drawable.sq_start_yellow), R.drawable.start_yellow, width, height);
        texture.draw(m);
    }

    public void update_button(Touch_point_parser touch_point)
    {
        this.touch_point = touch_point;
        activate();
    }

    private void activate()
    {
        if(touch_point.get_y() < pos_y + height/2 && touch_point.get_y() > pos_y - height/2  && touch_point.get_x() < pos_x + width/2 && touch_point.get_x() > pos_x - width/2  ) activated = true;
    }
    */
}

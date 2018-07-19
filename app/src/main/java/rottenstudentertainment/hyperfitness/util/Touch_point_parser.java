package rottenstudentertainment.hyperfitness.util;

/**
 * Created by Merty on 31.10.2017.
 */

public class Touch_point_parser
{
    private static float x;
    private static float y;

    public Touch_point_parser(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public float get_x()
    {
        return x;
    }

    public float get_y()
    {
        return y;
    }

    public void set_x(float x)
    {
        this.x = x;
    }

    public void set_y(float y)
    {
        this.y = y;
    }

}

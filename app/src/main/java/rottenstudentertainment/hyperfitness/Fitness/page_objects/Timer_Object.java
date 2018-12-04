package rottenstudentertainment.hyperfitness.Fitness.page_objects;

/**
 * Created by Merty on 04.11.2017.
 */

public class Timer_Object
{
    private float height;
    private float width;
    private float pos_x;
    private float pos_y;
    private int time;

    private String name;

    public Timer_Object(float width, float height, float pos_x, float pos_y, String name, int time)
    {
        this.height = height;
        this.width = width;
        this.name = name;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.time = time;
    }


    public float get_height()
    {
        return height;
    }

    public float get_width()
    {
        return width;
    }

    public String get_name()
    {
        return name;
    }

    public int get_time()
    {
        return time;
    }

    public float get_pos_x()
    {
        return pos_x;
    }

    public float get_pos_y()
    {
        return pos_y;
    }

}

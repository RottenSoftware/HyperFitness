package rottenstudentertainment.hyperfitness.Fitness.page_objects;

/**
 * Created by Merty on 04.11.2017.
 */

public class Anim_Object
{
    private float height;
    private float width;
    private float pos_x;
    private float pos_y;

    private String file_name;

    public Anim_Object(float width, float height, float pos_x, float pos_y, String file_name)
    {
        this.height = height;
        this.width = width;
        this.file_name = file_name;
        this.pos_x = pos_x;
        this.pos_y = pos_y;
    }


    public float get_height()
    {
        return height;
    }

    public float get_width()
    {
        return width;
    }

    public String get_file_name()
    {
        return file_name;
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

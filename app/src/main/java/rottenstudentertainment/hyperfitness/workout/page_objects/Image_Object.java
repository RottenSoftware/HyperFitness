package rottenstudentertainment.hyperfitness.workout.page_objects;

/**
 * Created by Merty on 31.10.2017.
 */

public class Image_Object
{
    private float height;
    private float width;
    private float pos_x;
    private float pos_y;

    private String name;

    public Image_Object(float width, float height, float pos_x, float pos_y, String name)
    {
        this.height = height;
        this.width = width;
        this.name = name;
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


    public float get_pos_x()
    {
        return pos_x;
    }

    public float get_pos_y()
    {
        return pos_y;
    }

    public String get_name()
    {
        return name;
    }



    public void set_height(float height)
    {
        this.height = height;
    }

    public void set_width(float width)
    {
        this.width = width;
    }


    public void set_pos_x(float pos_x)
    {
        this.pos_x = pos_x;
    }

    public void set_pos_y(float pos_y)
    {
        this.pos_y = pos_y;
    }

    public void set_name(String name)
    {
        this.name = name;
    }

    //copy contructer
    public Image_Object(Image_Object image)
    {
        this.height = image.get_height();
        this.width = image.get_width();
        this.name = image.get_name();
        this.pos_x = image.get_pos_x();
        this.pos_y = image.get_pos_y();
    }



}

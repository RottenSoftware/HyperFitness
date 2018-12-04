package rottenstudentertainment.hyperfitness.Fitness.page_objects;

/**
 * Created by Merty on 31.10.2017.
 */

public class Button_Object
{
    private float height;
    private float width;
    private float pos_x;
    private float pos_y;

    private String name;

    public Button_Object(float width, float height, float pos_x, float pos_y, String name)
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

    public String get_name()
    {
        return name;
    }

    public float get_pos_x()
    {
        return pos_x;
    }

    public float get_pos_y()
    {
        return pos_y;
    }


//copy contructer
    public Button_Object(Button_Object button)
    {
        this.height = button.get_height();
        this.width = button.get_width();
        this.name = button.get_name();
        this.pos_x = button.get_pos_x();
        this.pos_y = button.get_pos_y();
    }

}

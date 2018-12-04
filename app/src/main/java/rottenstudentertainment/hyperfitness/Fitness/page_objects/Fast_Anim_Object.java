package rottenstudentertainment.hyperfitness.Fitness.page_objects;

/**
 * Created by Merty on 04.11.2017.
 */

public class Fast_Anim_Object
{
    private float height;
    private float width;
    private float pos_x;
    private float pos_y;

    private String mesh_name;
    private String bones_name;
    private String keyframes_name;
    private String texture_name;

    public Fast_Anim_Object(float width, float height, float pos_x, float pos_y, String mesh_name, String bones_name, String keyframes_name, String texture_name)
    {
        this.height = height;
        this.width = width;
        this.mesh_name = mesh_name;
        this.pos_x = pos_x;
        this.pos_y = pos_y;

        this.bones_name = bones_name;
        this.keyframes_name = keyframes_name;
        this.texture_name = texture_name;
    }


    public float get_height()
    {
        return height;
    }

    public float get_width()
    {
        return width;
    }

    public String get_mesh_name()
    {
        return mesh_name;
    }

    public String get_bones_name()
    {
        return bones_name;
    }

    public String get_keyframes_name()
    {
        return keyframes_name;
    }

    public String get_texture_name()
    {
        return texture_name;
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

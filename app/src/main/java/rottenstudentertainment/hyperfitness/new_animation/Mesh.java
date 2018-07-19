package rottenstudentertainment.hyperfitness.new_animation;

import rottenstudentertainment.hyperfitness.util.Sort;

/**
 * Created by Merty on 07.08.2017.
 */

public class Mesh
{
    private float[] vertex_coord;
    private float[] normals_coord;
    private float[] tex_coord;

    private float[] bone_weights_data;
    private short[] bone_indices_data;  // inform how many bones influence vertex


    public Mesh(float[] vertex_coord, float[] normals_coord, float[] tex_coord,  float[] bone_weights_data, short[] bone_indices_data)
    {

        this.vertex_coord = vertex_coord;
        this.normals_coord = normals_coord;
        this.tex_coord = tex_coord;

        this.bone_weights_data = bone_weights_data;
        this.bone_indices_data = bone_indices_data;

    }

    public float[] get_vertices()
    {
        return vertex_coord;
    }

    public float[] get_normals()
    {
        return normals_coord;
    }

    public float[] get_tex()
    {
        return tex_coord;
    }

    public short[] get_skin_incdices_vec3()
    {
        return bone_indices_data;
    }

    public float[] get_skin_weights_vec3()
    {
        return bone_weights_data;
    }



}

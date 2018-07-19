package rottenstudentertainment.hyperfitness.animation;

import rottenstudentertainment.hyperfitness.util.Sort;

/**
 * Created by Merty on 07.08.2017.
 */

public class Mesh
{
    private float[] vertex_coord;
    private float[] normals_coord;
    private float[] tex_coord;
    private short[] elements;

    private float[] bone_weights_data;
    private short[] bone_indices_data;  // inform how many bones influence vertex


    public Mesh(float[] vertex_coords, float[] normal_coord, float[] texture_coord, short[] elements, float[] w_array, short[] v_c_data, short[] v_dat)
    {

//for(int i = 0; i< w_array.length; i++) if(w_array[i] > 1.0f) Log.e("wtoo much weight", "weight: " + w_array[i]);
        float[] coord = new float[elements.length];
        float[] normals = new float[elements.length];
        float[] tex = new float[elements.length/3*2];

        float[] weights_vec = new float[v_c_data.length *3]; // to store the three weights influencing for every vertex
        short[] bone_indices_vec = new short[v_c_data.length*3];

        float[] weights_vec3 = new float[elements.length]; // for opengl input expanded
        short[] bone_indices_vec3 = new short[elements.length];

        int v_data_offset = 0;
        //prepare skinning data just 3 bone influences per vertex
        for(int i = 0; i< v_c_data.length; i++)
        {
            if(v_c_data[i] == 1)
            {
                float normalize =  w_array[v_dat[i*2+1  + v_data_offset*2]];
                bone_indices_vec[i * 3] = v_dat[i * 2 + v_data_offset*2]; weights_vec[i*3] = w_array[v_dat[i*2+1 + v_data_offset*2]]/ v_dat[i * 2 + v_data_offset*2]; weights_vec[i*3] = w_array[v_dat[i*2+1 + v_data_offset*2]] / normalize;
                bone_indices_vec[i * 3 + 1] = 0; weights_vec[i*3 +1] = 0.0f;
                bone_indices_vec[i * 3 + 2] = 0; weights_vec[i*3 +2] = 0.0f;
            }
            if(v_c_data[i] == 2)
            {
                float normalize =  w_array[v_dat[i*2+1  + v_data_offset*2]] + w_array[v_dat[i*2+1  + v_data_offset*2 +2]];
                bone_indices_vec[i * 3] = v_dat[i * 2  + v_data_offset*2]; weights_vec[i*3] = w_array[v_dat[i*2+1  + v_data_offset*2]]  / normalize;
                bone_indices_vec[i * 3 + 1] = v_dat[i * 2 + v_data_offset*2 + 2]; weights_vec[i*3+1] = w_array[v_dat[i*2+1  + v_data_offset*2 +2]] / normalize;
                bone_indices_vec[i * 3 + 2] = 0; weights_vec[i*3+2] = 0.0f;
                v_data_offset++;
            }
            if(v_c_data[i] == 3)
            {
                bone_indices_vec[i * 3] = v_dat[i * 2  + v_data_offset*2]; weights_vec[i*3] = w_array[v_dat[i*2+1  + v_data_offset*2]];
                bone_indices_vec[i * 3 + 1] = v_dat[i * 2 + v_data_offset*2 + 2]; weights_vec[i*3 +1] = w_array[v_dat[i*2+1  + v_data_offset*2 +2]];
                bone_indices_vec[i * 3 + 2] = v_dat[i * 2 + v_data_offset*2 + 4]; weights_vec[i*3 +2] = w_array[v_dat[i*2+1  + v_data_offset*2 +4]];
                v_data_offset = v_data_offset + 2;
            }

            if( v_c_data[i] > 3)
            {
                float[] store_weights = new float[v_c_data[i]];
                short[] store_indices = new short[v_c_data[i]];
                for(int p = 0; p <  v_c_data[i]; p++)
                {
                    store_indices[p] = v_dat[i * 2  + v_data_offset*2 + p*2];
                    store_weights[p]= w_array[v_dat[i*2+1  + v_data_offset*2 +p*2]];
                }
                short[] order = Sort.sort_float_array(store_weights);
                float normalize_w = store_weights[order[0]] + store_weights[order[1]] + store_weights[order[2]];

                bone_indices_vec[i * 3] = store_indices[order[0]]; weights_vec[i*3] = store_weights[order[0]]/normalize_w;
                bone_indices_vec[i * 3 + 1] = store_indices[order[1]]; weights_vec[i*3 +1] =  store_weights[order[1]]/normalize_w;
                bone_indices_vec[i * 3 + 2] = store_indices[order[2]]; weights_vec[i*3 +2] =  store_weights[order[2]]/normalize_w;

                v_data_offset = v_data_offset + v_c_data[i] -1;
            }
        }


        //Log.e("check", "weight 1575:"  + w_array[1575] + "bone_weights last vec: " + weights_vec[bone_indices_vec.length-3] + " " + weights_vec[bone_indices_vec.length-2] + " " + weights_vec[bone_indices_vec.length-1]);

        for(int i = 0; i < elements.length/3; i++)
        {
            //texture
            tex[i*2] = texture_coord[elements[i*3+2]*2];
            tex[i*2+1] = -texture_coord[elements[i*3+2]*2+1];

            // vertex
            coord[i*3] = vertex_coords[elements[i*3]*3];
            coord[i*3+1] = vertex_coords[elements[i*3]*3+1];
            coord[i*3+2] = vertex_coords[elements[i*3]*3+2];

            //skinning
            weights_vec3[i*3] = weights_vec[elements[i*3]*3];
            weights_vec3[i*3+1] = weights_vec[elements[i*3]*3+1];
            weights_vec3[i*3+2] = weights_vec[elements[i*3]*3+2];

            bone_indices_vec3[i*3] = bone_indices_vec[elements[i*3]*3];
            bone_indices_vec3[i*3+1] = bone_indices_vec[elements[i*3]*3+1];
            bone_indices_vec3[i*3+2] = bone_indices_vec[elements[i*3]*3+2];

            //normals
            normals[i*3] = normal_coord[elements[i*3+1]*3];
            normals[i*3+1] = normal_coord[elements[i*3+1]*3+1];
            normals[i*3+2] = normal_coord[elements[i*3+1]*3+2];
        }

        vertex_coord = coord;
        normals_coord = normals;
        tex_coord = tex;

        bone_weights_data = weights_vec3;
        bone_indices_data = bone_indices_vec3;

        for(int i = 0; i < bone_weights_data.length/3; i++)
        {
//            if( vertex_coord[i*3 ] < 1.0f &&  vertex_coord[i*3 ] > 0.93f && vertex_coord[i*3 +1] < 0.65f && vertex_coord[i*3 +1] > 0.63f && vertex_coord[i*3+2] < 3.9f  && vertex_coord[i*3+2] > 3.7f )
//            {
//                //Log.e("vertex test", "vertex indice: " + i + "weights: " + bone_weights_data[i*3] + " " + bone_weights_data[i*3 +1] + " " + bone_weights_data[i*3 +2] + " bones: " + bone_indices_data[i*3] + " " + bone_indices_data[i*3+1] + " " + bone_indices_data[i*3+2] );
//            }
        }

        //for(int i = 0; i < bone_indices_data.length; i++) bone_indices_data[i]=0;

        this.elements = elements;
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

    public short[] get_elements()
    {
        return elements;
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

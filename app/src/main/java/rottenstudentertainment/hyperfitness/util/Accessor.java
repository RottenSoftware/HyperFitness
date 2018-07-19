package rottenstudentertainment.hyperfitness.util;

import rottenstudentertainment.hyperfitness.R;

/**
 * Created by Merty on 02.11.2017.
 */

public class Accessor
{

    public static int all_resource_ints(String file_name)
    {
        //pages
        if (file_name.equals("testpage") ) return R.raw.testpage;
        else if (file_name.equals("testpage2") ) return R.raw.testpage2;
        else if (file_name.equals("testpage3") ) return R.raw.testpage3;
        else if (file_name.equals("testpage4") ) return R.raw.testpage4;
        else if (file_name.equals("testpage3d") ) return R.raw.testpage3d;

        else if (file_name.equals("testpage6") ) return R.raw.testpage6;
        else if (file_name.equals("testpage7") ) return R.raw.testpage7;
        else if (file_name.equals("testpage8") ) return R.raw.testpage8;


        //drawables
        else if (file_name.equals("sq_start_display") ) return R.drawable.sq_start_display;
        else if (file_name.equals("sq_finish_blue") ) return R.drawable.sq_finish_blue;
        else if (file_name.equals("sq_start_blue") ) return R.drawable.sq_start_blue;
        else if (file_name.equals("sq_start_yellow") ) return R.drawable.sq_start_yellow;
        else if (file_name.equals("sq_abs_rep") ) return R.drawable.sq_abs_rep;
        else if (file_name.equals("sq_good") ) return R.drawable.sq_good;
        else if (file_name.equals("sq_situp") ) return R.drawable.sq_situp;
        else if (file_name.equals("sq_squads") ) return R.drawable.sq_squads;

      //timer
        else if (file_name.equals("sq_number_atlas") ) return R.drawable.sq_number_atlas;

        //3d animation
        else if (file_name.equals("rotten_squads") ) return R.raw.rotten_squads;
        else if (file_name.equals("rotten_walk") ) return R.raw.rotten_walk;
        else if (file_name.equals("rotten_pushups") ) return R.raw.rotten_pushups;
        else if (file_name.equals("rotten_crunch") ) return R.raw.rotten_crunch;
        else if (file_name.equals("rotten_flex") ) return R.raw.rotten_flex;

        //movable objects
        else if (file_name.equals("hand_to_origin") ) return R.raw.hand_to_origin;
        else if (file_name.equals("geometry_origin") ) return R.raw.geometry_origin;
        else if (file_name.equals("cursor_to_origin") ) return R.raw.cursor_to_origin;

        //textures for models
        else if (file_name.equals("model_texture") ) return R.drawable.model_texture;
        else if (file_name.equals("deadlift_weights_texture") ) return R.drawable.deadlift_weights_texture;


        //not from collada file
        else if (file_name.equals("rotten_mesh") ) return R.raw.rotten_mesh;
        else if (file_name.equals("rotten_bones") ) return R.raw.rotten_bones;
        else if (file_name.equals("rotten_situp") ) return R.raw.rotten_situp;

        //binary files
        else if (file_name.equals("mesh_b") ) return R.raw.mesh_b;
        else if (file_name.equals("bones_b") ) return R.raw.bones_b;
        else if (file_name.equals("keyframes_b") ) return R.raw.keyframes_b;

        //barbell mesh
        else if (file_name.equals("rotten_weights") ) return R.raw.rotten_weights;

        //keyframes
        else if (file_name.equals("rotten_deadlift_keyframes") ) return R.raw.rotten_deadlift_keyframes;
        else if (file_name.equals("debug_motions") ) return R.raw.debug_motions;



        return 0;
    }
}

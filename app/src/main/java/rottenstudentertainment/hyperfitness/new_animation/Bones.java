package rottenstudentertainment.hyperfitness.new_animation;

import android.opengl.Matrix;

import java.util.Vector;

import static android.opengl.Matrix.setIdentityM;

/**
 * Created by Merty on 10.08.2017.
 */

public class Bones
{

    private  float[] invBindMats;
    private  Vector <Vector<Integer>> bones_structure;

    public Bones( float[] invBindMats, Vector<Vector<Integer>> bone_structure )
    {

        this.bones_structure= bone_structure;
        this.invBindMats = invBindMats;

    }


    public float[] get_invBindMats()
    {
        return invBindMats;
    }


    public Vector<Vector<Integer>> get_structure()
    {
        return bones_structure;
    }
}

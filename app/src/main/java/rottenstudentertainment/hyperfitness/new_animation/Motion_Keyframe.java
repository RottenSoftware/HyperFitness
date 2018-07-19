package rottenstudentertainment.hyperfitness.new_animation;

import android.opengl.Matrix;

import rottenstudentertainment.hyperfitness.util.MatrixHelper;

import java.util.ArrayList;
import java.util.Vector;

import static android.opengl.Matrix.setIdentityM;

/**
 * Created by Merty on 11.08.2017.
 */

public class Motion_Keyframe
{
    private float[] timestamp;
    private float[][] local_keyframes_2d;

    public Motion_Keyframe(float[] timestamp, float[][] local_keyframes_2d)
    {
        this.timestamp = timestamp;
        this.local_keyframes_2d = local_keyframes_2d;
    }

    public  float[] get_time_stamps()
    {
        return timestamp;
    }


    public  float[][] getLocal_keyframes_2d()  {  return local_keyframes_2d;}
}

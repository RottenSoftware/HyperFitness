package rottenstudentertainment.hyperfitness.animation;

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
    private ArrayList <float[]> poses;
    private float[][] local_keyframes_2d;

    public Motion_Keyframe(float[] timestamp, ArrayList <float[]> pre_pose, Vector <Vector<Integer>> bones_structure)
    {

        poses= new ArrayList<>();
        this.timestamp = timestamp;
//Log.e("prepose.size", ": "+ pre_pose.size());
        float[][] array_2d = new float[pre_pose.get(0).length/16][pre_pose.size()*16]; // stimmt

        for(int i= 0; i <  pre_pose.size(); i++)
        {
            for(int j= 0; j < pre_pose.get(0).length/16; j++)
            {
                for(int p= 0; p < 16; p++) array_2d[j][i*16 +p]  = pre_pose.get(i)[j *16 + p];
            }
            //Log.e("loop poses", "number of elements: " + poses.size());
        }

        //MatrixHelper.matrix_printer(array_2d[0]);
        //local_keyframes_2d = array_2d; // for motion fusion

        //alternative
        local_keyframes_2d = new float[array_2d.length][array_2d[0].length];
        for(int i = 0; i < array_2d.length; i++)
        {
            for(int j = 0; j < array_2d[0].length; j++) local_keyframes_2d[i][j]= array_2d[i][j];
        }
       // MatrixHelper.matrix_printer(local_keyframes_2d[0]);

// implements bone structure
        for(int g = 0; g < timestamp.length; g++ )
        {

            float[] buffer_matrix = new float[pre_pose.size() * 16];  // for whole array
            //local to global
            for (int p = 0; p < bones_structure.size(); p++)
            {
                float[] total_matrix = new float[16];  //for one matrix entry
                float[] buffer1_matrix = new float[16];
                setIdentityM(buffer1_matrix, 0);
                float[] buffer2_matrix = new float[16];
                setIdentityM(buffer2_matrix, 0);


                //vertex wird erst mit buffer2 multipliziert, dann mit buffer1
                for (int j = 0; j < bones_structure.get(p).size(); j++)
                    //for(int j =  (bones_structure.get(p).size() -1); j > -1; j--)
                {
                    //Log.e("array größe", " :"+ array_2d[g].length);
                    for (int i = 0; i < 16; i++)  buffer1_matrix[i] = array_2d[g][bones_structure.get(p).get(j) * 16 + i];  // fill in new inv bone transform
                    Matrix.multiplyMM(total_matrix, 0, buffer1_matrix, 0, buffer2_matrix, 0);
                    System.arraycopy(total_matrix, 0, buffer2_matrix, 0, total_matrix.length);
                }
                for (int j = 0; j < 16; j++)
                {
                    buffer_matrix[p * 16 + j] = total_matrix[j];
                }
            }
            for (int p = 0; p < buffer_matrix.length; p++) array_2d[g][p] = buffer_matrix[p];
        }
// bone structure implemented





        for(int i= 0; i <  array_2d.length; i++)
        {
            poses.add(array_2d[i]);
        }

/*        for(int p = 0; p < poses.size(); p++ )
        {
            for(int i = 0; i < poses.size(); i++ )
            if(poses.get(p)[i] < 0.01)  poses.get(p)[i] = 0.0f;
        }*/

       // Log.e("direct poses", "number of elements: " + poses.size());
       //for (int i = 16; i< 32; i++) Log.e("direct poses", "element zero size: " + poses.get(0)[i]);
        MatrixHelper.matrix_printer(poses.get(0));


    }

    public  float[] get_time_stamps()
    {
        return timestamp;
    }

    public  ArrayList<float[]> get_poses()
    {
        return poses;
    }

    public  float[][] getLocal_keyframes_2d()  {  return local_keyframes_2d;}
}

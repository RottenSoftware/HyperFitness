package rottenstudentertainment.hyperfitness.new_animation;


import android.opengl.Matrix;
import android.util.Log;

import rottenstudentertainment.hyperfitness.util.MatrixHelper;

import java.util.Vector;

import static android.opengl.Matrix.setIdentityM;
import static android.os.SystemClock.elapsedRealtime;

/**
 * Created by Merty on 27.08.2017.
 */

public class Animator
{
    private float[] timestamps;
    private float[][] local_keyframes_2d;
    private float start_time;
    private float curTime;
    private float[] global_fused_keyframes;
    private float[] local_fused_keyframes;
    private Vector <Vector<Integer>> bones_structure;

    public Animator(float[] timestamps, float[][] local_keyframess_2d, Vector <Vector<Integer>> bones_structure)
    {
        //MatrixHelper.matrix_printer(local_keyframess_2d[0]);
        this.timestamps = timestamps;
        for(int i = 0; i < this.timestamps.length; i++) this.timestamps[i] = this.timestamps[i] - this.timestamps[0];
        this.local_keyframes_2d =local_keyframess_2d;

        //Log.e("", "" + local_keyframes_2d.length);
        start_time = elapsedRealtime();

        //preserve room for global matrices
        global_fused_keyframes = new float[local_keyframes_2d[0].length];
        local_fused_keyframes = new float[local_keyframes_2d[0].length];

        this.bones_structure = bones_structure;
        calcFusedGlobals( 0f,0);  //set first Pose
        //erste keyframe zu global keyframe

    }

    public  float[] fuse_locals( float progress, float[] m_one, float[] m_two)
    {
        float new_x;
        float new_y;
        float new_z;
        //Location mixing
       new_x = (m_two[3] - m_one[3] ) * progress + m_one[3];
       new_y = (m_two[7] - m_one[7] ) * progress + m_one[7];
       new_z = (m_two[11] - m_one[11] ) * progress + m_one[11];

    float[] new_matrix = Quaternion.interpolate_matrices(m_one, m_two, progress);

        new_matrix[3] = new_x;
        new_matrix[7] = new_y;
        new_matrix[11] = new_z;

        //Log.e("x koordinate test", "x: " + new_x);
        return new_matrix;
    }

    public float[] update_animation( boolean paused) {
        float motion_speed = 1000.0f;
        if( paused){ // warten vorm workout/ mögliche pause
            start_time = elapsedRealtime() -curTime;
            return global_fused_keyframes;
        }
        curTime = ( elapsedRealtime() - start_time);
        while( curTime > timestamps[ timestamps.length-1]*motion_speed){
            curTime -= timestamps[ timestamps.length-1]*motion_speed;
            start_time = elapsedRealtime();
        }
        //find the two keyframes that have to be merged and calculate the time progress between them
        //Log.e("time testing", "time :" + time);
       // for (int i = 0; i < timestamps.length; i++) Log.e("time testing", "timestamp " + i + " : " + timestamps[i]*motion_speed);
        int first_frame = 0;
        float progress = 0.0f;
        for(int p = 0; p < timestamps.length; p++)
        {
            //Log.e("time testing", "timestamp " + p + " : " + timestamps[p]*motion_speed);
            if( curTime < timestamps[p]* motion_speed)
            {
                //Log.e("inside if statement", "check");
                first_frame = p-1;
                progress = (curTime - timestamps[p-1] * motion_speed ) / ((timestamps[p] - timestamps[p-1]) * motion_speed);
                break;
            }
        }
        //Log.e("progress testing", "progress :" + progress + "first_frame " + first_frame );
        //---------
        calcFusedGlobals( progress, first_frame);
        MatrixHelper.matrix_printer(global_fused_keyframes);
        return global_fused_keyframes;
    }

    private void calcFusedGlobals( float progress, int first_frame){
        for( int i = 0; i < local_fused_keyframes.length/16; i++){
            insert_matrix( fuse_locals(progress, extract_matrix(local_keyframes_2d[first_frame], i), extract_matrix(local_keyframes_2d[first_frame +1], i)) ,i);
        }
        local_to_global();
    }

    public void local_to_global()
    {
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
            {
                for (int i = 0; i < 16; i++)
                {
                    //Log.e("new_animator", "local_fused_kexframes length: " + local_fused_keyframes.length/16);
                    //Log.e("new_animator", "number of bones: " + bones_structure.size() + " p: " + p + " j: " + j + " i: " + i + " calculated index: " + (bones_structure.get(p).get(j) * 16 + i) +  " bones_structure.get(p).get(j): " + bones_structure.get(p).get(j));
                    buffer1_matrix[i] = local_fused_keyframes[bones_structure.get(p).get(j) * 16 + i];  // fill in new inv bone transform
                }
                Matrix.multiplyMM(total_matrix, 0, buffer1_matrix, 0, buffer2_matrix, 0);
                System.arraycopy(total_matrix, 0, buffer2_matrix, 0, total_matrix.length);
            }
            for (int j = 0; j < 16; j++)
            {
                global_fused_keyframes[p * 16 + j] = total_matrix[j];
            }
        }



    }

    public float[] extract_matrix(float[] matrix_row, int matrix_number)
    {
        float[] matrix = new float[16];
        for(int i = 0; i < 16; i++)
        {
            matrix[i]= matrix_row[16*matrix_number + i];
        }
        return matrix;
    }

    public  void insert_matrix(float[] matrix, int matrix_number)
    {
        for(int i = 0; i < 16; i++)
        {
            local_fused_keyframes[16*matrix_number + i] = matrix[i];
        }
    }

    public  float[] get_global_keyframes()
    {
        return global_fused_keyframes;
    }

}

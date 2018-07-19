package rottenstudentertainment.hyperfitness.animation;

import android.opengl.Matrix;

import java.util.Vector;

import static android.opengl.Matrix.setIdentityM;

/**
 * Created by Merty on 10.08.2017.
 */

public class Bones
{

    private  String[] names_id;
    private  float[] invBindMats;
    private  float[] BindMats;
    private  Vector <Vector<Integer>> bones_structure;

    public Bones(String[] names_id, float[] localinvBindMats, Vector<Vector<Integer>> bone_structure, float[] bind_matrices )
    {
        this.names_id = names_id;
        float[] buffer = new float[bind_matrices.length];
        for(int i = 0; i < bind_matrices.length/16; i++)
        {
            float[] matrix = new float[16];
            for(int p = 0; p < 16; p++ )
            {
                matrix[p]= bind_matrices[i*16+p];
            }
            float[] im = new float[16];
            Matrix.invertM(im, 0, matrix,0);
            for(int p = 0; p < 16; p++ )
            {
                buffer[i*16 + p]= im[p];
            }
        }
        BindMats =buffer;


        float[] buffer_matrix = new float[BindMats.length];  // for whole array
        //local to global
        for(int p = 0; p < bone_structure.size(); p++ )
        {
            float[] total_matrix = new float[16];  //for one matrix entry
            float[] buffer1_matrix = new float[16];
            setIdentityM(buffer1_matrix, 0);
            float[] buffer2_matrix = new float[16];
            setIdentityM(buffer2_matrix, 0);


            //vertex wird erst mit buffer2 multipliziert, dann mit buffer1
            //for (int j = 0; j < bone_structure.get(p).size(); j++)
            for(int j =  (bone_structure.get(p).size() -1); j > -1; j--)
            {
                for(int i= 0; i < 16; i++) buffer1_matrix[i] = BindMats[bone_structure.get(p).get(j)* 16 + i];  // fill in new inv bone transform
                Matrix.multiplyMM(total_matrix, 0, buffer1_matrix, 0, buffer2_matrix, 0);
                System.arraycopy(total_matrix, 0, buffer2_matrix, 0, total_matrix.length);
            }
            for(int j =  0; j < 16; j++)
            {
                buffer_matrix[p*16 +j] = total_matrix[j];
            }
        }
        //invBindMats = localinvBindMats;
        invBindMats = buffer_matrix;
        bones_structure = bone_structure;
    }

    public String[] get_name_ids()
    {
        return names_id;
    }

    public float[] get_invBindMats()
    {
        return invBindMats;
    }

    public float[] get_BindMats()
    {
        return BindMats;
    }

    public Vector<Vector<Integer>> get_structure()
    {
        return bones_structure;
    }
}

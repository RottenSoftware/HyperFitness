package rottenstudentertainment.hyperfitness.util;

import android.opengl.Matrix;
import android.util.Log;

import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;

/**
 * Created by Merty on 02.01.2017.
 */

public class MatrixHelper
{
    public static void perspectiveM(float[] m, float yFovInDegrees, float aspect, float n, float f)
    {
        final float angleRadians = (float) (yFovInDegrees * Math.PI / 180.0);
        final float a = (float) (1.0/ Math.tan(angleRadians / 2.0));

        m[0] = a/ aspect;
        m[1] = 0f;
        m[2] = 0f;
        m[3] = 0f;

        m[4] = 0f;
        m[5] = a;
        m[6] = 0f;
        m[7] = 0f;

        m[8] = 0f;
        m[9] = 0f;
        m[10] = -((f+n)/(f-n));
        m[11] = -1f;

        m[12] = 0f;
        m[13] = 0f;
        m[14] = -((2f*f*n)/(f-n));
        m[15] = 0f;
    }


    public static boolean matrix_printer(float[] matrix)
    {
        //if(matrix.length != 16) return false;

        //for(int i = 0; i < 4; i++) Log.e("matrix","matrix line: " + i + "elements: " + matrix[i*4] + " " + matrix[i*4+1] + " " + matrix[i*4+2] + " " + matrix[i*4 + 3]);

        return true;
    }

    public static float[] move_rot_objects(float[] ortho_M, float size, float d_x, float d_y, float d_z)
    {
        float[] return_matrix = new float[16];
        copy_m(ortho_M, return_matrix);

        return_matrix[13] += d_y;
        return_matrix[12] += d_x;

        return return_matrix;
    }

    public static float[] copy_matrix(float[] in_matrix)
    {
        float[] copy = new float[16];
        for(int i = 0; i<16; i++) copy[i] = in_matrix[i];
        return copy;
    }

    public static void copy_m(float[] in_matrix, float[] copy)
    {
        for(int i = 0; i<16; i++) copy[i] = in_matrix[i];
    }


    public static float[] move_rotate_scale_matrix(float[] in_matrix, float size, float d_x, float d_y, float d_z, float rot_x, float rot_y, float rot_z)
    {
        float[] copy = copy_matrix(in_matrix);



        //move
        float[] model_Matrix = new float[16];
        setIdentityM(model_Matrix, 0);
        translateM(model_Matrix,0 ,d_x, d_y, d_z);

        Matrix.multiplyMM(copy, 0, in_matrix, 0, model_Matrix, 0);

        in_matrix = copy_matrix(copy);



        //scale
        float[] scalem = new float[16];
        setIdentityM(scalem,0);
        scalem[0]= size;
        scalem[5]= size;
        scalem[10]= size;

        Matrix.multiplyMM(copy, 0, in_matrix, 0, scalem, 0);

        in_matrix = copy_matrix(copy);

        //rotation

        float[] mRotationMatrix_x = new float[16];
        float[] mRotationMatrix_y = new float[16];
        float[] mRotationMatrix_z = new float[16];
        Matrix.setRotateM(mRotationMatrix_x, 0, rot_x, 1.0f, 0, 0);
        Matrix.setRotateM(mRotationMatrix_y, 0, rot_y, 0, 1.0f, 0);
        Matrix.setRotateM(mRotationMatrix_z, 0, rot_z, 0, 0, 1.0f);

        float[] rot_xy = new float[16];
        float[] rot_xyz = new float[16];

        Matrix.multiplyMM(rot_xy, 0, mRotationMatrix_y, 0, mRotationMatrix_x, 0);
        Matrix.multiplyMM(rot_xyz, 0, mRotationMatrix_z, 0, rot_xy, 0);
        // eigene funktion?^^

        Matrix.multiplyMM(copy, 0, in_matrix, 0, rot_xyz, 0);
        in_matrix = copy_matrix(copy);





        return in_matrix;

    }



}

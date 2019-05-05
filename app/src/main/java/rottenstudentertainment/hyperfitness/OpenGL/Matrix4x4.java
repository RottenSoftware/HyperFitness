package rottenstudentertainment.hyperfitness.OpenGL;

import android.opengl.Matrix;

public class Matrix4x4 {
    private float[] matrix = new float[16];

    public Matrix4x4(){
    }

    public Matrix4x4( float[] matrix){
        this.matrix = matrix;
    }

    public Matrix4x4( float scale){
        matrix[0] = scale;
        matrix[5] = scale;
        matrix[10] = scale;
        matrix[15] = 1.0f;
    }

    public float[] getMatrix(){
        return matrix;
    }

    public void setMatrix( float[] matrix){
        if( matrix.length != 16) throw new RuntimeException("Matrix4x4: setMatrix(): not a 4x4 matrix! length:" + matrix.length);
        this.matrix = matrix;
    }

    public void scaleMatrix( float scale){
        Matrix4x4 scaleMatrix = new Matrix4x4( scale);
        //matrix multiply
        float[] bufferMatrix = new float[16];
        Matrix.multiplyMM( bufferMatrix, 0, matrix, 0, scaleMatrix.getMatrix(), 0);
        matrix = bufferMatrix;
    }

    public static float[] moveMatrix( float x, float y, float z, float[] m){
        Matrix4x4 matrix = new Matrix4x4( m);
        Matrix4x4 movedMatrix = moveMatrix(x,y,z, matrix);
        return movedMatrix.getMatrix();
    }

    public static Matrix4x4 moveMatrix( float x, float y, float z, Matrix4x4 matrix){
        Matrix4x4 copy = copyMatrix( matrix.getMatrix());
        copy.getMatrix()[12] += x;
        copy.getMatrix()[13] += y;
        copy.getMatrix()[14] += z;
        return copy;
    }

    private static Matrix4x4 copyMatrix( float[] in_matrix)
    {
        float[] copy = new float[16];
        for(int i = 0; i<16; i++) copy[i] = in_matrix[i];
        return new Matrix4x4(copy);
    }
}

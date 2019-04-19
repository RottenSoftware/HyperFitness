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
}
